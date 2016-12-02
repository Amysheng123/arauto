package com.lombardrisk.pages;

import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Ray Rui on 3/3/15.
 * Refactored by Leo Tu on 1/25/16
 */
public class AllocationPage extends AbstractPage {


    public AllocationPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public String getSumCellValue() throws Exception {
        return element("ac.sumValue").getInnerText();
    }

    public String getSumRule() throws Exception {
        return element("ac.sumRule").getInnerText();
    }


    public boolean isSubItemValueExist(String cellName, String cellValue) throws Exception {
        boolean rst = false;
        int amt = (int) element("ac.allocation").getRowCount();
        for (int i = 0; i <= amt; i++) {
            if (element("ac.subCellName", String.valueOf(i)).getInnerText().equalsIgnoreCase(cellName)) {
                if (element("ac.subCellValue", String.valueOf(i)).getInnerText().equalsIgnoreCase(cellValue)) {
                    rst = true;
                    break;
                }
            }

        }
        return rst;
    }

    public boolean isAllocationItemValueExist(String CALCULATED, String CUSTOMER_CODE, String DRILL_REF) throws Exception {
        boolean find = false;
        int amt = (int) element("ac.allocationLeft").getRowCount();
        if (!element("ac.firstPage").getAttribute("class").contains("ui-state-disabled")) {
            element("ac.firstPage").click();
            waitStatusDlg();
        }
        boolean flag = true;
        while (flag) {

            for (int i = 1; i <= amt; i++) {
                if (element("ac.calculated", String.valueOf(i)).getInnerText().equals(CALCULATED)) {
                    if (element("ac.customerCode", String.valueOf(i)).getInnerText().equals(CUSTOMER_CODE)) {
                        if (element("ac.drillRef", String.valueOf(i)).getInnerText().equals(DRILL_REF)) {
                            find = true;
                            flag = false;
                            break;
                        }
                    }
                }
            }


            if (!find) {
                if (!element("ac.nextPage").getAttribute("class").contains("ui-state-disabled")) {
                    element("ac.nextPage").click();
                    waitStatusDlg();
                    amt = (int) element("ac.allocationLeft").getRowCount();
                } else {
                    flag = false;
                }
            }

        }
        return find;
    }


    public String exportAllocation() throws Exception {
        waitThat().timeout(3000);
        TestCaseManager.getTestCase().startTransaction("");
        TestCaseManager.getTestCase().setPrepareToDownload(true);
        element("ac.export").click();
        TestCaseManager.getTestCase().stopTransaction();
        element("fp.hidDrillDownTable").click();
        waitStatusDlg();
        return TestCaseManager.getTestCase().getDownloadFile();
    }

    public void clickCellLink(String cellName) throws Exception {
        element("ac.cellLink", cellName).click();
        waitStatusDlg();
    }

}
