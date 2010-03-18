/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.saver;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class SaverHandlerImpl implements SaverHandler{
    private Configuration cfg = new Configuration();
    private SAXReader saxReader = new SAXReader();
    private String packageName = null;
    Template temp = null;
    private PrintWriter writer;

    private Map<String,Map> classMap = new HashMap<String,Map>();

    public void execute(File sourceFile, File targetFile, String template) throws IOException {
        cfg.setDirectoryForTemplateLoading(new File(template).getParentFile());
        temp = cfg.getTemplate("template.ftl");
        Element root = null;
        try {root = saxReader.read(sourceFile).getRootElement();}
        catch (DocumentException ex) {throw new IOException(sourceFile.getAbsolutePath() + "解析失败");}
        packageName = root.attributeValue("package");
        List<Element> list = new ArrayList<Element>(root.elements("class"));
        list.addAll(root.elements("joined-subclass"));
        if(list.size() > 1 && targetFile.isFile())throw new IOException(sourceFile.getAbsolutePath() + "中的类有多个");
        for(Element e:list){
            parseClass(e, targetFile);
        }
    }

    private void parseClass(Element element,File targetFile) throws IOException{
        Map data = new HashMap();
        String className = element.attributeValue("name");
        if(className.matches("[^\\.]*")){
            data.put("packageName", packageName);
            data.put("className", className);
        }else{
            data.put("packageName", className.substring(0, className.lastIndexOf(".")));
            data.put("className", className.substring(className.lastIndexOf(".") + 1));
        }
        data.put("table", element.attributeValue("table"));
        data.put("id", parseId(element));
        data.put("pro", parseProperty(element));
        if(targetFile.isFile()){writer = new PrintWriter(targetFile);}
        else{writer = new PrintWriter(new File(targetFile,data.get("className") + "Saver.java"));}
        try {
            temp.process(data, writer);
        } catch (TemplateException ex) {
            ex.printStackTrace();
        }
        writer.close();
        classMap.put(className, data);
    }

    private Map parseProperty(Element element){
        Map map = new LinkedHashMap();
        List<Element> list = new ArrayList<Element>();
        list.addAll(element.elements("property"));
        list.addAll(element.elements("many-to-one"));
        list.addAll(element.elements("one-to-one"));
        String column = null;
        for(Element e:list){
            column = e.elementText("column");
            if(column == null||column.trim().length()==0){
                Element e1 = e.element("column");
                if(e1 != null)column = e1.attributeValue("name");
            }
            if(column == null||column.trim().length()==0)column = e.attributeValue("column");
            String key = e.attributeValue("name");
            String type = null;
            String pro = null;
            if(e.getName().matches("property")){
                type = parseType(e.attributeValue("type"));
                pro = key.substring(0, 1).toUpperCase() + key.substring(1);
            }else{
                type = "Long";
                pro = key.substring(0, 1).toUpperCase() + key.substring(1) + "().getId";
            }
            map.put(key, new String[]{column,type,pro});
        }
        return map;
    }

    private Map parseId(Element element){
        Map map = new HashMap();
        Element id = element.element("id");
        if(id != null){
            map.put("id", id.attributeValue("column"));
            for(Element e:(List<Element>)id.element("generator").elements("param")){
                map.put(e.attributeValue("name"), e.getText());
            }
        }else{
            map.put("id", element.element("key").attributeValue("column"));
        }
        return map;
    }

    private String parseType(String type){
        String result = null;
        if(type.matches(".*[L,l]ong.*"))result = "Long";
        if(type.matches(".*[I,i]nt.*"))result = "Int";
        if(type.matches(".*[D,d]ate.*"))result = "Date";
        if(type.matches(".*[T,t]imestamp.*"))result = "Timestamp";
        if(type.matches(".*[S,s]tring.*"))result = "String";
        if(type.matches(".*[B,b]oolean.*"))result = "Boolean";
        if(type.matches(".*[D,d]ouble.*"))result = "Double";
        return result;
    }
}
