package com.lombardrisk.pages;

import org.openqa.selenium.StaleElementReferenceException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Ray Rui on 2/28/15.
 * Refactored by Leo Tu on 1/25/16
 */
public class AdjustLogPage extends AbstractPage {


    public AdjustLogPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public void inputCellText(String text) throws Exception {
        waitThat().timeout(1000);
        element("alp.cellInput").input(text);
        clickEnterKey();
        waitThat().timeout(500);
    }

    public String getFilteCell() throws Exception {
        return element("alp.cellInput").getAttribute("value");
    }

    public void inputUserText(String text) throws Exception {
        element("alp.userInput").type(text);
        clickEnterKey();
        waitThat().timeout(500);
    }

    public void inputFromDateText(String text) throws Exception {
        element("alp.fromDateInput").type(text);
        clickEnterKey();
        waitThat().timeout(500);
    }

    public void inputToDateText(String text) throws Exception {
        element("alp.toDateInput").type(text);
        clickEnterKey();
        waitThat().timeout(500);
    }

    public void clearImageClick() throws Exception {
        element("clear").click();
        waitStatusDlg();
    }

    public int getLogNums() throws Exception {
        int Nums = (int) element("alp.logTable").getRowCount();
        if (Nums == 1) {
            if (element("alp.noRecords").getInnerText().equals("No records found."))
                Nums = 0;
        }
        return Nums;
    }

    public String getCellText() throws Exception {
        try {
            return element("alp.cellInput").getAttribute("value").trim();
        } catch (StaleElementReferenceException e) {
            waitThat().timeout(500);
            return getCellText();
        }
    }

    public String getUserText() throws Exception {
        try {
            return element("alp.userInput").getAttribute("value").trim();
        } catch (StaleElementReferenceException e) {
            waitThat().timeout(500);
            return getUserText();
        }
    }

    public String getFromDateText() throws Exception {
        try {
            return element("alp.fromDateInput").getAttribute("value").trim();
        } catch (StaleElementReferenceException e) {
            waitThat().timeout(500);
            return getFromDateText();
        }
    }

    public String getToDateText() throws Exception {
        try {
            return element("alp.toDateInput").getAttribute("value").trim();
        } catch (StaleElementReferenceException e) {
            waitThat().timeout(500);
            return getToDateText();
        }
    }

    public void orderByTimeAsc() throws Exception {
        while (element("alp.SBT").getAttribute("class").contains("ui-icon-triangle-1-n")) {
            element("alp.SBT").click();
            waitStatusDlg();
        }

    }

    public void orderByTimeDesc() throws Exception {
        while (element("alp.SBT").getAttribute("class").contains("ui-icon-triangle-1-s")) {
            element("alp.SBT").click();
            waitStatusDlg();
        }
    }

    public String getCellName(int index) throws Exception {
        if (element("alp.cellName", String.valueOf(index)).isDisplayed())
            return element("alp.cellName", String.valueOf(index)).getInnerText();
        else
            return element("alp.cellName2", String.valueOf(index)).getInnerText();
    }

    public String getInstance(int index) throws Exception {
        return element("alp.instance", String.valueOf(index)).getInnerText();
    }

    public String getGrdiKey(int index) throws Exception {
        if (element("alp.gridKey", String.valueOf(index)).isDisplayed())
            return element("alp.gridKey", String.valueOf(index)).getInnerText();
        else
            return "";
    }

    public String getValue(int index) throws Exception {
        try {
            if (element("alp.value2", String.valueOf(index)).isDisplayed())
                return element("alp.value2", String.valueOf(index)).getInnerText();
            else
                return element("alp.value", String.valueOf(index)).getInnerText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getModifiedTo(int index) throws Exception {
        try {
            if (element("alp.modifiedTo", String.valueOf(index)).isDisplayed())
                return element("alp.modifiedTo", String.valueOf(index)).getInnerText();
            else
                return element("alp.modifiedTo2", String.valueOf(index)).getInnerText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getEditTime(int index) throws Exception {
        try {
            return element("alp.editTime", String.valueOf(index)).getInnerText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getUser(int index) throws Exception {
        try {
            return element("alp.user", String.valueOf(index)).getInnerText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getComment(int index) throws Exception {
        try {
            return element("alp.comment", String.valueOf(index)).getInnerText();
        } catch (Exception e) {
            return "";
        }
    }

    public String exportAdjustment() throws Exception {
        waitThat("alp.export").toBeClickable();
        TestCaseManager.getTestCase().startTransaction("");
        TestCaseManager.getTestCase().setPrepareToDownload(true);
        element("alp.export").click();
        TestCaseManager.getTestCase().stopTransaction();
        waitStatusDlg();
        return TestCaseManager.getTestCase().getDownloadFile();
    }


}
