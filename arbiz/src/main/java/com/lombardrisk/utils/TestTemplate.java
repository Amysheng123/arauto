package com.lombardrisk.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.yiwan.webcore.test.ITestBase;
import org.yiwan.webcore.test.TestBase;
import org.yiwan.webcore.test.pojo.TestEnvironment;
import org.yiwan.webcore.util.Helper;
import org.yiwan.webcore.util.PropHelper;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.TxtUtil;
import com.lombardrisk.utils.fileService.XMLUtil;

public class TestTemplate extends TestBase
{
	protected static String targetLogFolder = System.getProperty("user.dir") + "\\target\\result\\logs\\";
	protected static String testDataFolderName = PropHelper.getProperty("data.type").trim();
	protected static boolean startService = Boolean.parseBoolean(PropHelper.getProperty("test.startService").trim());
	protected static String envPath = PropHelper.getProperty("test.environment.path").trim();
	protected static boolean httpDownload = Boolean.parseBoolean(PropHelper.getProperty("download.enable").trim());
	protected static String testdata_admin = null;
	protected static String testdata_DeleteReturn = null;
	protected static String testdata_edition = null;
	protected static String testdata_editForm = null;
	protected static String testdata_highlight = null;
	protected static String testdata_Utility = null;
	protected static String testdata_Workflow = null;
	protected static File editFormLogData = null;
	protected static String jobData = null;
	protected static String testdata_General = null;
	protected static File testRstFile = null;
	protected static String testdata_updateForm = null;
	protected static String testdata_OtherModule = null;
	protected static String testdata_FormVariable = null;
	protected static String testdata_RowLimit = null;
	protected static String testdata_Threshold = null;
	protected static String testdata_DropDown = null;
	protected static String testdata_GridWithinGrid = null;
	protected static String testdata_Contextual = null;
	protected static String testdata_Calendar = null;
	protected static String testdata_ReturnList = null;
	protected static String testdata_importExportFormat = null;
	protected static String testdata_Export_External = null;
	protected static String testdata_BatchRun = null;

	protected static String format = "";
	protected static String userName = "";
	protected static String AR_DBName = "";
	protected static String T_DBName = "";
	protected static String AR_DBType = "";
	protected static String T_DBType = "";
	protected static String AR_Server = "";
	protected static String T_Server = "";
	protected static String AR_IP = "";
	protected static String T_IP = "";
	protected static String AR_SID = "";
	protected static String T_SID = "";
	protected static String ConnectDBType = "";
	protected static String password = "password";
	static File nameFile = null;
	static TestEnvironment environment;
	protected Module m;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
	String curDate = sdf.format(new Date());
	WebDriver driver = null;

	/**
	 * set test data
	 *
	 * @throws Exception
	 */
	@BeforeSuite
	protected void beforeSuite() throws Exception
	{
		if (startService)
		{
			envPath = envPath + "/bin/start.bat";
			if (new File(envPath).exists())
			{
				logger.info("Begin start ar service");
				logger.info("Test env path is: " + envPath);
				Runtime.getRuntime().exec("cmd /c start " + envPath);
				logger.info("Starting ...waiting 90s");
				Thread.sleep(1000 * 90);
			}
		}
		nameFile = new File("target/result/Names.txt");
		if (nameFile.exists())
		{
			nameFile.delete();
		}
		nameFile.createNewFile();

		if (testDataFolderName.equalsIgnoreCase("ar"))
		{
			ConnectDBType = "ar";
			testDataFolderName = "data_ar";
		}
		else if (testDataFolderName.equalsIgnoreCase("toolSet"))
		{
			testDataFolderName = "data_toolset";
			ConnectDBType = "toolSet";
		}
		else if (testDataFolderName.equalsIgnoreCase("toolsetNull"))
		{
			testDataFolderName = "data_toolset_allownull";
			ConnectDBType = "toolSet";
		}

		testdata_admin = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Admin\\Admin.xml";
		testdata_DeleteReturn = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\DeleteReturn\\DeleteReturn.xml";
		testdata_edition = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Edition\\Edition.xml";
		testdata_editForm = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\EditForm\\EditForm.xml";
		editFormLogData = new File(testdata_editForm.replace("EditForm.xml", "EditForm_Data_Log.xlsx"));
		testdata_highlight = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\HighLight\\HighLight.xml";
		testdata_Utility = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Utility\\Utility.xml";
		testdata_Workflow = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Workflow\\Workflow.xml";
		testdata_General = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\GeneralFunction\\GeneralFunction.xml";
		jobData = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Job\\Job.xml";
		testdata_updateForm = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\UpdateForm\\UpdateForm.xml";
		testdata_OtherModule = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\OtherModule\\OtherModule.xml";
		testdata_FormVariable = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Admin\\FormVariable.xml";
		testdata_RowLimit = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\RowLimit\\RowLimit.xml";
		testdata_Threshold = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Threshold\\Threshold.xml";
		testdata_DropDown = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\DropDown\\DropDown.xml";
		testdata_GridWithinGrid = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\GridWithinGrid\\GridWithinGrid.xml";
		testdata_Contextual = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Contextual\\Contextual.xml";
		testdata_Calendar = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\Admin\\Calendar.xml";
		testdata_ReturnList = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\ReturnList\\ReturnList.xml";
		testdata_importExportFormat = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\ImportExportFormat\\ImportExportFormat.xml";
		testdata_Export_External = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\ExportForm_External\\ExportForm_External.xml";
		testdata_BatchRun = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\BatchRun\\BatchRun.xml";

		File testRstFolder = new File("target\\TestResult");
		if (!testRstFolder.exists())
			testRstFolder.mkdir();

		File logFolder = new File(targetLogFolder);
		if (!logFolder.exists())
			logFolder.mkdir();

	}

