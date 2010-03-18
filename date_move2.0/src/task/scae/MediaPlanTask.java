/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class MediaPlanTask extends J2JTaskSupport{

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException, SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
