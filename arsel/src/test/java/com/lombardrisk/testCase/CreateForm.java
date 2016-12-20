package com.lombardrisk.testCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.lombardrisk.pages.AdjustLogPage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.TxtUtil;

/**
 * Created by Leo Tu on 6/15/2015.
 */
public class CreateForm extends TestTemplate
{
	static List<String> Files = new ArrayList<>();
	static String testDataFolder = null;
	static String checkCellFileFolder = null;
	static File testRstFile = null;
	static String importFolder = null;

	protected void CreateNewForm(int ID, String Regulator, String Group, String Form, String ProcessDate, String CopyDataFromExistForm, boolean AllowNull, boolean DeleteExistForm,
			boolean InitialiseToZeros, String CheckCellValue, String ErrorMessage, String CaseID) throws Exception
	{
		logger.info("============test Create Form from exist form[caseid:" + CaseID + "]=============");
		boolean testRst = true;
		try
		{
			String formCode = null;
			String version = null;

			if (Form != null)
			{
				formCode = splitReturn(Form).get(0);
				version = splitReturn(Form).get(1);
			}
			if (CopyDataFromExistForm.length() < 4)
				CopyDataFromExistForm = null;

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Group, null, null);

			if (DeleteExistForm)
			{
				if (listPage.getFormOptions().contains(Form))
					listPage.setForm(Form);
				if (listPage.getProcessDateOptions().contains(ProcessDate))
					listPage.setProcessDate(ProcessDate);
				listPage.deleteFormInstance(Form, ProcessDate);
			}

			FormInstancePage formInstancePage = null;
			boolean openForm = false;
			if (!ErrorMessage.equals(""))
			{
				String actualMsg = listPage.getCreateNewFormErrorMsg(Group, ProcessDate, Form, CopyDataFromExistForm, AllowNull, InitialiseToZeros);
				if (!actualMsg.equals(ErrorMessage))
				{
					testRst = false;
					logger.info("Expected message is:" + ErrorMessage + ", but actual message is:" + actualMsg + "!");
				}
			}
			else
			{
				openForm = true;
				formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, CopyDataFromExistForm, AllowNull, InitialiseToZeros);
			}

			if (formInstancePage != null && openForm)
			{
				if (CheckCellValue.endsWith("xlsx"))
				{
					String source = testDataFolder + CheckCellValue;
					String dest = checkCellFileFolder + CheckCellValue;

					File expectedValueFile = new File(dest);
					if (!expectedValueFile.isDirectory())
					{
						if (expectedValueFile.exists())
							expectedValueFile.delete();
						FileUtils.copyFile(new File(source), expectedValueFile);
					}

					testRst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);

					if (DeleteExistForm)
					{
						AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();
						if (adjustLogPage.getLogNums() != 0)
							testRst = false;
					}
				}
				else if (CheckCellValue.equalsIgnoreCase("Y"))
				{
					// Test for allow null and value of CheckCellValue is cell
					String cellName = CheckCellValue;
					String expectedValue = "1";
					formInstancePage.editCellValue(null, cellName, null, expectedValue);
					String accValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, null);
					if (!accValue.equals(expectedValue))
					{
						testRst = false;
						logger.error("Expected value is " + expectedValue + " ,but acctual value is " + accValue);
					}

