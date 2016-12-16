package com.lombardrisk.testCase;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 8/19/2016
 */

public class Admin_FormVariable extends TestTemplate
{

	@Test
	public void test6085() throws Exception
	{
		String caseID = "6085";
		logger.info("====Verify entity level variable display correctly[case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String Entity = testData.get(0);
			String Regulator = testData.get(1);
			String prefix = testData.get(2);

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntityIfNotExist(Entity, Entity, Entity, true);
			List<String> varsList = entityManagePage.getVarialbe(Entity, Regulator);

			String SQL = "SELECT CCDV.VARIABLE_NAME FROM CFG_CONFIG_DEFINED_VARIABLES CCDV" + " LEFT JOIN CFG_INSTALLED_CONFIGURATIONS CIC"
					+ " ON  CCDV.ID < CIC.ID_RANGE_END AND CCDV.ID > CIC.ID_RANGE_START" + " WHERE CCDV.VARIABLE_SCOPE='ENTITY'  AND CIC.PREFIX = '" + prefix + "'";
			List<String> varsInDB = DBQuery.queryRecords(SQL);

			if (varsInDB.size() == varsList.size())
			{
				for (int i = 0; i < varsInDB.size(); i++)
				{
					String Variable = "";
					for (String s : varsList.get(i).split(" "))
					{
						Variable = Variable + StringUtils.capitalize(s) + "_";
					}
					Variable = Variable.substring(0, Variable.length() - 1);
					if (Variable.equals("Scope_Of_Consolidation"))
						Variable = "Group_Consolidation_Type";
					else if (Variable.equals("Currency"))
						Variable = "Reporting_Currency";
					if (!varsInDB.contains(Variable))
						testRst = false;
				}
			}
			else
				testRst = false;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// package com.lombardrisk.testCase;
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6086() throws Exception
	{
		// Verify entity level variable can be edit and saved successfully for
		// each type
		String caseID = "6086";
		logger.info("====Verify entity level variable can be edit and saved successfully [case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String entityId = testData.get(2).trim();
			String monetaryScale = testData.get(3).trim();
			String transmissionRef = testData.get(4).trim();
			String financialYearEnd = testData.get(5).trim();
			String entityIdEdit = testData.get(6).trim();
			String value = testData.get(7).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.openFormVariablePage(entity);

			// Update the value and save
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_EntityID(regulator, entityId);
			entityManagePage.updateVariable_MonetaryScale(regulator, monetaryScale);
			entityManagePage.updateVariable_TransmissionRef(regulator, transmissionRef);
			entityManagePage.updateVariable_FinancialYear(regulator, financialYearEnd);
			entityManagePage = entityManagePage.assignVariable(regulator);

			// Check the variable value
			entityManagePage.openFormVariablePage(entity);
			Assert.assertEquals(entityManagePage.getVariable_EntityID(regulator), entityId);
			Assert.assertEquals(entityManagePage.getVariable_MonetaryScale(regulator), monetaryScale);
			Assert.assertEquals(entityManagePage.getVariable_TransmissionRef(regulator), transmissionRef);
			Assert.assertEquals(entityManagePage.getVariable_FinancialYear(regulator), financialYearEnd);

			// Update the value and cancel
			entityManagePage.updateVariable_EntityID(regulator, entityIdEdit);
			entityManagePage = entityManagePage.cancelVariable(regulator);

			// Check the value does not save
			entityManagePage.openFormVariablePage(entity);
			Assert.assertEquals(entityManagePage.getVariable_EntityID(regulator), entityId);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// package com.lombardrisk.testCase;
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6089() throws Exception
	{
		// Verify pre-defined value for entity level variable shows completely
		String caseID = "6089";
		logger.info("====erify pre-defined value for entity level variable shows completely [case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String accountingStandard = testData.get(2).trim();
			String scopeOfConsolidation = testData.get(3).trim();
			String currency = testData.get(4).trim();
			String entityId = testData.get(5).trim();
			String value = testData.get(6).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.openFormVariablePage(entity);

			// Check the dropdown list menu.
			List<String> accountingStandards = new ArrayList<String>();
			accountingStandards.add("National GAAP");
			accountingStandards.add("IFRS");

			List<String> scopeOfConsolidations = new ArrayList<String>();
			scopeOfConsolidations.add("Individual");
			scopeOfConsolidations.add("Consolidated");

			Assert.assertEquals(entityManagePage.getVariable_All_AccountingStandard(regulator), accountingStandards);
			Assert.assertEquals(entityManagePage.getVariable_All_ScopeOfConsolidation(regulator), scopeOfConsolidations);

			// Update the dropdown list menu value
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_EntityID(regulator, entityId);
			entityManagePage.updateVariable_AccountingStandard(regulator, accountingStandard);
			entityManagePage.updateVariable_ScopeOfConsolidation(regulator, scopeOfConsolidation);
			entityManagePage.updateVariable_Currency(regulator, currency);
			entityManagePage = entityManagePage.assignVariable(regulator);

			// Check the dropdown list menu value
			entityManagePage.openFormVariablePage(entity);
			Assert.assertEquals(entityManagePage.getVariable_AccountingStandard(regulator), accountingStandard);
			Assert.assertEquals(entityManagePage.getVariable_ScopeOfConsolidation(regulator), scopeOfConsolidation);
			Assert.assertEquals(entityManagePage.getVariable_Currency(regulator), currency);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// package com.lombardrisk.testCase;
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6090() throws Exception
	{
		String caseID = "6090";
		logger.info("====Verify entity level variable defined as mandatory must be filled before saving [case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String entityId = testData.get(2).trim();
			String msg = testData.get(3).trim();
			String transmissionRef = testData.get(4).trim();
			String value = testData.get(5).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.openFormVariablePage(entity);

			// Leave entity id field blank and click assign button
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_EntityID(regulator, "");
			Assert.assertEquals(entityManagePage.assignVariableWithErrorMsg(regulator), msg);

			// Edit transmission ref value and click assign button
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.updateVariable_EntityID(regulator, "");
			entityManagePage.updateVariable_TransmissionRef(regulator, transmissionRef);
			Assert.assertEquals(entityManagePage.assignVariableWithErrorMsg(regulator), msg);

			// Update the entity id value and click assign button
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.updateVariable_EntityID(regulator, entityId);
			entityManagePage = entityManagePage.assignVariable(regulator);

			entityManagePage.openFormVariablePage(entity);
			Assert.assertEquals(entityManagePage.getVariable_EntityID(regulator), entityId);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// package com.lombardrisk.testCase;
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6093() throws Exception
	{
		String caseID = "6093";
		logger.info("====Verify default value of entity level variable displays when loaded and can be override [case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String entityId = testData.get(2).trim();
			String financialYearEnd = testData.get(3).trim();
			String financialYearEndEdit = testData.get(4).trim();
			String monetaryScale = testData.get(5).trim();
			String monetaryScaleEdit = testData.get(6).trim();
			String value = testData.get(7).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.openFormVariablePage(entity);

			// Check the default value
			Assert.assertEquals(entityManagePage.getVariable_FinancialYear(regulator), financialYearEnd);
			Assert.assertEquals(entityManagePage.getVariable_MonetaryScale(regulator), monetaryScale);

			// Edit the default value
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_FinancialYear(regulator, financialYearEndEdit);
			entityManagePage.updateVariable_MonetaryScale(regulator, monetaryScaleEdit);
			entityManagePage.updateVariable_EntityID(regulator, entityId);
			entityManagePage = entityManagePage.assignVariable(regulator);

			// Check the edit value is saved.
			entityManagePage.openFormVariablePage(entity);
			Assert.assertEquals(entityManagePage.getVariable_FinancialYear(regulator), financialYearEndEdit);
			Assert.assertEquals(entityManagePage.getVariable_MonetaryScale(regulator), monetaryScaleEdit);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// package com.lombardrisk.testCase;
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6094() throws Exception
	{
		String caseID = "6094";
		logger.info("====Verify entity level variable display correctly when upload multi config package[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String regulatorNew = testData.get(2);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			List<String> varsList = entityManagePage.getVarialbe(entity, regulator);

			String sql = "SELECT CCDV.VARIABLE_NAME FROM CFG_CONFIG_DEFINED_VARIABLES CCDV" + " LEFT JOIN CFG_INSTALLED_CONFIGURATIONS CIC"
					+ " ON  CCDV.ID < CIC.ID_RANGE_END AND CCDV.ID > CIC.ID_RANGE_START" + " WHERE CCDV.VARIABLE_SCOPE='ENTITY'  AND CIC.PREFIX = '" + regulator + "'";
			List<String> varsInDB = DBQuery.queryRecords(sql);

			// Check the label is correct of ECR package.
			Assert.assertTrue(varsList.contains("Accounting standard") && varsInDB.contains("Accounting_Standard"));
			Assert.assertTrue(varsList.contains("Entity ID Scheme") && varsInDB.contains("Entity_ID_Scheme"));
			Assert.assertTrue(varsList.contains("Entity ID Value") && varsInDB.contains("Entity_ID_Value"));
			Assert.assertTrue(varsList.contains("Financial Year End") && varsInDB.contains("Financial_Year_End"));
			Assert.assertTrue(varsList.contains("Scope of consolidation") && varsInDB.contains("Group_Consolidation_Type"));
			Assert.assertTrue(varsList.contains("Monetary Scale") && varsInDB.contains("Monetary_Scale"));
			Assert.assertTrue(varsList.contains("Currency") && varsInDB.contains("Reporting_Currency"));
			Assert.assertTrue(varsList.contains("Transmission Ref") && varsInDB.contains("Transmission_Ref"));

			// Check the label value is correct of FED1 package
			List<String> varsListNew = entityManagePage.getVarialbe(entity, regulatorNew);

			String sqlNew = "SELECT CCDV.VARIABLE_NAME FROM CFG_CONFIG_DEFINED_VARIABLES CCDV" + " LEFT JOIN CFG_INSTALLED_CONFIGURATIONS CIC"
					+ " ON  CCDV.ID < CIC.ID_RANGE_END AND CCDV.ID > CIC.ID_RANGE_START" + " WHERE CCDV.VARIABLE_SCOPE='ENTITY'  AND CIC.PREFIX = '" + regulatorNew + "'";
			List<String> varsInDBNew = DBQuery.queryRecords(sqlNew);
			Assert.assertTrue(varsListNew.contains("FED Accounting Standard") && varsInDBNew.contains("F_Accounting_Standard"));
			Assert.assertTrue(varsListNew.contains("FED Financial Year End") && varsInDBNew.contains("F_Financial_Year_End"));
			Assert.assertTrue(varsListNew.contains("FED Group Consolidation Type") && varsInDBNew.contains("F_Group_Consolidation_Type"));
			Assert.assertTrue(varsListNew.contains("FED Monetary Scale") && varsInDBNew.contains("F_Monetary_Scale"));
			Assert.assertTrue(varsListNew.contains("FED Reporting Currency") && varsInDBNew.contains("F_Reporting_Currency"));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// package com.lombardrisk.testCase;
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6097() throws Exception
	{
		String caseID = "6097";
		logger.info("====Verify entity level variable can be restored from restoring entity [case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String entityId = testData.get(2).trim();
			String financialYearEnd = testData.get(3).trim();
			String monetaryScale = testData.get(4).trim();
			String transmissionRef = testData.get(5).trim();
			String value = testData.get(6).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.openFormVariablePage(entity);

			// Edit the some field value
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_EntityID(regulator, entityId);
			entityManagePage.updateVariable_FinancialYear(regulator, financialYearEnd);
			entityManagePage.updateVariable_MonetaryScale(regulator, monetaryScale);
			entityManagePage.updateVariable_TransmissionRef(regulator, transmissionRef);
			entityManagePage = entityManagePage.assignVariable(regulator);

			// Delete the entity
			entityManagePage.deleteEntity(entity);

			// Show the deleted entities
			entityManagePage.showDeletedEntities();
			Assert.assertTrue(entityManagePage.getAllEntityName().contains(entity));

			// Restore the deleted entity
			entityManagePage.restoreDeleteEntity(entity);

			// Check the edit value still exist.
			entityManagePage.openFormVariablePage(entity);
			Assert.assertEquals(entityManagePage.getVariable_EntityID(regulator), entityId);
			Assert.assertEquals(entityManagePage.getVariable_FinancialYear(regulator), financialYearEnd);
			Assert.assertEquals(entityManagePage.getVariable_MonetaryScale(regulator), monetaryScale);
			Assert.assertEquals(entityManagePage.getVariable_TransmissionRef(regulator), transmissionRef);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// package com.lombardrisk.testCase;
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6095() throws Exception
	{
		String caseID = "6095";
		logger.info("====Verify the Form Variables UI when user has access to specified return [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();

			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			listPage = entityManagePage.backToDashboard();
			formVariablePage = listPage.openFormVariablePage();

			// Check all the colume name is correct.
			List<String> columeNames = formVariablePage.getAllColumeName();
			List<String> list = new ArrayList<>();
			list.add("ENTITY");
			list.add("CONFIG");
			list.add("FORM");
			list.add("VARIABLE");
			list.add("VALUE");
			Assert.assertEquals(list, columeNames);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6100() throws Exception
	{
		String caseID = "6100";
		logger.info("====Verify the entity&form level variables display correctly when upload multi config package [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String configName = testData.get(1).trim();
			String formName = testData.get(2).trim();
			String variableName = testData.get(3).trim();
			String value = testData.get(4).trim();

			ListPage listPage = super.m.listPage;
			formVariablePage = listPage.openFormVariablePage();

			// Check all colume can be sorted.
			formVariablePage = formVariablePage.sortBySpecialColume("ENTITY");
			Assert.assertTrue(formVariablePage.checkListOrder(formVariablePage.getAllSepcialColumeText("ENTITY"), "ASC"));
			formVariablePage = formVariablePage.sortBySpecialColume("ENTITY");
			Assert.assertTrue(formVariablePage.checkListOrder(formVariablePage.getAllSepcialColumeText("ENTITY"), "DESC"));

			formVariablePage = formVariablePage.sortBySpecialColume("CONFIG");
			Assert.assertTrue(formVariablePage.checkListOrder(formVariablePage.getAllSepcialColumeText("CONFIG"), "ASC"));
			formVariablePage = formVariablePage.sortBySpecialColume("CONFIG");
			Assert.assertTrue(formVariablePage.checkListOrder(formVariablePage.getAllSepcialColumeText("CONFIG"), "DESC"));

			formVariablePage = formVariablePage.sortBySpecialColume("FORM");
			Assert.assertTrue(formVariablePage.checkListOrder(formVariablePage.getAllSepcialColumeText("FORM"), "ASC"));
			formVariablePage = formVariablePage.sortBySpecialColume("FORM");
			Assert.assertTrue(formVariablePage.checkListOrder(formVariablePage.getAllSepcialColumeText("FORM"), "DESC"));

			formVariablePage = formVariablePage.sortBySpecialColume("VARIABLE");
			Assert.assertTrue(formVariablePage.checkListOrder(formVariablePage.getAllSepcialColumeText("VARIABLE"), "ASC"));
			formVariablePage = formVariablePage.sortBySpecialColume("VARIABLE");
			Assert.assertTrue(formVariablePage.checkListOrder(formVariablePage.getAllSepcialColumeText("VARIABLE"), "DESC"));

			// Check the filter function
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			Assert.assertTrue(formVariablePage.checkAllElementContainsSpecialStr(formVariablePage.getAllSepcialColumeText("ENTITY"), entityName));
			formVariablePage = formVariablePage.filterByEntityColume("");

			formVariablePage = formVariablePage.filterByConfigColume(configName);
			Assert.assertTrue(formVariablePage.checkAllElementContainsSpecialStr(formVariablePage.getAllSepcialColumeText("CONFIG"), configName));
			formVariablePage = formVariablePage.filterByConfigColume("");

			formVariablePage = formVariablePage.filterByFormColume(formName);
			Assert.assertTrue(formVariablePage.checkAllElementContainsSpecialStr(formVariablePage.getAllSepcialColumeText("FORM"), formName));
			formVariablePage = formVariablePage.filterByFormColume("");

			formVariablePage = formVariablePage.filterByVariableColume(variableName);
			Assert.assertTrue(formVariablePage.checkAllElementContainsSpecialStr(formVariablePage.getAllSepcialColumeText("VARIABLE"), variableName));
			formVariablePage = formVariablePage.filterByVariableColume("");

			formVariablePage = formVariablePage.filterByValueColume(value);
			Assert.assertTrue(formVariablePage.checkAllElementContainsSpecialStr(formVariablePage.getAllSepcialColumeText("VALUE"), value));
			formVariablePage = formVariablePage.filterByValueColume("");
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6101() throws Exception
	{
		String caseID = "6101";
		logger.info("====Verify the entity&form level variables can be edit and saved successfully for each type [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String configName = testData.get(1).trim();
			String formName = testData.get(2).trim();
			String cRGBName = testData.get(3).trim();
			String cRGBValue = testData.get(4).trim();
			String cALimLicName = testData.get(5).trim();
			String CALimLicValue = testData.get(6).trim();
			String cACombinedBufferName = testData.get(7).trim();
			String cACombinedBufferValue = testData.get(8).trim();
			String referenceDateName = testData.get(9).trim();
			String referenceDateValue = testData.get(10).trim();
			String referenceDateValueNew = testData.get(11).trim();
			String formNameNew = testData.get(12).trim();

			ListPage listPage = super.m.listPage;
			formVariablePage = listPage.openFormVariablePage();

			// Filter the special variable
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			formVariablePage = formVariablePage.filterByConfigColume(configName);
			formVariablePage = formVariablePage.filterByFormColume(formName);
			formVariablePage = formVariablePage.filterByVariableColume(cRGBName);
			formVariablePage.editVariableValue(entityName, formName, cRGBName, cRGBValue, "input");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, formName, cRGBName), cRGBValue);

			formVariablePage = formVariablePage.filterByVariableColume(cALimLicName);
			formVariablePage.editVariableValue(entityName, formName, cALimLicName, CALimLicValue, "input");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, formName, cALimLicName), CALimLicValue);

			formVariablePage = formVariablePage.filterByVariableColume(cACombinedBufferName);
			formVariablePage.editVariableValue(entityName, formName, cACombinedBufferName, cACombinedBufferValue, "input");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, formName, cACombinedBufferName), cACombinedBufferValue);

			formVariablePage = formVariablePage.filterByVariableColume(referenceDateName);
			formVariablePage.editVariableValue(entityName, formNameNew, referenceDateName, referenceDateValue, "Date");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, formNameNew, referenceDateName), referenceDateValue);

			// Cancel edit value and check the value
			formVariablePage.clickEditButtonOfVariable(entityName, formNameNew, referenceDateName);
			formVariablePage.editVariable(referenceDateValueNew, "Date");
			formVariablePage.cancelEdit();
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, formNameNew, referenceDateName), referenceDateValue);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6104() throws Exception
	{
		String caseID = "6104";
		logger.info("====Verify pre-defined value for entity&form level variable shows completely [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String configName = testData.get(1).trim();
			String formName = testData.get(2).trim();
			String variableName = testData.get(3).trim();
			String variableValue = testData.get(4).trim();
			String type = testData.get(5).trim();

			ListPage listPage = super.m.listPage;
			formVariablePage = listPage.openFormVariablePage();

			// Filter the special variable
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			formVariablePage = formVariablePage.filterByConfigColume(configName);
			formVariablePage = formVariablePage.filterByFormColume(formName);
			formVariablePage = formVariablePage.filterByVariableColume(variableName);
			formVariablePage = formVariablePage.clickEditButtonOfVariable(entityName, formName, variableName);
			List<String> list = formVariablePage.getDropDownList();
			Assert.assertTrue(list.contains("Consolidated") && list.contains("Individual"));
			formVariablePage.editVariable(variableValue, type);
			formVariablePage = formVariablePage.saveEdit();

			// Check the value is saved.
			formVariablePage = formVariablePage.clickEditButtonOfVariable(entityName, formName, variableName);
			Assert.assertEquals(formVariablePage.getSelectedOption(), variableValue);
			formVariablePage.cancelEdit();
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6103() throws Exception
	{
		String caseID = "6103";
		logger.info("====Verify variable declared by a form can be used for all form versions of the same form code [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String configName = testData.get(1).trim();
			String formName = testData.get(2).trim();
			String formNameNew = testData.get(3).trim();
			String variableName = testData.get(4).trim();
			String variableValue = testData.get(5).trim();
			String type = testData.get(6).trim();
			String variableValueNew = testData.get(7).trim();

			ListPage listPage = super.m.listPage;
			formVariablePage = listPage.openFormVariablePage();

			// Check the same variables for CAR v5 and CAR v6
			formVariablePage = formVariablePage.filterByAllColume(entityName, configName, formNameNew, null, null);
			List<String> variablesNew = formVariablePage.getAllSepcialColumeText("VARIABLE");

			formVariablePage = formVariablePage.filterByAllColume(entityName, configName, formName, null, null);
			List<String> variables = formVariablePage.getAllSepcialColumeText("VARIABLE");
			Assert.assertEquals(variables, variablesNew);

			// Check the edit variable UI.
			formVariablePage = formVariablePage.clickEditButtonOfVariable(entityName, formName, variableName);
			List<String> labelNames = new ArrayList<>();
			labelNames.add("Entity");
			labelNames.add("Config");
			labelNames.add("Form");
			Assert.assertEquals(formVariablePage.getLabelName(), labelNames);

			List<String> labelValues = new ArrayList<>();
			labelValues.add("6100");
			labelValues.add("European Common Reporting XBRL");
			labelValues.add("CAR");
			Assert.assertEquals(formVariablePage.getLabelValue(), labelValues);

			// Edit the value
			formVariablePage.editVariable(variableValue, type);
			formVariablePage = formVariablePage.saveEdit();
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, formName, variableName), variableValue);

			formVariablePage = formVariablePage.filterByFormColume(formNameNew);
			formVariablePage = formVariablePage.clickEditButtonOfVariable(entityName, formNameNew, variableName);
			formVariablePage.editVariable(variableValueNew, type);
			formVariablePage = formVariablePage.saveEdit();
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, formNameNew, variableName), variableValueNew);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6105() throws Exception
	{
		String caseID = "6105";
		logger.info("====Verify entity&form level variable defined as mandatory must be filled before saving [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String configName = testData.get(1).trim();
			String formName = testData.get(2).trim();
			String variableName = testData.get(3).trim();
			String variableValue = testData.get(4).trim();
			String type = testData.get(5).trim();
			String msg = testData.get(6).trim();

			ListPage listPage = super.m.listPage;
			formVariablePage = listPage.openFormVariablePage();

			// Check the variable defined as mandatory
			formVariablePage = formVariablePage.filterByAllColume(entityName, configName, formName, variableName, null);
			formVariablePage = formVariablePage.clickEditButtonOfVariable(entityName, formName, variableName);
			formVariablePage.editVariable("", type);
			String errorMsg = formVariablePage.saveEditWithMsg();
			Assert.assertEquals(errorMsg, msg);

			listPage = formVariablePage.backToDashboard();
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByAllColume(entityName, configName, formName, variableName, null);
			Assert.assertNotEquals(formVariablePage.getValueOfValueField(entityName, formName, variableName), "");

			formVariablePage.editVariableValue(entityName, formName, variableName, variableValue, type);
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, formName, variableName), variableValue);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6106() throws Exception
	{
		String caseID = "6106";
		logger.info("====Verify default value of entity&form level variable displays when loaded and can be override [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String productPrefix = testData.get(1).trim();
			String returnName = testData.get(2).trim();
			String ug = testData.get(3).trim();
			String pg = testData.get(4).trim();
			String form1 = testData.get(5).trim();
			String form2 = testData.get(6).trim();
			String variable1 = testData.get(7).trim();
			String value1 = testData.get(8).trim();
			String variable2 = testData.get(9).trim();
			String value2 = testData.get(10).trim();
			String variable3 = testData.get(11).trim();
			String value3 = testData.get(12).trim();

			// Assign return to entity
			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pg.split("#"));
			listPage = entityManagePage.backToDashboard();

			// Check the default value
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, form1, variable1), "1000000");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, form1, variable2), "01/01/9999");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, form2, variable3), "Retail");

			// Override the default value
			formVariablePage.editVariableValue(entityName, form1, variable1, value1, "Input");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, form1, variable1), value1);
			formVariablePage.editVariableValue(entityName, form1, variable2, value2, "Date");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, form1, variable2), value2);
			formVariablePage.editVariableValue(entityName, form2, variable3, value3, "Input");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, form2, variable3), value3);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6109() throws Exception
	{
		String caseID = "6109";
		logger.info("====Verify only the user with return variable update privilege can edit the entity&form level variables [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String productPrefix = testData.get(1).trim();
			String returnName = testData.get(2).trim();
			String ug = testData.get(3).trim();
			String pg = testData.get(4).trim();
			String entityName2 = testData.get(5).trim();
			String pg2 = testData.get(6).trim();
			String variable = testData.get(7).trim();
			String value = testData.get(8).trim();

			// Assign return to entity
			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pg.split("#"));
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName2, entityName2, entityName2, true);
			entityManagePage.assignReturnToEntity(entityName2, productPrefix, returnName.split("#"), ug.split("#"), pg2.split("#"));
			listPage = entityManagePage.backToDashboard();

			// Check the edit button beside variables of LR v3 is disabled.
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			Assert.assertFalse(formVariablePage.isEditButtonEnabled(entityName2, returnName, variable));

			// Edit the variable of LR v3 under entity1
			formVariablePage.editVariableValue(entityName, returnName, variable, value, "Select");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, returnName, variable), value);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6112() throws Exception
	{
		String caseID = "6112";
		logger.info("====Verify the form variable not displays after deselecting the return from the entity [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String productPrefix = testData.get(1).trim();
			String returnName = testData.get(2).trim();
			String ug = testData.get(3).trim();
			String pg = testData.get(4).trim();
			String variable = testData.get(5).trim();
			String value = testData.get(6).trim();

			// Assign return to entity
			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pg.split("#"));
			listPage = entityManagePage.backToDashboard();

			// Edit the variable of LR v3 under entity1
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			formVariablePage.editVariableValue(entityName, returnName, variable, value, "Select");
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, returnName, variable), value);

			// Deselect the assgined return
			listPage = formVariablePage.backToDashboard();
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.removeReturnFromEntity(entityName, productPrefix, returnName);
			listPage = entityManagePage.backToDashboard();

			// Check the variable does not display
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			Assert.assertFalse(formVariablePage.isEntityValueExist());
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6117() throws Exception
	{
		String caseID = "6117";
		logger.info("====Verify the user is only displayed with the returns he has access to in the Export To XBRL page from the entry in dashboard [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String productPrefix = testData.get(1).trim();
			String returnName = testData.get(2).trim();
			String ug = testData.get(3).trim();
			String pg = testData.get(4).trim();
			String value = testData.get(5).trim();
			String entityIdScheme = testData.get(6).trim();
			String entityIdValue = testData.get(7).trim();
			String regulator = testData.get(8).trim();
			String fieldFilter = testData.get(9).trim();
			String referenceDate = testData.get(10).trim();
			String form1 = testData.get(11).trim();
			String form2 = testData.get(12).trim();
			String fileType = testData.get(13).trim();
			String framework1 = testData.get(14).trim();
			String taxonomy1 = testData.get(15).trim();
			String module1 = testData.get(16).trim();
			String framework2 = testData.get(17).trim();
			String taxonomy2 = testData.get(18).trim();
			String module2 = testData.get(19).trim();
			String regulator2 = testData.get(20).trim();

			// Assign return to entity
			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pg.split("#"));

			// Update the field value for XBRL
			entityManagePage.openFormVariablePage(entityName);
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_EntityIDScheme(productPrefix, entityIdScheme);
			entityManagePage.updateVariable_EntityID(productPrefix, entityIdValue);
			entityManagePage = entityManagePage.assignVariable(productPrefix);
			listPage = entityManagePage.backToDashboard();

			// Create the new return F143 v1 and LR v3
			listPage.getProductListPage(regulator2, null, null, null);
			listPage.getProductListPage(regulator, entityName, fieldFilter, fieldFilter);

			// Create the new return
			FormInstancePage formInstancePage = listPage.createNewForm(entityName, referenceDate, form1, null, false, false);
			listPage = formInstancePage.closeFormInstance();
			formInstancePage = listPage.createNewForm(entityName, referenceDate, form2, null, false, false);
			listPage = formInstancePage.closeFormInstance();

			// Click the Export to Regulator Format
			listPage.openExportToFileBtnClick(fileType, form1);

			// Set the filter
			listPage.filterTheExportField(entityName, referenceDate, fileType, framework1, taxonomy1, module1);
			Assert.assertEquals(listPage.getFormNameOnExportXBRLPage().get(0), form1);

			listPage.filterTheExportField(entityName, referenceDate, fileType, framework2, taxonomy2, module2);
			Assert.assertEquals(listPage.getFormNameOnExportXBRLPage().get(0), form2);
			listPage.closeExportPage(fileType);
			testRst = true;
		}
		catch (RuntimeException e)
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
	public void test6118() throws Exception
	{
		String caseID = "6118";
		logger.info("====Verify the user is only displayed with the current return in the Export To XBRL page from the entry within return [case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String productPrefix = testData.get(1).trim();
			String returnName = testData.get(2).trim();
			String ug = testData.get(3).trim();
			String pg = testData.get(4).trim();
			String value = testData.get(5).trim();
			String entityIdScheme = testData.get(6).trim();
			String entityIdValue = testData.get(7).trim();
			String regulator = testData.get(8).trim();
			String fieldFilter = testData.get(9).trim();
			String referenceDate = testData.get(10).trim();
			String form1 = testData.get(11).trim();
			String form2 = testData.get(12).trim();
			String regulator2 = testData.get(13).trim();
			String fileType = testData.get(14).trim();

			// Assign return to entity
			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pg.split("#"));

			// Update the field value for XBRL
			entityManagePage.openFormVariablePage(entityName);
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_EntityIDScheme(productPrefix, entityIdScheme);
			entityManagePage.updateVariable_EntityID(productPrefix, entityIdValue);
			entityManagePage = entityManagePage.assignVariable(productPrefix);
			listPage = entityManagePage.backToDashboard();

			// Create the new return F143 v1 and LR v3
			listPage.getProductListPage(regulator2, null, null, null);
			listPage.getProductListPage(regulator, entityName, fieldFilter, fieldFilter);

			// Create the new return
			FormInstancePage formInstancePage = listPage.createNewForm(entityName, referenceDate, form1, null, false, false);
			listPage = formInstancePage.closeFormInstance();
			formInstancePage = listPage.createNewForm(entityName, referenceDate, form2, null, false, false);

			// Check the return is displayed on the export xbrl page.
			ExportToFilePage exportToFilePage = formInstancePage.enterExportRegulatorFormatPage(fileType, form1);
			Assert.assertEquals(formInstancePage.getFormNameOnExportXBRLPage().get(0), form2);
			Assert.assertEquals(formInstancePage.getFormNameOnExportXBRLPage().get(1), form1);
			exportToFilePage.closeExportPage(fileType);
			formInstancePage.closeFormInstance();
			testRst = true;
		}
		catch (RuntimeException e)
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
	public void test6119() throws Exception
	{
		String caseID = "6119";
		logger.info("====Verify the UI of XBRL generation when no privilege is assigned [case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String pgName = testData.get(0).trim();
			String type = testData.get(1).trim();
			String privilege = testData.get(2).trim();
			String entityName = testData.get(3);
			String productPrefix = testData.get(4).trim();
			String returnName = testData.get(5).trim();
			String ug = testData.get(6).trim();
			String value = testData.get(7).trim();
			String entityIdScheme = testData.get(8).trim();
			String entityIdValue = testData.get(9).trim();
			String regulator = testData.get(10).trim();
			String fieldFilter = testData.get(11).trim();
			String referenceDate = testData.get(12).trim();
			String form1 = testData.get(13).trim();
			String regulator2 = testData.get(14).trim();
			String menu = testData.get(15).trim();
			String username = testData.get(16).trim();
			String email = testData.get(17).trim();
			String password = testData.get(18).trim();
			String pgs = testData.get(19).trim();

			// Add user and user group
			ListPage listPage = super.m.listPage;
			UsersPage usersPage = listPage.EnterUserPage();
			usersPage.addUser(username, email, password, password);
			usersPage.addPrivilegeGroup(username, pgs);
			listPage = usersPage.backToDashboard();
			UserGroupPage userGroupPage = listPage.EnterUserGroupPage();
			userGroupPage = userGroupPage.addGroup(ug, ug);
			userGroupPage = userGroupPage.assignUserToGroup(ug, username);
			listPage = usersPage.backToDashboard();

			// Create a privilege group without "Export File Format"
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.addPrivilegeGroup(pgName, pgName, type);
			privilegeGroupPage.addAllPrivileges(pgName);
			privilegeGroupPage.deletePrivilege(pgName, privilege);
			listPage = privilegeGroupPage.backToDashboard();

			// Assign return to entity
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pgName.split("#"));

			// Update the field value for XBRL
			entityManagePage.openFormVariablePage(entityName);
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_EntityIDScheme(productPrefix, entityIdScheme);
			entityManagePage.updateVariable_EntityID(productPrefix, entityIdValue);
			entityManagePage = entityManagePage.assignVariable(productPrefix);
			listPage = entityManagePage.backToDashboard();

			// Switch to new user
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(username, password);

			// Create the new return LR v3
			listPage.getProductListPage(regulator, entityName, fieldFilter, fieldFilter);

			// Create the new return
			FormInstancePage formInstancePage = listPage.createNewForm(entityName, referenceDate, form1, null, false, false);

			// Check the Export To XBRL does not list.
			List<String> list = formInstancePage.getExportRFMenuText();
			Assert.assertFalse(formInstancePage.getExportRFMenuText().contains(menu));
			listPage = formInstancePage.closeFormInstance();
			list = listPage.getExportRFMenuText();
			Assert.assertFalse(listPage.getExportRFMenuText().contains(menu));
			testRst = true;
		}
		catch (RuntimeException e)
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
	public void test6116() throws Exception
	{
		String caseID = "6116";
		logger.info("====Verify the UI of XBRL generation when privilege is assigned [case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String productPrefix = testData.get(1).trim();
			String returnName = testData.get(2).trim();
			String ug = testData.get(3).trim();
			String pg = testData.get(4).trim();
			String value = testData.get(5).trim();
			String entityIdScheme = testData.get(6).trim();
			String entityIdValue = testData.get(7).trim();
			String regulator = testData.get(8).trim();
			String fieldFilter = testData.get(9).trim();
			String referenceDate = testData.get(10).trim();
			String form1 = testData.get(11).trim();
			String regulator2 = testData.get(12).trim();
			String fileType = testData.get(13).trim();

			// Assign return to entity
			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pg.split("#"));

			// Update the field value for XBRL
			entityManagePage.openFormVariablePage(entityName);
			entityManagePage.updateVariable_RequiredFieldOfFED1(value);
			entityManagePage.updateVariable_RequiredFieldOfFEDDS(value);
			entityManagePage.updateVariable_EntityIDScheme(productPrefix, entityIdScheme);
			entityManagePage.updateVariable_EntityID(productPrefix, entityIdValue);
			entityManagePage = entityManagePage.assignVariable(productPrefix);
			listPage = entityManagePage.backToDashboard();

			// Create the new return LR v3
			listPage.getProductListPage(regulator2, null, null, null);
			listPage.getProductListPage(regulator, entityName, fieldFilter, fieldFilter);

			// Create the new return
			FormInstancePage formInstancePage = listPage.createNewForm(entityName, referenceDate, form1, null, false, false);

			// Check the export to XBRL page UI.
			ExportToFilePage exportToFilePage = formInstancePage.enterExportRegulatorFormatPage(fileType, form1);
			List<String> list = new ArrayList<>();
			list.add("Entity");
			list.add("Reference Date");
			list.add("Framework");
			list.add("Taxonomy");
			list.add("Module");
			Assert.assertEquals(exportToFilePage.getAllLabelNames(), list);
			exportToFilePage.closeExportPage(fileType);
			listPage = formInstancePage.closeFormInstance();

			exportToFilePage = listPage.openExportToFileBtnClick(fileType, form1);
			Assert.assertEquals(exportToFilePage.getAllLabelNames(), list);
			exportToFilePage.closeExportPage(fileType);
			testRst = true;
		}
		catch (RuntimeException e)
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
	public void test6147() throws Exception
	{
		String caseID = "6147";
		logger.info("====Verify entity&form level variable can be restored from restoring entity [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String username = testData.get(0).trim();
			String email = testData.get(1).trim();
			String password = testData.get(2).trim();
			String pgs = testData.get(3).trim();
			String entityName = testData.get(4);
			String productPrefix = testData.get(5).trim();
			String returnName = testData.get(6).trim();
			String ug = testData.get(7).trim();
			String pg = testData.get(8).trim();
			String variableName = testData.get(9).trim();
			String value = testData.get(10).trim();
			String type = testData.get(11).trim();
			String adminUserName = testData.get(12).trim();

			// Add user and user group
			ListPage listPage = super.m.listPage;
			UsersPage usersPage = listPage.EnterUserPage();
			usersPage.addUser(username, email, password, password);
			usersPage.addPrivilegeGroup(username, pgs);
			listPage = usersPage.backToDashboard();
			UserGroupPage userGroupPage = listPage.EnterUserGroupPage();
			userGroupPage = userGroupPage.addGroup(ug, ug);
			userGroupPage.assignUserToGroup(ug, username);
			listPage = usersPage.backToDashboard();

			// Assign return to entity
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pg.split("#"));

			// Switch to user1 and set the form variable value
			listPage = entityManagePage.backToDashboard();
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(username, password);
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			formVariablePage.editVariableValue(entityName, returnName, variableName, value, type);
			Assert.assertFalse(formVariablePage.isNoDataDisplayed());

			// Switch to admin user and delete the entity
			listPage = formVariablePage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(adminUserName, password);
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.deleteEntity(entityName);

			// Switch to user1 and check the form variables for deleted entity
			listPage = entityManagePage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(username, password);
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			Assert.assertTrue(formVariablePage.isNoDataDisplayed());

			// Switch to admin user and restore the deleted entity
			listPage = formVariablePage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(adminUserName, password);
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.restoreDeleteEntity(entityName);

			// Switch to user1 and check the form variable value
			listPage = entityManagePage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(username, password);
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			Assert.assertEquals(formVariablePage.getValueOfValueField(entityName, returnName, variableName), value);
			formVariablePage.backToDashboard();
			testRst = true;
		}
		catch (RuntimeException e)
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
	public void test6201() throws Exception
	{
		String caseID = "6201";
		logger.info("====Verify the form variable display correctly when different return with same vairbale name [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entityName = testData.get(0);
			String productPrefix = testData.get(1).trim();
			String returnName = testData.get(2).trim();
			String ug = testData.get(3).trim();
			String pg = testData.get(4).trim();
			String productPrefix2 = testData.get(5).trim();
			String returnName2 = testData.get(6).trim();
			String form1 = testData.get(7).trim();
			String form2 = testData.get(8).trim();
			String form3 = testData.get(9).trim();

			// Assign return to entity
			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityManagePage.assignReturnToEntity(entityName, productPrefix, returnName.split("#"), ug.split("#"), pg.split("#"));
			entityManagePage.assignReturnToEntity(entityName, productPrefix2, returnName2.split("#"), ug.split("#"), pg.split("#"));
			listPage = entityManagePage.backToDashboard();

			// Check the form variable of return FRY9C v1 and FRY14ASUM displays
			// correctly.
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			formVariablePage = formVariablePage.filterByFormColume(returnName2);
			List<String> list = formVariablePage.getAllSepcialColumeText("VARIABLE");

			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			formVariablePage = formVariablePage.filterByFormColume(form1);
			List<String> list2 = formVariablePage.getAllSepcialColumeText("VARIABLE");
			Assert.assertTrue(checkListContainsAnotherList(list, list2));

			List<String> listFRY14 = new ArrayList<>();
			listFRY14.add("14AS Type");
			listFRY14.add("14AS Base Currency");
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			formVariablePage = formVariablePage.filterByFormColume(form2);
			Assert.assertEquals(formVariablePage.getAllSepcialColumeText("VARIABLE"), listFRY14);

			// Check the form variable of return DSFR03 v1 does not displayed
			formVariablePage = formVariablePage.filterByEntityColume(entityName);
			formVariablePage = formVariablePage.filterByFormColume(form3);
			Assert.assertTrue(formVariablePage.isNoDataDisplayed());
			formVariablePage.backToDashboard();
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formVariablePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_FormVariable");
		}
	}

	@Test
	public void test6110() throws Exception
	{
		String caseID = "6110";
		logger.info("====Verify the user is only displayed with the returns he has access to in the Form Variables page [case id=" + caseID + "]====");
		boolean testRst = false;
		FormVariablePage formVariablePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String username1 = testData.get(0).trim();
			String email1 = testData.get(1).trim();
			String password = testData.get(2).trim();
			String username2 = testData.get(3).trim();
			String email2 = testData.get(4).trim();
			String username3 = testData.get(5).trim();
			String email3 = testData.get(6).trim();
			String pgs = testData.get(7).trim();
			String entityName1 = testData.get(8).trim();
			String entityName2 = testData.get(9).trim();
			String productPrefixFED = testData.get(10).trim();
			String productPrefixECR = testData.get(11).trim();
			String pgReturnMaker = testData.get(12).trim();
			String pgReturnViewer = testData.get(13).trim();
			String returnCar = testData.get(14).trim();
			String returnF143 = testData.get(15).trim();
			String returnLR = testData.get(16).trim();
			String returnFry9c = testData.get(17).trim();
			String returnFry14 = testData.get(18).trim();
			String variableOfFRY9C = testData.get(19).trim();
			String variableOfLR = testData.get(20).trim();
			String variableOfF143 = testData.get(21).trim();
			String variableOfCAR = testData.get(22).trim();
			String variableOfFRY14 = testData.get(23).trim();
			String admin = testData.get(24).trim();

			// Create user and user group
			ListPage listPage = super.m.listPage;
			UsersPage usersPage = listPage.EnterUserPage();
			usersPage.addUser(username1, email1, password, password);
			usersPage.addUser(username2, email2, password, password);
			usersPage.addUser(username3, email3, password, password);
			usersPage.addPrivilegeGroup(username3, pgs);
			listPage = usersPage.backToDashboard();

			UserGroupPage userGroupPage = listPage.EnterUserGroupPage();
			userGroupPage = userGroupPage.addGroup(username1, username1);
			userGroupPage = userGroupPage.assignUserToGroup(username1, username1);
			userGroupPage = userGroupPage.addGroup(username2, username2);
			userGroupPage = userGroupPage.assignUserToGroup(username2, username2);
			userGroupPage = userGroupPage.addGroup(username3, username3);
			userGroupPage.assignUserToGroup(username3, username3);
			listPage = usersPage.backToDashboard();

			// Create entity1 and assign return with special privilege
			EntityPage entityPage = listPage.EnterEntityPage();
			entityPage.addRootEntityIfNotExist(entityName1, entityName1, entityName1, true);
			entityPage.assignReturnToEntity(entityName1, productPrefixECR, returnCar.split("#"), username1.split("#"), pgReturnMaker.split("#"));
			entityPage.assignReturnToEntity(entityName1, productPrefixECR, returnF143.split("#"), username1.split("#"), pgReturnMaker.split("#"));
			entityPage.assignReturnToEntity(entityName1, productPrefixECR, returnLR.split("#"), username1.split("#"), pgReturnViewer.split("#"));
			entityPage.assignReturnToEntity(entityName1, productPrefixFED, returnFry9c.split("#"), username1.split("#"), pgReturnMaker.split("#"));
			entityPage.assignReturnToEntity(entityName1, productPrefixECR, returnLR.split("#"), username2.split("#"), pgReturnMaker.split("#"));
			entityPage.assignReturnToEntity(entityName1, productPrefixFED, returnFry14.split("#"), username2.split("#"), pgReturnViewer.split("#"));

			// Create entity2 and assign return with special privilege
			entityPage.addRootEntityIfNotExist(entityName2, entityName2, entityName2, true);
			entityPage.assignReturnToEntity(entityName2, productPrefixECR, returnLR.split("#"), username1.split("#"), pgReturnViewer.split("#"));
			entityPage.assignReturnToEntity(entityName2, productPrefixFED, returnFry14.split("#"), username1.split("#"), pgReturnMaker.split("#"));
			entityPage.assignReturnToEntity(entityName2, productPrefixECR, returnF143.split("#"), username2.split("#"), pgReturnMaker.split("#"));
			entityPage.assignReturnToEntity(entityName2, productPrefixFED, returnFry9c.split("#"), username2.split("#"), pgReturnViewer.split("#"));
			listPage = entityPage.backToDashboard();

			// Login with test1 and check the form variables
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(username1, password);
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName1);
			Assert.assertTrue(formVariablePage.isEditButtonEnabled(entityName1, returnCar, variableOfCAR));
			Assert.assertTrue(formVariablePage.isEditButtonEnabled(entityName1, returnF143, variableOfF143));
			Assert.assertTrue(formVariablePage.isEditButtonEnabled(entityName1, returnFry9c, variableOfFRY9C));
			Assert.assertFalse(formVariablePage.isEditButtonEnabled(entityName1, returnLR, variableOfLR));

			formVariablePage = formVariablePage.filterByEntityColume(entityName2);
			Assert.assertFalse(formVariablePage.isEditButtonEnabled(entityName2, returnLR, variableOfLR));
			Assert.assertTrue(formVariablePage.isEditButtonEnabled(entityName2, returnFry14, variableOfFRY14));

			// Login with test2 and check the form variables
			listPage = formVariablePage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(username2, password);
			formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entityName1);
			Assert.assertTrue(formVariablePage.isEditButtonEnabled(entityName1, returnLR, variableOfLR));
			Assert.assertFalse(formVariablePage.isEditButtonEnabled(entityName1, returnFry14, variableOfFRY14));

			formVariablePage = formVariablePage.filterByEntityColume(entityName2);
			Assert.assertTrue(formVariablePage.isEditButtonEnabled(entityName2, returnF143, variableOfF143));
			Assert.assertFalse(formVariablePage.isEditButtonEnabled(entityName2, returnFry9c, variableOfFRY9C));

			// Login with test3 and check the form variables
			listPage = formVariablePage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(username3, password);
			formVariablePage = listPage.openFormVariablePage();
			Assert.assertTrue(formVariablePage.isNoDataDisplayed());

			testRst = true;
		}
		catch (RuntimeException e)
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
	public void test6636() throws Exception
	{
		String caseID = "6636";
		logger.info("====Verify entity variable in sum rule work well for each instance [case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		FormInstancePage formInstancePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String ugs = testData.get(3).trim();
			String pgs = testData.get(4).trim();
			String value = testData.get(5).trim();
			String valueOfMC = testData.get(6).trim();
			String regulator1 = testData.get(7).trim();
			String regulator2 = testData.get(8).trim();
			String referenceDate = testData.get(9).trim();
			String page = testData.get(10).trim();
			String cellName1 = testData.get(11).trim();
			String cellName2 = testData.get(12).trim();
			String cellName3 = testData.get(13).trim();
			String value1 = testData.get(14).trim();
			String value2 = testData.get(15).trim();
			String value3 = testData.get(16).trim();
			String columnName = testData.get(17).trim();
			String expression = testData.get(18).trim();
			String itemName = testData.get(19).trim();
			String desc = testData.get(20).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			// Add entity 6636 and assign return
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.assignReturnToEntity(entity, regulator, returnName.split("#"), ugs.split("#"), pgs.split("#"));

			// Edit the form variable
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.inputRequiredFieldsOfVariable(regulator, value);
			entityManagePage.updateColumnValueOfInput(regulator, desc, valueOfMC);
			entityManagePage = entityManagePage.assignVariable(regulator);
			listPage = entityManagePage.backToDashboard();

			// Create the new return CIMACPIS
			listPage.getProductListPage(regulator1, null, null, null);
			listPage.getProductListPage(regulator2, entity, "All", "All");
			formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(cellName1, value1);
			formInstancePage.editCellValue(cellName2, value2);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value3);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName3);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, valueOfMC, "", desc, ""));
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
	public void test6671() throws Exception
	{
		String caseID = "6671";
		logger.info("====Verify form variable in sum rule work well for each instance[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String ugs = testData.get(3).trim();
			String pgs = testData.get(4).trim();
			String value = testData.get(5).trim();
			String valueOfCB = testData.get(6).trim();
			String regulator1 = testData.get(7).trim();
			String regulator2 = testData.get(8).trim();
			String referenceDate = testData.get(9).trim();
			String page = testData.get(10).trim();
			String cellName1 = testData.get(11).trim();
			String cellName2 = testData.get(12).trim();
			String cellName3 = testData.get(13).trim();
			String value1 = testData.get(14).trim();
			String value2 = testData.get(15).trim();
			String value3 = testData.get(16).trim();
			String columnName = testData.get(17).trim();
			String expression = testData.get(18).trim();
			String itemName = testData.get(19).trim();
			String desc = testData.get(20).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			// Add entity and assign return
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.assignReturnToEntity(entity, regulator, returnName.split("#"), ugs.split("#"), pgs.split("#"));

			// Edit the form variable
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.inputRequiredFieldsOfVariable(regulator, value);
			entityManagePage = entityManagePage.assignVariable(regulator);
			listPage = entityManagePage.backToDashboard();

			// Navigate to form variable page.
			FormVariablePage formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entity);
			formVariablePage.editVariableValue(entity, returnName, desc, valueOfCB, "input");
			listPage = formVariablePage.backToDashboard();

			// Create the new return CIMACPIS
			listPage.getProductListPage(regulator1, null, null, null);
			listPage.getProductListPage(regulator2, entity, "All", "All");
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(cellName1, value1);
			formInstancePage.editCellValue(cellName2, value2);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value3);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName3);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, valueOfCB, "", desc, ""));
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
	public void test6670() throws Exception
	{
		String caseID = "6670";
		logger.info("==== Verify entity variable in sum rule work well for all instance[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String ugs = testData.get(3).trim();
			String pgs = testData.get(4).trim();
			String value = testData.get(5).trim();
			String valueOfCB = testData.get(6).trim();
			String regulator1 = testData.get(7).trim();
			String regulator2 = testData.get(8).trim();
			String referenceDate = testData.get(9).trim();
			String page = testData.get(10).trim();
			String cellName1 = testData.get(11).trim();
			String instanceName = testData.get(12).trim();
			String cellName3 = testData.get(13).trim();
			String value1 = testData.get(14).trim();
			String value2 = testData.get(15).trim();
			String value3 = testData.get(16).trim();
			String columnName = testData.get(17).trim();
			String expression = testData.get(18).trim();
			String itemName = testData.get(19).trim();
			String desc = testData.get(20).trim();
			String instanceName2 = testData.get(21).trim();
			String valueOfAll = testData.get(22).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			// Add entity and assign return
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.assignReturnToEntity(entity, regulator, returnName.split("#"), ugs.split("#"), pgs.split("#"));

			// Edit the form variable
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.inputRequiredFieldsOfVariable(regulator, value);
			entityManagePage.updateColumnValueOfDate(regulator, desc, valueOfCB);
			entityManagePage = entityManagePage.assignVariable(regulator);
			listPage = entityManagePage.backToDashboard();

			// Create the new return CIMACPIS
			listPage.getProductListPage(regulator1, null, null, null);
			listPage.getProductListPage(regulator2, entity, "All", "All");
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(cellName1, value1);

			// Add new instance 2
			formInstancePage.addInstance(instanceName);
			formInstancePage.editCellValue(cellName1, value2);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value3);
			formInstancePage.selectInstance(instanceName2);
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value3);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName3);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, valueOfAll, "", desc, ""));
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
	public void test6672() throws Exception
	{
		String caseID = "6672";
		logger.info("==== Dashboard FV-01-04 Verify form variable in sum rule work well for all instance[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String ugs = testData.get(3).trim();
			String pgs = testData.get(4).trim();
			String value = testData.get(5).trim();
			String valueOfCB = testData.get(6).trim();
			String regulator1 = testData.get(7).trim();
			String regulator2 = testData.get(8).trim();
			String referenceDate = testData.get(9).trim();
			String page = testData.get(10).trim();
			String cellName1 = testData.get(11).trim();
			String instanceName = testData.get(12).trim();
			String cellName3 = testData.get(13).trim();
			String value1 = testData.get(14).trim();
			String value2 = testData.get(15).trim();
			String value3 = testData.get(16).trim();
			String columnName = testData.get(17).trim();
			String expression = testData.get(18).trim();
			String itemName = testData.get(19).trim();
			String desc = testData.get(20).trim();
			String instanceName2 = testData.get(21).trim();
			String valueOfAll = testData.get(22).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			// Add entity and assign return
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.assignReturnToEntity(entity, regulator, returnName.split("#"), ugs.split("#"), pgs.split("#"));

			// Edit the form variable
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.inputRequiredFieldsOfVariable(regulator, value);
			entityManagePage = entityManagePage.assignVariable(regulator);
			listPage = entityManagePage.backToDashboard();

			// Navigate to form variable page.
			FormVariablePage formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entity);
			formVariablePage.editVariableValue(entity, returnName, desc, valueOfCB, "Date");
			listPage = formVariablePage.backToDashboard();

			// Create the new return CIMACPIS
			listPage.getProductListPage(regulator1, null, null, null);
			listPage.getProductListPage(regulator2, entity, "All", "All");
			FormInstancePage formInstancePage = listPage.createNewForm(entity, referenceDate, returnName, null, false, false);

			// Edit the cell value
			formInstancePage.selectPage(page);
			formInstancePage.editCellValue(cellName1, value1);

			// Add new instance 2
			formInstancePage.addInstance(instanceName);
			formInstancePage.editCellValue(cellName1, value2);

			// Check the cell value
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value3);
			formInstancePage.selectInstance(instanceName2);
			Assert.assertEquals(formInstancePage.getCellText(cellName3), value3);

			// Double click the cell
			AllocationPage allocationPage = formInstancePage.cellDoubleClick(cellName3);
			Assert.assertTrue(allocationPage.getSumTabHeader().contains(columnName));
			Assert.assertEquals(allocationPage.getSumRule(), expression);
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, valueOfAll, "", desc, ""));
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
	public void test6742() throws Exception
	{
		String caseID = "6742";
		logger.info("==== Verify entity variable in sum rule of extend grid work well[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String ugs = testData.get(3).trim();
			String pgs = testData.get(4).trim();
			String value = testData.get(5).trim();
			String valueOfCB = testData.get(6).trim();
			String regulator1 = testData.get(7).trim();
			String regulator2 = testData.get(8).trim();
			String referenceDate = testData.get(9).trim();
			String page = testData.get(10).trim();
			String cellName1 = testData.get(11).trim();
			String cellName2 = testData.get(12).trim();
			String cellName3 = testData.get(13).trim();
			String value1 = testData.get(14).trim();
			String value2 = testData.get(15).trim();
			String value3 = testData.get(16).trim();
			String columnName = testData.get(17).trim();
			String expression = testData.get(18).trim();
			String itemName = testData.get(19).trim();
			String desc = testData.get(20).trim();
			String cellId = testData.get(21).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			// Add entity and assign return
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.assignReturnToEntity(entity, regulator, returnName.split("#"), ugs.split("#"), pgs.split("#"));

			// Edit the form variable
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.inputRequiredFieldsOfVariable(regulator, value);
			entityManagePage.updateColumnValueOfInput(regulator, desc, valueOfCB);
			entityManagePage = entityManagePage.assignVariable(regulator);
			listPage = entityManagePage.backToDashboard();

			// Create the new return CRIR
			listPage.getProductListPage(regulator1, null, null, null);
			listPage.getProductListPage(regulator2, entity, "All", "All");
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
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, valueOfCB, "", desc, ""));
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
	public void test6743() throws Exception
	{
		String caseID = "6743";
		logger.info("==== Verify form variable in sum rule of extend grid work well[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String ugs = testData.get(3).trim();
			String pgs = testData.get(4).trim();
			String value = testData.get(5).trim();
			String valueOfCB = testData.get(6).trim();
			String regulator1 = testData.get(7).trim();
			String regulator2 = testData.get(8).trim();
			String referenceDate = testData.get(9).trim();
			String page = testData.get(10).trim();
			String cellName1 = testData.get(11).trim();
			String cellName2 = testData.get(12).trim();
			String cellName3 = testData.get(13).trim();
			String value1 = testData.get(14).trim();
			String value2 = testData.get(15).trim();
			String value3 = testData.get(16).trim();
			String columnName = testData.get(17).trim();
			String expression = testData.get(18).trim();
			String itemName = testData.get(19).trim();
			String desc = testData.get(20).trim();
			String cellId = testData.get(21).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			// Add entity and assign return
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.assignReturnToEntity(entity, regulator, returnName.split("#"), ugs.split("#"), pgs.split("#"));

			// Edit the form variable
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.inputRequiredFieldsOfVariable(regulator, value);
			entityManagePage = entityManagePage.assignVariable(regulator);
			listPage = entityManagePage.backToDashboard();

			// Navigate to form variable page
			FormVariablePage formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entity);
			formVariablePage.editVariableValue(entity, returnName, desc, valueOfCB, "Input");
			listPage = formVariablePage.backToDashboard();

			// Create the new return CRIR
			listPage.getProductListPage(regulator1, null, null, null);
			listPage.getProductListPage(regulator2, entity, "All", "All");
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
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, valueOfCB, "", desc, ""));
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
	public void test6756() throws Exception
	{
		String caseID = "6756";
		logger.info("==== Verify entity variable in function sum rule of extend grid work well[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String ugs = testData.get(3).trim();
			String pgs = testData.get(4).trim();
			String value = testData.get(5).trim();
			String valueOfCB = testData.get(6).trim();
			String regulator1 = testData.get(7).trim();
			String regulator2 = testData.get(8).trim();
			String referenceDate = testData.get(9).trim();
			String page = testData.get(10).trim();
			String cellName1 = testData.get(11).trim();
			String cellName2 = testData.get(12).trim();
			String value1 = testData.get(13).trim();
			String value2 = testData.get(14).trim();
			String columnName = testData.get(15).trim();
			String expression = testData.get(16).trim();
			String itemName = testData.get(17).trim();
			String desc = testData.get(18).trim();
			String cellId = testData.get(19).trim();
			String valueOfAll = testData.get(20).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			// Add entity and assign return
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.assignReturnToEntity(entity, regulator, returnName.split("#"), ugs.split("#"), pgs.split("#"));

			// Edit the form variable
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.inputRequiredFieldsOfVariable(regulator, value);
			entityManagePage.updateColumnValueOfDate(regulator, desc, valueOfCB);
			entityManagePage = entityManagePage.assignVariable(regulator);
			listPage = entityManagePage.backToDashboard();

			// Create the new return CRIR
			listPage.getProductListPage(regulator1, null, null, null);
			listPage.getProductListPage(regulator2, entity, "All", "All");
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
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, valueOfAll, "", desc, ""));
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
	public void test6757() throws Exception
	{
		String caseID = "6757";
		logger.info("==== Verify form variable in function sum rule of extend grid work well[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_FormVariable, nodeName);
			String entity = testData.get(0);
			String regulator = testData.get(1);
			String returnName = testData.get(2).trim();
			String ugs = testData.get(3).trim();
			String pgs = testData.get(4).trim();
			String value = testData.get(5).trim();
			String valueOfCB = testData.get(6).trim();
			String regulator1 = testData.get(7).trim();
			String regulator2 = testData.get(8).trim();
			String referenceDate = testData.get(9).trim();
			String page = testData.get(10).trim();
			String cellName1 = testData.get(11).trim();
			String cellName2 = testData.get(12).trim();
			String value1 = testData.get(13).trim();
			String value2 = testData.get(14).trim();
			String columnName = testData.get(15).trim();
			String expression = testData.get(16).trim();
			String itemName = testData.get(17).trim();
			String desc = testData.get(18).trim();
			String cellId = testData.get(19).trim();
			String valueOfAll = testData.get(20).trim();

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			// Add entity and assign return
			entityManagePage = entityManagePage.addRootEntityIfNotExist(entity, entity, entity, true);
			entityManagePage.assignReturnToEntity(entity, regulator, returnName.split("#"), ugs.split("#"), pgs.split("#"));

			// Edit the form variable
			entityManagePage.openFormVariablePage(entity);
			entityManagePage.inputRequiredFieldsOfVariable(regulator, value);
			entityManagePage = entityManagePage.assignVariable(regulator);
			listPage = entityManagePage.backToDashboard();

			// Navigate to form variable page
			FormVariablePage formVariablePage = listPage.openFormVariablePage();
			formVariablePage = formVariablePage.filterByEntityColume(entity);
			formVariablePage.editVariableValue(entity, returnName, desc, valueOfCB, "Date");
			listPage = formVariablePage.backToDashboard();

			// Create the new return CRIR
			listPage.getProductListPage(regulator1, null, null, null);
			listPage.getProductListPage(regulator2, entity, "All", "All");
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
			Assert.assertTrue(allocationPage.checkSumRowOfFormVariable(itemName, valueOfAll, "", desc, ""));
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