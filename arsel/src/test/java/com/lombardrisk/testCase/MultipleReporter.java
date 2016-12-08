package com.lombardrisk.testCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.Browser;
import com.lombardrisk.utils.fileService.ExcelUtil;

/**
 * Create by Leo Tu on 4/27/2016.
 */
public class MultipleReporter extends Browser
{
	private final static Logger logger = LoggerFactory.getLogger(MultipleReporter.class);
	List<String> Files = createFolderAndCopyFile("MultipleReporter");
	String testDataFolder = Files.get(0);
	String checkCellFileFolder = Files.get(1);
	File testRstFile = new File(Files.get(2));

	@Test
	public void testMultipleReporter() throws Exception
	{
		File testDataFile = new File(testDataFolderName + "/MultipleReporter/MultipleReporter.xls");
		int rouNums = ExcelUtil.getRowNums(testDataFile, null);
		for (int index = 1; index <= rouNums; index++)
		{
			ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
			int ID = Integer.parseInt(rowValue.get(0).trim());
			String Run = rowValue.get(1).trim();
			String ARInstallPath = rowValue.get(2).trim();
			String cPrefix = rowValue.get(3).trim();
			String cVersion = rowValue.get(4).trim();
			String Action = rowValue.get(5).trim();
			String Users = rowValue.get(6).trim();
			String Regulator = rowValue.get(7).trim();
			String Regulator2 = rowValue.get(8).trim();
			String Entity = rowValue.get(9).trim();
			String Form = rowValue.get(10).trim();
			String ReferenceDate = rowValue.get(11).trim();
			String CaseID = rowValue.get(13).trim();

			List<String> prefixs = Arrays.asList(cPrefix.split("#"));
			List<String> versions = Arrays.asList(cVersion.split("#"));
			List<String> users = Arrays.asList(Users.split("#"));
			List<String> regulartors = Arrays.asList(Regulator.split("#"));
			List<String> regulartors2 = Arrays.asList(Regulator2.split("#"));
			List<String> entities = Arrays.asList(Entity.split("#"));
			List<String> forms = Arrays.asList(Form.split("#"));
			List<String> dates = Arrays.asList(ReferenceDate.split("#"));
			if (Run.equalsIgnoreCase("y"))
			{
				MultipleReporterVerify(ID, ARInstallPath, Action, prefixs, versions, users, regulartors, regulartors2, entities, forms, dates, CaseID);
			}

		}
	}

