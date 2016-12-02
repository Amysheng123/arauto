package com.lombardrisk.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.yiwan.webcore.test.TestCaseManager;
import org.yiwan.webcore.web.IWebDriverWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Leo Tu on Dec 1, 2015
 * Refactored by Leo Tu on 1/25/16
 */

public class EntityPage extends AbstractImportPage {


    public EntityPage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);

    }

    public EntityPage addRootEntity(String name, String code, String description, boolean save) throws Exception {
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
        waitThat("emp.messageTitle").toBeInvisible();
        return this;
    }

    public String addEntity(String parentEntity, String name, String code, String description, boolean save) throws Exception {
        logger.info("Begin add entity[" + name + "] to parent[" + parentEntity + "]");
        if (!isEntityHighlight(parentEntity)) {
            element("emp.entityLabel", parentEntity).click();
            waitThat().timeout(2500);
        }
        element("emp.addEntity").click();
        waitThat().timeout(500);
        element("emp.edit.name").type(name);
        element("emp.edit.code").type(code);
        element("emp.edit.desc").type(description);
        if (save){
        	element("emp.addEntitySave").click();
        	logger.info("Click save button");
        }
            
        else{
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

    public EntityPage editEntity(String originalEntiry, String newName, String newCode, String newDescription) throws Exception {
        logger.info("Begin update entity");
        element("emp.entityImg", originalEntiry).click();
        waitStatusDlg();
        element("emp.edit.name").input(newName);
        element("emp.edit.code").input(newName);
        element("emp.edit.desc").input(newDescription);
        element("emp.edit.save").click();
        waitThat("emp.messageTitle").toBeVisible();
        waitThat("emp.messageTitle").toBeInvisible();
		closeEntityEditPage();
        return this;
    }


    public EntityPage deleteEntity(String name) throws Exception {
        logger.info("Begin delete Entity: "+name);
        element("emp.entityImg", name).click();
        waitStatusDlg();
        element("emp.deleteEntity").click();
        waitStatusDlg();
        element("emp.deleteConfirm").click();
        //waitThat("emp.messageTitle").toBeVisible();
        //waitThat("emp.messageTitle").toBeInvisible();
        waitThat().timeout(3000);
        closeEntityEditPage();
        return this;
    }


    public EntityPage assignReturnToEntity(String entityName, ArrayList<String> returnNames) throws Exception {
        logger.info("Begin  assign Return To Entity");
        openAssignReturnPage(entityName);

        for (String name : returnNames) {
            element("emp.edit.RetCheckBox", name).check(true);
            waitStatusDlg();
        }
        element("emp.edit.assSave").click();
        waitThat("emp.messageTitle").toBeVisible();
        waitThat("emp.messageTitle").toBeInvisible();

        return this;
    }

    public EntityPage assignReturnToEntity(String entityName, String ProductPrefxi, String[] returnNames) throws Exception {
        logger.info("Begin  assign Return To Entity");
        openAssignReturnPage(entityName);
        String id = element("emp.ProductTab", ProductPrefxi).getAttribute("id") + "_data";

        for (String name : returnNames) {
            String[] list = new String[]{id, name};
            element("emp.edit.RetCheckBox2", list).check(true);
            waitStatusDlg();
        }
        element("emp.edit.assSave").click();
        waitThat("emp.messageTitle").toBeVisible();
        waitThat("emp.messageTitle").toBeInvisible();

        return this;
    }

    public EntityPage removeReturnFromEntity(String entityName, String ProductPrefxi, String[] returnNames) throws Exception {
        logger.info("Begin  remove Return from Entity");
        openAssignReturnPage(entityName);
        String id = element("emp.ProductTab", ProductPrefxi).getAttribute("id") + "_data";

        for (String name : returnNames) {
            String[] list = new String[]{id, name};
            element("emp.edit.RetCheckBox2", list).check(true);
            waitStatusDlg();
        }
        element("emp.edit.assSave").click();
        waitThat("emp.messageTitle").toBeVisible();
        waitThat("emp.messageTitle").toBeInvisible();

        return this;
    }


    public String getAssignReturnToEntityMessage(String entityName, String Product, String[] returnNames) throws Exception {
        openAssignReturnPage(entityName);
        String id = element("emp.ProductTab", Product).getAttribute("id") + "_data";
        for (String name : returnNames) {
            String[] list = new String[]{id, name};
            element("emp.edit.RetCheckBox2", list).check(true);
            waitStatusDlg();
        }
        element("emp.edit.assSave").click();
        waitThat("emp.promptMsg").toBeVisible();
        return element("emp.promptMsg").getInnerText();
    }

    public String getRemoveReturnFromEntityMessage(String entityName, String Product, String[] returnNames) throws Exception {
        openAssignReturnPage(entityName);
        String id = element("emp.ProductTab", Product).getAttribute("id") + "_data";
        for (String name : returnNames) {
            String[] list = new String[]{id, name};
            element("emp.edit.RetCheckBox2", list).check(true);
            waitStatusDlg();
        }
        element("emp.edit.assSave").click();
        waitThat("emp.promptMsg").toBeVisible();
        return element("emp.promptMsg").getInnerText();
    }


    public EntityPage deleteRturnFromEntity(String entityName, String ProductPrefxi, String[] returnNames) throws Exception {
        logger.info("Begin  assign Return To Entity");
        openAssignReturnPage(entityName);
        String id = element("emp.ProductTab", ProductPrefxi).getAttribute("id") + "_data";

        for (String name : returnNames) {
            String[] list = new String[]{id, name};
            element("emp.edit.RetCheckBox2", list).click();
            waitStatusDlg();
        }
        element("emp.edit.assSave").click();
        waitThat("emp.messageTitle").toBeVisible();
        waitThat("emp.messageTitle").toBeInvisible();

        return this;
    }

    public EntityPage assignReturnToEntity(String entityName, String ProductPrefxi, String[] returnNames, String[] userGPNames, String[] permissionNames) throws Exception {
        logger.info("Begin  assign Return To Entity");
        openAssignReturnPage(entityName);
        String id = element("emp.ProductTab", ProductPrefxi).getAttribute("id") + "_data";
        for (String name : returnNames) {
            String[] list = new String[]{id, name};
            element("emp.edit.RetCheckBox2", list).check(true);
            waitStatusDlg();
            addUserGP(name, userGPNames, true, permissionNames);
        }
        waitThat("emp.messageTitle").toBeVisible();
        waitThat("emp.messageTitle").toBeInvisible();

        return this;
    }


    public void assignAllReturnToEntity(String Regulator, String EntityName, boolean save) throws Exception {
        openAssignReturnPage(EntityName);
        String id = element("emp.ProductTab", Regulator).getAttribute("id");
        element("emp.edit.assALL", id).click();
        waitStatusDlg();
        if (save) {
            element("emp.edit.assSave").click();
            waitThat("emp.messageTitle").toBeVisible();
            waitThat("emp.messageTitle").toBeInvisible();
        } else {
            element("emp.cancelAssignReturn").click();
            waitThat().timeout(1000);
        }
    }


    public boolean isEntityHighlight(String name) throws Exception {
        if (element("emp.entity", name).getAttribute("class").contains("highlightBorderGreen"))
            return true;
        else
            return false;
    }


    public boolean isEntitySelectable(String name) throws Exception {
        if (element("emp.entity").getAttribute("class").contains("ui-tree-selectable"))
            return true;
        else
            return false;
    }

    public List<String> getAllEntityName() throws Exception {
        logger.info("Get all entities");

        return element("emp.allEntity").getAllInnerTexts();
    }

    public void unselectHighlightEntity() throws Exception {
        logger.info("unselect Highlight Entity");
        for (String entityName : getAllEntityName()) {
            if (element("emp.entity", entityName).getAttribute("class").contains("ui-state-highlight")) {
                element("emp.entity", entityName).click();
                break;
            }
        }


    }


    public void openEntityEditPage(String EntityName) throws Exception {
        element("emp.entityImg", EntityName).click();
        waitStatusDlg();
		waitThat().timeout(500);
    }

    public String getParentEntity(String childEntity) throws Exception {
        String id = element("emp.entityImg", childEntity).getAttribute("id");
        String index = id.split(":")[2];
        if (index.contains("_")) {
            id = id.replace(":" + index + ":", ":" + index.split("_")[0] + ":");
        } else {
            id = id.replace(":" + index + ":", ":");
        }

        return element("emp.EBI", id).getInnerText();
    }


    public List<String> getAllAssignProducts() throws Exception {
        logger.info("Get all assigned products");
        return element("emp.assAllPro").getAllInnerTexts();
    }

    public List<String> getAllAssignUserGroup() throws Exception {
        logger.info("Get all assigned user groups");
        return element("emp.assAllUG").getAllInnerTexts();
    }


    public void expandAllSubItems() throws Exception {
        while (element("emp.expand").isDisplayed()) {
            element("emp.expand").click();
        }
    }


    public boolean ifExpandAllSubItems() throws Exception {
        if (element("emp.expand").isDisplayed())
            return false;
        else
            return true;
    }


    public void openAssignPrivPage(String ReturnName) throws Exception {
        element("emp.edit.AssPriv", ReturnName).click();
        waitStatusDlg();
    }


    public boolean verifyDeleteEntityWithChild(String Entity) throws Exception {
        boolean result = false;
        deleteEntity(Entity);
        long startTime = System.currentTimeMillis();
        long CurrentTime = System.currentTimeMillis();
        while ((CurrentTime - startTime) / 1000 < 6) {
            try {
                if (element("emp.promptMsg").getInnerText().equals("Entity cannot be removed as it has Assigned Entities")) {
                    result = true;
                    break;
                }
            } catch (NoSuchElementException e) {
            }
            CurrentTime = System.currentTimeMillis();
        }
        return result;
    }

    public boolean verifyDeleteEntityWithInstance(String Entity) throws Exception {
        boolean result = false;
        deleteEntity(Entity);
        long startTime = System.currentTimeMillis();
        long CurrentTime = System.currentTimeMillis();
        while ((CurrentTime - startTime) / 1000 < 6) {
            try {
                if (element("emp.promptMsg").isDisplayed()) {
                    result = true;
                    break;
                }

            } catch (NoSuchElementException e) {
            }

            CurrentTime = System.currentTimeMillis();
        }


        return result;
    }

    public boolean isReUsedEntity(String parent, String name, String code, String desc, String type) throws Exception {
        boolean result = true;
        String message = addEntity(parent, name, code, desc, true);

        if (type.equalsIgnoreCase("name")) {
            if (message.equals("Entity name " + name + " is already in use")) {
                result = false;
            }
        } else {
            if (message.equals("Entity code " + code + " is already in use")) {
                result = false;
            }
        }
        try{
        	element("emp.addEntityCancel").click();
            waitThat().timeout(1000);
        }catch(Exception e){}
        
        return result;
    }

    public void addPermissionGP(String[] permissionNames) throws Exception {
        logger.info("Click Add Permission Group icon");
        element("emp.addPG").click();
        waitStatusDlg();
        for (String permissionName : permissionNames) {
            element("emp.PrivCheckobx", permissionName).check(true);
            waitStatusDlg();
        }
        element("emp.addPGSave").click();
        waitThat("emp.messageTitle").toBeInvisible();

    }


    public void addUserGP(String returnName, String[] userGPNames, boolean addPersmission, String[] permissionNames) throws Exception {
        logger.info("Click Open[" + returnName + "] link");
        openAssignPrivPage(returnName);
        logger.info("Click Add user group icon");
        element("emp.addUG").click();
        waitStatusDlg();
        for (String UGP : userGPNames) {
            if (!element("emp.UGCheckBox", UGP).getAttribute("class").contains("ui-state-active"))
                element("emp.UGCheckBox", UGP).click();
        }
        element("emp.addUPSave").click();
		waitThat().timeout(1000);

        if (addPersmission) {
            addPermissionGP(permissionNames);
        }
        waitThat().timeout(1000);
        element("emp.confSave").click();
        waitThat().timeout(1000);
        element("emp.edit.assSave").click();
        waitThat().timeout(1000);

    }

    public HomePage logout() throws Exception {
        backToListPage();
        element("emp.userMenu").click();
        waitStatusDlg();
        element("emp.logout").click();
        waitStatusDlg();
        return new HomePage(getWebDriverWrapper());
    }

    public void backToListPage() throws Exception {
		closeEntityEditPage();
        waitThat().timeout(1000);
        element("emp.dashboard").click();
        waitStatusDlg();
		waitThat().timeout(500);
        logger.info("Back to listPage");
    }

    public void entityUsedForReporting(String entity, boolean action) throws Exception {
        openEntityEditPage(entity);
        if (action) {
            if (!element("emp.edit.slide").getAttribute("class").contains("slideBarSetTrue")) {
                element("emp.edit.slideBar").click();
                waitThat().timeout(1000);
                element("emp.edit.assSave").click();
                waitStatusDlg();
            }
        } else {
            if (element("emp.edit.slide").getAttribute("class").contains("slideBarSetTrue")) {
                element("emp.edit.slideBar").click();
                waitThat().timeout(1000);
                element("emp.edit.save").click();
                waitStatusDlg();
            }
        }


    }


    public boolean getDefaultStatus() throws Exception {
        if (element("emp.SHS").getAttribute("class").contains("slideBarSetFalse")) {
            return true;
        } else
            return false;

    }


    public void showDeletedEntities() throws Exception {
        logger.info("Show deleted entities");
        if (!element("emp.SHS").getAttribute("class").contains("slideBarSetTrue"))
        {
        	element("emp.SH").click();
        	waitStatusDlg();
        }

    }

    public void hideDeletedEntities() throws Exception {
        logger.info("Hide deleted entities");
        if (element("emp.SHS").getAttribute("class").contains("slideBarSetTrue")) {
            element("emp.SH").click();
            waitStatusDlg();
        }

    }

    public void restoreDeleteEntity(String name) throws Exception {
        logger.info("Begin retore deleted entity");
        showDeletedEntities();
        openEntityEditPage(name);
        element("emp.restoreEntity").click();
        waitThat().timeout(1000);
        element("emp.restoreconfirm").click();
        waitStatusDlg();
        hideDeletedEntities();
    }

    public void restoreDeleteEntity(String name, boolean save) throws Exception {
        logger.info("Begin retore deleted entity");
        showDeletedEntities();
        openEntityEditPage(name);
        element("emp.restoreEntity").click();
        waitThat().timeout(1000);
        if (save) {
            logger.info("Click OK button");
            element("emp.restoreconfirm").click();
        } else {
            logger.info("Click cancel button");
            element("emp.cancelRestore").click();
            waitThat().timeout(1000);
            element("emp.closeEditEntityForm").click();
        }
        waitStatusDlg();
    }

    public String getEntityStatus(String name) throws Exception {
        logger.info("Get entity status");
        showDeletedEntities();
        if (element("emp.entityLabel", name).getAttribute("class").contains("greyTreeNodeClass")) {
            logger.info("Entity[" + name + "] is deleted");
            return "Deleted";
        } else {
            logger.info("Entity[" + name + "] is active");
            return "Active";
        }
    }

    public List<String> getAssignedReturns(String EntityName, String Product) throws Exception {
        List<String> returns = new ArrayList<String>();
        openAssignReturnPage(EntityName);
        String id = element("emp.ProductTab", Product).getAttribute("id") + "_data";
        int amt = (int) element("emp.returnTable", id).getRowCount();
        for (int i = 1; i <= amt; i++) {
            if (element("emp.edit.RetChekStat", id, String.valueOf(i)).getAttribute("class").contains("ui-state-active")) {
                returns.add(element("emp.edit.RetName", String.valueOf(i)).getInnerText());
            }
        }
        waitThat().timeout(500);
        closeEntityEditPage();
        logger.info("There are "+returns.size()+"  returns");
        return returns;
    }


    public ArrayList<String> getAssignedUserGroupPermissions(String EntityName, String returnName, String uGP) throws Exception {
        ArrayList<String> PGs = new ArrayList<String>();
        openAssignReturnPage(EntityName);
        openAssignPrivPage(returnName);

        for (String userGroup : getAllAssignUserGroup()) {
            String id = element("emp.getUG", userGroup).getAttribute("id").replace("header", "content");
            int amt = (int) element("emp.pgTab", id).getRowCount();
            String[] list = new String[]{id, "", ""};
            for (int r = 1; r <= amt; r++) {
                list[1] = String.valueOf(r);
                try {
                    for (int c = 1; c <= 3; c++) {
                        list[2] = String.valueOf(c);
                        PGs.add(element("emp.getPriv", list).getInnerText());

                    }
                } catch (NoSuchElementException e) {
                }
            }
        }

        element("emp.editReturnCancel").click();
        waitStatusDlg();
        closeEntityEditPage();
        return PGs;
    }


    public boolean isAddEntityBtnClick() throws Exception {
        boolean clickAble = true;
        try {
            if (element("emp.addEntityBtnStat").getAttribute("class").contains("ui-state-disabled"))
                clickAble = false;

        } catch (NoSuchElementException e) {

        }

        return clickAble;
    }

    public boolean isDeleteEntityBtnClick(String Entity) throws Exception {
        boolean elementClick = true;
        openEntityEditPage(Entity);
        try {
            if (element("emp.deleteEntity").getAttribute("class").contains("ui-state-disabled")) {
                elementClick = false;
            }
        } catch (NoSuchElementException e) {
            elementClick = false;
        }

        try {
            closeEntityEditPage();
        } catch (NoSuchElementException e) {
        }

        return elementClick;
    }

    public boolean isEditEntity(String Entity) throws Exception {
        boolean rst = true;
        try {
            openEntityEditPage(Entity);
            rst = element("emp.entityEditForm").isDisplayed();

        } catch (NoSuchElementException e) {
            rst = false;
        }

        try {
            closeEntityEditPage();
        } catch (NoSuchElementException e) {
        }

        return rst;
    }

    public boolean verifyDeleteEntityName(String Entity) throws Exception {
        if (element("emp.edit.DelEntityName").getInnerText().equals(Entity))
            return true;
        else
            return false;
    }

    public List<String> getAssignedReturnsListInDeleteEntity(String Entity, String Regulator) throws Exception {
        List<String> returnList = new ArrayList<String>();
        for (String product : getAllAssignProducts()) {
            String id = element("emp.assPro", product).getAttribute("id") + "_data";
            int amt = (int) element("emp.tabName2", id).getRowCount();
            String[] list = new String[]{id, "1"};
            for (int i = 1; i <= amt; i++) {
                list[1] = String.valueOf(i);
                returnList.add(element("emp.assRetName", list).getInnerText());
            }
        }

        return returnList;
    }

    public void closeEntityEditPage() throws Exception {
        if(element("emp.closeEditEntityForm").isDisplayed()){
			logger.info("Close entity manage panel");
        	element("emp.closeEditEntityForm").click();
            waitThat().timeout(1000);
        }
        
    }


    public List<String> getEntityInfo(String entityName) throws Exception {
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
        try {
            if (!element("emp.edit.slide").getAttribute("checked").equals("checked")) {
                entityInfo.add("Y");
            } else
                entityInfo.add("N");
        } catch (Exception e) {
            entityInfo.add("N");
        }
        closeEntityEditPage();
        return entityInfo;
    }

    public boolean isImportAble() throws Exception {
        if (element("emp.import").getAttribute("aria-disabled").equalsIgnoreCase("false"))
            return true;
        else
            return false;
    }

    public boolean isExportAble() throws Exception {
        if (element("emp.export").getAttribute("aria-disabled").equalsIgnoreCase("false"))
            return true;
        else
            return false;
    }

    public String exportAcessSettings() throws Exception {
        TestCaseManager.getTestCase().startTransaction("");
        TestCaseManager.getTestCase().setPrepareToDownload(true);
        element("emp.export").click();
        TestCaseManager.getTestCase().stopTransaction();
        return TestCaseManager.getTestCase().getDownloadFile();

    }

    public String importAcessSettings(String importFile) throws Exception {
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
        return TestCaseManager.getTestCase().getDownloadFile();

    }

    public void openAssignReturnPage(String EntityName) throws Exception {
        openEntityEditPage(EntityName);
        element("emp.edit.assRet").click();
        waitStatusDlg();
		waitThat().timeout(1000);
    }

    @Override
    public String parentFormId(String type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public By getImportBtn(String type) {
        // TODO Auto-generated method stub
        return null;
    }
}
