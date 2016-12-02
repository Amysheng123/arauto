package com.lombardrisk.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 6/23/16
 */
public class JobDetailsPage extends AbstractPage
{

	public static final String INVOKEBY = "invoke by";
	public static final String OVERALLSTATUS = "Overall Status";
	public static final String FORMCODE = "Form Code";
	public static final String FORMVERSION = "Form Version";
	public static final String TEMPLATECODE = "Template Code";
	public static final String ENTITYCODE = "Entity Code";
	public static final String REFERENCEDATE = "Reference Date";

	public JobDetailsPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	public Map<String, String> getDWImportInfo() throws Exception
	{
		List<String> row = new ArrayList<String>();
		String[] list = new String[]
		{ "", "" };
		list[0] = "2";
		for (int j = 1; j <= 5; j++)
		{
			list[1] = String.valueOf(j);
			row = element("dwp.info", list).getAllInnerTexts();

		}

		list[0] = "5";
		for (int j = 1; j <= 5; j++)
		{
			list[1] = String.valueOf(j);
			row = element("dwp.info", list).getAllInnerTexts();

		}
		HashMap map = new HashMap();
		map.put(INVOKEBY, row.get(0));
		map.put(OVERALLSTATUS, row.get(3));
		map.put(FORMCODE, row.get(0));
		map.put(FORMVERSION, row.get(1));
		// map.put(FORMCODE,row.get(0));
		map.put(TEMPLATECODE, row.get(2));
		map.put(ENTITYCODE, row.get(3));
		map.put(REFERENCEDATE, row.get(4));
		return map;
	}

	public String getStatus() throws Exception
	{
		Map<String, String> map = getDWImportInfo();
		return map.get(OVERALLSTATUS);
	}

	public void exportBtnClick() throws Exception
	{
		element("dwp.export").click();
		waitStatusDlg();
	}

	public void showDetailBtnClick() throws Exception
	{
		element("dwp.showDetail").click();
		waitStatusDlg();
	}

	public void hideDetailBtnClick() throws Exception
	{
		element("dwp.hideDetail").click();
		waitStatusDlg();
	}

	public boolean checkDetailLogPanelShowable(int index) throws Exception
	{
		return element("dwp.logTab").isDisplayed();
	}

	public void setShowNumsSelector(int num) throws Exception
	{
		if (num == 10 || num == 5)
		{
			element("dwp.selectDisItem").selectByVisibleText(String.valueOf(num));
		}
		else
		{
			throw new Exception("one can only show 5 or 10 messages!");
		}
		waitStatusDlg();
	}

	public void setDetailLogLevel(String logLever) throws Exception
	{
		element("dwp.logLevel").selectByVisibleText(logLever);
		waitStatusDlg();
	}

	public String getLogNums() throws Exception
	{

		return element("dwp.logNums").getInnerText().replace("See all ", "").replace(" log entries", "");
	}

	public boolean isErrorMessageExist(String message, String level) throws Exception
	{
		setShowNumsSelector(100);
		int amt = (int) element("dwp.logsTab").getRowCount();
		boolean findMsg = false;
		boolean flag = true;
		while (flag)
		{
			try
			{
				for (int i = 1; i <= amt; i++)
				{
					if (element("dwp.message", String.valueOf(i)).getInnerText().equals(message))
					{
						String levelIcon = element("dwp.level", String.valueOf(i)).getAttribute("src");
						String acctualLevel = null;
						if (levelIcon.contains("FailIcon"))
							acctualLevel = "ERROR";
						else if (levelIcon.contains("WarningIcon"))
							acctualLevel = "WARN";
						else if (levelIcon.contains("SuccessIcon"))
							acctualLevel = "INFO";
						if (acctualLevel.equalsIgnoreCase(level))
						{
							findMsg = true;
							flag = false;
							break;
						}
						else
						{
							logger.error("Level is incorrect!");
						}
					}
				}

				if (!findMsg)
				{
					flag = false;

				}
			}
			catch (NoSuchElementException e)
			{
				flag = false;
			}

		}
		return findMsg;
	}

	public void backToJobDetails() throws Exception
	{
		if (element("dwp.backToJobDetail").isDisplayed())
		{
			element("dwp.backToJobDetail").click();
			waitStatusDlg();
		}

	}

