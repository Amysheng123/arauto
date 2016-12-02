package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactor by Leo Tu on 1/29/16
 */

public class NonWorkingDayListPage extends AbstractPage
{

	public NonWorkingDayListPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	public NonWorkingDayListPage setDescription(String description) throws Exception
	{
		element("nwp.DESC").type(description);
		return this;
	}

	public NonWorkingDayListPage setFixDate(String processDate) throws Exception
	{
		element("nwp.date").type(processDate);
		element("nwp.date").click();
		Thread.sleep(300);
		selectDate(processDate);
		return this;
	}

	public CalendarPage saveButtonClick() throws Exception
	{
		element("nwp.save").click();
		waitStatusDlg();
		return new CalendarPage(getWebDriverWrapper());
	}

	public CalendarPage cancelButtonClick() throws Exception
	{
		element("nwp.cancel").click();
		return new CalendarPage(getWebDriverWrapper());
	}

}
