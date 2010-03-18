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
    private Connection targetConn;
    private PreparedStatement preStat;

    private int count = 0;

    public abstract String getInsertSql();

    public abstract void cap(Object object) throws SQLException;

    public void save(Object object) throws SQLException {
        cap(object);
        this.preStat.executeUpdate();
//        this.preStat.addBatch();
//        count++;
//        if(count > 20){
//            this.preStat.executeBatch();
//            count = 0;
//        }
    }

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
    }

    public Connection getTargetConn() {
        return targetConn;
    }

    public void setTargetConn(Connection targetConn) {
        this.targetConn = targetConn;
    }

    public PreparedStatement getPreStat() {
        return preStat;
    }

    private void updateHi() throws SQLException{
        Statement statement = targetConn.createStatement();
        ResultSet rs = statement.executeQuery("select " + columnName + " from " + tableName);
        if(rs.next()){
            hi = rs.getInt(1);
        }
        else hi = 1;
        if(hi<1)hi=1;
        rs.close();
        statement.executeUpdate("update " + tableName + " set " + columnName + "=" + (hi+1));
        statement.close();
    }

    public void destory() throws SQLException {
        if(this.preStat!=null){
//            this.preStat.executeBatch();
            this.preStat.close();
        }
        if(this.targetConn!=null&&!this.targetConn.isClosed()){
            if(!targetConn.getAutoCommit())
                this.targetConn.commit();
            this.targetConn.close();
        }
    }

    public void init() throws SQLException {
        targetConn.setAutoCommit(false);
        if(tableName!=null){
            updateHi();
        }
        this.preStat = targetConn.prepareStatement(this.getInsertSql(),Statement.RETURN_GENERATED_KEYS);
    }

    public Connection getConn() {
        return this.targetConn;
    }

    public Object getResource() {return this.preStat;}
}
