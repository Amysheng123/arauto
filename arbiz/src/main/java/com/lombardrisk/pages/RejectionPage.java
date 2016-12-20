package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by leo tu on 12/15/2016.
 */
public class RejectionPage extends AbstractPage
{

	public RejectionPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * get all column name
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllFields() throws Exception
	{
		List<String> fields = new ArrayList<>();
		int i = 1;
		while (element("rj.columnField", String.valueOf(i)).isDisplayed())
		{
			fields.add(element("rj.columnField", String.valueOf(i)).getInnerText());
			i++;
		}
		return fields;
	}

	/**
	 * export allocation
	 *
	 * @return exported file
	 * @throws Exception
	 */
	public String exportRejection() throws Exception
	{
		Thread.sleep(3000);
		waitStatusDlg();
		String dir = FileUtils.getUserDirectoryPath() + "/downloads";
		String latestFile = getLatestFile(dir);
		if (httpDownload)
		{
			TestCaseManager.getTestCase().startTransaction("");
			TestCaseManager.getTestCase().setPrepareToDownload(true);
			element("rj.export").click();
			TestCaseManager.getTestCase().stopTransaction();
			String exportedFile = System.getProperty("user.dir") + "/" + TestCaseManager.getTestCase().getDownloadFile();
			return getOriginalFile(exportedFile, latestFile, setOriginalName);
		}
		else
		{
			element("rj.export").click();
			waitStatusDlg();
			return downloadFile(null, latestFile, null);
		}

	}
}
