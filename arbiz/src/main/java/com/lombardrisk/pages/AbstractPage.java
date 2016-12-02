package com.lombardrisk.pages;

import com.lombardrisk.utils.DBQuery;
import org.openqa.selenium.Keys;
import org.yiwan.webcore.util.PropHelper;
import org.yiwan.webcore.web.IWebDriverWrapper;
import org.yiwan.webcore.web.PageBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Ling on 2/17/15.
 * Refactored by Leo Tu on 1/29/16
 */
public abstract class AbstractPage extends PageBase {

    static String DBType = PropHelper.getProperty("db.type").trim();
    static String connectedDB = PropHelper.getProperty("db.connectedDB").trim();

    public AbstractPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    protected void waitStatusDlg() throws Exception {
        try {
        	waitThat().timeout(400);
            if (element("ap.ajaxstatusDlg").isDisplayed()) {
                waitThat("ap.ajaxstatusDlg").toBeInvisible();
                waitThat().timeout(200);
            }

            if (element("lp.ajaxstatusDlg_modal").isDisplayed()) {
                waitThat("lp.ajaxstatusDlg_modal").toBeInvisible();
                waitThat().timeout(200);
            }
        } catch (Exception e) {
        }
    }


    protected List<String> splitReturn(String returnName) throws Exception {
        List<String> returnNV = new ArrayList<>();
        String formCode;
        String formVersion;
        String Form;
        if (returnName.trim().contains(" ")) {
            formCode = returnName.split(" ")[0];
            formVersion = returnName.split(" ")[1].trim().toLowerCase().replace("v", "");
            Form = formCode + " v" + formVersion;
        } else {
            formCode = returnName.split("_")[0];
            formVersion = returnName.split("_")[1].trim().toLowerCase().replace("v", "");
            Form = formCode + "_v" + formVersion;
        }

        returnNV.add(formCode);
        returnNV.add(formVersion);
        returnNV.add(Form);
        return returnNV;
    }

    protected void clickEnterKey() throws Exception {
        actions().sendKeys(Keys.ENTER).perform();
    }


    protected void clickToday() throws Exception {
        for (int r = 1; r <= 6; r++) {
            boolean clicked = false;
            for (int c = 1; c <= 7; c++) {
                String[] list = {String.valueOf(r), String.valueOf(c)};
                if (element("ap.calendar", list).getAttribute("class").contains("ui-datepicker-current-day")) {
                    element("ap.calendar", list).click();
                    clicked = true;
                    break;
                }
            }
            if (clicked)
                break;
        }
    }

    protected String getRegulatorIDRangeStart(String Regulator) {
        String SQL = "SELECT \"ID_RANGE_START\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
        if (connectedDB.equalsIgnoreCase("ar"))
            return DBQuery.queryRecord(SQL);
        else
            return null;

    }

    protected String getRegulatorIDRangEnd(String Regulator) {
        String SQL = "SELECT \"ID_RANGE_END\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "' AND \"STATUS\"='A'  ";
        if (connectedDB.equalsIgnoreCase("ar"))
            return DBQuery.queryRecord(SQL);
        else
            return null;
    }

    protected String getToolsetRegPrefix(String Regulator) {
    	/*
        String SQL = "SELECT \"TOOLSET_REG_PREFIX\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
        return DBQuery.queryRecordSpecDB(PropHelper.getProperty("db.apDBName").trim(), SQL);*/
    	if (Regulator.equalsIgnoreCase("European Common Reporting"))
    		return "ECR";
    	else
    		return "";
    }

    public String getExtendCellName(String Regulator, String formCode, String version, String cellName) {
        if (connectedDB.equalsIgnoreCase("ar")) {
            String ID_Start = getRegulatorIDRangeStart(Regulator);
            String ID_End = getRegulatorIDRangEnd(Regulator);
            String SQL = "select \"GridName\" from \"CFG_RPT_GridRef\" "
                    + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") "
                    + "and \"Item\"='" + cellName + "'";
            return DBQuery.queryRecord(SQL);
        } else {
            String RegPrefix = getToolsetRegPrefix(Regulator);
            String SQL = "select \"GridName\" from \"" + RegPrefix + "GridRef\" "
                    + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + " ) "
                    + "and \"Item\"='" + cellName + "'";
            return DBQuery.queryRecord(SQL);
        }
    }

}
