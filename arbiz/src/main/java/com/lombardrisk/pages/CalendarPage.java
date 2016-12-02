package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 1/25/16
 */

public class CalendarPage extends AbstractPage {


    public CalendarPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public CalendarPage calendarAddClick() throws Exception {
        element("cal.calendarAdd").click();
        waitStatusDlg();
        return this;
    }

    public CalendarPage inputCalendarName(String name) throws Exception {
        element("cal.CalendarName").input(name);
        return this;
    }

    public void saveCalendarClick() throws Exception {
        element("cal.save").click();
        waitStatusDlg();
    }

    public NonWorkingDayListPage nonWorkingDaysListAddClick() throws Exception {
        element("cal.nonWorkingDaysAdd").click();
        return new NonWorkingDayListPage(getWebDriverWrapper());
    }


    public boolean isCalendarExist(String calendarName) throws Exception {
        boolean rst = false;
        int amt = (int) element("cal.calendarTable").getRowCount();
        for (int i = 1; i <= amt; i++) {
            if (element("cal.getCalName").getInnerText().equalsIgnoreCase(calendarName)) {
                rst = true;
                break;
            }
        }
        return rst;
    }

    public void deleteCalendar(String calendarName) throws Exception {
        int amt = (int) element("cal.calendarTable").getRowCount();
        for (int i = 1; i <= amt; i++) {
            if (element("cal.getCalName", String.valueOf(i)).getInnerText().equalsIgnoreCase(calendarName)) {
                element("cal.delCal").click();
                waitStatusDlg();
                element("cal.deleteConfirm").click();
                break;
            }

        }
    }
}
