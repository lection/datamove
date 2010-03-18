/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import com.linkin.crm.core.model.Hotline;
import core.adapter.DataException;
import core.adapter.J2JTaskSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import util.OrgUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class HotlineTask extends J2JTaskSupport{
    private OrgUtil orgUtil;

    @Override
    public Object parse(Connection conn, ResultSet rs) throws DataException {
        Hotline hotline = new Hotline();
        
        return hotline;
    }

    public OrgUtil getOrgUtil() {
        return orgUtil;
    }

    public void setOrgUtil(OrgUtil orgUtil) {
        this.orgUtil = orgUtil;
    }

}
