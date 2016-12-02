package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Leo Tu on Nov 30, 2015
 * Refactored by Leo Tu on 2/1/16
 */

public class UserGroupPage extends AbstractPage {
    public UserGroupPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
        // TODO Auto-generated constructor stub
    }

    public UserGroupPage addGroup(String groupName, String description) throws Exception {
        element("ugp.addGP").click();
        waitStatusDlg();
        element("ugp.GPName").type(groupName);
        element("ugp.GPDesc").type(description);
        element("ugp.save").click();
        waitStatusDlg();
        return this;
    }

    public void enterEditGroup(String groupName) throws Exception {
        element("ugp.editGPBtn", groupName).click();
        waitStatusDlg();
    }

    public UserGroupPage editGroup(String groupName, String newName, String newdescription, boolean save) throws Exception {
        enterEditGroup(groupName);
        if (newName != null) {
            element("ugp.GPName").input(newName);
        }
        if (newdescription != null) {
            element("ugp.GPDesc").input(newdescription);
        }
        if (save)
            element("ugp.save").click();
        else
            element("ugp.cancel").click();
        waitStatusDlg();
        return this;
    }

    public UserGroupPage assignUserToGroup(String groupName, String userName) throws Exception {
        element("ugp.addUser", groupName).click();
        waitStatusDlg();
        selectUserToGroup(userName);
        return this;
    }

    public UserGroupPage delUserFromGroup(String groupName, String userName) throws Exception {
        element("ugp.delUser", groupName).click();
        waitStatusDlg();
        element("ugp.delConf", groupName).click();
        return this;
    }


    public void selectUserToGroup(String userName) throws Exception {
        element("ugp.userCheckBox", userName).check(true);
        element("ugp.AUTGS").click();
        waitStatusDlg();
    }


    public List<String> getAllUserGroups() throws Exception {
        return element("ugp.allGP").getAllInnerTexts();
    }

    public List<String> getUsersByUG(String groupName) throws Exception {
        List<String> names = new ArrayList<String>();
        names = element("ugp.allUser").getAllInnerTexts();
        names.remove(names.size());
        return names;
    }

    public List<String> getUGInfo(String UGName) throws Exception {
        List<String> UGInfo = new ArrayList<String>();
        enterEditGroup(UGName);
        UGInfo.add(element("ugp.GPName").getAttribute("value"));
        UGInfo.add(element("ugp.GPDesc").getAttribute("value"));
        element("ugp.cancel").click();
        return UGInfo;
    }

}