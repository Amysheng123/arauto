package com.lombardrisk.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

import com.lombardrisk.utils.DBQuery;

/**
 * Created by Kevin Ling on 2/16/15.
 * Refactored by Leo Tu on 2/1/16
 */
public class FormInstancePage extends AbstractPage {

    public FormInstancePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public ListPage closeFormInstance() throws Exception {
    	if(element("fp.form").isDisplayed()){
    		if (element("fp.message").isDisplayed()) {
                logger.info("Close message");
                element("fp.message").moveTo();
                element("fp.closeMessage").click();
            }

            if (element("fp.ajaxstatusDlg_modal").isDisplayed()) {
                waitThat("fp.ajaxstatusDlg_modal").toBeInvisible();
            }

            if (element("fp.importDlgmodal").isDisplayed()) {
                waitThat("fp.importDlgmodal").toBeInvisible();
            }

            if (element("fp.close").isDisplayed()) {
                logger.info("Close form");
                element("fp.close").click();
            }
            waitStatusDlg();
    	}
        
        return new ListPage(getWebDriverWrapper());
    }


    public AdjustLogPage getDrillDown() throws Exception {
        if (!element("fp.exportAdjustment").isDisplayed()) {
            element("fp.adjustment").click();
            //waitThat("fp.adjLog").toBeVisible();
            element("fp.adjLog").click();
            waitStatusDlg();
        }

        return new AdjustLogPage(getWebDriverWrapper());
    }

    public void cellClick(String cellId) throws Exception {
        element("fp.cellID", cellId).click();
        waitStatusDlg();
    }

    public void cellClick(String Regulator, String form, String version, String instance, String cellName, String extendCell) throws Exception {
        getPageNameByCell(Regulator, form, version, instance, cellName, extendCell);
        element("fp.cellID", cellName).click();
        waitStatusDlg();
    }

    /*
     * get cell real value
     */
    public String getCellText(String cellId) throws Exception {
        String cellValue = null;
        try {
            String part1 = null;
            if (element("fp.inputCell", cellId).getAttribute("type").equals("checkbox"))
                part1 = element("fp.inputCell", cellId).getAttribute("onfocus").replace("onCellFocus", "");
            else
                part1 = element("fp.inputCell", cellId).getAttribute("onfocus").replace("hideScale();onCellFocus", "");
            String part2 = part1.substring(1, part1.length() - 2).replace("\',\'", "`");
            String[] rsts = part2.split("`");
            cellValue = rsts[2];
            if (cellValue.equals("")) {
                cellValue = element("fp.inputCell", cellId).getAttribute("value");
            }

        } catch (NoSuchElementException e) {
            cellValue = element("fp.selectCell", cellId).getSelectedText();
        }

        if (cellValue.equals("checked")) {
            cellValue = "1";
        }
        if (cellValue.equals("unchecked")) {
            cellValue = "0";
        }
        return cellValue;
    }


    /*
     * get cell displayed value
     */
    public String getCellDisplayedText(String cellId) throws Exception {
        String cellValue = null;
        if (element("fp.inputCell", cellId).getAttribute("type").equals("checkbox")) {
            String part1 = element("fp.inputCell", cellId).getAttribute("onfocus").replace("onCellFocus", "");
            String part2 = part1.substring(1, part1.length() - 2).replace("\',\'", "`");
            String[] rsts = part2.split("`");
            if (rsts[2].equals("checked")) {
                cellValue = "1";
            }
            if (rsts[2].equals("unchecked")) {
                cellValue = "0";
            }
        } else
        	cellValue = element("fp.inputCell", cellId).getAttribute("value");
            

        return cellValue;
    }


