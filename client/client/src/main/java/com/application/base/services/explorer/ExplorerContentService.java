package com.application.base.services.explorer;

import com.application.base.actions.actions.ClickedOnExplorerContentAction;
import com.application.base.layout.explorer.ListViewContent;
import com.application.base.services.internet.InternetService;
import com.application.base.services.language.LanguageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplorerContentService {

    private LanguageService languageService;

    private InternetService internetService;

    private Map<Integer, String> contentIdToSessionIdMap;

    public ExplorerContentService(LanguageService languageService, InternetService internetService) {
        this.internetService = internetService;
        this.languageService = languageService;
        this.contentIdToSessionIdMap = new HashMap<>();
    }

    public ObservableList<ListViewContent> getExplorerContent() {
        ObservableList<ListViewContent> content = FXCollections.observableArrayList();
        String path = "/com.application.base.layout/explorer/robo.png";
        ImageView imageView = new ImageView(new Image(ExplorerContentService.class.getResourceAsStream(path)));

        List<String> listContent = internetService.getListOfSessions();

        for (int i = 0; i < listContent.size(); i++) {
            content.add(new ListViewContent(i, listContent.get(i), imageView, new ClickedOnExplorerContentAction(i,  this.languageService)));
            contentIdToSessionIdMap.put(i, listContent.get(i));
        }
        return content;
    }

    public String getSessionIdByContentId(int id) {
        return contentIdToSessionIdMap.get(id);
    }
}
