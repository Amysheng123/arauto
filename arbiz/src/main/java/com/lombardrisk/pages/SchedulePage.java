package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 2/1/16
 */

public class SchedulePage extends AbstractPage {

    public SchedulePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
        // TODO Auto-generated constructor stub
    }

    public SchedulePage addScheduleClick() throws Exception {
        element("scp.ASB").click();
        waitStatusDlg();
        return this;
    }

    public SchedulePage addScheduleName(String name) throws Exception {
        element("scp.name").type(name);
        return this;
    }

    public SchedulePage addScheduleDescription(String name) throws Exception {
        element("scp.DESC").type(name);
        return this;
    }

    public SchedulePage saveScheduleClick() throws Exception {
        element("scp.save").click();
        return this;
    }

}
