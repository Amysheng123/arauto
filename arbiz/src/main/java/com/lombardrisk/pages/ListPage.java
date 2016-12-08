package com.lombardrisk.pages;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Refactor by Leo Tu on 2/1/16
 */
public class ListPage extends AbstractPage
{
	/**
	 *
	 * @param webDriverWrapper
	 */
	public ListPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/*
	 * private String getSelectedRegulator() throws Exception { return
	 * element("lp.slectedReg").getInnerText(); }
	 *
	 * private String getSelectedGroup() throws Exception { return
	 * element("lp.slectedGroup").getInnerText(); }
	 *
	 * private String getSelectedForm() throws Exception { return
	 * element("lp.slectedForm").getInnerText(); }
	 *
	 * private String getSelectedProcessDate() throws Exception { return
	 * element("lp.slectedPD").getInnerText(); }
	 */
	/**
	 * select regulator by visible text
	 *
	 * @param regulator
	 * @throws Exception
	 */
	public void setRegulator(String regulator) throws Exception
	{
		logger.info("Set regulator=" + regulator);
		if (!element("lp.REG").getSelectedText().equals(regulator))
		{
			element("lp.REG").selectByVisibleText(regulator);
			waitStatusDlg();
			Thread.sleep(1500);
		}
	}

	/**
	 * select regulator by value
	 *
	 * @param regulator
	 * @throws Exception
	 */
	public void setRegulatorByValue(String regulator) throws Exception
	{
		logger.info("Set regulator=" + regulator);
		element("lp.REG").selectByValue(regulator);
		waitStatusDlg();
	}

	/**
	 * select entity
	 *
	 * @param entityName
	 * @throws Exception
	 */
	public void setGroup(String entityName) throws Exception
	{
		logger.info("Set entity=" + entityName);
		waitForDropListLoaded("lp.Group");
		if (!element("lp.Group").getSelectedText().equals(entityName))
		{
			try
			{
				element("lp.Group").selectByVisibleText(entityName);
			}
			catch (Exception e)
			{
				Thread.sleep(1500);
				element("lp.Group").selectByVisibleText(entityName);
			}
			waitStatusDlg();
			Thread.sleep(1500);
		}
	}

	/**
	 * select form
	 *
	 * @param form
	 * @throws Exception
	 */
	public void setForm(String form) throws Exception
	{
		logger.info("Set form=" + form);
		waitForDropListLoaded("lp.Form");
		if (!element("lp.Form").getSelectedText().equals(form))
		{
			try
			{
				element("lp.Form").selectByVisibleText(form);
			}
			catch (Exception e)
			{
				Thread.sleep(1500);
				element("lp.Form").selectByVisibleText(form);
			}
			waitStatusDlg();
			Thread.sleep(2000);
		}
	}

	/**
	 * select reference date
	 *
	 * @param date
	 * @throws Exception
	 */
	public void setProcessDate(String date) throws Exception
	{
		logger.info("Set setProcessDate=" + date);
		waitForDropListLoaded("lp.Date");
		if (!element("lp.Date").getSelectedText().equals(date))
		{
			try
			{
				element("lp.Date").selectByVisibleText(date);
			}
			catch (Exception e)
			{
				Thread.sleep(1500);
				element("lp.Date").selectByVisibleText(date);
			}
			waitStatusDlg();
			Thread.sleep(2500);
		}
	}

	/**
	 * open form
	 *
	 * @param formCode
	 * @param version
	 * @param referenceDate
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage openFormInstance(String formCode, String version, String referenceDate) throws Exception
	{
		logger.info("Open form[" + formCode + " v" + version + " " + referenceDate + "]");
		waitStatusDlg();
		String list[] =
		{ formCode, version, referenceDate };
		logger.info("Click form link");
		element("lp.formLink", list).click();
		waitStatusDlg();
		waitThat().timeout(4000);
		if (element("lp.warnConfirmBtn").isDisplayed())
		{
			element("lp.warnConfirmBtn").click();
			waitStatusDlg();
			waitThat().timeout(2000);
		}

		return new FormInstancePage(getWebDriverWrapper());
	}

	/**
	 * Open fist form
	 *
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage openFirstFormInstance() throws Exception
	{
		logger.info("Open first form");
		waitStatusDlg();
		element("lp.firstFormLink").click();
		waitStatusDlg();
		waitThat().timeout(2000);
		if (element("lp.warnConfirmBtn").isDisplayed())
		{
			element("lp.warnConfirmBtn").click();
			waitStatusDlg();
		}
		waitStatusDlg();
		return new FormInstancePage(getWebDriverWrapper());
	}

	/**
	 * open change user password page
	 *
	 * @return openChangePasswordPage
	 * @throws Exception
	 */
	public ChangePasswordPage openChangePasswordPage() throws Exception
	{
		element("lp.uMenu").click();
		waitStatusDlg();
		element("lp.changePwd").click();
		waitStatusDlg();
		return new ChangePasswordPage(getWebDriverWrapper());
	}

	/**
	 * open create form page
	 *
	 * @return FormInstanceCreatePage
	 * @throws Exception
	 */
	private FormInstanceCreatePage openFormInstanceCreatePage() throws Exception
	{
		waitStatusDlg();
		logger.info("click create new button");
		element("lp.createNewBtn").click();
		waitStatusDlg();
		logger.info("click create new form");
		element("lp.createNewForm").click();
		waitThat("lp.createNewFormDialog").toBeVisible();
		return new FormInstanceCreatePage(getWebDriverWrapper());
	}

	/**
	 * open create form from excel page
	 *
	 * @return FormInstanceCreatePage
	 * @throws Exception
	 */
	public FormInstanceCreatePage openFormInstanceCreateFromExcelPage() throws Exception
	{
		element("lp.createNewBtn").click();
		waitStatusDlg();
		element("lp.createFromXls").click();
		waitStatusDlg();
		return new FormInstanceCreatePage(getWebDriverWrapper());
	}

	/**
	 * open retrieve page
	 *
	 * @return FormInstanceRetrievePage
	 * @throws Exception
	 */
	public FormInstanceRetrievePage openFormInstanceRetrievePage() throws Exception
	{
		try
		{
			element("lp.retrieve").click();
		}
		catch (Exception e)
		{
			waitThat().timeout(3000);
			element("lp.retrieve").click();
		}
		waitStatusDlg();
		return new FormInstanceRetrievePage(getWebDriverWrapper());
	}

	/**
	 * logout
	 *
	 * @return HomePage
	 * @throws Exception
	 */
	public HomePage logout() throws Exception
	{
		if (element("lp.message").isDisplayed())
			waitThat("lp.message").toBeInvisible();
		element("lp.uMenu").click();
		waitStatusDlg();
		element("lp.Logout").click();
		return new HomePage(getWebDriverWrapper());
	}

	/**
	 * get regulator options
	 *
	 * @return List
	 * @throws Exception
	 */
	public List<String> getRegulatorOptions() throws Exception
	{
		return element("lp.REG").getAllOptionTexts();
	}

	/**
	 * get entity options
	 *
	 * @return List
	 * @throws Exception
	 */
	public List<String> getGroupOptions() throws Exception
	{
		return element("lp.Group").getAllOptionTexts();
	}

	/**
	 * get form options
	 *
	 * @return Options
	 * @throws Exception
	 */
	public List<String> getFormOptions() throws Exception
	{
		List<String> forms = element("lp.Form").getAllOptionTexts();
		forms.remove(0);
		return forms;
	}

