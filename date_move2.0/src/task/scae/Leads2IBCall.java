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
    private long leads_id;
    private Connection targetConn;

    private int suc = 0;
    private int fail = 0;

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
                    date_str  =date.getYear() + "-" + date.getMonth() + "-" + date.getDate();
                    ibno = modifyNo(rs.getString("c_ibno"));
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
        return null;}});
        DBUtil.executeQuery(targetConn, 
        "select l.c_id,l.c_register_date,u.c_org_id,p.c_phone1,p.c_sphone1_ac,p.c_sphone1,p.c_phone2,p.c_sphone2_ac,p.c_sphone2 "
            +"from t_leads as l,t_person as p,t_um_user as u where l.c_user_id=c_person_id and l.c_contact_id=p.c_id and l.c_id>"+leads_id
        , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                Long org_id = null;
                Date date = null;
                String date_str = null;
                String phone = null;
                List<IBCall> list = null;
                while(rs.next()){
                    org_id = rs.getLong("c_org_id");
                    date = rs.getDate("c_register_date");
                    date_str = date.getYear()+"-"+date.getMonth()+"-"+date.getDate();
                    Map<String,List<IBCall>> map = null;
                    if(ibcallMap.containsKey(org_id)){
                        if(ibcallMap.get(org_id).containsKey(date_str)){
                            map = ibcallMap.get(org_id).get(date_str);
                            if(rs.getString("c_phone1")!=null){
                                phone = rs.getString("c_phone1");
                            }else{
                                phone = rs.getString("c_sphone1_ac")+rs.getString("c_sphone1");
                            }
                            if(map.containsKey(phone)){
                                list = map.get(phone);
                            }
                            else{
                                if(rs.getString("c_phone2")!=null){
                                    phone = rs.getString("c_phone2");
                                }else{
                                    phone = rs.getString("c_sphone2_ac")+rs.getString("c_sphone2");
                                }
                                list = map.get(phone);
                            }
                            if(list == null){
                                fail++;
                                continue;
                            }
                            else{
                                suc++;
                            }
                        }
                    }
                }
        return null;}});
        System.out.println("成功 " + suc +"  失败 " + fail);
    }

    private String modifyNo(String no){
        if(no.matches("\\d{8}")){
            no = "021"+no;
        }else if(no.matches("0\\d{8}")){
            no = "021"+no.substring(1);
        }
        return no;
    }

    public void setIbcall_id(long ibcall_id) {
        this.ibcall_id = ibcall_id;
    }

    public void setTargetConn(Connection targetConn) {
        this.targetConn = targetConn;
    }

    public void setLeads_id(long leads_id) {
        this.leads_id = leads_id;
    }
}
