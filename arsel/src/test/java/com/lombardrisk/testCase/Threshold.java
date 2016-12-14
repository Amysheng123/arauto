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
 * Create by Leo Tu on 9/8/2016
 */

public class Threshold extends TestTemplate
{
	String importFolder = System.getProperty("user.dir") + "/data_ar/Threshold/ImportFile/";

	@Test
	public void test6218() throws Exception
	{
		String caseID = "6218";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3);
			String Strikethrough = testdata.get(4);
			String NoStrikethrough = testdata.get(5);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}

				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID + ",6219", testRst, "Threshold");
		}
	}

	@Test
	public void test6220() throws Exception
	{
		String caseID = "6220";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3).replace("~", "&");
			String Strikethrough = testdata.get(4);
			String NoStrikethrough = testdata.get(5);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
						s1 = false;
					}
				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6221() throws Exception
	{
		String caseID = "6221";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3).replace("~", "&");
			String Strikethrough = testdata.get(4);
			String NoStrikethrough = testdata.get(5);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}

				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6299() throws Exception
	{
		String caseID = "6299";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String FileName2 = testdata.get(3);
			String Page = testdata.get(4).replace("~", "&");
			String Strikethrough = testdata.get(5);
			String NoStrikethrough = testdata.get(6);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			importFile = new File(importFolder + "/" + FileName2);
			formInstancePage.importAdjustment(importFile, true, false);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}

				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6301() throws Exception
	{
		String caseID = "6301";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String FileName2 = testdata.get(3);
			String Page = testdata.get(4).replace("~", "&");
			String Strikethrough = testdata.get(5);
			String NoStrikethrough = testdata.get(6);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			importFile = new File(importFolder + "/" + FileName2);
			formInstancePage.importAdjustment(importFile, false, false);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}

				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6313() throws Exception
	{
		String caseID = "6313";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String FileName2 = testdata.get(3);
			String Page = testdata.get(4).replace("~", "&");
			String Strikethrough = testdata.get(5);
			String NoStrikethrough = testdata.get(6);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			importFile = new File(importFolder + "/" + FileName2);
			formInstancePage.importAdjustment(importFile, true, false);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}

				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6314() throws Exception
	{
		String caseID = "6314";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String FileName2 = testdata.get(3);
			String Page = testdata.get(4).replace("~", "&");
			String Strikethrough = testdata.get(5);
			String NoStrikethrough = testdata.get(6);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			importFile = new File(importFolder + "/" + FileName2);
			formInstancePage.importAdjustment(importFile, false, false);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}
				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6229() throws Exception
	{
		String caseID = "6229";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3).replace("~", "&");
			String editCell = testdata.get(4);
			String Strikethrough = testdata.get(5);
			String NoStrikethrough = testdata.get(6);
			String editCell2 = testdata.get(7);
			String NoStrikethrough2 = testdata.get(8);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String cell = editCell.split("#")[0];
			String pageName = editCell.replace("@", "#").split("#")[1];
			cell = editCell.replace("@", "#").split("#")[0];
			String instance = null;
			if (pageName.contains("["))
			{
				String tmp = pageName;
				pageName = tmp.replace("[", "#").split("#")[0];
				instance = tmp.replace("[", "#").split("#")[1].replace("]", "");
			}

			String editValue = editCell.split("#")[1];
			formInstancePage.selectPage(pageName);
			formInstancePage.selectInstance(instance);
			formInstancePage.editCellValue(cell, editValue);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}
				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			boolean s3 = false;
			cell = editCell2.split("#")[0];
			editValue = editCell2.split("#")[1];
			formInstancePage.editCellValue(cell, editValue);

			if (!formInstancePage.isStrikethrough(NoStrikethrough2))
				s3 = true;
			else
			{
				logger.error("Instance[" + instance + "] row[" + NoStrikethrough2 + "] should not have strikethrough");
			}

			if (s1 && s2 && s3)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6302() throws Exception
	{
		String caseID = "6302";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3).replace("~", "&");
			String insertRow = testdata.get(4);
			String edit1 = testdata.get(5);
			String check1 = testdata.get(6);
			String edit2 = testdata.get(7);
			String check2 = testdata.get(8);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			formInstancePage.selectPage(Page);
			formInstancePage.insertRow(insertRow);
			for (String item : edit1.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}

			boolean s1 = false;
			String cell = check1.split("#")[0];
			String value = check1.split("#")[1];

			String cellValue = formInstancePage.getCellText(cell);
			if (cellValue.equals(value))
				s1 = true;

			formInstancePage.insertRow(insertRow);
			for (String item : edit2.split("/"))
			{
				cell = item.split("#")[0];
				value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}

			boolean s2 = false;
			cell = check2.split("#")[0];
			value = check2.split("#")[1];

			cellValue = formInstancePage.getCellText(cell);
			if (cellValue.equals(value))
				s2 = true;

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6308() throws Exception
	{
		String caseID = "6308";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3).replace("~", "&");
			String addInstace1 = testdata.get(4);
			String edit1 = testdata.get(5);
			String addInstace2 = testdata.get(6);
			String insert1 = testdata.get(7);
			String insert2 = testdata.get(8);
			String edit21 = testdata.get(9);
			String edit22 = testdata.get(10);
			String edit23 = testdata.get(11);
			String Strikethrough = testdata.get(12);
			String NoStrikethrough = testdata.get(13);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String pageName = addInstace1.split("#")[0];
			String instance = addInstace1.split("#")[1];

			formInstancePage.selectPage(pageName);
			formInstancePage.addInstance(instance);
			for (String item : edit1.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}

			pageName = addInstace2.split("#")[0];
			instance = addInstace2.split("#")[1];

			formInstancePage.selectPage(pageName);
			formInstancePage.addInstance(instance);
			formInstancePage.insertRow(insert1);
			for (String item : edit21.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}

			formInstancePage.insertRow(insert2);
			for (String item : edit22.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}
			formInstancePage.insertRow(insert2);
			for (String item : edit23.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}
				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6310() throws Exception
	{
		String caseID = "6310";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3);
			String Strikethrough = testdata.get(4);
			String NoStrikethrough = testdata.get(5);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			formInstancePage.selectPage(Page);
			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(rowIndex))
					{
						s1 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should have strikethrough");
					}

				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String instance = item.replace("[", "#").split("#")[1].replace("]", "");
				formInstancePage.selectInstance(instance);
				String rows = item.replace("[", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(rowIndex))
					{
						s2 = false;
						logger.error("Instance[" + instance + "] row[" + rowIndex + "] should not have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID + ",6323", testRst, "Threshold");
		}
	}

	@Test
	public void test6330() throws Exception
	{
		String caseID = "6330";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3);
			String Cell = testdata.get(4);
			String Value = testdata.get(5);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);

			File importFile = new File(importFolder + "/" + FileName);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			formInstancePage.selectPage(Page);
			String cellValue = formInstancePage.getCellText(Cell);
			if (Value.equals(cellValue))
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@SuppressWarnings("static-access")
	@Test
	public void test6331() throws Exception
	{
		String caseID = "6331";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String page = testdata.get(3);
			String cell = testdata.get(4);
			String Validation = testdata.get(5);

			List<String> Files = createFolderAndCopyFile("Threshold", null);
			;

			String testDataFolder = Files.get(0);
			String checkCellFileFolder = Files.get(1);

			File importFile = new File(importFolder + "/" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String editValue = cell.split("#")[1];
			String instance = cell.split("#")[0].replace("[", "#").split("#")[1].replace("]", "");
			String editCell = cell.split("#")[0].replace("[", "#").split("#")[0];

			formInstancePage.selectPage(page);
			formInstancePage.selectInstance(instance);
			formInstancePage.editCellValue(editCell, editValue);

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
			File compareRstFile = new File(targetLogFolder + "/rule_compareRst.txt");
			if (compareRstFile.exists())
				compareRstFile.delete();
			String exporteFile = formInstancePage.exportValidationResult();
			long startTime = System.currentTimeMillis();
			String commons[] =
			{ parentPath + "/public/extension/GetRuleResult/GetRuleResult.exe", exporteFile, expectedValueFile.getAbsolutePath(), targetLogFolder, "Y" };
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
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6223() throws Exception
	{
		String caseID = "6223";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Module = testdata.get(3);
			String CSV = testdata.get(4);
			String Vanilla = testdata.get(5);

			List<String> Files = createFolderAndCopyFile("Threshold", null);
			String testDataFolder = Files.get(0);

			File importFile = new File(importFolder + "/" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);

			String exportFilePath = formInstancePage.exportToFile(Entity, null, null, "csv", null, null);
			boolean s1 = Business.verifyExportedFile(testDataFolder + CSV, exportFilePath, "csv");

			exportFilePath = formInstancePage.exportToFile(Entity, null, null, "Vanilla", Module, null);
			boolean s2 = Business.verifyExportedFile(testDataFolder + Vanilla, exportFilePath, "Vanilla");

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID + ",6225", testRst, "Threshold");
		}
	}

	@Test
	public void test6228() throws Exception
	{
		String caseID = "6228";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Page = testdata.get(4);
			String Strikethrough = testdata.get(5);
			String NoStrikethrough = testdata.get(6);

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

			boolean s1 = true;
			for (String item : Strikethrough.split("#"))
			{
				String startCell = item.replace("@", "#").split("#")[1];
				String rows = item.replace("@", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (!formInstancePage.isStrikethrough(startCell, rowIndex))
					{
						s1 = false;
						logger.error("Row[" + rowIndex + "] should have strikethrough");
					}
				}
			}
			boolean s2 = true;
			for (String item : NoStrikethrough.split("#"))
			{
				String startCell = item.replace("@", "#").split("#")[1];
				String rows = item.replace("@", "#").split("#")[0];
				for (String rowIndex : rows.split(","))
				{
					if (formInstancePage.isStrikethrough(startCell, rowIndex))
					{
						s2 = false;
						logger.error("Row[" + rowIndex + "] should have strikethrough");
					}
				}
			}

			if (s1 && s2)
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}

	@Test
	public void test6372() throws Exception
	{
		String caseID = "6372";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Threshold, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String FileName = testdata.get(2);
			String Page = testdata.get(3);
			String Cell = testdata.get(4);
			String Label = testdata.get(5);

			File importFile = new File(importFolder + "/" + FileName);
			ListPage listPage = super.m.listPage;

			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			Page = Page.replace("[", "#").replace("]", "");
			String PageName = Page.split("#")[0];
			String instance = Page.split("#")[1];
			formInstancePage.selectPage(PageName);
			formInstancePage.selectInstance(instance);

			String editCell = Cell.split("#")[0];
			String editValue = Cell.split("#")[1];

			formInstancePage.editCellValue(editCell, editValue);
			if (!formInstancePage.isStrikethroughByValue(Label))
				testRst = true;

		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			logger.error(e.getMessage());
			testRst = false;
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Threshold");
		}
	}
}
