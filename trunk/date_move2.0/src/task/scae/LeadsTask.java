/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.core.model.Address;
import com.linkin.crm.customer.model.Contact;
import com.linkin.crm.customer.model.Customer;
import com.linkin.crm.sales.model.Leads;
import com.linkin.crm.um.model.UserImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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

    private Map<Long, UserImpl> salesMap = new HashMap<Long, UserImpl>(700);
    private Map<Integer,Integer> cityMap = new HashMap<Integer,Integer>(30);
    private Map<String,Long> customerMap = new HashMap<String,Long>();


    @Override
    public void init() throws Exception {
        super.init();
        DBUtil.executeQuery(getTargetConn(), "select u.c_id from t_um_user u where u.c_ext_str1 like 'scae%'"
        , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                UserImpl user = null;
                while(rs.next()){
                    user = new UserImpl();
                    user.setId(rs.getLong("c_id"));
                    LeadsTask.this.salesMap.put(user.getId(), user);
                }return null;}});
         final Map<String,Integer> tempMap = new HashMap<String, Integer>();
         DBUtil.executeQuery(getSourceConn(), "select area_name,id from scae_area"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next()){
                    tempMap.put(rs.getString("area_name"), rs.getInt("id"));
                }return null;}});
         DBUtil.executeQuery(getTargetConn(), "select c_id,c_name from t_biz_vo_category where c_parend_id=260"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next())
                    cityMap.put(tempMap.get(rs.getString("c_name")), rs.getInt("c_id"));
                cityMap.put(0, 703);
            return null;}});
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
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
        
        
        return lead;
    }

    private Customer getCustomer(Contact contact,Leads lead){
        Customer customer = null;
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

    public void setProUtil(ProductCategoryUtil proUtil) {
        this.proUtil = proUtil;
    }

    public void setOrgUtil(OrgUtil orgUtil) {
        this.orgUtil = orgUtil;
    }
    
}