	/**
	 * get reference date options
	 *
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getProcessDateOptions() throws Exception
	{
		List<String> dates = element("lp.Date").getAllOptionTexts();
		dates.remove(0);
		return dates;
	}

	/**
	 * get entity options in create form page
	 *
	 * @return List
	 * @throws Exception
	 */
	public List<String> getGroupOptions_Create() throws Exception
	{
		return element("lp.CGroup").getAllOptionTexts();
	}

	/**
	 * get form options in create form page
	 *
	 * @return List
	 * @throws Exception
	 */
	public List<String> getFormOptions_Create() throws Exception
	{
		return element("lp.CForm").getAllOptionTexts();
	}

	/**
	 * get form options in create form page
	 *
	 * @param Group
	 * @param ProcessDate
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getFormOptions_Create(String Group, String ProcessDate) throws Exception
	{
		FormInstanceCreatePage formInstanceCreatePage = openFormInstanceCreatePage();

		formInstanceCreatePage.setGroup(Group);
		if (ProcessDate == null)
		{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			ProcessDate = df.format(new Date());
		}
		formInstanceCreatePage.setProcessDate(ProcessDate);

		List<String> forms = getFormOptions_Create();
		formInstanceCreatePage.createCloseClick();
		waitThat().timeout(500);
		return forms;

	}

	/**
	 * get form options in retrieve page
	 *
	 * @param Group
	 * @param ProcessDate
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getFormOptions_Retrieve(String Group, String ProcessDate) throws Exception
	{
		FormInstanceRetrievePage retrievePage = openFormInstanceRetrievePage();
		retrievePage.setGroup(Group);
		if (ProcessDate == null)
		{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			ProcessDate = df.format(new Date());
		}
		retrievePage.setReferenceDate(ProcessDate);
		List<String> forms = retrievePage.getFormOptions();
		retrievePage.retrieveCloseClick();
		return forms;
	}

	/**
	 * get clone date options in create page
	 *
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getCloneDateOptions_Create() throws Exception
	{
		return element("lp.CloneDate").getAllOptionTexts();
	}

	/**
	 * click export to xbrl, enter export to xbrl page
	 *
	 * @return ExportXBRLPage
	 * @throws Exception
	 */
	public ExportXBRLPage clickXBRLButton() throws Exception
	{
		element("lp.xbrlSubmit").click();
		waitStatusDlg();
		return new ExportXBRLPage(getWebDriverWrapper());
	}

	/**
	 * click export button
	 *
	 * @return exported file
	 * @throws Exception
	 */
	private String clickExportButton() throws Exception
	{
		TestCaseManager.getTestCase().startTransaction("");
		TestCaseManager.getTestCase().setPrepareToDownload(true);
		element("lp.exportFormList").click();
		TestCaseManager.getTestCase().stopTransaction();
		String exportedFile = TestCaseManager.getTestCase().getDownloadFile();
		String oldName = new File(exportedFile).getName();
		String path = new File(exportedFile).getAbsolutePath().replace(oldName, "");
		String fileName = TestCaseManager.getTestCase().getDefaultDownloadFileName();
		String file = null;
		if (fileName == null || fileName.length() == 0)
		{
			file = exportedFile;
		}
		else
		{
			renameFile(path, oldName, fileName);
			file = path + fileName;
		}
		return file;

	}

	/**
	 * open import adjustment page
	 *
	 * @return ListImportFilePage
	 * @throws Exception
	 */
	public ListImportFilePage openImportAdjustmentPage() throws Exception
	{
		element("lp.import").click();
		waitThat("lp.importDialog").toBeVisible();
		return new ListImportFilePage(getWebDriverWrapper());
	}

	/**
	 * enter ListImportFilePage
	 *
	 * @return ListImportFilePage
	 * @throws Exception
	 */
	private ListImportFilePage openImporExcelPage() throws Exception
	{
		element("lp.createNewBtn").click();
		waitStatusDlg();
		element("lp.createFromXls").click();
		waitThat("lp.createFromXlsDialog").toBeVisible();
		return new ListImportFilePage(getWebDriverWrapper());
	}

	/**
	 * enter ExportToFilePage
	 *
	 * @param fileType
	 * @param Form
	 * @return ExportToFilePage
	 * @throws Exception
	 */
	public ExportToFilePage openExportToFileBtnClick(String fileType, String Form) throws Exception
	{
		element("lp.exportToFileBtn").click();
		waitStatusDlg();
		exportToFile(fileType, Form);
		return new ExportToFilePage(getWebDriverWrapper());
	}

	/**
	 * export to file
	 *
	 * @param fileType
	 * @param Form
	 * @throws Exception
	 */
	private void exportToFile(String fileType, String Form) throws Exception
	{
		getFormatFromDB();
		String optionName = null;
		String formCode = null, version = null;
		if (Form != null)
		{
			formCode = Form.split(" ")[0].toUpperCase();
			version = Form.split(" ")[1].substring(1);
		}
		if (fileType.toLowerCase().startsWith("text"))
		{
			if (fileType.contains("(") && fileType.contains(")"))
			{
				fileType = fileType.replace("(", "#").replace(")", "");
				String option = fileType.split("#")[1];
				optionName = "Export To " + option;
			}
			else
				optionName = "Export To RC_" + formCode;
		}
		else if (fileType.equalsIgnoreCase("Vanilla"))
		{
			if (format.equalsIgnoreCase("zh_CN"))
				optionName = "导出到Vanilla";
			else
				optionName = "Export To Vanilla";
		}
		else if (fileType.equalsIgnoreCase("ARBITRARY"))
		{
			if (format.equalsIgnoreCase("zh_CN"))
				optionName = "导出到Vanilla";
			else
				optionName = "Export To ARBITRARY";
			if (!element("lp.exportToFileOption", optionName).isDisplayed())
				optionName = "Export To " + formCode;
		}
		else if (fileType.equalsIgnoreCase("XBRL"))
		{
			if (format.equalsIgnoreCase("zh_CN"))
				optionName = "导出到Vanilla";
			else
				optionName = "Export To XBRL";
		}
		else if (fileType.equalsIgnoreCase("iFile"))
		{
			if (format.equalsIgnoreCase("zh_CN"))
				optionName = "导出到Vanilla";
			else
				optionName = "Export To iFile";
			if (!element("lp.exportToFileOption", optionName).isDisplayed())
				optionName = "Export To " + formCode;

		}
		else if (fileType.equalsIgnoreCase("ds"))
		{
			optionName = "Export To DataSchedule";
		}
		if (element("lp.exportToFileOption", optionName).isDisplayed())
			element("lp.exportToFileOption", optionName).click();
		else
		{
			optionName = "Export To RC_" + formCode + "_" + version;
			element("lp.exportToFileOption", optionName).click();
		}
		waitStatusDlg();
		Thread.sleep(2000);
	}

