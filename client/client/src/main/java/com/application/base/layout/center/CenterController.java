package com.application.base.layout.center;

import com.application.base.layout.robocontroller.RobotControllerController;
import com.application.base.main.MainClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;

import java.io.IOException;

public class CenterController {

    private MainClass mainClass;

    public CenterController(MainClass mainClass) {
        this.mainClass = mainClass;
    }

    public ScrollPane getContent() {

        RoomController roomController = new RoomController();
        roomController.load();

        ScrollPane scrollPane = new ScrollPane(roomController.gridPane) {
            @Override
            public void requestFocus() { }
        };
        scrollPane.setStyle("-fx-background-color:  lightGrey;");
        return scrollPane;
    }
}
