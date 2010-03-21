package saver;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Types;
import util.saver.AbstractSaver;
import java.sql.SQLException;
import com.linkin.crm.sales.model.Leads;

public class LeadsSaver extends AbstractSaver{
    public LeadsSaver() throws SQLException{initHilo("t_l1_hi_value","c_next_value",10);}
    public void cap(Object object) throws SQLException{
        Leads obj = (Leads)object;
        PreparedStatement pre = getPreStat();
        obj.setId(this.getHiloId());
        pre.setLong(1,obj.getId());
        try{pre.setString(2,obj.getColor());}
        catch(NullPointerException ex){pre.setNull(2,Types.INTEGER);}
        try{pre.setString(3,obj.getConcernPoints());}
        catch(NullPointerException ex){pre.setNull(3,Types.INTEGER);}
        try{pre.setString(4,obj.getStatus());}
        catch(NullPointerException ex){pre.setNull(4,Types.INTEGER);}
        if(obj.getRegisterDate() != null)
            pre.setTimestamp(5,new Timestamp(obj.getRegisterDate().getTime()));
        else pre.setTimestamp(5,null);
        try{pre.setString(6,obj.getExpOrderDur());}
        catch(NullPointerException ex){pre.setNull(6,Types.INTEGER);}
        try{pre.setString(7,obj.getExpVisitDur());}
        catch(NullPointerException ex){pre.setNull(7,Types.INTEGER);}
        if(obj.getExpVisitDate() != null)
            pre.setTimestamp(8,new Timestamp(obj.getExpVisitDate().getTime()));
        else pre.setTimestamp(8,null);
        try{pre.setString(9,obj.getCurlevel());}
        catch(NullPointerException ex){pre.setNull(9,Types.INTEGER);}
        try{pre.setString(10,obj.getOriginalLevel());}
        catch(NullPointerException ex){pre.setNull(10,Types.INTEGER);}
        if(obj.getNextFLDate() != null)
            pre.setDate(11,new Date(obj.getNextFLDate().getTime()));
        else pre.setDate(11,null);
        try{pre.setInt(12,obj.getTestDrive());}
        catch(NullPointerException ex){pre.setNull(12,Types.INTEGER);}
        try{pre.setString(13,obj.getFeedBack());}
        catch(NullPointerException ex){pre.setNull(13,Types.INTEGER);}
        try{pre.setInt(14,obj.getIsDealed());}
        catch(NullPointerException ex){pre.setNull(14,Types.INTEGER);}
        try{pre.setString(15,obj.getCurOwnedCar());}
        catch(NullPointerException ex){pre.setNull(15,Types.INTEGER);}
        try{pre.setInt(16,obj.getCurOwnedCarYears());}
        catch(NullPointerException ex){pre.setNull(16,Types.INTEGER);}
        try{pre.setString(17,obj.getCurOwnedCarLic());}
        catch(NullPointerException ex){pre.setNull(17,Types.INTEGER);}
        try{pre.setString(18,obj.getRequireLic());}
        catch(NullPointerException ex){pre.setNull(18,Types.INTEGER);}
        try{pre.setString(19,obj.getCustPref());}
        catch(NullPointerException ex){pre.setNull(19,Types.INTEGER);}
        try{pre.setString(20,obj.getCptrBrand());}
        catch(NullPointerException ex){pre.setNull(20,Types.INTEGER);}
        try{pre.setString(21,obj.getCptrCar());}
        catch(NullPointerException ex){pre.setNull(21,Types.INTEGER);}
        try{pre.setString(22,obj.getConcernComp());}
        catch(NullPointerException ex){pre.setNull(22,Types.INTEGER);}
        try{pre.setString(23,obj.getBudget());}
        catch(NullPointerException ex){pre.setNull(23,Types.INTEGER);}
        try{pre.setString(24,obj.getPayApproach());}
        catch(NullPointerException ex){pre.setNull(24,Types.INTEGER);}
        try{pre.setString(25,obj.getCurrPrep());}
        catch(NullPointerException ex){pre.setNull(25,Types.INTEGER);}
        try{pre.setString(26,obj.getContactVenue());}
        catch(NullPointerException ex){pre.setNull(26,Types.INTEGER);}
        try{pre.setString(27,obj.getAlreadyVistiedDlr());}
        catch(NullPointerException ex){pre.setNull(27,Types.INTEGER);}
        try{pre.setString(28,obj.getRegisterChannel());}
        catch(NullPointerException ex){pre.setNull(28,Types.INTEGER);}
        try{pre.setString(29,obj.getInfoChannel());}
        catch(NullPointerException ex){pre.setNull(29,Types.INTEGER);}
        try{pre.setString(30,obj.getKnsource());}
        catch(NullPointerException ex){pre.setNull(30,Types.INTEGER);}
        try{pre.setString(31,obj.getNumber());}
        catch(NullPointerException ex){pre.setNull(31,Types.INTEGER);}
        try{pre.setString(32,obj.getBuySeq());}
        catch(NullPointerException ex){pre.setNull(32,Types.INTEGER);}
        try{pre.setString(33,obj.getBuyUsedCars());}
        catch(NullPointerException ex){pre.setNull(33,Types.INTEGER);}
        try{pre.setString(34,obj.getVistiedDlr());}
        catch(NullPointerException ex){pre.setNull(34,Types.INTEGER);}
        try{pre.setString(35,obj.getVistiedDlrName());}
        catch(NullPointerException ex){pre.setNull(35,Types.INTEGER);}
        try{pre.setString(36,obj.getPricing());}
        catch(NullPointerException ex){pre.setNull(36,Types.INTEGER);}
        try{pre.setString(37,obj.getPayType());}
        catch(NullPointerException ex){pre.setNull(37,Types.INTEGER);}
        try{pre.setString(38,obj.getMemo());}
        catch(NullPointerException ex){pre.setNull(38,Types.INTEGER);}
        try{pre.setString(39,obj.getP_ext_long1());}
        catch(NullPointerException ex){pre.setNull(39,Types.INTEGER);}
        try{pre.setString(40,obj.getP_ext_long2());}
        catch(NullPointerException ex){pre.setNull(40,Types.INTEGER);}
        try{pre.setString(41,obj.getP_ext_long3());}
        catch(NullPointerException ex){pre.setNull(41,Types.INTEGER);}
        try{pre.setString(42,obj.getP_ext_str1());}
        catch(NullPointerException ex){pre.setNull(42,Types.INTEGER);}
        try{pre.setString(43,obj.getP_ext_str2());}
        catch(NullPointerException ex){pre.setNull(43,Types.INTEGER);}
        try{pre.setString(44,obj.getP_ext_str3());}
        catch(NullPointerException ex){pre.setNull(44,Types.INTEGER);}
        try{pre.setString(45,obj.getP_ext_str4());}
        catch(NullPointerException ex){pre.setNull(45,Types.INTEGER);}
        if(obj.getP_ext_dt1() != null)
            pre.setTimestamp(46,new Timestamp(obj.getP_ext_dt1().getTime()));
        else pre.setTimestamp(46,null);
        if(obj.getP_ext_dt2() != null)
            pre.setTimestamp(47,new Timestamp(obj.getP_ext_dt2().getTime()));
        else pre.setTimestamp(47,null);
        if(obj.getP_ext_dt3() != null)
            pre.setTimestamp(48,new Timestamp(obj.getP_ext_dt3().getTime()));
        else pre.setTimestamp(48,null);
        try{pre.setString(49,obj.getCreatedBy());}
        catch(NullPointerException ex){pre.setNull(49,Types.INTEGER);}
        if(obj.getCreatedDate() != null)
            pre.setTimestamp(50,new Timestamp(obj.getCreatedDate().getTime()));
        else pre.setTimestamp(50,null);
        try{pre.setString(51,obj.getUpdatedBy());}
        catch(NullPointerException ex){pre.setNull(51,Types.INTEGER);}
        if(obj.getUpdateDate() != null)
            pre.setTimestamp(52,new Timestamp(obj.getUpdateDate().getTime()));
        else pre.setTimestamp(52,null);
        if(obj.getCustomer() != null)
            pre.setLong(53,obj.getCustomer().getId());
        else pre.setNull(53,Types.BIGINT);
        if(obj.getContact() != null)
            pre.setLong(54,obj.getContact().getId());
        else pre.setNull(54,Types.BIGINT);
        if(obj.getProductCat() != null)
            pre.setLong(55,obj.getProductCat().getId());
        else pre.setNull(55,Types.BIGINT);
        if(obj.getProduct() != null)
            pre.setLong(56,obj.getProduct().getId());
        else pre.setNull(56,Types.BIGINT);
        if(obj.getOrigProduct() != null)
            pre.setLong(57,obj.getOrigProduct().getId());
        else pre.setNull(57,Types.BIGINT);
        if(obj.getCampaign() != null)
            pre.setLong(58,obj.getCampaign().getId());
        else pre.setNull(58,Types.BIGINT);
        if(obj.getUser() != null)
            pre.setLong(59,obj.getUser().getId());
        else pre.setNull(59,Types.BIGINT);
        if(obj.getOrder() != null)
            pre.setLong(60,obj.getOrder().getId());
        else pre.setNull(60,Types.BIGINT);
    }

