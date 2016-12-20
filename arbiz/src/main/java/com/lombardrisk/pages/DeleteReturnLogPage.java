package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Leo Tu on 4/27/2016.
 */
public class DeleteReturnLogPage extends AbstractPage
{

	/**
	 * 
	 * @param webDriverWrapper
	 */
	public DeleteReturnLogPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * get logs amount
	 * 
	 * @return logs amount(int)
	 * @throws Exception
	 */
	public int getlogNums() throws Exception
	{
		logger.info("Begin get log amount in delete return log page");
		if (element("drlp.firstPage").isDisplayed() && !element("drlp.firstPage").getAttribute("class").contains("ui-state-disabled"))
		{
			element("drlp.firstPage").click();
			waitStatusDlg();
		}

		int amt, rowAmt_F;
		if ("No records found.".equals(element("drlp.firstRowTxt").getInnerText()))
			amt = 0;
		else
		{
			rowAmt_F = (int) element("drlp.logTable").getRowCount();
			if (element("drlp.lastPage").isDisplayed() && !element("drlp.lastPage").getAttribute("class").contains("ui-state-disabled"))
			{
				element("drlp.lastPage").click();
				waitStatusDlg();
				int PageNums = Integer.parseInt(element("drlp.PageNo").getInnerText());

				int rowAmt_L = (int) element("drlp.logTable").getRowCount();
				amt = rowAmt_F * (PageNums - 1) + rowAmt_L;
			}
			else
			{
				amt = rowAmt_F;
			}
		}
		closeDeleteReturnLog();
		logger.info("There are " + amt + "  logs");
		return amt;
	}

	/**
	 * close delete return page
	 * 
	 * @return ListPage
	 * @throws Exception
	 */
	public ListPage closeDeleteReturnLog() throws Exception
	{
		logger.info("Close delete return log page");
		element("drlp.closeWindow").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * get latest action
	 * 
	 * @param formCode
	 * @param formVersion
	 * @param editionNo
	 * @param topNum
	 * @return latest action
	 * @throws Exception
	 */
	public String getLatestAction(String formCode, String formVersion, String editionNo, int topNum) throws Exception
	{
		String lastAction = null;
		while (!element("drlp.sort").getAttribute("class").contains("ui-icon-triangle-1-s"))
		{
			element("drlp.sortByAction").click();
			waitStatusDlg();
			waitThat().timeout(500);
		}
		for (int i = 1; i <= topNum; i++)
		{
			if (element("drlp.logItem", String.valueOf(i), "2").getInnerText().equalsIgnoreCase(formCode)
					&& element("drlp.logItem", String.valueOf(i), "3").getInnerText().equalsIgnoreCase(formVersion)
					&& element("drlp.logItem", String.valueOf(i), "4").getInnerText().equalsIgnoreCase(editionNo))
			{
				lastAction = element("drlp.logItem", String.valueOf(i), "7").getInnerText();
				break;
			}
		}
		return lastAction;

	}
}
