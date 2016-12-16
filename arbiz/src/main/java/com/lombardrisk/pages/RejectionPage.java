package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by leo tu on 12/15/2016.
 */
public class RejectionPage extends AbstractPage
{

	public RejectionPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * get all column name
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllFields() throws Exception
	{
		List<String> fields = new ArrayList<>();
		int i = 1;
		while (element("rj.columnField", String.valueOf(i)).isDisplayed())
		{
			fields.add(element("rj.columnField", String.valueOf(i)).getInnerText());
			i++;
		}
		return fields;
	}
}
