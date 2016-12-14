package com.lombardrisk.testCase;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.FormInstanceRetrievePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.pages.ReturnSourcePage;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;

/**
 * 
 * Create by Zhijun.Dai on July 21, 2016
 * 
 */
public class UpdateForm extends TestTemplate
{

	@Test
	public void testUpdateReturnWithRunkeyFixed() throws Exception
	{
		/**
		 * Verify Update button and Retrieve New button of Return Sources window
		 * are disabled via Retrieve Return button when there is no newer
		 * demension view
		 */
		String caseID = "4102";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Retrieve the same return again.
			FormInstanceRetrievePage retrievePageAgain = listPage.openFormInstanceRetrievePage();
			retrievePageAgain.setGroup(entity);
			retrievePageAgain.setReferenceDate(referenceDate);
			retrievePageAgain.setForm(form);
			ReturnSourcePage returnSourcePage = retrievePageAgain.clickOKWithExistentReturn();

			// Check the update button and retrieve new button is disabled.
			Assert.assertFalse(returnSourcePage.verifyUpdateButtonEnabled());
			Assert.assertFalse(returnSourcePage.verifyRetrieveNewButtonEnabled());

			returnSourcePage.closeReturnSourcePage();

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testUpdateReturnWithReturnCreated() throws Exception
	{
		/**
		 * Verify Update button of Return Sources window is disabled via
		 * Retrieve Return button when the return NOT get from retrieve
		 */
		String caseID = "4345";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");

			// Create the new return
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, form, null, false, false);

			// Click the "Return Sources" button
			ReturnSourcePage returnSourcePage = formInstancePage.enterReturnSourcePage();

			// Check the "Update" button is disabled.
			Assert.assertFalse(returnSourcePage.verifyUpdateButtonEnabled());
			returnSourcePage.closeReturnSourcePage();
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testUpdateReturnWithRunkeyChanged() throws Exception
	{
		/**
		 * Verify user can UPDATE return via Retrieve Return button
		 */
		String caseID = "4099";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String cellValueEdit = elementValues.get(9).trim();
			String cellValueNew = elementValues.get(10).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> editionInfo = formInstancePage.getEditionInfo();
			formInstancePage.selectPage(pageName);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Edit the cell value
			formInstancePage.editCellValue(cellId, cellValueEdit);
			formInstancePage.closeFormInstance();

			// Update the run_skey value and N_EOP_BAL_RCY to set the cell value
			// to 8000
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			String updateSource = "UPDATE MV_FRY9C_HI1_0318 SET N_EOP_BAL_RCY = 4000000000 WHERE N_REP_LINE_CD = 629000029 AND N_SCENARIO_CD = 100";
			DBQuery.updateSourceVew(updateRunSkeyValue);
			DBQuery.updateSourceVew(updateSource);

			// Retrieve the same return again.
			retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			ReturnSourcePage returnSourcePage = retrievePage.clickOKWithExistentReturn();
			returnSourcePage.update();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> editionInfoAgain = formInstancePage.getEditionInfo();
			formInstancePage.selectPage(pageName);
			String cellActualValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValueNew, cellValueNew);
			Assert.assertEquals(editionInfo, editionInfoAgain);

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// Revert the source data.
			String revertSource = "UPDATE MV_FRY9C_HI1_0318 SET N_EOP_BAL_RCY = 3000000000 WHERE N_REP_LINE_CD = 629000029 AND N_SCENARIO_CD = 100";
			DBQuery.updateSourceVew(revertSource);
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testRetrieveReturnWithRunkeyChangedDiscardAdj() throws Exception
	{
		/**
		 * Verify user can retreive and DISCARD ADJ via Retrieve Return button
		 * when retrun get from retrieve
		 */
		String caseID = "4100";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String cellValueEdit = elementValues.get(9).trim();
			String cellValueNew = elementValues.get(10).trim();
			String type = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Edit the cell value
			formInstancePage.editCellValue(cellId, cellValueEdit);
			formInstancePage.closeFormInstance();

			// Update the run_skey value and N_EOP_BAL_RCY to set the cell value
			// to 8000
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY =\"N_RUN_SKEY\"+1";
			String updateSource = "UPDATE MV_FRY9C_HI1_0318 SET N_EOP_BAL_RCY = 4000000000 WHERE N_REP_LINE_CD = 629000029 AND N_SCENARIO_CD = 100";
			DBQuery.updateSourceVew(updateRunSkeyValue);
			DBQuery.updateSourceVew(updateSource);

			// Retrieve the same return again.
			retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			ReturnSourcePage returnSourcePage = retrievePage.clickOKWithExistentReturn();
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			String cellActualValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValueNew, cellValueNew);

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// Revert the source data.

			String revertSource = "UPDATE MV_FRY9C_HI1_0318 SET N_EOP_BAL_RCY = 3000000000 WHERE N_REP_LINE_CD = 629000029 AND N_SCENARIO_CD = 100";
			DBQuery.updateSourceVew(revertSource);
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testRetrieveReturnWithRunkeyChangedPreserveAdj() throws Exception
	{
		/**
		 * Verify user can retrieve and PRESERVE ADJ via Retrieve Return button
		 * when return get from retrieve
		 */
		String caseID = "4101";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String cellValueEdit = elementValues.get(9).trim();
			String cellValueNew = elementValues.get(10).trim();
			String type = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> editionInfo = formInstancePage.getEditionInfo();
			formInstancePage.selectPage(pageName);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Edit the cell value
			formInstancePage.editCellValue(cellId, cellValueEdit);
			formInstancePage.closeFormInstance();

			// Update the run_skey value and N_EOP_BAL_RCY to set the cell value
			// to 8000
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			String updateSource = "UPDATE MV_FRY9C_HI1_0318 SET N_EOP_BAL_RCY = 4000000000 WHERE N_REP_LINE_CD = 629000029 AND N_SCENARIO_CD = 100";
			DBQuery.updateSourceVew(updateRunSkeyValue);
			DBQuery.updateSourceVew(updateSource);

			// Retrieve the same return again.
			retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			ReturnSourcePage returnSourcePage = retrievePage.clickOKWithExistentReturn();
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> editionInfoAgain = formInstancePage.getEditionInfo();
			formInstancePage.selectPage(pageName);
			String cellActualValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValueNew, cellValueNew);
			Assert.assertFalse(editionInfo.size() == editionInfoAgain.size());

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// Revert the source data.

			String revertSource = "UPDATE MV_FRY9C_HI1_0318 SET N_EOP_BAL_RCY = 3000000000 WHERE N_REP_LINE_CD = 629000029 AND N_SCENARIO_CD = 100";
			DBQuery.updateSourceVew(revertSource);
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testRetrieveReturnWithReturnCreatedDiscardAdj() throws Exception
	{
		/**
		 * Verify user can retreive and DISCARD ADJ via Retrieve Return button
		 * when retrun NOT get from retrieve
		 */
		String caseID = "4343";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String cellValueEdit = elementValues.get(9).trim();
			String cellValueNew = elementValues.get(10).trim();
			String type = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");

			// Create the new return
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, form, null, false, false);
			List<String> editionInfo = formInstancePage.getEditionInfo();
			formInstancePage.selectPage(pageName);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Edit the cell value
			formInstancePage.editCellValue(cellId, cellValueEdit);
			formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Retrieve the same return.
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			ReturnSourcePage returnSourcePage = retrievePage.clickOKWithExistentReturn();
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> editionInfoAgain = formInstancePage.getEditionInfo();
			formInstancePage.selectPage(pageName);
			String cellActualValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValueNew, cellValueNew);
			Assert.assertFalse(editionInfo.size() == editionInfoAgain.size());
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testRetrieveReturnWithReturnCreatedPreserveAdj() throws Exception
	{
		/**
		 * Verify user can retreive and PRESERVE ADJ via Retrieve Return button
		 * when retrun NOT get from retrieve
		 */
		String caseID = "4344";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String cellValueEdit = elementValues.get(9).trim();
			String cellValueNew = elementValues.get(10).trim();
			String type = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");

			// Create the new return
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, form, null, false, false);
			List<String> editionInfo = formInstancePage.getEditionInfo();
			formInstancePage.selectPage(pageName);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Edit the cell value
			formInstancePage.editCellValue(cellId, cellValueEdit);
			formInstancePage.closeFormInstance();

