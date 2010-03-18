/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.adapter;

import core.Task;
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
            while((object = this.readIn()) != null){
                try{
                    this.store(this.parse(object));
                }catch(DataException ex){
                    this.errorHandle(ex);
                }catch(Exception ex){
                    System.out.println("系统抛出不可处理的异常：");
                    log.error("系统出现不可处理的异常");
                    log.error(ex.getStackTrace());
                    throw new RuntimeException(ex);
                }
            }
            this.destory();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    public abstract Object readIn();
    public abstract Object parse(Object object) throws DataException;
    public abstract void store(Object object) throws Exception;
    public abstract void afterStore() throws Exception;
    public abstract void errorHandle(DataException ex);
    public abstract void init() throws Exception;
    public abstract void destory() throws Exception;
}
