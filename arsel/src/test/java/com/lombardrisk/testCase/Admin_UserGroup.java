package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.lombardrisk.pages.ListPage;
import com.lombardrisk.pages.UserGroupPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on Dec 14, 2015
 */

public class Admin_UserGroup extends TestTemplate
{

	@Test
	public void test4630() throws Exception
	{
		String caseID = "4630";
		logger.info("Test add user group[case id=" + caseID + "]");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String groupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String description = getElementValueFromXML(testData_admin, nodeName, "Desc");
			ListPage listPage = super.m.listPage;

			UserGroupPage userGroupManagePage = listPage.EnterUserGroupPage();
			userGroupManagePage.addGroup(groupName, description);
			assertThat(userGroupManagePage.getAllUserGroups()).contains(groupName);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_UG");
		}

	}

	@Test
	public void test4635() throws Exception
	{
		String caseID = "4635";
		logger.info("Test edit user group[case id=" + caseID + "]");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String groupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String newName = getElementValueFromXML(testData_admin, nodeName, "NewName");
			String newDesc = getElementValueFromXML(testData_admin, nodeName, "NewDesc");

			ListPage listPage = super.m.listPage;

			UserGroupPage userGroupManagePage = listPage.EnterUserGroupPage();
			userGroupManagePage.editGroup(groupName, newName, newDesc, false);
			assertThat(userGroupManagePage.getAllUserGroups()).contains(groupName);
			userGroupManagePage.editGroup(groupName, newName, newDesc, true);
			assertThat(userGroupManagePage.getAllUserGroups()).contains(newName);
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_UG");
		}

	}

	@Test
	public void test4636() throws Exception
	{
		String caseID = "4636";
		logger.info("Test assign user to group[case id=" + caseID + "]");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String groupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String userName = getElementValueFromXML(testData_admin, nodeName, "UserName");

			ListPage listPage = super.m.listPage;

			UserGroupPage userGroupManagePage = listPage.EnterUserGroupPage();
			userGroupManagePage.assignUserToGroup(groupName, userName);
			assertThat(userGroupManagePage.getUsersByUG(groupName)).contains(userName);
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_UG");
		}
	}

	@Test
	public void test4647() throws Exception
	{
		String caseID = "4647";
		logger.info("Test assign user to different group[case id=" + caseID + "]");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String groupNames = getElementValueFromXML(testData_admin, nodeName, "GroupNames");
			String userName = getElementValueFromXML(testData_admin, nodeName, "UserName");

			ListPage listPage = super.m.listPage;

			UserGroupPage userGroupManagePage = listPage.EnterUserGroupPage();
			for (String groupName : groupNames.split("#"))
			{
				userGroupManagePage.assignUserToGroup(groupName, userName);
				assertThat(userGroupManagePage.getUsersByUG(groupName)).contains(userName);
			}
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_UG");
		}

	}

	@Test
	public void test4643() throws Exception
	{
		String caseID = "4643";
		logger.info("Test delete user from group[case id=" + caseID + "]");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String groupName = getElementValueFromXML(testData_admin, nodeName, "GroupName");
			String userName = getElementValueFromXML(testData_admin, nodeName, "UserName");

			ListPage listPage = super.m.listPage;

			UserGroupPage userGroupManagePage = listPage.EnterUserGroupPage();
			userGroupManagePage.delUserFromGroup(groupName, userName);
			assertThat(userName).isNotIn(userGroupManagePage.getUsersByUG(groupName));

			testRst = true;
			userGroupManagePage.assignUserToGroup(groupName, userName);

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_UG");
		}

	}

}
