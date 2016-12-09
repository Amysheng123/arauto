package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.FormInstanceRetrievePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 9/13/2016
 */

public class GridWithinGrid extends TestTemplate
{

	@Test
	public void test6202() throws Exception
	{
		String caseID = "6202";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_GridWithinGrid, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Page = testdata.get(4);
			String Cell1 = testdata.get(5);
			String Cell2 = testdata.get(6);
			String Value = testdata.get(7);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.selectPage(Page);

			formInstancePage.insertRow(Cell1);
			formInstancePage.editCellValue(Cell2, Value);

			assertThat(formInstancePage.getCellText(Cell2)).isEqualTo(Value);
			formInstancePage.deleteRow(Cell2);
			assertThat(formInstancePage.getCellText(Cell2)).isEqualTo(null);
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
			writeTestResultToFile(caseID + ",6203", testRst, "GridWithinGrid");
		}
	}

	@Test
	public void test6204() throws Exception
	{
		String caseID = "6204";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_GridWithinGrid, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String CellValueFile = testdata.get(4);

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

			List<String> Files = createFolderAndCopyFile("GridWithinGrid", null);

			String testDataFolder = Files.get(0);
			String checkCellFileFolder = Files.get(1);
			String source = testDataFolder + CellValueFile;
			String dest = checkCellFileFolder + CellValueFile;

			File expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}
			testRst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
			assertThat(testRst).isEqualTo(true);
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
			writeTestResultToFile(caseID + ",6215", testRst, "GridWithinGrid");
		}
	}

	@Test
	public void test6214() throws Exception
	{
		String caseID = "6214";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_GridWithinGrid, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Page = testdata.get(4);
			String Insert1 = testdata.get(5);
			String Step2_1 = testdata.get(6);
			String Insert2 = testdata.get(7);
			String Step3_1 = testdata.get(8);
			String Step3_2 = testdata.get(9);
			String Insert3 = testdata.get(10);
			String Step4_1 = testdata.get(11);
			String Insert4 = testdata.get(12);
			String Step5_1 = testdata.get(13);
			String Insert5 = testdata.get(14);
			String Step6_1 = testdata.get(15);
			String Step6_2 = testdata.get(16);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.selectPage(Page);

			formInstancePage.insertRow(Insert1);
			for (String item : Step2_1.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}

			formInstancePage.insertRow(Insert2);
			for (String item : Step3_1.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}

			for (String item : Step3_2.split("/"))
			{
				String cell = item.split("#")[0];
				String value = formInstancePage.getCellText(cell);
				assertThat(value).isEqualTo(null);
			}

			formInstancePage.insertRow(Insert3);
			for (String item : Step4_1.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}
			formInstancePage.insertRow(Insert4);
			for (String item : Step5_1.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}
			formInstancePage.insertRow(Insert5);
			for (String item : Step6_1.split("/"))
			{
				String cell = item.split("#")[0];
				String value = item.split("#")[1];
				formInstancePage.editCellValue(cell, value);
			}

			for (String item : Step6_2.split("/"))
			{
				String cell = item.split("#")[0];
				String value = formInstancePage.getCellText(cell);
				assertThat(value).isEqualTo(null);
			}

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
			writeTestResultToFile(caseID, testRst, "GridWithinGrid");
		}
	}

	@Test
	public void test6440() throws Exception
	{
		String caseID = "6440";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_GridWithinGrid, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Page = testdata.get(4);
			String Insert1 = testdata.get(5);
			String Insert2 = testdata.get(6);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.selectPage(Page);

			formInstancePage.insertRow(Insert1);
			assertThat(formInstancePage.isExistInsertAbove_Below(Insert2)).isEqualTo(false);
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
			writeTestResultToFile(caseID, testRst, "GridWithinGrid");
		}
	}

	@Test
	public void test6441() throws Exception
	{
		String caseID = "6441";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_GridWithinGrid, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Page = testdata.get(4);
			String Insert1 = testdata.get(5);
			String Insert2 = testdata.get(6);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.selectPage(Page);

			formInstancePage.insertRow(Insert1);
			assertThat(formInstancePage.isExistInsertAbove_Below(Insert2)).isEqualTo(true);
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
			writeTestResultToFile(caseID, testRst, "GridWithinGrid");
		}
	}
}
