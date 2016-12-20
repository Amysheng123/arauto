package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.lombardrisk.pages.AllocationPage;
import com.lombardrisk.pages.EntityPage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.Business;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;

/**
 * Create by Leo Tu on Nov 11, 2015
 */
public class OtherModule extends TestTemplate
{

	String testData = testDataFolderName + "/OtherModule/OtherModule.xml";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
	String curDate = sdf.format(new Date());

	@Test(priority = 1)
	public void testC4770() throws Exception
	{
		String Regulator = "US1 FED Reserve";
		String Group = "0001";
		String caseID = "4770";
		boolean testRst = true;

		logger.info("==========Test case 4770========");
		try
		{
			File importFile = new File(testDataFolderName + "/CreateForm/ImportFile/FRY14QCNTPY_v3_G0001_11112015.xlsx");
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			if (formInstancePage != null)
			{
				String pageName = "1e) Agg CVA by ratings 3 of 4";
				logger.info("Enter page " + pageName);
				formInstancePage.selectPage(pageName);
				logger.info("Get row amount in firt page");
				int rows1 = formInstancePage.getTableRowAmt("fp.extGridTab");
				int rows2 = 0;
				if (formInstancePage.nextPageEnable())
				{
					formInstancePage.clickNextPage();
					rows2 = formInstancePage.getTableRowAmt("fp.extGridTab");
					if (rows1 + rows2 != 19)
					{
						logger.error("There are" + rows1 + rows2 + " rows in this table, not 19");
						testRst = false;
					}
				}

				if (testRst)
				{
					logger.info("Begin delete last row");
					formInstancePage.deleteRow("ExtDBGrid871576FEDFRY14QCOUP026R0030C0000");
					int rows3 = formInstancePage.getTableRowAmt("fp.extGridTab");
					if (rows2 - rows3 != 1)
					{
						logger.error("Delete last row failed");
						testRst = false;
					}
				}

				if (testRst)
				{
					logger.info("Go to previous page");
					formInstancePage.clickPreviousPage();
					logger.info("Begin delete row 6");
					formInstancePage.deleteRow("ExtDBGrid8754FEDFRY14QCOUP026R0030C0000");
					rows1 = formInstancePage.getTableRowAmt("fp.extGridTab");
					formInstancePage.clickNextPage();
					Thread.sleep(2000);
					rows2 = formInstancePage.getTableRowAmt("fp.extGridTab");

					if (rows1 + rows2 != 17)
					{
						logger.error("Delete row 6 failed");
						testRst = false;
					}
					formInstancePage.clickPreviousPage();
					if (formInstancePage.getCellText("ExtDBGrid8754FEDFRY14QCOUP026R0030C0000") == null)
					{
						String id = formInstancePage.getCellText("ExtDBGrid8755FEDFRY14QCOUP026R0030C0000");
						if (!id.equals("6"))
						{
							logger.error("Index id is incorrect");
							testRst = false;
						}
					}
					else
					{
						String id = formInstancePage.getCellText("ExtDBGrid8754FEDFRY14QCOUP026R0030C0000");
						if (!id.equals("6"))
						{
							logger.error("Index id is incorrect");
							testRst = false;
						}
					}

				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 2)
	public void testC4769() throws Exception
	{
		String Regulator = "US1 FED Reserve";
		String Group = "0001";
		String caseID = "4769";
		String Form = "FRY14QCNTPY v3";
		String ProcessDate = "11/11/2015";
		// String formCode = splitReturn(Form).get(0);
		// String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);

		String caseFolder = "C" + caseID;
		File folder = new File(testDataFolderName + "/OtherModule/" + caseFolder);
		if (folder.isDirectory())
		{
			File destFolder = new File("target/TestResult/" + curDate + "/OtherModule/" + caseFolder);
			if (destFolder.exists())
			{
				FileUtils.deleteDirectory(destFolder);
			}
			FileUtils.copyDirectory(folder, destFolder);
		}
		boolean testRst = true;

		logger.info("==========Test case 4769========");
		try
		{
			File file = new File("target/TestResult/" + curDate + "/OtherModule/" + caseFolder + "/FRY14QCNTPY_v3_ExpectedValue_CheckExcel.xlsx");
			FileUtils.copyFile(new File(testDataFolderName + "/OtherModule/C4769/FRY14QCNTPY_v3_ExpectedValue_CheckExcel.xlsx"), file);
			File importFile = new File(testDataFolderName + "/CreateForm/ImportFile/FRY14QCNTPY_v3_G0001_11112015.xlsx");
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			if (formInstancePage != null)
			{
				String exportFilePath = formInstancePage.exportToFile(Group, Form, ProcessDate, "excel", null, null);

				logger.info("Begin check cell value in exported file");

				testRst = Business.verifyExportedFile(file.getAbsolutePath(), exportFilePath, "excel");
				if (!testRst)
				{
					copyFailedFileToTestRst(exportFilePath, "OtherModule");
				}
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}

	}

	@Test(priority = 3)
	public void testC4657() throws Exception
	{

		String Regulator = "US1 FED Reserve";
		String Group = "0001";
		String caseID = "4657";
		String Form = "FFIEC002 v1";
		String ProcessDate = "30/11/2015";
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);
		boolean testRst = true;

		logger.info("==========Test case 4657========");
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			Thread.sleep(4000);
			String cellName = "LSBranch";
			logger.info("Click checkbox LSBranch ");
			formInstancePage.editCellValue(Regulator, formCode, version, null, cellName, null, true);

			logger.info("Begin check cell value in DB");
			String instance = formInstancePage.getCurrentPageInstance();
			String cellVlaue = DBQuery.getCellValeFromDB(Regulator, formCode, version, ProcessDate, Group, instance, cellName, false, 0);
			testRst = compareTwoValue(cellVlaue, "1");
			if (!testRst)
				logger.error("The value is " + cellVlaue + ", not equal 1");

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 4)
	public void testC4708() throws Exception
	{

		String Regulator = "Reserve Bank of India For Auto";
		String Group = "2999";
		String caseID = "4708";
		String Form = "LR v2";
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);
		String ProcessDate = "30/11/2015";

		boolean testRst = true;
		logger.info("==========Test case 4708========");
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);

			String pageName = "LRPartA2_P5";
			logger.info("Enter page " + pageName);
			formInstancePage.selectPage(pageName);
			Thread.sleep(2000);
			logger.info("Inset row...");
			try
			{
				formInstancePage.insertRow("RBIDBS8V2P005GRDRBIDBS8P005INDEX");
			}
			catch (Exception e)
			{
				formInstancePage.insertRowAbove("RBIDBS8V2P005GRD49RBIDBS8P005INDEX");
			}

			ArrayList<String> cellNames = new ArrayList<String>();
			cellNames.add("RBIDBS8P005R0040C0030");
			cellNames.add("RBIDBS8P005R0040C0040");
			cellNames.add("RBIDBS8V2P005GRD49RBIDBS8P005C0030");
			cellNames.add("RBIDBS8V2P005GRD49RBIDBS8P005C0040");

			ArrayList<String> cellValues = new ArrayList<String>();
			cellValues.add("123456");
			cellValues.add("1234567");
			cellValues.add("123456");
			cellValues.add("1234567");

			ArrayList<String> expectedRsts = new ArrayList<String>();
			expectedRsts.add("1,23,456");
			expectedRsts.add("12,34,567");
			expectedRsts.add("1,23,456");
			expectedRsts.add("12,34,567");

			ArrayList<String> expectedDBRsts = new ArrayList<String>();
			expectedDBRsts.add("12345600000");
			expectedDBRsts.add("1234567");
			expectedDBRsts.add("12345600000");
			expectedDBRsts.add("1234567");
			for (int i = 0; i < cellNames.size(); i++)
			{
				String cellName = cellNames.get(i);
				String cellValue = cellValues.get(i);
				String expectRst = expectedRsts.get(i);
				logger.info("Edit cell  " + cellName + "=" + cellValue);
				formInstancePage.editCellValue(cellName, cellValue);
				logger.info("Get cell value from return");
				String actualValue = formInstancePage.getCellText(cellName);
				logger.info("Actual value is:" + actualValue);
				testRst = compareTwoValue(actualValue, expectRst);
				if (!testRst)
					logger.error("Expected value is: " + expectRst + " ,  but actual value is:" + actualValue);

				logger.info("Get cell value from DB");
				boolean extendCell = false;
				if (cellName.contains("GRD"))
				{
					extendCell = true;
					cellName = cellName.replace("RBIDBS8V2P005GRD49", "");
				}
				String instance = formInstancePage.getCurrentPageInstance();
				String cellVlaueDB = DBQuery.getCellValeFromDB(Regulator, formCode, version, ProcessDate, Group, instance, cellName, extendCell, 0);
				logger.info("DB value is:" + cellVlaueDB);
				testRst = compareTwoValue(cellVlaueDB, expectedDBRsts.get(i));
				if (!testRst)
					logger.error("Expected value is: " + expectedDBRsts.get(i) + " ,  but actual value is:" + cellVlaueDB);

				Thread.sleep(1000);
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	// @Test(priority = 5)
	public void testC4812() throws Exception
	{

		String Regulator = "Test Product";
		String Group = "0001";
		String caseID = "4812";
		String Form = "FI01 v1";
		String ProcessDate = "01/07/2015";

		boolean testRst = true;
		logger.info("==========Test case F11100========");
		try
		{
			String SQL = "select * from \"FIN_FORM_INSTANCE\" WHERE \"CONFIG_PREFIX\"='TESTPRODUCT' AND \"FORM_CODE\"='FI01' AND \"EDITION_STATUS\"='ACTIVE' for update";
			DBQuery.update(SQL);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);
			listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			long statTime = System.currentTimeMillis();
			boolean loading = true;
			while (loading)
			{
				long curTime = System.currentTimeMillis();
				if ((curTime - statTime) / 1000 > 10)
				{
					if (listPage.getPopDialogTitle().equals("Loading..."))
					{
						testRst = false;
						loading = false;
					}
				}
			}

			SQL = "rollback";
			DBQuery.update(SQL);

			closeFormInstance();
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}

	}

	@Test(priority = 6)
	public void testC5096() throws Exception
	{

		String Regulator = "US1 FED Reserve";
		String Group = "2999";
		String caseID = "5096";
		String Form = "FRY14ASUMM v1";
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);
		String ProcessDate = "31/12/2015";
		String page = "PPNR NII 5 of 5";

		String cellName = "ExtDBGrid4149FEDFRY14ASUMP085R0830C0100";
		String editValue = "5";

		// String cell = "FEDFRY14ASUMP076R0260C0010";
		boolean testRst = true;
		logger.info("==========Test case C" + caseID + "========");
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.selectPage(page);
			try
			{
				formInstancePage.editCellValue(cellName, editValue);
			}
			catch (Exception e)
			{
				formInstancePage.insertRow("FEDFRY14ASUMP085R0830C0100");
				formInstancePage.editCellValue(cellName, editValue);
			}
			String FilePath = formInstancePage.exportToFile(Group, Form, ProcessDate, "Excel", null, null);
			String accValue = ExcelUtil.getCellValueByCellName(new File(FilePath), "FEDFRY14ASUMP085R0830C0100", "1", "1").trim();
			logger.info("Actual value is:" + accValue);
			testRst = compareTwoValue(editValue, accValue);
			if (!testRst)
				logger.error("Expected value is: " + editValue + " ,  but actual value is:" + accValue);

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 8)
	public void testC5099() throws Exception
	{

		String Regulator = "US1 FED Reserve";
		String Group = "0001";
		String caseID = "5099";
		String Form = "FFIEC041 v3";
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);
		String ProcessDate = "31/12/2015";
		String page1 = "Cover";

		String cellName1 = "ASOFDATE";
		String editValue = "31/12/2015";
		String cellName2 = "FEDFFIEC041P05DATE";
		String page2 = "Sch RI (1 of 4)";
		boolean testRst = true;
		logger.info("==========Test case C" + caseID + "========");
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			formInstancePage.selectPage(page1);
			formInstancePage.editCellValue(cellName1, editValue);
			AllocationPage allocationPage = formInstancePage.cellDoubleClick("RCON9999");
			/*
			 * if(!allocationPage.getSubItemLinkText(1).equals(cellName1)) {
			 * testRst=false; }
			 */
			formInstancePage.selectPage(page2);
			String cellText = formInstancePage.getCellText(cellName2);
			if (!cellText.equals("1/1/2015--31/12/2015"))
			{
				testRst = false;
			}

			allocationPage = formInstancePage.cellDoubleClick(cellName2);
			if (!allocationPage.getSumCellValue().equals("1/1/2015--31/12/2015"))
			{
				testRst = false;
			}
			if (!allocationPage.getSumRule().equals("IF(ASOFDATE=\"\",\"\",\"1/1/\"+YEAR(ASOFDATE)+\"--\"+DAYOFMONTH(ASOFDATE)+\"/\"+MONTH(ASOFDATE)+\"/\"+YEAR(ASOFDATE))"))
			{
				testRst = false;
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 9)
	public void testC5100() throws Exception
	{

		String Regulator = "US1 FED Reserve";
		String Group = "0003";
		String caseID = "5100";
		String Form = "FRY9C v2";
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);
		String ProcessDate = "30/06/2015";

		String cellName1 = "AdvAppro";
		String cellName2 = "BHCKM746";
		// String page1="Wkst";
		String page2 = "Sch HI-C";
		List<String> cells = new ArrayList<String>();
		cells.add("TotalAssetSize");
		cells.add("BHCKM733");
		cells.add("BHCKM721");
		cells.add("BHCKM708");
		cells.add("BHCKM739");
		cells.add("BHCKM727");
		cells.add("BHCKM714");

		boolean testRst = true;
		logger.info("==========Test case C" + caseID + "========");
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			String curValue = formInstancePage.getCellText(cellName1);
			if (curValue.equals("1"))
				formInstancePage.editCellValue(cellName1, "0");
			else
				formInstancePage.editCellValue(cellName1, "1");

			formInstancePage.selectPage(page2);
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName2);

