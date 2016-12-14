package com.lombardrisk.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;
import org.yiwan.webcore.web.IWebDriverWrapper.IWebElementWrapper;

public class FormInstancePage extends AbstractPage
{

	public FormInstancePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * Close formInstancePage
	 *
	 * @return ListPage
	 * @throws Exception
	 */
	public ListPage closeFormInstance() throws Exception
	{
		if (element("fp.form").isDisplayed())
		{
			if (element("fp.message").isDisplayed())
				waitThat("fp.message").toBeInvisible();
			if (element("fp.importDlgmodal").isDisplayed())
				waitThat("fp.importDlgmodal").toBeInvisible();

			if (element("fp.close").isDisplayed())
			{
				logger.info("Close form");
				Thread.sleep(2000);
				element("fp.close").click();
				waitThat("fp.close").toBeInvisible();
			}
		}

		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * Enter AdjustLogPage
	 *
	 * @return AdjustLogPage
	 * @throws Exception
	 */
	public AdjustLogPage getDrillDown() throws Exception
	{
		if (!element("fp.exportAdjustment").isDisplayed())
		{
			element("fp.adjustment").click();
			waitStatusDlg();
			element("fp.adjLog").click();
			waitStatusDlg();
		}
		return new AdjustLogPage(getWebDriverWrapper());
	}

	/**
	 * click cell
	 *
	 * @param cellId
	 * @throws Exception
	 */
	private void cellClick(String cellId) throws Exception
	{
		element("fp.inputCell", cellId).click();
		waitThat("fp.comment").toBeVisible();
		waitStatusDlg();
	}

	/**
	 * click cell
	 *
	 * @param Regulator
	 * @param form
	 * @param version
	 * @param instance
	 * @param cellName
	 * @param extendCell
	 * @throws Exception
	 */
	public void cellClick(String Regulator, String form, String version, String instance, String cellName, String extendCell) throws Exception
	{
		getPageNameByCell(Regulator, form, version, instance, cellName, extendCell);
		cellClick(cellName);
	}

	/**
	 * get cell real value in current page
	 *
	 * @param cellId
	 * @return real value
	 * @throws Exception
	 */
	public String getCellText(String cellId) throws Exception
	{
		String cellValue = null;
		boolean flag = true;
		while (flag)
		{
			if (element("fp.inputCell", cellId).isDisplayed())
			{
				if ("checkbox".equals(element("fp.inputCell", cellId).getAttribute("type")))
				{
					String part1 = element("fp.inputCell", cellId).getAttribute("onfocus").replace("onCellFocus", "");
					String part2 = part1.substring(1, part1.length() - 2).replace(" ", "").replace("\'", "").replace(",", "~");
					String[] rsts = part2.split("~");
					if ("checked".equals(rsts[2]))
					{
						cellValue = "1";
					}
					else if ("unchecked".equals(rsts[2]))
					{
						cellValue = "0";
					}
				}
				else
				{
					cellValue = element("fp.inputCell", cellId).getAttribute("value");
					String attr = element("fp.inputCell", cellId).getAttribute("onfocus").replace("hideScale();onCellFocus(", "").replace(");", "").replace("'", "");
					if (!"C".equalsIgnoreCase(attr.split(",")[3]))
						cellValue = trimRight(cellValue);
				}

				flag = false;
			}
			else
			{
				if (element("fp.nextPageSta").isDisplayed() && !"-1".equals(element("fp.nextPageSta").getAttribute("tabindex")))
				{
					element("fp.nextPage").click();
					waitStatusDlg();
					Thread.sleep(500);
				}
				else
				{
					flag = false;
				}
			}
		}
		return cellValue;
	}

	/**
	 * get cell text when edit
	 *
	 * @param cellId
	 * @return
	 * @throws Exception
	 */
	public String getCellTextInEdit(String cellId) throws Exception
	{
		element("fp.inputCell", cellId).click();
		waitStatusDlg();
		String text = element("fp.inputCell", cellId).getAttribute("onfocus").replace("',", "#").split("#")[2].substring(1);
		cellEditCancelBtnClick();
		return text;
	}

	/**
	 * get cell real value in current page
	 *
	 * @param startCell
	 * @param cellId
	 * @return
	 * @throws Exception
	 */
	public String getCellText(String startCell, String cellId) throws Exception
	{
		String cellValue = null;
		boolean flag = true;
		while (flag)
		{
			if (element("fp.inputCell", cellId).isDisplayed())
			{
				if ("checkbox".equals(element("fp.inputCell", cellId).getAttribute("type")))
				{
					String part1 = element("fp.inputCell", cellId).getAttribute("onfocus").replace("onCellFocus", "");
					String part2 = part1.substring(1, part1.length() - 2).replace(" ", "").replace("\'", "").replace(",", "~");
					String[] rsts = part2.split("~");
					if ("checked".equals(rsts[2]))
					{
						cellValue = "1";
					}
					else if ("unchecked".equals(rsts[2]))
					{
						cellValue = "0";
					}
				}
				else
				{
					cellValue = element("fp.inputCell", cellId).getAttribute("value");
					String attr = element("fp.inputCell", cellId).getAttribute("onfocus").replace("hideScale();onCellFocus(", "").replace(");", "").replace("'", "");
					if (!"C".equalsIgnoreCase(attr.split(",")[3]))
						cellValue = cellValue.trim();
				}

				flag = false;
			}
			else
			{
				if (element("fp.nextPageSta2", startCell).isDisplayed() && !"-1".equals(element("fp.nextPageSta2", startCell).getAttribute("tabindex")))
				{
					element("fp.nextPage2", startCell).click();
					waitStatusDlg();
					Thread.sleep(500);
				}
				else
					flag = false;
			}
		}
		return cellValue;
	}

	/**
	 * get cell value
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param instance
	 * @param cellId
	 * @param extendCell
	 * @return cell value
	 * @throws Exception
	 */
	public String getCellText(String Regulator, String formCode, String version, String instance, String cellId, String extendCell) throws Exception
	{
		String cellName;
		if (!element("fp.inputCell", cellId).isDisplayed() && !element("fp.inputCell", extendCell).isDisplayed())
		{
			String cellPage = getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);
			logger.info("Cell[" + cellId + "] in page[" + cellPage + "]");
		}
		if (extendCell != null)
			cellName = extendCell;
		else
			cellName = cellId;
		return getCellText(cellName);
	}

	/**
	 * double click cell
	 *
	 * @param cellId
	 * @return AllocationPage
	 * @throws Exception
	 */
	public AllocationPage cellDoubleClick(String cellId) throws Exception
	{
		element("fp.inputCell", cellId).doubleClick();
		waitStatusDlg();
		return new AllocationPage(getWebDriverWrapper());
	}

