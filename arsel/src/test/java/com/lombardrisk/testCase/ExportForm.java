package com.lombardrisk.testCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.lombardrisk.pages.ExportToFilePage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.Business;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.FileUtil;

/**
 * Created by Leo Tu on 6/17/2015.
 */
public class ExportForm extends TestTemplate
{

	static List<String> Files = new ArrayList<>();
	static String testDataFolder = null;
	static String checkRstFolder = null;
	static File testRstFile = null;

	public static void main(String args[]) throws Exception
	{
		String pathString = "C:/Documents/download/";
		String fileName = "RBIForAutomation_0001_RBIndia-FORMX_20250520.xlsx";
		String fileType = "iFile";
		String date = "20161116";

		testDataFolder = "D:/Develop/workspace/rpsel/data_ar/ExportForm/CheckCellValue/";
		checkRstFolder = "D:/Develop/workspace/rpsel/target/TestResult/" + date + "/ExportForm/CheckCellValue/";
		String baseLineFileName = "FormXv1_20250520_Arbitrary.xlsx";

		File expectedValueFile = new File(checkRstFolder + baseLineFileName);
		if (expectedValueFile.exists())
		{
			expectedValueFile.delete();
		}

		FileUtils.copyFile(new File(testDataFolder + baseLineFileName), expectedValueFile);

		boolean rst = Business.verifyExportedFile(expectedValueFile.getAbsolutePath(), pathString + fileName, fileType);
		System.out.print("Compare restult is:" + rst);
	}

	protected void ExportToExcel(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileType, String Module, String expectFileName, String CheckCellValueFile,
			String CaseID) throws Exception, InterruptedException, DocumentException
	{

		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);

		boolean testRst = false;
		try
		{
			logger.info("============test Export[" + Form + "] To Excel,case id is:" + CaseID + "=============");

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			String curUserName = listPage.getUserName().toUpperCase();
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

			String exportFilePath = formInstancePage.exportToFile(null, null, null, "excel", Module, null);
			boolean s1 = true;
			if (exportFilePath != null && CheckCellValueFile.trim().endsWith(".xlsx"))
			{
				logger.info("Begin compare the exported excel with baseline");
				String source = testDataFolder + CheckCellValueFile;
				String dest = checkRstFolder + CheckCellValueFile;
				logger.info("Copy file " + source + " to " + dest);

				File expectedValueFile = new File(dest);
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				if (!new File(source).exists())
				{
					s1 = false;
					logger.error(source + "deos not exist!");
				}
				else
				{
					FileUtils.copyFile(new File(source), expectedValueFile);
					s1 = Business.verifyExportedFile(dest, exportFilePath, "excel");
				}

			}
			boolean s2 = true;
			if (expectFileName.length() > 0)
			{
				expectFileName = expectFileName.replace("RPADMIN", curUserName);
				File exportedFile = new File(exportFilePath);
				if (!exportedFile.getName().equals(expectFileName))
				{
					s2 = false;
					logger.warn("Exported file name is not expected, exported file is:" + exportedFile.getName());
				}
			}
			if (s1 && s2)
			{
				testRst = true;
				logger.info("Test reult is: Pass");
			}
			else
			{
				logger.warn("Test reult is: Fail");
				copyFailedFileToTestRst(exportFilePath, "ExportForm");
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
			writeTestResultToFile(testRstFile, ID, 16, CaseID, testRst, "ExportForm");
		}
	}

