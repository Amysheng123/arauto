package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Create by Leo Tu on 8/9/2016
 */

public class MessageCenter extends AbstractPage
{

	/**
	 * @param webDriverWrapper
	 */
	public MessageCenter(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Click first error link
	 * 
	 * @return JobDetailsPage
	 * @throws Exception
	 */
	public JobDetailsPage clickFirstErrorLink() throws Exception
	{
		element("mcp.firstErrorLink").click();
		waitStatusDlg();
		return new JobDetailsPage(getWebDriverWrapper());
	}

	/**
	 * get first message
	 * 
	 * @return message
	 * @throws Exception
	 */
	public String getLatestMessage() throws Exception
	{
		return element("mcp.latestMessage").getInnerText();
	}

	/**
	 * close first message
	 * 
	 * @throws Exception
	 */
	public void closeFirstMessage() throws Exception
	{
		element("mcp.firstCloseIcon").click();
		waitStatusDlg();
	}

	/**
	 * close message center
	 * 
	 * @throws Exception
	 */
	public void closeMessageCenter() throws Exception
	{
		element("mcp.messageCenter").click();
		waitStatusDlg();
	}

	/**
	 * get message type,if job status is success, return true, otherwise return
	 * false
	 * 
	 * @return true or false
	 */
	public boolean getFirstMessageType() throws Exception
	{
		String source = element("mcp.firstJobStatus").getAttribute("src");
		if (source.contains("FailIcon"))
			return false;
		else
			return true;
	}

}
