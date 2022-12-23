package com.application.base.services;

import com.application.base.main.MainClass;
import com.application.base.services.action.ActionService;
import com.application.base.services.actionlog.ActionLogContentService;
import com.application.base.services.explorer.ExplorerContentService;
import com.application.base.services.internet.InternetService;
import com.application.base.services.language.LanguageService;
import com.application.base.services.persistence.PersistenceService;
import com.application.base.services.settings.SettingsService;

public class ServiceManager {

    private MainClass mainClass;

    private SettingsService settingsService;

    private ActionService actionService;

    private LanguageService languageService;

    private PersistenceService persistenceService;

    private ExplorerContentService explorerContentService;

    private ActionLogContentService actionLogContentService;

    private InternetService internetService;

    public ServiceManager(MainClass mainClass) {
        this.mainClass = mainClass;
        this.persistenceService = new PersistenceService();
        this.settingsService = new SettingsService(this.persistenceService);
        this.languageService = new LanguageService(this.settingsService.getLocale());
        this.actionService = new ActionService(this.languageService);
        this.internetService = new InternetService();
        this.explorerContentService = new ExplorerContentService(this.languageService, this.internetService);
        this.actionLogContentService = new ActionLogContentService(this.internetService);

        addSelectionObservers();
    }

    public void addSelectionObservers() {
        this.mainClass.addSelectionObserver(this.actionLogContentService);
    }

    public ActionService getActionService() {
        return actionService;
    }

    public LanguageService getLanguageService() {
        return languageService;
    }

    public SettingsService getSettingsService() {
        return settingsService;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public ExplorerContentService getExplorerContentService() {
        return explorerContentService;
    }

    public ActionLogContentService getActionLogContentService() {
        return actionLogContentService;
    }

    public InternetService getInternetService() {
        return internetService;
    }
}
