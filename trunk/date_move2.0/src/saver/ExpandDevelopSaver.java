package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.campaign.model.ExpandDevelop;
import util.saver.AbstractSaver;

public class ExpandDevelopSaver extends AbstractSaver{
    public ExpandDevelopSaver() throws SQLException{initHilo("t_s2_hi_value","next_value",100);}
    public void cap(Object object) throws SQLException{
        ExpandDevelop obj = (ExpandDevelop)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        try{pre.setInt(2,obj.getTarget());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        if(obj.getProductCategory() != null)
            pre.setLong(3,obj.getProductCategory().getId());
        else pre.setNull(3,Types.BIGINT);
    }

    public String getInsertSql(){
        return "insert into t_expanddevelop"
         + " (c_id ,c_target ,c_product_id"
         +") values(?,?,?)";
    }
}