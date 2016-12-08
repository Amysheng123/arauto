package com.lombardrisk.pages;

import java.io.File;

import org.openqa.selenium.By;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactored by Leo Tu on 1/25/16
 */
public abstract class AbstractImportPage extends AbstractPage
{
	/**
	 * 
	 * @param webDriverWrapper
	 */
	public AbstractImportPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);

	}

	/**
	 * upload file
	 * 
	 * @param file
	 * @param type
	 * @throws Exception
	 */
	public void setImportFile(File file, String type) throws Exception
	{
		logger.info("Execute js script");
		String js = "document.getElementById('" + getUploadId(type) + "').getElementsByTagName('div')[0].getElementsByTagName('span')[0].className='';";
		executeScript(js);
		element("aip.uploadInput", type).type(file.getAbsolutePath());
		waitUploading(type, file.getName());
	}

	public abstract String parentFormId(String type);

	public abstract By getImportBtn(String type);

	private String getUploadId(String type)
	{
		return type + ":importFileUpload";
	}

	private String getUploadInputId(String type)
	{
		return type + ":importFileUpload_input";
	}

	private String getErrorTextarea(String type)
	{
		return type + ":errorTextarea";
	}

	/**
	 * Get import error message
	 * 
	 * @param type
	 * @return Error message
	 * @throws Exception
	 */
	public String getErrorText(String type) throws Exception
	{
		return element("aip.error", type).getInnerText();
	}

	/**
	 * Enter ImportConfirmPage
	 * 
	 * @param type
	 * @return ImportConfirmPage
	 * @throws Exception
	 */
	public ImportConfirmPage importFileBtnClick(String type) throws Exception
	{
		long startTime = System.currentTimeMillis();
		if (element("aip.import", type).isDisplayed())
			element("aip.import", type).click();

		else if (element("aip.import2", type).isDisplayed())
			element("aip.import2", type).click();
		logger.info("Begin import data");
		long curTime = System.currentTimeMillis();
		waitStatusDlg();
		long spendTime = (curTime - startTime) / 1000;
		boolean flag = true;
		while (flag)
		{
			if (spendTime > 600)
				flag = false;
			if (!element("aip.loading").isDisplayed())
				flag = false;
			curTime = System.currentTimeMillis();
			spendTime = (curTime - startTime) / 1000;
		}
		logger.info("Spend " + spendTime + " seconds to import file");
		return new ImportConfirmPage(getWebDriverWrapper());
	}

	/**
	 * wait until file uploaded
	 * 
	 * @param type
	 * @param fileName
	 * @throws Exception
	 */
	public void waitUploading(String type, String fileName) throws Exception
	{
		long startTime = System.currentTimeMillis();
		boolean flag = true;
		long curTime = System.currentTimeMillis();
		long spendTime = (curTime - startTime) / 1000;
		while (flag)
		{
			if (element("aip.uploadFileName1", type).isDisplayed())
			{
				if (element("aip.uploadFileName1", type).getInnerText().equals(fileName))
					flag = false;
			}
			if (element("aip.uploadFileName2", type).isDisplayed())
			{
				if (element("aip.uploadFileName2", type).getInnerText().equals(fileName))
					flag = false;
			}
			if (element("aip.errorIcon", type).isDisplayed() || element("aip.errorIcon2", type).isDisplayed())
			{
				flag = false;
				logger.info("Find error icon, exit");
			}
			if (element("aip.errorInfo", type).getInnerText().length() > 0)
			{
				flag = false;
				logger.info("Find error message, exit");
			}
			if (flag)
			{
				curTime = System.currentTimeMillis();
				spendTime = (curTime - startTime) / 1000;
				if (spendTime > 600)
				{
					flag = false;
				}
			}
		}
		logger.info("Spend " + spendTime + " seconds to upload file");
	}

}
