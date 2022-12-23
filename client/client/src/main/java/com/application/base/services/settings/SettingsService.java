package com.application.base.services.settings;

import com.application.base.services.persistence.PersistenceService;

public class SettingsService {

    private PersistenceService persistenceService;

    public SettingsService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public double getWindowWidth() {
        return persistenceService.getWindowWidth();
    }

    public double getWindowHeight() {
        return persistenceService.getWindowHeight();
    }

    public double getWindowX() {
        return persistenceService.getWindowX();
    }

    public double getWindowY() {
        return persistenceService.getWindowY();
    }

    public String getLocale() {
        return persistenceService.getLocale();
    }

    public VisibilityState getLeftPartState() {
        return persistenceService.getLeftPartState();
    }

    public double getWidthOfLeftPart() {
        return persistenceService.getWidthOfLeftPart();
    }

    public VisibilityState getRightPartState() {
        return persistenceService.getRightPartState();
    }

    public double getWidthOfRightPart() {
        return persistenceService.getWidthOfRightPart();
    }

    public VisibilityState getBottomPartState() {
        return persistenceService.getBottomPartState();
    }

    public double getHeightOfBottomPart() {
        return persistenceService.getHeightOfBottomPart();
    }

    public boolean isWindowMaximized() { return persistenceService.isWindowMaximized(); }
}