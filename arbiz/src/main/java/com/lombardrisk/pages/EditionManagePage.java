package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Create by Leo Tu on Sep 17, 2015
 */
public class EditionManagePage extends AbstractPage
{
	/**
	 * 
	 * @param webDriverWrapper
	 */
	public EditionManagePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * find edition by creation date
	 * 
	 * @param creationDate
	 * @return page_position
	 * @throws Exception
	 */
	private String findForm(String creationDate) throws Exception
	{
		String page_index;
		int pageNO = 1;
		int rowNo = 0;
		boolean findForm = false;
		while (!findForm)
		{
			boolean nextPageage = true;
			while (nextPageage)
			{
				int amt = (int) element("edm.editionMagForm").getRowCount();
				for (int i = 1; i <= amt; i++)
				{
					String cDate = element("edm.createDt", String.valueOf(i)).getInnerText();
					if (cDate.equalsIgnoreCase(creationDate))
					{
						nextPageage = false;
						findForm = true;
						rowNo = i;
						logger.info("Find the edition!");
						break;
					}
				}
				if (nextPageage)
				{
					try
					{
						String p_Index = element("edm.nextPage").getAttribute("tabindex");
						if (!"-1".equals(p_Index))
						{
							element("edm.nextPage").click();
							pageNO++;
						}
						else
						{
							nextPageage = false;
							findForm = true;
							logger.error("The edition does not exist!");
						}
					}
					catch (Exception e)
					{
						logger.warn(e.getMessage());
						nextPageage = false;
						findForm = true;

					}
				}

			}
		}
		page_index = pageNO + "_" + rowNo;

		return page_index;
	}

	/**
	 * Active edition by creation date
	 * 
	 * @param creationDate
	 * @return true-active succeed or false active failed
	 * @throws Exception
	 */
	public boolean activateForm(String creationDate) throws Exception
	{
		boolean state = false;
		String formPosition = findForm(creationDate);
		if ("1_0".equals(formPosition))
		{
			logger.error("The form with creation date=" + creationDate + " does not exist!");
		}
		String rowNo = formPosition.split("_")[1];

		String status = element("edm.formState", String.valueOf(rowNo)).getInnerText();
		if ("ACTIVE".equalsIgnoreCase(status))
		{
			logger.error("This edition already activated");
		}
		else
		{
			element("edm.changeState", String.valueOf(rowNo)).click();
			waitThat().timeout(1000);
		}

		status = element("edm.formState", String.valueOf(rowNo)).getInnerText();
		if ("ACTIVE".equalsIgnoreCase(status))
		{
			state = true;
			logger.error("Activate edition successfully");
		}

		closeEditionManage();
		return state;

	}

	/**
	 * Deactive editionby creation date
	 * 
	 * @param creationDate
	 * @return true-deactive succeed or false deactive failed
	 * @throws Exception
	 */
	public boolean deactivateForm(String creationDate) throws Exception
	{
		boolean state = false;
		String formPosition = findForm(creationDate);
		if ("1_0".equals(formPosition))
		{
			logger.error("The form with creation date=" + creationDate + " does not exist!");
		}
		String rowNo = formPosition.split("_")[1];
		String status = element("edm.formState", String.valueOf(rowNo)).getInnerText();
		if ("DORMANT".equalsIgnoreCase(status))
		{
			logger.error("This edition is dormant");
		}
		else
		{
			element("edm.changeState", String.valueOf(rowNo)).click();
			waitThat().timeout(1000);
		}

		status = element("edm.formState", String.valueOf(rowNo)).getInnerText();
		if ("DORMANT".equalsIgnoreCase(status))
		{
			state = true;
			logger.error("Deactivate edition successfully");
		}
		closeEditionManage();
		return state;
	}

	/**
	 * open edition by creation date
	 * 
	 * @param creationDate
	 * @return FormInstancePage
	 * @throws Exception
	 */
	public FormInstancePage openForm(String creationDate) throws Exception
	{
		String formPosition = findForm(creationDate);
		if ("1_0".equals(formPosition))
		{
			logger.error("The form with creation date=" + creationDate + " does not exist!");
		}
		String rowNo = formPosition.split("_")[1];
		int row = Integer.parseInt(rowNo) - 1;
		element("edm.formLink", String.valueOf(row)).click();
		waitStatusDlg();

		return new FormInstancePage(getWebDriverWrapper());
	}

