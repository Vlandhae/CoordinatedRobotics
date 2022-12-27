module com.application.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires java.sql;
    requires org.java_websocket;


    //requires tyrus.standalone.client;
    //requires javax.websocket.api;

    exports com.application.base.main;
    opens com.application.base.main to javafx.fxml;
    exports com.application.base.actions.actions;
    opens com.application.base.actions.actions to javafx.fxml;
    exports com.application.base.actions.actions.layoutactions;
    opens com.application.base.actions.actions.layoutactions to javafx.fxml;
    exports com.application.base.layout;
    opens com.application.base.layout to javafx.fxml;
    exports com.application.base.layout.explorer;
    opens com.application.base.layout.explorer to javafx.fxml;
    exports com.application.base.layout.robocontroller;
    opens com.application.base.layout.robocontroller to javafx.fxml;

    opens com.application.base.layout.center to javafx.fxml;
    exports com.application.base.layout.center;
}
