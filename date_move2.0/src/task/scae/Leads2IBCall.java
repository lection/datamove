/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task.scae;

import core.Task;
import util.OrgUtil;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class Leads2IBCall implements Task{
    private OrgUtil orgUtil;
    private String orgs;

    public void execute() {
        
    }

    public void setOrgUtil(OrgUtil orgUtil) {
        this.orgUtil = orgUtil;
    }

    public void setOrgs(String orgs) {
        this.orgs = orgs;
    }
}
