package com.lombardrisk.pages;

import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Ray Rui on 5/21/15.
 * Refactored by Leo Tu on 1/25/16
 */
public class ExportToFilePage extends AbstractPage {
    public ExportToFilePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
        // TODO Auto-generated constructor stub
    }

    protected void setGroupSelector(String group) throws Exception {
        try {
            logger.info("Set group: " + group);
            element("efp.groupSelector").selectByVisibleText(group);
            waitStatusDlg();
            waitThat().timeout(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void setReferenceDate(String date) throws Exception {
        try {
            logger.info("Set referenceDate: " + date);
            element("efp.referenceDate").selectByVisibleText(date);
            waitStatusDlg();
            waitThat().timeout(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void setFrameworkSelector(String framework) throws Exception {
        try {
            logger.info("Set framework: " + framework);
            element("efp.frameworkSelector").selectByVisibleText(framework);
            waitStatusDlg();
            waitThat().timeout(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void setTaxonomySelector(String taxonomy) throws Exception {
        try {
            logger.info("Set taxonomy: " + taxonomy);
            element("efp.taxonomySelector").selectByVisibleText(taxonomy);
            waitStatusDlg();
            waitThat().timeout(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void setModuleSelector(String module) throws Exception {
        try {
            logger.info("Set module: " + module);
            element("efp.moduleSelector").selectByVisibleText(module);
            waitStatusDlg();
            waitThat().timeout(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void exportBtnClick() throws Exception {
        logger.info("Click export button");
        TestCaseManager.getTestCase().startTransaction("");
        TestCaseManager.getTestCase().setPrepareToDownload(true);
        element("efp.exportConfirm").click();
        TestCaseManager.getTestCase().stopTransaction();
        waitStatusDlg();
        if(element("efp.xbrlOK").isDisplayed()){
        	element("efp.xbrlOK").click();
        	waitStatusDlg();
        }
    }
    
    protected void deselectAll() throws Exception
    {
    	if(element("efp.allForm").getAttribute("class").contains("ui-icon-check")){
    		element("efp.allForm").click();
    		waitStatusDlg();
    	}
    		
    }
    
    protected void selectAll() throws Exception { 
        if (!element("efp.selectAll").getAttribute("class").contains("ui-icon-check")) {
            element("efp.selectAll").click();
            waitStatusDlg();
        }

    }
    
    protected void selectCompressType(String type) throws Exception {
		if(type==null)
			element("efp.comparessType").selectByVisibleText("NONE");
		
		else{
			type=type.toUpperCase();
			element("efp.comparessType").selectByVisibleText(type);
		}
		waitStatusDlg();
	}
    protected void selectForm_xbrl(String formCode,String formVersion) throws Exception {
    	deselectAll();
    	String [] list= new String[]{formCode,formVersion};
    	element("efp.formCheckBox",list).clickByJavaScript();
    	waitStatusDlg();
    }
}
