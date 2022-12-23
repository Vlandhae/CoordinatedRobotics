package com.application.base.actions.actions.actionlog;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.actions.actions.TimeStampHelper;
import com.application.base.services.actionlog.ActionLogContentService;
import com.application.base.services.language.LanguageService;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CopyActionLogAction extends Action {

    public CopyActionLogAction(LanguageService languageService) {
        super(languageService);
        this.setNameVisible(true);
        this.setDescription(languageService.getString("CopyActionLogActionDescription"));
        this.setName(languageService.getString("CopyActionLogActionName"));
        this.setDisplayName(languageService.getString("CopyActionLogActionDisplayName"));
        this.setMultiselectSupported(true);
    }

    @Override
    public boolean isEnabled(ActionEventArgs eventArgs) {
        return true;
    }

    @Override
    protected void execute(ActionEventArgs eventArgs) {
        ActionLogContentService service = eventArgs.mainClass().getServiceManager().getActionLogContentService();
        String actionLogContent = service.getActionLogContentAsString();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(actionLogContent);
        clipboard.setContent(content);

        addAdditionalInformationToActionLog(eventArgs, "Action log was copied");
    }
}
