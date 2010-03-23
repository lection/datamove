/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.sales.model.ContactRecord;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import util.DBUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class FailTask extends J2JTaskSupport{
    
    private Map<String,ContactRecord> recordMap = new HashMap<String,ContactRecord>(300000);
    private Map<String,String> rejectReason = new HashMap<String,String>(16);
    private PreparedStatement pre;

    {
        rejectReason.put("B", "149");
        rejectReason.put("BR", "150");
        rejectReason.put("ON", "150");
        rejectReason.put("ST", "150");
        rejectReason.put("S", "151");
        rejectReason.put("F", "151");
        rejectReason.put("AB", "151");
        rejectReason.put("N", "151");
        rejectReason.put("LI", "151");
        rejectReason.put("M", "151");
        rejectReason.put("A", "151");
        rejectReason.put("OT", "153");
        rejectReason.put("NC", "152");
    }

    @Override
    public void init() throws Exception {
        super.init();
        pre = getTargetConn().prepareStatement(
                "update t_contactrecord set c_rejection_reason=?,c_rejection_factors=?,c_comp_brand=?,c_comp_car=?,c_ext_dt1=? where c_id=?");
        DBUtil.executeQuery(getTargetConn(), "select r.c_id,r.c_ext_str1 from t_contactrecord as r where c_ext_str1 like 'f%'"
        , new DBUtil.Query() {
            public Object execute(ResultSet rs) throws SQLException {
                ContactRecord record = null;
                while(rs.next()){
                    record = new ContactRecord();
                    record.setId(rs.getLong("c_id"));
                    record.setP_ext_str1(rs.getString("c_ext_str1"));
                    recordMap.put(record.getP_ext_str1().replace("f", ""), record);
                }
            return null;}});
    }

    @Override
    public void destory() throws Exception {
        if(pre!=null)pre.close();
        super.destory();
    }

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        final ContactRecord record = recordMap.get(rs.getString("yellowcard_id"));
        if(record==null)throw new DataException(rs.getString("yellowcard_id"));
        record.setRejectionReason(rejectReason.get(rs.getString("fact")));
        if(record.getRejectionReason().equals("149")){
            record.setRejectionFactors("96");
            record.setCptrBrand(rs.getString("manu"));
            record.setCptrCar("未收集");
        }
        record.setP_ext_dt1(rs.getDate("rldate"));//战败日期
        return record;
    }

    @Override
    public void store(Object object) throws SQLException {
        ContactRecord record = (ContactRecord) object;
        pre.setString(1, record.getRejectionReason());
        if (record.getRejectionReason().equals("149")) {
            pre.setString(2, record.getRejectionFactors());
            pre.setString(3, record.getCptrBrand());
            pre.setString(4, record.getCptrCar());
        } else {
            pre.setString(2, null);
            pre.setString(3, null);
            pre.setString(4, null);
        }
        pre.setDate(5, new java.sql.Date(record.getP_ext_dt1().getTime()));
        pre.setLong(6, record.getId());
        pre.execute();
    }

    @Override
    public void errorHandle(DataException ex) {System.out.println("未找到\t" + ex.getErrorObject());}

}