	protected void ExportToCsv(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileType, String Module, String expectFileName, String BaselineFile, String CaseID)
			throws Exception, InterruptedException, DocumentException
	{

		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);
		BaselineFile = testDataFolder + "/" + BaselineFile;
		boolean testRst = false;
		try
		{
			logger.info("============test Export[" + Form + "] To CSV,case id is:" + CaseID + "=============");
			logger.info("Test " + Form + "_" + ProcessDate + "_" + Group);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			String curUserName = listPage.getUserName().toUpperCase();
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			String exportFilePath = formInstancePage.exportToFile(null, null, null, "csv", Module, null);

			boolean s1 = true;
			if (BaselineFile.length() > 0)
			{
				logger.info("Begin compare the downloaded csv file with baseline");
				if (!new File(BaselineFile).exists())
				{
					s1 = false;
					logger.error(BaselineFile + "deos not exist!");
				}
				else
					s1 = Business.verifyExportedFile(BaselineFile, exportFilePath, "csv");
			}

			boolean s2 = true;
			if (expectFileName.length() > 0)
			{
				expectFileName = expectFileName.replace("RPADMIN", curUserName);
				File exportedFile = new File(exportFilePath);
				if (!exportedFile.getName().equals(expectFileName))
				{
					s2 = false;
					logger.warn("Exported file name is not expected, exported file is:" + exportedFile.getName());
				}
			}
			if (s1 && s2)
			{
				testRst = true;
				logger.info("Test reult is: Pass");
			}
			else
			{
				logger.warn("Test reult is: Fail");
				copyFailedFileToTestRst(exportFilePath, "ExportForm");
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
			ExportToFilePage exportToFilePage = new ExportToFilePage(getWebDriverWrapper());
			exportToFilePage.closeExportPage(FileType);
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(testRstFile, ID, 16, CaseID, testRst, "ExportForm");
		}
	}

