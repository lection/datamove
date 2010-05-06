/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import com.linkin.crm.um.model.InternalOrgImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import util.DBUtil.Query;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class OrgUtil {
    private Map<String,InternalOrgImpl> orgAliasMap = new HashMap<String, InternalOrgImpl>();
    private Map<Long,InternalOrgImpl> orgIdMap = new HashMap<Long, InternalOrgImpl>();
    private Map<Long,InternalOrgImpl> mpmapsIdMap = new HashMap<Long, InternalOrgImpl>();
    private String parent_name;
    private Connection targetConn;
    private Connection sourceConn;
    private InternalOrgImpl org = new InternalOrgImpl();

    public OrgUtil(Connection targetConn, Connection sourceConn,String parent_name) {
        this.targetConn = targetConn;
        this.sourceConn = sourceConn;
        this.parent_name = parent_name;
        DBUtil.executeQuery(targetConn,
                "select o2.c_id as id,o2.c_alias as name from t_organization o2 where" +
                " o2.c_parent_id in (select o3.c_id from t_organization o3 where o3.c_parent_id in (select o.c_id from t_organization as o where o.c_name='"+parent_name+"'))"
                , new Query(){
            public Object execute(ResultSet rs) throws SQLException{
                while(rs.next()){
                    InternalOrgImpl org = new InternalOrgImpl();
                    org.setId(rs.getLong("id"));
                    OrgUtil.this.orgAliasMap.put(rs.getString("name"), org);
//                    OrgUtil.this.org.setId(rs.getLong("pid"));
                    mpmapsIdMap.put(org.getId(), org);
                }return null;
            }
        });
        DBUtil.executeQuery(targetConn,
                "select o.c_id as pid from t_organization as o where o.c_name='"+parent_name+"'"
                , new Query(){
            public Object execute(ResultSet rs) throws SQLException{
                while(rs.next()){
                    OrgUtil.this.org.setId(rs.getLong("pid"));
                }return null;
            }
        });
        
        DBUtil.executeQuery(sourceConn,
                "select org.id,org.dcenter_name from scae_dcenter org"
                , new Query(){
            public Object execute(ResultSet rs) throws SQLException{
                while(rs.next()){
                    OrgUtil.this.orgIdMap.put(rs.getLong("id")
                            , OrgUtil.this.orgAliasMap.get(rs.getString("dcenter_name")));
                }return null;
            }
        });
        DBUtil.close(targetConn);
        DBUtil.close(sourceConn);
    }

    public InternalOrgImpl getOrg(String name){
        return orgAliasMap.get(name);
    }

    public InternalOrgImpl getOrg(Long id){
        return orgIdMap.get(id);
    }

    public InternalOrgImpl getMOrg(Long id){
        return mpmapsIdMap.get(id);
    }

    public InternalOrgImpl getParentOrg(){
        return org;
    }
}
