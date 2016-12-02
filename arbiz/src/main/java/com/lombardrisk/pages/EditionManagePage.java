package com.lombardrisk.pages;

import com.lombardrisk.utils.DateTime;

import org.yiwan.webcore.web.IWebDriverWrapper;

import java.util.ArrayList;

/**
 * Create by Leo Tu on Sep 17, 2015
 */
public class EditionManagePage extends AbstractPage {


    public EditionManagePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public String findForm(String creationDate) throws Exception {
        String page_index = "";
        int pageNO = 0;
        int rowNo = 0;
        boolean findForm = false;
        while (!findForm) {
            boolean nextPageage = true;
            while (nextPageage) {
                int amt = (int) element("edm.editionMagForm").getRowCount();
                for (int i = 1; i <= amt; i++) {
                    String cDate = element("edm.createDt", String.valueOf(i)).getInnerText();
                    if (cDate.equalsIgnoreCase(creationDate)) {
                        nextPageage = false;
                        findForm = true;
                        rowNo = i;
                        logger.info("Find the edition!");
                    }
                }
                try {
                    String p_Index = element("edm.nextPage").getAttribute("tabindex");
                    if (!p_Index.equals("-1")) {
                        element("edm.nextPage").click();
                        pageNO++;
                    } else {
                        nextPageage = false;
                        findForm = true;
                        logger.error("The edition does not exist!");
                    }
                } catch (Exception e) {
                    nextPageage = false;
                    findForm = true;
                    e.printStackTrace();
                }

            }
        }
        page_index = pageNO + "_" + rowNo;


        return page_index;
    }


    public boolean activateForm(String creationDate) throws Exception {
        boolean state = false;
        String formPosttion = findForm(creationDate);
        if (formPosttion.equals("0_0")) {
            logger.error("The form with creation date=" + creationDate + " deoes not exist!");
        }
        String pageNo = formPosttion.split("_")[0];
        String rowNo = formPosttion.split("_")[1];
        if (!pageNo.equals("1")) {
            int p = Integer.parseInt(pageNo);
            int i = 0;
            while (i < p) {
                element("edm.nextPage").click();
                waitThat().timeout(1000);
            }
        }

        String status = element("edm.formState", String.valueOf(rowNo)).getInnerText();
        if (status.equalsIgnoreCase("ACTIVE")) {
            logger.error("This edition alread activated");
        } else {
            element("edm.changeState", String.valueOf(rowNo)).click();
            waitThat().timeout(1000);
        }

        status = element("edm.formState", String.valueOf(rowNo)).getInnerText();
        if (status.equalsIgnoreCase("ACTIVE")) {
            state = true;
            logger.error("Activate edition successfully");
        }
        return state;

    }

    public boolean deactivateForm(String creationDate) throws Exception {
        boolean state = false;
        String formPosttion = findForm(creationDate);
        if (formPosttion.equals("0_0")) {
            logger.error("The form with creation date=" + creationDate + " deoes not exist!");
        }
        String pageNo = formPosttion.split("_")[0];
        String rowNo = formPosttion.split("_")[1];
        if (!pageNo.equals("1")) {
            int p = Integer.parseInt(pageNo);
            int i = 0;
            while (i < p) {
                element("edm.nextPage").click();
                waitThat().timeout(1000);
            }
        }

        String status = element("edm.formState", String.valueOf(rowNo)).getInnerText();
        if (status.equalsIgnoreCase("DORMANT")) {
            logger.error("This edition is dormant");
        } else {
            element("edm.changeState", String.valueOf(rowNo)).click();
            waitThat().timeout(1000);
        }

        status = element("edm.formState", String.valueOf(rowNo)).getInnerText();
        if (status.equalsIgnoreCase("DORMANT")) {
            state = true;
            logger.error("Deactivate edition successfully");
        }
        return state;
    }


    public FormInstancePage openForm(String creationDate) throws Exception {
        String formPosttion = findForm(creationDate);
        if (formPosttion.equals("0_0")) {
            logger.error("The form with creation date=" + creationDate + " deoes not exist!");
        }
        String pageNo = formPosttion.split("_")[0];
        String rowNo = formPosttion.split("_")[1];
        if (!pageNo.equals("1")) {
            int p = Integer.parseInt(pageNo);
            int i = 0;
            while (i < p) {
                element("edm.nextPage").click();
                waitThat().timeout(1000);
            }
        }

        int row = Integer.parseInt(rowNo) - 1;
        element("edm.formLink", String.valueOf(row)).click();
        waitStatusDlg();

        return new FormInstancePage(getWebDriverWrapper());
    }


    public void deleteForm(String creationDate) throws Exception {
        String formPosttion = findForm(creationDate);
        if (formPosttion.equals("0_0")) {
            logger.error("The form with creation date=" + creationDate + " deoes not exist!");
        }
        String pageNo = formPosttion.split("_")[0];
        String rowNo = formPosttion.split("_")[1];
        if (!pageNo.equals("1")) {
            int p = Integer.parseInt(pageNo);
            int i = 0;
            while (i < p) {
                element("edm.nextPage").click();
                waitThat().timeout(1000);
            }
        }

        int row = Integer.parseInt(rowNo) - 1;
        element("edm.delEdition", String.valueOf(row)).click();
        waitStatusDlg();

    }


