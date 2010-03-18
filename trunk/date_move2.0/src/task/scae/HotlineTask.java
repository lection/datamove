/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.core.model.Hotline;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Date;
import util.DBUtil;
import util.DBUtil.Query;
import util.OrgUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class HotlineTask extends J2JTaskSupport{
    private OrgUtil orgUtil;

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException,SQLException {
        Hotline hotline = new Hotline();
        hotline.setHotline(rs.getString("hotline"));
        hotline.setOrg(orgUtil.getOrg(rs.getLong("dcenter_id")));
        if("active".equals(rs.getString("status"))){
            hotline.setVisible("1");
            hotline.setStatus("1");
        }else{
            hotline.setVisible("0");
            hotline.setStatus("0");
        }
        hotline.setStartDate(rs.getDate("create_date"));
        hotline.setCreatedBy(rs.getString("created_by"));
        Date endDate = rs.getDate("update_date");
        if(endDate == null)endDate = java.sql.Date.valueOf("2082-11-12");
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
        hotline.setExt_str1("2");
        hotline.setParent(hotline2);
        return hotline;
    }

    public OrgUtil getOrgUtil() {
        return orgUtil;
    }

    public void setOrgUtil(OrgUtil orgUtil) {
        this.orgUtil = orgUtil;
    }

}
