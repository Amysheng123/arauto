package com.lombardrisk.pages;

import java.util.ArrayList;
import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;
import org.yiwan.webcore.web.IWebDriverWrapper.IWebElementWrapper;

/**
 * 
 * Create by zhijun dai on 7/5/2016
 * 
 */
public class PhysicalLocationPage extends AbstractPage
{

	public PhysicalLocationPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * Get the each column value of exist physical location
	 * 
	 * @throws Exception
	 */
	public String[] getColumnValueOfPhysicalLocation() throws Exception
	{
		List<IWebElementWrapper> elements = element("pl.table").getAllMatchedElements();
		String[] columeValue = new String[elements.size()];
		for (int i = 0; i < elements.size(); i++)
		{
			columeValue[i] = elements.get(i).getInnerText().trim();
		}
		return columeValue;
	}

	/**
	 * Get the edit button element of location
	 * 
	 * @throws Exception
	 */
	public IWebElementWrapper getEditButton(String entityName) throws Exception
	{
		return element("pl.table.editBtn", entityName);
	}

	/**
	 * Get the delete button element of location
	 * 
	 * @throws Exception
	 */
	public IWebElementWrapper getDeleteButton(String entityName) throws Exception
	{
		return element("pl.table.deleteBtn", entityName);
	}

	/**
	 * Delete a exist location
	 * 
	 * @throws Exception
	 */
	public void deleteExistLocation(String entityName) throws Exception
	{
		logger.info("Begin delete location for entity " + entityName);
		element("pl.table.deleteBtn", entityName).click();
		waitStatusDlg();
		element("pl.deleteConfirm").click();
		waitStatusDlg();
	}

	/**
	 * Add a network file location
	 * 
	 * @throws Exception
	 */
	public void addFileLocation(String entityName, String networkLocation, String userPath) throws Exception
	{
		logger.info("Begin add location for entity " + entityName);
		element("pl.addBtn").click();
		waitStatusDlg();
		element("pl.entityField").selectByVisibleText(entityName);
		Thread.sleep(200);
		element("pl.phyPath").type(networkLocation);
		Thread.sleep(200);
		if (userPath != null)
			element("pl.userPath").type(userPath);
		element("pl.confirmBtn").click();
		waitStatusDlg();
	}

	/**
	 * Is the delete button exist or not
	 */
	public boolean isDeleteButtonExist(String entityName) throws Exception
	{
		return element("pl.table.deleteBtn", entityName).isDisplayed();
	}

	/**
	 * Navigate to the list page
	 */
	public ListPage backToDashboard() throws Exception
	{
		element("pl.dashboardBtn").click();
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * Edit the exist location
	 */
	public PhysicalLocationPage editExistLocation(String initEntityName, String modifiedEntityName, String networkLocation, String userPath) throws Exception
	{
		logger.info("Begin edit location for entity " + initEntityName);
		element("pl.table.editBtn", initEntityName).click();
		waitStatusDlg();
		if (modifiedEntityName != null)
		{
			element("pl.entityField").selectByVisibleText(modifiedEntityName);
			Thread.sleep(200);
		}
		if (networkLocation != null)
		{
			element("pl.phyPath").input(networkLocation);
			Thread.sleep(200);
		}
		if (userPath != null)
		{
			element("pl.userPath").input(userPath);
			Thread.sleep(200);
		}
		element("pl.confirmBtn").click();
		waitStatusDlg();
		return new PhysicalLocationPage(getWebDriverWrapper());
	}

	/**
	 * Get the message on the top
	 */
	public String getMessage() throws Exception
	{
		return element("pl.message").getInnerText().trim();
	}

	/**
	 * select type(Submission,DQLog,Alias)
	 * 
	 * @param type
	 * @throws Exception
	 */
	private void selectType(String type) throws Exception
	{
		if (type.equalsIgnoreCase("Submission"))
			element("pl.type_submission").click();
		else if (type.equalsIgnoreCase("DQLog"))
			element("pl.type_DQLog").click();
		else if (type.equalsIgnoreCase("Alias"))
			element("pl.type_standardAlias").click();
		waitStatusDlg();
	}

	/**
	 * add physical location
	 * 
	 * @param type
	 * @param entity
	 * @param networkLocation
	 * @param userPath
	 * @throws Exception
	 */
	public String addLocation(String type, String entity, String networkLocation, String userPath) throws Exception
	{

		element("pl.addBtn").click();
		waitStatusDlg();
		selectType(type);
		if (type.equalsIgnoreCase("Submission"))
		{
			logger.info("Begin add location for Entity:" + entity);
			if (element("pl.entityField").isDisplayed())
				element("pl.entityField").selectByVisibleText(entity);
			else if (element("pl.entityDropList").isDisplayed())
				element("pl.entityDropList").selectByVisibleText(entity);
			Thread.sleep(200);
			element("pl.phyPath").type(networkLocation);
			if (userPath != null)
				element("pl.userPath").type(userPath);
			Thread.sleep(200);
		}
		else if (type.equalsIgnoreCase("Alias"))
		{
			logger.info("Begin add location for :" + networkLocation);
			element("pl.entityField").input(networkLocation);
			Thread.sleep(200);
			element("pl.phyPath").input(userPath);
			Thread.sleep(200);
		}

		element("pl.confirmBtn").click();
		waitStatusDlg();
		String message = element("pl.message").getInnerText();
		waitThat("pl.message").toBeInvisible();
		return message;
	}

	/**
	 * get all added physical locations
	 * 
	 * @return all physical locations
	 * @throws Exception
	 */
	public List<String> getPhysicalLocation() throws Exception
	{
		List<String> locations = new ArrayList<>();
		int nums = element("pl.tableRow").getNumberOfMatches();
		for (int i = 1; i <= nums; i++)
		{
			locations.add(element("pl.physicalLocationName", String.valueOf(i)).getInnerText());
		}
		return locations;
	}
}
