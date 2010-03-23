package core;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ExecuteMove {
    private static final Log log = LogFactory.getLog(ExecuteMove.class);
    private ApplicationContext context;
    public ExecuteMove(String... xmlFiles){
        try{
            if(fileValidate(xmlFiles)){
                context = new FileSystemXmlApplicationContext(xmlFiles);
            }else{
                context = new ClassPathXmlApplicationContext(xmlFiles);
            }
            log.info("数据迁移程序配置信息加载成功");
        }catch(BeansException b){
            b.printStackTrace();
        }
    }

    public void execute(){
        TaskList taskList = (TaskList)context.getBean("taskList");
        long time = -1;
        for(Task task:taskList.getTaskList()){
            log.info("任务" + task.getClass().getSimpleName() + "开始启动");
            time = System.currentTimeMillis();
            task.execute();
            time = System.currentTimeMillis() - time;
            log.info(task.getClass().getSimpleName() + "执行完毕.耗时  " + (int)(time/3600000) + "时 "
                    + (int)((time%3600000)/60000) + "分 " + (int)((time%60000)/1000) + "秒");
        }
    }

    public Object getBean(String key){
        return context.getBean(key);
    }

    //验证文件是否存在
    private boolean fileValidate(String[] strs){
        for(String file:strs){
            if(!new File(file).exists()){
                return false;
            }
        }
        return true;
    }
}
