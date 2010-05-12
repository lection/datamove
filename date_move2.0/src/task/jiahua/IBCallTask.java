/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.jiahua;

import com.linkin.crm.comm.model.IBCall;
import com.linkin.crm.core.model.Hotline;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import util.DBUtil;
import util.HotlineUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class IBCallTask extends J2JTaskSupport{
    private HotlineUtil hotlineUtil;
//    private OrgUtil orgUtil;
    private Map<String,String> orgIdMap;
    private String orgs;
    private Set<Long> set = new HashSet<Long>();

    @Override
    public void init() throws Exception {
        super.init();
        for(String s:orgs.split(",")){
            set.add(Long.valueOf(orgIdMap.get(s)));
        }
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        setSource_id(rs.getLong("id"));//记录日志
        IBCall ibcall = new IBCall();
        ibcall.setCallno(rs.getString("callno"));
        ibcall.setStartTime(rs.getTimestamp("ibtime"));
        Hotline hotline = hotlineUtil.getHotline(ibcall.getCallno(), ibcall.getStartTime());
        if(hotline == null || !set.contains(hotline.getOrg().getId()))throw new DataException(null);
        ibcall.setCallno(hotline.getParent().getHotline());//翻译话单内容，挂在热线号码下
        ibcall.setOrg(hotline.getOrg());
        ibcall.setIbno(rs.getString("ibno"));
        ibcall.setEndTime(rs.getTimestamp("endtime"));
        ibcall.setDur(rs.getInt("dur"));
        ibcall.setPick(rs.getString("pick"));
        ibcall.setRec(rs.getString("rec"));
        ibcall.setDurDisplay(getDurDisplay(ibcall.getDur()));
        ibcall.setCreatedBy(rs.getString("created_by"));
        ibcall.setCreatedDate(rs.getDate("create_date"));
        ibcall.setVisible(true);
        ibcall.setId(DBUtil.getLaskKey(getTargetConn(), "t_ibcall"));
        return ibcall;
    }

    public static String getDurDisplay(int dur){
        return dur/3600+"时"+(dur%3600)/60+"分"+(dur%60)+"秒";
    }

    @Override
    public void errorHandle(DataException ex) {}

    public void setHotlineUtil(HotlineUtil hotlineUtil) {
        this.hotlineUtil = hotlineUtil;
    }

//    public void setOrgUtil(OrgUtil orgUtil) {
//        this.orgUtil = orgUtil;
//    }

    public void setOrgs(String orgs) {
        this.orgs = orgs;
    }

    public void setOrgIdMap(Map<String, String> orgIdMap) {
        this.orgIdMap = orgIdMap;
    }

}
