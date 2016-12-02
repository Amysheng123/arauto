package com.lombardrisk.pages;


import org.openqa.selenium.By;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Kevin Ling on 3/3/15.
 * Refactored by Leo Tu on 1/29/16
 */
public class ListImportFilePage extends AbstractImportPage {


    public ListImportFilePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public String getErrorMessage(String type) throws Exception {
    	String msg="";
        if (element("lif.error", type).isDisplayed())
        	msg= element("lif.error", type).getInnerText();
        else if (element("lif.error2").isDisplayed())
        	msg= element("lif.error2").getInnerText();
       
        waitThat().timeout(2500);
        return msg;

    }

    public boolean isExistErrorMessage(String type) throws Exception {
    	boolean exist=false;
        if (element("lif.error", type).isDisplayed() || element("lif.error2").isDisplayed())
        	exist=true;
        waitThat().timeout(2500);
        return exist;
    }

    public boolean isExistErrorInfo() throws Exception {
        if (element("lif.error2").isDisplayed())
            return true;
        else
            return false;
    }

    public String getErrorInfo(String type) throws Exception {
        return element("lif.createFromXLSError", type).getInnerText();
    }

    public void closeImportFileDlg(String type) throws Exception {
        if (element("lif.closeImportDlg", type).isDisplayed()) {
            waitThat("lif.closeImportDlg", type).toBeClickable();
            element("lif.closeImportDlg", type).click();
        } else if (element("lif.closeImportDlg2", type).isDisplayed()) {
            waitThat("lif.closeImportDlg2", type).toBeClickable();
            element("lif.closeImportDlg2", type).click();
        }
    }

    public boolean isExistIniToZero(String type) throws Exception {
        logger.info("Check if exits InitialiseToZeros checkbox");
        if (type.equalsIgnoreCase("createFromExcelForm"))
            return element("lif.zero1").isDisplayed();
        else if (type.equalsIgnoreCase("listImportFileForm"))
            return element("lif.zero2").isDisplayed();
        else if (type.equalsIgnoreCase("importFileForm"))
            return element("lif.zero3").isDisplayed();
        else
            return false;
    }

    public void assertExistIniToZero(String type) throws Exception {
        logger.info("Check if exits InitialiseToZeros checkbox");
        if (type.equalsIgnoreCase("createFromExcelForm"))
            assertThat("lif.zero1").displayed().isTrue();
        else if (type.equalsIgnoreCase("listImportFileForm"))
            assertThat("lif.zero2").displayed().isTrue();
        else if (type.equalsIgnoreCase("importFileForm"))
            assertThat("lif.zero3").displayed().isTrue();
        else
            throw new IllegalArgumentException(type);
    }

    @Override
    public String parentFormId(String type) {
        String rst = null;
        if (type.equalsIgnoreCase("List")) {
            rst = "listImportFileForm";
        } else if (type.equalsIgnoreCase("Return")) {
            rst = "importFileForm";
        } else if (type.equalsIgnoreCase("Create")) {
            rst = "createFromExcelForm";
        }
        return rst;
    }

    @Override
    public By getImportBtn(String type) {

        return By.id(type + ":listimportBtn");
    }

    public void importFileCheckbox() throws Exception {

    }


    public void tickInitToZero(String type) throws Exception {
        logger.info("Tick initialise to zeros checkbox.");
        if (type.equalsIgnoreCase("listImportFileForm") || type.equalsIgnoreCase("createFromExcelForm"))
            element("lif.zero", type).click();
        else
            element("lif.zero2").click();
    }

    public void confirmClick() throws Exception {
        element("lif.confirm").click();
    }

    public void selectAddToExistingValue(String type) throws Exception {
        logger.info("Click add to exits value");
        element("lif.addToValue", type).click();
        waitThat().timeout(500);
    }

}
