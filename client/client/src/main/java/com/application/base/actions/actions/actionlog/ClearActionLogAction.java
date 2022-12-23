package main.java.com.application.base.actions.actions.actionlog;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;

public class ClearActionLogAction extends Action {

    public ClearActionLogAction(LanguageService languageService) {
        super(languageService);
        this.setNameVisible(true);
        this.setDescription(languageService.getString("ClearActionLogActionDescription"));
        this.setName(languageService.getString("ClearActionLogActionName"));
        this.setDisplayName(languageService.getString("ClearActionLogActionDisplayName"));
        this.setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return true;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        eventArgs.mainClass().getServiceManager().getActionLogContentService().clearActionLog();
    }
}
