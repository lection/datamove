/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.sql.*;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class DBUtil {
    public static interface Query{
        Object execute(ResultSet rs) throws SQLException;
    }
    public static Object executeQuery(Connection conn,String sql,Query query){
        Object result = null;
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            result = query.execute(rs);
            if (rs != null) {
                rs.close();
            }
            if (stat != null) {
                stat.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    public static void close(Connection conn){
        try {
            if (conn != null && conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
