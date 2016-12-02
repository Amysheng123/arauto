package com.lombardrisk.testCase;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.Business;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.TestTemplate;
import com.lombardrisk.utils.fileService.ExcelUtil;
import com.lombardrisk.utils.fileService.FileUtil;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Leo Tu on 6/17/2015.
 */
public class ExportForm extends TestTemplate {

    List<String> Files = createFolderAndCopyFile("ExportForm");
    String testDataFolder = Files.get(0);
    String checkRstFolder = Files.get(1);
    File testRstFile = new File(Files.get(2));

    public void ExportToExcel(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileType, String Module, String CheckCellValueFile, String CaseID) throws Exception, InterruptedException, DocumentException {

        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        Form = splitReturn(Form).get(2);

        boolean testRst = true;
        try {
            logger.info("============test Export[" + Form + "] To Excel=============");

            ListPage listPage = super.m.listPage;
            listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
            FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);


            String exportFilePath = formInstancePage.exportToFile(null, null, null, null, null, "excel", Module);
            if ( exportFilePath != null && CheckCellValueFile.trim().endsWith(".xlsx") ) {
                logger.info("Begin compare the exported excel with baseline");

                String source = testDataFolder + CheckCellValueFile;
                String dest = checkRstFolder + CheckCellValueFile;
                logger.info("Copy file " + source + " to " + dest);

                File expectedValueFile = new File(dest);
                if ( expectedValueFile.exists() )
                    expectedValueFile.delete();
                FileUtils.copyFile(new File(source), expectedValueFile);

                testRst = Business.verifyExportedFile(dest, exportFilePath, "excel");

            }
        } catch (RuntimeException e) {
            testRst = false;
            e.printStackTrace();
            logger.error("ExportFormToExcel:", e);
            //snapshot( "ExportFormToExcel.png");
        } finally {
            closeFormInstance();
            writeTestResultToFile(testRstFile, ID, 13, CaseID, testRst,"ExportForm");
        }
    }


    public void ExportToCsv(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileType, String Module, String BaselineFile, String CaseID) throws Exception, InterruptedException, DocumentException {

        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        Form = splitReturn(Form).get(2);
        boolean testRst = true;
        try {
            logger.info("============test Export[" + Form + "] To CSV=============");
            logger.info("Test " + Form + "_" + ProcessDate + "_" + Group);

            ListPage listPage = super.m.listPage;
            listPage.getProductListPage(Regulator, Group, Form, ProcessDate);

            FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);
            String exportFilePath = formInstancePage.exportToFile(null, null, null, null, null, "csv", Module);
            BaselineFile = testDataFolder + BaselineFile;

            if ( exportFilePath == null ) {
            } else {
                logger.info("Begin compare the downloaded csv file with baseline");
                testRst = Business.verifyExportedFile(BaselineFile, exportFilePath, "csv");
                if ( testRst ) {
                    logger.info("CSV compare result: Pass");
                } else {
                    logger.info("CSV compare result: Fail");
                }
            }
        } catch (RuntimeException e) {
            testRst = false;
            e.printStackTrace();
            logger.error("ExportFormToCsv:", e);
        } finally {
            closeFormInstance();
            writeTestResultToFile(testRstFile, ID, 13, CaseID, testRst,"ExportForm");
        }
    }


    public void ExportToRegulatorFormatInFormPage(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileType, String Module, String BaselineFile, String CheckCellValueFile, String CaseID) throws Exception {
        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        Form = splitReturn(Form).get(2);
        boolean testRst = true;
        try {
            logger.info("============test Export[" + Form + "] To regulatore format in form page=============");
            ListPage listPage = super.m.listPage;
            listPage.getProductListPage(Regulator, Group, Form, ProcessDate);
            FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, ProcessDate);

            String exportFilePath = formInstancePage.exportToFile(Regulator, Group, formCode, version, ProcessDate, FileType, Module);
            if ( exportFilePath.equalsIgnoreCase("Error") ) {
                if ( CheckCellValueFile.equalsIgnoreCase("Error") ) {
                    if ( exportFilePath.equalsIgnoreCase("Error") )
                        testRst = true;
                    else
                        testRst = false;
                }
            } else {
                if ( FileType.equalsIgnoreCase("ARBITRARY") ) {
                    exportFilePath = (new File(exportFilePath)).getParent() + "\\" + FileUtil.unCompress(exportFilePath, null).get(0);
                }

                if ( !CheckCellValueFile.endsWith(".xlsx") ) {
                } else {
                    File expectedValueFile = new File(checkRstFolder + CheckCellValueFile);
                    if ( expectedValueFile.exists() ) {
                        expectedValueFile.delete();
                    }
                    logger.info("Begin check cell value in exported file");
                    FileUtils.copyFile(new File(testDataFolder + CheckCellValueFile), expectedValueFile);

                    testRst = Business.verifyExportedFile(expectedValueFile.getAbsolutePath(), exportFilePath, FileType);

                    if ( !testRst ) {
                        copyFailedFileToTestRst(exportFilePath);
                    }
                }

                if ( BaselineFile.endsWith(".xbrl") ) {
                	BaselineFile=testDataFolder+BaselineFile;
                    testRst = Business.verifyExportedFile(BaselineFile, exportFilePath, FileType);
                    if ( !testRst ) {
                        copyFailedFileToTestRst(exportFilePath);
                    }
                }
            }

        } catch (RuntimeException e) {
            testRst = false;
            e.printStackTrace();
        } finally {
            closeFormInstance();
            writeTestResultToFile(testRstFile, ID, 13, CaseID, testRst,"ExportForm");
        }
    }


    public void ExportToRegulatorFormatInDashBorad(int ID, String Regulator, String Group, String Form, String ProcessDate, String FileType, String Framework, String Taxonomy, String Module, String BaselineFile, String CheckCellValueFile, String CaseID) throws Exception {
        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        Form = splitReturn(Form).get(2);

        boolean testRst = true;
        try {
            logger.info("============test Export[" + Form + "] To regulatore format in Dashboard=============");
            ListPage listPage = super.m.listPage;
            listPage.getProductListPage(Regulator, Group, null, null);
            String exportFilePath = listPage.ExportToRegulatorFormat(Group, formCode, version, ProcessDate, FileType, Framework, Taxonomy, Module,null);
            if ( !exportFilePath.equalsIgnoreCase("Error") ) {
                if ( FileType.equalsIgnoreCase("ARBITRARY") ) {
                    exportFilePath = (new File(exportFilePath)).getParent() + "\\" + FileUtil.unCompress(exportFilePath, null).get(0);
                }
                if ( CheckCellValueFile.endsWith(".xlsx") ) {
                    File expectedValueFile = new File(checkRstFolder + CheckCellValueFile);
                    if ( expectedValueFile.exists() ) {
                        expectedValueFile.delete();
                    }
                    logger.info("Begin check cell value in exported file");
                    FileUtils.copyFile(new File(testDataFolder + CheckCellValueFile), expectedValueFile);

                    testRst = Business.verifyExportedFile(expectedValueFile.getAbsolutePath(), exportFilePath, FileType);
                    if ( !testRst ) {
                        copyFailedFileToTestRst(exportFilePath);
                    }
                }
                if ( BaselineFile.endsWith(".xbrl") ) {
                	BaselineFile=testDataFolder+BaselineFile;
                    testRst = Business.verifyExportedFile(BaselineFile, exportFilePath, FileType);
                    if ( !testRst ) {
                        copyFailedFileToTestRst(exportFilePath);
                    }
                }
            } else if ( CheckCellValueFile.equalsIgnoreCase("Error") ) {
                if ( exportFilePath.equalsIgnoreCase("Error") )
                    testRst = true;
                else
                    testRst = false;
            }


        } catch (RuntimeException e) {
            testRst = false;
            e.printStackTrace();
            logger.error("testExportToRegulatorFormatInDashboard:", e);
        } finally {
            closeFormInstance();
            writeTestResultToFile(testRstFile, ID, 13, CaseID, testRst,"ExportForm");
        }
    }

    @Test
    public void testExportFormToFile() throws Exception {
        File testDataFile = new File("data\\ExportForm\\ExportForm.xls");
        for (int index = 1; index <= ExcelUtil.getRowNums(testDataFile, null); index++) {
            ArrayList<String> rowValue = ExcelUtil.getRowValueFromExcel(testDataFile, null, index);
            int ID = Integer.parseInt(rowValue.get(0).trim());
            String Regulator = rowValue.get(1).trim();
            String Group = rowValue.get(2).trim();
            String Form = rowValue.get(3).trim();
            String ProcessDate = rowValue.get(4).trim();
            String Run = rowValue.get(5).trim();
            String FileType = rowValue.get(6).trim();
            String GroupConsolidationType = rowValue.get(7).trim();
            String Monetary_Scale = rowValue.get(8).trim();
            String Framework = rowValue.get(9).trim();
            String Taxonomy = rowValue.get(10).trim();
            String Module = rowValue.get(11).trim();
            String ExportFrom = rowValue.get(12).trim();
            String BaselineFile = rowValue.get(13).trim();
            String CheckCellValueFile = rowValue.get(14).trim();
            String CaseID = rowValue.get(16).trim();

            Form = splitReturn(Form).get(2);
            if ( Run.trim().equalsIgnoreCase("Y") ) {
            	if(GroupConsolidationType.equals("p")){
            		String SQL="UPDATE \"ECRuFormVars\" SET \"CCValue\"='P' WHERE \"EntityName\"='"+Group+"' and \"CCName\"='Group_Consolidation_Type'";
            		DBQuery.update(SQL);
            	}else if(GroupConsolidationType.equals("I")){
            		String SQL="UPDATE \"ECRuFormVars\" SET \"CCValue\"='I' WHERE \"EntityName\"='"+Group+"' and \"CCName\"='Group_Consolidation_Type'";
            		DBQuery.update(SQL);
				}
            	
            	if(Monetary_Scale.length()>0){
            		String SQL="UPDATE \"ECRuFormVars\" SET \"CCNumber\"='P' WHERE \"EntityName\"='"+Group+"' and \"CCName\"='Monetary_Scale'";
            		DBQuery.update(SQL);
            	}
                if ( ExportFrom.equalsIgnoreCase("Dashboard")) {
                    ExportToRegulatorFormatInDashBorad(ID, Regulator, Group, Form, ProcessDate, FileType, Framework, Taxonomy, Module, BaselineFile, CheckCellValueFile, CaseID);
                } else if ( ExportFrom.equalsIgnoreCase("FormPage") ) {
                    ExportToRegulatorFormatInFormPage(ID, Regulator, Group, Form, ProcessDate, FileType, Module, BaselineFile, CheckCellValueFile, CaseID);
                }

                if ( FileType.equalsIgnoreCase("excel") ) {
                    ExportToExcel(ID, Regulator, Group, Form, ProcessDate, FileType, Module, CheckCellValueFile, CaseID);
                } else if ( FileType.equalsIgnoreCase("csv") ) {
                    ExportToCsv(ID, Regulator, Group, Form, ProcessDate, FileType, Module, BaselineFile, CaseID);
                }

            }
        }


    }


}
