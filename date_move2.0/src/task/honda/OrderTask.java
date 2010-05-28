/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.honda;

import task.scae.*;
import com.linkin.crm.customer.model.Contact;
import com.linkin.crm.customer.model.Customer;
import com.linkin.crm.product.model.Product;
import com.linkin.crm.sales.model.ContactRecord;
import com.linkin.crm.sales.model.Leads;
import com.linkin.crm.sales.model.Order;
import com.linkin.crm.um.model.UserImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import util.DBUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class OrderTask extends J2JTaskSupport{
    private Map<String,Leads> leadsMap = new HashMap<String,Leads>();
    private Map<Long,Product> productMap = new HashMap<Long, Product>();
    private Map<Long,UserImpl> userMap = new HashMap<Long, UserImpl>();
    private Map<Long,ContactRecord> recordMap = new HashMap<Long, ContactRecord>();

    @Override
    public void init() throws Exception {
        super.init();
        DBUtil.executeQuery(getTargetConn(), "select c_id,c_ext_str1,c_product_id from t_leads"
        , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                Leads lead = null;
                Long product_id = null;
                while(rs.next()){
                    lead = new Leads();lead.setId(rs.getLong("c_id"));
                    product_id = rs.getLong("c_product_id");
                    if(!productMap.containsKey(product_id)){
                        productMap.put(product_id, new Product());
                        productMap.get(product_id).setId(product_id);
                        System.out.println("id:"+product_id+"\t"+lead.getId());
                    }
                    lead.setProduct(productMap.get(product_id));
                    leadsMap.put(rs.getString("c_ext_str1"),lead);
                }
        return null;}});
        DBUtil.executeQuery(getTargetConn(),
        "select c_leads_id,c_cust_id,c_user_id,c_contact_id,c_contact_date,c_result" +
                " from t_contactrecord where c_result='SUC' or c_result='ORDER'"
        , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                Customer customer = null;
                Contact contact = null;
                ContactRecord cr = null;
                ContactRecord cr_temp = null;
                Long leads_id = null;
                Long user_id = null;
                while(rs.next()){
                    cr = new ContactRecord();
                    leads_id = rs.getLong("c_leads_id");
                    cr.setResult(rs.getString("c_result"));
                    cr.setContactDate(rs.getDate("c_contact_date"));
                    cr_temp = recordMap.get(leads_id);
                    if(cr_temp != null){
                        if("ORDER".equals(cr.getResult())){
                            if("ORDER".equals(cr_temp.getResult())&&cr_temp.getContactDate().after(cr.getContactDate()))continue;
                        }else{
                            if("ORDER".equals(cr_temp.getResult()))continue;
                            if(cr_temp.getContactDate().before(cr.getContactDate()))continue;
                        }
                    }
                    recordMap.put(leads_id, cr);
                    customer = new Customer();customer.setId(rs.getLong("c_cust_id"));cr.setCustomer(customer);
                    contact = new Contact();contact.setId(rs.getLong("c_contact_id"));cr.setContact(contact);
                    user_id = rs.getLong("c_user_id");
                    if(!userMap.containsKey(user_id)){
                        userMap.put(user_id, new UserImpl());
                        userMap.get(user_id).setId(user_id);
                    }
                    cr.setUser(userMap.get(user_id));
                }
                
        return null;}});
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        setSource_id(rs.getLong("id"));//记录日志
        Order order = new Order();
        order.setLeads(leadsMap.get(rs.getString("id")));//跟进意向
        ContactRecord cr = recordMap.get(order.getLeads().getId());
        if(cr == null){
            System.out.println("未找到yellowcard对应的成交跟进   " + rs.getString("id"));
            throw new DataException(order);
        }
        order.setUser(cr.getUser());//跟进人
        order.setContact(cr.getContact());
        order.setOrderDate(cr.getContactDate());//订单日期
        order.setCreatedBy("data_m");
        order.setOrderType("0");//非订金
        order.setOrderValue("未收集");//订金金额
        order.setGiveType("148");//配给方式
        order.setCustomer(cr.getCustomer());
        order.setProduct(order.getLeads().getProduct());//车辆信息
        System.out.println(order.getLeads().getProduct().getId());
        return order;
    }

    @Override
    public void errorHandle(DataException ex) {}

}
