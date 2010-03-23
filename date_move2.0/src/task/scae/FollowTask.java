/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.sales.model.ContactRecord;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import util.DBUtil;
import util.OrgUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class FollowTask extends J2JTaskSupport{
    private ContactRecord recordBak;
    private OrgUtil orgUtil;

    private Map<String,Integer> levelMap = new HashMap<String,Integer>(16);

    private int sequence = 1;
    private String id_backup = null;
    private Date backupDate = null;

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        ContactRecord record = new ContactRecord();
        String lead_id = rs.getString("yellowcard_id");
        if(!lead_id.equals(id_backup)){
            sequence = 1;
//            DBUtil.executeUpdate(getTargetConn(), "update ");
            backupDate = null;
            recordBak = null;
        }
        record.setSequence(sequence++);
        id_backup = lead_id;//备份上次的lead id
        record.setCurlevel(levelMap.get(rs.getString("ycclass")).toString());//跟进级别
        record.setContact(getContact(record.getLeads(), session));//跟进的联系人 contact
        record.setCustomer(getCustomer(record.getLeads(), session));//跟进的 客户
        return record;
    }

    @Override
    public void init() throws Exception {
        super.init();
        DBUtil.executeQuery(getTargetConn(), "select c_code,c_id from t_biz_vo_category where c_org_id="
                 +orgUtil.getParentOrg().getId() + " and c_type='ycclass'"
         , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                while(rs.next())
                    levelMap.put(rs.getString("c_code"),rs.getInt("c_id"));
                levelMap.put("B", levelMap.get("A"));
                levelMap.put("C", levelMap.get("A"));
            return null;}});
    }

    public void setOrgUtil(OrgUtil orgUtil) {
        this.orgUtil = orgUtil;
    }
}
