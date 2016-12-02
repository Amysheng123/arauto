package com.lombardrisk.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.yiwan.webcore.test.ITestBase;
import org.yiwan.webcore.test.TestBase;
import org.yiwan.webcore.util.Helper;
import org.yiwan.webcore.util.PropHelper;

import com.lombardrisk.pages.AdjustLogPage;
import com.lombardrisk.pages.AdminPage;
import com.lombardrisk.pages.AllocationPage;
import com.lombardrisk.pages.CalendarPage;
import com.lombardrisk.pages.ChangePasswordPage;
import com.lombardrisk.pages.EditionManagePage;
import com.lombardrisk.pages.EntityPage;
import com.lombardrisk.pages.ErrorListPage;
import com.lombardrisk.pages.ExportToFilePage;
import com.lombardrisk.pages.FormInstanceCreatePage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.FormInstanceRetrievePage;
import com.lombardrisk.pages.FormSchedulePage;
import com.lombardrisk.pages.HomePage;
import com.lombardrisk.pages.ImportConfirmPage;
import com.lombardrisk.pages.ImportFileInReturnPage;
import com.lombardrisk.pages.ListImportFilePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.pages.NonWorkingDayListPage;
import com.lombardrisk.pages.PreferencePage;
import com.lombardrisk.pages.PrivilegeGroupPage;
import com.lombardrisk.pages.RetrieveResultPage;
import com.lombardrisk.pages.SchedulePage;
import com.lombardrisk.pages.ShowDWImportLogPage;
import com.lombardrisk.pages.UserGroupPage;
import com.lombardrisk.pages.UsersPage;
import com.lombardrisk.pages.ValidationPage;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.XMLUtil;

/**
 * @author Kenny Wang
 */
public class TestTemplate extends TestBase {

    protected static final String DBType = PropHelper.getProperty("db.type").trim();
    protected static final String connectedDB = PropHelper.getProperty("db.connectedDB").trim();
    protected File testRstFile = null;
    protected Module m;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
    String curDate = sdf.format(new Date());
    WebDriver driver = null;

    public static void waitForMilliseconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass(dependsOnMethods = {"beforeClass"})
    protected void setUpClass() throws Exception {   	
    	setFeatureId(this.getClass().getSimpleName().toLowerCase());
        setScenarioId(getFeatureId());// if a class indicates a test case, the feature id would be scenario id              
        setUpTest();
        
        logger.info("setup before class");
        getWebDriverWrapper().navigate().to(getTestEnvironment().getApplicationServer(0).getUrl());
        report(Helper.getTestReportStyle(getTestEnvironment().getApplicationServer(0).getUrl(), "open test server url"));
        m = new Module(this);
        m.homePage.logon();

        
        File testRstFolder = new File("target\\TestResult");
        if (!testRstFolder.exists()) {
            testRstFolder.mkdir();
        }
        

    }

    @AfterClass
    protected void tearDownClass() throws Exception {
        logger.info("teardown after class");
        tearDownTest();
    }

    @BeforeMethod
    protected void beforeMethod(ITestContext testContext, Method method) throws Exception {
        if (m.homePage.isLogonPage())
            m.homePage.logon();
    }

    @AfterMethod
    protected void afterMethod(ITestContext testContext, Method method, ITestResult testResult) throws Exception {
    	//logger the throwable in such test method
    	if (testResult.getThrowable() != null) {
            logger.error(method.getName(), testResult.getThrowable());
        }
        m.formInstancePage.closeFormInstance();

    }

    @AfterSuite
    public void SyncQC() throws Exception {
    	File from=new File(System.getProperty("user.dir") + "\\" + "target\\TestResult");
    	File to=new File("C:\\ARAutoTestResult");
    	FileUtils.copyDirectory(from, to);
    	
        if (PropHelper.getProperty("qc.sync").trim().equalsIgnoreCase("y")){
        	String TestStatusFile = System.getProperty("user.dir") + "\\" + "target\\TestResult\\" + curDate + "\\TestStatus.xlsx";
            logger.info("Reading data from " + TestStatusFile);
            UpdateCaseInQC.setStatus(TestStatusFile);
        }
        
    }

