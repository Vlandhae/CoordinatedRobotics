package com.application.base.services.action;

import com.application.base.actions.actions.*;
import com.application.base.actions.actions.actionlog.ClearActionLogAction;
import com.application.base.actions.actions.actionlog.CopyActionLogAction;
import com.application.base.actions.actions.actionlog.ReinitializeActionLogAction;
import com.application.base.actions.actions.layoutactions.*;
import com.application.base.services.language.LanguageService;

public class ActionService {

    private LanguageService languageService;

    private GlobalActionCollection globalActions;

    private ClassTypeActionCollection classTypeActions;

    private MenuActionCollection[] menuActionCollections;

    public ActionService(LanguageService languageService) {
        this.languageService = languageService;
        this.populateGlobalActions();
        this.populateClassTypeActions();
        this.populateMenuActions();
    }

    public GlobalActionCollection getGlobalActions() {
        return globalActions;
    }

    public ClassTypeActionCollection getClassTypeActions() {
        return classTypeActions;
    }

    public MenuActionCollection[] getMenuActionCollections() {
        return menuActionCollections;
    }

    private void populateGlobalActions() {
        this.globalActions = new GlobalActionCollection();
    }

    public void populateClassTypeActions() {
        this.classTypeActions = new ClassTypeActionCollection();
    }

    private void populateMenuActions() {
        this.menuActionCollections = new MenuActionCollection[3];
        MenuActionCollection fileActionCollection = new MenuActionCollection();
        MenuActionCollection viewActionCollection = new MenuActionCollection();
        MenuActionCollection actionLogActionCollection = new MenuActionCollection();

        fileActionCollection.setMenuTitle(this.languageService.getString("FileMenuTitle"));
        fileActionCollection.add(new RefreshAction(this.languageService));
        fileActionCollection.add(new CloseApplicationAction(this.languageService));

        viewActionCollection.setMenuTitle(this.languageService.getString("ViewMenuTitle"));
        viewActionCollection.add(new ShowLeftGuiPartAction(this.languageService));
        viewActionCollection.add(new MinimizeLeftGuiPartAction(this.languageService));
        viewActionCollection.add(new HideLeftGuiPartAction(this.languageService));
        viewActionCollection.add(new MenuSeparatorPlaceHolder(this.languageService));
        viewActionCollection.add(new ShowRightGuiPartAction(this.languageService));
        viewActionCollection.add(new MinimizeRightGuiPartAction(this.languageService));
        viewActionCollection.add(new HideRightGuiPartAction(this.languageService));
        viewActionCollection.add(new MenuSeparatorPlaceHolder(this.languageService));
        viewActionCollection.add(new ShowBottomGuiPartAction(this.languageService));
        viewActionCollection.add(new MinimizeBottomGuiPartAction(this.languageService));
        viewActionCollection.add(new HideBottomGuiPartAction(this.languageService));
        viewActionCollection.add(new MenuSeparatorPlaceHolder(this.languageService));
        viewActionCollection.add(new RestoreWindowPropertiesAction(this.languageService));

        actionLogActionCollection.setMenuTitle(this.languageService.getString("ActionLog"));
        actionLogActionCollection.add(new ClearActionLogAction(this.languageService));
        actionLogActionCollection.add(new CopyActionLogAction(this.languageService));
        actionLogActionCollection.add(new ReinitializeActionLogAction(this.languageService));

        this.menuActionCollections[0] = fileActionCollection;
        this.menuActionCollections[1] = viewActionCollection;
        this.menuActionCollections[2] = actionLogActionCollection;
    }
}
