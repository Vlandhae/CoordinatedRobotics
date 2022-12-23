package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.VisibilityState;

public class MinimizeLeftGuiPartAction extends Action {

    public MinimizeLeftGuiPartAction(LanguageService languageService) {
        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(null);
        setDescription(this.languageService.getString("MinimizeLeftPartActionDescription"));
        setName(this.languageService.getString("MinimizeLeftPartActionName"));
        setDisplayName(this.languageService.getString("MinimizeLeftPartActionDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return ((eventArgs.mainClass().getMainController().getLeftPartState().equals(VisibilityState.VISIBLE))
                || (eventArgs.mainClass().getMainController().getLeftPartState().equals(VisibilityState.NOT_VISIBLE)));
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getMainController().setLeftPartState(VisibilityState.MINIMIZED);
    }
}
