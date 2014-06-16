package com.tractionsoftware.reshoot;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tractionsoftware.reshoot.pages.LoginPage;
import com.tractionsoftware.reshoot.pages.ProteusPage;
import com.tractionsoftware.reshoot.util.TractionWebdriverUtils;

public class Reshoot {

    /**
     * Represents the configuration for the screenshots which is all
     * done using JSON. The username and password provided here will
     * be used as a default if there is none provided for a specific
     * screenshot.
     */
    public static final class Configuration {

        public String username;
        public String password;

        /**
         * A list of screenshots to take.
         */
        public Screenshot[] screenshots;

        /**
         * Identifies the default size of the browser. The left and top
         * properties of Region are ignored.
         */
        public Region browser = null;
    }

    /**
     * Represents the data for a single screenshot using JSON.
     */
    public static final class Screenshot {

        /**
         * Eventually we could support per-screenshot permissions, but
         * it's fine to just run a separate instance for now. If we
         * try to run with different than default username/password,
         * we would have to logout and then login again.
         */
        public String username;
        public String password;

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
        //public float zoom = 1;

        /**
         * Omit for no crop.
         */
        public Region crop = null;

        /**
         * Identifies the size of the browser. The left and top
         * properties of Region are ignored.
         */
        public Region browser = null;
    }

    /**
     * A Region is used to identify the crop area. -1 can be used to
     * use the full width or height of the image (removing the top or
     * left portion).
     */
    public static final class Region {

        public int left = 0;
        public int top = 0;
        public int width = -1;
        public int height = -1;

    }

    /**
     * Takes a single argument for the configuration.
     */
    public static void main(String[] args) {

        CommandLine cmd = parseCommandLine(args);
        Configuration config = parseConfigurationFile(cmd.getOptionValue('f'));

        RemoteWebDriver driver = createDriver();
        for (Screenshot screenshot : config.screenshots) {
            takeSingleScreenshot(driver, config, screenshot);
        }

        driver.quit();
    }

    private static void takeSingleScreenshot(RemoteWebDriver driver, Configuration configuration, Screenshot screenshot) {
        driver.get(screenshot.url);

        // support top-level defaults
        String username = notNull(screenshot.username, configuration.username);
        String password = notNull(screenshot.password, configuration.password);
        Region browser = notNull(screenshot.browser, configuration.browser);

        try {
            LoginPage login = PageFactory.initElements(driver, LoginPage.class);
            login.login(username, password);
        }
        catch (Exception e) {}

        ProteusPage home = new ProteusPage(driver, "");
        PageFactory.initElements(driver, home);
        home.waitForLoadingCycle();

        setBrowserSize(driver, browser);

        saveScreenshot(driver, screenshot);
    }

    private static <T> T notNull(T... list) {
        for (T t : list) {
            if (t != null) return t;
        }
        return null;
    }

    private static int getDimension(int specified, int current) {
        if (specified > 0) return specified; else return current;
    }

    private static void setBrowserSize(RemoteWebDriver driver, Region region) {
        if (region != null && (region.width > 0 || region.height > 0)) {
            Options manage = driver.manage();
            Window window = manage.window();

            Dimension current = window.getSize();

            int width = getDimension(region.width, current.width);
            int height = getDimension(region.height, current.height);

            window.setSize(new Dimension(width, height));
        }
    }

    private static Configuration parseConfigurationFile(String file) {
        try (FileReader f = new FileReader(file)) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(f, Configuration.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static CommandLine parseCommandLine(String[] args) {
        CommandLineParser parser = new GnuParser();
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();

        // create the command-line options
        Option f = OptionBuilder.hasArg().isRequired().withDescription("json configuration file").create('f');

        options.addOption(f);

        // attempt to parse them, printing help if it fails
        try {
            return parser.parse(options, args);
        }
        catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("com.tractionsoftware.screenshots.Screenshots", options);
            System.exit(1);
        }

        return null;
    }

    private static RemoteWebDriver createDriver() {
        return TractionWebdriverUtils.createFirefoxDriver();
        //return TractionWebdriverUtils.createChromeDriver();
    }

    private static boolean saveScreenshot(TakesScreenshot driver, Screenshot screenshot) {

        // get the raw png bytes
        byte[] pngBytes = driver.getScreenshotAs(OutputType.BYTES);

        try (InputStream in = new ByteArrayInputStream(pngBytes);
                FileOutputStream out = new FileOutputStream(screenshot.output)) {

            // read from bytes
            BufferedImage img = ImageIO.read(in);

            // crop and zoom
            img = cropImage(img, screenshot);
//            Image result = zoomImage(img, screenshot);

            // save to file
            ImageIO.write(img, "png", out);

            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    private static BufferedImage cropImage(BufferedImage img, Screenshot screenshot) {
        if (screenshot.crop != null) {
            Region crop = screenshot.crop;
            return img.getSubimage(crop.left, crop.top, crop.width, crop.height);
        }
        return img;
    }

//    private static Image zoomImage(BufferedImage img, Screenshot screenshot) {
//        if (screenshot.zoom != 1) {
//            int width = (int) (img.getWidth() * screenshot.zoom);
//            return img.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
//        }
//        return img;
//    }

    @SuppressWarnings("unused")
    private static void takeBrowserScreenshot(TakesScreenshot driver, File destFile) throws IOException {

        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, destFile);
    }

    @SuppressWarnings("unused")
    private static void takeFullscreenScreenshot(WebDriver driver, File destFile) throws IOException {

        BufferedImage image;
        try {
            image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", destFile);
        }
        catch (HeadlessException | AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
