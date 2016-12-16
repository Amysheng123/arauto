package com.lombardrisk.testCase_TS;

import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.ErrorListPage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.pages.ValidationPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Created by leo tu on 4/19/2016.
 */
public class HighLight extends TestTemplate
{
	@Test
	public void test5769() throws Exception
	{
		boolean testRst = true;
		String caseID = "5769";
		logger.info("====Verify the highlight color is right when cell is failed with X-validation rules,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_highlight, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Reds = testdata.get(4);
			String Yelolows = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.validationNowClick();

			for (String cellName : Reds.split("#"))
			{
				String color = formInstancePage.getCellColor(Regulator, formCode, version, cellName);
				if (!color.equalsIgnoreCase("Red"))
				{
					testRst = false;
					logger.error("Expected color is red, but actual color is " + color);
				}
			}
			for (String cellName : Yelolows.split("#"))
			{
				String color = formInstancePage.getCellColor(Regulator, formCode, version, cellName);
				if (!color.equalsIgnoreCase("Yellow"))
				{
					testRst = false;
					logger.error("Expected color is yellow, but actual color is " + color);
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5772() throws Exception
	{
		boolean testRst = true;
		String caseID = "5772";
		logger.info("====Verify the highlight color is right when cell is failed with X-validation rules,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_highlight, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Oranges = testdata.get(4);
			String Yelolows = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.validationNowClick();

			for (String cellName : Yelolows.split("#"))
			{
				if (cellName.toUpperCase().contains("[ALL]"))
				{
					cellName = cellName.toUpperCase().replace("[ALL]", "");
					for (String instance : formInstancePage.getAllInstance(Regulator, formCode, version, cellName))
					{
						String color = formInstancePage.getCellColor(Regulator, formCode, version, instance, cellName);
						if (!color.equalsIgnoreCase("Yellow"))
						{
							testRst = false;
							logger.error("Expected color is yellow, but actual color is " + color);
						}
					}
				}
			}

			for (String cellName : Oranges.split("#"))
			{
				if (cellName.toUpperCase().contains("[ALL]"))
				{
					cellName = cellName.toUpperCase().replace("[ALL]", "");
					for (String instance : formInstancePage.getAllInstance(Regulator, formCode, version, cellName))
					{
						String color = formInstancePage.getCellColor(Regulator, formCode, version, instance, cellName);
						if (!color.equalsIgnoreCase("Orange"))
						{
							testRst = false;
							logger.error("Expected color is Orange, but actual color is " + color);
						}
					}
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5773() throws Exception
	{
		boolean testRst = true;
		String caseID = "5773";
		logger.info("====Verify the highlight color is right when cell is failed with U-validation rules,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_highlight, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Reds = testdata.get(4);
			String Yelolows = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.validationNowClick();

			for (String cellName : Reds.split("#"))
			{
				if (cellName.toUpperCase().contains("[ALL]"))
				{
					cellName = cellName.toUpperCase().replace("[ALL]", "");
					List<String> instances = formInstancePage.getAllInstance(Regulator, formCode, version, cellName);
					for (String instance : instances)
					{
						String color = formInstancePage.getCellColor(Regulator, formCode, version, instance, cellName);
						if (!color.equalsIgnoreCase("Red"))
						{
							testRst = false;
							logger.error("Expected color is red, but actual color is " + color);
						}
					}
				}
				else
				{
					String color = formInstancePage.getCellColor(Regulator, formCode, version, cellName);
					if (!color.equalsIgnoreCase("Red"))
					{
						testRst = false;
						logger.error("Expected color is red, but actual color is " + color);
					}
				}
			}
			for (String cellName : Yelolows.split("#"))
			{
				if (cellName.toUpperCase().contains("[ALL]"))
				{
					cellName = cellName.toUpperCase().replace("[ALL]", "");
					List<String> instances = formInstancePage.getAllInstance(Regulator, formCode, version, cellName);
					for (String instance : instances)
					{
						String color = formInstancePage.getCellColor(Regulator, formCode, version, instance, cellName);
						if (!color.equalsIgnoreCase("Yellow"))
						{
							testRst = false;
							logger.error("Expected color is yellow, but actual color is " + color);
						}
					}
				}
				else
				{
					String color = formInstancePage.getCellColor(Regulator, formCode, version, cellName);
					if (!color.equalsIgnoreCase("Yellow"))
						;
					{
						testRst = false;
						logger.error("Expected color is yellow, but actual color is " + color);
					}
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5775() throws Exception
	{
		boolean testRst = true;
		String caseID = "5775";
		logger.info("====Verify cell is highlighted in red when it failed with both critical and warning validation rule,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_highlight, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Reds = testdata.get(4);
			String Yelolows = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.validationNowClick();

			for (String cellName : Reds.split("#"))
			{
				String color = formInstancePage.getCellColor(Regulator, formCode, version, cellName);
				if (!color.equalsIgnoreCase("Red"))
				{
					testRst = false;
					logger.error("Expected color is red, but actual color is " + color);
				}
			}
			for (String cellName : Yelolows.split("#"))
			{
				String color = formInstancePage.getCellColor(Regulator, formCode, version, cellName);
				if (!color.equalsIgnoreCase("Yellow"))
				{
					testRst = false;
					logger.error("Expected color is Yellow, but actual color is " + color);
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5776() throws Exception
	{
		boolean testRst = true;
		String caseID = "5776";
		logger.info("====Verify cell is highlighted in purple when it failed with both summing error, critical validation and critical x-validation rules,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_highlight, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String EditCell = testdata.get(4);
			String Cell = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);

			String curValue = formInstancePage.getCellText(Regulator, formCode, version, null, EditCell, null);
			logger.info("Current value is: " + curValue);
			String editValue;
			try
			{
				editValue = String.valueOf(Integer.parseInt(curValue.trim()) + 1);
			}
			catch (Exception e)
			{
				editValue = curValue.trim() + "1";
			}

			formInstancePage.editCellValue(null, EditCell, null, editValue);

			for (String cellName : Cell.split("#"))
			{
				cellName = cellName.toUpperCase();
				if (cellName.contains("[ALL]"))
				{
					for (String instance : formInstancePage.getAllInstance(Regulator, formCode, version, cellName.replace("[ALL]", "")))
					{
						String color = formInstancePage.getCellColor(Regulator, formCode, version, instance, cellName.replace("[ALL]", ""));
						if (!color.equalsIgnoreCase("Purple"))
						{
							testRst = false;
							logger.error("Expected color is Purple, but actual color is " + color);
						}

					}
				}
				else
				{
					String color = formInstancePage.getCellColor(Regulator, formCode, version, cellName);
					if (!color.equalsIgnoreCase("Purple"))
					{
						testRst = false;
						logger.error("Expected color is Purple, but actual color is " + color);
					}
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5777() throws Exception
	{
		boolean testRst = true;
		String caseID = "5777";
		logger.info("====Verify related cells are highlighted when clicking rule on validation result grid,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_highlight, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Rule = testdata.get(4);
			String Page = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			ValidationPage validationPage = formInstancePage.enterValidation(true);

			for (int i = 0; i < Rule.split("#").length; i++)
			{
				String ruleNo = Rule.split("#")[i];
				validationPage.clickRuleNO(ruleNo);

				String pageNames = Page.split("#")[i];
				if (pageNames.contains("/"))
				{
					for (String pageName : pageNames.split("/"))
					{
						if (formInstancePage.isPageHighlight(pageName))
							logger.info("Page[" + pageName + "] is highlight");
						else
						{
							testRst = false;
							logger.error("Page[" + pageName + "] is not highlight");
						}
					}
				}
				else
				{
					if (formInstancePage.isPageHighlight(pageNames))
						logger.info("Page[" + pageNames + "] is highlight");
					else
					{
						testRst = false;
						logger.error("Page[" + pageNames + "] is not highlight");
					}
				}

				for (String cell : validationPage.splitRuleExpression(ruleNo))
				{
					if (formInstancePage.isCellHighlight(Regulator, formCode, version, null, cell, null))
						logger.info("Cell[" + cell + "] is highlight");
					else
					{
						testRst = false;
						logger.error("Cell[" + cell + "] is not highlight");
					}
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5778() throws Exception
	{
		boolean testRst = true;
		String caseID = "5778";
		logger.info("====Verify related rules are highlighted when clicking cell on page,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_highlight, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Cell = testdata.get(4);
			String Rule = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			ValidationPage validationPage = formInstancePage.enterValidation(true);

			for (int i = 0; i < Cell.split("#").length; i++)
			{
				String cellName = Cell.split("#")[i];
				formInstancePage.cellClick(Regulator, formCode, version, null, cellName, null);

				String ruleNo = Rule.split("#")[i];
				if (ruleNo.contains("/"))
				{
					for (String ru : ruleNo.split("/"))
					{
						if (validationPage.getRuleBackgroudColor(ru).equalsIgnoreCase("Blue"))
							logger.info("Rule [" + ru + "] is highlight");
						else
						{
							testRst = false;
							logger.error("Rule [" + ru + "] is not highlight");
						}
					}
				}
				else
				{
					if (validationPage.getRuleBackgroudColor(ruleNo).equalsIgnoreCase("Blue"))
						logger.info("Rule [" + ruleNo + "] is highlight");
					else
					{
						testRst = false;
						logger.error("Rule [" + ruleNo + "] is not highlight");
					}
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5779() throws Exception
	{
		boolean testRst = true;
		String caseID = "5779";
		logger.info("====Verify highlight updates are updated when switching selecting rule to cell,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_highlight, nodeName);
			String Regulator = testdata.get(0);
			String Entity = testdata.get(1);
			String Form = testdata.get(2);
			String ReferenceDate = testdata.get(3);
			String Cell = testdata.get(4);
			String Rule = testdata.get(5);

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			ValidationPage validationPage = formInstancePage.enterValidation(true);

			formInstancePage.cellClick(Regulator, formCode, version, null, Cell, null);

			for (String ruleNo : Rule.split("#"))
			{
				if (validationPage.getRuleBackgroudColor(ruleNo).equalsIgnoreCase("Blue"))
					logger.info("Rule [" + ruleNo + "] is highlight");
				else
				{
					testRst = false;
					logger.error("Rule [" + ruleNo + "] is not highlight");
				}
			}
			validationPage.clickRuleNO(Rule.split("#")[0]);
			for (int i = 1; i <= 2; i++)
			{
				if (!validationPage.getRuleBackgroudColor(Rule.split("#")[i]).equalsIgnoreCase("Blue"))
					logger.info("Rule [" + Rule.split("#")[i] + "] is not highlight");
				else
				{
					testRst = false;
					logger.error("Rule [" + Rule.split("#")[i] + "] shoud not be highlight");
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5780() throws Exception
	{
		boolean testRst = true;
		String caseID = "5780";
		logger.info("====Verify related cells are highlighted when clicking rule on error tab,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_highlight, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_highlight, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_highlight, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_highlight, nodeName, "ReferenceDate");
			String Rule = getElementValueFromXML(testData_highlight, nodeName, "Rule");
			String EditCell = getElementValueFromXML(testData_highlight, nodeName, "EditCell");
			String HighLightCell = getElementValueFromXML(testData_highlight, nodeName, "HighLightCell");
			String HighLightPage = getElementValueFromXML(testData_highlight, nodeName, "HighLightPage");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);

			String curValue = formInstancePage.getCellText(Regulator, formCode, version, null, EditCell, null);
			logger.info("Current value is: " + curValue);
			String editValue;
			try
			{
				editValue = String.valueOf(Integer.parseInt(curValue.trim()) + 1);
			}
			catch (Exception e)
			{
				editValue = curValue.trim() + "1";
			}

			formInstancePage.editCellValue(null, EditCell, null, editValue);
			ErrorListPage errorListPage = formInstancePage.enterProblem();
			errorListPage.clickRuleNO(Rule);

			if (formInstancePage.isCellHighlight(Regulator, formCode, version, null, HighLightCell, null))
				logger.info("Cell[" + HighLightCell + "] is highlight");
			else
			{
				testRst = false;
				logger.error("Cell[" + HighLightCell + "] is not highlight");
			}

			if (formInstancePage.isPageHighlight(HighLightPage))
				logger.info("Page[" + HighLightPage + "] is highlight");
			else
			{
				testRst = false;
				logger.error("Page[" + HighLightPage + "] is not highlight");
			}

			if (formInstancePage.getCellColor(Regulator, formCode, version, HighLightCell).equalsIgnoreCase("Purple"))
				logger.info("Cell[" + HighLightPage + "] is purple");
			else
			{
				testRst = false;
				logger.error("Cell[" + HighLightPage + "] is not purple");
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5781() throws Exception
	{
		boolean testRst = true;
		String caseID = "5781";
		logger.info("====Verify related error rule are highlighted when clicking cell on page,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_highlight, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_highlight, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_highlight, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_highlight, nodeName, "ReferenceDate");
			String Rule = getElementValueFromXML(testData_highlight, nodeName, "Rule");
			String EditCell = getElementValueFromXML(testData_highlight, nodeName, "EditCell");
			String ClickCell = getElementValueFromXML(testData_highlight, nodeName, "ClickCell");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);

			String curValue = formInstancePage.getCellText(Regulator, formCode, version, null, EditCell, null);
			logger.info("Current value is: " + curValue);
			String editValue;
			try
			{
				editValue = String.valueOf(Integer.parseInt(curValue.trim()) + 1);
			}
			catch (Exception e)
			{
				editValue = curValue.trim() + "1";
			}

			formInstancePage.editCellValue(null, EditCell, null, editValue);
			ErrorListPage errorListPage = formInstancePage.enterProblem();
			formInstancePage.cellClick(Regulator, formCode, version, null, ClickCell, null);

			if (errorListPage.getRuleBackgroudColor(Rule).equalsIgnoreCase("Blue"))
				logger.info("Rule[" + Rule + "] is highlight");
			else
			{
				testRst = false;
				logger.error("Cell[" + Rule + "] is not highlight");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}

	@Test
	public void test5791() throws Exception
	{
		boolean testRst = true;
		String caseID = "5791";
		logger.info("====Verify the failure and warning count is right when cell is failed with validation rules,X-validation and U-validation rules,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_highlight, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_highlight, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_highlight, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_highlight, nodeName, "ReferenceDate");
			String Pages = getElementValueFromXML(testData_highlight, nodeName, "Pages");
			String Counts = getElementValueFromXML(testData_highlight, nodeName, "Counts");

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ReferenceDate);
			formInstancePage.validationNowClick();

			for (int i = 0; i < Pages.split("#").length; i++)
			{
				List<String> count = formInstancePage.getHighlightCount_Page(Pages.split("#")[i], null);
				if (count.get(0).equals(Counts.split("#")[i].split(",")[0]) && count.get(1).equals(Counts.split("#")[i].split(",")[1]))
					logger.info("Highlight count is correct");
				else
				{
					testRst = false;
					logger.error("Expected highlight count is[" + Counts.split("#")[i] + "], but actual is[" + count.get(0) + "," + count.get(1) + "]");
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			// closeFormInstance();
			writeTestResultToFile(caseID, testRst, "HighLight");
		}
	}
}