	@BeforeClass(dependsOnMethods =
	{ "beforeClass" })
	protected void setUpClass() throws Exception
	{
	}

	@BeforeMethod
	protected void setMethod() throws Exception
	{
		logger.info("set up  before method");
		try
		{
			setFeatureId(this.getClass().getSimpleName().toLowerCase());
			setScenarioId(getFeatureId());
			setUpTest();

			environment = getTestEnvironment();
			userName = getTestEnvironment().getApplicationServer(0).getUsername();
			password = getTestEnvironment().getApplicationServer(0).getPassword();

			AR_DBName = getTestEnvironment().getDatabaseServer(0).getSchema();
			AR_DBType = getTestEnvironment().getDatabaseServer(0).getDriver();
			AR_Server = getTestEnvironment().getDatabaseServer(0).getHost();
			if (AR_DBType.equalsIgnoreCase("oracle"))
			{
				AR_IP = AR_Server.split("@")[0];
				AR_SID = AR_Server.split("@")[1];
			}
			try
			{
				T_DBName = getTestEnvironment().getDatabaseServer(1).getSchema();
				T_DBType = getTestEnvironment().getDatabaseServer(1).getDriver();
				T_Server = getTestEnvironment().getDatabaseServer(1).getHost();
				if (AR_DBType.equalsIgnoreCase("oracle"))
				{
					T_IP = AR_Server.split("@")[0];
					T_SID = AR_Server.split("@")[1];
				}
			}
			catch (Exception e)
			{
				//
			}

			getWebDriverWrapper().navigate().to(getTestEnvironment().getApplicationServer(0).getUrl());
			report(Helper.getTestReportStyle(getTestEnvironment().getApplicationServer(0).getUrl(), "open test server url"));

			m = new Module(this);
			m.homePage.logon();

			List<String> userList = TxtUtil.getFileContent(nameFile);
			if (!userList.contains(userName))
			{
				format = getFormatFromDB();
				String expectedLang = PropHelper.getProperty("Regional.language").trim();
				logger.info("Expected language is:" + expectedLang);
				if (format == null || !format.equalsIgnoreCase(expectedLang))
				{
					m.listPage.enterPreferencePage();
					m.preferencePage.selectLanguageByValue(expectedLang);
					format = expectedLang;
					TxtUtil.writeToTxt(nameFile, userName);
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

	}

	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestContext testContext, Method method, ITestResult testResult) throws Exception
	{
		if (testResult.getThrowable() != null)
			logger.error(method.getName(), testResult.getThrowable());

		logger.info("tearDown after method");
		tearDownTest();
	}

	@AfterSuite
	public void SyncQC() throws Exception
	{
		File from = new File(System.getProperty("user.dir") + "\\" + "target\\TestResult");
		File to = new File("C:\\ARAutoTestResult");
		FileUtils.copyDirectory(from, to);

		if (PropHelper.getProperty("qc.sync").trim().equalsIgnoreCase("y"))
		{
			String TestStatusFile = System.getProperty("user.dir") + "\\" + "target\\TestResult\\" + curDate + "\\TestStatus.xlsx";
			logger.info("Reading data from " + TestStatusFile);
			UpdateCaseInQC.setStatus(TestStatusFile);
		}
	}

	public String getFormatFromDB() throws Exception
	{
		logger.info("update user language and format");
		String SQL = "SELECT MAX(\"ID\") FROM \"USR_PREFERENCE\" WHERE \"USER_ID\"='" + userName.toLowerCase() + "' and \"PREFERENCE_NAME\"='LANGUAGE'";
		String id = DBQuery.queryRecordSpecDB("ar", null, SQL);
		SQL = "SELECT \"PREFERENCE_CODE\" FROM \"USR_PREFERENCE\" WHERE \"USER_ID\"='" + userName.toLowerCase() + "' and \"ID\"=" + id;
		format = DBQuery.queryRecordSpecDB("ar", null, SQL);
		return format;
	}

	public String returnFormat() throws Exception
	{
		return format;
	}

	public List<String> createFolderAndCopyFile(String Function, String fileName) throws Exception
	{
		logger.info("Begin setup test folder and test data");
		List<String> Files = new ArrayList<String>();
		List<String> FuncList = Arrays.asList("CheckRule", "CreateForm", "ExportForm", "ImportForm", "RetrieveForm", "ImportExport", "Precision", "ComputeForm", "DataSchedule", "RowLimit",
				"Threshold", "DropDown", "GridWithinGrid", "Contextual", "ReturnList", "ExportForm_External", "BatchRun");

		if (FuncList.contains(Function))
		{
			String testDataFolderString = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\" + Function + "\\";
			String testFile = null;
			if (fileName != null)
				testFile = testDataFolderString + fileName;
			String testDataFolder;
			if (Function.equals("CheckRule"))
				testDataFolder = testDataFolderString + "TestData\\";
			else
				testDataFolder = testDataFolderString + "CheckCellValue\\";

			// add test data folder
			Files.add(testDataFolder);

			String currentDayFolderString = System.getProperty("user.dir") + "\\" + "target\\TestResult\\" + curDate;
			File currentDayFolder = new File(currentDayFolderString);
			if (!currentDayFolder.exists())
				currentDayFolder.mkdir();

			String testResultFolderString = currentDayFolderString + "\\" + Function;
			File testResultFolder = new File(testResultFolderString);
			if (!testResultFolder.exists())
				testResultFolder.mkdir();

			File checkDataFolder;
			if (Function.equals("CheckRule"))
				checkDataFolder = new File(testResultFolder + "\\" + "TestData\\");
			else
				checkDataFolder = new File(testResultFolder + "\\" + "CheckCellValue\\");

			if (!checkDataFolder.exists())
				checkDataFolder.mkdir();
			// add test result check data folder
			Files.add(checkDataFolder.getAbsolutePath() + "\\");

			testRstFile = new File(testResultFolder + "\\" + fileName);
			if (!testRstFile.exists() && fileName != null)
				FileUtils.copyFile(new File(testFile), testRstFile);

			// add test result file
			Files.add(testRstFile.getAbsolutePath());

			// add import file path
			Files.add(testDataFolder.replace("CheckCellValue", "ImportFile"));
		}

		return Files;

	}

	public List<String> getDBInfo(int DBIndex) throws Exception
	{
		List<String> DBInfo = new ArrayList<>();
		String DBType = environment.getDatabaseServer(DBIndex).getDriver().toString();
		String DBName = environment.getDatabaseServer(DBIndex).getSchema().toString();
		String Server = environment.getDatabaseServer(DBIndex).getHost().toString();
		DBInfo.add(DBType);
		DBInfo.add(DBName);
		DBInfo.add(Server);
		if (DBType.equalsIgnoreCase("oracle"))
		{
			String IP = Server.split("@")[0];
			String SID = Server.split("@")[1];
			DBInfo.add(IP);
			DBInfo.add(SID);
		}
		return DBInfo;
	}

	/**
	 * close formInstancePage
	 *
	 * @throws Exception
	 */
	public void closeFormInstance() throws Exception
	{
		FormInstancePage formInstancePage = m.formInstancePage;
		try
		{
			formInstancePage.closeFormInstance();
		}
		catch (Exception e)
		{
			getWebDriverWrapper().navigate().backward();
		}
	}

	/**
	 * get cell type from DB
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param cellName
	 * @param extendCell
	 * @return Cell type
	 */
	public String getCellType(String Regulator, String formCode, String version, String cellName, String extendCell)
	{

		if (ConnectDBType.equalsIgnoreCase("ar"))
		{
			String ID_Start = getRegulatorIDRangeStart(Regulator);
			String ID_End = getRegulatorIDRangEnd(Regulator);
			String tableName;
			if (extendCell == null)
				tableName = "CFG_RPT_Ref";
			else
				tableName = "CFG_RPT_GridRef";
			String SQL = "select \"Type\" from \"" + tableName + "\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"="
					+ version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") " + "and \"Item\"='" + cellName + "' " + " and \"ID\" between " + ID_Start + " and " + ID_End;

			return DBQuery.queryRecord(SQL);
		}
		else
		{
			String RegPrefix = getToolsetRegPrefix(Regulator);
			String tableName;
			if (extendCell == null)
				tableName = "Ref";
			else
				tableName = "GridRef";
			String SQL = "select \"Type\" from \"" + RegPrefix + "" + tableName + "\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode
					+ "' AND \"Version\"=" + version + " ) " + "and \"Item\"='" + cellName + "'";
			return DBQuery.queryRecord(SQL);
		}
	}

	/**
	 * get regulator prefix for toolset
	 *
	 * @param Regulator
	 * @return prefix
	 */
	public String getToolsetRegPrefix(String Regulator)
	{
		if (Regulator.equalsIgnoreCase("European Common Reporting"))
			return "ECR";
		else if (Regulator.equalsIgnoreCase("Hong Kong Monetary Authority"))
			return "HKMA";
		else if (Regulator.equalsIgnoreCase("Monetary Authority of Singapore"))
			return "MAS";
		else
			return "";
	}

	/**
	 * get Regulator IDRange Start
	 *
	 * @param Regulator
	 * @return IDRangeStart
	 */
	public String getRegulatorIDRangeStart(String Regulator)
	{
		String SQL = "SELECT \"ID_RANGE_START\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
		return DBQuery.queryRecord(SQL);

	}

	/**
	 * get Regulator IDRange End
	 *
	 * @param Regulator
	 * @return IDRangeEnd
	 */
	public String getRegulatorIDRangEnd(String Regulator)
	{
		String SQL = "SELECT \"ID_RANGE_END\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "' AND \"STATUS\"='A'  ";
		return DBQuery.queryRecord(SQL);
	}

	/**
	 * get GridName
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param cellName
	 * @return GridName
	 */
	public String getExtendCellName(String Regulator, String formCode, String version, String cellName)
	{
		if (ConnectDBType.equalsIgnoreCase("ar"))
		{
			String ID_Start = getRegulatorIDRangeStart(Regulator);
			String ID_End = getRegulatorIDRangEnd(Regulator);
			String SQL = "select \"GridName\" from \"CFG_RPT_GridRef\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"="
					+ version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") " + "and \"Item\"='" + cellName + "' " + " and \"ID\" between " + ID_Start + " and " + ID_End;
			String gridName = DBQuery.queryRecord(SQL);
			SQL = "select \"IS_INNERGRID_CELL\" from \"CFG_RPT_GridRef\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"="
					+ version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") " + "and \"Item\"='" + cellName + "' " + " and \"ID\" between " + ID_Start + " and " + ID_End;
			String inner = DBQuery.queryRecord(SQL);
			if (inner != null && inner.equals("1"))
				gridName = gridName + "_INNER";

			return gridName;
		}
		else
		{
			String RegPrefix = getToolsetRegPrefix(Regulator);
			String SQL = "select \"GridName\" from \"" + RegPrefix + "GridRef\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode
					+ "' AND \"Version\"=" + version + " ) " + "and \"Item\"='" + cellName + "'";
			return DBQuery.queryRecord(SQL);
		}
	}

	/**
	 * get DestFld From Sum Rule
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param ruleID
	 * @return DestFld
	 */
	public String getDestFldFromSumRule(String Regulator, String formCode, String version, int ruleID)
	{
		if (ConnectDBType.equalsIgnoreCase("ar"))
		{
			String ID_Start = getRegulatorIDRangeStart(Regulator);
			String ID_End = getRegulatorIDRangEnd(Regulator);

			String SQL = "SELECT \"DestFld\"  FROM \"CFG_RPT_Sums\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version
					+ " and \"ID\" between " + ID_Start + " and " + ID_End + ")  and \"ExpOrder\"=" + ruleID + " and \"ID\" between " + ID_Start + " and " + ID_End;
			return DBQuery.queryRecord(SQL);
		}
		else
		{
			String type = "toolSet";
			String RegPrefix = getToolsetRegPrefix(Regulator);
			String SQL = "SELECT \"DestFld\"  FROM \"" + RegPrefix + "Sums\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode
					+ "' AND \"Version\"=" + version + ")  and \"ExpOrder\"=" + ruleID;
			return DBQuery.queryRecord(SQL);
		}
	}

	/**
	 * get Expression of validation rule
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param ruleType
	 * @param ruleID
	 * @return validation rule
	 */
	public String getValidationExpression(String Regulator, String formCode, String version, String ruleType, int ruleID)
	{
		String SQL = null;
		if (ConnectDBType.equalsIgnoreCase("ar"))
		{
			String ID_Start = getRegulatorIDRangeStart(Regulator);
			String ID_End = getRegulatorIDRangEnd(Regulator);

			if (ruleType.equalsIgnoreCase("val"))
				SQL = "SELECT \"Expression\" FROM \"CFG_RPT_Vals\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version
						+ " and \"ID\" between " + ID_Start + " and " + ID_End + ")  and \"ExpOrder\"=" + ruleID + " and \"ID\" between " + ID_Start + " and " + ID_End;
			else if (ruleType.equalsIgnoreCase("xval"))
				SQL = "SELECT \"Expression\" FROM \"CFG_RPT_XVals\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version
						+ " and \"ID\" between " + ID_Start + " and " + ID_End + ")  and \"ExpOrder\"=" + ruleID + " and \"ID\" between " + ID_Start + " and " + ID_End;
			return DBQuery.queryRecord(SQL);
		}
		else
		{
			String RegPrefix = getToolsetRegPrefix(Regulator);
			if (ruleType.equalsIgnoreCase("val"))
				SQL = "SELECT \"Expression\" FROM \"" + RegPrefix + "Vals\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode
						+ "' AND \"Version\"=" + version + ")  and \"ExpOrder\"=" + ruleID;
			else if (ruleType.equalsIgnoreCase("xval"))
				SQL = "SELECT \"Expression\" FROM \"" + RegPrefix + "XVals\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode
						+ "' AND \"Version\"=" + version + ")  and \"ExpOrder\"=" + ruleID;
			return DBQuery.queryRecord(SQL);
		}
	}

	/**
	 * Logout
	 *
	 * @throws Exception
	 */
	public void logout() throws Exception
	{
		closeFormInstance();
		try
		{
			ListPage listPage = new ListPage(getWebDriverWrapper());
			listPage.logout();
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
		}
	}

	/**
	 * write test result to test result file
	 *
	 * @param caseID
	 * @param testResult
	 * @param module
	 * @throws Exception
	 */
	public void writeTestResultToFile(String caseID, boolean testResult, String module) throws Exception
	{
		String status;
		if (testResult)
		{
			status = "Pass";
			logger.info("Case[" + caseID + "] is passed");
		}
		else
		{
			status = "Fail";
			logger.info("Case[" + caseID + "] is failed");
		}

		if (caseID.length() > 3)
		{
			String source = testDataFolderName + "\\TestStatus.xlsx";
			File TestStatusFile = new File("target\\TestResult\\" + curDate + "\\TestStatus.xlsx");
			if (!TestStatusFile.exists())
				FileUtils.copyFile(new File(source), TestStatusFile);

			for (String caseNo : caseID.split(","))
			{
				List<String> existRow = ExcelUtil.getLastCaseID(TestStatusFile, caseNo);
				if (existRow.size() > 0)
				{
					if (status.equals("Fail"))
					{
						if (existRow.get(2).equals("Pass"))
							ExcelUtil.writeTestRstToFile(TestStatusFile, Integer.parseInt(existRow.get(0)), 1, testResult);
					}
				}
				else
					ExcelUtil.WriteTestRst(TestStatusFile, caseNo, status, module);

			}
		}
	}

	/**
	 * write test result to test result file
	 *
	 * @param TestResultFile
	 * @param rowID
	 * @param colID
	 * @param caseID
	 * @param testResult
	 * @param module
	 * @throws Exception
	 */
	public void writeTestResultToFile(File TestResultFile, int rowID, int colID, String caseID, boolean testResult, String module) throws Exception
	{
		String status;
		if (testResult)
			status = "Pass";
		else
			status = "Fail";

		if (TestResultFile != null)
		{
			ExcelUtil.writeToExcel(TestResultFile, rowID, colID, status);
		}

		if (caseID.length() > 3)
		{
			String source = testDataFolderName + "\\TestStatus.xlsx";
			File TestStatusFile = new File("target\\TestResult\\" + curDate + "\\TestStatus.xlsx");
			if (!TestStatusFile.exists())
			{
				FileUtils.copyFile(new File(source), TestStatusFile);
			}
			caseID = caseID.replace(".", "");
			for (String id : caseID.split(","))
				ExcelUtil.WriteTestRst(TestStatusFile, id, status, module);
		}

	}

	/**
	 * Copy test export failed file to TestResult folder
	 *
	 * @param copyFrom
	 * @throws Exception
	 */
	public void copyFailedFileToTestRst(String copyFrom, String Module) throws Exception
	{
		logger.info("Copy exported file to TestResult folder");
		File sourceFile = new File(copyFrom);
		String fileName = sourceFile.getName();
		File destFolder = new File("target\\TestResult\\" + curDate + "\\" + Module + "\\ExportedFile\\");
		if (!destFolder.exists())
			destFolder.mkdir();

		File destFile = new File("target\\TestResult\\" + curDate + "\\" + Module + "\\ExportedFile\\" + fileName);
		FileUtils.copyFile(sourceFile, destFile);
	}

	/**
	 * Read xml file and get element value
	 *
	 * @param xmlFile
	 * @param ChildNode
	 * @param element
	 * @return element value
	 */
	public String getElementValueFromXML(String xmlFile, String ChildNode, String element)
	{
		try
		{
			return XMLUtil.getElementValueFromXML(xmlFile, ChildNode, element);
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * Read xml file and get element value
	 *
	 * @param xmlFile
	 * @param node
	 * @return element value
	 * @throws Exception
	 */
	public List<String> getElementValueFromXML(String xmlFile, String node) throws Exception
	{
		List<String> elementValue = new ArrayList<String>();
		for (String element : XMLUtil.getelements(xmlFile, node))
		{
			elementValue.add(getElementValueFromXML(xmlFile, node, element));
		}
		return elementValue;
	}

	/**
	 * @param returnName
	 * @return
	 */
	public List<String> splitReturn(String returnName)
	{
		List<String> returnNV = new ArrayList<String>();
		String formCode = null;
		String formVersion = null;
		String Form = null;
		if (!returnName.equalsIgnoreCase("all"))
		{
			if (returnName.trim().contains(" "))
			{
				formCode = returnName.split(" ")[0];
				formVersion = returnName.split(" ")[1].trim().toLowerCase().replace("v", "");
				Form = formCode + " v" + formVersion;
			}
			else
			{
				formCode = returnName.split("_")[0];
				formVersion = returnName.split("_")[1].trim().toLowerCase().replace("v", "");
				Form = formCode + " v" + formVersion;
			}
		}

		returnNV.add(formCode);
		returnNV.add(formVersion);
		returnNV.add(Form);
		return returnNV;
	}

	public ListPage loginAsOtherUser(String userName, String password) throws Exception
	{
		ListPage listPage = m.listPage;
		HomePage homePage = listPage.logout();
		homePage.loginAs(userName, password);
		String SQL = "SELECT MAX(\"ID\") FROM \"USR_PREFERENCE\" WHERE \"USER_ID\"='" + userName.toLowerCase() + "' and \"PREFERENCE_NAME\"='LANGUAGE'";
		String id = DBQuery.queryRecord(SQL);
		SQL = "SELECT \"PREFERENCE_CODE\" FROM \"USR_PREFERENCE\" WHERE \"USER_ID\"='" + userName.toLowerCase() + "' and \"ID\"=" + id;
		format = DBQuery.queryRecord(SQL);
		logger.info("Note: The language is:" + format);

		String expectedLang = PropHelper.getProperty("Regional.language").trim();
		logger.info("Expected language is:" + expectedLang);
		if (format == null || !format.equalsIgnoreCase(expectedLang))
		{
			m.listPage.enterPreferencePage();
			m.preferencePage.selectLanguageByValue(expectedLang);
			format = expectedLang;
		}

		return m.listPage;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getPassword()
	{
		return password;
	}

	public String connetcedDB()
	{
		return ConnectDBType;
	}

	public String getFormat()
	{
		return format;
	}

	/**
	 * get page name
	 *
	 * @param Regulator
	 * @param form
	 * @param version
	 * @param cellName
	 * @param extendCell
	 * @return page name
	 */
	public String getPageName(String Regulator, String form, String version, String cellName, String extendCell)
	{
		String SQL;
		String refTable = "";
		if (ConnectDBType.equalsIgnoreCase("ar"))
		{
			String ID_Start = getRegulatorIDRangeStart(Regulator);
			String ID_End = getRegulatorIDRangEnd(Regulator);
			if (extendCell == null)
				refTable = "CFG_RPT_Ref";
			else
				refTable = "CFG_RPT_GridRef";
			SQL = "select \"PageName\" from \"CFG_RPT_List\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + form + "' AND \"Version\"=" + version
					+ " and \"ID\" between " + ID_Start + " and " + ID_End + ") " + "and \"TabName\" in (select \"TabName\" from \"" + refTable + "\" "
					+ "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + form + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and "
					+ ID_End + ") " + "and \"Item\"='" + cellName + "') and \"ID\" between " + ID_Start + " and " + ID_End;
			return DBQuery.queryRecord(SQL);
		}
		else
		{
			String RegPrefix = getToolsetRegPrefix(Regulator);
			if (extendCell == null)
				refTable = RegPrefix + "Ref";
			else
				refTable = RegPrefix + "GridRef";
			SQL = "select \"PageName\" from \"" + RegPrefix + "List\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + form + "' "
					+ "and \"TabName\" in (select \"TabName\" from \"" + refTable + "\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + form + "' "
					+ "and \"Version\"='" + version + "') and \"Item\"='" + cellName + "'))";

			return DBQuery.queryRecord(SQL);

		}

	}

	public List<String> getPageNames(String Regulator, String form, String version, String cellName, String extendCell) throws Exception
	{
		String SQL, refTable;
		if (ConnectDBType.equalsIgnoreCase("ar"))
		{
			String ID_Start = getRegulatorIDRangeStart(Regulator);
			String ID_End = getRegulatorIDRangEnd(Regulator);
			if (extendCell == null)
				refTable = "CFG_RPT_Ref";
			else
				refTable = "CFG_RPT_GridRef";
			SQL = "select \"PageName\" from \"CFG_RPT_List\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + form + "' AND \"Version\"=" + version
					+ " and \"ID\" between " + ID_Start + " and " + ID_End + ") " + "and \"TabName\" in (select \"TabName\" from \"" + refTable + "\" "
					+ "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + form + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and "
					+ ID_End + ") " + "and \"Item\"='" + cellName + "') and \"ID\" between " + ID_Start + " and " + ID_End;
			return DBQuery.queryRecords(SQL);
		}
		else
		{
			String RegPrefix = getToolsetRegPrefix(Regulator);
			SQL = "SELECT \"USERNAME\",\"SQLENGINE\",\"DB_HOST\",\"DB_INSTANCE\",\"DATABASENAME\" FROM \"ALIASES\" WHERE \"CONFIG_PREFIX\"='" + RegPrefix + "' AND \"ALIAS\"='STB Work'";
			String server = AR_Server;
			if (AR_DBType.equalsIgnoreCase("oracle"))
				server = AR_IP + "@" + AR_SID;

			List<String> dbInfo = DBAction.queryRecord(AR_DBType, server, AR_DBName, SQL).get(0);

			if (extendCell == null)
				refTable = RegPrefix + "Ref";
			else
				refTable = RegPrefix + "GridRef";
			SQL = "select \"PageName\" from \"" + RegPrefix + "List\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + form + "' "
					+ "and \"TabName\" in (select \"TabName\" from \"" + refTable + "\" " + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + form + "' "
					+ "and \"Version\"='" + version + "') and \"Item\"='" + cellName + "'))";
			List<String> pages = new ArrayList<>();
			String DB;
			if (dbInfo.get(1).equalsIgnoreCase("oracle"))
			{
				server = dbInfo.get(2) + "@" + dbInfo.get(4);
				DB = dbInfo.get(0);
			}
			else
			{
				server = dbInfo.get(2) + "@" + dbInfo.get(3);
				DB = dbInfo.get(4);
			}
			List<List<String>> rsts = DBAction.queryRecord(dbInfo.get(1), server, DB, SQL);
			for (List<String> r : rsts)
			{
				pages.add(r.get(0));
			}
			return pages;
		}

	}

	public String getRandomString(int length)
	{
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public boolean compareTwoValue(String value1, String value2)
	{
		boolean valueRst = true;
		try
		{
			if (Math.abs(Double.parseDouble(value1) - Double.parseDouble(value2)) > 0.5)
				valueRst = false;
		}
		catch (Exception e)
		{
			if (!value1.equalsIgnoreCase(value2))
				valueRst = false;
		}
		return valueRst;
	}

	public boolean compareTwoString(String value1, String value2)
	{
		if (!value1.equalsIgnoreCase(value2))
			return false;
		else
			return true;
	}

	public boolean compareCellValue(String value1, String value2, boolean isDisplayedValue)
	{
		boolean valueRst = true;
		if (isDisplayedValue)
		{
			if (!value1.equalsIgnoreCase(value2))
				valueRst = false;
		}
		else
			valueRst = compareTwoString(value1, value2);

		return valueRst;
	}

	public boolean getCellValueInForm(FormInstancePage formInstancePage, String Regulator, String formCode, String version, File testData) throws Exception
	{
		boolean testRst = false;
		boolean valueCorrect = true;
		int amt = ExcelUtil.getRowNums(testData, null);
		for (int index = 1; index <= amt; index++)
		{
			ArrayList<String> expectedValueValueList = ExcelUtil.getRowValueFromExcel(testData, null, index);
			String cellName = expectedValueValueList.get(0).trim();
			String rowID = expectedValueValueList.get(1).trim();
			String instance = (expectedValueValueList.get(2).trim().equals("")) ? null : expectedValueValueList.get(2).trim();
			String expectedValue = expectedValueValueList.get(3).trim();

			String extendCell = null;
			logger.info("Verify if " + cellName + "(instance=" + instance + " rowID=" + rowID + ")=" + expectedValue);
			if (rowID.length() > 0)
			{
				if (rowID.equals("0"))
				{
					String gridName = getExtendCellName(Regulator, formCode, version, cellName);
					extendCell = gridName + cellName;
				}
				else
				{
					rowID = String.valueOf(Integer.parseInt(rowID) + 48);
					String gridName = getExtendCellName(Regulator, formCode, version, cellName);
					extendCell = gridName + rowID + cellName;
				}
			}

			boolean findCell = true;
			String accValue = formInstancePage.getCellText(Regulator, formCode, version, instance, cellName, extendCell);

			if (accValue != null)
				ExcelUtil.writeToExcel(testData, index, 4, accValue);
			else
			{
				ExcelUtil.writeToExcel(testData, index, 4, "Cannot find cell");
				ExcelUtil.writeToExcel(testData, index, 5, "Fail");
				findCell = false;
				valueCorrect = false;
			}

			if (findCell)
			{
				if (!compareCellValue(accValue, expectedValue, true))
				{
					logger.error("Expected value(" + expectedValue + ") is not equal acctuall value(" + accValue + ")");
					valueCorrect = false;
					ExcelUtil.writeToExcel(testData, index, 5, "Fail");
				}
				else
				{
					ExcelUtil.writeToExcel(testData, index, 5, "Pass");
				}
			}
			logger.info(cellName + "(instance=" + instance + " rowID=" + rowID + ") expected value=" + expectedValue + " ,acctual value=" + accValue);
		}
		if (valueCorrect)
			testRst = true;
		return testRst;
	}

	public String getJobStatus() throws Exception
	{
		String SQL = "SELECT MAX(\"JOBINSTANCEID\") FROM \"JOB_INSTANCE\"";
		int jobId = Integer.parseInt(DBQuery.queryRecord(SQL));
		logger.info("Job instance id is: " + jobId);
		SQL = "SELECT \"JOBEXECUTIONID\" FROM \"JOB_EXECUTION\" where \"JOBINSTANCEID\"=" + jobId;
		String JOBEXECUTIONID = DBQuery.queryRecord(SQL);
		SQL = "SELECT \"STATUS\" FROM \"SVC_SERVICE_REQUEST_STATUS\" where \"JOB_EXECUTION_ID\"=" + JOBEXECUTIONID;
		String jobStatus = null;
		boolean flag = true;
		while (flag)
		{
			jobStatus = DBQuery.queryRecord(SQL);
			if (jobStatus != null && !jobStatus.equals("IN PROGRESS"))
				flag = false;
		}
		return jobStatus;
	}

	public boolean isJobSuccessed() throws Exception
	{
		String status = getJobStatus();
		boolean isSuccess = false;
		if (status.equals("SUCCESS"))
		{
			isSuccess = true;
			Thread.sleep(5000);
		}

		return isSuccess;
	}

	/**
	 * Assert list1 contains the value of list2
	 */
	public boolean checkListContainsAnotherList(List<String> listLong, List<String> listShort)
	{
		for (String aListShort : listShort)
		{
			if (!listLong.contains(aListShort))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * refresh page
	 *
	 * @throws Exception
	 */
	public void refreshPage() throws Exception
	{
		getWebDriverWrapper().navigate().refresh();
		Thread.sleep(3000);
	}

	public String addQuotesInPath(String path) throws Exception
	{
		String pathWithQuotes = "";
		if (path.contains(" "))
		{
			path = path.replace("\\", "~");
			for (String part : path.split("~"))
			{
				if (part.contains(" "))
				{
					part = "\"" + part + "\"";
				}
				pathWithQuotes = pathWithQuotes + part + "\\";
			}
			pathWithQuotes = pathWithQuotes.substring(0, pathWithQuotes.length() - 1);
		}
		else
			pathWithQuotes = path;
		return pathWithQuotes;
	}

	public class Module
	{
		public AdjustLogPage adjustLogPage;
		public AdminPage adminPage;
		public AllocationPage allocationPage;
		public CalendarPage calendarPage;
		public ChangePasswordPage changePasswordPage;
		public EditionManagePage editionManagePage;
		public EntityPage entityPage;
		public ErrorListPage errorListPage;
		public ExportToFilePage exportToFilePage;
		public ExportXBRLPage exportXBRLPage;
		public FormInstanceCreatePage formInstanceCreatePage;
		public FormInstancePage formInstancePage;
		public FormInstanceRetrievePage formInstanceRetrievePage;
		public FormSchedulePage formSchedulePage;
		public HomePage homePage;
		public ImportConfirmPage importConfirmPage;
		public ImportFileInReturnPage importFileInReturnPage;
		public ListImportFilePage listImportFilePage;
		public ListPage listPage;
		public NonWorkingDayListPage nonWorkingDayListPage;
		public PreferencePage preferencePage;
		public PrivilegeGroupPage privilegeGroupPage;
		public RetrieveResultPage retrieveResultPage;
		public SchedulePage schedulePage;
		public JobManagerPage jobManagerPage;
		public JobDetailsPage jobDetailsPage;
		public UserGroupPage userGroupPage;
		public UsersPage usersPage;
		public ValidationPage validationPage;
		public ReturnSourcePage returnSourcePage;
		public PhysicalLocationPage physicalLocationPage;
		public MessageCenter messageCenter;
		public FormVariablePage formVariablePage;
		public DWIntegrationPage dWIntegrationPage;

		public Module(ITestBase testCase)
		{
			adjustLogPage = new AdjustLogPage(getWebDriverWrapper());
			adminPage = new AdminPage(getWebDriverWrapper());
			allocationPage = new AllocationPage(getWebDriverWrapper());
			calendarPage = new CalendarPage(getWebDriverWrapper());
			changePasswordPage = new ChangePasswordPage(getWebDriverWrapper());
			editionManagePage = new EditionManagePage(getWebDriverWrapper());
			entityPage = new EntityPage(getWebDriverWrapper());
			errorListPage = new ErrorListPage(getWebDriverWrapper());
			exportToFilePage = new ExportToFilePage(getWebDriverWrapper());
			exportXBRLPage = new ExportXBRLPage(getWebDriverWrapper());
			formInstanceCreatePage = new FormInstanceCreatePage(getWebDriverWrapper());
			formInstancePage = new FormInstancePage(getWebDriverWrapper());
			formInstanceRetrievePage = new FormInstanceRetrievePage(getWebDriverWrapper());
			formSchedulePage = new FormSchedulePage(getWebDriverWrapper());
			homePage = new HomePage(getWebDriverWrapper());
			importConfirmPage = new ImportConfirmPage(getWebDriverWrapper());
			importFileInReturnPage = new ImportFileInReturnPage(getWebDriverWrapper());
			listImportFilePage = new ListImportFilePage(getWebDriverWrapper());
			listPage = new ListPage(getWebDriverWrapper());
			nonWorkingDayListPage = new NonWorkingDayListPage(getWebDriverWrapper());
			preferencePage = new PreferencePage(getWebDriverWrapper());
			privilegeGroupPage = new PrivilegeGroupPage(getWebDriverWrapper());
			retrieveResultPage = new RetrieveResultPage(getWebDriverWrapper());
			schedulePage = new SchedulePage(getWebDriverWrapper());
			jobManagerPage = new JobManagerPage(getWebDriverWrapper());
			jobDetailsPage = new JobDetailsPage(getWebDriverWrapper());
			userGroupPage = new UserGroupPage(getWebDriverWrapper());
			usersPage = new UsersPage(getWebDriverWrapper());
			validationPage = new ValidationPage(getWebDriverWrapper());
			returnSourcePage = new ReturnSourcePage(getWebDriverWrapper());
			physicalLocationPage = new PhysicalLocationPage(getWebDriverWrapper());
			messageCenter = new MessageCenter(getWebDriverWrapper());
			formVariablePage = new FormVariablePage(getWebDriverWrapper());
			dWIntegrationPage = new DWIntegrationPage(getWebDriverWrapper());
		}

	}
}