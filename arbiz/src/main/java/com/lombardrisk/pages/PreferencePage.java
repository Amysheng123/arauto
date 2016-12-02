package com.lombardrisk.pages;

import org.yiwan.webcore.web.IWebDriverWrapper;

/**
 * Created by Kevin Ling on 2/25/15.
 * Refactored by Leo Tu on 1/29/16
 */
public class PreferencePage extends AbstractPage {
    public PreferencePage(IWebDriverWrapper webDriverWrapper) {
        super(webDriverWrapper);
    }

    protected boolean isTimeZoneCheck() throws Exception {
        return element("prp.TZC").isChecked();
    }

    protected PreferencePage setTimeZoneCheck(boolean isChecked) throws Exception {
        if (element("prp.TZC").isChecked()) {
            element("prp.TZC").check(false);
        }
        return this;
    }

    protected boolean isTimeZoneSelectExist() throws Exception {
        return element("prp.TZS").isChecked();
    }

    protected PreferencePage setTimeZoneSelect(String timezone) throws Exception {
        element("prp.TZS").selectByVisibleText(timezone);
        return this;
    }

    protected boolean isLanguageCheck() throws Exception {
        return element("prp.LC").isChecked();
    }

    protected PreferencePage setLanguageCheck(boolean isChecked) throws Exception {
        if (element("prp.LC").isChecked()) {
            element("prp.LC").check(false);
        }
        return this;
    }

    protected boolean isLanguageSelectExist() throws Exception {
        return element("prp.LS").isChecked();
    }

    protected PreferencePage setLanguageSelect(String language) throws Exception {
        element("prp.LS").selectByVisibleText(language);
        return this;
    }

    protected ListPage saveSetting() throws Exception {
        element("prp.confirm").click();
        return new ListPage(getWebDriverWrapper());
    }

    protected ListPage cancelSetting() throws Exception {
        element("prp.canel").click();
        return new ListPage(getWebDriverWrapper());
    }


}
