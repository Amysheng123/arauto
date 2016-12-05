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

import com.lombardrisk.pages.JobDetailsPage;
import com.lombardrisk.pages.JobManagerPage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.pages.PhysicalLocationPage;
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
		String status = null;
		List<String> testData = getElementValueFromXML(testdata_BatchRun, "executeBatchRun");
		String path = testData.get(0) + "\\tools\\etl-cmd\\";
		String fullPath = path + "startBatchRun.bat";
		String jsonPath = System.getProperty("user.dir") + "\\" + testDataFolderName + "\\BatchRun\\JsonFile\\" + jsonFile;
		FileUtils.copyFile(new File(jsonPath), new File(path + jsonFile));
		Process process = Runtime.getRuntime().exec("cmd.exe /c start /b " + fullPath + " " + "admin" + " " + "5dfc52b51bd35553df8592078de921bc" + " " + jsonFile + " " + date, null, new File(path));
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			if (line.contains("is already in progress"))
			{
				status = "running";
				break;
			}
			if (line.contains("The Etl job starts successfully"))
			{
				status = "success";
				break;
			}
		}
		reader.close();
		return status;
	}

	public void waitJob(JobManagerPage jobManagerPage) throws Exception
	{
		List<String> jobInfo = jobManagerPage.getLatestJobInfo();
		String Status = jobInfo.get(8);
		while (Status.equalsIgnoreCase("IN PROGRESS"))
		{
			Thread.sleep(1000 * 30);
			refreshPage();
			jobInfo = jobManagerPage.getLatestJobInfo();
			Status = jobInfo.get(8);
		}
	}

	@Test
	public void test6544() throws Exception
	{
		String caseID = "6544";
		logger.info("====Test...[case id=" + caseID + "]====");
		boolean testRst = false;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testData = getElementValueFromXML(testdata_BatchRun, nodeName);
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
			List<String> testData = getElementValueFromXML(testdata_BatchRun, nodeName);
			String JsonFile = testData.get(0);
			String ReferenceDate = testData.get(1);
			String T_Table = testData.get(2);
			String X_Table = testData.get(3);
			int DBIndex = Integer.parseInt(testData.get(4));
			String TestDataPath = testData.get(5);
			File oriFile = new File(TestDataPath + "\\" + "Base");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String curDay = sdf.format(new Date());
			File newFile = new File(TestDataPath + "\\" + curDay.split("-")[0] + "_" + curDay.split("-")[2]);
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
			List<String> testData = getElementValueFromXML(testdata_BatchRun, nodeName);
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
			List<String> testData = getElementValueFromXML(testdata_BatchRun, nodeName);
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
			List<String> testData = getElementValueFromXML(testdata_BatchRun, nodeName);
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
			List<String> testData = getElementValueFromXML(testdata_BatchRun, nodeName);
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
}
