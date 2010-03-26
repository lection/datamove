/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.adapter;

import com.sun.java_cup.internal.production;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.saver.Saver;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public abstract class J2JTaskSupport extends SimpleTask{
    private Connection sourceConn;
    private Statement sourceStmt;
    private ResultSet sourceRs;
    private String sql;
    private Saver saver;
    private Log log;
    private Long source_id;
    private Long target_id;

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
    public Object parse(Object object) throws DataException,SQLException {
        return this.parse(saver.getConn(),sourceRs);
    }

    public abstract Object parse(Connection conn,ResultSet rs) throws DataException,SQLException;

    @Override
    public void store(Object object) throws SQLException {
        target_id  = (Long)saver.save(object);
    }

    @Override
    public void errorHandle(DataException ex) {
    }

    @Override
    public void init() throws Exception{
        try {
            saver.init();
            log = LogFactory.getLog(this.getClass());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void destory() throws Exception{
        try{
            saver.destory();
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

    @Override
    public void afterStore() throws Exception {
        if(source_id!=null&&target_id!=null)
            log.info(source_id + "\t" + target_id);
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
        return saver.getConn();
    }

    public void setSource_id(Long source_id) {
        this.source_id = source_id;
    }
}
