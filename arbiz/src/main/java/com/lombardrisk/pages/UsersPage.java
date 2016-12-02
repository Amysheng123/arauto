package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Leo Tu on Nov 30, 2015
 * Refactor by Leo Tu on 2/1/16
 */


public class UsersPage extends AbstractPage {


    public UsersPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
    }

    public UsersPage addUser(String userName, String email, String password, String confirmPassword) throws Exception {
        element("up.addUser").click();
        waitStatusDlg();
        element("up.userName").type(userName);
        element("up.email").type(email);
        element("up.pwd").type(password);
        element("up.cpwd").type(confirmPassword);
        element("up.save").click();
        waitStatusDlg();
        return this;
    }


    public ArrayList<String> getAllUsers() throws Exception {
        ArrayList<String> allUsers = new ArrayList<String>();
        int amt = (int) element("up.userTab").getRowCount();
        for (int i = 1; i <= amt; i++) {
            allUsers.add(element("up.userInfo", String.valueOf(i)).getInnerText());
        }
        return allUsers;
    }

    public void addPrivilege(String privilegeName) throws Exception {
        element("up.AddPGCheckBox", privilegeName).check(true);
        element("up.addP").click();
        waitStatusDlg();
        element("up.save").click();
        waitStatusDlg();
    }

    public void deletePrivilege(String privilegeName) throws Exception {
        element("up.DelPGCheckBox", privilegeName).check(true);
        element("up.delP").click();
        waitStatusDlg();
        element("up.save").click();
        waitStatusDlg();

    }

    public void enterEditPage(String userName) throws Exception {
        element("up.editUser", userName).click();
        waitStatusDlg();
    }


    public List<String> getUertInfo(String userName) throws Exception {
        List<String> userInfo = new ArrayList<String>();
        enterEditPage(userName);

        element("up.userName").getAttribute("value");
        element("up.email").getAttribute("value");
        if (getUserStatus(userName).equals("Active")) {
            userInfo.add("Y");
        } else {
            userInfo.add("N");
        }

        return userInfo;
    }

    public List<String> getSelectedPG(String userName) throws Exception {
        return element("up.selectedPG").getAllInnerTexts();

    }

    public String changePassword(String userName, String passowrd, String confirmPassword) throws Exception {
        if (element("up.userName").isDisplayed()) {
            if (!element("up.userName").getAttribute("value").equals(userName)) {
                element("up.editBtn", userName).click();
                waitStatusDlg();
            }
        } else {
            element("up.editBtn", userName).click();
            waitStatusDlg();
        }

        if (!element("up.changePwd").getAttribute("class").contains("ui-state-active")) {
            element("up.changePwd").check(true);
            waitStatusDlg();
        }
        if (!passowrd.equals(""))
            element("up.pwd").input(passowrd);
        else
            element("up.pwd").clear();
        if (!confirmPassword.equals(""))
            element("up.cpwd").input(confirmPassword);
        else
            element("up.cpwd").clear();
        element("up.save").click();
        try {
            waitThat("up.errorMsg").toBePresentIn(5000);
            return element("up.errorMsg").getInnerText();
        } catch (Exception e) {
            return null;
        }
    }


    public void changeUserStatus(String userName, boolean active) throws Exception {
        if (element("up.userName").isDisplayed()) {
            if (!element("up.userName").getAttribute("value").equals(userName)) {
                element("up.editBtn", userName).click();
                waitStatusDlg();
            }
        } else {
            element("up.editBtn", userName).click();
            waitStatusDlg();
        }
        if (active)
            element("up.active").check(true);
        else
            element("up.active").click();
        element("up.save").click();
    }

    public String getUserStatus(String userName) throws Exception {
        return element("up.userStatus", userName).getInnerText();
    }

    public HomePage logout() throws Exception {
        ListPage listPage = backToListPage();
        listPage.logout();
        return new HomePage(getWebDriverWrapper());
    }

    public ListPage backToListPage() throws Exception {
        element("up.dashboard").click();
        waitStatusDlg();
        return new ListPage(getWebDriverWrapper());
    }

}
