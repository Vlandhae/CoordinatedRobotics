package com.application.base.layout.robocontroller;

import com.application.base.main.MainClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.io.IOException;

public class RobotControllerController{

    private boolean buttonUpPressed = false;
    private boolean buttonLeftPressed = false;
    private boolean buttonRightPressed = false;
    private boolean buttonDownPressed = false;

    @FXML
    public Button buttonUp;

    @FXML
    public Button buttonLeft;

    @FXML
    public Button buttonRight;

    @FXML
    public Button buttonDown;

    @FXML
    public ScrollPane controller;

    private MainClass mainClass;

    public RobotControllerController(MainClass mainClass) {
        this.mainClass = mainClass;
        FXMLLoader fxmlLoader = new FXMLLoader(RobotControllerController.class.getResource("/com.application.base.layout/robocontroller/robocontroller.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeButtons();

        if (mainClass.getSelectedSession().isBlank()) {
            controller.setDisable(true);
        } else {
            controller.setDisable(false);
        }
    }

    private void initializeButtons() {
        buttonUp.setOnMousePressed(e -> {
            this.setButtonUpPressed(true);
        });
        buttonUp.setOnMouseReleased(e -> {
            this.setButtonUpPressed(false);
        });

        buttonDown.setOnMousePressed(e -> {
            this.setButtonDownPressed(true);
        });
        buttonDown.setOnMouseReleased(e -> {
            this.setButtonDownPressed(false);
        });

        buttonLeft.setOnMousePressed(e -> {
            this.setButtonLeftPressed(true);
        });
        buttonLeft.setOnMouseReleased(e -> {
            this.setButtonLeftPressed(false);
        });

        buttonRight.setOnMousePressed(e -> {
            this.setButtonRightPressed(true);
        });
        buttonRight.setOnMouseReleased(e -> {
            this.setButtonRightPressed(false);
        });
    }

    public ScrollPane getContent() {
        return controller;
    }

    private void submitCommand(String command) {
        mainClass.getServiceManager().getInternetService().sendCommandToServer(command);
        mainClass.getServiceManager().getActionLogContentService().addAdditionalContent("Submitted command to Server : " + command);
        mainClass.refresh();
    }

    @FXML
    public void submitCommandZero() {
        submitCommand("0");
    }

    @FXML
    public void submitCommandOne() {
        submitCommand("1");
    }

    @FXML
    public void submitCommandTwo() {
        submitCommand("2");
    }

    @FXML
    public void submitCommandThree() {
        submitCommand("3");
    }

    @FXML
    public void submitCommandFour() {
        submitCommand("4");
    }

    @FXML
    public void submitCommandFive() {
        submitCommand("5");
    }

    @FXML
    public void submitCommandSix() {
        submitCommand("6");
    }

    @FXML
    public void submitCommandSeven() {
        submitCommand("7");
    }

    @FXML
    public void submitCommandEight() {
        submitCommand("8");
    }

    @FXML
    public void submitCommandNine() {
        submitCommand("9");
    }

    @FXML
    public void submitCommandLeft() {
        submitCommand("L");
    }

    @FXML
    public void submitCommandUp() {
        submitCommand("U");
    }

    @FXML
    public void submitCommandDown() {
        submitCommand("D");
    }

    @FXML
    public void submitCommandRight() {
        submitCommand("R");
    }

    @FXML
    public void submitCommandStar() {
        submitCommand("*");
    }

    @FXML
    public void submitCommandHashtag() {
        submitCommand("#");
    }

    @FXML
    public void submitCommandOK() {
        submitCommand("K");
    }

    @FXML
    public void submitCommandIdle() {
        submitCommand("I");
    }

    public void setButtonDownPressed(boolean buttonDownPressed) {
        if (this.buttonDownPressed == buttonDownPressed) {
            return;
        }
        this.buttonDownPressed = buttonDownPressed;

        if (buttonDownPressed) {
            submitCommandDown();
            return;
        }
        submitCommandIdle();
    }

    public void setButtonLeftPressed(boolean buttonLeftPressed) {
        if (this.buttonLeftPressed == buttonLeftPressed) {
            return;
        }
        this.buttonLeftPressed = buttonLeftPressed;

        if (buttonLeftPressed) {
            submitCommandLeft();
            return;
        }
        submitCommandIdle();
    }

    public void setButtonRightPressed(boolean buttonRightPressed) {
        if (this.buttonRightPressed == buttonRightPressed) {
            return;
        }
        this.buttonRightPressed = buttonRightPressed;

        if (buttonRightPressed) {
            submitCommandRight();
            return;
        }
        submitCommandIdle();
    }

    public void setButtonUpPressed(boolean buttonUpPressed) {
        if (this.buttonUpPressed == buttonUpPressed) {
            return;
        }

        this.buttonUpPressed = buttonUpPressed;

        if (buttonUpPressed) {
            submitCommandUp();
            return;
        }
        submitCommandIdle();
    }
}
