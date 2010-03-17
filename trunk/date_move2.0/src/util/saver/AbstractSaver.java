/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.saver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public abstract class AbstractSaver implements Saver{
    private int max_lo;
    private int hi;
    private int lo = 0;
    private String tableName;
    private String columnName;
    private Connection conn;
    private PreparedStatement preStat;

    public abstract String getInsertSql();

    protected long getHiloId() throws SQLException{
        long id = -1;
        if(lo > max_lo){
            updateHi();
            lo = 0;
        }
        id = hi*(max_lo + 1) + lo;
        lo++;
        return id;
    }

    public void initHilo(String tableName,String columnName,int max_lo) throws SQLException{
        this.tableName = tableName;
        this.columnName = columnName;
        this.max_lo = max_lo;
        updateHi();
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) throws SQLException {
        this.conn = conn;
        preStat = conn.prepareStatement(getInsertSql());
    }

    public PreparedStatement getPreStat() {
        return preStat;
    }

    private void updateHi() throws SQLException{
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select " + columnName + " from" + tableName);
        if(rs.next())hi = rs.getInt(0);
        else hi = 1;
        rs.close();
        statement.executeUpdate("update " + tableName + " set " + columnName + "=" + (hi+1));
        statement.close();
        conn.close();
    }
}
