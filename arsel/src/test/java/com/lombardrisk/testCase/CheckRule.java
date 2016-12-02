package com.lombardrisk.testCase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * Create by Leo Tu on Jul 2, 2015
 */
public class CheckRule extends TestTemplate
{

	static List<String> Files = new ArrayList<>();
	static String testDataFolder = null;
	static String checkRuleFileFolder = null;
	static File testRstFile = null;
	static String parentPath = new File(System.getProperty("user.dir")).getParent().toString();

	protected void verifySumRule(int ID, String Regulator, String Group, String Form, String formCode, String version, String ProcessDate, String EditCell, String CheckRules) throws Exception
	{
		boolean testResult = false;
		try
		{
			logger.info("============Test Sum Rule[" + Form + "_" + ProcessDate + "_" + Group + "]=============");

			File expectedRstFile = new File(checkRuleFileFolder + CheckRules);
			if (expectedRstFile.exists())
			{
				expectedRstFile.delete();
			}
			logger.info("Copy file " + testDataFolder + CheckRules + " to " + expectedRstFile);
			FileUtils.copyFile(new File(testDataFolder + CheckRules), expectedRstFile);

			String cellName;
			String rowNO = null;
			if (EditCell.contains(","))
			{
				logger.info(EditCell + " is extendgrid cell");
				cellName = EditCell.split(",")[0].trim();
				rowNO = EditCell.split(",")[1].trim();
				rowNO = String.valueOf(Integer.parseInt(rowNO) + 48);
			}
			else
			{
				cellName = EditCell;
			}
			String gridName = getExtendCellName(Regulator, formCode, version, cellName);
			String ExtCellName = null;
			if (gridName != null)
			{
				ExtCellName = gridName + rowNO + cellName;
			}
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

			String curValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, ExtCellName);
			logger.info("Current value is: " + curValue);
			String editValue;
			try
			{
				if (curValue.equalsIgnoreCase("N"))
					curValue = "1";
				editValue = String.valueOf(Integer.parseInt(curValue.trim()) + 1);
			}
			catch (Exception e)
			{
				editValue = curValue.subSequence(0, 1) + getRandomString(2);
				if (editValue.equalsIgnoreCase(curValue))
				{
					editValue = curValue.subSequence(0, 1) + getRandomString(2);
				}
			}

			formInstancePage.editCellValue(null, cellName, ExtCellName, editValue);

			File exportProblem = new File(formInstancePage.exportProblem());

			long startTime = System.currentTimeMillis();
			String commons[] =
			{ parentPath + "\\public\\extension\\GetRuleProblem\\GetRuleProblem.exe", exportProblem.getAbsolutePath(), expectedRstFile.getAbsolutePath(), targetLogFolder };
			logger.info("cmd args are:" + commons[1] + " " + commons[2]);
			Process process = Runtime.getRuntime().exec(commons);
			process.waitFor();
			long cur = System.currentTimeMillis();
			logger.info("Take " + (cur - startTime) / 1000 + " seconds");

			boolean caseRst = true;
			int rowAmt = ExcelUtil.getRowNums(expectedRstFile, null);
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(expectedRstFile, null, i);
				String caseID = rowValueList.get(0).trim();
				String check = rowValueList.get(1).trim();
				String ruleType = rowValueList.get(2);
				int ruleID = Integer.parseInt(rowValueList.get(3).trim());
				String instance = rowValueList.get(4).trim();
				if (instance.equals(""))
					instance = null;
				String rowID = rowValueList.get(5).trim();
				String id = rowID;
				if (rowID.length() > 0)
					rowID = String.valueOf(Integer.parseInt(rowValueList.get(5).trim()) + 48);
				String expectedValue = rowValueList.get(6);
				String expectedLevel = rowValueList.get(8).trim();
				String expectedError = rowValueList.get(10).trim();
				boolean levelRst = false;
				if (expectedLevel.equals(rowValueList.get(9).trim()))
					levelRst = true;
				boolean errorMsgRst = false;
				if (expectedError.equals(rowValueList.get(11).trim()))
					errorMsgRst = true;
				boolean valueRst = true;

				if (check.equalsIgnoreCase("Y"))
					if (ruleType.equalsIgnoreCase("Sum"))
					{
						if (instance != null)
						{
							logger.info("Begin verify sum rule " + ruleID + " instance: " + instance + " row number: " + id);
						}
						else
						{
							logger.info("Begin verify sum rule " + ruleID + " row number: " + id);
						}

						String Destfld = getDestFldFromSumRule(Regulator, formCode, version, ruleID);

						if (Destfld.contains("@"))
						{
							String temp = Destfld.replace("@", "~");
							Destfld = temp.split("~")[0];

							if (Destfld.contains("["))
							{

								String tmp = Destfld.replace("[", "~").replace("]", "");
								Destfld = tmp.split("~")[0].trim();
								instance = tmp.split("~")[1].trim();
							}
						}
						if (Destfld.contains("["))
						{
							String tmp = Destfld.replace("[", "~").replace("]", "");
							Destfld = tmp.split("~")[0].trim();
							instance = tmp.split("~")[1].trim();
						}
						gridName = getExtendCellName(Regulator, formCode, version, Destfld);
						ExtCellName = null;
						if (gridName != null)
							ExtCellName = gridName + rowID + Destfld;

						if (!expectedLevel.equalsIgnoreCase("Error"))
						{
							logger.info("Get cell " + Destfld + " instance: " + instance + " row number: " + id + " in form");
							String actualValue = formInstancePage.getCellText(Regulator, formCode, version, instance, Destfld, ExtCellName);
							if (actualValue == null)
							{
								valueRst = false;
								ExcelUtil.writeToExcel(expectedRstFile, i, 7, "Cannot find the cell: " + Destfld);
							}
							else
							{
								ExcelUtil.writeToExcel(expectedRstFile, i, 7, actualValue);
								if (!actualValue.equals(expectedValue))
									valueRst = false;
							}

						}

						if (valueRst && levelRst && errorMsgRst)
						{
							logger.info("Same with expected value");
							ExcelUtil.writeToExcel(expectedRstFile, i, 13, "Pass");
							testResult = true;
						}
						else
						{
							logger.warn("Different with expected value");
							ExcelUtil.writeToExcel(expectedRstFile, i, 13, "Fail");
						}
						writeTestResultToFile(caseID, caseRst, "CheckRule");
					}
			}

		}
		catch (NumberFormatException e)
		{
		}
		catch (Exception e)
		{
			testResult = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			closeFormInstance();
			ExcelUtil.writeTestRstToFile(testRstFile, ID, 8, testResult);
		}
	}

	@SuppressWarnings("static-access")
	protected void verifyValRuleForPro(int ID, String Regulator, String Group, String Form, String formCode, String version, String ProcessDate, String CheckRules) throws Exception
	{
		boolean testRst = false;
		try
		{
			logger.info("============Test Validation Rule[" + Form + "_" + ProcessDate + "_" + Group + "]=============");
			File expectedRstFile = new File(checkRuleFileFolder + CheckRules);
			if (expectedRstFile.exists())
			{
				expectedRstFile.delete();
			}
			logger.info("Copy file " + testDataFolder + CheckRules + " to " + expectedRstFile);
			FileUtils.copyFile(new File(testDataFolder + CheckRules), expectedRstFile);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);

			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

			logger.info("Begin get rule result");
			File compareRstFile = new File(targetLogFolder + "\\rule_compareRst.txt");
			if (compareRstFile.exists())
				compareRstFile.delete();
			String exporteFile = formInstancePage.exportValidationResult();
			long startTime = System.currentTimeMillis();
			String commons[] =
			{ parentPath + "\\public\\extension\\GetRuleResult\\GetRuleResult.exe", exporteFile, expectedRstFile.getAbsolutePath(), targetLogFolder, "Y" };
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
			logger.info("Test result is:" + testRst);
		}
		catch (NumberFormatException e)
		{
			// e.printStackTrace();logger.error(e.getMessage());
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			closeFormInstance();
			ExcelUtil.writeTestRstToFile(testRstFile, ID, 8, testRst);

		}

	}

	@Parameters(
	{ "fileName" })
	@Test
	public void testRule(@Optional String fileName) throws Exception
	{
		logger.info("File is: " + fileName);
		if (fileName == null || fileName.equals(""))
			fileName = "CheckRule.xls";
		Files = createFolderAndCopyFile("CheckRule", fileName);
		testDataFolder = Files.get(0);
		checkRuleFileFolder = Files.get(1);
		testRstFile = new File(Files.get(2));

		if (!testRstFile.getName().equalsIgnoreCase(fileName))
			testRstFile = new File(testRstFile.getParent() + fileName);
		File testDataFile = new File(testDataFolderName + "\\CheckRule\\" + fileName);
		for (int index = 1; index <= ExcelUtil.getRowNums(testDataFile, null); index++)
		{
			ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
			int ID = Integer.parseInt(rowValue.get(0).trim());
			String Regulator = rowValue.get(1).trim();
			String Group = rowValue.get(2).trim();
			String Form = rowValue.get(3).trim();
			String ProcessDate = rowValue.get(4).trim();
			String Run = rowValue.get(5).trim();
			String EditCell = rowValue.get(6).trim();
			String CheckRules = rowValue.get(7).trim();

			EditCell = EditCell.trim();
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			Form = splitReturn(Form).get(2);

			if (CheckRules.endsWith(".xlsx") && Run.equalsIgnoreCase("Y"))
			{
				if (EditCell.length() > 3)
				{
					verifySumRule(ID, Regulator, Group, Form, formCode, version, ProcessDate, EditCell, CheckRules);
				}
				else
				{
					verifyValRuleForPro(ID, Regulator, Group, Form, formCode, version, ProcessDate, CheckRules);
				}
			}
		}
	}

}
