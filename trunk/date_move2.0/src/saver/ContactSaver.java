package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.customer.model.Contact;

public class ContactSaver extends AbstractSaver{
    public void cap(Object object) throws SQLException{
        Contact obj = (Contact)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        if(obj.getOrganization() != null)
            pre.setLong(2,obj.getOrganization().getId());
        else pre.setNull(2,Types.BIGINT);
    }

    public String getInsertSql(){
        return "insert into t_contact"
         + " (c_person_id ,c_org_id"
         +") values(?,?)";
    }
}