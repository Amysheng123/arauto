package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Create by Leo Tu on 25/6/16
 */

public class ReturnSourcePage extends AbstractPage
{
	public ReturnSourcePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	public List<String> getSourceView() throws Exception
	{
		List<String> SourceViews = new ArrayList<String>();
		int nums = (int) element("rsp.table.id").getRowCount();
		for (int i = 1; i <= nums; i++)
		{
			SourceViews.add(element("rsp.table.sourceName", String.valueOf(i)).getInnerText());
		}
		closeReturnSourcePage();
		return SourceViews;
	}

	public void update() throws Exception
	{
		element("rsp.updateBtn").click();
		waitStatusDlg();
	}

	public void retrieveNew(String type) throws Exception
	{
		element("rsp.retrieveNewBtn").click();
		waitThat().timeout(500);
		if (type.equalsIgnoreCase("discard"))
			element("rsp.adj.discard").click();
		else
			element("rsp.adj.preserve").click();

		waitStatusDlg();
	}

	public ListPage closeReturnSourcePage() throws Exception
	{
		element("rsp.cancelBtn").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}

	public String getWarningMessage() throws Exception
	{
		return element("rsp.message").getInnerText();
	}

	/**
	 * Verify the update button is enabled or not
	 */
	public boolean verifyUpdateButtonEnabled() throws Exception
	{
		return element("rsp.updateBtn").isEnabled();
	}

	/**
	 * Verify the retrieve new button is enabled or not
	 */
	public boolean verifyRetrieveNewButtonEnabled() throws Exception
	{
		return element("rsp.retrieveNewBtn").isEnabled();
	}

}
