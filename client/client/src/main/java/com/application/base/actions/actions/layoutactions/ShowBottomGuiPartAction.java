package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.VisibilityState;

public class ShowBottomGuiPartAction extends Action {

    public ShowBottomGuiPartAction(LanguageService languageService) {
        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(null);
        setDescription(this.languageService.getString("ShowBottomPartActionDescription"));
        setName(this.languageService.getString("ShowBottomPartActionName"));
        setDisplayName(this.languageService.getString("ShowBottomPartActionDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return ((eventArgs.mainClass().getMainController().getBottomPartState().equals(VisibilityState.NOT_VISIBLE))
                || (eventArgs.mainClass().getMainController().getBottomPartState().equals(VisibilityState.MINIMIZED)));
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getMainController().setBottomPartState(VisibilityState.VISIBLE);
    }
}
