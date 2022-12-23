package com.application.base.layout.explorer;

import com.application.base.main.MainClass;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class ExplorerController {

    private MainClass mainClass;

    private ListView<ListViewContent> listView;

    private ObservableList<ListViewContent> contentList;

    public ExplorerController(MainClass mainClass) {
        this.mainClass = mainClass;
        this.initializeGuiContent();
    }

    protected void initializeGuiContent() {
        this.listView = new ExplorerListView(this.mainClass);
        this.contentList = this.mainClass.getServiceManager().getExplorerContentService().getExplorerContent();
        this.listView.setItems(contentList);
        this.listView.setCellFactory(listViewCell -> new ExplorerCell(this.mainClass));
    }

    public ListView<ListViewContent> getContent() {
        return this.listView;
    }

    public int getIdOfSelectedItem() {
        ListViewContent listViewContent = this.listView.getSelectionModel().getSelectedItem();

        if (listViewContent == null) {
            return -1;
        }

        return listViewContent.contentID();
    }

    public void selectItem(int selectedItem) {
        int indexOfItemToSelect = -1;

        for (ListViewContent listViewContent : this.listView.getItems()) {
            if (listViewContent.contentID() == selectedItem) {
                indexOfItemToSelect = this.listView.getItems().indexOf(listViewContent);
            }
        }

        if (indexOfItemToSelect != -1) {
            this.listView.getSelectionModel().select(indexOfItemToSelect);
        }
    }
}
