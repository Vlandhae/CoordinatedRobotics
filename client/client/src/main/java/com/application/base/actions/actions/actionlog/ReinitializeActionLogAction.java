package com.application.base.actions.actions.actionlog;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;

public class ReinitializeActionLogAction extends Action {

    public ReinitializeActionLogAction(LanguageService languageService) {
        super(languageService);
        this.setNameVisible(true);
        this.setDescription(languageService.getString("ReinitializeActionDescription"));
        this.setName(languageService.getString("ReinitializeActionName"));
        this.setDisplayName(languageService.getString("ReinitializeActionDisplayName"));
        this.setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return true;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        String selectedSession = eventArgs.mainClass().getSelectedSession();
        eventArgs.mainClass().getServiceManager().getActionLogContentService().reloadContent(selectedSession);
    }
}
