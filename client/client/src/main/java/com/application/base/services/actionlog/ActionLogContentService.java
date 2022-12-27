package com.application.base.services.actionlog;

import com.application.base.actions.actions.TimeStampHelper;
import com.application.base.layout.actionlog.CustomTextArea;
import com.application.base.main.SelectionObserver;
import com.application.base.services.internet.InternetService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class ActionLogContentService implements SelectionObserver {

    private InternetService internetService;

    private CustomTextArea customTextArea;

    private ObservableList<String> actionLogBaseContent;
    private ObservableList<String> actionLogAdditionalContent;

    private String currentSession;

    public ActionLogContentService(InternetService internetService) {
        this.internetService = internetService;
        this.internetService.setActionLog(this);
        this.actionLogBaseContent = FXCollections.observableArrayList();
        this.actionLogAdditionalContent = FXCollections.observableArrayList();
        this.currentSession = "";
        this.initializeContent("");
    }

    private void initializeContent(String idString) {
        if (currentSession.equals(idString)) {
            return;
        }
        this.currentSession = idString;

        actionLogBaseContent = FXCollections.observableArrayList();
        actionLogAdditionalContent = FXCollections.observableArrayList();

        if (idString.isBlank()) {
            return;
        }
        String cars = Arrays.toString(internetService.getListOfCarsInSession(currentSession).toArray());

        this.actionLogBaseContent.add("Selected Session: " + currentSession);
        this.actionLogBaseContent.add("Robots in Session: " + cars);
        this.actionLogBaseContent.add(" ");
    }

    public ObservableList<String> getActionLogContent() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(actionLogBaseContent);
        list.addAll(actionLogAdditionalContent);
        return list;
    }

    public void setAndInitializeTextField(CustomTextArea customTextArea) {
        this.customTextArea = customTextArea;
        this.addContentToTextField();
    }

    public void addContentToTextField() {
        ObservableList<String> content = getActionLogContent();

        for (String string : content) {
            this.addLine(string);
        }
    }

    public void addLine(String string) {
        if (this.customTextArea == null) {
            return;
        }
        this.customTextArea.addLine(string);
    }

    public void clearActionLog() {
        actionLogAdditionalContent = FXCollections.observableArrayList();
        this.customTextArea.clearActionLog();
        addContentToTextField();
    }

    public String getActionLogContentAsString() {
        return customTextArea.getText();
    }

    public void reloadContent(String selectedSession) {
        this.initializeContent(selectedSession);
        this.setAndInitializeTextField(customTextArea);
    }

    public void addAdditionalContent(String string) {
        String information = TimeStampHelper.getTimeStamp() + " --- " + string + ".";
        this.actionLogAdditionalContent.add(information);
    }

    @Override
    public void selectionChanged(String newValue) {
        reloadContent(newValue);
    }
}
