package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Leo Tu on 1/25/16
 */
public class ChangePasswordPage extends AbstractPage
{

	/**
	 * 
	 * @param webDriverWrapper
	 */
	public ChangePasswordPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * input current password
	 * 
	 * @param password
	 * @throws Exception
	 */
	private void setCurrentPassword(String password) throws Exception
	{
		element("cpw.curPD").type(password);
	}

	/**
	 * input new password
	 * 
	 * @param password
	 * @throws Exception
	 */
	private void setNewPassword(String password) throws Exception
	{
		element("cpw.newPD").type(password);
	}

	/**
	 * input new password again
	 * 
	 * @param password
	 * @throws Exception
	 */
	private void setConfirmPassword(String password) throws Exception
	{
		element("cpw.conPD").type(password);
	}

	/**
	 * save settings
	 * 
	 * @return ListPage
	 * @throws Exception
	 */
	public ListPage saveSetting() throws Exception
	{
		element("cpw.save").click();
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * cancel settings
	 * 
	 * @return ListPage
	 * @throws Exception
	 */
	public ListPage cancelSetting() throws Exception
	{
		element("cpw.cancel").click();
		return new ListPage(getWebDriverWrapper());
	}

}
