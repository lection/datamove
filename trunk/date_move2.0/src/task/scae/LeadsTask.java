/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.core.model.Address;
import com.linkin.crm.customer.model.Contact;
import com.linkin.crm.customer.model.Customer;
import com.linkin.crm.customer.model.IndividuleCustomer;
import com.linkin.crm.product.model.Product;
import com.linkin.crm.sales.model.ContactRecord;
import com.linkin.crm.sales.model.Leads;
import com.linkin.crm.um.model.UserImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import saver.ContactSaver;
import saver.CustomerSaver;
import saver.PersonImplSaver;
import util.DBUtil;
import util.OrgUtil;
import util.ProductCategoryUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class LeadsTask extends J2JTaskSupport{

    private ProductCategoryUtil proUtil;
    private OrgUtil orgUtil;
    private CustomerSaver customerSaver;
    private ContactSaver contactSaver;
    private PersonImplSaver personSaver;

    private Map<Long, UserImpl> salesMap = new HashMap<Long, UserImpl>(700);
    private Map<String, UserImpl> salesMap2 = new HashMap<String, UserImpl>(700);
    private Map<Integer,Integer> cityMap = new HashMap<Integer,Integer>(30);
    private Map<String,Long> customerMap = new HashMap<String,Long>();
    private Map<String,Integer> levelMap = new HashMap<String,Integer>(16);
    private Map<String,Integer> flMap = new HashMap<String,Integer>(16);
    private Map<String,Integer> colorMap = new HashMap<String,Integer>(16);
    private Map<Long,Product> productMap = new HashMap<Long,Product>(100);
    private Map<String,Product> productNameMap = new HashMap<String,Product>(100);
    private Map<String,String> anthorBizMap = new HashMap<String,String>(16);
    private Map<String,String> anthor2BizMap = new HashMap<String,String>(8);
    private Map<String,String> anthor3BizMap = new HashMap<String,String>(8);

    {
        anthorBizMap.put("P", "46");
        anthorBizMap.put("C", "47");
        anthorBizMap.put("G", "48");
        anthorBizMap.put("O", "49");

        anthorBizMap.put("F", "50");
        anthorBizMap.put("A", "51");
        anthorBizMap.put("U", "52");

        anthor2BizMap.put("AH", "53");
        anthor2BizMap.put("H", "54");
        anthor2BizMap.put("HC", "55");
        anthor2BizMap.put("O", "56");

        anthor3BizMap.put("H", "H");
        anthor3BizMap.put("P", "I");
        anthor3BizMap.put("O", "C");
    }

    @Override
    public void init() throws Exception {
        super.init();
        personSaver.setTargetConn(getTargetConn());personSaver.init();
        customerSaver.setTargetConn(getTargetConn());customerSaver.init();
        contactSaver.setTargetConn(getTargetConn());contactSaver.init();
        DBUtil.executeQuery(getTargetConn(), 
        "select p.c_id,p.c_ext_str1,p.c_ext_str2,u.c_org_id from t_person p,t_um_user u where p.c_ext_str1 like 'scae%' and p.c_id=u.c_person_id"
        , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                UserImpl user = null;
                String id = null;
                while(rs.next()){
                    user = new UserImpl();
                    id = rs.getString("c_ext_str1");
                    user.setId(rs.getLong("c_id"));
                    user.setIntOrg(orgUtil.getMOrg(rs.getLong("c_org_id")));
                    LeadsTask.this.salesMap.put(Long.parseLong(id.replaceAll("scae", "")), user);
                    salesMap2.put(rs.getString("c_ext_str2"), user);
                }return null;}});
         final Map<String,Integer> tempMap = new HashMap<String, Integer>();
         DBUtil.executeQuery(getSourceConn(), "select area_name,id from scae_area"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next()){
                    tempMap.put(rs.getString("area_name"), rs.getInt("id"));
                }return null;}});
         DBUtil.executeQuery(getTargetConn(), "select c_id,c_name from t_biz_vo_category where c_parent_id=260"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next()){
                    cityMap.put(tempMap.get(rs.getString("c_name")), rs.getInt("c_id"));
                }
                cityMap.put(14, 397);//南汇归为浦东
                cityMap.put(18, 397);//奉贤归为浦东
