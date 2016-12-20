package com.lombardrisk.testCase_TS;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.pages.AllocationPage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Created by leo tu on 12/14/2016.
 */
public class Admin_FormVariable extends TestTemplate
{
	@Test
	public void test6673() throws Exception
	{
		// Toolset environment
		String caseID = "6673";
		logger.info("==== Verify entity variable in sum rule work well for all instance with toolset DB[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String referenceDate = testData.get(3).trim();
			String instanceName1 = testData.get(4).trim();
			// String instanceName2 = testData.get(5).trim();
			String cellName1 = testData.get(6).trim();
			String cellName2 = testData.get(7).trim();
			String cellName3 = testData.get(8).trim();
			String value1 = testData.get(9).trim();
			String value2 = testData.get(10).trim();
			String value3 = testData.get(11).trim();
			String value4 = testData.get(12).trim();
			String value5 = testData.get(13).trim();
			String columnName = testData.get(14).trim();
			String expression = testData.get(15).trim();
			String itemName = testData.get(16).trim();
			String entityValue = testData.get(17).trim();

			// Create the new return CRIR
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, "All", "All");
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.editCellValue(cellName1, value1);
			formInstancePage.editCellValue(cellName2, value3);
			formInstancePage.addInstance(instanceName1);
			formInstancePage.editCellValue(cellName1, value2);
			formInstancePage.editCellValue(cellName2, value4);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value5);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName3);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, entityValue, "", "", ""));
			testRst = true;
		}
		catch (Throwable e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6674() throws Exception
	{
		// Toolset environment
		String caseID = "6674";
		logger.info("==== Verify entity variable in sum rule work well for each instance with toolset DB[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String referenceDate = testData.get(3).trim();
			String page = testData.get(4).trim();
			String cellName1 = testData.get(5).trim();
			String cellName2 = testData.get(6).trim();
			String value1 = testData.get(7).trim();
			String value2 = testData.get(8).trim();
			String columnName = testData.get(9).trim();
			String expression = testData.get(10).trim();
			String itemName = testData.get(11).trim();
			String entityValue = testData.get(12).trim();

			// Create the new return CRIR
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, "All", "All");
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(cellName1, value1);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName2), value2);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName2);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, entityValue, "", "", ""));
			testRst = true;
		}
		catch (Throwable e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6675() throws Exception
	{
		// Toolset environment
		String caseID = "6675";
		logger.info("==== Verify form variable in sum rule work well for all instance with toolset DB[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String referenceDate = testData.get(3).trim();
			String instanceName1 = testData.get(4).trim();
			// String instanceName2 = testData.get(5).trim();
			String cellName1 = testData.get(6).trim();
			String cellName2 = testData.get(7).trim();
			String cellName3 = testData.get(8).trim();
			String value1 = testData.get(9).trim();
			String value2 = testData.get(10).trim();
			String value3 = testData.get(11).trim();
			String value4 = testData.get(12).trim();
			String value5 = testData.get(13).trim();
			String columnName = testData.get(14).trim();
			String expression = testData.get(15).trim();
			String itemName = testData.get(16).trim();
			String entityValue = testData.get(17).trim();

			// Create the new return CRIR
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, "All", "All");
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.editCellValue(cellName1, value1);
			formInstancePage.editCellValue(cellName2, value3);
			formInstancePage.addInstance(instanceName1);
			formInstancePage.editCellValue(cellName1, value2);
			formInstancePage.editCellValue(cellName2, value4);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value5);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName3);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, entityValue, "", "", ""));
			testRst = true;
		}
		catch (Throwable e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6676() throws Exception
	{
		// Toolset environment
		String caseID = "6676";
		logger.info("==== Verify form variable in sum rule work well for each instance with toolset DB[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String referenceDate = testData.get(3).trim();
			String page = testData.get(4).trim();
			String cellName1 = testData.get(5).trim();
			String cellName2 = testData.get(6).trim();
			String value1 = testData.get(7).trim();
			String value2 = testData.get(8).trim();
			String columnName = testData.get(9).trim();
			String expression = testData.get(10).trim();
			String itemName = testData.get(11).trim();
			String entityValue = testData.get(12).trim();

			// Create the new return CRIR
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, "All", "All");
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(cellName1, value1);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName2), value2);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName2);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, entityValue, "", "", ""));
			testRst = true;
		}
		catch (Throwable e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6748() throws Exception
	{
		// Toolset environment
		String caseID = "6748";
		logger.info("==== Verify entity variable in sum rule of extend grid work well with toolset DB[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String referenceDate = testData.get(3).trim();
			String page = testData.get(4).trim();
			String cellName1 = testData.get(5).trim();
			String cellName2 = testData.get(6).trim();
			String cellName3 = testData.get(7).trim();
			String value1 = testData.get(8).trim();
			String value2 = testData.get(9).trim();
			String value3 = testData.get(10).trim();
			String columnName = testData.get(11).trim();
			String expression = testData.get(12).trim();
			String itemName = testData.get(13).trim();
			String entityValue = testData.get(14).trim();
			String cellId = testData.get(15).trim();

			// Create the new return CRIR
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.insertRow(cellId);
			formInstancePage.editCellValue(cellName1, value1);
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(cellName2, value2);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value3);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName3);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, entityValue, "", "", ""));
			testRst = true;
		}
		catch (Throwable e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6751() throws Exception
	{
		// Toolset environment
		String caseID = "6751";
		logger.info("==== Verify form variable in sum rule of extend grid work well with toolset DB[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String referenceDate = testData.get(3).trim();
			String page = testData.get(4).trim();
			String cellName1 = testData.get(5).trim();
			String cellName2 = testData.get(6).trim();
			String cellName3 = testData.get(7).trim();
			String value1 = testData.get(8).trim();
			String value2 = testData.get(9).trim();
			String value3 = testData.get(10).trim();
			String columnName = testData.get(11).trim();
			String expression = testData.get(12).trim();
			String itemName = testData.get(13).trim();
			String entityValue = testData.get(14).trim();
			String cellId = testData.get(15).trim();

			// Create the new return CRIR
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.insertRow(cellId);
			formInstancePage.editCellValue(cellName1, value1);
			formInstancePage.editCellValue(cellName2, value2);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value3);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName3);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, entityValue, "", "", ""));
			testRst = true;
		}
		catch (Throwable e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6759() throws Exception
	{
		// Toolset environment
		String caseID = "6759";
		logger.info("====Verify entity variable in function sum rule of extend grid work well with toolset DB[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String referenceDate = testData.get(3).trim();
			String page = testData.get(4).trim();
			String cellName1 = testData.get(5).trim();
			String cellName2 = testData.get(6).trim();
			String value1 = testData.get(7).trim();
			String value2 = testData.get(8).trim();
			String columnName = testData.get(9).trim();
			String expression = testData.get(10).trim();
			String itemName = testData.get(11).trim();
			String entityValue = testData.get(12).trim();
			String cellId = testData.get(13).trim();

			// Create the new return CRIR
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.insertRow(cellId);
			formInstancePage.editCellValue(cellName1, value1);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName2), value2);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName2);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, entityValue, "", "", ""));
			testRst = true;
		}
		catch (Throwable e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6760() throws Exception
	{
		// Toolset environment
		String caseID = "6760";
		logger.info("====Verify form variable in function sum rule of extend grid work well with toolset DB[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String referenceDate = testData.get(3).trim();
			String page = testData.get(4).trim();
			String cellName1 = testData.get(5).trim();
			String cellName2 = testData.get(6).trim();
			String value1 = testData.get(7).trim();
			String value2 = testData.get(8).trim();
			String columnName = testData.get(9).trim();
			String expression = testData.get(10).trim();
			String itemName = testData.get(11).trim();
			String entityValue = testData.get(12).trim();
			String cellId = testData.get(13).trim();

			// Create the new return CRIR
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(regulator, entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.insertRow(cellId);
			formInstancePage.editCellValue(cellName1, value1);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName2), value2);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName2);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, entityValue, "", "", ""));
			testRst = true;
		}
		catch (Throwable e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}
}
