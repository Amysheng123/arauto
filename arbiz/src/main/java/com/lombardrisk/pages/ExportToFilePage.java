package com.lombardrisk.pages;

import java.util.List;

import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Leo Tu on 1/25/2016
 */
public class ExportToFilePage extends AbstractPage
{
	public ExportToFilePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}

	public void setGroupSelector(String group, String type) throws Exception
	{
		if (group != null)
		{
			logger.info("Set group: " + group);
			String replace = "";
			if (!"xbrl".equalsIgnoreCase(type))
				replace = "4Fed";
			element("efp.groupSelector", replace).selectByVisibleText(group);
			waitStatusDlg();
			Thread.sleep(2000);
		}

	}

	public void setReferenceDate(String date, String type) throws Exception
	{
		if (date != null)
		{
			logger.info("Set referenceDate: " + date);
			String replace = "";
			if (!"xbrl".equalsIgnoreCase(type))
				replace = "4Fed";
			element("efp.referenceDate", replace).selectByVisibleText(date);
			waitStatusDlg();
			Thread.sleep(2000);
		}
	}

	public void setFrameworkSelector(String framework, String type) throws Exception
	{
		try
		{
			logger.info("Set framework: " + framework);
			String replace = "";
			if (!"xbrl".equalsIgnoreCase(type))
				replace = "4Fed";
			element("efp.frameworkSelector", replace).selectByVisibleText(framework);
			waitStatusDlg();
			Thread.sleep(2000);
			if (!element("efp.frameworkSelector", replace).getSelectedText().equalsIgnoreCase(framework))
			{
				element("efp.frameworkSelector", replace).selectByVisibleText(framework);
				waitStatusDlg();
				Thread.sleep(2000);
			}
		}
		catch (Exception e)
		{
			logger.warn("warn", e);
			// e.printStackTrace();
		}
	}

	public void setTaxonomySelector(String taxonomy, String type) throws Exception
	{
		if (taxonomy != null)
		{
			logger.info("Set taxonomy: " + taxonomy);
			String replace = "";
			if (!"xbrl".equalsIgnoreCase(type))
				replace = "4Fed";
			element("efp.taxonomySelector", replace).selectByVisibleText(taxonomy);
			waitStatusDlg();
			Thread.sleep(2000);
		}

	}

	public void setModuleSelector(String module, String type) throws Exception
	{
		if (module != null)
		{
			logger.info("Set module: " + module);
			String replace = "";
			if (!"xbrl".equalsIgnoreCase(type))
				replace = "4Fed";
			element("efp.moduleSelector", replace).selectByVisibleText(module);
			waitStatusDlg();
			Thread.sleep(2000);
		}
	}

	public void setCompressType(String compressType, String type) throws Exception
	{
		if (compressType != null)
		{
			logger.info("Set compressType: " + compressType);
			String replace = "";
			if (!"xbrl".equalsIgnoreCase(type))
				replace = "4Fed";
			element("efp.compressType", replace).selectByVisibleText(compressType.toUpperCase());
			waitStatusDlg();
			Thread.sleep(2000);
		}

	}

	public void setExportComment(String comment) throws Exception
	{
		if (comment != null)
		{
			logger.info("Set comment: " + comment);
			element("efp.exportComment").input(comment);
			waitStatusDlg();
			element("efp.exportBtn2").click();
			waitStatusDlg();
		}

	}

	public void exportBtnClick(String type) throws Exception
	{
		logger.info("Click export button");
		String replace = "";
		if (!"xbrl".equalsIgnoreCase(type))
			replace = "4Fed";
		if (!type.toLowerCase().startsWith("ds"))
		{
			if (httpDownload)
			{
				TestCaseManager.getTestCase().startTransaction("");
				TestCaseManager.getTestCase().setPrepareToDownload(true);
				element("efp.exportConfirm", replace).click();
				TestCaseManager.getTestCase().stopTransaction();
			}
			else
			{
				element("efp.exportConfirm", replace).click();
			}
		}
		else
			element("efp.exportConfirm_DS").click();
		waitStatusDlg();
	}

	public void closeExportPage(String type) throws Exception
	{
		String replace = "";
		if (!"xbrl".equalsIgnoreCase(type))
			replace = "4Fed";
		if (element("efp.cancel", replace).isDisplayed())
		{
			element("efp.cancel", replace).click();
			waitStatusDlg();
		}

	}

	/**
	 * Get all the dropdown list label name
	 */
	public List<String> getAllLabelNames() throws Exception
	{
		return element("efp.labelName").getAllInnerTexts();
	}

}
