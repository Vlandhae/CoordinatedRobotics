package com.application.base.layout;

import com.application.base.actions.ActionEventArgs;
import com.application.base.actions.actions.layoutactions.*;
import com.application.base.layout.actionlog.ActionLogController;
import com.application.base.layout.center.CenterController;
import com.application.base.layout.explorer.ExplorerController;
import com.application.base.layout.robocontroller.RobotControllerController;
import com.application.base.main.MainClass;
import com.application.base.services.language.LanguageService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CustomPaneController {

    private Scene scene;

    @FXML
    private VBox pane;

    @FXML
    private VBox contentPane;

    @FXML
    private HBox buttonBox;

    @FXML HBox titleBox;

    @FXML
    private Text title;

    @FXML
    private ImageView iconMinus;

    @FXML
    private ImageView iconMinusMouseOver;

    @FXML
    private ImageView iconX;

    @FXML
    private ImageView iconXMouseOver;

    @FXML
    private Button xButton;

    @FXML
    private Button minusButton;

    private String titleString;

    private MainClass mainClass;

    private GuiPart guiPart;

    private Node content;

    private ExplorerController explorerController;
    private ActionLogController actionLogController;
    private RobotControllerController robotControllerController;

    public CustomPaneController(MainClass mainClass, GuiPart guiPart, String title) {
        this.explorerController = null;
        this.content = null;
        this.mainClass = mainClass;
        this.guiPart = guiPart;
        this.titleString = title;
        this.load();
        this.initializeButtons();
        this.doSettings();
        this.initializeContent();
    }

    private void initializeContent() {
        switch (guiPart) {
            case LEFT_PART -> {
                this.initializeExplorer();
            }
            case RIGHT_PART -> {
                this.initializeRoboControllerView();
            }
            case BOTTOM_PART -> {
                initializeActionLog();
            }
            case CENTER_PART -> {
                initializeCenter();
            }
        }
    }

    private void initializeCenter() {
        CenterController guiPartController = new CenterController(this.mainClass);
        this.contentPane.getChildren().setAll(guiPartController.getContent());
    }

    private void initializeRoboControllerView() {
        this.robotControllerController = new RobotControllerController(this.mainClass);
        this.contentPane.getChildren().add(this.robotControllerController.getContent());
        this.content = robotControllerController.getContent();
        robotControllerController.getContent().minHeightProperty().bind(pane.heightProperty().subtract(46.0));
        robotControllerController.getContent().maxHeightProperty().bind(pane.heightProperty().subtract(46.0));
        robotControllerController.getContent().prefHeightProperty().bind(pane.heightProperty().subtract(46.0));
    }

    private void initializeExplorer() {
        int selectedItem = -1;

        if (explorerController != null) {
            selectedItem = explorerController.getIdOfSelectedItem();
        }
        explorerController = new ExplorerController(this.mainClass);
        this.contentPane.getChildren().add(explorerController.getContent());
        explorerController.getContent().minHeightProperty().bind(pane.heightProperty().subtract(46.0));
        explorerController.getContent().maxHeightProperty().bind(pane.heightProperty().subtract(46.0));
        explorerController.getContent().prefHeightProperty().bind(pane.heightProperty().subtract(46.0));
        String style = "-fx-background-color: white; -fx-border-color: grey; -fx-border-width: 1 0 0 0;";
        this.contentPane.setStyle(style);
        this.content = explorerController.getContent();

        if (selectedItem != -1) {
            this.explorerController.selectItem(selectedItem);
        }
    }

    private void initializeActionLog() {
        this.actionLogController = new ActionLogController(this.mainClass, this);
        this.content = this.actionLogController.getContent();
        this.contentPane.getChildren().add(this.actionLogController.getContent());
        String style = "-fx-background-color: white; -fx-border-color: grey; -fx-border-width: 1 0 0 0;";
        this.contentPane.setStyle(style);
        this.contentPane.toFront();
    }

    private void initializeButtons() {

        this.iconMinusMouseOver = new ImageView(new Image(CustomPaneController.class.getResourceAsStream("/com.application.base.layout/iconMinusMouseOver.png")));
        this.iconXMouseOver = new ImageView(new Image(CustomPaneController.class.getResourceAsStream("/com.application.base.layout/iconXMouseOver.png")));

        xButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                xButton.setGraphic(iconXMouseOver);
            }
        });

        xButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                xButton.setGraphic(iconX);
            }
        });

        minusButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                minusButton.setGraphic(iconMinusMouseOver);
            }
        });

        minusButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                minusButton.setGraphic(iconMinus);
            }
        });
    }

    private void doSettings() {
        if (guiPart.equals(GuiPart.CENTER_PART)) {
            buttonBox.setVisible(false);
            AnchorPane.setRightAnchor(titleBox, 0.0);
            AnchorPane.setLeftAnchor(titleBox, 0.0);
        }
    }

    private void load() {
        try {
            String path = "/com.application.base.layout/custom-anchor-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(CustomPaneController.class.getResource(path));
            fxmlLoader.setController(this);
            scene = new Scene(fxmlLoader.load());
            title.setText(titleString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VBox getPane() {
        return pane;
    }

    public void closeButtonAction() {
        LanguageService languageService = mainClass.getServiceManager().getLanguageService();
        ActionEventArgs actionEventArgs = new ActionEventArgs(mainClass);
        switch (guiPart) {
            case LEFT_PART -> {
                new HideLeftGuiPartAction(languageService).invokeExecute(actionEventArgs);
            }
            case RIGHT_PART -> {
                new HideRightGuiPartAction(languageService).invokeExecute(actionEventArgs);
            }
            case BOTTOM_PART -> {
                new HideBottomGuiPartAction(languageService).invokeExecute(actionEventArgs);
            }
            case CENTER_PART -> {
                //Do Nothing
            }
        }
    }

    public void minimizeButtonAction() {
        LanguageService languageService = mainClass.getServiceManager().getLanguageService();
        ActionEventArgs actionEventArgs = new ActionEventArgs(mainClass);
        switch (guiPart) {
            case LEFT_PART -> {
                new MinimizeLeftGuiPartAction(languageService).invokeExecute(actionEventArgs);
            }
            case RIGHT_PART -> {
                new MinimizeRightGuiPartAction(languageService).invokeExecute(actionEventArgs);
            }
            case BOTTOM_PART -> {
                new MinimizeBottomGuiPartAction(languageService).invokeExecute(actionEventArgs);
            }
            case CENTER_PART -> {
                //Do Nothing
            }
        }
    }

    public void refresh() {
        this.contentPane.getChildren().remove(this.content);
        initializeContent();
    }
}
