package com.tractionsoftware.reshoot;

/**
 * A Region is used to identify the crop area. -1 can be used to use
 * the full width or height of the image (removing the top or left
 * portion).
 */
public class Region {

    public int left = 0;
    public int top = 0;
    public int width = -1;
    public int height = -1;

}
