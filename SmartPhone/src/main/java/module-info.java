module com.example.smartphone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires java.mail;


    opens com.example.smartphone to javafx.fxml;
    exports com.example.smartphone.controller;
    exports com.example.smartphone.model;
    opens com.example.smartphone.model to javafx.fxml;
    opens com.example.smartphone.controller to javafx.fxml;
    exports com.example.smartphone.view;
    opens com.example.smartphone.view to javafx.fxml;
}