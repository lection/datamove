/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.honda;

import com.linkin.crm.customer.model.Contact;
import com.linkin.crm.customer.model.Customer;
import com.linkin.crm.sales.model.ContactRecord;
import com.linkin.crm.sales.model.Leads;
import com.linkin.crm.um.model.InternalOrgImpl;
import com.linkin.crm.um.model.UserImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import util.DBUtil;
import util.OrgUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class FollowTask extends J2JTaskSupport{
    private ContactRecord recordBak;//用于记录是否有写重复的战败、成交跟进的错误数据出现
    private ContactRecord recordBak2;//用于记录上一次出现的跟进，当跟进
//    private OrgUtil orgUtil;
    private InternalOrgImpl intOrg;

    private Map<String,Leads> leadsMap = new HashMap<String,Leads>();
    private Map<String,String> levelMap = new HashMap<String,String>(16);
    private Map<Long,Customer> customerMap = new HashMap<Long,Customer>(200000);
    private Map<Long,Contact> contactMap = new HashMap<Long,Contact>(200000);
    private Map<String,UserImpl> userMap = new HashMap<String,UserImpl>(2000,0.5f);
    private Map<String,String> typeMap = new HashMap<String,String>(16);
    private Map<String,String> contactChannelMap = new HashMap<String,String>(16);
    private Map<String,String> testDriverMap = new HashMap<String, String>(8);

    private int sequence = 1;
    private String id_backup = null;
    private Date contactDate;//记录上次接触日期
    private Date backupDate = null;

    {
        typeMap.put("N", "8103");
        typeMap.put("I", "8104");
        typeMap.put("C", "8105");
        typeMap.put("R", "8106");
        typeMap.put("OT", "8108");

        testDriverMap.put("P", "8066");
        testDriverMap.put("C", "8067");
        testDriverMap.put("B", "8068");
        testDriverMap.put("", "8069");
        testDriverMap.put(null, "8069");

        contactChannelMap.put("P", "8059");
        contactChannelMap.put("S", "8061");
        contactChannelMap.put("E", "8058");
        contactChannelMap.put("V", "8062");
        contactChannelMap.put("M", "8064");
        contactChannelMap.put("A", "8065");
        contactChannelMap.put("OT", "8065");
        contactChannelMap.put("", "8065");
        contactChannelMap.put(null, "8065");
        
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        setSource_id(rs.getLong("id"));//记录日志
        ContactRecord record = new ContactRecord();
        String lead_id = rs.getString("yellowcard_id");
        record.setLeads(leadsMap.get(lead_id));
        if(!lead_id.equals(id_backup)){
            sequence = 1;
            final Date date = contactDate;
            if(date != null)
            DBUtil.executeUpdate(getTargetConn(), "update t_leads set c_ext_dt2=? where c_id=?", new DBUtil.Excute() {
                public void execute(PreparedStatement pre) throws SQLException {
                    pre.setDate(1, new java.sql.Date(date.getTime()));
                    pre.setLong(2, leadsMap.get(id_backup).getId());
                }
            });
            if(recordBak2!=null&&!recordBak2.getNextDate().after(contactDate)&&"SUC".equals(recordBak2.getResult())){
                DBUtil.executeUpdate(getTargetConn(), "update t_leads set c_next_fldate=? where c_id=?", new DBUtil.Excute() {
                public void execute(PreparedStatement pre) throws SQLException {
                    pre.setDate(1, java.sql.Date.valueOf("3000-12-31"));
                    pre.setLong(2, leadsMap.get(id_backup).getId());
                    }
                });
                DBUtil.executeUpdate(getTargetConn(), "update t_contactrecord set c_next_date=? where c_id=?", new DBUtil.Excute() {
                public void execute(PreparedStatement pre) throws SQLException {
                    pre.setDate(1, java.sql.Date.valueOf("3000-12-31"));
                    pre.setLong(2, recordBak2.getId());
                    }
                });
            }
            backupDate = null;
            recordBak = null;
        }
        record.setSequence(sequence++);
        id_backup = lead_id;//备份上次的lead id
        record.setCurlevel(levelMap.get(rs.getString("ycclass")));//跟进级别
        record.setContact(contactMap.get(record.getLeads().getId()));//跟进的联系人 contact
        record.setCustomer(customerMap.get(record.getLeads().getId()));//跟进的 客户
        //跟进人
        String flUserName = rs.getString("flsales") + rs.getString("org_id");
        record.setUser(userMap.get(flUserName));
        //跟进日期 下次跟进日期
        record.setContactDate(rs.getDate("fldate"));
        contactDate = record.getContactDate();//记录本次跟进的接触时间
        record.setNextDate(rs.getDate("nxfldate"));
        //创建跟进日期
        record.setCreatedDate(rs.getDate("create_date"));
        record.setCreatedBy(rs.getString("created_by"));
        record.setFlType(typeMap.get(rs.getString("type")));//跟进性质
        if(rs.getString("trial").equals("T")){
            record.setFlContent("129");//跟进内容-试乘试驾
            record.setFeedBack("118");
            record.setTestDrive(1);//试乘试驾
        }else{
            record.setFlContent("132");//跟进内容 其他
            record.setTestDrive(0);
        }
        record.setContactChannel(contactChannelMap.get(rs.getString("contact")));
        if(backupDate==null||record.getContactDate().before(backupDate)||record.getContactDate().equals(backupDate)){
            record.setP_ext_long1("close");
        }else{
            record.setP_ext_long1("expired");
        }
        record.setMemo(rs.getString("remark"));//备注
        backupDate = record.getNextDate();
        getResult(rs.getString("oc"), rs.getString("ordr"),record,lead_id);//状态变更
//        System.out.println(record.getLeads().getId() + "\t" + record.getCustomer().getId() + "\t" + record.getContact().getId() + "\t" + record.getUser().getId());
        recordBak2 = record;
        return record;
    }

    @Override
    public void init() throws Exception {
        super.init();
        DBUtil.executeQuery(getTargetConn(), "select c_code,c_id from t_biz_vo_category where c_org_id="
                 +intOrg.getId() + " and c_type='ycclass'"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next())
                    levelMap.put(rs.getString("c_code"),rs.getInt("c_id")+"");
//                levelMap.put("B", levelMap.get("A"));
//                levelMap.put("C", levelMap.get("A"));
            return null;}});

        DBUtil.executeQuery(getTargetConn(), "select p.c_ext_str2,p.c_id from t_person as p"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                UserImpl user = null;
                while(rs.next()){
                    user = new UserImpl();
                    user.setId(rs.getLong("c_id"));
                    userMap.put(rs.getString("c_ext_str2"), user);
                }
            return null;}});

        DBUtil.executeQuery(getTargetConn(), "select l.c_ext_str1,l.c_id,l.c_cust_id,l.c_contact_id from t_leads as l"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                Leads lead = null;
                Customer customer = null;
                Contact contact = null;
                while(rs.next()){
                    lead = new Leads();
                    customer = new Customer();
                    contact = new Contact();
                    lead.setId(rs.getLong("c_id"));
                    leadsMap.put(rs.getString("c_ext_str1"), lead);
                    customer.setId(rs.getLong("c_cust_id"));
                    contact.setId(rs.getLong("c_contact_id"));
                    customerMap.put(lead.getId(), customer);
                    contactMap.put(lead.getId(), contact);
                }
            return null;}});
    }

    private void getResult(String s1,String s2,final ContactRecord cr,String ycid) throws SQLException{
        String result = "FL";
        if("s".equals(s1)){
            cr.setP_ext_str1("s" + ycid);
            result = "SUC";
            if(recordBak != null){
                cr.setSequence(recordBak.getSequence());
                DBUtil.delete(getTargetConn(),"t_contactrecord",recordBak.getId());
            }
            cr.setP_ext_dt1(cr.getContactDate());//成交日期
            recordBak = cr;
            DBUtil.executeUpdate(getTargetConn(), "update t_leads set c_ext_dt3=? where c_id=?", new DBUtil.Excute() {
                public void execute(PreparedStatement pre) throws SQLException {
                    pre.setDate(1, new java.sql.Date(cr.getContactDate().getTime()));
                    pre.setLong(2, cr.getLeads().getId());
                }
            });
        }else if("f".equals(s1)){
            cr.setP_ext_str1("f" + ycid);
            result = "FAIL";
            if(recordBak != null){
                cr.setSequence(recordBak.getSequence());
                DBUtil.delete(getTargetConn(),"t_contactrecord",recordBak.getId());
            }
            cr.setNextDate(java.sql.Date.valueOf("3000-12-31"));
            recordBak = cr;
            DBUtil.executeUpdate(getTargetConn(), "update t_leads set c_ext_dt3=? where c_id=?", new DBUtil.Excute() {
                public void execute(PreparedStatement pre) throws SQLException {
                    pre.setDate(1, new java.sql.Date(cr.getContactDate().getTime()));
                    pre.setLong(2, cr.getLeads().getId());
                }
            });
        }else if(recordBak!=null){
            result = recordBak.getResult();
        }else if("T".equals(s2)){
            result = "ORDER";
        }
        cr.setResult(result);
    }

    public void setIntOrg(InternalOrgImpl intOrg) {
        this.intOrg = intOrg;
    }

//    public void setOrgUtil(OrgUtil orgUtil) {
//        this.orgUtil = orgUtil;
//    }
}
