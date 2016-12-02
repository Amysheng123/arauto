package com.lombardrisk.testCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.Business;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;

/**
 * Create by Leo Tu on 5/20/2016
 */

public class DataSchedule extends TestTemplate
{
	@Parameters(
	{ "fileName" })
	@Test
	public void DsReturnRetrieve(@Optional String fileName) throws Exception
	{
		logger.info("File is: " + fileName);
		if (fileName == null || fileName.equals(""))
			fileName = "DSRetrieve.xls";
		List<String> Files = createFolderAndCopyFile("DataSchedule", fileName);
		String testDataFolder = Files.get(0);
		String checkCellFileFolder = Files.get(1);

		if (!testRstFile.getName().equalsIgnoreCase(fileName))
			testRstFile = new File(testRstFile.getParent() + fileName);

		logger.info("============testDSRetrieve=============");
		ListPage listPage = super.m.listPage;
		boolean testRst = true;
		File testDataFile = new File(testDataFolderName + "\\DataSchedule\\" + fileName);
		int rouNums = ExcelUtil.getRowNums(testDataFile, null);
		for (int index = 1; index <= rouNums; index++)
		{
			ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
			int ID = Integer.parseInt(rowValue.get(0).trim());
			String Regulator = rowValue.get(1).trim();
			String Group = rowValue.get(2).trim();
			String Form = rowValue.get(3).trim();
			String ProcessDate = rowValue.get(4).trim();
			if (ProcessDate.equals(""))
				ProcessDate = null;
			String Run = rowValue.get(5).trim();
			boolean DeleteExistForm = false;
			if (rowValue.get(6).trim().equals("Y"))
				DeleteExistForm = true;

			String jobStatus = rowValue.get(7).trim();
			String CheckCellValueFile = rowValue.get(8).trim();
			String Update_New = rowValue.get(9).trim();
			String formStatus = rowValue.get(10).trim();
			String action = rowValue.get(11).trim();
			String ErrorMessage = rowValue.get(12).trim();
			String CaseID = rowValue.get(14).trim();

			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);

			if (Run.equalsIgnoreCase("Y"))
			{
				logger.info("Case id is:" + CaseID);
				try
				{
					listPage.getProductListPage(Regulator, Group, Form, null);

					if (CheckCellValueFile.endsWith(".xlsx") && Update_New.length() == 0)
					{
						if (DeleteExistForm)
						{
							// listPage.setProcessDate(ProcessDate);
							listPage.deleteFormInstance(Form, ProcessDate);
						}

						logger.info("Begin set retrieve properties");
						int init = listPage.getNotificationNums();
						logger.info("There are " + init + " notifcation(s)");
						FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
						retrievePage.setGroup(Group);
						retrievePage.setReferenceDate(ProcessDate);
						retrievePage.setForm(Form);
						retrievePage.clickOK();
						boolean openForm = false;
						if (isJobSuccessed())
						{
							logger.info("Retrieve form sucessed");
							openForm = true;
						}
						else
							logger.error("Retrieve form failed");

						listPage.closeJobDialog();

						if (jobStatus.length() > 1)
						{
							JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
							if (!jobManagerPage.getLatestJobInfo().get(8).equals(jobStatus))
							{
								logger.error("Job status is incorrect,should be " + jobStatus);
								testRst = false;
							}
							jobManagerPage.backToDashboard();
						}

						listPage.clickDashboard();

						if (openForm)
						{

							logger.info("Copy file " + CheckCellValueFile + " to " + checkCellFileFolder);
							String source = testDataFolder + CheckCellValueFile;
							String dest = checkCellFileFolder + CheckCellValueFile;

							FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
							File expectedValueFile = new File(dest);
							if (expectedValueFile.exists())
							{
								expectedValueFile.delete();
							}
							FileUtils.copyFile(new File(source), expectedValueFile);

							testRst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
						}
					}
					else if (Update_New.length() > 0)
					{
						String type = null, cellName = null, cellValue = null;
						if (Update_New.length() > 1)
						{
							type = Update_New.split(",")[0];
							cellName = Update_New.split(",")[1];
							cellValue = Update_New.split(",")[2];
							if (type.equalsIgnoreCase("D"))
								type = "Discard";
							else
								type = "Preserve";
						}
						else
						{
							type = Update_New;
						}
						ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, ProcessDate);
						List<String> views = returnSourcePage.getSourceView();

						String SQL = "SELECT \"PHYSICAL_VIEW_NAME\" FROM \"CFG_DW_VIEW_BINDING\" WHERE \"VIEW_CODE\"='" + views.get(0) + "'";
						String physicalViewName = DBQuery.queryRecord(SQL);
						SQL = "UPDATE \"" + physicalViewName + "\" SET \"N_RUN_SKEY\"= \"N_RUN_SKEY\"+1";
						DBQuery.updateSourceVew(SQL);

						Random rand = new Random();
						int ran = rand.nextInt(2);

						FormInstancePage formInstancePage = null;

						if (Update_New.length() == 1)
						{
							if (ran == 0)
							{
								returnSourcePage = listPage.enterReturnSourcePage(formCode, version, ProcessDate);
								returnSourcePage.update();
								if (isJobSuccessed())
								{
									logger.info("Update form sucessed");
								}
								else
								{
									logger.error("Update form failed");
									testRst = false;
								}
								listPage.closeJobDialog();

								if (jobStatus.length() > 1)
								{
									JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
									if (!jobManagerPage.getLatestJobInfo().get(8).equals(jobStatus))
									{
										logger.error("Job status is incorrect,should be " + jobStatus);
										testRst = false;
									}
									jobManagerPage.backToDashboard();
								}

								listPage.clickDashboard();
							}
							else
							{
								formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
								returnSourcePage = formInstancePage.enterReturnSourcePage();
								returnSourcePage.update();
								if (isJobSuccessed())
								{
									logger.info("Update form sucessed");
								}
								else
								{
									logger.error("Update form failed");
									testRst = false;
								}
								formInstancePage.closeRetrieveDialog();
								formInstancePage.closeFormInstance();

								if (jobStatus.length() > 1)
								{
									JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
									if (!jobManagerPage.getLatestJobInfo().get(8).equals(jobStatus))
									{
										logger.error("Job status is incorrect,should be " + jobStatus);
										testRst = false;
									}
									jobManagerPage.backToDashboard();
								}
							}

						}
						else
						{
							formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
							formInstancePage.editCellValue(null, cellName, null, cellValue);
							if (ran == 0)
							{
								formInstancePage.closeFormInstance();
								returnSourcePage = listPage.enterReturnSourcePage(formCode, version, ProcessDate);
								returnSourcePage.retrieveNew(type);
								if (isJobSuccessed())
								{
									logger.info("Retrieve new form sucessed");
								}
								else
								{
									logger.error("Retrieve new form failed");
									testRst = false;
								}
								listPage.closeJobDialog();

								if (jobStatus.length() > 1)
								{
									JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
									if (!jobManagerPage.getLatestJobInfo().get(8).equals(jobStatus))
									{
										logger.error("Job status is incorrect,should be " + jobStatus);
										testRst = false;
									}
									jobManagerPage.backToDashboard();

								}
								listPage.clickDashboard();
							}
							else
							{
								returnSourcePage = formInstancePage.enterReturnSourcePage();
								returnSourcePage.retrieveNew(type);
							}
							if (isJobSuccessed())
							{
								logger.info("Retrieve new form sucessed");
							}
							else
							{
								logger.error("Retrieve new form failed");
								testRst = false;
							}
							formInstancePage.closeRetrieveDialog();
							formInstancePage.closeFormInstance();
							if (jobStatus.length() > 1)
							{
								JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
								if (!jobManagerPage.getLatestJobInfo().get(8).equals(jobStatus))
								{
									logger.error("Job status is incorrect,should be " + jobStatus);
									testRst = false;
								}
								jobManagerPage.backToDashboard();
							}
						}

						if (testRst)
						{
							formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
							logger.info("Copy file " + CheckCellValueFile + " to " + checkCellFileFolder);
							String source = testDataFolder + CheckCellValueFile;
							String dest = checkCellFileFolder + CheckCellValueFile;

							File expectedValueFile = new File(dest);
							if (expectedValueFile.exists())
							{
								expectedValueFile.delete();
							}
							FileUtils.copyFile(new File(source), expectedValueFile);

							testRst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
						}

					}
					else if (ErrorMessage.length() > 1)
					{
						String errorString = null;

						if (formStatus.equalsIgnoreCase("locked"))
						{
							String formLock = listPage.getFormDetailInfo(1).get(6);
							if (!formLock.equalsIgnoreCase("lock"))
							{
								FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
								formInstancePage.lockClick();
								formInstancePage.closeFormInstance();
							}
						}

						ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, ProcessDate);
						List<String> views = returnSourcePage.getSourceView();
						String SQL = "SELECT \"PHYSICAL_VIEW_NAME\" FROM \"CFG_DW_VIEW_BINDING\" WHERE \"VIEW_CODE\"='" + views.get(0) + "'";
						String physicalViewName = DBQuery.queryRecord(SQL);
						SQL = "UPDATE \"" + physicalViewName + "\" SET \"N_RUN_SKEY\"= \"N_RUN_SKEY\"+1";
						DBQuery.updateSourceVew(SQL);

						if (action.equalsIgnoreCase("retrieve"))
						{
							FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
							retrievePage.setGroup(Group);
							retrievePage.setReferenceDate(ProcessDate);
							retrievePage.setForm(Form);
							retrievePage.clickOK();

							errorString = returnSourcePage.getWarningMessage();
							returnSourcePage.closeReturnSourcePage();

						}
						else if (action.equalsIgnoreCase("update"))
						{
							returnSourcePage = listPage.enterReturnSourcePage(formCode, version, ProcessDate);
							errorString = returnSourcePage.getWarningMessage();
							returnSourcePage.closeReturnSourcePage();
						}
						else if (action.equalsIgnoreCase("lock"))
						{
							returnSourcePage = listPage.enterReturnSourcePage(formCode, version, ProcessDate);
							returnSourcePage.update();

							FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
							formInstancePage.lockClick();
						}

						if (!errorString.equalsIgnoreCase(ErrorMessage))
							testRst = false;
					}
					else
					{
						if (DeleteExistForm)
							listPage.deleteFormInstance(Form, ProcessDate);

						logger.info("Begin set retrieve properties");
						int init = listPage.getNotificationNums();
						logger.info("There are " + init + " notifcation(s)");
						FormInstanceRetrievePage retrievePage = listPage.openFormInstanceRetrievePage();
						retrievePage.setGroup(Group);
						retrievePage.setReferenceDate(ProcessDate);
						retrievePage.setForm(Form);
						retrievePage.clickOK();
						if (isJobSuccessed())
						{
							logger.info("Retrieve form sucessed");
						}
						else
							logger.warn("Retrieve form failed");

						listPage.closeJobDialog();

						if (jobStatus.length() > 1)
						{
							JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
							if (!jobManagerPage.getLatestJobInfo().get(8).equals(jobStatus))
							{
								logger.error("Job status is incorrect,should be " + jobStatus);
								testRst = false;
							}
							jobManagerPage.backToDashboard();
						}
					}

				}
				catch (RuntimeException e)
				{
					testRst = false;
					e.printStackTrace();
					logger.error(e.getMessage());
				}
				finally
				{
					closeFormInstance();
					writeTestResultToFile(testRstFile, ID, 13, CaseID, testRst, "DS_RetrieveForm");
				}
			}
		}

	}

	@Parameters(
	{ "fileName" })
	@Test
	public void DsReturnExport(@Optional String fileName) throws Exception
	{
		if (fileName == null || fileName.equals(""))
			fileName = "DSExport.xls";
		createFolderAndCopyFile("DataSchedule", fileName);
		List<String> Files = createFolderAndCopyFile("DataSchedule", fileName);
		String testDataFolder = Files.get(0);
		// String checkCellFileFolder = Files.get(1);

		if (!testRstFile.getName().equalsIgnoreCase(fileName))
			testRstFile = new File(testRstFile.getParent() + fileName);

		ListPage listPage = super.m.listPage;
		File testDataFile = new File(testDataFolderName + "\\DataSchedule\\" + fileName);
		for (int i = 1; i <= ExcelUtil.getRowNums(testDataFile, null); i++)
		{
			ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, i);
			int ID = Integer.parseInt(rowValue.get(0).trim());
			String Regulator = rowValue.get(1).trim();
			String Group = rowValue.get(2).trim();
			String Form = rowValue.get(3).trim();
			String referenceDate = rowValue.get(4).trim();
			boolean Run = rowValue.get(5).trim().equalsIgnoreCase("Y") ? true : false;
			boolean approve = rowValue.get(6).trim().equalsIgnoreCase("Y") ? true : false;
			String updateSource = rowValue.get(7).trim();
			String fileType = rowValue.get(8).trim();
			String module = rowValue.get(9).trim();
			String compressType = rowValue.get(10).trim();
			String message = rowValue.get(11).trim();
			String jobStatus = rowValue.get(12).trim();
			String errorMessage = rowValue.get(13).trim();
			String errorLog = rowValue.get(14).trim();
			String location = rowValue.get(15).trim();
			String baseline = rowValue.get(16).trim();
			String caseId = rowValue.get(18).trim();
			boolean testRst = true;

			if (Run)
			{
				logger.info("==========Test ds return export==========");
				logger.info("Case id is:" + caseId);
				try
				{
					listPage.getProductListPage(Regulator, Group, Form, referenceDate);

					String formCode = splitReturn(Form).get(0);
					String version = splitReturn(Form).get(1);
					List<String> formDetail = listPage.getFormDetailInfo(1);

					if (approve)
					{
						if (formDetail.get(6).equalsIgnoreCase("lock"))
						{
							FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
							formInstancePage.unlockClick();
							formInstancePage.closeFormInstance();
						}

						FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
						formInstancePage.clickReadyForApprove();
						listPage = formInstancePage.closeFormInstance();
						HomePage homePage = listPage.approveReturn(listPage, Regulator, Group, Form, referenceDate);
						homePage.logon();
						listPage.getProductListPage(Regulator, Group, Form, referenceDate);
					}

					if (updateSource.length() > 1)
					{

						ReturnSourcePage returnSourcePage = listPage.enterReturnSourcePage(formCode, version, referenceDate);
						List<String> views = returnSourcePage.getSourceView();
						String SQL = "SELECT \"PHYSICAL_SCHEMA_NAME\" FROM \"CFG_DW_VIEW_BINDING\" WHERE \"VIEW_CODE\"='" + views.get(0) + "'";
						String physicalViewName = DBQuery.queryRecord(SQL);
						SQL = "UPDATE \"" + physicalViewName + "\" SET \"" + updateSource + "\"= \"" + updateSource + "\"+1";
						DBQuery.updateSourceVew(SQL);
					}

					String regulatorPrefix = listPage.getSelectRegulatorPrefix();
					logger.info("Prefix of regulator[" + Regulator + "] is:" + regulatorPrefix);
					FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);

					if (message.length() > 1)
					{
						boolean lock = false;
						if (caseId.equalsIgnoreCase("6122"))
							lock = true;
						String actualMessage = formInstancePage.getExportDataSchduleMessage(fileType, module, compressType, lock);
						if (!actualMessage.equalsIgnoreCase(message))
						{
							testRst = false;
							logger.error("Expected message is[" + message + "], but actual message is[" + actualMessage + "]");
						}
					}
					else if (location.length() > 1)
					{
						String dateString = referenceDate.replace("/", "");
						dateString = dateString.substring(4, 8) + dateString.substring(2, 4) + dateString.substring(0, 2);
						location = location + "Submission\\" + regulatorPrefix + "\\" + Group + "\\" + dateString + "\\";
						File folderFile = new File(location);
						FileUtils.cleanDirectory(folderFile);
						if (errorLog.length() > 0)
						{
							logger.info("File location is:" + location);
							location = location + "ValidationErrors\\";
							testRst = formInstancePage.isExportDataSchduleSucessed(fileType, module, compressType, "test export", location);
						}
						else
						{
							logger.info("File location is:" + location);
							boolean exportRst = formInstancePage.isExportDataSchduleSucessed(fileType, module, compressType, "test export", location);
							if (!exportRst)
								testRst = false;
							if (baseline.length() > 1 && testRst)
							{
								baseline = testDataFolder + baseline;
								String exportFilePath = formInstancePage.getDownloadedDSReturn(location);
								if (fileType.equalsIgnoreCase("ds"))
									fileType = "xml";
								else if (fileType.equalsIgnoreCase("DS(txt)"))
									fileType = "text";
								else if (fileType.equalsIgnoreCase("DS(csv)"))
									fileType = "csv";
								testRst = Business.verifyExportedFile(baseline, exportFilePath, fileType);
								if (!testRst)
								{
									copyFailedFileToTestRst(exportFilePath, "DataSchdule");
								}
							}
						}

					}
					else
					{
						String dateString = referenceDate.replace("\\", "");
						dateString = dateString.substring(4, 8) + dateString.substring(0, 5);
						location = location + "\\Submission\\" + regulatorPrefix + "\\" + Group + "\\" + dateString + "\\";
						logger.info("File location is:" + location);
						boolean exportRst = formInstancePage.isExportDataSchduleSucessed(fileType, module, compressType, "test export", location);
						if (exportRst)
						{
							testRst = false;
							logger.error("Should not generated file");
						}
					}
					formInstancePage.closeFormInstance();

					if (errorMessage.length() > 1)
					{
						if (jobStatus.length() > 1)
						{
							JobManagerPage jobManagerPage = listPage.enterJobManagerPage();
							List<String> jobDetailsList = jobManagerPage.getLatestJobInfo();
							if (!jobDetailsList.get(8).equals(jobStatus))
							{
								logger.error("Job status is incorrect,should be " + jobStatus);
								testRst = false;
							}
							if (!jobDetailsList.get(12).equals(errorLog))
							{
								testRst = false;
								logger.error("Expected log is:" + errorLog + ", but actual log is:" + jobDetailsList.get(7));
							}
							jobManagerPage.backToDashboard();
						}

						MessageCenter messageCenter = listPage.enterMessageCenterPage();
						String msg = messageCenter.getLatestMessage();
						for (String item : errorMessage.split("#"))
						{
							if (!msg.contains(item))
							{
								testRst = false;
								logger.error("Error message is incorrect, actual message is: " + msg);
							}
						}
						messageCenter.closeMessageCenter();
					}
				}
				catch (RuntimeException e)
				{
					testRst = false;
					e.printStackTrace();
					logger.error(e.getMessage());
				}
				finally
				{
					closeFormInstance();
					writeTestResultToFile(testRstFile, ID, 17, caseId, testRst, "DS_Export");
				}
			}

		}
	}

}
