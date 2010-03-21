/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.sales.model.Leads;
import com.linkin.crm.um.model.UserImpl;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.DBUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class LeadsTask extends J2JTaskSupport{

    private Map<Long, UserImpl> salesMap = new HashMap<Long, UserImpl>(700);

    @Override
    public void init() throws Exception {
        super.init();
        DBUtil.executeQuery(getTargetConn(), "select u.c_id from t_um_user u where u.c_ext_str1 like 'scae%'"
        , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                UserImpl user = null;
                while(rs.next()){
                    user = new UserImpl();
                    user.setId(rs.getLong("c_id"));
                    LeadsTask.this.salesMap.put(user.getId(), user);
                }
                return null;
            }
        });
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        Leads lead = new Leads();
        //特殊标示字段
        lead.setP_ext_str1(rs.getString("id"));
        lead.setP_ext_str2("scae");
        //销售顾问
        lead.setUser(salesMap.get(rs.getLong("dcentersales_id")));
        //建卡日期 状态
        lead.setRegisterDate(rs.getDate("contact_date"));
        lead.setStatus(rs.getString("status").toUpperCase());
        
        
        return lead;
    }

}
