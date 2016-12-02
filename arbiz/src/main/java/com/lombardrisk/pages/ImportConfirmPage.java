package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Ray Rui on 5/29/15. Refactored by Leo Tu on 1/29/16
 */
public class ImportConfirmPage extends AbstractPage
{

	public ImportConfirmPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);

	}

	protected FormInstancePage confirmBtnClick(String type) throws Exception
	{

		if (type.equalsIgnoreCase("createFromExcelForm"))
		{
			try
			{
				if (element("icp.createConf").isDisplayed())
				{
					element("icp.createConf").click();
					waitStatusDlg();
				}
				element("icp.OKBtn").click();
			}
			catch (Exception e)
			{
			}
		}
		else if (type.equalsIgnoreCase("listImportFileForm"))
		{
			element("icp.listConf").click();
			waitStatusDlg();
		}

		return new FormInstancePage(getWebDriverWrapper());
	}

	public void closeImportResultDialog() throws Exception
	{
		element("icp.closeDlg").click();
		waitStatusDlg();
	}

	public boolean isVisible(String loc) throws Exception
	{
		return element(loc).isDisplayed();
	}

	public String getText(String loc) throws Exception
	{
		return element(loc).getInnerText();
	}
}
