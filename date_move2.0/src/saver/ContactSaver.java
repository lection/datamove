package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.customer.model.Contact;

public class ContactSaver extends AbstractSaver{
    public Object cap(Object object) throws SQLException{
        Object[] objs = (Object[])object;
        Contact obj = (Contact)objs[0];
        PreparedStatement pre = getPreStat();
        pre.setLong(1,obj.getId());
        if(obj.getOrganization() != null)
            pre.setLong(2,obj.getOrganization().getId());
        else pre.setNull(2,Types.BIGINT);
        if(objs[1] != null)
            pre.setLong(3,(Long)objs[1]);
        else pre.setNull(3,Types.BIGINT);
        return null;
    }

    public String getInsertSql(){
        return "insert into t_contact"
         + " (c_person_id ,c_org_id,c_cust_id"
         +") values(?,?,?)";
    }
}