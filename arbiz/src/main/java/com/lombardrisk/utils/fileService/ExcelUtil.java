package com.lombardrisk.utils.fileService;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Leo Tu on Jun 19, 2015
 */
public class ExcelUtil {
    private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static int getColumnNums(File file, String sheetName) throws IOException {
        if (file.getAbsolutePath().endsWith(".xlsx")) {
            InputStream inp = new FileInputStream(file);
            XSSFWorkbook xwb = new XSSFWorkbook(inp);
            XSSFSheet sheet = null;
            if (sheetName == null)
                sheet = xwb.getSheetAt(0);
            else
                sheet = xwb.getSheet(sheetName);
            return sheet.getRow(0).getPhysicalNumberOfCells();

        } else if (file.getAbsolutePath().endsWith(".xls")) {
            InputStream inp = new FileInputStream(file);
            HSSFWorkbook xwb = new HSSFWorkbook(inp);
            HSSFSheet sheet = null;
            ;
            if (sheetName == null)
                sheet = xwb.getSheetAt(0);
            else
                sheet = xwb.getSheet(sheetName);
            return sheet.getRow(0).getPhysicalNumberOfCells();

        } else {
            return 0;
        }

    }

    public static int getRowAmts(File file, String sheetName) throws IOException {
        int amt = 0;
        InputStream inp = new FileInputStream(file);
        if (file.getAbsolutePath().endsWith(".xlsx")) {
            XSSFWorkbook xwb = new XSSFWorkbook(inp);
            XSSFSheet sheet = null;
            if (sheetName == null)
                sheet = xwb.getSheetAt(0);
            else
                sheet = xwb.getSheet(sheetName);

            amt = sheet.getLastRowNum();

        } else if (file.getAbsolutePath().endsWith(".xls")) {
            HSSFWorkbook xwb = new HSSFWorkbook(inp);
            HSSFSheet sheet = null;
            ;
            if (sheetName == null)
                sheet = xwb.getSheetAt(0);
            else
                sheet = xwb.getSheet(sheetName);

            amt = sheet.getLastRowNum();
        }
        return amt;
    }

