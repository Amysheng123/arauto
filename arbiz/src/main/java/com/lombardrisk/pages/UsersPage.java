package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Create by Leo Tu on Nov 30, 2015 Refactor by Leo Tu on 2/1/16
 */

public class UsersPage extends AbstractPage
{

	public UsersPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	public void addUser(String userName, String email, String password, String confirmPassword) throws Exception
	{
		element("up.addUser").click();
		waitStatusDlg();
		element("up.userName").type(userName);
		element("up.email").type(email);
		element("up.createpwd").click();
		Thread.sleep(500);
		element("up.pwd").input(password);
		element("up.cpwd").input(confirmPassword);
		element("up.save").click();
		waitStatusDlg();
	}

	public ArrayList<String> getAllUsers() throws Exception
	{
		ArrayList<String> allUsers = new ArrayList<String>();
		int amt = (int) element("up.userTab").getRowCount();
		for (int i = 1; i <= amt; i++)
		{
			allUsers.add(element("up.userInfo", String.valueOf(i)).getInnerText());
		}
		return allUsers;
	}

	public void addPrivilegeGroup(String user, String privilegeGroup) throws Exception
	{
		enterEditPage(user);
		element("up.AddPGCheckBox", privilegeGroup).click();
		waitStatusDlg();
		element("up.addP").click();
		waitStatusDlg();
		element("up.save").click();
		waitStatusDlg();
	}

	public void deletePrivilegeGroup(String user, String privilegeGroup) throws Exception
	{
		enterEditPage(user);
		element("up.DelPGCheckBox", privilegeGroup).check(true);
		element("up.delP").click();
		waitStatusDlg();
		element("up.save").click();
		waitStatusDlg();

	}

	public void enterEditPage(String userName) throws Exception
	{
		element("up.editUser", userName).click();
		waitStatusDlg();
	}

	public List<String> getUertInfo(String userName) throws Exception
	{
		List<String> userInfo = new ArrayList<String>();
		enterEditPage(userName);

		element("up.userName").getAttribute("value");
		element("up.email").getAttribute("value");
		if (getUserStatus(userName).equals("Active"))
		{
			userInfo.add("Y");
		}
		else
		{
			userInfo.add("N");
		}

		return userInfo;
	}

	public List<String> getSelectedPG(String userName) throws Exception
	{
		return element("up.selectedPG").getAllInnerTexts();

	}

	public String changePassword(String userName, String passowrd, String confirmPassword) throws Exception
	{
		if (element("up.userName").isDisplayed())
		{
			if (!element("up.userName").getAttribute("value").equals(userName))
			{
				element("up.editBtn", userName).click();
				waitStatusDlg();
			}
		}
		else
		{
			element("up.editBtn", userName).click();
			waitStatusDlg();
		}

		if (!element("up.changePwd").getAttribute("class").contains("ui-state-active"))
		{
			element("up.changePwd").click();
			waitStatusDlg();
		}
		if (!passowrd.equals(""))
			element("up.edit_pw").input(passowrd);
		else
			element("up.edit_pw").clear();

		if (!confirmPassword.equals(""))
			element("up.edit_cpw").input(confirmPassword);
		else
			element("up.edit_cpw").clear();

		element("up.save").click();
		try
		{
			waitThat("up.errorMsg").toBePresentIn(5000);
			String msg = element("up.errorMsg").getInnerText();
			waitThat("up.errorMsg").toBeInvisible();
			return msg;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void changeUserStatus(String userName, boolean active) throws Exception
	{
		if (element("up.userName").isDisplayed())
		{
			if (!element("up.userName").getAttribute("value").equals(userName))
			{
				element("up.editBtn", userName).click();
				waitStatusDlg();
			}
		}
		else
		{
			element("up.editBtn", userName).click();
			waitStatusDlg();
		}
		if (active)
		{
			if (!element("up.active").getAttribute("class").contains("ui-state-active"))
			{
				element("up.activeCheckBox").click();
				waitStatusDlg();
			}
		}
		else
		{
			if (element("up.active").getAttribute("class").contains("ui-state-active"))
			{
				element("up.activeCheckBox").click();
				waitStatusDlg();
			}
		}
		element("up.save").click();
		waitStatusDlg();
	}

	public String getUserStatus(String userName) throws Exception
	{
		return element("up.userStatus", userName).getInnerText();
	}

	public HomePage logout() throws Exception
	{
		ListPage listPage = backToDashboard();
		listPage.logout();
		return new HomePage(getWebDriverWrapper());
	}

	public ListPage backToDashboard() throws Exception
	{
		element("up.dashboard").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}

}
