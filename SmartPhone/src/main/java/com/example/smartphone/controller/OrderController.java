package com.example.smartphone.controller;

import com.example.smartphone.model.Order;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderController {

    @FXML
    private TableView<Order> productTableView;

    @FXML
    private TableColumn<Order, Integer> phoneIdModelView;

    @FXML
    private TableColumn<Order, String> phoneProductNameView;

    @FXML
    private TableColumn<Order, Double> phonePriceModelView;

    @FXML
    private TableColumn<Order, Integer> phoneStatusModelView;

    @FXML
    private TableColumn<Order, String> phoneDateModelView;

    public void initialize() {
        // Thiết lập cột của TableView
        phoneIdModelView.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        phoneProductNameView.setCellValueFactory(new PropertyValueFactory<>("productName"));
        phonePriceModelView.setCellValueFactory(new PropertyValueFactory<>("price"));
        phoneStatusModelView.setCellValueFactory(new PropertyValueFactory<>("status"));
        phoneDateModelView.setCellValueFactory(new PropertyValueFactory<>("date_order"));
}
    public void updateOrderTableView(Order order) {
        ObservableList<Order> items = productTableView.getItems();
        items.add(order);
        productTableView.setItems(items);
    }}

