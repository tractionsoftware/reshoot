package com.tractionsoftware.reshoot.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.tractionsoftware.reshoot.Configuration;
import com.tractionsoftware.reshoot.Screenshot;
import com.tractionsoftware.reshoot.Setup;
import com.tractionsoftware.reshoot.util.TractionWebdriverUtils;

public class GoogleQuery extends Setup {

    @Override
    public void setup(RemoteWebDriver driver, Configuration configuration, Screenshot screenshot) {
        WebElement query = driver.findElementByName("q");
        if (query != null) {
            String q = getProperty("query");
            if (q != null) {
                query.sendKeys(q);
            }
        }
        TractionWebdriverUtils.waitForElement(driver, By.id("resultStats"));
    }

}
