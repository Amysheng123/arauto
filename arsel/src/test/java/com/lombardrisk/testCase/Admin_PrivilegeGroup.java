package com.lombardrisk.testCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;

/**
 * Create by Leo Tu on Dec 21, 2015
 */

public class Admin_PrivilegeGroup extends TestTemplate
{

	@Test
	public void preCondition() throws Exception
	{
		ListPage listPage = super.m.listPage;
		PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
		privilegeGroupPage.addPrivilegeGroup("Admin1", "Admin1", "Entity Related Privileges");
		privilegeGroupPage.addPrivilegeGroup("Empty", "Empty", "Entity Related Privileges");
		privilegeGroupPage.addAllPrivileges("Admin1");
		privilegeGroupPage.backToDashboard();

		EntityPage entityManagePage = listPage.EnterEntityPage();
		entityManagePage.addRootEntity("1999", "1999", "1999", true);
		entityManagePage.addRootEntity("1998", "1998", "1998", true);
		entityManagePage.assignReturnToEntity("1999", "TESTPRODUCT", new String[]
		{ "CAR v5" }, new String[]
		{ "Team1" }, new String[]
		{ "Admin1" });
		entityManagePage.assignReturnToEntity("1999", "TESTPRODUCT", new String[]
		{ "CAR v5" }, new String[]
		{ "Team1" }, new String[]
		{ "Empty" });
		entityManagePage.backToDashboard();

		FormInstancePage formInstancePage = listPage.createNewForm("1999", "31/12/2015", "CAR v5", null, false, false);
		formInstancePage.closeFormInstance();
	}

