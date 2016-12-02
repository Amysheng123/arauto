package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 1/25/16
 */

public class FormInstanceCreatePage extends AbstractPage {


    public FormInstanceCreatePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public FormInstanceCreatePage setGroup(String group) throws Exception {
        element("fcp.group").selectByVisibleText(group);
        waitStatusDlg();
        return this;
    }

    public FormInstanceCreatePage setProcessDate(String processDate) throws Exception {
        if (processDate != null) {
            element("fcp.date").input(processDate);
            //element("fcp.date").type(Keys.ENTER);
            //element("fcp.refDate").click();
            clickToday();
            waitThat().timeout(1000);
        }
        return this;
    }


    public FormInstanceCreatePage setForm(String form) throws Exception {
        if (form != null) {
            element("fcp.form").selectByVisibleText(form);
            waitThat().timeout(1000);
        }

        return this;
    }

    public FormInstanceCreatePage selectCloneCheck() throws Exception {
        element("fcp.copyData").click();
        waitThat().timeout(1000);
        return this;
    }

    public FormInstanceCreatePage setSelectCloneDate(String selectCloneDate) throws Exception {
        element("fcp.copyDate").selectByVisibleText(selectCloneDate);
        return this;
    }

    public FormInstanceCreatePage selectInitToZeroCheck() throws Exception {
        element("fcp.zero").click();
        return this;
    }

    public FormInstancePage createConfirmClick() throws Exception {
        element("fcp.create").click();
        waitThat().timeout(1500);
        if (element("fcp.createConfirm").isDisplayed())
        {
        	element("fcp.createConfirm").click();
        	waitStatusDlg();
        }
        return new FormInstancePage(getWebDriverWrapper());
    }

    public ListPage closeMessageClick() throws Exception {
    	if(element("fcp.closeMsg").isDisplayed())
    		element("fcp.closeMsg").click();
        return new ListPage(getWebDriverWrapper());
    }

    public void createCloseClick() throws Exception {
    	if(element("fcp.closeCreateFormWin").isDisplayed()){
    		 element("fcp.closeCreateFormWin").click();
    	}
    }

    public String getErrorMsg() throws Exception {
        return element("fcp.message").getInnerText();
    }
    public void closeCreateNew() throws Exception{
    	element("fcp.cancel").click();
    	waitStatusDlg();
    }

    public boolean initToZeroDisplaye() throws Exception {
        return element("fcp.zero").isDisplayed();
    }
    
    public boolean isCopyDataDisplayed() throws Exception{
    	return element("fcp.copyData").isDisplayed();
    }
}
