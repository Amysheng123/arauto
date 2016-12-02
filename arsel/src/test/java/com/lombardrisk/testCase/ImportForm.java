package com.lombardrisk.testCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.TxtUtil;

/**
 * Create by Leo Tu on Aug 17, 2015
 */
public class ImportForm extends TestTemplate
{
	static List<String> Files = new ArrayList<>();
	static String testDataFolder = null;
	static String checkRstFolder = null;
	static File testRstFile = null;
	static String importFolder = null;

	protected void ImportInDashboard(int ID, String Regulator, String Group, String Form, String ProcessDate, boolean deleteForm, String FileName, String CheckCellValue, String CheckInstance,
			String ErrorMsg, String ErrorInfo, String CaseID, boolean addToExistValue, boolean InitialiseToZeros) throws Exception
	{

		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		boolean testRst = true;
		try
		{
			logger.info("============test ImportAdjustments from dashboard,caseID[" + CaseID + "]=============");
			logger.info("Test " + Form + "_" + ProcessDate + "_" + Group);

			File importFile = new File(importFolder + FileName);
			if (!importFile.exists())
			{
				logger.error("The import file " + importFile + " does not exist!");
				testRst = false;
			}

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);

			if (deleteForm)
			{
				if (listPage.getFormOptions().contains(Form))
					listPage.setForm(Form);
				if (listPage.getProcessDateOptions().contains(ProcessDate))
					listPage.setProcessDate(ProcessDate);
				listPage.deleteFormInstance(Form, ProcessDate);

			}

			if (ErrorMsg.length() > 1)
			{
				logger.info("Begin get import error message");
				String error = listPage.getImportAdjustmentErrorMsg(importFile, addToExistValue);
				if (!ErrorMsg.equals(error))
				{
					testRst = false;
					logger.error("The expected error message is:" + ErrorMsg + ", but actual message is:[" + error + "]");
					ExcelUtil.writeToExcel(testRstFile, ID, 18, error);
				}
			}
			else if (!ErrorInfo.equals("") && ErrorInfo.endsWith(".txt"))
			{
				logger.info("Begin get import error info");
				File txt = new File(testDataFolderName + "\\ImportForm\\CheckCellValue\\" + ErrorInfo);
				String actualInfo = listPage.getimportAdjustmentErrorInfo(importFile, addToExistValue).replace("\r", "\n");
				String expectInfo = TxtUtil.getAllContent(txt).replace("\r", "\n");
				if (!expectInfo.equalsIgnoreCase(actualInfo))
				{
					testRst = false;
					logger.error("The expected error info is:" + expectInfo + ", but actual error info is:[" + actualInfo + "]");
					ExcelUtil.writeToExcel(testRstFile, ID, 18, actualInfo);
				}

			}
			else
			{
				FormInstancePage formInstancePage = listPage.importAdjustment(importFile, addToExistValue, InitialiseToZeros);
				if (formInstancePage != null)
				{
					if (CheckCellValue.endsWith(".xlsx"))
					{
						File sourceFile = new File(testDataFolder + CheckCellValue);
						File expectedValueFile = new File(checkRstFolder + CheckCellValue);
						logger.info("Copy file " + sourceFile + " to " + expectedValueFile);
						if (expectedValueFile.exists())
							expectedValueFile.delete();
						FileUtils.copyFile(sourceFile, expectedValueFile);

						testRst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
					}
					else if (CheckInstance.endsWith(".xlsx"))
					{
						File sourceFile = new File(testDataFolder + CheckInstance);
						File expectedValueFile = new File(checkRstFolder + CheckInstance);
						logger.info("Copy file " + sourceFile + " to " + expectedValueFile);
						if (expectedValueFile.exists())
							expectedValueFile.delete();
						FileUtils.copyFile(sourceFile, expectedValueFile);

						int amt = ExcelUtil.getRowNums(expectedValueFile, null);
						for (int index = 1; index <= amt; index++)
						{
							ArrayList<String> expectedValueValueList = ExcelUtil.getRowValueFromExcel(expectedValueFile, null, index);

							if (CheckInstance.endsWith(".xlsx"))
							{
								String page = expectedValueValueList.get(0).trim();
								String expectInstance = expectedValueValueList.get(1).trim();
								List<String> instances = formInstancePage.getAllInstance(page);
								String actualInstance = "";
								for (String item : instances)
								{
									actualInstance = actualInstance + item + "#";
								}
								actualInstance = actualInstance.substring(0, actualInstance.length() - 1);
								ExcelUtil.writeToExcel(expectedValueFile, index, 2, actualInstance);

								if (!actualInstance.equalsIgnoreCase(expectInstance))
								{
									testRst = false;
									ExcelUtil.writeToExcel(expectedValueFile, index, 3, "Fail");
								}
								else
									ExcelUtil.writeToExcel(expectedValueFile, index, 3, "Pass");
							}
						}
					}
				}
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
			closeFormInstance();
			writeTestResultToFile(testRstFile, ID, 16, CaseID, testRst, "ImportForm");
		}
	}

