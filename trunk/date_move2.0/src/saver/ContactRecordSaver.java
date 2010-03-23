package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.sales.model.ContactRecord;

public class ContactRecordSaver extends AbstractSaver{
    public ContactRecordSaver() throws SQLException{initHilo("t_c2_hi_value","next_value",10);}
    public void cap(Object object) throws SQLException{
        ContactRecord obj = (ContactRecord)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        if(obj.getContactDate() != null)
            pre.setDate(2,new Date(obj.getContactDate().getTime()));
        else pre.setDate(2,null);
        if(obj.getNextDate() != null)
            pre.setDate(3,new Date(obj.getNextDate().getTime()));
        else pre.setDate(3,null);
        try{pre.setString(4,obj.getFlType());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        try{pre.setString(5,obj.getFlContent());}
        catch(NullPointerException ex){pre.setNull(5,Types.INTEGER);}
        try{pre.setString(6,obj.getContactChannel());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        try{pre.setString(7,obj.getExpVisitDur());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        if(obj.getInvitedDate() != null)
            pre.setTimestamp(8,new Timestamp(obj.getInvitedDate().getTime()));
        else pre.setTimestamp(8,null);
        try{pre.setInt(9,obj.getSequence());}
        catch(NullPointerException ex){pre.setNull(9,Types.INTEGER);}
        try{pre.setInt(10,obj.getTestDrive());}
        catch(NullPointerException ex){pre.setNull(10,Types.INTEGER);}
        try{pre.setString(11,obj.getFeedBack());}
        catch(NullPointerException ex){pre.setNull(11,Types.INTEGER);}
        try{pre.setString(12,obj.getCurlevel());}
        catch(NullPointerException ex){pre.setNull(12,Types.INTEGER);}
        try{pre.setString(13,obj.getResult());}
        catch(NullPointerException ex){pre.setNull(13,Types.INTEGER);}
        try{pre.setString(14,obj.getRejectionReason());}
        catch(NullPointerException ex){pre.setNull(14,Types.INTEGER);}
        try{pre.setString(15,obj.getRejectionFactors());}
        catch(NullPointerException ex){pre.setNull(15,Types.INTEGER);}
        try{pre.setString(16,obj.getCptrBrand());}
        catch(NullPointerException ex){pre.setNull(16,Types.INTEGER);}
        try{pre.setString(17,obj.getCptrCar());}
        catch(NullPointerException ex){pre.setNull(17,Types.INTEGER);}
        try{pre.setString(18,obj.getPricing());}
        catch(NullPointerException ex){pre.setNull(18,Types.INTEGER);}
        try{pre.setString(19,obj.getMemo());}
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
        if(obj.getLeads() != null)
            pre.setLong(34,obj.getLeads().getId());
        else pre.setNull(34,Types.BIGINT);
        if(obj.getCustomer() != null)
            pre.setLong(35,obj.getCustomer().getId());
        else pre.setNull(35,Types.BIGINT);
        if(obj.getContact() != null)
            pre.setLong(36,obj.getContact().getId());
        else pre.setNull(36,Types.BIGINT);
        if(obj.getUser() != null)
            pre.setLong(37,obj.getUser().getId());
        else pre.setNull(37,Types.BIGINT);
        if(obj.getCampaign() != null)
            pre.setLong(38,obj.getCampaign().getId());
        else pre.setNull(38,Types.BIGINT);
    }

    public String getInsertSql(){
        return "insert into t_contactrecord"
         + " (c_id ,c_contact_date ,c_next_date ,c_fl_type ,c_fl_content ,c_contact_chnl ,c_expvisit_dur ,c_invited_date ,c_sequence ,c_testDrive ,c_feedback ,c_curlevel ,c_result ,c_rejection_reason ,c_rejection_factors ,c_comp_brand ,c_comp_car ,c_pricing ,c_memo ,c_ext_long1 ,c_ext_long2 ,c_ext_long3 ,c_ext_str1 ,c_ext_str2 ,c_ext_str3 ,c_ext_str4 ,c_ext_dt1 ,c_ext_dt2 ,c_ext_dt3 ,c_created_by ,c_created_date ,c_updated_by ,c_update_date ,c_leads_id ,c_cust_id ,c_contact_id ,c_user_id ,c_campaign_id"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}