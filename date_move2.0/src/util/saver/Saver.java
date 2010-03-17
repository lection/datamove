/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.saver;

import java.sql.SQLException;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public interface Saver {
    void save(Object object) throws SQLException;
}
