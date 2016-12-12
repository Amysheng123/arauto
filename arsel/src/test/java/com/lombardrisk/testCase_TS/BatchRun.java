package com.lombardrisk.testCase_TS;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;

/**
 * Created by Leo Tu on 11/29/2016. Below test cases must be executed in machine
 * installed AR server
 */
public class BatchRun extends TestTemplate
{
	private String executeBatchRun(String jsonFile, String date) throws Exception
	{
		return executeBatchRun(jsonFile, "admin", "5dfc52b51bd35553df8592078de921bc", date);
	}

	private String executeBatchRun(String jsonFile, String userName, String password, String date) throws Exception
	{
		List<String> testData = getElementValueFromXML(testData_BatchRun, "executeBatchRun");
		String path = testData.get(0) + "/tools/etl-cmd/";
		String fullPath = path + "startBatchRun.bat";
		String jsonPath = System.getProperty("user.dir") + "/" + testDataFolderName + "/BatchRun/JsonFile/" + jsonFile;
		FileUtils.copyFile(new File(jsonPath), new File(path + jsonFile));
		logger.info("Json file is:" + jsonFile);
		Process process = Runtime.getRuntime().exec("cmd.exe /c start /b " + fullPath + " " + userName + " " + password + " " + jsonFile + " " + date, null, new File(path));
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line, status = null;
		while ((line = reader.readLine()) != null)
		{
			if (line.startsWith("The Etl job starts successfully"))
			{
				status = "success";
				break;
			}
			else if (line.startsWith("The username or password does not match."))
			{
				status = "fail_userIssue";
				break;
			}

			else
				status = "";
		}
		reader.close();
		return status;
	}

	public void waitJob(JobManagerPage jobManagerPage) throws Exception
	{
		List<String> jobInfo;
		DateFormat df;
		if (format.equalsIgnoreCase("en_GB"))
			df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		else if (format.equalsIgnoreCase("en_US"))
			df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss E");
		else
			df = new SimpleDateFormat("yyyy-DD-dd HH:mm:ss E");
		Date initTime = df.parse(df.format(new Date()));
		while (true)
		{
			refreshPage();
			Thread.sleep(1000 * 50);
			jobInfo = jobManagerPage.getLatestJobInfo();
			String Status = jobInfo.get(8);
			Date startTime = df.parse(jobInfo.get(6));
			Date currentTime = df.parse(df.format(new Date()));
			long diff = (initTime.getTime() - startTime.getTime()) / 1000;
			if (!Status.equalsIgnoreCase("IN PROGRESS") && diff < 60)
				break;
			long min = (currentTime.getTime() - initTime.getTime()) / (1000 * 60);
			if (min > 120)
				break;
		}
		logger.info("Job completed!");
	}

	@Test
	public void test0001() throws Exception
	{
		String caseID = "6544";
		logger.info("====Test...[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String Type = testData.get(0);
			String Alias = testData.get(1);
			String Path = testData.get(2);

			String[] Aliases = Alias.split("#");
			String[] Paths = Path.split("#");

			ListPage listPage = super.m.listPage;
			PhysicalLocationPage physicalLocationPage = listPage.enterPhysicalLoaction();
			List<String> existedLocations = physicalLocationPage.getPhysicalLocation();
			for (String alias : Aliases)
			{
				if (existedLocations.contains(alias))
					physicalLocationPage.deleteExistLocation(alias);
			}
			for (int i = 0; i < Aliases.length; i++)
			{
				physicalLocationPage.addLocation(Type, null, Aliases[i], Paths[i]);
			}
			existedLocations = physicalLocationPage.getPhysicalLocation();
			for (String alias : Aliases)
			{
				assertThat(existedLocations).contains(alias);
			}
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6552() throws Exception
	{
		String caseID = "6552";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String T_Table = testData.get(2);
			String X_Table = testData.get(3);
			int DBIndex = Integer.parseInt(testData.get(4));
			String TestDataPath = testData.get(5);
			File oriFile = new File(TestDataPath + "/" + "Base");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String curDay = sdf.format(new Date());
			File newFile = new File(TestDataPath + "/" + curDay.split("-")[0] + "_" + curDay.split("-")[2]);
			if (!newFile.exists())
				FileUtils.copyFile(oriFile, newFile);

			String SQL = "TRUNCATE TABLE \"" + T_Table + "\"";
			DBQuery.update(DBIndex, SQL);
			SQL = "TRUNCATE TABLE \"" + X_Table + "\"";
			DBQuery.update(DBIndex, SQL);

			String status = executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			waitJob(jobManagerPage);
			SQL = "SELECT COUNT(*) FROM \"" + T_Table + "\"";
			int num = Integer.parseInt(DBQuery.queryRecord(DBIndex, SQL));
			assertThat(num).isPositive();

			SQL = "SELECT COUNT(*) FROM \"" + X_Table + "\"";
			num = Integer.parseInt(DBQuery.queryRecord(DBIndex, SQL));
			assertThat(num).isPositive();
			assertThat(status).isEqualTo("success");
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",6557,6499", testRst, "BatchRun");
		}
	}