    public int getEditionAmt() throws Exception {

        int editionAmt = (int) element("edm.editionMagForm").getRowCount();
        try {
            String p_Index = element("edm.nextPage").getAttribute("tabindex");
            if (!p_Index.equals("-1")) {
                element("edm.nextPage").click();
                int cutAmt = (int) element("edm.editionMagForm").getRowCount();
                editionAmt = editionAmt + cutAmt;
            }
        } catch (Exception e) {

        }
        
        if(!element("edm.firtPage").getAttribute("class").contains("ui-state-disabled")){
        	element("edm.firtPage").click();
        	waitStatusDlg();
        }
        
        return editionAmt;
    }


    public FormInstancePage closeEditionManage_Form() throws Exception {
        element("edm.closeEdition").click();
        waitStatusDlg();
        return new FormInstancePage(getWebDriverWrapper());
    }


    public ArrayList<String> getEditionInfo(int rowIndex) throws Exception {
        int index = rowIndex - 1;
        ArrayList<String> editionInfo = new ArrayList<String>();
        editionInfo.add(element("edm.editNO", String.valueOf(index)).getInnerText());
        editionInfo.add(element("edm.createDt", String.valueOf(index)).getInnerText());
        editionInfo.add(element("edm.modifyDt", String.valueOf(index)).getInnerText());
        editionInfo.add(element("edm.modifyOW", String.valueOf(index)).getInnerText());
        editionInfo.add(element("edm.reportStatus", String.valueOf(index)).getInnerText());
        editionInfo.add(element("edm.formState", String.valueOf(index)).getInnerText());

        return editionInfo;
    }


    public String getLastModifiedEdition() throws Exception {
        int page = 1;
        String curDateTime = DateTime.getCurrentDateTime();
        boolean nextPageage = true;
        String modifiedDt = element("edm.modifyDt", "1").getInnerText();
        long diff_Base = DateTime.getDiffTwoDate(curDateTime, modifiedDt, "m");
        String lastTime = modifiedDt;
        while (nextPageage) {
            int amt = (int) element("edm.editionMagForm").getRowCount();
            for (int i = 1; i <= amt; i++) {
                String lastModifiedDt = element("edm.modifyDt", String.valueOf(i)).getInnerText();
                ;
                long diff_Cur = DateTime.getDiffTwoDate(curDateTime, lastModifiedDt, "m");
                if (diff_Cur < diff_Base) {
                    lastTime = lastModifiedDt;
                }

            }
            try {
                String p_Index = element("edm.nextPage").getAttribute("tabindex");
                if (!p_Index.equals("-1")) {
                    element("edm.nextPage").click();
                    waitStatusDlg();
                    page = page + 1;

                }

            } catch (Exception e) {
                nextPageage = false;
                e.printStackTrace();
            }

        }

        return lastTime;
    }

    public boolean selectEditionFromManage(int m) throws Exception {
        boolean rst = true;
        int amt = (int) element("edm.editionMagForm").getRowCount();
        if (amt >= m) {
            try {
                int index = m - 1;
                ArrayList<String> editionInfo = getEditionInfo(m);
                element("edm.formLink", String.valueOf(index)).click();
                waitStatusDlg();

                FormInstancePage formInstancePage = new FormInstancePage(getWebDriverWrapper());

                if (!element("edm.curEdition").getInnerText().equals((editionInfo.get(1) + " " + editionInfo.get(4))))
                    rst = false;

                formInstancePage.closeFormInstance();
            } catch (Exception e) {
                rst = false;
                e.printStackTrace();
            }
        }
        return rst;
    }


    public boolean isVisible(String loc) throws Exception {
        return element(loc).isDisplayed();
    }

    /*
     * delete edition by row index
     */
    public void deleteEdition(int index, String comment) throws Exception {
        element("edm.delEdition", String.valueOf(index - 1)).click();
        element("edm.edition.confirm").click();
        waitStatusDlg();
        element("edm.edition.comment").input(comment);
        element("edm.edition.comment.confirm").click();
        waitStatusDlg();

    }


    /*
     * delete edition by edition number
     */
    public void deleteEdition(String editionNo, String comment) throws Exception {
    	logger.info("Begin delete edition "+editionNo);
        element("edm.edition.delete", editionNo).click();
        element("edm.edition.confirm").click();
        waitStatusDlg();
        element("edm.edition.comment").input(comment);
        element("edm.edition.comment.confirm").click();
        waitStatusDlg();
    }


    /*
     * close edition manager window
     */
    public ListPage closeEditionManage() throws Exception {
    	logger.info("Close edition manager page");
        element("edm.closeEdition").click();
        waitStatusDlg();
        return new ListPage(getWebDriverWrapper());
    }
    
    public String getEditionState(String editionNo) throws Exception {
		return element("edm.edition.state",editionNo).getInnerText();
	}
}
   