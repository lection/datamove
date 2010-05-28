/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.honda;

import com.linkin.crm.campaign.model.Budget;
import com.linkin.crm.campaign.model.Campaign;
import com.linkin.crm.campaign.model.ExpandDevelop;
import com.linkin.crm.product.model.ProductCategory;
import com.linkin.crm.um.model.InternalOrgImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class CampaignTask extends J2JTaskSupport{
//    private OrgUtil orgUtil;
//    private ProductCategoryUtil proUtil;
    private InternalOrgImpl intOrg;
    private Map<Long,Long> proCatIdMap;
    private Map<Long,ProductCategory> proMap = new HashMap<Long, ProductCategory>();
    private Campaign cam = null;
    private String idBak = null;

    @Override
    public void init() throws Exception {
        super.init();
        ProductCategory pro = null;
        for(Long id:proCatIdMap.keySet()){
            pro = new ProductCategory();
            pro.setId(proCatIdMap.get(id));
            proMap.put(id, pro);
        }
    }


    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        String id = null;
        try{
            id = rs.getString("id");
            if(cam == null || !id.equals(idBak)){
                cam = new Campaign();
                cam.setUserId(1);//用户ID
                cam.setName(rs.getString("campaign_name"));//活动名称
                cam.setOrg(intOrg);//组织
                //起止时间
                cam.setStartDate(rs.getDate("start_date"));cam.setEndDate(rs.getDate("end_date"));
                cam.setCampLocation("O");cam.setCampAddress("未收集");//活动场所--其他 信息是 未收集
                cam.setCampType("E");//活动类型-外拓开发
                cam.setTotal(0);//总预算
                cam.setExpand(0);//外拓明细预算
                cam.setBudgetType("single");//预算分配类型 单月
                //添加0元单月预算
                Budget budget = new Budget();
                budget.setYear(cam.getStartDate().getYear() + 1900);
                budget.setMonth(cam.getStartDate().getMonth() + 1);
                budget.setCampaign(cam);
                budget.setMoney(0);
                Set<Budget> bset = new HashSet<Budget>();
                bset.add(budget);
                cam.setSetBudget(bset);
                idBak = id;//备份id
            }
            cam.setSet(null);//每次储存ExpandDevelop时，清空原有的set
            ExpandDevelop ed = new ExpandDevelop();
            ed.setTarget(rs.getInt("obj"));
            ed.setProductCategory(proMap.get(rs.getLong("carmodel_id")));
            if(ed.getProductCategory() != null){
                cam.setSet(new HashSet<ExpandDevelop>());
                cam.getSet().add(ed);//为了能够方便储存，使用了并不好的方法。每次返回的set都只有一个值
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return cam;
    }

    public void setIntOrg(InternalOrgImpl intOrg) {
        this.intOrg = intOrg;
    }

    public void setProCatIdMap(Map<Long, Long> proCatIdMap) {
        this.proCatIdMap = proCatIdMap;
    }

//    public void setOrgUtil(OrgUtil orgUtil) {
//        this.orgUtil = orgUtil;
//    }

//    public void setProUtil(ProductCategoryUtil proUtil) {
//        this.proUtil = proUtil;
//    }

}
