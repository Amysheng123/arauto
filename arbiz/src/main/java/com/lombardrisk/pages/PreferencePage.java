package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Leo Tu on 8/2/16
 */
public class PreferencePage extends AbstractPage
{
	public PreferencePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	protected ListPage saveSetting() throws Exception
	{
		element("prp.confirm").click();
		return new ListPage(getWebDriverWrapper());
	}

	protected ListPage cancelSetting() throws Exception
	{
		element("prp.canel").click();
		return new ListPage(getWebDriverWrapper());
	}

	public ListPage selectTimeZone(String timezone) throws Exception
	{
		element("prp.TZC").click();
		waitStatusDlg();
		element("prp.TZS").selectByVisibleText(timezone);
		element("prp.confirm").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}

	public ListPage selectLanguage(String language) throws Exception
	{
		element("prp.LC").click();
		waitStatusDlg();
		element("prp.LS").selectByVisibleText(language);
		element("prp.confirm").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}

	public ListPage selectLanguageByValue(String language) throws Exception
	{
		element("prp.LC").click();
		waitStatusDlg();
		element("prp.LS").selectByValue(language);
		Thread.sleep(500);
		element("prp.confirm").click();
		waitStatusDlg();
		Thread.sleep(2000);
		return new ListPage(getWebDriverWrapper());
	}

}
