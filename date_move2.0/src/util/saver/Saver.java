/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.saver;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public interface Saver {
    Object save(Object object) throws SQLException;
    void init() throws SQLException;
    void destory() throws SQLException;
    Connection getConn();
    Object getResource();
}
