package com.lombardrisk.testCase_TS;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.HomePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.Business;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.FileUtil;

public class ExportForm_External extends TestTemplate
{

	@Test
	public void test6586() throws Exception
	{
		String caseID = "6586";
		logger.info("====Verify entity variables values from EXTERNAL metadata can be transmitted to Vanilla successfully[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			String Framework = testData.get(4);
			String Taxonomy = testData.get(5);
			String Module = testData.get(6);
			String BaseLine = testData.get(7);
			int DBIndex = Integer.parseInt(testData.get(8));

			logger.info("Set type= Consolidated");
			String FileType = "Vanilla";
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);
			String prefix = getToolsetRegPrefix(Regulator);
			String tableName = prefix + "uFormVars";
			String SQL = "UPDATE \"" + tableName + "\" SET \"CCValue\"='P' WHERE \"EntityName\"=" + Entity + "  and \"CCName\"='Group_Consolidation_Type'";
			DBQuery.update(DBIndex, SQL);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.closeFormInstance();
			String exportFilePath = listPage.ExportToRegulatorFormat(Entity, Form, ReferenceDate, FileType, Framework, Taxonomy, Module, null);
			String BaselineFile = System.getProperty("user.dir") + "/" + testDataFolderName + "/ExportForm_External/CheckCellValue/" + BaseLine;
			Boolean rst = Business.verifyExportedFile(BaselineFile, exportFilePath, FileType);
			assertThat(rst).as("Exist difference between exported file and baseline").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID + ",6587,", testRst, "Export_External");
		}
	}

	@Test
	public void test6591() throws Exception
	{
		String caseID = "6591";
		logger.info("====Verify entity variables values from EXTERNAL metadata can be transmitted to Vanilla successfully[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			// String Framework = testData.get(4);
			// String Taxonomy = testData.get(5);
			String Module = testData.get(6);
			String BaseLine = testData.get(7);

			String FileType = "Vanilla";
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			int DBIndex = Integer.parseInt(testData.get(8));
			String prefix = getToolsetRegPrefix(Regulator);
			String tableName = prefix + "uFormVars";
			String SQL = "UPDATE \"" + tableName + "\" SET \"CCValue\"='P' WHERE \"EntityName\"=" + Entity + "  and \"CCName\"='Group_Consolidation_Type'";
			DBQuery.update(DBIndex, SQL);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			String exportFilePath = formInstancePage.exportToFile(Entity, Form, ReferenceDate, FileType, Module, null);
			String BaselineFile = System.getProperty("user.dir") + "/" + testDataFolderName + "/ExportForm_External/CheckCellValue/" + BaseLine;
			Boolean rst = Business.verifyExportedFile(BaselineFile, exportFilePath, FileType);
			assertThat(rst).as("Exist difference between exported file and baseline").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID + ",6592,", testRst, "Export_External");
		}
	}

	@Test
	public void test6697() throws Exception
	{
		String caseID = "6697";
		logger.info("====Verify User can not do transmit Vanilla without privilege in toolset envrionment[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			boolean rst = listPage.isExportToVanillaDisplayed();
			assertThat(rst).as("Should not display Export To Vanilla option").isEqualTo(false);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			rst = formInstancePage.isExistExportToVanilla();
			assertThat(rst).as("Should not display Export To Vanilla option").isEqualTo(false);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Export_External");
		}
	}

	@Test
	public void test6695() throws Exception
	{
		String caseID = "6695";
		logger.info("==== Verify Toolset(external metadata) Support transmit  Arbitrary successfully - Dashboard entry[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			List<String> Files = createFolderAndCopyFile("ExportForm_External", null);
			String testDataFolder = Files.get(0);
			String checkRstFolder = Files.get(1);
			String importFolder = Files.get(3);

			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			String ImportFileName = testData.get(4);
			String Module = testData.get(5);
			String CheckCellValueFile = testData.get(6);
			int DBIndex = Integer.parseInt(testData.get(7));

			File importFile = new File(importFolder + ImportFileName);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			listPage.createFormFromExcel(importFile, false, false, false);
			String SQL = "update [MASStat] set [Monetary_Scale]=3 where ReturnId=310002 and EntityId=1 and Process_Date='2015-12-31'";
			DBQuery.update(DBIndex, SQL);
			String exportFilePath = listPage.ExportToRegulatorFormat(Entity, Form, ReferenceDate, "iFile", null, null, Module, null);
			if (exportFilePath.endsWith(".zip"))
			{
				String saveFolderString = new File(exportFilePath).getParent();
				exportFilePath = saveFolderString + "/" + FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
			}
			File expectedValueFile = new File(checkRstFolder + CheckCellValueFile);
			if (expectedValueFile.exists())
			{
				expectedValueFile.delete();
			}
			logger.info("Begin check cell value in exported file");
			FileUtils.copyFile(new File(testDataFolder + CheckCellValueFile), expectedValueFile);
			boolean rst = Business.verifyExportedFile(expectedValueFile.getAbsolutePath(), exportFilePath, "iFile");

			assertThat(rst).as("Exported value should same with expected value").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Export_External");
		}
	}

	@Test
	public void test6691() throws Exception
	{
		String caseID = "6691";
		logger.info("==== Verify Toolset(external metadata) Support transmit  Arbitrary successfully - Form page entry[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			List<String> Files = createFolderAndCopyFile("ExportForm_External", null);
			String testDataFolder = Files.get(0);
			String checkRstFolder = Files.get(1);
			String importFolder = Files.get(3);

			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			String ImportFileName = testData.get(4);
			String Module = testData.get(5);
			String CheckCellValueFile = testData.get(6);
			int DBIndex = Integer.parseInt(testData.get(7));

			File importFile = new File(importFolder + ImportFileName);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			String SQL = "update [MASStat] set [Monetary_Scale]=3 where ReturnId=310030 and EntityId=1 and Process_Date='2015-12-31'";
			DBQuery.update(DBIndex, SQL);
			String exportFilePath = formInstancePage.exportToFile(Entity, Form, ReferenceDate, "iFile", Module, null);
			if (exportFilePath.endsWith(".zip"))
			{
				String saveFolderString = new File(exportFilePath).getParent();
				exportFilePath = saveFolderString + "/" + FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
			}
			File expectedValueFile = new File(checkRstFolder + CheckCellValueFile);
			if (expectedValueFile.exists())
				expectedValueFile.delete();
			logger.info("Begin check cell value in exported file");
			FileUtils.copyFile(new File(testDataFolder + CheckCellValueFile), expectedValueFile);
			boolean rst = Business.verifyExportedFile(expectedValueFile.getAbsolutePath(), exportFilePath, "iFile");
			assertThat(rst).as("Exported value should same with expected value").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Export_External");
		}
	}

	@Test
	public void test6696() throws Exception
	{
		String caseID = "6696";
		logger.info("==== Verify User can not do transmit XSLT or Arbitrary without privilege in toolset envrionment[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			String User = testData.get(4);

			ListPage listPage = super.m.listPage;
			HomePage homePage = listPage.logout();
			homePage.loginAs(User, "password");
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			List<String> options = listPage.getExportToRegOptions();
			assertThat(options.size()).isEqualTo(1);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			options = formInstancePage.getExportToRegOptions();
			assertThat(options.size()).isEqualTo(1);
			assertThat(options.get(0)).isEqualTo("Export To Vanilla");
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Export_External");
		}
	}

	@Test
	public void test6532() throws Exception
	{
		String caseID = "6532";
		logger.info("====Verify XBRL can be generated correctly when using REPORTER metadata-dashboard[case id=" + caseID + "]====");
		boolean testRst = false;
		List<String> Files = createFolderAndCopyFile("ExportForm_External", null);
		String testDataFolder = Files.get(0);
		// String checkRstFolder = Files.get(1);
		String importFolder = Files.get(3);
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			String createFile = testData.get(4);
			String Framework = testData.get(5);
			String Taxonomy = testData.get(6);
			String Module = testData.get(7);
			String BaseLine = testData.get(8);
			int DBIndex = Integer.parseInt(testData.get(9));
			String ConsolidationType = testData.get(10);

			logger.info("Set type= Consolidated");
			String FileType = "XBRL";
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			String prefix = getToolsetRegPrefix(Regulator);
			String tableName = prefix + "uFormVars";
			String SQL = "UPDATE \"" + tableName + "\" SET \"CCValue\"='" + ConsolidationType.toUpperCase() + "' WHERE \"EntityName\"=" + Entity + "  and \"CCName\"='Group_Consolidation_Type'";
			DBQuery.update(DBIndex, SQL);

			for (String fileName : createFile.split("#"))
			{
				File importFile = new File(importFolder + fileName);
				listPage.createFormFromExcel(importFile, false, false, false);
			}
			String exportFilePath = listPage.ExportToRegulatorFormat(Entity, Form, ReferenceDate, FileType, Framework, Taxonomy, Module, null);
			String BaselineFile = testDataFolder + BaseLine;
			Boolean rst = Business.verifyExportedFile(BaselineFile, exportFilePath, FileType);
			assertThat(rst).as("Exist difference between exported file and baseline").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Export_External");
		}
	}

	@Test
	public void test6567() throws Exception
	{
		String caseID = "6567";
		logger.info("====Verify XBRL can be generated correctly when using REPORTER metadata-return review page[case id=" + caseID + "]====");
		boolean testRst = false;
		List<String> Files = createFolderAndCopyFile("ExportForm_External", null);
		String testDataFolder = Files.get(0);
		// String checkRstFolder = Files.get(1);
		String importFolder = Files.get(3);
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String Forms = testData.get(3);
			String ReferenceDate = testData.get(4);
			String createFile = testData.get(5);
			String Module = testData.get(6);
			String BaseLine = testData.get(7);
			int DBIndex = Integer.parseInt(testData.get(8));
			String ConsolidationType = testData.get(9);

			logger.info("Set type= Consolidated");
			String FileType = "XBRL";
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			String prefix = getToolsetRegPrefix(Regulator);
			String tableName = prefix + "uFormVars";
			String SQL = "UPDATE \"" + tableName + "\" SET \"CCValue\"='" + ConsolidationType.toUpperCase() + "' WHERE \"EntityName\"=" + Entity + "  and \"CCName\"='Group_Consolidation_Type'";
			DBQuery.update(DBIndex, SQL);

			for (String fileName : createFile.split("#"))
			{
				File importFile = new File(importFolder + fileName);
				listPage.createFormFromExcel(importFile, false, false, false);
			}

			listPage.setForm(Form);
			listPage.setProcessDate(ReferenceDate);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			String exportFilePath = formInstancePage.exportToFile(Entity, Forms, ReferenceDate, FileType, Module, null);
			String BaselineFile = testDataFolder + BaseLine;
			Boolean rst = Business.verifyExportedFile(BaselineFile, exportFilePath, FileType);
			assertThat(rst).as("Exist difference between exported file and baseline").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Export_External");
		}
	}

	@Test
	public void test6568() throws Exception
	{
		String caseID = "6568";
		logger.info("====Verify return not in list of Export To XBRL when delete from ECRuFormVals[case id=" + caseID + "]====");
		boolean testRst = false;
		int DBIndex = 0;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			String Framework = testData.get(4);
			String Taxonomy = testData.get(5);
			String Module = testData.get(6);
			DBIndex = Integer.parseInt(testData.get(7));
			String ConsolidationType = testData.get(8);

			logger.info("Set type= Consolidated");
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			String prefix = getToolsetRegPrefix(Regulator);
			String tableName = prefix + "uFormVars";
			String SQL = "UPDATE \"" + tableName + "\" SET \"CCValue\"='" + ConsolidationType.toUpperCase() + "' WHERE \"EntityName\"=" + Entity + "  and \"CCName\"='Group_Consolidation_Type'";
			DBQuery.update(DBIndex, SQL);
			SQL = "delete from \"" + tableName + "\" where \"EntityName\"=2999 and \"ReturnId\"=360168";
			DBQuery.update(DBIndex, SQL);
			List<String> AllForms = listPage.getExistedFormsInExportXBRL(Entity, ReferenceDate, Framework, Taxonomy, Module);
			assertThat(Form).as("Exist difference between exported file and baseline").isNotIn(AllForms);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			String SQL = "insert into \"ECRuFormVars\"" + "(\"EntityName\", \"ReturnId\", \"CCName\")" + "values" + "('2999', '360168', '<Default>')";
			DBQuery.update(DBIndex, SQL);
			writeTestResultToFile(caseID + ",6491", testRst, "Export_External");
		}
	}

	@Test
	public void test6688() throws Exception
	{
		String caseID = "6688";
		logger.info("==== Verify Toolset(external metadata) Support transmit  XSLT successfully - Dashboard entry[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			List<String> Files = createFolderAndCopyFile("ExportForm_External", null);
			String testDataFolder = Files.get(0);
			// String checkRstFolder = Files.get(1);
			String importFolder = Files.get(3);

			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_Export_External, nodeName);
			String Regulator = testData.get(0);
			String Entity = testData.get(1);
			String Form = testData.get(2);
			String ReferenceDate = testData.get(3);
			String ImportFileName = testData.get(4);
			String ExportType = testData.get(5);
			String Module = testData.get(6);
			String BaselineName = testData.get(7);

			File importFile = new File(importFolder + ImportFileName);
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createFormFromExcel(importFile, false, false, true);
			String exportFilePath = formInstancePage.exportToFile(Entity, Form, ReferenceDate, ExportType, Module, null);
			if (exportFilePath.endsWith(".zip"))
			{
				String saveFolderString = new File(exportFilePath).getParent();
				exportFilePath = saveFolderString + "/" + FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
			}
			String baseline = testDataFolder + BaselineName;
			boolean rst = Business.verifyExportedFile(baseline, exportFilePath, ExportType);
			assertThat(rst).as("Exported value should same with expected value").isEqualTo(true);
			formInstancePage.closeFormInstance();
			exportFilePath = listPage.ExportToRegulatorFormat(Entity, Form, ReferenceDate, ExportType, null, null, Module, null);
			if (exportFilePath.endsWith(".zip"))
			{
				String saveFolderString = new File(exportFilePath).getParent();
				exportFilePath = saveFolderString + "/" + FileUtil.unCompress(exportFilePath, saveFolderString).get(0);
			}
			rst = Business.verifyExportedFile(baseline, exportFilePath, ExportType);
			assertThat(rst).as("Exported value should same with expected value").isEqualTo(true);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error("error", e);
		}
		finally
		{
			writeTestResultToFile(caseID + ",6694", testRst, "Export_External");
		}
	}
}
