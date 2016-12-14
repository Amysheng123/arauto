package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 12/1/2015
 */
public class Admin_Entity extends TestTemplate
{
	@Test
	public void test4772() throws Exception
	{
		String caseID = "4772";
		logger.info("====Test add entity to root[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String EntityNames = testData.get(0);
			String EntityCodes = testData.get(1);
			String EntityDescs = testData.get(2);

			String Names[];
			String Codes[];
			String Descs[];
			if (!EntityNames.contains("#"))
			{
				Names = new String[]
				{ EntityNames };
				Codes = new String[]
				{ EntityCodes };
				Descs = new String[]
				{ EntityDescs };
			}
			else
			{
				Names = EntityNames.split("#");
				Codes = EntityCodes.split("#");
				Descs = EntityDescs.split("#");
			}

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			for (int i = 0; i < Names.length; i++)
			{
				entityManagePage.addRootEntity(Names[i], Codes[i], Descs[i], true);
				assertThat(entityManagePage.getAllEntityName()).as("New added entity should be displayed").contains(Names[i]);
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4773() throws Exception
	{
		String caseID = "4773";
		logger.info("====Test add entity to parent[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Parent = testData.get(0);
			String EntityNames = testData.get(1);
			String EntityCodes = testData.get(2);
			String EntityDescs = testData.get(3);
			String Names[];
			String Codes[];
			String Descs[];
			if (EntityNames.contains("#"))
			{
				Names = EntityNames.split("#");
				Codes = EntityCodes.split("#");
				Descs = EntityDescs.split("#");
			}
			else
			{
				Names = new String[]
				{ EntityNames };
				Codes = new String[]
				{ EntityCodes };
				Descs = new String[]
				{ EntityDescs };
			}

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			for (int i = 0; i < Names.length; i++)
			{
				entityManagePage.addEntity(Parent, Names[i], Codes[i], Descs[i], true);
				assertThat(entityManagePage.getAllEntityName()).as("New added entity should be displayed").contains(Names[i]);

				assertThat(entityManagePage.getParentEntity(Names[i])).as("Entity[" + Names[i] + "]'s parent should be " + Parent).isEqualTo(Parent);
			}
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}

	}

	@Test
	public void test4774() throws Exception
	{
		String caseID = "4774";
		logger.info("====Test add entity to child[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Child = testData.get(0);
			String EntityNames = testData.get(1);
			String EntityCodes = testData.get(2);
			String EntityDescs = testData.get(3);
			String Names[] = null;
			String Codes[] = null;
			String Descs[] = null;
			if (EntityNames.contains("#"))
			{
				Names = EntityNames.split("#");
				Codes = EntityCodes.split("#");
				Descs = EntityDescs.split("#");
			}
			else
			{
				Names = new String[]
				{ EntityNames };
				Codes = new String[]
				{ EntityCodes };
				Descs = new String[]
				{ EntityDescs };
			}

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			for (int i = 0; i < Names.length; i++)
			{
				entityManagePage.addEntity(Child, Names[i], Codes[i], Descs[i], true);
				assertThat(entityManagePage.getAllEntityName()).as("New added entity should be displayed").contains(Names[i]);
				assertThat(entityManagePage.getParentEntity(Names[i])).as("Entity[" + Names[i] + "]'s parent should be " + Child).isEqualTo(Child);
			}
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",4974", testRst, "Admin_Entity");
		}

	}

	@Test
	public void test4778() throws Exception
	{
		String caseID = "4778";
		logger.info("====Test update entity[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String newName = testData.get(1);
			String newCode = testData.get(2);
			String newDesc = testData.get(3);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.editEntity(Entity, newName, newCode, newDesc);
			assertThat(entityManagePage.getAllEntityName()).as("Updated entity should be displayed").contains(newName);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}

	}

	@Test
	public void test4782() throws Exception
	{
		String caseID = "4782";
		logger.info("====Test delete entity without child and form instance[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.deleteEntity(Entity);
			assertThat(Entity).as("Delete entity should not be displayed").isNotIn(entityManagePage.getAllEntityName());
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4786() throws Exception
	{
		String caseID = "4786";
		logger.info("====Test delete entity with child (no form instance)[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			assertThat(entityManagePage.isDeleteEntityWithInstanceFailed(Entity)).isEqualTo(true);
			assertThat(entityManagePage.getAllEntityName()).contains(Entity);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4787() throws Exception
	{
		String caseID = "4787";
		logger.info("====Test delete entity with form instance (no child)[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String ProductPrefxi = testData.get(1);
			String returnName = testData.get(2);
			String UG = testData.get(3);
			String PG = testData.get(4);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.assignReturnToEntity(Entity, ProductPrefxi, returnName.split("#"), UG.split("#"), PG.split("#"));
			entityManagePage.backToDashboard();

			HomePage homePage = listPage.logout();
			homePage.logon();
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, null, returnName, null, false, false);
			formInstancePage.closeFormInstance();

			entityManagePage = listPage.EnterEntityPage();
			assertThat(entityManagePage.isDeleteEntityWithInstanceFailed(Entity)).isEqualTo(true);
			assertThat(entityManagePage.getAllEntityName()).contains(Entity);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4788() throws Exception
	{
		String caseID = "4788";
		logger.info("====Test the entity name or entity code  of an active entity Can NOT be re-used in the same entity tree[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Child = testData.get(0);
			String EntityNames = testData.get(1);
			String EntityCodes = testData.get(2);
			String EntityDescs = testData.get(3);
			String Names[] = null;
			String Codes[] = null;
			String Descs[] = null;
			if (EntityNames.contains("#"))
			{
				Names = EntityNames.split("#");
				Codes = EntityCodes.split("#");
				Descs = EntityDescs.split("#");
			}
			else
			{
				Names = new String[]
				{ EntityNames };
				Codes = new String[]
				{ EntityCodes };
				Descs = new String[]
				{ EntityDescs };
			}

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			for (int i = 0; i < Names.length; i++)
			{
				boolean rst = false;
				if (i == 0)
				{
					rst = entityManagePage.isReUsedEntity(Child, Names[i], Codes[i], Descs[i], "name");
				}
				else
				{
					rst = entityManagePage.isReUsedEntity(Child, Names[i], Codes[i], Descs[i], "code");
				}
				if (!rst)
				{
					testRst = false;
					logger.error("Entity name[" + Names[i] + "]/code[" + Codes[i] + "] should not be reused!");
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4789() throws Exception
	{
		String caseID = "4789";
		logger.info("====Test the entity name or entity code  of an active entity Can NOT be re-used in the different entity tree[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Child = testData.get(0);
			String EntityNames = testData.get(1);
			String EntityCodes = testData.get(2);
			String EntityDescs = testData.get(3);
			String Names[] = null;
			String Codes[] = null;
			String Descs[] = null;
			if (EntityNames.contains("#"))
			{
				Names = EntityNames.split("#");
				Codes = EntityCodes.split("#");
				Descs = EntityDescs.split("#");
			}
			else
			{
				Names = new String[]
				{ EntityNames };
				Codes = new String[]
				{ EntityCodes };
				Descs = new String[]
				{ EntityDescs };
			}

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			for (int i = 0; i < Names.length; i++)
			{
				boolean rst = false;
				if (i == 0)
				{
					rst = entityManagePage.isReUsedEntity(Child, Names[i], Codes[i], Descs[i], "name");
				}
				else
				{
					rst = entityManagePage.isReUsedEntity(Child, Names[i], Codes[i], Descs[i], "code");
				}
				if (!rst)
				{
					testRst = false;
					logger.error("Entity name[" + Names[i] + "]/code[" + Codes[i] + "] should not be reused!");
				}
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID + ",4759", testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4790() throws Exception
	{
		String caseID = "4790";
		logger.info("====Test the entity name or entity code  of an deleted entity Can NOT be re-used in the same entity tree[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Child = testData.get(0);
			String EntityName = testData.get(1);
			String EntityCode = testData.get(2);
			String EntityDesc = testData.get(3);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			if (!entityManagePage.isReUsedEntity(Child, EntityName, EntityCode, EntityDesc, "name"))
			{
				testRst = false;
				logger.error("Deleted entity name[" + EntityName + "]/code[" + EntityCode + "] should not be reused!");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4791() throws Exception
	{
		String caseID = "4791";
		logger.info("====Test the entity name or entity code  of an deleted entity Can NOT be re-used in the different entity tree[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Child = testData.get(0);
			String EntityName = testData.get(1);
			String EntityCode = testData.get(2);
			String EntityDesc = testData.get(3);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			if (!entityManagePage.isReUsedEntity(Child, EntityName, EntityCode, EntityDesc, "name"))
			{
				testRst = false;
				logger.error("Deleted entity name[" + EntityName + "]/code[" + EntityCode + "] should not be reused!");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4792() throws Exception
	{
		String caseID = "4792";
		logger.info("====Verity the slider Can be used for reportting works[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Product = testData.get(1);
			String Return = testData.get(2);
			String Privilege = testData.get(3);
			String UserGroup = testData.get(4);
			String[] Returns = null;
			if (Return.contains("#"))
				Returns = Return.split("#");
			else
				Returns = new String[]
				{ Return };

			String[] Privileges = null;
			if (Privilege.contains("#"))
				Returns = Privilege.split("#");
			else
				Privileges = new String[]
				{ Privilege };

			String[] UserGroups = null;
			if (UserGroup.contains("#"))
				UserGroups = UserGroup.split("#");
			else
				UserGroups = new String[]
				{ UserGroup };

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.assignReturnToEntity(Entity, Product, Returns, UserGroups, Privileges);
			HomePage homePage = entityManagePage.logout();
			homePage.logon();
			if (!listPage.getGroupOptions().contains(Entity))
			{
				testRst = false;
				logger.error("Entity[" + Entity + "] should in entity list");
			}

			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.entityUsedForReporting(Entity, false);
			entityManagePage.backToDashboard();
			if (listPage.getGroupOptions().contains(Entity))
			{
				testRst = false;
				logger.error("Entity[" + Entity + "] should not in entity list after close slider");
			}
			entityManagePage.entityUsedForReporting(Entity, true);
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4793() throws Exception
	{
		String caseID = "4793";
		logger.info("====Test disable the reporting function of a parent entity if impact the child entity[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Parent = testData.get(0);
			String Child = testData.get(1);
			String Product = testData.get(2);
			String Return = testData.get(3);
			String Privilege = testData.get(4);
			String UserGroup = testData.get(5);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			List<String> entityInfo = entityManagePage.getEntityInfo(Parent);
			if (entityInfo.get(4).equalsIgnoreCase("n"))
				entityManagePage.entityUsedForReporting(Parent, true);
			entityManagePage.addEntity(Parent, Child, Child, Child, true);
			entityManagePage.entityUsedForReporting(Parent, false);
			entityManagePage.assignReturnToEntity(Child, Product, Return.split("#"), UserGroup.split("#"), Privilege.split("#"));
			entityManagePage.backToDashboard();
			HomePage homePage = listPage.logout();
			homePage.logon();
			if (listPage.getGroupOptions().contains(Parent))
			{
				testRst = false;
				logger.error("Parent entity[" + Parent + "] still in entity list");
			}

			if (!listPage.getGroupOptions().contains(Child))
			{
				testRst = false;
				logger.error("Child entity[" + Child + "] should in entity list");
			}

			FormInstancePage formInstancePage = listPage.createNewForm(Child, null, Return, null, false, false);
			if (formInstancePage == null)
			{
				testRst = false;
				logger.error("Create new form failed for entity[" + Child + "]");
			}
			closeFormInstance();
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4794() throws Exception
	{
		String caseID = "4794";
		logger.info("====Verify the deleted entity can be shown by switching the slider Hide or Show[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.addRootEntity(Entity, Entity, Entity, true);

			entityManagePage.deleteEntity(Entity);
			entityManagePage.showDeletedEntities();
			assertThat(entityManagePage.getAllEntityName()).contains(Entity);

			entityManagePage.hideDeletedEntities();
			assertThat(Entity).isNotIn(entityManagePage.getAllEntityName());
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4795() throws Exception
	{
		String caseID = "4795";
		logger.info("====Verify the deleted entity are not shown in the Entity setup page by default[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			assertThat(entityManagePage.isDefaultStatus()).isEqualTo(true);
			assertThat(Entity).isNotIn(entityManagePage.getAllEntityName());
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4798() throws Exception
	{
		String caseID = "4798";
		logger.info("====Verify the Audit Trail of deleted entity[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String ECR = testData.get(1);
			// TESTPRODUCT
			String group = "rpadmin_grp";
			String privilege = "Return Maker";

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.addRootEntity(Entity, Entity, Entity, true);
			entityManagePage.assignReturnToEntity(Entity, "TESTPRODUCT", ECR.split("#"), group.split("#"), privilege.split("#"));
			entityManagePage.deleteEntity(Entity);
			entityManagePage.showDeletedEntities();
			entityManagePage.openEntityEditPage(Entity);

			if (!entityManagePage.verifyDeleteEntityName(Entity))
				testRst = false;

			List<String> ECRList = entityManagePage.getAssignedReturnsListInDeleteEntity(Entity, "TESTPRODUCT");
			assertThat(ECR.split("#").length).isEqualTo(ECRList.size());

			for (String form : ECR.split("#"))
			{
				assertThat(ECRList).contains(form);
			}
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4799() throws Exception
	{
		String caseID = "4799";
		logger.info("====Verify the deleted entity can be restored[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");
			String Parent = getElementValueFromXML(testData_admin, nodeName, "ParentEntity");

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.restoreDeleteEntity(Entity, false);
			assertThat(entityManagePage.getEntityStatus(Entity)).isEqualTo("Deleted");

			entityManagePage.restoreDeleteEntity(Entity);
			assertThat(entityManagePage.getEntityStatus(Entity)).isEqualTo("Active");

			String par = entityManagePage.getParentEntity(Entity);
			assertThat(par).isEqualTo(Parent);
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4801() throws Exception
	{
		String caseID = "4801";
		logger.info("====Verify the entity details structure and returns at deletion date can be restored[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Regulator = testData.get(1);
			String Return = testData.get(2);
			String Privilege = testData.get(3);
			String UserGroup = testData.get(4);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			String parentEntity = Entity + getRandomString(2);
			entityManagePage.addRootEntity(parentEntity, parentEntity, parentEntity, true);
			entityManagePage.addEntity(parentEntity, Entity, Entity, Entity, true);
			entityManagePage.assignReturnToEntity(Entity, Regulator, Return.split("#"), UserGroup.split("#"), Privilege.split("#"));
			entityManagePage.deleteEntity(Entity);
			entityManagePage.restoreDeleteEntity(Entity);

			String par = entityManagePage.getParentEntity(Entity);
			assertThat(par).isEqualTo(parentEntity);

			logger.info("Check if return " + Return + " be assigned to " + Entity);
			List<String> returnList = entityManagePage.getAssignedReturns(Entity, Regulator);
			for (String returName : Return.split("#"))
			{
				assertThat(returnList).contains(returName);
			}

			logger.info("Check if user group " + UserGroup + " be assigned to " + Entity);
			List<String> pg = entityManagePage.getAssignedprivilegeGroups(Entity, Return, UserGroup);
			assertThat(pg).contains(Privilege);
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",4743", testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4804() throws Exception
	{
		String caseID = "4804";
		logger.info("====Verify the default displaying mode of entity tree[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			logger.info("Check if default status is: Hide deleted entities");
			boolean rst = entityManagePage.isDefaultStatus();
			if (!rst)
			{
				testRst = false;
				logger.error("Failed");
			}

			logger.info("Check if all entities are expanded");
			rst = entityManagePage.isExpandAllSubItems();
			if (!rst)
			{
				testRst = false;
				logger.error("Failed");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4975() throws Exception
	{
		String caseID = "4975";
		logger.info("====Verify child entity CAN NOT be added for a deleted entity[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			String deleteEntity = getElementValueFromXML(testData_admin, nodeName, "Entity");
			String addEntity = getElementValueFromXML(testData_admin, nodeName, "Child");
			ListPage listPage = super.m.listPage;

			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.addRootEntity(deleteEntity, deleteEntity, deleteEntity, true);
			entityManagePage.deleteEntity(deleteEntity);
			entityManagePage.showDeletedEntities();
			entityManagePage.addEntity(deleteEntity, addEntity, addEntity, addEntity, true);
			String parent = entityManagePage.getParentEntity(addEntity);
			assertThat(parent).isNotEqualTo(deleteEntity);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4637() throws Exception
	{
		String caseID = "4637";
		logger.info("====Verify map entity and return with one regulator[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;

		try
		{
			String nodeName = "C" + caseID;
			String EntityName = getElementValueFromXML(testData_admin, nodeName, "EntityName");
			String Product = getElementValueFromXML(testData_admin, nodeName, "Product");
			// String ReturnNums = getElementValueFromXML(testData_admin,
			// nodeName, "ReturnNums");

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();
			entityManagePage.addRootEntity(EntityName, EntityName, EntityName, true);
			entityManagePage.assignAllReturnToEntity(Product, EntityName, false);
			int amt = entityManagePage.getAssignedReturns(EntityName, Product).size();
			if (amt != 0)
			{
				testRst = false;
				logger.error("Expected value is 0, but actual value is " + amt);
			} /*
				 * entityManagePage.assignAllReturnToEntity(Product, EntityName,
				 * true); amt = entityManagePage.getAssignedReturns(EntityName,
				 * Product).size(); if (amt != Integer.parseInt(ReturnNums)) {
				 * testRst = false; logger.error("Expected value is " +
				 * ReturnNums + ", but actual value is " + amt); }
				 */
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4747() throws Exception
	{
		String caseID = "4747";
		logger.info("====Verify the permission to return when the user is assigned to different user groups with different permission[case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			String Product = getElementValueFromXML(testData_admin, nodeName, "Product");
			String UserGroup = getElementValueFromXML(testData_admin, nodeName, "UserGroup");
			String EntityName = getElementValueFromXML(testData_admin, nodeName, "EntityName");
			String Return1 = getElementValueFromXML(testData_admin, nodeName, "Return1");
			String Return2 = getElementValueFromXML(testData_admin, nodeName, "Return2");
			String Privilege1 = getElementValueFromXML(testData_admin, nodeName, "Privilege1");
			String Privilege2 = getElementValueFromXML(testData_admin, nodeName, "Privilege2");
			String UserName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String Password = getElementValueFromXML(testData_admin, nodeName, "Password");

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage.addRootEntity(EntityName, EntityName, EntityName, true);
			entityManagePage.assignReturnToEntity(EntityName, Product, Return1.split("#"), UserGroup.split("#"), Privilege1.split("#"));
			entityManagePage.assignReturnToEntity(EntityName, Product, Return2.split("#"), UserGroup.split("#"), Privilege2.split("#"));
			entityManagePage.backToDashboard();
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);
			listPage.setRegulatorByValue(Product);
			listPage.setGroup(EntityName);

			List<String> forms = listPage.getFormOptions_Create(EntityName, null);

			if (forms.contains(Return1))
			{
				testRst = false;
				logger.error("Form[" + Return1 + "] not in create form list");
			}
			if (!forms.contains(Return2))
			{
				testRst = false;
				logger.error("Form[" + Return2 + "] should in create form list");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID + ",4745", testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4749() throws Exception
	{
		String caseID = "4749";
		logger.info("====Verify the permission to return when the user group is assigned to different entity[case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			String Product = getElementValueFromXML(testData_admin, nodeName, "Product");
			String UserGroup = getElementValueFromXML(testData_admin, nodeName, "UserGroup");
			String EntityName1 = getElementValueFromXML(testData_admin, nodeName, "EntityName1");
			String EntityName2 = getElementValueFromXML(testData_admin, nodeName, "EntityName2");
			String Return1 = getElementValueFromXML(testData_admin, nodeName, "Return1");
			String Return2 = getElementValueFromXML(testData_admin, nodeName, "Return2");
			String Privilege1 = getElementValueFromXML(testData_admin, nodeName, "Privilege1");
			String Privilege2 = getElementValueFromXML(testData_admin, nodeName, "Privilege2");
			String UserName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String Password = getElementValueFromXML(testData_admin, nodeName, "Password");

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage.assignReturnToEntity(EntityName1, Product, Return1.split("#"), UserGroup.split("#"), Privilege1.split("#"));
			entityManagePage.assignReturnToEntity(EntityName2, Product, Return2.split("#"), UserGroup.split("#"), Privilege2.split("#"));
			entityManagePage.backToDashboard();
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);
			listPage.setRegulatorByValue(Product);
			List<String> forms = listPage.getFormOptions_Create(EntityName1, null);

			if (!forms.contains(Return1))
			{
				testRst = false;
				logger.error("Form[" + Return1 + "] should in create form list");
			}
			if (forms.contains(Return2))
			{
				testRst = false;
				logger.error("Form[" + Return1 + "] should not in create form list");
			}

			forms = listPage.getFormOptions_Create(EntityName2, null);
			if (!forms.contains(Return2))
			{
				testRst = false;
				logger.error("Form[" + Return2 + "] should in create form list");
			}
			if (forms.contains(Return1))
			{
				testRst = false;
				logger.error("Form[" + Return1 + "] should in not  create form list");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4757() throws Exception
	{
		String caseID = "4757";
		logger.info("====Verify Create a return from EXCEL to which the user has no permission to access[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_admin, nodeName, "Regulator");
			String EntityName = getElementValueFromXML(testData_admin, nodeName, "EntityName");
			String fileName = getElementValueFromXML(testData_admin, nodeName, "File");
			String UserName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String Password = getElementValueFromXML(testData_admin, nodeName, "Password");
			ListPage listPage = super.loginAsOtherUser(UserName, Password);
			listPage.setRegulator(Regulator);
			listPage.setGroup(EntityName);
			File importFile = new File(testData_admin.replace("Admin.xml", fileName));
			String msg = listPage.getCreateFromExcelErrorMsg(importFile, false);
			if (!msg.equalsIgnoreCase("You do not have right to operate this return."))
			{
				testRst = false;
				logger.error("should prompt: You do not have right to operate this return. but expected meesage is:" + msg);
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4760() throws Exception
	{
		String caseID = "4760";
		logger.info("==== Verify deselect return from entity when this return  has no form instance existing[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityPage = null;
		try
		{
			String nodeName = "C" + caseID;
			String Product = getElementValueFromXML(testData_admin, nodeName, "Product");
			String EntityName = getElementValueFromXML(testData_admin, nodeName, "EntityName");
			String Return = getElementValueFromXML(testData_admin, nodeName, "Return");

			ListPage listPage = super.m.listPage;
			entityPage = listPage.EnterEntityPage();
			String msg = entityPage.getRemoveReturnFromEntityMessage(EntityName, Product, Return.split("#"));
			if (!msg.equalsIgnoreCase("Returns assigned to Entity Successfully"))
			{
				testRst = false;
				logger.error("Deselect return[" + Return + "] from entity[" + EntityName + "] failed");
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4761() throws Exception
	{
		String caseID = "4761";
		logger.info("====Verify deselect return from entity when this return  has form instance existing[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityPage = null;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_admin, nodeName, "Regulator");
			String Product = getElementValueFromXML(testData_admin, nodeName, "Product");
			String EntityName = getElementValueFromXML(testData_admin, nodeName, "EntityName");
			String Return = getElementValueFromXML(testData_admin, nodeName, "Return");
			String Message = getElementValueFromXML(testData_admin, nodeName, "Message").trim();
			String ProcessDate = getElementValueFromXML(testData_admin, nodeName, "ProcessDate").trim();

			ListPage listPage = super.m.listPage;
			entityPage = listPage.EnterEntityPage();
			entityPage.addRootEntity(EntityName, EntityName, EntityName, true);
			entityPage.assignReturnToEntity(EntityName, Product, Return.split("#"), new String[]
			{ "rpadmin_grp" }, new String[]
			{ "Return Maker" });
			listPage = entityPage.backToDashboard();
			HomePage homePage = listPage.logout();
			homePage.logon();
			listPage.getProductListPage(Regulator, EntityName, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(EntityName, ProcessDate, Return, null, false, false);
			formInstancePage.closeFormInstance();
			entityPage = listPage.EnterEntityPage();
			String msg = entityPage.getAssignReturnToEntityMessage(EntityName, Product, new String[]
			{ Return }).trim();
			if (!msg.equals(Message))
			{
				testRst = false;
				logger.error("Expected message is: " + Message + ",  but actual mesage is: " + msg);
			}
			entityPage.backToDashboard();
			listPage.getProductListPage(Regulator, EntityName, Return, null);
			listPage.deleteFormInstance(Return, ProcessDate);
			entityPage = listPage.EnterEntityPage();
			entityPage.removeReturnFromEntity(EntityName, Product, new String[]
			{ Return });
			if (entityPage.getAssignedReturns(EntityName, Product).size() != 0)
			{
				testRst = false;
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test5589() throws Exception
	{
		String caseID = "5589";
		logger.info("====Verify special characters in entity code can be added[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			String EntityName = getElementValueFromXML(testData_admin, nodeName, "EntityName");
			String Code = getElementValueFromXML(testData_admin, nodeName, "Code");
			String Desc = getElementValueFromXML(testData_admin, nodeName, "Desc");

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntity(EntityName, Code, Desc, true);
			List<String> entities = entityManagePage.getAllEntityName();
			assertThat(entities).contains(EntityName);
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test5590() throws Exception
	{
		String caseID = "5590";
		logger.info("====Verify restricting characters in entity code can not be added[case id=" + caseID + "]====");
		boolean testRst = true;
		try
		{
			String nodeName = "C" + caseID;
			String EntityName = getElementValueFromXML(testData_admin, nodeName, "EntityName");
			String Code = getElementValueFromXML(testData_admin, nodeName, "Code");
			String Desc = getElementValueFromXML(testData_admin, nodeName, "Desc");

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntity(EntityName, Code, Desc, true);

			if (entityManagePage.getAllEntityName().contains(Code))
			{
				testRst = false;
				logger.error("Add entity[name=" + EntityName + ",code=" + Code + ",desc=" + Desc + "] failed");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test5609() throws Exception
	{
		String caseID = "5609";
		logger.info("====Verify restricting characters in entity code can not be added[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String EntityName = testData.get(1);
			String Code = testData.get(2);
			String Desc = testData.get(3);

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();

			String msg = entityManagePage.editEntity(Entity, EntityName, Code, Desc);
			assertThat(msg).isEqualTo("character \"|\" is not allowed in entity Code");
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4638() throws Exception
	{
		String caseID = "4638";
		logger.info("====Verify restricting characters in entity code can not be added[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Code = testData.get(1);
			String Desc = testData.get(2);
			String Regulator = testData.get(3);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntity(Entity, Code, Desc, true);
			if (!entityManagePage.isRegulatorExist(Entity, Regulator))
				testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4752() throws Exception
	{
		String caseID = "4752";
		logger.info("====Verify the permission to return when the user group is assigned to different regulator return[case id=" + caseID + "]====");
		boolean testRst = false;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Regulators = testData.get(1);
			String Returns = testData.get(2);
			String ug = testData.get(3);
			String Privileges = testData.get(4);

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntity(Entity, Entity, Entity, true);
			entityManagePage.assignReturnToEntity(Entity, Regulators.split("#")[0], new String[]
			{ Returns.split("#")[0] }, new String[]
			{ ug }, new String[]
			{ Privileges.split("#")[0] });
			entityManagePage.assignReturnToEntity(Entity, Regulators.split("#")[1], new String[]
			{ Returns.split("#")[1] }, new String[]
			{ ug }, new String[]
			{ Privileges.split("#")[1] });

			listPage = entityManagePage.backToDashboard();
			listPage.setRegulatorByValue(Regulators.split("#")[0]);
			listPage.setGroup(Entity);

			assertThat(listPage.getFormOptions().size()).isEqualTo(1);
			assertThat(listPage.getFormOptions().get(0)).isEqualTo(Returns.split("#")[0]);

			listPage.setRegulatorByValue(Regulators.split("#")[1]);
			listPage.setGroup(Entity);

			assertThat(listPage.getFormOptions().size()).isEqualTo(1);
			assertThat(listPage.getFormOptions().get(0)).isEqualTo(Returns.split("#")[1]);

			List<String> optionsList = listPage.getFormOptions_Create(Entity, null);
			assertThat(optionsList.size()).isEqualTo(1);
			assertThat(optionsList.get(0)).isEqualTo(Returns.split("#")[1]);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",4755", testRst, "Admin_Entity");
		}
	}

	@Test
	public void test4891() throws Exception
	{
		String caseID = "4891";
		logger.info("====Verify the return viewer UI when the user only has return viewer priviledge[case id=" + caseID + "]====");
		boolean testRst = false;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Regulator = testData.get(1);
			String RegulatorPrefix = testData.get(2);
			String Return = testData.get(3);
			String ug = testData.get(4);
			String Privileges = testData.get(5);
			String user = testData.get(6);

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntity(Entity, Entity, Entity, true);
			entityManagePage.assignReturnToEntity(Entity, RegulatorPrefix, new String[]
			{ Return }, new String[]
			{ ug.split("#")[0] }, new String[]
			{ Privileges.split("#")[0] });
			entityManagePage.assignReturnToEntity(Entity, RegulatorPrefix, new String[]
			{ Return }, new String[]
			{ ug.split("#")[1] }, new String[]
			{ Privileges.split("#")[1] });

			boolean s1 = false;
			boolean s2 = false;
			boolean s3 = false;
			listPage = entityManagePage.backToDashboard();
			HomePage homePage = listPage.logout();
			homePage.logon();
			listPage.setRegulatorByValue(Regulator);
			listPage.setGroup(Entity);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, null, Return, null, false, false);
			listPage = formInstancePage.closeFormInstance();

			homePage = listPage.logout();
			listPage = homePage.loginAs(user, "password");
			listPage.setRegulatorByValue(Regulator);
			listPage.setGroup(Entity);
			formInstancePage = listPage.openFirstFormInstance();
			if (!formInstancePage.isImportAdjustmentEnabled())
				s1 = true;
			if (!formInstancePage.isWorkflowDisplayed())
				s2 = true;

			if (formInstancePage.isExportToExcelCsvDisplayed())
				s3 = true;
			if (s1 && s2 && s3)
				testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test5588() throws Exception
	{
		String caseID = "5588";
		logger.info("====Verify the privilege works under the entity with special characters in entity code[case id=" + caseID + "]====");
		boolean testRst = false;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Code = testData.get(1);
			String Regulator = testData.get(2);
			String Return = testData.get(3);
			String ug = testData.get(4);
			String Privileges = testData.get(5);

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntity(Entity, Code, Entity, true);
			entityManagePage.assignReturnToEntity(Entity, Regulator, new String[]
			{ Return }, new String[]
			{ ug }, new String[]
			{ Privileges });

			listPage = entityManagePage.backToDashboard();
			listPage.setRegulatorByValue(Regulator);
			listPage.setGroup(Entity);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, null, Return, null, false, false);
			if (formInstancePage != null)
				testRst = true;
			listPage = formInstancePage.closeFormInstance();

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test5587() throws Exception
	{
		String caseID = "5587";
		logger.info("====Verify the privilege not works under the entity with special characters in entity code[case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Code = testData.get(1);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			String msg = entityManagePage.addRootEntity(Entity, Code, Entity, true);
			if (msg.equals("character \"|\" is not allowed in entity Code"))
				testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test6046() throws Exception
	{
		String caseID = "6046";
		logger.info("====Verify user can view activate date and deactivate date in Entity Setup page [case id=" + caseID + "]====");
		boolean testRst = false;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Regulator = testData.get(1);
			String Return = testData.get(2);
			String active = testData.get(3);
			String deactive = testData.get(4);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntity(Entity, Entity, Entity, true);
			List<String> date = entityManagePage.getActiveAndDeactiveDate(Entity, Regulator, Return);
			if (date.get(0).equals(active) && date.get(1).equals(deactive))
				testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}

	@Test
	public void test6048() throws Exception
	{
		String caseID = "6048";
		logger.info("====Verify the format of activate date and deactivate date is same as the locale settings[case id=" + caseID + "]====");
		boolean testRst = true;
		EntityPage entityManagePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_admin, nodeName);
			String Entity = testData.get(0);
			String Regulator = testData.get(1);
			String Return = testData.get(2);
			String ActiveDates = testData.get(3);
			String LanguageRegions = testData.get(4);

			ListPage listPage = super.m.listPage;
			entityManagePage = listPage.EnterEntityPage();

			entityManagePage.addRootEntity(Entity, Entity, Entity, true);
			for (int i = 0; i < LanguageRegions.split("#").length; i++)
			{
				PreferencePage preferencePage = entityManagePage.enterPreferencePage();
				preferencePage.selectLanguage(LanguageRegions.split("#")[i]);
				List<String> date = entityManagePage.getActiveAndDeactiveDate(Entity, Regulator, Return);
				if (!date.get(0).equals(ActiveDates.split("#")[i]))
					testRst = false;
			}
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Entity");
		}
	}
}
