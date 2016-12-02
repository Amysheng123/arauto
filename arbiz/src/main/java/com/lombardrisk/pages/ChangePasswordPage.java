package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Kevin Ling on 2/25/15.
 * Refactored by Leo Tu on 1/25/16
 */
public class ChangePasswordPage extends AbstractPage {

    public ChangePasswordPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public ChangePasswordPage setCurrentPassword(String password) throws Exception {
        element("cpw.curPD").type(password);
        return this;
    }

    public ChangePasswordPage setNewPassword(String password) throws Exception {
        element("cpw.newPD").type(password);
        return this;
    }

    public ChangePasswordPage setConfirmPassword(String password) throws Exception {
        element("cpw.conPD").type(password);
        return this;
    }

    public ListPage saveSetting() throws Exception {
        element("cpw.save").click();
        return new ListPage(getWebDriverWrapper());
    }

    public ListPage cancelSetting() throws Exception {
        element("cpw.cancel").click();
        return new ListPage(getWebDriverWrapper());
    }


}
