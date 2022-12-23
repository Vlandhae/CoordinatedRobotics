package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.main.MainClass;
import com.application.base.main.MainController;
import com.application.base.services.language.LanguageService;
import com.application.base.services.persistence.StagePropertiesListener;
import com.application.base.services.settings.SettingsService;
import com.application.base.services.settings.VisibilityState;

public class SaveWindowPropertiesAction extends Action {

    public SaveWindowPropertiesAction(LanguageService languageService) {
        super(languageService);
        this.setNameVisible(true);
        this.setImageView(null);
        this.setShortCutData(null);
        this.setDescription(this.languageService.getString("SaveWindowPropertiesActionDescription"));
        this.setName(this.languageService.getString("SaveWindowPropertiesActionName"));
        this.setDisplayName(this.languageService.getString("SaveWindowPropertiesActionDisplayName"));
        this.setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return true;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        MainClass mainClass = eventArgs.mainClass();
        StagePropertiesListener listener
                = mainClass.getServiceManager().getPersistenceService().getStagePropertiesListener();
        MainController controller = mainClass.getMainController();
        SettingsService settings = mainClass.getServiceManager().getSettingsService();

        double windowWidth;
        if (listener.hasChangedWidth()) {
            windowWidth = listener.getWindowWidth();
        } else {
            windowWidth = settings.getWindowWidth();
        }
        double windowHeight;
        if (listener.hasChangedHeight()) {
            windowHeight = listener.getWindowHeight();
        } else {
            windowHeight = settings.getWindowHeight();
        }
        double windowX;
        if (listener.hasChangedX()) {
            windowX = listener.getWindowX();
        } else {
            windowX = settings.getWindowX();
        }
        double windowY;
        if (listener.hasChangedY()) {
            windowY = listener.getWindowY();
        } else {
            windowY = settings.getWindowY();
        }
        boolean isMaximized;
        if (listener.hasChangedMaximized()) {
            isMaximized = listener.getIsMaximized();
        } else {
            isMaximized = settings.isWindowMaximized();
        }

        String locale = mainClass.getServiceManager().getLanguageService().getLocale();
        VisibilityState leftPartState = controller.getLeftPartState();
        double widthOfLeftPart = controller.getLeftPartWidth();
        if (widthOfLeftPart == 0.0) {
            widthOfLeftPart = settings.getWidthOfLeftPart();
        }
        VisibilityState rightPartState = controller.getRightPartState();
        double widthOfRightPart = controller.getRightPartWidth();
        if (widthOfRightPart == 0.0) {
            widthOfRightPart = settings.getWidthOfRightPart();
        }
        VisibilityState bottomPartState = controller.getBottomPartState();
        double heightOfBottomPart = controller.getBottomPartHeight();
        if (heightOfBottomPart == 0.0) {
            heightOfBottomPart = settings.getHeightOfBottomPart();
        }

        mainClass.getServiceManager().getPersistenceService().overrideSettings(windowWidth,
                windowHeight, windowX, windowY, locale, leftPartState, widthOfLeftPart,
                rightPartState, widthOfRightPart, bottomPartState, heightOfBottomPart, isMaximized);
    }
}
