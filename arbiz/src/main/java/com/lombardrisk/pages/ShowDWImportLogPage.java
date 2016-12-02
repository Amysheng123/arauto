package com.lombardrisk.pages;

import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray Rui on 5/27/15.
 * Refactored by Leo Tu on 2/1/16
 */
public class ShowDWImportLogPage extends AbstractPage {


    public static final String INVOKEBY = "invoke by";
    public static final String OVERALLSTATUS = "Overall Status";
    public static final String FORMCODE = "Form Code";
    public static final String FORMVERSION = "Form Version";
    public static final String TEMPLATECODE = "Template Code";
    public static final String ENTITYCODE = "Entity Code";
    public static final String REFERENCEDATE = "Reference Date";

    public ShowDWImportLogPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, String> getDWImportInfo() throws Exception {
        List<String> row = new ArrayList<String>();
        String[] list = new String[]{"", ""};
        list[0] = "2";
        for (int j = 1; j <= 5; j++) {
            list[1] = String.valueOf(j);
            row = element("dwp.info", list).getAllInnerTexts();

        }

        list[0] = "5";
        for (int j = 1; j <= 5; j++) {
            list[1] = String.valueOf(j);
            row = element("dwp.info", list).getAllInnerTexts();

        }
        HashMap map = new HashMap();
        map.put(INVOKEBY, row.get(0));
        map.put(OVERALLSTATUS, row.get(3));
        map.put(FORMCODE, row.get(0));
        map.put(FORMVERSION, row.get(1));
        //map.put(FORMCODE,row.get(0));
        map.put(TEMPLATECODE, row.get(2));
        map.put(ENTITYCODE, row.get(3));
        map.put(REFERENCEDATE, row.get(4));
        return map;
    }

    public String getStatus() throws Exception {
        Map<String, String> map = getDWImportInfo();
        return map.get(OVERALLSTATUS);
    }

    public void exportBtnClick() throws Exception {
        element("dwp.export").click();
        waitStatusDlg();
    }

    public void showDetailBtnClick() throws Exception {
        element("dwp.showDetail").click();
        waitStatusDlg();
    }

    public void hideDetailBtnClick() throws Exception {
        element("dwp.hideDetail").click();
        waitStatusDlg();
    }

    public boolean checkDetailLogPanelShowable(int index) throws Exception {
        return element("dwp.logTab").isDisplayed();
    }

    public void setShowNumsSelector(int num) throws Exception {
        if (num == 10 || num == 5) {
            element("dwp.selectDisItem").selectByVisibleText(String.valueOf(num));
        } else {
            throw new Exception("one can only show 5 or 10 messages!");
        }
        waitStatusDlg();
    }

    public void setDetailLogLevel(String logLever) throws Exception {
        element("dwp.logLevel").selectByVisibleText(logLever);
        waitStatusDlg();
    }

    public String getLogNums() throws Exception {

        return element("dwp.logNums").getInnerText().replace("See all ", "").replace(" log entries", "");
    }

    public boolean isErrorMessageExist(String message, String level) throws Exception {
        setShowNumsSelector(100);
        int amt = (int) element("dwp.logsTab").getRowCount();
        boolean findMsg = false;
        boolean flag = true;
        while (flag) {
            try {
                for (int i = 1; i <= amt; i++) {
                    if (element("dwp.message", String.valueOf(i)).getInnerText().equals(message)) {
                        String levelIcon = element("dwp.level", String.valueOf(i)).getAttribute("src");
                        String acctualLevel = null;
                        if (levelIcon.contains("FailIcon"))
                            acctualLevel = "ERROR";
                        else if (levelIcon.contains("WarningIcon"))
                            acctualLevel = "WARN";
                        else if (levelIcon.contains("SuccessIcon"))
                            acctualLevel = "INFO";
                        if (acctualLevel.equalsIgnoreCase(level)) {
                            findMsg = true;
                            flag = false;
                            break;
                        } else {
                            logger.error("Level is incorrect!");
                        }
                    }
                }

                if (!findMsg) {
                    flag = false;

                }
            } catch (NoSuchElementException e) {
                flag = false;
            }

        }
        return findMsg;
    }

    public void closeLogPage() throws Exception {
        //waitThat("dwp.closeLogPage").toBeClickable();
        element("dwp.closeLogPage").click();
        waitStatusDlg();

    }

    public String exportRetrieveLog() throws Exception {
        TestCaseManager.getTestCase().startTransaction("");
        TestCaseManager.getTestCase().setPrepareToDownload(true);
        element("dwp.export").click();
        TestCaseManager.getTestCase().stopTransaction();
        waitStatusDlg();
        return TestCaseManager.getTestCase().getDownloadFile();
    }


}
