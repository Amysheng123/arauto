package com.lombardrisk.testCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.lombardrisk.pages.AllocationPage;
import com.lombardrisk.pages.ComputePage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.Business;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.CsvUtil;
import com.lombardrisk.utils.fileService.ExcelUtil;

/**
 * Created by leo tu on 4/13/2016.
 */
public class ComputeForm extends TestTemplate
{
	List<String> Files = new ArrayList<>();
	String testDataFolder = null;
	String checkCellFileFolder = null;
	File testRstFile = null;

	@Parameters(
	{ "fileName" })
	@Test
	public void testComputeForm(@Optional String fileName) throws Exception
	{
		logger.info("File is: " + fileName);
		if (fileName == null || fileName.equals(""))
			fileName = "ComputeForm.xls";
		Files = createFolderAndCopyFile("ComputeForm", fileName);
		testDataFolder = Files.get(0);
		checkCellFileFolder = Files.get(1);
		testRstFile = new File(Files.get(2));

		try
		{
			File testDataFile = new File(testDataFolderName + "/ComputeForm/" + fileName);
			for (int index = 1; index <= ExcelUtil.getRowNums(testDataFile, null); index++)
			{
				ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
				int ID = Integer.parseInt(rowValue.get(0).trim());
				String Regulator = rowValue.get(1).trim();
				String Group = rowValue.get(2).trim();
				String Form = rowValue.get(3).trim().length() == 0 ? null : rowValue.get(3).trim();
				String ProcessDate = rowValue.get(4).trim().length() == 0 ? null : rowValue.get(4).trim();
				String Run = rowValue.get(5).trim();
				String DeleteExistForm = rowValue.get(6).trim();
				String BaselineFile = rowValue.get(7).trim();
				String cellValueFile = rowValue.get(8).trim();
				String drilldownCell = rowValue.get(9).trim();
				String allocationFile = rowValue.get(10).trim();

				String ErrorMessage = rowValue.get(11).trim();
				String CaseID = rowValue.get(13).trim();

				if (Run.equalsIgnoreCase("Y"))
				{
					computeForm(ID, Regulator, Group, Form, ProcessDate, DeleteExistForm, BaselineFile, cellValueFile, drilldownCell, allocationFile, ErrorMessage, CaseID);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	private void computeForm(int ID, String Regulator, String Group, String Form, String ProcessDate, String DeleteExistForm, String BaselineFile, String cellValueFile, String drilldownCell,
			String allocationFile, String ErrorMessage, String CaseID) throws Exception
	{
		boolean testRst = true;
		try
		{
			logger.info("=======Test Compue return[" + Form + "_" + ProcessDate + "_" + Group + "],caseid=" + CaseID + "=======");
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Group, null, null);

			if (DeleteExistForm.equalsIgnoreCase("Y"))
			{
				if (listPage.getFormOptions().contains(Form))
					listPage.setForm(Form);
				if (listPage.getProcessDateOptions().contains(ProcessDate))
					listPage.setProcessDate(ProcessDate);
				listPage.deleteFormInstance(Form, ProcessDate);
			}

			ComputePage computePage = null;
			FormInstancePage formInstancePage = null;

			if (ErrorMessage.length() > 1)
			{
				computePage = listPage.enterComputePage();
				String msg = computePage.getErrorMessage(Group, ProcessDate, Form);
				if (!msg.equalsIgnoreCase(ErrorMessage))
				{
					testRst = false;
					logger.error("Expected is message is:" + ErrorMessage + ", but actual message is:" + msg);
				}
			}
			else
			{
				if (BaselineFile.length() > 1 || drilldownCell.length() > 1 || cellValueFile.length() > 1)
				{
					computePage = listPage.enterComputePage();
					formInstancePage = computePage.computeReturn(Group, ProcessDate, Form, false, true);
				}

				if (BaselineFile.length() == 0 && allocationFile.length() == 0 && ErrorMessage.length() == 0 && cellValueFile.length() == 0)
				{
					computePage = listPage.enterComputePage();
					if (computePage.getForms(Group, ProcessDate).contains(Form))
						testRst = false;
				}
				else
				{
					String formCode = splitReturn(Form).get(0);
					String version = splitReturn(Form).get(1);

					if (BaselineFile.length() > 1)
					{
						String exportFilePath = formInstancePage.exportToFile(Group, formCode, ProcessDate, "csv", null, null);
						if (exportFilePath == null)
						{
							testRst = false;
						}
						else
						{
							logger.info("Begin compare the downloaded csv file with baseline");
							BaselineFile = testDataFolder + BaselineFile;
							testRst = Business.verifyExportedFile(BaselineFile, exportFilePath, "csv");
							if (testRst)
							{
								logger.info("CSV compare result: Pass");
							}
							else
							{
								logger.info("CSV compare result: Fail");
							}
						}
					}
					if (drilldownCell.length() > 0)
					{
						String ExtCellName = null;
						String cellName = null;
						String rowNO = null;
						if (drilldownCell.contains(","))
						{
							logger.info(drilldownCell + " is extendgrid cell");
							cellName = drilldownCell.split(",")[0].trim();
							rowNO = drilldownCell.split(",")[1].trim();
							rowNO = String.valueOf(Integer.parseInt(rowNO) + 48);
						}
						else
						{
							cellName = drilldownCell;
						}
						String gridName = getExtendCellName(Regulator, formCode, version, cellName);
						if (gridName != null)
						{
							ExtCellName = gridName + rowNO + cellName;
						}

						AllocationPage allocationPage = formInstancePage.cellDoubleClick(Regulator, formCode, version, null, cellName, ExtCellName);
						String exportFilePath = allocationPage.exportAllocation();

						String BaselineAlloc = testDataFolder + allocationFile;
						logger.info("Begin compare the downloaded csv file with baseline");
						testRst = CsvUtil.compareCSV(new File(BaselineAlloc), new File(exportFilePath));
						if (testRst)
						{
							logger.info("CSV compare result: Pass");
						}
						else
						{
							logger.info("CSV compare result: Fail");
						}
					}
					if (cellValueFile.length() > 1)
					{
						String source = testDataFolder + cellValueFile;
						String dest = checkCellFileFolder + cellValueFile;

						File expectedValueFile = new File(dest);
						if (!expectedValueFile.isDirectory())
						{
							if (expectedValueFile.exists())
								expectedValueFile.delete();
							FileUtils.copyFile(new File(source), expectedValueFile);
						}

						testRst = getCellValueInForm(formInstancePage, Regulator, formCode, version, expectedValueFile);
					}
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
			writeTestResultToFile(testRstFile, ID, 12, CaseID, testRst, "ComputeForm");

		}

	}
}