	public void MultipleReporterVerify(int ID, String ARInstallPath, String Action, List<String> prefixs, List<String> versions, List<String> users, List<String> regulartors,
			List<String> regulartors2, List<String> entities, List<String> forms, List<String> dates, String CaseID) throws Exception
	{
		boolean testRst = true;
		logger.info("Begin verify case " + CaseID);
		try
		{
			boolean restartService = false;
			for (int i = 0; i < prefixs.size(); i++)
			{
				if (Action.equalsIgnoreCase("active"))
				{
					if (testRPConfigActivate(ARInstallPath, prefixs.get(i), versions.get(i)))
						restartService = true;
				}

			}
			startService(ARInstallPath, restartService);

			reStartBrowser();
			ListPage listPage = null;

			if (!CaseID.equalsIgnoreCase("5190"))
			{
				for (int i = 0; i < users.size(); i++)
				{
					listPage = super.loginAsOtherUser(users.get(i), "password");
					if (regulartors.size() > 1)
					{
						List<String> regList = listPage.getRegulatorOptions();

						if (regList.size() != regulartors.get(i).split(",").length)
						{
							testRst = false;
						}
						else
						{
							for (String reg : regulartors.get(i).split(","))
							{
								if (!regList.contains(reg))
									testRst = false;
							}
						}
					}

					if (regulartors2.size() > 1)
					{
						listPage.setRegulator(regulartors2.get(i));
						if (entities.size() > 1)
						{
							List<String> entityList = listPage.getGroupOptions();
							for (String entity : entities.get(i).split(","))
								if (entity.startsWith("!"))
								{
									if (entityList.contains(entity.replace("!", "")))
									{
										testRst = false;
									}
								}
								else if (!entityList.contains(entity))
								{
									testRst = false;
								}
						}

						if (forms.size() > 1)
						{
							listPage.setGroup(entities.get(i));
							List<String> formList = listPage.getFormOptions();
							String inForm = null;
							String exForm = null;
							for (String f : forms.get(i).split(","))
							{
								if (f.startsWith("!"))
								{
									exForm = f.replace("!", "");
								}
								else
								{
									inForm = f;
								}
							}
							if (!formList.contains(inForm))
								testRst = false;
							else if (formList.contains(exForm))
								testRst = false;
							else
							{
								listPage.setForm(inForm);
								if (dates.size() > 1)
								{
									List<String> dateList = listPage.getProcessDateOptions();
									for (String processDate : dates.get(i).split(","))
									{
										if (!dateList.contains(processDate))
											testRst = false;
									}
								}
							}
						}
					}
				}
			}
			else
			{

				restartService = false;
				for (int i = 0; i < prefixs.size(); i++)
				{
					if (Action.equalsIgnoreCase("active"))
					{
						if (testRPConfigActivate(ARInstallPath, prefixs.get(i), versions.get(i)))
							restartService = true;
					}

				}
				startService(ARInstallPath, restartService);
				reStartBrowser();
				listPage = super.loginAsOtherUser("user1", "password");
				if (listPage.isExistCopyData())
				{
					testRst = false;
					logger.error("Copy data from still exist!");
				}

				if (!listPage.isExistImportAdjsutment())
				{
					testRst = false;
					logger.error("Import adjustment does not exist in dashboard!");
				}

				listPage.setGroup("2999");
				listPage.setForm("CRGB v5");
				listPage.setProcessDate("04/01/2016");
				FormInstancePage formInstancePage = listPage.openFirstFormInstance();
				if (formInstancePage.isExportToFileEnabled())
				{
					testRst = false;
					logger.error("Export to cvs.excel still exist in form page!");
				}
				if (formInstancePage.isAddInstanceBtnDisplayed())
				{
					testRst = false;
					logger.error("Add/delete instance still exist in form page!");
				}

				formInstancePage.closeFormInstance();
				listPage = super.loginAsOtherUser("user2", "password");

				listPage.setGroup("2999");
				listPage.setForm("CRGB v5");
				listPage.setProcessDate("04/01/2016");
				formInstancePage = listPage.openFirstFormInstance();
				if (formInstancePage.isViewAdjustmentLogDisplayed())
				{
					testRst = false;
					logger.error("View adjustmentlog still exist!");
				}

				if (formInstancePage.isEnableEditeCell("CRGB01R010C010"))
				{
					testRst = false;
					logger.error("Cell should not be editable!");
				}

				if (formInstancePage.isAllocationDisplayed("CRGB01R170C010"))
				{
					testRst = false;
					logger.error("Allocation table should not exits!");
				}

				formInstancePage.closeFormInstance();

				restartService = false;
				versions = new ArrayList<>();
				versions.add("1.24.1.4");
				versions.add("5.19.0.5");
				for (int i = 0; i < prefixs.size(); i++)
				{
					if (Action.equalsIgnoreCase("active"))
					{
						if (testRPConfigActivate(ARInstallPath, prefixs.get(i), versions.get(i)))
							restartService = true;
					}

				}
				startService(ARInstallPath, restartService);
				reStartBrowser();
				listPage = super.loginAsOtherUser("user2", "password");

				if (listPage.isCreateNewDisplayed())
				{
					testRst = false;
					logger.error("Still find create new button!");
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
			closeBrowser();
			writeTestResultToFile(testRstFile, ID, 12, CaseID, testRst, "MultipleReporter");

		}
	}

	public boolean testRPConfigActivate(String installPath, String productPrefix, String productVersion) throws Exception
	{
		logger.info("Begin activate configuration[" + productPrefix + " v " + productVersion + "]");
		String cmd = installPath + "/bin/config.bat  -t " + productPrefix + " -iv " + productVersion;
		logger.info("Activate config in cmd: " + cmd);
		String line = getCMDRst(cmd);
		logger.info(line);
		if (line.equals("Product configuration is already active"))
			return false;
		else
		{
			return true;
		}

	}

	public String getCMDRst(String cmd) throws Exception
	{
		BufferedReader br = null;
		String rst = null;
		try
		{
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
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

	public void startService(String installPath, boolean reStart) throws Exception
	{
		if (reStart)
			stopService(installPath);
		File File = new File(installPath + "/bin/run.lock");
		if (!File.exists())
		{
			logger.info("Start ar service");
			String cmd = installPath + "/bin/start.bat ";
			Runtime.getRuntime().exec("cmd.exe /C start " + cmd);
			Thread.sleep(1000 * 60);
		}
	}

	public void stopService(String installPath) throws Exception
	{
		File File = new File(installPath + "/bin/run.lock");
		if (File.exists())
		{
			logger.info("Stop ar service");
			String cmd = installPath + "/bin/stop.bat ";
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(1000 * 15);
		}
	}
}
