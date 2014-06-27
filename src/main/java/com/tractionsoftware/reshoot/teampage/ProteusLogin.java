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