    /*
     * get cell real value
     */
    public String getCellText(String Regulator, String formCode, String version, String instance, String cellId, String extendCell) throws Exception {
        String cellPage = getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);
        logger.info("Cell[" + cellId + "] in page[" + cellPage + "]");
        String value="";
        String cellName;
        if (extendCell != null)
            cellName = extendCell;
        else
            cellName = cellId;
        if(element("fp.inputCell", cellName).isPresent())
        	value=element("fp.inputCell", cellName).getAttribute("value");
        else if(element("fp.selectCell", cellName).isPresent())
        	value=element("fp.selectCell", cellName).getSelectedText();
        return value;
    }


    public AllocationPage cellDoubleClick(String cellId) throws Exception {
        element("fp.inputCell", cellId).doubleClick();
        waitStatusDlg();
        return new AllocationPage(getWebDriverWrapper());
    }
    
    public boolean isAllocationDisplayed(String cellId) throws Exception {
        element("fp.inputCell", cellId).doubleClick();
        waitStatusDlg();
        return element("fp.allocation").isDisplayed();
    }

    public AllocationPage cellDoubleClick(String Regulator, String formCode, String version, String instance, String cellId, String extendCell) throws Exception {
        getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);
        selectInstance(instance);
        if (extendCell != null)
            element("fp.inputCell", extendCell).doubleClick();
        else
            element("fp.inputCell", cellId).doubleClick();
        return new AllocationPage(getWebDriverWrapper());
    }

    public void editCellValue(String Regulator, String formCode, String version, String instance, String cellId, String extendCell, boolean tick) throws Exception {
        if (tick) {
            if (!getCellText(Regulator, formCode, version, instance, cellId, extendCell).equals("1")) {
                element("fp.inputCell", cellId).click();
            }
        } else {
            if (getCellText(Regulator, formCode, version, instance, cellId, extendCell).equals("1")) {
                element("fp.inputCell", cellId).click();
            }
        }
        cellEditOkBtnClick();
    }

    public void editCellValue(String cellId, String text) throws Exception {
        element("fp.inputCell", cellId).click();
        waitThat().timeout(500);
        if(element("fp.inputCell", cellId).isPresent())
        	element("fp.editCell", cellId).input(text);
        else if(element("fp.selectCell", cellId).isPresent())
        	element("fp.selectCell", cellId).selectByVisibleText(text);
        
        cellEditOkBtnClick();

    }

    public void editCellValue(String cellId, String text, String comment) throws Exception {
        element("fp.inputCell", cellId).click();
        waitThat().timeout(500);
        String id = null;
        String[] ids = new String[]{"numericCellEditor", "longCellEditor", "dateCellEditor_input", "stringCellEditor", "memoCellEditor", "selectCellEditor"};
        for (String idName : ids) {
            if (element("fp.cellID", idName).getAttribute("style").contains("width")) {
                id = idName;
                break;
            }
        }

        if (id != null) {
            logger.info("id of input is:" + id);

            if (!id.equalsIgnoreCase("selectCellEditor")) {
                if (id.equalsIgnoreCase("dateCellEditor_input")) {
                    element("fp.cellID", id).input(text);
                    waitThat().timeout(300);
                    clickToday();
                    waitThat().timeout(200);
                }
                if (text == null) {
                    element("fp.cellID", id).clear();
                } else {
                    element("fp.cellID", id).input(text);
                }
            } else {
                element("fp.cellID", id).click();
                waitThat().timeout(500);
                element("fp.cellID", id).selectByVisibleText(text);
            }

            if (comment != null) {
                if (!comment.equals("")) {
                    element("fp.comment").input(comment);
                }
            }
            cellEditOkBtnClick();
            if (id.equalsIgnoreCase("dateCellEditor_input"))
                element("fp.cellID", id).click();
            waitStatusDlg();
        }
    }


    public void editCellValue(String instance, String cellId, String extendCell, String text) throws Exception {
        logger.info("Begin edit cell[" + cellId + "]=" + text + "");
        String Form = getFormInfo();
        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        String Regulator = element("fp.regulator").getInnerText().split("/")[0];
        Regulator = Regulator.substring(0, Regulator.length() - 1);
        String pageName = getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);

        if (pageName != null) {
            if (extendCell != null)
                cellId = extendCell;

            editCellValue(cellId, text);
        }

    }

    public void editCellValue(String instance, String cellId, String extendCell, String text, String comment) throws Exception {
        logger.info("Begin edit cell[" + cellId + "]=" + text + "");
        String Form = getFormInfo();
        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        String Regulator = element("fp.regulator").getInnerText().split("/")[0];
        Regulator = Regulator.substring(0, Regulator.length() - 1);
        String pageName = getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);

        if (pageName != null) {
            if (extendCell != null)
                cellId = extendCell;

            editCellValue(cellId, text, comment);
        }

    }

    public String editCellValueGetErrorMsg(String instance, String cellId, String extendCell, String text, String comment) throws Exception {
        logger.info("Begin edit cell[" + cellId + "]=" + text + "");
        String Form = getFormInfo();
        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        String Regulator = element("fp.regulator").getInnerText().split("/")[0];
        Regulator = Regulator.substring(0, Regulator.length() - 1);
        String pageName = getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);

        if (pageName != null) {
            if (extendCell != null)
                cellId = extendCell;

            editCellValue(cellId, text, comment);
        }

        return element("fp.message").getInnerText();

    }

    public boolean isCellEditing(String cellId) throws Exception {
        element("fp.inputCell", cellId).click();
        waitThat().timeout(300);
        return element("fp.cellEditorContainer").isDisplayed();
    }


    public void cellEditOkBtnClick() throws Exception {
        element("fp.cellEditSave").click();
        waitStatusDlg();
    }

    public void cellEditCancelBtnClick() throws Exception {
        element("fp.cellEditCancel").click();
    }

    public String getCellAttribute(String cellID, String attribute) throws Exception {
        return element("fp.cellID", cellID).getAttribute(attribute);
    }

    public boolean isShowNull(String cellId) throws Exception {
        return "NULL".equals(getCellAttribute(cellId, "value"));
    }

    private void moveOverToCell(String cellId) throws Exception {
        element("fp.cellID", cellId).moveTo();
        waitThat().timeout(1000);
    }

    private void addRowClick() throws Exception {
        element("fp.addRowIcon").click();
        waitStatusDlg();
    }


    public boolean isAddRowShow() throws Exception {
        return element("fp.addRowMenu").isDisplayed();
    }

    private void addRowMenuClick(int index) throws Exception {
        if (index > 3 || index < 0)
            return;
        if (index == 1)
            element("fp.inRowAbo").click();
        if (index == 2)
            element("fp.inRowBel").click();
        if (index == 3)
            element("fp.deleteRow").click();
        waitStatusDlg();
    }

    private void deleteRowConfirmClick() throws Exception {
        element("fp.delRowConfirm").click();
        waitStatusDlg();
    }


    public ArrayList<String> getPageNames() throws Exception {
        ArrayList<String> pageNames = new ArrayList<String>();
        int amt = (int) element("fp.pageTab").getRowCount();
        for (int i = 1; i <= amt; i++) {
            pageNames.add(element("fp.pageName", String.valueOf(i)).getInnerText());
        }

        return pageNames;
    }



    public void liveValidationClick() throws Exception {
        if (!element("fp.liveVal").getAttribute("class").contains("liveValidationBtnOutsetClass"))
            element("fp.liveVal").click();
        waitStatusDlg();
    }

    public boolean checkLiveVlidationHilight() throws Exception {
        if (element("fp.liveVal").getAttribute("class").contains("liveValidationBtnOutsetClass"))
            return true;
        else
            return false;
    }

    public boolean checkValidationNowEnable() throws Exception {
        try {
            if (element("fp.valNowBtn").getAttribute("aria-disabled").equals("true"))
                return false;
            else
                return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isLiveValidationShow() throws Exception {
        try {
            element("fp.liveVal").getAttribute("class");
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void validationNowClick() throws Exception {
        logger.info("Click validate now button");
        element("fp.valNowBtn").click();
        waitStatusDlg();
    }


    public boolean isImportAdjustmentEnabled() throws Exception {
        boolean visible = true;
        try {
            if (element("fp.adjustment").isDisplayed()) {
                element("fp.adjustment").click();
                waitStatusDlg();
                if (element("fp.import").getAttribute("class").contains("ui-state-disabled"))
                    visible = false;
                else if (!element("fp.import").isDisplayed())
                    visible = false;

            } else
                visible = false;
        } catch (Exception e) {

            visible = false;
        }

        return visible;

    }

    public boolean isViewAdjustmentLogDisplayed() throws Exception {
        boolean visible = true;
        if (element("fp.adjustment").isDisplayed()) {
            element("fp.adjustment").click();
            if (!element("fp.adjLog").isDisplayed())
                visible = false;
        } else
            visible = false;

        return visible;

    }


    public boolean isLcokDisplayed() throws Exception {
        try {
            return element("fp.lock").isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isExportToFileDisplayed() throws Exception {
        try {
            element("fp.expToFile").click();
            waitStatusDlg();
            element("fp.expToFile").click();
            if (element("fp.expToXls").isDisplayed() && element("fp.expToCSV").isDisplayed())
                return true;
            else
                return false;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    public boolean isAddInstanceBtnDisplayed() throws Exception {
        try {
            return element("fp.addInst").isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isDeleteInstanceBtnDisplayed() throws Exception {
        try {
            return element("fp.delInst").isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isApproveRejectDisplayed() throws Exception {
        boolean visible = false;
        try {
            element("fp.wfBtn").click();
            try {
                if (element("fp.readyAppr").isDisplayed()) {
                    element("fp.readyAppr").click();
                    waitStatusDlg();
                }
            } catch (NoSuchElementException e) {
            }

            if (element("fp.approve").isDisplayed() && element("fp.reject").isDisplayed())
                visible = true;
        } catch (NoSuchElementException e) {
            visible = false;
        }
        return visible;
    }

    public boolean isReadyApproveDisplayed() throws Exception {
        element("fp.wfBtn").click();
        waitThat().timeout(300);
        if (element("fp.readyAppr").isDisplayed())
            return true;
        else
            return false;
    }


    public void lockClick() throws Exception {
        try {
            element("fp.lock").click();
        } catch (NoSuchElementException e) {
            try {
                throw new Exception("The FormInstance has been locked already!");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        waitStatusDlg();
    }

    public void unlockClick() throws Exception {
        if (element("fp.unLock").isDisplayed()) {
            element("fp.unLock").click();
            waitStatusDlg();
        } else
            logger.info("The FormInstance has been unlocked already!");

    }

    private void addPageInstanceClick() throws Exception {
        element("fp.addInst").click();
    }

    public void deletePageInstanceClick() throws Exception {
        element("fp.delInst").click();
        waitThat("fp.delInstConf").toBeVisible();
        element("fp.delInstConf").click();

    }

    private void addPageInstanceConfirmBtnClick() throws Exception {
        element("fp.addInstConf").click();
        waitStatusDlg();
    }

    private void deletePageInstanceConfirmBtnClick() throws Exception {
        element("fp.delInstConf").click();
        waitStatusDlg();
    }

    private void inputInstanName(String instanceName) throws Exception {
        if (!element("fp.select.addInstance").isDisplayed()) {
            logger.info("Inputed instance is : " + instanceName);
            element("fp.instName").type(instanceName);
            element("fp.addInstAcc").click();
        } else {
            logger.info("Selected instance is : " + instanceName);
            element("fp.select.addInstance").selectByVisibleText(instanceName);
            waitThat().timeout(600);
            element("fp.accept.addInstance").click();
        }
        waitStatusDlg();
    }

    public void insertRow(String cellId) throws Exception {
        moveOverToCell(cellId);
        addRowClick();
        element("fp.insertRow").click();
        waitStatusDlg();
    }


    public void insertRowAbove(String cellId) throws Exception {
        moveOverToCell(cellId);
        addRowClick();
        addRowMenuClick(1);
        waitStatusDlg();
    }

    public void insertRowBelow(String cellId) throws Exception {
        moveOverToCell(cellId);
        addRowClick();
        addRowMenuClick(2);
        waitStatusDlg();
    }

    public void deleteRow(String cellId) throws Exception {
        moveOverToCell(cellId);
        addRowClick();
        addRowMenuClick(3);
        deleteRowConfirmClick();
        waitStatusDlg();
    }

    public void deletePageInstance(String instance) throws Exception {
        selectInstance(instance);
        element("fp.delInst").click();
        waitStatusDlg();
        element("fp.delInstConf").click();
        waitStatusDlg();
    }

    public boolean checkAdjustmentLog(String cellId, String fromValue, String toValue) throws Exception {
        boolean rst = false;
        boolean c1 = false;
        boolean c2 = false;
        boolean c3 = false;
        AdjustLogPage adjustLogPage = getDrillDown();

        element("fp.cellFilter").input(cellId);
        element("fp.cellFilter").type(Keys.ENTER);
        waitStatusDlg();
        logger.info("Order adjustment table by edit time desc");
        adjustLogPage.orderByTimeDesc();
        waitStatusDlg();


        logger.info("Check if cellName=" + cellId);
        if (adjustLogPage.getCellName(1).trim().equals(cellId)) {
            c1 = true;
        }


        logger.info("Check if cell value=" + fromValue);
        if (adjustLogPage.getValue(1).trim().equals(fromValue)) {
            c2 = true;
        }


        logger.info("Check if cell value=" + toValue);
        if (adjustLogPage.getModifiedTo(1).trim().equals(toValue)) {
            c3 = true;
        }

        if (c1 & c2 & c3)
            rst = true;
        return rst;
    }

    public boolean checkFormInstanceEditable(String cellId) throws Exception {

        element("fp.inputCell", cellId).click();
        if (element("fp.cellEditArea").isDisplayed()) {
            return false;
        }
        return true;
    }

    public ListImportFilePage getImportPageInReturn() throws Exception {
        element("fp.adjustment").click();
        waitThat().timeout(1000);
        element("fp.import").click();
        return new ListImportFilePage(getWebDriverWrapper());

    }


    public boolean isLockedInReturn() throws Exception {
        try {
            if (element("fp.unLock").isDisplayed())
                return true;
            else
                return false;

        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isEnable_Add_Del_Instance(String type) throws Exception {
        try {
            if (type.equalsIgnoreCase("add")) {
                if (element("fp.addInst").getAttribute("aria-disabled").equals("false"))
                    return true;
                else
                    return false;
            } else {
                if (element("fp.delInst").getAttribute("aria-disabled").equals("false"))
                    return true;
                else
                    return false;
            }
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void selectInstance(String instance) throws Exception {
    	if(instance!=null)
    	{
    		if (!getCurrentPageInstance().equalsIgnoreCase(instance) ){
        		logger.info("Select instance "+instance);
        		element("fp.curInst").click();
                waitStatusDlg();
                element("fp.selectInstace", instance).click();
                waitStatusDlg();
        	}
    	}
        
    }

    public String getCurrentPageInstance() throws Exception {
        return element("fp.curInst").getInnerText();

    }


    public int getTableRowAmt(String by) throws Exception {
        return (int) element(by).getRowCount();
    }

    public int getExtGDRowAmt() throws Exception {
        int RowAmtF = getTableRowAmt("fp.extGridTab");
        if (element("fp.lastPageSta").getAttribute("tabindex").equals("0")) {
            int PageAmt = 0;
            try {
                PageAmt = Integer.parseInt(element("fp.lastPageNO").getInnerText());
            } catch (Exception e) {
                PageAmt = Integer.parseInt(element("fp.lastPageNO2").getInnerText());
            }
            int RowAmtL = getTableRowAmt("fp.extGridTab");
            return RowAmtF * (PageAmt - 1) + RowAmtL;
        } else
            return RowAmtF;
    }


    public void selectPage(String page) throws Exception {
    	if (!isPageHighlight(page)){
    		logger.info("Click page :" + page);
            element("fp.pageLocator", page).click();
            waitStatusDlg();
    	}
    }



    public boolean editCell_Checklog(String Regulator, String Group, String ProcessDate, String formCode, String version, String cellName, String extendCell) throws Exception {
        String beforeEdit = getCellText(Regulator, formCode, version, "", cellName, extendCell);

        String editValue = null;
        String editValue_p1 = beforeEdit.substring(0, beforeEdit.length() - 1);
        String editValue_p2 = beforeEdit.substring(beforeEdit.length() - 1);
        if (beforeEdit.contains(".")) {
            editValue_p1 = beforeEdit.split(".")[0];
            editValue_p2 = editValue_p1.substring(editValue_p1.length() - 1);
        }
        try {
            if (Integer.parseInt(editValue_p2) >= 1 && Integer.parseInt(editValue_p2) < 9)
                editValue_p2 = (String.valueOf((Integer.parseInt(editValue_p2) + 1)));
            else if (Integer.parseInt(editValue_p2) > 9)
                editValue_p2 = (String.valueOf((Integer.parseInt(editValue_p2) - 1)));


            editValue = editValue_p1 + editValue_p2;
        } catch (Exception e) {
            editValue = beforeEdit + "1";
        }

        logger.info("Edit cell=" + cellName + " value=" + editValue);
        logger.info("Verify if " + cellName + "=" + editValue);
        editCellValue(null, cellName, extendCell, editValue);

        String modifiedValue = getCellText(Regulator, formCode, version, null, cellName, extendCell);


        if (modifiedValue.equals(editValue)) {
            logger.info("Begin check adjustment log ");
            if (checkAdjustmentLog(cellName, beforeEdit, editValue))
                return true;
            else
                return false;
        } else
            return false;

    }

    public EditionManagePage openEditionManage() throws Exception {
        element("fp.editionIcon").click();
        waitStatusDlg();
        return new EditionManagePage(getWebDriverWrapper());
    }

    public FormInstancePage selectEditionFromManage(String edition) throws Exception {
        return new FormInstancePage(getWebDriverWrapper());
    }

    public FormInstancePage selectEditionFromDropList(String edition) throws Exception {
        element("fp.selectEdition").selectByVisibleText(edition);
        waitStatusDlg();
        return new FormInstancePage(getWebDriverWrapper());
    }


    public boolean editionExistInDropdownList(String edition) throws Exception {
        if (element("fp.selectEdition").getAllOptionTexts().contains(edition))
            return true;
        else
            return false;
    }

    public String getCurrentEditionInfo() throws Exception {
        return element("fp.curEdition").getInnerText();
    }

    public boolean isEnableEditeCell(String cellId) throws Exception {
        boolean editeEnable = false;
        element("fp.inputCell",cellId).click();
        waitStatusDlg();
        if (element("fp.cellEditorContainer").isDisplayed())
            editeEnable = true;
        return editeEnable;
    }

    public boolean addInstance(String instanceName) throws Exception {
        boolean rst = false;
        addPageInstanceClick();
        inputInstanName(instanceName);

        if (getCurrentPageInstance().equals(instanceName))
            rst = true;

        return rst;
    }


    public boolean selectEditionFomDropdownList(int m) throws Exception {
        List<String> allEditions = element("fp.selectEdition").getAllOptionTexts();
        String selectedEdition = allEditions.get(m - 1);
        element("fp.selectEdition").selectByIndex(m - 1);
        waitStatusDlg();
        if (element("fp.curEdition").getInnerText().equals(selectedEdition))
            return true;
        else
            return false;
    }


    public void turnOnScale() throws Exception {
        logger.info("Turn on show scale button");
        if (!element("fp.scaleStatus").getAttribute("class").contains("slideBarSetTrue")) {
            element("fp.chanScaleStatus").click();
            waitStatusDlg();
        }
    }

    public void turnOffScale() throws Exception {
        logger.info("Turn off show scale button");
        if (element("fp.scaleStatus").getAttribute("class").contains("slideBarSetTrue")) {
            element("fp.chanScaleStatus").click();
            waitStatusDlg();
        }
    }


    public boolean importAdjustment(File importFile, boolean addToExistValue, boolean InitialiseToZeros) throws Exception {
        boolean importRst = true;
        String type = "importFileForm";
        ListImportFilePage importFileInReturnPage = getImportPageInReturn();
        try {
            importFileInReturnPage.setImportFile(importFile, type);
            if (addToExistValue)
                importFileInReturnPage.selectAddToExistingValue(type);
            if (InitialiseToZeros)
                importFileInReturnPage.tickInitToZero(type);
            importFileInReturnPage.importFileBtnClick(type);
        } catch (Exception e) {
            importRst = false;
            importFileInReturnPage.closeImportFileDlg(type);
            e.printStackTrace();
        }
        waitStatusDlg();

        return importRst;
    }

    public String getImportAdjustmentErrorMsg(File importFile) throws Exception {
        String type = "importFileForm";
        String error = null;
        ListImportFilePage importFileInReturnPage = getImportPageInReturn();
        try {

            importFileInReturnPage.setImportFile(importFile, type);
            error = importFileInReturnPage.getErrorMessage(type);
            if (error.length() == 0) {
                importFileInReturnPage.importFileBtnClick(type);
                error = importFileInReturnPage.getErrorMessage(type);
            }
            importFileInReturnPage.closeImportFileDlg(type);
        } catch (Exception e) {
            importFileInReturnPage.closeImportFileDlg(type);
            e.printStackTrace();
        }
        return error;
    }

    public String getImportAdjustmentErrorInfo(File importFile) throws Exception {
        String type = "importFileForm";
        String info = null;
        ListImportFilePage importFileInReturnPage = getImportPageInReturn();
        try {
            importFileInReturnPage.setImportFile(importFile, type);
            info = importFileInReturnPage.getErrorInfo(type);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            importFileInReturnPage.closeImportFileDlg(type);
        }
        return info;
    }

    public String getSelectedPage() throws Exception {
        return element("fp.selectedPage").getInnerText();
    }

    private String getPageNameByCell(String Regulator, String form, String version, String instance, String cellName, String extendCell) throws Exception {
        String page = null;
        String SQL = null;

        if (extendCell == null) {
            String pageName = null;
            if (connectedDB.equalsIgnoreCase("ar")) {
                String ID_Start = getRegulatorIDRangeStart(Regulator);
                String ID_End = getRegulatorIDRangEnd(Regulator);
                SQL = "select \"PageName\" from \"CFG_RPT_List\" "
                        + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + form + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") "
                        + "and \"TabName\" in (select \"TabName\" from \"CFG_RPT_Ref\" "
                        + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + form + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") "
                        + "and \"Item\"='" + cellName + "') and \"ID\" between " + ID_Start + " and " + ID_End;
            } else if (connectedDB.equalsIgnoreCase("toolset")) {
                String RegPrefix = getToolsetRegPrefix(Regulator);
                SQL = "select \"PageName\" from \"" + RegPrefix + "List\" "
                        + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + form + "' "+ "and \"Version\"='" + version + "')"
                        + "and \"TabName\" in (select \"TabName\" from \"" + RegPrefix + "Ref\" "
                        + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + form + "' "
                        + "and \"Version\"='" + version + "') and \"Item\"='"+cellName+"')";
            }

            pageName = DBQuery.queryRecord(SQL);
            
            page=pageName;
            selectPage(pageName);
            selectInstance(instance);
                
        } else {
        	
            boolean findCell = false;
            if (element("fp.nextPage").isDisplayed()) {
                if (!element("fp.firstPageSta").getAttribute("class").contains("ui-state-disabled")) {
                    element("fp.firstPage").click();
                    waitStatusDlg();
                }

                if (element("fp.inputCell", extendCell).isDisplayed()) {
                	selectInstance(instance);
                	if (element("fp.inputCell", extendCell).isDisplayed()) {
                        findCell = true;
                        page = getSelectedPage();
                    }
                }else{
                	while (!findCell && !element("fp.nextPageSta").getAttribute("class").contains("ui-state-disabled")) {
                        element("fp.nextPage").click();
                        waitStatusDlg();
                        if (element("fp.inputCell", extendCell).isDisplayed()) {
                        	selectInstance(instance);
                        	if (element("fp.inputCell", extendCell).isDisplayed()) {
                                findCell = true;
                                page = getSelectedPage();
                            }
                        }
                    }
                }
                
            } else {
            	if (element("fp.inputCell", extendCell).isDisplayed()) {
                	selectInstance(instance);
                	if (element("fp.inputCell", extendCell).isDisplayed()) {
                		findCell = true;
                	}
                    page = getSelectedPage();
                }
            }

            if (!findCell) {
                List<String> pages = new ArrayList<String>();
                if (connectedDB.equalsIgnoreCase("ar")) {
                    String ID_Start = getRegulatorIDRangeStart(Regulator);
                    String ID_End = getRegulatorIDRangEnd(Regulator);

                    SQL = "select \"PageName\" from \"CFG_RPT_List\" "
                            + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + form + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") "
                            + "and \"TabName\" in (select \"TabName\" from \"CFG_RPT_GridRef\" "
                            + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" where \"Return\"='" + form + "' AND \"Version\"=" + version + " and \"ID\" between " + ID_Start + " and " + ID_End + ") "
                            + "and \"Item\"='" + cellName + "') and \"ID\" between " + ID_Start + " and " + ID_End;
                } else if (connectedDB.equalsIgnoreCase("toolset")) {
                    String RegPrefix = getToolsetRegPrefix(Regulator);
                    SQL = "select \"PageName\" from \"" + RegPrefix + "List\" "
                            + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + form + "' "+ "and \"Version\"='" + version + "')"
                            + "and \"TabName\" in (select \"TabName\" from \"" + RegPrefix + "GridRef\" "
                            + "where \"ReturnId\" IN(SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" where \"Return\"='" + form + "' "
                            + "and \"Version\"='" + version + "') and \"Item\"='"+cellName+"')";

                }
                pages = DBQuery.queryRecords(SQL);


                for (String pageName : pages) {
                    selectPage(pageName);
                    selectInstance(instance);

                    if (element("fp.nextPage").isDisplayed()) {
                        if (!element("fp.firstPageSta").getAttribute("class").contains("ui-state-disabled")) {
                            element("fp.firstPage").click();
                            waitStatusDlg();
                        }

                        if (element("fp.inputCell", extendCell).isDisplayed()) {
                            findCell = true;
                            page = pageName;
                        }


                        while (!findCell && !element("fp.nextPageSta").getAttribute("class").contains("ui-state-disabled")) {
                            waitThat().timeout(500);
                            element("fp.nextPage").click();
                            waitStatusDlg();
                            if (element("fp.inputCell", extendCell).isDisplayed()) {
                                findCell = true;
                                page = pageName;
                            }
                        }
                    } else {
                        if (element("fp.inputCell", extendCell).isDisplayed()) {
                            findCell = true;
                            page = pageName;
                        }
                    }

                    if (findCell) {
                        break;
                    }
                }
            }
        }
        return page;
    }


    public String exportToFile(String Regulator, String Group, String formCode, String FormVersion, String ProcessDate, String fileType, String Module) throws Exception {
        String filePath = null;
        exportToFileClick(fileType, formCode,FormVersion, Module);

        boolean flag = true;
        long statTime = System.currentTimeMillis();
        while (flag) {
            try {
                if (element("fp.message").isDisplayed()) {
                    if (element("fp.message").getInnerText().startsWith("Processing terminated by xsl:message")) {
                        filePath = "Error";
                        break;
                    }
                }
            } catch (NoSuchElementException e) {
            }
            long curTime = System.currentTimeMillis();
            if ((curTime - statTime) / 1000 > 5) {
                break;
            }

        }
        if (filePath == null) {
            filePath = TestCaseManager.getTestCase().getDownloadFile().replace("/", "\\");
            filePath = System.getProperty("user.dir") + "\\" + filePath;
        }

        return filePath;
    }

    private ExportToFilePage enterExportToFiLePage(String type, String formCode) throws Exception {
        if (type.equalsIgnoreCase("text"))
            element("fp.exportToRC", formCode.toUpperCase()).click();

        else if (type.equalsIgnoreCase("vanilla"))
            element("fp.exportToVan").click();

        else if (type.equalsIgnoreCase("ARBITRARY"))
            element("fp.exportToARB").click();
        
        else if (type.equalsIgnoreCase("xbrl"))
        	 element("fp.exportToXBRL").click();

        return new ExportToFilePage(getWebDriverWrapper());
    }


    private void exportToFileClick(String fileType, String formCode, String formVersion,String module) throws Exception {
        ExportToFilePage exportToFilePage = null;
        if (fileType.equalsIgnoreCase("csv")) {
            element("fp.expToFile").click();
            waitStatusDlg();
            TestCaseManager.getTestCase().startTransaction("");
            TestCaseManager.getTestCase().setPrepareToDownload(true);
            element("fp.expToCSV").click();
            TestCaseManager.getTestCase().stopTransaction();
            waitStatusDlg();
        } else if (fileType.equalsIgnoreCase("excel")) {
            element("fp.expToFile").click();
            waitStatusDlg();
            TestCaseManager.getTestCase().startTransaction("");
            TestCaseManager.getTestCase().setPrepareToDownload(true);
            element("fp.expToXls").click();
            TestCaseManager.getTestCase().stopTransaction();
            waitStatusDlg();
        }else if (fileType.equalsIgnoreCase("xbrl")) {
        	element("fp.expToFile").click();
            waitStatusDlg();
            exportToFilePage = enterExportToFiLePage(fileType, formCode);
            exportToFilePage.selectForm_xbrl(formCode, formVersion);
            exportToFilePage.selectCompressType(null);
            exportToFilePage.exportBtnClick();
            waitStatusDlg();
            
        }else if (fileType.equalsIgnoreCase("Text")) {
            element("fp.exportToRF").click();
            waitStatusDlg();
            logger.info("Click first option: RC");
            exportToFilePage = enterExportToFiLePage(fileType, formCode);
            exportToFilePage.setModuleSelector(module);
            exportToFilePage.exportBtnClick();
            waitStatusDlg();

        } else if (fileType.equalsIgnoreCase("vanilla")) {
            element("fp.exportToRF").click();
            waitStatusDlg();
            logger.info("Click second option: vanilla");
            exportToFilePage = enterExportToFiLePage(fileType, formCode);
            exportToFilePage.setModuleSelector(module);
            waitThat().timeout(500);
            exportToFilePage.exportBtnClick();
            waitStatusDlg();
        } else if (fileType.equalsIgnoreCase("ARBITRARY")) {
            element("fp.exportToRF").click();
            waitStatusDlg();
            logger.info("Click Export to ARBITRARY");
            exportToFilePage = enterExportToFiLePage(fileType, formCode);
            exportToFilePage.setModuleSelector(module);
            waitThat().timeout(500);
            exportToFilePage.exportBtnClick();
            waitStatusDlg();
        }
        
    }

    private String getFormInfo() throws Exception {
        return element("fp.form").getInnerText();
    }

    public boolean showProblemsPanel() throws Exception {
        boolean openProblemPanel = false;
        try {
            if (!element("fp.valTab").getAttribute("aria-expanded").equals("true"))
                openProblemPanel = true;
        } catch (Exception e) {
            openProblemPanel = true;
        }

        return openProblemPanel;
    }

    public ValidationPage enterValidation(boolean valNow) throws Exception {
        if (valNow) {
            validationNowClick();
            waitStatusDlg();
        }
        if (!element("fp.formFooter").isDisplayed()) {
            element("fp.adjustment").click();
            waitStatusDlg();
            element("fp.adjLog").click();
            waitStatusDlg();
        }

        element("fp.valTab").click();
        waitStatusDlg();

        return new ValidationPage(getWebDriverWrapper());
    }

    public ErrorListPage enterProblem() throws Exception {
        logger.info("Enter Problems table");
        if (!(element("fp.proDataTab")).isDisplayed()) {
            try {
                if (element("fp.errorBtn").isDisplayed()) {
                    element("fp.errorBtn").click();
                    waitStatusDlg();
                } else if (element("fp.formFooter").isDisplayed()) {
                    element("fp.proTab").click();
                    waitStatusDlg();
                } else {
                    element("fp.adjustment").click();
                    waitStatusDlg();
                    element("fp.adjLog").click();
                    waitStatusDlg();
                    element("fp.proTab").click();
                    waitStatusDlg();
                }
            } catch (NoSuchElementException e) {
                element("fp.adjustment").click();
                waitStatusDlg();
                element("fp.adjLog").click();
                waitStatusDlg();
                element("fp.proTab").click();
                waitStatusDlg();
            }
        }

        return new ErrorListPage(getWebDriverWrapper());
    }


    private String getValStatus(String instance, String ruleID, String cellName, String ruleType, String rowKey) throws Exception {
        ValidationPage validationPage = enterValidation(true);
        return validationPage.getValStatus(ruleType, ruleID, cellName, rowKey, instance);
    }


    public List<String> getHighlightCount_Instance(String instance) throws Exception {
        logger.info("Begin get highlight count on instance name");
        List<String> rst = new ArrayList<String>();
        element("fp.curInst").click();
        waitThat().timeout(1000);
        if (element("fp.instance.critical", instance).isDisplayed()) {
            String count = element("fp.instance.critical", instance).getInnerText().trim();
            rst.add(count);
        } else
            rst.add("0");

        if (element("fp.instance.warn", instance).isDisplayed()) {
            String count = element("fp.instance.warn", instance).getInnerText().trim();
            rst.add(count);
        } else
            rst.add("0");
        element("fp.curInst").click();
        return rst;
    }

    public List<String> getHighlightCount_Page(String pageName, String instance) throws Exception {
        logger.info("Begin get highlight count on page name");
        List<String> rst = new ArrayList<String>();
        selectInstance(instance);
        if (element("fp.criticalNums", pageName).isDisplayed()) {
            String count = element("fp.criticalNums", pageName).getInnerText().trim();
            rst.add(count);
        } else
            rst.add("0");

        if (element("fp.warnNums", pageName).isDisplayed()) {
            String count = element("fp.warnNums", pageName).getInnerText().trim();
            rst.add(count);
        } else
            rst.add("0");
        return rst;
    }

    public String getCellColor(String Regulator, String formCode, String version, String cellName) throws Exception {
        logger.info("Check  if " + cellName + " backgroud color");
        String extendCell = null;
        String instance = null;
        if (cellName.contains("[")) {
            instance = cellName.substring(cellName.indexOf("[") + 1, cellName.indexOf("]"));
            cellName = cellName.replace("[", "").replace("]", "");
        }
        if (cellName.contains("@")) {
            cellName = cellName.split("@")[0];
            String rowID = cellName.split("@")[1];
            if (rowID.equals("0")) {
                extendCell = cellName;
            } else {
                rowID = String.valueOf(Integer.parseInt(rowID) + 48);
                String gridName = getExtendCellName(Regulator, formCode, version, cellName);
                extendCell = gridName + rowID + cellName;
            }
        }
        String page = getPageNameByCell(Regulator, formCode, version, instance, cellName, extendCell);
        logger.info("Cell[" + cellName + "] in page[" + page + "] ");
        selectInstance(instance);
        if (extendCell != null)
            cellName = extendCell;
        String color = element("fp.inputCell", cellName).getCssValue("background-color");
        if (color.equalsIgnoreCase("rgba(204, 153, 204, 1)"))
            return "Purple";
        else if (color.equalsIgnoreCase("rgba(236, 184, 188, 1)"))
            return "Red";
        else if (color.equalsIgnoreCase("rgba(255, 255, 204, 1)"))
            return "Yellow";
        else
            return "";

    }

    public String getCellColor(String Regulator, String formCode, String version, String instance, String cellName) throws Exception {
        logger.info("Get " + cellName + " backgroud color");
        String extendCell = null;
        if (cellName.contains("@")) {
            String tmp = cellName;
            cellName = tmp.split("@")[0];
            String rowID = tmp.split("@")[1];
            if (rowID.equals("0")) {
                extendCell = cellName;
            } else {
                rowID = String.valueOf(Integer.parseInt(rowID) + 48);
                String gridName = getExtendCellName(Regulator, formCode, version, cellName);
                extendCell = gridName + rowID + cellName;
            }
        }

        String page = getPageNameByCell(Regulator, formCode, version, instance, cellName, extendCell);
        logger.info("Cell[" + cellName + "] in page " + page);
        selectInstance(instance);
        if (extendCell != null)
            cellName = extendCell;
        String color = element("fp.inputCell", cellName).getCssValue("background-color");
        if (color.equalsIgnoreCase("rgba(204, 153, 204, 1)"))
            return "Purple";
        else if (color.equalsIgnoreCase("rgba(236, 184, 188, 1)"))
            return "Red";
        else if (color.equalsIgnoreCase("rgba(255, 255, 204, 1)"))
            return "Yellow";
        else
            return "";
    }

    public boolean workflowIsVisible() throws Exception {
        return element("fp.wfBtn").isDisplayed();
    }

    public boolean viewWorkflowLogIsVisible() throws Exception {
        return element("fp.workflowMenu").isDisplayed();
    }

    public void closeTransDlg() throws Exception {
        element("fp.closeTransDlg").click();
    }

    public boolean nextPageEnable() throws Exception {
        if (!element("fp.nextPageSta").getAttribute("class").contains("ui-state-disabled"))
            return true;
        else
            return false;
    }

    public void clickNextPage() throws Exception {
        element("fp.nextPage").click();
        waitStatusDlg();
    }


    public void clickPreviousPage() throws Exception {
        element("fp.prePage").click();
        waitStatusDlg();
    }


    public boolean isVisible(String loc) throws Exception {
        return element(loc).isDisplayed();
    }


    public String getElementAttribute(String loc, String type) throws Exception {
        return element(loc).getAttribute(type);
    }


    public List<String> getAllInstance(String page) throws Exception {
        List<String> instances = new ArrayList<String>();
        selectPage(page);
        element("fp.curInst").click();
        waitThat().timeout(300);
        int amt = (int) element("fp.instaceTab").getRowCount();
        for (int i = 1; i <= amt; i++) {
            instances.add(element("fp.instace", String.valueOf(i)).getAttribute("data-label"));
        }
        return instances;
    }

    public List<String> getAllInstance(String Regulator, String formCode, String version, String cellName) throws Exception {
        List<String> instances = new ArrayList<String>();
        String extendCell = null;
        if (cellName.contains("@")) {
            String tmp = cellName;
            cellName = tmp.split("@")[0];
            String rowID = tmp.split("@")[1];
            if (rowID.equals("0")) {
                extendCell = cellName;
            } else {
                rowID = String.valueOf(Integer.parseInt(rowID) + 48);
                String gridName = getExtendCellName(Regulator, formCode, version, cellName);
                extendCell = gridName + rowID + cellName;
            }
        }
        String page = getPageNameByCell(Regulator, formCode, version, null, cellName, extendCell);
        logger.info("Page is[" + page + "]");
        element("fp.curInst").click();
        waitThat().timeout(300);
        int amt = (int) element("fp.instaceTab").getRowCount();
        for (int i = 1; i <= amt; i++) {
            instances.add(element("fp.instace", String.valueOf(i)).getAttribute("data-label"));
        }
        element("fp.curInst").click();
        waitThat().timeout(300);
        return instances;
    }

    public void clickReadyForApprove() throws Exception {
        element("fp.wfBtn").click();
        waitThat().timeout(400);
        element("fp.readyAppr").click();
        waitStatusDlg();
        waitThat().timeout(500);
    }

    public void approveForm(String comment) throws Exception {
        element("fp.wfBtn").click();
        waitThat("fp.approve").toBeVisible();
        element("fp.approve").click();
        waitThat("fp.apprCommentOK").toBeVisible();
        if (comment != null)
            element("fp.apprComment").type(comment);
        element("fp.apprCommentOK").click();
        waitStatusDlg();
        waitThat().timeout(1000);
    }

    public String getApproveFormMessage(String comment) throws Exception {
        element("fp.wfBtn").click();
        waitThat("fp.approve").toBeVisible();
        element("fp.approve").click();
        waitThat("fp.apprCommentOK").toBeVisible();
        if (comment != null)
            element("fp.apprComment").type(comment);
        element("fp.apprCommentOK").click();
        return element("fp.message").getInnerText();
    }


    public void rejectForm(String comment) throws Exception {
        element("fp.wfBtn").click();
        waitThat("fp.reject").toBeVisible();
        element("fp.reject").click();
        waitThat("fp.rejCommentOK").toBeVisible();
        if (comment == null)
            comment = "  ";
        element("fp.rejComment").type(comment);
        element("fp.rejCommentOK").click();
        waitStatusDlg();
        waitThat().timeout(1000);
    }

    public String getRejectFormMessage(String comment) throws Exception {
        element("fp.wfBtn").click();
        waitThat("fp.reject").toBeVisible();
        element("fp.reject").click();
        waitThat("fp.rejCommentOK").toBeVisible();
        if (comment == null)
            comment = "reject";
        element("fp.rejComment").type(comment);
        element("fp.rejCommentOK").click();
        return element("fp.message").getInnerText();
    }

    public String getApproveCount() throws Exception {
        return element("fp.attestation").getInnerText().replace("Ready For Attestation ", "").replace("Being Attested ", "").replace("Attested ", "");
    }

    public String getApproveStatus() throws Exception {
        return element("fp.attestation").getInnerText().split("(")[0];
    }

    public String exportValidationResult() throws Exception {
        enterValidation(true);
        TestCaseManager.getTestCase().startTransaction("");
        TestCaseManager.getTestCase().setPrepareToDownload(true);
        element("fp.validationExport").click();
        TestCaseManager.getTestCase().stopTransaction();
        String file = TestCaseManager.getTestCase().getDownloadFile().replace("/", "\\");
        file = System.getProperty("user.dir") + "\\" + file;
        element("fp.hideValTable").click();
        return file;
    }

    public String exportProblem() throws Exception {
        logger.info("Begin get rule message");
        getDrillDown();
        waitThat().timeout(2500);
        element("fp.proTab").click();
        waitStatusDlg();
        waitThat("fp.problemExport").toBeClickable();
        TestCaseManager.getTestCase().startTransaction("");
        TestCaseManager.getTestCase().setPrepareToDownload(true);
        element("fp.problemExport").click();
        TestCaseManager.getTestCase().stopTransaction();
        String file = TestCaseManager.getTestCase().getDownloadFile().replace("/", "\\");
        file = System.getProperty("user.dir") + "\\" + file;
        element("fp.hideErrorTable").click();
        return file;
    }

    public boolean isCellHighlight(String Regulator, String form, String version, String instance, String cellName, String extendCell) throws Exception {
        getPageNameByCell(Regulator, form, version, instance, cellName, extendCell);
        if (element("fp.highlightCell", cellName).getAttribute("class").contains("highlightBorderBlue"))
            return true;
        else
            return false;
    }

    public boolean isPageHighlight(String pageName) throws Exception {
        String clsss = element("fp.highlightPage", pageName).getAttribute("class");
        if (clsss.contains("highlightRowBlue") || clsss.contains("ui-state-highlight"))
            return true;
        else
            return false;
    }
    

}
