package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.lombardrisk.pages.HomePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.pages.UsersPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 12/1/2015
 */
public class Admin_User extends TestTemplate
{

	@Test
	public void test4631() throws Exception
	{
		boolean testRst = false;
		String caseID = "4631";
		logger.info("====Verify create user can work[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String UserName = getElementValueFromXML(testdata_admin, nodeName, "UserName");
			String Email = getElementValueFromXML(testdata_admin, nodeName, "Email");
			String Password = getElementValueFromXML(testdata_admin, nodeName, "Password");
			String ConfimPassword = getElementValueFromXML(testdata_admin, nodeName, "ConfimPassword");

			ListPage listPage = super.m.listPage;
			UsersPage usersPage = listPage.EnterUserPage();
			usersPage.addUser(UserName, Email, Password, ConfimPassword);
			HomePage homePage = usersPage.logout();
			listPage = homePage.loginAs(UserName, Password);
			String loginUser = listPage.getUserName();
			assertThat(loginUser).isEqualTo(UserName);
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
			writeTestResultToFile(caseID, testRst, "Admin_User");
		}
	}

	@Test
	public void test5145() throws Exception
	{
		boolean testRst = false;
		String caseID = "5145";
		logger.info("====Verify user password and user status[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String UserName = getElementValueFromXML(testdata_admin, nodeName, "UserName");
			String Password1 = getElementValueFromXML(testdata_admin, nodeName, "Password1");
			String ConfimPassword1 = getElementValueFromXML(testdata_admin, nodeName, "ConfimPassword1");
			String Message1 = getElementValueFromXML(testdata_admin, nodeName, "Message1");
			String Password2 = getElementValueFromXML(testdata_admin, nodeName, "Password2");
			String ConfimPassword2 = getElementValueFromXML(testdata_admin, nodeName, "ConfimPassword2");
			String Message2 = getElementValueFromXML(testdata_admin, nodeName, "Message2");
			String Password3 = getElementValueFromXML(testdata_admin, nodeName, "Password3");
			String ConfimPassword3 = getElementValueFromXML(testdata_admin, nodeName, "ConfimPassword3");
			String Message3 = getElementValueFromXML(testdata_admin, nodeName, "Message3");
			String Password4 = getElementValueFromXML(testdata_admin, nodeName, "Password4");
			String ConfimPassword4 = getElementValueFromXML(testdata_admin, nodeName, "ConfimPassword4");

			ListPage listPage = super.m.listPage;
			UsersPage usersPage = listPage.EnterUserPage();
			String message = usersPage.changePassword(UserName, Password1, ConfimPassword1);
			assertThat(message).isEqualTo(Message1);

			message = usersPage.changePassword(UserName, Password2, ConfimPassword2);
			assertThat(message).isEqualTo(Message2);

			message = usersPage.changePassword(UserName, Password3, ConfimPassword3);
			assertThat(message).isEqualTo(Message3);

			usersPage.backToDashboard();
			listPage.EnterUserPage();
			usersPage.changeUserStatus(UserName, false);
			String state = usersPage.getUserStatus(UserName);
			assertThat(state).isEqualTo("Inactive");
			testRst = true;

			usersPage.changeUserStatus(UserName, true);

			usersPage.changePassword(UserName, Password4, ConfimPassword4);
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_User");
		}
	}

	@Test
	public void addUser() throws Exception
	{
		ListPage listPage = super.m.listPage;
		UsersPage usersPage = listPage.EnterUserPage();
		usersPage.addUser("test2", "test2@1.com", "password", "password");
	}

}
