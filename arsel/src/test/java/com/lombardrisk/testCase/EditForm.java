package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.AdjustLogPage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;

/**
 * Create by Leo Tu on 8/17/2015
 */
public class EditForm extends TestTemplate
{

	@Test
	public void testC5469() throws Exception
	{
		String caseID = "5469";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, true, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;

			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String displayed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueTo = rowValueList.get(10).trim();
				if (log_valueTo.length() == 0)
					log_valueTo = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (displayed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(displayed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueTo + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC5470() throws Exception
	{
		String caseID = "5470";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, true, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC5472() throws Exception
	{
		String caseID = "5472";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, true, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC5471() throws Exception
	{
		String caseID = "5471";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, true, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC5473() throws Exception
	{
		String caseID = "5473";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, "01/01/2016", true, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC1903() throws Exception
	{
		String caseID = "1903";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, true, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC3278() throws Exception
	{
		String caseID = "3278";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC3274() throws Exception
	{
		String caseID = "3274";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC3273() throws Exception
	{
		String caseID = "3273";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC3275() throws Exception
	{
		String caseID = "3275";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC2564() throws Exception
	{
		String caseID = "2564";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC1715() throws Exception
	{
		String caseID = "1715";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC1756() throws Exception
	{
		String caseID = "1756";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC1714() throws Exception
	{
		String caseID = "1714";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			File exportFile = null;
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC3194() throws Exception
	{
		String caseID = "3194";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC3196() throws Exception
	{
		String caseID = "3196";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC3251() throws Exception
	{
		String caseID = "3251";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Group, ProcessDate, Form, null, false, false);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_value = rowValueList.get(9).trim();
				String log_valueto = rowValueList.get(10).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(11).trim();
				String id = rowValueList.get(12).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}
				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}
				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC5540() throws Exception
	{
		String caseID = "5540";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
					cell2 = cell2.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5548() throws Exception
	{
		String caseID = "5548";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5541() throws Exception
	{
		String caseID = "5541";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5555() throws Exception
	{
		String caseID = "5555";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5542() throws Exception
	{
		String caseID = "5542";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5543() throws Exception
	{
		String caseID = "5543";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			splitReturn(Form).get(0);
			splitReturn(Form).get(1);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}
	}

	@Test
	public void testC5575() throws Exception
	{
		String caseID = "5575";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5576() throws Exception
	{
		String caseID = "5576";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5577() throws Exception
	{
		String caseID = "5577";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRow(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5580() throws Exception
	{
		String caseID = "5580";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);

					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5581() throws Exception
	{
		String caseID = "5581";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5584() throws Exception
	{
		String caseID = "5584";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			listPage.deleteFormInstance(Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String dispalyed = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
					if (dispalyed.length() > 0)
					{
						String actualValue = formInstancePage.getCellText(editCell).trim();
						assertThat(actualValue).isEqualTo(dispalyed);
					}
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);

					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5564() throws Exception
	{
		String caseID = "5564";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String ErrorMessage = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					String msg = null;
					if (ErrorMessage.length() > 0)
					{
						msg = formInstancePage.editCellValueGetErrorMsg(null, cell1, cell2, editValue, comment);
						assertThat(msg).isEqualTo(ErrorMessage);
					}
					else
						formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);

					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5565() throws Exception
	{
		String caseID = "5565";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String ErrorMessage = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					String msg = null;
					if (ErrorMessage.length() > 0)
					{
						msg = formInstancePage.editCellValueGetErrorMsg(null, cell1, cell2, editValue, comment);
						assertThat(msg).isEqualTo(ErrorMessage);
					}
					else
						formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);
				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5566() throws Exception
	{
		String caseID = "5566";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String ErrorMessage = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					String msg = null;
					if (ErrorMessage.length() > 0)
					{
						msg = formInstancePage.editCellValueGetErrorMsg(null, cell1, cell2, editValue, comment);
						assertThat(msg).isEqualTo(ErrorMessage);
					}
					else
						formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);

				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "EditForm");
		}

	}

	@Test
	public void testC5567() throws Exception
	{
		String caseID = "5567";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_editForm, nodeName);
			String Regulator = testData.get(0);
			String Group = testData.get(1);
			String Form = testData.get(2);
			String ProcessDate = testData.get(3);
			File ImportFile = new File(testData_editForm.replace("EditForm.xml", "ImportFile/") + getElementValueFromXML(testData_editForm, nodeName, "ImportFile").trim());

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			listPage.deleteFormInstance(Form, ProcessDate);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(ImportFile, false, false, true);
			int rowAmt = ExcelUtil.getRowAmts(editFormLogData, caseID);
			File exportFile = null;
			for (int i = 1; i <= rowAmt; i++)
			{
				ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(editFormLogData, caseID, i);
				String insertRow = rowValueList.get(0).trim();
				String deleteRow = rowValueList.get(1).trim();
				String addInstance = rowValueList.get(2).trim();
				String delInstance = rowValueList.get(3).trim();
				String editCell = rowValueList.get(4).trim();
				String editValue = rowValueList.get(5).trim();
				if (editValue.equals(""))
					editValue = null;
				String comment = rowValueList.get(6).trim();
				String ErrorMessage = rowValueList.get(7).trim();
				String log_cell = rowValueList.get(8).trim();
				String log_instance = rowValueList.get(9).trim();
				String log_gridKey = rowValueList.get(10).trim();
				String log_value = rowValueList.get(11).trim();
				String log_valueto = rowValueList.get(12).trim();
				if (log_valueto.length() == 0)
					log_valueto = "NULL";
				String log_comment = rowValueList.get(13).trim();
				String id = rowValueList.get(14).trim();
				int logId = 0;
				if (!id.equals(""))
					logId = Integer.parseInt(id);

				if (addInstance.length() > 0)
					formInstancePage.addInstance(addInstance);
				if (delInstance.length() > 0)
					formInstancePage.deletePageInstance(delInstance);

				if (insertRow.length() > 3)
				{
					if (insertRow.contains("[") && insertRow.contains("]"))
					{
						String instance = insertRow.substring(insertRow.indexOf("[") + 1, insertRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						insertRow = insertRow.replace("[" + instance + "]", "");
					}
					formInstancePage.insertRowAbove(insertRow);
				}
				if (deleteRow.length() > 3)
				{
					if (deleteRow.contains("[") && deleteRow.contains("]"))
					{
						String instance = deleteRow.substring(deleteRow.indexOf("[") + 1, deleteRow.indexOf("]"));
						formInstancePage.selectInstance(instance);
						deleteRow = deleteRow.replace("[" + instance + "]", "");
					}
					formInstancePage.deleteRow(deleteRow);
				}

				String cell1 = editCell;
				String cell2 = null;
				if (editCell.startsWith("ExtDBGrid1"))
				{
					cell1 = editCell.substring(12);
					cell2 = editCell;
					cell2 = cell2.replace("[", "~").split("~")[0];
				}
				if (cell1.contains("[") && cell1.contains("]"))
				{
					String instance = cell1.substring(cell1.indexOf("[") + 1, cell1.indexOf("]"));
					formInstancePage.selectInstance(instance);
					cell1 = cell1.replace("[" + instance + "]", "");
				}

				if (!editCell.equals(""))
				{
					String msg = null;
					if (ErrorMessage.length() > 0)
					{
						msg = formInstancePage.editCellValueGetErrorMsg(null, cell1, cell2, editValue, comment);
						assertThat(msg).isEqualTo(ErrorMessage);
					}
					else
						formInstancePage.editCellValue(null, cell1, cell2, editValue, comment);

				}

				if (log_cell.length() > 1)
				{
					String expectedLog = log_cell + "#" + log_instance + "#" + log_value + "#" + log_valueto + "#" + log_comment;
					logger.info("Expected log is:" + expectedLog);

					AdjustLogPage adjustLogPage = formInstancePage.getDrillDown();

					if (!adjustLogPage.getFilterCell().equalsIgnoreCase(log_cell))
					{
						adjustLogPage.inputCellText(log_cell);
						exportFile = new File(adjustLogPage.exportAdjustment());
					}
					List<String> actualLogs = ExcelUtil.getRowValueFromExcel(exportFile, null, logId);
					String actualLog = actualLogs.get(0) + "#" + actualLogs.get(1) + "#" + actualLogs.get(3) + "#" + actualLogs.get(4) + "#" + actualLogs.get(7);
					logger.error("Actual log is[" + actualLog + "]");
					assertThat(actualLog).isEqualTo(expectedLog);
					// if (!log_gridKey.equalsIgnoreCase(""))
					// {
					// if (log_gridKey.equalsIgnoreCase("XXX"))
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(null);
					// }
					// else
					// {
					// assertThat(actualLogs.get(2)).isEqualTo(log_gridKey);
					// }
					// }
				}
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID + ",6334,6356", testRst, "EditForm");
		}

	}
}
