package com.lombardrisk.pages;

import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.util.PropHelper;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Kevin Ling on 2/16/15. Refactored by Leo Tu on 1/29/16
 */
public class HomePage extends AbstractPage
{
	public HomePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	public ListPage logon() throws Exception
	{

		ListPage listPage = null;
		try
		{
			HomePage homePage = new HomePage(getWebDriverWrapper());
			homePage.typeUsername(userName);
			homePage.typePassword(password);
			listPage = homePage.submitLogin();
			waitThat("lp.dashboard").toBeVisible();
		}
		catch (NoSuchElementException e)
		{
			if (element("hm.pageError").getInnerText().equals("This webpage is not available"))
			{
				logger.error("This webpage is not available, please check url and server status!");
				throw new RuntimeException("This webpage is not available");
			}

		}
		waitStatusDlg();
		return listPage;
	}

	public void typeUsername(String username) throws Exception
	{
		element("hm.name").input(username);
	}

	public void typePassword(String password) throws Exception
	{
		element("hm.pwd").input(password);
	}

	public ListPage submitLogin() throws Exception
	{
		element("hm.login").click();
		return new ListPage(getWebDriverWrapper());
	}

	public HomePage submitLoginExpectingFailure() throws Exception
	{
		element("hm.login").click();
		return new HomePage(getWebDriverWrapper());
	}

	public ListPage loginAs(String username, String password) throws Exception
	{
		logger.info("Login RP with user[" + username + "]");
		typeUsername(username);
		typePassword(password);
		waitStatusDlg();
		ListPage listPage = submitLogin();

		String currFormat = getFormatFromDB(username);
		String expectedLang = PropHelper.getProperty("Regional.language").trim();
		logger.info("Expected language is:" + expectedLang);
		if (currFormat == null || !currFormat.equalsIgnoreCase(expectedLang))
		{
			logger.info("update user language and format");
			PreferencePage preferencePage = listPage.enterPreferencePage();
			preferencePage.selectLanguageByValue(expectedLang);
		}
		return listPage;
	}

	public boolean isLogonPage() throws Exception
	{
		return element("hm.login").isDisplayed();
	}

}
