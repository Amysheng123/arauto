package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 1/25/16
 */

public class FormInstanceCreatePage extends AbstractPage
{

	public FormInstanceCreatePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);

	}

	public FormInstanceCreatePage setGroup(String group) throws Exception
	{
		logger.info("set group=" + group);
		element("fcp.group").selectByVisibleText(group);
		waitStatusDlg();
		return this;
	}

	public void setProcessDate(String processDate) throws Exception
	{
		if (processDate != null)
		{
			logger.info("set processDate=" + processDate);
			element("fcp.date").input(processDate);
			selectDate(processDate);

		}
	}

	public void setProcessDate2(String processDate) throws Exception
	{
		if (processDate != null)
		{
			logger.info("set processDate=" + processDate);
			element("fcp.date").input(processDate);
			element("fcp.refDate").click();
			waitThat().timeout(1000);
		}
	}

	public void setForm(String form) throws Exception
	{
		if (form != null)
		{
			logger.info("set form=" + form);
			element("fcp.form").selectByVisibleText(form);
			waitThat().timeout(1000);
		}

	}

	public void selectCloneCheck() throws Exception
	{
		element("fcp.copyData").click();
		waitThat().timeout(1000);
	}

	public void setSelectCloneDate(String selectCloneDate) throws Exception
	{
		element("fcp.copyDate").selectByVisibleText(selectCloneDate);
	}

	public void selectInitToZeroCheck() throws Exception
	{
		element("fcp.zero").click();
	}

	public FormInstancePage createConfirmClick() throws Exception
	{
		element("fcp.create").click();
		waitStatusDlg();
		if (element("fcp.createConfirm").isDisplayed())
		{
			element("fcp.createConfirm").click();
			waitStatusDlg();
		}
		return new FormInstancePage(getWebDriverWrapper());
	}

	public ListPage closeMessageClick() throws Exception
	{
		if (element("fcp.closeMsg").isDisplayed())
			element("fcp.closeMsg").click();
		return new ListPage(getWebDriverWrapper());
	}

	public void createCloseClick() throws Exception
	{
		if (element("fcp.closeCreateFormWin").isDisplayed())
		{
			element("fcp.closeCreateFormWin").click();
		}
	}

	public String getErrorMsg() throws Exception
	{
		return element("fcp.message").getInnerText();
	}

	public void closeCreateNew() throws Exception
	{
		element("fcp.cancel").click();
		waitStatusDlg();
	}

	public boolean initToZeroDisplaye() throws Exception
	{
		return element("fcp.zero").isDisplayed();
	}

	public boolean isCopyDataDisplayed() throws Exception
	{
		return element("fcp.copyData").isDisplayed();
	}
}
