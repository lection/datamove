/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.comm.model.IBCall;
import com.linkin.crm.core.model.Hotline;
import com.linkin.crm.um.model.InternalOrgImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import util.DBUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class IBCall_temp_51_54 extends J2JTaskSupport {
    public Map<String,Hotline> hotlineMap = new HashMap<String, Hotline>();

    @Override
    public void init() throws Exception {
        super.init();
        Hotline l1 = new Hotline();
        Hotline l2 = new Hotline();
        InternalOrgImpl o1 = new InternalOrgImpl();
        l1.setId(1347);
        l2.setId(1342);
        o1.setId(35);
        l1.setOrg(o1);
        l2.setOrg(o1);
        hotlineMap.put("61501366", l1);
        hotlineMap.put("61501388", l2);
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        IBCall ibcall = new IBCall();
        ibcall.setCallno(rs.getString("callno"));
        ibcall.setStartTime(rs.getTimestamp("ibtime"));
        Hotline hotline = hotlineMap.get(ibcall.getCallno());
//        if(hotline == null || !set.contains(hotline.getOrg().getId()))throw new DataException(null);
        ibcall.setOrg(hotline.getOrg());
        ibcall.setIbno(rs.getString("ibno"));
        ibcall.setEndTime(rs.getTimestamp("endtime"));
        ibcall.setDur(rs.getInt("dur"));
        ibcall.setPick(rs.getString("pick"));
        ibcall.setRec(rs.getString("rec"));
        ibcall.setDurDisplay(IBCallTask.getDurDisplay(ibcall.getDur()));
        ibcall.setCreatedBy(rs.getString("created_by"));
        ibcall.setCreatedDate(rs.getDate("create_date"));
        ibcall.setVisible(true);
        ibcall.setId(DBUtil.getLaskKey(getTargetConn(), "t_ibcall"));
        return ibcall;
    }

}
