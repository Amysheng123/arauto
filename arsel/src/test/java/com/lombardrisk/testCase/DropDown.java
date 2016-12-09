package com.lombardrisk.testCase;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.Business;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 9/12/2016
 */

public class DropDown extends TestTemplate
{
	String importFolder = System.getProperty("user.dir") + "/data_ar/DropDown/ImportFile/";

	@Test
	public void test6298() throws Exception
	{
		String caseID = "6298";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_DropDown, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String Page = testdata.get(4);
			String Cell = testdata.get(5);
			String Step6 = testdata.get(6);
			String Step9 = testdata.get(7);
			String Step9_2 = testdata.get(8).replace("~", "&");
			String Step10 = testdata.get(9);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ProcessDate, Form, null, false, false);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			List<String> options = formInstancePage.getAllOptionsFromDropdown(Cell, Step6);
			for (String option : options)
			{
				if (!option.contains(Step6))
				{
					s1 = false;
					break;
				}
			}

			boolean s2 = true;
			options = formInstancePage.getAllOptionsFromDropdown(Cell, Step9);
			for (String option : options)
			{
				if (!option.contains(Step9))
				{
					s2 = false;
					break;
				}
			}

			boolean s3 = true;
			options = formInstancePage.getAllOptionsFromDropdown(Cell, Step9_2);
			for (String option : options)
			{
				if (!option.contains(Step9_2))
				{
					s3 = false;
					break;
				}
			}

			boolean s4 = false;
			formInstancePage.editCellValue(Cell, Step10);
			String cellText = formInstancePage.getCellText(Cell);
			if (cellText.equals(Step10))
				s4 = true;

			if (s1 & s2 & s3 & s4)
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

