package com.application.base.main;

import com.application.base.actions.ActionEventArgs;
import com.application.base.actions.actions.layoutactions.SaveWindowPropertiesAction;
import com.application.base.services.ServiceManager;
import com.application.base.services.action.GlobalActionCollection;
import com.application.base.services.action.MenuActionCollection;
import com.application.base.services.language.LanguageService;
import com.application.base.services.persistence.StagePropertiesListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClass extends Application {

    private String selectedSession = "";

    private ServiceManager serviceManager;

    private MainController mainController;

    private Stage stage;

    public static void main(String[] args) {
        launch();
    }

    public MainClass() {
        this.selectedSession = "";
    }

    public List<SelectionObserver> selectionObservers;

    @Override
    public void start(Stage stage) throws IOException {
        this.initializeApplication();
        this.loadGui(stage);
        this.initializeListeners(stage);
        this.setWindowProperties(stage);
        this.populateGlobalActions();
        this.populateMenuActions();
        stage.show();
    }

    private void initializeApplication() {
        this.selectionObservers = new ArrayList<>();
        this.serviceManager = new ServiceManager(this);
        this.mainController = new MainController(this, serviceManager.getSettingsService());
    }

    private void loadGui(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("/com/application/base.main/main-view.fxml"));
        fxmlLoader.setController(this.mainController);
        Scene scene = new Scene(fxmlLoader.load());
        this.stage.setTitle(serviceManager.getLanguageService().getString("MainFrameTitle"));
        this.stage.setScene(scene);
    }

    private void initializeListeners(Stage stage) {
        StagePropertiesListener stagePropertiesListener
                = serviceManager.getPersistenceService().getStagePropertiesListener();

        stagePropertiesListener.initializeListener(stage);

        stage.xProperty().addListener(stagePropertiesListener);

        stage.yProperty().addListener(stagePropertiesListener);

        stage.widthProperty().addListener(stagePropertiesListener);

        stage.heightProperty().addListener(stagePropertiesListener);

        stage.maximizedProperty().addListener(stagePropertiesListener);
    }

    private void setWindowProperties(Stage stage) {
        double windowWidth = serviceManager.getSettingsService().getWindowWidth();
        double windowHeight = serviceManager.getSettingsService().getWindowHeight();
        double windowX = serviceManager.getSettingsService().getWindowX();
        double windowY = serviceManager.getSettingsService().getWindowY();
        boolean isMaximized = serviceManager.getSettingsService().isWindowMaximized();

        stage.setWidth(windowWidth);
        stage.setMinWidth(550);
        stage.setHeight(windowHeight);
        stage.setMinHeight(750);
        if (windowX != -1.1) { this.stage.setX(windowX); }
        if (windowY != -1.1) { this.stage.setY(windowY); }
        if (isMaximized) { this.stage.setMaximized(true); }
    }

    public void setWindowProperties(double windowWidth, double windowHeight,
                                         double windowX, double windowY, boolean isMaximized) {
        this.stage.setMaximized(false);
        this.stage.setWidth(windowWidth);
        this.stage.setHeight(windowHeight);
        this.stage.setX(windowX);
        this.stage.setY(windowY);
        if (isMaximized) { this.stage.setMaximized(true); }
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public MainController getMainController() {
        return mainController;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void stop(){
        LanguageService languageService = getServiceManager().getLanguageService();
        SaveWindowPropertiesAction saveWindowPropertiesAction = new SaveWindowPropertiesAction(languageService);
        saveWindowPropertiesAction.invokeExecute(new ActionEventArgs(this));
        getServiceManager().getInternetService().closeConnectionToWebSocket();
    }

    private void populateGlobalActions() {
        GlobalActionCollection globalActionCollection = serviceManager.getActionService().getGlobalActions();
        mainController.populateGlobalActions(globalActionCollection);
    }

    private void populateMenuActions() {
        MenuActionCollection[] menuActionCollections = serviceManager.getActionService().getMenuActionCollections();
        mainController.populateMenuActions(menuActionCollections);
    }

    public void refresh() {
        this.mainController.refresh();
    }

    public void setSelectedSession(String selectedSession) {
        this.selectedSession = selectedSession;
        this.notifySelectionObservers();
    }

    public String getSelectedSession() {
        return selectedSession;
    }

    public void addSelectionObserver(SelectionObserver selectionObserver) {
        this.selectionObservers.add(selectionObserver);
    }

    private void notifySelectionObservers() {
        for (SelectionObserver selectionObserver : selectionObservers) {
            selectionObserver.selectionChanged(this.selectedSession);
        }
    }
}