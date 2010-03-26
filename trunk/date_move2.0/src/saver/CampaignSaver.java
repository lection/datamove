package saver;

import com.linkin.crm.campaign.model.Budget;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.SQLException;
import com.linkin.crm.campaign.model.Campaign;
import com.linkin.crm.campaign.model.ExpandDevelop;
import util.saver.AbstractSaver;

public class CampaignSaver extends AbstractSaver{
    private ExpandDevelopSaver expandDevelopSaver;
    private BudgetSaver budgetSaver;
    private Campaign cam;
    public CampaignSaver() throws SQLException{initHilo("t_c3_hi_value","c_next_value",100);}
    public Object cap(Object object) throws SQLException{
        Campaign obj = (Campaign)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        try{pre.setString(2,obj.getName());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        if(obj.getStartDate() != null)
            pre.setTimestamp(3,new Timestamp(obj.getStartDate().getTime()));
        else pre.setTimestamp(3,null);
        if(obj.getEndDate() != null)
            pre.setTimestamp(4,new Timestamp(obj.getEndDate().getTime()));
        else pre.setTimestamp(4,null);
        try{pre.setString(5,obj.getCampLocation());}
        catch(NullPointerException ex){pre.setNull(5,Types.INTEGER);}
        try{pre.setString(6,obj.getCampAddress());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        try{pre.setString(7,obj.getCampType());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        try{pre.setDouble(8,obj.getTotal());}
        catch(NullPointerException ex){pre.setNull(8,Types.INTEGER);}
        try{pre.setDouble(9,obj.getExpand());}
        catch(NullPointerException ex){pre.setNull(9,Types.INTEGER);}
        try{pre.setDouble(10,obj.getInventory());}
        catch(NullPointerException ex){pre.setNull(10,Types.INTEGER);}
        try{pre.setDouble(11,obj.getOther());}
        catch(NullPointerException ex){pre.setNull(11,Types.INTEGER);}
        try{pre.setString(12,obj.getBudgetType());}
        catch(NullPointerException ex){pre.setNull(12,Types.INTEGER);}
        try{pre.setLong(13,obj.getUserId());}
        catch(NullPointerException ex){pre.setNull(13,Types.BIGINT);}
        if(obj.getOrg() != null)
            pre.setLong(14,obj.getOrg().getId());
        else pre.setNull(14,Types.BIGINT);
        return obj.getId();
    }

    public String getInsertSql(){
        return "insert into t_campaign"
         + " (c_id ,c_name ,c_startDate ,c_endDate ,c_campLocation ,c_campAddress ,c_campType ,c_total ,c_expand ,c_inventory ,c_other ,t_budgetType ,c_user_id ,c_org_id"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    @Override
    public Object save(Object object) throws SQLException {
        Campaign  c = (Campaign)object;
        if(cam==null || c!=cam){
            super.save(c);
            cam = c;
            for(Budget b:c.getSetBudget()){
                budgetSaver.save(b);
            }
        }
        if(c.getSet()!=null){
            for(ExpandDevelop ex:c.getSet()){
                expandDevelopSaver.save(ex);
            }
        }
        return c.getId();
    }

    @Override
    public void destory() throws SQLException {
        super.destory();
        budgetSaver.destory();
        expandDevelopSaver.destory();
    }

    @Override
    public void init() throws SQLException {
        super.init();
        expandDevelopSaver.setTargetConn(this.getTargetConn());
        expandDevelopSaver.init();
        budgetSaver.setTargetConn(this.getTargetConn());
        budgetSaver.init();
    }

    public void setExpandDevelopSaver(ExpandDevelopSaver expandDevelopSaver) {
        this.expandDevelopSaver = expandDevelopSaver;
    }

    public void setBudgetSaver(BudgetSaver budgetSaver) {
        this.budgetSaver = budgetSaver;
    }
}