	public JobManagerPage backToJobManager() throws Exception
	{
		if (element("dwp.backtoJobManager").isDisplayed())
		{
			element("dwp.backtoJobManager").click();
			waitStatusDlg();
		}
		return new JobManagerPage(getWebDriverWrapper());
	}

	public void backToDashboard() throws Exception
	{
		if (element("dwp.backToList").isDisplayed())
		{
			element("dwp.backToList").click();
			waitStatusDlg();
		}
	}

	public String exportRetrieveLog(int rowIndex) throws Exception
	{
		element("dwp.list.Job", String.valueOf(rowIndex)).click();
		waitStatusDlg();
		String dir = FileUtils.getUserDirectoryPath() + "\\downloads";
		String latestFile = getLatestFile(dir);
		if (httpDownload)
		{
			TestCaseManager.getTestCase().startTransaction("");
			TestCaseManager.getTestCase().setPrepareToDownload(true);
			element("dwp.export").click();
			TestCaseManager.getTestCase().stopTransaction();
			String exportedFile = TestCaseManager.getTestCase().getDownloadFile();
			String oldName = new File(exportedFile).getName();
			String path = new File(exportedFile).getAbsolutePath().replace(oldName, "");
			String fileName = TestCaseManager.getTestCase().getDefaultDownloadFileName();
			String file = null;
			if (fileName == null || fileName.length() == 0)
			{
				file = downloadFile(null, latestFile, null);
			}
			else
			{
				renameFile(path, oldName, fileName);
				file = path + fileName;
			}
			return file;
		}
		else
		{
			element("dwp.export").click();
			waitStatusDlg();
			backToJobDetails();
			return downloadFile(null, latestFile, null);
		}
	}

	public List<String> getColumnNamesInList() throws Exception
	{
		List<String> columns = new ArrayList<>();
		int i = 1;
		while (element("dwp.list.column", String.valueOf(i)).isDisplayed() && i != 8)
		{
			columns.add(element("dwp.list.column", String.valueOf(i)).getInnerText());
			if (i == 7)
				i = i + 2;
			else
				i++;
		}
		return columns;
	}

	public boolean isColumnSupportSort() throws Exception
	{
		boolean rst = true;
		int i = 1;
		while (element("dwp.list.column", String.valueOf(i)).isDisplayed() && i != 8)
		{
			if (element("dwp.list.column.sort", String.valueOf(i)).isDisplayed())
			{
				element("dwp.list.column.sort", String.valueOf(i)).click();
				if (!element("dwp.list.column.sort", String.valueOf(i)).getAttribute("class").contains("ui-icon-triangle-1-n"))
					rst = false;
				element("dwp.list.column", String.valueOf(i)).click();
				if (!element("dwp.list.column.sort", String.valueOf(i)).getAttribute("class").contains("ui-icon-triangle-1-s"))
					rst = false;
			}
			else
			{
				rst = false;
			}

			if (!rst)
				break;
			if (i == 7)
				i = i + 2;
			else
				i++;
		}
		return rst;
	}

	public boolean isJobDetailsPage() throws Exception
	{
		if (element("dwp.jobDetailsLabel").isDisplayed())
		{
			if (element("dwp.jobDetailsLabel").getInnerText().equals("Job Manager Results"))
				return true;
			else
			{
				return false;
			}
		}
		else
			return false;
	}

	public List<String> getErrorLogs() throws Exception
	{
		List<String> errors = new ArrayList<>();
		showDetailBtnClick();
		setShowNumsSelector(100);
		int nums = (int) element("dwp.logsTab").getRowCount();
		for (int i = 1; i <= nums; i++)
		{
			if (element("dwp.level").getInnerText().equalsIgnoreCase("error"))
			{
				errors.add(element("dwp.message").getInnerText());
			}
		}
		return errors;
	}

	public void stopJob(int rowIndex) throws Exception
	{
		String id = String.valueOf(rowIndex - 1);
		element("dwp.stopJobButton", id).click();
		waitStatusDlg();
		element("dwp.stopJob.confirm").click();
		waitStatusDlg();
	}

}
