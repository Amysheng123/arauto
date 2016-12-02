package com.lombardrisk.pages;

import org.openqa.selenium.By;
import org.yiwan.webcore.web.IWebDriverWrapper;

import java.io.File;

/**
 * Created by Ray Rui on 5/25/15.
 * Refactored by Leo Tu on 1/25/16
 */
public abstract class AbstractImportPage extends AbstractPage {


    public AbstractImportPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public void setImportFile(File file, String type) throws Exception {
        logger.info("Execute js script");
        waitThat().timeout(1000);
        executeScript("document.getElementById('" + getUploadId(type) + "').getElementsByTagName('div')[0].getElementsByTagName('span')[0].className='';");
        element("aip.uploadInput", type).type(file.getAbsolutePath());
        waitUploading(type);
    }

    public abstract String parentFormId(String type);

    public abstract By getImportBtn(String type);

    public String getUploadId(String type) throws Exception {
        return type + ":importFileUpload";
    }


    public String getUploadInputId(String type) throws Exception {
        return type + ":importFileUpload_input";
    }

    public String getErrorTextarea(String type) throws Exception {
        return type + ":errorTextarea";
    }

    public String getErrorText(String type) throws Exception {
        return element("aip.error", type).getInnerText();
    }


    public ImportConfirmPage importFileBtnClick(String type) throws Exception {
        if (element("aip.import", type).isDisplayed())
            element("aip.import", type).click();

        else if (element("aip.import2", type).isDisplayed())
            element("aip.import2", type).click();
        waitStatusDlg();
        waitThat().timeout(300);
        return new ImportConfirmPage(getWebDriverWrapper());
    }


    public void waitUploading(String type) throws Exception {
        long beginTime = System.currentTimeMillis();
        boolean flag = true;
        while (flag) {
            if (!element("aip.uploadFileName1", type).isDisplayed())
                flag = false;
            if (!element("aip.uploadFileName2", type).isDisplayed())
                flag = false;

            long curTime = System.currentTimeMillis();
            long spendTime = (curTime - beginTime) / 1000;
            if (spendTime > 600) {
                logger.info("Spend " + spendTime + " seconds to upload file");
                flag = false;
            } else {
                logger.info("Uploading, take " + spendTime + " seconds");
            }
        }
        waitThat().timeout(1000);
    }

}
