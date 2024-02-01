module com.example.smartphone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.smartphone to javafx.fxml;
    exports com.example.smartphone;
}