			for (String cell : cells)
			{
				String cellValue = null;
				if (cell.equals("TotalAssetSize"))
					cellValue = "4560000000";
				else
					cellValue = cell.replace("BHCKM", "123");
				if (!allocationPage.isSubItemValueExist(cell, cellValue))
				{
					testRst = false;
				}
			}

			formInstancePage.closeFormInstance();

			ProcessDate = "31/12/2016";
			listPage.setProcessDate(ProcessDate);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			curValue = formInstancePage.getCellText(cellName1);
			if (curValue.equals("1"))
				formInstancePage.editCellValue(cellName1, "0");
			else
				formInstancePage.editCellValue(cellName1, "1");

			formInstancePage.selectPage(page2);
			allocationPage = formInstancePage.cellDoubleClick(cellName2);

			for (String cell : cells)
			{
				String cellValue = null;
				if (cell.equals("TotalAssetSize"))
					cellValue = "4560000000";
				else
					cellValue = cell.replace("BHCKM", "123");
				if (!allocationPage.isSubItemValueExist(cell, cellValue))
				{
					testRst = false;
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 10)
	public void testC5495() throws Exception
	{
		String Regulator = "Reserve Bank of India For Auto";
		String Group = "0001";
		String caseID = "5495";
		String page1 = "SectionA_P2";
		String page2 = "GeneralInformation_P1";

		String cellName1 = "RBIDBS3RORP002R0020C0030SUM";
		String cellName2 = "RBIDBS3RORVALSTATUS";
		String cellValue1 = "220,569,301,500,000.00";
		String cellValue2 = "Un-Validated";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);
			File importFile = new File(testDataFolderName + "/OtherModule/C5495/DSB3ROR_v1_0001_01032015.xlsx");
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			formInstancePage.selectPage(page1);
			String actuallValue = formInstancePage.getCellText(cellName1);
			assertThat(actuallValue).isEqualTo(cellValue1);

			formInstancePage.selectPage(page2);
			formInstancePage.editCellValue(cellName2, cellValue2);
			formInstancePage.selectPage(page1);
			actuallValue = formInstancePage.getCellText(cellName1);
			assertThat(actuallValue).isEqualTo(cellValue2);

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
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 11)
	public void testC6178() throws Exception
	{
		String caseID = "6178";
		String Regulator = "European Common Reporting XBRL";
		String Group = "2999";
		String Form = "C107 v1";
		String ProcessDate = "31/10/2016";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			String exportFile = formInstancePage.exportToFile(Group, Form, ProcessDate, "excel", null, null);
			if (exportFile != null)
			{
				testRst = formInstancePage.importAdjustment(new File(exportFile), false, false);
			}
			if (!testRst)
				logger.error("Import adjustment from exported file failed");
			assertThat(testRst).isEqualTo(true);
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
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 12)
	public void testC6133() throws Exception
	{
		String caseID = "6133";
		String Regulator = "US1 FED Reserve";
		String Group = "0001";
		String Form = "CREII v1";
		String ProcessDate = "01/07/2016";
		boolean testRst = false;
		// String formCode = splitReturn(Form).get(0);
		logger.info("==========Test case C" + caseID + "========");
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			String exportFile = formInstancePage.exportToFile(Group, Form, ProcessDate, "excel", null, null);
			assertThat(exportFile).isEqualTo("error");
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
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 13)
	public void testC6361() throws Exception
	{
		String caseID = "6361";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");
		ListPage listPage = super.m.listPage;
		String nodeName = "C" + caseID;
		List<String> elementValues = getElementValueFromXML(testData_OtherModule, nodeName);
		String Regulator = elementValues.get(0);
		String Entity = elementValues.get(1);
		String NewEntityName = elementValues.get(2);
		String Form = elementValues.get(3);
		String ReferenceDate = elementValues.get(4);
		try
		{
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage.editEntity(Entity, NewEntityName, null, null);
			entityManagePage.backToDashboard();

			listPage.getProductListPage(Regulator, NewEntityName, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			String exportFile = formInstancePage.exportToFile(NewEntityName, Form, ReferenceDate, "xbrl", null, null);
			assertThat(exportFile).isNotEqualTo("error");
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
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage.editEntity(NewEntityName, Entity, null, null);
			entityManagePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 14)
	public void testC4371() throws Exception
	{
		String caseID = "4371";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");

		String nodeName = "C" + caseID;
		List<String> elementValues = getElementValueFromXML(testData_OtherModule, nodeName);
		String Regulator = elementValues.get(0);
		String Entity = elementValues.get(1);
		String Form = elementValues.get(2);
		String ReferenceDate = elementValues.get(3);
		String Page = elementValues.get(4);
		String Insert = elementValues.get(5);
		String Cell = elementValues.get(6);
		String Formats = elementValues.get(7);
		String Expecteds = elementValues.get(8);

		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.selectPage(Page);
			formInstancePage.insertRow(Insert);
			formInstancePage.editCellValue(Cell, "30/09/2016");
			formInstancePage.closeFormInstance();
			listPage.setForm(Form);
			listPage.setProcessDate(ReferenceDate);

			for (int i = 0; i < Formats.split("#").length; i++)
			{
				m.listPage.enterPreferencePage();
				m.preferencePage.selectLanguageByValue(Formats.split("#")[i]);

				formInstancePage = listPage.openFirstFormInstance();
				formInstancePage.selectPage(Page);
				String actualValue = formInstancePage.getCellText(Cell);
				formInstancePage.closeFormInstance();
				assertThat(actualValue).isEqualTo(Expecteds.split("#")[i]);
			}

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
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 15)
	public void testC6692() throws Exception
	{
		String caseID = "6692";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");

		String nodeName = "C" + caseID;
		List<String> elementValues = getElementValueFromXML(testData_OtherModule, nodeName);
		String Regulator = elementValues.get(0);
		String Entity = elementValues.get(1);
		String Form = elementValues.get(2);
		String ReferenceDate = elementValues.get(3);
		String Page = elementValues.get(4);
		String Cell = elementValues.get(5);
		String EditValue = elementValues.get(6);
		String ExpectedValue = elementValues.get(7);
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.selectPage(Page);
			formInstancePage.editCellValue(Cell, EditValue);
			String actualValue = formInstancePage.getCellText(Cell);
			assertThat(actualValue).isEqualTo(ExpectedValue);

			actualValue = formInstancePage.getCellTextInEdit(Cell);
			assertThat(actualValue).isEqualTo(EditValue);
			testRst = true;

		}
		catch (Exception e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test(priority = 16)
	public void testC6693() throws Exception
	{
		String caseID = "6693";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");

		String nodeName = "C" + caseID;
		List<String> elementValues = getElementValueFromXML(testData_OtherModule, nodeName);
		String Regulator = elementValues.get(0);
		String Entity = elementValues.get(1);
		String Form = elementValues.get(2);
		String ReferenceDate = elementValues.get(3);
		String Page = elementValues.get(4);
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);

			for (String pageName : Page.split("#"))
			{
				formInstancePage.selectPage(pageName);
				String currentPage = formInstancePage.getCurrentPage();
				assertThat(currentPage).isEqualTo(pageName);
			}

			testRst = true;

		}
		catch (Exception e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	// @Test
	public void testC6115() throws Exception
	{
		String caseID = "6115";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");

		String nodeName = "C" + caseID;
		List<String> elementValues = getElementValueFromXML(testData_OtherModule, nodeName);
		String Regulator = elementValues.get(0);
		String Entity = elementValues.get(1);
		// String Form = elementValues.get(2);
		String ReturnId = elementValues.get(3);
		String CreateFile = elementValues.get(4);
		String ConsolidationType = elementValues.get(5);
		String Cell = elementValues.get(6);
		String Scale = elementValues.get(7);
		String Baseline = elementValues.get(8);

		String caseFolder = "C" + caseID;
		String path = testDataFolderName + "/OtherModule/" + caseFolder + "/";

		String prefix = "ECR";
		String SQL = "SELECT \"ID\" FROM \"USR_NATIVE_ENTITY\" WHERE \"ENTITY_NAME\"='" + Entity + "'";
		String entityId = DBQuery.queryRecord(SQL);

		if (ConsolidationType.equalsIgnoreCase("p"))
		{
			logger.info("Set type= Consolidated");
			SQL = "UPDATE \"USR_VARIABLE_VALUE\" SET \"CHAR_VALUE\"='P' WHERE \"ENTITY_ID\"='" + entityId + "' and \"VARIABLE_LABEL\"='Scope of consolidation'  and \"PRODUCT_PREFIX\"='" + prefix
					+ "'";
			DBQuery.update(SQL);
		}
		else if (ConsolidationType.equalsIgnoreCase("I"))
		{
			logger.info("Set type= Individual");
			SQL = "UPDATE \"USR_VARIABLE_VALUE\" SET \"CHAR_VALUE\"='I' WHERE \"ENTITY_ID\"='" + entityId + "' and \"VARIABLE_LABEL\"='Scope of consolidation'  and \"PRODUCT_PREFIX\"='" + prefix
					+ "'";
			DBQuery.update(SQL);
		}

		String start = getRegulatorIDRangeStart(Regulator);
		String end = getRegulatorIDRangEnd(Regulator);
		SQL = "UPDATE \"CFG_RPT_Ref\" SET \"NUMERIC_SCALE\"=" + Scale + " WHERE \"ID\" BETWEEN " + start + " AND " + end + " AND \"ReturnId\"=" + ReturnId + " AND \"Item\"=\'" + Cell + "\'";
		DBQuery.update(SQL);

		try
		{

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			File importFile = new File(path + CreateFile);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			String exportFilePath = formInstancePage.exportToFile(null, null, null, "xbrl", null, "NONE");

			boolean rst = Business.verifyExportedFile(path + Baseline, exportFilePath, "xbrl");

			assertThat(rst).as("Compare xbrl result should be True").isEqualTo(true);
			testRst = true;

		}
		catch (Exception e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			// SQL = "UPDATE \"CFG_RPT_Ref\" SET \"NUMERIC_SCALE\"=" + Scale +
			// " WHERE \"ID\" BETWEEN " + start + " AND " + end +
			// " AND \"ReturnId\"=" + ReturnId + " AND \"Item\"=\'" + Cell +
			// "\'";
			// DBQuery.update(SQL);
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	public void startService(String installPath, boolean reStart) throws Exception
	{
		if (reStart)
			stopService(installPath);
		File File = new File(installPath + "/bin/run.lock");
		if (!File.exists())
		{
			logger.info("Start ar service");
			String cmd = installPath + "/bin/start.bat ";
			Runtime.getRuntime().exec("cmd.exe /C start " + cmd);
			Thread.sleep(1000 * 60);
		}
	}

	public void stopService(String installPath) throws Exception
	{
		File File = new File(installPath + "/bin/run.lock");
		if (File.exists())
		{
			logger.info("Stop ar service");
			String cmd = installPath + "/bin/stop.bat ";
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(1000 * 15);
		}
	}

	@Test
	public void testC4862() throws Exception
	{
		String caseID = "4862";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");

		String nodeName = "C" + caseID;
		List<String> elementValues = getElementValueFromXML(testData_OtherModule, nodeName);
		String Regulator = elementValues.get(0);
		String Entity = elementValues.get(1);
		String Form = elementValues.get(2);
		String ReferenceDate = elementValues.get(3);
		String Module = elementValues.get(4);
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();

			String exportFilePath = formInstancePage.exportToFile(Entity, Form, ReferenceDate, "text", Module, null);
			assertThat(exportFilePath).isEqualTo("Error");
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

	@Test
	public void testC4863() throws Exception
	{
		String caseID = "4863";
		boolean testRst = false;
		logger.info("==========Test case C" + caseID + "========");

		String nodeName = "C" + caseID;
		List<String> elementValues = getElementValueFromXML(testData_OtherModule, nodeName);
		String Regulator = elementValues.get(0);
		String Entity = elementValues.get(1);
		String Form = elementValues.get(2);
		String ReferenceDate = elementValues.get(3);
		String Module = elementValues.get(4);
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			String exportFilePath = listPage.ExportToRegulatorFormat(Entity, Form, ReferenceDate, "text", null, null, Module, null);
			assertThat(exportFilePath).isEqualTo("Error");
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "OtherModule");
		}
	}

}
