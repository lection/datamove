/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package saver;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class Saver implements util.saver.Saver{

    private Connection targetConn;

    public void save(Object object) throws SQLException {}

    public void init() throws SQLException {
        targetConn.setAutoCommit(false);
    }

    public void destory() throws SQLException {
        if (this.targetConn != null && !this.targetConn.isClosed()) {
            if (!targetConn.getAutoCommit()) {
                this.targetConn.commit();
            }
            this.targetConn.close();
        }
    }

    public Connection getConn() {
        return targetConn;
    }

    public Object getResource() {
        return null;
    }

    public Connection getTargetConn() {
        return targetConn;
    }

    public void setTargetConn(Connection targetConn) {
        this.targetConn = targetConn;
    }

}
