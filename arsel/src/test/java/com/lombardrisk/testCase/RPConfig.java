package com.lombardrisk.testCase;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.FileUtil;
import com.lombardrisk.utils.fileService.XMLUtil;

/**
 * Create by Leo Tu on Jul 13, 2015
 */
public class RPConfig extends TestTemplate
{
	private final static Logger logger = LoggerFactory.getLogger(RPConfig.class);
	static String productPrefix = "";
	static String productVersion = "";

	@Test
	public void testReporterPortalConfig(String RPInstallPath, String ZipFile, String Server, String DBName) throws Exception
	{
		boolean testRst = false;
		boolean add = testRPConfigAdd(RPInstallPath, ZipFile, Server, DBName);
		boolean deact = testRPConfigDeactivate(RPInstallPath, ZipFile, Server, DBName);

		boolean act = testRPConfigActivate(RPInstallPath, ZipFile, Server, DBName);

		boolean del = testRPConfigDelete(RPInstallPath, ZipFile, Server, DBName);

		if (add == true && deact == true && act == true && del == true)
			testRst = true;
		Assert.assertEquals(true, testRst);
	}

	public boolean testRPConfigAdd(String installPath, String ZipFile, String SERVERNAME, String DBNAME) throws Exception
	{
		logger.info("Begin test add config zip");
		boolean rst = false;
		String cmd = installPath + "\\Reporter Portal\\bin\\config.bat  -a " + ZipFile;
		logger.info("Upload zip in cmd");
		getCMDRst(cmd);
		String path = ZipFile.replace(".zip", "") + "\\";
		logger.info("Unzip " + ZipFile + " to " + path);

		logger.info("Unzip  zip file");
		FileUtil.unCompress(ZipFile, path);

		String xmlFile = path + "\\manifest.xml";

		String prefix = XMLUtil.getElementContentFromXML(xmlFile, "prefix");
		String version = XMLUtil.getElementContentFromXML(xmlFile, "implementationVersion");
		String DPMVersion = XMLUtil.getElementContentFromXML(xmlFile, "version");

		productPrefix = prefix;
		productVersion = version;

		String verifyRst1 = "f";
		String verifyRst2 = "f";
		String verifyRst3 = "f";
		String SQL = "SELECT PREFIX FROM CFG_INSTALLED_CONFIGURATIONS;";
		String prefix_db = DBQuery.queryRecord(SQL);
		if (prefix.equals(prefix_db))
			verifyRst1 = "p";

		SQL = "SELECT IMPLEMENTATION_VERSION FROM CFG_INSTALLED_CONFIGURATIONS;";
		String version_db = DBQuery.queryRecord(SQL);
		if (version.equals(version_db))
			verifyRst2 = "p";

		SQL = "SELECT DPM_VERSION FROM CFG_INSTALLED_CONFIGURATIONS;";
		String DPM_db = DBQuery.queryRecord(SQL);
		if (DPMVersion.equals(DPM_db))
			verifyRst3 = "p";

		if (verifyRst1.equals("p") && verifyRst2.equals("p") && verifyRst3.equals("p"))
			rst = true;
		return rst;
	}

	public boolean testRPConfigDeactivate(String installPath, String ZipFile, String SERVERNAME, String DBNAME) throws Exception
	{
		boolean rst = false;
		logger.info("Begin test deactivate");
		String cmd = installPath + "\\Reporter Portal\\bin\\config.bat  -d " + productPrefix + " -iv " + productVersion;
		logger.info("Deactivate config in cmd");
		getCMDRst(cmd);

		String SQL = "SELECT STATUS FROM CFG_INSTALLED_CONFIGURATIONS WHERE PREFIX='" + productPrefix + "' AND IMPLEMENTATION_VERSION='" + productVersion + "'";

		String status_db = DBQuery.queryRecord(SQL);
		if (status_db.equals("I"))
			rst = true;

		return rst;

	}

	public boolean testRPConfigActivate(String installPath, String ZipFile, String SERVERNAME, String DBNAME) throws Exception
	{
		boolean rst = false;
		logger.info("Begin test activate");
		String cmd = installPath + "\\Reporter Portal\\bin\\config.bat  -t " + productPrefix + " -iv " + productVersion;
		logger.info("Activate config in cmd");
		String line = getCMDRst(cmd);
		if (line.equals("Product configuration is active now"))
		{
			String SQL = "SELECT STATUS FROM CFG_INSTALLED_CONFIGURATIONS WHERE PREFIX='" + productPrefix + "' AND IMPLEMENTATION_VERSION='" + productVersion + "'";

			String status_db = DBQuery.queryRecord(SQL);
			if (status_db.equals("A"))
				rst = true;
		}

		return rst;

	}

	public boolean testRPConfigDelete(String installPath, String ZipFile, String SERVERNAME, String DBNAME) throws Exception
	{
		boolean rst = false;
		logger.info("Begin test delete");
		String cmd = installPath + "\\Reporter Portal\\bin\\config.bat  -r " + productPrefix + " -iv " + productVersion;
		logger.info("Delete config in cmd");
		getCMDRst(cmd);

		String SQL = "SELECT count(*) FROM CFG_INSTALLED_CONFIGURATIONS WHERE PREFIX='" + productPrefix + "' AND IMPLEMENTATION_VERSION='" + productVersion + "'";

		String cmt_db = DBQuery.queryRecord(SQL);
		if (cmt_db.equals("0"))
			rst = true;

		return rst;

	}

	public String getCMDRst(String cmd) throws Exception
	{
		BufferedReader br = null;
		String rst = null;
		try
		{
			// String
			// cmd="D:\\Testing\\Reporter Portal\\bin\\config.bat -list";
			Process p = Runtime.getRuntime().exec(cmd);
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null)
			{
				if (!line.startsWith("PREFIX"))
				{
					rst = line;
					// System.out.println(line);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{

			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}

		return rst;
	}

}
