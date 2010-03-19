/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.adapter;

import core.Task;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public abstract class SimpleTask implements Task{
    private static final Log log = LogFactory.getLog(Task.class);
    
    public void execute() {
        Object object = null;
        try {
            this.init();
            int count = 0;
            int fail = 0;
            while((object = this.readIn()) != null){
                try{
                    this.store(this.parse(object));
                    count ++;
                }catch(DataException ex){
                    this.errorHandle(ex);
                    fail++;
                }catch(Exception ex){
                    System.out.println("系统抛出不可处理的异常：");
                    log.error("系统出现不可处理的异常");
                    log.error(ex.getStackTrace());
                    throw new RuntimeException(ex);
                }
            }
            log.info(this.getClass().getSimpleName() + " 共执行 " + count +" 条记录  失败 " + fail + " 条记录");
            this.destory();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    public abstract Object readIn();
    public abstract Object parse(Object object) throws DataException,SQLException;
    public abstract void store(Object object) throws Exception;
    public abstract void afterStore() throws Exception;
    public abstract void errorHandle(DataException ex);
    public abstract void init() throws Exception;
    public abstract void destory() throws Exception;
}
