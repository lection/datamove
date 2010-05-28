/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.honda;

import com.linkin.crm.core.model.Tenent;
import com.linkin.crm.um.model.InternalOrgImpl;
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
//    private Map<String,InternalOrgImpl> orgMap = new HashMap<String, InternalOrgImpl>();
    private InternalOrgImpl intOrg = new InternalOrgImpl();
    private HanyuPinyinOutputFormat format;
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
    public void init() throws Exception{
        super.init();
//        DBUtil.executeQuery(getTargetConn(),
//                "select t.c_id as id from t_tenent t,t_organization o,t_internal_org io where o.c_name='" + parent_name
//                + "' and o.c_id=io.c_org_id and io.c_tenent_id=t.c_id"
//                , new Query(){
//            public Object execute(ResultSet rs) throws SQLException{
//                rs.next();
                SaleUserTask.this.tenent = new Tenent();
                SaleUserTask.this.tenent.setId(45);
                intOrg.setId(265);
//                return null;
//            }
//        });
//        DBUtil.executeQuery(getTargetConn(),
//                "select o2.c_id as id,o2.c_alias as name from t_organization o,t_organization o2 where o.c_name='" + parent_name
//                + "' and o2.c_parent_id in (select o3.c_id as id_3 from t_organization as o3 where o3.c_parent_id=o.c_id)"
//                , new Query(){
//            public Object execute(ResultSet rs) throws SQLException{
//                while(rs.next()){
//                    InternalOrgImpl org = new InternalOrgImpl();
//                    org.setId(rs.getLong("id"));
//                    SaleUserTask.this.orgMap.put(rs.getString("name"), org);
//                }return null;
//            }
//        });
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException {
        UserImpl user = new UserImpl();
        try {
            setSource_id(rs.getLong("id"));//记录日志
            user.setLastname(rs.getString("sales_name"));
            user.setIntOrg(intOrg);
            user.setTenent(tenent);
            user.setPassword("bGMhKrSOhAHq9rWbldgWqQ==");
            user.setActiveStatus(1);
            user.setLogStatus(1);
            user.setLock(1);
            user.setInactiveDate(date1);
            user.setPasswordExpirationDate(date2);
            //user的loginName应当放在最后，因为这个属性依赖组织、用户id
            user.setLoginName(this.getLoginName(user,rs.getLong("id")));
            user.setEmail(user.getLoginName() + "@linkinways.com");
            user.setCphone1("");
            //特殊字段，记录user在scae系统中的id,解决重名用户的迁移问题
            user.setP_ext_str1("honda" + rs.getLong("id"));
            //lead表迁移时，根据顾问名+组织号，解决重名问题
            user.setP_ext_str2(rs.getString("sales_name")+rs.getString("org_id"));
//            System.out.println(user.getLastname());
        } catch (SQLException ex) {
            ex.printStackTrace();
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
