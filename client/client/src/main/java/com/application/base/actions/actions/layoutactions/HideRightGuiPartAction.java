package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.VisibilityState;

public class HideRightGuiPartAction extends Action {

    public HideRightGuiPartAction(LanguageService languageService) {
        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(null);
        setDescription(this.languageService.getString("HideRightPartActionDescription"));
        setName(this.languageService.getString("HideRightPartActionName"));
        setDisplayName(this.languageService.getString("HideRightPartActionDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return ((eventArgs.mainClass().getMainController().getRightPartState().equals(VisibilityState.VISIBLE))
                || (eventArgs.mainClass().getMainController().getRightPartState().equals(VisibilityState.MINIMIZED)));
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getMainController().setRightPartState(VisibilityState.NOT_VISIBLE);
    }
}
