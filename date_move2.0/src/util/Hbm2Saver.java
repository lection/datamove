/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.saver.SaverHandler;
import util.saver.SaverHandlerImpl;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class Hbm2Saver {
    private String sourcePath;
    private String targetPath;
    private String template;
    private SaverHandler handler;

    public Hbm2Saver() {}

    public Hbm2Saver(String sourcePath, String targetPath, String template, SaverHandler handler) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.template = template;
        this.handler = handler;
    }

    public void build() throws IOException{
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        if(sourceFile.isDirectory()&&targetFile.isDirectory()
                ||targetFile.isFile()&&targetFile.isFile()){
                if(sourceFile.isFile()){
                    handler.execute(sourceFile, targetFile, template);
                }else{
                    List<File> fileList = new ArrayList<File>(Arrays.asList(
                            sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.getName().matches(".*hbm\\.xml");
                    }
                })));
                    for(File file:fileList){
                        handler.execute(file, new File(targetPath), template);
                    }
                }
        }else{
            throw new IOException("输入与输入路径必须同为目录或文件");
        }
    }

    public static void main(String[] args) throws Exception{
        new Hbm2Saver("E:\\workspace\\mpmaps\\src\\java\\com\\linkin\\crm",
                "E:\\temp","E:\\netbean_projects\\date_move2.0\\src\\util\\saver\\template.ftl"
                , new SaverHandlerImpl()).build();
    }
    
    public SaverHandler getHandler() {
        return handler;
    }

    public void setHandler(SaverHandler handler) {
        this.handler = handler;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
