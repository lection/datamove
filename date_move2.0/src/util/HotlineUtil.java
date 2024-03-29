/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import com.linkin.crm.core.model.Hotline;
import com.linkin.crm.um.model.InternalOrgImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class HotlineUtil {
    private Map<String,List<Hotline>> hotlineMap = new HashMap<String,List<Hotline>>();
    private Map<Long,Hotline> parentMap = new HashMap<Long,Hotline>();
    public HotlineUtil(Connection targetConn){
        DBUtil.executeQuery(targetConn, "select h.c_id,h.c_hotline,h.c_start_date,h.c_end_date,h.c_org_id,h.c_parent_id " +
                "from t_hotline h where h.c_ext_str1='2' and h.c_start_date is not null", new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                Hotline hotline = null;
                Map<Long,InternalOrgImpl> orgMap = new HashMap<Long, InternalOrgImpl>();
                InternalOrgImpl org = null;
                List<Hotline> list = null;
                long org_id = -1;
                while(rs.next()){
                    hotline = new Hotline();
                    hotline.setId(rs.getLong("c_id"));
                    hotline.setHotline(rs.getString("c_hotline"));
                    hotline.setStartDate(rs.getDate("c_start_date"));
                    hotline.setEndDate(rs.getDate("c_end_date"));
                    Hotline parent_hotline = new Hotline();
                    parent_hotline.setId(rs.getLong("c_parent_id"));
                    hotline.setParent(parent_hotline);
                    parentMap.put(parent_hotline.getId(), parent_hotline);
                    
                    if(hotline.getEndDate() != null){
                        hotline.setEndDate(new java.util.Date(hotline.getEndDate().getTime()));
                        hotline.getEndDate().setHours(23);
                        hotline.getEndDate().setMinutes(59);
                        hotline.getEndDate().setSeconds(59);
                    }
                    org_id = rs.getLong("c_org_id");
                    org = orgMap.get(org_id);
                    if(org == null){
                        org = new InternalOrgImpl();
                        org.setId(org_id);
                        orgMap.put(org_id, org);
                    }
                    hotline.setOrg(org);
                    if(!hotlineMap.containsKey(hotline.getHotline())){
                        hotlineMap.put(hotline.getHotline(), new ArrayList<Hotline>());
                    }
                    list = hotlineMap.get(hotline.getHotline());
                    list.add(hotline);
                }
                return null;
            }
        });
        DBUtil.executeQuery(targetConn, "select h.c_id,h.c_hotline " +
                "from t_hotline h where h.c_ext_str1='1'", new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                Long id = null;
                while(rs.next()){
                    id = rs.getLong("c_id");
                    try{
                    parentMap.get(id).setHotline(rs.getString("c_hotline"));
                    }catch(NullPointerException ne){

                    }
                }
                return null;
            }
        });
        DBUtil.close(targetConn);
    }

    public Hotline getHotline(String hotline,Date date){
        Hotline result = null;
        if(hotlineMap.containsKey(hotline)){
            for(Hotline line:hotlineMap.get(hotline)){
                try{
                if(date.after(line.getStartDate()) && (line.getEndDate()==null||date.before(line.getEndDate()))){
                    result = line;
                    break;
                }
                }catch(NullPointerException e){
                    System.out.println(hotline+"\t"+hotlineMap.get(hotline).size()+"\t"+line
                            +"\t"+date+"\t"+line.getStartDate()+"\t"+line.getEndDate());
                    throw new NullPointerException(e.getMessage());
                }
            }
        }
        return result;
    }

    public boolean containsHotline(String hotline){
        return hotlineMap.containsKey(hotline);
    }
}
