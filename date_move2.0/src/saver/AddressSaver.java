package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.core.model.Address;
import util.DBUtil;

public class AddressSaver extends AbstractSaver{
    public Object cap(Object object) throws SQLException{
        Address obj = (Address)object;
        PreparedStatement pre = getPreStat();
        pre.setNull(1, Types.BIGINT);
        try{pre.setString(2,obj.getType());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        try{pre.setString(3,obj.getStatus());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setInt(4,obj.getCountry());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        try{pre.setInt(5,obj.getProvince());}
        catch(NullPointerException ex){pre.setNull(5,Types.INTEGER);}
        try{pre.setInt(6,obj.getCity());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        try{pre.setString(7,obj.getStreet());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        try{pre.setString(8,obj.getBuilding());}
        catch(NullPointerException ex){pre.setNull(8,Types.INTEGER);}
        try{pre.setString(9,obj.getZip());}
        catch(NullPointerException ex){pre.setNull(9,Types.INTEGER);}
        try{pre.setString(10,obj.getFax());}
        catch(NullPointerException ex){pre.setNull(10,Types.INTEGER);}
        return obj.getId();
    }

    @Override
    public Object save(Object object) throws SQLException {
        super.save(object);
        ((Address)object).setId(DBUtil.getLaskKey(getTargetConn(), "t_address"));
        return null;
    }

    public String getInsertSql(){
        return "insert into t_address"
         + " (c_id ,c_type ,c_status ,c_country ,c_province ,c_city ,c_street ,c_building ,c_zip ,c_fax"
         +") values(?,?,?,?,?,?,?,?,?,?)";
    }
}