//                cityMap.put(19, 703);//崇明归为其他
                cityMap.put(0, 703);//其他
            return null;}});
         DBUtil.executeQuery(getTargetConn(), "select c_code,c_id from t_biz_vo_category where c_org_id="
                 +orgUtil.getParentOrg().getId() + " and c_type='ycclass'"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next())
                    levelMap.put(rs.getString("c_code"),rs.getInt("c_id"));
                levelMap.put("B", levelMap.get("A"));
                levelMap.put("C", levelMap.get("A"));
            return null;}});
         DBUtil.executeQuery(getTargetConn(), "select c_code,c_id from t_biz_vo_category where c_org_id="
                 +orgUtil.getParentOrg().getId() + " and c_type='fl_level'"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next())
                    flMap.put(rs.getString("c_code"),rs.getInt("c_id"));
                flMap.put("O", flMap.get("7"));
                flMap.put("H", flMap.get("2"));
                flMap.put("A", flMap.get("7"));
                flMap.put("B", flMap.get("7"));
                flMap.put("C", flMap.get("7"));
                flMap.put("N", flMap.get("30"));
            return null;}});
         DBUtil.executeQuery(getTargetConn(), "select c_special5,c_id from t_biz_vo_category where c_org_id="
                 +orgUtil.getParentOrg().getId() + " and c_type='color'"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next())
                    colorMap.put(rs.getString("c_special5"),rs.getInt("c_id"));
            return null;}});
         DBUtil.executeQuery(getTargetConn(), "select c_id,c_code,c_name from t_product where c_code like '%-%'"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                Product product = null;
                String code = null;
                while(rs.next()){
                    product = new Product();
                    product.setId(rs.getLong("c_id"));
                    code = rs.getString("c_code");
                    try{
                        productMap.put(Long.parseLong(code.substring(code.lastIndexOf("-")+1)),product);
                        productNameMap.put(rs.getString("c_name"), product);
                    }catch(Exception ex){continue;}
                }
            return null;}});
    }

    @Override
    public void destory() throws Exception {
        super.destory();
        personSaver.destory();
        customerSaver.destory();
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        setSource_id(rs.getLong("id"));//记录日志
        Leads lead = new Leads();
        //特殊标示字段
        lead.setP_ext_str1(rs.getString("id"));
        lead.setP_ext_str2("scae");
        //销售顾问
        lead.setUser(salesMap.get(rs.getLong("dcentersales_id")));
        //建卡日期 状态
        lead.setRegisterDate(rs.getDate("contact_date"));
        lead.setStatus(rs.getString("status").toUpperCase());
         //客户信息 联系人信息
        String contactno = rs.getString("contactno");//主要联系号码
        String ibno = rs.getString("ibno");//来电号码
        //创建联系人
        Contact contact = new Contact();
        contact.setLastname(rs.getString("name"));
        contact.setSex(rs.getString("gender"));
        Address address = new Address();
        address.setCity(cityMap.get(rs.getInt("area_id")));
        if(address.getCity() == 703){
            address.setProvince(286);
        }else{
            address.setProvince(260);
        }
        address.setBuilding(rs.getString("block"));
        contact.getAddresses().add(address);
        //设置组织
        contact.setOrganization(orgUtil.getOrg(rs.getLong("dcenter_id")));
        //处理电话号码
        Object[] objects = modify(contactno);//主要号码
        Object[] ibnos = modify(ibno);//次要号码
        contactPhone(contact,objects,ibnos,contactno);
        lead.setContact(contact);
        personSaver.save(lead.getContact());//保存Contact的Person部分
        lead.setCustomer(getCustomer(contact, lead, contactno, objects));//customer保存在getCustomer方法中
        contactSaver.save(new Object[]{lead.getContact(),new Long(lead.getCustomer().getId())});
        //客户意向信息
        lead.setOriginalLevel(levelMap.get(rs.getString("cycclass")).toString());
        lead.setCurlevel(levelMap.get(rs.getString("ycclass")).toString());
        lead.setExpOrderDur(flMap.get(rs.getString("ycclass")).toString());
        if("FAIL".equals(lead.getStatus()))
            lead.setNextFLDate(Date.valueOf("3000-12-31"));
        else
            lead.setNextFLDate(rs.getDate("fl_date"));
        lead.setTestDrive(rs.getString("trial").equals("F")?0:1);
        if(lead.getTestDrive() == 1){
            lead.setFeedBack("118");//试驾反馈
        }
        lead.setIsDealed((rs.getString("oc").equals("s"))?1:null);//是否交车
        //需求信息
        lead.setColor(colorMap.get(rs.getString("color")).toString());
        lead.setPayType(anthorBizMap.get(rs.getString("purpose")));
        lead.setBuySeq(anthorBizMap.get(rs.getString("type")));
        lead.setProductCat(proUtil.getProductCategory(rs.getString("carmodel_id")));
        lead.setProduct(productMap.get(rs.getLong("carsubmodel_id")));
        lead.setOrigProduct(productNameMap.get(rs.getString("ccarsubmodel")));
        lead.setRequireLic(anthor2BizMap.get(rs.getString("lic")));
        //其他信息
        lead.setPayApproach((rs.getString("pay")!=null)?(rs.getString("pay").equals("O")?"103":"102"):null);
        lead.setRegisterChannel(anthor3BizMap.get(rs.getString("chanel")));
        lead.setKnsource(getKnSource(rs.getString("source"),rs.getString("ksource")));//留档渠道
        String mediaName = rs.getString("medianame");
        if(mediaName.length()>20)mediaName = mediaName.substring(0, 20);
        lead.setInfoChannel(mediaName);//媒体名称
        lead.setP_ext_long2(salesMap2.get(rs.getString("csales")+""+rs.getString("dcenter_id")).getId()+"");//留档顾问

        lead.setBudget("101");//购车预算
        lead.setCurrPrep("107");//资金储备
        lead.setConcernPoints("22");//通话关注点
        lead.setAlreadyVistiedDlr("0");//是否已进店
        lead.setContactVenue("114");//接触场所
        lead.setCreatedBy(rs.getString("created_by"));
        lead.setCreatedDate(rs.getDate("create_date"));
        saveFl(lead);

        return lead;
    }

    private Customer getCustomer(Contact contact,Leads lead,String contactno,Object[] objects) throws SQLException{
        IndividuleCustomer customer = new IndividuleCustomer();
        String no = (String)objects[1];
        if(customerMap.containsKey(no)){
            customer.setId(customerMap.get(no));
            return customer;
        }
        customer.setPerson(contact);
        customer.setOwner(lead.getUser());
        customer.setOrganization(lead.getUser().getIntOrg());
        customer.setName(contact.getLastname());
        customer.setType("P");
        if(objects[0] != null){
            if((Boolean)objects[0]){
                customer.setBzid1("cp");
                customer.setBzid2(no);
            }else{
                customer.setBzid1("sp");
                String[] noArray = no.split(",");
                customer.setBzid2(noArray[0]);
                customer.setBzid3(noArray[1]);
                if(noArray.length == 3){
                    customer.setBzid4(noArray[2]);
                }
            }
        }else{
            customer.setBzid4("scae" + contactno);
        }
        customerSaver.save(customer);
        customerMap.put(no, customer.getId());
        return customer;
    }

    private void contactPhone(Contact contact,Object[] objects,Object[] ibnos,String contactno){
        String no = (String)objects[1];
        if(objects[0] != null){
            if((Boolean)objects[0]){
                contact.setCphone1(no);
            }else{
                String[] noArray = no.split(",");
                contact.setSphone1_ac(noArray[0]);
                contact.setSphone1(noArray[1]);
                if(noArray.length == 3){
                    contact.setSphone1_ext(noArray[2]);
                }
            }
        }else{
            contact.setP_ext_str1("scae" + contactno);
            System.out.println("未能解析\t" + contactno);
        }
        String ib = (String)ibnos[1];
        if(ibnos[0] != null){
            if((Boolean)ibnos[0]){
                contact.setCphone2(ib);
            }else{
                String[] noArray = ib.split(",");
                contact.setSphone2_ac(noArray[0]);
                contact.setSphone2(noArray[1]);
                if(noArray.length == 3){
                    contact.setSphone2_ext(noArray[2]);
                }
            }
        }
    }

    private Object[] modify(String contactno){
        String phone = contactno.replaceAll("\\D", "");
        String no = null;
        boolean noType = false;
        if(phone.matches("0?1[3,5,8]\\d*")){
            noType = true;
            no = phone;
            if(no.startsWith("0")){
                no = no.substring(1);
            }
            if(no.length()>11){
                no = no.substring(0, 11);
            }
        }else if(phone.matches("\\d{8}")){
            no = "021,"+phone;
        }else if(phone.matches("0?(2\\d|10)\\d{8}")){
            int skip = phone.startsWith("0")?3:2;
            no = (phone.startsWith("0")?"":"0") + phone.substring(0, skip) + "," + phone.substring(skip);
        }else if(phone.matches("0?[3-9]\\d{9,10}")){
            int skip = phone.startsWith("0")?4:3;
            no = (phone.startsWith("0")?"":"0") + phone.substring(0, skip) + "," + phone.substring(skip);
        }else if(phone.matches("\\d*1[3,5,8]\\d{9}")){
            noType = true;
            no = phone.substring(phone.length()-11);
        }else if(phone.matches("\\d*1[3,5,8]\\d{8}$")){
            noType = true;
            no = phone.substring(phone.length()-10);
        }else if(phone.matches("[2-9]\\d{8,12}")){
            no = "021," + phone.substring(0, 8) + "," + phone.substring(8);
        }else{
            return new Object[]{null,contactno};
        }
        return new Object[]{noType,no};
    }

    public String getKnSource(String s1,String s2){
        String result = null;
        if(s1.equals("C")){
            result = "23";
        }else if(s1.equals("B")){
            result = "24";
        }else if(s1.equals("S")||s1.equals("A")){
            if(s2.length() == 1){
                switch(s2.charAt(0)){
                    case 'T':result="25";break;
                    case 'B':result="26";break;
                    case 'N':result="27";break;
                    case 'J':result="28";break;
                    case 'W':result="29";break;
                    case 'M':result="33";break;
                    case 'F':result="33";break;
                    case 'O':result="38";break;
                }
            }
            if(s2.equals("FM")){
                result = "32";
            }else if(s2.equals("SMS")){
                result="33";
            }else if(s2.equals("DM")){
                result="35";
            }
        }
        return result;
    }

    public void saveFl(Leads lead){
        ContactRecord cr = new ContactRecord();
        cr.setContactDate(lead.getRegisterDate());
        cr.setSequence(0);
        cr.setFlType("123");//设置跟进性质,首次建卡
        cr.setFlContent("126");//预约来店
        Long fl_way_id = null;
        switch(lead.getRegisterChannel().charAt(0)){
            case 'I':fl_way_id = 127l;break;
            case 'C':fl_way_id = 131l;break;
            case 'H':fl_way_id = 126l;break;
        }
        cr.setContactChannel(String.valueOf(fl_way_id));
        cr.setP_ext_long1("close");
        cr.setLeads(lead);
        cr.setCustomer(lead.getCustomer());
        cr.setCreatedBy(lead.getCreatedBy());
        cr.setCreatedDate(lead.getCreatedDate());
        cr.setUser(lead.getUser());
        cr.setContact(lead.getContact());
        cr.setResult("FL");
        lead.getContactRecords().add(cr);
        lead.setP_ext_dt2(cr.getContactDate());//此字段的含义为上次跟进日期
        //如果建卡级别为O级，即为首次留档就订单，跟进为订单信息
        if(cr.getLeads().getOriginalLevel().equals(levelMap.get("O").toString())){
            cr.setResult("ORDER");
        }
        //如果建卡用户直接成交，跟进为成交信息
        if(Integer.valueOf(1).equals(lead.getIsDealed())){
            cr.setP_ext_str1("s" + lead.getP_ext_str1());
            cr.setP_ext_dt1(cr.getContactDate());//成交日期
            lead.setP_ext_dt3(cr.getContactDate());//成交日期记录在留档中
            cr.setResult("SUC");
        }
    }

    public void setProUtil(ProductCategoryUtil proUtil) {
        this.proUtil = proUtil;
    }

    public void setOrgUtil(OrgUtil orgUtil) {
        this.orgUtil = orgUtil;
    }

    public void setCustomerSaver(CustomerSaver customerSaver) {
        this.customerSaver = customerSaver;
    }

    public void setPersonSaver(PersonImplSaver personSaver) {
        this.personSaver = personSaver;
    }

    public void setContactSaver(ContactSaver contactSaver) {
        this.contactSaver = contactSaver;
    }
    
}
