module com.example.smartphone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.smartphone to javafx.fxml;
    exports com.example.smartphone;
}