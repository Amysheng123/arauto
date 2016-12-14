package com.lombardrisk.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.util.PropHelper;
import org.yiwan.webcore.web.IWebDriverWrapper;
import org.yiwan.webcore.web.PageBase;

import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;

/**
 * Refactored by Leo Tu on 1/29/16
 */
public abstract class AbstractPage extends PageBase
{
	public static String userName, password, connectedDB, format;
	public static boolean setOriginalName;

	protected static boolean httpDownload = Boolean.parseBoolean(PropHelper.getProperty("download.enable").trim());

	/**
	 * 
	 * @param webDriverWrapper
	 */
	public AbstractPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		TestTemplate test = new TestTemplate();
		userName = test.getUserName();
		password = test.getPassword();
		connectedDB = test.connetcedDB();
		format = test.getFormat();
		setOriginalName = test.isSetOriginalName();
	}

	/**
	 * Get all files in specific folder
	 *
	 * @param path
	 * @param files
	 * @return All files(List)
	 */
	protected static List<File> getFiles(String path, List<File> files)
	{
		File realFile = new File(path);
		if (realFile.isDirectory())
		{
			File[] subFiles = realFile.listFiles();
			for (File file : subFiles)
			{
				if (file.isDirectory())
				{
					getFiles(file.getAbsolutePath(), files);
				}
				else
				{
					files.add(file);
				}
			}
		}
		return files;
	}

	/**
	 * Sort files by modified time
	 *
	 * @param path
	 * @return Files(List)
	 */
	protected static List<File> sortFileByModifiedTime(String path)
	{

		List<File> list = getFiles(path, new ArrayList<File>());

		if (list != null && !list.isEmpty())
		{

			Collections.sort(list, new Comparator<File>() {
				public int compare(File file, File newFile)
				{
					if (file.lastModified() < newFile.lastModified())
					{
						return 1;
					}
					else if (file.lastModified() == newFile.lastModified())
					{
						return 0;
					}
					else
					{
						return -1;
					}

				}
			});

		}

		return list;
	}

	/**
	 * Get latest file in specific folder
	 *
	 * @param path
	 * @return Latest file
	 */
	protected static String getLatestFile(String path)
	{
		List<File> files = sortFileByModifiedTime(path);
		if (!files.isEmpty())
			return files.get(0).toString();
		else
			return "";
	}

	/**
	 * Generate random string
	 *
	 * @param length
	 * @return Random string
	 */
	protected static String getRandomString(int length)
	{
		TestTemplate test = new TestTemplate();
		return test.getRandomString(length);
	}

	public void refreshPage()
	{
		getWebDriverWrapper().navigate().refresh();
	}

	/**
	 * Wait for ajax dialog disappear
	 * 
	 * @throws Exception
	 */
	protected void waitStatusDlg() throws Exception
	{
		waitThat("ap.ajaxstatusDlg").toBeInvisible();
		waitThat("ap.ajaxstatusDlg2").toBeInvisible();
		Thread.sleep(500);
	}

	/**
	 * Wait for elements loaded
	 * 
	 * @throws Exception
	 */
	protected void waitForPageLoaded() throws InterruptedException
	{
		String js = "return document.readyState";
		boolean rst = "complete".equals(executeScript(js));
		while (!rst)
		{
			logger.info("Current status is[" + executeScript(js) + "],loading...");
			Thread.sleep(300);
			rst = "complete".equals(executeScript(js));
		}
		Thread.sleep(300);
	}

	/**
	 * Wait for dropList Loaded
	 * 
	 * @param locator
	 * @throws Exception
	 */
	protected void waitForDropListLoaded(String locator) throws Exception
	{
		long init = System.currentTimeMillis();
		int i = 0;
		while (i < 5)
		{
			try
			{
				if (!element(locator).getAllOptions().isEmpty())
					break;
				long cur = System.currentTimeMillis();
				if ((cur - init) / 1000 > 5)
					break;
			}
			catch (StaleElementReferenceException e)
			{
				logger.warn("Try again");
				e.printStackTrace();
				i++;
			}
		}

	}

	/**
	 * Split return
	 * 
	 * @param returnName
	 * @return Return
	 * @throws Exception
	 */
	protected List<String> splitReturn(String returnName)
	{
		List<String> returnNV = new ArrayList<>();
		String formCode, formVersion, Form;
		if (returnName.contains("("))
		{
			returnName = returnName.replace("(", "#");
			returnName = returnName.split("#")[0];
		}
		if (returnName.trim().contains(" "))
		{
			formCode = returnName.split(" ")[0].toUpperCase();
			formVersion = returnName.split(" ")[1].trim().toLowerCase().replace("v", "");
			Form = formCode + " v" + formVersion;
		}
		else
		{
			formCode = returnName.split("_")[0].toUpperCase();
			formVersion = returnName.split("_")[1].trim().toLowerCase().replace("v", "");
			Form = formCode + "_v" + formVersion;
		}

		returnNV.add(formCode);
		returnNV.add(formVersion);
		returnNV.add(Form);
		return returnNV;
	}

	/**
	 * Click EnterKey
	 * 
	 * @throws Exception
	 */
	protected void clickEnterKey() throws InterruptedException
	{
		actions().sendKeys(Keys.ENTER).perform();
		Thread.sleep(1000);
	}

	/**
	 * Select date
	 * 
	 * @param date
	 * @throws Exception
	 */
	protected void selectDate(String date) throws Exception
	{
		getFormatFromDB();
		String year, month, day;
		if ("en_GB".equalsIgnoreCase(format))
		{
			month = date.substring(3, 5);
			day = date.substring(0, 2);
			year = date.substring(6, 10);
		}
		else if ("en_US".equalsIgnoreCase(format))
		{
			day = date.substring(3, 5);
			month = date.substring(0, 2);
			year = date.substring(6, 10);
		}
		else if ("zh_CN".equalsIgnoreCase(format))
		{
			month = date.substring(8, 10);
			day = date.substring(5, 7);
			year = date.substring(0, 4);
		}
		else
		{
			month = date.substring(3, 5);
			day = date.substring(0, 2);
			year = date.substring(6, 10);
		}
		if (day.startsWith("0"))
		{
			day = day.substring(1);
		}
		switch (month)
		{
			case "01":
				month = "Jan";
				break;
			case "02":
				month = "Feb";
				break;
			case "03":
				month = "Mar";
				break;
			case "04":
				month = "Apr";
				break;
			case "05":
				month = "May";
				break;
			case "06":
				month = "Jun";
				break;
			case "07":
				month = "Jul";
				break;
			case "08":
				month = "Aug";
				break;
			case "09":
				month = "Sep";
				break;
			case "10":
				month = "Oct";
				break;
			case "11":
				month = "Nov";
				break;
			case "12":
				month = "Dec";
				break;
		}
		element("ap.calendar.month").selectByVisibleText(month);
		Thread.sleep(300);
		element("ap.calendar.year").selectByVisibleText(year);
		Thread.sleep(300);
		String[] list =
		{ day, day };
		element("ap.calendar.day", list).click();
		waitStatusDlg();
	}

	/**
	 * Click current date
	 * 
	 * @throws Exception
	 */
	protected void clickCurrentDate() throws Exception
	{
		for (int r = 1; r <= 6; r++)
		{
			boolean clicked = false;
			for (int c = 1; c <= 7; c++)
			{
				String[] list =
				{ String.valueOf(r), String.valueOf(c) };
				if (element("ap.calendar", list).getAttribute("class").contains("ui-datepicker-current-day"))
				{
					element("ap.calendar", list).click();
					clicked = true;
					break;
				}
			}
			if (clicked)
				break;
		}
	}

	/**
	 * get Regulator IDRange Start
	 * 
	 * @param Regulator
	 * @return IDRangeStart
	 */
	protected String getRegulatorIDRangeStart(String Regulator) throws Exception
	{
		TestTemplate test = new TestTemplate();
		return test.getRegulatorIDRangeStart(Regulator);
	}

	/**
	 * get Regulator IDRangEnd from DB
	 * 
	 * @param Regulator
	 * @return IDRangEnd
	 */
	protected String getRegulatorIDRangEnd(String Regulator) throws Exception
	{
		TestTemplate test = new TestTemplate();
		return test.getRegulatorIDRangEnd(Regulator);
	}

	/**
	 * get regulator prefix in toolset env
	 * 
	 * @param Regulator
	 * @return prefix
	 */
	protected String getToolsetRegPrefix(String Regulator) throws Exception
	{
		TestTemplate test = new TestTemplate();
		return test.getToolsetRegPrefix(Regulator);
	}

	/**
	 * get extend grid name from DB
	 * 
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param cellName
	 * @return grid name
	 */
	protected String getExtendCellName(String Regulator, String formCode, String version, String cellName) throws Exception
	{
		TestTemplate test = new TestTemplate();
		return test.getExtendCellName(Regulator, formCode, version, cellName);
	}

	/**
	 * get page name from DB
	 * 
	 * @param Regulator
	 * @param form
	 * @param version
	 * @param cellName
	 * @param extendCell
	 * @return page name(List)
	 */
	protected List<String> getPageNames(String Regulator, String form, String version, String cellName, String extendCell) throws Exception
	{
		TestTemplate test = new TestTemplate();
		return test.getPageNames(Regulator, form, version, cellName, extendCell);
	}

	/**
	 * get cell Type from DB
	 * 
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param cellName
	 * @param extendCell
	 * @return cell Type
	 */
	protected String getCellType(String Regulator, String formCode, String version, String cellName, String extendCell) throws Exception
	{
		TestTemplate test = new TestTemplate();
		return test.getCellType(Regulator, formCode, version, cellName, extendCell);
	}

	/**
	 * get downloaded file
	 * 
	 * @param exportType
	 * @param LatestFileName
	 * @param dir
	 * @return downloaded file
	 * @throws Exception
	 */
	protected String downloadFile(String exportType, String LatestFileName, String dir) throws Exception
	{
		if (exportType == null)
			exportType = "";
		String filePath, fileName = null;
		if (dir == null)
			dir = FileUtils.getUserDirectoryPath() + "/downloads/";
		boolean flag = true;
		long statTime = System.currentTimeMillis();

		while (flag)
		{
			fileName = getLatestFile(dir);
			long curTime = System.currentTimeMillis();
			if (!fileName.equalsIgnoreCase(LatestFileName) && !fileName.endsWith(".tmp") && !fileName.endsWith(".crdownload"))
				flag = false;
			else if ((curTime - statTime) / 1000 > 600)
			{
				flag = false;
				fileName = null;
			}
			else
			{
				logger.info("Downloading");
				waitThat().timeout(5000);
			}
		}
		if (exportType.toLowerCase().startsWith("ds"))
			filePath = fileName;
		else
		{
			File exportedFile = new File("target/result/data/download/" + new File(fileName).getName());
			if (exportedFile.exists())
				exportedFile.delete();
			if (fileName != null)
				FileUtils.copyFile(new File(fileName), exportedFile);
			filePath = exportedFile.getAbsolutePath();
		}
		return filePath;
	}

	/**
	 * query job status from DB
	 * 
	 * @return job status
	 * @throws Exception
	 */
	protected String getJobStatus() throws Exception
	{
		String SQL = "SELECT MAX(\"JOBINSTANCEID\") FROM \"JOB_INSTANCE\"";
		int jobId = Integer.parseInt(DBQuery.queryRecord(SQL));
		SQL = "SELECT \"BATCHSTATUS\" FROM \"JOB_EXECUTION\" where \"JOBINSTANCEID\"=" + jobId;
		return DBQuery.queryRecord(SQL);
	}

	/**
	 * verify if job status is success
	 * 
	 * @return true or false
	 * @throws Exception
	 */
	protected boolean isJobSucceed() throws Exception
	{
		boolean isSuccess = false;
		boolean flag = true;
		while (flag)
		{
			String status = getJobStatus();
			if (status != null)
			{
				if (!status.equalsIgnoreCase("FAILED"))
					isSuccess = true;
				flag = false;
			}
			else
			{
				Thread.sleep(5000);
			}
		}
		Thread.sleep(10000);
		return isSuccess;
	}

	/**
	 * rename file
	 * 
	 * @param path
	 * @param oldname
	 * @param newname
	 */
	public void renameFile(String path, String oldname, String newname)
	{
		if (!oldname.equals(newname))
		{
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			if (!oldfile.exists())
			{
				return;
			}
			if (newfile.exists())
			{
				logger.warn("The file already exist, old file will be deleted");
				newfile.delete();
			}
			oldfile.renameTo(newfile);
		}
		else
		{
			logger.error("New file name is same with old one!");
		}
	}

	public String trimRight(String sString)
	{
		String sResult = "";
		if (sString.startsWith(" "))
		{
			sResult = sString.substring(0, sString.indexOf(sString.trim().substring(0, 1)) + sString.trim().length());
		}
		else
			sResult = sString.trim();

		return sResult;
	}

	public void getFormatFromDB() throws Exception
	{
		TestTemplate t = new TestTemplate();
		format = t.returnFormat();
	}

	public String getFormatFromDB(String UserName) throws Exception
	{
		logger.info("update user language and format");
		String SQL = "SELECT MAX(\"ID\") FROM \"USR_PREFERENCE\" WHERE \"USER_ID\"='" + UserName.toLowerCase() + "' and \"PREFERENCE_NAME\"='LANGUAGE'";
		String id = DBQuery.queryRecordSpecDB("ar", null, SQL);
		SQL = "SELECT \"PREFERENCE_CODE\" FROM \"USR_PREFERENCE\" WHERE \"USER_ID\"='" + UserName.toLowerCase() + "' and \"ID\"=" + id;
		return DBQuery.queryRecordSpecDB("ar", null, SQL);
	}

	public String getOriginalFile(String exportedFile, String latestFile, boolean getOriginalName) throws Exception
	{
		String file;
		if (getOriginalName)
		{
			String oldName = new File(exportedFile).getName();
			String path = new File(exportedFile).getAbsolutePath().replace(oldName, "");
			String fileName = TestCaseManager.getTestCase().getDefaultDownloadFileName();
			if (fileName == null || fileName.length() == 0)
			{
				file = downloadFile(null, latestFile, null);
			}
			else
			{
				renameFile(path, oldName, fileName);
				file = path + fileName;
			}
		}
		else
		{
			file = exportedFile;
		}
		logger.info("Exported file is:" + file);
		return file;
	}
}
