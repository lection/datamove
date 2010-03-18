package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.core.model.PersonImpl;

public class PersonImplSaver extends AbstractSaver{
    public PersonImplSaver() throws SQLException{initHilo("t_n1_hi_value","c_psn_next_hi_value",10);}
    public void cap(Object object) throws SQLException{
        PersonImpl obj = (PersonImpl)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());//加入主键值
        pre.setLong(1,obj.getId());
        pre.setString(2,obj.getLastname());
        pre.setString(3,obj.getFirstname());
        pre.setString(4,obj.getSex());
        if(obj.getBirthday() != null)
            pre.setDate(5,new Date(obj.getBirthday().getTime()));
        else pre.setDate(5,null);
        pre.setInt(6,obj.getAge());
        pre.setString(7,obj.getAgeRange());
        pre.setString(8,obj.getCphone1());
        pre.setString(9,obj.getSphone1_ac());
        pre.setString(10,obj.getSphone1());
        pre.setString(11,obj.getSphone1_ext());
        pre.setString(12,obj.getCphone2());
        pre.setString(13,obj.getSphone2_ac());
        pre.setString(14,obj.getSphone2());
        pre.setString(15,obj.getSphone2_ext());
        pre.setString(16,obj.getEmail());
        pre.setString(17,obj.getIndustry());
        pre.setString(18,obj.getTitle());
        pre.setString(19,obj.getCompany());
        pre.setString(20,obj.getP_ext_long1());
        pre.setString(21,obj.getP_ext_long2());
        pre.setString(22,obj.getP_ext_long3());
        pre.setString(23,obj.getP_ext_str1());
        pre.setString(24,obj.getP_ext_str2());
        pre.setString(25,obj.getP_ext_str3());
        pre.setString(26,obj.getP_ext_str4());
        if(obj.getP_ext_dt1() != null)
            pre.setTimestamp(27,new Timestamp(obj.getP_ext_dt1().getTime()));
        else pre.setTimestamp(27,null);
        if(obj.getP_ext_dt2() != null)
            pre.setTimestamp(28,new Timestamp(obj.getP_ext_dt2().getTime()));
        else pre.setTimestamp(28,null);
        if(obj.getP_ext_dt3() != null)
            pre.setTimestamp(29,new Timestamp(obj.getP_ext_dt3().getTime()));
        else pre.setTimestamp(29,null);
        pre.setString(30,obj.getCreatedBy());
        if(obj.getCreatedDate() != null)
            pre.setTimestamp(31,new Timestamp(obj.getCreatedDate().getTime()));
        else pre.setTimestamp(31,null);
        pre.setString(32,obj.getUpdatedBy());
        if(obj.getUpdateDate() != null)
            pre.setTimestamp(33,new Timestamp(obj.getUpdateDate().getTime()));
        else pre.setTimestamp(33,null);
    }

    public String getInsertSql(){
        return "insert into t_person"
         + " (c_id ,c_lastname ,c_firstname ,c_sex ,c_birthday ,c_age ,c_agerange ,c_phone1 ,c_sphone1_ac ,c_sphone1 ,c_sphone1_ext ,c_phone2 ,c_sphone2_ac ,c_sphone2 ,c_sphone2_ext ,c_email ,c_industry ,c_title ,c_company ,c_ext_long1 ,c_ext_long2 ,c_ext_long3 ,c_ext_str1 ,c_ext_str2 ,c_ext_str3 ,c_ext_str4 ,c_ext_dt1 ,c_ext_dt2 ,c_ext_dt3 ,c_created_by ,c_created_date ,c_updated_by ,c_update_date"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}