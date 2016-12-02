package com.lombardrisk.testCase;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.TxtUtil;
import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Create by Leo Tu on Aug 17, 2015
 */
public class ImportForm extends TestTemplate {
    List<String> Files = createFolderAndCopyFile("ImportForm");
    String testDataFolder = Files.get(0);
    String checkRstFolder = Files.get(1);
    File testRstFile = new File(Files.get(1));
    String importFolder = new File(testDataFolder).getParent() + "\\Import\\";

    public void ImportInDashboard(int ID, String Regulator, String Group, String Form, String ProcessDate, boolean deleteForm, String FileName, String CheckCellValue, String CheckInstance, String ErrorMsg, String ErrorInfo, String CaseID, boolean addToExistValue, boolean InitialiseToZeros) throws Exception {

        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        boolean testRst = true;
        try {
            logger.info("============test ImportAdjustments from dashboard,caseID[" + CaseID + "]=============");
            logger.info("Test " + Form + "_" + ProcessDate + "_" + Group);

            String fileName = "";
            if ( CheckCellValue.endsWith(".xlsx") )
                fileName = CheckCellValue;
            if ( CheckInstance.endsWith(".xlsx") )
                fileName = CheckInstance;
            String source = testDataFolder + fileName;
            String dest = checkRstFolder + fileName;
            logger.info("Copy file " + source + " to " + dest);

            File expectedValueFile = new File(dest);
            if ( !expectedValueFile.isDirectory() ) {
                if ( expectedValueFile.exists() )
                    expectedValueFile.delete();
                FileUtils.copyFile(new File(source), expectedValueFile);
            }

            File importFile = new File(importFolder + FileName);
            if ( !importFile.exists() ) {
                logger.error("The import file " + importFile + " does not exist!");
                testRst = false;
            }

            ListPage listPage = super.m.listPage;
            listPage.getProductListPage(Regulator, Group, null, null);
            if ( deleteForm )
                listPage.deleteFormInstance(Form, ProcessDate);


            if ( ErrorMsg.length() > 1 ) {
                String error = listPage.getImportAdjustmentErrorMsg(importFile, addToExistValue);
                if ( !ErrorMsg.equals(error) ) {
                    testRst = false;
                    logger.error("The expected error message is:" + ErrorMsg + ", but actual message is:[" + error + "]");
                    ExcelUtil.writeToExcel(testRstFile, ID, 18, error);
                }
            } else if ( !ErrorInfo.equals("") && ErrorInfo.endsWith(".txt") ) {
                File txt = new File("data\\ImportForm\\CheckCellValue\\" + ErrorInfo);
                String actualInfo = listPage.getimportAdjustmentErrorInfo(importFile, addToExistValue).replace("\r", "\n");
                String expectInfo = TxtUtil.getAllContent(txt).replace("\r", "\n");

                //.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
                /*
                if(expectInfo.length()!=actualInfo.length())
                {
                	testRst = false;
                	logger.error("The expected error info is:" + expectInfo + ", but actual error info is:[" + actualInfo + "]");
                    ExcelUtil.writeToExcel(testRstFile, ID, 18, actualInfo);
                }
                else
                {
                	for(int index=1;index<expectInfo.length();index++)
                	{
                		if(!expectInfo.substring(0, index).equalsIgnoreCase(actualInfo.substring(0, index)))
                		{
                			logger.error("The expected error info is:" + expectInfo.substring(0, index) + ", but actual error info is:" + actualInfo.substring(0, index));
                			testRst = false;
                			break;
                		}
                				
                	}
                }*/
                if ( !expectInfo.equalsIgnoreCase(actualInfo) ) {
                    testRst = false;
                    logger.error("The expected error info is:" + expectInfo + ", but actual error info is:[" + actualInfo + "]");
                    ExcelUtil.writeToExcel(testRstFile, ID, 18, actualInfo);
                }

            } else {
                FormInstancePage formInstancePage = listPage.importAdjustment(importFile, addToExistValue, InitialiseToZeros);

                if ( formInstancePage != null && (CheckCellValue.endsWith(".xlsx") || CheckInstance.endsWith(".xlsx")) ) {

                    int amt = ExcelUtil.getRowNums(expectedValueFile, null);
                    for (int index = 1; index <= amt; index++) {
                        ArrayList<String> expectedValueValueList = ExcelUtil.getRowValueFromExcel(expectedValueFile, null, index);

                        if ( CheckCellValue.endsWith(".xlsx") ) {
                            String cellName = expectedValueValueList.get(0).trim();
                            String rowID = expectedValueValueList.get(1).trim();
                            String instance = (expectedValueValueList.get(2).trim().equals("")) ? null : expectedValueValueList.get(2).trim();
                            String expectedValue = expectedValueValueList.get(3).trim();


                            String extendCell = null;
                            logger.info("Verify if " + cellName + "(instance=" + instance + " rowID=" + rowID + ")=" + expectedValue);
                            if ( !rowID.equals("") ) {
                                rowID = String.valueOf(Integer.parseInt(rowID) + 48);
                                String gridName = getExtendCellName(Regulator, formCode, version, cellName);
                                extendCell = gridName + rowID + cellName;

                            }
                            boolean findCell = true;
                            String accValue_tmp = formInstancePage.getCellText(Regulator, formCode, version, instance, cellName, extendCell);
                            String accValue = "";
                            if ( accValue_tmp == null ) {
                                ExcelUtil.writeToExcel(expectedValueFile, index, 4, "Cannot find cell");
                                findCell = false;
                            } else {
                                try {
                                    accValue = String.valueOf(Math.round(Float.parseFloat(accValue_tmp)));
                                } catch (Exception e) {
                                    accValue = accValue_tmp;
                                }

                                ExcelUtil.writeToExcel(expectedValueFile, index, 4, accValue);
                            }

                            if ( findCell ) {
                                if ( !(accValue.equalsIgnoreCase(expectedValue)) ) {

                                    ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Fail");
                                    testRst = false;
                                } else {
                                    ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Pass");

                                }

                            }
                            logger.info(String.format("%s(instance=%s rowID=%s) expected value=%s ,acctual value=%s", cellName, instance, rowID, expectedValue, accValue));
                        }

                        if ( CheckInstance.endsWith(".xlsx") ) {
                            String page = expectedValueValueList.get(0).trim();
                            String expectInstance = expectedValueValueList.get(1).trim();
                            List<String> instances = formInstancePage.getAllInstance(page);
                            String actualInstance = "";
                            for (String item : instances) {
                                actualInstance = actualInstance + item + "#";
                            }
                            actualInstance = actualInstance.substring(0, actualInstance.length() - 1);
                            ExcelUtil.writeToExcel(expectedValueFile, index, 2, actualInstance);

                            if ( !actualInstance.equalsIgnoreCase(expectInstance) ) {
                                testRst = false;
                                ExcelUtil.writeToExcel(expectedValueFile, index, 3, "Fail");
                            } else
                                ExcelUtil.writeToExcel(expectedValueFile, index, 3, "Pass");
                        }

                    }
                } else if ( formInstancePage != null && !CheckCellValue.endsWith("xlsx") ) {
                    /*
                    // Test for allow null
                    String cellName = CheckCellValue;
                    String expectedValue = "1";
                    formInstancePage.editCellValue(null, cellName, null, expectedValue);
                    String accValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, null);
                    if ( !accValue.equalsIgnoreCase(expectedValue) )
                        testRst = false;


                    formInstancePage.editCellValue(null, cellName, null, null);
                    accValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, null);
                    if ( accValue.equalsIgnoreCase("Null") ) {
                    } else {
                        testRst = false;
                    }*/
                }

            }


        } catch (RuntimeException e) {
            testRst = false;
            e.printStackTrace();
        } finally {
            closeFormInstance();
            writeTestResultToFile(testRstFile, ID, 16, CaseID, testRst,"ImportForm");
        }
    }