			writeTestResultToFile(caseID + ",6282,6297", testRst, "DropDown");
		}
	}

	@Test
	public void test6300() throws Exception
	{
		String caseID = "6300";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_DropDown, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String Page = testdata.get(4);
			String Insert = testdata.get(5);
			String Cell = testdata.get(6);
			String Step6 = testdata.get(7);
			String Step9 = testdata.get(8);
			String Step10 = testdata.get(9);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ProcessDate, Form, null, false, false);

			formInstancePage.selectPage(Page);
			formInstancePage.insertRow(Insert);
			boolean s1 = true;
			List<String> options = formInstancePage.getAllOptionsFromDropdown(Cell, Step6);
			for (String option : options)
			{
				if (!option.contains(Step6))
				{
					s1 = false;
					break;
				}
			}

			boolean s2 = true;
			options = formInstancePage.getAllOptionsFromDropdown(Cell, Step9);
			for (String option : options)
			{
				if (!option.contains(Step9))
				{
					s2 = false;
					break;
				}
			}

			boolean s3 = false;
			formInstancePage.editCellValue(Cell, Step10);
			String cellText = formInstancePage.getCellText(Cell);
			if (cellText.equals(Step10))
				s3 = true;

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

			writeTestResultToFile(caseID, testRst, "DropDown");
		}
	}

	@Test
	public void test6303() throws Exception
	{
		String caseID = "6303";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_DropDown, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String Page = testdata.get(4);
			String Edit1 = testdata.get(5);
			String Cell = testdata.get(6);
			String Step7_1 = testdata.get(7);
			String Step7_2 = testdata.get(8);
			String Edit2 = testdata.get(9);
			String Step9_1 = testdata.get(10);
			String Step9_2 = testdata.get(11);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ProcessDate, Form, null, false, false);

			formInstancePage.selectPage(Page);
			String editCell = Edit1.split("#")[0];
			String editValue = Edit1.split("#")[1];
			formInstancePage.editCellValue(editCell, editValue);
			boolean s1 = true;
			List<String> options = formInstancePage.getAllOptionsFromDropdown(Cell, Step7_1);
			if (options.size() == Step7_2.split(",").length)
			{
				for (String option : Step7_2.split(","))
				{
					if (!options.contains(option))
					{
						s1 = false;
						break;
					}
				}
			}
			else
				s1 = false;

			editCell = Edit2.split("#")[0];
			editValue = Edit2.split("#")[1];
			formInstancePage.editCellValue(editCell, editValue);
			boolean s2 = true;
			options = formInstancePage.getAllOptionsFromDropdown(Cell, Step9_1);
			if (options.size() == Step9_2.split(",").length)
			{
				for (String option : Step9_2.split(","))
				{
					if (!options.contains(option))
					{
						s1 = false;
						break;
					}
				}
			}
			else
				s2 = false;

			if (s1 & s2)
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

			writeTestResultToFile(caseID, testRst, "DropDown");
		}
	}

	@Test
	public void test6318() throws Exception
	{
		String caseID = "6318";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_DropDown, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String Page = testdata.get(4);
			String Insert = testdata.get(5);
			String Edit1 = testdata.get(6);
			String Cell = testdata.get(7);
			String Step7_1 = testdata.get(8);
			String Step7_2 = testdata.get(9);
			String Edit2 = testdata.get(10);
			String Step9_1 = testdata.get(11);
			String Step9_2 = testdata.get(12);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ProcessDate, Form, null, false, false);

			formInstancePage.selectPage(Page);
			formInstancePage.insertRow(Insert);
			String editCell = Edit1.split("#")[0];
			String editValue = Edit1.split("#")[1];
			formInstancePage.editCellValue(editCell, editValue);
			boolean s1 = true;
			List<String> options = formInstancePage.getAllOptionsFromDropdown(Cell, Step7_1);
			if (options.size() == Step7_2.split(",").length)
			{
				for (String option : Step7_2.split(","))
				{
					if (!options.contains(option))
					{
						s1 = false;
						break;
					}
				}
			}
			else
				s1 = false;

			editCell = Edit2.split("#")[0];
			editValue = Edit2.split("#")[1];
			formInstancePage.editCellValue(editCell, editValue);
			boolean s2 = true;
			options = formInstancePage.getAllOptionsFromDropdown(Cell, Step9_1);
			if (options.size() == Step9_2.split(",").length)
			{
				for (String option : Step9_2.split(","))
				{
					if (!options.contains(option))
					{
						s1 = false;
						break;
					}
				}
			}
			else
				s2 = false;

			if (s1 & s2)
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
			writeTestResultToFile(caseID + ",6436,6437,6438", testRst, "DropDown");
		}
	}

	@Test
	public void test6374() throws Exception
	{
		String caseID = "6374";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_DropDown, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String Page = testdata.get(4);
			String Insert = testdata.get(5);
			String Edit = testdata.get(6);
			String FileName = testdata.get(7);
			String ExpectedCell_Value = testdata.get(8);
			String CheckCellValueFile = testdata.get(9);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ProcessDate, Form, null, false, false);

			formInstancePage.selectPage(Page);
			formInstancePage.insertRow(Insert);
			for (String item : Edit.split("/"))
			{
				String editCell = item.split("#")[0];
				String editValue = item.split("#")[1];
				formInstancePage.editCellValue(editCell, editValue);
			}
			boolean s1 = true;
			File importFile = new File(importFolder + FileName);
			formInstancePage.importAdjustment(importFile, false, false);
			formInstancePage.selectPage(Page);
			for (String item : ExpectedCell_Value.split("/"))
			{
				String cell = item.split("#")[0];
				String expectedValue = item.split("#")[1];
				String actual = formInstancePage.getCellText(cell);
				if (!actual.equals(expectedValue))
					s1 = false;
			}

			boolean s2 = true;
			List<String> Files = createFolderAndCopyFile("DropDown", null);
			String testDataFolder = Files.get(0);
			String checkRstFolder = Files.get(1);
			String exportFilePath = formInstancePage.exportToFile(null, null, null, "excel", null, null);
			String source = testDataFolder + CheckCellValueFile;
			String dest = checkRstFolder + CheckCellValueFile;
			logger.info("Copy file " + source + " to " + dest);

			File expectedValueFile = new File(dest);
			if (expectedValueFile.exists())
				expectedValueFile.delete();
			if (!new File(source).exists())
			{
				s2 = false;
				logger.error(source + "deos not exist!");
			}
			else
			{
				FileUtils.copyFile(new File(source), expectedValueFile);
				s2 = Business.verifyExportedFile(dest, exportFilePath, "excel");
			}

			if (s1 & s2)
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
			writeTestResultToFile(caseID, testRst, "DropDown");
		}
	}

	@Test
	public void test6605() throws Exception
	{
		String caseID = "6605";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_DropDown, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String Page = testdata.get(4);
			String Insert = testdata.get(5);
			String Cell = testdata.get(6);
			String EditValue = testdata.get(7);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ProcessDate, Form, null, false, false);

			formInstancePage.selectPage(Page);
			formInstancePage.insertRow(Insert);
			boolean s1 = true;
			for (String item : EditValue.split("~"))
			{
				formInstancePage.editCellValue(Cell, item);
				String actual = formInstancePage.getCellText(Cell);
				if (!actual.equals(item))
					s1 = false;
			}

			if (s1)
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
			writeTestResultToFile(caseID, testRst, "DropDown");
		}
	}

	@Test
	public void test6606() throws Exception
	{
		String caseID = "6606";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testdata_DropDown, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ProcessDate = testdata.get(3);
			String Page = testdata.get(4);
			String Insert = testdata.get(5);
			String Cell = testdata.get(6);
			String EditValue = testdata.get(7);
			String ExpectedValue = testdata.get(8);

			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ProcessDate, Form, null, false, false);

			formInstancePage.selectPage(Page);
			formInstancePage.insertRow(Insert);
			boolean s1 = true;

			for (int i = 0; i < EditValue.split("~").length; i++)
			{
				formInstancePage.editCellValue(Cell, EditValue.split("~")[i]);
				String actual = formInstancePage.getCellText(Cell);
				if (!actual.equals(ExpectedValue.split("~")[i]))
					s1 = false;
			}
			if (s1)
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
			writeTestResultToFile(caseID, testRst, "DropDown");
		}
	}
}
