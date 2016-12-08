package com.lombardrisk.pages;

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
		String dir = FileUtils.getUserDirectoryPath() + "/downloads";
		String latestFile = getLatestFile(dir);
		if (httpDownload)
		{
			TestCaseManager.getTestCase().startTransaction("");
			TestCaseManager.getTestCase().setPrepareToDownload(true);
			element("dwp.export").click();
			TestCaseManager.getTestCase().stopTransaction();
			String exportedFile = TestCaseManager.getTestCase().getDownloadFile();
			return getOriginalFile(exportedFile, latestFile);
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

	/**
	 * get error log
	 * 
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * stop job
	 * 
	 * @param rowIndex
	 * @throws Exception
	 */
	public void stopJob(int rowIndex) throws Exception
	{
		String id = String.valueOf(rowIndex - 1);
		element("dwp.stopJobButton", id).click();
		waitStatusDlg();
		element("dwp.stopJob.confirm").click();
		waitStatusDlg();
	}

	/**
	 * open sub item
	 * 
	 * @param jobIndex
	 * @param openMap
	 * @throws Exception
	 */
	public void openSubItem(int jobIndex, boolean openMap) throws Exception
	{
		String id = String.valueOf(jobIndex - 1);
		if (element("dwp.showBatchRun", id).getAttribute("class").contains("ui-icon-triangle-1-e"))
		{
			element("dwp.showBatchRun", id).click();
			waitStatusDlg();
		}
		if (openMap)
		{
			int mapIndex = 0;
			String[] list =
			{ id, String.valueOf(mapIndex) };
			while (element("dwp.showMap", list).isDisplayed() && element("dwp.showMap", list).getAttribute("class").contains("ui-icon-triangle-1-e"))
			{
				element("dwp.showMap", list).click();
				waitStatusDlg();
				mapIndex = mapIndex + 1;
				list[1] = String.valueOf(mapIndex);
			}
		}
	}

	/**
	 * show detail batch run
	 * 
	 * @param jobIndex
	 * @throws Exception
	 */
	public void showBatchRun(int jobIndex) throws Exception
	{
		String id = String.valueOf(jobIndex - 1);
		if (element("dwp.showBatchRun", id).getAttribute("class").contains("ui-icon-triangle-1-e"))
		{
			element("dwp.showBatchRun", id).click();
			waitStatusDlg();
		}
	}

	/**
	 * show detail map
	 * 
	 * @param jobIndex
	 * @param batchRunIndex
	 * @throws Exception
	 */
	private void showMap(int jobIndex, int batchRunIndex) throws Exception
	{
		String[] list =
		{ String.valueOf(jobIndex - 1), String.valueOf(batchRunIndex - 1) };
		if (element("dwp.showMap", list).getAttribute("class").contains("ui-icon-triangle-1-e"))
		{
			element("dwp.showMap", list).click();
			waitStatusDlg();
		}
	}

	/**
	 * get cell text in specific column
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @param openMap
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getColumnCellText(int rowIndex, int columnIndex, boolean openMap) throws Exception
	{
		List<String> columnCell = new ArrayList<>();
		openSubItem(rowIndex, openMap);
		String normal = String.valueOf(rowIndex - 1);
		int rowId = 1;
		while (element("dwp.cellRow", String.valueOf(rowId)).isDisplayed()
				&& element("dwp.cellRow", String.valueOf(rowId)).getAttribute("id").startsWith("formJobManagerListForm:formJobManagerListTable_node_" + normal))
		{
			String[] list =
			{ String.valueOf(rowId), String.valueOf(columnIndex) };
			if (element("dwp.cellText", list).isDisplayed())
				columnCell.add(element("dwp.cellText", list).getInnerText());
			else if (element("dwp.cellText2", list).isDisplayed())
				columnCell.add(element("dwp.cellText2", list).getInnerText());
			rowId++;
		}
		return columnCell;
	}

	/**
	 * get all batch run status under specific job
	 * 
	 * @param JobIndex
	 * @return List
	 * @throws Exception
	 */
	public List<String> getBatchRunStatus(int JobIndex) throws Exception
	{
		List<String> BatchRunStatus = new ArrayList<>();
		showBatchRun(JobIndex);
		int BatchRunIndex = 1;
		String[] list =
		{ String.valueOf(JobIndex - 1), String.valueOf(BatchRunIndex - 1), "8" };
		while (element("dwp.cellText4", list).isDisplayed())
		{
			BatchRunStatus.add(element("dwp.cellText4", list).getInnerText());
			BatchRunIndex++;
			list[1] = String.valueOf(BatchRunIndex - 1);
		}
		return BatchRunStatus;
	}

	/**
	 * get all map status under specific job and batch run
	 * 
	 * @param JobIndex
	 * @param
	 * @return List
	 * @throws Exception
	 */
	public List<String> getMapStatus(int JobIndex, int BatchRunIndex) throws Exception
	{
		List<String> mapStatus = new ArrayList<>();
		showBatchRun(JobIndex);
		showMap(JobIndex, BatchRunIndex);
		int mapID = 1;
		String[] list =
		{ String.valueOf(JobIndex), String.valueOf(BatchRunIndex - 1), String.valueOf(mapID - 1), "8" };
		while (element("dwp.cellText3", list).isDisplayed())
		{
			mapStatus.add(element("dwp.cellText3", list).getInnerText());
			mapID++;
			list[2] = String.valueOf(mapID - 1);
		}

		return mapStatus;
	}

	public List<String> getMapMessage(int JobIndex, int BatchRunIndex) throws Exception
	{
		List<String> messages = new ArrayList<>();
		showBatchRun(JobIndex);
		showMap(JobIndex, BatchRunIndex);
		int mapID = 1;
		String[] list =
		{ String.valueOf(JobIndex - 1), String.valueOf(BatchRunIndex - 1), String.valueOf(mapID - 1) };
		while (element("dwp.mapMessage", list).isDisplayed())
		{
			messages.add(element("dwp.mapMessage", list).getAttribute("value"));
			mapID++;
			list[2] = String.valueOf(mapID - 1);
		}

		return messages;
	}

	/**
	 * get batchRun progress
	 * 
	 * @param JobIndex
	 * @return
	 * @throws Exception
	 */
	public List<String> getBatchRunProgress(int JobIndex) throws Exception
	{
		List<String> BatchRunProgress = new ArrayList<>();
		showBatchRun(JobIndex);
		int BatchRunIndex = 1;
		String[] list =
		{ String.valueOf(JobIndex - 1), String.valueOf(BatchRunIndex - 1), "11" };
		while (element("dwp.cellText5", list).isDisplayed())
		{
			BatchRunProgress.add(element("dwp.cellText5", list).getInnerText());
			BatchRunIndex++;
			list[1] = String.valueOf(BatchRunIndex - 1);
		}
		return BatchRunProgress;
	}

	/**
	 * if batchRub link exist
	 * 
	 * @param jobIndex
	 * @param batchRunIndex
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isBatchRunLinkExits(int jobIndex, int batchRunIndex) throws Exception
	{
		String[] list =
		{ String.valueOf(jobIndex - 1), String.valueOf(batchRunIndex - 1) };
		if (element("dwp.batchRunName", list).isDisplayed())
			return true;
		else
			return false;
	}

	/**
	 * enter job result page
	 * 
	 * @param jobIndex
	 * @param batchRunIndex
	 * @return JobResultPage
	 * @throws Exception
	 */
	public JobResultPage enterJobResultPage(int jobIndex, int batchRunIndex) throws Exception
	{
		String[] list =
		{ String.valueOf(jobIndex - 1), String.valueOf(batchRunIndex - 1) };
		element("dwp.batchRunName", list).click();
		waitStatusDlg();
		return new JobResultPage(getWebDriverWrapper());
	}

	public boolean isDeleteIconExits(int jobIndex) throws Exception
	{
		return element("dwp.deleteIcon", String.valueOf(jobIndex - 1)).isDisplayed();
	}

	public void deleteJob(int jobIndex) throws Exception
	{
		element("dwp.deleteIcon", String.valueOf(jobIndex - 1)).click();
		waitStatusDlg();
	}

	public int getJobNums() throws Exception
	{
		return element("dwp.rowAmt").getNumberOfMatches();
	}

}
