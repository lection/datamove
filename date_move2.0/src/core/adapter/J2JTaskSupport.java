/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.adapter;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.saver.Saver;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public abstract class J2JTaskSupport extends SimpleTask{
    private static final Log log = LogFactory.getLog(J2JTaskSupport.class);
    private Connection sourceConn;
    private Connection targetConn;
    private Statement sourceStmt;
    private ResultSet sourceRs;
    private String sql;
    private Saver saver;

    @Override
    public Object readIn() {
        try {
            if(sourceRs == null){
                sourceStmt = sourceConn.createStatement();
                sourceRs = sourceStmt.executeQuery(sql);
                System.out.println("本次查找，结果集结构:");
                ResultSetMetaData rsm = sourceRs.getMetaData();
                System.out.println("共" + rsm.getColumnCount() + "列");
                for(int i=1;i<=rsm.getColumnCount();i++){
                    System.out.print(rsm.getColumnLabel(i) + "   ");
                }
                System.out.println();
                if(sql == null || sourceConn == null){
                    throw new RuntimeException(this.getClass() + "  Sql与Conn都不能为空");
                }
            }
            if(sourceRs.next()){
                return sourceRs;
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Object parse(Object object) throws DataException {
        return this.parse(targetConn,sourceRs);
    }

    public abstract Object parse(Connection conn,ResultSet rs) throws DataException;

    @Override
    public void store(Object object) throws SQLException {
//        saver.save(object);
    }

    @Override
    public void errorHandle(DataException ex) {
        log.error(ex.getErrorObject());
    }

    @Override
    public void destory() {
        try{
            if(sourceRs!=null)
                sourceRs.close();
            if(sourceStmt!=null)
                sourceStmt.close();
            if(sourceConn!=null && sourceConn.isClosed())
                sourceConn.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public Saver getSaver() {
        return saver;
    }

    public void setSaver(Saver saver) {
        this.saver = saver;
    }

    public Connection getSourceConn() {
        return sourceConn;
    }

    public void setSourceConn(Connection sourceConn) {
        this.sourceConn = sourceConn;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Connection getTargetConn() {
        return targetConn;
    }

    public void setTargetConn(Connection targetConn) {
        this.targetConn = targetConn;
    }

}
