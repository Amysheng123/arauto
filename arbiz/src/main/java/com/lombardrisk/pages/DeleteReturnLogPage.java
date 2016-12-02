package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Leo Tu on 4/27/2016.
 */
public class DeleteReturnLogPage extends AbstractPage {
    public DeleteReturnLogPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
    }
    
    /*
     * get log amount
     */
    public int getlogNums() throws Exception {
    	logger.info("Begin get log amount in delete return log page");
    	int amt=0;
    	int rowAmt_F=0;
    	if(element("drlp.firstRowTxt").isDisplayed()){
    		if(element("drlp.firstRowTxt").getInnerText().equals("No records found."))
    			amt= 0;
    	}else{
    		rowAmt_F = (int) element("drlp.logTable").getRowCount();
            if (!element("drlp.lastPage").getAttribute("class").equals("ui-state-disabled")) {
                element("drlp.lastPage").click();
                waitStatusDlg();
                int PageNums =0;
                if(element("drlp.PageNo","3").isPresent()){
                	PageNums = Integer.parseInt(element("drlp.PageNo","3").getSelectedText());
                }
                else{
                	PageNums = 2;
                }
                int rowAmt_L = (int) element("drlp.logTable").getRowCount();
                amt= rowAmt_F * (PageNums - 1) + rowAmt_L;
            }
            else{
            	amt= rowAmt_F;
            }
    	}
    	
    	logger.info("There are "+amt+"  logs");
    	return amt;

    }


    /*
     * close delete return page
     */
    public ListPage closeDeleteReturnLog() throws Exception {
    	logger.info("Close delete return log page");
        element("drlp.closeWindow").click();
        waitStatusDlg();
        return new ListPage(getWebDriverWrapper());
    }
    
    public String getLastAction(String formCode,String formVersion,String editionNo,int topNum) throws Exception{
    	String lastAction=null;
    	while(!element("drlp.sort").getAttribute("class").contains("ui-icon-triangle-1-s"))
    	{
    		element("drlp.sortByAction").click();
    		waitStatusDlg();
    		waitThat().timeout(500);
    	}
    	for(int i=1;i<=topNum;i++){
    		if(element("drlp.logItem",String.valueOf(i),"1").getInnerText().equalsIgnoreCase(formCode) && element("drlp.logItem",String.valueOf(i),"2").getInnerText().equalsIgnoreCase(formVersion) && element("drlp.logItem",String.valueOf(i),"3").getInnerText().equalsIgnoreCase(editionNo)){
    			lastAction=element("drlp.logItem",String.valueOf(i),"6").getInnerText();
    			break;
    		}
    	}
    	return lastAction;
    	
    }
}