    public void ImportInFormPage(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileName, String CheckCellValue, String CheckInstance, String ErrorMsg, String ErrorInfo, String CaseID, boolean addToExistValue, boolean InitialiseToZeros, String FormLocked) throws Exception {
        boolean testRst = true;
        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        try {
            logger.info("============test ImportAdjustments from dashboard,caseID[" + CaseID + "]=============");
            logger.info("Test " + Form + "_" + ProcessDate + "_" + Group);

            File importFile = new File(importFolder + FileName);
            if ( !importFile.exists() ) {
                logger.error("The import file " + importFile + " deos not exist!");
                testRst = false;
            }

            ListPage listPage = super.m.listPage;
            listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
            FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

            if ( ErrorMsg.length() > 1 ) {
                if ( !ErrorMsg.equals(formInstancePage.getImportAdjustmentErrorMsg(importFile)) ) {
                    testRst = false;
                }
            } else if ( !ErrorInfo.equals("") && ErrorInfo.endsWith(".txt") ) {
                File txt = new File("data\\ImportForm\\CheckCellValue\\" + ErrorInfo);
                String actualInfo = formInstancePage.getImportAdjustmentErrorInfo(importFile).replaceAll("(\r\n|\r|\n|\n\r)", "<br>");

                String expectInfo = TxtUtil.getAllContent(txt).replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
                if ( !expectInfo.equals(actualInfo) ) {
                    testRst = false;
                    logger.error("The expected error info is:" + expectInfo + ", but actual error info is:[" + actualInfo + "]");
                }
            } else if ( FormLocked.equalsIgnoreCase("Y") ) {
                if ( formInstancePage.isImportAdjustmentEnabled() ) {
                    testRst = false;
                    logger.error("Import adjustment should be disabled");
                }
            } else {
                boolean imported = formInstancePage.importAdjustment(importFile, addToExistValue, InitialiseToZeros);

                if ( imported && (CheckCellValue.endsWith(".xlsx") || CheckInstance.endsWith(".xlsx")) ) {
                    String fileName = "";
                    if ( CheckCellValue.endsWith(".xlsx") )
                        fileName = CheckCellValue;
                    if ( CheckInstance.endsWith(".xlsx") )
                        fileName = CheckInstance;
                    String source = testDataFolder + fileName;
                    String dest = checkRstFolder + fileName;
                    logger.info("Copy file " + source + " to " + dest);

                    File expectedValueFile = new File(dest);
                    if ( !expectedValueFile.isDirectory() ) {
                        if ( expectedValueFile.exists() )
                            expectedValueFile.delete();
                        FileUtils.copyFile(new File(source), expectedValueFile);
                    }

                    int amt = ExcelUtil.getRowNums(expectedValueFile, null);

                    for (int index = 1; index <= amt; index++) {
                        ArrayList<String> expectedValueValueList = ExcelUtil.getRowValueFromExcel(expectedValueFile, null, index);
                        if ( CheckCellValue.endsWith(".xlsx") ) {
                            String cellName = expectedValueValueList.get(0).trim();
                            String rowID = expectedValueValueList.get(1).trim();
                            String instance = (expectedValueValueList.get(2).trim().equals("")) ? null : expectedValueValueList.get(2).trim();
                            String expectedValue = expectedValueValueList.get(3).trim();


                            String extendCell = null;
                            logger.info("Verify if " + cellName + "(instance=" + instance + " rowID=" + rowID + ")=" + expectedValue);
                            if ( !rowID.equals("") ) {
                                rowID = String.valueOf(Integer.parseInt(rowID) + 48);
                                String gridName = getExtendCellName(Regulator, formCode, version, cellName);
                                extendCell = gridName + rowID + cellName;

                            }
                            boolean findCell = true;
                            String accValue_tmp = formInstancePage.getCellText(Regulator, formCode, version, instance, cellName, extendCell);
                            String accValue = "";
                            if ( accValue_tmp != null ) {
                                try {
                                    accValue = String.valueOf(Math.round(Float.parseFloat(accValue_tmp)));
                                } catch (Exception e) {
                                    accValue = accValue_tmp;
                                }

                                ExcelUtil.writeToExcel(expectedValueFile, index, 4, accValue);
                            } else {
                                ExcelUtil.writeToExcel(expectedValueFile, index, 4, "Cannot find cell");
                                findCell = false;
                            }

                            if ( findCell ) {
                                if ( !accValue.equalsIgnoreCase(expectedValue) ) {

                                    ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Fail");
                                    testRst = false;
                                } else {
                                    ExcelUtil.writeToExcel(expectedValueFile, index, 5, "Pass");

                                }

                            }
                            logger.info(cellName + "(instance=" + instance + " rowID=" + rowID + ") expected value=" + expectedValue + " ,acctual value=" + accValue);
                        }

                        if ( CheckInstance.endsWith(".xlsx") ) {
                            String page = expectedValueValueList.get(0).trim();
                            String expectInstance = expectedValueValueList.get(1).trim();
                            List<String> instances = formInstancePage.getAllInstance(page);
                            String actualInstance = "";
                            for (String item : instances) {
                                actualInstance = actualInstance + item + "#";
                            }
                            actualInstance = actualInstance.substring(0, actualInstance.length() - 1);
                            ExcelUtil.writeToExcel(expectedValueFile, index, 2, actualInstance);

                            if ( !actualInstance.equalsIgnoreCase(expectInstance) ) {
                                testRst = false;
                                ExcelUtil.writeToExcel(expectedValueFile, index, 3, "Fail");
                            } else
                                ExcelUtil.writeToExcel(expectedValueFile, index, 3, "Pass");
                        }

                    }
                } else if ( imported && !CheckCellValue.endsWith("xlsx") ) {
                	/*
                    // Test for allow null
                    String cellName = CheckCellValue;
                    String expectedValue = "1";
                    formInstancePage.editCellValue(null, cellName, null, expectedValue);
                    String accValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, null);
                    if ( !accValue.equalsIgnoreCase(expectedValue) )
                        testRst = false;


                    formInstancePage.editCellValue(null, cellName, null, null);
                    accValue = formInstancePage.getCellText(Regulator, formCode, version, null, cellName, null);
                    if ( !accValue.equalsIgnoreCase("Null") )
                        testRst = false;
                        */
                }
            }

        } catch (RuntimeException e) {
            testRst = false;
            e.printStackTrace();
        } finally {
            closeFormInstance();
            writeTestResultToFile(testRstFile, ID, 16, CaseID, testRst,"ImportForm");
        }
    }


