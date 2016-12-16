package com.lombardrisk.testCase;

import java.io.File;
import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;

/**
 * Create by Leo Tu on Jan 11, 2016
 */

public class Admin_ImportExport extends TestTemplate
{
	String path = null;
	String logPath = null;

	@Test
	public void test5108() throws Exception
	{
		String caseID = "5108";
		logger.info("====Verify User has Import and Export access settings privilege can do Bulk access control settings[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String userName = getElementValueFromXML(testData_admin, nodeName, "UserName");
			String password = getElementValueFromXML(testData_admin, nodeName, "Password");

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			homePage.loginAs(userName, password);
			EntityPage entityPage = listPage.EnterEntityPage();
			if (entityPage.isImportEnabled() && entityPage.isExportEnabled())
			{
				testRst = false;
				logger.error("Import/export button should be disable");
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test
	public void test5109() throws Exception
	{
		String caseID = "5109";
		logger.info("====Verify Export access settings works well for Entities[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			String path = entityManagePage.exportAcessSettings();
			if (path != null)
			{
				File exportFile = new File(path);
				int amt = ExcelUtil.getRowNums(exportFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, sheetName, i);
					List<String> entityInfo = entityManagePage.getEntityInfo(rowValueList.get(1));
					if (entityInfo.size() > 1)
					{
						for (int index = 0; index < 5; index++)
						{
							if (!rowValueList.get(index).equalsIgnoreCase(entityInfo.get(index)))
								testRst = false;
						}
					}
					else
					{
						testRst = false;
					}
				}

			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test(dependsOnMethods =
	{ "test5109" })
	public void test5115() throws Exception
	{
		String caseID = "5115";
		logger.info("====Verify Export access settings works well for Users[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");

			ListPage listPage = super.m.listPage;
			UsersPage usersPage = listPage.EnterUserPage();
			if (path != null)
			{
				File exportFile = new File(path);
				int amt = ExcelUtil.getRowNums(exportFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, sheetName, i);
					List<String> userInfo = usersPage.getUertInfo(rowValueList.get(0));
					if (userInfo.size() > 1)
					{
						for (int index = 0; index < 3; index++)
						{
							if (!rowValueList.get(index).equalsIgnoreCase(userInfo.get(index)))
								testRst = false;
						}
					}
					else
					{
						testRst = false;
					}
				}

			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test(dependsOnMethods =
	{ "test5109" })
	public void test5116() throws Exception
	{
		String caseID = "5116";
		logger.info("====Verify Export access settings works well for UserGroups[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");

			ListPage listPage = super.m.listPage;
			UserGroupPage userGroupPage = listPage.EnterUserGroupPage();
			if (path != null)
			{
				File exportFile = new File(path);
				int amt = ExcelUtil.getRowNums(exportFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, sheetName, i);
					List<String> userInfo = userGroupPage.getUGInfo(rowValueList.get(0));
					if (userInfo.size() > 1)
					{
						for (int index = 0; index < 2; index++)
						{
							if (!rowValueList.get(index).equalsIgnoreCase(userInfo.get(index)))
								testRst = false;
						}
					}
					else
					{
						testRst = false;
					}
				}

			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test(dependsOnMethods =
	{ "test5109" })
	public void test5118() throws Exception
	{
		String caseID = "5118";
		logger.info("====Verify Export access settings works well for User Group Membership[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");

			ListPage listPage = super.m.listPage;
			UserGroupPage userGroupPage = listPage.EnterUserGroupPage();
			if (path != null)
			{
				File exportFile = new File(path);
				int amt = ExcelUtil.getRowNums(exportFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, sheetName, i);
					List<String> userInfo = userGroupPage.getUsersByUG(rowValueList.get(1));
					if (userInfo.size() > 1)
					{
						if (!userInfo.contains(rowValueList.get(0)))
							testRst = false;
					}
					else
					{
						testRst = false;
					}
				}

			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test(dependsOnMethods =
	{ "test5109" })
	public void test5128() throws Exception
	{
		String caseID = "5128";
		logger.info("====Verify Export access settings works well for Privilege Group Membership[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			if (path != null)
			{
				File exportFile = new File(path);
				int amt = ExcelUtil.getRowNums(exportFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, sheetName, i);
					List<String> PrivilegeInfo = privilegeGroupPage.getPrivilegeByPG(rowValueList.get(0));
					if (PrivilegeInfo.size() > 1)
					{
						if (!PrivilegeInfo.contains(rowValueList.get(1)))
							testRst = false;
					}
					else
					{
						testRst = false;
					}
				}
			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test(dependsOnMethods =
	{ "test5109" })
	public void test5129() throws Exception
	{
		String caseID = "5129";
		logger.info("====Verify Export access settings works well for Privilege Group User Mapping[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");

			ListPage listPage = super.m.listPage;
			UsersPage usersPage = listPage.EnterUserPage();
			if (path != null)
			{
				File exportFile = new File(path);
				int amt = ExcelUtil.getRowNums(exportFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, sheetName, i);
					List<String> pg = usersPage.getSelectedPG(rowValueList.get(0));
					if (rowValueList.size() > 1)
					{
						if (!rowValueList.get(1).equalsIgnoreCase(pg.get(1)))
							testRst = false;
					}
					else
						testRst = false;

				}
			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test
	public void test5131() throws Exception
	{
		String caseID = "5131";
		logger.info("====Verify Import access settings works well for Entities[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String importFilePath = getElementValueFromXML(testData_admin, nodeName, "ImportFile");
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");
			String baseFile = getElementValueFromXML(testData_admin, nodeName, "BaseLineFile");

			ListPage listPage = super.m.listPage;
			EntityPage entityManagePage = listPage.EnterEntityPage();
			String logPath = entityManagePage.importAccessSettings(importFilePath);
			if (logPath != null)
			{
				File importFile = new File(importFilePath);
				int amt = ExcelUtil.getRowNums(importFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(importFile, sheetName, i);
					List<String> entityInfo = entityManagePage.getEntityInfo(rowValueList.get(1));
					if (entityInfo.size() > 1)
					{
						for (int index = 0; index < 5; index++)
						{
							if (!rowValueList.get(index).equalsIgnoreCase(entityInfo.get(index)))
							{
								testRst = false;
								break;
							}
						}
					}
					else
					{
						testRst = false;
						break;
					}
				}

				File exportFile = new File(logPath);
				amt = ExcelUtil.getRowNums(exportFile, null);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, null, i);
					if (rowValueList.get(1).equals("Entity"))
					{
						List<String> baseValue = ExcelUtil.getRowValueFromExcel(new File(baseFile), null, i);
						if (!rowValueList.equals(baseValue))
						{
							testRst = false;
						}
					}
					else
					{
						break;
					}
				}
			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test(dependsOnMethods =
	{ "test5131" })
	public void test5132() throws Exception
	{
		String caseID = "5132";
		logger.info("====Verify Import access settings works well for User Groups[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String importFilePath = getElementValueFromXML(testData_admin, nodeName, "ImportFile");
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");
			String baseFile = getElementValueFromXML(testData_admin, nodeName, "BaseLineFile");

			ListPage listPage = super.m.listPage;
			UserGroupPage userGroupPage = listPage.EnterUserGroupPage();

			if (logPath != null)
			{
				File importFile = new File(importFilePath);
				int amt = ExcelUtil.getRowNums(importFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(importFile, sheetName, i);
					List<String> userInfo = userGroupPage.getUGInfo(rowValueList.get(0));
					if (userInfo.size() > 1)
					{
						for (int index = 0; index < 2; index++)
						{
							if (!rowValueList.get(index).equalsIgnoreCase(userInfo.get(index)))
								testRst = false;
						}
					}
					else
					{
						testRst = false;
					}
				}

				File exportFile = new File(logPath);
				amt = ExcelUtil.getRowNums(exportFile, null);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, null, i);
					if (rowValueList.get(1).equals("UserGroup"))
					{
						List<String> baseValue = ExcelUtil.getRowValueFromExcel(new File(baseFile), null, i);
						if (!rowValueList.equals(baseValue))
						{
							testRst = false;
						}
					}
					else
					{
						break;
					}
				}
			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test(dependsOnMethods =
	{ "test5131" })
	public void test5133() throws Exception
	{
		String caseID = "5133";
		logger.info("====Verify Import access settings works well for User Group Membership[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String importFilePath = getElementValueFromXML(testData_admin, nodeName, "ImportFile");
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");

			ListPage listPage = super.m.listPage;
			UserGroupPage userGroupPage = listPage.EnterUserGroupPage();

			if (logPath != null)
			{
				File importFile = new File(importFilePath);
				int amt = ExcelUtil.getRowNums(importFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(importFile, sheetName, i);
					List<String> userInfo = userGroupPage.getUGInfo(rowValueList.get(0));
					if (userInfo.size() > 1)
					{
						for (int index = 0; index < 2; index++)
						{
							if (!rowValueList.get(index).equalsIgnoreCase(userInfo.get(index)))
								testRst = false;
						}
					}
					else
					{
						testRst = false;
					}
				}
			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test
	public void test5134() throws Exception
	{
		String caseID = "5134";
		logger.info("====Verify Import access settings works well for Privilege Groups[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String importFilePath = getElementValueFromXML(testData_admin, nodeName, "ImportFile");
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");
			String baseFile = getElementValueFromXML(testData_admin, nodeName, "BaseLineFile");

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			if (logPath != null)
			{
				File importFile = new File(importFilePath);
				int amt = ExcelUtil.getRowNums(importFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(importFile, sheetName, i);
					List<String> PrivilegeInfo = privilegeGroupPage.getPrivilegeByPG(rowValueList.get(0));
					if (PrivilegeInfo.size() > 1)
					{
						if (!PrivilegeInfo.contains(rowValueList.get(1)))
							testRst = false;
					}
					else
					{
						testRst = false;
					}
				}

				File exportFile = new File(logPath);
				amt = ExcelUtil.getRowNums(exportFile, null);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, null, i);
					if (rowValueList.get(1).equals("PrivilegeGroup"))
					{
						List<String> baseValue = ExcelUtil.getRowValueFromExcel(new File(baseFile), null, i);
						if (!rowValueList.equals(baseValue))
						{
							testRst = false;
						}
					}
					else
					{
						break;
					}
				}
			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test(dependsOnMethods =
	{ "test5131" })
	public void test5135() throws Exception
	{
		String caseID = "5135";
		logger.info("====Verify Import access settings works well for Privilege Group Membership[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String importFilePath = getElementValueFromXML(testData_admin, nodeName, "ImportFile");
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();

			if (logPath != null)
			{
				File importFile = new File(importFilePath);
				int amt = ExcelUtil.getRowNums(importFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(importFile, sheetName, i);
					List<String> PrivilegeInfo = privilegeGroupPage.getPrivilegeByPG(rowValueList.get(0));
					if (PrivilegeInfo.size() > 1)
					{
						if (!PrivilegeInfo.contains(rowValueList.get(1)))
							testRst = false;
					}
					else
					{
						testRst = false;
					}
				}
			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

	@Test
	public void test5136() throws Exception
	{
		String caseID = "5136";
		logger.info("====Verify Import access settings works well for Privilege Group User Mapping[case id=" + caseID + "]====");
		boolean testRst = true;

		try
		{
			String nodeName = "C" + caseID;
			String importFilePath = getElementValueFromXML(testData_admin, nodeName, "ImportFile");
			String sheetName = getElementValueFromXML(testData_admin, nodeName, "SheetName");
			String baseFile = getElementValueFromXML(testData_admin, nodeName, "BaseLineFile");

			ListPage listPage = super.m.listPage;
			PrivilegeGroupPage privilegeGroupPage = listPage.EnterPrivilegeGroupsPage();
			if (logPath != null)
			{
				File importFile = new File(importFilePath);
				int amt = ExcelUtil.getRowNums(importFile, sheetName);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(importFile, sheetName, i);
					List<String> PrivilegeInfo = privilegeGroupPage.getPrivilegeByPG(rowValueList.get(0));
					if (PrivilegeInfo.size() > 1)
					{
						if (!PrivilegeInfo.contains(rowValueList.get(1)))
							testRst = false;
					}
					else
					{
						testRst = false;
					}
				}

				File exportFile = new File(logPath);
				amt = ExcelUtil.getRowNums(exportFile, null);
				for (int i = 1; i <= amt; i++)
				{
					List<String> rowValueList = ExcelUtil.getRowValueFromExcel(exportFile, null, i);
					if (rowValueList.get(1).equals("PrivilegeGroupUserMapping"))
					{
						List<String> baseValue = ExcelUtil.getRowValueFromExcel(new File(baseFile), null, i);
						if (!rowValueList.equals(baseValue))
						{
							testRst = false;
						}
					}
					else
					{
						break;
					}
				}
			}
			else
			{
				testRst = false;
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
			writeTestResultToFile(caseID, testRst, "Admin");
		}
	}

}
