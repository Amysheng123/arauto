package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Create by Leo Tu on Dec 1
 */

public class PrivilegeGroupPage extends AbstractPage
{

	public PrivilegeGroupPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * add privilege group
	 * 
	 * @param name
	 * @param description
	 * @param typeName
	 * @return if add group successed
	 * @throws Exception
	 */
	public boolean addPrivilegeGroup(String name, String description, String typeName) throws Exception
	{
		logger.info("Begin add a new privilege group[" + name + "]");
		boolean addRst = true;
		clickAddGroupBtn();
		element("pp.GPN").type(name);
		element("pp.GPD").type(description);
		selectGroupType(typeName);
		element("pp.ADS").click();
		long startTime = System.currentTimeMillis();
		long CurrentTime = System.currentTimeMillis();
		while ((CurrentTime - startTime) / 1000 < 6)
		{
			if (element("pp.messageTitle").isDisplayed())
			{
				if (element("pp.messageTitle").getInnerText().equals("Error"))
				{
					addRst = false;
					logger.warn("Got error when add privilege group[" + name + "], maybe the groups already existed!");
					break;
				}
			}
			CurrentTime = System.currentTimeMillis();
		}
		return addRst;
	}

	/**
	 * edit privilege group
	 * 
	 * @param originalGroupName
	 * @param newGroupName
	 * @param newDescription
	 * @throws Exception
	 */
	public void editPrivilegeGroup(String originalGroupName, String newGroupName, String newDescription) throws Exception
	{
		clickEditPGBtn(originalGroupName);
		if (newGroupName != null)
		{
			element("pp.EGPN").input(newGroupName);
		}
		if (newDescription != null)
		{
			element("pp.EGPD").input(newDescription);
		}
		element("pp.EADS").click();
		waitStatusDlg();

	}

	/**
	 * delete privilege group
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void deletePrivilegeGroup(String name) throws Exception
	{
		logger.info("Dlete pg[" + name + "]");
		clickDeletePGBtn(name);
		waitStatusDlg();
		if (element("pp.DELGPC").isDisplayed())
		{
			element("pp.DELGPC").click();
			waitStatusDlg();
		}

	}

	/**
	 * add privilege for group
	 * 
	 * @param groupName
	 * @param privilegesName
	 * @throws Exception
	 */
	public void addPrivileges(String groupName, String... privilegesName) throws Exception
	{
		if (privilegesName.length > 0)
		{
			logger.info("Begin add privilege" + privilegesName);
			clickAddPrivilegeBtn(groupName);
			selectPrivilege(privilegesName);
		}
		else
		{
			logger.info("Privilege is empty,do not need add privilege");
		}

	}

	/**
	 * add all privileges for group
	 * 
	 * @param groupName
	 * @throws Exception
	 */
	public void addAllPrivileges(String groupName) throws Exception
	{
		clickAddPrivilegeBtn(groupName);
		selectAllPrivilege();
	}

	/**
	 * deselect privilege in privilege group
	 * 
	 * @param groupName
	 * @param privilegesName
	 * @throws Exception
	 */
	public void deletePrivilege(String groupName, String... privilegesName) throws Exception
	{
		logger.info("Begin delete privilege");
		String id = element("pp.getPG", groupName).getAttribute("id").replace("permissionListForm:CustomGroupPanel", "permissionListForm:customGroupPanelGrid").replace("_header", "");
		String[] list = new String[]
		{ id, "", "" };
		int amt = (int) element("pp.privTab", id).getRowCount();
		for (String privilege : privilegesName)
		{
			for (int r = 1; r <= amt; r++)
			{
				list[1] = String.valueOf(r);
				for (int c = 1; c <= 4; c++)
				{
					list[2] = String.valueOf(c);
					if (element("pp.getPriv", list).isDisplayed())
					{
						if (element("pp.getPriv", list).getInnerText().equals(privilege))
						{
							element("pp.delPriv", list).click();
							element("pp.DPC").click();
							waitStatusDlg();
						}
					}
					else
						break;
				}
			}
		}

	}

	/**
	 * select privilege
	 * 
	 * @param privilegesName
	 * @throws Exception
	 */
	public void selectPrivilege(String... privilegesName) throws Exception
	{
		for (String privilege : privilegesName)
		{
			element("pp.privCheckBox", privilege).check(true);
		}
		element("pp.editPrivSave").click();
		waitStatusDlg();
	}

	/**
	 * select all privileges
	 * 
	 * @throws Exception
	 */
	public void selectAllPrivilege() throws Exception
	{
		int nums = (int) element("pp.privTable").getRowCount();
		for (int i = 1; i <= nums; i++)
		{
			element("pp.privCheckBox2", String.valueOf(i)).check(true);
		}
		element("pp.editPrivSave").click();
		waitStatusDlg();
	}

	/**
	 * get all customer privilege groups
	 * 
	 * @return privilege groups(List)
	 * @throws Exception
	 */
	public List<String> getAllCustomPG() throws Exception
	{
		return element("pp.allPG").getAllInnerTexts();
	}

