package com.lombardrisk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yiwan.webcore.util.PropHelper;

/**
 * Create by Leo Tu on Jul 14, 2015
 */
public class UpdateCaseInQC
{
	public static final String userName = PropHelper.getProperty("rp.user");
	public static final String project = PropHelper.getProperty("rp.project");
	public static final String testFolder = PropHelper.getProperty("rp.testFolder");
	public static final String testSetName = PropHelper.getProperty("rp.testSet");
	private final static Logger logger = LoggerFactory.getLogger(UpdateCaseInQC.class);

	public static void setStatus(String testResultFile)
	{
		try
		{
			String path = System.getProperty("user.dir").toLowerCase().replace("rpsel", "");
			String exeFilePath = "\"" + path + "public/extension/UpdateQC/UpdateQCCase.exe" + "\"";
			logger.info("Exe file:" + exeFilePath);
			String testFilePath = "\"" + testResultFile + "\"";

			String commons[] =
			{ exeFilePath, userName, project, testFolder, testSetName, testFilePath };
			Process process = Runtime.getRuntime().exec(commons);
			int exitcode = process.waitFor();
			logger.info("Finished:" + exitcode);

		}
		catch (Exception e)
		{
			// e.printStackTrace();
			logger.warn("warn", e);
		}
	}

}
