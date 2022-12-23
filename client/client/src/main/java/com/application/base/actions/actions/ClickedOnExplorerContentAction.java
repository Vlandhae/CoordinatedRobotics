package com.application.base.actions.actions;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.services.language.LanguageService;

public class ClickedOnExplorerContentAction extends Action {

    private int contentId;

    public ClickedOnExplorerContentAction(int contentId, LanguageService languageService) {
        super(languageService);
        this.contentId = contentId;
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return false;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        String sessionId = eventArgs.mainClass().getServiceManager()
                .getExplorerContentService().getSessionIdByContentId(contentId);

        eventArgs.mainClass().setSelectedSession(sessionId);
    }
}
