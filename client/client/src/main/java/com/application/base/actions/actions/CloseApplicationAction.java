package com.application.base.actions.actions;

import com.application.base.actions.ActionEventArgs;
import com.application.base.actions.ShortCutData;
import com.application.base.actions.actions.layoutactions.SaveWindowPropertiesAction;
import com.application.base.services.language.LanguageService;
import javafx.scene.input.KeyCode;

public class CloseApplicationAction extends SaveWindowPropertiesAction {

    public CloseApplicationAction(LanguageService languageService) {
        super(languageService);
        this.setNameVisible(true);
        this.setImageView(null);
        this.setShortCutData(new ShortCutData(false, true, false, KeyCode.F4));
        this.setDescription(this.languageService.getString("CloseApplicationActionDescription"));
        this.setName(this.languageService.getString("CloseApplicationActionName"));
        this.setDisplayName(this.languageService.getString("CloseApplicationActionDisplayName"));
        this.setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return true;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        super.execute(eventArgs);
        eventArgs.mainClass().getServiceManager().getInternetService().closeConnectionToWebSocket();
        System.exit(0);
    }
}
