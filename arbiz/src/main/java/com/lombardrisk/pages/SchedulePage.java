package com.lombardrisk.pages;

import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 2/1/16
 */

public class SchedulePage extends AbstractPage
{

	public SchedulePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	public SchedulePage addScheduleClick() throws Exception
	{
		element("scp.ASB").click();
		waitStatusDlg();
		return this;
	}

	public SchedulePage addScheduleName(String name) throws Exception
	{
		element("scp.name").type(name);
		return this;
	}

	public SchedulePage addScheduleDescription(String name) throws Exception
	{
		element("scp.DESC").type(name);
		return this;
	}

	public String saveScheduleClick() throws Exception
	{
		element("scp.save").click();
		waitStatusDlg();
		waitThat("scp.messageTitle").toBeVisible();
		String msg = element("scp.promptMsg").getInnerText();
		waitThat("scp.messageTitle").toBeInvisible();
		return msg;
	}

	/**
	 * Is edit schedule button exist or not
	 */
	public boolean isEditSchBtnExist(String scheduleName) throws Exception
	{
		return element("scp.editBtn", scheduleName).isDisplayed();
	}

	/**
	 * Is delete schedule button exist or not
	 */
	public boolean isDelSchBtnExist(String scheduleName) throws Exception
	{
		return element("scp.delBtn", scheduleName).isDisplayed();
	}

	/**
	 * click edit button of special schedule name
	 */
	public void clickEditButtonOfSchedule(String scheduleName) throws Exception
	{
		element("scp.editBtn", scheduleName).click();
		waitStatusDlg();
	}

	/**
	 * Click delete button of special schedule name
	 */
	public void clickDeleteButtonOfSchedule(String scheduleName) throws Exception
	{
		element("scp.delBtn", scheduleName).click();
		waitStatusDlg();
	}

	/**
	 * Get delete message on the delete dialog
	 */
	public String getDeleteMsg() throws Exception
	{
		return element("scp.deleteMsg").getInnerText();
	}

	/**
	 * Cancel delete schedule
	 */
	public void cancelDeleteSch() throws Exception
	{
		element("scp.cancelDeleteBtn").click();
		waitStatusDlg();
	}

	/**
	 * Confirm delete schedule
	 */
	public void confirmDeleteSch() throws Exception
	{
		element("scp.confirmDeleteBtn").click();
		waitStatusDlg();
	}

	/**
	 * Add daily recurring pattern
	 */
	public void addDailyRecPattern(String type) throws Exception
	{
		element("scp.addRecPatternBtn", type).click();
		waitStatusDlg();
	}

	/**
	 * Add schedule
	 */
	public String addSchedule(String name, String description, String type) throws Exception
	{
		addScheduleClick();
		addScheduleName(name);
		addScheduleDescription(description);
		addDailyRecPattern(type);
		String msg = saveScheduleClick();
		return msg;
	}

	/**
	 * Is the label name exist or not
	 */
	public boolean isLabelNameExist(String labelName) throws Exception
	{
		return element("scp.labelName", labelName).isDisplayed();
	}

	/**
	 * Click recurring pattern table button
	 */
	public void clickRecPatternTabBtn(String tableName) throws Exception
	{
		String type = tableName.substring(0, 1).toLowerCase() + tableName.substring(1);
		element("scp.recPatternTabBtn", type).click();
		waitStatusDlg();
	}

	/**
	 * Get the recurring pattern dropdown list options dropdownName should be
	 * the end value of id attribute
	 */
	public List<String> getRecPattDropdownListOptions(String dropdownName) throws Exception
	{
		return element("scp.recPatternDropdown", dropdownName, dropdownName).getAllOptionTexts();
	}

	/**
	 * Back to dashboard
	 */
	public ListPage backToDashboard() throws Exception
	{
		element("scp.dashboard").click();
		waitStatusDlg();
		waitThat().timeout(500);
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * Select the recurring pattern dropdown list option dropdownName should be
	 * the end value of id attribute
	 */
	public void selectRecPattDropdownListOption(String dropdownName, String selectOption) throws Exception
	{
		element("scp.recPatternDropdown", dropdownName, dropdownName).selectByVisibleText(selectOption);
		waitStatusDlg();
	}

	/**
	 * Input the recurring pattern interval value
	 */
	public void inputRecPatternInterval(String inputName, String value) throws Exception
	{
		element("scp.recPatternInterval", inputName, inputName).type(value);
		waitStatusDlg();
	}

	/**
	 * Get exist recurring pattern
	 */
	public List<String> getExistRecPattern(String tableName) throws Exception
	{
		String type = tableName.substring(0, 1).toLowerCase() + tableName.substring(1);
		return element("scp.existRecPattern", type).getAllInnerTexts();
	}

	/**
	 * Select based on calendar
	 */
	public void selectBasedOnCal(String type, String value) throws Exception
	{
		element("scp.basedOnCalCheckbox", type).click();
		waitStatusDlg();
		if (type.equalsIgnoreCase("Last working day before") && value != null)
		{
			element("scp.basedOnCalBackward").selectByVisibleText(value);
		}
		else if (type.equalsIgnoreCase("First working day after") && value != null)
		{
			element("scp.basedOnCalForward").selectByVisibleText(value);
		}
	}

	/**
	 * Is schedule exist
	 */
	public boolean isScheduleExist(String scheduleName) throws Exception
	{
		return element("scp.scheduleName", scheduleName).isDisplayed();
	}
}
