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
