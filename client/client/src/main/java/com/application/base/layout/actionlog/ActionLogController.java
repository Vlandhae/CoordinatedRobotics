package com.application.base.layout.actionlog;

import com.application.base.layout.CustomPaneController;
import com.application.base.main.MainClass;
import com.application.base.services.actionlog.ActionLogContentService;
import javafx.scene.control.TextArea;

public class ActionLogController {

    private CustomTextArea textArea;

    private MainClass mainClass;
    private CustomPaneController controller;
    private ActionLogContentService actionLogContentService;

    public ActionLogController(MainClass mainClass, CustomPaneController controller) {
        this.mainClass = mainClass;
        this.controller = controller;
        this.initializeTextArea();
    }

    private void initializeTextArea() {
        this.textArea = new CustomTextArea();
        this.textArea.prefHeightProperty().bind(controller.getPane().heightProperty().subtract(36));
        this.textArea.minHeightProperty().bind(controller.getPane().heightProperty().subtract(36));
        this.textArea.maxHeightProperty().bind(controller.getPane().heightProperty().subtract(36));
        this.actionLogContentService = mainClass.getServiceManager().getActionLogContentService();
        this.actionLogContentService.setAndInitializeTextField(this.textArea);
    }

    public TextArea getContent() {
        return textArea;
    }
}
