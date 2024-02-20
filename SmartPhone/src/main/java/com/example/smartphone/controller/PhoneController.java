package com.example.smartphone.controller;

import com.example.smartphone.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PhoneController {

    @FXML
    private Button phoneButton;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label priceLabel;
    // Xử lý sự kiện khi nút được nhấn
    // Xử lý sự kiện khi người dùng click vào nút handleTextClick

    // Phương thức để thêm đối tượng Order vào cơ sở dữ liệu
    private void insertOrderIntoDatabase(Order order, String username) {
        // Connection parameters
        String url = "jdbc:mysql://localhost:3306/e_project2";
        String dbUsername = "root";
        String dbPassword = "";
        // SQL query to insert the order into the database
        String sql = "INSERT INTO orderr (date, status_id, ProductName, price) " +
                "VALUES (?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setInt(1, 1); // Mặc định status_id là 1
            statement.setString(2, order.getProductName());
            statement.setDouble(3, order.getPrice());
            statement.setObject(4, order.getDate_order());
            // Thực thi PreparedStatement để chèn order vào cơ sở dữ liệu
            int affectedRows = statement.executeUpdate();

            // Xử lý kết quả
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrder_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleTextClick(MouseEvent event) {
        // Trích xuất thông tin sản phẩm từ các label
        String productName = ((Button) event.getSource()).getText(); // Lấy text của nút được click
        String priceStr = ((Button) event.getSource()).getText().replace("Đ", "").replace(",", "");
        priceStr = priceStr.replace(".", ""); // Loại bỏ các dấu chấm thừa
        double price = Double.parseDouble(priceStr);

        // Tạo một đối tượng Order mới và thiết lập thông tin sản phẩm
        Order order = new Order(1, 1, LocalDateTime.now(), "Product 1", 1, 100.0);
        order.setDate_order(LocalDate.now().atStartOfDay());
        order.setProductName(productName);
        order.setPrice(price);
        order.setStatus_id(1);

        // Lấy tên người dùng từ đâu đó, chẳng hạn từ một trường nhập liệu
        String username = System.getProperty("user.name");; // Thay bằng tên người dùng thực tế

        // Thêm đối tượng Order vào cơ sở dữ liệu
        insertOrderIntoDatabase(order, username);
    }


}
