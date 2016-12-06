package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by leo tu on 12/5/2016.
 */
public class JobResultPage extends AbstractPage
{
	/**
	 * @param webDriverWrapper
	 */
	public JobResultPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	private void showLog() throws Exception
	{
		if (element("jrp.showLog").isDisplayed())
		{
			element("jrp.showLog").click();
			waitStatusDlg();
		}
	}

	public List<String> getLog() throws Exception
	{
		List<String> logs = new ArrayList<>();
		showLog();
		int num = element("jrp.logTable").getNumberOfMatches();
		for (int i = 1; i <= num; i++)
		{
			String[] list =
			{ String.valueOf(i), "2" };
			logs.add(element("jrp.logMessage", list).getInnerText());
		}
		return logs;
	}

	public String exportLog() throws Exception
	{
		String dir = FileUtils.getUserDirectoryPath() + "\\downloads";
		String latestFile = getLatestFile(dir);
		if (httpDownload)
		{
			TestCaseManager.getTestCase().startTransaction("");
			TestCaseManager.getTestCase().setPrepareToDownload(true);
			element("jrp.exportLog").click();
			waitStatusDlg();
			TestCaseManager.getTestCase().stopTransaction();
			String exportedFile = TestCaseManager.getTestCase().getDownloadFile();
			return getOriginalFile(exportedFile, latestFile);
		}
		else
		{
			element("jrp.exportLog").click();
			waitStatusDlg();
			return downloadFile(null, latestFile, null);
		}
	}

	public List<String> getJobDetail() throws Exception
	{
		List<String> jobInfo = new ArrayList<>();
		for (int i = 1; i <= 4; i++)
		{
			String id = String.valueOf(i);
			jobInfo.add(element("jrp.firtRow", id).getInnerText());
		}
		for (int i = 1; i <= 3; i++)
		{
			String id = String.valueOf(i);
			jobInfo.add(element("jrp.secondRow", id).getInnerText());
		}
		return jobInfo;
	}
}
