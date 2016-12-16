package com.lombardrisk.pages;

import org.apache.commons.lang3.StringUtils;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Ray Rui on 6/2/15. Refactored by Leo Tu on 2/1/16
 */
public class RetrieveResultPage extends AbstractPage
{

	public RetrieveResultPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	public JobDetailsPage getShowDWImportLogPage() throws Exception
	{
		// waitThat("rrp.showLogBtn").toBeClickable();
		element("rrp.showLogBtn").click();
		waitStatusDlg();
		return new JobDetailsPage(getWebDriverWrapper());
	}

	public boolean isRetrieveResultPicWarning() throws Exception
	{
		String name = getResultPicName();
		return name.substring(name.lastIndexOf("/") + 1).contains("Warning");
	}

	public boolean isRetrieveResultPicFailed() throws Exception
	{
		String name = getResultPicName();
		return name.substring(name.lastIndexOf("/") + 1).contains("Fail");
	}

	public boolean isRetrieveResultPicSuccess() throws Exception
	{
		String name = getResultPicName();
		return name.substring(name.lastIndexOf("/") + 1).contains("Success");
	}

	private String getResultPicName() throws Exception
	{
		String name = element("rrp.result").getAttribute("src");
		if (StringUtils.isEmpty(name))
		{
			try
			{
				throw new Exception("Retrieve result");
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		return name;
	}

	public void closeRetrieveResultPage() throws Exception
	{
		waitThat().timeout(600);
		element("rrp.close").click();
		waitStatusDlg();
	}
}
