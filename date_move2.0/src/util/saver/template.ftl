package saver.scae;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import ${packageName}.${className};

public class ${className}Saver extends AbstractSaver{
    <#if id.table?exists>
    public ${className}Saver() throws SQLException{initHilo("${id.table}","${id.column}",${id.max_lo});}
    </#if>
    public void cap(Object object) throws SQLException{
        ${className} obj = (${className})object;
        PreparedStatement pre = getPreStat();
        pre.setLong(1,obj.getId());
        <#list pro?keys as key>
        <#if (pro[key][1]?exists&&pro[key][1]=="Timestamp")>
        if(obj.get${pro[key][2]}() != null)
            pre.set${pro[key][1]?if_exists}(${key_index+2},new Timestamp(obj.get${pro[key][2]?if_exists}().getTime()));
        else pre.set${pro[key][1]?if_exists}(${key_index+2},null);
        <#elseif (pro[key][1]?exists&&pro[key][1]=="Date")>
        if(obj.get${pro[key][2]?if_exists}() != null)
            pre.set${pro[key][1]}(${key_index+2},new Date(obj.get${pro[key][2]?if_exists}().getTime()));
        else pre.set${pro[key][1]?if_exists}(${key_index+2},null);
        <#elseif (pro[key][2]?ends_with("getId"))>
        if(obj.get${key?cap_first}() != null)
            pre.set${pro[key][1]?if_exists}(${key_index+2},obj.get${pro[key][2]?if_exists}());
        else pre.setNull(${key_index+2},Types.BIGINT);
        <#else>
        try{pre.set${pro[key][1]?if_exists}(${key_index+2},obj.get${pro[key][2]?if_exists}());}
        catch(NullPointerException ex){pre.setNull(${key_index+2},Types.<#if pro[key][1]?exists&&pro[key][1]=="Long">BIGINT<#else>INTEGER</#if>);}
        </#if>
        </#list>
    }

    public String getInsertSql(){
        return "insert into ${table}"
         + " (${id.id}<#list pro?keys as key> ,${pro[key][0]?if_exists}</#list>"
         +") values(?<#list pro?keys as key>,?</#list>)";
    }
}