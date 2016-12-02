package com.lombardrisk.pages;

import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.util.PropHelper;
import org.yiwan.webcore.web.IWebDriverWrapper;


/**
 * Created by Kevin Ling on 2/16/15.
 * Refactored by Leo Tu on 1/29/16
 */
public class HomePage extends AbstractPage {

    public static final String userName = PropHelper.getProperty("rp.user");
    public static final String password = PropHelper.getProperty("rp.password");

    public HomePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public ListPage logon() throws Exception {

        ListPage listPage = null;
        try {
            HomePage homePage = new HomePage(getWebDriverWrapper());
            homePage.typeUsername(userName);
            homePage.typePassword(password);
            listPage = homePage.submitLogin();
        } catch (NoSuchElementException e) {
            if (element("hm.pageError").getInnerText().equals("This webpage is not available")) {
                logger.error("This webpage is not available, please check url and server status!");
                throw new RuntimeException("This webpage is not available");
            }

        }
        return listPage;
    }


    public HomePage typeUsername(String username) throws Exception {
        element("hm.name").input(username);
        return this;
    }

    public HomePage typePassword(String password) throws Exception {
        element("hm.pwd").input(password);
        return this;
    }

    public ListPage submitLogin() throws Exception {
        element("hm.login").click();
        return new ListPage(getWebDriverWrapper());
    }


    public HomePage submitLoginExpectingFailure() throws Exception {
        element("hm.login").click();
        return new HomePage(getWebDriverWrapper());
    }

    public ListPage loginAs(String username, String password) throws Exception {
        logger.info("Login RP with user[" + username + "]");
        typeUsername(username);
        typePassword(password);
        return submitLogin();
    }

    public boolean isLogonPage() throws Exception {
        return element("hm.login").isDisplayed();
    }

}
