package com.lombardrisk.utils.fileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by Leo Tu on 6/19/2015
 */
public class ExcelUtil
{
	private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	public static int getColumnNums(File file, String sheetName) throws Exception
	{
		int nums = 0;
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook xwb = WorkbookFactory.create(inp);
			Sheet sheet = null;
			if (sheetName == null)
				sheet = xwb.getSheetAt(0);
			else
				sheet = xwb.getSheet(sheetName);
			nums = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return nums;
	}

	public static int getRowAmts(File file, String sheetName) throws Exception
	{
		// logger.info("File is:"+file);
		int amt = 0;
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workBook = WorkbookFactory.create(inp);
			Sheet sheet = null;
			if (sheetName != null)
			{
				sheet = workBook.getSheet(sheetName);
			}
			else
			{
				sheet = workBook.getSheetAt(0);
			}
			amt = sheet.getLastRowNum();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// logger.info("There are " + amt + " records");
		return amt;
	}

	public static int getRowNums(File file, String sheetName) throws Exception
	{
		// logger.info("File is:"+file);
		int amt = 0;
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workBook = WorkbookFactory.create(inp);
			Sheet sheet = null;
			if (sheetName != null)
			{
				sheet = workBook.getSheet(sheetName);
			}
			else
			{
				sheet = workBook.getSheetAt(0);
			}

			for (int i = 0; i < sheet.getLastRowNum(); i++)
			{
				Row row = sheet.getRow(i);
				try
				{
					row.getCell(0).setCellType(1);
				}
				catch (Exception e)
				{
				}
				if (row.getCell(0) == null)
				{
					break;
				}
				else if (row.getCell(0).getStringCellValue().equals(""))
				{
					break;
				}
				else
				{
					amt++;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// logger.info("There are " + amt + " records");
		return amt;
	}

	public static int getRowNums(File file, String sheetName, int columnID) throws Exception
	{
		// logger.info("File is:"+file+", sheet is:"+sheetName+" , column
		// is:"+columnID);
		int amt = 0;
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook xwb = WorkbookFactory.create(inp);
			Sheet sheet = null;
			if (sheetName == null)
				sheet = xwb.getSheetAt(0);
			else
				sheet = xwb.getSheet(sheetName);

			for (int i = 0; i < sheet.getLastRowNum(); i++)
			{
				try
				{
					Row row = sheet.getRow(i);
					try
					{
						row.getCell(0).setCellType(1);
					}
					catch (Exception e)
					{
					}
					if (row.getCell(columnID - 1).getStringCellValue().equals(""))
					{
						break;
					}
					else
					{
						amt++;
					}
				}
				catch (Exception e)
				{
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		logger.info("There are " + amt + " records");
		return amt;
	}

	@SuppressWarnings("finally")
	public static ArrayList<String> getSpecficColRowValueFromExcel(File file, String sheetName, int colStart, int rowIndex) throws Exception
	{
		// logger.info("File is:"+file+", sheet is:"+sheetName+" , column
		// start:"+colStart+" , row start:"+rowIndex);
		ArrayList<String> rowVal = new ArrayList<String>();
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workbook = WorkbookFactory.create(inp);
			Sheet sheet = null;
			if (sheetName == null)
				sheet = workbook.getSheetAt(0);
			else
				sheet = workbook.getSheet(sheetName);
			Row row = sheet.getRow(rowIndex);
			Cell cell = null;
			int colAmt = sheet.getRow(0).getPhysicalNumberOfCells();
			String cellValue = null;
			for (int i = colStart - 1; i < colAmt; i++)
			{
				cell = row.getCell(i);
				if (cell != null)
				{
					getCellValue(cell);
				}
				else
				{
					cellValue = "";
				}

				rowVal.add(cellValue);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return rowVal;
		}
	}

	@SuppressWarnings("finally")
	public static ArrayList<String> getRowValueFromExcel(File file, String sheetName, int rowIndex) throws Exception
	{
		// logger.info("File is:"+file+", sheet is:"+sheetName+" , row
		// is:"+rowIndex);
		ArrayList<String> rowVal = new ArrayList<String>();
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook xwb = WorkbookFactory.create(inp);
			Sheet sheet = null;
			if (sheetName == null)
				sheet = xwb.getSheetAt(0);
			else
				sheet = xwb.getSheet(sheetName);
			Row row = sheet.getRow(rowIndex);
			Cell cell = null;

			int colAmt = sheet.getRow(0).getPhysicalNumberOfCells();
			String cellValue = null;
			for (int i = 0; i < colAmt; i++)
			{
				cell = row.getCell(i);
				if (cell != null)
				{
					cellValue = getCellValue(cell);
				}
				else
					cellValue = "";

				rowVal.add(cellValue);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return rowVal;
		}
	}

	public static ArrayList<List<String>> getExcelContent(File file, String sheetName, int startColumn, int startRow) throws Exception
	{
		ArrayList<List<String>> content = new ArrayList<List<String>>();
		int amt = getRowAmts(file, sheetName);
		for (int i = startRow; i <= amt; i++)
		{
			ArrayList<String> rowContent = ExcelUtil.getSpecficColRowValueFromExcel(file, sheetName, startColumn, i);
			content.add(rowContent);
		}
		return content;
	}

	public static boolean getCellValueForArbitrary(File expectedValueFile, File exportedFile, String targetDataFolder) throws Exception
	{
		boolean compareRst = true;
		try
		{
			File txtFile_iFile = new File(targetDataFolder + "worksheet.txt");
			String lastSheet = "";
			List<String> content = null;
			int amt = ExcelUtil.getRowAmts(expectedValueFile, null);
			for (int index = 1; index <= amt; index++)
			{
				ArrayList<String> expectedValueValueList = ExcelUtil.getRowValueFromExcel(expectedValueFile.getAbsoluteFile(), null, index);

				String sheetName = expectedValueValueList.get(0);
				String cellName = expectedValueValueList.get(1).trim();
				String expectedValue = expectedValueValueList.get(3).trim();

				String actualValue = null;
				if (exportedFile.length() / 1024 / 1024 > 1)
				{
					if (!sheetName.equals(lastSheet))
					{
						System.out.println("Sheet name:" + sheetName);
						int minColumns = -1;
						OPCPackage p = OPCPackage.open(exportedFile.getPath(), PackageAccess.READ);
						XLSX2CSV xlsx2csv = new XLSX2CSV(p, System.out, minColumns);
						xlsx2csv.process(sheetName);
						p.close();

						content = TxtUtil.getFileContent(txtFile_iFile);
						lastSheet = sheetName;
					}

					int colIndex = getColumnIndex(cellName) - 1;
					int rowIndex = Integer.parseInt(cellName.replaceAll("[A-Z]", "")) - 1;

					List<String> rowList = Arrays.asList(content.get(rowIndex).split("~"));
					actualValue = rowList.get(colIndex);
				}
				else
				{
					InputStream inp = new FileInputStream(exportedFile);
					Workbook workBook = WorkbookFactory.create(inp);
					Sheet sheet = workBook.getSheet(sheetName);
					int rowID = Integer.parseInt(cellName.substring(numericPos(cellName))) - 1;
					int colID = convertColumnID(cellName.substring(0, numericPos(cellName)));
					Row row = null;
					Cell cell = null;
					row = sheet.getRow(rowID);
					cell = row.getCell(colID);
					if (cell != null)
					{
						actualValue = getCellValue(cell);
					}
				}
				if (actualValue == null)
				{
					logger.info("Cannot find cell[" + cellName + "] in file[" + exportedFile.getName() + "]");
				}

				ExcelUtil.writeToExcel(expectedValueFile, index, 4, actualValue);
				boolean rst = false;
				try
				{
					if (Double.parseDouble(expectedValue) - Double.parseDouble((actualValue)) == 0)
						rst = true;
				}
				catch (Exception e)
				{
					if (expectedValue.equals(actualValue))
						rst = true;
				}
				if (rst)
				{
					ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Pass");
				}
				else
				{
					ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Fail");
					compareRst = false;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			compareRst = false;
		}
		return compareRst;
	}

	public static int getColumnIndex(String rowStr)
	{
		rowStr = rowStr.replaceAll("[^A-Z]", "");
		byte[] rowAbc = rowStr.getBytes();
		int len = rowAbc.length;
		float num = 0;
		for (int i = 0; i < len; i++)
		{
			num += (rowAbc[i] - 'A' + 1) * Math.pow(26, len - i - 1);
		}
		return (int) num;
	}

	public static List<String> getCellNamesFromExcel(File file) throws Exception
	{
		List<String> cellNames = new ArrayList<>();
		try
		{
			InputStream inp = new FileInputStream(file);
			Workbook workBook = WorkbookFactory.create(inp);
			int nameCount = workBook.getNumberOfNames();
			for (int nameIndex = 0; nameIndex < nameCount; nameIndex++)
			{
				Name name = workBook.getNameAt(nameIndex);
				cellNames.add(name.getNameName());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return cellNames;
	}

	/**
	 * get cell(plain or extend) value from exported excel
	 *
	 * @param file
	 * @param cellName
	 * @param instance
	 * @param rowKey
	 * @return
	 * @throws Exception
	 */
	public static String getCellValueByCellName(File file, String cellName, String instance, String rowKey) throws Exception
	{
		String cellValue = null;
		String rowIndex = null;
		String columnIndex = null;

		String sheetName = null;
		// logger.info("Get value of cell[" + cellName + ", rowkey=" + rowKey +
		// ",instance=" + instance + "] in excel");
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workBook = WorkbookFactory.create(inp);
			int nameCount = workBook.getNumberOfNames();
			for (int nameIndex = 0; nameIndex < nameCount; nameIndex++)
			{
				Name name = workBook.getNameAt(nameIndex);
				if (name.getNameName().equals(cellName))
				{
					String ref = name.getRefersToFormula();
					sheetName = ref.split("!")[0];
					String rowColumn = ref.split("!")[1].substring(1);
					rowColumn = rowColumn.replace("$", "~");
					columnIndex = rowColumn.split("~")[0];
					rowIndex = rowColumn.split("~")[1];
					break;
				}
			}
			sheetName = sheetName.replace("'", "");
			Sheet sheet = workBook.getSheet(sheetName);
			if (sheet == null)
			{
				sheetName = sheetName + "|" + instance;
				sheet = workBook.getSheet(sheetName);
			}
			int rowID = Integer.parseInt(rowIndex) - 1;
			int colID = convertColumnID(columnIndex);
			if (rowKey != null && rowKey.length() > 0)
			{
				int rowNo = Integer.parseInt(rowKey);
				if (rowNo > 1)
				{
					rowID = rowID + rowNo - 1;
				}
			}
			Row row = null;
			Cell cell = null;
			try
			{
				row = sheet.getRow(rowID);
				cell = row.getCell(colID);
			}
			catch (Exception e)
			{

			}

			if (cell != null)
			{
				cellValue = getCellValue(cell);
			}
			else
			{
				logger.info("Cannot find cell[" + cellName + "] in file[" + file.getName() + "]");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return cellValue;
	}

	private static Map<String, String> getAllNames(File file) throws Exception
	{
		logger.info("Get all names");
		Map<String, String> names = new HashMap<String, String>();
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workBook = WorkbookFactory.create(inp);
			int nameCount = workBook.getNumberOfNames();
			for (int nameIndex = 0; nameIndex < nameCount; nameIndex++)
			{
				Name name = workBook.getNameAt(nameIndex);
				String ref = name.getRefersToFormula();
				String sheetName = ref.split("!")[0];
				String rowColumn = ref.split("!")[1].substring(1);
				rowColumn = rowColumn.replace("$", "~");
				String columnIndex = rowColumn.split("~")[0];
				String rowIndex = rowColumn.split("~")[1];

				int rowID = Integer.parseInt(rowIndex) - 1;
				int colID = convertColumnID(columnIndex);
				names.put(name.getNameName(), sheetName + "#" + rowID + "#" + colID);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return names;
	}

	/**
	 * get plain cell value from exported excel
	 *
	 * @param file
	 * @param cellName
	 * @return
	 * @throws Exception
	 */
	public static String getCellValueByCellName(File file, String cellName) throws Exception
	{
		logger.info("Get value of cell[" + cellName + "]");
		String cellValue = null;
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workBook = WorkbookFactory.create(inp);
			String nameDetail = getAllNames(file).get(cellName);

			String sheetName = nameDetail.split("#")[0];
			int rowID = Integer.parseInt(nameDetail.split("#")[1]);
			int colID = Integer.parseInt(nameDetail.split("#")[2]);
			Sheet sheet = workBook.getSheet(sheetName);
			Row row = null;
			Cell cell = null;
			try
			{
				row = sheet.getRow(rowID);
				cell = row.getCell(colID);
			}
			catch (Exception e)
			{
				sheet = workBook.getSheet(sheetName);
				row = sheet.getRow(rowID);
				cell = row.getCell(colID);
			}

			if (cell != null)
			{
				cellValue = getCellValue(cell);
			}
			else
			{
				logger.info("Cannot find cell[" + cellName + "] in file[" + file.getName() + "]");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return cellValue;
	}

	public static List<String> getAllSheets(File file) throws Exception
	{

		List<String> sheets = new ArrayList<String>();
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workbook = WorkbookFactory.create(inp);
			int count = workbook.getNumberOfSheets();

			for (int index = 0; index < count; index++)
			{
				Sheet sheet = workbook.getSheetAt(index);
				sheets.add(sheet.getSheetName());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return sheets;
	}

	public static boolean isDefinedCellNameExistInExcel(File file, List<String> cellNames) throws Exception
	{

		boolean testResult = true;
		boolean find = false;
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workbook = WorkbookFactory.create(inp);
			int nameCount = workbook.getNumberOfNames();
			for (String cellName : cellNames)
			{
				for (int nameIndex = 0; nameIndex < nameCount; nameIndex++)
				{
					Name name = workbook.getNameAt(nameIndex);
					if (name.getNameName().equals(cellName))
					{
						find = true;
					}

				}
				if (!find)
				{
					testResult = false;
					logger.error("Cannot find cell:" + cellName);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return testResult;
	}

	public static void writeToExcel(File fileName, int rowID, int colID, String value) throws Exception
	{
		FileInputStream inp = new FileInputStream(fileName);
		try
		{
			Workbook xwb = WorkbookFactory.create(inp);
			if (!value.equals(""))
			{
				Sheet sheet = xwb.getSheetAt(0);
				Row row = sheet.getRow(rowID);
				if (row == null)
					row = sheet.createRow(rowID);
				Cell cell = row.getCell(colID);
				if (cell == null)
					cell = row.createCell(colID);
				CellStyle cellStyle2 = xwb.createCellStyle();
				DataFormat format = xwb.createDataFormat();
				cellStyle2.setDataFormat(format.getFormat("@"));
				cell.setCellStyle(cellStyle2);
				cell.setCellValue(value);

				FileOutputStream out = new FileOutputStream(fileName);
				xwb.write(out);
				out.close();

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static int convertColumnID(String columnName)
	{
		int colID = 0;
		if (columnName.length() == 1)
		{
			char[] chars = columnName.toCharArray();
			colID = (int) chars[0];
			colID = colID - 65;
		}
		else
		{
			char[] chars = columnName.toCharArray();

			int id2 = (int) chars[1];
			if (columnName.substring(0, 1).equals("A"))
				colID = 26 + id2 - 65;
			else if (columnName.substring(0, 1).equals("B"))
				colID = 26 * 2 + id2 - 65;
			else if (columnName.substring(0, 1).equals("C"))
				colID = 26 * 3 + id2 - 65;

		}

		return colID;
	}

	public static int numericPos(String str)
	{
		int pos = 0;
		for (int i = 0; i < str.length(); i++)
		{
			if (Character.isDigit(str.charAt(i)))
			{
				pos = i;
				break;
			}
		}
		return pos;
	}

	public static List<String> getLastCaseID(File file, String caseID) throws Exception
	{
		List<String> rst = new ArrayList<String>();
		InputStream inp = new FileInputStream(file);
		Workbook workBook = WorkbookFactory.create(inp);
		Sheet sheet = workBook.getSheetAt(0);

		Row row = null;
		Cell cell = null;
		int rowAmt = sheet.getLastRowNum();
		for (int i = 1; i <= rowAmt; i++)
		{
			row = sheet.getRow(i);
			cell = row.getCell(0);
			String caseNO = cell.getStringCellValue();
			if (caseNO.contains("."))
				caseNO = caseNO.split(".")[0];
			if (caseNO.equals(caseID))
			{
				rst.add(String.valueOf(i));
				rst.add(caseNO);
				cell = row.getCell(1);
				rst.add(cell.getStringCellValue());
				break;
			}
		}
		return rst;

	}

	public static void WriteTestResult(File testRstFile, String caseID, String step, String testRst) throws Exception
	{
		int rowIndex = getRowNums(testRstFile, null) + 1;
		writeToExcel(testRstFile, rowIndex, 0, caseID);
		if (!step.equals(""))
			writeToExcel(testRstFile, rowIndex, 1, step);
		writeToExcel(testRstFile, rowIndex, 2, testRst);
	}

	public static void WriteTestRst(File testRstFile, String caseID, String testRst, String module) throws Exception
	{
		int rowIndex = getRowNums(testRstFile, null) + 1;
		writeToExcel(testRstFile, rowIndex, 0, caseID);
		writeToExcel(testRstFile, rowIndex, 1, testRst);
		writeToExcel(testRstFile, rowIndex, 2, module);
	}

	public static void writeTestRstToFile(File testRstFile, int rowID, int colID, boolean testRst) throws Exception
	{
		String testResult = null;
		if (testRst)
			testResult = "Pass";
		else
			testResult = "Fail";
		writeToExcel(testRstFile, rowID, colID, testResult);
	}

	public static boolean isInteger(String str)
	{
		return str.matches("[0-9]+");
	}

	private static String getCellValue(Cell cell) throws Exception
	{
		String cellValue = "";
		DataFormatter formatter = new DataFormatter();
		switch (cell.getCellType())
		{
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell))
				{
					cellValue = formatter.formatCellValue(cell);
				}
				else
				{
					cellValue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellValue = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			default:
				cellValue = cell.toString().trim();
				break;
		}
		return cellValue;
	}

}
