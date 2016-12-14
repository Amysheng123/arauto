package com.lombardrisk.testCase;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.pages.EntityPage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.pages.PhysicalLocationPage;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;

public class Admin_NetworkFileLocation extends TestTemplate
{

	@Test
	public void testLocationWithWindowsFormat() throws Exception
	{
		String caseID = "5872";
		logger.info("====Test add network file location with windows format[case id=" + caseID + "]====");
		boolean testRst = true;

		/**
		 * 1. Pre-condition: Install AgileREPORTER 1.14 successfully 2. Login
		 * with valid username and password. 3. In dashboard, click
		 * Settings->Network File Location Setup. 4. Click Add button 5. Select
		 * Type:Submission Files 6. Select Entity:0001 7. Select Network
		 * Location:C:\DSLOCATION 8. User path:T:\\ 9. Click Add button and
		 * check that the location using windows format can be set properly.
		 * Verify a record display in Network File Location setup screen. -EDIT
		 * edit icon and remove icon -ENTITY 0001 -NETWORK LOCATION
		 * C:\DSLOCATION -TYPE Submission Files -User path:T:\\ Data saved in
		 * table USR_PHYSICAL_LOCATION
		 */
		PhysicalLocationPage physicalLocationPage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_admin, nodeName);
			String networkLocation = elementValues.get(0);
			String userPath = elementValues.get(1);
			String entityName = elementValues.get(2);
			String type = elementValues.get(3);
			String count = elementValues.get(4);
			String entity1 = elementValues.get(5);
			String entity2 = elementValues.get(6);

			EntityPage entityPage = listPage.EnterEntityPage();
			entityPage.addRootEntity(entity1, entity1, entity1, true);
			entityPage.addRootEntity(entity2, entity2, entity2, true);
			listPage = entityPage.backToDashboard();

