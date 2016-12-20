package com.lombardrisk.testCase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.FileUtil;

/**
 * Create by Leo Tu on 3/8/2016.
 */

public class Utility extends TestTemplate
{

	@Test
	public void test5524() throws Exception
	{
		String caseID = "5524";
		logger.info("====Test case[" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_Utility, nodeName, "Regulator");
			String Group = getElementValueFromXML(testData_Utility, nodeName, "Group");
			String Form = getElementValueFromXML(testData_Utility, nodeName, "Form");
			String ProcessDate = getElementValueFromXML(testData_Utility, nodeName, "ProcessDate");
			String FileName = getElementValueFromXML(testData_Utility, nodeName, "File");
			String Cells = getElementValueFromXML(testData_Utility, nodeName, "CellNames");
			String Module = getElementValueFromXML(testData_Utility, nodeName, "Module");

			List<String> cellNames = new ArrayList<>();
			Collections.addAll(cellNames, Cells.split(","));

			File importFile = new File(testData_Utility.replace("Utility.xml", caseID + "/" + FileName));
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, Form, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			String exportFilePath = formInstancePage.exportToFile(Group, Form, ProcessDate, "iFile", Module, null);
			String saveFolderString = new File(exportFilePath).getParent();
			exportFilePath = saveFolderString + "/" + FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
			if (ExcelUtil.isDefinedCellNameExistInExcel(new File(exportFilePath), cellNames))
				testRst = true;
		}
		catch (RuntimeException e)
		{
			// e.printStackTrace();
		}
		finally
		{
			closeFormInstance();
			writeTestResultToFile(caseID, testRst, "Utility");
		}

	}

}
