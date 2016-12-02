package com.lombardrisk.testCase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.FormInstanceRetrievePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.Business;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.TxtUtil;

/**
 * Create by Leo Tu on 9/7/2016
 */

public class RowLimit extends TestTemplate
{

	String parentPath = new File(System.getProperty("user.dir")).getParent().toString();
	String importFolder = System.getProperty("user.dir") + "\\data_ar\\RowLimit\\ImportFile\\";

	@Test
	public void test6304() throws Exception
	{
		String caseID = "6304";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Step4_1 = testdata.get(3);
			String Step4_2 = testdata.get(4);
			String Step5 = testdata.get(5);
			String Step6_1 = testdata.get(6);
			String Step6_2 = testdata.get(7);

			File importFile = new File(importFolder + "\\" + FileName);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			boolean s1 = false;
			String page = Step4_1.split("#")[0].replace("~", "&");
			String instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			int rowLimit = Integer.parseInt(Step4_1.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s1 = true;

			boolean s2 = false;
			page = Step4_2.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step4_2.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s2 = true;

			boolean s3 = false;
			page = Step5.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step5.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s3 = true;

			String editCell = Step6_1.split("#")[0];
			page = editCell.replace("@", "#").split("#")[1].replace("~", "&");
			editCell = editCell.replace("@", "#").split("#")[0];
			String editValue = Step6_1.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(editCell, editValue);

			boolean s4 = false;
			page = Step6_2.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step6_2.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s4 = true;

			if (s1 && s2 && s3 && s4)
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
			closeFormInstance();
			writeTestResultToFile(caseID + ",6391", testRst, "RowLimit");
		}

	}

	@Test
	public void test6305() throws Exception
	{
		String caseID = "6305";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String FileName = testdata.get(4);
			String Step4_1 = testdata.get(5);
			String Step4_2 = testdata.get(6);
			String Step5 = testdata.get(7);
			String Step6_1 = testdata.get(8);
			String Step6_2 = testdata.get(9);

			File importFile = new File(importFolder + "\\" + FileName);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			formInstancePage.importAdjustment(importFile, false, false);
			boolean s1 = false;
			String page = Step4_1.split("#")[0].replace("~", "&");
			String instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			int rowLimit = Integer.parseInt(Step4_1.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s1 = true;

			boolean s2 = false;
			page = Step4_2.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step4_2.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s2 = true;

			boolean s3 = false;
			page = Step5.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step5.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s3 = true;

			String editCell = Step6_1.split("#")[0];
			page = editCell.replace("@", "#").split("#")[1].replace("~", "&");
			editCell = editCell.replace("@", "#").split("#")[0];
			String editValue = Step6_1.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(editCell, editValue);

			boolean s4 = false;
			page = Step6_2.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step6_2.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s4 = true;

			if (s1 && s2 && s3 && s4)
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
			writeTestResultToFile(caseID + ",6445", testRst, "RowLimit");
		}

	}

