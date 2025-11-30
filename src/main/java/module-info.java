module com.example.firmwise {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.firmwise to javafx.fxml;
    opens com.example.firmwise.controller to javafx.fxml;
    opens com.example.firmwise.model to javafx.base;

    exports com.example.firmwise;
}