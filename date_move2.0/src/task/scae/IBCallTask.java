/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.comm.model.IBCall;
import com.linkin.crm.core.model.Hotline;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.HotlineUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class IBCallTask extends J2JTaskSupport{
    private HotlineUtil hotlineUtil;
    
    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        IBCall ibcall = new IBCall();
        ibcall.setCallno(rs.getString("callno"));
        ibcall.setStartTime(rs.getTimestamp("ibtime"));
        Hotline hotline = hotlineUtil.getHotline(ibcall.getCallno(), ibcall.getStartTime());
        if(hotline == null)throw new DataException(null);
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
        return ibcall;
    }

    private String getDurDisplay(int dur){
        return dur/3600+"时"+(dur%3600)/60+"分"+(dur%60)+"秒";
    }

    @Override
    public void errorHandle(DataException ex) {}

    public void setHotlineUtil(HotlineUtil hotlineUtil) {
        this.hotlineUtil = hotlineUtil;
    }

}
