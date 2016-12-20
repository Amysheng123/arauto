package com.lombardrisk.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.yiwan.webcore.test.ITestBase;
import org.yiwan.webcore.util.Helper;
import org.yiwan.webcore.util.PropHelper;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.fileService.ExcelUtil;

/**
 * Create by Leo Tu on 4/28/2016.
 */

public class Browser extends TestTemplate
{
	protected Module m;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
	String curDate = sdf.format(new Date());
	WebDriver driver = null;

	protected void startBrowser() throws Exception
	{
		setFeatureId(this.getClass().getSimpleName().toLowerCase());
		setScenarioId(getFeatureId());// if a class indicates a test case, the
										// feature id would be scenario id
		setUpTest();

		logger.info("setup before class");
		getWebDriverWrapper().navigate().to(getTestEnvironment().getApplicationServer(0).getUrl());
		report(Helper.getTestReportStyle(getTestEnvironment().getApplicationServer(0).getUrl(), "open test server url"));
		m = new Module(this);
		m.homePage.logon();

		File testRstFolder = new File("target/TestResult");
		if (!testRstFolder.exists())
		{
			testRstFolder.mkdir();
		}

		if (testDataFolderName.equalsIgnoreCase("ar"))
			testDataFolderName = "data_ar";
		else if (testDataFolderName.equalsIgnoreCase("toolset"))
			testDataFolderName = "data_toolset";
		else if (testDataFolderName.equalsIgnoreCase("toolsetNull"))
			testDataFolderName = "data_toolset_allownull";

	}

	protected void closeBrowser() throws Exception
	{
		logger.info("teardown after class");
		tearDownTest();
	}

	public void reStartBrowser() throws Exception
	{
		try
		{
			tearDownTest();
		}
		catch (Exception e)
		{
		}
		setUpTest();
		logger.info("setup before class");
		getWebDriverWrapper().navigate().to(getTestEnvironment().getApplicationServer(0).getUrl());
		m = new Module(this);
		m.homePage.logon();
	}

	public ListPage loginAsOtherUser(String userName, String password) throws Exception
	{
		ListPage listPage = m.listPage;
		HomePage homePage = listPage.logout();
		homePage.loginAs(userName, password);
		return m.listPage;
	}

	public void writeTestResultToFile(File TestResultFile, int rowID, int colID, String caseID, boolean testResult, String module) throws Exception
	{
		String status = null;

		if (testResult)
			status = "Pass";
		else
			status = "Fail";

		if (TestResultFile != null)
		{
			ExcelUtil.writeToExcel(testRstFile, rowID, colID, status);
		}

		if (caseID.length() > 3)
		{
			String source = "data_toolset/TestStatus.xlsx";
			File TestStatusFile = new File("target/TestResult/" + curDate + "/TestStatus.xlsx");
			if (!TestStatusFile.exists())
			{
				FileUtils.copyFile(new File(source), TestStatusFile);
			}
			if (caseID.contains(","))
			{
				for (String id : caseID.split(","))
				{
					if (caseID.contains("."))
						caseID = caseID.replace(".", "#").split("#")[0];
					ExcelUtil.WriteTestRst(TestStatusFile, id, status, module);
				}
			}
			else
			{
				if (caseID.contains("."))
					caseID = caseID.replace(".", "#").split("#")[0];
				ExcelUtil.WriteTestRst(TestStatusFile, caseID, status, module);
			}
		}
	}

	public List<String> createFolderAndCopyFile(String Function)
	{
		logger.info("Begin setup test folder and test data");
		List<String> Files = new ArrayList<String>();
		List<String> FuncList = Arrays.asList("MultipleReporter");
		if (testDataFolderName.equalsIgnoreCase("ar"))
			testDataFolderName = "data_ar";
		else if (testDataFolderName.equalsIgnoreCase("toolset"))
			testDataFolderName = "data_toolset";
		else if (testDataFolderName.equalsIgnoreCase("toolsetNull"))
			testDataFolderName = "data_toolset_allownull";

		if (FuncList.contains(Function))
		{
			String TD_TestFile = System.getProperty("user.dir") + "/" + testDataFolderName + "/" + Function + "/" + Function + ".xls";
			String TD_checkDataFolder = System.getProperty("user.dir") + "/" + testDataFolderName + "/" + Function + "/" + "CheckCellValue/";

			// add test data folder
			Files.add(TD_checkDataFolder);

			String TR_CurrenrDayFolder = System.getProperty("user.dir") + "/" + "target/TestResult/" + curDate + "/";
			String TR_FunctionFolder = TR_CurrenrDayFolder + Function;
			String TR_TestFile = TR_CurrenrDayFolder + Function + "/" + Function + ".xls";
			String TR_checkDataFolder = null;

			if (Function.equals("CheckRule"))
			{
				TR_checkDataFolder = TR_CurrenrDayFolder + Function + "/" + "TestData/";
			}
			else
			{
				TR_checkDataFolder = TR_CurrenrDayFolder + Function + "/" + "CheckCellValue/";
			}

			// add test result check data folder
			Files.add(TR_checkDataFolder);

			logger.info("Begin create folder");
			File createFolder = new File(TR_CurrenrDayFolder);
			if (!createFolder.exists())
			{
				createFolder.mkdir();
			}
			createFolder = new File(TR_FunctionFolder);
			if (!createFolder.exists())
			{
				createFolder.mkdir();
			}

			createFolder = new File(TR_checkDataFolder);
			if (!createFolder.exists())
			{
				createFolder.mkdir();
			}

			testRstFile = new File(TR_TestFile);
			if (!testRstFile.exists())
				try
				{
					FileUtils.copyFile(new File(TD_TestFile), testRstFile);
				}
				catch (Exception e)
				{
					// e.printStackTrace();
				}

			// add test result file
			Files.add(TR_TestFile);
		}
		else
		{
		}

		return Files;

	}

	@AfterSuite
	public void SyncQC() throws Exception
	{
		File from = new File(System.getProperty("user.dir") + "/" + "target/TestResult");
		File to = new File("C:/ARAutoTestResult");
		FileUtils.copyDirectory(from, to);

		if (PropHelper.getProperty("qc.sync").trim().equalsIgnoreCase("y"))
		{
			String TestStatusFile = System.getProperty("user.dir") + "/" + "target/TestResult/" + curDate + "/TestStatus.xlsx";
			logger.info("Reading data from " + TestStatusFile);
			UpdateCaseInQC.setStatus(TestStatusFile);
		}

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
		public JobDetailsPage showDWImportLogPage;
		public UserGroupPage userGroupPage;
		public UsersPage usersPage;
		public ValidationPage validationPage;

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
			showDWImportLogPage = new JobDetailsPage(getWebDriverWrapper());
			userGroupPage = new UserGroupPage(getWebDriverWrapper());
			usersPage = new UsersPage(getWebDriverWrapper());
			validationPage = new ValidationPage(getWebDriverWrapper());
		}

	}

}
