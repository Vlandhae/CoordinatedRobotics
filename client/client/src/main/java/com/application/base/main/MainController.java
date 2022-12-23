package com.application.base.main;

import com.application.base.actions.Action;
import com.application.base.actions.ActionEventArgs;
import com.application.base.actions.actions.MenuSeparatorPlaceHolder;
import com.application.base.actions.actions.layoutactions.ShowBottomGuiPartAction;
import com.application.base.actions.actions.layoutactions.ShowLeftGuiPartAction;
import com.application.base.actions.actions.layoutactions.ShowRightGuiPartAction;
import com.application.base.layout.CustomMenuItem;
import com.application.base.layout.CustomPaneController;
import com.application.base.layout.GuiPart;
import com.application.base.services.action.GlobalActionCollection;
import com.application.base.services.action.MenuActionCollection;
import com.application.base.services.language.LanguageService;
import com.application.base.services.settings.SettingsService;
import com.application.base.services.settings.VisibilityState;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    /** The margin around the control that a user can click in to start resizing the region. */
    private static final int RESIZE_MARGIN = 5;

    private boolean draggingLeftPart;

    private boolean draggingRightPart;

    private boolean draggingBottomPart;

    private CustomPaneController leftCustomPaneController;
    private CustomPaneController rightCustomPaneController;
    private CustomPaneController bottomCustomPaneController;
    private CustomPaneController centerCustomPaneController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane top;

    @FXML
    private AnchorPane left;

    @FXML
    private AnchorPane right;

    @FXML
    private AnchorPane bottom;

    @FXML
    private AnchorPane center;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Line line;

    @FXML
    private Button explorerButton;

    @FXML
    private Button actionLogButton;

    @FXML
    private Button propertiesButton;

    @FXML
    private VBox leftBox;

    @FXML
    private VBox rightBox;

    @FXML
    private HBox bottomBox;

    @FXML
    private HBox contentContainer;

    private List<com.application.base.layout.CustomMenuItem> menuItems;

    private VisibilityState leftPartState;
    private VisibilityState rightPartState;
    private VisibilityState bottomPartState;

    private SettingsService settingsService;

    private MainClass mainClass;

    public MainController(MainClass mainClass, SettingsService settingsService) {
        this.menuItems = new ArrayList<>();
        this.mainClass = mainClass;
        this.settingsService = settingsService;
    }

    public void initialize() {
        contentContainer.getChildren().remove(0);
        contentContainer.getChildren().remove(1);
        LanguageService languageService = this.mainClass.getServiceManager().getLanguageService();
        setLeftPartWidth(settingsService.getWidthOfLeftPart());
        setRightPartWidth(250);
        setBottomPartHeight(settingsService.getHeightOfBottomPart());
        setLeftPartState(settingsService.getLeftPartState());
        setRightPartState(settingsService.getRightPartState());
        setBottomPartState(settingsService.getBottomPartState());
        this.explorerButton.setText(languageService.getString("Explorer"));
        this.actionLogButton.setText(languageService.getString("ActionLog"));
        this.propertiesButton.setText(languageService.getString("Properties"));
        this.initContent(languageService);
        this.initButtons(this.actionLogButton);
        this.initButtons(this.explorerButton);
        this.initButtons(this.propertiesButton);
        this.borderPane.prefHeightProperty().bind(anchorPane.heightProperty().subtract(35));
        this.borderPane.maxHeightProperty().bind(anchorPane.heightProperty().subtract(35));
        this.borderPane.minHeightProperty().bind(anchorPane.heightProperty().subtract(35));
        this.borderPane.prefWidthProperty().bind(anchorPane.widthProperty().subtract(0));
        this.borderPane.maxWidthProperty().bind(anchorPane.widthProperty().subtract(0));
        this.borderPane.minWidthProperty().bind(anchorPane.widthProperty().subtract(0));
        this.bottom.maxHeightProperty().bind(borderPane.heightProperty().subtract(600));
        this.left.maxWidthProperty().bind(borderPane.widthProperty().subtract(right.widthProperty()).subtract(160));

        this.right.setMinWidth(250);
        this.right.setMaxWidth(250);
        this.right.setPrefWidth(250);
    }

    private void initButtons(Button button) {
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                button.setStyle("-fx-background-color:#dae7f3;");
            }
        });

        button.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                button.setStyle("-fx-background-color:transparent;");
            }
        });
    }

    private void initContent(LanguageService languageService) {
        leftCustomPaneController = new CustomPaneController(mainClass,
                GuiPart.LEFT_PART, languageService.getString("Explorer"));
        rightCustomPaneController = new CustomPaneController(mainClass,
                GuiPart.RIGHT_PART, languageService.getString("Properties"));
        centerCustomPaneController = new CustomPaneController(mainClass,
                GuiPart.CENTER_PART, languageService.getString("MainFrameTitle"));
        bottomCustomPaneController = new CustomPaneController(mainClass,
                GuiPart.BOTTOM_PART, languageService.getString("ActionLog"));
        this.left.getChildren().add(leftCustomPaneController.getPane());
        this.right.getChildren().add(rightCustomPaneController.getPane());
        this.bottom.getChildren().add(bottomCustomPaneController.getPane());
        this.center.getChildren().add(centerCustomPaneController.getPane());
    }

    public void setWindowProperties(VisibilityState leftPartState, double widthOfLeftPart,
                                    VisibilityState rightPartState, double widthOfRightPart,
                                    VisibilityState bottomPartState, double heightOfBottomPart) {
        setLeftPartWidth(widthOfLeftPart);
        setRightPartWidth(250);
        setBottomPartHeight(heightOfBottomPart);
        setLeftPartState(leftPartState);
        setRightPartState(rightPartState);
        setBottomPartState(bottomPartState);
    }

    public void mouseReleasedForLeftPart(MouseEvent event) {

        if (!draggingLeftPart) {
            return;
        }
        draggingLeftPart = false;
        this.line.setVisible(false);
        left.setCursor(Cursor.DEFAULT);
        double mouseSceneX = event.getSceneX();
        double value = mouseSceneX - left.localToScene(left.getWidth(), 0).getX();
        double newWidth = left.getWidth() + (value);

        if (newWidth < 5) {
            newWidth = 5;
        }

        double maxValue = this.mainClass.getStage().getWidth() - this.right.getWidth() - 165;

        if (newWidth > maxValue) {
            newWidth = maxValue;
        }

        setLeftPartWidth(newWidth);

        this.borderPane.setLeft(null);
        this.borderPane.setRight(null);
        this.borderPane.setLeft(left);
        this.borderPane.setRight(right);
    }

    public void mouseReleasedForRightPart(MouseEvent event) {

        return;
        //if (!draggingRightPart) {
        //    return;
        //}
        //draggingRightPart = false;
        //this.line.setVisible(false);
        //right.setCursor(Cursor.DEFAULT);
        //double mouseSceneX = event.getSceneX();
        //double value = right.localToScene(0, 0).getX() - mouseSceneX;
        //double newWidth = right.getWidth() + (value);

        //if (newWidth < 5) {
        //    newWidth = 5;
        //}

        //double maxValue = this.mainClass.getStage().getWidth() - left.getWidth() - 165;

        //if (newWidth > maxValue) {
        //    newWidth = maxValue;
        //}

        //setRightPartWidth(newWidth);

        //this.borderPane.setLeft(null);
        //this.borderPane.setRight(null);
        //this.borderPane.setLeft(left);
        //this.borderPane.setRight(right);
    }

    public void mouseReleasedForBottomPart(MouseEvent event) {

        if (!draggingBottomPart) {
            return;
        }
        draggingBottomPart = false;
        this.line.setVisible(false);
        bottom.setCursor(Cursor.DEFAULT);
        double mouseSceneY = event.getSceneY();
        double value = bottom.localToScene(0, 0).getY() - mouseSceneY;
        double newHeight = bottom.getHeight() + (value);

        if (newHeight < 5) {
            newHeight = 5;
        }

        setBottomPartHeight(newHeight);
    }

    public void mouseMovedOverLeftPart(MouseEvent event) {

        if (isInDraggableZoneForLeftPart(event) || draggingLeftPart) {
            left.setCursor(Cursor.E_RESIZE);
        } else {
            left.setCursor(Cursor.DEFAULT);
        }
    }

    public void mouseMovedOverRightPart(MouseEvent event) {

        //if (isInDraggableZoneForRightPart(event) || draggingRightPart) {
        //    right.setCursor(Cursor.W_RESIZE);
        //} else {
        //    right.setCursor(Cursor.DEFAULT);
        //}
    }

    public void mouseMovedOverBottomPart(MouseEvent event) {

        if (isInDraggableZoneForBottomPart(event) || draggingBottomPart) {
            bottom.setCursor(Cursor.N_RESIZE);
        } else {
            bottom.setCursor(Cursor.DEFAULT);
        }
    }

    private boolean isInDraggableZoneForLeftPart(MouseEvent event) {
        return ((event.getSceneX() > (left.localToScene(left.getWidth(), 0).getX() - RESIZE_MARGIN))
                && (event.getSceneX() < (left.localToScene(left.getWidth(), 0).getX() + RESIZE_MARGIN)));
    }

    private boolean isInDraggableZoneForRightPart(MouseEvent event) {
        return false;
        //return ((event.getSceneX() > (right.localToScene(0, 0).getX() - RESIZE_MARGIN))
        //        && (event.getSceneX() < (right.localToScene(0, 0).getX() + RESIZE_MARGIN)));
    }

    private boolean isInDraggableZoneForBottomPart(MouseEvent event) {
        return ((event.getSceneY() > (bottom.localToScene(0, 0).getY() - RESIZE_MARGIN))
                && (event.getSceneY() < (bottom.localToScene(0, 0).getY() + RESIZE_MARGIN)));
    }

    public void mouseDraggedForLeftPart(MouseEvent event) {

        if (!draggingLeftPart) {
            return;
        }
        setMouseDraggedForLeftPart(event);
    }

    public void mouseDraggedForRightPart(MouseEvent event) {
        return;
        //if (!draggingRightPart) {
        //    return;
        //}
        //setMouseDraggedForRightPart(event);
    }

    public void mouseDraggedForBottomPart(MouseEvent event) {

        if (!draggingBottomPart) {
            return;
        }
        setMouseDraggedForBottomPart(event);
    }

    private void setMouseDraggedForLeftPart(MouseEvent event) {
        line.setVisible(true);
        line.setStartY(left.localToScene(0,0).getY());
        line.setEndY(left.localToScene(0,0).getY() + left.getHeight());
        line.setStartX(event.getSceneX());
        line.setEndX(event.getSceneX());
    }

    private void setMouseDraggedForRightPart(MouseEvent event) {
        //line.setVisible(true);
        //line.setStartY(right.localToScene(0,0).getY());
        //line.setEndY(right.localToScene(0,0).getY() + right.getHeight());
        //line.setStartX(event.getSceneX());
       // line.setEndX(event.getSceneX());
    }

    private void setMouseDraggedForBottomPart(MouseEvent event) {
        line.setVisible(true);
        line.setStartX(bottom.localToScene(0, 0).getX());
        line.setEndX(bottom.localToScene(0, 0).getX() + bottom.getWidth());
        line.setStartY(event.getSceneY());
        line.setEndY(event.getSceneY());
    }

    public void mousePressedOnLeftPart(MouseEvent event) {

        if (!isInDraggableZoneForLeftPart(event)) {
            return;
        }
        draggingLeftPart = true;
    }

    public void mousePressedOnRightPart(MouseEvent event) {

        //if (!isInDraggableZoneForRightPart(event)) {
       //     return;
        //}
        //draggingRightPart = true;
    }

    public void mousePressedOnBottomPart(MouseEvent event) {

        if (!isInDraggableZoneForBottomPart(event)) {
            return;
        }
        draggingBottomPart = true;
    }

    public void populateGlobalActions(GlobalActionCollection globalActionCollection) {
    }

    public void populateMenuActions(MenuActionCollection[] menuActionWrapper) {
        for (int i = 0; i < menuActionWrapper.length; i++) {
            Menu menu = new Menu(menuActionWrapper[i].getMenuTitle());

            for (Action action : menuActionWrapper[i]) {

                if (action instanceof MenuSeparatorPlaceHolder) {
                    menu.getItems().add(new SeparatorMenuItem());
                    continue;
                }
                ActionEventArgs actionEventArgs = new ActionEventArgs(this.mainClass);
                com.application.base.layout.CustomMenuItem menuItem = new com.application.base.layout.CustomMenuItem(action.getDisplayName());
                menuItem.setAction(action);
                menuItem.setOnAction(event -> {
                    action.invokeExecute(new ActionEventArgs(this.mainClass));
                });
                menuItem.setDisable(!action.isEnabled(actionEventArgs));
                menu.getItems().add(menuItem);
                this.menuItems.add(menuItem);
            }
            menuBar.getMenus().add(menu);
        }
    }

    public void refresh() {
        this.refreshMenu();
        this.leftCustomPaneController.refresh();
        this.rightCustomPaneController.refresh();
        this.bottomCustomPaneController.refresh();
        this.centerCustomPaneController.refresh();
    }

    private void refreshMenu() {
        if (menuItems.isEmpty()) {
            return;
        }
        ActionEventArgs actionEventArgs = new ActionEventArgs(this.mainClass);

        for (CustomMenuItem menuItem : menuItems) {
            menuItem.setDisable(!menuItem.getAction().isEnabled(actionEventArgs));
        }
    }

    public VisibilityState getLeftPartState() {
        return leftPartState;
    }

    public void setLeftPartState(VisibilityState leftPartState) {
        this.leftPartState = leftPartState;

        switch (leftPartState) {
            case NOT_VISIBLE -> {
                borderPane.setLeft(null);
                this.explorerButton.setVisible(false);
                this.leftBox.setVisible(false);
                this.leftBox.setPrefWidth(0.0);
                this.leftBox.setMinWidth(0.0);
                this.leftBox.setMaxWidth(0.0);
            }
            case VISIBLE -> {
                borderPane.setLeft(this.left);
                this.explorerButton.setVisible(false);
                this.leftBox.setVisible(false);
                this.leftBox.setPrefWidth(0.0);
                this.leftBox.setMinWidth(0.0);
                this.leftBox.setMaxWidth(0.0);
            }
            case MINIMIZED -> {
                borderPane.setLeft(leftBox);
                this.explorerButton.setVisible(true);
                this.leftBox.setVisible(true);
                this.leftBox.setPrefWidth(30.0);
                this.leftBox.setMinWidth(30.0);
                this.leftBox.setMaxWidth(30.0);
            }
        }
    }

    public VisibilityState getRightPartState() {
        return rightPartState;
    }

    public void setRightPartState(VisibilityState rightPartState) {
        this.rightPartState = rightPartState;

        switch (rightPartState) {
            case NOT_VISIBLE -> {
                borderPane.setRight(null);
                this.propertiesButton.setVisible(false);
                this.rightBox.setVisible(false);
                this.rightBox.setPrefWidth(0.0);
                this.rightBox.setMinWidth(0.0);
                this.rightBox.setMaxWidth(0.0);
            }
            case VISIBLE -> {
                borderPane.setRight(this.right);
                this.propertiesButton.setVisible(false);
                this.rightBox.setVisible(false);
                this.rightBox.setPrefWidth(0.0);
                this.rightBox.setMinWidth(0.0);
                this.rightBox.setMaxWidth(0.0);
            }
            case MINIMIZED -> {
                borderPane.setRight(rightBox);
                this.propertiesButton.setVisible(true);
                this.rightBox.setVisible(true);
                this.rightBox.setPrefWidth(30.0);
                this.rightBox.setMinWidth(30.0);
                this.rightBox.setMaxWidth(30.0);
            }
        }
    }

    public VisibilityState getBottomPartState() {
        return bottomPartState;
    }

    public void setBottomPartState(VisibilityState bottomPartState) {
        this.bottomPartState = bottomPartState;

        switch (bottomPartState) {
            case NOT_VISIBLE -> {
                borderPane.setBottom(null);
                this.actionLogButton.setVisible(false);
                this.actionLogButton.setVisible(false);
                this.bottomBox.setVisible(false);
                this.bottomBox.setPrefHeight(0.0);
                this.bottomBox.setMinHeight(0.0);
                this.bottomBox.setMaxHeight(0.0);
            }
            case VISIBLE -> {
                borderPane.setBottom(this.bottom);
                this.actionLogButton.setVisible(false);
                this.actionLogButton.setVisible(false);
                this.bottomBox.setVisible(false);
                this.bottomBox.setPrefHeight(0.0);
                this.bottomBox.setMinHeight(0.0);
                this.bottomBox.setMaxHeight(0.0);
            }
            case MINIMIZED -> {
                borderPane.setBottom(bottomBox);
                this.actionLogButton.setVisible(true);
                this.actionLogButton.setVisible(true);
                this.bottomBox.setVisible(true);
                this.bottomBox.setPrefHeight(20.0);
                this.bottomBox.setMinHeight(20.0);
                this.bottomBox.setMaxHeight(20.0);
            }
        }
    }

    public void minimizedExplorerButtonAction() {
        ActionEventArgs actionEventArgs = new ActionEventArgs(mainClass);
        new ShowLeftGuiPartAction(mainClass.getServiceManager().getLanguageService()).invokeExecute(actionEventArgs);
    }

    public void minimizedPropertiesViewButtonAction() {
        ActionEventArgs actionEventArgs = new ActionEventArgs(mainClass);
        new ShowRightGuiPartAction(mainClass.getServiceManager().getLanguageService()).invokeExecute(actionEventArgs);
    }

    public void minimizedActionLogButtonAction() {
        ActionEventArgs actionEventArgs = new ActionEventArgs(mainClass);
        new ShowBottomGuiPartAction(mainClass.getServiceManager().getLanguageService()).invokeExecute(actionEventArgs);
    }

    public double getLeftPartWidth() {
        return left.getWidth();
    }

    public void setLeftPartWidth(double width) {
        left.setMinWidth(160);
        left.setPrefWidth(width);
        left.setMinHeight(0);
    }

    public double getRightPartWidth() {
        return right.getWidth();
    }

    public void setRightPartWidth(double width) {
        right.setMinWidth(250);
        right.setPrefWidth(250);
        right.setMinHeight(500);
    }

    public double getBottomPartHeight() {
        return bottom.getHeight();
    }

    public void setBottomPartHeight(double height) {
        bottom.setMinHeight(100);
        bottom.setPrefHeight(height);
        bottom.setMinWidth(0);
    }
}

