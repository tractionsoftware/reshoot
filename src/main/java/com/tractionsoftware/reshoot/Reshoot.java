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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tractionsoftware.reshoot.util.TractionWebdriverUtils;

public class Reshoot {

    /**
     * Takes a single argument for the configuration.
     */
    public static void main(String[] args) {

        CommandLine cmd = parseCommandLine(args);

        String[] files = cmd.getArgs();
        for (String file : files) {
            Configuration config = parseConfigurationFile(file);

            RemoteWebDriver driver = createDriver(cmd);
            for (Screenshot screenshot : config.screenshots) {
                takeSingleScreenshot(driver, config, screenshot);
            }
            driver.quit();
        }
    }

    private static CommandLine parseCommandLine(String[] args) {
        CommandLineParser parser = new GnuParser();
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();

        // create the command-line options
        Option chrome = OptionBuilder.withDescription("use Chrome (for retina screenshots on retina displays)").withLongOpt("chrome").create();
        Option firefox = OptionBuilder.withDescription("use Firefox (for full scrollheight screenshots)").withLongOpt("firefox").create();

        options.addOption(chrome);
        options.addOption(firefox);

        // attempt to parse them, printing help if it fails
        try {
            CommandLine cmd = parser.parse(options, args);

            // make sure we have some files to process
            if (cmd.getArgList().isEmpty()) {
                showUsageAndExit(options);
            }

            return cmd;
        }
        catch (ParseException e) {
            showUsageAndExit(options);
        }

        return null;
    }

    private static void showUsageAndExit(org.apache.commons.cli.Options options) {
        PrintWriter pw = new PrintWriter(System.out);

        pw.println("Reshoot uses Selenium WebDriver to automate shooting product screenshots");
        pw.println();
        pw.println("  Usage:");
        pw.println("    java -jar Reshoot.jar [options] [files...]");
        pw.println();
        pw.println("  Options:");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printOptions(pw, 74, options, 1, 2);

        pw.println();
        pw.println("  Documentation:");
        pw.println("    https://github.com/tractionsoftware/reshoot");
        pw.println();

        pw.flush();
        System.exit(1);
    }

    private static Configuration parseConfigurationFile(String file) {
        try (FileReader f = new FileReader(file)) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(f, Configuration.class);
        }
        catch (IOException e) {
            System.err.println("Error reading "+file+":");
            e.printStackTrace();
            return null;
        }
    }

    private static RemoteWebDriver createDriver(CommandLine cmd) {
        if (cmd.hasOption("firefox")) {
            return TractionWebdriverUtils.createFirefoxDriver();
        }
        else {
            // cmd.hasOption("chrome")
            return TractionWebdriverUtils.createChromeDriver();
        }
    }

    private static void takeSingleScreenshot(RemoteWebDriver driver, Configuration configuration, Screenshot screenshot) {
        driver.get(screenshot.url);

        Setup setup = getSetup(configuration, screenshot);
        if (setup != null) {
            setup.setup(driver, configuration, screenshot);
        }

        Region browser = deferIfNull(screenshot.browser, configuration.browser);
        setBrowserSize(driver, browser);

        saveScreenshot(driver, screenshot);

        if (setup != null) {
            setup.teardown(driver, configuration, screenshot);
        }
    }

    private static Setup getSetup(Configuration configuration, Screenshot screenshot) {
        if (configuration.setups != null && screenshot.setup != null) {
            Class cls;
            try {
                Map<String,String> setup = configuration.setups.get(screenshot.setup);
                cls = Class.forName(setup.get("class"));
                Setup s = (Setup) cls.newInstance();
                s.init(setup);
                return s;
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T deferIfNull(T... list) {
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

    private static boolean saveScreenshot(RemoteWebDriver driver, Screenshot screenshot) {

        // get the raw png bytes
        byte[] pngBytes = driver.getScreenshotAs(OutputType.BYTES);

        try (InputStream in = new ByteArrayInputStream(pngBytes);
                FileOutputStream out = new FileOutputStream(screenshot.output)) {

            // read from bytes
            BufferedImage img = ImageIO.read(in);

            // crop and zoom
            img = cropImage(driver, img, screenshot);
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

    private static BufferedImage cropImage(RemoteWebDriver driver, BufferedImage img, Screenshot screenshot) {
        if (screenshot.crop != null) {
            Region crop = screenshot.crop;
            int left = crop.left;
            int top = crop.top;
            int width = crop.width;
            int height = crop.height;

            // retina images will be twice the size, but we want to
            // keep the crop in normal browser coordinates.
            if (isRetina(driver, img)) {
                left *= 2;
                top *= 2;
                width *= 2;
                height *= 2;
            }

            return img.getSubimage(left, top, width, height);
        }
        return img;
    }

    private static boolean isRetina(RemoteWebDriver driver, BufferedImage img) {
        Dimension size = driver.manage().window().getSize();
        return (size.width == img.getWidth() / 2);
    }

//    private static Image zoomImage(BufferedImage img, Screenshot screenshot) {
//        if (screenshot.zoom != 1) {
//            int width = (int) (img.getWidth() * screenshot.zoom);
//            return img.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
//        }
//        return img;
//    }

}
