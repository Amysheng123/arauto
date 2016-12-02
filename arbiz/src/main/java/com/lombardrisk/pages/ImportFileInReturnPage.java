package com.lombardrisk.pages;

import org.openqa.selenium.By;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Ray Rui on 5/25/15. Refactored by Leo Tu on 1/29/16
 */
public class ImportFileInReturnPage extends AbstractImportPage
{

	public ImportFileInReturnPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);

	}

	public String getErrorMessage(String type) throws Exception
	{
		return element("lif.error", type).getInnerText();
	}

	public String getErrorInfo(String type) throws Exception
	{
		return element("lif.createFromXLSError", type).getInnerText();
	}

	public void closeImportFileDlg() throws Exception
	{
		element("lif.closeImportDlg").click();
	}

	@Override
	public String parentFormId(String type)
	{
		return type;
	}

	@Override
	public By getImportBtn(String type)
	{

		return By.id(parentFormId(type) + ":importBtn");
	}

	protected void selectInitToZeroCheck() throws Exception
	{
		element("ifr.zero").click();
	}
}