    public List<String> createFolderAndCopyFile(String Function) {
        logger.info("Begin setup test folder and test data");
        List<String> Files = new ArrayList<String>();
        List<String> FuncList = Arrays.asList("CheckRule", "CreateForm", "ExportForm", "ImportForm", "RetrieveForm", "ImportExport", "Precision", "ComputeForm");

        if (FuncList.contains(Function)) {
            String TD_TestFile = System.getProperty("user.dir") + "\\" + "data\\" + Function + "\\" + Function + ".xls";
            String TD_checkDataFolder;
            if (Function.equals("CheckRule")) {
                TD_checkDataFolder = System.getProperty("user.dir") + "\\" + "data\\" + Function + "\\" + "TestData\\";
            } else {
                TD_checkDataFolder = System.getProperty("user.dir") + "\\" + "data\\" + Function + "\\" + "CheckCellValue\\";
            }

            // add test data folder
            Files.add(TD_checkDataFolder);


            String TR_CurrenrDayFolder = System.getProperty("user.dir") + "\\" + "target\\TestResult\\" + curDate + "\\";
            String TR_FunctionFolder = TR_CurrenrDayFolder + Function;
            String TR_TestFile = TR_CurrenrDayFolder + Function + "\\" + Function + ".xls";
            String TR_checkDataFolder = null;


            if (Function.equals("CheckRule")) {
                TR_checkDataFolder = TR_CurrenrDayFolder + Function + "\\" + "TestData\\";
            } else {
                TR_checkDataFolder = TR_CurrenrDayFolder + Function + "\\" + "CheckCellValue\\";
            }

            // add test result check data  folder
            Files.add(TR_checkDataFolder);

            logger.info("Begin create folder");
            File createFolder = new File(TR_CurrenrDayFolder);
            if (!createFolder.exists()) {
                createFolder.mkdir();
            }
            createFolder = new File(TR_FunctionFolder);
            if (!createFolder.exists()) {
                createFolder.mkdir();
            }

            createFolder = new File(TR_checkDataFolder);
            if (!createFolder.exists()) {
                createFolder.mkdir();
            }


            testRstFile = new File(TR_TestFile);
            if (!testRstFile.exists())
                try {
                    FileUtils.copyFile(new File(TD_TestFile), testRstFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            //add test result file
            Files.add(TR_TestFile);
        } else {
        }

        return Files;

    }

    public void closeFormInstance() throws Exception {
        try {
            FormInstancePage formInstancePage = m.formInstancePage;
            formInstancePage.closeFormInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToolsetRegPrefix(String Regulator) {
    	/*
        String SQL = "SELECT \"TOOLSET_REG_PREFIX\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
        return DBQuery.queryRecordSpecDB(PropHelper.getProperty("db.apDBName").trim(), SQL);*/
    	if (Regulator.equalsIgnoreCase("European Common Reporting"))
    		return "ECR";
    	else
    		return "";
    }

    public String getRegulatorIDRangeStart(String Regulator) {
        String SQL = "SELECT \"ID_RANGE_START\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
        return  DBQuery.queryRecord(SQL);
    }

    public String getRegulatorIDRangEnd(String Regulator) {
        String SQL = "SELECT \"ID_RANGE_END\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "' AND \"STATUS\"='A'  ";
        return DBQuery.queryRecord(SQL);
    }

    public String getExtendCellName(String Regulator, String formCode, String version, String cellName) {
        if (connectedDB.equalsIgnoreCase("ar")) {
            String ID_Start = getRegulatorIDRangeStart(Regulator);
            String ID_End = getRegulatorIDRangEnd(Regulator);
            String SQL = "select \"GridName\" from \"CFG_RPT_GridRef\" "
                    + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") "
                    + "and \"Item\"='" + cellName + "'";
            return DBQuery.queryRecord(SQL);
        } else {
            String RegPrefix = getToolsetRegPrefix(Regulator);
            String SQL = "select \"GridName\" from \"" + RegPrefix + "GridRef\" "
                    + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + " ) "
                    + "and \"Item\"='" + cellName + "'";
            return DBQuery.queryRecord(SQL);
        }
    }

    public String getDestFldFormSumRule(String Regulator, String formCode, String version, int ruleID) {
        if (connectedDB.equalsIgnoreCase("ar")) {
            String ID_Start = getRegulatorIDRangeStart(Regulator);
            String ID_End = getRegulatorIDRangEnd(Regulator);

            String SQL = "SELECT \"DestFld\"  FROM \"CFG_RPT_Sums\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ")  and \"ExpOrder\"=" + ruleID + " and \"ID\" between " + ID_Start + " and " + ID_End;
            return DBQuery.queryRecord(SQL);
        } else {
            String RegPrefix = getToolsetRegPrefix(Regulator);
            String SQL = "SELECT \"DestFld\"  FROM \"" + RegPrefix + "Sums\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \""+RegPrefix+"Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + ")  and \"ExpOrder\"=" + ruleID;
            return DBQuery.queryRecord(SQL);
        }
    }

    public String getValidationExpression(String Regulator, String formCode, String version, String ruleType, int ruleID) {
        String SQL = null;
        if (connectedDB.equalsIgnoreCase("ar")) {
            String ID_Start = getRegulatorIDRangeStart(Regulator);
            String ID_End = getRegulatorIDRangEnd(Regulator);

            if (ruleType.equalsIgnoreCase("val"))
                SQL = "SELECT \"Expression\" FROM \"CFG_RPT_Vals\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ")  and \"ExpOrder\"=" + ruleID + " and \"ID\" between " + ID_Start + " and " + ID_End;
            else if (ruleType.equalsIgnoreCase("xval"))
                SQL = "SELECT \"Expression\" FROM \"CFG_RPT_XVals\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ")  and \"ExpOrder\"=" + ruleID + " and \"ID\" between " + ID_Start + " and " + ID_End;
            return DBQuery.queryRecord(SQL);
        } else {
            String RegPrefix = getToolsetRegPrefix(Regulator);
            if (ruleType.equalsIgnoreCase("val"))
                SQL = "SELECT \"Expression\" FROM \"" + RegPrefix + "Vals\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + ")  and \"ExpOrder\"=" + ruleID;
            else if (ruleType.equalsIgnoreCase("xval"))
                SQL = "SELECT \"Expression\" FROM \"" + RegPrefix + "XVals\" where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + formCode + "' AND \"Version\"=" + version + ")  and \"ExpOrder\"=" + ruleID;
            return DBQuery.queryRecord(SQL);
        }
    }


    public void logout() throws Exception {
        closeFormInstance();
        try {
            ListPage listPage = new ListPage(getWebDriverWrapper());
            listPage.logout();
        } catch (Exception e) {

        }
    }


    public void writeTestResultToFile(String caseID, boolean testResult,String module) throws IOException {
        String status = null;
        if (testResult)
            status = "Pass";
        else
            status = "Fail";

        if (caseID.length() > 3) {
            if (caseID.contains("."))
                caseID = caseID.split(".")[0];
            String source = "data\\TestStatus.xlsx";
            File TestStatusFile = new File("target\\TestResult\\" + curDate + "\\TestStatus.xlsx");
            if (!TestStatusFile.exists()) {
                FileUtils.copyFile(new File(source), TestStatusFile);
            }

            List<String> existRow = ExcelUtil.getLastCaseID(TestStatusFile, caseID);
            if (existRow.size() > 0) {
                if (existRow.get(1).equals(caseID)) {
                    if (!existRow.get(2).equals(testResult) && status.equals("Fail")) {
                        ExcelUtil.writeTestRstToFile(TestStatusFile, Integer.parseInt(existRow.get(0)), 1, testResult);
                    }
                } else {
                    ExcelUtil.WriteTestRst(TestStatusFile, caseID, status, module);
                }
            } else {
                ExcelUtil.WriteTestRst(TestStatusFile, caseID, status, module);
            }
        }
    }

    public void writeTestResultToFile(File TestResultFile, int rowID, int colID, String caseID, boolean testResult,String module) throws IOException {
        String status = null;

        if (testResult)
            status = "Pass";
        else
            status = "Fail";

        if (TestResultFile != null) {
            ExcelUtil.writeToExcel(testRstFile, rowID, colID, status);
        }


        if (caseID.length() > 3) {
            String source = "data\\TestStatus.xlsx";
            File TestStatusFile = new File("target\\TestResult\\" + curDate + "\\TestStatus.xlsx");
            if (!TestStatusFile.exists()) {
                FileUtils.copyFile(new File(source), TestStatusFile);
            }
            if (caseID.contains(",")) {
                for (String id : caseID.split(",")) {
                    if (caseID.contains("."))
                        caseID = caseID.replace(".", "#").split("#")[0];
                    ExcelUtil.WriteTestRst(TestStatusFile, id, status, module);
                }
            } else {
                if (caseID.contains("."))
                    caseID = caseID.replace(".", "#").split("#")[0];
                ExcelUtil.WriteTestRst(TestStatusFile, caseID, status, module);
            }
        }
        //Assert.assertTrue(testResult);
    }


    public void copyFailedFileToTestRst(String copyFrom) throws IOException {
        logger.info("Copy exported file to TestResult folder");
        File sourceFile = new File(copyFrom);
        String fileName = sourceFile.getName();
        File destFile = new File("target\\TestResult\\" + curDate + "\\ExportForm\\ExportedFile\\" + fileName);
        FileUtils.copyFile(sourceFile, destFile);
    }

    public String getElementValueFromXML(String xmlFile, String ChildNode, String element) {

        try {
            return XMLUtil.getElementValueFromXML(xmlFile, ChildNode, element);
        } catch (DocumentException e) {
            return "";
        }

    }


    public List<String> splitReturn(String returnName) {
        List<String> returnNV = new ArrayList<String>();
        String formCode = null;
        String formVersion = null;
        String Form = null;
        if (returnName.trim().contains(" ")) {
            formCode = returnName.split(" ")[0];
            formVersion = returnName.split(" ")[1].trim().toLowerCase().replace("v", "");
            Form = formCode + " v" + formVersion;
        } else if(returnName.trim().contains("_")){
            formCode = returnName.split("_")[0];
            formVersion = returnName.split("_")[1].trim().toLowerCase().replace("v", "");
            Form = formCode + " v" + formVersion;
        }
        else {
        	formCode=returnName;
        	formVersion="";
        	Form = formCode + " v" + formVersion;
		}

        returnNV.add(formCode);
        returnNV.add(formVersion);
        returnNV.add(Form);
        return returnNV;
    }

    public ListPage loginAsOtherUser(String userName, String password) throws Exception {
        ListPage listPage = m.listPage;
        HomePage homePage = listPage.logout();
        homePage.loginAs(userName, password);
        return m.listPage;
    }


    public class Module {
        public AdjustLogPage adjustLogPage;
        public AdminPage adminPage;
        public AllocationPage allocationPage;
        public CalendarPage calendarPage;
        public ChangePasswordPage changePasswordPage;
        public EditionManagePage editionManagePage;
        public EntityPage entityPage;
        public ErrorListPage errorListPage;
        public ExportToFilePage exportToFilePage;
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
        public ShowDWImportLogPage showDWImportLogPage;
        public UserGroupPage userGroupPage;
        public UsersPage usersPage;
        public ValidationPage validationPage;

        public Module(ITestBase testCase) {
            adjustLogPage = new AdjustLogPage(getWebDriverWrapper());
            adminPage = new AdminPage(getWebDriverWrapper());
            allocationPage = new AllocationPage(getWebDriverWrapper());
            calendarPage = new CalendarPage(getWebDriverWrapper());
            changePasswordPage = new ChangePasswordPage(getWebDriverWrapper());
            editionManagePage = new EditionManagePage(getWebDriverWrapper());
            entityPage = new EntityPage(getWebDriverWrapper());
            errorListPage = new ErrorListPage(getWebDriverWrapper());
            exportToFilePage = new ExportToFilePage(getWebDriverWrapper());
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
            showDWImportLogPage = new ShowDWImportLogPage(getWebDriverWrapper());
            userGroupPage = new UserGroupPage(getWebDriverWrapper());
            usersPage = new UsersPage(getWebDriverWrapper());
            validationPage = new ValidationPage(getWebDriverWrapper());
        }


    }
}