			// Retrieve the same return.
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			ReturnSourcePage returnSourcePage = retrievePage.clickOKWithExistentReturn();
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> editionInfoAgain = formInstancePage.getEditionInfo();
			formInstancePage.selectPage(pageName);
			String cellActualValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValueNew, cellValueNew);
			Assert.assertFalse(editionInfo.size() == editionInfoAgain.size());

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testUpdateReturnWithAddPageInstance() throws Exception
	{
		/**
		 * Verify user can UPDATE return when new page instance added for plain
		 * cell - dashboard
		 */
		String caseID = "4441";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String cellValueEdit = elementValues.get(9).trim();
			String cellValueNew = elementValues.get(10).trim();
			String instanceId = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return and add new page instance
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.addInstance(instanceId);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Edit the cell value
			formInstancePage.editCellValue(cellId, cellValueEdit);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.update();

			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);

			// Switch to the page instance 5
			List<String> updateEditions = formInstancePage.getEditionInfo();
			formInstancePage.selectInstance(instanceId);

			String cellActualValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValueNew, cellValueNew);
			Assert.assertTrue(initEditions.size() == updateEditions.size());

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testPreserveAdjReturnWithAddPageInstance() throws Exception
	{
		/**
		 * Verify user can PRESERVE ADJ return when new page instance added for
		 * plain cell - dashboard
		 */
		String caseID = "6452";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String cellValueEdit = elementValues.get(9).trim();
			String cellValueNew = elementValues.get(10).trim();
			String instanceId = elementValues.get(11).trim();
			String type = elementValues.get(12).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return and add new page instance
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.addInstance(instanceId);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Edit the cell value
			formInstancePage.editCellValue(cellId, cellValueEdit);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.retrieveNew(type);

			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);

			// Switch to the page instance 5
			List<String> updateEditions = formInstancePage.getEditionInfo();
			formInstancePage.selectInstance(instanceId);

			String cellActualValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValueNew, cellValueNew);
			Assert.assertFalse(initEditions.size() == updateEditions.size());
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testDiscardAdjReturnWithAddPageInstance() throws Exception
	{
		/**
		 * Verify user can DISCARD ADJ return when new page instance added for
		 * plain cell - dashboard
		 */
		String caseID = "6453";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String cellValueEdit = elementValues.get(9).trim();
			String cellValueNew = elementValues.get(10).trim();
			String instanceId = elementValues.get(11).trim();
			String type = elementValues.get(12).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return and add new page instance
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.addInstance(instanceId);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Edit the cell value
			formInstancePage.editCellValue(cellId, cellValueEdit);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.retrieveNew(type);

			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();

			// Check instance 5 does not exist.
			List<String> instances = formInstancePage.getAllInstance(pageName);
			Assert.assertFalse(initEditions.size() == updateEditions.size());
			Assert.assertFalse(instances.contains(instanceId));
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testUpdateReturnWithDeletePageInstance() throws Exception
	{
		/**
		 * Verify user can UPDATE return when page instance deleted for plain
		 * cell - dashboard
		 */
		String caseID = "4444";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Delete page instance 411
			formInstancePage.deletePageInstance(instanceId);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.update();

			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);

			// Switch to the page instance 411
			List<String> updateEditions = formInstancePage.getEditionInfo();
			formInstancePage.selectInstance(instanceId);

			String cellActualValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValueNew, cellValue);
			Assert.assertTrue(initEditions.size() == updateEditions.size());
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testPreserveAdjReturnWithDeletePageInstance() throws Exception
	{
		/**
		 * Verify user can PRESERVE ADJ return when page instance deleted for
		 * plain cell - dashboard
		 */
		String caseID = "6455";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();
			String type = elementValues.get(10).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Delete page instance 411
			formInstancePage.deletePageInstance(instanceId);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			ReturnSourcePage returnSourcePage = retrievePage.clickOKWithExistentReturn();
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertFalse(initEditions.size() == updateEditions.size());

			// Check the page instance 411 does not exist
			List<String> instances = formInstancePage.getAllInstance(pageName);
			Assert.assertFalse(instances.contains(instanceId));
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testDiscardAdjReturnWithDeletePageInstance() throws Exception
	{
		/**
		 * Verify user can DISCARD ADJ return when page instance deleted for
		 * plain cell - dashboard
		 */
		String caseID = "6454";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();
			String type = elementValues.get(10).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			String cellActualValue = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellActualValue, cellValue);

			// Delete page instance 411
			formInstancePage.deletePageInstance(instanceId);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			ReturnSourcePage returnSourcePage = retrievePage.clickOKWithExistentReturn();
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertFalse(initEditions.size() == updateEditions.size());

			// Check the page instance 411 exists and contains value.
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			String cellValueNew = formInstancePage.getCellText(cellId);
			Assert.assertEquals(cellValueNew, cellValue);
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testUpdateReturnWithAddRow() throws Exception
	{
		/**
		 * Verify user can UPDATE return when new row added - return viewer page
		 */
		String caseID = "4447";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellName = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();
			String rowNO = elementValues.get(10).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);

			// Insert new row
			String gridName = getExtendCellName(regulator, formCode, version, cellName);
			rowNO = (Integer.valueOf(rowNO) + 48) + "";
			String extCellName = null;
			if (gridName != null)
			{
				extCellName = gridName + rowNO + cellName;
			}
			formInstancePage.insertRow(extCellName);

			// Edit the cell value of the added row
			rowNO = (Integer.valueOf(rowNO) + 1) + "";
			String extNewCellName = gridName + rowNO + cellName;
			formInstancePage.editCellValue(extNewCellName, cellValue);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY14ASUMM_PPME6 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.update();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Check that no extra edition added
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertTrue(initEditions.size() == updateEditions.size());

			// Check the new grid row does not exist.
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			Assert.assertFalse(formInstancePage.isCellExist(extNewCellName));
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testPreserveAdjReturnWithAddRow() throws Exception
	{
		/**
		 * Verify user can PRESERVE ADJ return when new row added - return
		 * viewer page
		 */
		String caseID = "6457";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellName = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();
			String rowNO = elementValues.get(10).trim();
			String type = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);

			// Insert new row
			String gridName = getExtendCellName(regulator, formCode, version, cellName);
			rowNO = (Integer.valueOf(rowNO) + 48) + "";
			String extCellName = null;
			if (gridName != null)
			{
				extCellName = gridName + rowNO + cellName;
			}
			formInstancePage.insertRow(extCellName);

			// Edit the cell value of the added row
			rowNO = (Integer.valueOf(rowNO) + 1) + "";
			String extNewCellName = gridName + rowNO + cellName;
			formInstancePage.editCellValue(extNewCellName, cellValue);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY14ASUMM_PPME6 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Check that an extra edition added
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertFalse(initEditions.size() == updateEditions.size());

			// Check the new grid row exists and the cell value is same with
			// before.
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			String cellValueNew = formInstancePage.getCellText(extNewCellName);
			Assert.assertEquals(cellValueNew, cellValue);

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testDiscardAdjReturnWithAddRow() throws Exception
	{
		/**
		 * Verify user can DISCARD ADJ return when new row added - return viewer
		 * page
		 */
		String caseID = "6456";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellName = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();
			String rowNO = elementValues.get(10).trim();
			String type = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);

			// Insert new row
			String gridName = getExtendCellName(regulator, formCode, version, cellName);
			rowNO = (Integer.valueOf(rowNO) + 48) + "";
			String extCellName = null;
			if (gridName != null)
			{
				extCellName = gridName + rowNO + cellName;
			}
			formInstancePage.insertRow(extCellName);

			// Edit the cell value of the added row
			rowNO = (Integer.valueOf(rowNO) + 1) + "";
			String extNewCellName = gridName + rowNO + cellName;
			formInstancePage.editCellValue(extNewCellName, cellValue);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			formInstancePage.closeFormInstance();
			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY14ASUMM_PPME6 SET N_RUN_SKEY = N_RUN_SKEY+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Check that an extra edition added
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertFalse(initEditions.size() == updateEditions.size());

			// Check the new grid row does not exist.
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			Assert.assertFalse(formInstancePage.isCellExist(extNewCellName));

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testUpdateReturnWithDeleteRow() throws Exception
	{
		/**
		 * Verify user can UPDATE return when delete grid row - return viewer
		 * page
		 */
		String caseID = "4450";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellName = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();
			String rowNO = elementValues.get(10).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);

			// Delete the last row
			String gridName = getExtendCellName(regulator, formCode, version, cellName);
			rowNO = (Integer.valueOf(rowNO) + 48) + "";
			String extCellName = null;
			if (gridName != null)
			{
				extCellName = gridName + rowNO + cellName;
			}
			String cellValueInit = formInstancePage.getCellText(extCellName);
			Assert.assertEquals(cellValueInit, cellValue);
			formInstancePage.deleteRow(extCellName);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY14ASUMM_PPME6 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.update();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Check that no extra edition added
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertTrue(initEditions.size() == updateEditions.size());

			// Check the deleted row still exist and the value does not change.
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			String cellValueUpdate = formInstancePage.getCellText(extCellName);
			Assert.assertEquals(cellValueUpdate, cellValue);

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testPreserveAdjReturnWithDeleteRow() throws Exception
	{
		/**
		 * Verify user can PRESERVE ADJ return when delete grid row - return
		 * viewer page
		 */
		String caseID = "6459";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellName = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();
			String rowNO = elementValues.get(10).trim();
			String type = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);

			// Delete the last row
			String gridName = getExtendCellName(regulator, formCode, version, cellName);
			rowNO = (Integer.valueOf(rowNO) + 48) + "";
			String extCellName = null;
			if (gridName != null)
			{
				extCellName = gridName + rowNO + cellName;
			}
			String cellValueInit = formInstancePage.getCellText(extCellName);
			Assert.assertEquals(cellValueInit, cellValue);
			formInstancePage.deleteRow(extCellName);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY14ASUMM_PPME6 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Check that an extra edition added
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertFalse(initEditions.size() == updateEditions.size());

			// Check the deleted row does not exist
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			Assert.assertFalse(formInstancePage.isCellExist(extCellName));
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testDeleteAdjReturnWithDeleteRow() throws Exception
	{
		/**
		 * Verify user can DISCARD ADJ return when delete grid row - return
		 * viewer page
		 */
		String caseID = "6458";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellName = elementValues.get(7).trim();
			String cellValue = elementValues.get(8).trim();
			String instanceId = elementValues.get(9).trim();
			String rowNO = elementValues.get(10).trim();
			String type = elementValues.get(11).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);

			// Delete the last row
			String gridName = getExtendCellName(regulator, formCode, version, cellName);
			rowNO = (Integer.valueOf(rowNO) + 48) + "";
			String extCellName = null;
			if (gridName != null)
			{
				extCellName = gridName + rowNO + cellName;
			}
			String cellValueInit = formInstancePage.getCellText(extCellName);
			Assert.assertEquals(cellValueInit, cellValue);
			formInstancePage.deleteRow(extCellName);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY14ASUMM_PPME6 SET \"N_RUN_SKEY\"= \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Check that an extra edition added
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertFalse(initEditions.size() == updateEditions.size());

			// Check the deleted row still exist and the cell value does not
			// change
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instanceId);
			String cellValueUpdate = formInstancePage.getCellText(extCellName);
			Assert.assertEquals(cellValueUpdate, cellValue);
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testUpdateReturnWithAdjLogExist() throws Exception
	{
		/**
		 * Verify adjustment log still exists when user choose UPDATE return via
		 * Retrieve Return button
		 */
		String caseID = "4373";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String updateCellValue = elementValues.get(8).trim();
			String initCellValue = elementValues.get(9).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.editCellValue(cellId, updateCellValue);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			Assert.assertTrue(formInstancePage.checkAdjustmentLog(cellId, initCellValue, updateCellValue));
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.update();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertTrue(initEditions.size() == updateEditions.size());

			// Check the adjustment log still exists.
			Assert.assertTrue(formInstancePage.checkAdjustmentLog(cellId, initCellValue, updateCellValue));
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testDiscardAdjReturnWithAdjLogExist() throws Exception
	{
		/**
		 * Verify adjustment log NOT exists when user choose DISCARD ADJ return
		 * via Retrieve Return button
		 */
		String caseID = "6451";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String updateCellValue = elementValues.get(8).trim();
			String initCellValue = elementValues.get(9).trim();
			String type = elementValues.get(10).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.editCellValue(cellId, updateCellValue);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			Assert.assertTrue(formInstancePage.checkAdjustmentLog(cellId, initCellValue, updateCellValue));
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertFalse(initEditions.size() == updateEditions.size());

			// Check the adjustment log does not exist.
			Assert.assertFalse(formInstancePage.checkAdjustmentLog(cellId, initCellValue, updateCellValue));

		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testPreserveAdjReturnWithAdjLogExist() throws Exception
	{
		/**
		 * Verify adjustment log still exists when user choose PRESERVE ADJ
		 * return via Retrieve Return button
		 */
		String caseID = "6450";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String pageName = elementValues.get(6).trim();
			String cellId = elementValues.get(7).trim();
			String updateCellValue = elementValues.get(8).trim();
			String initCellValue = elementValues.get(9).trim();
			String type = elementValues.get(10).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.selectPage(pageName);
			formInstancePage.editCellValue(cellId, updateCellValue);

			// Get the editions
			List<String> initEditions = formInstancePage.getEditionInfo();
			Assert.assertTrue(formInstancePage.checkAdjustmentLog(cellId, initCellValue, updateCellValue));
			listPage = formInstancePage.closeFormInstance();

			// Update the run_skey value
			String updateRunSkeyValue = "UPDATE MV_FRY9C_HI1_0318 SET N_RUN_SKEY = \"N_RUN_SKEY\"+1";
			DBQuery.updateSourceVew(updateRunSkeyValue);

			// Update the return
			ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			returnSourcePage.retrieveNew(type);
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();
			listPage.clickDashboard();

			// Open the retrieved return
			formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			List<String> updateEditions = formInstancePage.getEditionInfo();
			Assert.assertFalse(initEditions.size() == updateEditions.size());

			// Check the adjustment log still exists.
			Assert.assertTrue(formInstancePage.checkAdjustmentLog(cellId, initCellValue, updateCellValue));
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}

	@Test
	public void testUpdateReturnWithlocked() throws Exception
	{
		/**
		 * Verify user cannot retrieve or upadte return when return is locked
		 */
		String caseID = "4417";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_updateForm, nodeName);
			String regulator = elementValues.get(0).trim();
			String entity = elementValues.get(1).trim();
			String form = elementValues.get(2).trim();
			String referenceDate = elementValues.get(3).trim();
			String version = elementValues.get(4).trim();
			String formCode = elementValues.get(5).trim();
			String warningMsg = elementValues.get(6).trim();

			// List the page filter by regulator and entity
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, form, "All");
			listPage.deleteFormInstance(form, referenceDate);

			// Retrieve the special return
			FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
			retrievePage.setGroup(entity);
			retrievePage.setReferenceDate(referenceDate);
			retrievePage.setForm(form);
			retrievePage.clickOK();
			if (isJobSuccessed())
			{
				logger.info("Retrieve form sucessed");
			}
			else
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			// Open the retrieved return and lock
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.lockClick();

			// Click Return Sources and check the update and Retrieve New button
			// is disabled.
			ReturnSourcePage returnSourcePage = formInstancePage.enterReturnSourcePage();
			Assert.assertEquals(returnSourcePage.getWarningMessage(), warningMsg);
			Assert.assertFalse(returnSourcePage.verifyRetrieveNewButtonEnabled());
			Assert.assertFalse(returnSourcePage.verifyUpdateButtonEnabled());
			returnSourcePage.closeReturnSourcePage();
			listPage = formInstancePage.closeFormInstance();

			// Click update button and check the update and Retrieve New button
			// is disabled.
			returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
			Assert.assertEquals(returnSourcePage.getWarningMessage(), warningMsg);
			Assert.assertFalse(returnSourcePage.verifyRetrieveNewButtonEnabled());
			Assert.assertFalse(returnSourcePage.verifyUpdateButtonEnabled());
			returnSourcePage.closeReturnSourcePage();
			listPage.clickDashboard();
		}
		catch (Exception e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "UpdateForm");
		}
	}
}
