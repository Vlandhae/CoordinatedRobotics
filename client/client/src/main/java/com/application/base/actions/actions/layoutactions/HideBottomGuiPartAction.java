package com.application.base.actions.actions.layoutactions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.VisibilityState;

public class HideBottomGuiPartAction extends Action {

    public HideBottomGuiPartAction(LanguageService languageService) {
        super(languageService);
        setNameVisible(true);
        setImageView(null);
        setShortCutData(null);
        setDescription(this.languageService.getString("HideBottomPartActionDescription"));
        setName(this.languageService.getString("HideBottomPartActionName"));
        setDisplayName(this.languageService.getString("HideBottomPartActionDisplayName"));
        setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return ((eventArgs.mainClass().getMainController().getBottomPartState().equals(VisibilityState.VISIBLE))
                || (eventArgs.mainClass().getMainController().getBottomPartState().equals(VisibilityState.MINIMIZED)));
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getMainController().setBottomPartState(VisibilityState.NOT_VISIBLE);
    }
}
