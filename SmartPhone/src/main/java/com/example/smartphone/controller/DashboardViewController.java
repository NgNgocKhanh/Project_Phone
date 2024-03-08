package com.example.demojavafxproject.controller;

import dao.JDBCConnect;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.Locale;

public class DashboardController {
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    Connection connection = JDBCConnect.getJDBCConnection();
    private double x = 0, y = 0;

    @FXML
    private Label totalCarsLabel;

    @FXML
    private Label totalCustomersLabel;

    @FXML
    private Label totalEmployeeLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label totalOrderLabel;

    @FXML
    private Label totalOrdersMonthLabel;

    @FXML
    private Label revenueLabel;

    @FXML
    private Label compareRevenueLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label compareTotalOrderLabel;

    @FXML
    private LineChart<String, Number> revenueStatisticsLineChart;

    @FXML
    private BarChart<String, Number> orderBarChart;


    /**
     * Displays the total number of cars in the dashboard.
     * It queries the database to get the total count of cars and updates the corresponding label.
     */
    private void displayTotalCar() {
        //query total cars
        String query = "SELECT COUNT(*) AS totalCars FROM car;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int total = resultSet.getInt("totalCars");
                totalCarsLabel.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the total number of customers in the dashboard.
     * It queries the database to get the total count of customers and updates the corresponding label.
     */
    private void displayTotalCustomer() {
        // query total customers
        String query = "SELECT COUNT(*) AS totalCustomers FROM customer";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int total = resultSet.getInt("totalCustomers");
                totalCustomersLabel.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the total number of employees in the dashboard.
     * It queries the database to get the total count of employees and updates the corresponding label.
     */
    private void displayTotalEmployee() {
        // query total customers
        String query = "SELECT COUNT(*) AS totalEmployees FROM employee";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int total = resultSet.getInt("totalEmployees");
                totalEmployeeLabel.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the total number of orders in the dashboard.
     * It queries the database to get the total count of orders and updates the corresponding label.
     */
    private void displayTotalOrder() {
        // query total customers
        String query = "SELECT COUNT(*) AS totalOrders FROM `order`";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int total = resultSet.getInt("totalOrders");
                totalOrderLabel.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the line chart with revenue data within the selected date range.
     * Retrieves revenue data from the database and creates data points for the line chart.
     * Updates the total revenue label with the sum of revenue data points.
     */

    private void populateLineChartRevenue() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        String fullMonthName = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int currentYear = currentDate.getYear();

        int lastMonth, lastYear = 0;
        if (currentMonth == 1) {
            lastMonth = 12;
            lastYear = currentYear - 1;
        } else {
            lastMonth = currentMonth - 1;
            lastYear = currentYear;
        }

        double revenueThisMonth = 0, revenueLastMonth = 0;

        int lastDay = java.time.YearMonth.of(currentYear, currentMonth).lengthOfMonth();

        String sql = "SELECT DAY(orderDate) AS day, IFNULL(SUM(totalAmount), 0) AS revenue " +
                "FROM `order` " +
                "WHERE YEAR(orderDate) = ? AND MONTH(orderDate) = ? " +
                "GROUP BY DAY(orderDate)";

        try {
            PreparedStatement preparedStatementThisMonth = connection.prepareStatement(sql);
            preparedStatementThisMonth.setInt(1, currentYear);
            preparedStatementThisMonth.setInt(2, currentMonth);

            ResultSet resultSet = preparedStatementThisMonth.executeQuery();

            PreparedStatement preparedStatementLastMonth = connection.prepareStatement(sql);
            preparedStatementLastMonth.setInt(1, lastYear);
            preparedStatementLastMonth.setInt(2, lastMonth);

            ResultSet resultSetLastMonth = preparedStatementLastMonth.executeQuery();


            XYChart.Series<String, Number> series = new XYChart.Series<>();

            while (resultSet.next()) {
                int day = resultSet.getInt("day");
                double revenue = resultSet.getDouble("revenue");

                // Add the data points to the series
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(Integer.toString(day), revenue);
                series.getData().add(dataPoint);

                // Create a tooltip for the data point
                Tooltip tooltip = new Tooltip("Revenue: " + revenue + "\nDate: " + day + "/" + currentMonth + "/" + currentYear);
                tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

                // Postpone setting the tooltip until the line chart is fully rendered
                Platform.runLater(() -> {
                    Node node = dataPoint.getNode();
                    Tooltip.install(node, tooltip);

                    // Add event handler to show the tooltip when hovering over the data point
                    node.setOnMouseEntered(event -> tooltip.show(node, event.getScreenX(), event.getScreenY() + 20));
                    node.setOnMouseExited(event -> tooltip.hide());
                });

                revenueThisMonth += revenue;
            }
            while (resultSetLastMonth.next()) {
                revenueLastMonth += resultSetLastMonth.getDouble("revenue");

            }

            // Fill in missing days with zero revenue
            for (int day = 1; day <= lastDay; day++) {
                boolean found = false;
                for (XYChart.Data<String, Number> data : series.getData()) {
                    if (Integer.parseInt(data.getXValue()) == day) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    series.getData().add(new XYChart.Data<>(Integer.toString(day), 0));
                }
            }

            // Set the name for the series (optional)
            series.setName("Revenue " + fullMonthName);

            // Update revenueLabel and compareRevenueLabel
            revenueLabel.setText(String.format("%.2f", revenueThisMonth));
            compareRevenueLabel.setText(decimalFormat.format(revenueThisMonth - revenueLastMonth));

            // Update revenueLabel and compareRevenueLabel
//            revenueLabel.setText(String.format("%.2f", revenueThisMonth));
            revenueLabel.setText(decimalFormat.format(revenueThisMonth));

            double compareRevenue = revenueThisMonth - revenueLastMonth;


            // Set the text fill based on compareRevenue value
            if (compareRevenue > 0) {
                compareRevenueLabel.setText("+" + decimalFormat.format(compareRevenue));
                compareRevenueLabel.setTextFill(Color.GREEN);
            } else {
                compareRevenueLabel.setText(decimalFormat.format(compareRevenue));
                compareRevenueLabel.setTextFill(Color.RED);
            }

            // Sort the data by day
            series.getData().sort(Comparator.comparingInt(data -> Integer.parseInt(data.getXValue())));

            revenueStatisticsLineChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a custom tooltip for a data point with the total orders information.
     *
     * @param totalAmount The total number of orders for a specific data point.
     * @return The custom tooltip to display the total orders information.
     */
    private Tooltip createTooltipRevenue(int totalAmount) {
        Tooltip tooltip = new Tooltip("Orders: " + totalAmount);

        // Apply custom CSS styles to the tooltip
        tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

        return tooltip;
    }

    /**
     * Populates the line chart with order statistics based on the selected date range and order type.
     */
    private void populateBarChartOrders() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        String fullMonthName = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int currentYear = currentDate.getYear();

        int lastMonth, lastYear = 0;
        if (currentMonth == 1) {
            lastMonth = 12;
            lastYear = currentYear - 1;
        } else {
            lastMonth = currentMonth - 1;
            lastYear = currentYear;
        }

        int ordersThisMonth = 0, ordersLastMonth = 0;

        int lastDay = java.time.YearMonth.of(currentYear, currentMonth).lengthOfMonth();

        String sql = "SELECT DAY(orderDate) AS day, IFNULL(COUNT(*), 0) AS totalOrders " +
                "FROM `order` " +
                "WHERE YEAR(orderDate) = ? AND MONTH(orderDate) = ? " +
                "GROUP BY DAY(orderDate)";

        try {
            PreparedStatement preparedStatementThisMonth = connection.prepareStatement(sql);
            preparedStatementThisMonth.setInt(1, currentYear);
            preparedStatementThisMonth.setInt(2, currentMonth);

            ResultSet resultSet = preparedStatementThisMonth.executeQuery();

            PreparedStatement preparedStatementLastMonth = connection.prepareStatement(sql);
            preparedStatementLastMonth.setInt(1, lastYear);
            preparedStatementLastMonth.setInt(2, lastMonth);

            ResultSet resultSetLastMonth = preparedStatementLastMonth.executeQuery();


            XYChart.Series<String, Number> series = new XYChart.Series<>();

            while (resultSet.next()) {
                int day = resultSet.getInt("day");
                int orders = resultSet.getInt("totalOrders");

                // Add the data points to the series
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(Integer.toString(day), orders);
                series.getData().add(dataPoint);

                // Create a tooltip for the data point
                Tooltip tooltip = new Tooltip("Orders: " + orders + "\nDate: " + day + "/" + currentMonth + "/" + currentYear);
                tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

                // Postpone setting the tooltip until the line chart is fully rendered
                Platform.runLater(() -> {
                    Node node = dataPoint.getNode();
                    Tooltip.install(node, tooltip);

                    // Add event handler to show the tooltip when hovering over the data point
                    node.setOnMouseEntered(event -> tooltip.show(node, event.getScreenX(), event.getScreenY() + 20));
                    node.setOnMouseExited(event -> tooltip.hide());
                });

                ordersThisMonth += orders;
            }
            while (resultSetLastMonth.next()) {
                ordersLastMonth += resultSetLastMonth.getInt("totalOrders");

            }

            // Fill in missing days with zero revenue
            for (int day = 1; day <= lastDay; day++) {
                boolean found = false;
                for (XYChart.Data<String, Number> data : series.getData()) {
                    if (Integer.parseInt(data.getXValue()) == day) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    series.getData().add(new XYChart.Data<>(Integer.toString(day), 0));
                }
            }

            // Set the name for the series (optional)
            series.setName("Orders " + fullMonthName);


            totalOrdersLabel.setText(String.valueOf(ordersThisMonth));
            compareTotalOrderLabel.setText(String.valueOf(ordersThisMonth - ordersLastMonth));

            totalOrdersLabel.setText(String.valueOf(ordersThisMonth));

            int compareOrders = ordersThisMonth - ordersLastMonth;


            // Set the text fill based on compareRevenue value
            if (compareOrders > 0) {
                compareTotalOrderLabel.setText("+" + compareOrders);
                compareTotalOrderLabel.setTextFill(Color.GREEN);
            } else {
                compareTotalOrderLabel.setText(String.valueOf(compareOrders));
                compareTotalOrderLabel.setTextFill(Color.RED);
            }

            // Sort the data by day
            series.getData().sort(Comparator.comparingInt(data -> Integer.parseInt(data.getXValue())));

            orderBarChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a custom tooltip for a data point on the line chart.
     *
     * @param totalOrders The total number of orders for the data point.
     * @return A custom tooltip to display order statistics.
     */
    private Tooltip createTooltipOrder(int totalOrders) {
        Tooltip tooltip = new Tooltip("Orders: " + totalOrders);

        // Apply custom CSS styles to the tooltip
        tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

        return tooltip;
    }

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        String monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        totalRevenueLabel.setText("Total Revenue " + monthName + ":");
        totalOrdersMonthLabel.setText("Total Orders " + monthName + ":");

        displayTotalCar();
        displayTotalCustomer();
        displayTotalEmployee();
        displayTotalOrder();

        // Populate the line chart when the view is initialized
        populateLineChartRevenue();

        // Apply custom tooltips to each data point
        for (XYChart.Series<String, Number> series : revenueStatisticsLineChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                int totalOrders = data.getYValue().intValue();
                Tooltip tooltip = createTooltipRevenue(totalOrders);
                Tooltip.install(data.getNode(), tooltip);
            }
        }

        // Populate the line chart when the view is initialized
        populateBarChartOrders();

        // Apply custom tooltips to each data point
        for (XYChart.Series<String, Number> series : orderBarChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                int totalOrders = data.getYValue().intValue();
                Tooltip tooltip = createTooltipOrder(totalOrders);
                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }
}