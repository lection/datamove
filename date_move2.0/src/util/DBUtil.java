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

    public static interface Excute{
        void execute(PreparedStatement pre) throws SQLException;
    }

    public static void executeUpdate(Connection conn,String sql,Excute e){
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            e.execute(stat);
            stat.executeUpdate();
            if(stat != null){
                stat.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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

    public static void delete(Connection conn,String table,Long id) throws SQLException{
        delete(conn, table, "c_id",id);
    }

    public static void delete(Connection conn,String table,String column,Long id) throws SQLException{
        Statement stat = conn.createStatement();
        stat.executeUpdate("delete from " + table + " where " + column + "=" + id);
        if(stat!=null)stat.close();
    }

    public static long getLaskKey(Connection conn,String table){
        return getLaskKey(conn, table, "c_id");
    }

    public static long getLaskKey(Connection conn,String table,String column){
       return (Long)executeQuery(conn, "select max(" + column + ") from " + table, new Query(){
            public Object execute(ResultSet rs) throws SQLException {
                rs.next();
                return rs.getLong(1);
            }
        });
    }
}