	/**
	 * enter CalendarPage
	 *
	 * @return CalendarPage
	 * @throws Exception
	 */
	public CalendarPage openCalendarPage() throws Exception
	{
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

	/**
	 * enter SchedulePage
	 *
	 * @return SchedulePage
	 * @throws Exception
	 */
	public SchedulePage openSchedulePage() throws Exception
	{
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.adminMenu").click();
		waitStatusDlg();
		element("lp.calendarMenu").click();
		waitStatusDlg();
		element("lp.subSchMenu").click();
		return new SchedulePage(getWebDriverWrapper());
	}

	/**
	 * enter FormSchedulePage
	 *
	 * @return FormSchedulePage
	 * @throws Exception
	 */
	public FormSchedulePage openFormSchedulePage() throws Exception
	{
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.adminMenu").click();
		waitStatusDlg();
		element("lp.formSchedule").click();
		return new FormSchedulePage(getWebDriverWrapper());
	}

	/**
	 * verify if form is locked
	 *
	 * @param form
	 * @param processDate
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isFormLockedInList(String form, String processDate) throws Exception
	{
		if (element("lp.formlock").getInnerText().equals("LOCK"))
			return true;
		else
			return false;
	}

	/**
	 * get user name
	 *
	 * @return userName
	 * @throws Exception
	 */
	public String getUserName() throws Exception
	{
		return element("lp.userName").getInnerText().replace("hi ", "");
	}

	/**
	 * get rows of form list
	 *
	 * @return rowNums
	 * @throws Exception
	 */
	public int getFormListRowSize() throws Exception
	{
		try
		{
			if (element("lp.noRecords").getInnerText().equals("No records found."))
				return 0;
			else
				return (int) element("lp.formListTab").getRowCount();
		}
		catch (Exception e)
		{
			return (int) element("lp.formListTab").getRowCount();
		}
	}

	/**
	 * get form rowindex
	 *
	 * @param formCode
	 * @param version
	 * @param referenceDate
	 * @return rowindex
	 * @throws Exception
	 */
	public int getFormInstanceRowPos(String formCode, String version, String referenceDate) throws Exception
	{
		String list[] =
		{ formCode, version, referenceDate };
		int index = 0;
		if (element("lp.formLink", list).isPresent())
		{
			String r = element("lp.formLink", list).getAttribute("id").replace("formInstanceListForm:formInstanceListTable:", "").replace(":selectButton", "");
			index = Integer.parseInt(r) + 1;
		}

		return index;
	}

	/**
	 * get form info(Get form details from list 0:entity 1:formCode
	 * 2:fromVersion 3:referenceDate 4:retrieveJob 5:exportJob 6:lock/Unlock
	 * 7:validation 8:Xvalidation 9:approve1 10:export 11:approve status)
	 *
	 * @param rowIndex
	 * @return DetailInfo
	 * @throws Exception
	 */
	public List<String> getFormDetailInfo(int rowIndex) throws Exception
	{
		List<String> formDetailInfo = new ArrayList<String>();
		String list[] = new String[2];
		list[0] = String.valueOf(rowIndex);
		for (int i = 1; i <= 4; i++)
		{
			list[1] = String.valueOf(i);
			if (i == 2)
				formDetailInfo.add(element("lp.formCode", String.valueOf(rowIndex - 1)).getInnerText());
			else
				formDetailInfo.add(element("lp.cell.text1", list).getInnerText());
		}
		String status = element("lp.job.retrieve", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Success"))
			formDetailInfo.add("Success");
		else
			formDetailInfo.add("UNKNOWN");

		status = element("lp.job.export", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Success"))
			formDetailInfo.add("Success");
		else if (status.endsWith("Abandoned"))
			formDetailInfo.add("UNKNOWN");
		else
			formDetailInfo.add("Fail");

		status = element("lp.list.lock", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("IsLocked"))
			formDetailInfo.add("Lock");
		else
			formDetailInfo.add("unlock");

		status = element("lp.list.val", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Validated"))
			formDetailInfo.add("Pass");
		else if (status.endsWith("ValidationFailure"))
			formDetailInfo.add("Fail");
		else
			formDetailInfo.add("UNKNOWN");

		status = element("lp.list.xval", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Validated"))
			formDetailInfo.add("Pass");
		else if (status.endsWith("ValidationFailure"))
			formDetailInfo.add("Fail");
		else
			formDetailInfo.add("UNKNOWN");

		status = element("lp.list.assest", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Attested"))
			formDetailInfo.add("Attested");
		else if (status.endsWith("Rejected"))
			formDetailInfo.add("Rejected");
		else if (status.endsWith("AWaitingApproval"))
			formDetailInfo.add("AWaiting");
		else
			formDetailInfo.add("UNKNOWN");

		status = element("lp.list.export", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Exported"))
			formDetailInfo.add("Exported");
		else if (status.endsWith("ExportFailure"))
			formDetailInfo.add("ExportFailure");
		else
			formDetailInfo.add("UNKNOWN");

		status = element("lp.approveLink", String.valueOf(rowIndex - 1)).getInnerText();
		formDetailInfo.add(status);
		return formDetailInfo;
	}

	/**
	 * get form info(Get form details from list 0:entity 1:formCode
	 * 2:fromVersion 3:referenceDate 4:lock/Unlock 5:validation 6:Xvalidation
	 * 7:approve1 8:approve status)
	 *
	 * @param rowIndex
	 * @return DetailInfo
	 * @throws Exception
	 */
	public List<String> getFormDetailInfo_toolset(int rowIndex) throws Exception
	{
		List<String> formDetailInfo = new ArrayList<String>();
		String list[] = new String[2];
		list[0] = String.valueOf(rowIndex);
		for (int i = 1; i <= 4; i++)
		{
			list[1] = String.valueOf(i);
			if (i == 2)
				formDetailInfo.add(element("lp.formCode", String.valueOf(rowIndex - 1)).getInnerText());
			else
				formDetailInfo.add(element("lp.cell.text1", list).getInnerText());
		}

		String status = element("lp.list.lock", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("IsLocked"))
			formDetailInfo.add("Lock");
		else
			formDetailInfo.add("unlock");

		status = element("lp.list.val", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Validated"))
			formDetailInfo.add("Pass");
		else if (status.endsWith("ValidationFailure"))
			formDetailInfo.add("Fail");
		else
			formDetailInfo.add("UNKNOWN");

		status = element("lp.list.xval", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Validated"))
			formDetailInfo.add("Pass");
		else if (status.endsWith("ValidationFailure"))
			formDetailInfo.add("Fail");
		else
			formDetailInfo.add("UNKNOWN");

		status = element("lp.list.assest", String.valueOf(rowIndex - 1)).getAttribute("id");
		if (status.endsWith("Attested"))
			formDetailInfo.add("Attested");
		else if (status.endsWith("Rejected"))
			formDetailInfo.add("Rejected");
		else if (status.endsWith("WaitingApproval"))
			formDetailInfo.add("Waiting");
		else
			formDetailInfo.add("UNKNOWN");

		status = element("lp.approveLink", String.valueOf(rowIndex - 1)).getInnerText();
		formDetailInfo.add(status);
		return formDetailInfo;
	}

	/**
	 * delete form
	 *
	 * @param Form
	 * @param referenceDate
	 * @throws Exception
	 */
	public void deleteFormInstance(String Form, String referenceDate) throws Exception
	{
		logger.info("Delete form[" + Form + " " + referenceDate + "] if exist");
		boolean delete = false;
		int rowIndex = 1;
		if (Form != null)
		{
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			String list[] =
			{ formCode, version, referenceDate };
			try
			{
				if (element("lp.formLink", list).isPresent())
				{
					rowIndex = Integer.parseInt(element("lp.formLink", list).getAttribute("id").replace("formInstanceListForm:formInstanceListTable:", "").replace(":selectButton", "")) + 1;
					delete = true;
				}
			}
			catch (Exception e)
			{
				logger.info("Form does not exist");
			}
		}

		if (delete)
		{
			element("lp.delFormIcon1", String.valueOf(rowIndex - 1)).click();
			waitStatusDlg();
			element("lp.delConfBtn").click();
			waitStatusDlg();
			element("lp.deleteComment").input("Delete return by automation");
			element("lp.deleteReturnBtn").click();
			waitStatusDlg();
			waitThat("lp.message").toBeInvisible();
			Thread.sleep(1500);
		}
		else
		{
			logger.warn("Form[" + Form + "@" + referenceDate + "] does not exist");
		}

	}

	/**
	 * delete form
	 *
	 * @param Form
	 * @param date
	 * @param comment
	 * @param save
	 * @throws Exception
	 */
	public void deleteFormInstance(String Form, String date, String comment, boolean save) throws Exception
	{
		logger.info("Delete form[" + Form + " " + date + "(" + date + ")]");
		if (Form != null)
		{
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			int rowAmt = getFormListRowSize();
			logger.info("There are " + rowAmt + " records in list");
			for (int i = 1; i <= rowAmt; i++)
			{
				String list1[] = new String[2];
				String list2[] = new String[2];
				if (element("lp.delFormIcon1", String.valueOf(i - 1)).isPresent())
				{
					list1[0] = String.valueOf(i);
					list1[1] = "3";
					list2[0] = String.valueOf(i);
					list2[1] = "4";
				}
				else
				{
					list1[0] = String.valueOf(i);
					list1[1] = "2";
					list2[0] = String.valueOf(i);
					list2[1] = "3";
				}

				if (element("lp.formCode", String.valueOf(i - 1)).getInnerText().equals(formCode) && element("lp.cell.text1", list1).getInnerText().equals(version))
				{
					if (date != null && element("lp.cell.text1", list2).getInnerText().equals(date))
					{
						element("lp.delFormIcon1", String.valueOf(i - 1)).click();
						if (save)
						{
							element("lp.delConfBtn").click();
							waitStatusDlg();
							element("lp.deleteComment").input(comment);
							element("lp.deleteReturnBtn").click();
							waitStatusDlg();
							waitThat("lp.message").toBeInvisible();
							Thread.sleep(1500);
						}
						else
							element("lp.deleteReturnCancelBtn").click();

						break;
					}
				}
			}
		}

	}

	/**
	 * get message when delete from
	 *
	 * @param Form
	 * @param date
	 * @return message
	 * @throws Exception
	 */
	public String getDeleteFormInstanceMessage(String Form, String date) throws Exception
	{
		logger.info("Delete form[" + Form + " " + date + "(" + date + ")]");
		String message = null;
		if (Form != null)
		{
			String formCode = splitReturn(Form).get(0);
			String version = splitReturn(Form).get(1);
			int rowAmt = getFormListRowSize();
			logger.info("There are " + rowAmt + " records in list");
			for (int i = 1; i <= rowAmt; i++)
			{
				String list1[] = new String[2];
				String list2[] = new String[2];
				if (element("lp.delFormIcon1", String.valueOf(i - 1)).isPresent())
				{
					list1[0] = String.valueOf(i);
					list1[1] = "3";
					list2[0] = String.valueOf(i);
					list2[1] = "4";

				}
				else
				{
					list1[0] = String.valueOf(i);
					list1[1] = "2";
					list2[0] = String.valueOf(i);
					list2[1] = "3";
				}

				if (element("lp.formCode", String.valueOf(i - 1)).getInnerText().equals(formCode) && element("lp.cell.text1", list1).getInnerText().equals(version))
				{
					if (date != null && element("lp.cell.text1", list2).getInnerText().equals(date))
					{
						element("lp.delFormIcon1", String.valueOf(i - 1)).click();
						waitStatusDlg();
						if (element("lp.deleteReturnMsg1").isDisplayed())
						{
							message = element("lp.deleteReturnMsg1").getInnerText() + element("lp.deleteReturnMsg2").getInnerText();
							element("lp.deleteReturnCancelBtn").click();
						}
						else
						{
							element("lp.delConfBtn").click();
							element("lp.deleteReturnBtn").click();
							waitThat().timeout(300);
							message = element("lp.message").getInnerText();
							waitThat("lp.message").toBeInvisible();
						}
						break;
					}
				}
			}
		}
		return message;

	}

	/**
	 * enter edition manager page
	 *
	 * @param formCode
	 * @param version
	 * @param date
	 * @return EditionManagePage
	 * @throws Exception
	 */
	public EditionManagePage openEditionManage(String formCode, String version, String date) throws Exception
	{
		element("lp.editionMag", String.valueOf(getFormInstanceRowPos(formCode, version, date) - 1)).click();
		waitStatusDlg();
		return new EditionManagePage(getWebDriverWrapper());
	}

	/**
	 * enter edition manager page
	 *
	 * @param index
	 * @return EditionManagePage
	 * @throws Exception
	 */
	public EditionManagePage openEditionManage(int index) throws Exception
	{
		logger.info("Open edition manage");
		waitStatusDlg();
		element("lp.editionMag", String.valueOf(index - 1)).click();
		waitStatusDlg();
		return new EditionManagePage(getWebDriverWrapper());
	}

	/**
	 * create new form
	 *
	 * @param Group
	 * @param ProcessDate
	 * @param Form
	 * @param copyData
	 * @param AllowNull
	 * @param InitialiseToZeros
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage createNewForm(String Group, String ProcessDate, String Form, String copyData, boolean AllowNull, boolean InitialiseToZeros) throws Exception
	{
		logger.info("Begin create new form[" + Form + "]");
		FormInstanceCreatePage formInstanceCreatePage = openFormInstanceCreatePage();
		try
		{
			formInstanceCreatePage.setGroup(Group);
			if (ProcessDate == null)
			{
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				ProcessDate = df.format(new Date());
			}
			formInstanceCreatePage.setProcessDate(ProcessDate);
			formInstanceCreatePage.setForm(Form);
			if (copyData != null)
			{
				formInstanceCreatePage.selectCloneCheck();
				formInstanceCreatePage.setSelectCloneDate(copyData);
			}
			if (AllowNull)
			{
				assertThat(locator("fcp.zero")).displayed().isTrue();
				if (InitialiseToZeros)
					formInstanceCreatePage.selectInitToZeroCheck();
			}
			FormInstancePage formInstancePage = formInstanceCreatePage.createConfirmClick();
			waitStatusDlg();
			Thread.sleep(3000);
			return formInstancePage;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			formInstanceCreatePage.createCloseClick();
			return null;
		}
	}

	/**
	 * create form from excel
	 *
	 * @param importFile
	 * @param allowNull
	 * @param InitialiseToZeros
	 * @param openForm
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage createFormFromExcel(File importFile, boolean allowNull, boolean InitialiseToZeros, boolean openForm) throws Exception
	{
		logger.info("Begin create form from excel");
		logger.info("Import file is :" + importFile);
		String type = "createFromExcelForm";
		ListImportFilePage listImportFilePage = null;
		try
		{
			listImportFilePage = openImporExcelPage();
			listImportFilePage.setImportFile(importFile, type);
			FormInstancePage formInstancePage = null;
			if (allowNull)
			{
				listImportFilePage.assertExistIniToZero(type);
				if (InitialiseToZeros)
					listImportFilePage.tickInitToZero(type);
			}
			if (!openForm)
				element("lp.openFormCheckBox").click();
			try
			{
				ImportConfirmPage confirmPage = listImportFilePage.importFileBtnClick(type);
				formInstancePage = confirmPage.confirmBtnClick(type);

			}
			catch (Exception e)
			{
				logger.error("Create form form excel failed");
				listImportFilePage.closeImportFileDlg(type);
				e.printStackTrace();
			}
			if (element("lp.warnConfirmBtn").isDisplayed())
			{
				element("lp.warnConfirmBtn").click();
				waitStatusDlg();
			}
			waitStatusDlg();
			Thread.sleep(3000);
			return formInstancePage;
		}
		catch (Exception e)
		{
			return null;
		}

	}

	/**
	 * import adjustment file
	 *
	 * @param importFile
	 * @param addToExistValue
	 * @param InitialiseToZeros
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage importAdjustment(File importFile, boolean addToExistValue, boolean InitialiseToZeros) throws Exception
	{
		logger.info("Begin import adjustment");
		logger.info("Import file is :" + importFile);
		String type = "listImportFileForm";
		ListImportFilePage listImportFilePage = openImportAdjustmentPage();
		try
		{
			listImportFilePage.setImportFile(importFile, type);
			if (addToExistValue)
				listImportFilePage.selectAddToExistingValue(type);
			if (InitialiseToZeros)
				listImportFilePage.tickInitToZero(type);
			try
			{
				ImportConfirmPage confirmPage = listImportFilePage.importFileBtnClick(type);
				confirmPage.confirmBtnClick(type);
			}
			catch (Exception e)
			{
				logger.error("Import adjustment failed");
				e.printStackTrace();
			}
			Thread.sleep(500);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			listImportFilePage.closeImportFileDlg(type);
		}
		return new FormInstancePage(getWebDriverWrapper());
	}

	/**
	 * get error message when import adjustment
	 *
	 * @param importFile
	 * @param addToExistValue
	 * @return error message
	 * @throws Exception
	 */
	public String getImportAdjustmentErrorMsg(File importFile, boolean addToExistValue) throws Exception
	{
		logger.info("Begin import adjustment");
		logger.info("Import file is :" + importFile);
		String type = "listImportFileForm";
		ListImportFilePage listImportFilePage = openImportAdjustmentPage();
		String error = "";
		try
		{
			listImportFilePage.setImportFile(importFile, type);
			if (addToExistValue)
				listImportFilePage.selectAddToExistingValue(type);

			if (!listImportFilePage.isExistErrorMessage(type))
			{
				element("lp.importBtn", type).click();
				waitStatusDlg();
			}
			error = listImportFilePage.getErrorMessage(type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			listImportFilePage.closeImportFileDlg(type);
		}

		return error;
	}

	/**
	 * get error info when import adjustment
	 *
	 * @param importFile
	 * @param addToExistValue
	 * @return error info
	 * @throws Exception
	 */
	public String getimportAdjustmentErrorInfo(File importFile, boolean addToExistValue) throws Exception
	{
		String type = "listImportFileForm";
		String info = "";
		ListImportFilePage importFileInReturnPage = openImportAdjustmentPage();
		try
		{
			importFileInReturnPage.setImportFile(importFile, type);
			info = importFileInReturnPage.getErrorInfo(type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			importFileInReturnPage.closeImportFileDlg(type);
		}
		return info;
	}

	/**
	 * get error info when create form frm excel
	 *
	 * @param importFile
	 * @param addToExistValue
	 * @return error info
	 * @throws Exception
	 */
	public String getCreateFromExcelErrorMsg(File importFile, boolean addToExistValue) throws Exception
	{
		logger.info("Begin create form form excel");
		logger.info("Import file is :" + importFile);
		String type = "createFromExcelForm";
		ListImportFilePage listImportFilePage = openImporExcelPage();
		try
		{
			listImportFilePage.setImportFile(importFile, type);
			String error = listImportFilePage.getErrorMessage(type);
			if (error.length() == 0)
			{
				element("lp.importBtn", type).click();
				waitStatusDlg();
				error = listImportFilePage.getErrorMessage(type);
				if (element("lp.confirmBtn2").isDisplayed())
				{
					element("lp.confirmBtn2").click();
					waitStatusDlg();
				}

			}
			logger.info("Message is: " + error);
			return error;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		finally
		{
			listImportFilePage.closeImportFileDlg(type);
		}

	}

	/**
	 * get error message when create form frm excel
	 *
	 * @param importFile
	 * @return error info
	 * @throws Exception
	 */
	public String getCreateFromExcelErrorInfo(File importFile) throws Exception
	{
		logger.info("Begin create form form excel");
		logger.info("Import file is :" + importFile);
		String type = "createFromExcelForm";
		ListImportFilePage listImportFilePage = openImporExcelPage();
		try
		{
			listImportFilePage.setImportFile(importFile, type);
			waitThat().timeout(1500);
			String error = listImportFilePage.getErrorInfo(type);
			return error;
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			listImportFilePage.closeImportFileDlg(type);
		}

	}

	/**
	 * get error message when create form
	 *
	 * @param Group
	 * @param ProcessDate
	 * @param Form
	 * @param copyData
	 * @param AllowNull
	 * @param InitialiseToZeros
	 * @return error message
	 * @throws Exception
	 */
	public String getCreateNewFormErrorMsg(String Group, String ProcessDate, String Form, String copyData, boolean AllowNull, boolean InitialiseToZeros) throws Exception
	{
		logger.info("Begin create new form[" + Form + "]");
		FormInstanceCreatePage formInstanceCreatePage = openFormInstanceCreatePage();
		try
		{
			formInstanceCreatePage.setGroup(Group);
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String curDate = df.format(new Date());
			if (ProcessDate != null)
			{
				if (ProcessDate.length() < 9)
					formInstanceCreatePage.setProcessDate2(ProcessDate);
				else
					formInstanceCreatePage.setProcessDate(ProcessDate);
			}

			formInstanceCreatePage.setForm(Form);
			if (copyData != null)
			{
				formInstanceCreatePage.selectCloneCheck();
				formInstanceCreatePage.setSelectCloneDate(copyData);
			}
			if (AllowNull)
			{
				assertThat(locator("fcp.zero")).displayed().isTrue();
				if (InitialiseToZeros)
					formInstanceCreatePage.selectInitToZeroCheck();
			}
			formInstanceCreatePage.createConfirmClick();
			String errorMsg = formInstanceCreatePage.getErrorMsg();
			if (errorMsg.contains(curDate))
			{
				errorMsg = errorMsg.replace(curDate, "18/01/2016");
			}
			return errorMsg;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		finally
		{
			formInstanceCreatePage.createCloseClick();
		}
	}

	/**
	 * retrieve form
	 *
	 * @param group
	 * @param referenceDate
	 * @param form
	 * @param logLevel
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage retrieveForm(String group, String referenceDate, String form, String logLevel) throws Exception
	{
		boolean openForm = false;
		logger.info("Begin set retrieve properties");
		FormInstanceRetrievePage retrievePage = null;
		try
		{
			retrievePage = openFormInstanceRetrievePage();
			logger.info("Set group= " + group);
			retrievePage.setGroup(group);
			logger.info("Set referenceDate= " + referenceDate);
			retrievePage.setReferenceDate(referenceDate);
			logger.info("Set form= " + form);
			retrievePage.setForm(form);

			RetrieveResultPage retrieveRstPage = null;
			if (logLevel != null)
			{
				logger.info("Set logLevel= " + logLevel);
				retrievePage.setLogLevel(logLevel);
			}
			logger.info("Click retrieve button");
			retrieveRstPage = retrievePage.retrieveConfirmClick();
			if (retrieveRstPage.isRetrieveResultPicFailed())
			{
				logger.error("Retrieve form failed");
			}
			else if (retrieveRstPage.isRetrieveResultPicWarning())
			{
				openForm = true;
				logger.error("Retrieve form completed,but there are some warnings.");
			}
			else if (retrieveRstPage.isRetrieveResultPicSuccess())
			{
				openForm = true;
				logger.error("Retrieve form sucessed");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (openForm)
		{
			FormInstancePage formInstancePage = retrievePage.openForm();
			return formInstancePage;
		}
		else
			return null;

	}

	/**
	 * Export form to file
	 *
	 * @param Group
	 * @param Form
	 * @param ProcessDate
	 * @param FileType
	 *            (Excel,csv,text,vanilla,xbrl,iFile,arbitrary)
	 * @param Framework
	 * @param Taxonomy
	 * @param Module
	 * @param compressType
	 * @return exported file
	 * @throws Exception
	 */
	public String ExportToRegulatorFormat(String Group, String Form, String ProcessDate, String FileType, String Framework, String Taxonomy, String Module, String compressType) throws Exception
	{
		String filePath = null;
		ExportToFilePage exportToFilePage = openExportToFileBtnClick(FileType, Form);
		exportToFilePage.setGroupSelector(Group, FileType);
		exportToFilePage.setReferenceDate(ProcessDate, FileType);
		if (Framework != null && Framework.length() > 1)
			exportToFilePage.setFrameworkSelector(Framework, FileType);
		if (Taxonomy != null && Taxonomy.length() > 1)
			exportToFilePage.setTaxonomySelector(Taxonomy, FileType);
		if (Module != null && Module.length() > 1)
			exportToFilePage.setModuleSelector(Module, FileType);

		if (FileType.equalsIgnoreCase("XBRL"))
		{
			ExportXBRLPage exportXBRLPage = new ExportXBRLPage(getWebDriverWrapper());
			if (Form != null && !Form.equalsIgnoreCase("ALL"))
			{
				exportXBRLPage.deselectAll();
				for (String form : Form.split("#"))
				{
					exportXBRLPage.selectForm(form);
				}
			}
			exportXBRLPage.selectCompressType(compressType);
		}
		String dir = FileUtils.getUserDirectoryPath() + "/downloads";
		String latestFile = getLatestFile(dir);
		exportToFilePage.exportBtnClick(FileType);

		boolean flag = true;
		long statTime = System.currentTimeMillis();
		while (flag)
		{
			if (element("lp.messageTitle").isDisplayed())
			{
				if (element("lp.messageTitle").getInnerText().equalsIgnoreCase("error"))
				{
					filePath = "Error";
					break;
				}
			}
			long curTime = System.currentTimeMillis();
			if ((curTime - statTime) / 1000 > 5)
			{
				break;
			}
		}
		if (element("lp.xbrlOK").isDisplayed())
		{
			element("lp.xbrlOK").click();
			waitStatusDlg();
		}
		if (filePath == null)
			if (httpDownload)
			{
				String exportedFile = TestCaseManager.getTestCase().getDownloadFile();
				filePath = getOriginalFile(exportedFile, latestFile);
			}
			else
				filePath = downloadFile(FileType, latestFile, null);

		return filePath;

	}

	public List<String> getExistedFormsInExportXBRL(String Group, String ProcessDate, String Framework, String Taxonomy, String Module) throws Exception
	{
		List<String> forms;
		String FileType = "XBRL";
		ExportToFilePage exportToFilePage = openExportToFileBtnClick(FileType, null);
		exportToFilePage.setGroupSelector(Group, FileType);
		exportToFilePage.setReferenceDate(ProcessDate, FileType);
		if (Framework != null && Framework.length() > 1)
			exportToFilePage.setFrameworkSelector(Framework, FileType);
		if (Taxonomy != null && Taxonomy.length() > 1)
			exportToFilePage.setTaxonomySelector(Taxonomy, FileType);
		if (Module != null && Module.length() > 1)
			exportToFilePage.setModuleSelector(Module, FileType);
		ExportXBRLPage exportXBRLPage = new ExportXBRLPage(getWebDriverWrapper());

		forms = exportXBRLPage.getForm();
		return forms;

	}

	/**
	 * enter UsersPage
	 *
	 * @return UsersPage
	 * @throws Exception
	 */
	public UsersPage EnterUserPage() throws Exception
	{
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.Admin").click();
		waitStatusDlg();
		element("lp.admin_User").click();
		waitStatusDlg();
		logger.info("Enter user manage form");
		return new UsersPage(getWebDriverWrapper());
	}

	/**
	 * enter UserGroupPage
	 *
	 * @return UserGroupPage
	 * @throws Exception
	 */
	public UserGroupPage EnterUserGroupPage() throws Exception
	{
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.Admin").click();
		waitStatusDlg();
		element("lp.userGroupMag").click();
		waitStatusDlg();
		logger.info("Enter user group manage form");
		return new UserGroupPage(getWebDriverWrapper());
	}

	/**
	 * enter PrivilegeGroupPage
	 *
	 * @return PrivilegeGroupPage
	 * @throws Exception
	 */
	public PrivilegeGroupPage EnterPrivilegeGroupsPage() throws Exception
	{
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.Admin").click();
		waitStatusDlg();
		element("lp.permissionMag").click();
		waitStatusDlg();
		logger.info("Enter permission manage form");
		return new PrivilegeGroupPage(getWebDriverWrapper());
	}

	/**
	 * enter Entity page
	 *
	 * @return EntityPage
	 * @throws Exception
	 */
	public EntityPage EnterEntityPage() throws Exception
	{
		waitThat("lp.settingBtn").toBePresentIn(3000);
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.entity").click();
		waitStatusDlg();
		logger.info("Enter entity manage form");
		return new EntityPage(getWebDriverWrapper());
	}

	/**
	 * enter data warehouse integration page
	 *
	 * @return DWIntegrationPage
	 * @throws Exception
	 */
	public DWIntegrationPage EnterDWIntegrationPage() throws Exception
	{
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.Admin").click();
		waitStatusDlg();
		element("lp.dwIntegration").click();
		waitStatusDlg();
		logger.info("Enter data warehouse integration page");
		return new DWIntegrationPage(getWebDriverWrapper());
	}

	/**
	 * verify if Create new button displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isCreateNewDisplayed() throws Exception
	{
		try
		{
			return element("lp.createNewBtn").isDisplayed();
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
	}

	/**
	 * verify if Compute displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isComputeDisplayed() throws Exception
	{
		try
		{
			return element("lp.compute").isDisplayed();
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
	}

	/**
	 * verify if Import adjustment displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isImportAdjustmentDisplayed() throws Exception
	{
		try
		{
			return element("lp.import").isDisplayed();
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
	}

	/**
	 * verify if Export To RegulatorFormat displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isExportToRFDisplayed() throws Exception
	{
		try
		{
			element("lp.exportToFileBtn").click();
			waitStatusDlg();
			if (element("lp.firstOption").isDisplayed())
				return true;
			else
			{
				return false;
			}
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
	}

	/**
	 * verify user have admin privilege
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isHaveAdminPrivilege() throws Exception
	{
		boolean adminPriv = true;
		element("lp.settingBtn").click();
		waitStatusDlg();
		if (element("lp.Admin").isDisplayed())
		{
			element("lp.Admin").click();
			waitStatusDlg();
			if (!element("lp.admin_User").isDisplayed() && !element("lp.userGroupMag").isDisplayed() && !element("lp.permissionMag").isDisplayed())
				adminPriv = false;
		}
		return adminPriv;
	}

	/**
	 * verify if CreateNew displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isExistFormInCreateNew(String Group, String ProcessDate, String Form) throws Exception
	{
		boolean isExistForm = true;
		FormInstanceCreatePage formInstanceCreatePage = null;
		try
		{
			formInstanceCreatePage = openFormInstanceCreatePage();
			formInstanceCreatePage.setGroup(Group);
			if (ProcessDate == null)
			{
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				ProcessDate = df.format(new Date());
			}

			formInstanceCreatePage.setProcessDate(ProcessDate);
			if (!getFormOptions_Create().contains(Form))
				isExistForm = false;

		}
		catch (Exception e)
		{
			isExistForm = false;
		}
		finally
		{
			formInstanceCreatePage.createCloseClick();
		}

		return isExistForm;
	}

	/**
	 * verify if CreateNew displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	private boolean isExistCreateNew() throws Exception
	{
		return element("lp.createNewBtn").isDisplayed();
	}

	/**
	 * set regulator Entity, Form and ReferenceDate
	 *
	 * @param Regulator
	 * @param Group
	 * @param Form
	 * @param ProcessDate
	 * @throws Exception
	 */
	public void getProductListPage(String Regulator, String Group, String Form, String ProcessDate) throws Exception
	{
		logger.info("Enter list page");

		if (Regulator != null)
			setRegulator(Regulator);

		if (Group != null)
			setGroup(Group);

		if (Form != null)
			setForm(Form);
		else
			setForm("All");

		if (ProcessDate != null)
			setProcessDate(ProcessDate);
		waitStatusDlg();
		waitThat().timeout(3000);
	}

	/**
	 * get title of pop up dialog
	 *
	 * @return title
	 * @throws Exception
	 */
	public String getPopDialogTitle() throws Exception
	{
		return element("lp.popDlg").getInnerText();
	}

	/**
	 * enter compute page
	 *
	 * @return ComputePage
	 * @throws Exception
	 */
	public ComputePage enterComputePage() throws Exception
	{
		logger.info("Open compute return page");
		element("lp.computeBtn").click();
		waitStatusDlg();
		return new ComputePage(getWebDriverWrapper());
	}

	/**
	 * verify if form exist
	 *
	 * @param Form
	 * @param ProcessDate
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isFormExist(String Form, String ProcessDate) throws Exception
	{
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		String list[] =
		{ formCode, version, ProcessDate };
		if (element("lp.formLink", list).isPresent())
			return true;
		else
			return false;
	}

	/**
	 * enter DeleteReturnLogPage
	 *
	 * @return DeleteReturnLogPage
	 * @throws Exception
	 */
	public DeleteReturnLogPage enterDeleteReturnLogPage() throws Exception
	{
		element("lp.deleteReturnLog").click();
		waitStatusDlg();
		return new DeleteReturnLogPage(getWebDriverWrapper());
	}

	/**
	 * enter ShowDeletedReturnPage
	 *
	 * @return ShowDeletedReturnPage
	 * @throws Exception
	 */
	public ShowDeletedReturnPage enterShowDeletedReturnPage() throws Exception
	{
		element("lp.showDeleteReturn").click();
		waitStatusDlg();
		return new ShowDeletedReturnPage(getWebDriverWrapper());

	}

	/**
	 * verify if import adjustment is displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isExistImportAdjsutment() throws Exception
	{
		return element("lp.import").isDisplayed();
	}

	/**
	 * verify if exist clone date
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isExistCopyData() throws Exception
	{
		FormInstanceCreatePage formInstanceCreatePage = openFormInstanceCreatePage();
		boolean rst = formInstanceCreatePage.isCopyDataDisplayed();
		formInstanceCreatePage.closeCreateNew();
		return rst;
	}

	/**
	 * get columns in approve log
	 *
	 * @param formCode
	 * @param version
	 * @param processDate
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getApproveLogColumns(String formCode, String version, String processDate) throws Exception
	{
		logger.info("Get approve log columns");
		List<String> columnsList = new ArrayList<String>();
		int row = getFormInstanceRowPos(formCode, version, processDate) - 1;
		element("lp.approveLink", String.valueOf(row)).click();
		waitStatusDlg();
		columnsList = element("lp.workflowLogHeadColumn").getAllInnerTexts();
		element("lp.closeWorkflowLog").click();
		waitStatusDlg();
		Thread.sleep(2000);
		return columnsList;
	}

	/**
	 * enter job manager page
	 *
	 * @return JobManagerPage
	 * @throws Exception
	 */
	public JobManagerPage enterJobManagerPage() throws Exception
	{
		element("lp.JobManger").click();
		waitStatusDlg();
		Thread.sleep(1500);
		return new JobManagerPage(getWebDriverWrapper());
	}

	/**
	 * enter ReturnSourcePage
	 *
	 * @param formCode
	 * @param version
	 * @param processDate
	 * @return ReturnSourcePage
	 * @throws Exception
	 */
	public ReturnSourcePage enterReturnSourcePage(String formCode, String version, String processDate) throws Exception
	{
		int index = getFormInstanceRowPos(formCode, version, processDate) - 1;
		element("lp.form.update2", String.valueOf(index)).click();
		waitStatusDlg();
		return new ReturnSourcePage(getWebDriverWrapper());
	}

	/**
	 * verify if job message is correct
	 *
	 * @param Form
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isJobResultCorrect(String Form) throws Exception
	{
		boolean rst = true;
		if (element("lp.JobResultForm").isDisplayed())
		{
			if (!element("lp.JobMessage", "1").getInnerText().equals("Job Started!"))
				rst = false;
			if (!element("lp.JobMessage", "2").getInnerText().startsWith("New job for " + Form))
				rst = false;
			if (!element("lp.JobMessage", "3").getInnerText().startsWith("Started:"))
				rst = false;
			if (!element("lp.JobMessage", "4").getInnerText().startsWith("Expected to Finish:"))
				rst = false;
			element("lp.JobResultOK").click();
			waitStatusDlg();
		}
		else
			rst = false;

		return rst;
	}

	/**
	 * get notification number
	 *
	 * @return notification nums
	 * @throws Exception
	 */
	public int getNotificationNums() throws Exception
	{
		String num = element("lp.notificationNums").getInnerText().trim();
		if (num.equalsIgnoreCase("."))
			num = "0";
		else if (num.equalsIgnoreCase(""))
			num = "0";
		return Integer.parseInt(num);
	}

	/**
	 * wait job finished
	 *
	 * @param initNums
	 * @throws Exception
	 */
	public void waitJobCompleted(int initNums) throws Exception
	{
		while (getNotificationNums() - initNums < 1)
		{
			waitThat().timeout(2000);
		}
	}

	/**
	 * if job executed successfully
	 *
	 * @param jobType
	 * @param Form
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isJobSuccessed(String jobType, String Form) throws Exception
	{
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);

		String message = element("lp.firstNotification").getInnerText();
		if (jobType.toLowerCase().equals("retrieve"))
			jobType = "Retrieve";
		else
			jobType = "Export";

		if (message.startsWith(jobType + " job " + formCode + " " + version) && message.endsWith("successfully."))
			return true;
		else
			return false;
	}

	/**
	 * get prefix of current regulator
	 *
	 * @return regulator prefix
	 * @throws Exception
	 */
	public String getSelectRegulatorPrefix() throws Exception
	{
		return element("lp.selectedRegPrefix").getAttribute("value");
	}

	/**
	 * approve return
	 *
	 * @param listPage
	 * @param Regulator
	 * @param Group
	 * @param Form
	 * @param referenceDate
	 * @return HomePage
	 * @throws Exception
	 */
	public HomePage approveReturn(ListPage listPage, String Regulator, String Group, String Form, String referenceDate) throws Exception
	{
		String userList[] =
		{ "admin1", "admin2", "admin3" };
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		for (String username : userList)
		{
			HomePage homePage = listPage.logout();
			homePage.loginAs(username, "password");
			listPage.getProductListPage(Regulator, Group, Form, referenceDate);
			FormInstancePage formInstancePage = listPage.openFormInstance(formCode, version, referenceDate);
			formInstancePage.approveForm("test");
			formInstancePage.closeFormInstance();
		}
		listPage.logout();
		return new HomePage(getWebDriverWrapper());
	}

	/**
	 * close job dialog
	 *
	 * @throws Exception
	 */
	public void closeJobDialog() throws Exception
	{
		try
		{
			if (element("lp.JobResultOK").isDisplayed())
				element("lp.JobResultOK").click();
		}
		catch (Exception e)
		{
			waitStatusDlg();
			element("lp.JobResultOK").click();
		}
		waitStatusDlg();
		clickDashboard();
		Thread.sleep(500);
	}

	/**
	 * Navigate to the Setup network file location page.
	 *
	 * @return PhysicalLocationPage
	 * @throws Exception
	 */
	public PhysicalLocationPage enterPhysicalLoaction() throws Exception
	{
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.adminMenu").click();
		waitStatusDlg();
		element("lp.physicalLocation").click();
		waitStatusDlg();
		return new PhysicalLocationPage(getWebDriverWrapper());
	}

	/**
	 * click dashboard
	 *
	 * @throws Exception
	 */
	public void clickDashboard() throws Exception
	{
		waitStatusDlg();
		Thread.sleep(3000);
		element("lp.dashboard").click();
		waitStatusDlg();
		Thread.sleep(3000);
	}

	/**
	 * enter preferencePage
	 *
	 * @return PreferencePage
	 * @throws Exception
	 */
	public PreferencePage enterPreferencePage() throws Exception
	{
		element("lp.uMenu").click();
		waitStatusDlg();
		element("lp.Preferences").click();
		waitStatusDlg();
		return new PreferencePage(getWebDriverWrapper());
	}

	/**
	 * get column name in forn list
	 *
	 * @param columnIndex
	 * @return column name
	 * @throws Exception
	 */
	public String getColumnNameInFormList(int columnIndex) throws Exception
	{
		return element("lp.list.columnName", String.valueOf(columnIndex)).getInnerText();
	}

	/**
	 * if work flow status column is displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isWorkflowStatusDisplayed() throws Exception
	{
		boolean rst = true;
		if (element("lp.list.columnName", "13").isDisplayed())
		{
			if (!element("lp.list.columnName", "13").getInnerText().equalsIgnoreCase("Workflow Status"))
				rst = false;

			for (int i = 1; i <= 5; i++)
			{
				if (!element("lp.list.workflowStatus", String.valueOf(i)).isDisplayed())
					rst = false;
			}
		}
		else
		{
			rst = false;
		}
		return rst;
	}

	/**
	 * if job status column is displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isJobStatusDisplayed() throws Exception
	{
		boolean rst = true;
		if (element("lp.list.columnName", "12").isDisplayed())
		{
			if (!element("lp.list.columnName", "12").getInnerText().equalsIgnoreCase("Job Status"))
				rst = false;
		}
		else
		{
			rst = false;
		}
		return rst;
	}

	/**
	 * get approve count
	 *
	 * @param rowIndex
	 * @return ApproveCount
	 * @throws Exception
	 */
	public String getApproveCount(int rowIndex) throws Exception
	{
		String[] list =
		{ String.valueOf(rowIndex), "14" };
		return element("lp.cell.text1", list).getInnerText();
	}

	/**
	 * enter MessageCenter
	 *
	 * @return MessageCenter
	 * @throws Exception
	 */
	public MessageCenter enterMessageCenterPage() throws Exception
	{
		element("lp.notificationCenter").click();
		waitStatusDlg();
		return new MessageCenter(getWebDriverWrapper());
	}

	public boolean isDuplicatedJob() throws Exception
	{
		if (element("lp.jobRst.label").getInnerText().equalsIgnoreCase("ERROR!") && element("lp.jobRst.label2").getInnerText().equalsIgnoreCase("Job already running!"))
			return true;
		else
		{
			return false;
		}
	}

	public JobDetailsPage enterJobDetailsPageFromJobDialog() throws Exception
	{
		element("lp.jobRst.jobManager").click();
		waitStatusDlg();
		return new JobDetailsPage(getWebDriverWrapper());
	}

	/**
	 * Enter FormVariablePage
	 */
	public FormVariablePage openFormVariablePage() throws Exception
	{
		element("lp.settingBtn").click();
		waitStatusDlg();
		element("lp.formVariableBtn").click();
		waitStatusDlg();
		return new FormVariablePage(getWebDriverWrapper());
	}

	/**
	 * Filter the export field.
	 */
	public void filterTheExportField(String entityCode, String referenceDate, String fileType, String framework, String taxonomy, String module) throws Exception
	{
		ExportToFilePage exportToFilePage = new ExportToFilePage(getWebDriverWrapper());
		exportToFilePage.setGroupSelector(entityCode, fileType);
		exportToFilePage.setReferenceDate(referenceDate, fileType);
		exportToFilePage.setFrameworkSelector(framework, fileType);
		exportToFilePage.setTaxonomySelector(taxonomy, fileType);
		exportToFilePage.setModuleSelector(module, fileType);
	}

	/**
	 * Get the form name on the export XBRL page
	 */
	public List<String> getFormNameOnExportXBRLPage() throws Exception
	{
		ExportXBRLPage exportXBRLPage = new ExportXBRLPage(getWebDriverWrapper());
		return exportXBRLPage.getForm();
	}

	/**
	 * Close the ecport page
	 */
	public void closeExportPage(String fileType) throws Exception
	{
		ExportToFilePage exportToFilePage = new ExportToFilePage(getWebDriverWrapper());
		exportToFilePage.closeExportPage(fileType);
	}

	/**
	 * Get the export menu
	 */
	public List<String> getExportRFMenuText() throws Exception
	{
		element("lp.exportToFileBtn").click();
		waitStatusDlg();
		return element("lp.firstOption").getAllInnerTexts();
	}

	/**
	 * verify if current page is listPage
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isListPage() throws Exception
	{
		if (element("lp.formListTab").isDisplayed())
			return true;
		else
			return false;
	}

	/**
	 * close delete form dialog
	 *
	 * @throws Exception
	 */
	public void closeDeleteFormDlg() throws Exception
	{
		element("lp.closeDeleteDlg").click();
		waitStatusDlg();
	}

	/**
	 * if export to vanilla option is displayed
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isExportToVanillaDisplayed() throws Exception
	{
		element("lp.exportToFileBtn").click();
		waitStatusDlg();
		String optionName = "Export To Vanilla";
		boolean rst = element("lp.exportToFileOption", optionName).isDisplayed();
		element("lp.exportToFileBtn").click();
		waitStatusDlg();
		return rst;
	}

	/**
	 * get all options in Export to regulator
	 *
	 * @return all options
	 * @throws Exception
	 */
	public List<String> getExportToRegOptions() throws Exception
	{
		List<String> options = new ArrayList<>();
		element("lp.exportToFileBtn").click();
		waitStatusDlg();
		int nums = element("lp.exportRegOptions").getNumberOfMatches();
		for (int i = 1; i <= nums; i++)
		{
			options.add(element("lp.exportRegOption", String.valueOf(i)).getInnerText());
		}
		return options;
	}

	public AboutPage enterAboutPage() throws Exception
	{
		element("lp.help").click();
		waitStatusDlg();
		element("lp.about").click();
		waitStatusDlg();
		return new AboutPage(getWebDriverWrapper());
	}
}
