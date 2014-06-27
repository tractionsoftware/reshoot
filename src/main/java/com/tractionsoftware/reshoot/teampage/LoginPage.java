/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2014 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.
package com.tractionsoftware.reshoot.teampage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {

    protected final WebDriver driver;

    @FindBy(id = "username")
    public WebElement username;

    @FindBy(id = "password")
    public WebElement password;

    @FindBy(css = "#loginbutton button")
    public WebElement button;

    public LoginPage(WebDriver driver) {
        this.driver = driver;

        if (!driver.getTitle().contains("Log In")) {
            throw new IllegalStateException("This page title does not contain: Log In");
        }
    }

    public void login(String name, String pw) {
        setUsername(name);
        setPassword(pw);
        login();
    }

    public void setUsername(String name) {
        username.clear();
        username.sendKeys(name);
    }

    public void setPassword(String pw) {
        password.clear();
        password.sendKeys(pw);
    }

    public void login() {
        button.click();
    }

}
