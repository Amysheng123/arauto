package com.lombardrisk.testCase;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.HomePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 3/28/2016.
 */
public class Workflow extends TestTemplate
{

	@Test
	public void test3945() throws Exception
	{
		String caseID = "3945";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserNameA = testdata.get(4);
			String ExpectedCountA = testdata.get(5);
			String UserNameB = testdata.get(6);
			String ExpectedCountB = testdata.get(7);
			String UserNameC = testdata.get(8);
			String ExpectedCountC = testdata.get(9);
			String UserNameD = testdata.get(10);
			String ExpectedCountD = testdata.get(11);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			String[] users =
			{ UserNameB, UserNameC, UserNameD };
			String[] exp =
			{ ExpectedCountB, ExpectedCountC, ExpectedCountD };

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.validationNowClick();
			formInstancePage.clickReadyForApprove();
			String actual = formInstancePage.getApproveCount();
			boolean c1 = false;
			if (actual.equalsIgnoreCase(ExpectedCountA))
				c1 = true;
			Assert.assertTrue(c1);
			listPage = formInstancePage.closeFormInstance();
			homePage = listPage.logout();
			boolean c2 = true;
			for (int i = 0; i < users.length; i++)
			{
				listPage = homePage.loginAs(users[i], "password");
				listPage.getProductListPage(Regulator, Group, null, null);
				formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
				formInstancePage.approveForm(null);
				actual = formInstancePage.getApproveCount();
				if (!actual.equalsIgnoreCase(exp[i]))
					c2 = false;
				Assert.assertTrue(c2);
				listPage = formInstancePage.closeFormInstance();
				homePage = listPage.logout();
			}

			if (c1 && c2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3960() throws Exception, InterruptedException
	{
		String caseID = "3960";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);
			String Status1 = testdata.get(5);
			String EditCell = testdata.get(6);
			String Status2 = testdata.get(7);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			listPage = formInstancePage.closeFormInstance();

			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.unlockClick();
			formInstancePage.closeFormInstance();

			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetail = listPage.getFormDetailInfo(row);
			String actualStatus = formDetail.get(6) + "#" + formDetail.get(7) + "#" + formDetail.get(8) + "#" + formDetail.get(11).replace("_", " ");
			boolean c1 = false;
			if (actualStatus.equalsIgnoreCase(Status1))
				c1 = true;

			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.editCellValue(EditCell, "10");
			listPage = formInstancePage.closeFormInstance();
			formDetail = listPage.getFormDetailInfo(row);
			actualStatus = formDetail.get(6) + "#" + formDetail.get(7) + "#" + formDetail.get(8) + "#" + formDetail.get(11).replace("_", " ");
			boolean c2 = false;
			if (actualStatus.equalsIgnoreCase(Status2))
				c2 = true;

			if (c1 && c2)
				testRst = true;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3961() throws Exception
	{
		String caseID = "3961";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);
			String Status1 = testdata.get(5);
			String EditCell = testdata.get(6);
			String Status2 = testdata.get(7);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.unlockClick();
			listPage = formInstancePage.closeFormInstance();
			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetail = listPage.getFormDetailInfo(row);
			String actualStatus = formDetail.get(6) + "#" + formDetail.get(7) + "#" + formDetail.get(8) + "#" + formDetail.get(11).replace("_", " ");
			boolean c1 = false;
			if (actualStatus.equalsIgnoreCase(Status1))
				c1 = true;

			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			String editValue = formInstancePage.getCellText(EditCell) + "1";
			formInstancePage.editCellValue(EditCell, editValue);
			listPage = formInstancePage.closeFormInstance();
			formDetail = listPage.getFormDetailInfo(row);
			actualStatus = formDetail.get(6) + "#" + formDetail.get(7) + "#" + formDetail.get(8) + "#" + formDetail.get(11).replace("_", " ");
			boolean c2 = false;
			if (actualStatus.equalsIgnoreCase(Status2))
				c2 = true;

			if (c1 && c2)
				testRst = true;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test6125() throws Exception
	{
		String caseID = "6125";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserNameA = testdata.get(4);
			// String UserNameB = testdata.get(5);
			String Status1 = testdata.get(6);
			// String EditCell = testdata.get(7);
			// String Status2 = testdata.get(8);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();

			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.unlockClick();
			listPage = formInstancePage.closeFormInstance();
			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetail = listPage.getFormDetailInfo(row);
			String actualStatus = formDetail.get(6) + "#" + formDetail.get(7) + "#" + formDetail.get(8) + "#" + formDetail.get(11).replace("_", " ");
			boolean c1 = false;
			if (actualStatus.equalsIgnoreCase(Status1))
				c1 = true;

			boolean c2 = false;
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			if (formInstancePage.isReadyApproveDisplayed())
				c2 = true;

			if (c1 && c2)
				testRst = true;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3966() throws Exception
	{
		String caseID = "3966";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserNameA = testdata.get(4);
			String UserNameB = testdata.get(5);
			String ApproveMsg = testdata.get(6);
			String RejectMsg = testdata.get(7);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, null, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.validationNowClick();
			formInstancePage.clickReadyForApprove();
			listPage = formInstancePage.closeFormInstance();
			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.approveForm("Test approve");
			String message = formInstancePage.getApproveFormMessage("Test approve");
			boolean c1 = false;
			if (message.equalsIgnoreCase(ApproveMsg))
				c1 = true;

			boolean c2 = false;
			message = formInstancePage.getRejectFormMessage("Test reject");
			if (message.equalsIgnoreCase(RejectMsg))
				c2 = true;

			if (c1 && c2)
				testRst = true;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3915() throws Exception
	{
		String caseID = "3915";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			if (formInstancePage.isLockedInReturn())
			{
				testRst = true;
			}
			else
			{
				logger.error("The form should be locked");
			}

			testRst = false;
			formInstancePage.unlockClick();
			formInstancePage.closeFormInstance();
			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			String status = listPage.getFormDetailInfo(row).get(6);
			if (!status.equalsIgnoreCase("UNKNOWN"))
				testRst = true;
			else
			{
				logger.error("The validation status should not be UNKNOWN");
			}

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test3916() throws Exception
	{
		String caseID = "3916";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);
			String message = testdata.get(5);

			// String formCode = splitReturn(Form).get(0);
			// String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			String errorMsg = formInstancePage.getReadyForApproveMessage();
			if (errorMsg.equalsIgnoreCase(message))
			{
				testRst = true;
			}
			else
			{
				logger.error("The error message should be:" + message);
			}

			testRst = false;
			String status = formInstancePage.getApproveStatus();
			if (status.equalsIgnoreCase("NOT ATTESTED"))
				testRst = true;
			else
			{
				logger.error("The approve status should be NOT_ATTESTED");
			}

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3917() throws Exception
	{
		String caseID = "3917";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserNameA = testdata.get(4);
			String UserNameB = testdata.get(5);
			String message = testdata.get(6);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();
			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

			formInstancePage.approveForm("Test approve");
			String status = formInstancePage.getApproveStatus();
			if (status.equalsIgnoreCase("Being Attested"))
				testRst = true;
			else
			{
				logger.error("The approve status should be Attested");
			}

			testRst = false;
			String msg = formInstancePage.getApproveFormMessage("Test approve");
			if (msg.equalsIgnoreCase(message))
				testRst = true;
			else
			{
				logger.error("The approve message should be:" + message);
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3955() throws Exception
	{
		String caseID = "3955";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);
			String Amessage = testdata.get(5);
			String Rmessage = testdata.get(6);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			String msg = formInstancePage.getApproveFormMessage("Test approve");
			if (msg.equalsIgnoreCase(Amessage))
				testRst = true;
			else
			{
				logger.error("The approve message should be:" + Amessage);
			}
			testRst = false;
			msg = formInstancePage.getRejectFormMessage("Test reject");
			if (msg.equalsIgnoreCase(Rmessage))
				testRst = true;
			else
			{
				logger.error("The approve message should be:" + Rmessage);
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3965() throws Exception
	{
		String caseID = "3965";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);
			String ImportFile = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);

			int pos = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			if (pos > 0)
			{
				String status = listPage.getFormDetailInfo(pos).get(6);
				if (status.equalsIgnoreCase("lock"))
				{
					FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
					formInstancePage.unlockClick();
					formInstancePage.closeFormInstance();
				}
			}

			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			int logNums = formInstancePage.getApproveLogAmt();
			if (logNums > 0)
				testRst = true;
			else
			{
				logger.error("The approve log nums should not be 0");
			}
			formInstancePage.closeFormInstance();
			testRst = false;
			File importFile = new File(testdata_Workflow.replace("Workflow.xml", ImportFile));
			formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			logNums = formInstancePage.getApproveLogAmt();
			if (logNums == 0)
				testRst = true;
			else
			{
				logger.error("The approve log nums should be 0");
			}

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3929() throws Exception
	{
		String caseID = "3929";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserNameA = testdata.get(4);
			String UserNameB = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();
			homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameB, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.rejectForm("test reject");
			String status = formInstancePage.getApproveStatus();
			if (status.equalsIgnoreCase("Not Attested"))
				testRst = true;
			else
			{
				logger.error("The approve status should be:Not Attested");
			}
			formInstancePage.closeFormInstance();
			testRst = false;
			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo(row);
			if (formDetailInfo.get(6).equalsIgnoreCase("Lock") && formDetailInfo.get(7).equalsIgnoreCase("pass") && formDetailInfo.get(11).equalsIgnoreCase("NOT_ATTESTED"))
				testRst = true;
			else
			{
				logger.error("The form status should be:unLock,pass,NOT_ATTESTED");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	public void test3931() throws Exception
	{
		String caseID = "3931";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);

			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.rejectForm("test reject");
			String status = formInstancePage.getApproveStatus();
			if (status.equalsIgnoreCase("Not Attested"))
				testRst = true;
			else
			{
				logger.error("The approve status should be:Not Attested");
			}
			formInstancePage.closeFormInstance();
			testRst = false;
			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo(row);
			if (formDetailInfo.get(6).equalsIgnoreCase("Lock") && formDetailInfo.get(7).equalsIgnoreCase("pass") && formDetailInfo.get(11).equalsIgnoreCase("NOT_ATTESTED"))
				testRst = true;
			else
			{
				logger.error("The form status should be:Lock,pass,NOT_ATTESTED");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3932() throws Exception
	{
		String caseID = "3932";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserNameA = testdata.get(4);
			String UserNameB = testdata.get(5);
			String UserNameC = testdata.get(6);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserNameA, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();
			homePage = listPage.logout();

			String[] users =
			{ UserNameB, UserNameC };
			for (int i = 0; i < users.length; i++)
			{
				listPage = homePage.loginAs(users[i], "password");
				listPage.getProductListPage(Regulator, Group, null, null);
				formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
				formInstancePage.approveForm("test approve");
				listPage = formInstancePage.closeFormInstance();
				homePage = listPage.logout();
			}

			listPage = homePage.logon();
			listPage.getProductListPage(Regulator, Group, Form, null);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.approveForm("test approve");

			String status = formInstancePage.getApproveStatus();
			if (status.equalsIgnoreCase("Attested"))
				testRst = true;
			else
			{
				logger.error("The approve status should be:Attested");
			}
			formInstancePage.closeFormInstance();
			testRst = false;
			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo(row);
			if (formDetailInfo.get(6).equalsIgnoreCase("Lock") && formDetailInfo.get(7).equalsIgnoreCase("PASS") && formDetailInfo.get(11).equalsIgnoreCase("ATTESTED"))
				testRst = true;
			else
			{
				logger.error("The form status should be:Lock,PASS,ATTESTED");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3943() throws Exception
	{
		String caseID = "3943";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			if (formInstancePage.isPagination())
				testRst = true;
			else
			{
				logger.error("The workflow log does not support pagination");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test6067() throws Exception
	{
		String caseID = "6067";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);

			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			int logNums = formInstancePage.getApproveLogAmt();
			if (logNums > 0)
				testRst = true;
			else
			{
				logger.error("The approve log nums should not be 0");
			}
			formInstancePage.closeFormInstance();
			testRst = false;

			formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			logNums = formInstancePage.getApproveLogAmt();
			if (logNums == 0)
				testRst = true;
			else
			{
				logger.error("The approve log nums should be 0");
			}

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3919() throws Exception
	{
		String caseID = "3919";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			List<String> cols = formInstancePage.getApproveLogColumns();
			if (cols.get(0).equalsIgnoreCase("User") && cols.get(1).equalsIgnoreCase("Time") && cols.get(2).equalsIgnoreCase("Action") && cols.get(3).equalsIgnoreCase("On Revision")
					&& cols.get(4).equalsIgnoreCase("Comment"))
				testRst = true;
			else
			{
				logger.error("Workflow log table columns are wrong!");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test3918() throws Exception
	{
		String caseID = "3918";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			List<String> cols = listPage.getApproveLogColumns(formCode, version, ProcessDate);
			if (cols.get(0).equalsIgnoreCase("User") && cols.get(1).equalsIgnoreCase("Time") && cols.get(2).equalsIgnoreCase("Action") && cols.get(3).equalsIgnoreCase("On Revision")
					&& cols.get(4).equalsIgnoreCase("Comment"))
				testRst = true;
			else
			{
				logger.error("Workflow log table columns are wrong!");
			}

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}

	@Test
	public void test3926() throws Exception
	{
		String caseID = "3926";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.clickReadyForApprove();
			formInstancePage.closeFormInstance();

			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo(row);
			if (formDetailInfo.get(6).equalsIgnoreCase("Lock") && formDetailInfo.get(7).equalsIgnoreCase("PASS") && formDetailInfo.get(11).equalsIgnoreCase("READY_FOR_ATTESTATION"))
				testRst = true;
			else
			{
				logger.error("The form status should be:Lock,PASS,READY_FOR_ATTESTATION");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
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
	public void test3930() throws Exception
	{
		String caseID = "3930";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_Workflow, nodeName);
			String Regulator = testdata.get(0);
			String Group = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String UserName = testdata.get(4);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			formInstancePage.closeFormInstance();

			int row = listPage.getFormInstanceRowPos(formCode, version, ProcessDate);
			List<String> formDetailInfo = listPage.getFormDetailInfo(row);
			if (formDetailInfo.get(6).equalsIgnoreCase("UNLOCK") && formDetailInfo.get(7).equalsIgnoreCase("UNKNOWN") && formDetailInfo.get(11).equalsIgnoreCase("NOT_ATTESTED"))
				testRst = true;
			else
			{
				logger.error("The form status should be: UNLOCK,UNKNOWN,NOT_ATTESTED");
			}

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Workflow");

		}
	}
}
