package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.um.model.UserImpl;

public class UserImplSaver extends AbstractSaver{
    public void save(Object object) throws SQLException{
        UserImpl obj = (UserImpl)object;
        PreparedStatement pre = getPreStat();
        pre.setString(1,obj.getLoginName());
        pre.setString(2,obj.getPassword());
        pre.setInt(3,obj.getLock());
        pre.setInt(4,obj.getActiveStatus());
        pre.setInt(5,obj.getLogStatus());
        if(obj.getRegisterDate() != null)
            pre.setDate(6,new Date(obj.getRegisterDate().getTime()));
        else pre.setDate(6,null);
        if(obj.getInactiveDate() != null)
            pre.setDate(7,new Date(obj.getInactiveDate().getTime()));
        else pre.setDate(7,null);
        pre.setInt(8,obj.getExpirationDay());
        if(obj.getPasswordExpirationDate() != null)
            pre.setDate(9,new Date(obj.getPasswordExpirationDate().getTime()));
        else pre.setDate(9,null);
        if(obj.getManager() != null)
            pre.setLong(10,obj.getManager().getId());
        else pre.setNull(10,Types.BIGINT);
        if(obj.getIntOrg() != null)
            pre.setLong(11,obj.getIntOrg().getId());
        else pre.setNull(11,Types.BIGINT);
        if(obj.getTenent() != null)
            pre.setLong(12,obj.getTenent().getId());
        else pre.setNull(12,Types.BIGINT);
    }

    public String getInsertSql(){
        return "insert into t_um_user"
         + " (c_person_id ,c_login ,c_password ,c_status ,c_activestatus ,c_logstatus ,c_registerDate ,c_inactiveDate ,c_expirationDay ,c_passwordExpirationDate ,c_user_id ,c_org_id ,c_tenent_id"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}