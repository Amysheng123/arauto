package com.lombardrisk.testCase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.pages.PreferencePage;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.CsvUtil;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.XMLUtil;

/**
 * Created by zhijun dai on 11/28/2016.
 */
public class ImportExportFormat extends TestTemplate
{
	String importFolder = System.getProperty("user.dir") + "\\data_ar\\ImportExportFormat\\";

	@Test
	public void test6504() throws Exception
	{
		String caseID = "6504";
		logger.info("Verify the date format of exported CSV file is correct when Regional language is China_Chinese");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String cellID = testdata.get(5);
			String cellValue = testdata.get(6);
			String comment = testdata.get(7);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);
			formInstancePage.editCellValue(cellID, cellValue);

			String exportFilePath = formInstancePage.exportToFile(referenceDate, returnName, referenceDate, "csv", null, null);

			System.out.println(exportFilePath);
			List<String> list = CsvUtil.readFile(new File(exportFilePath));
			Assert.assertTrue(list.get(0).contains(comment));
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).contains(cellID))
				{
					String str = list.get(i).split(",")[2];
					String actualDate = str.substring(1, str.length() - 1);
					Pattern pattern = Pattern.compile("^\\d{4}-[0-1][0-9]-[0-3][0-9]$");
					Assert.assertTrue(pattern.matcher(actualDate).matches());
					break;
				}
			}
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6505() throws Exception
	{
		String caseID = "6505";
		logger.info("Verify the date format of exported CSV file is correct when Regional language is UK_English");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String cellID = testdata.get(5);
			String cellValue = testdata.get(6);
			String comment = testdata.get(7);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);
			formInstancePage.editCellValue(cellID, cellValue);

			String exportFilePath = formInstancePage.exportToFile(referenceDate, returnName, referenceDate, "csv", null, null);

			System.out.println(exportFilePath);
			List<String> list = CsvUtil.readFile(new File(exportFilePath));
			Assert.assertTrue(list.get(0).contains(comment));
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).contains(cellID))
				{
					String str = list.get(i).split(",")[2];
					String actualDate = str.substring(1, str.length() - 1);
					Pattern pattern = Pattern.compile("^[0-3][0-9]/[0-1][0-9]/\\d{4}$");
					Assert.assertTrue(pattern.matcher(actualDate).matches());
					break;
				}
			}
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6506() throws Exception
	{
		String caseID = "6506";
		logger.info("Verify the date format of exported CSV file is correct when Regional language is US_English");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String cellID = testdata.get(5);
			String cellValue = testdata.get(6);
			String comment = testdata.get(7);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);
			formInstancePage.editCellValue(cellID, cellValue);

			String exportFilePath = formInstancePage.exportToFile(referenceDate, returnName, referenceDate, "csv", null, null);

			System.out.println(exportFilePath);
			List<String> list = CsvUtil.readFile(new File(exportFilePath));
			Assert.assertTrue(list.get(0).contains(comment));
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).contains(cellID))
				{
					String str = list.get(i).split(",")[2];
					String actualDate = str.substring(1, str.length() - 1);
					Pattern pattern = Pattern.compile("^[0-1][0-9]/[0-3][0-9]/\\d{4}$");
					Assert.assertTrue(pattern.matcher(actualDate).matches());
					break;
				}
			}
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6518() throws Exception
	{
		String caseID = "6518";
		logger.info("Verify CSV file of yyyy-mm-dd date format can be uploaded and imported properly");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String fileName = testdata.get(5);
			String cellID = testdata.get(6);
			String value = testdata.get(7);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			File importFile = new File(importFolder + "\\" + fileName);
			formInstancePage.importAdjustment(importFile, false, false);
			String cellValue = formInstancePage.getCellText(cellID);
			Assert.assertEquals(cellValue, value);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6514() throws Exception
	{
		String caseID = "6514";
		logger.info("Dashboard EI-01-08 Verify CSV file of dd mm yyyy date format can be uploaded and imported properly");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String fileName = testdata.get(5);
			String cellID = testdata.get(6);
			String value = testdata.get(7);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			File importFile = new File(importFolder + "\\" + fileName);
			formInstancePage.importAdjustment(importFile, false, false);
			String cellValue = formInstancePage.getCellText(cellID);
			Assert.assertEquals(cellValue, value);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6517() throws Exception
	{
		String caseID = "6517";
		logger.info("Dashboard EI-01-09 Verify CSV file of mm dd yyyy date format can be uploaded and imported properly");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String fileName = testdata.get(5);
			String cellID = testdata.get(6);
			String value = testdata.get(7);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			File importFile = new File(importFolder + "\\" + fileName);
			formInstancePage.importAdjustment(importFile, false, false);
			String cellValue = formInstancePage.getCellText(cellID);
			Assert.assertEquals(cellValue, value);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6563() throws Exception
	{
		String caseID = "6563";
		logger.info("Dashboard EI-01-14 Verify CSV file can be uploaded and imported properly when date format is not same as Preferences settings");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String fileName = testdata.get(5);
			String cellID = testdata.get(6);
			String value = testdata.get(7);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			File importFile = new File(importFolder + "\\" + fileName);
			formInstancePage.importAdjustment(importFile, false, false);
			String cellValue = formInstancePage.getCellText(cellID);
			Assert.assertEquals(cellValue, value);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6521() throws Exception
	{
		String caseID = "6521";
		logger.info("Dashboard EI-02-01 Verify error message will popup when DateFormat commentary does not exist in the CSV file");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String fileName = testdata.get(5);
			String value = testdata.get(6);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			File importFile = new File(importFolder + "\\" + fileName);
			String errorMsg = formInstancePage.getImportAdjustmentErrorInfo(importFile);
			Assert.assertEquals(errorMsg, value);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6564() throws Exception
	{
		String caseID = "6564";
		logger.info("Dashboard EI-02-02 Verify error message will popup when DateFormat commentary is not in the recognized form");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String fileName = testdata.get(5);
			String value = testdata.get(6);
			String fileName2 = testdata.get(7);
			String value2 = testdata.get(8);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			File importFile = new File(importFolder + "\\" + fileName);
			String errorMsg = formInstancePage.getImportAdjustmentErrorInfo(importFile);
			Assert.assertEquals(errorMsg, value);

			File importFile2 = new File(importFolder + "\\" + fileName2);
			String errorMsg2 = formInstancePage.getImportAdjustmentErrorInfo(importFile2);
			Assert.assertEquals(errorMsg2, value2);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6534() throws Exception
	{
		String caseID = "6534";
		logger.info("Dashboard EI-02-03 Verify error message will popup when DateFormat commentary in the CSV file NOT supported by AR");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String fileName = testdata.get(5);
			String value = testdata.get(6);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			File importFile = new File(importFolder + "\\" + fileName);
			String errorMsg = formInstancePage.getImportAdjustmentErrorInfo(importFile);
			Assert.assertEquals(errorMsg, value);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6529() throws Exception
	{
		String caseID = "6529";
		logger.info("Dashboard EI-02-04 Verify error message will popup when part of date type value in the CSV file is not same as commentary line");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String language = testdata.get(0);
			String regulator = testdata.get(1);
			String entity = testdata.get(2);
			String referenceDate = testdata.get(3);
			String returnName = testdata.get(4);
			String fileName = testdata.get(5);
			String value = testdata.get(6);

			ListPage listPage = super.m.listPage;
			PreferencePage preferencePage = listPage.enterPreferencePage();
			listPage = preferencePage.selectLanguageByValue(language);
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			File importFile = new File(importFolder + "\\" + fileName);
			String errorMsg = formInstancePage.getImportAdjustmentErrorInfo(importFile).replaceAll("\n|\r", "");
			Assert.assertEquals(errorMsg, value);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6615() throws Exception
	{
		String caseID = "6615";
		logger.info("Verify date and time stamp is added when export return to excel file and date type cell format is specified");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String regulator = testdata.get(0);
			String entity = testdata.get(1);
			String referenceDate = testdata.get(2);
			String returnName = testdata.get(3);
			String time = testdata.get(4);
			String timeZone_expected = testdata.get(5);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);
			String exportFilePath = formInstancePage.exportToFile(referenceDate, returnName, referenceDate, "excel", null, null);
			String exportTime = ExcelUtil.getCellValueByCellName(new File(exportFilePath), "_ExportTime", null, null);
			String timeZone = ExcelUtil.getCellValueByCellName(new File(exportFilePath), "_TimeZone", null, null);
			String date = new SimpleDateFormat(time).format(new Date());
			Assert.assertTrue(exportTime.contains(date));
			Assert.assertEquals(timeZone, timeZone_expected);
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6627() throws Exception
	{
		String caseID = "6627";
		logger.info("Dashboard ETIT-01-05 Verify date and time stamp is added when export return to vanilla file");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String regulator = testdata.get(0);
			String entity = testdata.get(1);
			String referenceDate = testdata.get(2);
			String returnName = testdata.get(3);
			String module = testdata.get(4);
			String format = testdata.get(5);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);
			String filePath = formInstancePage.exportToFile(regulator, returnName, referenceDate, "vanilla", module, null);
			String exportTime = XMLUtil.getcellValueFromVanilla(filePath, "exportTime", null);
			String date = new SimpleDateFormat(format).format(new Date());
			Assert.assertTrue(exportTime.contains(date));
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}

	@Test
	public void test6702() throws Exception
	{
		String caseID = "6702";
		logger.info("Verify date and time stamp is not added when timezone and exporttime are not defined");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_importExportFormat, nodeName);
			String regulator = testdata.get(0);
			String entity = testdata.get(1);
			String referenceDate = testdata.get(2);
			String returnName = testdata.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);
			String exportFilePath = formInstancePage.exportToFile(referenceDate, returnName, referenceDate, "excel", null, null);
			List<String> names = ExcelUtil.getCellNamesFromExcel(new File(exportFilePath));
			Assert.assertTrue(!names.contains("_ExportTime"));
		}
		catch (Throwable e)
		{
			testRst = false;
			e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "ExportFormat");
		}
	}
}
