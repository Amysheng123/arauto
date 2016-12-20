package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Create by Leo Tu on 9/18/2016
 */

public class DWIntegrationPage extends AbstractPage
{

	/**
	 * @param webDriverWrapper
	 */
	public DWIntegrationPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	/**
	 * select DW
	 * 
	 * @param DW
	 * @throws Exception
	 */
	private void SelectDW(String DW) throws Exception
	{
		element("dwi.DWName", DW).click();
		waitStatusDlg();
	}

	/**
	 * get all existed Contextual buttons
	 * 
	 * @param DW
	 * @return Contextual buttons(List)
	 * @throws Exception
	 */
	public List<String> getExistedContextual(String DW) throws Exception
	{
		logger.info("Get all contextual from:" + DW);
		List<String> Contextuals = new ArrayList<>();
		SelectDW(DW);
		int nums = (int) element("dwi.dataTable").getRowCount();
		for (int i = 1; i <= nums; i++)
		{
			Contextuals.add(element("dwi.dataTable.name", String.valueOf(i)).getInnerText());
		}
		return Contextuals;
	}

	/**
	 * delete contextual
	 * 
	 * @param DW
	 * @param contextualName
	 * @throws Exception
	 */
	public void deleteContextual(String DW, String contextualName) throws Exception
	{
		SelectDW(DW);
		int num = (int) element("dwi.dataTable").getRowCount();
		for (int i = 1; i <= num; i++)
		{
			if (element("dwi.dataTable.name", String.valueOf(i)).getInnerText().equalsIgnoreCase(contextualName))
			{
				logger.info("Delete contextual on:" + DW);
				element("dwi.dataTable.remove", String.valueOf(i)).click();
				waitStatusDlg();
				element("dwi.dataTable.deleteConfirm").click();
				waitThat("dwi.message.title").toBeVisible();
				waitThat("dwi.message.title").toBeInvisible();
				break;
			}
		}
	}

	/**
	 * delete all contextual button
	 * 
	 * @param DW
	 * @throws Exception
	 */
	public void deleteAllContextual(String DW) throws Exception
	{
		SelectDW(DW);
		if (element("dwi.dataTable2").isDisplayed() && "No records found.".equals(element("dwi.dataTable2").getInnerText()))
		{

		}
		else
		{
			int nums = (int) element("dwi.dataTable").getRowCount();
			for (int i = 1; i <= nums; i++)
			{
				logger.info("Delete contextual on:" + DW);
				element("dwi.dataTable.remove", "1").click();
				waitStatusDlg();
				element("dwi.dataTable.deleteConfirm").click();
				waitThat("dwi.message.title").toBeVisible();
				waitThat("dwi.message.title").toBeInvisible();
			}
		}

	}

	/**
	 * edit contextual
	 * 
	 * @param DW
	 * @param contextualName
	 * @param newContextualName
	 * @param uRLPattern
	 * @param desc
	 * @throws Exception
	 */
	public void editContextual(String DW, String contextualName, String newContextualName, List<String> uRLPattern, String desc) throws Exception
	{
		SelectDW(DW);
		int nums = (int) element("dwi.dataTable").getRowCount();
		for (int i = 1; i <= nums; i++)
		{
			if (element("dwi.dataTable.name", String.valueOf(i)).getInnerText().equalsIgnoreCase(contextualName))
			{
				logger.info("Begin edit contextual on:" + DW);
				element("dwi.dataTable.edit", String.valueOf(i)).click();
				waitStatusDlg();
				inputContextualInfo(contextualName, uRLPattern, desc);
				element("dwi.add.confirm").click();
				waitThat("dwi.message.title").toBeVisible();
				waitThat("dwi.message.title").toBeInvisible();
				break;
			}
		}
	}

	/**
	 * input contextual info
	 * 
	 * @param contextualName
	 * @param uRLPattern
	 * @param desc
	 * @throws Exception
	 */
	private void inputContextualInfo(String contextualName, List<String> uRLPattern, String desc) throws Exception
	{
		if (contextualName != null)
		{
			element("dwi.edit.name").input(contextualName);
			Thread.sleep(300);
		}
		if (uRLPattern != null)
		{
			element("dwi.edit.url").input(uRLPattern.get(0));
			for (int i = 1; i < uRLPattern.size(); i++)
			{
				if (i == 1)
					element("dwi.edit.url").type("?");
				else
					element("dwi.edit.url").type("&");
				Thread.sleep(100);
				element("dwi.edit.url").type(uRLPattern.get(i) + "=");
				Thread.sleep(100);
				String list[] =
				{ uRLPattern.get(i), uRLPattern.get(i) };
				element("dwi.edit.pattern", list).click();
				waitStatusDlg();
			}
		}

		if (desc != null)
		{
			element("dwi.edit.desc").input(desc);
		}

		Random random = new Random();
		int x = random.nextInt(10);

		element("dwi.edit.icon", String.valueOf(x)).click();
		waitStatusDlg();
	}

	/**
	 * add new contextual button
	 * 
	 * @param DW
	 * @param contextualName
	 * @param uRLPattern
	 * @param desc
	 * @throws Exception
	 */
	public void addContextual(String DW, String contextualName, List<String> uRLPattern, String desc, boolean save) throws Exception
	{
		logger.info("Begin add contextual on:" + DW);
		SelectDW(DW);
		element("dwi.dataTable.add").click();
		waitStatusDlg();
		inputContextualInfo(contextualName, uRLPattern, desc);
		if (save)
		{
			element("dwi.add.confirm").click();
			waitThat("dwi.message.title").toBeVisible();
			waitThat("dwi.message.title").toBeInvisible();
		}
		else
		{
			element("dwi.add.canel").click();
			waitStatusDlg();
		}
	}

	public ListPage backToDashboard() throws Exception
	{
		element("lp.dashboard").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}
}
