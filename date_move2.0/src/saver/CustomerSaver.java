package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.customer.model.IndividuleCustomer;

public class CustomerSaver extends AbstractSaver{
    public CustomerSaver() throws SQLException{initHilo("t_c1_hi_value","c_cust_next_value",10);}
    public Object cap(Object object) throws SQLException{
        IndividuleCustomer obj = (IndividuleCustomer)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        try{pre.setString(2,obj.getName());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        try{pre.setString(3,obj.getBzid1());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setString(4,obj.getBzid2());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        try{pre.setString(5,obj.getBzid3());}
        catch(NullPointerException ex){pre.setNull(5,Types.INTEGER);}
        try{pre.setString(6,obj.getBzid4());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        try{pre.setString(7,obj.getType());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        try{pre.setString(8,obj.getCreatedBy());}
        catch(NullPointerException ex){pre.setNull(8,Types.INTEGER);}
        if(obj.getCreatedDate() != null)
            pre.setTimestamp(9,new Timestamp(obj.getCreatedDate().getTime()));
        else pre.setTimestamp(9,null);
        try{pre.setString(10,obj.getUpdatedBy());}
        catch(NullPointerException ex){pre.setNull(10,Types.INTEGER);}
        if(obj.getUpdateDate() != null)
            pre.setTimestamp(11,new Timestamp(obj.getUpdateDate().getTime()));
        else pre.setTimestamp(11,null);
        if(obj.getOwner() != null)
            pre.setLong(12,obj.getOwner().getId());
        else pre.setNull(12,Types.BIGINT);
        if(obj.getOrganization() != null)
            pre.setLong(13,obj.getOrganization().getId());
        else pre.setNull(13,Types.BIGINT);
        pre.setString(14, "IND");
        if(obj.getPerson() != null)
            pre.setLong(15,obj.getPerson().getId());
        else pre.setNull(15,Types.BIGINT);
        return null;
    }

    public String getInsertSql(){
        return "insert into t_customer"
         + " (c_id ,c_name ,c_bzid1 ,c_bzid2 ,c_bzid3 ,c_bzid4 ,c_type ,c_created_by ,c_created_date ,c_updated_by ,c_update_date ,c_owner_id ,c_intorg_id,c_cust_type,c_person_id"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}