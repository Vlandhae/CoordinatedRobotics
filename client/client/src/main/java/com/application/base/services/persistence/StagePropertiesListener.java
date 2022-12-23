package com.application.base.services.persistence;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StagePropertiesListener implements ChangeListener {

    private boolean hasChangedWidth;
    private boolean hasChangedHeight;
    private boolean hasChangedX;
    private boolean hasChangedY;
    private boolean hasChangedMaximized;

    private double windowWidth;
    private double windowHeight;
    private double windowX;
    private double windowY;
    private boolean isMaximized;

    private Stage stage;

    public StagePropertiesListener() {
        this.hasChangedWidth = false;
        this.hasChangedHeight = false;
        this.hasChangedX = false;
        this.hasChangedY = false;
        this.hasChangedMaximized = false;
        this.stage = null;
        this.windowWidth = 0;
        this.windowHeight = 0;
        this.windowX = 0;
        this.windowY = 0;
        this.isMaximized = false;
    }

    public void initializeListener(Stage stage) {
        this.stage = stage;
        this.windowWidth = stage.getWidth();
        this.windowHeight = stage.getHeight();
        this.windowX = stage.getX();
        this.windowY = stage.getY();
        this.isMaximized = stage.isMaximized();
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if ((this.windowWidth != stage.getWidth()) && (!stage.isMaximized())) {
            this.hasChangedWidth = true;
            this.windowWidth = stage.getWidth();
        }
        if ((this.windowHeight != stage.getHeight()) && (!stage.isMaximized())) {
            this.hasChangedHeight = true;
            this.windowHeight = stage.getHeight();
        }
        if ((this.windowX != stage.getX()) && (!stage.isMaximized()) && (stage.getX() > 0)) {
            this.hasChangedX = true;

            if ((stage.getX() > (Screen.getPrimary().getBounds().getMaxX()) - 400)) {
                this.windowX = Screen.getPrimary().getBounds().getMaxX() - 400;
            } else {
                this.windowX = stage.getX();
            }
        }
        if ((this.windowY != stage.getY()) && (!stage.isMaximized()) && (stage.getY() > 0)) {
            this.hasChangedY = true;

            if ((stage.getY() > (Screen.getPrimary().getBounds().getMaxY()) - 400)) {
                this.windowY = Screen.getPrimary().getBounds().getMaxY() - 400;
            } else {
                this.windowY = stage.getY();
            }
        }
        if (this.isMaximized != stage.isMaximized()) {
            this.hasChangedMaximized = true;
            this.isMaximized = stage.isMaximized();
        }
    }

    public boolean hasChangedHeight() {
        return hasChangedHeight;
    }

    public boolean hasChangedWidth() {
        return hasChangedWidth;
    }

    public boolean hasChangedX() {
        return hasChangedX;
    }

    public boolean hasChangedY() {
        return hasChangedY;
    }

    public boolean hasChangedMaximized() {
        return hasChangedMaximized;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public double getWindowX() {
        return windowX;
    }

    public double getWindowY() {
        return windowY;
    }

    public boolean getIsMaximized() {
        return isMaximized;
    }
}
