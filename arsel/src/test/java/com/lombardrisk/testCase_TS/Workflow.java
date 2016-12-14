package com.lombardrisk.testCase_TS;

import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.HomePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 3/28/2016.
 */
public class Workflow extends TestTemplate
{
	@Test
	public void test5177() throws Exception
	{
		String caseID = "5177";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserNameA = getElementValueFromXML(testData_Workflow, nodeName, "UserNameA");
			String UserNameB = getElementValueFromXML(testData_Workflow, nodeName, "UserNameB");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, null, null);

			updateFormStatus(Group, formCode, version, ProcessDate, 0);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			updateFormStatus(Group, formCode, version, ProcessDate, 1);
			refreshPage();
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();

			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.rejectForm("Test reject");
			formInstancePage.closeFormInstance();
			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo_toolset(row);
			if (formDetailInfo.get(8).equalsIgnoreCase("Rejected"))
				testRst = true;
			else
			{
				logger.error("The form status should be: Rejected");
			}
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.unlockClick();
			formInstancePage.closeFormInstance();

			testRst = false;
			row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			formDetailInfo = listPage.getFormDetailInfo_toolset(row);
			if (formDetailInfo.get(4).equalsIgnoreCase("UNLOCK") && formDetailInfo.get(7).equalsIgnoreCase("UNKNOWN"))
				testRst = true;
			else
			{
				logger.error("The form status should be: UNLOCK,UNKNOWN");
			}

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test5178() throws Exception
	{
		String caseID = "5178";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserNameA = getElementValueFromXML(testData_Workflow, nodeName, "UserName");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, null, null);

			updateFormStatus(Group, formCode, version, ProcessDate, 0);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			updateFormStatus(Group, formCode, version, ProcessDate, 1);
			refreshPage();
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();

			String status = formInstancePage.getApproveStatus();
			if (status.equalsIgnoreCase("READY FOR ATTESTATION"))
				testRst = true;
			else
			{
				logger.error("The form status should be: READY FOR ATTESTATION");
			}
			formInstancePage.unlockClick();
			status = formInstancePage.getApproveStatus();
			testRst = false;
			if (status.equalsIgnoreCase("Not Attested"))
				testRst = true;
			else
			{
				logger.error("The form status should be: Not Attested");
			}

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test5179() throws Exception
	{
		String caseID = "5179";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserNameA = getElementValueFromXML(testData_Workflow, nodeName, "UserNameA");
			String UserNameB = getElementValueFromXML(testData_Workflow, nodeName, "UserNameB");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, null, null);

			updateFormStatus(Group, formCode, version, ProcessDate, 0);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			updateFormStatus(Group, formCode, version, ProcessDate, 1);
			refreshPage();
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();

			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.approveForm("Test approve");
			formInstancePage.unlockClick();
			String status = formInstancePage.getApproveStatus();
			if (status.equalsIgnoreCase("Not Attested"))
				testRst = true;
			else
			{
				logger.error("The form status should be: Not Attested ");
			}