	@Test
	public void test6324() throws Exception
	{
		String caseID = "6324";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Step3_1 = testdata.get(4);
			String Step3_2 = testdata.get(5);
			String Step4_1 = testdata.get(6);
			String Step4_2 = testdata.get(7);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);
			if (listPage.getProcessDateOptions().contains(ReferenceDate))
			{
				listPage.setProcessDate(ReferenceDate);
				listPage.deleteFormInstance(Form, ReferenceDate);
			}

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
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);

			boolean s1 = false;
			int rowLimit = Integer.parseInt(Step3_1.split("#")[1]);
			String page = Step3_1.split("#")[0].replace("~", "&");
			String startCell = null;
			String instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			if (page.contains("@"))
			{
				String tmp = page;
				page = tmp.replace("@", "#").split("#")[0];
				startCell = tmp.replace("@", "#").split("#")[1].replace("[", "#").split("#")[0];
			}

			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);

			if (formInstancePage.isRowlimit(startCell, rowLimit))
				s1 = true;

			boolean s2 = false;
			rowLimit = Integer.parseInt(Step3_2.split("#")[1]);
			page = Step3_2.split("#")[0].replace("~", "&");
			startCell = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			if (page.contains("@"))
			{
				String tmp = page;
				page = tmp.replace("@", "#").split("#")[0];
				startCell = tmp.replace("@", "#").split("#")[1].replace("[", "#").split("#")[0];
			}

			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);

			if (formInstancePage.isRowlimit(startCell, rowLimit))
				s2 = true;

			boolean s3 = false;

			String editCell = Step4_1.split("#")[0].replace("~", "&");
			instance = editCell.replace("[", "#").split("#")[1].replace("]", "");
			page = editCell.replace("@", "#").split("#")[1].replace("[", "#").split("#")[0];
			editCell = editCell.replace("@", "#").split("#")[0];
			String editValue = Step4_1.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(editCell, editValue);

			rowLimit = Integer.parseInt(Step4_2.split("#")[1]);
			page = Step4_2.split("#")[0].replace("~", "&");
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			if (page.contains("@"))
			{
				String tmp = page;
				page = tmp.replace("@", "#").split("#")[0];
				startCell = tmp.replace("@", "#").split("#")[1].replace("[", "#").split("#")[0];
			}

			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);

			if (formInstancePage.isRowlimit(startCell, rowLimit))
				s3 = true;

			if (s1 && s2 && s3)
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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}

	@Test
	public void test6306() throws Exception
	{
		String caseID = "6306";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Step6 = testdata.get(3);
			String CSV = testdata.get(4);
			String module = testdata.get(5);
			String Vanilla = testdata.get(6);

			List<String> Files = createFolderAndCopyFile("RowLimit", null);
			String testDataFolder = Files.get(0);

			File importFile = new File(importFolder + "\\" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String editCell = Step6.split("#")[0];
			String page = editCell.replace("@", "#").split("#")[1].replace("~", "&");
			editCell = editCell.replace("@", "#").split("#")[0];
			String editValue = Step6.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(editCell, editValue);
			String exportFilePath = formInstancePage.exportToFile(null, null, null, "csv", null, null);
			boolean s1 = Business.verifyExportedFile(testDataFolder + CSV, exportFilePath, "csv");

			exportFilePath = formInstancePage.exportToFile(null, null, null, "Vanilla", module, null);
			boolean s2 = Business.verifyExportedFile(testDataFolder + Vanilla, exportFilePath, "Vanilla");

			if (s1 && s2)
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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}

	@Test
	public void test6307() throws Exception
	{
		String caseID = "6307";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Step5 = testdata.get(3);
			String Step6_1 = testdata.get(4);
			String Step6_2 = testdata.get(5);

			File importFile = new File(importFolder + "\\" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			String page = Step5.split("#")[0].replace("~", "&");
			String instance = Step5.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.deletePageInstance(instance);

			boolean s1 = false;
			page = Step6_1.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			int rowLimit = Integer.parseInt(Step6_1.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s1 = true;

			boolean s2 = false;
			page = Step6_2.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step6_2.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s2 = true;

			if (s1 && s2)
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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}

	@Test
	public void test6309() throws Exception
	{
		String caseID = "6309";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Step4 = testdata.get(3);
			String Step5_1 = testdata.get(4);
			String Step5_2 = testdata.get(5);
			String Step6_1 = testdata.get(6);
			String Step6_2 = testdata.get(7);
			String Step7_1 = testdata.get(8);
			String Step7_2 = testdata.get(9);

			File importFile = new File(importFolder + "\\" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String page = Step4.split("#")[0].replace("~", "&");
			String cell = Step4.split("#")[1];
			formInstancePage.selectPage(page);
			int rowLimit = Integer.parseInt(Step4.split("#")[1]);
			formInstancePage.selectPage(page);
			boolean s1 = false;
			if (formInstancePage.isRowlimit(rowLimit))
				s1 = true;

			boolean s2 = false;
			String editCell = null;
			page = Step5_1.split("#")[0];
			if (page.contains("@"))
			{
				String tmp = page;
				page = tmp.replace("@", "#").split("#")[1];
				editCell = tmp.replace("@", "#").split("#")[0];
			}
			String editValue = Step5_1.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(editCell, editValue);

			page = Step5_2.split("#")[0].replace("~", "&");
			cell = Step5_2.split("#")[1];
			formInstancePage.selectPage(page);
			rowLimit = Integer.parseInt(Step5_2.split("#")[1]);
			formInstancePage.selectPage(page);
			if (formInstancePage.isRowlimit(rowLimit))
				s2 = true;

			boolean s3 = false;
			page = Step6_1.split("#")[0].replace("~", "&");
			cell = Step6_1.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.deleteRow(cell);

			page = Step6_2.split("#")[0].replace("~", "&");
			String instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step6_2.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s3 = true;

			boolean s4 = false;
			formInstancePage.insertRow(cell);
			editCell = Step7_1.split("#")[0];
			editValue = Step7_1.split("#")[1];
			formInstancePage.editCellValue(editCell, editValue);
			page = Step7_2.split("#")[0].replace("~", "&");
			instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			rowLimit = Integer.parseInt(Step7_2.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
				s4 = true;

			if (s1 && s2 && s3 && s4)
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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}

	@Test
	public void test6316() throws Exception
	{
		String caseID = "6316";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Step6 = testdata.get(3);

			File importFile = new File(importFolder + "\\" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String page = Step6.split("#")[0].replace("~", "&");
			String instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			int rowLimit = Integer.parseInt(Step6.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(rowLimit))
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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}

	@Test
	public void test6319() throws Exception
	{
		String caseID = "6319";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Step6 = testdata.get(3);

			File importFile = new File(importFolder + "\\" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String page = Step6.split("#")[0].replace("~", "&");
			String instance = null;
			if (page.contains("["))
			{
				String tmp = page;
				page = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}
			String startCell = null;
			if (page.contains("@"))
			{
				String tmp = page;
				page = tmp.replace("@", "#").split("#")[0];
				startCell = tmp.replace("@", "#").split("#")[1];
			}
			int rowLimit = Integer.parseInt(Step6.split("#")[1]);
			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			if (formInstancePage.isRowlimit(startCell, rowLimit))
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
			writeTestResultToFile(caseID + ",6390", testRst, "RowLimit");
		}
	}

	@SuppressWarnings("static-access")
	@Test
	public void test6392() throws Exception
	{
		String caseID = "6392";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Validation = testdata.get(3);

			List<String> Files = createFolderAndCopyFile("RowLimit", null);
			String testDataFolder = Files.get(0);
			String checkCellFileFolder = Files.get(1);

			File importFile = new File(importFolder + "\\" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String source = testDataFolder + Validation;
			String dest = checkCellFileFolder + Validation;

			File expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}

			logger.info("Begin get rule result");
			File compareRstFile = new File(targetLogFolder + "\\rule_compareRst.txt");
			if (compareRstFile.exists())
				compareRstFile.delete();
			String exporteFile = formInstancePage.exportValidationResult();
			long startTime = System.currentTimeMillis();
			String commons[] =
			{ parentPath + "\\public\\extension\\GetRuleResult\\GetRuleResult.exe", exporteFile, expectedValueFile.getAbsolutePath(), targetLogFolder, "Y" };
			logger.info("cmd args are:" + commons[0] + " " + commons[1] + " " + commons[2]);
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			logger.info("Current time is:  " + dateFormat.format(now));
			Process process = Runtime.getRuntime().exec(commons);
			process.waitFor();
			long cur = System.currentTimeMillis();
			logger.info("Take " + (cur - startTime) / 1000 + " seconds");

			String rst = TxtUtil.getAllContent(compareRstFile).trim();
			if (rst.valueOf(0).equalsIgnoreCase("0"))
				rst = rst.substring(1);
			if (rst.equalsIgnoreCase("pass"))
				testRst = true;
			else
				testRst = false;

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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}

	@Test
	public void test6439() throws Exception
	{
		String caseID = "6439";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String FileName = testdata.get(3);
			String FileName2 = testdata.get(4);
			String Step5 = testdata.get(5);
			String Step6 = testdata.get(6);
			String Step7_1 = testdata.get(7);
			String Step7_2 = testdata.get(8);
			String data1 = testdata.get(9);
			String data2 = testdata.get(10);
			String data3 = testdata.get(11);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			List<String> Files = createFolderAndCopyFile("RowLimit", null);
			String testDataFolder = Files.get(0);
			String checkCellFileFolder = Files.get(1);

			File importFile = new File(importFolder + "\\" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String editCell = Step5.split("#")[0].replace("~", "&");
			String page = editCell.replace("@", "#").split("#")[1];
			editCell = editCell.replace("@", "#").split("#")[0];
			String editValue = Step5.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(editCell, editValue);

			page = Step6.split("#")[0].replace("~", "&");
			String cell = Step6.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.deleteRow(cell);

			String source = testDataFolder + data1;
			String dest = checkCellFileFolder + data1;

			File expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}

			boolean s1 = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);

			page = Step7_1.split("#")[0].replace("~", "&");
			cell = Step7_1.split("#")[1];

			formInstancePage.selectPage(page);
			formInstancePage.insertRow(cell);

			cell = Step7_2.split("#")[0];
			editValue = Step7_2.split("#")[1];
			formInstancePage.editCellValue(cell, editValue);

			source = testDataFolder + data2;
			dest = checkCellFileFolder + data2;

			expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}

			boolean s2 = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);

			importFile = new File(importFolder + "\\" + FileName2);
			formInstancePage.importAdjustment(importFile, true, false);
			source = testDataFolder + data3;
			dest = checkCellFileFolder + data3;

			expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}
			boolean s3 = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);

			if (s1 && s2 && s3)
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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}

	@Test
	public void test6446() throws Exception
	{
		String caseID = "6446";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String FileName = testdata.get(3);
			String FileName2 = testdata.get(4);
			String Step5 = testdata.get(5);
			String Step6 = testdata.get(6);
			String Step7_1 = testdata.get(7);
			String Step7_2 = testdata.get(8);
			String data1 = testdata.get(9);
			String data2 = testdata.get(10);
			String data3 = testdata.get(11);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			List<String> Files = createFolderAndCopyFile("RowLimit", null);
			String testDataFolder = Files.get(0);
			String checkCellFileFolder = Files.get(1);

			File importFile = new File(importFolder + "\\" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String editCell = Step5.split("#")[0];
			String page = editCell.replace("@", "#").split("#")[1].replace("~", "&");
			editCell = editCell.replace("@", "#").split("#")[0];
			String editValue = Step5.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(editCell, editValue);

			page = Step6.split("#")[0].replace("~", "&");
			String cell = Step6.split("#")[1];
			formInstancePage.selectPage(page);
			formInstancePage.deleteRow(cell);

			String source = testDataFolder + data1;
			String dest = checkCellFileFolder + data1;

			File expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}

			boolean s1 = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);

			page = Step7_1.split("#")[0].replace("~", "&");
			cell = Step7_1.split("#")[1];

			formInstancePage.selectPage(page);
			formInstancePage.insertRow(cell);

			cell = Step7_2.split("#")[0];
			editValue = Step7_2.split("#")[1];
			formInstancePage.editCellValue(cell, editValue);

			source = testDataFolder + data2;
			dest = checkCellFileFolder + data2;

			expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}

			boolean s2 = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
			importFile = new File(importFolder + "\\" + FileName2);
			formInstancePage.importAdjustment(importFile, true, false);
			source = testDataFolder + data3;
			dest = checkCellFileFolder + data3;

			expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}
			boolean s3 = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);

			if (s1 && s2 && s3)
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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}

	@Test
	public void test6365() throws Exception
	{
		String caseID = "6365";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_RowLimit, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Page = testdata.get(4);
			String instance = testdata.get(5);
			String Edit = testdata.get(6);
			String Step4_1 = testdata.get(7);
			String Step4_2 = testdata.get(8);
			String Step4_3 = testdata.get(9);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);
			if (listPage.getProcessDateOptions().contains(ReferenceDate))
			{
				listPage.setProcessDate(ReferenceDate);
				listPage.deleteFormInstance(Form, ReferenceDate);
			}

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
				logger.error("Retrieve form failed");
			listPage.closeJobDialog();

			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.selectPage(Page);
			formInstancePage.selectInstance(instance);

			boolean s1 = false;
			String editCell = Edit.split("#")[0];
			String editValue = Edit.split("#")[1];
			formInstancePage.editCellValue(editCell, editValue);

			int rowLimit = Integer.parseInt(Step4_1.split("#")[1]);
			String startCell = Step4_1.split("#")[0];
			if (formInstancePage.isRowlimit(startCell, rowLimit))
				s1 = true;

			boolean s2 = false;
			String cell = Step4_2.split("#")[0];
			String expectedValue = Step4_2.split("#")[1];
			String actualValue = formInstancePage.getCellText(startCell, cell);
			if (actualValue.equals(expectedValue))
				s2 = true;

			boolean s3 = formInstancePage.isStrikethrough(startCell, Step4_3);

			if (s1 & s2 & s3)
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
			writeTestResultToFile(caseID, testRst, "RowLimit");
		}
	}
}
