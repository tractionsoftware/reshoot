/*
 * Copyright 2014 Traction Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tractionsoftware.reshoot.teampage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

/**
 * @author andy
 */
public class ProteusPage {

    public static final int DEFAULT_WAIT_SECONDS = 60;
    public static final int LOADING_WAIT_SECONDS = 5;

    protected final WebDriver driver;

    public ProteusPage(WebDriver driver, String htmlTitlePrefix) {
        this.driver = driver;

        if (!isProteusPage(driver)) {
            throw new IllegalStateException("This page does not appear to be a Proteus page.");
        }

        waitForProteusIdle(60);

        // make sure we're on the right page. this avoids
        if (!driver.getTitle().startsWith(htmlTitlePrefix)) {
            throw new IllegalStateException("This page title does not start with: "+htmlTitlePrefix);
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * Checks to see if this is a Proteus page.
     */
    public static final boolean isProteusPage(WebDriver driver) {
        try {
            return driver.findElement(By.id("proteus-color-css")) != null;
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

    // ----------------------------------------------------------------------
    // important parts

    public WebElement getMainTab(int index) {
        index++;
        return driver.findElement(By.xpath("//ul[@id='primary-tabs']/li["+index+"]"));
    }

    public WebElement getMainTabLink(int index) {
        return getMainTab(index).findElement(By.xpath("a"));
    }

    // ----------------------------------------------------------------------
    // main actions

    public void doClickMainTabLinkAndWait(int index) {
        getMainTabLink(index).click();
        waitForLoadingCycle();
    }

    // ----------------------------------------------------------------------
    // waits

    /**
     * Wait for the ajax to complete
     */
    public void waitForLoadingCycle() {
        waitForLoadingCycle(DEFAULT_WAIT_SECONDS);
    }

    public void waitForLoadingCycle(int timeoutSeconds) {
        try {
            waitForProteusLoading(LOADING_WAIT_SECONDS);
        }
        catch (Exception e) {}
        waitForProteusIdle(timeoutSeconds);
    }

    public void waitForFormLoad(int timeoutSeconds) {
        waitForClassName(timeoutSeconds, "fm-dialog");
    }

    public void waitForProteusLoading(int timeoutSeconds) {
        waitForClassName(timeoutSeconds, "proteus-view-loading");
    }

    public void waitForProteusIdle(int timeoutSeconds) {
        waitForClassName(timeoutSeconds, "proteus-view-idle");
    }

    public void waitForClassName(int timeoutSeconds, final String className) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeoutSeconds, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        wait.until(new Function<WebDriver,WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.className(className));
            }
        });
    }

    public void waitForClassName(int timeoutSeconds, final String className, WebElement scope) {
        Wait<WebElement> wait = new FluentWait<WebElement>(scope).withTimeout(timeoutSeconds, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        wait.until(new Function<WebElement,WebElement>() {
            @Override
            public WebElement apply(WebElement scope) {
                return scope.findElement(By.className(className));
            }
        });
    }

    public WebElement getSideColumn() {
        return driver.findElement(By.xpath("//div[@id='sidebar'][1]"));
    }

    public WebElement getMainAddButton() {
        return getSideColumn().findElement(By.xpath("//div[contains(@class,'add-button')][1]/div/a[contains(@class,'gwt-Anchor')]"));
    }

    public List<WebElement> getOtherAddButtons() {
        return getSideColumn().findElements(By.xpath("//div[contains(@class,'add-drop')][1]/div/a[contains(@class,'gwt-Anchor')]"));
    }

    public WebElement getFormDialog() {
        return driver.findElement(By.xpath("//div[contains(@class,'fm-dialog')]"));
    }

    public WebElement getFormTitleElement() {
        WebElement fmDialog = getFormDialog();
        if (fmDialog == null) {
            return null;
        }
        return fmDialog.findElement(By.xpath("//div[contains(@class,'Caption')]"));
    }

    public String getFormTitle() {
        WebElement element = getFormTitleElement();
        if (element == null) {
            return null;
        }
        return element.getText();
    }

    public String getFormId() {
        WebElement fmDialog = getFormDialog();
        if (fmDialog == null) {
            return null;
        }
        return fmDialog.getAttribute("id");
    }

    public WebElement getAddMenuButton(String linkText) {
        WebElement addButton = getMainAddButton();
        if (linkText.equals(addButton.getText())) {
            return addButton;
        }
        for (WebElement otherAddButton : getOtherAddButtons()) {
            if (linkText.equals(otherAddButton.getText())) {
                return otherAddButton;
            }
        }
        return null;
    }

    public void clickAddButtonAndWait(String linkText) {
        getAddMenuButton("New Article").click();
        waitForFormLoad(DEFAULT_WAIT_SECONDS);
    }

    public WebElement getFormFieldByClass(String className) {
        return getFormDialog().findElement(By.className(className));
    }

}