    public static int getRowNums(File file, String sheetName) throws IOException {
        int amt = 0;
        InputStream inp = new FileInputStream(file);
        if (file.getAbsolutePath().endsWith(".xlsx")) {
            XSSFWorkbook xwb = new XSSFWorkbook(inp);
            XSSFSheet sheet = null;
            if (sheetName == null)
                sheet = xwb.getSheetAt(0);
            else
                sheet = xwb.getSheet(sheetName);

            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                try {
                    XSSFRow row = sheet.getRow(i);
                    try {
                        row.getCell(0).setCellType(1);
                    } catch (Exception e) {
                    }
                    if (row.getCell(0).getStringCellValue().equals("")) {
                        break;
                    } else {
                        amt++;
                    }
                } catch (Exception e) {
                }
            }
        } else if (file.getAbsolutePath().endsWith(".xls")) {
            HSSFWorkbook xwb = new HSSFWorkbook(inp);
            HSSFSheet sheet = null;
            ;
            if (sheetName == null)
                sheet = xwb.getSheetAt(0);
            else
                sheet = xwb.getSheet(sheetName);


            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                try {
                    HSSFRow row = sheet.getRow(i);
                    try {
                        row.getCell(0).setCellType(1);
                    } catch (Exception e) {
                    }
                    if (row.getCell(0).getStringCellValue().equals("")) {
                        break;
                    } else {
                        amt++;
                    }
                } catch (Exception e) {
                }

            }

        }


        return amt;
    }

    public static int getRowNums(File file, String sheetName, int columnID) throws IOException {
        int amt = 0;
        InputStream inp = new FileInputStream(file);
        if (file.getAbsolutePath().endsWith(".xlsx")) {
            XSSFWorkbook xwb = new XSSFWorkbook(inp);
            XSSFSheet sheet = null;
            if (sheetName == null)
                sheet = xwb.getSheetAt(0);
            else
                sheet = xwb.getSheet(sheetName);

            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                try {
                    XSSFRow row = sheet.getRow(i);
                    try {
                        row.getCell(0).setCellType(1);
                    } catch (Exception e) {
                    }
                    if (row.getCell(columnID - 1).getStringCellValue().equals("")) {
                        break;
                    } else {
                        amt++;
                    }
                } catch (Exception e) {
                }
            }
        } else if (file.getAbsolutePath().endsWith(".xls")) {
            HSSFWorkbook xwb = new HSSFWorkbook(inp);
            HSSFSheet sheet = null;
            ;
            if (sheetName == null)
                sheet = xwb.getSheetAt(0);
            else
                sheet = xwb.getSheet(sheetName);


            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                try {
                    HSSFRow row = sheet.getRow(i);
                    try {
                        row.getCell(0).setCellType(1);
                    } catch (Exception e) {
                    }
                    if (row.getCell(columnID - 1).getStringCellValue().equals("")) {
                        break;
                    } else {
                        amt++;
                    }
                } catch (Exception e) {
                }

            }

        }


        return amt;
    }

    @SuppressWarnings("finally")
    public static ArrayList<String> getSpecficColRowValueFromExcel(File file, String sheetName, int colStart, int rowIndex) throws IOException {
        ArrayList<String> rowVal = new ArrayList<String>();
        try {
            if (file.getAbsolutePath().endsWith(".xlsx")) {
                InputStream inp = new FileInputStream(file);
                XSSFWorkbook xwb = new XSSFWorkbook(inp);
                XSSFSheet sheet = null;
                if (sheetName == null)
                    sheet = xwb.getSheetAt(0);
                else
                    sheet = xwb.getSheet(sheetName);
                XSSFRow row = null;
                XSSFCell cell = null;
                row = sheet.getRow(rowIndex);
                int colAmt = sheet.getRow(0).getPhysicalNumberOfCells();
                String txt = null;
                for (int i = colStart - 1; i < colAmt; i++) {
                    cell = row.getCell(i);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try {
                        txt = cell.getStringCellValue();
                    } catch (Exception e) {
                        txt = "";
                        //e.printStackTrace();
                    }
                    rowVal.add(txt);
                }
            } else if (file.getAbsolutePath().endsWith(".xls")) {
                InputStream inp = new FileInputStream(file);
                HSSFWorkbook xwb = new HSSFWorkbook(inp);
                HSSFSheet sheet = null;
                if (sheetName == null)
                    sheet = xwb.getSheetAt(0);
                else
                    sheet = xwb.getSheet(sheetName);
                HSSFRow row = null;
                HSSFCell cell = null;
                row = sheet.getRow(rowIndex);
                int colAmt = sheet.getRow(0).getPhysicalNumberOfCells();
                String txt = null;
                for (int i = colStart - 1; i < colAmt; i++) {
                    cell = row.getCell(i);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try {
                        txt = cell.getStringCellValue();
                    } catch (Exception e) {
                        txt = "";
                        //e.printStackTrace();
                    }
                    rowVal.add(txt);
                }
            }

        } catch (Exception e) {

        } finally {
            return rowVal;
        }

    }

    @SuppressWarnings("finally")
    public static ArrayList<String> getRowValueFromExcel(File file, String sheetName, int rowIndex) throws IOException {
        ArrayList<String> rowVal = new ArrayList<String>();
        try {
            if (file.getAbsolutePath().endsWith(".xlsx")) {
                InputStream inp = new FileInputStream(file);
                XSSFWorkbook xwb = new XSSFWorkbook(inp);
                XSSFSheet sheet = null;
                if (sheetName == null)
                    sheet = xwb.getSheetAt(0);
                else
                    sheet = xwb.getSheet(sheetName);
                XSSFRow row = null;
                XSSFCell cell = null;
                row = sheet.getRow(rowIndex);
                int colAmt = sheet.getRow(0).getPhysicalNumberOfCells();
                String txt = null;
                for (int i = 0; i < colAmt; i++) {
                    cell = row.getCell(i);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try {
                        txt = cell.getStringCellValue();
                    } catch (Exception e) {
                        txt = "";
                        //e.printStackTrace();
                    }
                    rowVal.add(txt);
                }
            } else if (file.getAbsolutePath().endsWith(".xls")) {
                InputStream inp = new FileInputStream(file);
                HSSFWorkbook xwb = new HSSFWorkbook(inp);
                HSSFSheet sheet = null;
                if (sheetName == null)
                    sheet = xwb.getSheetAt(0);
                else
                    sheet = xwb.getSheet(sheetName);
                HSSFRow row = null;
                HSSFCell cell = null;
                row = sheet.getRow(rowIndex);
                int colAmt = sheet.getRow(0).getPhysicalNumberOfCells();
                String txt = null;
                for (int i = 0; i < colAmt; i++) {
                    cell = row.getCell(i);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try {
                        txt = cell.getStringCellValue();
                    } catch (Exception e) {
                        txt = "";
                        //e.printStackTrace();
                    }
                    rowVal.add(txt);
                }
            }

        } catch (Exception e) {

        } finally {
            return rowVal;
        }

    }


    public static ArrayList<List<String>> getExcelContent(File file, String sheetName, int startColumn, int startRow) throws IOException {
        ArrayList<List<String>> content = new ArrayList<List<String>>();
        int amt = getRowAmts(file, sheetName);
        for (int i = startRow; i <= amt; i++) {
            ArrayList<String> rowContent = ExcelUtil.getSpecficColRowValueFromExcel(file, sheetName, startColumn, i);
            content.add(rowContent);
        }
        return content;
    }

    public static String getCellValueByCellName(File file, String instance, String sheetName, String cellName) throws IOException {

        String cellValue = null;
        String rowCloumn = null;
        if (instance != null) {
            if (instance.trim().length() == 0)
                instance = null;
        }
        String sheetName_Tmp = null;
        try {
            if (file.getAbsolutePath().endsWith(".xlsx")) {
                InputStream inp = new FileInputStream(file);
                XSSFWorkbook xwb = new XSSFWorkbook(inp);
                int nameCount = xwb.getNumberOfNames();

                if (sheetName == null) {
                    for (int nameIndex = 0; nameIndex < nameCount; nameIndex++) {
                        XSSFName name = xwb.getNameAt(nameIndex);
                        if (name.getNameName().equals(cellName)) {
                            sheetName_Tmp = name.getSheetName();
                            int pos = sheetName_Tmp.lastIndexOf("|");
                            if (pos > 0 && isInteger(sheetName_Tmp.substring(pos + 1, pos + 2))) {
                                sheetName_Tmp = sheetName_Tmp.substring(0, pos);
                            }

                            rowCloumn = name.getRefersToFormula().split("!")[1].substring(1);
                            rowCloumn = rowCloumn.replace("$", "~");
                            break;
                        }

                    }
                } else {
                    sheetName_Tmp = sheetName;
                    int m = numericPos(cellName);
                    rowCloumn = cellName.substring(0, m) + "~" + cellName.substring(m);
                }


                if (instance != null) {
                    sheetName = sheetName_Tmp + "|" + instance;
                } else {
                    sheetName = sheetName_Tmp;
                }
                if (sheetName_Tmp != null) {
                    logger.info("SheetName is: " + sheetName);
                    logger.info("rowCloumn is: " + rowCloumn);

                    String colIndex = rowCloumn.split("~")[0];

                    int rowID = Integer.parseInt(rowCloumn.split("~")[1]) - 1;
                    int colID = convertCoumnID(colIndex);
                    XSSFSheet sheet = xwb.getSheet(sheetName);

                    XSSFRow row = null;
                    XSSFCell cell = null;
                    try {
                        row = sheet.getRow(rowID);
                        cell = row.getCell(colID);
                    } catch (Exception e) {
                        sheetName = sheetName_Tmp;
                        ;
                        sheet = xwb.getSheet(sheetName);
                        row = sheet.getRow(rowID);
                        cell = row.getCell(colID);
                    }

                    if (cell.getCellType() == 0) {
                        DecimalFormat df = new DecimalFormat("###############.000");
                        cellValue = df.format(cell.getNumericCellValue());
                    } else if (cell.getCellType() == 1)
                        cellValue = cell.getStringCellValue();

                    else if (cell.getCellType() == 2) {
                        try {
                            cellValue = String.valueOf(cell.getRichStringCellValue());
                        } catch (IllegalStateException e) {
                            cellValue = String.valueOf(cell.getNumericCellValue());
                        }
                    }
                } else {
                    logger.info("Cannot find cell[" + cellName + "] in file[" + file.getName() + "]");
                }
            } else if (file.getAbsolutePath().endsWith(".xls")) {
                InputStream inp = new FileInputStream(file);
                HSSFWorkbook xwb = new HSSFWorkbook(inp);
                int nameCount = xwb.getNumberOfNames();
                if (sheetName == null) {
                    for (int nameIndex = 0; nameIndex < nameCount; nameIndex++) {
                        HSSFName name = xwb.getNameAt(nameIndex);
                        if (name.getNameName().equals(cellName)) {
                            sheetName_Tmp = name.getSheetName();
                            rowCloumn = name.getRefersToFormula().split("!")[1].substring(1);
                            rowCloumn = rowCloumn.replace("$", "~");
                            break;
                        }

                    }
                } else {
                    sheetName_Tmp = sheetName;
                    int m = numericPos(cellName);
                    rowCloumn = cellName.substring(0, m) + "~" + cellName.substring(m);
                }

                if (instance != null) {
                    sheetName = sheetName_Tmp + "!" + instance;
                } else {
                    sheetName = sheetName_Tmp;
                }
                if (sheetName != null) {
                    logger.info("SheetName is: " + sheetName);
                    logger.info("rowCloumn is: " + rowCloumn);

                    String colIndex = rowCloumn.split("~")[0];

                    int rowID = Integer.parseInt(rowCloumn.split("~")[1]) - 1;
                    int colID = convertCoumnID(colIndex);
                    HSSFSheet sheet = xwb.getSheet(sheetName);

                    HSSFRow row = null;
                    HSSFCell cell = null;
                    try {
                        row = sheet.getRow(rowID);
                        cell = row.getCell(colID);
                    } catch (Exception e) {
                        sheetName = sheetName_Tmp;
                        ;
                        sheet = xwb.getSheet(sheetName);
                        row = sheet.getRow(rowID);
                        cell = row.getCell(colID);
                    }

                    if (cell.getCellType() == 0) {
                        DecimalFormat df = new DecimalFormat("###############.000");
                        cellValue = df.format(cell.getNumericCellValue());
                    } else if (cell.getCellType() == 1)
                        cellValue = cell.getStringCellValue();

                    else if (cell.getCellType() == 2) {
                        try {
                            cellValue = String.valueOf(cell.getRichStringCellValue());
                        } catch (IllegalStateException e) {
                            cellValue = String.valueOf(cell.getNumericCellValue());
                        }
                    }
                } else {
                    logger.info("Cannot find cell[" + cellName + "] in file[" + file.getName() + "]");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return cellValue;
    }

    public static List<String> getAllSheetS(File file) throws IOException {

        List<String> sheets = new ArrayList<String>();
        if (file.getAbsolutePath().endsWith(".xlsx")) {
            InputStream inp = new FileInputStream(file);
            XSSFWorkbook xwb = new XSSFWorkbook(inp);
            int count = xwb.getNumberOfSheets();

            for (int index = 0; index < count; index++) {
                XSSFSheet sheet = xwb.getSheetAt(index);
                sheets.add(sheet.getSheetName());
            }
        } else if (file.getAbsolutePath().endsWith(".xls")) {
            InputStream inp = new FileInputStream(file);
            HSSFWorkbook xwb = new HSSFWorkbook(inp);
            int count = xwb.getNumberOfNames();

            for (int index = 0; index < count; index++) {
                HSSFSheet sheet = xwb.getSheetAt(index);
                sheets.add(sheet.getSheetName());
            }
        }

        return sheets;
    }


    public static boolean isDefinedCellNameExistInExcel(File file, List<String> cellNames) throws IOException {

        boolean testResult = true;
        boolean find = false;
        try {
            if (file.getAbsolutePath().endsWith(".xlsx")) {
                InputStream inp = new FileInputStream(file);
                XSSFWorkbook xwb = new XSSFWorkbook(inp);
                int nameCount = xwb.getNumberOfNames();
                for (String cellName : cellNames) {
                    for (int nameIndex = 0; nameIndex < nameCount; nameIndex++) {
                        XSSFName name = xwb.getNameAt(nameIndex);
                        if (name.getNameName().equals(cellName)) {
                            find = true;
                        }

                    }
                    if (!find) {
                        testResult = false;
                        logger.error("Cannot find cell:" + cellName);
                    }
                }

            } else if (file.getAbsolutePath().endsWith(".xls")) {
                InputStream inp = new FileInputStream(file);
                HSSFWorkbook xwb = new HSSFWorkbook(inp);
                int nameCount = xwb.getNumberOfNames();
                for (String cellName : cellNames) {
                    for (int nameIndex = 0; nameIndex < nameCount; nameIndex++) {
                        HSSFName name = xwb.getNameAt(nameIndex);
                        if (name.getNameName().equals(cellName)) {
                            find = true;
                        }

                    }
                    if (!find) {
                        testResult = false;
                        logger.error("Cannot find cell:" + cellName);
                    }
                }
            }
        } catch (Exception e) {
        }


        return testResult;
    }


    public static void writeToExcel(File fileName, int rowID, int colID, String value) {
        try {
            if (fileName.getAbsolutePath().endsWith(".xlsx")) {
                if (!value.equals("")) {
                    XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(fileName));
                    XSSFRow xRow = null;
                    XSSFSheet xSheet = xwb.getSheetAt(0);
                    xRow = xSheet.getRow(rowID);
                    if (xRow == null)
                        xRow = xSheet.createRow(rowID);
                    XSSFCell xCell = xRow.getCell(colID);
                    if (xCell == null)
                        xCell = xRow.createCell(colID);
                    xCell.setCellValue(value);

                    FileOutputStream out = new FileOutputStream(fileName);
                    xwb.write(out);
                    out.close();

                }
            } else if (fileName.getAbsolutePath().endsWith(".xls")) {
                if (!value.equals("")) {
                    HSSFWorkbook xwb = new HSSFWorkbook(new FileInputStream(fileName));
                    HSSFRow Row = null;
                    HSSFSheet Sheet = xwb.getSheetAt(0);
                    Row = Sheet.getRow(rowID);
                    HSSFCell Cell = Row.getCell(colID);
                    if(Cell==null){
                    	Cell = Row.createCell(colID);
                    }

                    Cell.setCellValue(value);

                    FileOutputStream out = new FileOutputStream(fileName);
                    xwb.write(out);
                    out.close();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static int convertCoumnID(String comlumnName) {
        int colID = 0;
        if (comlumnName.length() == 1) {
            char[] chars = comlumnName.toCharArray();
            colID = (int) chars[0];
            colID = colID - 65;
        } else {
            char[] chars = comlumnName.toCharArray();

            int id2 = (int) chars[1];
            if (comlumnName.substring(0, 1).equals("A"))
                colID = 26 + id2 - 65;
            else if (comlumnName.substring(0, 1).equals("B"))
                colID = 26 * 2 + id2 - 65;
            else if (comlumnName.substring(0, 1).equals("C"))
                colID = 26 * 3 + id2 - 65;

        }


        return colID;
    }

    public static int numericPos(String str) {
        int pos = 0;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public static List<String> getLastCaseID(File file, String caseID) throws IOException {
        List<String> rst = new ArrayList<String>();
        InputStream inp = new FileInputStream(file);
        XSSFWorkbook xwb = new XSSFWorkbook(inp);
        XSSFSheet sheet = xwb.getSheetAt(0);

        XSSFRow row = null;
        XSSFCell cell = null;
        int rowAmt = sheet.getLastRowNum();
        if (rowAmt > 0) {
            rst.add(String.valueOf(rowAmt));

            row = sheet.getRow(rowAmt);
            cell = row.getCell(0);
            String caseNO = cell.getStringCellValue();
            if (caseNO.contains("."))
                caseNO = caseNO.split(".")[0];

            rst.add(caseNO);
            cell = row.getCell(1);
            rst.add(cell.getStringCellValue());
        }
        /*
        for( int i=1;i<=rowAmt;i++)
		{
			row = sheet.getRow(i);
			cell=row.getCell(0);
			double caseNO=0;
			try{
				caseNO=cell.getNumericCellValue();
				
			}catch(Exception e)
			{
				caseNO=Double.parseDouble(cell.getStringCellValue());
			}
			cell=row.getCell(1);
			String status=cell.getStringCellValue();
			if(caseNO-Double.parseDouble(caseID)==0)
			{
				rst=caseNO+"#"+status;
				break;
			}
		}*/
        return rst;

    }

    public static void WriteTestResult(File testRstFile, String caseID, String step, String testRst) throws IOException {
        int rowIndex = getRowNums(testRstFile, null) + 1;
        writeToExcel(testRstFile, rowIndex, 0, caseID);
        if (!step.equals(""))
            writeToExcel(testRstFile, rowIndex, 1, step);
        writeToExcel(testRstFile, rowIndex, 2, testRst);
    }

    public static void WriteTestRst(File testRstFile, String caseID, String testRst, String module) throws IOException {
        int rowIndex = getRowNums(testRstFile, null) + 1;
        writeToExcel(testRstFile, rowIndex, 0, caseID);
        writeToExcel(testRstFile, rowIndex, 1, testRst);
        writeToExcel(testRstFile, rowIndex, 2, module);
    }


    public static void writeTestRstToFile(File testRstFile, int rowID, int colID, boolean testRst) throws IOException {
        String testResult = null;
        if (testRst)
            testResult = "Pass";
        else
            testResult = "Fail";
        writeToExcel(testRstFile, rowID, colID, testResult);
    }

    public static boolean isInteger(String str) {
        return str.matches("[0-9]+");
    }

}