	protected void ExportToRegulatorFormatInFormPage(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileType, String Module, String compressType,
			String expectFileName, String BaselineFileName, String CheckCellValueFile, String CaseID) throws Exception
	{
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		Form = splitReturn(Form).get(2);
		String BaselineFile = testDataFolder + BaselineFileName;
		boolean testRst = false;
		try
		{
			logger.info("============test Export[" + Form + "] To regulatore format in form page,case id is:" + CaseID + "=============");
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
			String curUserName = listPage.getUserName().toUpperCase();
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

			String exportFilePath = formInstancePage.exportToFile(Group, Form, ProcessDate, FileType, Module, compressType);
			// due to bug in 1.15.0, unzip exported file first
			if (exportFilePath.endsWith(".zip") && !FileType.equalsIgnoreCase("Vanilla"))
			{
				String saveFolderString = new File(exportFilePath).getParent();
				exportFilePath = saveFolderString + "/" + FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
			}
			boolean s1 = true;
			if (exportFilePath.equalsIgnoreCase("Error"))
			{
				if (CheckCellValueFile.equalsIgnoreCase("Error"))
					s1 = true;
				else
					logger.warn("Export to file failed");
			}
			else
			{
				// if (FileType.equalsIgnoreCase("ARBITRARY") ||
				// FileType.equalsIgnoreCase("iFile"))
				// {
				// String saveFolderString = new
				// File(exportFilePath).getParent();
				// exportFilePath = saveFolderString + "/" +
				// FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
				// }

				if (CheckCellValueFile.endsWith(".xlsx"))
				{
					File expectedValueFile = new File(checkRstFolder + CheckCellValueFile);
					if (expectedValueFile.exists())
					{
						expectedValueFile.delete();
					}
					logger.info("Begin check cell value in exported file");
					if (!new File(testDataFolder + CheckCellValueFile).exists())
					{
						s1 = false;
						logger.error(CheckCellValueFile + "deos not exist!");
					}
					else
					{
						FileUtils.copyFile(new File(testDataFolder + CheckCellValueFile), expectedValueFile);
						s1 = Business.verifyExportedFile(expectedValueFile.getAbsolutePath(), exportFilePath, FileType);
					}
				}

				else if (BaselineFileName.length() > 1)
					s1 = Business.verifyExportedFile(BaselineFile, exportFilePath, FileType);

			}

			boolean s2 = true;
			if (expectFileName.length() > 0)
			{
				expectFileName = expectFileName.replace("RPADMIN", curUserName);
				s2 = false;
				File exportedFile = new File(exportFilePath);
				if (expectFileName.contains("exporttime"))
				{
					expectFileName = expectFileName.replace("exporttime", "~");
					String part1 = expectFileName.split("~")[0];
					String part2 = expectFileName.split("~")[1];
					if (exportedFile.getName().startsWith(part1) && exportedFile.getName().endsWith(part2))
						s2 = true;
				}
				else if (exportedFile.getName().equals(expectFileName))
				{
					s2 = true;
				}

				if (!s2)
					logger.warn("Exported file name is not expected, exported file is:" + exportedFile.getName());
			}
			if (s1 && s2)
			{
				testRst = true;
				logger.info("Test reult is: Pass");
			}
			else
			{
				logger.warn("Test reult is: Fail");
				copyFailedFileToTestRst(exportFilePath, "ExportForm");
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
			writeTestResultToFile(testRstFile, ID, 16, CaseID, testRst, "ExportForm");
		}
	}

	protected void ExportToRegulatorFormatInDashBorad(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileType, String Framework, String Taxonomy, String Module,
			String compressType, String expectFileName, String BaselineFileName, String CheckCellValueFile, String CaseID) throws Exception
	{
		String BaselineFile = testDataFolder + BaselineFileName;
		boolean testRst = false;
		ListPage listPage = super.m.listPage;
		listPage.getProductListPage(Regulator, Group, null, null);
		String curUserName = listPage.getUserName().toUpperCase();
		try
		{
			logger.info("============test Export[" + Form + "] To regulatore format in Dashboard,case id is:" + CaseID + "=============");

			String exportFilePath = listPage.ExportToRegulatorFormat(Group, Form, ProcessDate, FileType, Framework, Taxonomy, Module, compressType);
			// due to bug in 1.15.0, unzip exported file first
			if (exportFilePath.endsWith(".zip") && !FileType.equalsIgnoreCase("Vanilla"))
			{
				String saveFolderString = new File(exportFilePath).getParent();
				exportFilePath = saveFolderString + "/" + FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
			}
			boolean s1 = true;
			if (!exportFilePath.equalsIgnoreCase("Error"))
			{
				// if (FileType.equalsIgnoreCase("ARBITRARY") ||
				// FileType.equalsIgnoreCase("iFile"))
				// {
				// String saveFolderString = new
				// File(exportFilePath).getParent();
				// exportFilePath = saveFolderString + "/" +
				// FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
				// }
				if (CheckCellValueFile.endsWith(".xlsx"))
				{
					File expectedValueFile = new File(checkRstFolder + CheckCellValueFile);
					if (expectedValueFile.exists())
					{
						expectedValueFile.delete();
					}
					logger.info("Begin check cell value in exported file");
					if (!new File(testDataFolder + CheckCellValueFile).exists())
					{
						s1 = false;
						logger.error(CheckCellValueFile + "deos not exist!");
					}
					FileUtils.copyFile(new File(testDataFolder + CheckCellValueFile), expectedValueFile);
					s1 = Business.verifyExportedFile(expectedValueFile.getAbsolutePath(), exportFilePath, FileType);
				}
				else if (BaselineFileName.length() > 1)
					s1 = Business.verifyExportedFile(BaselineFile, exportFilePath, FileType);

				boolean s2 = true;
				if (expectFileName.length() > 0)
				{
					expectFileName = expectFileName.replace("RPADMIN", curUserName);
					s2 = false;
					File exportedFile = new File(exportFilePath);
					if (expectFileName.contains("exporttime"))
					{
						expectFileName = expectFileName.replace("exporttime", "~");
						String part1 = expectFileName.split("~")[0];
						String part2 = expectFileName.split("~")[1];
						if (exportedFile.getName().startsWith(part1) && exportedFile.getName().endsWith(part2))
							s2 = true;
					}
					else if (exportedFile.getName().equals(expectFileName))
					{
						s2 = true;
					}

					if (!s2)
						logger.warn("Exported file name is not expected, exported file is:" + exportedFile.getName());
				}
				if (s1 && s2)
				{
					testRst = true;
					logger.info("Test reult is: Pass");
				}
				else
				{
					logger.warn("Test reult is: Fail");
					copyFailedFileToTestRst(exportFilePath, "ExportForm");
				}
			}
			else
			{
				if (CheckCellValueFile.equalsIgnoreCase("Error"))
					testRst = true;
				else
					logger.warn("Export to file failed");
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
			ExportToFilePage exportToFilePage = new ExportToFilePage(getWebDriverWrapper());
			exportToFilePage.closeExportPage(FileType);
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(testRstFile, ID, 16, CaseID, testRst, "ExportForm");
		}
	}

	@Parameters(
	{ "fileName2" })
	@Test
	public void ExportToArbitraryiFile(@Optional String fileName) throws Exception
	{
		if (fileName == null || fileName == null || fileName.equals(""))
			fileName = "ExportForm_Arbitrary.xls";
		Files = createFolderAndCopyFile("ExportForm", fileName);
		testDataFolder = Files.get(0);
		checkRstFolder = Files.get(1);
		testRstFile = new File(Files.get(2));
		boolean testRst = true;
		try
		{
			ListPage listPage = super.m.listPage;

			if (!testRstFile.getName().equalsIgnoreCase(fileName))
				testRstFile = new File(testRstFile.getParent() + fileName);
			File testDataFile = new File(testDataFolderName + "/ExportForm/" + fileName);
			for (int index = 1; index <= ExcelUtil.getRowNums(testDataFile, null); index++)
			{
				ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
				int ID = Integer.parseInt(rowValue.get(0).trim());
				String Run = rowValue.get(5).trim();

				if (Run.trim().equalsIgnoreCase("Y"))
				{
					String Regulator = rowValue.get(1).trim();
					String Group = rowValue.get(2).trim();
					String Form = rowValue.get(3).trim();
					String ProcessDate = rowValue.get(4).trim();
					String Module = rowValue.get(7).trim();
					String CheckSheets = rowValue.get(8).trim();
					String CaseID = rowValue.get(10).trim();

					String formCode = splitReturn(Form).get(0);
					String version = splitReturn(Form).get(1);
					logger.info("============test Export[" + Form + "] To arbtitrary in form page,case id is:" + CaseID + "=============");
					listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
					FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

					String exportFilePath = formInstancePage.exportToFile(Group, Form, ProcessDate, "iFile", Module, null);
					formInstancePage.closeFormInstance();

					String saveFolderString = new File(exportFilePath).getParent();
					exportFilePath = saveFolderString + "/" + FileUtil.unCompress(exportFilePath, saveFolderString).get(0);

					List<String> sheets = ExcelUtil.getAllSheets(new File(exportFilePath));

					for (String sheet : CheckSheets.split(","))
					{
						if (!sheets.contains(sheet))
						{
							testRst = false;
							logger.error("Sheet " + sheet + " deoe not exist in exported arbitrart file");
						}
					}
					writeTestResultToFile(testRstFile, ID, 9, CaseID, testRst, "ExportForm_Arbitrary");
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
		}
	}

	@Parameters(
	{ "fileName1" })
	@Test
	public void testExportFormToFile(@Optional String fileName) throws Exception
	{
		logger.info("File is: " + fileName);
		if (fileName == null || fileName == null || fileName.equals(""))
			fileName = "ExportForm.xls";
		Files = createFolderAndCopyFile("ExportForm", fileName);
		testDataFolder = Files.get(0);
		checkRstFolder = Files.get(1);
		testRstFile = new File(Files.get(2));

		if (!testRstFile.getName().equalsIgnoreCase(fileName))
			testRstFile = new File(testRstFile.getParent() + fileName);
		File testDataFile = new File(testDataFolderName + "/ExportForm/" + fileName);
		for (int index = 1; index <= ExcelUtil.getRowNums(testDataFile, null); index++)
		{
			ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
			int ID = Integer.parseInt(rowValue.get(0).trim());
			String Run = rowValue.get(5).trim();
			if (Run.trim().equalsIgnoreCase("Y"))
			{
				String Regulator = rowValue.get(1).trim();
				String Group = rowValue.get(2).trim();
				String Form = rowValue.get(3).trim();
				String ProcessDate = rowValue.get(4).trim();
				String ExportFrom = rowValue.get(6).trim();
				String FileType = rowValue.get(7).trim();
				String GroupConsolidationType = rowValue.get(8).trim();
				String Framework = rowValue.get(9).trim();
				String Taxonomy = rowValue.get(10).trim();
				String Module = rowValue.get(11).trim();
				String compressType = rowValue.get(12).trim().length() == 0 ? null : rowValue.get(12).trim();
				String expectFileName = rowValue.get(13).trim();
				String BaselineFile = rowValue.get(14).trim();
				String CheckCellValueFile = rowValue.get(15).trim();
				String CaseID = rowValue.get(17).trim();

				Form = splitReturn(Form).get(2);
				if (GroupConsolidationType.length() > 0)
				{
					ListPage listPage = super.m.listPage;
					listPage.setRegulator(Regulator);
					String prefix = listPage.getSelectRegulatorPrefix();
					/*
					 * EntityPage entityPage = listPage.EnterEntityPage(); if
					 * (GroupConsolidationType.equalsIgnoreCase("i"))
					 * entityPage.updateVariable_consolidation(Group, prefix,
					 * "Individual"); else
					 * entityPage.updateVariable_consolidation(Group, prefix,
					 * "Consolidated"); entityPage.backToDashboard();
					 */
					String SQL = "SELECT \"ID\" FROM \"USR_NATIVE_ENTITY\" WHERE \"ENTITY_NAME\"='" + Group + "'";
					String entityId = DBQuery.queryRecord(SQL);

					if (GroupConsolidationType.equalsIgnoreCase("p"))
					{
						logger.info("Set type= Consolidated");
						SQL = "UPDATE \"USR_VARIABLE_VALUE\" SET \"CHAR_VALUE\"='P' WHERE \"ENTITY_ID\"='" + entityId + "' and \"VARIABLE_LABEL\"='Scope of consolidation'  and \"PRODUCT_PREFIX\"='"
								+ prefix + "'";
						DBQuery.update(SQL);
					}
					else if (GroupConsolidationType.equalsIgnoreCase("I"))
					{
						logger.info("Set type= Individual");
						SQL = "UPDATE \"USR_VARIABLE_VALUE\" SET \"CHAR_VALUE\"='I' WHERE \"ENTITY_ID\"='" + entityId + "' and \"VARIABLE_LABEL\"='Scope of consolidation'  and \"PRODUCT_PREFIX\"='"
								+ prefix + "'";
						DBQuery.update(SQL);
					}
				}

				if (FileType.equalsIgnoreCase("excel"))
					ExportToExcel(ID, Regulator, Group, Form, ProcessDate, FileType, Module, expectFileName, CheckCellValueFile, CaseID);
				else if (FileType.equalsIgnoreCase("csv"))
					ExportToCsv(ID, Regulator, Group, Form, ProcessDate, FileType, Module, expectFileName, BaselineFile, CaseID);

				if (ExportFrom.equalsIgnoreCase("Dashboard") && !FileType.equalsIgnoreCase("excel") && !FileType.equalsIgnoreCase("csv"))
					ExportToRegulatorFormatInDashBorad(ID, Regulator, Group, Form, ProcessDate, FileType, Framework, Taxonomy, Module, compressType, expectFileName, BaselineFile, CheckCellValueFile,
							CaseID);
				else if (ExportFrom.equalsIgnoreCase("FormPage") && !FileType.equalsIgnoreCase("excel") && !FileType.equalsIgnoreCase("csv"))
					ExportToRegulatorFormatInFormPage(ID, Regulator, Group, Form, ProcessDate, FileType, Module, compressType, expectFileName, BaselineFile, CheckCellValueFile, CaseID);

			}
		}

	}
}
