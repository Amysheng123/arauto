package com.lombardrisk.pages;


import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Ray Rui on 3/2/15.
 * Refactored by Leo Tu on 1/25/16
 */
public abstract class AbstractDrilldownPage extends AbstractPage {

    public AbstractDrilldownPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public ValidationPage getValidationTab() throws Exception {
        element("adp.validation").click();
        waitStatusDlg();
        return new ValidationPage(getWebDriverWrapper());
    }

    public ErrorListPage getErrorListTab() throws Exception {
        element("adp.problems").click();
        return new ErrorListPage(getWebDriverWrapper());
    }

    public AdjustLogPage getAdjustLogTab() throws Exception {
        element("adp.adjustment").click();
        return new AdjustLogPage(getWebDriverWrapper());
    }

    public void showClick() throws Exception {
        element("adp.hide").click();
    }

    public void hideClick() throws Exception {
        element("adp.hide").click();
        waitStatusDlg();
    }

}
