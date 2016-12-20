package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.testng.annotations.Test;

import com.lombardrisk.pages.EditionManagePage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 9/18/2015
 */
public class EditionManage extends TestTemplate
{

	@Test
	public void test4042() throws Exception
	{
		boolean testRst = false;
		String caseID = "4042";
		logger.info("====Verify new edition will generate when create return for an existing return,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			int initAmt = editionPage.getEditionAmt();
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, ReferenceDate, false, false);
			formInstancePage.closeFormInstance();
			rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			editionPage = listPage.openEditionManage(rowIndex);
			int afterAmt = editionPage.getEditionAmt();
			assertThat(1).as("No edition was generated after create new return").isEqualTo(afterAmt - initAmt);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4043() throws Exception
	{
		boolean testRst = false;
		String caseID = "4043";
		logger.info("====Verify new edition will generate when create return from EXCEL for an existing return,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");
			String fileName = getElementValueFromXML(testData_edition, nodeName, "ImportFile");
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			int initAmt = editionPage.getEditionAmt();
			File importFile = new File(testData_edition.replace("Edition.xml", fileName));
			listPage.createFormFromExcel(importFile, false, false, false);
			rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			editionPage = listPage.openEditionManage(rowIndex);
			int afterAmt = editionPage.getEditionAmt();
			assertThat(1).as("No edition was generated after create return from excel").isEqualTo(afterAmt - initAmt);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4044() throws Exception
	{
		boolean testRst = false;
		String caseID = "4044";
		logger.info("====Verify REPORT STATUS column of EDITION MANAGER window is editable,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, ReferenceDate, false, false);
			formInstancePage.closeFormInstance();
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			assertThat(true).as("REPORT STATUS should be editable").isEqualTo(editionPage.isStatusEditable(1));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4045() throws Exception
	{
		boolean testRst = false;
		String caseID = "4045";
		logger.info("====Verify the dormant edition can be deleted in Edition Manager window with warning message,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			String editionInfo = editionPage.getFirstDormantEdition();
			String creationDate = editionInfo.split("#")[0];

			listPage.openEditionManage(rowIndex);
			editionPage.deleteEdition(creationDate);

			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			assertThat(false).as("Deleted edtions still in editions droplist").isEqualTo(formInstancePage.isEditionExist(editionInfo.replace("#", " #")));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4046() throws Exception
	{
		boolean testRst = false;
		String caseID = "4046";
		logger.info("====Verify the dormant edition can be deleted in Edition Manager window with warning message-return page,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			EditionManagePage editionPage = formInstancePage.openEditionManage();
			String editionInfo = editionPage.getFirstDormantEdition();
			String creationDate = editionInfo.split("#")[0];

			formInstancePage.openEditionManage();
			editionPage.deleteEdition(creationDate);

			assertThat(false).as("Deleted edtions still in editions droplist").isEqualTo(formInstancePage.isEditionExist(editionInfo.replace("#", " #")));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4303() throws Exception
	{
		boolean testRst = false;
		String caseID = "4303";
		logger.info("====Verify the active edition can be deleted in Edition Manager window with warning message -dashboard,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.closeFormInstance();

			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			String editionInfo = editionPage.getFirstActiveEdition();
			String creationDate = editionInfo.split("#")[0];

			listPage.openEditionManage(rowIndex);
			editionPage.deleteEdition(creationDate);

			formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			assertThat(false).as("Deleted edtions still in editions droplist").isEqualTo(formInstancePage.isEditionExist(editionInfo.replace("#", " #")));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4048() throws Exception
	{
		boolean testRst = false;
		String caseID = "4048";
		logger.info("====Verify the active edition can be deleted in Edition Manager window with warning message-return viewer page,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			EditionManagePage editionPage = formInstancePage.openEditionManage();
			String editionInfo = editionPage.getFirstActiveEdition();
			String creationDate = editionInfo.split("#")[0];

			formInstancePage.openEditionManage();
			editionPage.deleteEdition(creationDate);

			assertThat(false).as("Deleted edtions still in editions droplist").isEqualTo(formInstancePage.isEditionExist(editionInfo.replace("#", " #")));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4050() throws Exception
	{
		boolean testRst = false;
		String caseID = "4303";
		logger.info("====Verify Verify ACTIVE edition can be set to DORMANT -dashboard,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, ReferenceDate, false, false);
			formInstancePage.closeFormInstance();
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			String editionInfo = editionPage.getFirstActiveEdition();
			String creationDate = editionInfo.split("#")[0];

			listPage.openEditionManage(rowIndex);
			editionPage.deactivateForm(creationDate);

			formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			assertThat(editionInfo).as("Dormant edition failed").isNotEqualTo(formInstancePage.getCurrentEditionInfo());
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4282() throws Exception
	{
		boolean testRst = false;
		String caseID = "4282";
		logger.info("====Verify DORMANT edition can be set to ACTIVE -return viewer page,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, ReferenceDate, false, false);
			formInstancePage.closeFormInstance();
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			String editionInfo = editionPage.getFirstDormantEdition();
			String creationDate = editionInfo.split("#")[0];

			listPage.openEditionManage(rowIndex);
			editionPage.activateForm(creationDate);

			formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			assertThat(formInstancePage.getCurrentEditionInfo().trim()).as("Active edition failed").isEqualTo(editionInfo.replace("#", " #"));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4051() throws Exception
	{
		boolean testRst = false;
		String caseID = "4051";
		logger.info("====Verify DORMANT edition is READ-ONLY,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");
			String CellName = getElementValueFromXML(testData_edition, nodeName, "CellName");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, ReferenceDate, false, false);
			formInstancePage.closeFormInstance();
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			String editionInfo = editionPage.getFirstDormantEdition();
			String creationDate = editionInfo.split("#")[0];

			listPage.openEditionManage(rowIndex);
			formInstancePage = editionPage.openForm(creationDate);
			boolean t1 = formInstancePage.isAddInstanceBtnDisplayed();
			boolean t2 = formInstancePage.isImportAdjustmentEnabled();
			boolean t3 = formInstancePage.isLockUnlockDisabled();
			boolean t4 = formInstancePage.isCellEditable(CellName);
			boolean t5 = formInstancePage.isValidationNowEnable();
			boolean t6 = formInstancePage.isLiveValidationEnabled();
			boolean t7 = formInstancePage.isExportToFileEnabled();
			boolean t8 = formInstancePage.isApproveRejectDisplayed();

			assertThat(true).isEqualTo(!t1 && !t2 && t3 && !t4 && !t5 && !t6 && t7 && !t8);
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4052() throws Exception
	{
		boolean testRst = false;
		String caseID = "4052";
		logger.info("====Verify ACTIVE edition that activated from DORMANT edition can work properly,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");
			String CellName = getElementValueFromXML(testData_edition, nodeName, "CellName");
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, ReferenceDate, false, false);
			formInstancePage.closeFormInstance();
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			String editionInfo = editionPage.getFirstDormantEdition();
			String creationDate = editionInfo.split("#")[0];

			listPage.openEditionManage(rowIndex);
			editionPage.activateForm(creationDate);
			formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);

			boolean t1 = formInstancePage.isAddInstanceBtnDisplayed();
			boolean t2 = formInstancePage.isImportAdjustmentEnabled();
			boolean t3 = formInstancePage.isCellEditable(CellName);
			boolean t4 = formInstancePage.isValidationNowEnable();
			boolean t5 = formInstancePage.isLiveValidationEnabled();
			boolean t6 = formInstancePage.isReadyApproveDisplayed();

			if (t1 && t2 && t3 && t4 && t5 && t6)

				assertThat(true).isEqualTo(t1 && t2 && t3 && t4 && t5 && t6);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4053() throws Exception
	{
		boolean testRst = false;
		String caseID = "4053";
		logger.info("====Verify user can view different editions via the Editions drop-down list,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, ReferenceDate, false, false);
			formInstancePage.closeFormInstance();
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);
			String editionInfo = editionPage.getFirstDormantEdition();

			formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.selectEditionFomDropdownList(editionInfo.replace("#", " #"));

			String curEditon = formInstancePage.getCurrentEditionInfo();
			assertThat(curEditon).as(("Select edition from drop list failed")).isEqualTo(editionInfo.replace("#", " #"));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

	@Test
	public void test4287() throws Exception
	{
		boolean testRst = false;
		String caseID = "4287";
		logger.info("====Verify user cannot delete or dormant the edition in Edition Manager window when there is only one edition exists,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_edition, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_edition, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_edition, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_edition, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			listPage.deleteFormInstance(Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.closeFormInstance();
			int rowIndex = listPage.getFormInstanceRowPos(formCode, version, ReferenceDate);
			EditionManagePage editionPage = listPage.openEditionManage(rowIndex);

			assertThat(editionPage.isDeleteEditionEnabled()).isEqualTo(false);
			assertThat(editionPage.isChangeEditionStateEnabled()).isEqualTo(false);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "FormEdition");
		}
	}

}
