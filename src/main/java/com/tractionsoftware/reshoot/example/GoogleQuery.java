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
