package com.lombardrisk.testCase;

import java.io.File;

import org.testng.annotations.Test;

import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.FileUtil;
import com.lombardrisk.utils.fileService.XMLUtil;

public class TestGeneralFuntion extends TestTemplate
{

	@Test(priority = 1)
	public void testC4818() throws Exception
	{
		String caseID = "4818";
		String nodeName = "C" + caseID;
		boolean testRst = true;
		logger.info("==========Test case C" + caseID + ": Verify apache commons-collections is upgraded to 3.2.2 ========");
		try
		{
			String ProductInstallPath = getElementValueFromXML(testData_General, nodeName, "ProductInstallPath");
			String UnzipPath = getElementValueFromXML(testData_General, nodeName, "UnzipPath");
			String warFile = ProductInstallPath + "/jboss-eap-6.4.0.Alpha/standalone/deployments/ocelot.war";
			File file = new File(UnzipPath);
			if (!file.exists())
				file.mkdir();
			FileUtil.unCompress(warFile, UnzipPath);

			File unzipJarFile = new File(UnzipPath + "/WEB-INF/lib/Commons-collections-3.2.2.jar");
			if (!unzipJarFile.exists())
				testRst = false;
			FileUtil.deleteDirectory(file);

		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "TestGeneralFuntion");
		}
	}

	@Test(priority = 2)
	public void testC4819() throws Exception
	{
		String caseID = "4819";
		String nodeName = "C" + caseID;
		boolean testRst = true;
		logger.info("==========Test case C" + caseID + ": Verify override commons-collection define in the JBoss EAP6.1 ========");
		try
		{
			String ProductInstallPath = getElementValueFromXML(testData_General, nodeName, "ProductInstallPath");
			String folder = ProductInstallPath + "/jboss-eap-6.4.0.Alpha/modules/system/layers/base/org/apache/commons/collections/main";
			File jarFile = new File(folder + "/Commons-collections-3.2.2.jar");
			if (!jarFile.exists())
				testRst = false;

			String jarFileName = XMLUtil.getElementAttributeFromXML(folder + "/module.xml", "resources", "resource-root", "path");
			if (!jarFileName.equalsIgnoreCase("Commons-collections-3.2.2.jar"))
				testRst = false;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "TestGeneralFuntion");
		}
	}

}
