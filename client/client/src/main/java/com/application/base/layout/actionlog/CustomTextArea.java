package com.application.base.layout.actionlog;

import javafx.scene.control.TextArea;

public class CustomTextArea extends TextArea {

    public CustomTextArea() {
        this.getStylesheets().setAll("com.application.base.layout/actionlog/custom-text-area.css");
        this.setWrapText(true);
    }

    @Override
    public void requestFocus() { }

    public void addLine(String string) {
        if (this.getText().isEmpty()) {
            this.setText(string);
        } else {
            this.setText(this.getText() + "\n" + string);
        }
        this.appendText("");
    }

    public void clearActionLog() {
        this.setText("");
    }
}