			physicalLocationPage = listPage.enterPhysicalLoaction();
			physicalLocationPage.addFileLocation(entityName, networkLocation, userPath);
			Assert.assertNotNull(physicalLocationPage.getDeleteButton(entityName));
			Assert.assertNotNull(physicalLocationPage.getEditButton(entityName));
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[0], entityName);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[1], networkLocation);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[2], userPath);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[3], type);

			String sql = "SELECT COUNT(ID) FROM USR_PHYSICAL_LOCATION WHERE NAME = (SELECT ID FROM USR_NATIVE_ENTITY WHERE ENTITY_NAME = '" + entityName + "')";
			String number = DBQuery.queryRecord(sql);
			Assert.assertEquals(number, count);

			// Delete the exist location.
			physicalLocationPage.deleteExistLocation(entityName);
			Assert.assertFalse(physicalLocationPage.isDeleteButtonExist(entityName));
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			physicalLocationPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "NetworkFileLocation");
		}
	}

	@Test
	public void testLocationWithLinuxFormat() throws Exception
	{
		String caseID = "5900";
		logger.info("====Test add network file location with linux format[case id=" + caseID + "]====");
		boolean testRst = true;

		/**
		 * 1. Pre-condition: Install AgileREPORTER 1.14 successfully 2. Login
		 * with valid username and password. 3. In dashboard, click
		 * Settings->Network File Location Setup. 4. Click Add button 5. Select
		 * Type:Submission Files 6. Select Entity:0001 7. Select Network
		 * Location:opt\test\DSLOCATION 8. User path:T:\\ 9. Click Add button
		 * and check that the location using Linux format can be set properly.
		 * Click Add button and check that the location using Linux format can
		 * be set properly. -EDIT edit icon and remove icon -ENTITY 0001
		 * -NETWORK LOCATION opt\test\DSLOCATION -TYPE Submission Files -User
		 * path:T:\\ Data saved in table USR_PHYSICAL_LOCATION
		 */
		PhysicalLocationPage physicalLocationPage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			physicalLocationPage = listPage.enterPhysicalLoaction();

			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_admin, nodeName);
			String networkLocation = elementValues.get(0);
			String userPath = elementValues.get(1);
			String entityName = elementValues.get(2);
			String type = elementValues.get(3);
			String count = elementValues.get(4);

			physicalLocationPage.addFileLocation(entityName, networkLocation, userPath);
			Assert.assertNotNull(physicalLocationPage.getDeleteButton(entityName));
			Assert.assertNotNull(physicalLocationPage.getEditButton(entityName));
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[0], entityName);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[1], networkLocation);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[2], userPath);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[3], type);

			String sql = "SELECT COUNT(ID) FROM USR_PHYSICAL_LOCATION WHERE NAME = (SELECT ID FROM USR_NATIVE_ENTITY WHERE ENTITY_NAME = '" + entityName + "')";
			String number = DBQuery.queryRecord(sql);
			Assert.assertEquals(number, count);

			// Delete the exist location.
			physicalLocationPage.deleteExistLocation(entityName);
			Assert.assertFalse(physicalLocationPage.isDeleteButtonExist(entityName));

		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			physicalLocationPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "NetworkFileLocation");
		}
	}

	@Test
	public void testEditLocation() throws Exception
	{
		String caseID = "5874";
		logger.info("====Test edit network file location[case id=" + caseID + "]====");
		boolean testRst = true;

		/**
		 * 1. Pre-condition: Install AgileREPORTER 1.14 successfully 2. Login
		 * with valid username and password. 3. In dashboard, click
		 * Settings->Network File Location Setup. 4. Change Select Entity from
		 * 0001 to 2999 5. Change Select Network Location from C:\DSLOCATION to
		 * D:\\user\DSLOCATION 6. Click Save button and check that the changes
		 * can be edited properly. The location can be edited properly, Data
		 * saved in table USR_PHYSICAL_LOCATION
		 */
		PhysicalLocationPage physicalLocationPage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			physicalLocationPage = listPage.enterPhysicalLoaction();

			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_admin, nodeName);
			String initNetworkLocation = elementValues.get(0);
			String initUserPath = elementValues.get(1);
			String initEntityName = elementValues.get(2);
			String type = elementValues.get(3);
			String initCount = elementValues.get(4);
			String modifiedNetworkLocation = elementValues.get(5);
			String modifiedUserPath = elementValues.get(6);
			String modifiedEntityName = elementValues.get(7);

			physicalLocationPage.addFileLocation(initEntityName, initNetworkLocation, initUserPath);
			Assert.assertNotNull(physicalLocationPage.getDeleteButton(initEntityName));
			Assert.assertNotNull(physicalLocationPage.getEditButton(initEntityName));
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[0], initEntityName);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[1], initNetworkLocation);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[2], initUserPath);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[3], type);

			String sql = "SELECT COUNT(ID) FROM USR_PHYSICAL_LOCATION WHERE NAME = (SELECT ID FROM USR_NATIVE_ENTITY WHERE ENTITY_NAME = '" + initEntityName + "')";
			String number = DBQuery.queryRecord(sql);
			Assert.assertEquals(number, initCount);

			// Edit the exist location.
			physicalLocationPage = physicalLocationPage.editExistLocation(initEntityName, modifiedEntityName, modifiedNetworkLocation, modifiedUserPath);
			Assert.assertNotNull(physicalLocationPage.getDeleteButton(modifiedEntityName));
			Assert.assertNotNull(physicalLocationPage.getEditButton(modifiedEntityName));
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[0], modifiedEntityName);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[1], modifiedNetworkLocation);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[2], modifiedUserPath);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[3], type);

			String sqlEdit = "SELECT COUNT(ID) FROM USR_PHYSICAL_LOCATION WHERE NAME = (SELECT ID FROM USR_NATIVE_ENTITY WHERE ENTITY_NAME = '" + modifiedEntityName + "')";
			String numberEdit = DBQuery.queryRecord(sqlEdit);
			Assert.assertEquals(numberEdit, initCount);

			// Delete the exist location.
			physicalLocationPage.deleteExistLocation(modifiedEntityName);
			Assert.assertFalse(physicalLocationPage.isDeleteButtonExist(modifiedEntityName));

		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			physicalLocationPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "NetworkFileLocation");
		}
	}

	@Test
	public void testDeleteLocation() throws Exception
	{
		String caseID = "5875";
		logger.info("====Test delete network file location[case id=" + caseID + "]====");
		boolean testRst = true;

		/**
		 * 1. Pre-condition: Install AgileREPORTER 1.14 successfully 2. Login
		 * with valid username and password. 3. In dashboard, click
		 * Settings->Network File Location Setup. 4. Click Delete button and
		 * check that the location can be deleted properly. 5. The location can
		 * be deleted properly.Data saved in table USR_PHYSICAL_LOCATION
		 */
		PhysicalLocationPage physicalLocationPage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			physicalLocationPage = listPage.enterPhysicalLoaction();

			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_admin, nodeName);
			String initNetworkLocation = elementValues.get(0);
			String initUserPath = elementValues.get(1);
			String initEntityName = elementValues.get(2);
			String type = elementValues.get(3);
			String Count = elementValues.get(4);

			physicalLocationPage.addFileLocation(initEntityName, initNetworkLocation, initUserPath);
			Assert.assertNotNull(physicalLocationPage.getDeleteButton(initEntityName));
			Assert.assertNotNull(physicalLocationPage.getEditButton(initEntityName));
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[0], initEntityName);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[1], initNetworkLocation);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[2], initUserPath);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[3], type);

			// Delete the exist location.
			physicalLocationPage.deleteExistLocation(initEntityName);
			Assert.assertFalse(physicalLocationPage.isDeleteButtonExist(initEntityName));
			String sql = "SELECT COUNT(ID) FROM USR_PHYSICAL_LOCATION WHERE NAME = (SELECT ID FROM USR_NATIVE_ENTITY WHERE ENTITY_NAME = '" + initEntityName + "')";
			String number = DBQuery.queryRecord(sql);
			Assert.assertEquals(number, Count);
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			physicalLocationPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "NetworkFileLocation");
		}
	}

	@Test
	public void testAddDuplicatedLocation() throws Exception
	{
		String caseID = "5954";
		logger.info("====Test add duplicated network file location[case id=" + caseID + "]====");
		boolean testRst = true;

		/**
		 * 1. Pre-condition: Install AgileREPORTER 1.14 successfully 2. Login
		 * with valid username and password. 3. In dashboard, click
		 * Settings->Network File Location Setup. 4. Click Add button 5. Select
		 * Type:Submission Select Entity:0001 Select Network
		 * Location:C:\DSLOCATION 6. Click OK button. Verify this location can
		 * be created successfully. 7. Click Add button again. 8. Select
		 * Type:Submission Select Entity:0001 Select Network Location:C:\TEST 9.
		 * Click OK Verify this location can not be created due to submission
		 * type has existed already in entity 0001
		 */
		PhysicalLocationPage physicalLocationPage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			physicalLocationPage = listPage.enterPhysicalLoaction();

			String nodeName = "C" + caseID;
			List<String> elementValues = getElementValueFromXML(testData_admin, nodeName);
			String initNetworkLocation = elementValues.get(0);
			String initUserPath = elementValues.get(1);
			String initEntityName = elementValues.get(2);
			String type = elementValues.get(3);
			String secondNetworkLocation = elementValues.get(4);
			String message = elementValues.get(5);

			physicalLocationPage.addFileLocation(initEntityName, initNetworkLocation, initUserPath);
			Assert.assertNotNull(physicalLocationPage.getDeleteButton(initEntityName));
			Assert.assertNotNull(physicalLocationPage.getEditButton(initEntityName));
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[0], initEntityName);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[1], initNetworkLocation);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[2], initUserPath);
			Assert.assertEquals(physicalLocationPage.getColumnValueOfPhysicalLocation()[3], type);

			// Add the location with same entity
			physicalLocationPage.addFileLocation(initEntityName, secondNetworkLocation, initUserPath);
			Assert.assertEquals(physicalLocationPage.getMessage(), message);

			// Delete the exist location.
			physicalLocationPage.deleteExistLocation(initEntityName);
			Assert.assertFalse(physicalLocationPage.isDeleteButtonExist(initEntityName));
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
		}
		finally
		{
			physicalLocationPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "NetworkFileLocation");
		}
	}

}