package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.VisibilityState;

public class ShowLeftGuiPartAction extends Action {

    public ShowLeftGuiPartAction(LanguageService languageService) {
        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(null);
        setDescription(this.languageService.getString("ShowLeftPartActionDescription"));
        setName(this.languageService.getString("ShowLeftPartActionName"));
        setDisplayName(this.languageService.getString("ShowLeftPartActionDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return ((eventArgs.mainClass().getMainController().getLeftPartState().equals(VisibilityState.NOT_VISIBLE))
                || (eventArgs.mainClass().getMainController().getLeftPartState().equals(VisibilityState.MINIMIZED)));
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getMainController().setLeftPartState(VisibilityState.VISIBLE);
    }
}