	protected void ImportInFormPage(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileName, String CheckCellValue, String CheckInstance, String ErrorMsg,
			String ErrorInfo, String CaseID, boolean addToExistValue, boolean InitialiseToZeros, String FormLocked) throws Exception
	{
		boolean testRst = true;
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		try
		{
			logger.info("============test ImportAdjustments from dashboard,caseID[" + CaseID + "]=============");
			logger.info("Test " + Form + "_" + ProcessDate + "_" + Group);

			File importFile = new File(importFolder + FileName);
			if (!importFile.exists())
			{
				logger.error("The import file " + importFile + " deos not exist!");
				testRst = false;
			}

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

			if (ErrorMsg.length() > 1)
			{
				if (!ErrorMsg.equals(formInstancePage.getImportAdjustmentErrorMsg(importFile)))
				{
					testRst = false;
				}
			}
			else if (!ErrorInfo.equals("") && ErrorInfo.endsWith(".txt"))
			{
				File txt = new File(testDataFolderName + "\\ImportForm\\CheckCellValue\\" + ErrorInfo);
				String actualInfo = formInstancePage.getImportAdjustmentErrorInfo(importFile).replaceAll("(\r\n|\r|\n|\n\r)", "<br>");

				String expectInfo = TxtUtil.getAllContent(txt).replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
				if (!expectInfo.equals(actualInfo))
				{
					testRst = false;
					logger.error("The expected error info is:" + expectInfo + ", but actual error info is:[" + actualInfo + "]");
				}
			}
			else if (FormLocked.equalsIgnoreCase("Y"))
			{
				if (formInstancePage.isImportAdjustmentEnabled())
				{
					testRst = false;
					logger.error("Import adjustment should be disabled");
				}
			}
			else
			{
				boolean imported = formInstancePage.importAdjustment(importFile, addToExistValue, InitialiseToZeros);

				if (imported)
				{
					if (CheckCellValue.endsWith(".xlsx"))
					{
						File sourceFile = new File(testDataFolder + CheckCellValue);
						File expectedValueFile = new File(checkRstFolder + CheckCellValue);
						logger.info("Copy file " + sourceFile + " to " + expectedValueFile);
						if (expectedValueFile.exists())
							expectedValueFile.delete();
						FileUtils.copyFile(sourceFile, expectedValueFile);

						testRst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
					}
					else
					{
						File sourceFile = new File(testDataFolder + CheckInstance);
						File expectedValueFile = new File(checkRstFolder + CheckInstance);
						logger.info("Copy file " + sourceFile + " to " + expectedValueFile);
						if (expectedValueFile.exists())
							expectedValueFile.delete();
						FileUtils.copyFile(sourceFile, expectedValueFile);

						int amt = ExcelUtil.getRowNums(expectedValueFile, null);

						for (int index = 1; index <= amt; index++)
						{
							ArrayList<String> expectedValueValueList = ExcelUtil.getRowValueFromExcel(expectedValueFile, null, index);
							String page = expectedValueValueList.get(0).trim();
							String expectInstance = expectedValueValueList.get(1).trim();
							List<String> instances = formInstancePage.getAllInstance(page);
							String actualInstance = "";
							for (String item : instances)
							{
								actualInstance = actualInstance + item + "#";
							}
							actualInstance = actualInstance.substring(0, actualInstance.length() - 1);
							ExcelUtil.writeToExcel(expectedValueFile, index, 2, actualInstance);

							if (!actualInstance.equalsIgnoreCase(expectInstance))
							{
								testRst = false;
								ExcelUtil.writeToExcel(expectedValueFile, index, 3, "Fail");
							}
							else
								ExcelUtil.writeToExcel(expectedValueFile, index, 3, "Pass");

						}
					}
				}
				else if (imported && !CheckCellValue.endsWith("xlsx"))
				{
					/*
					 * // Test for allow null String cellName = CheckCellValue;
					 * String expectedValue = "1";
					 * formInstancePage.editCellValue(null, cellName, null,
					 * expectedValue); String accValue =
					 * formInstancePage.getCellText(Regulator, formCode,
					 * version, null, cellName, null); if (
					 * !accValue.equalsIgnoreCase(expectedValue) ) testRst =
					 * false;
					 * 
					 * 
					 * formInstancePage.editCellValue(null, cellName, null,
					 * null); accValue = formInstancePage.getCellText(Regulator,
					 * formCode, version, null, cellName, null); if (
					 * !accValue.equalsIgnoreCase("Null") ) testRst = false;
					 */
				}
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
			closeFormInstance();
			writeTestResultToFile(testRstFile, ID, 16, CaseID, testRst, "ImportForm");
		}
	}

	@Parameters(
	{ "fileName" })
	@Test
	public void testImportAdjustment(@Optional String fileName) throws Exception
	{
		logger.info("File is: " + fileName);
		if (fileName == null || fileName.equals(""))
			fileName = "ImportForm.xls";

		logger.info("test data file is:" + fileName);
		Files = createFolderAndCopyFile("ImportForm", fileName);
		testDataFolder = Files.get(0);
		checkRstFolder = Files.get(1);
		testRstFile = new File(Files.get(2));
		importFolder = Files.get(3);

		if (!testRstFile.getName().equalsIgnoreCase(fileName))
			testRstFile = new File(testRstFile.getParent() + fileName);
		File testDataFile = new File(testDataFolderName + "\\ImportForm\\" + fileName);
		for (int index = 1; index <= ExcelUtil.getRowNums(testDataFile, null); index++)
		{
			ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
			int ID = Integer.parseInt(rowValue.get(0).trim());
			String Regulator = rowValue.get(1).trim();
			String Group = rowValue.get(2).trim();
			String Form = rowValue.get(3).trim();
			String ProcessDate = rowValue.get(4).trim();
			String Run = rowValue.get(5).trim();
			String ImportFrom = rowValue.get(6).trim();
			String ReplaceORAdd = rowValue.get(8).trim();
			boolean InitialiseToZeros = false;
			if (rowValue.get(9).trim().equalsIgnoreCase("Y"))
				InitialiseToZeros = true;
			String formLocked = rowValue.get(10).trim();
			String ImportFileName = rowValue.get(11).trim();
			String CheckCellValue = rowValue.get(12).trim();
			String CheckInstance = rowValue.get(13).trim();
			String ErrorMessage = rowValue.get(14).trim();
			String ErrorInfo = rowValue.get(15).trim();
			String CaseID = rowValue.get(17).trim();

			boolean deleteForm = false;
			if (rowValue.get(7).trim().equalsIgnoreCase("Y"))
				deleteForm = true;

			Form = splitReturn(Form).get(2);
			boolean addTo = false;
			if (Run.trim().equalsIgnoreCase("Y"))
			{
				if (ReplaceORAdd.equalsIgnoreCase("A"))
					addTo = true;

				if (ImportFrom.equalsIgnoreCase("Dashboard"))
					ImportInDashboard(ID, Regulator, Group, Form, ProcessDate, deleteForm, ImportFileName, CheckCellValue, CheckInstance, ErrorMessage, ErrorInfo, CaseID, addTo, InitialiseToZeros);

				else if (ImportFrom.equalsIgnoreCase("FormPage"))
					ImportInFormPage(ID, Regulator, Group, Form, ProcessDate, ImportFileName, CheckCellValue, CheckInstance, ErrorMessage, ErrorInfo, CaseID, addTo, InitialiseToZeros, formLocked);
			}

		}

	}

}
