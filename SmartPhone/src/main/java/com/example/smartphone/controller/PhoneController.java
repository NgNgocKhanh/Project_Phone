package com.example.smartphone.controller;

import com.example.smartphone.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String sql = "INSERT INTO orderr (user_id, status_id, ProductName, Price, date, phone_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setInt(1, 1); // Set the value for 'user_id'
            statement.setInt(2, order.getStatus_id()); // Set the value for 'status_id'
            statement.setString(3, order.getProductName()); // Set the value for 'ProductName'
            statement.setDouble(4, order.getPrice()); // Set the value for 'price'
            statement.setString(5, formattedDateTime); // Set the value for 'date'
            statement.setNull(6, java.sql.Types.INTEGER); // Set the value for 'phone_id'


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
        // Trích xuất thông tin sản phẩm từ nút được nhấp
        Button clickedButton = (Button) event.getSource();
        String ProductName = clickedButton.getText(); // Lấy nội dung của button được nhấp
        // Trích xuất giá sản phẩm từ nội dung của button
        String priceStr = ProductName.replace("Đ", "").replace(",", "");
        priceStr = priceStr.replace(".", ""); // Loại bỏ các dấu chấm thừa
        double price = Double.parseDouble(priceStr);

        // Tạo một đối tượng Order mới và thiết lập thông tin sản phẩm
        Order order = new Order();
        order.setDate_order(LocalDateTime.now());
        order.setProductName(ProductName);
        order.setPrice(price);
        order.setStatus_id(1);

        // Lấy tên người dùng từ đâu đó, chẳng hạn từ một trường nhập liệu
        String username = System.getProperty("user.name"); // Thay bằng tên người dùng thực tế

        // Thêm đối tượng Order vào cơ sở dữ liệu
        insertOrderIntoDatabase(order, username);
    }



}