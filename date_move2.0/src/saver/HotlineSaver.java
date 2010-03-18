package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.core.model.Hotline;

public class HotlineSaver extends AbstractSaver{
    public void cap(Object object) throws SQLException{
        Hotline obj = (Hotline)object;
        PreparedStatement pre = getPreStat();
        pre.setLong(1,obj.getId());
        try{pre.setString(2,obj.getHotline());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        try{pre.setString(3,obj.getStatus());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setString(4,obj.getVisible());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        if(obj.getStartDate() != null)
            pre.setDate(5,new Date(obj.getStartDate().getTime()));
        else pre.setDate(5,null);
        if(obj.getEndDate() != null)
            pre.setDate(6,new Date(obj.getEndDate().getTime()));
        else pre.setDate(6,null);
        try{pre.setString(7,obj.getExt_long1());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        try{pre.setString(8,obj.getExt_str1());}
        catch(NullPointerException ex){pre.setNull(8,Types.INTEGER);}
        try{pre.setString(9,obj.getExt_str2());}
        catch(NullPointerException ex){pre.setNull(9,Types.INTEGER);}
        if(obj.getExt_dt1() != null)
            pre.setTimestamp(10,new Timestamp(obj.getExt_dt1().getTime()));
        else pre.setTimestamp(10,null);
        if(obj.getExt_dt2() != null)
            pre.setTimestamp(11,new Timestamp(obj.getExt_dt2().getTime()));
        else pre.setTimestamp(11,null);
        try{pre.setString(12,obj.getCreatedBy());}
        catch(NullPointerException ex){pre.setNull(12,Types.INTEGER);}
        if(obj.getCreatedDate() != null)
            pre.setTimestamp(13,new Timestamp(obj.getCreatedDate().getTime()));
        else pre.setTimestamp(13,null);
        try{pre.setString(14,obj.getUpdatedBy());}
        catch(NullPointerException ex){pre.setNull(14,Types.INTEGER);}
        if(obj.getUpdateDate() != null)
            pre.setTimestamp(15,new Timestamp(obj.getUpdateDate().getTime()));
        else pre.setTimestamp(15,null);
        if(obj.getParent() != null)
            pre.setLong(16,obj.getParent().getId());
        else pre.setNull(16,Types.BIGINT);
        if(obj.getOrg() != null)
            pre.setLong(17,obj.getOrg().getId());
        else pre.setNull(17,Types.BIGINT);
    }

    public String getInsertSql(){
        return "insert into t_hotline"
         + " (c_id ,c_hotline ,c_status ,c_visible ,c_start_date ,c_end_date ,c_ext_long1 ,c_ext_str1 ,ext_str2 ,c_ext_dt1 ,c_ext_dt2 ,c_created_by ,c_created_date ,c_updated_by ,c_update_date ,c_parent_id ,c_org_id"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}