package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ray Rui on 3/2/15.
 * Refactored by Leo Tu on 1/28/16
 */
public class ErrorListPage extends AbstractDrilldownPage {


    public ErrorListPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
    }

    public String getRuleNo(int index) throws Exception {
        return element("elp.ruleNO", String.valueOf(index)).getInnerText().split(" ")[1];
    }

    public String getLevelText(int index) throws Exception {
        return element("elp.ruleLevel", String.valueOf(index)).getInnerText().trim();
    }

    public String getMessageText(int index) throws Exception {
        return element("elp.message", String.valueOf(index)).getInnerText().trim();
    }

    public List<String> getErrorInfo(String instance, String ruleType, String ruleID, String rowKey) throws Exception {
        List<String> ErrorInfo = new ArrayList<String>();
        boolean flag = true;
        int rowAmt = (int) element("elp.problemTab").getRowCount();
        boolean find = false;
        boolean lastPage = false;
        int count = 0;
        String startPage = null;
        String curPage = null;
        for (int i = 1; i <= 3; i++) {
            if (element("elp.curPageNO", String.valueOf(i)).getAttribute("class").contains("ui-state-active")) {
                startPage = element("elp.curPageNO", String.valueOf(i)).getInnerText();
                break;
            }
        }
        while (flag) {
            for (int i = 1; i <= rowAmt; i++) {
                String ID = getRuleNo(i);
                if (ID.equals(ruleID)) {
                    if (rowKey.equals("")) {
                        String Msg = getMessageText(i);
                        if (!instance.equals("")) {
                            String instanceName = Msg.substring(Msg.indexOf(":") + 1, Msg.indexOf(":") + 2);
                            if (instanceName.equals(instance)) {
                                ErrorInfo.add(getLevelText(i));
                                ErrorInfo.add(Msg);
                                find = true;
                                flag = false;
                                break;
                            }
                        } else {
                            ErrorInfo.add(getLevelText(i));
                            ErrorInfo.add(Msg);
                            find = true;
                            flag = false;
                            break;
                        }
                    } else {
                        String Msg = getMessageText(i);
                        if (!Msg.startsWith("It is not allowed that a \"for each instance\"")) {
                            String instanceName = Msg.substring(Msg.indexOf(":") + 1, Msg.indexOf(":") + 2);
                            String Msg_temp = Msg.replace("[PageInstance:" + instanceName + "]", "");
                            String rowID = Msg_temp.substring(Msg_temp.indexOf(":") + 1, Msg_temp.indexOf(":") + 2);

                            if (!instance.equals("")) {
                                if (rowID.equals(rowKey) && instanceName.equals(instance)) {
                                    ErrorInfo.add(getLevelText(i));
                                    ErrorInfo.add(Msg);
                                    find = true;
                                    flag = false;
                                    break;
                                }
                            } else {
                                if (rowID.equals(rowKey)) {
                                    ErrorInfo.add(getLevelText(i));
                                    ErrorInfo.add(Msg);
                                    find = true;
                                    flag = false;
                                    break;
                                }
                            }
                        } else {
                            ErrorInfo.add(getLevelText(i));
                            ErrorInfo.add(Msg);
                            find = true;
                            flag = false;
                            break;
                        }

                    }

                }

            }

            if (count > 0) {

                for (int i = 1; i <= 3; i++) {
                    if (element("elp.curPageNO", String.valueOf(i)).getAttribute("class").contains("ui-state-active")) {
                        curPage = element("elp.curPageNO", String.valueOf(i)).getInnerText();
                        break;
                    }
                }

                if (Integer.parseInt(curPage) - Integer.parseInt(startPage) >= 0) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                try {
                    if (element("elp.nextPage").getAttribute("tabindex").equals("0")) {
                        element("elp.nextPage").click();
                        waitStatusDlg();
                    } else {
                        lastPage = true;
                    }
                } catch (Exception e) {
                    flag = false;
                }
            }

            if (!find && lastPage && count < 1) {
                try {
                    if (element("elp.firtPage").getAttribute("tabindex").equals("0")) {
                        element("elp.firtPage").click();
                        count++;

                        for (int i = 1; i <= 3; i++) {
                            if (element("elp.curPageNO", String.valueOf(i)).getAttribute("class").contains("ui-state-active")) {
                                curPage = element("elp.curPageNO", String.valueOf(i)).getInnerText();
                                break;
                            }
                        }
                        if (Integer.parseInt(curPage) - Integer.parseInt(startPage) >= 0) {
                            flag = false;
                            break;
                        }

                    }
                } catch (Exception e) {

                }
            }

            if (!find && count > 0 && Integer.parseInt(curPage) - Integer.parseInt(startPage) >= 0) {
                flag = false;
            }

            rowAmt = (int) element("elp.problemTab").getRowCount();
        }

        return ErrorInfo;
    }

    public void clickRuleNO(String ruleNo) throws Exception {
        logger.info("Click rule[" + ruleNo + "] in PROBELMS");
        if (!element("elp.firtPage").getAttribute("class").contains("ui-state-disabled")) {
            element("elp.firtPage").click();
            waitStatusDlg();
            waitThat().timeout(300);
        }
        if (!element("elp.nextPage").getAttribute("class").contains("ui-state-disabled")) {
            while (!element("elp.nextPage").getAttribute("class").contains("ui-state-disabled")) {
                if (element("elp.ruleNo", ruleNo).isDisplayed()) {
                    element("elp.ruleNo", ruleNo).click();
                    waitStatusDlg();
                } else {
                    element("elp.nextPage").click();
                    waitStatusDlg();
                    waitThat().timeout(300);
                }
            }
        } else {
            element("elp.ruleNo", ruleNo).click();
            waitStatusDlg();
        }

    }

    public String getRuleBackgroudColor(String ruleNo) throws Exception {
        String color = null;
        if (element("elp.firtPage").getAttribute("class").contains("ui-state-disabled")) {
            element("elp.firtPage").click();
            waitStatusDlg();
            waitThat().timeout(300);
        }
        if (!element("elp.nextPage").getAttribute("class").contains("ui-state-disabled")) {
            while (!element("elp.nextPage").getAttribute("class").contains("ui-state-disabled")) {
                if (element("elp.ruleNo", ruleNo).isDisplayed()) {
                    color = element("elp.ruleBackgroud", ruleNo).getCssValue("background-color");
                    break;
                } else {
                    element("elp.nextPage").click();
                    waitStatusDlg();
                    waitThat().timeout(300);
                }
            }
        } else {
            color = element("elp.ruleBackgroud", ruleNo).getCssValue("background-color");
        }

        return color;
    }

}
