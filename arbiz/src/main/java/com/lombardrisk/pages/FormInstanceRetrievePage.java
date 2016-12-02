package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

import java.util.List;

/**
 * Refactored by Leo Tu on 1/29/16
 */

public class FormInstanceRetrievePage extends AbstractPage {

    public FormInstanceRetrievePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public void setGroup(String group) throws Exception {
        element("frp.group").selectByVisibleText(group);
        waitStatusDlg();
    }

    public void setReferenceDate(String processDate) throws Exception {
        element("frp.date").input(processDate);
        clickToday();
        waitThat().timeout(3000);
    }

    public void setForm(String form) throws Exception {
        element("frp.form").selectByVisibleText(form);
        waitStatusDlg();
    }

    public RetrieveResultPage retrieveConfirmClick() throws Exception {
        element("frp.retrConfirm").click();
        waitStatusDlg();

        return new RetrieveResultPage(getWebDriverWrapper());
    }

    public ListPage retrieveCloseClick() throws Exception {
    	if(element("frp.cancel").isDisplayed()){
    		element("frp.cancel").click();
    		waitStatusDlg();
    	}
        return new ListPage(getWebDriverWrapper());
    }

    public ListPage retrieveDeclineClick() throws Exception {
    	if(element("frp.retrCancel").isDisplayed()){
    		element("frp.retrCancel").click();
    		waitStatusDlg();
    	}
        return new ListPage(getWebDriverWrapper());
    }

    public boolean isMessagesVisible() throws Exception {
        return element("frp.message").isDisplayed();
    }


    public void setLogLevel(String level) throws Exception {
        element("frp.logLevel").selectByVisibleText(level);
    }

    public FormInstancePage openForm() throws Exception {
        waitThat().timeout(1000);
        element("frp.openForm").click();
        waitStatusDlg();
        return new FormInstancePage(getWebDriverWrapper());
    }

    public List<String> getFormOptions() throws Exception {
        return element("frp.form").getAllOptionTexts();
    }

    public RetrieveResultPage clickOK() throws Exception {
        element("frp.retrieve").click();
        waitStatusDlg();
        waitThat().timeout(1500);
        return new RetrieveResultPage(getWebDriverWrapper());
    }


}