	/**
	 * click add privilege button
	 * 
	 * @param permissionGoupName
	 * @throws Exception
	 */
	public void clickAddPrivilegeBtn(String permissionGoupName) throws Exception
	{
		String id = element("pp.getPG", permissionGoupName).getAttribute("id").replace("permissionListForm:CustomGroupPanel", "permissionListForm:customGroupPanelGrid").replace("_header", "");
		String[] list = new String[]
		{ id, "", "" };
		int amt = (int) element("pp.privTab", id).getRowCount();
		for (int r = 1; r <= amt; r++)
		{
			list[1] = String.valueOf(r);
			for (int c = 1; c <= 4; c++)
			{
				list[2] = String.valueOf(c);
				if (element("pp.getPriv", list).getInnerText().equals("Add privilege"))
				{
					element("pp.addPriv", list).click();
					waitStatusDlg();
					r = amt + 1;
					break;
				}
			}
		}

	}

	/**
	 * click edit privilege group button
	 * 
	 * @param permissionGoupName
	 * @throws Exception
	 */
	public void clickEditPGBtn(String permissionGoupName) throws Exception
	{
		String id = element("pp.getPG", permissionGoupName).getAttribute("id");
		element("pp.editPG", id).click();
		waitStatusDlg();
	}

	/**
	 * click delete privilege group button
	 * 
	 * @param groupName
	 * @throws Exception
	 */
	public void clickDeletePGBtn(String groupName) throws Exception
	{
		String id = element("pp.getPG", groupName).getAttribute("id");
		element("pp.DELGP", id).click();
		waitStatusDlg();
	}

	/**
	 * click add privilege group button
	 * 
	 * @throws Exception
	 */
	public void clickAddGroupBtn() throws Exception
	{
		element("pp.addPG").click();
		waitStatusDlg();
	}

	/**
	 * get added privileges from PG
	 * 
	 * @param PGName
	 * @return privileges(List)
	 * @throws Exception
	 */
	public List<String> getPrivilegeByPG(String PGName) throws Exception
	{
		logger.info("Begin get assigned privilege on group " + PGName);
		List<String> existPrivileges = new ArrayList<String>();
		String id = element("pp.getPG", PGName).getAttribute("id").replace("permissionListForm:CustomGroupPanel", "permissionListForm:customGroupPanelGrid").replace("_header", "");
		String[] list = new String[]
		{ id, "", "" };
		int amt = (int) element("pp.privTab", id).getRowCount();
		try
		{
			for (int r = 1; r <= amt; r++)
			{
				list[1] = String.valueOf(r);
				for (int c = 1; c <= 4; c++)
				{
					list[2] = String.valueOf(c);
					{
						if (element("pp.getPriv", list).isDisplayed())
						{
							existPrivileges.add(element("pp.getPriv", list).getInnerText());
						}
						else
							break;
					}

				}
			}
		}
		catch (NoSuchElementException e)
		{
		}

		return existPrivileges;
	}

	/**
	 * get available privileges from 'add privilege' table
	 * 
	 * @param groupName
	 * @return all privileges(List)
	 * @throws Exception
	 */
	public List<String> getAllPrivileges(String groupName) throws Exception
	{
		List<String> existPrivileges = new ArrayList<String>();
		clickAddPrivilegeBtn(groupName);
		int amt = (int) element("pp.existPrivTab").getRowCount();
		for (int i = 1; i <= amt; i++)
		{
			existPrivileges.add(element("pp.existPriv", String.valueOf(i)).getInnerText());
		}
		element("pp.closeWindow").click();
		return existPrivileges;
	}

	/**
	 * logout
	 * 
	 * @return HomePage
	 * @throws Exception
	 */
	public HomePage logout() throws Exception
	{
		backToDashboard();
		element("pp.userMenu").click();
		element("pp.logout").click();
		return new HomePage(getWebDriverWrapper());
	}

	/**
	 * back to dashboard page
	 * 
	 * @return ListPage
	 * @throws Exception
	 */
	public ListPage backToDashboard() throws Exception
	{
		element("pp.dashboard").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * select privilege group type
	 * 
	 * @param type
	 */
	public void selectGroupType(String type) throws Exception
	{
		element("pp.GPCT").click();
		waitStatusDlg();
		if (type.equalsIgnoreCase("General User Privileges"))
			element("pp.GPT", "1").click();
		else
			element("pp.GPT", "2").click();
		waitStatusDlg();

	}

	/**
	 * if AddPrivilegeGroup UI is Correct
	 * 
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isAddPrivilegeGroupUICorrect() throws Exception
	{
		boolean rst = true;
		clickAddGroupBtn();
		if (!element("pp.GPN").isDisplayed())
			rst = false;
		if (!element("pp.GPD").isDisplayed())
			rst = false;
		if (!element("pp.ADS").isDisplayed())
			rst = false;
		if (!element("pp.cancel").isDisplayed())
			rst = false;

		element("pp.cancel").click();
		waitStatusDlg();
		return rst;
	}

	/**
	 * if AddPrivilege UI is Correct
	 * 
	 * @param privilegeGroup
	 * @return if AddPrivilege UI is Correct
	 * @throws Exception
	 */
	public boolean isAddPrievegeUICorrect(String privilegeGroup) throws Exception
	{
		boolean rst = true;
		clickAddPrivilegeBtn(privilegeGroup);
		if (!element("pp.PF").isDisplayed())
			rst = false;
		if (!element("pp.PL").isDisplayed())
			rst = false;
		if (!element("pp.editPrivSave").isDisplayed())
			rst = false;
		if (!element("pp.editPrivCancel").isDisplayed())
			rst = false;
		element("pp.editPrivCancel").click();
		waitStatusDlg();
		return rst;
	}

}
