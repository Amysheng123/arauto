package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;
import org.yiwan.webcore.web.IWebDriverWrapper.IWebElementWrapper;

/**
 * Created by Leo Tu on 4/27/2016.
 */
public class ShowDeletedReturnPage extends AbstractPage
{

	/**
	 * @param webDriverWrapper
	 */
	public ShowDeletedReturnPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	public ListPage restoreReturn(String formCode, String formVerion, String referenceDate) throws Exception
	{
		logger.info("Begin restore form [" + formCode + " v" + formVerion + "@" + referenceDate + "]");
		String list[] =
		{ referenceDate, formCode, formVerion };
		if (element("sdrp.radioBtn", list).isDisplayed())
		{
			element("sdrp.radioBtn", list).click();
			waitStatusDlg();
			element("sdrp.restoreBtn").click();
			waitStatusDlg();
			element("sdrp.comment").input("restore test");
			waitStatusDlg();
			element("sdrp.restoreConfirm").click();
			waitStatusDlg();
		}
		else
		{
			boolean flag = true;
			while (flag)
			{
				if (!element("sdrp.nextPage").getAttribute("class").contains("ui-state-disabled"))
				{
					element("sdrp.nextPage").click();
					waitStatusDlg();
				}
				else if (element("sdrp.radioBtn", list).isDisplayed())
				{
					element("sdrp.radioBtn", list).click();
					waitStatusDlg();
					element("sdrp.restoreBtn").click();
					waitStatusDlg();
					element("sdrp.comment").input("restore test");
					element("sdrp.restoreConfirm").click();
					element("sdrp.cancelBtn").click();
					flag = false;
				}

			}
		}
		return closeShowDeletedReturnPage();
	}

	public ListPage restoreReturn(String referenceDate, String formCode, String formVerion, String... edition) throws Exception
	{
		logger.info("Begin restore form [" + formCode + " v" + formVerion + "@" + referenceDate + "]");
		if (!element("sdrp.firstPage").getAttribute("class").contains("ui-state-disabled"))
		{
			element("sdrp.firstPage").click();
			waitStatusDlg();
		}
		if (edition != null)
		{
			for (String e : edition)
			{
				String list[] =
				{ referenceDate, formCode, formVerion, e };
				if (element("sdrp.radioBtn2", list).isDisplayed())
				{
					element("sdrp.radioBtn2", list).click();
					waitStatusDlg();
					element("sdrp.restoreBtn").click();
					waitStatusDlg();
					element("sdrp.comment").input("restore test");
					waitStatusDlg();
					element("sdrp.restoreConfirm").click();
					waitThat("sdrp.messageTitle").toBeVisible();
					waitThat("sdrp.messageTitle").toBeInvisible();
				}
				else
				{
					boolean flag = true;
					while (flag)
					{
						if (!element("sdrp.nextPage").getAttribute("class").contains("ui-state-disabled"))
						{
							element("sdrp.nextPage").click();
							waitStatusDlg();
						}
						else if (element("sdrp.radioBtn2", list).isDisplayed())
						{
							element("sdrp.radioBtn2", list).click();
							waitStatusDlg();
							element("sdrp.restoreBtn").click();
							waitStatusDlg();
							element("sdrp.comment").input("restore test");
							waitStatusDlg();
							element("sdrp.restoreConfirm").click();
							waitThat("sdrp.messageTitle").toBeVisible();
							waitThat("sdrp.messageTitle").toBeInvisible();
							flag = false;
						}
						else
							flag = false;

					}
				}
			}
		}
		else
		{
			String list[] =
			{ referenceDate, formCode, formVerion };
			while (element("sdrp.radioBtn3", list).isDisplayed())
			{
				element("sdrp.radioBtn3", list).click();
				waitStatusDlg();
				element("sdrp.restoreBtn").clickByJavaScript();
				waitStatusDlg();
				element("sdrp.comment").input("restore test");
				waitStatusDlg();
				element("sdrp.restoreConfirm").click();

				boolean flag = true;
				while (flag)
				{
					if (!element("sdrp.nextPage").getAttribute("class").contains("ui-state-disabled"))
					{
						element("sdrp.nextPage").click();
						waitStatusDlg();
					}
					else if (element("sdrp.radioBtn3", list).isDisplayed())
					{
						element("sdrp.radioBtn3", list).click();
						waitStatusDlg();
						element("sdrp.restoreBtn").click();
						waitStatusDlg();
						element("sdrp.comment").input("restore test");
						waitStatusDlg();
						element("sdrp.restoreConfirm").click();
						waitThat("sdrp.messageTitle").toBeVisible();
						waitThat("sdrp.messageTitle").toBeInvisible();
						flag = false;
					}
					else
						flag = false;

				}
			}
		}

		return closeShowDeletedReturnPage();
	}

	public ListPage closeShowDeletedReturnPage() throws Exception
	{
		element("sdrp.cancelBtn").click();
		waitStatusDlg();
		Thread.sleep(1000);
		return new ListPage(getWebDriverWrapper());
	}

	public List<String> getDeletedEditions(String referenceDate) throws Exception
	{
		List<String> editions = new ArrayList<>();
		int i = 0;
		for (IWebElementWrapper e : element("sdrp.deletedReturnTable").getAllMatchedElements())
		{
			i = i + 1;
			if (element("sdrp.referenceDate", String.valueOf(i)).getInnerText().equals(referenceDate))
			{
				editions.add(element("sdrp.edition", String.valueOf(i)).getInnerText());
			}
		}

		return editions;
	}

}
