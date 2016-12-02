package com.lombardrisk.pages;

import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 1/29/16
 */

public class FormInstanceRetrievePage extends AbstractPage
{

	public FormInstanceRetrievePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);

	}

	public void setGroup(String group) throws Exception
	{
		logger.info("Set group= " + group);
		element("frp.group").selectByVisibleText(group);
		waitStatusDlg();
	}

	public void setReferenceDate(String processDate) throws Exception
	{
		logger.info("Set processDate= " + processDate);
		element("frp.dateInput").input(processDate);
		element("frp.dateInput").click();
		Thread.sleep(300);
		selectDate(processDate);
	}

	public void setForm(String form) throws Exception
	{
		logger.info("Set form= " + form);
		element("frp.form").selectByVisibleText(form);
		waitStatusDlg();
	}

	public RetrieveResultPage retrieveConfirmClick() throws Exception
	{
		element("frp.retrConfirm").click();
		waitStatusDlg();
		return new RetrieveResultPage(getWebDriverWrapper());
	}

	public ListPage retrieveCloseClick() throws Exception
	{
		if (element("frp.cancel").isDisplayed())
		{
			element("frp.cancel").click();
			waitStatusDlg();
		}
		return new ListPage(getWebDriverWrapper());
	}

	public ListPage retrieveDeclineClick() throws Exception
	{
		if (element("frp.retrCancel").isDisplayed())
		{
			element("frp.retrCancel").click();
			waitStatusDlg();
		}
		return new ListPage(getWebDriverWrapper());
	}

	public boolean isMessagesVisible() throws Exception
	{
		return element("frp.message").isDisplayed();
	}

	public void setLogLevel(String level) throws Exception
	{
		element("frp.logLevel").selectByVisibleText(level);
	}

	public FormInstancePage openForm() throws Exception
	{
		element("frp.openForm").click();
		waitStatusDlg();
		return new FormInstancePage(getWebDriverWrapper());
	}

	public List<String> getFormOptions() throws Exception
	{
		return element("frp.form").getAllOptionTexts();
	}

	public ListPage clickOK() throws Exception
	{
		logger.info("Click retrieve button");
		element("frp.retrieve").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}

	public ReturnSourcePage clickOKWithExistentReturn() throws Exception
	{
		logger.info("Click retrieve button");
		element("frp.retrieveAga").click();
		waitStatusDlg();
		return new ReturnSourcePage(getWebDriverWrapper());
	}

	public boolean isMessageDisplayed() throws Exception
	{
		Thread.sleep(1000);
		return element("frp.message.info").isDisplayed();
	}

	public String getRetrieveErrorMessage() throws Exception
	{
		return element("frp.message.info").getInnerText();
	}

}
