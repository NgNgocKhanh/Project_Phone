package com.example.smartphone.controller;

import com.example.smartphone.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderController {

    @FXML
    private Label idLabel;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label successLabel;

    @FXML
    private Button completeButton;

    private Order order;

    public void initialize() {
        if (order != null) {
            // Cập nhật thông tin của đơn hàng
            idLabel.setText("ID: " + order.getOrder_id());
            productNameLabel.setText("Tên sản phẩm: " + order.getProductName());
            priceLabel.setText("Giá tiền: " + order.getPrice() + "Đ");
            dateLabel.setText("Ngày order: " + formatDate(order.getDate_order()));
            statusLabel.setText("Trạng thái: " + order.getStatus());
        }
    }

    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return date.format(formatter);
    }

    @FXML
    private void completeOrder() {
        // Thực hiện các hành động khi hoàn thành đơn hàng

        // Hiển thị thông báo "Success"
        successLabel.setVisible(true);
        // Ẩn nút "Hoàn thành"
        completeButton.setVisible(false);
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
