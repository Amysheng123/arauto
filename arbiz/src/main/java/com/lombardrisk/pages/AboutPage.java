package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Leo Tu on 11/25/2016.
 */
public class AboutPage extends AbstractPage
{
	public AboutPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	public void sortColumn(String type, int columnID) throws Exception
	{
		String ID = String.valueOf(columnID);
		if (!element("abp.sortColumn", ID).getAttribute("class").contains("ui-icon-triangle-1-n") && !element("abp.sortColumn", ID).getAttribute("class").contains("ui-icon-triangle-1-s"))
		{
			element("abp.sortColumn", ID).click();
			waitStatusDlg();
		}
		if ("asc".equalsIgnoreCase(type))
		{
			if (!element("abp.sortColumn", ID).getAttribute("class").contains("ui-icon-triangle-1-n"))
			{
				element("abp.sortColumn", ID).click();
				waitStatusDlg();
			}
		}
		else
		{
			if (!element("abp.sortColumn", ID).getAttribute("class").contains("ui-icon-triangle-1-s"))
			{
				element("abp.sortColumn", ID).click();
				waitStatusDlg();
			}
		}
	}

	public List<String> getConfigurationsColumns(int columnID) throws Exception
	{
		List<String> ConfigurationsColumns = new ArrayList<>();
		int num = element("abp.rows").getNumberOfMatches();
		for (int i = 1; i <= num; i++)
		{
			String[] list =
			{ String.valueOf(i), String.valueOf(columnID) };
			ConfigurationsColumns.add(element("abp.cell", list).getInnerText());
		}
		return ConfigurationsColumns;
	}

	public List<String> getConfigurationsRow(String prefix) throws Exception
	{
		List<String> ConfigurationsRow = new ArrayList<>();
		int num = element("abp.rows").getNumberOfMatches();
		for (int i = 1; i <= num; i++)
		{
			String[] list =
			{ String.valueOf(i), "1" };
			if (element("abp.cell", list).getInnerText().equalsIgnoreCase(prefix))
			{
				for (int j = 1; j <= 5; j++)
				{
					String[] list2 =
					{ String.valueOf(i), String.valueOf(j) };
					ConfigurationsRow.add(element("abp.cell", list2).getAttribute("title"));
				}
				break;
			}
		}
		return ConfigurationsRow;
	}
}