	@Test
	public void test4555() throws Exception
	{
		String caseID = "4555";
		logger.info("====Verify Permission Group can be added[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String GroupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String Description = getElementValueFromXML(testData_admin, nodeName, "Description");
			String Type = getElementValueFromXML(testData_admin, nodeName, "Type");

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.addPrivilegeGroup(GroupName, Description, Type);
			if (!privilegeGroupPage.getAllCustomPG().contains(GroupName))
			{
				testRst = false;
				logger.error("Add pg[" + GroupName + "] failed!");
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4565() throws Exception
	{
		String caseID = "4565";
		logger.info("====Verify Permission Group can be edited[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String GroupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String NewGroupName = getElementValueFromXML(testData_admin, nodeName, "NewGroupName");
			String NewDescription = getElementValueFromXML(testData_admin, nodeName, "NewDescription");
			String AddPrivileges = getElementValueFromXML(testData_admin, nodeName, "AddPrivilege").trim();
			String DelPrivileges = getElementValueFromXML(testData_admin, nodeName, "DeletePrivilege").trim();

			if (NewGroupName.length() == 0)
			{
				NewGroupName = GroupName;
			}

			List<String> addPrivilegesName = new ArrayList<String>();

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.editPrivilegeGroup(GroupName, NewGroupName, NewDescription);
			if (!privilegeGroupPage.getAllCustomPG().contains(NewGroupName))
			{
				testRst = false;
				logger.error("Edit pg[" + GroupName + "] to [" + NewGroupName + "] failed");
			}

			privilegeGroupPage.addPrivileges(NewGroupName, AddPrivileges.split("#"));

			List<String> currentPrivileges = privilegeGroupPage.getPrivilegeByPG(NewGroupName);
			for (String privilege : addPrivilegesName)
			{
				if (!currentPrivileges.contains(privilege))
				{
					testRst = false;
					logger.error("Cannot find added privilege[" + privilege + "] in " + NewGroupName);
				}

			}

			privilegeGroupPage.deletePrivilege(NewGroupName, DelPrivileges.split("#"));

			currentPrivileges = privilegeGroupPage.getPrivilegeByPG(NewGroupName);
			for (String privilege : DelPrivileges.split("#"))
			{
				if (currentPrivileges.contains(privilege))
				{
					testRst = false;
					logger.error("Still find deleted privilege[" + privilege + "] in " + NewGroupName);
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4566() throws Exception
	{
		String caseID = "4566";
		logger.info("====Verify Permission Group can be deleted[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_admin, nodeName);
			String GroupWithReturn = testdata.get(0);
			String Entity = testdata.get(1);
			String Product = testdata.get(2);
			String Return = testdata.get(3);
			String UserGroup = testdata.get(4);
			String GroupWithoutReturn = testdata.get(5);

			String Type = "Entity Related Privileges";
			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.addPrivilegeGroup(GroupWithReturn, GroupWithReturn, Type);
			privilegeGroupPage.addPrivilegeGroup(GroupWithoutReturn, GroupWithReturn, Type);
			privilegeGroupPage.backToDashboard();

			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage.assignReturnToEntity(Entity, Product, Return.split("#"), UserGroup.split("#"), GroupWithReturn.split("#"));
			entityManagePage.backToDashboard();

			privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.deletePrivilegeGroup(GroupWithoutReturn);
			if (privilegeGroupPage.getAllCustomPG().contains(GroupWithoutReturn))
			{
				testRst = false;
				logger.error("Delete pg[" + GroupWithoutReturn + "] without return failed");
			}

			privilegeGroupPage.deletePrivilegeGroup(GroupWithReturn);
			if (!privilegeGroupPage.getAllCustomPG().contains(GroupWithReturn))
			{
				testRst = false;
				logger.error("The pg[" + GroupWithoutReturn + "] with return failed");
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4679() throws Exception
	{
		String caseID = "4679";
		logger.info("====Verify Permission Group can not be added with not unique name[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_admin, nodeName);
			String GroupName = testdata.get(0);
			String Description = testdata.get(1);
			String Type = testdata.get(2);

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.addPrivilegeGroup(GroupName, Description, Type);
			if (privilegeGroupPage.addPrivilegeGroup(GroupName, Description, Type))
			{
				testRst = false;
				logger.error("The pg[" + GroupName + "] should not be added, becasue name is not unique");
			}
			privilegeGroupPage.deletePrivilegeGroup(GroupName);
			privilegeGroupPage.addPrivilegeGroup(GroupName, Description, Type);
			if (!privilegeGroupPage.getAllCustomPG().contains(GroupName))
			{
				testRst = false;
				logger.error("Add pg[" + GroupName + "] failed!");
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4684() throws Exception
	{
		String caseID = "4684";
		logger.info("====Verify Permission Group with Group Structure privilege[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_admin, nodeName);
			String GroupName = testdata.get(0);
			String UserName = testdata.get(1);
			String Password = testdata.get(2);
			String DeletePrivilege = testdata.get(3);
			String Entity = testdata.get(4);

			List<String> DeletePrivileges = new ArrayList<String>();
			if (DeletePrivilege.contains("#"))
			{
				for (String privilege : DeletePrivilege.split("#"))
				{
					DeletePrivileges.add(privilege);
				}
			}
			else
			{
				DeletePrivileges.add(DeletePrivilege);
			}

			ListPage listPage = super.m.listPage;
			UsersPage usersPage = listPage.EnterUserPage();
			usersPage.addPrivilegeGroup(UserName, GroupName);
			usersPage.backToDashboard();

			// Verfy if user [UserName] could add/edit/delete entity
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);

			EntityPage entityManagePage = listPage.EnterEntityPage();
			if (!entityManagePage.isAddEntityBtnClick())
			{
				testRst = false;
				logger.error("No right to add entity");
			}

			if (!entityManagePage.isEditEntity(Entity))
			{
				testRst = false;
				logger.error("No right to edit entity");
			}

			if (!entityManagePage.isDeleteEntityBtnClick(Entity))
			{
				testRst = false;
				logger.error("No right to delete entity");
			}
			entityManagePage.backToDashboard();

			PrivilegeGroupPage privilegeGroupPage = null;
			for (int i = 0; i < DeletePrivileges.size(); i++)
			{
				// Delete privilege add/edit/delete entity
				homePage = entityManagePage.logout();
				homePage.logon();

				privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
				privilegeGroupPage.deletePrivilege(GroupName, DeletePrivileges.get(i));

				// Verfy if user [UserName] could add/edit/delete entity
				listPage = privilegeGroupPage.backToDashboard();
				listPage.logout();
				listPage = homePage.loginAs(UserName, Password);

				switch (i)
				{
					case 0:
					{
						entityManagePage = listPage.EnterEntityPage();
						if (entityManagePage.isAddEntityBtnClick())
						{
							testRst = false;
							logger.error("Should no right to add entity");
						}
						break;
					}
					case 1:
					{
						try
						{
							entityManagePage = listPage.EnterEntityPage();
							if (entityManagePage.isDeleteEntityBtnClick(Entity))
							{
								testRst = false;
								logger.error("Should no right to delete entity");
							}
						}
						catch (Exception e)
						{

						}
						break;
					}

					case 2:
					{
						try
						{
							entityManagePage = listPage.EnterEntityPage();
							if (entityManagePage.isEditEntity(Entity))
							{
								testRst = false;
								logger.error("Should no right to edit entity");
							}
						}
						catch (Exception e)
						{

						}

						break;
					}

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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4685() throws Exception
	{
		String caseID = "4685";
		logger.info("====Verify Permission Group with Adjustments privilege[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_admin, nodeName);
			String GroupName = testdata.get(0);
			String UserName = testdata.get(1);
			String Password = testdata.get(2);
			String Entity = testdata.get(3);
			// String Return = testdata.get(4);
			// String ProcessDate = testdata.get(5);
			String DeletePrivilege = testdata.get(6);

			// check user could import adjustment
			ListPage listPage = super.loginAsOtherUser(UserName, Password);
			listPage.setGroup(Entity);

			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			if (!formInstancePage.isImportAdjustmentEnabled())
			{
				testRst = false;
				logger.error("ImportAdjustment button should be enabled");
			}

			formInstancePage.closeFormInstance();

			// delete privilege
			HomePage homePage = listPage.logout();
			listPage = homePage.logon();
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.deletePrivilege(GroupName, DeletePrivilege.split("#"));

			// check user could import adjustment
			listPage = privilegeGroupPage.backToDashboard();
			listPage.logout();
			listPage = homePage.loginAs(UserName, Password);
			listPage.setGroup(Entity);
			formInstancePage = listPage.openFirstFormInstance();
			if (formInstancePage.isImportAdjustmentEnabled())
			{
				testRst = false;
				logger.error("ImportAdjustment button should not be displayed ");
			}
			formInstancePage.closeFormInstance();
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4689() throws Exception
	{
		String caseID = "4689";
		logger.info("====Verify Permission Group with Export to File privilege[case id=" + caseID + "]====");
		boolean testRst = false;

		try
		{
			String nodeName = "C" + caseID;
			String UserName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String Password = getElementValueFromXML(testData_admin, nodeName, "Password");
			String GroupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");
			String ImportFileName = getElementValueFromXML(testData_admin, nodeName, "ImportFile");
			String DeletePrivilege = getElementValueFromXML(testData_admin, nodeName, "DeletePrivilege");

			ListPage listPage = super.m.listPage;

			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.deletePrivilege(GroupName, DeletePrivilege.split("#"));

			listPage = privilegeGroupPage.backToDashboard();
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);

			listPage.setGroup(Entity);
			File importFile = new File(testData_admin.replace("Admin.xml", ImportFileName));
			String msg = listPage.getImportAdjustmentErrorMsg(importFile, false);
			if (!msg.equalsIgnoreCase("You do not have right to write this return."))
			{
				testRst = true;
			}
			else
				logger.error("Should not have right to import adjustment");

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4688() throws Exception
	{
		String caseID = "4688";
		logger.info("====Verify Permission Group with Return privilege[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String UserName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String Password = getElementValueFromXML(testData_admin, nodeName, "Password");
			String GroupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");
			String Return = getElementValueFromXML(testData_admin, nodeName, "Return");
			String ProcessDate = getElementValueFromXML(testData_admin, nodeName, "ProcessDate");
			String DeletePrivilege = getElementValueFromXML(testData_admin, nodeName, "DeletePrivilege");
			String CellName = getElementValueFromXML(testData_admin, nodeName, "CellName");

			String formCode = splitReturn(Return).get(0);
			String version = splitReturn(Return).get(1);

			ListPage listPage = super.m.listPage;

			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.deletePrivilege(GroupName, DeletePrivilege.split("#"));

			logger.info("Log out then log on as " + UserName);
			listPage = privilegeGroupPage.backToDashboard();
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);
			listPage.setGroup(Entity);

			if (listPage.getCreateNewFormErrorMsg(Entity, null, Return, null, false, false).equals("You do not have right to create this return."))
			{
				testRst = false;
				logger.error("Create new button should not be displayed");
			}

			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			if (formInstancePage.isLockDisplayed())
			{
				testRst = false;
				logger.error("lock/unlock button should be disabled");
			}

			if (formInstancePage.isEnableEditeCell(CellName))
			{
				testRst = false;
				logger.error("Normal cell should not be editable");
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4686() throws Exception
	{
		String caseID = "4686";
		logger.info("====Verify Permission Group with Import from File privilege[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_admin, nodeName);
			String GroupName = testdata.get(0);
			String UserName = testdata.get(1);
			String Password = testdata.get(2);
			String Entity = testdata.get(3);
			// String Return = testdata.get(4);
			// String ProcessDate = testdata.get(5);
			String DeletePrivilege = testdata.get(6);

			ListPage listPage = super.loginAsOtherUser(UserName, Password);

			listPage.setGroup(Entity);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			if (!formInstancePage.isExportToFileEnabled())
			{
				testRst = false;
				logger.error("Export to excel/csv should be enabled");
			}
			formInstancePage.closeFormInstance();

			HomePage homePage = listPage.logout();
			listPage = homePage.logon();

			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.deletePrivilege(GroupName, DeletePrivilege.split("#"));

			listPage = privilegeGroupPage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);

			formInstancePage = listPage.openFirstFormInstance();
			if (formInstancePage.isExportToFileEnabled())
			{
				testRst = false;
				logger.error("Export to excel/csv should not be enabled");
			}
			formInstancePage.closeFormInstance();

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4690() throws Exception
	{
		String caseID = "4690";
		logger.info("====Verify Permission Group with Instance privilege[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String UserName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String Password = getElementValueFromXML(testData_admin, nodeName, "Password");
			String GroupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");
			String Return = getElementValueFromXML(testData_admin, nodeName, "Return");
			String ProcessDate = getElementValueFromXML(testData_admin, nodeName, "ProcessDate");
			String DeletePrivilege = getElementValueFromXML(testData_admin, nodeName, "DeletePrivilege");
			String formCode = splitReturn(Return).get(0);
			String version = splitReturn(Return).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);

			listPage.setGroup(Entity);
			listPage.setForm(Return);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			if (!formInstancePage.isAddInstanceBtnDisplayed())
			{
				testRst = false;
				logger.error("Add instance button should be displayed");
			}

			if (!formInstancePage.isDeleteInstanceBtnDisplayed())
			{
				testRst = false;
				logger.error("Delete instance button should be displayed");
			}

			formInstancePage.closeFormInstance();

			homePage = listPage.logout();
			listPage = homePage.logon();

			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.deletePrivilege(GroupName, DeletePrivilege.split("#"));

			listPage = privilegeGroupPage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);

			listPage.setGroup(Entity);
			listPage.setForm(Return);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			if (formInstancePage.isAddInstanceBtnDisplayed())
			{
				testRst = false;
				logger.error("Add instance button should not be displayed");
			}

			if (formInstancePage.isDeleteInstanceBtnDisplayed())
			{
				testRst = false;
				logger.error("Delete instance button should not be displayed");
			}

			formInstancePage.closeFormInstance();

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4691() throws Exception
	{
		String caseID = "4691";
		logger.info("====Verify Permission Group with Approver privilege[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String UserName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String Password = getElementValueFromXML(testData_admin, nodeName, "Password");
			String GroupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");
			String Return = getElementValueFromXML(testData_admin, nodeName, "Return");
			String ProcessDate = getElementValueFromXML(testData_admin, nodeName, "ProcessDate");
			String DeletePrivilege = getElementValueFromXML(testData_admin, nodeName, "DeletePrivilege");
			String formCode = splitReturn(Return).get(0);
			String version = splitReturn(Return).get(1);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);

			listPage.setGroup(Entity);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			if (!formInstancePage.isApproveRejectDisplayed())
			{
				testRst = false;
				logger.error("Approve/rejected menue should be displayed");
			}

			formInstancePage.closeFormInstance();

			homePage = listPage.logout();
			listPage = homePage.logon();

			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.deletePrivilege(GroupName, DeletePrivilege.split("#"));

			listPage = privilegeGroupPage.backToDashboard();
			homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);

			listPage.setGroup(Entity);
			formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
			if (formInstancePage.isApproveRejectDisplayed())
			{
				testRst = false;
				logger.error("Approve/rejected menue should not be displayed");
			}
			formInstancePage.closeFormInstance();

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4692() throws Exception
	{
		String caseID = "4692";
		logger.info("====Verify Permission Group without any privilege[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String UserName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String Password = getElementValueFromXML(testData_admin, nodeName, "Password");
			String Entity = getElementValueFromXML(testData_admin, nodeName, "Entity");
			String Return = getElementValueFromXML(testData_admin, nodeName, "Return");
			String ImportFileName = getElementValueFromXML(testData_admin, nodeName, "ImportFile");

			File importFile = new File(testData_admin.replace("Admin.xml", ImportFileName));
			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs(UserName, Password);
			listPage.setGroup(Entity);

			if (listPage.getCreateNewFormErrorMsg(Entity, null, Return, null, false, false).equals("You do not have right to create this return."))
			{
				testRst = false;
				logger.error("Create new button should not be displayed");
			}

			if (listPage.getCreateFromExcelErrorMsg(importFile, false).equals("You do not have right to create this return."))
			{
				testRst = false;
				logger.error("Create new button should not be displayed");
			}

			String msg = listPage.getImportAdjustmentErrorMsg(importFile, false);
			if (!msg.equalsIgnoreCase("You do not have right to write this return."))
			{
				testRst = true;
			}
			else
				logger.error("Should not have right to import adjustment");

			if (listPage.isHaveAdminPrivilege())
			{
				testRst = false;
				logger.error("Should not have admin right");
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test5097() throws Exception
	{
		String caseID = "5097";
		logger.info("====Verify Entity-Return mapping is saved after entity code has been changed[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String Entity = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String Product = getElementValueFromXML(testData_admin, nodeName, "Product");
			String NewName = getElementValueFromXML(testData_admin, nodeName, "NewName");
			String Return = getElementValueFromXML(testData_admin, nodeName, "Return");

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			entityManagePage.addRootEntity(Entity, Entity, Entity, true);
			entityManagePage.assignReturnToEntity(Entity, Product, Return);
			entityManagePage.editEntity(Entity, NewName, NewName, "");
			if (!entityManagePage.getAssignedReturns(NewName, Product).contains(Return))
			{
				testRst = false;
				logger.error("Entity-Return mapping is changed after entity code has been changed");
			}
			entityManagePage.backToDashboard();
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test5146() throws Exception
	{
		String caseID = "5146";
		logger.info("====Verify Privilege Group with different type will be assigned different privileges[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String PG_User = getElementValueFromXML(testData_admin, nodeName, "PG_User");
			String PG_Entity = getElementValueFromXML(testData_admin, nodeName, "PG_Entity");
			String File = getElementValueFromXML(testData_admin, nodeName, "File");
			File privilgeFile = new File(testData_admin.replace("Admin.xml", File));

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.addPrivilegeGroup(PG_User, PG_User, "General User Privileges");
			List<String> privileges = privilegeGroupPage.getAllPrivileges(PG_User);
			privilegeGroupPage.deletePrivilegeGroup(PG_User);

			int amt = ExcelUtil.getRowNums(privilgeFile, "userPrivilege");
			if (amt != privileges.size())
				testRst = false;
			else
			{
				for (int i = 1; i <= amt; i++)
				{
					ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(privilgeFile, "userPrivilege", i);
					if (!privileges.contains(rowValueList.get(0)))
					{
						testRst = false;
						break;
					}
				}
			}

			privilegeGroupPage.addPrivilegeGroup(PG_Entity, PG_Entity, "Entity Related Privileges");
			privileges = privilegeGroupPage.getAllPrivileges(PG_Entity);
			privilegeGroupPage.deletePrivilegeGroup(PG_Entity);

			amt = ExcelUtil.getRowNums(privilgeFile, "entityPrivilege");
			if (amt != privileges.size())
				testRst = false;
			else
			{
				for (int i = 1; i <= amt; i++)
				{
					ArrayList<String> rowValueList = ExcelUtil.getRowValueFromExcel(privilgeFile, "entityPrivilege", i);
					if (!privileges.contains(rowValueList.get(0)))
					{
						testRst = false;
						break;
					}
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4556() throws Exception
	{
		String caseID = "4556";
		logger.info("====Verify the UI of add Permission Group is same as design[case id=" + caseID + "]====");
		boolean testRst = false;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_admin, nodeName);
			String privilegeGroup = testdata.get(0);

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			if (privilegeGroupPage.isAddPrievegeUICorrect(privilegeGroup) && privilegeGroupPage.isAddPrivilegeGroupUICorrect())
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4696() throws Exception
	{
		String caseID = "4696";
		logger.info("====Verify Custom Permission Group with Group Structure privilege works well[case id=" + caseID + "]====");
		boolean testRst = false;

		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_admin, nodeName);
			String GroupName = testdata.get(0);
			String GroupDesc = testdata.get(1);
			String GroupType = testdata.get(2);
			String Privilegs = testdata.get(3);
			String User = testdata.get(4);
			String Entity = testdata.get(5);

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.addPrivilegeGroup(GroupName, GroupDesc, GroupType);
			privilegeGroupPage.addPrivileges(GroupName, Privilegs.split("#"));

			listPage = privilegeGroupPage.backToDashboard();

			UsersPage usersPage = listPage.EnterUserPage();
			usersPage.addPrivilegeGroup(User, GroupName);
			listPage = usersPage.backToDashboard();

			EntityPage entityManagePage = listPage.EnterEntityPage();
			boolean s1 = false;
			boolean s2 = false;
			if (entityManagePage.isAddEntityBtnClick() && entityManagePage.isDeleteEntityBtnClick(Entity) && entityManagePage.isEditEntity(Entity))
				s1 = true;

			listPage = entityManagePage.backToDashboard();

			privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			privilegeGroupPage.deletePrivilege(GroupName, Privilegs.split("#"));
			listPage = privilegeGroupPage.backToDashboard();
			entityManagePage = listPage.EnterEntityPage();
			if (!entityManagePage.isAddEntityBtnClick() && !entityManagePage.isDeleteEntityBtnClick(Entity) && !entityManagePage.isEditEntity(Entity))
				s1 = true;

			if (s1 && s2)
				testRst = true;

			entityManagePage.backToDashboard();

		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

	@Test
	public void test4655() throws Exception
	{
		String caseID = "4655";
		logger.info("====Verify user without any privilege[case id=" + caseID + "]====");
		boolean testRst = false;

		try
		{
			ListPage listPage = super.m.listPage;

			UsersPage usersPage = listPage.EnterUserPage();
			usersPage.addUser("test4", "test3@2.com", "password", "password");
			listPage = usersPage.backToDashboard();
			HomePage homePage = listPage.logout();
			listPage = homePage.loginAs("test4", "password");
			if (!listPage.isHaveAdminPrivilege() && !listPage.isImportAdjustmentDisplayed() && !listPage.isCreateNewDisplayed())
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
			writeTestResultToFile(caseID, testRst, "Admin_PG");
		}
	}

}
