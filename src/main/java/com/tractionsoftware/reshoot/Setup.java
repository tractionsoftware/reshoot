package com.tractionsoftware.reshoot;

import java.util.Map;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Allows a page to be setup before shooting the screenshot.
 *
 * @author andy
 */
public class Setup {

    private Map<String,String> data = null;
    public void init(Map<String,String> data) {
        this.data = data;
    }

    public String getProperty(String name) {
        return data.get(name);
    }

    public void setup(RemoteWebDriver driver, Configuration configuration, Screenshot screenshot) {}
    public void teardown(RemoteWebDriver driver, Configuration configuration, Screenshot screenshot) {}

}
