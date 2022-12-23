package com.application.base.services.persistence;

import com.application.base.services.settings.VisibilityState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class PersistenceService {

    private static final String DEFAULT_SETTINGS_PATH = "com.application.base/services/persistence/settings.properties";
    private static final String FILE_NAME = "layout.properties";
    private static final String WINDOW_WIDTH = "WindowWidth";
    private static final String WINDOW_HEIGHT = "WindowHeight";
    private static final String WINDOW_X = "WindowX";
    private static final String WINDOW_Y = "WindowY";
    private static final String LOCALE = "Locale";
    private static final String LEFT_PART_STATE = "LeftPartState";
    private static final String LEFT_PART_WIDTH = "WidthOfLeftPart";
    private static final String RIGHT_PART_STATE = "RightPartState";
    private static final String RIGHT_PART_WIDTH = "WidthOfRightPart";
    private static final String BOTTOM_PART_STATE = "BottomPartState";
    private static final String BOTTOM_PART_HEIGHT = "HeightOfBottomPart";
    private static final String IS_MAXIMIZED = "IsMaximized";

    private StagePropertiesListener stagePropertiesListener;

    private Properties properties;
    private Properties defaultProperties;

    private String settingsPath;

    public PersistenceService() {
        this.stagePropertiesListener = new StagePropertiesListener();
        this.properties = this.readProperties();
        this.defaultProperties = this.readDefaultProperties();
    }

    private Properties getProperties() {
        if (!properties.isEmpty()) {
            return properties;
        }
        return defaultProperties;
    }

    private Properties getDefaultProperties() {
        return defaultProperties;
    }

    private Properties readDefaultProperties() {
        try {
            Properties properties = new Properties();
            InputStream configStream = PersistenceService.class.getClassLoader().getResourceAsStream(DEFAULT_SETTINGS_PATH);
            properties.load(configStream);
            configStream.close();
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public StagePropertiesListener getStagePropertiesListener() {
        return stagePropertiesListener;
    }

    private Properties readProperties() {
        Properties properties = new Properties();

        try {
            settingsPath = new File(".").getCanonicalPath() + "/" + FILE_NAME;

            if (settingsExist()) {
                FileInputStream configStream = new FileInputStream(settingsPath);
                properties.load(configStream);
                configStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public boolean settingsExist() {
        return new File(settingsPath).exists();
    }

    public double getWindowWidth() {
        return Double.parseDouble(getProperties().getProperty(WINDOW_WIDTH));
    }

    public double getDefaultWindowWidth() {
        return Double.parseDouble(getDefaultProperties().getProperty(WINDOW_WIDTH));
    }

    public double getWindowHeight() {
        return Double.parseDouble(getProperties().getProperty(WINDOW_HEIGHT));
    }

    public double getDefaultWindowHeight() {
        return Double.parseDouble(getDefaultProperties().getProperty(WINDOW_HEIGHT));
    }

    public double getWindowX() {
        return Double.parseDouble(getProperties().getProperty(WINDOW_X));
    }

    public double getDefaultWindowX() {
        return Double.parseDouble(getDefaultProperties().getProperty(WINDOW_X));
    }

    public double getWindowY() {
        return Double.parseDouble(getProperties().getProperty(WINDOW_Y));
    }

    public double getDefaultWindowY() {
        return Double.parseDouble(getDefaultProperties().getProperty(WINDOW_Y));
    }

    public String getLocale() {
        return getProperties().getProperty(LOCALE);
    }

    public String getDefaultLocale() {
        return getDefaultProperties().getProperty(LOCALE);
    }

    public VisibilityState getLeftPartState() {
        return VisibilityState.valueOf(getProperties().getProperty(LEFT_PART_STATE));
    }

    public VisibilityState getDefaultLeftPartState() {
        return VisibilityState.valueOf(getDefaultProperties().getProperty(LEFT_PART_STATE));
    }

    public double getWidthOfLeftPart() {
        return Double.parseDouble(getProperties().getProperty(LEFT_PART_WIDTH));
    }

    public double getDefaultWidthOfLeftPart() {
        return Double.parseDouble(getDefaultProperties().getProperty(LEFT_PART_WIDTH));
    }

    public VisibilityState getRightPartState() {
        return VisibilityState.valueOf(getProperties().getProperty(RIGHT_PART_STATE));
    }

    public VisibilityState getDefaultRightPartState() {
        return VisibilityState.valueOf(getDefaultProperties().getProperty(RIGHT_PART_STATE));
    }

    public double getWidthOfRightPart() {
        return Double.parseDouble(getProperties().getProperty(RIGHT_PART_WIDTH));
    }

    public double getDefaultWidthOfRightPart() {
        return Double.parseDouble(getDefaultProperties().getProperty(RIGHT_PART_WIDTH));
    }

    public VisibilityState getBottomPartState() {
        return VisibilityState.valueOf(getProperties().getProperty(BOTTOM_PART_STATE));
    }

    public VisibilityState getDefaultBottomPartState() {
        return VisibilityState.valueOf(getDefaultProperties().getProperty(BOTTOM_PART_STATE));
    }

    public double getHeightOfBottomPart() {
        return Double.parseDouble(getProperties().getProperty(BOTTOM_PART_HEIGHT));
    }

    public double getDefaultHeightOfBottomPart() {
        return Double.parseDouble(getDefaultProperties().getProperty(BOTTOM_PART_HEIGHT));
    }

    public boolean isWindowMaximized() {
        return Boolean.valueOf(getProperties().getProperty(IS_MAXIMIZED));
    }

    public boolean isDefaultWindowMaximized() {
        return Boolean.valueOf(getDefaultProperties().getProperty(IS_MAXIMIZED));
    }

    public void overrideSettings(double windowWidth, double windowHeight, double windowX, double windowY, String locale,
    VisibilityState leftPartState, double widthOfLeftPart, VisibilityState rightPartState, double widthOfRightPart,
    VisibilityState bottomPartState, double heightOfBottomPart, boolean isMaximized) {

        if (!settingsExist()) {
            try {
                File file = new File(settingsPath);
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Properties newProperties = new Properties();

        newProperties.setProperty(WINDOW_WIDTH, String.valueOf(windowWidth));
        newProperties.setProperty(WINDOW_HEIGHT, String.valueOf(windowHeight));
        newProperties.setProperty(WINDOW_X, String.valueOf(windowX));
        newProperties.setProperty(WINDOW_Y, String.valueOf(windowY));
        newProperties.setProperty(LOCALE, locale);
        newProperties.setProperty(LEFT_PART_STATE, leftPartState.toString());
        newProperties.setProperty(LEFT_PART_WIDTH, String.valueOf(widthOfLeftPart));
        newProperties.setProperty(RIGHT_PART_STATE, rightPartState.toString());
        newProperties.setProperty(RIGHT_PART_WIDTH, String.valueOf(widthOfRightPart));
        newProperties.setProperty(BOTTOM_PART_STATE, bottomPartState.toString());
        newProperties.setProperty(BOTTOM_PART_HEIGHT, String.valueOf(heightOfBottomPart));
        newProperties.setProperty(IS_MAXIMIZED, String.valueOf(isMaximized));

        try {
            FileOutputStream output = new FileOutputStream(settingsPath);
            newProperties.store(output, "Layout settings.");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
