package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.VisibilityState;

public class MinimizeRightGuiPartAction extends Action {

    public MinimizeRightGuiPartAction(LanguageService languageService) {
        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(null);
        setDescription(this.languageService.getString("MinimizeRightPartActionDescription"));
        setName(this.languageService.getString("MinimizeRightPartActionName"));
        setDisplayName(this.languageService.getString("MinimizeRightPartActionDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return ((eventArgs.mainClass().getMainController().getRightPartState().equals(VisibilityState.VISIBLE))
                || (eventArgs.mainClass().getMainController().getRightPartState().equals(VisibilityState.NOT_VISIBLE)));
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getMainController().setRightPartState(VisibilityState.MINIMIZED);
    }
}
