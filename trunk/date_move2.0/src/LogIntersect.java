
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class LogIntersect {
    public static void main(String[] args){
        try{
            File file = new File("D:\\work\\dm_id_mapping\\data_migration.log");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            short b = 0;
            PrintWriter pw = null;
            while((s=br.readLine())!=null){
                if(b==0){
                    if(s.matches(".*开始启动")){
                        File f = new File(file.getParent(),s.replace("任务", "").replace("开始启动", "")+".log");
                        f.createNewFile();
                        pw = new PrintWriter(f);
                        b = 1;
                    }
                }else if(b==1){
                    if(s.matches(".*条记录")){
                        pw.flush();pw.close();b=0;
                    }else{
                        pw.println(s);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