			// formInstancePage.editCellValue(CellName, "10");
			formInstancePage.closeFormInstance();

			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo_toolset(row);
			testRst = false;
			if (formDetailInfo.get(5).equalsIgnoreCase("unknown"))
				testRst = true;
			else
			{
				logger.error("The form validation status should be: unknown");
			}

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test5205() throws Exception
	{
		String caseID = "5205";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserNameA = getElementValueFromXML(testData_Workflow, nodeName, "UserNameA");
			String UserNameB = getElementValueFromXML(testData_Workflow, nodeName, "UserNameB");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, null, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.validationNowClick();
			formInstancePage.lockClick();
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();

			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.rejectForm("Test reject");
			formInstancePage.closeFormInstance();

			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.unlockClick();
			String status = formInstancePage.getApproveStatus();
			if (status.equalsIgnoreCase("Not Attested"))
				testRst = true;
			else
			{
				logger.error("The form status should be: Not Attested ");
			}

			// formInstancePage.editCellValue(CellName, "10");
			formInstancePage.closeFormInstance();

			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo_toolset(row);
			testRst = false;
			if (formDetailInfo.get(5).equalsIgnoreCase("unknown"))
				testRst = true;
			else
			{
				logger.error("The form validation status should be: unknown");
			}

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test5192() throws Exception
	{
		String caseID = "5192";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserNameA = getElementValueFromXML(testData_Workflow, nodeName, "UserNameA");
			String UserNameB = getElementValueFromXML(testData_Workflow, nodeName, "UserNameB");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, null, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.validationNowClick();
			formInstancePage.lockClick();
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();

			String SQL = "SELECT \"ReviewStatus\" FROM \"ECRStat\" WHERE \"ReturnId\" in(select \"ReturnId\" from \"ECRRets\" where \"Return\"='" + formCode + "' and \"Version\"=" + version
					+ ")  and \"Process_Date\"='31-MAY-16' order by \"EditTime\" desc";
			String reviewStatus = DBQuery.queryRecords(SQL).get(0);
			if (reviewStatus.equalsIgnoreCase("1"))
				testRst = true;
			else
			{
				logger.error("The review status should be: 1 ");
			}

			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.rejectForm("Test reject");
			formInstancePage.closeFormInstance();
			reviewStatus = DBQuery.queryRecords(SQL).get(0);
			testRst = false;
			if (reviewStatus.equalsIgnoreCase("3"))
				testRst = true;
			else
			{
				logger.error("The review status should be: 3 ");
			}

			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.unlockClick();
			reviewStatus = DBQuery.queryRecords(SQL).get(0);
			testRst = false;
			if (reviewStatus.equalsIgnoreCase("0"))
				testRst = true;
			else
			{
				logger.error("The review status should be: 0 ");
			}

			formInstancePage.validationNowClick();
			formInstancePage.lockClick();
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();
			reviewStatus = DBQuery.queryRecords(SQL).get(0);
			testRst = false;
			if (reviewStatus.equalsIgnoreCase("1"))
				testRst = true;
			else
			{
				logger.error("The review status should be: 1 ");
			}

			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.approveForm("Test approve");
			reviewStatus = DBQuery.queryRecords(SQL).get(0);
			testRst = false;
			if (reviewStatus.equalsIgnoreCase("2"))
				testRst = true;
			else
			{
				logger.error("The review status should be: 2 ");
			}
		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test5196() throws Exception
	{
		String caseID = "5196";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserName = getElementValueFromXML(testData_Workflow, nodeName, "UserName");

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, null, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			if (!formInstancePage.isReadyForApproveDisplayed())
				testRst = true;
			else
			{
				logger.error("The ReadyForApprove option should be invisible");
			}
		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test5204() throws Exception
	{
		String caseID = "5204";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserNameA = getElementValueFromXML(testData_Workflow, nodeName, "UserNameA");
			String UserNameB = getElementValueFromXML(testData_Workflow, nodeName, "UserNameB");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, null, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.closeFormInstance();

			homePage = listPage.logout();
			homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			if (!formInstancePage.isWorkflowDisplayed())
				testRst = true;
			else
			{
				logger.error("The form validation status should be: unknown");
			}
		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test5276() throws Exception
	{
		String caseID = "5276";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserName = getElementValueFromXML(testData_Workflow, nodeName, "UserName");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, null, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			String SQL = "SELECT \"STATEGUID\" FROM \"ECRStat\" WHERE \"ReturnId\" in(select \"ReturnId\" from \"ECRRets\" where \"Return\"='" + formCode + "' and \"Version\"=" + version
					+ ")  and \"Process_Date\"='31-MAY-16' order by \"EditTime\" desc";
			String STATEGUID = DBQuery.queryRecords(SQL).get(0);
			SQL = "update  \"ECRStat\" set \"Status\"='LIt' WHERE  \"STATEGUID\"='" + STATEGUID + "'";
			DBQuery.update(SQL);
			formInstancePage.closeFormInstance();

			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			String error = formInstancePage.getReadyForApproveMessage();

			if (error.equalsIgnoreCase("Cannot submit attestation due to validation failure."))
				testRst = true;
			else
			{
				logger.error("Error message should be:Cannot submit attestation due to validation failure.");
			}
		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test6478() throws Exception
	{
		String caseID = "6478";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Workflow, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Workflow, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Workflow, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Workflow, nodeName, "ProcessDate");
			String UserNameA = getElementValueFromXML(testData_Workflow, nodeName, "UserNameA");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.closeFormInstance();
			boolean s1 = false;
			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo_toolset(row);
			if (formDetailInfo.get(7).equalsIgnoreCase("UNKNOWN"))
				s1 = true;
			else
				logger.error("The form status should be: UNKNOWN");

			String SQL = "SELECT \"STATEGUID\" FROM \"ECRStat\" WHERE \"ReturnId\" in(select \"ReturnId\" from \"ECRRets\" where \"Return\"='" + formCode + "' and \"Version\"=" + version
					+ ")  and \"Process_Date\"='31-MAY-16' order by \"EditTime\" desc";
			String STATEGUID = DBQuery.queryRecords(SQL).get(0);
			SQL = "update  \"ECRStat\" set \"Status\"='LXt' WHERE  \"STATEGUID\"='" + STATEGUID + "'";
			DBQuery.update(SQL);

			boolean s2 = false;
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.clickReadyForApprove();
			listPage = formInstancePage.closeFormInstance();
			formDetailInfo = listPage.getFormDetailInfo_toolset(row);
			if (formDetailInfo.get(7).equalsIgnoreCase("Waiting"))
				s2 = true;
			else
				logger.error("The form status should be: WaitingApproval");

			HomePage homePage = listPage.logout();
			homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.approveForm("test approve");
			formInstancePage.closeFormInstance();
			boolean s3 = false;
			formDetailInfo = listPage.getFormDetailInfo_toolset(row);
			if (formDetailInfo.get(7).equalsIgnoreCase("Attested"))
				s3 = true;
			else
				logger.error("The form status should be: Attested");

			homePage = listPage.logout();
			homePage.logon();

			boolean s4 = false;
			listPage.deleteFormInstance(Form, ProcessDate);
			formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			SQL = "SELECT \"STATEGUID\" FROM \"ECRStat\" WHERE \"ReturnId\" in(select \"ReturnId\" from \"ECRRets\" where \"Return\"='" + formCode + "' and \"Version\"=" + version
					+ ")  and \"Process_Date\"='31-MAY-16' order by \"EditTime\" desc";
			STATEGUID = DBQuery.queryRecords(SQL).get(0);
			SQL = "update  \"ECRStat\" set \"Status\"='LXt' WHERE  \"STATEGUID\"='" + STATEGUID + "'";
			DBQuery.update(SQL);

			refreshPage();
			formInstancePage.clickReadyForApprove();
			listPage = formInstancePage.closeFormInstance();

			homePage = listPage.logout();
			homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.rejectForm("Test reject");
			formInstancePage.closeFormInstance();
			formDetailInfo = listPage.getFormDetailInfo_toolset(row);
			if (formDetailInfo.get(7).equalsIgnoreCase("Rejected"))
				s4 = true;
			else
				logger.error("The form status should be: Rejected");

			if (s1 && s2 && s3 && s4)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	public void updateFormStatus(String Entity, String formCode, String formVersion, String ReferenceDate, int flag) throws Exception
	{
		String sql = "SELECT \"EntityId\" from \"ECRGrps\" Where \"Name\"='" + Entity + "'";
		String entityid = DBQuery.queryRecord(sql);

		sql = "SELECT \"ReturnId\" from \"ECRRets\" Where \"Return\"='" + formCode + "' and \"Version\"=" + formVersion;
		String returnid = DBQuery.queryRecord(sql);
		if (T_DBType.equalsIgnoreCase("oracle"))
		{
			String month, day, year;
			month = ReferenceDate.substring(3, 5);
			day = ReferenceDate.substring(0, 2);
			year = ReferenceDate.substring(8, 10);

			if (month.startsWith("0"))
				month = month.substring(1);
			if (day.startsWith("0"))
				day = day.substring(1);
			String processDate = day + "-" + month + "-" + year;
			switch (month)
			{
				case "01":
					month = "JAN";
					break;
				case "02":
					month = "FEB";
					break;
				case "03":
					month = "MAR";
					break;
				case "04":
					month = "APR";
					break;
				case "05":
					month = "MAY";
					break;
				case "06":
					month = "JUN";
					break;
				case "07":
					month = "JUL";
					break;
				case "08":
					month = "AUG";
					break;
				case "09":
					month = "SEP";
					break;
				case "10":
					month = "OCT";
					break;
				case "11":
					month = "NOV";
					break;
				case "12":
					month = "DEC";
					break;
			}
			if (flag == 0)
				sql = "UPDATE \"ECRStat\" SET \"Status\"='UIt' WHERE \"ReturnId\"=" + returnid + " AND \"EntityId\"=" + entityid + " AND \"Process_Date\"='" + processDate + "'";
			else
				sql = "UPDATE \"ECRStat\" SET \"Status\"='LXt' WHERE \"ReturnId\"=" + returnid + " AND \"EntityId\"=" + entityid + " AND \"Process_Date\"='" + processDate + "'";
		}
		else
		{
			String month, day, year;
			month = ReferenceDate.substring(3, 5);
			day = ReferenceDate.substring(0, 2);
			year = ReferenceDate.substring(6, 10);

			if (month.length() == 1)
				month = month + "0";
			if (day.length() == 0)
				day = day + "0";
			String processDate = year + "-" + month + "-" + day;
			if (flag == 0)
				sql = "UPDATE \"ECRStat\" SET \"Status\"='UIt' WHERE \"ReturnId\"=" + returnid + " AND \"EntityId\"=" + entityid + " AND \"Process_Date\"='" + processDate + "'";
			else
				sql = "UPDATE \"ECRStat\" SET \"Status\"='LXt' WHERE \"ReturnId\"=" + returnid + " AND \"EntityId\"=" + entityid + " AND \"Process_Date\"='" + processDate + "'";
		}

		DBQuery.update(sql);

	}
}