					expectedValue = "NULL";
					formInstancePage.editCellValue(null, cellName, null, null);
					accValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, null);
					if (!accValue.equalsIgnoreCase(expectedValue))
					{
						testRst = false;
						logger.error("Expected value is " + expectedValue + " ,but acctual value is " + accValue);
					}
				}

			}
			else if (formInstancePage == null && openForm)
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
			writeTestResultToFile(testRstFile, ID, 14, CaseID, testRst, "CreateForm");

		}
	}

	protected void CreateFormFromExcel(int ID, String Regulator, String Group, String Form, String ProcessDate, String CreatefromExcel, boolean AllowNull, boolean DeleteExistForm,
			boolean InitialiseToZeros, String CheckCellValue, String ErrorMessage, String ErrorInfo, String CaseID) throws Exception
	{

		logger.info("============testCreateFormFromExcel[caseid:" + CaseID + "]=============");

		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);

		boolean openForm = false;
		boolean testRst = true;
		try
		{
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);

			if (DeleteExistForm)
			{
				if (listPage.getFormOptions().contains(Form))
					listPage.setForm(Form);
				if (listPage.getProcessDateOptions().contains(ProcessDate))
					listPage.setProcessDate(ProcessDate);
				listPage.deleteFormInstance(Form, ProcessDate);
			}

			File importFile = new File(importFolder + CreatefromExcel);
			FormInstancePage formInstancePage = null;
			if (!ErrorMessage.equals(""))
			{
				String actualMsg = listPage.getCreateFromExcelErrorMsg(importFile, false);
				if (!actualMsg.equals(ErrorMessage))
				{
					logger.error("Exptected message is:[" + ErrorMessage + "], but actual message is[" + actualMsg + "]");
					testRst = false;
				}
				else
				{
					logger.info("Error message is same");
				}
			}
			else if (!ErrorInfo.equals("") && ErrorInfo.endsWith(".txt"))
			{
				File txt = new File(testDataFolder + ErrorInfo);
				String expectedInfo = TxtUtil.getAllContent(txt);
				String actualInfo = listPage.getCreateFromExcelErrorInfo(importFile);
				if (!expectedInfo.equals(actualInfo))
				{
					logger.error("Exptected info is:[" + expectedInfo + "], but actual info is[" + actualInfo + "]");
					testRst = false;
				}
				else
				{
					logger.info("Error infomation is same");
				}
			}
			else
			{
				openForm = true;
				formInstancePage = listPage.createFormFromExcel(importFile, AllowNull, InitialiseToZeros, openForm);
			}

			if (formInstancePage != null && openForm)
			{
				if (CheckCellValue.endsWith(".xlsx"))
				{
					String source = testDataFolder + CheckCellValue;
					String dest = checkCellFileFolder + CheckCellValue;

					File expectedValueFile = new File(dest);
					if (!expectedValueFile.isDirectory())
					{
						if (expectedValueFile.exists())
							expectedValueFile.delete();
						FileUtils.copyFile(new File(source), expectedValueFile);
					}

					testRst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);

					if (DeleteExistForm)
					{
						AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();
						if (adjustLogPage.getLogNums() != 0)
							testRst = false;
					}
				}
				else
				{
					// Test for allow null and value of CheckCellValue is cell
					// name
					String cellName = CheckCellValue;
					String expectedValue = "1";
					formInstancePage.editCellValue(null, cellName, null, expectedValue);
					String accValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, null);
					if (!accValue.equals(expectedValue))
					{
						testRst = false;
						logger.error("Expected value is " + expectedValue + " ,but acctual value is " + accValue);
					}

					expectedValue = "NULL";
					formInstancePage.editCellValue(null, cellName, null, null);
					accValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, null);
					if (!accValue.equalsIgnoreCase(expectedValue))
					{
						testRst = false;
						logger.error("Expected value is " + expectedValue + " ,but acctual value is " + accValue);
					}
				}
			}
			else if (formInstancePage == null && openForm)
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
			if (openForm)
				closeFormInstance();
			writeTestResultToFile(testRstFile, ID, 14, CaseID, testRst, "CreateForm");
		}
	}

	@Parameters(
	{ "fileName" })
	@Test
	public void testCreateForm(@Optional String fileName) throws Exception
	{
		logger.info("File is: " + fileName);
		if (fileName == null || fileName.equals(""))
			fileName = "CreateForm.xls";
		Files = createFolderAndCopyFile("CreateForm", fileName);
		testDataFolder = Files.get(0);
		checkCellFileFolder = Files.get(1);
		testRstFile = new File(Files.get(2));
		importFolder = Files.get(3);

		if (!testRstFile.getName().equalsIgnoreCase(fileName))
			testRstFile = new File(testRstFile.getParent() + fileName);
		File testDataFile = new File(testDataFolderName + "/CreateForm/" + fileName);
		int rouNums = ExcelUtil.getRowNums(testDataFile, null);
		for (int index = 1; index <= rouNums; index++)
		{
			ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
			int ID = Integer.parseInt(rowValue.get(0).trim());
			String Regulator = rowValue.get(1).trim();
			boolean AllowNull = false;
			if (rowValue.get(2).trim().equals("Y"))
				AllowNull = true;
			String Group = rowValue.get(3).trim();
			String Form = rowValue.get(4).trim();
			String ProcessDate = rowValue.get(5).trim();
			if (ProcessDate.equals(""))
				ProcessDate = null;
			String Run = rowValue.get(6).trim();
			String CopyDataFromExistForm = rowValue.get(7).trim();
			String CreatefromExcel = rowValue.get(8).trim();

			boolean DeleteExistForm = false;
			if (rowValue.get(9).trim().equals("Y"))
				DeleteExistForm = true;
			boolean InitialiseToZeros = false;
			if (rowValue.get(10).trim().equals("Y"))
				InitialiseToZeros = true;
			String CheckCellValue = rowValue.get(11).trim();
			String ErrorMessage = rowValue.get(12).trim();
			String ErrorInfo = rowValue.get(13).trim();
			String CaseID = rowValue.get(15).trim();

			if (!Form.equals(""))
				Form = splitReturn(Form).get(2);
			else
				Form = null;

			if (Run.equalsIgnoreCase("Y"))
			{
				if (CreatefromExcel.endsWith(".xlsx") || CreatefromExcel.endsWith(".xls"))
					CreateFormFromExcel(ID, Regulator, Group, Form, ProcessDate, CreatefromExcel, AllowNull, DeleteExistForm, InitialiseToZeros, CheckCellValue, ErrorMessage, ErrorInfo, CaseID);
				else
					CreateNewForm(ID, Regulator, Group, Form, ProcessDate, CopyDataFromExistForm, AllowNull, DeleteExistForm, InitialiseToZeros, CheckCellValue, ErrorMessage, CaseID);
			}
		}

	}
}
