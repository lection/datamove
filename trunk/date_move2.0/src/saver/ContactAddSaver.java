/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package saver;

import java.sql.SQLException;
import util.saver.AbstractSaver;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class ContactAddSaver extends AbstractSaver{

    @Override
    public String getInsertSql() {
        return "insert into t_contact_address (c_contact_id,c_addr_id) values(?,?)";
    }

    @Override
    public void cap(Object object) throws SQLException {
        Long[] ls = (Long[])object;
        getPreStat().setLong(1, ls[0]);
        getPreStat().setLong(2, ls[1]);
    }

}
