package com.application.base.actions;

import com.application.base.actions.actions.TimeStampHelper;
import com.application.base.main.MainClass;
import com.application.base.services.language.LanguageService;
import javafx.scene.image.ImageView;

public abstract class Action {

    protected LanguageService languageService;

    protected ActionEventArgs args;

    protected boolean nameVisible;

    protected ImageView imageView;

    protected ShortCutData shortCutData;

    protected String description;

    protected String name;

    protected String displayName;

    protected boolean multiselectSupported;

    public Action(LanguageService languageService) {
        this.languageService = languageService;
        this.args = null;
        this.nameVisible = false;
        this.imageView = null;
        this.shortCutData = null;
        this.description = "";
        this.name = "";
        this.displayName = "";
        this.multiselectSupported = false;
    }

    public void setArgs(ActionEventArgs args) {
        this.args = args;
    }

    public ActionEventArgs getArgs() {
        return this.args;
    }

    public void setNameVisible(boolean nameVisible) {
        this.nameVisible = nameVisible;
    }

    public boolean isNameVisible() {
        return this.nameVisible;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public void setShortCutData(ShortCutData shortCutData) {
        this.shortCutData = shortCutData;
    }

    public ShortCutData getShortCutData() {
        return this.shortCutData;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setMultiselectSupported(boolean multiselectSupported) {
        this.multiselectSupported = multiselectSupported;
    }

    public boolean isMultiselectSupported() {
        return multiselectSupported;
    }

    public abstract boolean isEnabled(ActionEventArgs eventArgs);

    protected abstract void execute(ActionEventArgs eventArgs);

    public void invokeExecute(ActionEventArgs actionEventArgs) {
        this.execute(actionEventArgs);
        actionEventArgs.mainClass().refresh();
    }

    public void addAdditionalInformationToActionLog(ActionEventArgs eventArgs, String string) {
        eventArgs.mainClass().getServiceManager().getActionLogContentService().addAdditionalContent(string);
    }
}
