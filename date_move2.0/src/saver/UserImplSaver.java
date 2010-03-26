package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.um.model.UserImpl;

public class UserImplSaver extends AbstractSaver{

    private PersonImplSaver personSaver;

    public Object cap(Object object) throws SQLException{
        UserImpl obj = (UserImpl)object;
        personSaver.save(obj);
        PreparedStatement pre = getPreStat();
        pre.setLong(1,obj.getId());
        try{pre.setString(2,obj.getLoginName());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        try{pre.setString(3,obj.getPassword());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setInt(4,obj.getLock());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        try{pre.setInt(5,obj.getActiveStatus());}
        catch(NullPointerException ex){pre.setNull(5,Types.INTEGER);}
        try{pre.setInt(6,obj.getLogStatus());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        if(obj.getRegisterDate() != null)
            pre.setDate(7,new Date(obj.getRegisterDate().getTime()));
        else pre.setDate(7,null);
        if(obj.getInactiveDate() != null)
            pre.setDate(8,new Date(obj.getInactiveDate().getTime()));
        else pre.setDate(8,null);
        try{pre.setInt(9,obj.getExpirationDay());}
        catch(NullPointerException ex){pre.setNull(9,Types.INTEGER);}
        if(obj.getPasswordExpirationDate() != null)
            pre.setDate(10,new Date(obj.getPasswordExpirationDate().getTime()));
        else pre.setDate(10,null);
        if(obj.getManager() != null)
            pre.setLong(11,obj.getManager().getId());
        else pre.setNull(11,Types.BIGINT);
        if(obj.getIntOrg() != null)
            pre.setLong(12,obj.getIntOrg().getId());
        else pre.setNull(12,Types.BIGINT);
        if(obj.getTenent() != null)
            pre.setLong(13,obj.getTenent().getId());
        else pre.setNull(13,Types.BIGINT);
        return obj.getId();
    }

    public String getInsertSql(){
        return "insert into t_um_user"
         + " (c_person_id ,c_login ,c_password ,c_status ,c_activestatus ,c_logstatus ,c_registerDate ,c_inactiveDate ,c_expirationDay ,c_passwordExpirationDate ,c_user_id ,c_org_id ,c_tenent_id"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    @Override
    public void init() throws SQLException {
        super.init();
        personSaver.destory();
        personSaver.setTargetConn(this.getTargetConn());
        personSaver.init();
    }

    @Override
    public void destory() throws SQLException {
        super.destory();
        personSaver.destory();
    }

    public void setPersonSaver(PersonImplSaver personSaver) {
        this.personSaver = personSaver;
    }
}