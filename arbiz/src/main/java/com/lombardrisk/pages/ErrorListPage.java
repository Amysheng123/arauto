package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Leo Tu on 1/28/16
 */
public class ErrorListPage extends AbstractDrilldownPage
{

	/**
	 * 
	 * @param webDriverWrapper
	 */
	public ErrorListPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * get rule No by row index
	 * 
	 * @param index
	 * @return rule No
	 * @throws Exception
	 */
	public String getRuleNo(int index) throws Exception
	{
		return element("elp.ruleNO", String.valueOf(index)).getInnerText().split(" ")[1];
	}

	/**
	 * get rule level by row index
	 * 
	 * @param index
	 * @return rule level
	 * @throws Exception
	 */
	public String getLevelText(int index) throws Exception
	{
		return element("elp.ruleLevel", String.valueOf(index)).getInnerText().trim();
	}

	/**
	 * get message by row index
	 * 
	 * @param index
	 * @return message
	 * @throws Exception
	 */
	public String getMessageText(int index) throws Exception
	{
		return element("elp.message", String.valueOf(index)).getInnerText().trim();
	}

	/**
	 * get error info
	 * 
	 * @param instance
	 * @param ruleType
	 * @param ruleID
	 * @param rowKey
	 * @return error info(List)
	 * @throws Exception
	 */
	public List<String> getErrorInfo(String instance, String ruleType, String ruleID, String rowKey) throws Exception
	{
		List<String> ErrorInfo = new ArrayList<String>();
		boolean flag = true;
		int rowAmt = (int) element("elp.problemTab").getRowCount();
		boolean find = false;
		boolean lastPage = false;
		int count = 0;
		String startPage = null;
		String curPage = null;
		for (int i = 1; i <= 3; i++)
		{
			if (element("elp.curPageNO", String.valueOf(i)).getAttribute("class").contains("ui-state-active"))
			{
				startPage = element("elp.curPageNO", String.valueOf(i)).getInnerText();
				break;
			}
		}
		while (flag)
		{
			for (int i = 1; i <= rowAmt; i++)
			{
				String ID = getRuleNo(i);
				if (ID.equals(ruleID))
				{
					if (rowKey.length() == 0)
					{
						String Msg = getMessageText(i);
						if (instance.length() > 0)
						{
							String instanceName = Msg.substring(Msg.indexOf(":") + 1, Msg.indexOf(":") + 2);
							if (instanceName.equals(instance))
							{
								ErrorInfo.add(getLevelText(i));
								ErrorInfo.add(Msg);
								find = true;
								flag = false;
								break;
							}
						}
						else
						{
							ErrorInfo.add(getLevelText(i));
							ErrorInfo.add(Msg);
							find = true;
							flag = false;
							break;
						}
					}
					else
					{
						String Msg = getMessageText(i);
						if (!Msg.startsWith("It is not allowed that a \"for each instance\""))
						{
							String instanceName = Msg.substring(Msg.indexOf(":") + 1, Msg.indexOf(":") + 2);
							String Msg_temp = Msg.replace("[PageInstance:" + instanceName + "]", "");
							String rowID = Msg_temp.substring(Msg_temp.indexOf(":") + 1, Msg_temp.indexOf(":") + 2);

							if (!"".equals(instance))
							{
								if (rowID.equals(rowKey) && instanceName.equals(instance))
								{
									ErrorInfo.add(getLevelText(i));
									ErrorInfo.add(Msg);
									find = true;
									flag = false;
									break;
								}
							}
							else
							{
								if (rowID.equals(rowKey))
								{
									ErrorInfo.add(getLevelText(i));
									ErrorInfo.add(Msg);
									find = true;
									flag = false;
									break;
								}
							}
						}
						else
						{
							ErrorInfo.add(getLevelText(i));
							ErrorInfo.add(Msg);
							find = true;
							flag = false;
							break;
						}
					}
				}
			}

			if (count > 0)
			{
				for (int i = 1; i <= 3; i++)
				{
					if (element("elp.curPageNO", String.valueOf(i)).getAttribute("class").contains("ui-state-active"))
					{
						curPage = element("elp.curPageNO", String.valueOf(i)).getInnerText();
						break;
					}
				}

				if (Integer.parseInt(curPage) - Integer.parseInt(startPage) >= 0)
					break;
			}

			if (flag)
			{
				try
				{
					if ("0".equals(element("elp.nextPage").getAttribute("tabindex")))
					{
						element("elp.nextPage").click();
						waitStatusDlg();
					}
					else
					{
						lastPage = true;
					}
				}
				catch (Exception e)
				{
					logger.warn("warn", e);
					flag = false;
				}
			}

			if (!find && lastPage && count < 1)
			{
				try
				{
					if ("0".equals(element("elp.firtPage").getAttribute("tabindex")))
					{
						element("elp.firtPage").click();
						count++;

						for (int i = 1; i <= 3; i++)
						{
							if (element("elp.curPageNO", String.valueOf(i)).getAttribute("class").contains("ui-state-active"))
							{
								curPage = element("elp.curPageNO", String.valueOf(i)).getInnerText();
								break;
							}
						}
						if (Integer.parseInt(curPage) - Integer.parseInt(startPage) >= 0)
						{
							break;
						}

					}
				}
				catch (Exception e)
				{
					logger.warn("warn", e);
				}
			}

			if (!find && count > 0 && Integer.parseInt(curPage) - Integer.parseInt(startPage) >= 0)
			{
				flag = false;
			}

			rowAmt = (int) element("elp.problemTab").getRowCount();
		}

		return ErrorInfo;
	}

