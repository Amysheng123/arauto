package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Leo Tu on 2/1/16
 */
public class ExportXBRLPage extends AbstractPage
{
	public ExportXBRLPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	protected String getParentForm()
	{
		return "transmitForm";
	}

	protected String getParentSubmitForm()
	{
		return "transmitSubmitForm";
	}

	protected void selectForm(String Form) throws Exception
	{
		logger.info("Click return:" + Form);
		String formCode = Form.split(" ")[0];
		String formVersion = Form.split("v")[1];
		deselectAll();
		String[] list = new String[]
		{ formCode, formVersion };
		element("exbrl.retCheckBox", list).click();
		waitStatusDlg();
	}

	protected void deselectAll() throws Exception
	{
		if (element("exbrl.selectAll").getAttribute("class").contains("ui-state-active"))
		{
			element("exbrl.selectAll").click();
			waitStatusDlg();
		}
	}

	protected void selectAll() throws Exception
	{
		if (!element("exbrl.selectAll").getAttribute("class").contains("ui-state-active"))
		{
			element("exbrl.selectAll").click();
			waitStatusDlg();
		}
	}

	protected void selectCompressType(String type) throws Exception
	{
		if (type != null)
		{
			element("exbrl.selectCompressType").selectByVisibleText(type.toUpperCase());
			waitStatusDlg();
		}
	}

	/**
	 * Get the form name and version
	 */
	public List<String> getForm() throws Exception
	{
		List<String> list = new ArrayList<>();
		List<String> info = element("exbrl.formInfo").getAllInnerTexts();
		for (int i = 0; i < info.size(); i = i + 2)
		{
			list.add(info.get(i) + " v" + info.get(i + 1));
		}
		return list;
	}
}
