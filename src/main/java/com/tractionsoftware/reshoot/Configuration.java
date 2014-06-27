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
package com.tractionsoftware.reshoot;

import java.util.Map;

/**
 * Represents the configuration for the screenshots which is all done
 * using JSON. The username and password provided here will be used as
 * a default if there is none provided for a specific screenshot.
 */
public final class Configuration {

    /**
     * A list of screenshots to take.
     */
    public Screenshot[] screenshots;

    /**
     * Identifies the default size of the browser. The left and top
     * properties of Region are ignored.
     */
    public Region browser = null;

    /**
     * Setups that can be referenced in screenshots.
     */
    public Map<String,Map<String,String>> setups = null;
}
