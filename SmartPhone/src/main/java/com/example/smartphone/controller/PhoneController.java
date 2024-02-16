package com.example.smartphone.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PhoneController {

    @FXML
    private Button priceButton;

    // Phương thức này được gọi khi controller được khởi tạo
    public void initialize() {
        // Thêm sự kiện cho nút
        priceButton.setOnAction(event -> handleButtonClick());
    }

    // Xử lý sự kiện khi nút được nhấn
    private void handleButtonClick() {
        // In ra "trang phone"
        System.out.println("Trang phone");
    }
}
