package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 1/25/16
 */
public class AdminPage extends AbstractPage {

    public AdminPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
        // TODO Auto-generated constructor stub
    }

    public AdminPage EnterAdminPage() throws Exception {
        element("am.setting").click();
        waitStatusDlg();
        element("am.administration").click();
        waitStatusDlg();
        return this;
    }

}