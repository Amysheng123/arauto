package com.lombardrisk.pages;

import org.yiwan.webcore.locator.Locator;
import org.yiwan.webcore.web.IWebDriverWrapper;

import java.util.List;

/**
 * Created by leo tu on 4/13/2016.
 */
public class ComputePage extends AbstractPage {
    public ComputePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
    }

    protected void setEntity(String entity) throws Exception {
        element("cp.entity").selectByVisibleText(entity);
        waitStatusDlg();
    }

    public void setReferencedate(String processDate) throws Exception {
        element("cp.date").input(processDate);
        clickToday();
        waitThat().timeout(1000);
    }

    protected void setform(String form) throws Exception {
        element("cp.form").selectByVisibleText(form);
        waitStatusDlg();
    }


    public String getErrorMessage(String entity, String processDate, String form) throws Exception {
        setEntity(entity);
        setReferencedate(processDate);
        setform(form);
        element("cp.computeBtn").click();
        waitStatusDlg();
        return element("cp.errorMsg").getInnerText();
    }

    public FormInstancePage computeReturn(String entity, String processDate, String form, boolean showProgress) throws Exception {
        setEntity(entity);
        setReferencedate(processDate);
        setform(form);
        if (showProgress) {
            element("cp.showProgress").click();
            waitThat().timeout(300);
        }
        element("cp.computeBtn").click();
        waitStatusDlg();
        return new FormInstancePage(getWebDriverWrapper());
    }

    public FormInstancePage computeReturn(String entity, String processDate, String form, boolean showProgress, boolean showConfirm) throws Exception {
        setEntity(entity);
        setReferencedate(processDate);
        setform(form);
        if (showProgress) {
            element("cp.showProgress").click();
            waitThat().timeout(300);
        }
        element("cp.computeBtn").click();
        waitStatusDlg();
        if (showConfirm)
            assertThat((Locator) element("cp.confirmMsg")).innerText().isEqualTo("Overwriting existing forms:" + form.split(" ")[0]);
        return new FormInstancePage(getWebDriverWrapper());
    }

    public List<String> getForms(String entity, String processDate) throws Exception {
        setEntity(entity);
        setReferencedate(processDate);
        return element("cp.form").getAllInnerTexts();
    }
}
