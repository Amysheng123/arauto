package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by zhijun dai on 9/23/2016.
 */
public class NonWorkingDaysPatternPage extends AbstractPage
{
	/**
	 * @param webDriverWrapper
	 */
	public NonWorkingDaysPatternPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * Input description value
	 */
	public void inputDescField(String value) throws Exception
	{
		element("nwdp.descField").type(value);
	}

	/**
	 * Click daily add button
	 */
	public void clickDailyAddBtn(String type) throws Exception
	{
		element("nwdp.dailyAddBtn", type).click();
		waitStatusDlg();
	}

	/**
	 * Click save button
	 */
	public CalendarPage clickSaveBtn() throws Exception
	{
		element("nwdp.saveBtn").click();
		waitStatusDlg();
		return new CalendarPage(getWebDriverWrapper());
	}
}
