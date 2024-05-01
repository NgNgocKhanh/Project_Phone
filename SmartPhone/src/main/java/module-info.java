module com.example.smartphone {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    requires java.sql; //
    requires javafx.graphics;
    requires java.base;

    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.smartphone.model;
    exports com.example.smartphone.view;
    opens com.example.smartphone.view;

    opens com.example.smartphone.controller to javafx.fxml;
    exports com.example.smartphone.controller to javafx.fxml;
}