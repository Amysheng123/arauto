package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

public class FormSchedulePage extends AbstractPage
{

	public FormSchedulePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	public FormSchedulePage setRegulator(String regulator) throws Exception
	{
		element("fsp.regulator").type(regulator);
		waitStatusDlg();
		return this;
	}

	public FormSchedulePage setForm(String form) throws Exception
	{
		element("fsp.form").type(form);
		waitStatusDlg();
		return this;
	}

	public FormSchedulePage setGroup(String group) throws Exception
	{
		element("fsp.group").type(group);
		waitStatusDlg();
		return this;
	}

	public FormSchedulePage setSchedule(String schedule) throws Exception
	{
		element("fsp.schedule").type(schedule);
		waitStatusDlg();
		return this;
	}

	public FormSchedulePage formScheduleAddClick() throws Exception
	{
		element("fsp.addBtn").click();
		waitStatusDlg();
		return this;
	}

	/**
	 * Is regulator dropdown list exist
	 */
	public boolean isRegulatorDropdownExist() throws Exception
	{
		return element("fsp.regulator").isDisplayed();
	}

	/**
	 * Is form dropdown list exist
	 */
	public boolean isFormDropdownExist() throws Exception
	{
		return element("fsp.form").isDisplayed();
	}

	/**
	 * Is entity dropdowm list exist
	 */
	public boolean isEntityDropdownExist() throws Exception
	{
		return element("fsp.group").isDisplayed();
	}

	/**
	 * Is schedule dropdown list exist
	 */
	public boolean isScheduleDropdownExist() throws Exception
	{
		return element("fsp.schedule").isDisplayed();
	}

	/**
	 * Back to dashboard
	 */
	public ListPage backToDashboard() throws Exception
	{
		element("fsp.dashboard").click();
		waitStatusDlg();
		waitThat().timeout(500);
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * Binding form with schedule
	 */
	public void bindingFormWithSch(String regulator, String form, String entity, String sch) throws Exception
	{
		setRegulator(regulator);
		setGroup(entity);
		setForm(form);
		setSchedule(sch);
		formScheduleAddClick();
	}

	/**
	 * Get current page number
	 */
	public String getCurrentPageNumber() throws Exception
	{
		return element("fsp.curPage").getInnerText();
	}

	/**
	 * Get the count of binding records
	 */
	public String getCountOfBindingRecords() throws Exception
	{
		return element("fsp.bindingRecords").getRowCount() + "";
	}

	/**
	 * Click next page button
	 */
	public void clickNextPage() throws Exception
	{
		element("fsp.nextPage").click();
		waitStatusDlg();
	}
}