	/**
	 * delete edition by creation date
	 * 
	 * @param creationDate
	 * @throws Exception
	 */
	public void deleteEdition(String creationDate) throws Exception
	{
		String formPosition = findForm(creationDate);
		if ("1_0".equals(formPosition))
		{
			logger.error("The form with creation date=" + creationDate + " does not exist!");
		}
		String rowNo = formPosition.split("_")[1];
		int row = Integer.parseInt(rowNo);
		element("edm.delEdition", String.valueOf(row)).click();
		waitStatusDlg();
		element("edm.delEditionConBtn").click();
		waitStatusDlg();
		element("edm.delEditionComment").type("Delete by automation");
		element("edm.deleteBtn").click();
		waitStatusDlg();
		waitThat("edm.message").toBeInvisible();
		waitStatusDlg();
		closeEditionManage();
	}

	/**
	 * delete edition by row index
	 * 
	 * @param index
	 * @param comment
	 * @throws Exception
	 */
	public void deleteEdition(int index, String comment) throws Exception
	{
		element("edm.delEdition", String.valueOf(index - 1)).click();
		element("edm.edition.confirm").click();
		waitStatusDlg();
		element("edm.edition.comment").input(comment);
		element("edm.edition.comment.confirm").click();
		waitStatusDlg();
		waitThat("edm.message").toBeInvisible();
	}

	/**
	 * delete edition by edition number
	 * 
	 * @param editionNo
	 * @param comment
	 * @throws Exception
	 */
	public void deleteEdition(String editionNo, String comment) throws Exception
	{
		logger.info("Begin delete edition " + editionNo);
		element("edm.edition.delete", editionNo).click();
		element("edm.edition.confirm").click();
		waitStatusDlg();
		element("edm.edition.comment").input(comment);
		element("edm.edition.comment.confirm").click();
		waitStatusDlg();
		waitThat("edm.message").toBeInvisible();
	}

	/**
	 * get edition amount
	 * 
	 * @return edition amount(int)
	 * @throws Exception
	 */
	public int getEditionAmt() throws Exception
	{
		int editionAmt = (int) element("edm.editionMagForm").getRowCount();
		while (!element("edm.nextPage").getAttribute("class").contains("ui-state-disabled"))
		{
			element("edm.nextPage").click();
			waitStatusDlg();
			int cutAmt = (int) element("edm.editionMagForm").getRowCount();
			editionAmt = editionAmt + cutAmt;
		}
		if (!element("edm.firtPage").getAttribute("class").contains("ui-state-disabled"))
		{
			element("edm.firtPage").click();
			waitStatusDlg();
		}
		closeEditionManage();
		logger.info("There are " + editionAmt + " editions");
		return editionAmt;
	}

	/**
	 * get edition detail info
	 * 
	 * @param rowIndex
	 * @return detail info(List)
	 * @throws Exception
	 */
	public List<String> getEditionInfo(int rowIndex) throws Exception
	{
		int index = rowIndex - 1;
		List<String> editionInfo = new ArrayList<String>();
		editionInfo.add(element("edm.editNO", String.valueOf(index)).getInnerText());
		editionInfo.add(element("edm.createDt", String.valueOf(index)).getInnerText());
		editionInfo.add(element("edm.modifyDt", String.valueOf(index)).getInnerText());
		editionInfo.add(element("edm.modifyOW", String.valueOf(index)).getInnerText());
		editionInfo.add(element("edm.reportStatus", String.valueOf(index)).getInnerText());
		editionInfo.add(element("edm.formState", String.valueOf(index)).getInnerText());

		closeEditionManage();
		return editionInfo;
	}

