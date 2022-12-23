package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.persistence.PersistenceService;
import com.application.base.services.settings.VisibilityState;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class RestoreWindowPropertiesAction extends Action {

    public RestoreWindowPropertiesAction(LanguageService languageService) {
        super(languageService);
        this.nameVisible = true;
        this.imageView = null;
        this.setDescription(this.languageService.getString("RestoreWindowPropertiesActionDescription"));
        this.setName(this.languageService.getString("RestoreWindowPropertiesActionName"));
        this.setDisplayName(this.languageService.getString("RestoreWindowPropertiesActionDisplayName"));
        this.multiselectSupported = true;
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return true;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {

        PersistenceService persistenceService = eventArgs.mainClass().getServiceManager().getPersistenceService();

        double windowWidth = persistenceService.getDefaultWindowWidth();
        double windowHeight = persistenceService.getDefaultWindowHeight();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        double windowX = (primScreenBounds.getWidth() - windowWidth) / 2;
        double windowY = (primScreenBounds.getHeight() - windowHeight) / 2;
        boolean isMaximized = persistenceService.isDefaultWindowMaximized();
        String locale = persistenceService.getLocale(); //Locale is not changed.
        VisibilityState leftPartState = persistenceService.getDefaultLeftPartState();
        double widthOfLeftPart = persistenceService.getDefaultWidthOfLeftPart();
        VisibilityState rightPartState = persistenceService.getDefaultRightPartState();
        double widthOfRightPart = persistenceService.getDefaultWidthOfRightPart();
        VisibilityState bottomPartState = persistenceService.getDefaultBottomPartState();
        double heightOfBottomPart = persistenceService.getDefaultHeightOfBottomPart();

        eventArgs.mainClass().getServiceManager().getPersistenceService().overrideSettings(windowWidth,
                windowHeight, windowX, windowY, locale, leftPartState, widthOfLeftPart,
                rightPartState, widthOfRightPart, bottomPartState, heightOfBottomPart, isMaximized);

        eventArgs.mainClass().setWindowProperties(windowWidth, windowHeight, windowX, windowY, isMaximized);

        eventArgs.mainClass().getMainController().setWindowProperties(leftPartState, widthOfLeftPart, rightPartState,
                widthOfRightPart, bottomPartState, heightOfBottomPart);
    }
}
