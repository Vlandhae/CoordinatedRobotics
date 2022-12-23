package com.application.base.layout.robocontroller;


import javafx.scene.control.ScrollPane;

public class CustomScrollPane extends ScrollPane {

    public CustomScrollPane() {
        this.getStylesheets().setAll("com.application.base.layout/robocontroller/list-view.css");
        this.setStyle(this.getStyle() + "-fx-background-color: transparent;");
    }

    @Override
    public void requestFocus() { }
}
