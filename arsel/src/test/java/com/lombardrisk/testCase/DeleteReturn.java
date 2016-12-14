package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;

/**
 * Created by Leo Tu on 4/25/2016.
 */
public class DeleteReturn extends TestTemplate
{

	@Test
	public void test5595() throws Exception
	{
		boolean testRst = false;
		String caseID = "5595";
		logger.info("====Verify privileged user is able to delete return with one edition,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");
			int EditionAmt = Integer.parseInt(getElementValueFromXML(testData_DeleteReturn, nodeName, "EditionAmt"));
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			String edition = "1";

			String random = getRandomString(10);
			String sql = "SELECT COUNT(*) FROM \"FIN_FORM_INSTANCE_DEL_LOG\" WHERE \"FORM_CODE\"='" + formCode + "' AND \"FORM_VERSION\"=" + version + " AND \"COMMENT\"=\'" + random + "\'";
			// String nums_before = DBQuery.queryRecord(sql);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, "All");
			ShowDeletedReturnPage showDeletedReturnPage = listPage.enterShowDeletedReturnPage();
			showDeletedReturnPage.restoreReturn(ReferenceDate, formCode, version, edition);

			listPage.deleteFormInstance(Form, ReferenceDate, random, true);
			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("Deleted form still existed in list!").isEqualTo(false);
			String nums_after = DBQuery.queryRecord(sql);
			assertThat(Integer.parseInt(nums_after)).as("Delete return logs are incorrect!").isEqualTo(EditionAmt);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5610() throws Exception
	{
		boolean testRst = false;
		String caseID = "5610";
		logger.info("====Verify privileged user is able to delete return with multi editions,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");
			int EditionAmt = Integer.parseInt(getElementValueFromXML(testData_DeleteReturn, nodeName, "EditionAmt"));
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			String random = getRandomString(10);
			String sql = "SELECT COUNT(*) FROM \"FIN_FORM_INSTANCE_DEL_LOG\" WHERE \"FORM_CODE\"='" + formCode + "' AND \"FORM_VERSION\"=" + version + " AND \"COMMENT\"=\'" + random + "\'";
			// String nums_before = DBQuery.queryRecord(sql);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, Form, "All");
			String list[] =
			{ "1", "2", "3", "4" };
			ShowDeletedReturnPage showDeletedReturnPage = listPage.enterShowDeletedReturnPage();
			showDeletedReturnPage.restoreReturn(ReferenceDate, formCode, version, list);
			listPage.deleteFormInstance(Form, ReferenceDate, random, true);
			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("Deleted form still existed in list!").isEqualTo(false);
			String nums_after = DBQuery.queryRecord(sql);
			assertThat(Integer.parseInt(nums_after)).as("Delete return logs are incorrect!").isEqualTo(EditionAmt);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5596() throws Exception
	{
		boolean testRst = false;
		String caseID = "5596";
		logger.info("====Verify privileged user is able to cancel delete return,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			listPage.deleteFormInstance(Form, ReferenceDate, "test delete return", false);
			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("Cancel deleted return does not exist in list!").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5598() throws Exception
	{
		boolean testRst = false;
		String caseID = "5598";
		logger.info("====Verify privileged user is unable to delete locked return,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String UserName = getElementValueFromXML(testData_DeleteReturn, nodeName, "UserName");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			homePage.loginAs(UserName, "password");
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			String msg = listPage.getDeleteFormInstanceMessage(Form, ReferenceDate);
			assertThat(msg).as("Delete return message is incorrect, now prompt[" + msg + "]!").isEqualTo("The return " + formCode + " is locked,only un-locked return can be deleted.");
			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("The locked return should not be deleted!").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test6083() throws Exception
	{
		boolean testRst = false;
		String caseID = "6083";
		logger.info("====Verify privileged user is able to delete locked return with one edition,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, "All");
			listPage.deleteFormInstance(Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.lockClick();
			formInstancePage.closeFormInstance();
			listPage.deleteFormInstance(Form, ReferenceDate);
			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("The locked return should be deleted!").isEqualTo(false);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test6084() throws Exception
	{
		boolean testRst = false;
		String caseID = "6084";
		logger.info("====Verify privileged user is able to delete locked return with multi edition,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, Form, "All");
			String list[] =
			{ "1", "2" };
			ShowDeletedReturnPage showDeletedReturnPage = listPage.enterShowDeletedReturnPage();
			showDeletedReturnPage.restoreReturn(ReferenceDate, formCode, version, list);
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			List<String> status = listPage.getFormDetailInfo(rowIndex);
			if (status.get(6).equals("unlock"))
			{
				FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
				formInstancePage.lockClick();
				formInstancePage.closeFormInstance();
			}
			listPage.deleteFormInstance(Form, ReferenceDate);
			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("The locked return should be deleted!").isEqualTo(false);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5597() throws Exception
	{
		boolean testRst = false;
		String caseID = "5597";
		logger.info("====Verify privileged user is unable to delete return without comments,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			String msg = listPage.getDeleteFormInstanceMessage(Form, ReferenceDate);
			assertThat(msg).as("Delete return message is incorrect").isEqualTo("Delete comment is required");
			listPage.closeDeleteFormDlg();
			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("Delete return without comment should not be deleted!").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID + ",5651", testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5650() throws Exception
	{
		boolean testRst = false;
		String caseID = "5650";
		logger.info("====Verify delete edition comments is required,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, "All");
			ShowDeletedReturnPage showDeletedReturnPage = listPage.enterShowDeletedReturnPage();
			showDeletedReturnPage.restoreReturn(ReferenceDate, formCode, version, "3");

			DeleteReturnLogPage deleteReturnLogPag = listPage.enterDeleteReturnLogPage();
			int editionAmt = deleteReturnLogPag.getlogNums();
			EditionManagePage editionManagePage = listPage.openEditionManage(formCode, version, ReferenceDate);
			editionManagePage.deleteEdition("3", "test delete edition");
			editionManagePage.closeEditionManage();

			deleteReturnLogPag = listPage.enterDeleteReturnLogPage();
			int editionAmt_After = deleteReturnLogPag.getlogNums();
			assertThat(editionAmt_After - editionAmt).as("Deleted edition not in delete return log page!").isEqualByComparingTo(1);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5604() throws Exception
	{
		boolean testRst = false;
		String caseID = "5604";
		logger.info("====Verify privileged user is able to Restore return which has one edition,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");
			String EditionNo = getElementValueFromXML(testData_DeleteReturn, nodeName, "EditionNo");

			int topNums = 1;
			String editionNo = EditionNo.split(",")[0];

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, "All");
			if (listPage.getProcessDateOptions().contains(ReferenceDate))
			{
				listPage.deleteFormInstance(Form, ReferenceDate);
			}
			ShowDeletedReturnPage showDeletedReturnPage = listPage.enterShowDeletedReturnPage();
			showDeletedReturnPage.restoreReturn(formCode, version, ReferenceDate);

			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("Restored return[" + Form + "] deos not exist in list!").isEqualTo(true);
			DeleteReturnLogPage deleteReturnLogPage = listPage.enterDeleteReturnLogPage();
			String action = deleteReturnLogPage.getLatestAction(formCode, version, editionNo, topNums);
			deleteReturnLogPage.closeDeleteReturnLog();
			assertThat(action).as("Last action should be restore!").isEqualTo("Restored");
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5615() throws Exception
	{
		boolean testRst = true;
		String caseID = "5615";
		logger.info("====Verify privileged user is able to Restore return which has multi edition,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_DeleteReturn, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String RestoreEditionNo1 = testdata.get(4);
			String DeletedEditionNo1 = testdata.get(5);
			String RestoreEditionNo2 = testdata.get(6);
			String DeletedEditionNo2 = testdata.get(7);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, "All");
			listPage.deleteFormInstance(Form, ReferenceDate);
			ShowDeletedReturnPage showDeletedReturnPage = listPage.enterShowDeletedReturnPage();
			showDeletedReturnPage.restoreReturn(ReferenceDate, formCode, version, RestoreEditionNo1);
			EditionManagePage editionManagePage = listPage.openEditionManage(formCode, version, ReferenceDate);
			if (!editionManagePage.getEditionState(RestoreEditionNo1).equals("ACTIVE"))
			{
				testRst = false;
				logger.error("Restore edtion should be ACTIVE!");
			}
			editionManagePage.closeEditionManage();
			// listPage.setProcessDate(ReferenceDate);
			showDeletedReturnPage = listPage.enterShowDeletedReturnPage();
			List<String> editions = showDeletedReturnPage.getDeletedEditions(ReferenceDate);
			if (editions.size() != DeletedEditionNo1.split("#").length)
			{
				testRst = false;
			}
			else
			{
				for (String item : DeletedEditionNo1.split("#"))
				{
					if (!editions.contains(item))
					{
						testRst = false;
						logger.error("Edition [" + item + "]  should be deleted");
					}
				}
			}

			showDeletedReturnPage.restoreReturn(ReferenceDate, formCode, version, RestoreEditionNo2);
			editionManagePage = listPage.openEditionManage(formCode, version, ReferenceDate);
			if (!editionManagePage.getEditionState(RestoreEditionNo2).equals("ACTIVE"))
			{
				testRst = false;
				logger.error("Restore edtion should be ACTIVE!");
			}
			if (!editionManagePage.getEditionState(RestoreEditionNo1).equals("DORMANT"))
			{
				testRst = false;
				logger.error("Restore edtion should be DORMANT!");
			}
			editionManagePage.closeEditionManage();

			showDeletedReturnPage = listPage.enterShowDeletedReturnPage();
			editions = showDeletedReturnPage.getDeletedEditions(ReferenceDate);
			if (editions.size() != DeletedEditionNo2.split("#").length)
			{
				testRst = false;
			}
			else
			{
				for (String item : DeletedEditionNo2.split("#"))
				{
					if (!editions.contains(item))
					{
						testRst = false;
						logger.error("Edition [" + item + "]  should be deleted");
					}
				}
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}
}
