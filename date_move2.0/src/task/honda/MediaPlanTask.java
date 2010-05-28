/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.honda;

import task.scae.*;
import com.linkin.crm.campaign.model.Budget;
import com.linkin.crm.campaign.model.MediaPlan;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import util.HotlineUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class MediaPlanTask extends J2JTaskSupport{
    private HotlineUtil hotlineUtil;
    private Map<String,Integer> channelMap = new HashMap<String,Integer>();
    
    {
        channelMap.put("SMS", 8086);
        channelMap.put("W", 8081);
        channelMap.put("O", 8090);
        channelMap.put("N", 8079);
        channelMap.put("T", 8109);
        channelMap.put("M", 8083);
        channelMap.put("FM", 8084);
        channelMap.put("DM", 8087);
        channelMap.put("J", 8080);
        channelMap.put("F", 8085);
        channelMap.put("B", 8110);
        channelMap.put("114", 8088);
        channelMap.put("EZZ", 8089);
    }
    
    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        MediaPlan plan = new MediaPlan();
        plan.setStartDate(rs.getDate("start_date"));
        plan.setEndDate(rs.getDate("end_date"));
        String ibno = rs.getString("ibno");
        plan.setHotLine(hotlineUtil.getHotline(ibno,plan.getStartDate()));
        if(plan.getHotLine() == null){
            plan.setHotLine(hotlineUtil.getHotline(ibno,plan.getStartDate()));
            throw new DataException(new Object[]{rs.getLong("id"),ibno});
        }
        plan.setOrg(plan.getHotLine().getOrg());
        plan.setMediaName(rs.getString("media_name"));
        plan.setMediaChannel(channelMap.get(rs.getString("media_type")));
        plan.setBudgetType("single");
        plan.setBudgetTotal(0);
        plan.setCreatedDate(rs.getDate("create_date"));
        Budget budget = new Budget();
        budget.setMediaPlan(plan);
        budget.setMoney(0);
        budget.setYear(plan.getStartDate().getYear()+1900);
        budget.setMonth(plan.getStartDate().getMonth()+1);
        plan.setMediaBudget(new HashSet<Budget>());
        plan.getMediaBudget().add(budget);
        return plan;
    }

    @Override
    public void errorHandle(DataException ex) {
//        super.errorHandle(ex);
        System.out.println("未找到" + ((Object[])ex.getErrorObject())[0]);
    }

    public void setHotlineUtil(HotlineUtil hotlineUtil) {
        this.hotlineUtil = hotlineUtil;
    }
}
