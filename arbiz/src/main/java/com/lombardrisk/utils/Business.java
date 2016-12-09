package com.lombardrisk.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lombardrisk.utils.fileService.*;

/**
 * Created by Leo Tu on 6/17/2015
 */
public class Business
{
	public final static String targetLogFolder = System.getProperty("user.dir") + "/target/result/logs/";
	private final static Logger logger = LoggerFactory.getLogger(Business.class);
	private final static String targetDataFolder = System.getProperty("user.dir") + "/target/result/data/";
	static String parentPath = new File(new File(System.getProperty("user.dir")).getParent()).getParent().toString();

	@SuppressWarnings("static-access")
	public static boolean verifyExportedFile(String baselineFile, String exportedFile, String fileType) throws Exception
	{
		logger.info("Begin verify exported file");
		boolean compareRst = false;
		File expectedValueFile = new File(baselineFile);
		File exportFile = new File(exportedFile);
		if (baselineFile.endsWith(".xml"))
		{
			String exportTime = XMLUtil.getElementContentFromXML(exportedFile, "exportTime");
			if (exportTime != null)
			{
				baselineFile = XMLUtil.updateXMLFile(baselineFile, "exportTime", exportTime);
			}
		}
		else
		{
			String fileName = expectedValueFile.getName();
			String newName = fileName.replace(".", "~").split("~")[0] + "_new." + fileName.replace(".", "~").split("~")[1];
			File newFile = new File(baselineFile.replace(fileName, newName));
			if (newFile.exists())
				newFile.delete();

			FileUtils.copyFile(expectedValueFile, newFile);
			baselineFile = newFile.getAbsolutePath();
		}
		try
		{
			if (fileType.equalsIgnoreCase("csv"))
			{
				if (!exportedFile.toLowerCase().endsWith(".csv"))
				{
					String saveFolder = new File(exportedFile).getParent().toString();
					exportedFile = saveFolder + "/" + FileUtil.unCompress(exportedFile, saveFolder).get(0);
				}
				String exeFilePath = parentPath + "/public/extension/CompareExcel/CompareExcel.exe";
				logger.info("Exe file:" + exeFilePath);

				String commons[] =
				{ exeFilePath, baselineFile, exportedFile, targetDataFolder };
				Process process = Runtime.getRuntime().exec(commons);
				int exitcode = process.waitFor();
				logger.info("Finished:" + exitcode);

				File testRstFile = new File(targetDataFolder + "CompareRst.log");
				if (TxtUtil.getAllContent(testRstFile).length() < 3)
				{
					compareRst = true;
				}
				else
				{
					File testLogFile = new File(targetDataFolder + "Comparelog.log");
					if (!compareRst)
					{
						for (String log : TxtUtil.getFileContent(testLogFile))
						{
							logger.error(log);
						}
					}
					testLogFile.delete();
				}
			}
			else if ((baselineFile.endsWith(".xlsx") || baselineFile.endsWith(".xls"))
					&& (fileType.equalsIgnoreCase("Text") || fileType.equalsIgnoreCase("Vanilla") || fileType.equalsIgnoreCase("xml")))
			{
				compareRst = true;
				int amt = ExcelUtil.getRowAmts(expectedValueFile, null);
				for (int index = 1; index <= amt; index++)
				{
					ArrayList<String> expectedValueValueList = ExcelUtil.getRowValueFromExcel(expectedValueFile.getAbsoluteFile(), null, index);
					if (fileType.equalsIgnoreCase("Text"))
					{
						String cellName = expectedValueValueList.get(0).trim();
						String expectedValue = expectedValueValueList.get(1).trim();
						String keyWord = cellName + "+" + expectedValue;
						if (!TxtUtil.searchInTxt(exportFile, keyWord))
						{
							compareRst = false;
							ExcelUtil.writeToExcel(expectedValueFile, index, 2, "Fail");
						}
						else
						{
							ExcelUtil.writeToExcel(expectedValueFile, index, 2, "Pass");
						}
					}
					else if (fileType.equalsIgnoreCase("Vanilla") || fileType.equalsIgnoreCase("xml"))
					{
						String cellName = expectedValueValueList.get(0).trim();
						String instance = expectedValueValueList.get(1).trim();
						String rowID = expectedValueValueList.get(2).trim();
						String expectedValue = expectedValueValueList.get(3);
						String actualValue = null;
						if (!exportedFile.toLowerCase().endsWith(".xml"))
						{
							String saveFolder = new File(exportedFile).getParent().toString();
							exportedFile = saveFolder + "/" + FileUtil.unCompress(exportedFile, saveFolder).get(0);
						}
						if (instance.equals(""))
							actualValue = XMLUtil.getcellValueFromVanilla(exportedFile, cellName, null);
						else if (rowID.equals(""))
							actualValue = XMLUtil.getcellValueFromVanilla(exportedFile, null, cellName + "," + instance);
						else
							actualValue = XMLUtil.getcellValueFromVanilla(exportedFile, null, cellName + "," + instance + "," + rowID);

						ExcelUtil.writeToExcel(expectedValueFile, index, 4, actualValue);

						if (!expectedValue.equalsIgnoreCase(actualValue))
						{
							compareRst = false;
							ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Fail");
						}
						else
						{
							ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Pass");
						}
					}
				}
			}
			else if (baselineFile.endsWith(".txt"))
			{
				compareRst = true;
				if (!exportedFile.toLowerCase().endsWith(".txt"))
				{
					String saveFolder = new File(exportedFile).getParent().toString();
					exportedFile = saveFolder + "/" + FileUtil.unCompress(exportedFile, saveFolder).get(0);
				}
				List<String> base = TxtUtil.getFileContent(new File(baselineFile));
				List<String> exp = TxtUtil.getFileContent(new File(exportedFile));
				int baseAmt = base.size();
				int expAmt = exp.size();
				if (baseAmt != exp.size())
				{
					logger.info("The row amount is different.Baseline is[" + baseAmt + "], but expected is[" + expAmt + "]");
					compareRst = false;
				}
				else
				{
					for (int i = 0; i < expAmt; i++)
					{
						if (!base.get(i).equals(exp.get(i)))
						{
							compareRst = false;
							logger.info("Line " + i + 1 + ": Baseline is[" + base.get(i) + "] , but expected is[" + exp.get(i) + "]");
							break;
						}
					}
				}
			}
			else if (fileType.equalsIgnoreCase("XBRL") || baselineFile.endsWith(".xml"))
			{
				if (baselineFile.endsWith(".xbrl"))
				{
					if (!exportedFile.toLowerCase().endsWith(".xbrl"))
					{
						String saveFolder = new File(exportedFile).getParent().toString();
						exportedFile = saveFolder + "/" + FileUtil.unCompress(exportedFile, saveFolder).get(0);
					}
				}
				compareRst = XBRLUtil.XBRLCompare(baselineFile, exportedFile);
			}

			else if (fileType.equalsIgnoreCase("excel"))
			{
				long startTime = System.currentTimeMillis();
				String commons[] =
				{ parentPath + "/public/extension/GetCellValueFromExcel/GetCellValueFromExcel.exe", "\"" + exportFile.getAbsolutePath() + "\"", "\"" + baselineFile + "\"", targetLogFolder };
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//
				logger.info("Current time is:  " + dateFormat.format(now));
				Process process = Runtime.getRuntime().exec(commons);
				process.waitFor();
				long cur = System.currentTimeMillis();
				logger.info("Take " + (cur - startTime) / 1000 + " seconds");
				File compareRstFile = new File(targetLogFolder + "/queryCellValueRst.txt");
				String rst = TxtUtil.getAllContent(compareRstFile).trim();
				if (rst.valueOf(0).equalsIgnoreCase("0"))
					rst = rst.substring(1);
				if (rst.equalsIgnoreCase("pass"))
					compareRst = true;
				compareRstFile.delete();
			}
			else if (fileType.equalsIgnoreCase("ARBITRARY") || fileType.equalsIgnoreCase("iFile"))
				compareRst = ExcelUtil.getCellValueForArbitrary(expectedValueFile, exportFile, targetDataFolder);

			if (compareRst)
				logger.info("Compare result is: Pass");
			else
				logger.warn("Compare result is: Fail");
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		finally
		{
			new File(baselineFile).delete();
		}

		return compareRst;
	}

	public ArrayList<ArrayList<String>> splitSumRules(String rule)
	{
		String left = null;
		String right = null;
		ArrayList<ArrayList<String>> exp = new ArrayList<ArrayList<String>>();
		ArrayList<String> leftPart = new ArrayList<String>();
		ArrayList<String> rightPart = new ArrayList<String>();
		if (rule.contains("=") && !rule.contains(">") && !rule.contains("<"))
		{

			logger.info("This is sum rule");
			left = rule.split("=")[0];
			leftPart.add(left);
			right = rule.split("=")[1];
			String rightTemp = right;
			if (rightTemp.contains("+"))
			{
				rightTemp = rightTemp.replace("+", "~");
			}
			if (rightTemp.contains("-"))
			{
				rightTemp = rightTemp.replace("-", "~");
			}
			if (rightTemp.contains("*"))
			{
				rightTemp = rightTemp.replace("*", "~");
			}
			if (rightTemp.contains("/"))
			{
				rightTemp = rightTemp.replace("/", "~");
			}

			for (String s : rightTemp.split("~"))
			{
				rightPart.add(s);
			}

			exp.add(leftPart);
			exp.add(rightPart);
		}

		return exp;
	}

	public ArrayList<ArrayList<String>> splitValRules(String rule, String flag)
	{
		String left = null;
		String right = null;
		ArrayList<ArrayList<String>> exp = new ArrayList<ArrayList<String>>();
		ArrayList<String> leftPart = new ArrayList<String>();
		ArrayList<String> rightPart = new ArrayList<String>();

		logger.info("This is validation rule");

		rule = rule.replace(flag, "~");
		left = rule.split("~")[0];
		right = rule.split("~")[1];

		String leftTemp = left;
		if (leftTemp.contains("+"))
		{
			leftTemp = leftTemp.replace("+", "~");
		}
		if (leftTemp.contains("-"))
		{
			leftTemp = leftTemp.replace("-", "~");
		}
		if (leftTemp.contains("*"))
		{
			leftTemp = leftTemp.replace("*", "~");
		}
		if (leftTemp.contains("/"))
		{
			leftTemp = leftTemp.replace("/", "~");
		}

		for (String s : leftTemp.split("~"))
		{
			leftPart.add(s);
		}

		String rightTemp = right;
		if (rightTemp.contains("+"))
		{
			rightTemp = rightTemp.replace("+", "~");
		}
		if (rightTemp.contains("-"))
		{
			rightTemp = rightTemp.replace("-", "~");
		}
		if (rightTemp.contains("*"))
		{
			rightTemp = rightTemp.replace("*", "~");
		}
		if (rightTemp.contains("/"))
		{
			rightTemp = rightTemp.replace("/", "~");
		}

		for (String s : rightTemp.split("~"))
		{
			rightPart.add(s);
		}

		exp.add(leftPart);
		exp.add(rightPart);

		return exp;
	}

	public ArrayList<String> splitCell(String cell)
	{
		ArrayList<String> cellNameList = new ArrayList<String>();
		String returnName = "";
		String instance = "";
		String cellName = "";
		if (cell.contains("@"))
		{
			returnName = cell.split("@")[1];
		}
		if (cell.contains("[") && cell.contains("]"))
		{
			int f = cell.indexOf('[');
			int l = cell.indexOf(']');
			instance = cell.substring(f + 1, l);
			cellName = cell.substring(0, f);
		}
		if (cell.contains("@") && (!(cell.contains("["))))
		{
			cellName = cell.split("@")[0];
			;
		}
		if (!cell.contains("@") && (!(cell.contains("["))))
		{
			cellName = cell;
		}

		cellNameList.add(returnName);
		cellNameList.add(instance);
		cellNameList.add(cellName);
		return cellNameList;
	}

	public boolean isNumeric(String str)
	{
		String temp = null;
		if (str.startsWith("-"))
		{
			temp = str.substring(1);
		}
		else
		{
			temp = str;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(temp);
		if (!isNum.matches())
		{
			return false;
		}
		return true;
	}
}
