package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.sales.model.Order;

public class OrderSaver extends AbstractSaver{
    public OrderSaver() throws SQLException{initHilo("t_01_hi_value","next_value",10);}
    public Object cap(Object object) throws SQLException{
        Order obj = (Order)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        if(obj.getOrderDate() != null)
            pre.setDate(2,new Date(obj.getOrderDate().getTime()));
        else pre.setDate(2,null);
        try{pre.setString(3,obj.getOrderNumber());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setString(4,obj.getOrderType());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        try{pre.setString(5,obj.getOrderValue());}
        catch(NullPointerException ex){pre.setNull(5,Types.INTEGER);}
        try{pre.setString(6,obj.getGiveType());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        if(obj.getGetDate() != null)
            pre.setDate(7,new Date(obj.getGetDate().getTime()));
        else pre.setDate(7,null);
        try{pre.setString(8,obj.getMemo());}
        catch(NullPointerException ex){pre.setNull(8,Types.INTEGER);}
        try{pre.setString(9,obj.getP_ext_long1());}
        catch(NullPointerException ex){pre.setNull(9,Types.INTEGER);}
        try{pre.setString(10,obj.getP_ext_long2());}
        catch(NullPointerException ex){pre.setNull(10,Types.INTEGER);}
        try{pre.setString(11,obj.getP_ext_long3());}
        catch(NullPointerException ex){pre.setNull(11,Types.INTEGER);}
        try{pre.setString(12,obj.getP_ext_str1());}
        catch(NullPointerException ex){pre.setNull(12,Types.INTEGER);}
        try{pre.setString(13,obj.getP_ext_str2());}
        catch(NullPointerException ex){pre.setNull(13,Types.INTEGER);}
        try{pre.setString(14,obj.getP_ext_str3());}
        catch(NullPointerException ex){pre.setNull(14,Types.INTEGER);}
        try{pre.setString(15,obj.getP_ext_str4());}
        catch(NullPointerException ex){pre.setNull(15,Types.INTEGER);}
        if(obj.getP_ext_dt1() != null)
            pre.setTimestamp(16,new Timestamp(obj.getP_ext_dt1().getTime()));
        else pre.setTimestamp(16,null);
        if(obj.getP_ext_dt2() != null)
            pre.setTimestamp(17,new Timestamp(obj.getP_ext_dt2().getTime()));
        else pre.setTimestamp(17,null);
        if(obj.getP_ext_dt3() != null)
            pre.setTimestamp(18,new Timestamp(obj.getP_ext_dt3().getTime()));
        else pre.setTimestamp(18,null);
        try{pre.setString(19,obj.getCreatedBy());}
        catch(NullPointerException ex){pre.setNull(19,Types.INTEGER);}
        if(obj.getCreatedDate() != null)
            pre.setTimestamp(20,new Timestamp(obj.getCreatedDate().getTime()));
        else pre.setTimestamp(20,null);
        try{pre.setString(21,obj.getUpdatedBy());}
        catch(NullPointerException ex){pre.setNull(21,Types.INTEGER);}
        if(obj.getUpdateDate() != null)
            pre.setTimestamp(22,new Timestamp(obj.getUpdateDate().getTime()));
        else pre.setTimestamp(22,null);
        if(obj.getCustomer() != null)
            pre.setLong(23,obj.getCustomer().getId());
        else pre.setNull(23,Types.BIGINT);
        if(obj.getContact() != null)
            pre.setLong(24,obj.getContact().getId());
        else pre.setNull(24,Types.BIGINT);
        if(obj.getProduct() != null)
            pre.setLong(25,obj.getProduct().getId());
        else pre.setNull(25,Types.BIGINT);
        if(obj.getUser() != null)
            pre.setLong(26,obj.getUser().getId());
        else pre.setNull(26,Types.BIGINT);
        if(obj.getLeads() != null)
            pre.setLong(27,obj.getLeads().getId());
        else pre.setNull(27,Types.BIGINT);
        return obj.getId();
    }

    public String getInsertSql(){
        return "insert into t_order"
         + " (c_id ,c_order_date ,c_order_number ,c_order_type ,c_order_value ,c_give_type ,c_get_date ,c_memo ,c_ext_long1 ,c_ext_long2 ,c_ext_long3 ,c_ext_str1 ,c_ext_str2 ,c_ext_str3 ,c_ext_str4 ,c_ext_dt1 ,c_ext_dt2 ,c_ext_dt3 ,c_created_by ,c_created_date ,c_updated_by ,c_update_date ,c_cust_id ,c_contact_id ,c_product_id ,c_user_id ,c_leads_id"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}