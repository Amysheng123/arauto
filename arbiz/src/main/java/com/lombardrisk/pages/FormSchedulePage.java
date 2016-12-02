package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

public class FormSchedulePage extends AbstractPage {

    public FormSchedulePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
        // TODO Auto-generated constructor stub
    }

    protected FormSchedulePage setRegulator(String regulator) throws Exception {
        element("fsp.regulator").type(regulator);
        return this;
    }

    protected FormSchedulePage setForm(String form) throws Exception {
        element("fsp.form").type(form);
        return this;
    }

    protected FormSchedulePage setGroup(String group) throws Exception {
        element("fsp.group").type(group);
        return this;
    }

    protected FormSchedulePage setSchedule(String schedule) throws Exception {
        element("fsp.schedule").type(schedule);
        return this;
    }

    protected FormSchedulePage formScheduleAddClick() throws Exception {
        element("fsp.addBtn").click();
        waitStatusDlg();
        return this;
    }


}
