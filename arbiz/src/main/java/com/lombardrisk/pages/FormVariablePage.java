package com.lombardrisk.pages;

import java.util.List;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by zhijun dai on 9/6/2016.
 */
public class FormVariablePage extends AbstractPage
{

	public FormVariablePage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);
	}

	/**
	 * Get all colume name
	 */
	public List<String> getAllColumeName() throws Exception
	{
		return element("fv.columeName").getAllInnerTexts();
	}

	/**
	 * Filter by all colume
	 */
	public FormVariablePage filterByAllColume(String entityName, String configName, String formName, String variableValue, String value) throws Exception
	{
		if (entityName != null)
		{
			element("fv.columeField", "ENTITY").input(entityName);
			waitStatusDlg();
		}

		if (configName != null)
		{
			element("fv.columeField", "CONFIG").input(configName);
			waitStatusDlg();

		}

		if (formName != null)
		{
			element("fv.columeField", "FORM").input(formName);
			waitStatusDlg();
		}

		if (variableValue != null)
		{
			element("fv.columeField", "VARIABLE").input(variableValue);
			waitStatusDlg();
		}

		if (value != null)
		{
			element("fv.columeField", "VALUE").input(value);
			waitStatusDlg();
		}
		Thread.sleep(3000);
		return new FormVariablePage(getWebDriverWrapper());
	}

	/**
	 * Filter by entity colume
	 */
	public FormVariablePage filterByEntityColume(String entityName) throws Exception
	{
		return filterByAllColume(entityName, null, null, null, null);
	}

	/**
	 * Filter by config colume
	 */
	public FormVariablePage filterByConfigColume(String configName) throws Exception
	{
		return filterByAllColume(null, configName, null, null, null);
	}

	/**
	 * Filter by form colume
	 */
	public FormVariablePage filterByFormColume(String formName) throws Exception
	{
		return filterByAllColume(null, null, formName, null, null);
	}

	/**
	 * Filter by variable colume
	 */
	public FormVariablePage filterByVariableColume(String variableName) throws Exception
	{
		return filterByAllColume(null, null, null, variableName, null);
	}

	/**
	 * Filter by value colume
	 */
	public FormVariablePage filterByValueColume(String value) throws Exception
	{
		return filterByAllColume(null, null, null, null, value);
	}

	/**
	 * Sort by special colume
	 */
	public FormVariablePage sortBySpecialColume(String columeName) throws Exception
	{

		switch (columeName)
		{
			case "ENTITY":
				element("fv.columeSortButton", "ENTITY").click();
				waitStatusDlg();
				break;
			case "CONFIG":
				element("fv.columeSortButton", "CONFIG").click();
				waitStatusDlg();
				break;
			case "FORM":
				element("fv.columeSortButton", "FORM").click();
				waitStatusDlg();
				break;
			case "VARIABLE":
				element("fv.columeSortButton", "VARIABLE").click();
				waitStatusDlg();
				break;
			case "VALUE":
				element("fv.columeSortButton", "VALUE").click();
				waitStatusDlg();
				break;
			default:
				waitStatusDlg();
		}

		return new FormVariablePage(getWebDriverWrapper());
	}

	/**
	 * Get all special colume text
	 */
	public List<String> getAllSepcialColumeText(String columeName) throws Exception
	{
		List<String> list = null;
		switch (columeName)
		{
			case "ENTITY":
				list = element("fv.columeElement", "2").getAllInnerTexts();
				break;
			case "CONFIG":
				list = element("fv.columeElement", "3").getAllInnerTexts();
				break;
			case "FORM":
				list = element("fv.columeElement", "4").getAllInnerTexts();
				break;
			case "VARIABLE":
				list = element("fv.columeElement", "5").getAllInnerTexts();
				break;
			case "VALUE":
				list = element("fv.columeElement", "7").getAllInnerTexts();
				break;
			default:
				list = null;
		}
		return list;
	}

	/**
	 * Check the list is order by asc or desc
	 */
	public boolean checkListOrder(List<String> list, String order)
	{
		boolean flag = true;
		if ("asc".equalsIgnoreCase(order))
		{
			for (int i = 0; i < list.size() - 1; i++)
			{
				if (list.get(i).compareTo(list.get(i + 1)) <= 0)
					continue;
				else
					return false;
			}
		}

		if ("desc".equalsIgnoreCase(order))
		{
			for (int i = 0; i < list.size() - 1; i++)
			{
				if (list.get(i).compareTo(list.get(i + 1)) >= 0)
					continue;
				else
					return false;
			}
		}

		return flag;
	}

	/**
	 * Back to dashboard page
	 */
	public ListPage backToDashboard() throws Exception
	{
		element("fv.dashboard").click();
		waitStatusDlg();
		waitThat().timeout(500);
		return new ListPage(getWebDriverWrapper());
	}

	/**
	 * Check all the element in the list contains special string
	 */
	public boolean checkAllElementContainsSpecialStr(List<String> list, String str)
	{
		boolean flag = true;
		for (int i = 0; i < list.size(); i++)
		{
			if (!list.get(i).contains(str))
				return false;
		}
		return flag;
	}

	/**
	 * Click edit button of the special variable
	 */
	public FormVariablePage clickEditButtonOfVariable(String entity, String form, String variable) throws Exception
	{
		element("fv.editIcon", entity, form, variable).click();
		waitStatusDlg();
		return new FormVariablePage(getWebDriverWrapper());
	}

	/**
	 * Edit variable value
	 */
	public void editVariable(String value, String type) throws Exception
	{
		if ("Date".equalsIgnoreCase(type))
		{
			element("fv.editField").input(value);
			waitStatusDlg();
			selectDate(value);
			waitStatusDlg();
		}
		else if ("Select".equalsIgnoreCase(type))
		{
			element("fv.editField").selectByVisibleText(value);
			waitStatusDlg();
		}
		else
		{
			element("fv.editField").input(value);
			waitStatusDlg();
		}
	}

	/**
	 * Save the edited value
	 */
	public FormVariablePage saveEdit() throws Exception
	{
		element("fv.saveBtn").click();
		waitStatusDlg();
		return new FormVariablePage(getWebDriverWrapper());
	}

	/**
	 * Cancel the edited value
	 */
	public FormVariablePage cancelEdit() throws Exception
	{
		element("fv.cancelBtn").click();
		waitStatusDlg();
		return new FormVariablePage(getWebDriverWrapper());
	}

	/**
	 * Get the value field value
	 */
	public String getValueOfValueField(String entity, String form, String variable) throws Exception
	{
		return element("fv.valueField", entity, form, variable).getInnerText();
	}

	/**
	 * Edit the variable value
	 */
	public void editVariableValue(String entity, String form, String variable, String value, String type) throws Exception
	{
		clickEditButtonOfVariable(entity, form, variable);
		editVariable(value, type);
		saveEdit();
	}

	/**
	 * Edit the variable value with error message
	 */
	public String editVariableValueWithErrorMsg(String entity, String form, String variable, String value, String type) throws Exception
	{
		clickEditButtonOfVariable(entity, form, variable);
		editVariable(value, type);
		String msg = saveEditWithMsg();
		return msg;
	}

	/**
	 * Get the options of dropdown list
	 */
	public List<String> getDropDownList() throws Exception
	{
		return element("fv.editField").getAllOptionTexts();
	}

	/**
	 * Get the selected option of dropdown list
	 */
	public String getSelectedOption() throws Exception
	{
		return element("fv.editField").getSelectedText();
	}

	/**
	 * Get the label name of the edit variable page
	 */
	public List<String> getLabelName() throws Exception
	{
		return element("fv.edit.labelName").getAllInnerTexts();
	}

	/**
	 * Get the lable value of the edit variable page
	 */
	public List<String> getLabelValue() throws Exception
	{
		return element("fv.edit.labelValue").getAllInnerTexts();
	}

	/**
	 * Save the edited value
	 */
	public String saveEditWithMsg() throws Exception
	{
		element("fv.saveBtn").click();
		waitStatusDlg();
		waitThat("fv.promptMsg").toBeVisible();
		String msg = element("fv.promptMsg").getInnerText();
		waitThat("fv.promptMsg").toBeInvisible();
		return msg;
	}

	/**
	 * Is the edit button enabled or not
	 */
	public boolean isEditButtonEnabled(String entity, String form, String variable) throws Exception
	{
		return !element("fv.editIcon", entity, form, variable).getAttribute("class").contains("ui-state-disabled");
	}

	/**
	 * Is the entity value exist
	 */
	public boolean isEntityValueExist() throws Exception
	{
		return element("fv.columeElement", "2").isPresent();
	}

	/**
	 * Is the No Data displayed
	 */
	public boolean isNoDataDisplayed() throws Exception
	{
		return element("fv.noDataElement").isDisplayed();
	}
}
