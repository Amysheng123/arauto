package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Create by Leo Tu on Dec 1, 2015
 * Refactored by Leo Tu on 1/29/16
 */

public class PrivilegeGroupPage extends AbstractPage {

    public PrivilegeGroupPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
    }


    public boolean addPrivilegeGroup(String name, String description, String typeName) throws Exception {
        logger.info("Begin add a new privilege group[" + name + "]");
        boolean addRst = true;
        clickAddGroupBtn();
        element("pp.GPN").type(name);
        element("pp.GPD").type(description);
        selectGroupType(typeName);
        element("pp.ADS").click();
        long startTime = System.currentTimeMillis();
        long CurrentTime = System.currentTimeMillis();
        while ((CurrentTime - startTime) / 1000 < 6) {
            try {
                if (element("pp.messageTitle").getInnerText().equals("Error")) {
                    addRst = false;
                    logger.error("Got error when add privilege group[" + name + "], maybe the groups already existed!");
                    break;
                }
            } catch (Exception e) {
            }
            CurrentTime = System.currentTimeMillis();
        }
        return addRst;
    }


    public void editPrivilegeGroup(String originalGroupName, String newGroupName, String newDescription) throws Exception {
    	clickEditPGBtn(originalGroupName);
        if (newGroupName != null) {
            element("pp.EGPN").input(newGroupName);
        }
        if (newDescription != null) {
            element("pp.EGPD").input(newDescription);
        }
        element("pp.EADS").click();
        waitStatusDlg();

    }

    public void deletePrivilegeGroup(String name) throws Exception {
    	logger.info("Dlete pg["+name+"]");
        clickDeletePGBtn(name);
        waitStatusDlg();
        element("pp.DELGPC").click();
        waitStatusDlg();

    }


    public void addPrivileges(String groupName, List<String> privilegesName) throws Exception {
        if (privilegesName.size() > 0) {
            logger.info("Begin add privilege"+privilegesName);
            clickAddPrivilegeBtn(groupName);
            selectPrivilege(privilegesName);
        } else {
            logger.info("Privilege is empty,do not need add privilege");
        }

    }


    public void deletePrivilege(String groupName, List<String> privilegesName) throws Exception {
        logger.info("Begin delete privilege");
        String id = element("pp.getPG", groupName).getAttribute("id").replace("permissionListForm:CustomGroupPanel", "permissionListForm:customGroupPanelGrid").replace("_header", "");
        String[] list = new String[]{id, "", ""};
        int amt = (int) element("pp.privTab", id).getRowCount();
        for (String privilege : privilegesName) {
        	for (int r = 1; r <= amt; r++) {
                list[1] = String.valueOf(r);
                for (int c = 1; c <= 4; c++) {
                    list[2] = String.valueOf(c);
                    if(element("pp.getPriv", list).isDisplayed()){
                    	if (element("pp.getPriv", list).getInnerText().equals(privilege)) {
                            element("pp.delPriv", list).click();
                            element("pp.DPC").click();
                            waitStatusDlg();
                        }
                    }else
                    	break;
                }
            }
        }


    }

    public void deletePrivilege(String groupName, String privilegesName) throws Exception {
        logger.info("Begin delete privilege:"+privilegesName);
        String id = element("pp.getPG", groupName).getAttribute("id").replace("permissionListForm:CustomGroupPanel", "permissionListForm:customGroupPanelGrid").replace("_header", "");
        String[] list = new String[]{id, "", ""};
        int amt = (int) element("pp.privTab", id).getRowCount();
        for (int r = 1; r <= amt; r++) {
            list[1] = String.valueOf(r);
            for (int c = 1; c <= 4; c++) {
                list[2] = String.valueOf(c);
                if(element("pp.getPriv", list).isDisplayed()){
                	if (element("pp.getPriv", list).getInnerText().equals(privilegesName)) {
                        element("pp.delPriv", list).click();
                        element("pp.DPC").click();
                        waitStatusDlg();
                    }
                }else
                	break;
            }
        }

    }

    public void selectPrivilege(List<String> privilegesName) throws Exception {
        for (String privilege : privilegesName) {
            element("pp.privCheckBox", privilege).check(true);
        }
        element("pp.editPrivSave").click();
        waitStatusDlg();
    }

    public List<String> getAllCustomPG() throws Exception {
        return element("pp.allPG").getAllInnerTexts();
    }


    public void clickAddPrivilegeBtn(String permissionGoupName) throws Exception {
        String id = element("pp.getPG", permissionGoupName).getAttribute("id").replace("permissionListForm:CustomGroupPanel", "permissionListForm:customGroupPanelGrid").replace("_header", "");
        String[] list = new String[]{id, "", ""};
        int amt = (int) element("pp.privTab", id).getRowCount();
        for (int r = 1; r <= amt; r++) {
            list[1] = String.valueOf(r);
            for (int c = 1; c <= 4; c++) {
                list[2] = String.valueOf(c);
                if (element("pp.getPriv", list).getInnerText().equals("Add privilege")) {
                    element("pp.addPriv", list).click();
                    waitStatusDlg();
                    r = amt + 1;
                    break;
                }
            }
        }

    }


    public void clickEditPGBtn(String permissionGoupName) throws Exception {
        String id = element("pp.getPG", permissionGoupName).getAttribute("id");
        element("pp.editPG", id).click();
        waitStatusDlg();
    }


    public void clickDeletePGBtn(String groupName) throws Exception {
        String id = element("pp.getPG", groupName).getAttribute("id");
        element("pp.DELGP", id).click();
        waitStatusDlg();
    }


    public void clickAddGroupBtn() throws Exception {
        element("pp.addPG").click();
        waitStatusDlg();
    }

    /*
     * get added privileges from PG
     */
    public List<String> getPrivilegeByPG(String PGName) throws Exception {
    	logger.info("Begin get assigned privilege on group "+PGName);
        List<String> existPrivileges = new ArrayList<String>();
        String id = element("pp.getPG", PGName).getAttribute("id").replace("permissionListForm:CustomGroupPanel", "permissionListForm:customGroupPanelGrid").replace("_header", "");
        String[] list = new String[]{id, "", ""};
        int amt = (int) element("pp.privTab", id).getRowCount();
        try {
            for (int r = 1; r <= amt; r++) {
                list[1] = String.valueOf(r);
                for (int c = 1; c <= 4; c++) {
                    list[2] = String.valueOf(c);{
                    	if(element("pp.getPriv", list).isDisplayed()){
                    		existPrivileges.add(element("pp.getPriv", list).getInnerText());
                    	}else
                    		break;
                    }
                    
                }
            }
        } catch (NoSuchElementException e) {
        }

        return existPrivileges;
    }

    /*
     * get available privileges from 'add privilege' table
     */
    public List<String> getAllPrivileges(String groupName) throws Exception {
        List<String> existPrivileges = new ArrayList<String>();
        clickAddPrivilegeBtn(groupName);
        int amt = (int) element("pp.existPrivTab").getRowCount();
        for (int i = 1; i <= amt; i++) {
            existPrivileges.add(element("pp.existPriv", String.valueOf(i)).getInnerText());
        }
        element("pp.closeWindow").click();
        return existPrivileges;
    }

    public HomePage logout() throws Exception {
        backToListPage();
        element("pp.userMenu").click();
        element("pp.logout").click();
        return new HomePage(getWebDriverWrapper());
    }

    public ListPage backToListPage() throws Exception {
        element("pp.dashboard").click();
        waitStatusDlg();
        return new ListPage(getWebDriverWrapper());
    }


    public void selectGroupType(String type) throws Exception {
        element("pp.GPCT").click();
        waitStatusDlg();
        if (type.equalsIgnoreCase("General User Privileges"))
            element("pp.GPT", "1").click();
        else
            element("pp.GPT", "2").click();
        waitStatusDlg();

    }

    
}
