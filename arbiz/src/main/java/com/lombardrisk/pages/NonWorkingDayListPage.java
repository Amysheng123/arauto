package com.lombardrisk.pages;

import org.openqa.selenium.Keys;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactor by Leo Tu on 1/29/16
 */

public class NonWorkingDayListPage extends AbstractPage {

    public NonWorkingDayListPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
    }

    protected NonWorkingDayListPage setDescription(String description) throws Exception {
        element("nwp.DESC").type(description);
        return this;
    }

    protected NonWorkingDayListPage setFixDate(String processDate) throws Exception {
        element("nwp.date").type(processDate);
        element("nwp.date").type(Keys.ENTER);
        element("nwp.date").click();
        return this;
    }

    protected void saveButtonClick() throws Exception {
        element("nwp.save").click();
        waitStatusDlg();
    }

    protected CalendarPage cancelButtonClick() throws Exception {
        element("nwp.cancel").click();
        return new CalendarPage(getWebDriverWrapper());
    }

}
