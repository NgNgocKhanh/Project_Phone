package com.example.smartphone.controller;

import com.example.smartphone.model.Order;
import com.example.smartphone.util.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

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

        // Get user_id from username
        int userId = getUserIdFromUsername(username);

        // SQL query to insert the order into the database
        String sql = "INSERT INTO orderr (user_id, date_order, status_id, ProductName, price) VALUES (?, ?, ?, ?, ?)";

        try (
                // Create a connection to the database
                Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
                // Create a PreparedStatement object for the SQL query
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            // Set values for the PreparedStatement parameters
            statement.setInt(1, userId);
            statement.setObject(2, order.getDate_order());
            statement.setInt(3, 1); // Mặc định status_id là 1
            statement.setString(4, order.getProductName());
            statement.setDouble(5, order.getPrice());

            // Execute the PreparedStatement to insert the order into the database
            int affectedRows = statement.executeUpdate();

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
        Order order = new Order();
        order.setDate_order(LocalDate.now().atStartOfDay());
        order.setProductName(productName);
        order.setPrice(price);
        order.setStatus_id(1);

        // Lấy tên người dùng từ đâu đó, chẳng hạn từ một trường nhập liệu
        String username = System.getProperty("user.name");; // Thay bằng tên người dùng thực tế

        // Thêm đối tượng Order vào cơ sở dữ liệu
        insertOrderIntoDatabase(order, username);
    }

    private int getUserIdFromUsername(String username) {
        int userId = -1; // Giá trị mặc định nếu không tìm thấy user

        String url = "jdbc:mysql://localhost:3306/e_project2";
        String dbUsername = "root";
        String dbPassword = "";
        String sql = "SELECT id FROM user WHERE username = ?";

        try (
                Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public static void main(String[] args) {
        try {
            String username = HomeController.
            System.out.println("Username: " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
