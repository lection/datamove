package saver;

import com.linkin.crm.campaign.model.Budget;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.campaign.model.MediaPlan;

public class MediaPlanSaver extends AbstractSaver{
    private BudgetSaver budgetSaver;
    public MediaPlanSaver() throws SQLException{initHilo("t_m2_hi_value","next_value",100);}
    public Object cap(Object object) throws SQLException{
        MediaPlan obj = (MediaPlan)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        if(obj.getStartDate() != null)
            pre.setTimestamp(2,new Timestamp(obj.getStartDate().getTime()));
        else pre.setTimestamp(2,null);
        if(obj.getEndDate() != null)
            pre.setTimestamp(3,new Timestamp(obj.getEndDate().getTime()));
        else pre.setTimestamp(3,null);
        try{pre.setLong(4,obj.getMediaChannel());}
        catch(NullPointerException ex){pre.setNull(4,Types.BIGINT);}
        try{pre.setString(5,obj.getMediaName());}
        catch(NullPointerException ex){pre.setNull(5,Types.INTEGER);}
        if(obj.getCreatedDate() != null)
            pre.setTimestamp(6,new Timestamp(obj.getCreatedDate().getTime()));
        else pre.setTimestamp(6,null);
        try{pre.setString(7,obj.getBudgetType());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        try{pre.setDouble(8,obj.getBudgetTotal());}
        catch(NullPointerException ex){pre.setNull(8,Types.INTEGER);}
        if(obj.getHotLine() != null)
            pre.setLong(9,obj.getHotLine().getId());
        else pre.setNull(9,Types.BIGINT);
        if(obj.getOrg() != null)
            pre.setLong(10,obj.getOrg().getId());
        else pre.setNull(10,Types.BIGINT);
        return obj.getId();
    }

    public String getInsertSql(){
        return "insert into t_mediaplan"
         + " (c_id ,c_startdate ,c_enddate ,c_mediaChannel_id ,c_mediaName ,c_created_date ,t_budgetType ,c_budgetTotal ,c_hotline_id ,c_org_id"
         +") values(?,?,?,?,?,?,?,?,?,?)";
    }

    @Override
    public Object save(Object object) throws SQLException {
        super.save(object);
        for(Budget budget:((MediaPlan)object).getMediaBudget()){
            budget.setMediaPlan((MediaPlan)object);
            budgetSaver.save(budget);
        }
        return ((MediaPlan)object).getId();
    }

    @Override
    public void init() throws SQLException {
        super.init();
        budgetSaver.setTargetConn(this.getTargetConn());
        budgetSaver.init();
    }

    @Override
    public void destory() throws SQLException {
        super.destory();
        budgetSaver.destory();
    }

    public void setBudgetSaver(BudgetSaver budgetSaver) {
        this.budgetSaver = budgetSaver;
    }
}