/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2014 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.
package com.tractionsoftware.reshoot.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class TractionWebdriverUtils {

    public static RemoteWebDriver createDriver() {
        return createFirefoxDriver();
        //createIE8Driver();
        //createIE9Driver();
        //createChromeDriver();
    }

    /**
     * You need to be running the chromedriver command line server,
     * located in ~/src/webdriver/chromedriver on my system and
     * available for download below.
     *
     * http://code.google.com/p/selenium/wiki/ChromeDriver
     * @return
     */
    public static RemoteWebDriver createChromeDriver() {
        try {
            return new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static FirefoxDriver createFirefoxDriver() {
        return new FirefoxDriver();
    }

    public static RemoteWebDriver createIEDriver(String version) {
        DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
        try {
            capability.setVersion(version);
            capability.setPlatform(Platform.WINDOWS);
            capability.setBrowserName("internet explorer");
            return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static RemoteWebDriver createIE7Driver() {
        return createIEDriver("7");
    }

    public static RemoteWebDriver createIE8Driver() {
        return createIEDriver("8");
    }

    public static RemoteWebDriver createIE9Driver() {
        return createIEDriver("9");
    }

    public static WebElement waitForElement(WebDriver driver, By by) {
        return waitForElement(driver, by, 10);
    }
    public static WebElement waitForElement(WebDriver driver, final By by, int seconds) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        WebElement elm = wait.until(new Function<WebDriver,WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(by);
            }
        });
        return elm;
    }

}