	/**
	 * open form
	 * 
	 * @param rowIndex
	 * @return true or false
	 * @throws Exception
	 */
	public boolean openForm(int rowIndex) throws Exception
	{
		boolean rst = true;
		int amt = (int) element("edm.editionMagForm").getRowCount();
		if (amt >= rowIndex)
		{
			try
			{
				int index = rowIndex - 1;
				List<String> editionInfo = getEditionInfo(rowIndex);
				element("edm.formLink", String.valueOf(index)).click();
				waitStatusDlg();

				FormInstancePage formInstancePage = new FormInstancePage(getWebDriverWrapper());

				if (!element("edm.curEdition").getInnerText().equals((editionInfo.get(1) + " " + editionInfo.get(4))))
					rst = false;

				formInstancePage.closeFormInstance();
			}
			catch (Exception e)
			{
				logger.warn(e.getMessage());
				e.printStackTrace();
				rst = false;
			}
		}
		return rst;
	}

	/**
	 * get first dormant edition
	 * 
	 * @return edition info
	 * @throws Exception
	 */
	public String getFirstDormantEdition() throws Exception
	{
		String editionInfo = null;
		int amt = (int) element("edm.editionMagForm").getRowCount();
		for (int i = 1; i <= amt; i++)
		{
			if ("DORMANT".equalsIgnoreCase(element("edm.formState", String.valueOf(i)).getInnerText()))
			{
				String editionNo = element("edm.formLink", String.valueOf(i - 1)).getInnerText();
				String creationDt = element("edm.createDt", String.valueOf(i)).getInnerText();
				editionInfo = creationDt + "#" + editionNo;
				break;
			}
		}
		closeEditionManage();
		logger.info("First dormant edition is:" + editionInfo);
		return editionInfo;
	}

	/**
	 * get first active edition
	 * 
	 * @return edition info
	 * @throws Exception
	 */
	public String getFirstActiveEdition() throws Exception
	{
		String editionInfo = null;
		int amt = (int) element("edm.editionMagForm").getRowCount();
		for (int i = 1; i <= amt; i++)
		{
			if ("ACTIVE".equalsIgnoreCase(element("edm.formState", String.valueOf(i)).getInnerText()))
			{
				String editionNo = element("edm.formLink", String.valueOf(i - 1)).getInnerText();
				String creationDt = element("edm.createDt", String.valueOf(i)).getInnerText();
				editionInfo = creationDt + "#" + editionNo;
				break;
			}
		}
		closeEditionManage();
		logger.info("Active edition is:" + editionInfo);
		return editionInfo;
	}

	/**
	 * verify status is editable
	 * 
	 * @param rowIndex
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isStatusEditable(int rowIndex) throws Exception
	{
		boolean rst = false;
		String randomString = getRandomString(3);
		String id = element("edm.editStatus", String.valueOf(rowIndex - 1)).getAttribute("id");
		executeScript("document.getElementById('" + id + "').click();");
		waitStatusDlg();
		element("edm.editStatus", String.valueOf(rowIndex - 1)).type(randomString);
		waitStatusDlg();
		element("edm.modifyOW", String.valueOf(rowIndex)).click();
		waitStatusDlg();
		String curStatus = element("edm.reportStatus", String.valueOf(rowIndex)).getInnerText();
		if (randomString.equalsIgnoreCase(curStatus))
			rst = true;

		closeEditionManage();
		return rst;
	}

	/**
	 * close edition manager window
	 * 
	 * @return ListPage
	 * @throws Exception
	 */
	public void closeEditionManage() throws Exception
	{
		logger.info("Close edition manager page");
		element("edm.closeEdition").click();
		waitStatusDlg();
		Thread.sleep(1000);
	}

	/**
	 * get edition state by edition No
	 * 
	 * @param editionNo
	 * @return edition state
	 * @throws Exception
	 */
	public String getEditionState(String editionNo) throws Exception
	{
		return element("edm.edition.state", editionNo).getInnerText();
	}

	/**
	 * verify if Delete Edition Enabled
	 * 
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isDeleteEditionEnabled() throws Exception
	{
		if (element("edm.delEdition", "0").isDisplayed())
			return true;
		else
			return false;
	}

	/**
	 * verify if Change Edition State Enabled
	 * 
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isChangeEditionStateEnabled() throws Exception
	{
		if (element("edm.changeState", "0").isDisplayed())
			return true;
		else
			return false;
	}
}
