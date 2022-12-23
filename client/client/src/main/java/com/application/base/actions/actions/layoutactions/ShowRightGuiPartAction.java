package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.VisibilityState;

public class ShowRightGuiPartAction extends Action {

    public ShowRightGuiPartAction(LanguageService languageService) {
        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(null);
        setDescription(this.languageService.getString("ShowRightPartActionDescription"));
        setName(this.languageService.getString("ShowRightPartActionName"));
        setDisplayName(this.languageService.getString("ShowRightPartActionDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return ((eventArgs.mainClass().getMainController().getRightPartState().equals(VisibilityState.NOT_VISIBLE))
                || (eventArgs.mainClass().getMainController().getRightPartState().equals(VisibilityState.MINIMIZED)));
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getMainController().setRightPartState(VisibilityState.VISIBLE);
    }
}
