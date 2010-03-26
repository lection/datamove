package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.comm.model.IBCall;

public class IBCallSaver extends AbstractSaver{
    public Object cap(Object object) throws SQLException{
        IBCall obj = (IBCall)object;
        PreparedStatement pre = getPreStat();
        pre.setNull(1, Types.BIGINT);
        try{pre.setString(2,obj.getLeads());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        try{pre.setString(3,obj.getIbno());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setString(4,obj.getCallno());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        try{pre.setString(5,obj.getPick());}
        catch(NullPointerException ex){pre.setNull(5,Types.INTEGER);}
        try{pre.setInt(6,obj.getDur());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        try{pre.setString(7,obj.getDurDisplay());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        if(obj.getStartTime() != null)
            pre.setTimestamp(8,new Timestamp(obj.getStartTime().getTime()));
        else pre.setTimestamp(8,null);
        if(obj.getEndTime() != null)
            pre.setTimestamp(9,new Timestamp(obj.getEndTime().getTime()));
        else pre.setTimestamp(9,null);
        try{pre.setString(10,obj.getRec());}
        catch(NullPointerException ex){pre.setNull(10,Types.INTEGER);}
        try{pre.setString(11,obj.getHallLine());}
        catch(NullPointerException ex){pre.setNull(11,Types.INTEGER);}
        try{pre.setBoolean(12,obj.getVisible());}
        catch(NullPointerException ex){pre.setNull(12,Types.INTEGER);}
        try{pre.setString(13,obj.getP_ext_long1());}
        catch(NullPointerException ex){pre.setNull(13,Types.INTEGER);}
        try{pre.setString(14,obj.getP_ext_str1());}
        catch(NullPointerException ex){pre.setNull(14,Types.INTEGER);}
        try{pre.setString(15,obj.getP_ext_str2());}
        catch(NullPointerException ex){pre.setNull(15,Types.INTEGER);}
        if(obj.getP_ext_dt1() != null)
            pre.setTimestamp(16,new Timestamp(obj.getP_ext_dt1().getTime()));
        else pre.setTimestamp(16,null);
        if(obj.getP_ext_dt2() != null)
            pre.setTimestamp(17,new Timestamp(obj.getP_ext_dt2().getTime()));
        else pre.setTimestamp(17,null);
        try{pre.setString(18,obj.getCreatedBy());}
        catch(NullPointerException ex){pre.setNull(18,Types.INTEGER);}
        if(obj.getCreatedDate() != null)
            pre.setTimestamp(19,new Timestamp(obj.getCreatedDate().getTime()));
        else pre.setTimestamp(19,null);
        try{pre.setString(20,obj.getUpdatedBy());}
        catch(NullPointerException ex){pre.setNull(20,Types.INTEGER);}
        if(obj.getUpdateDate() != null)
            pre.setTimestamp(21,new Timestamp(obj.getUpdateDate().getTime()));
        else pre.setTimestamp(21,null);
        if(obj.getOrg() != null)
            pre.setLong(22,obj.getOrg().getId());
        else pre.setNull(22,Types.BIGINT);
        return obj.getId();
    }

    public String getInsertSql(){
        return "insert into t_ibcall"
         + " (c_id ,c_leads ,c_ibno ,c_callno ,c_pick ,c_dur ,c_dur_display ,c_starttime ,c_endtime ,c_rec ,c_hallline ,c_visible ,c_ext_long1 ,c_ext_str1 ,c_ext_str2 ,c_ext_dt1 ,c_ext_dt2 ,c_created_by ,c_created_date ,c_updated_by ,c_update_date ,c_org_id"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}