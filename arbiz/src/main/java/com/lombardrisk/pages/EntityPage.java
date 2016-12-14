package com.lombardrisk.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Create by Leo Tu on 12/1/2015
 */

public class EntityPage extends AbstractImportPage
{

	/**
	 * 
	 * @param webDriverWrapper
	 */
	public EntityPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);

	}

	/**
	 * add entity to root
	 * 
	 * @param name
	 * @param code
	 * @param description
	 * @param save
	 *            //boolean if save
	 * @return message
	 * @throws Exception
	 */
	public String addRootEntity(String name, String code, String description, boolean save) throws Exception
	{
		logger.info("Begin add entity[" + name + "] to root");
		unselectHighlightEntity();
		element("emp.addEntity").click();
		waitStatusDlg();
		element("emp.edit.name").input(name);
		element("emp.edit.code").input(code);
		element("emp.edit.desc").input(description);
		if (save)
			element("emp.addEntitySave").click();
		else
			element("emp.addEntityCancel").click();

		waitThat("emp.messageTitle").toBeVisible();
		String msg = element("emp.promptMsg").getInnerText();
		waitThat("emp.messageTitle").toBeInvisible();
		return msg;
	}

	/**
	 * add entity
	 * 
	 * @param parentEntity
	 * @param name
	 * @param code
	 * @param description
	 * @param save
	 *            //boolean is save
	 * @return add entity message
	 * @throws Exception
	 */
	public String addEntity(String parentEntity, String name, String code, String description, boolean save) throws Exception
	{
		logger.info("Begin add entity[" + name + "] to parent[" + parentEntity + "]");
		if (!isEntityHighlight(parentEntity))
		{
			element("emp.entityLabel", parentEntity).click();
			waitStatusDlg();
		}
		element("emp.addEntity").click();
		waitStatusDlg();
		element("emp.edit.name").type(name);
		waitThat().timeout(200);
		element("emp.edit.code").type(code);
		waitThat().timeout(200);
		element("emp.edit.desc").type(description);
		if (save)
		{
			element("emp.addEntitySave").click();
			logger.info("Click save button");
		}
		else
		{
			element("emp.addEntityCancel").click();
			logger.info("Click cancel button");
		}

		waitThat("emp.messageTitle").toBeVisible();
		String message = element("emp.promptMsg").getInnerText();
		waitThat("emp.messageTitle").toBeInvisible();
		logger.info(message);
		closeEntityEditPage();
		return message;
	}

	/**
	 * edit entity
	 * 
	 * @param originalEntity
	 * @param newName
	 * @param newCode
	 * @param newDescription
	 * @throws Exception
	 */
	public String editEntity(String originalEntity, String newName, String newCode, String newDescription) throws Exception
	{
		logger.info("Begin update entity");
		element("emp.entityImg", originalEntity).click();
		waitStatusDlg();
		if (newName != null)
			element("emp.edit.name").input(newName);
		if (newCode != null)
			element("emp.edit.code").input(newCode);
		if (newDescription != null)
			element("emp.edit.desc").input(newDescription);
		element("emp.edit.save").click();
		waitThat("emp.messageTitle").toBeVisible();
		String msg = element("emp.message").getInnerText();
		waitThat("emp.messageTitle").toBeInvisible();
		closeEntityEditPage();
		return msg;
	}

	/**
	 * delete entity
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void deleteEntity(String name) throws Exception
	{
		logger.info("Begin delete Entity: " + name);
		element("emp.entityImg", name).click();
		waitStatusDlg();
		element("emp.deleteEntity").click();
		waitStatusDlg();
		element("emp.deleteConfirm").click();
		// waitThat("emp.messageTitle").toBeVisible();
		// waitThat("emp.messageTitle").toBeInvisible();
		waitStatusDlg();
		closeEntityEditPage();
	}

	/**
	 * assign return to entity
	 * 
	 * @param entityName
	 * @param ProductPrefix
	 * @param returnNames
	 * @throws Exception
	 */
	public void assignReturnToEntity(String entityName, String ProductPrefix, String... returnNames) throws Exception
	{
		logger.info("Begin  assign Return To Entity");
		openAssignReturnPage(entityName);
		String id = element("emp.ProductTab", ProductPrefix).getAttribute("id") + "_data";

		for (String name : returnNames)
		{
			String[] list = new String[]
			{ id, name };
			element("emp.edit.RetCheckBox2", list).click();
			waitStatusDlg();
		}
		element("emp.edit.assSave").click();
		Thread.sleep(500);
		waitThat("emp.messageTitle").toBeInvisible();
	}

	/**
	 * assign return with user group and privilege to entity
	 * 
	 * @param entityName
	 * @param ProductPrefix
	 * @param returnNames
	 * @param userGPNames
	 * @param permissionNames
	 * @throws Exception
	 */
	public void assignReturnToEntity(String entityName, String ProductPrefix, String[] returnNames, String[] userGPNames, String[] permissionNames) throws Exception
	{
		logger.info("Begin  assign Return To Entity");
		openAssignReturnPage(entityName);
		String id = element("emp.ProductTab", ProductPrefix).getAttribute("id") + "_data";
		for (String name : returnNames)
		{
			String[] list = new String[]
			{ id, name };
			if (!element("emp.edit.RetCheckBox2", list).getAttribute("class").contains("ui-state-active"))
			{
				element("emp.edit.RetCheckBox2", list).click();
				waitStatusDlg();
			}
			addUserGP(name, userGPNames, true, permissionNames);
		}
		element("emp.edit.assSave").click();
		waitStatusDlg();
		Thread.sleep(500);
		waitThat("emp.messageTitle").toBeInvisible();
	}

	/**
	 * assign all return to entity
	 * 
	 * @param Regulator
	 * @param EntityName
	 * @param save
	 * @throws Exception
	 */
	public void assignAllReturnToEntity(String Regulator, String EntityName, boolean save) throws Exception
	{
		openAssignReturnPage(EntityName);
		String id = element("emp.ProductTab", Regulator).getAttribute("id");
		element("emp.edit.assALL", id).click();
		waitStatusDlg();
		if (save)
		{
			element("emp.edit.assSave").click();
			waitThat("emp.messageTitle").toBeVisible();
			waitThat("emp.messageTitle").toBeInvisible();
		}
		else
		{
			element("emp.cancelAssignReturn").click();
			waitThat().timeout(1000);
		}
	}

	/**
	 * remove assigned return from entity
	 * 
	 * @param entityName
	 * @param ProductPrefxi
	 * @param returnNames
	 * @throws Exception
	 */
	public void removeReturnFromEntity(String entityName, String ProductPrefxi, String... returnNames) throws Exception
	{
		logger.info("Begin  remove Return from Entity");
		openAssignReturnPage(entityName);
		String id = element("emp.ProductTab", ProductPrefxi).getAttribute("id") + "_data";

		for (String name : returnNames)
		{
			String[] list = new String[]
			{ id, name };
			element("emp.edit.RetCheckBox2", list).check(true);
			waitStatusDlg();
		}
		element("emp.edit.assSave").click();
		Thread.sleep(500);
		waitThat("emp.messageTitle").toBeInvisible();
	}

	/**
	 * get message when assign return to entity
	 * 
	 * @param entityName
	 * @param Product
	 * @param returnNames
	 * @return message
	 * @throws Exception
	 */
	public String getAssignReturnToEntityMessage(String entityName, String Product, String... returnNames) throws Exception
	{
		openAssignReturnPage(entityName);
		String id = element("emp.ProductTab", Product).getAttribute("id") + "_data";
		for (String name : returnNames)
		{
			String[] list = new String[]
			{ id, name };
			element("emp.edit.RetCheckBox2", list).check(true);
			waitStatusDlg();
		}
		element("emp.edit.assSave").click();
		waitStatusDlg();
		String msg = element("emp.promptMsg").getInnerText();
		waitThat("emp.promptMsg").toBeInvisible();
		return msg;
	}

	/**
	 * get message when remove return from entity
	 * 
	 * @param entityName
	 * @param Product
	 * @param returnNames
	 * @return message
	 * @throws Exception
	 */
	public String getRemoveReturnFromEntityMessage(String entityName, String Product, String... returnNames) throws Exception
	{
		openAssignReturnPage(entityName);
		String id = element("emp.ProductTab", Product).getAttribute("id") + "_data";
		for (String name : returnNames)
		{
			String[] list = new String[]
			{ id, name };
			element("emp.edit.RetCheckBox2", list).check(true);
			waitStatusDlg();
		}
		element("emp.edit.assSave").click();
		Thread.sleep(500);
		return element("emp.promptMsg").getInnerText();
	}

	/**
	 * verify if entity is highlight
	 * 
	 * @param entityName
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isEntityHighlight(String entityName) throws Exception
	{
		if (element("emp.entity", entityName).getAttribute("class").contains("highlightBorderGreen"))
			return true;
		else
			return false;
	}

	/**
	 * verify if entity could be selected
	 * 
	 * @param entityName
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isEntitySelectable(String entityName) throws Exception
	{
		if (element("emp.entity").getAttribute("class").contains("ui-tree-selectable"))
			return true;
		else
			return false;
	}

	/**
	 * get all existed entities
	 * 
	 * @return all entities(List)
	 * @throws Exception
	 */
	public List<String> getAllEntityName()
	{
		logger.info("Get all entities");
		try
		{
			return element("emp.allEntity").getAllInnerTexts();
		}
		catch (Exception e)
		{
			logger.warn("No Entity");
			return null;
		}
	}

	/**
	 * click highlight entity
	 * 
	 * @throws Exception
	 */
	public void unselectHighlightEntity() throws Exception
	{
		logger.info("unselect Highlight Entity");
		List<String> entityList = getAllEntityName();
		if (entityList != null)
		{
			for (String entityName : entityList)
			{
				if (element("emp.entity", entityName).getAttribute("class").contains("ui-state-highlight"))
				{
					element("emp.entity", entityName).click();
					break;
				}
			}

		}
	}

	/**
	 * open entity edit page
	 * 
	 * @param EntityName
	 * @throws Exception
	 */
	public void openEntityEditPage(String EntityName) throws Exception
	{
		waitThat("emp.entityImg", EntityName).toBeClickable();
		element("emp.entityImg", EntityName).click();
		waitStatusDlg();
		waitThat().timeout(500);
	}

	/**
	 * get parent entity
	 * 
	 * @param childEntity
	 * @return parentEntity
	 * @throws Exception
	 */
	public String getParentEntity(String childEntity) throws Exception
	{
		String id = element("emp.entityImg", childEntity).getAttribute("id");
		String index = id.split(":")[2];
		if (index.contains("_"))
		{
			id = id.replace(":" + index + ":", ":" + index.split("_")[0] + ":");
		}
		else
		{
			id = id.replace(":" + index + ":", ":");
		}

		return element("emp.EBI", id).getInnerText();
	}

	/**
	 * get all assigned Products
	 * 
	 * @return all regulators(List)
	 * @throws Exception
	 */
	public List<String> getAllAssignProducts() throws Exception
	{
		logger.info("Get all assigned products");
		return element("emp.assAllPro").getAllInnerTexts();
	}

	/**
	 * verify if regulator exist in assign return page
	 * 
	 * @return true/false
	 * @throws Exception
	 */
	public boolean isRegulatorExist(String entityName, String ProductPrefix) throws Exception
	{
		boolean rst = false;
		openAssignReturnPage(entityName);
		if (element("emp.ProductTab", ProductPrefix).isDisplayed())
			rst = true;
		closeEntityEditPage();
		return rst;
	}

	/**
	 * get all assigned user group
	 * 
	 * @return user groups(List)
	 * @throws Exception
	 */
	private List<String> getAllAssignUserGroup() throws Exception
	{
		logger.info("Get all assigned user groups");
		return element("emp.assAllUG").getAllInnerTexts();
	}

	/**
	 * expand all sub entities
	 * 
	 * @throws Exception
	 */
	public void expandAllSubItems() throws Exception
	{
		while (element("emp.expand").isDisplayed())
		{
			element("emp.expand").click();
		}
	}

	/**
	 * if entities if expanded
	 * 
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isExpandAllSubItems() throws Exception
	{
		if (element("emp.expand").isDisplayed())
			return false;
		else
			return true;
	}

	/**
	 * open assigen privilege page
	 * 
	 * @param ReturnName
	 * @throws Exception
	 */
	private void openAssignPrivPage(String ReturnName) throws Exception
	{
		element("emp.edit.AssPriv", ReturnName).click();
		waitStatusDlg();
	}

	/**
	 * if delete entity with child failed
	 * 
	 * @param Entity
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isDeleteEntityWithChildFailed(String Entity) throws Exception
	{
		boolean result = false;
		deleteEntity(Entity);
		long startTime = System.currentTimeMillis();
		long CurrentTime = System.currentTimeMillis();
		while ((CurrentTime - startTime) / 1000 < 6)
		{
			try
			{
				if ("Entity cannot be removed as it has Assigned Entities".equals(element("emp.promptMsg").getInnerText()))
				{
					result = true;
					break;
				}
			}
			catch (NoSuchElementException e)
			{
				logger.warn("Entity: " + Entity + " does not exist");
			}
			CurrentTime = System.currentTimeMillis();
		}
		return result;
	}

	/**
	 * if delete entity with form instance failed
	 * 
	 * @param Entity
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isDeleteEntityWithInstanceFailed(String Entity) throws Exception
	{
		boolean result = false;
		deleteEntity(Entity);
		long startTime = System.currentTimeMillis();
		long CurrentTime = System.currentTimeMillis();
		while ((CurrentTime - startTime) / 1000 < 6)
		{
			try
			{
				if (element("emp.promptMsg").isDisplayed())
				{
					result = true;
					break;
				}

			}
			catch (NoSuchElementException e)
			{
				logger.warn("Entity: " + Entity + " does not exist");
			}

			CurrentTime = System.currentTimeMillis();
		}

		return result;
	}

	/**
	 * if entity could be reused
	 * 
	 * @param parent
	 * @param name
	 * @param code
	 * @param desc
	 * @param type
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isReUsedEntity(String parent, String name, String code, String desc, String type) throws Exception
	{
		boolean result = true;
		String message = addEntity(parent, name, code, desc, true);

		if ("name".equalsIgnoreCase(type))
		{
			if (message.equals("Entity name " + name + " is already in use"))
			{
				result = false;
			}
		}
		else
		{
			if (message.equals("Entity code " + code + " is already in use"))
			{
				result = false;
			}
		}
		try
		{
			element("emp.addEntityCancel").click();
			waitThat().timeout(1000);
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
		}

		return result;
	}

	/**
	 * add privilege group to entity
	 * 
	 * @param group
	 * @param permissionNames
	 * @throws Exception
	 */
	private void addPrivilegeGroup(String group, String[] permissionNames) throws Exception
	{
		List<String> existedPG = getAssignedPrivilegeGroup(group);
		boolean click = true;
		for (String permissionName : permissionNames)
		{
			if (!existedPG.contains(permissionName))
			{
				if (click)
				{
					logger.info("Click Add Permission Group icon");
					String id = element("emp.PGHeader", group).getAttribute("id").replace("header", "content");
					element("emp.addPG", id).click();
					waitStatusDlg();
					click = false;
				}
				element("emp.PrivCheckobx", permissionName).click();
				waitStatusDlg();
			}
		}
		if (!click)
		{
			element("emp.addPGSave").click();
			waitThat("emp.messageTitle").toBeInvisible();
		}
	}

	/**
	 * add user group to entity
	 * 
	 * @param returnName
	 * @param userGPNames
	 * @param addPermission
	 * @param permissionNames
	 * @throws Exception
	 */
	private void addUserGP(String returnName, String[] userGPNames, boolean addPermission, String[] permissionNames) throws Exception
	{
		logger.info("Click Open[" + returnName + "] link");
		openAssignPrivPage(returnName);
		for (String UGP : userGPNames)
		{
			if (!element("emp.existUG", UGP).isDisplayed())
			{
				logger.info("Click Add user group icon");
				element("emp.addUG").click();
				waitStatusDlg();

				if (!element("emp.UGCheckBox", UGP).getAttribute("class").contains("ui-state-active"))
				{
					element("emp.UGCheckBox", UGP).click();
					waitStatusDlg();
				}
				element("emp.addUPSave").click();
				waitStatusDlg();
			}

			if (addPermission)
				addPrivilegeGroup(UGP, permissionNames);

		}
		Thread.sleep(1000);
		element("emp.confSave").click();
		waitStatusDlg();
		Thread.sleep(1000);
	}

	/**
	 * user logout
	 * 
	 * @return HomePage
	 * @throws Exception
	 */
	public HomePage logout() throws Exception
	{
		backToDashboard();
		element("emp.userMenu").click();
		waitStatusDlg();
		element("emp.logout").click();
		waitStatusDlg();
		return new HomePage(getWebDriverWrapper());
	}

	/**
	 * back to dashboard page
	 * 
	 * @return ListPage
	 * @throws Exception
	 */
	public ListPage backToDashboard() throws Exception
	{
		logger.info("Back to listPage");
		closeEntityEditPage();
		if (element("emp.addEntityCancel").isDisplayed())
		{
			element("emp.addEntityCancel").click();
			waitStatusDlg();
		}
		waitStatusDlg();
		element("emp.dashboard").click();
		waitStatusDlg();
		waitThat().timeout(500);
		return new ListPage(getWebDriverWrapper());

	}

	/**
	 * if action=true, entity is used for reporting
	 * 
	 * @param entity
	 * @param action
	 * @throws Exception
	 */
	public void entityUsedForReporting(String entity, boolean action) throws Exception
	{
		openEntityEditPage(entity);
		if (action)
		{
			if (!element("emp.edit.slide").getAttribute("class").contains("slideBarSetTrue"))
			{
				element("emp.edit.slideBar").click();
				waitStatusDlg();
				element("emp.edit.save").click();
				waitStatusDlg();
			}
			else
			{
				element("emp.edit.cancel").click();
				waitStatusDlg();
			}
		}
		else
		{
			if (element("emp.edit.slide").getAttribute("class").contains("slideBarSetTrue"))
			{
				element("emp.edit.slideBar").click();
				waitStatusDlg();
				element("emp.edit.save").click();
				waitStatusDlg();
			}
			else
			{
				element("emp.edit.cancel").click();
				waitStatusDlg();
			}
		}

	}

	/**
	 * get entity default status
	 * 
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isDefaultStatus() throws Exception
	{
		if (element("emp.SHS").getAttribute("class").contains("slideBarSetFalse"))
		{
			return true;
		}
		else
			return false;

	}

	/**
	 * show deleted entities
	 * 
	 * @throws Exception
	 */
	public void showDeletedEntities() throws Exception
	{
		logger.info("Show deleted entities");
		if (!element("emp.SHS").getAttribute("class").contains("slideBarSetTrue"))
		{
			element("emp.SH").click();
			waitStatusDlg();
		}

	}

	/**
	 * hide deleted entities
	 * 
	 * @throws Exception
	 */
	public void hideDeletedEntities() throws Exception
	{
		logger.info("Hide deleted entities");
		if (element("emp.SHS").getAttribute("class").contains("slideBarSetTrue"))
		{
			element("emp.SH").click();
			waitStatusDlg();
		}

	}

	/**
	 * restore deleted entity
	 * 
	 * @param entityName
	 * @throws Exception
	 */
	public void restoreDeleteEntity(String entityName) throws Exception
	{
		logger.info("Begin retore deleted entity");
		showDeletedEntities();
		openEntityEditPage(entityName);
		element("emp.restoreEntity").click();
		waitStatusDlg();
		element("emp.restoreconfirm").click();
		waitStatusDlg();
		hideDeletedEntities();
	}

	/**
	 * restore deleted entity
	 * 
	 * @param name
	 * @param save
	 * @throws Exception
	 */
	public void restoreDeleteEntity(String name, boolean save) throws Exception
	{
		logger.info("Begin retore deleted entity");
		showDeletedEntities();
		openEntityEditPage(name);
		element("emp.restoreEntity").click();
		waitThat().timeout(1000);
		if (save)
		{
			logger.info("Click OK button");
			element("emp.restoreconfirm").click();
		}
		else
		{
			logger.info("Click cancel button");
			element("emp.cancelRestore").click();
			waitStatusDlg();
			element("emp.closeEditEntityForm").click();
		}
		waitStatusDlg();
	}

	/**
	 * get entity status
	 * 
	 * @param name
	 * @return Deleted or Active
	 * @throws Exception
	 */
	public String getEntityStatus(String name) throws Exception
	{
		logger.info("Get entity status");
		showDeletedEntities();
		if (element("emp.entityLabel", name).getAttribute("class").contains("greyTreeNodeClass"))
		{
			logger.info("Entity[" + name + "] is deleted");
			return "Deleted";
		}
		else
		{
			logger.info("Entity[" + name + "] is active");
			return "Active";
		}
	}

	/**
	 * get assigned returns from entity and regulator
	 * 
	 * @param EntityName
	 * @param Product
	 * @return all assigned returns(List)
	 * @throws Exception
	 */
	public List<String> getAssignedReturns(String EntityName, String Product) throws Exception
	{
		List<String> returns = new ArrayList<String>();
		openAssignReturnPage(EntityName);
		String id = element("emp.ProductTab", Product).getAttribute("id") + "_data";
		int amt = (int) element("emp.returnTable", id).getRowCount();
		for (int i = 1; i <= amt; i++)
		{
			if (element("emp.edit.RetChekStat", id, String.valueOf(i)).getAttribute("class").contains("ui-state-active"))
			{
				returns.add(element("emp.edit.RetName", id, String.valueOf(i)).getInnerText());
			}
		}
		waitThat().timeout(500);
		closeEntityEditPage();
		logger.info("There are " + returns.size() + "  returns");
		return returns;
	}

	/**
	 * get all assigned privilegeGroups
	 * 
	 * @param EntityName
	 * @param returnName
	 * @param userGroup
	 * @return privilegeGroups(List)
	 * @throws Exception
	 */
	public List<String> getAssignedprivilegeGroups(String EntityName, String returnName, String userGroup) throws Exception
	{
		List<String> PGs = new ArrayList<String>();
		openAssignReturnPage(EntityName);
		openAssignPrivPage(returnName);

		for (String ug : getAllAssignUserGroup())
		{
			if (ug.equalsIgnoreCase(userGroup))
			{
				PGs = getAssignedPrivilegeGroup(userGroup);
				break;
			}
		}

		element("emp.editReturnCancel").click();
		waitStatusDlg();
		closeEntityEditPage();
		return PGs;
	}

	/**
	 * get assigned privilege groups
	 * 
	 * @param userGroup
	 * @return
	 * @throws Exception
	 */
	public List<String> getAssignedPrivilegeGroup(String userGroup) throws Exception
	{
		List<String> PGs = new ArrayList<>();
		String id = element("emp.PGHeader", userGroup).getAttribute("id").replace("header", "content");
		int amt = (int) element("emp.pgTab", id).getRowCount();
		String[] list = new String[]
		{ id, "", "" };
		for (int r = 1; r <= amt; r++)
		{
			list[1] = String.valueOf(r);
			for (int c = 1; c <= 3; c++)
			{
				list[2] = String.valueOf(c);
				if (element("emp.getPriv", list).isDisplayed())
					PGs.add(element("emp.getPriv", list).getInnerText());
			}
		}
		return PGs;
	}

	/**
	 * if add entity button could be clicked
	 * 
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isAddEntityBtnClick() throws Exception
	{
		boolean clickAble = false;
		if (element("emp.addEntityBtnStat").isDisplayed())
		{
			if (!element("emp.addEntityBtnStat").getAttribute("class").contains("ui-state-disabled"))
				clickAble = true;
		}
		return clickAble;
	}

	/**
	 * if deleted entity button could be clicked
	 * 
	 * @param Entity
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isDeleteEntityBtnClick(String Entity) throws Exception
	{
		boolean elementClick = false;
		openEntityEditPage(Entity);

		if (element("emp.deleteEntity").isDisplayed())
		{
			if (!element("emp.deleteEntity").getAttribute("class").contains("ui-state-disabled"))
			{
				elementClick = true;
			}
		}

		closeEntityEditPage();
		return elementClick;
	}

	/**
	 * if entity could be edited
	 * 
	 * @param Entity
	 * @return true or false
	 * @throws Exception
	 */
	public boolean isEditEntity(String Entity) throws Exception
	{
		boolean rst;
		try
		{
			openEntityEditPage(Entity);
			rst = element("emp.entityEditForm").isDisplayed();

		}
		catch (NoSuchElementException e)
		{
			logger.warn(e.getMessage());
			rst = false;
		}
		closeEntityEditPage();
		return rst;
	}

	/**
	 * verify if entity name is correct in deleted entity
	 * 
	 * @param Entity
	 * @return true or false
	 * @throws Exception
	 */
	public boolean verifyDeleteEntityName(String Entity) throws Exception
	{
		if (element("emp.edit.DelEntityName").getInnerText().equals(Entity))
			return true;
		else
			return false;
	}

	/**
	 * get assigned returns in deleted entity
	 * 
	 * @param Entity
	 * @param Regulator
	 * @return returnsNames(List)
	 * @throws Exception
	 */
	public List<String> getAssignedReturnsListInDeleteEntity(String Entity, String Regulator) throws Exception
	{
		List<String> returnList = new ArrayList<String>();
		for (String product : getAllAssignProducts())
		{
			if (product.equalsIgnoreCase(Regulator))
			{
				String id = element("emp.assPro", product).getAttribute("id") + "_data";
				int amt = (int) element("emp.tabName2", id).getRowCount();
				String[] list = new String[]
				{ id, "1" };
				for (int i = 1; i <= amt; i++)
				{
					list[1] = String.valueOf(i);
					returnList.add(element("emp.assRetName", list).getInnerText());
				}
			}
		}

		return returnList;
	}

	/**
	 * close entity edit page
	 * 
	 * @throws Exception
	 */
	public void closeEntityEditPage() throws Exception
	{
		try
		{
			if (element("emp.closeEditEntityForm").isDisplayed())
			{
				logger.info("Close entity manage panel");
				element("emp.closeEditEntityForm").click();
				waitStatusDlg();
			}
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
		}
	}

	/**
	 * get entity info(entity name,code,desc,parent, if used for reporting)
	 * 
	 * @param entityName
	 * @return entity info(List)
	 * @throws Exception
	 */
	public List<String> getEntityInfo(String entityName) throws Exception
	{
		List<String> entityInfo = new ArrayList<String>();
		openEntityEditPage(entityName);
		entityInfo.add(element("emp.edit.name").getInnerText());
		entityInfo.add(element("emp.edit.code").getInnerText());
		entityInfo.add(element("emp.edit.desc").getInnerText());

		String parent = getParentEntity(entityName);
		if (parent == null)
			entityInfo.add("");
		else
			entityInfo.add(parent);
		try
		{
			if (!"checked".equals(element("emp.edit.slide").getAttribute("checked")))
			{
				entityInfo.add("Y");
			}
			else
				entityInfo.add("N");
		}
		catch (Exception e)
		{
			entityInfo.add("N");
		}
		closeEntityEditPage();
		return entityInfo;
	}

	/**
	 * if import button is enabled
	 * 
	 * @return true/false
	 * @throws Exception
	 */
	public boolean isImportEnabled() throws Exception
	{
		if ("false".equalsIgnoreCase(element("emp.import").getAttribute("aria-disabled")))
			return true;
		else
			return false;
	}

	/**
	 * if export button is enabled
	 * 
	 * @return true/false
	 * @throws Exception
	 */
	public boolean isExportEnabled() throws Exception
	{
		if ("false".equalsIgnoreCase(element("emp.export").getAttribute("aria-disabled")))
			return true;
		else
			return false;
	}

	/**
	 * export access settings
	 * 
	 * @return exported file
	 * @throws Exception
	 */
	public String exportAcessSettings() throws Exception
	{
		TestCaseManager.getTestCase().startTransaction("");
		TestCaseManager.getTestCase().setPrepareToDownload(true);
		element("emp.export").click();
		TestCaseManager.getTestCase().stopTransaction();
		String exportedFile = System.getProperty("user.dir") + "/" + TestCaseManager.getTestCase().getDownloadFile();
		String oldName = new File(exportedFile).getName();
		String path = new File(exportedFile).getAbsolutePath().replace(oldName, "");
		String fileName = TestCaseManager.getTestCase().getDefaultDownloadFileName();
		String file;
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
	 * import access setting and export log
	 * 
	 * @param importFile
	 * @return exported log file
	 * @throws Exception
	 */
	public String importAccessSettings(String importFile) throws Exception
	{
		logger.info("Begin import adjustment");
		logger.info("Import file is :" + importFile);
		element("emp.import").click();
		waitStatusDlg();
		setImportFile(new File(importFile), "importFileForm");
		TestCaseManager.getTestCase().startTransaction("");
		TestCaseManager.getTestCase().setPrepareToDownload(true);
		element("emp.downloadLog").click();
		waitThat().timeout(10000);
		element("emp.importBtn").click();
		TestCaseManager.getTestCase().stopTransaction();
		String exportedFile = System.getProperty("user.dir") + "/" + TestCaseManager.getTestCase().getDownloadFile();
		String oldName = new File(exportedFile).getName();
		String path = new File(exportedFile).getAbsolutePath().replace(oldName, "");
		String fileName = TestCaseManager.getTestCase().getDefaultDownloadFileName();
		String file;
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
	 * open assign return page
	 * 
	 * @param EntityName
	 * @throws Exception
	 */
	private void openAssignReturnPage(String EntityName) throws Exception
	{
		openEntityEditPage(EntityName);
		element("emp.edit.assRet").click();
		waitStatusDlg();
		waitThat().timeout(1000);
	}

	/**
	 * update consolidation value in variable page
	 * 
	 * @param EntityName
	 * @param RegulatorPrefix
	 * @param value
	 * @throws Exception
	 */
	public void updateVariable_consolidation(String EntityName, String RegulatorPrefix, String value) throws Exception
	{
		openEntityEditPage(EntityName);
		element("emp.edit.assVar").click();
		waitStatusDlg();
		String id = element("emp.regulator.table", RegulatorPrefix).getAttribute("id");
		element("emp.regulator.consolidation", id).clickByJavaScript();
		waitStatusDlg();
		element("emp.regulator.consolidation", id).selectByVisibleText(value);
		waitStatusDlg();
		element("emp.edit.SaveVar").click();
		waitStatusDlg();
	}

	/**
	 * get form's active date and deactive date
	 * 
	 * @param EntityName
	 * @param RegulatorPrefix
	 * @param Form
	 * @return form's active date and deactive date(List)
	 * @throws Exception
	 */
	public List<String> getActiveAndDeactiveDate(String EntityName, String RegulatorPrefix, String Form) throws Exception
	{
		List<String> date = new ArrayList<>();
		openAssignReturnPage(EntityName);
		String id = element("emp.ProductTab", RegulatorPrefix).getAttribute("id") + "_data";
		String[] list = new String[]
		{ id, Form };
		date.add(element("emp.edit.activeDate", list).getInnerText());
		date.add(element("emp.edit.deactiveDate", list).getInnerText());
		element("emp.edit.assCancel").click();
		waitStatusDlg();

		return date;
	}

	/**
	 * enter PreferencePage
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
	 * open openFormVariablePage
	 * 
	 * @param Entity
	 * @throws Exception
	 */
	public void openFormVariablePage(String Entity) throws Exception
	{
		openEntityEditPage(Entity);
		element("emp.formVariable").click();
		waitStatusDlg();
	}

	/**
	 * get variable label by entity and RegulatorPrefix
	 * 
	 * @param Entity
	 * @param RegulatorPrefix
	 * @return all variables(List)
	 * @throws Exception
	 */
	public List<String> getVarialbe(String Entity, String RegulatorPrefix) throws Exception
	{
		List<String> varis = new ArrayList<>();
		openFormVariablePage(Entity);
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		int nums = (int) element("emp.Variable.Reg.Table", id).getRowCount();
		for (int i = 1; i <= nums; i++)
		{
			String[] list =
			{ id, String.valueOf(i) };
			varis.add(element("emp.Variable.ItemLabel", list).getInnerText());
		}
		closeEntityEditPage();
		return varis;
	}

	@Override
	public String parentFormId(String type)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public By getImportBtn(String type)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Update the entity id value
	 */
	public void updateVariable_EntityID(String RegulatorPrefix, String value) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "3").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "3").input(value);
		waitStatusDlg();
		element("emp.Variable.ItemLabel", id, "3").click();
		waitStatusDlg();
	}

	/**
	 * Update the entity id scheme
	 */
	public void updateVariable_EntityIDScheme(String RegulatorPrefix, String value) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "2").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "2").input(value);
		waitStatusDlg();
		element("emp.Variable.ItemLabel", id, "2").click();
		waitStatusDlg();
	}

	/**
	 * Update the monetary scale value
	 */
	public void updateVariable_MonetaryScale(String RegulatorPrefix, String value) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "6").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "6").input(value);
		waitStatusDlg();
		element("emp.Variable.ItemLabel", id, "3").click();
		waitStatusDlg();
	}

	/**
	 * Update the transmission ref value
	 */
	public void updateVariable_TransmissionRef(String RegulatorPrefix, String value) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "8").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "8").input(value);
		waitStatusDlg();
		element("emp.Variable.ItemLabel", id, "3").click();
		waitStatusDlg();
	}

	/**
	 * Update the financial year value
	 */
	public void updateVariable_FinancialYear(String RegulatorPrefix, String value) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "4").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.field", id, "4").setInnerText(value);
		waitStatusDlg();
		Thread.sleep(300);
		selectDate(value);
	}

	/**
	 * Update the accounting standard value
	 */
	public void updateVariable_AccountingStandard(String RegulatorPrefix, String value) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "1").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldSelect", id, "1").selectByVisibleText(value);
		waitStatusDlg();
	}

	/**
	 * Update the scope of consolidation value
	 */
	public void updateVariable_ScopeOfConsolidation(String RegulatorPrefix, String value) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "5").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldSelect", id, "5").selectByVisibleText(value);
		waitStatusDlg();
	}

	/**
	 * Update the currency value
	 */
	public void updateVariable_Currency(String RegulatorPrefix, String value) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "7").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldSelect", id, "7").selectByVisibleText(value);
		waitStatusDlg();
	}

	/**
	 * Get the entity id value
	 */
	public String getVariable_EntityID(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		return element("emp.variable.field", id, "3").getInnerText();
	}

	/**
	 * Get the monetary scale value
	 */
	public String getVariable_MonetaryScale(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		return element("emp.variable.field", id, "6").getInnerText();
	}

	/**
	 * Get the transmission ref value
	 */
	public String getVariable_TransmissionRef(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		return element("emp.variable.field", id, "8").getInnerText();
	}

	/**
	 * Get the financial year value
	 */
	public String getVariable_FinancialYear(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		return element("emp.variable.field", id, "4").getInnerText();
	}

	/**
	 * Get the accounting standard value
	 */
	public String getVariable_AccountingStandard(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		return element("emp.variable.field", id, "1").getInnerText();
	}

	/**
	 * Get the scope of consolidation value
	 */
	public String getVariable_ScopeOfConsolidation(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		return element("emp.variable.field", id, "5").getInnerText();
	}

	/**
	 * Get all the accounting standard value
	 */
	public List<String> getVariable_All_AccountingStandard(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "1").clickByJavaScript();
		waitStatusDlg();
		return element("emp.variable.fieldSelect", id, "1").getAllOptionTexts();
	}

	/**
	 * Get all the scope of consolidation value
	 */
	public List<String> getVariable_All_ScopeOfConsolidation(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		element("emp.variable.field", id, "5").clickByJavaScript();
		waitStatusDlg();
		return element("emp.variable.fieldSelect", id, "5").getAllOptionTexts();
	}

	/**
	 * Get the currency value
	 */
	public String getVariable_Currency(String RegulatorPrefix) throws Exception
	{
		String id = element("emp.Variable.Reg", RegulatorPrefix).getAttribute("id") + "_data";
		return element("emp.variable.field", id, "7").getInnerText();
	}

	/**
	 * Assign entity variable
	 */
	public EntityPage assignVariable(String RegulatorPrefix) throws Exception
	{
		element("emp.edit.SaveVar").clickByJavaScript();
		waitStatusDlg();
		return new EntityPage(getWebDriverWrapper());
	}

	/**
	 * Assign entity variable with error message
	 */
	public String assignVariableWithErrorMsg(String regulatorPrefix) throws Exception
	{
		element("emp.edit.SaveVar").clickByJavaScript();
		waitThat("emp.messageTitle").toBeVisible();
		String msg = element("emp.promptMsg").getInnerText();
		waitThat("emp.messageTitle").toBeInvisible();
		return msg;
	}

	/**
	 * cancel entity variable
	 */
	public EntityPage cancelVariable(String RegulatorPrefix) throws Exception
	{
		element("emp.variable.cancelBtn").click();
		waitStatusDlg();
		return new EntityPage(getWebDriverWrapper());
	}

	/**
	 * Add root entity if entity does not exist
	 */
	public EntityPage addRootEntityIfNotExist(String name, String code, String description, boolean save) throws Exception
	{
		List<String> entities = getAllEntityName();
		if (!entities.contains(name))
		{
			addRootEntity(name, code, description, save);
		}
		return new EntityPage(getWebDriverWrapper());
	}

	/**
	 * Update the required field of FED1
	 */
	public void updateVariable_RequiredFieldOfFED1(String value) throws Exception
	{
		String id = element("emp.Variable.Reg", "FED_SP0").getAttribute("id") + "_data";

		// Set the value of FED Accounting Standard
		element("emp.variable.field", id, "1").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "1").input(value);
		waitStatusDlg();

		// Set the value of FED Group Consolidation Type
		element("emp.variable.field", id, "3").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "3").input(value);
		waitStatusDlg();

		// Set the value of FED Reporting Currency
		element("emp.variable.field", id, "5").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "5").input(value);
		waitStatusDlg();
	}

	/**
	 * Update the required field of FEDDS
	 */
	public void updateVariable_RequiredFieldOfFEDDS(String value) throws Exception
	{
		String id = element("emp.Variable.Reg", "FED_DS").getAttribute("id") + "_data";

		// Set the value of FED Accounting Standard
		element("emp.variable.field", id, "1").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "1").input(value);
		waitStatusDlg();

		// Set the value of FED Group Consolidation Type
		element("emp.variable.field", id, "3").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "3").input(value);
		waitStatusDlg();

		// Set the value of FED Reporting Currency
		element("emp.variable.field", id, "4").clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.fieldInput", id, "4").input(value);
		waitStatusDlg();
	}

	/**
	 * Input all required form variable field.
	 */
	public void inputRequiredFieldsOfVariable(String prefix, String value) throws Exception
	{
		List<IWebDriverWrapper.IWebElementWrapper> list = element("emp.variable.requiredField").getAllMatchedElements();
		for (int i = 0; i < list.size(); i++)
		{
			IWebDriverWrapper.IWebElementWrapper element = list.get(i);
			if (!StringUtils.isNotBlank(element.getInnerText()))
			{
				String id = element.getAttribute("id").substring(0, element.getAttribute("id").indexOf("Output"));
				element.clickByJavaScript();
				waitStatusDlg();
				element("emp.variable.requiredInput", id).input(value);
				waitStatusDlg();
				element("emp.variable.productPrefix", prefix).click();
				waitStatusDlg();
			}
		}
	}

	/**
	 * Update the column value of input
	 */
	public void updateColumnValueOfInput(String prefix, String columnName, String value) throws Exception
	{
		element("emp.variable.column", prefix, columnName).clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.columnInput", prefix, columnName).input(value);
		waitStatusDlg();
		element("emp.variable.columnName", prefix, columnName).click();
		waitStatusDlg();
	}

	/**
	 * Update the column value of date
	 */
	public void updateColumnValueOfDate(String prefix, String columnName, String value) throws Exception
	{
		element("emp.variable.column", prefix, columnName).clickByJavaScript();
		waitStatusDlg();
		element("emp.variable.columnInput", prefix, columnName).input(value);
		selectDate(value);
		waitStatusDlg();
		element("emp.variable.columnName", prefix, columnName).click();
		waitStatusDlg();
	}
}
