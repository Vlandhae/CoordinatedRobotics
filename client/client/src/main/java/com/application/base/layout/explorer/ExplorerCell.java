package com.application.base.layout.explorer;

import com.application.base.actions.ActionEventArgs;
import com.application.base.main.MainClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;

public class ExplorerCell extends ListCell<ListViewContent> {

    @FXML
    private HBox hBox;

    @FXML
    private ImageView image;

    @FXML
    private Text text;

    public ExplorerCell(MainClass mainClass) {
        this.setOnMouseClicked(event -> {

            if ((event.getClickCount() != 2)
                    || (this.getItem() == null)
                    || (this.getItem().action() == null)) {
                return;
            }
            this.getItem().action().invokeExecute(new ActionEventArgs(mainClass));
        });

    }

    @Override
    protected void updateItem(ListViewContent content, boolean empty) {
        super.updateItem(content, empty);
        setStyle("-fx-background-color: transparent;");

        if (content == null) {
            setText(null);
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(ExplorerCell.class.getResource("/com.application.base.layout/explorer/list-cell.fxml"));
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        text.setText(content.text());
        changeColor();
        if (content.icon() != null) {
            image.setImage(content.icon().getImage());
        }

        setText(null);
        setGraphic(hBox);
    }

    private void changeColor() {
        if(isSelected()) {
            setStyle("-fx-background-color: #2A75BB;");
            text.setFill(Color.WHITE);
        } else {
            setStyle("-fx-background-color: transparent;");
            text.setFill(Color.BLACK);
        }
    }
}
