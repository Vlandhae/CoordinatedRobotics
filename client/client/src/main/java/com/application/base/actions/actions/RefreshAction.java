package com.application.base.actions.actions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.actions.ShortCutData;
import com.application.base.services.language.LanguageService;
import javafx.scene.input.KeyCode;

public class RefreshAction extends Action {

    public RefreshAction(LanguageService languageService) {
        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(new ShortCutData(false, false, false, KeyCode.F5));
        setDescription(this.languageService.getString("RefreshActionDescription"));
        setName(this.languageService.getString("RefreshActionName"));
        setDisplayName(this.languageService.getString("RefreshDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return true;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().refresh();

        addAdditionalInformationToActionLog(eventArgs, "Application was refreshed");
    }
}
