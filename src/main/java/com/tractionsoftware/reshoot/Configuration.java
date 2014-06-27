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
