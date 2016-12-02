package com.lombardrisk.pages;

import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 1/25/16
 */

public class CalendarPage extends AbstractPage
{

	public CalendarPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	public CalendarPage calendarAddClick() throws Exception
	{
		element("cal.calendarAdd").click();
		waitStatusDlg();
		return this;
	}

	public CalendarPage inputCalendarName(String name) throws Exception
	{
		element("cal.CalendarName").input(name);
		return this;
	}

	public String saveCalendarClick() throws Exception
	{
		element("cal.save").click();
		waitStatusDlg();
		waitThat("cal.messageTitle").toBeVisible();
		String msg = element("scp.promptMsg").getInnerText();
		waitThat("cal.messageTitle").toBeInvisible();
		return msg;
	}

	public NonWorkingDayListPage nonWorkingDaysListAddClick() throws Exception
	{
		element("cal.nonWorkingDaysAdd").click();
		waitStatusDlg();
		return new NonWorkingDayListPage(getWebDriverWrapper());
	}

	public boolean isCalendarExist(String calendarName) throws Exception
	{
		return element("cal.existCal", calendarName).isDisplayed();
	}

	public void deleteCalendar(String calendarName) throws Exception
	{
		element("cal.delBtn", calendarName).click();
		waitStatusDlg();
		element("cal.deleteConfirm").click();
		waitStatusDlg();
	}

	/**
	 * Click delete calendar button
	 */
	public void clickDeleteCal(String calName) throws Exception
	{
		element("cal.delBtn", calName).click();
		waitStatusDlg();
	}

	/**
	 * Cancel delete calendar
	 */
	public void cancelDeleteCal() throws Exception
	{
		element("cal.deleteCancel").click();
		waitStatusDlg();
	}

	/**
	 * Confirm delete canendar
	 */
	public void confirmDeleteCal() throws Exception
	{
		element("cal.deleteConfirm").click();
		waitStatusDlg();
	}

	/**
	 * Get the delete message
	 */
	public String getDeleteMsg() throws Exception
	{
		return element("cal.deleteMsg").getInnerText();
	}

	/**
	 * Check the special lable is exist or not
	 */
	public boolean isSpecialLabelExist(String text) throws Exception
	{
		return element("cal.labelName", text).isDisplayed();
	}

	/**
	 * Check the edit button of special calendar is exist or not
	 */
	public boolean isEditButtonExist(String calendar) throws Exception
	{
		return element("cal.editBtn", calendar).isDisplayed();
	}

	/**
	 * Check the delete button of special calendar is exist or not
	 */
	public boolean isDelButtonExist(String calendar) throws Exception
	{
		return element("cal.delBtn", calendar).isDisplayed();
	}

	/**
	 * Check the add calendar button is exist or not
	 */
	public boolean isAddCalButtonExist() throws Exception
	{
		return element("cal.calendarAdd").isDisplayed();
	}

	/**
	 * Check the save calendar button is exist or not
	 */
	public boolean isSaveCalButtonExist() throws Exception
	{
		return element("cal.save").isDisplayed();
	}

	/**
	 * Back to dashboard
	 */
	public ListPage backToDashboard() throws Exception
	{
		element("cal.dashboard").click();
		waitStatusDlg();
		waitThat().timeout(500);
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * Select weekends
	 */
	public void selectWeekends(String... str) throws Exception
	{
		for (int i = 0; i < str.length; i++)
		{
			if (!isWeekendsChecked(str[i]))
			{
				element("cal.weekendsCheckboxIcon", str[i]).click();
				waitStatusDlg();
			}
		}
	}

	/**
	 * Deselect weekends
	 */
	public void deselectWeekends(String... str) throws Exception
	{
		for (int i = 0; i < str.length; i++)
		{
			if (isWeekendsChecked(str[i]))
			{
				element("cal.weekendsCheckboxIcon", str[i]).click();
				waitStatusDlg();
			}
		}
	}

	/**
	 * Check weekends is selected or not
	 */
	public boolean isWeekendsChecked(String str) throws Exception
	{
		return element("cal.weekendsCheckbox", str).isChecked();
	}

	/**
	 * Click edit button of the calendar
	 */
	public void clickEditButtonOfCalendar(String calName) throws Exception
	{
		element("cal.editBtn", calName).click();
		waitStatusDlg();
	}

	/**
	 * Get the exist non working days list
	 */
	public List<String> getExistNonWorkingDaysList() throws Exception
	{
		return element("cal.existNonWorkingDayList").getAllInnerTexts();
	}

	/**
	 * Click non working days pattern page
	 */
	public NonWorkingDaysPatternPage clickNonWorkingDaysPatternAddBtn() throws Exception
	{
		element("cal.addNonWorkingDayPatBtn").click();
		waitStatusDlg();
		return new NonWorkingDaysPatternPage(getWebDriverWrapper());
	}
}
