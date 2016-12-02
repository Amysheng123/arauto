package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Ray Rui on 3/2/15. Refactored by Leo Tu on 1/25/16
 */
public abstract class AbstractDrilldownPage extends AbstractPage
{

	/**
	 * 
	 * @param webDriverWrapper
	 */
	public AbstractDrilldownPage(IWebDriverWrapper webDriverWrapper)
	{
		super(webDriverWrapper);

	}

	/**
	 * Enter validation Page
	 * 
	 * @return ValidationPage
	 * @throws Exception
	 */
	public ValidationPage getValidationTab() throws Exception
	{
		element("adp.validation").click();
		waitStatusDlg();
		return new ValidationPage(getWebDriverWrapper());
	}

	/**
	 * Enter ErrorList Page
	 * 
	 * @return ErrorListPage
	 * @throws Exception
	 */
	public ErrorListPage getErrorListTab() throws Exception
	{
		element("adp.problems").click();
		return new ErrorListPage(getWebDriverWrapper());
	}

	/**
	 * Eneter AdjustLogPage
	 * 
	 * @return AdjustLogPage
	 * @throws Exception
	 */
	public AdjustLogPage getAdjustLogTab() throws Exception
	{
		element("adp.adjustment").click();
		return new AdjustLogPage(getWebDriverWrapper());
	}

	/**
	 * Show adjustmentPage
	 * 
	 * @throws Exception
	 */
	public void showClick() throws Exception
	{
		element("adp.hide").click();
	}

	/**
	 * Hide adjustmentPage
	 * 
	 * @throws Exception
	 */
	public void hideClick() throws Exception
	{
		element("adp.hide").click();
		waitStatusDlg();
	}

}
