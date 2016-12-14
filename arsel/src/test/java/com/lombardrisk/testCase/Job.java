package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 7/1/2016
 */

public class Job extends TestTemplate
{
	@Test
	public void testJobStatus() throws Exception
	{
		ListPage listPage = super.m.listPage;

		String caseIDs[] =
		{ "5749", "5800" };
		for (String caseID : caseIDs)
		{
			logger.info("====Test retrieve[case id=" + caseID + "]====");
			boolean testRst = true;
			try
			{
				String nodeName = "C" + caseID;
				List<String> elementValues = getElementValueFromXML(jobData, nodeName);
				String Regulator = elementValues.get(0);
				String Form = elementValues.get(1);
				String Entity = elementValues.get(2);
				String ReferenceDate = elementValues.get(3);
				String Status = elementValues.get(4);

				listPage.getProductListPage(Regulator, Entity, null, null);
				listPage.deleteFormInstance(Form, ReferenceDate);
				logger.info("Begin set retrieve properties");
				int init = listPage.getNotificationNums();
				logger.info("There are " + init + " notifcation(s)");
				FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
				retrievePage.setGroup(Entity);
				retrievePage.setReferenceDate(ReferenceDate);
				retrievePage.setForm(Form);
				retrievePage.clickOK();
				if (isJobSuccessed())
				{
					logger.info("Retrieve form sucessed");
				}
				else
					logger.info("Retrieve form failed");

				logger.info("Begin check job status in job manager");
				JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
				List<String> jobDetail = jobManagerPage.getLatestJobInfo();
				if (!jobDetail.get(8).equalsIgnoreCase(Status))
				{
					testRst = true;
					logger.error("Expected status is[" + Status + "], but actual status is[" + jobDetail.get(6) + "]");
				}
				else
					logger.info("Test passed");

				jobManagerPage.backToDashboard();

			}
			catch (RuntimeException e)
			{
				testRst = false;
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			finally
			{
				writeTestResultToFile(caseID, testRst, "Job");
			}
		}
	}

	@Test
	public void testC5912() throws Exception
	{
		String caseID = "5912";
		logger.info("====Test Verify Duplicated export job cannot be started [case id=" + caseID + "]====");
		boolean testRst1 = false;
		boolean testRst2 = false;
		boolean testRst3 = false;
		boolean testRst4 = false;
		String physicalViewName = null;
		String backup1 = null;
		String backup2 = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Regulator = elementValues.get(0);
			String Form = elementValues.get(1);
			String Entity = elementValues.get(2);
			String ReferenceDate = elementValues.get(3);
			backup1 = elementValues.get(4);
			backup2 = elementValues.get(5);
			physicalViewName = elementValues.get(6);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);

			String sql_drop = "drop table \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_drop);

			String sql_Create = "create table \"" + physicalViewName + "\" as select * from \"" + backup2 + "\"";
			DBQuery.updateSourceVew(sql_Create);

			listPage.deleteFormInstance(Form, ReferenceDate);
			logger.info("Begin set retrieve properties");
			int init = listPage.getNotificationNums();
			logger.info("There are " + init + " notifcation(s)");
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(Entity);
			retrievePage.setReferenceDate(ReferenceDate);
			retrievePage.setForm(Form);
			retrievePage.clickOK();
			listPage.closeJobDialog();

			retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(Entity);
			retrievePage.setReferenceDate(ReferenceDate);
			retrievePage.setForm(Form);
			retrievePage.clickOK();

			if (listPage.isDuplicatedJob())
			{
				testRst1 = true;
				JobDetailsPage jobDetailsPage = listPage.enterJobDetailsPageFromJobDialog();
				if (jobDetailsPage.isJobDetailsPage())
				{
					testRst2 = true;
					jobDetailsPage.stopJob(1);
					JobManagerPage jobManagerPage = jobDetailsPage.backToJobManager();
					List<String> jobDetail = jobManagerPage.getJobInfo(1);
					if (jobDetail.get(8).equalsIgnoreCase("STOPPED"))
						testRst3 = true;
					else
						logger.error("Job status should be: STOPPED");
					jobManagerPage.backToDashboard();

					int after = listPage.getNotificationNums();
					logger.info("There are " + init + " notifcation(s)");
					if (after - init == 1)
						testRst4 = true;
					logger.error("Should add a message in message center!");
				}
			}
			else
				logger.error("Should pop up dialog to prompt Duplicated job!");

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			// restore source view
			String sql_drop = "drop table \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_drop);
			String sql_backup = "create table \"" + physicalViewName + "\" as select * from \"" + backup1 + "\"";
			DBQuery.updateSourceVew(sql_backup);
			writeTestResultToFile(caseID, testRst1, "Job");
			writeTestResultToFile("5988", testRst2, "Job");
			writeTestResultToFile("5841", testRst3, "Job");
			writeTestResultToFile("6057", testRst4, "Job");
		}
	}

	@Test
	public void testFormStatus() throws Exception
	{
		String caseID = "6009";
		logger.info("====Test Verify form Status after form exported [case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Regulator = elementValues.get(0);
			String ReferenceDate = elementValues.get(2);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, null, null, null);
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			String Form = jobManagerPage.getPassedExpoertedJob();
			String formCode = splitReturn(Form).get(0);
			String formVersion = splitReturn(Form).get(1);
			int rowIndex = listPage.getFormInstanceRowPos(formCode, formVersion, ReferenceDate);
			List<String> formDetail = listPage.getFormDetailInfo(rowIndex);
			if (formDetail.get(5).equalsIgnoreCase("lock") && formDetail.get(6).equalsIgnoreCase("Pass") && formDetail.get(7).equalsIgnoreCase("Pass")
					&& formDetail.get(8).equalsIgnoreCase("Attested"))
				testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			caseID = "6009,6011,6012,6014,6015";
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5728() throws Exception
	{

		String caseID = "5728";
		logger.info("====Verify Job Manager button has been added to menu bar in dashboard [case id=" + caseID + "]====");
		boolean testRst = false;
		JobManagerPage jobManagerPage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			jobManagerPage = listPage.enterJobManagerPage();
			assertThat(jobManagerPage.isFilterExist()).isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5893() throws Exception
	{

		String caseID = "5893";
		logger.info("====Verify ENTITY column has been added to return list panel in dashboard [case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			ListPage listPage = super.m.listPage;
			if (!listPage.getColumnNameInFormList(1).equalsIgnoreCase("ENTITY"))
				testRst = false;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5895() throws Exception
	{

		String caseID = "5895";
		logger.info("====Verify the UI of Main Job Manager window is same as design [case id=" + caseID + "]====");
		boolean testRst = true;
		JobManagerPage jobManagerPage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String expectedColumns = elementValues.get(0);
			ListPage listPage = super.m.listPage;
			jobManagerPage = listPage.enterJobManagerPage();
			List<String> columns = jobManagerPage.getColumnNamesInList();
			if (expectedColumns.split("#").length == columns.size())
			{
				for (String item : expectedColumns.split("#"))
				{
					if (!columns.contains(item))
						testRst = false;
				}
			}
			else
			{
				testRst = false;
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			jobManagerPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5897() throws Exception
	{
		String caseID = "5897";
		logger.info("====Verify the UI of Run Details window is same as design [case id=" + caseID + "]====");
		boolean testRst = true;
		JobManagerPage jobManagerPage = null;
		JobDetailsPage jobDetailsPage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String expectedColumns = elementValues.get(0);
			ListPage listPage = super.m.listPage;
			jobManagerPage = listPage.enterJobManagerPage();
			jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> columns = jobDetailsPage.getColumnNamesInList();
			if (expectedColumns.split("#").length == columns.size())
			{
				for (String item : expectedColumns.split("#"))
				{
					item = item.toUpperCase();
					if (!columns.contains(item))
						testRst = false;
				}
			}
			else
			{
				testRst = false;
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			jobDetailsPage.backToDashboard();
			writeTestResultToFile(caseID + ",5899", testRst, "Job");
		}
	}

	@Test
	public void testC5819() throws Exception
	{
		String caseID = "5819";
		logger.info("====Verify columns can be sorted in Main Job Manager window [case id=" + caseID + "]====");
		boolean testRst = true;
		JobManagerPage jobManagerPage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			jobManagerPage = listPage.enterJobManagerPage();
			if (!jobManagerPage.isColumnSupportSort())
				testRst = false;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			jobManagerPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5738() throws Exception
	{
		String caseID = "5738";
		logger.info("====Verify columns can be sorted in Main Job Manager window [case id=" + caseID + "]====");
		boolean testRst = true;
		JobManagerPage jobManagerPage = null;
		JobDetailsPage jobDetailsPage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			jobManagerPage = listPage.enterJobManagerPage();
			jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			if (!jobDetailsPage.isColumnSupportSort())
				testRst = false;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			jobDetailsPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5964() throws Exception
	{
		String caseID = "5964";
		logger.info("====Verify JOB STATUS column has been added to return list panel in dashboard [case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			ListPage listPage = super.m.listPage;
			if (!listPage.isJobStatusDisplayed())
				testRst = false;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5965() throws Exception
	{
		String caseID = "5965";
		logger.info("====Verify WORKFLOW STATUS column has been added to return list panel in dashboard [case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			ListPage listPage = super.m.listPage;
			if (!listPage.isWorkflowStatusDisplayed())
				testRst = false;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5933() throws Exception
	{
		String caseID = "5933";
		logger.info("====Verify approve count has been added to APPROVAL column in return list panel [case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			ListPage listPage = super.m.listPage;
			if (listPage.getApproveCount(1).startsWith("(") && listPage.getApproveCount(1).endsWith(")"))
				testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5989() throws Exception
	{
		String caseID = "5989";
		logger.info("====Verify user can jump to Job Results page by clicking Error Details link in Message Centre [case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Regulator = elementValues.get(0);
			String Entity = elementValues.get(1);
			String Form = elementValues.get(2);
			String ReferenceDate = elementValues.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);
			listPage.deleteFormInstance(Form, ReferenceDate);
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(Entity);
			retrievePage.setReferenceDate(ReferenceDate);
			retrievePage.setForm(Form);
			retrievePage.clickOK();
			if (isJobSuccessed())
				logger.info("Retrieve form sucessed");
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			MessageCenter messageCenter = listPage.enterMessageCenterPage();
			JobDetailsPage jobDetailsPage = messageCenter.clickFirstErrorLink();
			if (!jobDetailsPage.isJobDetailsPage())
				testRst = false;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",5831", testRst, "Job");
		}
	}

	@Test
	public void testC5982() throws Exception
	{
		String caseID = "5982";
		logger.info("====Verify Export file will be failed due to Network location does not setup [case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Entity = elementValues.get(0);
			String Location = elementValues.get(1);
			String Regulator = elementValues.get(2);
			String Form = elementValues.get(3);
			String ReferenceDate = elementValues.get(4);
			String ExportType = elementValues.get(5);
			String Module = elementValues.get(6);
			String ErrorMessage = elementValues.get(7);

			ListPage listPage = super.m.listPage;
			PhysicalLocationPage physicalLocationPage = listPage.enterPhysicalLoaction();
			physicalLocationPage.deleteExistLocation(Entity);
			physicalLocationPage.backToDashboard();

			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			List<String> formDetail = listPage.getFormDetailInfo(1);
			if (formDetail.get(6).equalsIgnoreCase("lock"))
			{
				FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
				formInstancePage.unlockClick();
				formInstancePage.closeFormInstance();
			}
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.clickReadyForApprove();
			listPage = formInstancePage.closeFormInstance();
			HomePage homePage = listPage.approveReturn(listPage, Regulator, Entity, Form, ReferenceDate);
			homePage.logon();
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

			formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			String errorMsg = formInstancePage.getExportDataScheduleMessage(ExportType, Module, null, false);
			formInstancePage.closeFormInstance();
			if (ErrorMessage.equals(errorMsg))
				testRst = false;
			else
				logger.error("Expected message is:" + ErrorMessage + ", but actual message is:" + errorMsg);

			physicalLocationPage = listPage.enterPhysicalLoaction();
			physicalLocationPage.addFileLocation(Entity, Location, null);
			physicalLocationPage.backToDashboard();

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC6030() throws Exception
	{
		String caseID = "6030";
		logger.info("====Verify Export file will be failed due to physical path does not have write permission-Windows [case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Entity = elementValues.get(0);
			String Location = elementValues.get(1);
			String Regulator = elementValues.get(2);
			String Form = elementValues.get(3);
			String ReferenceDate = elementValues.get(4);
			String ExportType = elementValues.get(5);
			String Module = elementValues.get(6);
			String JobStatus = elementValues.get(7);
			String ErrorMessage = elementValues.get(8);
			String Location2 = elementValues.get(9);

			ListPage listPage = super.m.listPage;
			PhysicalLocationPage physicalLocationPage = listPage.enterPhysicalLoaction();
			physicalLocationPage.editExistLocation(Entity, null, Location2, null);
			physicalLocationPage.backToDashboard();

			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			/*
			 * List<String> formDetail = listPage.getFormDetailInfo(1); if
			 * (formDetail.get(6).equalsIgnoreCase("lock")) { FormInstancePage
			 * formInstancePage = listPage.openFormInstance(formCode, version,
			 * ReferenceDate); formInstancePage.unlockClick();
			 * formInstancePage.closeFormInstance(); } FormInstancePage
			 * formInstancePage = listPage.openFormInstance(formCode, version,
			 * ReferenceDate); formInstancePage.clickReadyForApprove(); listPage
			 * = formInstancePage.closeFormInstance(); HomePage homePage =
			 * listPage.approveReturn(listPage, Regulator, Entity, Form,
			 * ReferenceDate); homePage.logon();
			 * listPage.getProductListPage(Regulator, Entity, Form,
			 * ReferenceDate);
			 */
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.exportDataSchduleReturn(ExportType, Module, null);
			formInstancePage.closeFormInstance();

			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			List<String> jobDetailsList = jobManagerPage.getLatestJobInfo();
			if (!jobDetailsList.get(8).equals(JobStatus))
			{
				logger.error("Job status is incorrect,should be " + JobStatus);
				testRst = false;
			}
			if (!jobDetailsList.get(10).equals(ErrorMessage))
			{
				testRst = false;
				logger.error("Expected log is:" + ErrorMessage + ", but actual log is:" + jobDetailsList.get(7));
			}
			jobManagerPage.backToDashboard();

			physicalLocationPage = listPage.enterPhysicalLoaction();
			physicalLocationPage.editExistLocation(Entity, null, Location, null);
			physicalLocationPage.backToDashboard();

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC6033() throws Exception
	{
		String caseID = "6033";
		logger.info("====Verify Export file will be failled due to source field not found [case id=" + caseID + "]====");
		boolean testRst = false;
		String physicalViewName = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Regulator = elementValues.get(0);
			String Entity = elementValues.get(1);
			String Form = elementValues.get(2);
			String ReferenceDate = elementValues.get(3);
			String ExportType = elementValues.get(4);
			String Module = elementValues.get(5);
			String Fields = elementValues.get(6);
			String JobStatus = elementValues.get(7);
			String ErrorMessage = elementValues.get(8);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

			// if form is locked, open form then unlock this form
			List<String> formDetail = listPage.getFormDetailInfo(1);
			if (formDetail.get(6).equalsIgnoreCase("lock"))
			{
				FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
				formInstancePage.unlockClick();
				formInstancePage.closeFormInstance();
			}

			// approve this form
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.clickReadyForApprove();
			listPage = formInstancePage.closeFormInstance();
			HomePage homePage = listPage.approveReturn(listPage, Regulator, Entity, Form, ReferenceDate);
			homePage.logon();
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

			// backup source view, then update source view
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, ReferenceDate);
			List<String> views = returnSourcePage.getSourceView();
			String SQL = "SELECT \"PHYSICAL_VIEW_NAME\" FROM \"CFG_DW_VIEW_BINDING\" WHERE \"VIEW_CODE\"='" + views.get(0) + "'";
			physicalViewName = DBQuery.queryRecord(SQL);

			String sql_backup = "create table \"" + physicalViewName + "_backup\" as select * from \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_backup);
			String sql_drop = "";
			for (String field : Fields.split("#"))
			{
				sql_drop = sql_drop + "\"" + field + "\",";
			}
			sql_drop = sql_drop.substring(0, sql_drop.length() - 1);
			sql_drop = "alter table \"" + physicalViewName + "\" drop(" + sql_drop + ")";
			DBQuery.updateSourceVew(sql_drop);

			// export ds return
			formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.exportDataSchduleReturn(ExportType, Module, null);
			formInstancePage.closeFormInstance();

			// check job status and error message
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			List<String> jobDetailsList = jobManagerPage.getLatestJobInfo();
			if (!jobDetailsList.get(8).equals(JobStatus))
			{
				logger.error("Job status is incorrect,should be " + JobStatus);
				testRst = false;
			}
			if (!jobDetailsList.get(10).equals(ErrorMessage))
			{
				testRst = false;
				logger.error("Expected log is:" + ErrorMessage + ", but actual log is:" + jobDetailsList.get(7));
			}
			jobManagerPage.backToDashboard();

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// restore source view
			String sql_drop = "drop table \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_drop);
			String sql_backup = "create table \"" + physicalViewName + "\" as select * from \"" + physicalViewName + "_backup\"";
			DBQuery.updateSourceVew(sql_backup);

			sql_drop = "drop table \"" + physicalViewName + "_backup\"";
			DBQuery.updateSourceVew(sql_drop);
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC6049() throws Exception
	{
		String caseID = "6049";
		logger.info("====Verify Export file will be failled due to source field not found [case id=" + caseID + "]====");
		boolean testRst = false;
		String physicalViewName = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Regulator = elementValues.get(0);
			String Entity = elementValues.get(1);
			String Form = elementValues.get(2);
			String ReferenceDate = elementValues.get(3);
			String ExportType = elementValues.get(4);
			String Module = elementValues.get(5);
			String ErrorMessage = elementValues.get(6);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

			// if form is locked, open form then unlock this form
			List<String> formDetail = listPage.getFormDetailInfo(1);
			if (formDetail.get(6).equalsIgnoreCase("lock"))
			{
				FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
				formInstancePage.unlockClick();
				formInstancePage.closeFormInstance();
			}

			// approve this form
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.clickReadyForApprove();
			listPage = formInstancePage.closeFormInstance();
			HomePage homePage = listPage.approveReturn(listPage, Regulator, Entity, Form, ReferenceDate);
			homePage.logon();
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

			// backup source view, then drop source view
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, ReferenceDate);
			List<String> views = returnSourcePage.getSourceView();
			String SQL = "SELECT \"PHYSICAL_VIEW_NAME\" FROM \"CFG_DW_VIEW_BINDING\" WHERE \"VIEW_CODE\"='" + views.get(0) + "'";
			physicalViewName = DBQuery.queryRecord(SQL);

			String sql_backup = "create table \"" + physicalViewName + "_backup\" as select * from \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_backup);
			String sql_drop = "drop table \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_drop);

			// export ds return
			formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			String errorMsg = formInstancePage.getExportDataScheduleMessage(ExportType, Module, null, false);
			formInstancePage.closeFormInstance();
			if (ErrorMessage.equals(errorMsg))
				testRst = false;
			else
				logger.error("Expected message is:" + ErrorMessage + ", but actual message is:" + errorMsg);

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// restore source view
			String sql_drop = "drop table \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_drop);
			String sql_backup = "create table \"" + physicalViewName + "\" as select * from \"" + physicalViewName + "_backup\"";
			DBQuery.updateSourceVew(sql_backup);

			sql_drop = "drop table \"" + physicalViewName + "_backup\"";
			DBQuery.updateSourceVew(sql_drop);
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC5948() throws Exception
	{
		String caseID = "5948";
		logger.info("====Verify User can Stop DS return export job [case id=" + caseID + "]====");
		boolean testRst = false;
		boolean testRst2 = false;
		String physicalViewName = null;
		String backup1 = null;
		String backup2 = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Regulator = elementValues.get(0);
			String Entity = elementValues.get(1);
			String Form = elementValues.get(2);
			String ReferenceDate = elementValues.get(3);
			String ExportType = elementValues.get(4);
			String Module = elementValues.get(5);
			backup1 = elementValues.get(6);
			backup2 = elementValues.get(7);
			physicalViewName = elementValues.get(8);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);

			String sql_drop = "drop table \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_drop);

			String sql_Create = "create table \"" + physicalViewName + "\" as select * from \"" + backup2 + "\"";
			DBQuery.updateSourceVew(sql_Create);

			listPage.deleteFormInstance(Form, ReferenceDate);
			logger.info("Begin set retrieve properties");
			int init = listPage.getNotificationNums();
			logger.info("There are " + init + " notifcation(s)");
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(Entity);
			retrievePage.setReferenceDate(ReferenceDate);
			retrievePage.setForm(Form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				listPage.closeJobDialog();

				// approve this form
				FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
				formInstancePage.clickReadyForApprove();
				listPage = formInstancePage.closeFormInstance();
				HomePage homePage = listPage.approveReturn(listPage, Regulator, Entity, Form, ReferenceDate);
				homePage.logon();
				listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

				// export ds return
				formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
				formInstancePage.exportDataSchduleReturn(ExportType, Module, null);
				listPage = formInstancePage.closeFormInstance();
				JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
				JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
				jobDetailsPage.stopJob(1);

				jobManagerPage = jobDetailsPage.backToJobManager();
				List<String> jobDetail = jobManagerPage.getJobInfo(1);
				if (jobDetail.get(8).equalsIgnoreCase("STOPPED"))
					testRst = true;
				jobManagerPage.backToDashboard();

				int after = listPage.getNotificationNums();
				logger.info("There are " + init + " notifcation(s)");
				if (after - init == 1)
					testRst2 = true;
				logger.error("Should add a message in message center!");
			}

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// restore source view
			String sql_drop = "drop table \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_drop);
			String sql_backup = "create table \"" + physicalViewName + "\" as select * from \"" + backup1 + "\"";
			DBQuery.updateSourceVew(sql_backup);

			writeTestResultToFile(caseID, testRst, "Job");
			writeTestResultToFile("6061", testRst2, "Job");
		}
	}

	@Test
	public void testC5830() throws Exception
	{
		String caseID = "5830";
		logger.info("====Verify unread job number will decrease 1when user click X to close the message[case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			ListPage listPage = super.m.listPage;
			int init = listPage.getNotificationNums();
			logger.info("There are " + init + " notifcation(s)");
			MessageCenter messageCenter = listPage.enterMessageCenterPage();
			messageCenter.closeFirstMessage();
			int after = listPage.getNotificationNums();
			logger.info("There are " + after + " notifcation(s)");
			if (after - init != 1)
				testRst = false;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Job");
		}
	}

	@Test
	public void testC6126() throws Exception
	{
		String caseID = "6126";
		logger.info("====Verify new message will send to Message Center when export job is failed due to Checksum changed[case id=" + caseID + "]====");
		boolean testRst = false;
		String physicalViewName = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(jobData, nodeName);
			String Regulator = elementValues.get(0);
			String Entity = elementValues.get(1);
			String Form = elementValues.get(2);
			String ReferenceDate = elementValues.get(3);
			String ExportType = elementValues.get(4);
			String Module = elementValues.get(5);
			physicalViewName = elementValues.get(6);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);

			String sql_drop = "drop table \"" + physicalViewName + "_BAK" + "\"";
			DBQuery.updateSourceVew(sql_drop);

			String sql_Create = "create table \"" + physicalViewName + "_BAK" + "\" as select * from \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_Create);

			listPage.deleteFormInstance(Form, ReferenceDate);
			logger.info("Begin set retrieve properties");
			int init = listPage.getNotificationNums();
			logger.info("There are " + init + " notifcation(s)");
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(Entity);
			retrievePage.setReferenceDate(ReferenceDate);
			retrievePage.setForm(Form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				listPage.closeJobDialog();

				// approve this form
				FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
				formInstancePage.clickReadyForApprove();
				listPage = formInstancePage.closeFormInstance();
				HomePage homePage = listPage.approveReturn(listPage, Regulator, Entity, Form, ReferenceDate);
				homePage.logon();
				listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);

				String sql_update = "UPDATE \"" + physicalViewName + "\" SET \"S_N_11\"= \"S_N_11\"+1";
				DBQuery.updateSourceVew(sql_update);
				// export ds return
				formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
				formInstancePage.exportDataSchduleReturn(ExportType, Module, null);
				listPage = formInstancePage.closeFormInstance();
				JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
				List<String> jobDetail = jobManagerPage.getJobInfo(1);
				if (jobDetail.get(10).equalsIgnoreCase("Export checksum is not equal to Retrieve checksum."))
					testRst = true;
				jobManagerPage.backToDashboard();

			}

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			String sql_drop = "drop table \"" + physicalViewName + "\"";
			DBQuery.updateSourceVew(sql_drop);

			String sql_Create = "create table \"" + physicalViewName + "\" as select * from \"" + physicalViewName + "_BAK" + "\"";
			DBQuery.updateSourceVew(sql_Create);

			writeTestResultToFile(caseID, testRst, "Job");
		}
	}
}
