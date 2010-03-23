/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.comm.model.IBCall;
import core.Task;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.DBUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class Leads2IBCall implements Task{
    private Map<Long,Map<String,Map<String,List<IBCall>>>> ibcallMap =
            new HashMap<Long,Map<String,Map<String,List<IBCall>>>>();
    private long ibcall_id;
    private Connection targetConn;

    public void execute() {
        DBUtil.executeQuery(targetConn,"select c_id,c_org_id,c_ibno,c_starttime from t_ibcall where c_id>"+ibcall_id
        ,new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                IBCall ibcall = null;
                Long org_id = null;
                Date date = null;
                String date_str = null;
                String ibno = null;
                while(rs.next()){
                    ibcall = new IBCall();
                    ibcall.setId(rs.getLong("c_id"));
                    org_id = rs.getLong("c_org_id");
                    date = rs.getDate("c_starttime");
                    date_str  =date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                    ibno = rs.getString("c_ibno");
                    if(!ibcallMap.containsKey(org_id)){
                        ibcallMap.put(org_id, new HashMap<String, Map<String, List<IBCall>>>());
                    }
                    if(!ibcallMap.get(org_id).containsKey(date_str)){
                        ibcallMap.get(org_id).put(date_str, new HashMap<String, List<IBCall>>());
                    }
                    if(!ibcallMap.get(org_id).get(date_str).containsKey(ibno)){
                        ibcallMap.get(org_id).get(date_str).put(ibno, new ArrayList<IBCall>());
                    }
                    ibcallMap.get(org_id).get(date_str).get(ibno).add(ibcall);
                }
                for(Long id:ibcallMap.keySet()){
                    System.out.println("ID: " + id);
                }
        return null;}});
    }

    public void setIbcall_id(long ibcall_id) {
        this.ibcall_id = ibcall_id;
    }

    public void setTargetConn(Connection targetConn) {
        this.targetConn = targetConn;
    }
}
