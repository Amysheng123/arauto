package com.lombardrisk.pages;

import org.apache.commons.io.FileUtils;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 1/25/16
 */
public class AdjustLogPage extends AbstractPage
{

	public AdjustLogPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);

	}

	/**
	 * input cellName to filter
	 * 
	 * @param text
	 * @throws Exception
	 */
	public void inputCellText(String text) throws Exception
	{
		logger.info("Input filter cell:" + text);
		Thread.sleep(3000);
		element("alp.cellInput").input(text);
		clickEnterKey();
		waitStatusDlg();
	}

	/**
	 * get filter cellName
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFilterCell() throws Exception
	{
		return element("alp.cellInput").getAttribute("value");
	}

	/**
	 * input user to filter
	 * 
	 * @param text
	 * @throws Exception
	 */
	public void inputUserText(String text) throws Exception
	{
		Thread.sleep(3000);
		element("alp.userInput").type(text);
		clickEnterKey();
		waitStatusDlg();
	}

	/**
	 * input fromDate to filter
	 * 
	 * @param text
	 * @throws Exception
	 */
	public void inputFromDateText(String text) throws Exception
	{
		Thread.sleep(3000);
		element("alp.fromDateInput").type(text);
		clickEnterKey();
		waitStatusDlg();
	}

	/**
	 * input toDate filter
	 * 
	 * @param text
	 * @throws Exception
	 */
	public void inputToDateText(String text) throws Exception
	{
		Thread.sleep(3000);
		element("alp.toDateInput").type(text);
		clickEnterKey();
		waitStatusDlg();
	}

	/**
	 * clear filter
	 * 
	 * @throws Exception
	 */
	public void clearImageClick() throws Exception
	{
		element("clear").click();
		waitStatusDlg();
	}

	/**
	 * get log amounts
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getLogNums() throws Exception
	{
		int Nums = (int) element("alp.logTable").getRowCount();
		if (Nums == 1)
		{
			if ("No records found.".equals(element("alp.noRecords").getInnerText()))
				Nums = 0;
		}
		return Nums;
	}

	/**
	 * sort by edit time asc
	 * 
	 * @throws Exception
	 */
	public void orderByTimeAsc() throws Exception
	{
		while (element("alp.SBT").getAttribute("class").contains("ui-icon-triangle-1-n"))
		{
			element("alp.SBT").click();
			waitStatusDlg();
		}

	}

	/**
	 * sort by edit time desc
	 * 
	 * @throws Exception
	 */
	public void orderByTimeDesc() throws Exception
	{
		while (element("alp.SBT").getAttribute("class").contains("ui-icon-triangle-1-s"))
		{
			element("alp.SBT").click();
			waitStatusDlg();
		}
	}

	/**
	 * get cell name by row index
	 * 
	 * @param index
	 * @return cellName
	 * @throws Exception
	 */
	public String getCellName(int index) throws Exception
	{
		if (element("alp.cellName", String.valueOf(index)).isDisplayed())
			return element("alp.cellName", String.valueOf(index)).getInnerText();
		else
			return element("alp.cellName2", String.valueOf(index)).getInnerText();
	}

	/**
	 * get instance by row index
	 * 
	 * @param index
	 * @return instance
	 * @throws Exception
	 */
	public String getInstance(int index) throws Exception
	{
		return element("alp.instance", String.valueOf(index)).getInnerText();
	}

	/**
	 * get gridkey by row index
	 * 
	 * @param index
	 * @return gridkey
	 * @throws Exception
	 */
	public String getGrdiKey(int index) throws Exception
	{
		if (element("alp.gridKey", String.valueOf(index)).isDisplayed())
			return element("alp.gridKey", String.valueOf(index)).getInnerText();
		else
			return "";
	}

	/**
	 * get value by row index
	 * 
	 * @param index
	 * @return cellValue
	 * @throws Exception
	 */
	public String getValue(int index) throws Exception
	{
		try
		{
			if (element("alp.value2", String.valueOf(index)).isDisplayed())
				return element("alp.value2", String.valueOf(index)).getInnerText();
			else
				return element("alp.value", String.valueOf(index)).getInnerText();
		}
		catch (Exception e)
		{
			logger.warn("warn", e);
			// e.printStackTrace();
			return "";
		}
	}

	/**
	 * get modified value by row index
	 * 
	 * @param index
	 * @return cellValue
	 * @throws Exception
	 */
	public String getModifiedTo(int index) throws Exception
	{
		try
		{
			if (element("alp.modifiedTo", String.valueOf(index)).isDisplayed())
				return element("alp.modifiedTo", String.valueOf(index)).getInnerText();
			else
				return element("alp.modifiedTo2", String.valueOf(index)).getInnerText();
		}
		catch (Exception e)
		{
			logger.warn("warn", e);
			// e.printStackTrace();
			return "";
		}
	}

	/**
	 * get edit time by row index
	 * 
	 * @param index
	 * @return editTime
	 * @throws Exception
	 */
	public String getEditTime(int index) throws Exception
	{
		try
		{
			return element("alp.editTime", String.valueOf(index)).getInnerText();
		}
		catch (Exception e)
		{
			logger.warn("warn", e);
			// e.printStackTrace();
			return "";
		}
	}

	/**
	 * get user name by row index
	 * 
	 * @param index
	 * @return userName
	 * @throws Exception
	 */
	public String getUser(int index) throws Exception
	{
		try
		{
			return element("alp.user", String.valueOf(index)).getInnerText();
		}
		catch (Exception e)
		{
			logger.warn("warn", e);
			// e.printStackTrace();
			return "";
		}
	}

	/**
	 * get comment by row index
	 * 
	 * @param index
	 * @return comment
	 * @throws Exception
	 */
	public String getComment(int index) throws Exception
	{
		try
		{
			return element("alp.comment", String.valueOf(index)).getInnerText();
		}
		catch (Exception e)
		{
			logger.warn("warn", e);
			// e.printStackTrace();
			return "";
		}
	}

	/**
	 * export adjustment log
	 * 
	 * @return exported file
	 * @throws Exception
	 */
	public String exportAdjustment() throws Exception
	{
		waitStatusDlg();
		waitThat("alp.export").toBeClickable();
		String dir = FileUtils.getUserDirectoryPath() + "/downloads";
		String latestFile = getLatestFile(dir);
		if (httpDownload)
		{
			TestCaseManager.getTestCase().startTransaction("");
			TestCaseManager.getTestCase().setPrepareToDownload(true);
			element("alp.export").click();
			TestCaseManager.getTestCase().stopTransaction();
			String exportedFile = System.getProperty("user.dir") + "/" + TestCaseManager.getTestCase().getDownloadFile();
			return getOriginalFile(exportedFile, latestFile, setOriginalName);
		}
		else
		{
			element("alp.export").click();
			waitStatusDlg();
			return downloadFile(null, latestFile, null);
		}

	}

}
