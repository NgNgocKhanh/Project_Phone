package com.example.smartphone.controller;

import com.example.smartphone.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderController {
    @FXML
    private TableView<Order> productTableView;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, String> productNameColumn;

    @FXML
    private TableColumn<Order, Double> priceColumn;

    @FXML
    private TableColumn<Order, Integer> statusColumn;

    @FXML
    private TableColumn<Order, String> dateColumn;

    public void initialize() {
        // Set up columns
        orderIdColumn.setCellValueFactory(data -> data.getValue().order_idProperty().asObject());
        productNameColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        statusColumn.setCellValueFactory(data -> data.getValue().status_idProperty().asObject());
        dateColumn.setCellValueFactory(data -> data.getValue().date_orderProperty().asString());

        // Set column formatting
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dateColumn.setCellFactory(column -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(LocalDateTime.parse(item, dateFormatter).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                }
            }
        });
        ObservableList<Order> orders = FXCollections.observableArrayList(
                new Order(1, 1, LocalDateTime.now(), "Product 1", 1, 100.0),
                new Order(2, 1, LocalDateTime.now(), "Product 2", 1, 150.0),
                new Order(3, 1, LocalDateTime.now(), "Product 3", 1, 200.0)
        );
        // Set data to TableView
        productTableView.setItems(orders);
    }
}