    public String getInsertSql(){
        return "insert into t_leads"
         + " (c_id ,c_color ,c_concern_points ,c_status ,c_register_date ,c_expOrder_dur ,c_expvisit_dur ,c_expvisit_date ,c_curlevel ,c_original_level ,c_next_fldate ,c_testdrive ,c_feedback ,c_dealed ,c_curowned_car ,c_curowned_caryear ,c_curowned_carlic ,c_requirelic ,c_custpref ,c_comp_brand ,c_comp_car ,c_concern_compare ,c_budget ,c_payapproach ,c_curr_prep ,c_contact_venue ,c_already_visited_dlr ,c_register_channel ,c_info_channel ,c_knsource ,c_number ,c_buy_seq ,c_buy_usedcars ,c_visited_dlr ,c_visited_dlr_name ,c_pricing ,c_payType ,c_memo ,c_ext_long1 ,c_ext_long2 ,c_ext_long3 ,c_ext_str1 ,c_ext_str2 ,c_ext_str3 ,c_ext_str4 ,c_ext_dt1 ,c_ext_dt2 ,c_ext_dt3 ,c_created_by ,c_created_date ,c_updated_by ,c_update_date ,c_cust_id ,c_contact_id ,c_productcat_id ,c_product_id ,c_orig_product_id ,c_campaign_id ,c_user_id ,"
         +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
}