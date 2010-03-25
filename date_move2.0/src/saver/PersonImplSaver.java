package saver;

import com.linkin.crm.core.model.Address;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.core.model.PersonImpl;

public class PersonImplSaver extends AbstractSaver{
    private AddressSaver addSaver;
    private ContactAddSaver caSaver;

    @Override
    public void destory() throws SQLException {
        super.destory();
        addSaver.destory();
        caSaver.destory();
    }

    @Override
    public void init() throws SQLException {
        addSaver.setTargetConn(getTargetConn());
        caSaver.setTargetConn(getTargetConn());
        super.init();
        addSaver.init();
        caSaver.init();
    }

    public PersonImplSaver() throws SQLException{
        initHilo("t_n1_hi_value","c_psn_next_hi_value",10);
    }
    public void cap(Object object) throws SQLException{
        PersonImpl obj = (PersonImpl)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());//加入主键值
        pre.setLong(1,obj.getId());
        try{pre.setString(2,obj.getLastname());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        try{pre.setString(3,obj.getFirstname());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setString(4,obj.getSex());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        if(obj.getBirthday() != null)
            pre.setDate(5,new Date(obj.getBirthday().getTime()));
        else pre.setDate(5,null);
        try{pre.setInt(6,obj.getAge());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        try{pre.setString(7,obj.getAgeRange());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        try{pre.setString(8,obj.getCphone1());}
        catch(NullPointerException ex){pre.setNull(8,Types.INTEGER);}
        try{pre.setString(9,obj.getSphone1_ac());}
        catch(NullPointerException ex){pre.setNull(9,Types.INTEGER);}
        try{pre.setString(10,obj.getSphone1());}
        catch(NullPointerException ex){pre.setNull(10,Types.INTEGER);}
        try{pre.setString(11,obj.getSphone1_ext());}
        catch(NullPointerException ex){pre.setNull(11,Types.INTEGER);}
        try{pre.setString(12,obj.getCphone2());}
        catch(NullPointerException ex){pre.setNull(12,Types.INTEGER);}
        try{pre.setString(13,obj.getSphone2_ac());}
        catch(NullPointerException ex){pre.setNull(13,Types.INTEGER);}
        try{pre.setString(14,obj.getSphone2());}
        catch(NullPointerException ex){pre.setNull(14,Types.INTEGER);}
        try{pre.setString(15,obj.getSphone2_ext());}
        catch(NullPointerException ex){pre.setNull(15,Types.INTEGER);}
        try{pre.setString(16,obj.getEmail());}
        catch(NullPointerException ex){pre.setNull(16,Types.INTEGER);}
        try{pre.setString(17,obj.getIndustry());}
        catch(NullPointerException ex){pre.setNull(17,Types.INTEGER);}
        try{pre.setString(18,obj.getTitle());}
        catch(NullPointerException ex){pre.setNull(18,Types.INTEGER);}
        try{pre.setString(19,obj.getCompany());}
        catch(NullPointerException ex){pre.setNull(19,Types.INTEGER);}
        try{pre.setString(20,obj.getP_ext_long1());}
        catch(NullPointerException ex){pre.setNull(20,Types.INTEGER);}
        try{pre.setString(21,obj.getP_ext_long2());}
        catch(NullPointerException ex){pre.setNull(21,Types.INTEGER);}
        try{pre.setString(22,obj.getP_ext_long3());}
        catch(NullPointerException ex){pre.setNull(22,Types.INTEGER);}
        try{pre.setString(23,obj.getP_ext_str1());}
        catch(NullPointerException ex){pre.setNull(23,Types.INTEGER);}
        try{pre.setString(24,obj.getP_ext_str2());}
        catch(NullPointerException ex){pre.setNull(24,Types.INTEGER);}
        try{pre.setString(25,obj.getP_ext_str3());}
        catch(NullPointerException ex){pre.setNull(25,Types.INTEGER);}
        try{pre.setString(26,obj.getP_ext_str4());}
        catch(NullPointerException ex){pre.setNull(26,Types.INTEGER);}
        if(obj.getP_ext_dt1() != null)
            pre.setTimestamp(27,new Timestamp(obj.getP_ext_dt1().getTime()));
        else pre.setTimestamp(27,null);
        if(obj.getP_ext_dt2() != null)
            pre.setTimestamp(28,new Timestamp(obj.getP_ext_dt2().getTime()));
        else pre.setTimestamp(28,null);
        if(obj.getP_ext_dt3() != null)
            pre.setTimestamp(29,new Timestamp(obj.getP_ext_dt3().getTime()));
        else pre.setTimestamp(29,null);
        try{pre.setString(30,obj.getCreatedBy());}
        catch(NullPointerException ex){pre.setNull(30,Types.INTEGER);}
        if(obj.getCreatedDate() != null)
            pre.setTimestamp(31,new Timestamp(obj.getCreatedDate().getTime()));
        else pre.setTimestamp(31,null);
        try{pre.setString(32,obj.getUpdatedBy());}
        catch(NullPointerException ex){pre.setNull(32,Types.INTEGER);}
        if(obj.getUpdateDate() != null)
            pre.setTimestamp(33,new Timestamp(obj.getUpdateDate().getTime()));
        else pre.setTimestamp(33,null);
        if(obj.getAddresses()!=null)
            for(Address add:obj.getAddresses()){
                addSaver.save(add);
            }
    }

    @Override
    public void save(Object object) throws SQLException {
        super.save(object);
        PersonImpl obj = (PersonImpl)object;
        if(obj.getAddresses()!=null)
            for(Address add:obj.getAddresses()){
                caSaver.save(new Long[]{obj.getId(),add.getId()});
            }
    }

    public String getInsertSql(){
        return "insert into t_person"
         + " (c_id ,c_lastname ,c_firstname ,c_sex ,c_birthday ,c_age ,c_agerange ,c_phone1 ,c_sphone1_ac ,c_sphone1 ,c_sphone1_ext ,c_phone2 ,c_sphone2_ac ,c_sphone2 ,c_sphone2_ext ,c_email ,c_industry ,c_title ,c_company ,c_ext_long1 ,c_ext_long2 ,c_ext_long3 ,c_ext_str1 ,c_ext_str2 ,c_ext_str3 ,c_ext_str4 ,c_ext_dt1 ,c_ext_dt2 ,c_ext_dt3 ,c_created_by ,c_created_date ,c_updated_by ,c_update_date"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public void setAddSaver(AddressSaver addSaver) {
        this.addSaver = addSaver;
    }

    public void setCaSaver(ContactAddSaver caSaver) {
        this.caSaver = caSaver;
    }
}