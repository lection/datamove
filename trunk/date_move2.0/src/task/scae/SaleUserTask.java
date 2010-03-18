/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.core.model.Organization;
import com.linkin.crm.core.model.Tenent;
import com.linkin.crm.um.model.UserImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.DBUtil;
import util.DBUtil.Query;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class SaleUserTask extends J2JTaskSupport{
    private static final Log log = LogFactory.getLog(SaleUserTask.class);
    private Tenent tenent;
    private Map<String,Organization> orgMap = new HashMap<String, Organization>();
    private HanyuPinyinOutputFormat format;
    private Long parent_id = null;
    private String parent_name;
    private Date date1;
    private Date date2;

    public SaleUserTask() {
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        date1 = java.sql.Date.valueOf("2007-1-1");
        date2 = java.sql.Date.valueOf("2007-1-2");
    }

    @Override
    public void init() {
        super.init();
        DBUtil.executeQuery(getTargetConn(), 
                "select t.c_id as id from t_tenent t,t_organization o,t_internal_org io where o.c_name='" + parent_name
                + "' and o.c_id=io.c_org_id and io.c_tenent_id=t.c_id"
                , new Query(){
            public Object execute(ResultSet rs) throws SQLException{
                rs.next();
                SaleUserTask.this.tenent = new Tenent();
                SaleUserTask.this.tenent.setId(rs.getLong("id"));
                return null;
            }
        });
        DBUtil.executeQuery(getTargetConn(),
                "select o2.c_id as id,o2.c_alias as name from t_organization o,t_organization o2 where o.c_name='" + parent_name
                + "' and o.c_id=o2.c_parent_id"
                , new Query(){
            public Object execute(ResultSet rs) throws SQLException{
                while(rs.next()){
                    Organization org = new Organization();
                    org.setId(rs.getLong("id"));
                    SaleUserTask.this.orgMap.put(rs.getString("name"), org);
                }return null;
            }
        });
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException {
        UserImpl user = new UserImpl();
        try {
            System.out.println(rs.getLong("id") + "\t" + orgMap.get(rs.getString("dcenter_name")));
        } catch (SQLException ex) {
            throw new DataException(user);
        }
        return user;
    }
    
    @Override
    public void errorHandle(DataException ex) {
        UserImpl user = (UserImpl)ex.getErrorObject();
        log.warn(ex.getMessage() + "\t" + user.getLastname());
    }

    private String getLoginName(UserImpl user,long id){
        StringBuilder sb = new StringBuilder();
        sb.append(user.getIntOrg().getId());
        try{
            for(char c:user.getLastname().toCharArray()){
                sb.append(PinyinHelper.toHanyuPinyinStringArray(c, format)[0].charAt(0));
            }
        }catch(Exception ex){
            System.out.println(user.getLastname());
        }
        sb.append(id);
        return sb.toString();
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }
}
