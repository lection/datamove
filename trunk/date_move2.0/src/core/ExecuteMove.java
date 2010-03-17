package core;

import java.io.File;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ExecuteMove {
    private ApplicationContext context;
    public ExecuteMove(String... xmlFiles){
        try{
            if(fileValidate(xmlFiles)){
                context = new FileSystemXmlApplicationContext(xmlFiles);
            }else{
                context = new ClassPathXmlApplicationContext(xmlFiles);
            }
        }catch(BeansException b){
            b.printStackTrace();
        }
    }

    public void execute(){
        TaskList taskList = (TaskList)context.getBean("taskList");
        for(Task task:taskList.getTaskList()){
            task.execute();
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
