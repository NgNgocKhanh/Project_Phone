package com.example.smartphone.controller;

import com.example.smartphone.model.Order;
import com.example.smartphone.model.Phone;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderController {
    @FXML
    private TableView<Order> productTableView;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, Phone> productColumn; // Sửa đổi kiểu dữ liệu của cột để chứa thông tin về sản phẩm

    @FXML
    private TableColumn<Order, Double> priceColumn;

    @FXML
    private TableColumn<Order, Integer> statusColumn;

    @FXML
    private TableColumn<Order, String> dateColumn;

    public void initialize() {
        // Set up columns
        orderIdColumn.setCellValueFactory(data -> data.getValue().setOrder_id();
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

        // Set sample data
        ObservableList<Order> orders = getOrdersFromDatabase();
        productTableView.setItems(orders);
    }

    private ObservableList<Order> getOrdersFromDatabase() {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String url = "jdbc:mysql://localhost:3306/e_project2";
        String dbUsername = "root";
        String dbPassword = "";
        String sql = "SELECT * FROM orderr";
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                LocalDateTime dateOrder = resultSet.getTimestamp("date").toLocalDateTime();
                String productName = resultSet.getString("ProductName");
                double price = resultSet.getDouble("price");
                int statusId = resultSet.getInt("status_id");

                orders.add(new Order(orderId, dateOrder, productName, price, statusId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