	/**
	 * verify if allocation displayed
	 *
	 * @param cellId
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isAllocationDisplayed(String cellId) throws Exception
	{
		element("fp.inputCell", cellId).doubleClick();
		waitStatusDlg();
		return element("fp.allocation").isDisplayed();
	}

	/**
	 * double click cell
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param instance
	 * @param cellId
	 * @param extendCell
	 * @return AllocationPage
	 * @throws Exception
	 */
	public AllocationPage cellDoubleClick(String Regulator, String formCode, String version, String instance, String cellId, String extendCell) throws Exception
	{
		getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);
		selectInstance(instance);
		if (extendCell != null)
			element("fp.inputCell", extendCell).doubleClick();
		else
			element("fp.inputCell", cellId).doubleClick();
		waitStatusDlg();
		Thread.sleep(1000);
		return new AllocationPage(getWebDriverWrapper());
	}

	/**
	 * edit cell that is checkbox
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param instance
	 * @param cellId
	 * @param extendCell
	 * @param tick
	 * @throws Exception
	 */
	public void editCellValue(String Regulator, String formCode, String version, String instance, String cellId, String extendCell, boolean tick) throws Exception
	{
		if (tick)
		{
			if (!"1".equals(getCellText(Regulator, formCode, version, instance, cellId, extendCell)))
				element("fp.inputCell", cellId).click();
		}
		waitStatusDlg();
		cellEditOkBtnClick();
	}

	/**
	 * edit cell
	 *
	 * @param cellId
	 * @param text
	 * @throws Exception
	 */
	private void editCell(String cellId, String text) throws Exception
	{
		if (!element("fp.inputCell", cellId).isDisplayed())
		{
			String js = "document.getElementById('" + cellId + "').scrollIntoViewIfNeeded();";
			executeScript(js);
			Thread.sleep(300);
			moveOverToCell(cellId);
		}
		cellClick(cellId);
		String id = null;
		String[] ids = new String[]
		{ "numericCellEditor", "longCellEditor", "dateCellEditor", "stringCellEditor", "memoCellEditor", "selectCellEditor" };
		for (String idName : ids)
		{
			if (!element("fp.cellID", idName).getAttribute("style").contains("display: none"))
			{
				if (idName.equals("dateCellEditor"))
					id = "dateCellEditor_input";
				else
					id = idName;
				break;
			}
		}

		if (id != null)
		{
			logger.info("id of input is:" + id);
			if (!"selectCellEditor".equalsIgnoreCase(id))
			{
				if ("dateCellEditor_input".equalsIgnoreCase(id))
				{
					element("fp.cellID", id).click();
					waitThat().timeout(300);
				}
				if (text == null)
					element("fp.cellID", id).clear();
				else
				{
					element("fp.cellID", id).input(text);
					if ("dateCellEditor_input".equalsIgnoreCase(id))
						element("fp.currentDay").click();
				}
			}
			else
			{
				id = "selectCellEditor_input";
				if (text == null)
				{
					element("fp.dropdown.button").click();
					waitStatusDlg();
					element("fp.dropdown.firstOption").click();
				}
				else
				{
					element("fp.cellID", id).input(text);
					waitStatusDlg();
					// element("fp.dropdown.firstOption").click();
				}
				waitStatusDlg();
			}
		}
	}

	/**
	 * input text in dropdown cell
	 *
	 * @param cellId
	 * @param text
	 * @throws Exception
	 */
	private void inputInDropDownCell(String cellId, String text) throws Exception
	{
		cellClick(cellId);
		element("fp.cellID", "selectCellEditor_input").input(text);
		waitStatusDlg();
	}

	/**
	 * edit cell
	 *
	 * @param cellId
	 * @param text
	 * @param comment
	 * @throws Exception
	 */
	private void editCellValue(String cellId, String text, String comment) throws Exception
	{
		editCell(cellId, text);
		if (comment != null && !"".equals(comment))
			element("fp.comment").input(comment);
		cellEditOkBtnClick();
	}

	/**
	 * edit cell
	 *
	 * @param cellId
	 * @param text
	 * @throws Exception
	 */
	public void editCellValue(String cellId, String text) throws Exception
	{
		waitStatusDlg();
		editCell(cellId, text);
		waitStatusDlg();
		cellEditOkBtnClick();
	}

	/**
	 * edit cell
	 *
	 * @param instance
	 * @param cellId
	 * @param extendCell
	 * @param text
	 * @throws Exception
	 */
	public void editCellValue(String instance, String cellId, String extendCell, String text) throws Exception
	{
		logger.info("Begin edit cell[" + cellId + "]=" + text + "");
		String Form = getFormInfo();
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		String Regulator = element("fp.regulator").getInnerText().split("/")[0];
		Regulator = Regulator.substring(0, Regulator.length() - 1);
		String pageName = getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);

		if (pageName != null)
		{
			if (extendCell != null)
				cellId = extendCell;

			editCellValue(cellId, text);
		}

	}

	/**
	 * edit cell
	 *
	 * @param instance
	 * @param cellId
	 * @param extendCell
	 * @param text
	 * @param comment
	 * @throws Exception
	 */
	public void editCellValue(String instance, String cellId, String extendCell, String text, String comment) throws Exception
	{
		logger.info("Begin edit cell[" + cellId + "]=" + text + "");
		String Form = getFormInfo();
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		String Regulator = element("fp.regulator").getInnerText().split("/")[0];
		Regulator = Regulator.substring(0, Regulator.length() - 1);
		String pageName = getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);

		if (pageName != null)
		{
			if (extendCell != null)
				cellId = extendCell;

			editCellValue(cellId, text, comment);
		}

	}

	/**
	 * get error message when edit cell
	 *
	 * @param instance
	 * @param cellId
	 * @param extendCell
	 * @param text
	 * @param comment
	 * @return message
	 * @throws Exception
	 */
	public String editCellValueGetErrorMsg(String instance, String cellId, String extendCell, String text, String comment) throws Exception
	{
		logger.info("Begin edit cell[" + cellId + "]=" + text + "");
		String Form = getFormInfo();
		String formCode = splitReturn(Form).get(0);
		String version = splitReturn(Form).get(1);
		String Regulator = element("fp.regulator").getInnerText().split("/")[0];
		Regulator = Regulator.substring(0, Regulator.length() - 1);
		String pageName = getPageNameByCell(Regulator, formCode, version, instance, cellId, extendCell);

		if (pageName != null)
		{
			if (extendCell != null)
				cellId = extendCell;

			editCellValue(cellId, text, comment);
		}

		return element("fp.message").getInnerText();

	}

	/**
	 * verify if cell is editable
	 *
	 * @param cellId
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isCellEditable(String cellId) throws Exception
	{
		element("fp.inputCell", cellId).click();
		waitThat().timeout(300);
		return element("fp.cellEditorContainer").isDisplayed();
	}

	/**
	 * save edit
	 *
	 * @throws Exception
	 */
	private void cellEditOkBtnClick() throws Exception
	{
		element("fp.cellEditSave").click();
		waitStatusDlg();
	}

	/**
	 * cancel edit
	 *
	 * @throws Exception
	 */
	private void cellEditCancelBtnClick() throws Exception
	{
		element("fp.cellEditCancel").click();
	}

	/**
	 * get cell attribute
	 *
	 * @param cellID
	 * @param attribute
	 * @return attribute
	 * @throws Exception
	 */
	private String getCellAttribute(String cellID, String attribute) throws Exception
	{
		return element("fp.cellID", cellID).getAttribute(attribute);
	}

	/**
	 * if cell value displayed as NULL
	 *
	 * @param cellId
	 * @return
	 * @throws Exception
	 */
	public boolean isShowNull(String cellId) throws Exception
	{
		return "NULL".equals(getCellAttribute(cellId, "value"));
	}

	private void moveOverToCell(String cellId) throws Exception
	{

		element("fp.cellID", cellId).moveTo();
		waitThat().timeout(1000);
	}

	private void addRowClick(String cellId) throws Exception
	{
		moveOverToCell(cellId);
		if (!element("fp.addRowIcon").isDisplayed())
		{
			String id = element("fp.addRowIcon2").getAttribute("id");
			String js = "document.getElementById('" + id + "').scrollIntoViewIfNeeded();";
			executeScript(js);
			Thread.sleep(300);
			moveOverToCell(cellId);
		}
		element("fp.addRowIcon").click();
		waitStatusDlg();
	}

	public boolean isAddRowShow() throws Exception
	{
		return element("fp.addRowMenu").isDisplayed();
	}

	private void addRowMenuClick(int index) throws Exception
	{
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

	private void deleteRowConfirmClick() throws Exception
	{
		element("fp.delRowConfirm").click();
		waitStatusDlg();
	}

	public boolean isExistInsertAbove_Below(String cellId) throws Exception
	{
		addRowClick(cellId);
		if (element("fp.inRowAbo").isPresent() && element("fp.inRowBel").isPresent())
			return true;
		else
			return false;
	}

	public ArrayList<String> getPageNames() throws Exception
	{
		ArrayList<String> pageNames = new ArrayList<String>();
		int amt = (int) element("fp.pageTab").getRowCount();
		for (int i = 1; i <= amt; i++)
		{
			pageNames.add(element("fp.pageName", String.valueOf(i)).getInnerText());
		}

		return pageNames;
	}

	public String getCurrentPage() throws Exception
	{
		String pageName = null;
		int amt = (int) element("fp.pageTab").getRowCount();
		for (int i = 1; i <= amt; i++)
		{
			if ("true".equals(element("fp.pageState", String.valueOf(i)).getAttribute("aria-selected")))
			{
				pageName = element("fp.pageName", String.valueOf(i)).getInnerText();
				break;
			}
		}

		return pageName;
	}

	public ExportXBRLPage getExportToXBRLPage() throws Exception
	{
		element("fp.expToFile").click();
		waitStatusDlg();
		element("fp.exportToXBRL").click();
		waitStatusDlg();
		return new ExportXBRLPage(getWebDriverWrapper());
	}

	public void liveValidationClick() throws Exception
	{
		if (!element("fp.liveVal").getAttribute("class").contains("liveValidationBtnOutsetClass"))
			element("fp.liveVal").click();
		waitStatusDlg();
	}

	public boolean isLiveVlidationHilight() throws Exception
	{
		if (element("fp.liveVal").getAttribute("class").contains("liveValidationBtnOutsetClass"))
			return true;
		else
			return false;
	}

	public boolean isValidationNowEnable() throws Exception
	{
		if (element("fp.valNowBtn").isDisplayed())
		{
			if ("true".equals(element("fp.valNowBtn").getAttribute("aria-disabled")))
				return false;
			else
				return true;
		}
		else
			return false;
	}

	public boolean isLiveValidationEnabled() throws Exception
	{
		if (element("fp.liveVal").isDisplayed())
		{
			if ("true".equals(element("fp.liveVal").getAttribute("aria-disabled")))
				return false;
			else
				return true;
		}
		else
			return false;
	}

	public void validationNowClick() throws Exception
	{
		logger.info("Click validate now button");
		element("fp.valNowBtn").click();
		waitStatusDlg();
	}

	public boolean isImportAdjustmentEnabled() throws Exception
	{
		boolean visible = true;
		if (element("fp.adjustment").isDisplayed())
		{
			element("fp.adjustment").click();
			waitStatusDlg();
			if (element("fp.import").isDisplayed())
			{
				if (element("fp.import").getAttribute("class").contains("ui-state-disabled"))
					visible = false;
				else if (!element("fp.import").isDisplayed())
					visible = false;
			}
			else
			{
				visible = false;
			}

		}
		else
			visible = false;
		element("fp.adjustment").click();
		waitStatusDlg();
		return visible;

	}

	public boolean isViewAdjustmentLogDisplayed() throws Exception
	{
		boolean visible = true;
		if (element("fp.adjustment").isDisplayed())
		{
			element("fp.adjustment").click();
			if (!element("fp.adjLog").isDisplayed())
				visible = false;
		}
		else
			visible = false;

		return visible;

	}

	public boolean isLockDisplayed() throws Exception
	{
		try
		{
			return element("fp.lock").isDisplayed();
		}
		catch (NoSuchElementException e)
		{
			logger.warn(e.getMessage());
			//e.printStackTrace();
			return false;
		}
	}

	/**
	 * if Export To File button is enabled
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isExportToFileEnabled() throws Exception
	{
		element("fp.expToFile").click();
		waitStatusDlg();
		element("fp.expToFile").click();
		if (element("fp.expToXls").isDisplayed() && element("fp.expToCSV").isDisplayed())
			return true;
		else
			return false;
	}

	/**
	 * if add instance button is enabled
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isAddInstanceBtnDisplayed() throws Exception
	{
		try
		{
			return element("fp.addInst").isDisplayed();
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
	}

	/**
	 * if delete instance button is enabled
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isDeleteInstanceBtnDisplayed() throws Exception
	{
		try
		{
			return element("fp.delInst").isDisplayed();
		}
		catch (NoSuchElementException e)
		{
			logger.warn(e.getMessage());
			//e.printStackTrace();
			return false;
		}
	}

	/**
	 * if Approve and reject options are displayed
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isApproveRejectDisplayed() throws Exception
	{
		boolean visible = false;
		try
		{
			element("fp.wfBtn").click();
			try
			{
				if (element("fp.readyAppr").isDisplayed())
				{
					element("fp.readyAppr").click();
					waitStatusDlg();
				}
			}
			catch (NoSuchElementException e)
			{
				logger.warn(e.getMessage());
			}

			if (element("fp.approve").isDisplayed() && element("fp.reject").isDisplayed())
				visible = true;
		}
		catch (NoSuchElementException e)
		{
			logger.warn(e.getMessage());
			//e.printStackTrace();
			visible = false;
		}
		return visible;
	}

	/**
	 * if readApprove option is displayed
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isReadyApproveDisplayed() throws Exception
	{
		element("fp.wfBtn").click();
		waitThat().timeout(300);
		if (element("fp.readyAppr").isDisplayed())
			return true;
		else
			return false;
	}

	/**
	 * click lock button
	 *
	 * @throws Exception
	 */
	public void lockClick() throws Exception
	{
		try
		{
			logger.info("Lock form");
			element("fp.lock").click();
		}
		catch (NoSuchElementException e)
		{
			try
			{
				throw new Exception("The FormInstance has been locked already!");
			}
			catch (Exception e1)
			{
				logger.warn(e1.getMessage());
				e1.printStackTrace();
			}
		}
		waitThat("fp.message").toBeVisible();
		waitThat("fp.message").toBeInvisible();
		waitStatusDlg();
	}

	/**
	 * click unlock button
	 *
	 * @throws Exception
	 */
	public void unlockClick() throws Exception
	{
		if (element("fp.unLock").isDisplayed())
		{
			logger.info("Unlock form");
			element("fp.unLock").click();
			waitStatusDlg();
		}
		else
			logger.info("The FormInstance has been unlocked already!");

	}

	/**
	 * click add instance button
	 *
	 * @throws Exception
	 */
	private void addPageInstanceClick() throws Exception
	{
		element("fp.addInst").click();
		waitStatusDlg();
	}

	/*
	 * private void addPageInstanceConfirmBtnClick() throws Exception {
	 * element("fp.addInstConf").click(); waitStatusDlg(); }
	 *
	 * private void deletePageInstanceConfirmBtnClick() throws Exception {
	 * element("fp.delInstConf").click(); waitStatusDlg(); }
	 */

	/**
	 * input or select instance name
	 *
	 * @param instanceName
	 * @throws Exception
	 */
	private void inputInstanceName(String instanceName) throws Exception
	{
		if (!element("fp.select.addInstance").isDisplayed())
		{
			logger.info("Inputed instance is : " + instanceName);
			element("fp.instName").type(instanceName);
			element("fp.addInstAcc").click();
		}
		else
		{
			logger.info("Selected instance is : " + instanceName);
			element("fp.select.addInstance").selectByVisibleText(instanceName);
			waitThat().timeout(600);
			element("fp.accept.addInstance").click();
		}
		waitStatusDlg();
		waitThat("fp.messageTitle").toBeInvisible();
	}

	/**
	 * insert row
	 *
	 * @param cellId
	 * @throws Exception
	 */
	public void insertRow(String cellId) throws Exception
	{
		addRowClick(cellId);
		element("fp.insertRow").click();
		waitStatusDlg();
	}

	/**
	 * insert row above
	 *
	 * @param cellId
	 * @throws Exception
	 */
	public void insertRowAbove(String cellId) throws Exception
	{
		addRowClick(cellId);
		addRowMenuClick(1);
		waitStatusDlg();
	}

	/**
	 * insert row below
	 *
	 * @param cellId
	 * @throws Exception
	 */
	public void insertRowBelow(String cellId) throws Exception
	{
		addRowClick(cellId);
		addRowMenuClick(2);
		waitStatusDlg();
	}

	/**
	 * delete row
	 *
	 * @param cellId
	 * @throws Exception
	 */
	public void deleteRow(String cellId) throws Exception
	{
		addRowClick(cellId);
		addRowMenuClick(3);
		deleteRowConfirmClick();
		waitStatusDlg();
		waitThat("fp.pop.message").toBeInvisible();
	}

	/**
	 * delete instance
	 *
	 * @param instance
	 * @throws Exception
	 */
	public void deletePageInstance(String instance) throws Exception
	{
		selectInstance(instance);
		element("fp.delInst").click();
		waitStatusDlg();
		element("fp.delInstConf").click();
		waitStatusDlg();
		waitThat("fp.pop.message").toBeInvisible();
	}

	/**
	 * verify if adjustment log is correct
	 *
	 * @param cellId
	 * @param fromValue
	 * @param toValue
	 * @return
	 * @throws Exception
	 */
	public boolean checkAdjustmentLog(String cellId, String fromValue, String toValue) throws Exception
	{
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
		if (adjustLogPage.getCellName(1).trim().equals(cellId))
		{
			c1 = true;
		}

		logger.info("Check if cell value=" + fromValue);
		if (adjustLogPage.getValue(1).trim().equals(fromValue))
		{
			c2 = true;
		}

		logger.info("Check if cell value=" + toValue);
		if (adjustLogPage.getModifiedTo(1).trim().equals(toValue))
		{
			c3 = true;
		}

		if (c1 & c2 & c3)
			rst = true;
		return rst;
	}

	/**
	 * enter import adjustment page
	 *
	 * @return ListImportFilePage
	 * @throws Exception
	 */
	public ListImportFilePage getImportPageInReturn() throws Exception
	{
		element("fp.adjustment").click();
		waitStatusDlg();
		element("fp.import").click();
		waitThat("fp.importDlg").toBeVisible();
		return new ListImportFilePage(getWebDriverWrapper());

	}

	/**
	 * verify form is locked
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isLockedInReturn() throws Exception
	{
		if (element("fp.unLock").isDisplayed())
			return true;
		else
			return false;
	}

	/**
	 * verify if lock/unlock button is disabled
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isLockUnlockDisabled() throws Exception
	{
		if (element("fp.lock").getAttribute("class").contains("ui-state-disabled"))
			return true;
		else
			return false;
	}

	/**
	 * select instance
	 *
	 * @param instance
	 * @throws Exception
	 */
	public void selectInstance(String instance) throws Exception
	{
		waitStatusDlg();
		if (instance != null)
		{
			if (!getCurrentPageInstance().equalsIgnoreCase(instance))
			{
				logger.info("Select instance " + instance);
				element("fp.curInst").click();
				waitStatusDlg();
				element("fp.selectInstace", instance).click();
				waitStatusDlg();
			}
		}

	}

	/**
	 * get current instance
	 *
	 * @return instance
	 * @throws Exception
	 */
	public String getCurrentPageInstance() throws Exception
	{
		return element("fp.curInst").getInnerText();

	}

	public int getTableRowAmt(String by) throws Exception
	{
		return (int) element(by).getRowCount();
	}

	/**
	 * get extend row amount
	 *
	 * @return row amount
	 * @throws Exception
	 */
	public int getExtGDRowAmt() throws Exception
	{
		int RowAmtF = getTableRowAmt("fp.extGridTab");
		if (element("fp.lastPageSta").getAttribute("tabindex").equals("0"))
		{
			int PageAmt = 0;
			try
			{
				PageAmt = Integer.parseInt(element("fp.lastPageNO").getInnerText());
			}
			catch (Exception e)
			{
				PageAmt = Integer.parseInt(element("fp.lastPageNO2").getInnerText());
			}
			int RowAmtL = getTableRowAmt("fp.extGridTab");
			return RowAmtF * (PageAmt - 1) + RowAmtL;
		}
		else
			return RowAmtF;
	}

	/**
	 * select page
	 *
	 * @param page
	 * @throws Exception
	 */
	public void selectPage(String page) throws Exception
	{
		if (!element("fp.pageLocator2", page).getAttribute("class").contains("ui-state-highlight"))
		{
			waitStatusDlg();
			logger.info("Click page :" + page);
			element("fp.pageLocator", page).click();
			waitStatusDlg();
		}
	}

	/**
	 * verify if adjustment log is correct after edit cell
	 *
	 * @param Regulator
	 * @param Group
	 * @param ProcessDate
	 * @param formCode
	 * @param version
	 * @param cellName
	 * @param extendCell
	 * @return true or false
	 * @throws Exception
	 */
	public boolean editCell_Checklog(String Regulator, String Group, String ProcessDate, String formCode, String version, String cellName, String extendCell) throws Exception
	{
		String beforeEdit = getCellText(Regulator, formCode, version, "", cellName, extendCell);

		String editValue = null;
		String editValue_p1 = beforeEdit.substring(0, beforeEdit.length() - 1);
		String editValue_p2 = beforeEdit.substring(beforeEdit.length() - 1);
		if (beforeEdit.contains("."))
		{
			editValue_p1 = beforeEdit.split(".")[0];
			editValue_p2 = editValue_p1.substring(editValue_p1.length() - 1);
		}
		try
		{
			if (Integer.parseInt(editValue_p2) >= 1 && Integer.parseInt(editValue_p2) < 9)
				editValue_p2 = (String.valueOf((Integer.parseInt(editValue_p2) + 1)));
			else if (Integer.parseInt(editValue_p2) > 9)
				editValue_p2 = (String.valueOf((Integer.parseInt(editValue_p2) - 1)));

			editValue = editValue_p1 + editValue_p2;
		}
		catch (Exception e)
		{
			editValue = beforeEdit + "1";
		}

		logger.info("Edit cell=" + cellName + " value=" + editValue);
		logger.info("Verify if " + cellName + "=" + editValue);
		editCellValue(null, cellName, extendCell, editValue);

		String modifiedValue = getCellText(Regulator, formCode, version, null, cellName, extendCell);

		if (modifiedValue.equals(editValue))
		{
			logger.info("Begin check adjustment log ");
			if (checkAdjustmentLog(cellName, beforeEdit, editValue))
				return true;
			else
				return false;
		}
		else
			return false;

	}

	/**
	 * open edition manager page
	 *
	 * @return EditionManagePage
	 * @throws Exception
	 */
	public EditionManagePage openEditionManage() throws Exception
	{
		logger.info("Open edition manage in form page");
		element("fp.editionIcon").click();
		waitStatusDlg();
		return new EditionManagePage(getWebDriverWrapper());
	}

	/**
	 * select edition from dropdown list
	 *
	 * @param editionInfo
	 * @throws Exception
	 */
	public void selectEditionFomDropdownList(String editionInfo) throws Exception
	{
		int index = 1;
		boolean flag = true;
		element("fp.curEdition").click();
		waitStatusDlg();
		while (flag)
		{
			if (element("fp.edition", String.valueOf(index)).getInnerText().equalsIgnoreCase(editionInfo))
			{
				element("fp.edition", String.valueOf(index)).click();
				waitStatusDlg();
				flag = false;
			}
			else
			{
				index++;
			}

		}
	}

	/**
	 * verify if edition exist
	 *
	 * @param editionInfo
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isEditionExist(String editionInfo) throws Exception
	{
		element("fp.curEdition").click();
		waitStatusDlg();
		boolean rst = element("fp.edition", editionInfo).isPresent();
		element("fp.curEdition").click();
		return rst;

	}

	/**
	 * get current edition
	 *
	 * @return
	 * @throws Exception
	 */
	public String getCurrentEditionInfo() throws Exception
	{
		return element("fp.curEdition").getInnerText();
	}

	public boolean isEnableEditeCell(String cellId) throws Exception
	{
		boolean editeEnable = false;
		element("fp.inputCell", cellId).click();
		waitStatusDlg();
		if (element("fp.cellEditorContainer").isDisplayed())
			editeEnable = true;
		return editeEnable;
	}

	/**
	 * add instance, if add succeed, return true,otherwise return false
	 *
	 * @param instanceName
	 * @return
	 * @throws Exception
	 */
	public boolean addInstance(String instanceName) throws Exception
	{
		boolean rst = false;
		addPageInstanceClick();
		inputInstanceName(instanceName);

		if (getCurrentPageInstance().equals(instanceName))
			rst = true;

		return rst;
	}

	/**
	 * turn of scale
	 *
	 * @throws Exception
	 */
	public void turnOnScale() throws Exception
	{
		logger.info("Turn on show scale button");
		if (!element("fp.scaleStatus").getAttribute("class").contains("slideBarSetTrue"))
		{
			element("fp.chanScaleStatus").click();
			waitStatusDlg();
		}
	}

	/**
	 * turn off scale
	 *
	 * @throws Exception
	 */
	public void turnOffScale() throws Exception
	{
		logger.info("Turn off show scale button");
		if (element("fp.scaleStatus").getAttribute("class").contains("slideBarSetTrue"))
		{
			element("fp.chanScaleStatus").click();
			waitStatusDlg();
		}
	}

	/**
	 * import adjustment file, if import succeed, return true, otherwise return
	 * false
	 *
	 * @param importFile
	 * @param addToExistValue
	 * @param InitialiseToZeros
	 * @return true or false
	 * @throws Exception
	 */
	public boolean importAdjustment(File importFile, boolean addToExistValue, boolean InitialiseToZeros) throws Exception
	{
		logger.info("Begin import file:" + importFile);
		boolean importRst = true;
		String type = "importFileForm";
		ListImportFilePage importFileInReturnPage = getImportPageInReturn();
		try
		{
			importFileInReturnPage.setImportFile(importFile, type);
			if (addToExistValue)
				importFileInReturnPage.selectAddToExistingValue(type);
			if (InitialiseToZeros)
				importFileInReturnPage.tickInitToZero(type);
			importFileInReturnPage.importFileBtnClick(type);
		}
		catch (Exception e)
		{
			importRst = false;
			importFileInReturnPage.closeImportFileDlg(type);
			//e.printStackTrace();
		}
		return importRst;
	}

	/**
	 * get Error Message when import adjustment file
	 *
	 * @param importFile
	 * @return ErrorMessage
	 * @throws Exception
	 */
	public String getImportAdjustmentErrorMsg(File importFile) throws Exception
	{
		String type = "importFileForm";
		String error = null;
		ListImportFilePage importFileInReturnPage = getImportPageInReturn();
		try
		{
			importFileInReturnPage.setImportFile(importFile, type);
			error = importFileInReturnPage.getErrorMessage(type);
			if (error.length() == 0)
			{
				importFileInReturnPage.importFileBtnClick(type);
				error = importFileInReturnPage.getErrorMessage(type);
			}
			importFileInReturnPage.closeImportFileDlg(type);
		}
		catch (Exception e)
		{
			importFileInReturnPage.closeImportFileDlg(type);
			//e.printStackTrace();
		}
		return error;
	}

	/**
	 * get ErrorInfo when import adjustment file
	 *
	 * @param importFile
	 * @return ErrorInfo
	 * @throws Exception
	 */
	public String getImportAdjustmentErrorInfo(File importFile) throws Exception
	{
		String type = "importFileForm";
		String info = null;
		ListImportFilePage importFileInReturnPage = getImportPageInReturn();
		try
		{
			importFileInReturnPage.setImportFile(importFile, type);
			info = importFileInReturnPage.getErrorInfo(type);
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
		finally
		{
			importFileInReturnPage.closeImportFileDlg(type);
		}
		return info;
	}

	/**
	 * get selected page
	 *
	 * @return pageName
	 * @throws Exception
	 */
	public String getSelectedPage() throws Exception
	{
		return element("fp.selectedPage").getInnerText();
	}

	/**
	 * get pageName,if cell is not extend grid,extendCell= null
	 *
	 * @param Regulator
	 * @param form
	 * @param version
	 * @param instance
	 * @param cellName
	 * @param extendCell
	 * @return pageName
	 * @throws Exception
	 */
	private String getPageNameByCell(String Regulator, String form, String version, String instance, String cellName, String extendCell) throws Exception
	{
		String page = null;
		if (extendCell == null)
		{
			List<String> pageNames = getPageNames(Regulator, form, version, cellName, extendCell);
			page = pageNames.get(0);
			selectPage(page);
			selectInstance(instance);

		}
		else
		{
			boolean findCell = false;
			if (element("fp.nextPage").isDisplayed())
			{
				if (!element("fp.firstPageSta").getAttribute("class").contains("ui-state-disabled"))
				{
					element("fp.firstPage").click();
					waitStatusDlg();
				}

				if (element("fp.inputCell", extendCell).isDisplayed())
				{
					selectInstance(instance);
					if (element("fp.inputCell", extendCell).isDisplayed())
					{
						findCell = true;
						page = getSelectedPage();
					}
				}
				else
				{
					while (!findCell && !element("fp.nextPageSta").getAttribute("class").contains("ui-state-disabled"))
					{
						waitStatusDlg();
						element("fp.nextPage").click();
						waitStatusDlg();
						if (element("fp.inputCell", extendCell).isDisplayed())
						{
							selectInstance(instance);
							if (element("fp.inputCell", extendCell).isDisplayed())
							{
								findCell = true;
								page = getSelectedPage();
							}
						}
					}
				}

			}
			else
			{
				if (element("fp.inputCell", extendCell).isDisplayed())
				{
					selectInstance(instance);
					if (element("fp.inputCell", extendCell).isDisplayed())
					{
						findCell = true;
					}
					page = getSelectedPage();
				}
			}

			if (!findCell)
			{
				List<String> pages = getPageNames(Regulator, form, version, cellName, extendCell);

				for (String pageName : pages)
				{
					selectPage(pageName);
					selectInstance(instance);

					if (element("fp.nextPage").isDisplayed())
					{
						if (!element("fp.firstPageSta").getAttribute("class").contains("ui-state-disabled"))
						{
							element("fp.firstPage").click();
							waitStatusDlg();
						}
						if (element("fp.inputCell", extendCell).isDisplayed())
						{
							findCell = true;
							page = pageName;
						}
						while (!findCell && !element("fp.nextPageSta").getAttribute("class").contains("ui-state-disabled"))
						{
							waitThat().timeout(500);
							element("fp.nextPage").click();
							waitStatusDlg();
							if (element("fp.inputCell", extendCell).isDisplayed())
							{
								findCell = true;
								page = pageName;
							}
						}
					}
					else
					{
						if (element("fp.inputCell", extendCell).isDisplayed())
						{
							findCell = true;
							page = pageName;
						}
					}

					if (findCell)
					{
						break;
					}
				}
			}
		}
		return page;
	}

	/**
	 * export form to file
	 *
	 * @param Group
	 * @param Form
	 * @param ProcessDate
	 * @param fileType
	 * @param Module
	 * @param compressType
	 * @return export file path or error
	 * @throws Exception
	 */
	public String exportToFile(String Group, String Form, String ProcessDate, String fileType, String Module, String compressType) throws Exception
	{
		logger.info("Begin exprot to " + fileType);
		String filePath = null;
		String dir = FileUtils.getUserDirectoryPath() + "/downloads";
		String latestFile = getLatestFile(dir);
		exportToFileClick(fileType, Form, Module, compressType, null);
		boolean flag = true;
		long statTime = System.currentTimeMillis();
		while (flag)
		{
			if (element("fp.messageTitle").isDisplayed())
			{
				if (element("fp.messageTitle").getInnerText().equalsIgnoreCase("error"))
				{
					filePath = "Error";
					logger.info("Error msssage is:" + element("fp.message").getInnerText());
					break;
				}
			}
			long curTime = System.currentTimeMillis();
			if ((curTime - statTime) / 1000 > 5)
			{
				break;
			}
		}
		if (filePath == null)
		{
			if (httpDownload)
			{
				String exportedFile = System.getProperty("user.dir") + "/" + TestCaseManager.getTestCase().getDownloadFile();
				filePath = getOriginalFile(exportedFile, latestFile, true);
			}
			else
				filePath = downloadFile(fileType, latestFile, null);
		}

		return filePath;
	}

	/**
	 * export data schdule return
	 *
	 * @param fileType
	 * @param Module
	 * @param compressType
	 * @throws Exception
	 */
	public void exportDataSchduleReturn(String fileType, String Module, String compressType) throws Exception
	{
		logger.info("Begin exprot to " + fileType);
		fileType = fileType.toLowerCase();
		exportToFileClick(fileType, null, Module, compressType, null);
		if (isJobSucceed())
		{
			logger.info("Export to dataSchedule succeed");
		}
		else
			logger.error("Export to dataSchedule failed");

	}

	/**
	 * verify if data schdule return could exported successfully
	 *
	 * @param fileType
	 * @param Module
	 * @param compressType
	 * @param comment
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public boolean isExportDataSchduleSucceed(String fileType, String Module, String compressType, String comment, String location) throws Exception
	{
		logger.info("Begin exprot to " + fileType);
		boolean exportRst = false;
		fileType = fileType.toLowerCase();
		String latest = getLatestFile(location);
		exportToFileClick(fileType, null, Module, compressType, comment);
		if (isJobSucceed())
		{
			String downloadedFile = downloadFile(fileType, latest, location).toLowerCase();
			if ("NONE".equals(compressType))
			{
				if ("ds".equals(fileType))
				{
					if (downloadedFile.endsWith(".xml"))
						exportRst = true;
				}
				else if ("ds(csv)".equals(fileType))
				{
					if (downloadedFile.endsWith(".csv"))
						exportRst = true;
				}
				else if ("ds(txt)".equals(fileType))
				{
					if (downloadedFile.endsWith(".txt"))
						exportRst = true;
				}
			}
			else
			{
				compressType = compressType.toLowerCase();
				if (downloadedFile.endsWith("." + compressType))
					exportRst = true;
			}
		}
		if (exportRst)
			logger.info("Export to dataSchedule succeed");
		else
			logger.error("Export to dataSchedule failed");
		return exportRst;
	}

	/**
	 *
	 * @param fileType
	 * @param Module
	 * @param compressType
	 * @param comment
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public boolean isExportDataSchduleSucessed_XMLVAL(String fileType, String Module, String compressType, String comment, String location) throws Exception
	{
		logger.info("Begin exprot to " + fileType);
		boolean exportRst = false;
		fileType = fileType.toLowerCase();
		String latest = getLatestFile(location);
		exportToFileClick(fileType, null, Module, compressType, comment);
		isJobSucceed();
		String downloadedFile = downloadFile(fileType, latest, location).toLowerCase();
		if ("NONE".equals(compressType))
		{
			if ("ds".equals(fileType))
			{
				if (downloadedFile.endsWith(".xml"))
					exportRst = true;
			}
			else if ("ds(csv)".equals(fileType))
			{
				if (downloadedFile.endsWith(".csv"))
					exportRst = true;
			}
			else if ("ds(txt)".equals(fileType))
			{
				if (downloadedFile.endsWith(".txt"))
					exportRst = true;
			}
		}
		else
		{
			compressType = compressType.toLowerCase();
			if (downloadedFile.endsWith("." + compressType))
				exportRst = true;
		}
		if (exportRst)
			logger.info("Export to dataSchedule succeed");
		else
			logger.error("Export to dataSchedule failed");
		return exportRst;
	}

	public String getDownloadedDSReturn(String location) throws Exception
	{
		return getLatestFile(location);
	}

	public String getExportDataScheduleMessage(String fileType, String Module, String compressType, boolean lock) throws Exception
	{
		logger.info("Begin export to " + fileType);
		String message = null;
		exportToFileClick(fileType, null, Module, compressType, null);
		if (lock)
			unlockClick();
		boolean flag = true;
		long statTime = System.currentTimeMillis();
		while (flag)
		{
			if (element("fp.message").isDisplayed())
			{
				message = element("fp.message").getInnerText();
				break;
			}
			long curTime = System.currentTimeMillis();
			if ((curTime - statTime) / 1000 > 5)
			{
				break;
			}
		}
		return message;
	}

	/**
	 * Enter export to file page
	 *
	 * @param type
	 * @param Form
	 * @return ExportToFilePage
	 * @throws Exception
	 */
	public ExportToFilePage enterExportToFiLePage(String type, String Form) throws Exception
	{
		if (type.toLowerCase().startsWith("text"))
		{
			logger.info("Click export to RC-Txt");
			String formCode = splitReturn(Form).get(0);
			String formVersion = splitReturn(Form).get(1);
			if (type.contains("(") && type.contains(")"))
			{
				type = type.replace("(", "#").replace(")", "");
				String option = type.split("#")[1];
				element("fp.exportToRC2", option).click();
			}
			else
			{
				if (element("fp.exportToRC", formCode.toUpperCase()).isDisplayed())
					element("fp.exportToRC", formCode.toUpperCase()).click();
				else if (element("fp.exportToRC", formCode.toUpperCase() + "_V" + formVersion).isDisplayed())
					element("fp.exportToRC", formCode.toUpperCase() + "_V" + formVersion).click();
			}
		}
		else if (type.equalsIgnoreCase("vanilla"))
		{
			logger.info("Click export to vanilla");
			if (format.equalsIgnoreCase("zh_CN"))
				element("fp.exportToVan_CN").click();
			else
				element("fp.exportToVan").click();
		}
		else if (type.equalsIgnoreCase("ARBITRARY"))
		{
			logger.info("Click export to ARBITRARY");
			if (element("fp.exportToARB").isDisplayed())
			{
				if (format.equalsIgnoreCase("zh_CN"))
					element("fp.exportToARB_CN").click();
				else
					element("fp.exportToARB").click();
			}

			else if (element("fp.exportToARB2").isDisplayed())
			{
				if (format.equalsIgnoreCase("zh_CN"))
					element("fp.exportToARB2_CN").click();
				else
					element("fp.exportToARB2").click();
			}

		}
		else if (type.equalsIgnoreCase("DS"))
		{
			logger.info("Click export to data schdule");
			element("fp.exportToDS").click();
		}
		else if (type.equalsIgnoreCase("DS(csv)"))
		{
			logger.info("Click export to data schdule(csv)");
			element("fp.exportToDSCsv").click();
		}
		else if (type.equalsIgnoreCase("DS(txt)"))
		{
			logger.info("Click export to data schdule(txt)");
			element("fp.exportToDSTxt").click();
		}
		else if (type.equalsIgnoreCase("iFile"))
		{
			logger.info("Click export to iFile");
			if (format.equalsIgnoreCase("zh_CN"))
				element("fp.exportToiFIile_CN").click();
			else
				element("fp.exportToiFIile").click();
		}
		else if (type.equalsIgnoreCase("xbrl"))
		{
			logger.info("Click export to xbrl");
			if (format.equalsIgnoreCase("zh_CN"))
				element("fp.exportToXBRL_CN").click();
			else
				element("fp.exportToXBRL").click();
			Thread.sleep(5000);
		}
		waitStatusDlg();

		return new ExportToFilePage(getWebDriverWrapper());
	}

	/**
	 * click export to file
	 *
	 * @param fileType
	 * @param Form
	 * @param module
	 * @param compressType
	 * @param comment
	 * @throws Exception
	 */
	private void exportToFileClick(String fileType, String Form, String module, String compressType, String comment) throws Exception
	{
		ExportToFilePage exportToFilePage = null;
		getFormatFromDB();
		if (fileType.equalsIgnoreCase("csv"))
		{
			element("fp.expToFile").click();
			waitStatusDlg();
			if (httpDownload)
			{
				TestCaseManager.getTestCase().startTransaction("");
				TestCaseManager.getTestCase().setPrepareToDownload(true);
				if (format.equalsIgnoreCase("zh_CN"))
					element("fp.expToCSV_CN").click();
				else
					element("fp.expToCSV").click();
				TestCaseManager.getTestCase().stopTransaction();
			}
			else
			{
				if (format.equalsIgnoreCase("zh_CN"))
					element("fp.expToCSV_CN").click();
				else
					element("fp.expToCSV").click();
			}

			waitStatusDlg();
		}
		else if (fileType.equalsIgnoreCase("excel"))
		{
			element("fp.expToFile").click();
			waitStatusDlg();
			if (httpDownload)
			{
				TestCaseManager.getTestCase().startTransaction("");
				TestCaseManager.getTestCase().setPrepareToDownload(true);
				if (format.equalsIgnoreCase("zh_CN"))
					element("fp.expToXls_CN").click();
				else
					element("fp.expToXls").click();
				TestCaseManager.getTestCase().stopTransaction();
			}
			else
			{
				if (format.equalsIgnoreCase("zh_CN"))
					element("fp.expToXls_CN").click();
				else
					element("fp.expToXls").click();
			}

			waitStatusDlg();
		}
		else if (fileType.toLowerCase().startsWith("text") || fileType.equalsIgnoreCase("vanilla") || fileType.equalsIgnoreCase("ARBITRARY") || fileType.toLowerCase().startsWith("ds")
				|| fileType.equalsIgnoreCase("iFile"))
		{
			element("fp.exportToRF").click();
			waitStatusDlg();
			exportToFilePage = enterExportToFiLePage(fileType, Form);
			exportToFilePage.setModuleSelector(module, fileType);
			exportToFilePage.setCompressType(compressType, fileType);
			exportToFilePage.exportBtnClick(fileType);

			if (fileType.toLowerCase().startsWith("ds"))
			{
				if (element("fp.JobResultOK").isDisplayed())
				{
					element("fp.JobResultOK").click();
					waitStatusDlg();
				}
			}
			else
			{
				exportToFilePage.setExportComment(comment);
			}
			exportToFilePage.closeExportPage(fileType);
		}
		else if (fileType.equalsIgnoreCase("XBRL"))
		{
			element("fp.exportToRF").click();
			waitStatusDlg();
			exportToFilePage = enterExportToFiLePage(fileType, Form);
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

			exportToFilePage.exportBtnClick(fileType);
			exportToFilePage.closeExportPage(fileType);
		}

	}

	/**
	 * if exit export to csv option
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isExportToExcelCsvDisplayed() throws Exception
	{
		boolean rst = false;
		element("fp.expToFile").click();
		Thread.sleep(300);
		if (element("fp.expToCSV").isDisplayed() && element("fp.expToXls").isDisplayed())
			rst = true;
		element("fp.expToFile").click();
		Thread.sleep(300);
		return rst;
	}

	/**
	 * get form info
	 *
	 * @return
	 * @throws Exception
	 */
	private String getFormInfo() throws Exception
	{
		return element("fp.form").getInnerText();
	}

	/**
	 * if problems panel is displayed
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isProblemsPanelDisplayed() throws Exception
	{
		boolean openProblemPanel = false;
		try
		{
			if (!element("fp.valTab").getAttribute("aria-expanded").equals("true"))
				openProblemPanel = true;
		}
		catch (Exception e)
		{
			openProblemPanel = true;
		}

		return openProblemPanel;
	}

	/**
	 * enter valiadtion result panel
	 *
	 * @param valNow
	 * @return ValidationPage
	 * @throws Exception
	 */
	public ValidationPage enterValidation(boolean valNow) throws Exception
	{
		if (valNow)
		{
			validationNowClick();
			Thread.sleep(3000);
		}
		if (!element("fp.formFooter").isDisplayed())
		{
			element("fp.adjustment").click();
			waitStatusDlg();
			element("fp.adjLog").click();
			waitStatusDlg();
			waitThat().timeout(2000);
		}

		element("fp.valTab").click();
		waitStatusDlg();

		return new ValidationPage(getWebDriverWrapper());
	}

	/**
	 * enter error result page
	 *
	 * @return ErrorListPage
	 * @throws Exception
	 */
	public ErrorListPage enterProblem() throws Exception
	{
		logger.info("Enter Problems table");
		if (!(element("fp.proDataTab")).isDisplayed())
		{
			try
			{
				if (element("fp.errorBtn").isDisplayed())
				{
					element("fp.errorBtn").click();
					waitStatusDlg();
				}
				else if (element("fp.formFooter").isDisplayed())
				{
					element("fp.proTab").click();
					waitStatusDlg();
				}
				else
				{
					element("fp.adjustment").click();
					waitStatusDlg();
					element("fp.adjLog").click();
					waitStatusDlg();
					element("fp.proTab").click();
					waitStatusDlg();
				}
			}
			catch (NoSuchElementException e)
			{
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

	public List<String> getHighlightCount_Instance(String instance) throws Exception
	{
		logger.info("Begin get highlight count on instance name");
		List<String> rst = new ArrayList<String>();
		element("fp.curInst").click();
		waitThat().timeout(1000);
		if (element("fp.instance.critical", instance).isDisplayed())
		{
			String count = element("fp.instance.critical", instance).getInnerText().trim();
			rst.add(count);
		}
		else
			rst.add("0");

		if (element("fp.instance.warn", instance).isDisplayed())
		{
			String count = element("fp.instance.warn", instance).getInnerText().trim();
			rst.add(count);
		}
		else
			rst.add("0");
		element("fp.curInst").click();
		return rst;
	}

	public List<String> getHighlightCount_Page(String pageName, String instance) throws Exception
	{
		logger.info("Begin get highlight count on page name");
		List<String> rst = new ArrayList<String>();
		selectInstance(instance);
		if (element("fp.criticalNums", pageName).isDisplayed())
		{
			String count = element("fp.criticalNums", pageName).getInnerText().trim();
			rst.add(count);
		}
		else
			rst.add("0");

		if (element("fp.warnNums", pageName).isDisplayed())
		{
			String count = element("fp.warnNums", pageName).getInnerText().trim();
			rst.add(count);
		}
		else
			rst.add("0");
		return rst;
	}

	/**
	 * get cell color
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param cellName
	 * @return color
	 * @throws Exception
	 */
	public String getCellColor(String Regulator, String formCode, String version, String cellName) throws Exception
	{
		logger.info("Check  if " + cellName + " backgroud color");
		String extendCell = null;
		String instance = null;
		if (cellName.contains("["))
		{
			instance = cellName.substring(cellName.indexOf("[") + 1, cellName.indexOf("]"));
			cellName = cellName.replace("[", "").replace("]", "");
		}
		if (cellName.contains("@"))
		{
			cellName = cellName.split("@")[0];
			String rowID = cellName.split("@")[1];
			if (rowID.equals("0"))
			{
				extendCell = cellName;
			}
			else
			{
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

	/**
	 * get cell color
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param instance
	 * @param cellName
	 * @return color
	 * @throws Exception
	 */
	public String getCellColor(String Regulator, String formCode, String version, String instance, String cellName) throws Exception
	{
		logger.info("Get " + cellName + " backgroud color");
		String extendCell = null;
		if (cellName.contains("@"))
		{
			String tmp = cellName;
			cellName = tmp.split("@")[0];
			String rowID = tmp.split("@")[1];
			if (rowID.equals("0"))
			{
				extendCell = cellName;
			}
			else
			{
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
		else if (color.equalsIgnoreCase("rgba(250, 205, 189, 1)"))
			return "orange";
		else if (color.equalsIgnoreCase("rgba(255, 255, 255, 1)"))
			return "white";
		else
			return "";
	}

	/**
	 * if workflow button is displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isWorkflowDisplayed() throws Exception
	{
		boolean rst = false;
		if (connectedDB.equalsIgnoreCase("ar"))
		{
			if (element("fp.wfBtn").isDisplayed())
			{
				element("fp.wfBtn").click();
				Thread.sleep(500);
				if (element("fp.approve").isDisplayed() && element("fp.reject").isDisplayed())
					rst = true;
				element("fp.wfBtn").click();
				Thread.sleep(500);
			}
		}
		else
		{
			if (element("fp.toolset.wfBtn").isDisplayed())
			{
				element("fp.toolset.wfBtn").click();
				Thread.sleep(500);
				if (element("fp.approve").isDisplayed() && element("fp.reject").isDisplayed())
					rst = true;
				element("fp.toolset.wfBtn").click();
				Thread.sleep(500);
			}
		}
		return rst;

	}

	/**
	 * if workflow log option is displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isWorkflowLogDisplayed() throws Exception
	{
		element("fp.wfBtn").click();
		Thread.sleep(500);
		return element("fp.wfLog").isDisplayed();
	}

	public void closeTransDlg() throws Exception
	{
		element("fp.closeTransDlg").click();
	}

	public boolean nextPageEnable() throws Exception
	{
		if (!element("fp.nextPageSta").getAttribute("class").contains("ui-state-disabled"))
			return true;
		else
			return false;
	}

	public void clickNextPage() throws Exception
	{
		element("fp.nextPage").click();
		waitStatusDlg();
	}

	public void clickPreviousPage() throws Exception
	{
		element("fp.prePage").click();
		waitStatusDlg();
	}

	public boolean isVisible(String loc) throws Exception
	{
		return element(loc).isDisplayed();
	}

	public String getElementAttribute(String loc, String type) throws Exception
	{
		return element(loc).getAttribute(type);
	}

	public List<String> getAllInstance(String page) throws Exception
	{
		logger.info("Get all instance names");
		List<String> instances = new ArrayList<String>();
		selectPage(page);
		element("fp.curInst").click();
		waitThat().timeout(300);
		int amt = (int) element("fp.instaceTab").getRowCount();
		for (int i = 1; i <= amt; i++)
		{
			instances.add(element("fp.instace", String.valueOf(i)).getAttribute("data-label"));
		}
		return instances;
	}

	/**
	 * get all instances
	 *
	 * @param Regulator
	 * @param formCode
	 * @param version
	 * @param cellName
	 * @return instances
	 * @throws Exception
	 */
	public List<String> getAllInstance(String Regulator, String formCode, String version, String cellName) throws Exception
	{
		logger.info("Get all instance names");
		List<String> instances = new ArrayList<String>();
		String extendCell = null;
		if (cellName.contains("@"))
		{
			String tmp = cellName;
			cellName = tmp.split("@")[0];
			String rowID = tmp.split("@")[1];
			if (rowID.equals("0"))
			{
				extendCell = cellName;
			}
			else
			{
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
		for (int i = 1; i <= amt; i++)
		{
			instances.add(element("fp.instace", String.valueOf(i)).getAttribute("data-label"));
		}
		element("fp.curInst").click();
		waitThat().timeout(300);
		return instances;
	}

	/**
	 * if ReadForApprove button is displayed
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isReadyForApproveDisplayed() throws Exception
	{
		boolean displayed = false;
		if (connectedDB.equalsIgnoreCase("ar"))
		{
			element("fp.wfBtn").click();
			waitThat().timeout(400);
			if (element("fp.readyAppr").isDisplayed())
				displayed = true;
			element("fp.wfBtn").click();
		}
		else
		{
			element("fp.toolset.wfBtn").click();
			waitThat().timeout(400);
			if (element("fp.toolset.readyAppr").isDisplayed())
				displayed = true;
			element("fp.toolset.wfBtn").click();
		}
		return displayed;
	}

	/**
	 * click readyForApprove button
	 *
	 * @throws Exception
	 */
	public void clickReadyForApprove() throws Exception
	{
		logger.info("Click ready for approve button");
		if (connectedDB.equalsIgnoreCase("ar"))
		{
			element("fp.wfBtn").click();
			waitThat().timeout(500);
			element("fp.readyAppr").click();
		}
		else
		{
			element("fp.toolset.wfBtn").click();
			waitThat().timeout(300);
			element("fp.toolset.readyAppr").click();
		}
		waitStatusDlg();
	}

	/**
	 * get message when click readForApprove button
	 *
	 * @return message
	 * @throws Exception
	 */
	public String getReadyForApproveMessage() throws Exception
	{
		if (connectedDB.equalsIgnoreCase("ar"))
		{
			element("fp.wfBtn").click();
			waitThat().timeout(400);
			element("fp.readyAppr").click();
		}
		else
		{
			element("fp.toolset.wfBtn").click();
			waitThat().timeout(400);
			element("fp.toolset.readyAppr").click();
		}
		String msg = element("fp.message").getInnerText();
		waitThat("fp.message").toBeInvisible();
		return msg;
	}

	/**
	 * click approve
	 *
	 * @param comment
	 * @throws Exception
	 */
	public void approveForm(String comment) throws Exception
	{
		logger.info("Begin approve form");
		if (connectedDB.equalsIgnoreCase("ar"))
		{
			element("fp.wfBtn").click();
			waitThat("fp.approve").toBeVisible();
			element("fp.approve").click();
		}
		else
		{
			element("fp.toolset.wfBtn").click();
			waitThat("fp.toolset.approve").toBeVisible();
			element("fp.toolset.approve").click();
		}
		waitThat("fp.apprCommentOK").toBeVisible();
		if (comment != null)
			element("fp.apprComment").type(comment);
		element("fp.apprCommentOK").click();
		waitStatusDlg();
		waitThat().timeout(1000);
	}

	/**
	 * get approve message
	 *
	 * @param comment
	 * @return message
	 * @throws Exception
	 */
	public String getApproveFormMessage(String comment) throws Exception
	{
		if (connectedDB.equalsIgnoreCase("ar"))
		{
			element("fp.wfBtn").click();
			waitThat("fp.approve").toBeVisible();
			element("fp.approve").click();
		}
		else
		{
			element("fp.toolset.wfBtn").click();
			waitThat("fp.toolset.approve").toBeVisible();
			element("fp.toolset.approve").click();
		}
		waitThat("fp.apprCommentOK").toBeVisible();
		if (comment != null)
			element("fp.apprComment").type(comment);
		element("fp.apprCommentOK").click();
		String msg = element("fp.message").getInnerText();
		waitThat("fp.message").toBeInvisible();
		return msg;
	}

	/**
	 * reject form
	 *
	 * @param comment
	 * @throws Exception
	 */
	public void rejectForm(String comment) throws Exception
	{
		logger.info("Begin reject form");
		if (connectedDB.equalsIgnoreCase("ar"))
		{
			element("fp.wfBtn").click();
			waitThat("fp.reject").toBeVisible();
			element("fp.reject").click();
		}
		else
		{
			element("fp.toolset.wfBtn").click();
			waitThat("fp.toolset.reject").toBeVisible();
			element("fp.toolset.reject").click();
		}

		waitThat("fp.rejCommentOK").toBeVisible();
		if (comment == null)
			comment = "  ";
		element("fp.rejComment").type(comment);
		element("fp.rejCommentOK").click();
		waitStatusDlg();
		waitThat().timeout(1000);
	}

	/**
	 * get reject form message
	 *
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public String getRejectFormMessage(String comment) throws Exception
	{
		logger.info("Get reject message");
		if (connectedDB.equalsIgnoreCase("ar"))
		{
			element("fp.wfBtn").click();
			waitThat("fp.reject").toBeVisible();
			element("fp.reject").click();
		}
		else
		{
			element("fp.toolset.wfBtn").click();
			waitThat("fp.toolset.reject").toBeVisible();
			element("fp.toolset.reject").click();
		}
		waitThat("fp.rejCommentOK").toBeVisible();
		if (comment == null)
			comment = "reject";
		element("fp.rejComment").type(comment);
		element("fp.rejCommentOK").click();
		String msg = element("fp.message").getInnerText();
		waitThat("fp.message").toBeInvisible();
		return msg;
	}

	/**
	 * get approve count
	 *
	 * @return num
	 * @throws Exception
	 */
	public String getApproveCount() throws Exception
	{
		logger.info("Get approve nums");
		return element("fp.attestation").getInnerText().replace("Ready For Attestation ", "").replace("Being Attested ", "").replace("Attested ", "");
	}

	/**
	 * get current approve status
	 *
	 * @return status
	 * @throws Exception
	 */
	public String getApproveStatus() throws Exception
	{
		logger.info("Get approve status");
		String status = element("fp.attestation").getInnerText().replace("(", "#");
		return status.split("#")[0].trim();
	}

	/**
	 * get approve log nums
	 *
	 * @return nums
	 * @throws Exception
	 */
	public int getApproveLogAmt() throws Exception
	{
		logger.info("Get approve log nums");
		int num = 0;
		element("fp.wfBtn").click();
		waitThat("fp.viewApproveLog").toBeVisible();
		element("fp.viewApproveLog").click();
		waitStatusDlg();
		if (element("fp.approveLog1").getInnerText().equalsIgnoreCase("No records found."))
			num = 0;
		else
		{
			num = (int) element("fp.approveLog").getRowCount();
		}
		element("fp.CloseApproveLog").click();
		waitStatusDlg();
		return num;
	}

	/**
	 * get columns in approve log panel
	 *
	 * @return columns
	 * @throws Exception
	 */
	public List<String> getApproveLogColumns() throws Exception
	{
		logger.info("Get approve log columns");
		List<String> columnsList = new ArrayList<String>();
		element("fp.wfBtn").click();
		waitThat("fp.viewApproveLog").toBeVisible();
		element("fp.viewApproveLog").click();
		waitStatusDlg();
		columnsList = element("fp.workflowLogHeadColumn").getAllInnerTexts();
		element("fp.CloseApproveLog").click();
		waitStatusDlg();
		return columnsList;
	}

	/**
	 * if support pagination in workflow log panel
	 *
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isPagination() throws Exception
	{
		element("fp.wfBtn").click();
		waitThat("fp.viewApproveLog").toBeVisible();
		element("fp.viewApproveLog").click();
		Thread.sleep(500);
		boolean rst = false;
		if (element("fp.workflowLogPage").isDisplayed())
			rst = true;
		closeWorkflowlog();
		return rst;

	}

	/**
	 * export validation result to file
	 *
	 * @return export file path
	 * @throws Exception
	 */
	public String exportValidationResult() throws Exception
	{
		String file = null;
		logger.info("Begin export validation result to excel");
		enterValidation(true);
		String dir = FileUtils.getUserDirectoryPath() + "/downloads";
		String latestFile = getLatestFile(dir);
		if (httpDownload)
		{
			TestCaseManager.getTestCase().startTransaction("");
			TestCaseManager.getTestCase().setPrepareToDownload(true);
			element("fp.validationExport").click();
			TestCaseManager.getTestCase().stopTransaction();
			String exportedFile = System.getProperty("user.dir") + "/" + TestCaseManager.getTestCase().getDownloadFile();
			file = getOriginalFile(exportedFile, latestFile, setOriginalName);
		}
		else
		{
			element("fp.validationExport").click();
			file = downloadFile(null, latestFile, null);
		}

		return file;
	}

	public String exportProblem() throws Exception
	{
		logger.info("Begin get rule message");
		getDrillDown();
		waitStatusDlg();
		try
		{
			element("fp.proTab").click();
		}
		catch (Exception e)
		{
			Thread.sleep(3000);
			element("fp.proTab").click();
		}
		waitStatusDlg();
		waitThat("fp.problemExport").toBeClickable();
		String file = null;
		String dir = FileUtils.getUserDirectoryPath() + "/downloads";
		String latestFile = getLatestFile(dir);
		if (httpDownload)
		{
			TestCaseManager.getTestCase().startTransaction("");
			TestCaseManager.getTestCase().setPrepareToDownload(true);
			element("fp.problemExport").click();
			TestCaseManager.getTestCase().stopTransaction();
			String exportedFile = System.getProperty("user.dir") + "/" + TestCaseManager.getTestCase().getDownloadFile();
			file = getOriginalFile(exportedFile, latestFile, setOriginalName);
		}
		else
		{
			element("fp.problemExport").click();
			waitStatusDlg();
			file = downloadFile(null, latestFile, null);
		}
		waitStatusDlg();
		waitThat().timeout(500);
		element("fp.hideErrorTable").click();
		waitStatusDlg();
		return file;
	}

	public boolean isCellHighlight(String Regulator, String form, String version, String instance, String cellName, String extendCell) throws Exception
	{
		getPageNameByCell(Regulator, form, version, instance, cellName, extendCell);
		String classContext = element("fp.highlightCell", cellName).getAttribute("class");
		if (classContext.contains("h_val_critical") || classContext.contains("h_u_val_warning"))
			return true;
		else
			return false;
	}

	public boolean isPageHighlight(String pageName) throws Exception
	{
		String clsss = element("fp.highlightPage", pageName).getAttribute("class");
		if (clsss.contains("highlightRowBlue") || clsss.contains("ui-state-highlight"))
			return true;
		else
			return false;
	}

	public ReturnSourcePage enterReturnSourcePage() throws Exception
	{
		element("fp.returnSource").click();
		waitStatusDlg();
		return new ReturnSourcePage(getWebDriverWrapper());
	}

	public String exportRetrieveLog() throws Exception
	{
		logger.info("Export retrieve log");
		element("fp.showImportLog").click();
		Thread.sleep(3000);
		String file = null;
		String dir = FileUtils.getUserDirectoryPath() + "/downloads";
		String latestFile = getLatestFile(dir);
		if (httpDownload)
		{
			TestCaseManager.getTestCase().startTransaction("");
			TestCaseManager.getTestCase().setPrepareToDownload(true);
			element("fp.firstExportBrn").click();
			TestCaseManager.getTestCase().stopTransaction();
			String exportedFile = System.getProperty("user.dir") + "/" + TestCaseManager.getTestCase().getDownloadFile();
			file = getOriginalFile(exportedFile, latestFile, setOriginalName);
		}
		else
		{
			element("fp.firstExportBrn").click();
			file = downloadFile(null, latestFile, null);
		}
		return file;

	}

	public void closeRetrieveLog() throws Exception
	{
		waitStatusDlg();
		element("fp.closeLog").click();
		waitStatusDlg();
	}

	public void closeRetrieveDialog() throws Exception
	{
		try
		{
			if (element("fp.JobResultOK").isDisplayed())
				element("fp.JobResultOK").click();
		}
		catch (Exception e)
		{
			waitStatusDlg();
			element("fp.JobResultOK").click();
		}
		waitStatusDlg();
	}

	public void closeWorkflowlog() throws Exception
	{
		waitStatusDlg();
		element("lp.closeWorkflowLog").click();
		waitStatusDlg();
	}

	/**
	 * Get the edition information
	 *
	 * @throws Exception
	 */
	public List<String> getEditionInfo() throws Exception
	{
		List<String> editions = new ArrayList<>();
		element("fp.curEdition").click();
		waitStatusDlg();

		for (IWebElementWrapper element : element("fp.editionInfo").getAllMatchedElements())
		{
			editions.add(element.getInnerText());
		}
		element("fp.curEdition").click();
		waitStatusDlg();
		return editions;
	}

	/**
	 * Is the cell exist or not
	 */
	public boolean isCellExist(String cellId) throws Exception
	{
		return element("fp.cellID", cellId).isPresent();
	}

	/**
	 * verify if row have no strikethrough
	 *
	 * @param rows
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isRowlimit(int rows) throws Exception
	{
		logger.info("Begin check rowlimit");
		boolean rst = true;
		for (int i = 1; i <= rows; i++)
		{
			logger.info("Check row " + i);
			if (!element("fp.cell.rowlimit", String.valueOf(i)).isDisplayed())
			{
				if (element("fp.nextPageSta").isDisplayed())
				{
					if (!element("fp.nextPageSta").getAttribute("tabindex").equals("-1"))
					{
						element("fp.nextPage").click();
						waitStatusDlg();
					}

				}
			}
			if (element("fp.cell.rowlimit", String.valueOf(i)).getAttribute("class").contains("invalidCellClass"))
			{
				rst = false;
				logger.error("Row " + i + " should not have strikethrough");
				break;
			}
		}
		return rst;
	}

	/**
	 * verify if row have no strikethrough
	 *
	 * @param startCell
	 *            ,rows
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isRowlimit(String startCell, int rows) throws Exception
	{
		logger.info("Begin check rowlimit");
		boolean rst = true;
		String id = startCell.substring(0, 11);
		for (int i = 1; i <= rows; i++)
		{
			logger.info("Check row " + i);
			if (!element("fp.cell.rowlimit", String.valueOf(i)).isDisplayed())
			{
				if (element("fp.nextPageSta2", id).isDisplayed())
				{
					if (!element("fp.nextPageSta2", id).getAttribute("tabindex").equals("-1"))
					{
						element("fp.nextPage2", id).click();
						waitStatusDlg();
					}

				}
			}
			String[] list =
			{ id, String.valueOf(i) };
			if (element("fp.cell.rowlimit2", list).getAttribute("class").contains("invalidCellClass"))
			{
				rst = false;
				logger.error("Row " + i + " should not have strikethrough");
				break;
			}
		}
		return rst;
	}

	/**
	 * verify if row is have strikethrough
	 *
	 * @param rowIndex
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isStrikethrough(String rowIndex) throws Exception
	{
		logger.info("Begin check if row[" + rowIndex + "]is have strikethrough");

		if (!element("fp.cell.rowlimit", rowIndex).isDisplayed())
		{
			if (element("fp.firstPageSta").isDisplayed())
			{
				if (!element("fp.firstPageSta").getAttribute("tabindex").equals("-1"))
				{
					element("fp.firstPage").click();
					waitStatusDlg();
				}
			}
		}
		if (!element("fp.cell.rowlimit", rowIndex).isDisplayed())
		{
			if (element("fp.nextPageSta").isDisplayed())
			{
				if (!element("fp.nextPageSta").getAttribute("tabindex").equals("-1"))
				{
					element("fp.nextPage").click();
					waitStatusDlg();
				}

			}
		}
		if (element("fp.cell.rowlimit", rowIndex).getAttribute("class").contains("invalidCellClass"))
		{
			logger.info("Row have strikethrough");
			return true;
		}

		else
		{
			logger.info("Row have no strikethrough");
			return false;
		}
	}

	/**
	 * verify if row is have strikethrough
	 *
	 * @param startCell
	 * @param rowIndex
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isStrikethrough(String startCell, String rowIndex) throws Exception
	{
		logger.info("Begin check if row[" + rowIndex + "] is have strikethrough");
		String[] list =
		{ startCell, rowIndex };

		if (!element("fp.cell.rowlimit2", list).isDisplayed())
		{
			try
			{
				if (!element("fp.firstPageSta2", startCell).getAttribute("tabindex").equals("-1"))
				{
					element("fp.firstPage2", startCell).click();
					waitStatusDlg();
				}
			}
			catch (Exception e)
			{
			}

		}

		if (!element("fp.cell.rowlimit2", list).isDisplayed())
		{
			try
			{
				if (!element("fp.nextPageSta2", startCell).getAttribute("tabindex").equals("-1"))
				{
					element("fp.nextPage2", startCell).click();
					waitStatusDlg();
				}
			}
			catch (Exception e)
			{
			}
		}

		if (element("fp.cell.rowlimit2", list).getAttribute("class").contains("invalidCellClass"))
		{
			logger.info("Row have strikethrough");
			return true;
		}
		else
		{
			logger.info("Row have no strikethrough");
			return false;
		}
	}

	/**
	 * get cell id by cell value
	 *
	 * @param cellValue
	 * @return cellID
	 * @throws Exception
	 */
	public String getCellIDByValue(String cellValue) throws Exception
	{
		return element("fp.cellIDByValue", cellValue).getAttribute("id");
	}

	/**
	 * check if cell have strikethrough by cell value
	 *
	 * @param cellValue
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isStrikethroughByValue(String cellValue) throws Exception
	{
		if (element("fp.cellIDByValue", cellValue).getAttribute("class").contains("invalidCellClass"))
		{
			logger.info("Row have strikethrough");
			return true;
		}
		else
		{
			logger.info("Row have no strikethrough");
			return false;
		}
	}

	/**
	 * get all options from cell drop down list
	 *
	 * @param cellName
	 *            ,cellValue
	 * @return All options(List)
	 * @throws Exception
	 */
	public List<String> getAllOptionsFromDropdown(String cellName, String cellValue) throws Exception
	{
		logger.info("Begin get options from dropdown list");
		// editCell(cellName, cellValue);
		inputInDropDownCell(cellName, cellValue);
		Thread.sleep(1000);
		List<String> optionsList = new ArrayList<>();
		for (IWebElementWrapper element : element("fp.dropdown.options").getAllMatchedElements())
		{
			optionsList.add(element.getInnerText());
		}
		cellEditCancelBtnClick();
		return optionsList;
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
	 * Open the export regulator format page
	 */
	public ExportToFilePage enterExportRegulatorFormatPage(String type, String form) throws Exception
	{
		element("fp.exportToRF").click();
		waitStatusDlg();
		return enterExportToFiLePage(type, form);
	}

	/**
	 * Get the export menu text
	 */
	public List<String> getExportRFMenuText() throws Exception
	{
		element("fp.exportToRF").click();
		waitStatusDlg();
		return element("fp.dropdown.exportRFMenu").getAllInnerTexts();
	}

	/**
	 * click contextual button
	 *
	 * @param contextualName
	 * @param cellName
	 * @throws Exception
	 */
	public void clickContextualButton(String contextualName, String cellName) throws Exception
	{
		cellClick(cellName);
		element("fp.contextual", contextualName).click();
		waitStatusDlg();
	}

	/**
	 * get url in new opened window
	 *
	 * @param contextualName
	 * @param cellName
	 * @return url
	 * @throws Exception
	 */
	public String getNewWindowURL(String contextualName, String cellName) throws Exception
	{
		String url = null;
		clickContextualButton(contextualName, cellName);
		String initHandletHandle = getWebDriverWrapper().getWindowHandle();
		for (String handle : getWebDriverWrapper().getWindowHandles())
		{
			if (!handle.equals(initHandletHandle))
			{
				getWebDriverWrapper().switchTo().window(handle);
				url = getWebDriverWrapper().getCurrentUrl();
				getWebDriverWrapper().close();
			}
		}
		getWebDriverWrapper().switchTo().window(initHandletHandle);
		return url;
	}

	/**
	 * if exist export to vanilla option
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean isExistExportToVanilla() throws Exception
	{
		element("fp.exportToRF").click();
		waitStatusDlg();
		return element("fp.exportToVan").isDisplayed();
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
		element("fp.exportToRF").click();
		waitStatusDlg();
		int nums = element("fp.exportRegOptions").getNumberOfMatches();
		for (int i = 1; i <= nums; i++)
		{
			options.add(element("fp.exportRegOption", String.valueOf(i)).getInnerText());
		}
		return options;
	}
}
