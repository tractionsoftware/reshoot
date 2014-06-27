package com.tractionsoftware.reshoot;

/**
 * Represents the data for a single screenshot using JSON.
 */
public class Screenshot {

    /**
     * The page of which a screenshot will be taken.
     */
    public String url;

    /**
     * The path of the output png file.
     */
    public String output;

    /**
     * A value of 1 indicates no zoom.
     */
    // public float zoom = 1;

    /**
     * Omit for no crop.
     */
    public Region crop = null;

    /**
     * Identifies the size of the browser. The left and top properties
     * of Region are ignored.
     */
    public Region browser = null;

    /**
     * Class name that will be called to prepare the browser for
     * capture. This can be used to click buttons, open dialogs, open
     * menus, etc.
     */
    public String setup = null;

}
