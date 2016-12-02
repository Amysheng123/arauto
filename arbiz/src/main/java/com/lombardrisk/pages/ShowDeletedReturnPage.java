package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;


/**
 * Created by Leo Tu on 4/27/2016.
 */
public class ShowDeletedReturnPage extends AbstractPage{

	/**
	 * @param webDriverWrapper
	 */
	public ShowDeletedReturnPage(IWebDriverWrapper webDriverWrapper) {
		super(webDriverWrapper);
		// TODO Auto-generated constructor stub
	}
	
	public ListPage restoreReturn(String formCode,String formVerion) throws Exception{
		if(element("sdrp.radioBtn",formCode,formVerion).isDisplayed()){
			element("sdrp.radioBtn",formCode,formVerion).click();
			waitStatusDlg();
			element("sdrp.comment").input("restore test");
			element("sdrp.restoreConfirm").click();
			element("sdrp.cancelBtn" ).click();
		}
		else {
			boolean flag=true;
			while(flag)
			{
				if(!element("sdrp.nextPage").getAttribute("class").contains("ui-state-disabled")){
					element("sdrp.nextPage").click();
					waitStatusDlg();
				}
				else{
					element("sdrp.radioBtn",formCode,formVerion).click();
					waitStatusDlg();
					element("sdrp.comment").input("restore test");
					element("sdrp.restoreConfirm").click();
					element("sdrp.cancelBtn" ).click();
					flag=false;
				}
				
			}
		}
		return new ListPage(getWebDriverWrapper());
	}
	
	public ListPage closeShowDeletedReturnPage() throws Exception{
		element("sdrp.cancelBtn").click();
		waitStatusDlg();
		return new ListPage(getWebDriverWrapper());
	}
	
}
