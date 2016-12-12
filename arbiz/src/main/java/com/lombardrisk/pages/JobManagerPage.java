package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Create by Leo Tu on 6/23/16
 */

public class JobManagerPage extends AbstractPage
{
	/**
	 * 
	 * @param webDriverWrapper
	 */
	public JobManagerPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	/**
	 * get latest job detail info
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getLatestJobInfo() throws Exception
	{
		return getJobInfo(1);
	}

	/**
	 * get job detail info
	 * 
	 * @param rowIndex
	 * @return detail info(List)
	 * @throws Exception
	 */
	public List<String> getJobInfo(int rowIndex) throws Exception
	{
		logger.info("Get job info");
		List<String> jobInfo = new ArrayList<String>();
		String specical = String.valueOf(rowIndex - 1);
		String normal = String.valueOf(rowIndex);
		String runType = element("jmp.list.runType", normal).getInnerText();
		if (!runType.equalsIgnoreCase("EtlJob"))
		{
			String detailName = element("jmp.list.Name", specical).getInnerText().replace("|", "~");
			for (String item : detailName.split("~"))
			{
				jobInfo.add(item);
			}
		}
		else
		{
			String Name = element("jmp.list.Name", specical).getInnerText();
			jobInfo.add(Name);
			jobInfo.add("");
			jobInfo.add("");
			jobInfo.add("");

		}
		jobInfo.add(element("jmp.list.RerenceDate", normal).getInnerText());// 4
		jobInfo.add(element("jmp.list.runType", normal).getInnerText());// 5
		jobInfo.add(element("jmp.list.jobStart", normal).getInnerText());// 6
		jobInfo.add(element("jmp.list.jobComplete", normal).getInnerText());// 7
		jobInfo.add(element("jmp.list.jobStatus", normal).getInnerText());// 8
		jobInfo.add(element("jmp.list.startedBy", normal).getInnerText());// 9
		jobInfo.add(element("jmp.list.jobMessage", normal).getInnerText());// 10
		jobInfo.add(element("jmp.list.jobProgress", normal).getInnerText());// 11
		return jobInfo;

	}

	/**
	 * enter JobDetailsPage
	 * 
	 * @param formCode
	 * @param formVersion
	 * @param referenceDate
	 * @return JobDetailsPage
	 * @throws Exception
	 */
	public JobDetailsPage enterJobDetailsPage(String formCode, String formVersion, String referenceDate) throws Exception
	{
		logger.info("Enter job details page");
		element("jmp.filter").input(formCode);
		Thread.sleep(1000);
		int nums = (int) element("jmp.table").getRowCount();
		int index = 0;
		for (int i = 1; i <= nums; i++)
		{
			List<String> JobInfo = getJobInfo(i);
			if (JobInfo.get(2).equals(formCode) && JobInfo.get(3).equals(formVersion) && JobInfo.get(4).equals(referenceDate))
			{
				index = i;
				break;
			}
		}

		element("jmp.list.Name", String.valueOf(index - 1)).click();
		waitStatusDlg();
		return new JobDetailsPage(getWebDriverWrapper());
	}

	/**
	 * enter JobDetailsPage
	 * 
	 * @param rowIndex
	 * @return JobDetailsPage
	 * @throws Exception
	 */
	public JobDetailsPage enterJobDetailsPage(int rowIndex) throws Exception
	{
		logger.info("Enter job details page");
		element("jmp.list.Name", String.valueOf(rowIndex - 1)).click();
		waitStatusDlg();
		return new JobDetailsPage(getWebDriverWrapper());
	}

	/**
	 * back to ListPage
	 * 
	 * @return ListPage
	 * @throws Exception
	 */
	public ListPage backToDashboard() throws Exception
	{
		logger.info("Back to list page");
		element("jmp.backToList2").click();
		Thread.sleep(300);
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * get export succeed job
	 * 
	 * @return Form
	 * @throws Exception
	 */
	public String getPassedExpoertedJob() throws Exception
	{
		String Form = null;
		int nums = (int) element("jmp.table").getRowCount();
		for (int i = 1; i <= nums; i++)
		{
			if (element("jmp.list.runType", String.valueOf(i)).getInnerText().equals("ExportJob") && element("jmp.list.jobStatus", String.valueOf(i)).getInnerText().equals("SUCCESS"))
			{
				String list[] = element("jmp.list.Name", String.valueOf(i - 1)).getInnerText().split("|");
				Form = list[3] + " v" + list[4];
				break;
			}
		}
		backToDashboard();
		return Form;
	}

	/**
	 * get all columns name
	 * 
	 * @return columns name(List)
	 * @throws Exception
	 */
	public List<String> getColumnNamesInList() throws Exception
	{
		List<String> columns = new ArrayList<>();
		int nums = element("jmp.list.head").getAllMatchedElements().size();
		for (int i = 1; i <= nums; i++)
		{
			columns.add(element("jmp.list.column", String.valueOf(i)).getInnerText());
		}
		return columns;
	}

	/**
	 * verify if could sort column
	 * 
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isColumnSupportSort() throws Exception
	{
		boolean rst = true;
		int nums = element("jmp.list.head").getAllMatchedElements().size();
		for (int i = 1; i <= nums; i++)
		{
			if (element("jmp.list.column.sort", String.valueOf(i)).isDisplayed())
			{
				element("jmp.list.column.sort", String.valueOf(i)).click();
				if (!element("jmp.list.column.sort", String.valueOf(i)).getAttribute("class").contains("ui-icon-triangle-1-n"))
					rst = false;
				element("jmp.list.column.sort", String.valueOf(i)).click();
				if (!element("jmp.list.column.sort", String.valueOf(i)).getAttribute("class").contains("ui-icon-triangle-1-s"))
					rst = false;
			}
			else
			{
				rst = false;
			}

			if (!rst)
				break;
		}
		return rst;
	}

	/**
	 * verify if filter exist
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isFilterExist() throws Exception
	{
		if (element("jmp.filter").isDisplayed())
			return true;
		else
			return false;
	}

	public int getJobNum() throws Exception
	{
		int firstPage = element("dwp.rowAmt").getNumberOfMatches();
		if ("0".equalsIgnoreCase(element("dwp.lastPageStatus").getAttribute("tabindex")))
		{
			element("dwp.lastPageStatus").click();
			waitStatusDlg();
			int lastPageNo;
			if (element("dwp.lastPageNo1").isDisplayed())
				lastPageNo = Integer.parseInt(element("dwp.lastPageNo1").getInnerText());
			else
				lastPageNo = Integer.parseInt(element("dwp.lastPageNo2").getInnerText());

			int lastPage = element("dwp.rowAmt").getNumberOfMatches();
			int maxRow = Integer.parseInt(element("dwp.displayedRow").getSelectedText());
			return (lastPageNo - 1) * maxRow + lastPage;
		}
		else
			return firstPage;
	}

}
