package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.campaign.model.Budget;

public class BudgetSaver extends AbstractSaver{
    public BudgetSaver() throws SQLException{initHilo("t_s2_hi_value","next_value",100);}
    public void cap(Object object) throws SQLException{
        Budget obj = (Budget)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        try{pre.setInt(2,obj.getMonth());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        try{pre.setInt(3,obj.getYear());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setDouble(4,obj.getMoney());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
    }

    public String getInsertSql(){
        return "insert into t_budget"
         + " (c_id ,c_month ,c_year ,c_money"
         +") values(?,?,?,?)";
    }
}