    @Test
    public void testImportAdjustment() throws Exception, InterruptedException, DocumentException {
        File testDataFile = new File("data\\ImportForm\\ImportForm.xls");
        for (int index = 1; index <= ExcelUtil.getRowNums(testDataFile, null); index++) {
            ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
            int ID = Integer.parseInt(rowValue.get(0).trim());
            String Regulator = rowValue.get(1).trim();
            String Group = rowValue.get(2).trim();
            String Form = rowValue.get(3).trim();
            String ProcessDate = rowValue.get(4).trim();
            String Run = rowValue.get(5).trim();
            String ImportFrom = rowValue.get(6).trim();
            String ReplaceORAdd = rowValue.get(8).trim();
            boolean InitialiseToZeros = false;
            if ( rowValue.get(9).trim().equalsIgnoreCase("Y") )
                InitialiseToZeros = true;
            String formLocked = rowValue.get(10).trim();
            String ImportFileName = rowValue.get(11).trim();
            String CheckCellValue = rowValue.get(12).trim();
            String CheckInstance = rowValue.get(13).trim();
            String ErrorMessage = rowValue.get(14).trim();
            String ErrorInfo = rowValue.get(15).trim();
            String CaseID = rowValue.get(17).trim();

            boolean deleteForm = false;
            if ( rowValue.get(7).trim().equalsIgnoreCase("Y") )
                deleteForm = true;

            Form = splitReturn(Form).get(2);
            boolean addTo = false;
            if ( Run.trim().equalsIgnoreCase("Y") ) {
                if ( ReplaceORAdd.equalsIgnoreCase("A") )
                    addTo = true;

                if ( ImportFrom.equalsIgnoreCase("Dashboard") )
                    ImportInDashboard(ID, Regulator, Group, Form, ProcessDate, deleteForm, ImportFileName, CheckCellValue, CheckInstance, ErrorMessage, ErrorInfo, CaseID, addTo, InitialiseToZeros);

                else if ( ImportFrom.equalsIgnoreCase("FormPage") )
                    ImportInFormPage(ID, Regulator, Group, Form, ProcessDate, ImportFileName, CheckCellValue, CheckInstance, ErrorMessage, ErrorInfo, CaseID, addTo, InitialiseToZeros, formLocked);
            }

        }

    }

}
   