	@Test
	public void test6609() throws Exception
	{
		String caseID = "6609";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String T_Table = testData.get(2);
			int DBIndex = Integer.parseInt(testData.get(3));

			String SQL = "TRUNCATE TABLE \"" + T_Table + "\"";
			DBQuery.update(DBIndex, SQL);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			waitJob(jobManagerPage);

			SQL = "SELECT COUNT(*) FROM \"" + T_Table + "\" WHERE \"HKWORKNUM\"=1234567.8";
			int num = Integer.parseInt(DBQuery.queryRecord(DBIndex, SQL));
			assertThat(num).isPositive();
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6762() throws Exception
	{
		String caseID = "6762";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String Type = testData.get(0);
			String Alias = testData.get(1);
			String Path = testData.get(2);
			String ExpectMessage = testData.get(3);

			ListPage listPage = super.m.listPage;
			PhysicalLocationPage physicalLocationPage = listPage.enterPhysicalLoaction();
			String message = physicalLocationPage.addLocation(Type, null, Alias, Path);
			assertThat(message).isEqualTo(ExpectMessage);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6492() throws Exception
	{
		String caseID = "6492";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFiles = testData.get(0);
			String ReferenceDates = testData.get(1);

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(ReferenceDates.split("#")[0]);
			DateFormat sdf2 = new SimpleDateFormat("dd/MM/yyy");
			String date1 = sdf2.format(date);

			sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(ReferenceDates.split("#")[1]);
			sdf2 = new SimpleDateFormat("dd/MM/yyy");
			String date2 = sdf2.format(date);

			for (int i = 0; i < JsonFiles.split("#").length; i++)
			{
				executeBatchRun(JsonFiles.split("#")[i], ReferenceDates.split("#")[i]);
			}

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			List<String> jobInfo1 = jobManagerPage.getJobInfo(1);
			List<String> jobInfo2 = jobManagerPage.getJobInfo(2);
			assertThat(jobInfo1.get(0)).isEqualTo("KM");
			assertThat(jobInfo2.get(0)).isEqualTo("MAS1105_Product");
			assertThat(jobInfo1.get(4)).isEqualTo(date2);
			assertThat(jobInfo2.get(4)).isEqualTo(date1);
			assertThat(jobInfo1.get(8)).isEqualTo("IN PROGRESS");
			assertThat(jobInfo2.get(8)).isEqualTo("IN PROGRESS");
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6493() throws Exception
	{
		String caseID = "6493";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFiles = testData.get(0);
			String ReferenceDates = testData.get(1);

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(ReferenceDates.split("#")[0]);
			DateFormat sdf2 = new SimpleDateFormat("dd/MM/yyy");
			String date1 = sdf2.format(date);

			sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(ReferenceDates.split("#")[1]);
			sdf2 = new SimpleDateFormat("dd/MM/yyy");
			String date2 = sdf2.format(date);

			for (int i = 0; i < JsonFiles.split("#").length; i++)
			{
				executeBatchRun(JsonFiles.split("#")[i], ReferenceDates.split("#")[i]);
			}

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			List<String> jobInfo1 = jobManagerPage.getJobInfo(1);
			List<String> jobInfo2 = jobManagerPage.getJobInfo(2);
			assertThat(jobInfo1.get(0)).isEqualTo("BatchRun1");
			assertThat(jobInfo2.get(0)).isEqualTo("BatchRun1");
			assertThat(jobInfo1.get(4)).isEqualTo(date2);
			assertThat(jobInfo2.get(4)).isEqualTo(date1);
			assertThat(jobInfo1.get(8)).isEqualTo("IN PROGRESS");
			assertThat(jobInfo2.get(8)).isEqualTo("IN PROGRESS");

			waitJob(jobManagerPage);
			jobInfo1 = jobManagerPage.getJobInfo(1);
			assertThat(jobInfo1.get(8)).isEqualTo("SUCCESS");

			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			String status = jobDetailsPage.getStatus();
			assertThat(status).isEqualTo("SUCCESS");
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test(dependsOnMethods =
	{ "test6552" })
	public void test6531() throws Exception
	{
		String caseID = "6531";
		logger.info("====Test...[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			List<String> jobInfo = jobManagerPage.getLatestJobInfo();
			assertThat(jobInfo.get(2)).isEqualTo("");

			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> columns = jobDetailsPage.getColumnNamesInList();
			assertThat("NAME").isIn(columns);
			assertThat("REFERENCE DATE").isIn(columns);

			List<String> columnCellText = jobDetailsPage.getColumnCellText(1, 4, true);
			assertThat(columnCellText).contains("EtlJob");
			assertThat(columnCellText).contains("BatchRun");
			assertThat(columnCellText).contains("Map");
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",6533,6535", testRst, "BatchRun");
		}
	}

	@Test
	public void test6546() throws Exception
	{
		String caseID = "6546";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> columnCellText = jobDetailsPage.getColumnCellText(1, 8, false);
			assertThat(columnCellText.get(0)).isEqualTo("IN PROGRESS");
			assertThat(columnCellText.get(1)).isEqualTo("IN PROGRESS");
			assertThat(columnCellText.get(2)).isEqualTo("WAITING");
			assertThat(columnCellText.get(3)).isEqualTo("WAITING");

			boolean link = jobDetailsPage.isBatchRunLinkExits(1, 2);
			assertThat(link).isEqualTo(false);

			while (true)
			{
				columnCellText = jobDetailsPage.getColumnCellText(1, 8, false);
				if (!columnCellText.get(2).equalsIgnoreCase("IN PROGRESS") && !columnCellText.get(2).equalsIgnoreCase("WAITING"))
					break;
				Thread.sleep(3000 * 10);
				refreshPage();
			}
			assertThat(columnCellText.get(2)).isEqualTo("SUCCESS");
			link = jobDetailsPage.isBatchRunLinkExits(1, 2);
			assertThat(link).isEqualTo(true);
			JobResultPage jobResultPage = jobDetailsPage.enterJobResultPage(1, 2);
			List<String> logs = jobResultPage.getLog();
			assertThat(logs.get(0)).isEqualTo("Start execute Batchrun Job:MASPREPRO");
			assertThat(logs.get(1)).isEqualTo("BatchRun Job Finished.");

			File logFile = new File(jobResultPage.exportLog());
			assertThat(logFile.exists()).isEqualTo(true);
			assertThat(logFile.getName().endsWith(".xlsx")).isEqualTo(true);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6547() throws Exception
	{
		String caseID = "6547";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			waitJob(jobManagerPage);

			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> columnCellText = jobDetailsPage.getColumnCellText(1, 4, false);
			assertThat(columnCellText).contains("EtlJob");
			assertThat(columnCellText).contains("Compute");

			JobResultPage jobResultPage = jobDetailsPage.enterJobResultPage(1, 1);
			List<String> logs = jobResultPage.getLog();
			assertThat(logs.get(0)).isEqualTo("Start execute compute Job.");
			assertThat(logs.get(1)).isEqualTo("Compute Job finished.");

			File logFile = new File(jobResultPage.exportLog());
			assertThat(logFile.exists()).isEqualTo(true);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6545() throws Exception
	{
		String caseID = "6545";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date processDate = df.parse(ReferenceDate);

			DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
			String process = df2.format(processDate);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			List<String> columnCellText = jobManagerPage.getLatestJobInfo();
			assertThat(columnCellText.get(0)).isEqualTo("MAS1105_Product");
			assertThat(columnCellText.get(4)).isEqualTo(process);
			assertThat(columnCellText.get(8)).isEqualTo("IN PROGRESS");

			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> batRunStatus = jobDetailsPage.getBatchRunStatus(1);
			int batchRunId = 0;
			for (int i = 0; i < batRunStatus.size(); i++)
			{
				if (batRunStatus.get(i).equalsIgnoreCase("IN PROGRESS"))
				{
					assertThat(batRunStatus.get(i + 1)).isEqualTo("WAITING");
					batchRunId = i + 1;
				}
			}
			List<String> mapStatus = jobDetailsPage.getMapStatus(1, batchRunId);
			for (String status : mapStatus)
			{
				assertThat(status).isEqualTo("WAITING");
			}
			while (!batRunStatus.get(0).equalsIgnoreCase("SUCCESS"))
			{
				Thread.sleep(3000 * 10);
				refreshPage();
				batRunStatus = jobDetailsPage.getBatchRunStatus(1);
			}
			JobResultPage jobResultPage = jobDetailsPage.enterJobResultPage(1, 1);
			List<String> jobInfo = jobResultPage.getJobDetail();
			assertThat(jobInfo.get(3)).isEqualTo("SUCCESS");
			assertThat(jobInfo.get(4)).isEqualTo("MASMAPEDM");
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6542() throws Exception
	{
		String caseID = "6542";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			executeBatchRun(JsonFile, ReferenceDate);
			waitJob(jobManagerPage);
			List<String> jobInfo = jobManagerPage.getLatestJobInfo();
			assertThat(jobInfo.get(8)).isEqualTo("FAILURE");
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> batRunStatus = jobDetailsPage.getBatchRunStatus(1);
			assertThat(batRunStatus.get(0)).isEqualTo("SUCCESS");
			assertThat(batRunStatus.get(1)).isEqualTo("FAILURE");
			assertThat(batRunStatus.get(2)).isEqualTo("NO RUN");
			assertThat(batRunStatus.get(3)).isEqualTo("NO RUN");

			List<String> messages = jobDetailsPage.getMapMessage(1, 2);
			assertThat(messages.get(1)).isEqualTo("Append map rule place holder missing for Append map : LOOKUPTB_MISSING");

			JobResultPage jobResultPage = jobDetailsPage.enterJobResultPage(1, 2);
			jobInfo = jobResultPage.getJobDetail();
			assertThat(jobInfo.get(3)).isEqualTo("FAILED");

			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6543() throws Exception
	{
		String caseID = "6543";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			executeBatchRun(JsonFile, ReferenceDate);
			waitJob(jobManagerPage);
			List<String> jobInfo = jobManagerPage.getLatestJobInfo();
			assertThat(jobInfo.get(8)).isEqualTo("FAILURE");
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> batRunStatus = jobDetailsPage.getBatchRunStatus(1);
			assertThat(batRunStatus.get(0)).isEqualTo("SUCCESS");
			assertThat(batRunStatus.get(1)).isEqualTo("FAILURE");
			assertThat(batRunStatus.get(2)).isEqualTo("SUCCESS");
			assertThat(batRunStatus.get(3)).isEqualTo("SUCCESS");

			List<String> messages = jobDetailsPage.getMapMessage(1, 2);
			assertThat(messages.get(1)).isEqualTo("Append map rule place holder missing for Append map : LOOKUPTB_MISSING");

			JobResultPage jobResultPage = jobDetailsPage.enterJobResultPage(1, 2);
			jobInfo = jobResultPage.getJobDetail();
			assertThat(jobInfo.get(3)).isEqualTo("FAILED");

			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6548() throws Exception
	{
		String caseID = "6548";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> status = jobDetailsPage.getMapStatus(1, 2);
			while (!status.get(0).equalsIgnoreCase("IN PROGRESS") && !status.get(0).equalsIgnoreCase("STOPPING"))
			{
				Thread.sleep(1000 * 15);
				status = jobDetailsPage.getMapStatus(1, 2);
			}
			jobDetailsPage.stopJob(1);

			jobManagerPage = jobDetailsPage.backToJobManager();
			waitJob(jobManagerPage);
			List<String> jobInfo = jobManagerPage.getLatestJobInfo();
			assertThat(jobInfo.get(8)).isEqualTo("STOPPED");
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6549() throws Exception
	{
		String caseID = "6549";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			jobDetailsPage.stopJob(1);
			jobManagerPage = jobDetailsPage.backToJobManager();
			waitJob(jobManagerPage);
			List<String> jobInfo = jobManagerPage.getLatestJobInfo();
			assertThat(jobInfo.get(8)).isEqualTo("STOPPED");
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6551() throws Exception
	{
		String caseID = "6551";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			List<String> jobInfo = jobManagerPage.getLatestJobInfo();
			assertThat(jobInfo.get(11)).isEqualTo("0");
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> progresses = jobDetailsPage.getBatchRunProgress(1);
			int progress = Integer.parseInt(progresses.get(0));
			if (progress < 100)
			{
				assertThat(progresses.get(1)).isEqualTo("0");
				assertThat(progresses.get(2)).isEqualTo("0");
				assertThat(progresses.get(3)).isEqualTo("0");
			}

			progress = Integer.parseInt(progresses.get(2));
			if (progress < 100)
				assertThat(progresses.get(3)).isEqualTo("0");

			jobManagerPage = jobDetailsPage.backToJobManager();
			waitJob(jobManagerPage);
			jobInfo = jobManagerPage.getLatestJobInfo();
			assertThat(jobInfo.get(11)).isEqualTo("100");

			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6601() throws Exception
	{
		String caseID = "6601";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		List<String> Files = createFolderAndCopyFile("BatchRun", null);
		String testDataFolder = Files.get(0);
		String checkCellFileFolder = Files.get(1);
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String Regulator = testData.get(2);
			String Entity = testData.get(3);
			String Form = testData.get(4);
			String ReferenceDate2 = testData.get(5);
			String CheckCellValue = testData.get(6);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			waitJob(jobManagerPage);
			jobManagerPage.backToDashboard();
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate2);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			String source = testDataFolder + CheckCellValue;
			String dest = checkCellFileFolder + CheckCellValue;

			File expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			boolean rst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
			assertThat(rst).isEqualTo(true);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",6746", testRst, "BatchRun");
		}
	}

	@Test
	public void test6602() throws Exception
	{
		String caseID = "6602";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		List<String> Files = createFolderAndCopyFile("BatchRun", null);
		String testDataFolder = Files.get(0);
		String checkCellFileFolder = Files.get(1);
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String Regulator = testData.get(2);
			String Entity = testData.get(3);
			String Form = testData.get(4);
			String ReferenceDate2 = testData.get(5);
			String CheckCellValue = testData.get(6);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			waitJob(jobManagerPage);
			jobManagerPage.backToDashboard();
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate2);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			String source = testDataFolder + CheckCellValue;
			String dest = checkCellFileFolder + CheckCellValue;

			File expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			boolean rst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
			assertThat(rst).isEqualTo(true);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6603() throws Exception
	{
		String caseID = "6603";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		List<String> Files = createFolderAndCopyFile("BatchRun", null);
		String testDataFolder = Files.get(0);
		String checkCellFileFolder = Files.get(1);
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String Regulator = testData.get(2);
			String Entity = testData.get(3);
			String Form = testData.get(4);
			String ReferenceDate2 = testData.get(5);
			String CheckCellValue = testData.get(6);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			waitJob(jobManagerPage);
			jobManagerPage.backToDashboard();
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate2);
			FormInstancePage formInstancePage = listPage.openFirstFormInstance();
			String source = testDataFolder + CheckCellValue;
			String dest = checkCellFileFolder + CheckCellValue;

			File expectedValueFile = new File(dest);
			if (!expectedValueFile.isDirectory())
			{
				if (expectedValueFile.exists())
					expectedValueFile.delete();
				FileUtils.copyFile(new File(source), expectedValueFile);
			}
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			boolean rst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
			assertThat(rst).isEqualTo(true);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6608() throws Exception
	{
		String caseID = "6608";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String status = testData.get(2);
			String StatusMessage = testData.get(3);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			waitJob(jobManagerPage);
			List<String> jobInfo = jobManagerPage.getLatestJobInfo();
			assertThat(jobInfo.get(8).equalsIgnoreCase(status));
			assertThat(jobInfo.get(10).equalsIgnoreCase(StatusMessage));
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6610() throws Exception
	{
		String caseID = "6610";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			executeBatchRun(JsonFile, ReferenceDate);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			List<String> jobInfo = jobManagerPage.getLatestJobInfo();
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			if ("IN PROGRESS".equalsIgnoreCase(jobInfo.get(8)))
			{
				assertThat(jobDetailsPage.isDeleteIconExits(1)).isEqualTo(false);
				jobManagerPage = jobDetailsPage.backToJobManager();
				waitJob(jobManagerPage);
				jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
				assertThat(jobDetailsPage.isDeleteIconExits(1)).isEqualTo(true);
			}
			else
				assertThat(jobDetailsPage.isDeleteIconExits(1)).isEqualTo(true);

			int init = jobDetailsPage.getJobNums();
			jobDetailsPage.deleteJob(1);
			int after = jobDetailsPage.getJobNums();
			assertThat(after - init).isEqualTo(1);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6618() throws Exception
	{
		String caseID = "6618";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String Items = testData.get(2);
			String Job1 = testData.get(3);
			String Job2 = testData.get(4);

			ListPage listPage = m.listPage;
			for (String item : Items.split("#"))
			{
				String[] i = item.split("_");
				String Regulator = i[0];
				String Entity = i[1];
				String Form = i[2];
				String Date = i[3];
				listPage.setRegulatorByValue(Regulator);
				listPage.setGroup(Entity);
				listPage.setForm(Form);
				listPage.setProcessDate(Date);
				listPage.deleteFormInstance(Form, Date);
			}
			executeBatchRun(JsonFile, ReferenceDate);

			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> subJobNames = jobDetailsPage.getSubJobName(1);
			assertThat(Job1).isIn(subJobNames);
			assertThat(Job2).isIn(subJobNames);
			jobDetailsPage.backToDashboard();
			waitJob(jobManagerPage);

			listPage = jobManagerPage.backToDashboard();
			for (String item : Items.split("#"))
			{
				String[] i = item.split("_");
				String Regulator = i[0];
				String Entity = i[1];
				String Form = i[2];
				String Date = i[2];
				listPage.setRegulatorByValue(Regulator);
				listPage.setGroup(Entity);
				listPage.setForm(Form);
				listPage.setProcessDate(Date);
				assertThat(listPage.isFormExist(Form, Date)).isEqualTo(true);
			}

			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6740() throws Exception
	{
		String caseID = "6740";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String ExecuteJob = testData.get(0);
			String Items = testData.get(1);
			String Job1 = testData.get(2);
			String Job2 = testData.get(3);

			ListPage listPage = m.listPage;
			for (String item : Items.split("#"))
			{
				String[] i = item.split("_");
				String Regulator = i[0];
				String Entity = i[1];
				String Form = i[2];
				String Date = i[3];
				listPage.setRegulatorByValue(Regulator);
				listPage.setGroup(Entity);
				listPage.setForm(Form);
				// listPage.setProcessDate(Date);
				listPage.deleteFormInstance(Form, Date);
			}

			for (String jobDetail : ExecuteJob.split("#"))
			{
				String JsonFile = jobDetail.split("@")[0];
				String ReferenceDate = jobDetail.split("@")[1];
				executeBatchRun(JsonFile, ReferenceDate);
			}

			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			while (!"IN PROGRESS".equalsIgnoreCase(jobManagerPage.getJobInfo(1).get(8)) && !"IN PROGRESS".equalsIgnoreCase(jobManagerPage.getJobInfo(2).get(8)))
			{
				Thread.sleep(1000 * 30);
				refreshPage();
			}

			listPage = jobManagerPage.backToDashboard();
			for (String item : Items.split("#"))
			{
				String[] i = item.split("_");
				String Regulator = i[0];
				String Entity = i[1];
				String Form = i[2];
				String Date = i[2];
				listPage.setRegulatorByValue(Regulator);
				listPage.setGroup(Entity);
				listPage.setForm(Form);
				listPage.setProcessDate(Date);
				assertThat(listPage.isFormExist(Form, Date)).isEqualTo(true);
			}

			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6776() throws Exception
	{
		String caseID = "6776";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String Items = testData.get(2);
			String Job1 = testData.get(3);
			String Job2 = testData.get(4);

			ListPage listPage = m.listPage;
			for (String item : Items.split("#"))
			{
				String[] i = item.split("_");
				String Regulator = i[0];
				String Entity = i[1];
				String Form = i[2];
				String Date = i[3];
				listPage.setRegulatorByValue(Regulator);
				listPage.setGroup(Entity);
				listPage.setForm(Form);
				listPage.setProcessDate(Date);
				listPage.deleteFormInstance(Form, Date);
			}
			executeBatchRun(JsonFile, ReferenceDate);

			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			JobDetailsPage jobDetailsPage = jobManagerPage.enterJobDetailsPage(1);
			List<String> subJobNames = jobDetailsPage.getSubJobName(1);
			assertThat(Job1).isIn(subJobNames);
			assertThat(Job2).isIn(subJobNames);
			jobDetailsPage.backToDashboard();
			waitJob(jobManagerPage);

			listPage = jobManagerPage.backToDashboard();
			for (String item : Items.split("#"))
			{
				String[] i = item.split("_");
				String Regulator = i[0];
				String Entity = i[1];
				String Form = i[2];
				String Date = i[2];
				listPage.setRegulatorByValue(Regulator);
				listPage.setGroup(Entity);
				listPage.setForm(Form);
				listPage.setProcessDate(Date);
				assertThat(listPage.isFormExist(Form, Date)).isEqualTo(true);
			}

			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "BatchRun");
		}
	}

	@Test
	public void test6526() throws Exception
	{
		String caseID = "6526";
		boolean testRst = false;
		logger.info("====Test...[case id=" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testData_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);

			ListPage listPage = m.listPage;
			JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
			int init = jobManagerPage.getJobNum();
			executeBatchRun(JsonFile, "admin", "password", ReferenceDate);
			Thread.sleep(1000 * 10);
			refreshPage();
			int after = jobManagerPage.getJobNum();
			assertThat(after).isEqualTo(init);

			executeBatchRun(JsonFile, "adminadmin", "5dfc52b51bd35553df8592078de921bc", ReferenceDate);
			Thread.sleep(1000 * 10);
			refreshPage();
			after = jobManagerPage.getJobNum();
			assertThat(after).isEqualTo(init);
			testRst = true;
		}
		catch (Exception e)
		{
			testRst = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",6528", testRst, "BatchRun");
		}
	}
}
