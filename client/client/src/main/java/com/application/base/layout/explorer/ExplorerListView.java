package com.application.base.layout.explorer;

import com.application.base.main.MainClass;
import javafx.scene.Cursor;
import javafx.scene.control.ListView;

public class ExplorerListView extends ListView<ListViewContent> {

    private MainClass mainClass;

    public ExplorerListView(MainClass mainClass) {
        this.getStylesheets().setAll("com.application.base.layout/explorer/list-view.css");
        this.mainClass = mainClass;

        this.setOnMouseEntered(event -> {
            this.setCursor(Cursor.DEFAULT);
        });
        this.setStyle("-fx-background-color: transparent; -fx-selection-bar:green ;");
    }

    @Override
    public void requestFocus() { }
}
