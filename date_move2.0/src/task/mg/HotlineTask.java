/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.mg;

import com.linkin.crm.core.model.Hotline;
import com.linkin.crm.um.model.InternalOrgImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import util.DBUtil;
import util.DBUtil.Query;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class HotlineTask extends J2JTaskSupport{
    private Map<Long,InternalOrgImpl> orgMap = new HashMap<Long, InternalOrgImpl>();

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        Hotline hotline = new Hotline();
        hotline.setHotline(rs.getString("hotline"));
        hotline.setOrg(orgMap.get(rs.getLong("org_id")));
//        if("active".equals(rs.getString("status"))){
            hotline.setVisible("1");
//            hotline.setStatus("1");
//        }else{
//            hotline.setVisible("0");
            hotline.setStatus("0");
//        }
        hotline.setStartDate(rs.getDate("create_date"));
        hotline.setCreatedBy(rs.getString("created_by")+" data_m");
        Date endDate = rs.getDate("update_date");
        if(endDate == null)endDate = new Date();
        hotline.setUpdateDate(endDate);
        hotline.setEndDate(endDate);
        hotline.setUpdatedBy(rs.getString("updated_by"));
        hotline.setExt_str1("1");
        getSaver().save(hotline);
        Hotline hotline2 = new Hotline();
        long key = (Long)DBUtil.executeQuery(getTargetConn(), "select max(c_id) from t_hotline", new Query(){
            public Object execute(ResultSet rs) throws SQLException {
                rs.next();
                return rs.getLong(1);
            }
        });
        hotline2.setId(key);
        hotline.setHotline(rs.getString("mainline"));
        hotline.setExt_str1("2");
        hotline.setParent(hotline2);
        return hotline;
    }

    public void setOrgIdMap(Map orgIdMap) {
        for(Map.Entry entry:(Set<Entry>)orgIdMap.entrySet()){
            orgMap.put(Long.valueOf((String)entry.getKey()), new InternalOrgImpl());
            orgMap.get(Long.valueOf((String)entry.getKey())).setId(Long.valueOf((String)entry.getValue()));
        }
    }

}
