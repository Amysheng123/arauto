package com.lombardrisk.testCase;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ImportConfirmPage;
import com.lombardrisk.pages.ListImportFilePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on Jun 18, 2015
 */
public class LockUnlockForm extends TestTemplate
{

	@Test(priority = 1)
	public void testLockFormInList() throws Exception
	{
		String Regulator = "Test Product";
		String Group = "0001";
		String Form = "CREI v3";
		String ProcessDate = "01/07/2015";

		boolean testRst = false;
		String caseID = "4520";
		try
		{
			logger.info("============testLockUnlockInList=============");
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			logger.info("Check form status");
			String fileName = "FR2052B_v1_0004_20150930_Import.xlsx";
			File importFile = new File(testDataFolderName + "/CreateForm/Import/" + fileName);
			if (listPage.isFormLockedInList(Form, ProcessDate))
			{
				logger.info("Form status is Invalid");
				logger.info("Begin verify if could import adjustment");
				ListImportFilePage listImportFilePage = listPage.openImportAdjustmentPage();

				String type = "listImportFileForm";
				listImportFilePage.setImportFile(importFile, type);
				ImportConfirmPage confirmPage = listImportFilePage.importFileBtnClick(type);
				logger.info("Check if ger error when import excel");
				if (confirmPage.isVisible("icp.error"))
				{
					logger.info("Error message is: " + confirmPage.getText("icp.error"));
				}

				Assert.assertEquals(confirmPage.isVisible("icp.error"), true);
				logger.info("testLockUnlockInList: successes");

			}
			else
			{
				logger.info("Form status is Open for edit, now open the form to lock it");
				FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
				logger.info("Open form " + Form);
				formInstancePage.lockClick();
				logger.info("Lock form " + Form);
				formInstancePage.closeFormInstance();
				logger.info("Close form " + Form + ", back to form list page");

				listPage.setRegulator(Regulator);
				listPage.setGroup(Group);
				listPage.setForm(Form);
				listPage.setProcessDate(ProcessDate);

				if (listPage.isFormLockedInList(Form, ProcessDate))
				{
					logger.info("Now form status is Invalid");
					logger.info("Begin verfy if could importadjustment");
					ListImportFilePage listImportFilePage = listPage.openImportAdjustmentPage();

					listImportFilePage.setImportFile(importFile, "Import");
					ImportConfirmPage confirmPage = listImportFilePage.importFileBtnClick("Import");
					logger.info("Check if ger error when import excel");
					if (confirmPage.isVisible("icp.error"))
					{
						logger.info("Error message is: " + confirmPage.getText("icp.error"));
						testRst = true;
					}

					if (testRst)
					{
						logger.info("testLockFormInList  : successed");
					}
					else
					{
						logger.info("testLockFormInList  : successed");
					}

				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			logger.error("testLockFormInList:", e);
			// snapshot( "testLockFormInList.png");

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "LockUnlock");
		}
	}

	@Test(priority = 2)
	public void testLockFormInReturn() throws Exception
	{
		String Regulator = "Test Product";
		String Group = "0001";
		String Form = "CREI v3";
		String ProcessDate = "01/07/2015";
		String EditCell = "CREIR090C020";

		boolean testRst = false;
		String caseID = "4521";
		try
		{
			logger.info("============testLockUnlockInReturn=============");
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			logger.info("Check form status");
			if (formInstancePage.isLockedInReturn())
			{
				logger.info("Form status: Lock");
				formInstancePage.getImportPageInReturn();

				logger.info("Check if pop up import window");
				Assert.assertEquals(formInstancePage.isVisible("fp.importDlg"), false);
				logger.info("Import window does not pop up");
				logger.info("Check if cell is editable");
				Assert.assertEquals(formInstancePage.isCellEditable(EditCell), false);
				logger.info("Cells cannot be edited");
				logger.info("testLockUnlockInReturn: successed");
			}
			else
			{
				logger.info("Form status:Unlock");
				formInstancePage.lockClick();
				if (formInstancePage.isLockedInReturn())
				{
					logger.info("Form status: Lock");
					formInstancePage.getImportPageInReturn();

					logger.info("Check if pop up import window");
					Assert.assertEquals(formInstancePage.isVisible("fp.importDlg"), false);
					logger.info("Import window does not pop up");
					logger.info("Check if cell is editable");

					if (!formInstancePage.isCellEditable(EditCell))
					{
						logger.info("Cells cannot be edited");
						testRst = true;
					}
					else
					{
						logger.info("Cells could be edited");
						testRst = false;
					}

					formInstancePage.closeFormInstance();
					if (testRst)
					{
						logger.info("testLockUnlockInReturn  : successed");
					}
					else
					{
						logger.info("testLockUnlockInReturn  : successed");
					}
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			logger.error("testLockUnlockInReturn:", e);
			// snapshot( "testLockUnlockInReturn.png");

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "LockUnlock");
		}

	}

}
