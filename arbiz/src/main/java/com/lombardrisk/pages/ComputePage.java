package com.lombardrisk.pages;

import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by leo tu on 4/13/2016.
 */
public class ComputePage extends AbstractPage
{
	/**
	 * 
	 * @param webDriverWrapper
	 */
	public ComputePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * set entity
	 * 
	 * @param entity
	 * @throws Exception
	 */
	protected void setEntity(String entity) throws Exception
	{
		if (entity != null)
		{
			logger.info("Set entity:" + entity);
			element("cp.entity").selectByVisibleText(entity);
			waitStatusDlg();
		}
	}

	/**
	 * set reference date
	 * 
	 * @param processDate
	 * @throws Exception
	 */
	public void setReferencedate(String processDate) throws Exception
	{
		if (processDate != null)
		{
			logger.info("Set processDate:" + processDate);
			element("cp.date").input(processDate);
			Thread.sleep(300);
			selectDate(processDate);
		}
	}

	/**
	 * set form
	 * 
	 * @param form
	 * @throws Exception
	 */
	protected void setform(String form) throws Exception
	{
		if (form != null)
		{
			logger.info("Set form:" + form);
			element("cp.form").selectByVisibleText(form);
			waitStatusDlg();
		}
	}

	/**
	 * close compute page
	 * 
	 * @throws Exception
	 */
	protected void closeCompute() throws Exception
	{
		logger.info("Close compute page");
		element("cp.canel").click();
		waitStatusDlg();
	}

	/**
	 * get error message when compute
	 * 
	 * @param entity
	 * @param processDate
	 * @param form
	 * @return message
	 * @throws Exception
	 */
	public String getErrorMessage(String entity, String processDate, String form) throws Exception
	{
		setEntity(entity);
		setReferencedate(processDate);
		setform(form);
		element("cp.computeBtn").click();
		waitStatusDlg();
		String msg = element("cp.errorMsg").getInnerText();
		closeCompute();
		return msg;
	}

	/**
	 * compute form
	 * 
	 * @param entity
	 * @param processDate
	 * @param form
	 * @param showProgress
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage computeReturn(String entity, String processDate, String form, boolean showProgress) throws Exception
	{
		setEntity(entity);
		setReferencedate(processDate);
		setform(form);
		if (showProgress)
		{
			element("cp.showProgress").click();
			waitThat().timeout(300);
		}
		element("cp.computeBtn").click();
		waitStatusDlg();
		if (element("cp.confirmMsg").isDisplayed())
		{
			String msg = element("cp.confirmMsg").getInnerText();
			if (!msg.equalsIgnoreCase("Overwriting existing forms:" + form.split(" ")[0]))
				logger.error("The prompt message should be:" + "Overwriting existing forms:" + form.split(" ")[0] + ", but actual message is:" + msg);
			element("cp.computeConfirmBtn").click();
			waitStatusDlg();
		}
		return new FormInstancePage(getWebDriverWrapper());
	}

	/**
	 * compute form
	 * 
	 * @param entity
	 * @param processDate
	 * @param form
	 * @param showProgress
	 * @param showConfirm
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage computeReturn(String entity, String processDate, String form, boolean showProgress, boolean showConfirm) throws Exception
	{
		setEntity(entity);
		setReferencedate(processDate);
		setform(form);
		if (showProgress)
		{
			element("cp.showProgress").click();
			waitThat().timeout(300);
		}
		element("cp.computeBtn").click();
		waitStatusDlg();
		if (element("cp.confirmMsg").isDisplayed())
		{
			String msg = element("cp.confirmMsg").getInnerText();
			if (!msg.equalsIgnoreCase("Overwriting existing forms:" + form.split(" ")[0]))
				logger.error("The prompt message should be:" + "Overwriting existing forms:" + form.split(" ")[0] + ", but actual message is:" + msg);
			element("cp.computeConfirmBtn").click();
			waitStatusDlg();
		}

		return new FormInstancePage(getWebDriverWrapper());
	}

	/**
	 * get existed forms from compute page
	 * 
	 * @param entity
	 * @param processDate
	 * @return forms(List)
	 * @throws Exception
	 */
	public List<String> getForms(String entity, String processDate) throws Exception
	{
		setEntity(entity);
		setReferencedate(processDate);
		List<String> forms = element("cp.form").getAllInnerTexts();
		closeCompute();
		return forms;
	}
}
