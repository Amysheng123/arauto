package com.lombardrisk.pages;

import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kevin Ling on 2/16/15.
 * Refactor by Leo Tu on 2/1/16
 */
public class ListPage extends AbstractPage {
    public ListPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
    }

    private String getSelectedRegulator() throws Exception {
        return element("lp.slectedReg").getInnerText();
    }

    private String getSelectedGroup() throws Exception {
        return element("lp.slectedGroup").getInnerText();
    }

    private String getSelectedForm() throws Exception {
        return element("lp.slectedForm").getInnerText();
    }

    private String getSelectedProcessDate() throws Exception {
        return element("lp.slectedPD").getInnerText();
    }

    public void setRegulator(String regulator) throws Exception {
        element("lp.REG").selectByVisibleText(regulator);
        waitStatusDlg();
        //waitThat().timeout(2500);

    }

    public void setGroup(String group) throws Exception {
        element("lp.Group").selectByVisibleText(group);
        waitStatusDlg();
        //waitThat().timeout(2500);
    }

    public void setForm(String form) throws Exception {
        element("lp.Form").selectByVisibleText(form);
        waitStatusDlg();
        //waitThat().timeout(2500);

    }

    public void setProcessDate(String date) throws Exception {
        element("lp.Date").selectByVisibleText(date);
        waitStatusDlg();
        //waitThat().timeout(2500);

    }

    public FormInstancePage openFormInstance(String formCode, String version, String date) throws Exception {
        logger.info("Open form[" + formCode + " v" + version + " " + date + "]");
        int rowAmt = getFormListRowSize();
        for (int i = 0; i < rowAmt; i++) {
            String index = String.valueOf((i + 1));
            if (element("lp.formCode", String.valueOf(i)).getInnerText().equals(formCode) && element("lp.formVersion", index).getInnerText().equals(version) && element("lp.procesDate", index).getInnerText().equals(date)) {
                element("lp.formCode", String.valueOf(i)).click();
                waitStatusDlg();
                waitThat().timeout(2000);
                if (element("lp.warnConfirmBtn").isDisplayed()) {
                    element("lp.warnConfirmBtn").click();
                    waitStatusDlg();
                }
                break;
            }
        }
        
        return new FormInstancePage(getWebDriverWrapper());
    }


    public FormInstancePage openFirstFormInstance() throws Exception {
        logger.info("Open first form");
        element("lp.firstFormLink").click();
        waitStatusDlg();
        waitThat().timeout(1000);
        if (element("lp.warnConfirmBtn").isDisplayed()) {
            element("lp.warnConfirmBtn").click();
            waitStatusDlg();
        }
        return new FormInstancePage(getWebDriverWrapper());
    }


    public ChangePasswordPage openChangePasswordPage() throws Exception {
        element("lp.uMenu").click();
        waitStatusDlg();
        element("lp.changePwd").click();
        waitStatusDlg();
        return new ChangePasswordPage(getWebDriverWrapper());
    }

    public FormInstanceCreatePage openFormInstanceCreatePage() throws Exception {
        element("lp.createNewBtn").click();
        waitStatusDlg();
        element("lp.createNewForm").click();
        waitStatusDlg();
        return new FormInstanceCreatePage(getWebDriverWrapper());
    }

    public FormInstanceCreatePage openFormInstanceCreateFromExcelPage() throws Exception {
        element("lp.createNewBtn").click();
        waitStatusDlg();
        element("lp.createFromXls").click();
        waitStatusDlg();
        return new FormInstanceCreatePage(getWebDriverWrapper());
    }

    public FormInstanceRetrievePage openFormInstanceRetrievePage() throws Exception {
        try {
            element("lp.retrieve").click();
        } catch (Exception e) {
            waitThat().timeout(5000);
            element("lp.retrieve").click();
        }
        waitStatusDlg();
        return new FormInstanceRetrievePage(getWebDriverWrapper());
    }

    public HomePage logout() throws Exception {
    	waitStatusDlg();
        element("lp.uMenu").click();
        waitStatusDlg();
        element("lp.Logout").click();
        return new HomePage(getWebDriverWrapper());
    }


    public List<String> getRegulatorOptions() throws Exception {
        return element("lp.REG").getAllOptionTexts();
    }


    public List<String> getGroupOptions() throws Exception {
        return element("lp.Group").getAllOptionTexts();
    }

    public List<String> getGroupOptions_Create() throws Exception {
        return element("lp.CGroup").getAllOptionTexts();
    }

    public List<String> getFormOptions_Create() throws Exception {
        return element("lp.CForm").getAllOptionTexts();
    }

    public List<String> getFormOptions_Create(String Group, String ProcessDate) throws Exception {
        FormInstanceCreatePage formInstanceCreatePage = openFormInstanceCreatePage();

        formInstanceCreatePage.setGroup(Group);
        if (ProcessDate == null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            ProcessDate = df.format(new Date());
        }
        formInstanceCreatePage.setProcessDate(ProcessDate);

        List<String> forms = getFormOptions_Create();
        formInstanceCreatePage.createCloseClick();
        waitThat().timeout(500);
        return forms;

    }

    public List<String> getFormOptions_Retrieve(String Group, String ProcessDate) throws Exception {
        FormInstanceRetrievePage retrievePage = openFormInstanceRetrievePage();
        retrievePage.setGroup(Group);
        if (ProcessDate == null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            ProcessDate = df.format(new Date());
        }
        retrievePage.setReferenceDate(ProcessDate);
        List<String> forms = retrievePage.getFormOptions();
        retrievePage.retrieveCloseClick();
        return forms;
    }

    public List<String> getCloneDateOptions_Create() throws Exception {
        return element("lp.CloneDate").getAllOptionTexts();
    }

    public List<String> getFormOptions() throws Exception {
        List<String> forms = element("lp.Form").getAllOptionTexts();
        forms.remove(0);
        return forms;
    }

    public List<String> getProcessDateOptions() throws Exception {
        List<String> dates = element("lp.Date").getAllOptionTexts();
        dates.remove(0);
        return dates;
    }

    public String clickExportButton() throws Exception {
        TestCaseManager.getTestCase().startTransaction("");
        TestCaseManager.getTestCase().setPrepareToDownload(true);
        element("lp.exportFormList").click();
        TestCaseManager.getTestCase().stopTransaction();
        waitStatusDlg();
        return TestCaseManager.getTestCase().getDownloadFile();

    }

    public ListImportFilePage openImportAdjustmentPage() throws Exception {
        element("lp.import").click();
        waitStatusDlg();
        return new ListImportFilePage(getWebDriverWrapper());
    }

    public ListImportFilePage openImporExcelPage() throws Exception {
        element("lp.createNewBtn").click();
        waitStatusDlg();
        element("lp.createFromXls").click();
        waitStatusDlg();
        return new ListImportFilePage(getWebDriverWrapper());
    }

    public ExportToFilePage openExportToFileBtnClick(String fileType, String formCode) throws Exception {
    	if(fileType.equalsIgnoreCase("xbrl")){
    		element("lp.exportToXBRL").click();
    	    waitStatusDlg();
    	}else {
    		element("lp.exportToFileBtn").click();
	        waitStatusDlg();
	        exportToFile(fileType, formCode);
		}
       
        return new ExportToFilePage(getWebDriverWrapper());
    }
    

    public void exportToFile(String fileType, String formCode) throws Exception {
        String optionName = null;
        if (fileType.equalsIgnoreCase("Text")) {
            optionName = "Export to RC_" + formCode;
        } else if (fileType.equalsIgnoreCase("Vanilla")) {
            optionName = "Export To Vanilla";
        } else if (fileType.equalsIgnoreCase("ARBITRARY")) {
            optionName = "Export to ARBITRARY";
        } else if (fileType.equalsIgnoreCase("XBRL")) {
            optionName = "Export to XBRL";
        }

        element("lp.exportToFileOption", optionName).click();
        waitStatusDlg();

    }

    public CalendarPage openCalendarPage() throws Exception {
        element("lp.settingBtn").click();
        waitStatusDlg();
        element("lp.adminMenu").click();
        waitStatusDlg();
        element("lp.calendarMenu").click();
        waitStatusDlg();
        element("lp.subCalMenu").click();
        waitStatusDlg();
        return new CalendarPage(getWebDriverWrapper());
    }

    public SchedulePage openSchedulePage() throws Exception {
        element("lp.settingBtn").click();
        waitStatusDlg();
        element("lp.adminMenu").click();
        waitStatusDlg();
        element("lp.calendarMenu").click();
        waitStatusDlg();
        element("lp.subSchMenu").click();
        return new SchedulePage(getWebDriverWrapper());
    }

    public FormSchedulePage openFormSchedulePage() throws Exception {
        element("lp.settingBtn").click();
        waitStatusDlg();
        element("lp.adminMenu").click();
        waitStatusDlg();
        element("lp.formSchedule").click();
        return new FormSchedulePage(getWebDriverWrapper());
    }


    public boolean isFormLockedInList(String form, String processDate) throws Exception {
        if (element("lp.formlock").getInnerText().equals("LOCK"))
            return true;
        else
            return false;
    }

    public String getUserName() throws Exception {
        return element("lp.userName").getInnerText().replace("hi ", "");
    }


    public int getFormListRowSize() throws Exception {
        try {
            if (element("lp.noRecords").getInnerText().equals("No records found."))
                return 0;
            else
                return (int) element("lp.formListTab").getRowCount();
        } catch (Exception e) {
            return (int) element("lp.formListTab").getRowCount();
        }
    }


    public List<String> getFormStatus(int row) throws Exception {
        List<String> formDetailInfo = new ArrayList<String>();
        for (int i = 2; i <= 14; i++) {
            String[] list = new String[]{String.valueOf(row), String.valueOf(i)};
            if (i == 2 || i == 9)
                formDetailInfo.add(element("lp.cellText2", list).getInnerText());
            else if (i == 5 || i == 6 || i == 7) {
                element("lp.cellText3", list).setAttribute("style", "visibility:visible;");
                formDetailInfo.add(element("lp.cellText3", list).getInnerText());
            } else if (i == 8)
                formDetailInfo.add(element("lp.cellText4", list).getInnerText());
            else if (i == 10)
                continue;
            else
                formDetailInfo.add(element("lp.cellText1", list).getInnerText());
        }
        return formDetailInfo;
    }


    public void deleteFormInstance(String Form, String date) throws Exception {
        logger.info("Delete form[" + Form + " " + date + "(" + date + ")] if exist");
        if (Form != null) {
            String formCode = splitReturn(Form).get(0);
            String version = splitReturn(Form).get(1);
            int rowAmt = getFormListRowSize();
            if (rowAmt > 0) {
                if (date != null) {
                    logger.info("There are " + rowAmt + " records in list");
                    for (int i = 1; i <= rowAmt; i++) {
                        if (element("lp.formCode", String.valueOf(i - 1)).getInnerText().equals(formCode) && element("lp.formVersion", String.valueOf(i)).getInnerText().equals(version)) {
                            if (date != null && element("lp.procesDate", String.valueOf(i)).getInnerText().equals(date)) {
                                element("lp.delFormIcon", String.valueOf(i)).click();
                                waitStatusDlg();
                                element("lp.delConfBtn").click();
                                waitStatusDlg();
                                waitThat().timeout(1000);
                                element("lp.deleteComment").input("Delete return by automation");
                                element("lp.deleteReturnBtn").click();
                                waitStatusDlg();
                                if (element("lp.message").isDisplayed()) {
                                    element("lp.closeMessage").setAttribute("style", "display:inline");
                                    waitStatusDlg();
                                    element("lp.closeMessage").click();
                                    waitStatusDlg();
                                    logger.info("Close message");
                                }
                                break;
                            }
                        }
                    }
                }

            }
        } else {
            element("lp.delFormIcon", "1").click();
            waitStatusDlg();
            element("lp.delConfBtn").click();
            waitThat().timeout(500);
            element("lp.deleteComment").input("Delete return by automation");
            element("lp.deleteReturnBtn").click();
            waitStatusDlg();
            if (element("lp.message").isDisplayed()) {
                element("lp.closeMessage").setAttribute("style", "display:inline");
                element("lp.closeMessage").click();
                waitStatusDlg();
                logger.info("Close message");
            }
        }
        waitStatusDlg();
    }


    public void deleteFormInstance(String Form, String date, String comment, boolean save) throws Exception {
        logger.info("Delete form[" + Form + " " + date + "(" + date + ")]");
        if (Form != null) {
            String formCode = splitReturn(Form).get(0);
            String version = splitReturn(Form).get(1);
            int rowAmt = getFormListRowSize();
            logger.info("There are " + rowAmt + " records in list");
            for (int i = 1; i <= rowAmt; i++) {
                if (element("lp.formCode", String.valueOf(i - 1)).getInnerText().equals(formCode) && element("lp.formVersion", String.valueOf(i)).getInnerText().equals(version)) {
                    if (date != null && element("lp.procesDate", String.valueOf(i)).getInnerText().equals(date)) {
                        element("lp.delFormIcon", String.valueOf(i)).click();
                        if (save) {
                            element("lp.delConfBtn").click();
                            waitStatusDlg();
                            element("lp.deleteComment").input(comment);
                            element("lp.deleteReturnBtn").click();
                            waitStatusDlg();
                            if (element("lp.message").isDisplayed()) {
                                element("lp.closeMessage").setAttribute("style", "display:inline");
                                element("lp.closeMessage").click();
                                logger.info("Close message");
                                waitStatusDlg();
                            }
                        } else
                            element("lp.deleteReturnCancelBtn").click();

                        break;
                    }
                }
            }
        }

    }

    public String getDeleteFormInstanceMessage(String Form, String date) throws Exception {
        logger.info("Delete form[" + Form + " " + date + "(" + date + ")]");
        String message = null;
        if (Form != null) {
            String formCode = splitReturn(Form).get(0);
            String version = splitReturn(Form).get(1);
            int rowAmt = getFormListRowSize();
            logger.info("There are " + rowAmt + " records in list");
            for (int i = 1; i <= rowAmt; i++) {
                if (element("lp.formCode", String.valueOf(i - 1)).getInnerText().equals(formCode) && element("lp.formVersion", String.valueOf(i)).getInnerText().equals(version)) {
                    if (date != null && element("lp.procesDate", String.valueOf(i)).getInnerText().equals(date)) {
                        element("lp.delFormIcon", String.valueOf(i)).click();
                        waitStatusDlg();
                        if (element("lp.deleteReturnMsg1").isDisplayed()) {
                            message = element("lp.deleteReturnMsg1").getInnerText() + element("lp.deleteReturnMsg2").getInnerText();
                            element("lp.deleteReturnCancelBtn").click();
                        } else {
                            element("lp.delConfBtn").click();
                            element("lp.deleteReturnBtn").click();
                            waitThat().timeout(300);
                            message = element("lp.message").getInnerText();
                        }
                        break;
                    }
                }
            }
        }
        return message;

    }


    public int getFormInstanceRowPos(String formCode, String version, String date) throws Exception {
        int rowIndex = 0;
        int rowAmt = getFormListRowSize();
        for (int i = 0; i < rowAmt; i++) {
            int index = i + 1;
            if (element("lp.formCode", String.valueOf(i)).getInnerText().equals(formCode) && element("lp.formVersion", String.valueOf(index)).getInnerText().equals(version) && element("lp.procesDate", String.valueOf(index)).getInnerText().equals(date)) {
                rowIndex = index;
                break;
            }
        }
        return rowIndex;
    }


    public EditionManagePage openEditionManage(String formCode, String version, String date) throws Exception {
        element("lp.editionNo", String.valueOf(getFormInstanceRowPos(formCode, version, date))).click();
        waitStatusDlg();
        return new EditionManagePage(getWebDriverWrapper());
    }

    public EditionManagePage openEditionManage(int index) throws Exception {
        element("lp.editionNo", String.valueOf(index-1)).click();
        waitStatusDlg();
        return new EditionManagePage(getWebDriverWrapper());
    }

    public FormInstancePage createNewForm(String Group, String ProcessDate, String Form, String copyData, boolean AllowNull, boolean InitialiseToZeros) throws Exception {
        logger.info("Begin create new form[" + Form + "]");
        FormInstanceCreatePage formInstanceCreatePage = openFormInstanceCreatePage();
        try {
            formInstanceCreatePage.setGroup(Group);
            if (ProcessDate == null) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                ProcessDate = df.format(new Date());
            }
            formInstanceCreatePage.setProcessDate(ProcessDate);
            formInstanceCreatePage.setForm(Form);
            if (copyData != null) {
                formInstanceCreatePage.selectCloneCheck();
                formInstanceCreatePage.setSelectCloneDate(copyData);
            }
            if (AllowNull) {
                assertThat(locator("fcp.zero")).displayed().isTrue();
                if (InitialiseToZeros)
                    formInstanceCreatePage.selectInitToZeroCheck();
            }
            FormInstancePage formInstancePage = formInstanceCreatePage.createConfirmClick();
            
            return formInstancePage;
        } catch (Exception e) {
            e.printStackTrace();
            formInstanceCreatePage.createCloseClick();
            return null;
        }
    }

    public FormInstancePage createFormFromExcel(File importFile, boolean allowNull, boolean InitialiseToZeros, boolean checkCellValue) throws Exception {
        logger.info("Begin create form from excel");
        logger.info("Import file is :" + importFile);
        String type = "createFromExcelForm";
        ListImportFilePage listImportFilePage = null;
        try {
            listImportFilePage = openImporExcelPage();
            listImportFilePage.setImportFile(importFile, type);
            FormInstancePage formInstancePage = null;
            if (allowNull) {
                listImportFilePage.assertExistIniToZero(type);
                if (InitialiseToZeros)
                    listImportFilePage.tickInitToZero(type);
            }
            if (!checkCellValue)
                element("lp.openFormCheckBox").click();
            try {
                ImportConfirmPage confirmPage = listImportFilePage.importFileBtnClick(type);
                formInstancePage = confirmPage.confirmBtnClick(type);

            } catch (Exception e) {
                logger.error("Create form form excel failed");
                listImportFilePage.closeImportFileDlg(type);
                e.printStackTrace();
            }
            if (element("lp.warnConfirmBtn").isDisplayed()) {
                element("lp.warnConfirmBtn").click();
                waitStatusDlg();
            }
            return formInstancePage;
        } catch (Exception e) {
            return null;
        }

    }

    public FormInstancePage importAdjustment(File importFile, boolean addToExistValue, boolean InitialiseToZeros) throws Exception {
        logger.info("Begin import adjustment");
        logger.info("Import file is :" + importFile);
        String type = "listImportFileForm";
        ListImportFilePage listImportFilePage = openImportAdjustmentPage();
        try {
            listImportFilePage.setImportFile(importFile, type);
            if (addToExistValue)
                listImportFilePage.selectAddToExistingValue(type);
            if (InitialiseToZeros)
                listImportFilePage.tickInitToZero(type);
            try {
                ImportConfirmPage confirmPage = listImportFilePage.importFileBtnClick(type);
                confirmPage.confirmBtnClick(type);
            } catch (Exception e) {
                logger.error("Import adjustment failed");
                e.printStackTrace();

            }
        } catch (Exception e) {
            listImportFilePage.closeImportFileDlg(type);
        }
        return new FormInstancePage(getWebDriverWrapper());
    }

    public String getImportAdjustmentErrorMsg(File importFile, boolean addToExistValue) throws Exception {
        logger.info("Begin import adjustment");
        logger.info("Import file is :" + importFile);
        String type = "listImportFileForm";
        ListImportFilePage listImportFilePage = openImportAdjustmentPage();
        String error = "";
        try {
            listImportFilePage.setImportFile(importFile, type);
            if (addToExistValue)
                listImportFilePage.selectAddToExistingValue(type);

            if (!listImportFilePage.isExistErrorMessage(type)) {
                element("lp.importBtn", type).click();
                waitStatusDlg();
            }
            /*
            if(!listImportFilePage.isExistErrorInfo())
            {
			 waitThat("lp.confirmBtn2",type).toBeClickable();
			 element("lp.confirmBtn2",type).click();
			 waitStatusDlg();
            }*/
            error = listImportFilePage.getErrorMessage(type);

            listImportFilePage.closeImportFileDlg(type);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	listImportFilePage.closeImportFileDlg(type);
        }
        
        return error;
    }

    public String getimportAdjustmentErrorInfo(File importFile, boolean addToExistValue) throws Exception {
        String type = "listImportFileForm";
        String info = "";
        ListImportFilePage importFileInReturnPage = openImportAdjustmentPage();
        try {
            importFileInReturnPage.setImportFile(importFile, type);
            if (addToExistValue)
                importFileInReturnPage.selectAddToExistingValue(type);
            info = importFileInReturnPage.getErrorInfo(type);
            importFileInReturnPage.closeImportFileDlg(type);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	importFileInReturnPage.closeImportFileDlg(type);
        }
        return info;
    }


    public String getCreateFromExcelErrorMsg(File importFile, boolean addToExistValue) throws Exception {
        logger.info("Begin create form form excel");
        logger.info("Import file is :" + importFile);
        String type = "createFromExcelForm";
        ListImportFilePage listImportFilePage = openImporExcelPage();
        try {
            listImportFilePage.setImportFile(importFile, type);
            if (addToExistValue)
                listImportFilePage.selectAddToExistingValue(type);
            waitThat("lp.importBtn", type).toBeClickable();
            element("lp.importBtn", type).click();
            waitThat("lp.confirmBtn2").toBeClickable();
            element("lp.closeConfirm").click();
            
            String error = listImportFilePage.getErrorMessage(type);
            listImportFilePage.closeImportFileDlg(type);
            logger.info("Message is: " + error);
            return error;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }finally{
        	listImportFilePage.closeImportFileDlg(type);
        }
        
    }


    public String getCreateFromExcelErrorInfo(File importFile) throws Exception {
        logger.info("Begin create form form excel");
        logger.info("Import file is :" + importFile);
        String type = "createFromExcelForm";
        ListImportFilePage listImportFilePage = openImporExcelPage();
        try {
            listImportFilePage.setImportFile(importFile, type);
            waitThat().timeout(1500);
            String error = listImportFilePage.getErrorInfo(type);
            listImportFilePage.closeImportFileDlg(type);
            return error;
        } catch (Exception e) {
            return null;
        }finally{
        	listImportFilePage.closeImportFileDlg(type);
        }

    }

    public String getCreateNewFormErrorMsg(String Group, String ProcessDate, String Form, String copyData, boolean AllowNull, boolean InitialiseToZeros) throws Exception {
        logger.info("Begin create new form[" + Form + "]");
        FormInstanceCreatePage formInstanceCreatePage = openFormInstanceCreatePage();
        try {
            formInstanceCreatePage.setGroup(Group);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String curDate = df.format(new Date());
            formInstanceCreatePage.setProcessDate(ProcessDate);
            formInstanceCreatePage.setForm(Form);
            if (copyData != null) {
                formInstanceCreatePage.selectCloneCheck();
                formInstanceCreatePage.setSelectCloneDate(copyData);
            }
            if (AllowNull) {
                assertThat(locator("fcp.zero")).displayed().isTrue();
                if (InitialiseToZeros)
                    formInstanceCreatePage.selectInitToZeroCheck();
            }
            formInstanceCreatePage.createConfirmClick();
            String errorMsg = formInstanceCreatePage.getErrorMsg();
            if (errorMsg.contains(curDate)) {
                errorMsg = errorMsg.replace(curDate, "18/01/2016");
            }
            formInstanceCreatePage.createCloseClick();
            return errorMsg;

        } catch (Exception e) {
            logger.error(e.toString());
            return "";
        }finally{
        	formInstanceCreatePage.createCloseClick();
        }
    }

    public FormInstancePage retrieveForm(String group, String referenceDate, String form, String logLevel) throws Exception {
        boolean openForm = false;
        logger.info("Begin set retrieve properties");
        FormInstanceRetrievePage retrievePage = null;
        try {
            retrievePage = openFormInstanceRetrievePage();
            logger.info("Set group= " + group);
            retrievePage.setGroup(group);
            logger.info("Set referenceDate= " + referenceDate);
            retrievePage.setReferenceDate(referenceDate);
            logger.info("Set form= " + form);
            retrievePage.setForm(form);

            RetrieveResultPage retrieveRstPage = null;
            if (logLevel != null) {
                logger.info("Set logLevel= " + logLevel);
                retrievePage.setLogLevel(logLevel);
            }
            logger.info("Click retrieve button");
            retrieveRstPage = retrievePage.retrieveConfirmClick();
            if (retrieveRstPage.isRetrieveResultPicFailed()) {
                logger.error("Retrieve form failed");
            } else if (retrieveRstPage.isRetrieveResultPicWarning()) {
                openForm = true;
                logger.error("Retrieve form completed,but there are some warnings.");
            } else if (retrieveRstPage.isRetrieveResultPicSuccess()) {
                openForm = true;
                logger.error("Retrieve form sucessed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (openForm) {
            FormInstancePage formInstancePage = retrievePage.openForm();
            return formInstancePage;
        } else
            return null;

    }

    public String ExportToRegulatorFormat(String Group, String formCode, String version, String ProcessDate, String FileType, String Framework, String Taxonomy, String Module,String CompressType) throws Exception {
        String filePath = null;
        ExportToFilePage exportToFilePage = openExportToFileBtnClick(FileType, formCode);
        exportToFilePage.setGroupSelector(Group);
        exportToFilePage.setReferenceDate(ProcessDate);
        if (Framework.length() > 1)
            exportToFilePage.setFrameworkSelector(Framework);
        if (Taxonomy.length() > 1)
            exportToFilePage.setTaxonomySelector(Taxonomy);
        if (Module.length() > 1)
            exportToFilePage.setModuleSelector(Module);

        if (FileType.equalsIgnoreCase("XBRL")) {
        	if(formCode.equalsIgnoreCase("all"))
        		exportToFilePage.selectAll();
        	else
        		exportToFilePage.selectForm_xbrl(formCode, version);
        	exportToFilePage.selectCompressType(null);
        }

        try {
            try {
                exportToFilePage.exportBtnClick();
            } catch (Exception e) {

            }
            boolean flag = true;
            long statTime = System.currentTimeMillis();
            while (flag) {
                try {

                    if (element("lp.exportError").getInnerText().startsWith("Processing terminated by xsl:message")) {
                        filePath = "Error";
                        break;
                    }
                } catch (NoSuchElementException e) {
                }
                long curTime = System.currentTimeMillis();
                if ((curTime - statTime) / 1000 > 5) {
                    break;
                }

            }
        } catch (Exception e) {

        }
        if (filePath == null)
            filePath = TestCaseManager.getTestCase().getDownloadFile();

        return filePath;

    }

    public UsersPage EnterUserPage() throws Exception {
        element("lp.settingBtn").click();
        waitStatusDlg();
        element("lp.Admin").click();
        waitStatusDlg();
        element("lp.admin_User").click();
        waitStatusDlg();
        logger.info("Enter user manage form");
        return new UsersPage(getWebDriverWrapper());
    }


    public UserGroupPage EnterUserGroupPage() throws Exception {
        element("lp.settingBtn").click();
        waitStatusDlg();
        element("lp.Admin").click();
        waitStatusDlg();
        element("lp.userGroupMag").click();
        waitStatusDlg();
        logger.info("Enter user group manage form");
        return new UserGroupPage(getWebDriverWrapper());
    }


    public PrivilegeGroupPage EnterPrivilegeGroupsPage() throws Exception {
        element("lp.settingBtn").click();
        waitStatusDlg();
        element("lp.Admin").click();
        waitStatusDlg();
        element("lp.permissionMag").click();
        waitStatusDlg();
        logger.info("Enter permission manage form");
        return new PrivilegeGroupPage(getWebDriverWrapper());
    }


    public EntityPage EnterEntityPage() throws Exception {
        waitThat("lp.settingBtn").toBePresentIn(3000);
        element("lp.settingBtn").click();
        waitStatusDlg();
        element("lp.entity").click();
        waitStatusDlg();
        logger.info("Enter entity manage form");
        return new EntityPage(getWebDriverWrapper());
    }

    public boolean isCreateNewDisplayed() throws Exception {
        try {
            return element("lp.createNewBtn").isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isComputeDisplayed() throws Exception {
        try {
            return element("lp.compute").isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    public boolean isImportAdjustmentDisplayed() throws Exception {
        try {
            return element("lp.import").isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isExportToRFDisplayed() throws Exception {
        try {
            element("lp.exportToFileBtn").click();
            waitStatusDlg();
            if(element("lp.firstOption").isDisplayed())
            	return true;
            else {
            	return false;
			}
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isHaveAdminPrivilege() throws Exception {
        boolean adminPriv = true;
        try {
            element("lp.settingBtn").click();
            waitStatusDlg();
            element("lp.Admin").click();
            waitStatusDlg();
            if(element("lp.admin_User").isDisplayed() && element("lp.userGroupMag").isDisplayed() && element("lp.permissionMag").isDisplayed() )
            	adminPriv = true;
        } catch (NoSuchElementException e) {
            adminPriv = false;
        }
        return adminPriv;
    }


    public boolean isExistFormInCreateNew(String Group, String ProcessDate, String Form) throws Exception {
        boolean isExistForm = true;
        FormInstanceCreatePage formInstanceCreatePage = null;
        try {
            formInstanceCreatePage = openFormInstanceCreatePage();
            formInstanceCreatePage.setGroup(Group);
            if (ProcessDate == null) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                ProcessDate = df.format(new Date());
            }

            formInstanceCreatePage.setProcessDate(ProcessDate);
            if (!getFormOptions_Create().contains(Form))
                isExistForm = false;
            
        } catch (Exception e) {
            isExistForm = false;
        }finally{
        	formInstanceCreatePage.createCloseClick();
        }

        return isExistForm;
    }
    
    public boolean isExistCreateNew() throws Exception{
    	return element("lp.createNewBtn").isDisplayed();
    }
    


    public void closeTransDlg() throws Exception {
        try {
            element("lp.closeTranDlg").click();
        } catch (NoSuchElementException e) {
        }
    }


    public void getProductListPage(String Regulator, String Group, String Form, String ProcessDate) throws Exception {
        logger.info("Enter list page");

        if (Regulator != null) {
            setRegulator(Regulator);
        }

        if (Group != null) {
            setGroup(Group);
        }

        if (Form != null) {
            setForm(Form);
        }

        if (ProcessDate != null) {
            setProcessDate(ProcessDate);
        }
        waitThat().timeout(2000);
    }

    public String getPopDialogTitle() throws Exception {
        return element("lp.popDlg").getInnerText();
    }

    public ComputePage enterComputePage() throws Exception {
        element("lp.computeBtn").click();
        waitStatusDlg();
        return new ComputePage(getWebDriverWrapper());
    }

    public boolean isFormExist(String Form, String ProcessDate) throws Exception {
        boolean existed = false;
        String formCode = splitReturn(Form).get(0);
        String version = splitReturn(Form).get(1);
        int rowAmt = getFormListRowSize();
        if (rowAmt > 0) {
            for (int i = 1; i <= rowAmt; i++) {
                if (element("lp.formCode", String.valueOf(i - 1)).getInnerText().equals(formCode) && element("lp.formVersion", String.valueOf(i)).getInnerText().equals(version)) {
                    if (element("lp.procesDate", String.valueOf(i)).getInnerText().equals(ProcessDate)) {
                        existed = true;
                        break;
                    }

                }

            }
        }
        return existed;
    }

    public DeleteReturnLogPage enterDeleteReturnLogPage() throws Exception {
        element("lp.deleteReturnLog").click();
        waitStatusDlg();
        return new DeleteReturnLogPage(getWebDriverWrapper());
    }
    
    public ShowDeletedReturnPage enterShowDeletedReturnPage() throws Exception{
    	element("lp.showDeleteReturn").click();
        waitStatusDlg();
        return new ShowDeletedReturnPage(getWebDriverWrapper());
    	
    }
    
    public boolean isExistImportAdjsutment() throws Exception{
    	return element("lp.import").isDisplayed();
    }
    
    public boolean isExistCopyData() throws Exception{
    	FormInstanceCreatePage formInstanceCreatePage = openFormInstanceCreatePage();
    	boolean rst=formInstanceCreatePage.isCopyDataDisplayed();
    	formInstanceCreatePage.closeCreateNew();
    	return rst;
    }
    
    
    

}
