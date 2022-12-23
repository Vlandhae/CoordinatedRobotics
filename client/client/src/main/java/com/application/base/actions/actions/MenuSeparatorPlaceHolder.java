package com.application.base.actions.actions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;

public class MenuSeparatorPlaceHolder extends Action {

    public MenuSeparatorPlaceHolder(LanguageService languageService) {
        super(languageService);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return false;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
    }
}
