package com.tractionsoftware.reshoot.teampage;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import com.tractionsoftware.reshoot.Configuration;
import com.tractionsoftware.reshoot.Screenshot;
import com.tractionsoftware.reshoot.Setup;

public class ProteusLogin extends Setup {

    @Override
    public void setup(RemoteWebDriver driver, Configuration configuration, Screenshot screenshot) {

        // support top-level defaults
        String username = getProperty("username");
        String password = getProperty("password");

        try {
            // attempt to identify and use the login page
            LoginPage login = PageFactory.initElements(driver, LoginPage.class);
            login.login(username, password);
        }
        catch (Exception e) {}

        try {
            // wait to allow proteus to load content
            ProteusPage proteus = new ProteusPage(driver, "");
            PageFactory.initElements(driver, proteus);
            proteus.waitForLoadingCycle();
        }
        catch (Exception e) {}

    }

}
