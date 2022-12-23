package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.VisibilityState;

public class MinimizeBottomGuiPartAction extends Action {

    public MinimizeBottomGuiPartAction(LanguageService languageService) {

        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(null);
        setDescription(this.languageService.getString("MinimizeBottomPartActionDescription"));
        setName(this.languageService.getString("MinimizeBottomPartActionName"));
        setDisplayName(this.languageService.getString("MinimizeBottomPartActionDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return ((eventArgs.mainClass().getMainController().getBottomPartState().equals(VisibilityState.NOT_VISIBLE))
                || (eventArgs.mainClass().getMainController().getBottomPartState().equals(VisibilityState.VISIBLE)));
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getMainController().setBottomPartState(VisibilityState.MINIMIZED);
    }
}
