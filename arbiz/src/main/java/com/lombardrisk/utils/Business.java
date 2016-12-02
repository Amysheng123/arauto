package com.lombardrisk.utils;

import com.lombardrisk.utils.fileService.*;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Leo Tu on Jun 17, 2015
 */
public class Business {
    private final static Logger logger = LoggerFactory.getLogger(Business.class);
    static String parentPath= new File(System.getProperty("user.dir").toLowerCase()).getParent().toString();
    public static boolean verifyExportedFile(String baselineFile, String exportedFile, String fileType) throws IOException, DocumentException {
        boolean compareRst = true;
        File expectedValueFile = new File(baselineFile);
        File exportFile = new File(exportedFile);
        if (fileType.equalsIgnoreCase("csv")) {

            File base = new File(System.getProperty("user.dir") + "\\target\\result\\data\\base.xls");
            File exp = new File(System.getProperty("user.dir") + "\\target\\result\\data\\exp.xls");

            if (base.exists())
                base.delete();
            if (exp.exists())
                exp.delete();

            try {

                CsvUtil.CsvToExcel(baselineFile, base.getAbsolutePath());
                CsvUtil.CsvToExcel(exportedFile, exp.getAbsolutePath());

                try {
                    String exeFilePath = parentPath + "\\public\\extension\\CompareExcel\\CompareExcel.exe";
                    logger.info("Exe file:" + exeFilePath);

                    String commons[] = {exeFilePath, base.getAbsolutePath(), exp.getAbsolutePath()};
                    Process process = Runtime.getRuntime().exec(commons);
                    process.waitFor();
                   
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (base.exists())
                        base.delete();
                    if (exp.exists())
                        exp.delete();
                }

                File testRstFile = new File("C:\\autoTmp\\CompareRst.log");
                compareRst = Boolean.parseBoolean(TxtUtil.getAllContent(testRstFile));
                testRstFile.delete();

                File testLogFile = new File("C:\\autoTmp\\Comparelog.log");
                for (String log : TxtUtil.getFileContent(testLogFile)) {
                    logger.error(log);
                }
                testLogFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (baselineFile.endsWith(".xlsx") || baselineFile.endsWith(".xls")) {
            int amt = ExcelUtil.getRowAmts(expectedValueFile, null);
            for (int index = 1; index <= amt; index++) {
                ArrayList<String> expectedValueValueList = ExcelUtil.getRowValueFromExcel(expectedValueFile.getAbsoluteFile(), null, index);
                boolean goNext = true;
                try {
                    if (fileType.equalsIgnoreCase("text") || fileType.equalsIgnoreCase("vanilla")) {
                        String haveData = expectedValueValueList.get(0).trim();
                        if (haveData.length() < 1)
                            goNext = false;
                    } else {
                        String haveData = expectedValueValueList.get(1).trim();
                        if (haveData.length() < 1)
                            goNext = false;
                    }

                } catch (Exception e) {

                }
                if (goNext) {
                    if (fileType.equalsIgnoreCase("Text")) {
                        String cellName = expectedValueValueList.get(0).trim();
                        String expectedValue = expectedValueValueList.get(1).trim();
                        String keyWord = cellName + "+" + expectedValue;
                        if (!TxtUtil.searchInTxt(exportFile, keyWord)) {
                            compareRst = false;
                            ExcelUtil.writeToExcel(expectedValueFile, index, 2, "Fail");
                        } else {
                            ExcelUtil.writeToExcel(expectedValueFile, index, 2, "Pass");
                        }
                    } else if (fileType.equalsIgnoreCase("Vanilla")) {
                        String cellName = expectedValueValueList.get(0).trim();
                        String instance = expectedValueValueList.get(1).trim();
                        String rowID = expectedValueValueList.get(2).trim();
                        String expectedValue = expectedValueValueList.get(3);
                        String actualValue = null;
                        if (instance.equals("")) {

                            actualValue = XMLUtil.getcellValueFromVanilla(exportedFile, cellName, null);

                        } else if (rowID.equals("")) {
                            actualValue = XMLUtil.getcellValueFromVanilla(exportedFile, null, cellName + "," + instance);
                        } else {
                            actualValue = XMLUtil.getcellValueFromVanilla(exportedFile, null, cellName + "," + instance + "," + rowID);
                        }

                        if (!expectedValue.equalsIgnoreCase(actualValue)) {
                            compareRst = false;
                            ExcelUtil.writeToExcel(expectedValueFile, index, 4, "Fail");
                        } else {
                            ExcelUtil.writeToExcel(expectedValueFile, index, 4, "Pass");
                        }

                    } else if (fileType.equalsIgnoreCase("excel") || fileType.equalsIgnoreCase("ARBITRARY") || (fileType.equalsIgnoreCase("xbrl") && baselineFile.endsWith(".xlsx"))) {
                        try {
                            String sheetName = expectedValueValueList.get(0);
                            String cellName = expectedValueValueList.get(1).trim();
                            String instance = expectedValueValueList.get(2).trim();
                            String expectedValue = expectedValueValueList.get(3).trim();
                            if (!cellName.equals("")) {
                                if (sheetName.equals(""))
                                    sheetName = null;

                                String accValue = ExcelUtil.getCellValueByCellName(exportFile, instance, sheetName, cellName);

                                if (accValue != null) {
                                    accValue = accValue.trim();
                                    ExcelUtil.writeToExcel(expectedValueFile, index, 4, accValue);
                                } else {
                                    ExcelUtil.writeToExcel(expectedValueFile, index, 4, "Cannot find cell");
                                    ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Fail");
                                    compareRst = false;
                                }

                                try {
                                    DecimalFormat df = new DecimalFormat("###############.000");
                                    Double val = Double.parseDouble(expectedValue);
                                    String expectedValue_Tmp = df.format(val);

                                    Double expValue = Double.valueOf(expectedValue_Tmp);
                                    Double acValue = Double.parseDouble(accValue);
                                    if (Math.abs(expValue - acValue) < 0.5)
                                        ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Pass");
                                    else {
                                        compareRst = false;
                                        ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Fail");
                                        logger.error("Expected value(" + expectedValue + ") is not equal acctuall value(" + accValue + ")");
                                    }

                                } catch (Exception e) {
                                    if (accValue.equals(expectedValue))
                                        ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Pass");
                                    else {
                                        compareRst = false;
                                        ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Fail");
                                        logger.error("Expected value(" + expectedValue + ") is not equal acctuall value(" + accValue + ")");
                                    }
                                }

                                logger.info(cellName + "(instance=" + instance + " ) expected value=" + expectedValue + " ,acctual value=" + accValue);
                            }
                        } catch (Exception e) {
                        }
                    } 
                }

            }
        } else if (baselineFile.endsWith(".txt")) {

            List<String> base = TxtUtil.getFileContent(new File(baselineFile));
            List<String> exp = TxtUtil.getFileContent(new File(exportedFile));
            int baseAmt = base.size();
            int expAmt = exp.size();
            if (baseAmt != exp.size()) {
                logger.info("The row amount is different.Baseline is[" + baseAmt + "], but expected is[" + expAmt + "]");
                compareRst = false;
            } else {
                for (int i = 0; i < expAmt; i++) {
                    if (!base.get(i).equals(exp.get(i))) {
                        compareRst = false;
                        logger.info("Line " + i + 1 + ": Baseline is[" + base.get(i) + "] , but expected is[" + exp.get(i) + "]");
                        break;
                    }
                }
            }

        }
        
        else if (fileType.equalsIgnoreCase("XBRL")) {
            if (baselineFile.endsWith("xbrl"))
                compareRst = XBRLUtil.XBRLCompare(baselineFile, exportedFile);

        }


        return compareRst;
    }

    public ArrayList<ArrayList<String>> splitSumRules(String rule) {
        String left = null;
        String right = null;
        ArrayList<ArrayList<String>> exp = new ArrayList<ArrayList<String>>();
        ArrayList<String> leftPart = new ArrayList<String>();
        ArrayList<String> rightPart = new ArrayList<String>();
        if (rule.contains("=") && !rule.contains(">") && !rule.contains("<")) {


            logger.info("This is sum rule");
            left = rule.split("=")[0];
            leftPart.add(left);
            right = rule.split("=")[1];
            String rightTemp = right;
            if (rightTemp.contains("+")) {
                rightTemp = rightTemp.replace("+", "~");
            }
            if (rightTemp.contains("-")) {
                rightTemp = rightTemp.replace("-", "~");
            }
            if (rightTemp.contains("*")) {
                rightTemp = rightTemp.replace("*", "~");
            }
            if (rightTemp.contains("/")) {
                rightTemp = rightTemp.replace("/", "~");
            }

            for (String s : rightTemp.split("~")) {
                rightPart.add(s);
            }

            exp.add(leftPart);
            exp.add(rightPart);
        }


        return exp;
    }

    public ArrayList<ArrayList<String>> splitValRules(String rule, String flag) {
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
        if (leftTemp.contains("+")) {
            leftTemp = leftTemp.replace("+", "~");
        }
        if (leftTemp.contains("-")) {
            leftTemp = leftTemp.replace("-", "~");
        }
        if (leftTemp.contains("*")) {
            leftTemp = leftTemp.replace("*", "~");
        }
        if (leftTemp.contains("/")) {
            leftTemp = leftTemp.replace("/", "~");
        }

        for (String s : leftTemp.split("~")) {
            leftPart.add(s);
        }


        String rightTemp = right;
        if (rightTemp.contains("+")) {
            rightTemp = rightTemp.replace("+", "~");
        }
        if (rightTemp.contains("-")) {
            rightTemp = rightTemp.replace("-", "~");
        }
        if (rightTemp.contains("*")) {
            rightTemp = rightTemp.replace("*", "~");
        }
        if (rightTemp.contains("/")) {
            rightTemp = rightTemp.replace("/", "~");
        }

        for (String s : rightTemp.split("~")) {
            rightPart.add(s);
        }

        exp.add(leftPart);
        exp.add(rightPart);


        return exp;
    }

    public ArrayList<String> splitCell(String cell) {
        ArrayList<String> cellNameList = new ArrayList<String>();
        String returnName = "";
        String instance = "";
        String cellName = "";
        if (cell.contains("@")) {
            returnName = cell.split("@")[1];
        }
        if (cell.contains("[") && cell.contains("]")) {
            int f = cell.indexOf('[');
            int l = cell.indexOf(']');
            instance = cell.substring(f + 1, l);
            cellName = cell.substring(0, f);
        }
        if (cell.contains("@") && (!(cell.contains("[")))) {
            cellName = cell.split("@")[0];
            ;
        }
        if (!cell.contains("@") && (!(cell.contains("[")))) {
            cellName = cell;
        }


        cellNameList.add(returnName);
        cellNameList.add(instance);
        cellNameList.add(cellName);
        return cellNameList;
    }

    public boolean isNumeric(String str) {
        String temp = null;
        if (str.startsWith("-")) {
            temp = str.substring(1);
        } else {
            temp = str;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(temp);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


}	
   