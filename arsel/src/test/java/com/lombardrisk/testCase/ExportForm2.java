package com.lombardrisk.testCase;

import java.io.File;
import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Created by leo tu on 12/19/2016.
 */
public class ExportForm2 extends TestTemplate
{

	@Test
	public void test6682() throws Exception
	{
		String caseID = "6682";
		logger.info("====Test...[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_ExportForm2, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ImportFile = testData.get(3);

			File importFile = new File(testData_ExportForm2.replace("ExportForm2.xml", "ImportFile") + "/" + ImportFile);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

}