	/**
	 * click rule No
	 * 
	 * @param ruleNo
	 * @throws Exception
	 */
	public void clickRuleNO(String ruleNo) throws Exception
	{
		logger.info("Click rule[" + ruleNo + "] in PROBELMS");
		if (!element("elp.firtPage").getAttribute("class").contains("ui-state-disabled"))
		{
			element("elp.firtPage").click();
			waitStatusDlg();
			waitThat().timeout(300);
		}
		if (!element("elp.nextPage").getAttribute("class").contains("ui-state-disabled"))
		{
			while (!element("elp.nextPage").getAttribute("class").contains("ui-state-disabled"))
			{
				if (element("elp.ruleNo", ruleNo).isDisplayed())
				{
					element("elp.ruleNo", ruleNo).click();
					waitStatusDlg();
				}
				else
				{
					element("elp.nextPage").click();
					waitStatusDlg();
					waitThat().timeout(300);
				}
			}
		}
		else
		{
			element("elp.ruleNo", ruleNo).click();
			waitStatusDlg();
		}

	}

	/**
	 * get rule background color
	 * 
	 * @param ruleNo
	 * @return color
	 * @throws Exception
	 */
	public String getRuleBackgroudColor(String ruleNo) throws Exception
	{
		String color = "";
		if (element("elp.firtPage").getAttribute("class").contains("ui-state-disabled"))
		{
			element("elp.firtPage").click();
			waitStatusDlg();
			waitThat().timeout(300);
		}
		if (!element("elp.nextPage").getAttribute("class").contains("ui-state-disabled"))
		{
			while (!element("elp.nextPage").getAttribute("class").contains("ui-state-disabled"))
			{
				if (element("elp.ruleNo", ruleNo).isDisplayed())
				{
					color = element("elp.ruleBackgroud", ruleNo).getCssValue("background-color");
					break;
				}
				else
				{
					element("elp.nextPage").click();
					waitStatusDlg();
					waitThat().timeout(300);
				}
			}
		}
		else
		{
			color = element("elp.ruleBackgroud", ruleNo).getCssValue("background-color");
		}
		if ("rgba(204, 153, 204, 1)".equalsIgnoreCase(color))
			return "Purple";
		else if ("rgba(236, 184, 188, 1)".equalsIgnoreCase(color))
			return "Red";
		else if ("rgba(255, 255, 204, 1)".equalsIgnoreCase(color))
			return "Yellow";
		else if ("rgba(181, 217, 245, 1)".equalsIgnoreCase(color))
			return "Blue";
		else
			return "";
	}

}
