package com.example.smartphone.controller;

import com.example.smartphone.model.Event;
import com.example.smartphone.model.Order;
import dao.JDBCConnect;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderHistoryController {

    @FXML
    private TableColumn<Order, String> phoneNameTableColumn;
    @FXML
    private Button close;

    @FXML
    private TableColumn<Event, Integer> orderNumberTableColumn;

    @FXML
    private TableColumn<Order, String> customerTableColumn;

    @FXML
    private ComboBox<String> filterStatusComboBox;

    @FXML
    private Button minimizeButton;

    @FXML
    private TableColumn<Order, String> orderDateTableColumn;

    @FXML
    private TableColumn<Order, Integer> orderIdTableColumn;

    @FXML
    private TableColumn<Order, String> orderStatusTableColumn;

    @FXML
    private TableView<Order> orderTableView;

    @FXML
    private AnchorPane orderViewPage;

    @FXML
    private Pagination paginationPagination;

    @FXML
    private TableColumn<Order, String> paymentStatusTableColumn;

    @FXML
    private TableColumn<Order, String> paymentTableColumn;

    @FXML
    private TableColumn<Order, Integer> quantityTableColumn;

    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private TextField searchKeywordTextField;

    @FXML
    private TableColumn<Order, String> sellerTableColumn;

    @FXML
    private TableColumn<Order, Double> totalAmountTableColumn;

    @FXML
    private Label totalCompletedAmountLabel;

    @FXML
    private Label totalCompletedOrderLabel;

    @FXML
    private Label totalConfirmedAmountLabel;
    @FXML
    private TableColumn<Order, String> checkBoxTableColumn;
    @FXML
    private Label totalConfirmedOrderLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label totalNewOrderAmountLabel;

    @FXML
    private Label totalNewOrderLabel;

    @FXML
    private Label totalShippingAmountLabel;

    @FXML
    private Label totalShippingOrderLabel;

    @FXML
    private CheckBox selectAllCheckBox;
    Connection connection = JDBCConnect.getJDBCConnection();
    ObservableList<Order> orderObservableList = FXCollections.observableArrayList();
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    private final int itemsPerPage = 20;
    private int currentPage = 1;

    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void initialize() {
        setupTable();
        addStatusToComboBox();
        setupPagination();
        addSearchByToComboBox();
        setupTotalNewOrder();
        setupTotalConfirmedOrder();
        setupTotalShippingOrder();
        setupTotalCompletedOrder();

        // Bind select all CheckBox to the selectedProperty of all Order objects
        selectAllCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            for (Order order : orderObservableList) {
                order.setSelected(newValue);
            }
        });

    }

    /**
     * Sets up the total new order label and amount based on orders within the last 7 days.
     */
    private void setupTotalNewOrder() {
        try {
            String queryTotalNewOrder = "SELECT COUNT(*) AS totalNewOrder FROM `order` WHERE `orderDate` >= CURDATE() - INTERVAL 7 DAY";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryTotalNewOrder);

            while (resultSet.next()) {
                int totalNewOrder = resultSet.getInt("totalNewOrder");
                totalNewOrderLabel.setText("Total orders: " + totalNewOrder);

            }

            String queryTotalAmountNewOrder = "SELECT SUM(totalAmount) AS totalAmountNewOrder FROM `order` WHERE `orderDate` >= CURDATE() - INTERVAL 7 DAY";
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(queryTotalAmountNewOrder);

            while (resultSet1.next()) {
                double totalAmountNewOrder = resultSet1.getDouble("totalAmountNewOrder");
                totalNewOrderAmountLabel.setText("Total amount: " + decimalFormat.format(totalAmountNewOrder));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the total confirmed order label and amount based on orders with statusId = 1.
     */
    private void setupTotalConfirmedOrder() {
        try {
            String queryTotalConfirmedOrder = "SELECT COUNT(*) AS totalConfirmedOrder FROM `order` WHERE statusId = 1";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryTotalConfirmedOrder);

            while (resultSet.next()) {
                int totalConfirmedOrder = resultSet.getInt("totalConfirmedOrder");
                totalConfirmedOrderLabel.setText("Total orders: " + totalConfirmedOrder);
            }

            String queryTotalAmountConfirmedOrder = "SELECT SUM(totalAmount) AS totalAmountConfirmedOrder FROM `order` WHERE statusId = 1";
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(queryTotalAmountConfirmedOrder);

            while (resultSet1.next()) {
                double totalAmountConfirmedOrder = resultSet1.getDouble("totalAmountConfirmedOrder");
                totalConfirmedAmountLabel.setText("Total amount: " + decimalFormat.format(totalAmountConfirmedOrder));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the total shipping order label and amount based on orders with statusId = 3.
     */
    private void setupTotalShippingOrder() {
        try {
            String queryTotalShippingOrder = "SELECT COUNT(*) AS totalShippingOrder FROM `order` WHERE statusId = 3";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryTotalShippingOrder);

            while (resultSet.next()) {
                int totalShippingOrder = resultSet.getInt("totalShippingOrder");
                totalShippingOrderLabel.setText("Total orders: " + totalShippingOrder);
            }

            String queryTotalAmountShippingOrder = "SELECT SUM(totalAmount) AS totalAmountShippingOrder FROM `order` WHERE statusId = 3";
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(queryTotalAmountShippingOrder);

            while (resultSet1.next()) {
                double totalAmountShippingOrder = resultSet1.getDouble("totalAmountShippingOrder");
                totalShippingAmountLabel.setText("Total amount: " + decimalFormat.format(totalAmountShippingOrder));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the total completed order label and amount based on orders with statusId = 4.
     */
    private void setupTotalCompletedOrder() {
        try {
            String queryTotalCompletedOrder = "SELECT COUNT(*) AS totalCompletedOrder FROM `order` WHERE statusId = 4";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryTotalCompletedOrder);

            while (resultSet.next()) {
                int totalCompletedOrder = resultSet.getInt("totalCompletedOrder");
                totalCompletedOrderLabel.setText("Total orders: " + totalCompletedOrder);
            }

            String queryTotalAmountCompletedOrder = "SELECT SUM(totalAmount) AS totalAmountCompletedOrder FROM `order` WHERE statusId = 4";
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(queryTotalAmountCompletedOrder);

            while (resultSet1.next()) {
                double totalAmountCompletedOrder = resultSet1.getDouble("totalAmountCompletedOrder");
                totalCompletedAmountLabel.setText("Total amount: " + decimalFormat.format(totalAmountCompletedOrder));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds search options to the searchComboBox.
     */
    private void addSearchByToComboBox() {
        searchComboBox.getItems().addAll("Customer", "Seller", "Make", "Model", "Price", "Status", "Date", "Amount");
        searchComboBox.setValue("Customer");
    }

    /**
     * Exports selected orders to an Excel file.
     * The user can choose the location and name of the exported file.
     */
    @FXML
    void exportToFileExcel() {
        // Get the selected orders
        List<Order> selectedOrders = orderObservableList.stream().filter(Order::isSelected).collect(Collectors.toList());

        if (selectedOrders.isEmpty()) {
            GetData.showWarningAlert("Export Warning", "No rows selected to export!");
            return;
        }

        // Create FileChooser to let the user choose the file location and name
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Order Data");

                // Create header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Order ID");
                headerRow.createCell(1).setCellValue("Customer Name");
                headerRow.createCell(2).setCellValue("Order Date");
                headerRow.createCell(3).setCellValue("Total Amount");
                headerRow.createCell(4).setCellValue("Order Status");
                headerRow.createCell(5).setCellValue("Seller Name");
                headerRow.createCell(6).setCellValue("Order Quantity");
                headerRow.createCell(7).setCellValue("Payment Type");
                headerRow.createCell(8).setCellValue("Payment Status");

                // Fill data rows
                int rowIndex = 1;
                for (Order order : selectedOrders) {
                    Row dataRow = sheet.createRow(rowIndex++);
                    dataRow.createCell(0).setCellValue(order.getOrderId());
                    dataRow.createCell(1).setCellValue(order.getCustomerName());
                    dataRow.createCell(2).setCellValue(order.getOrderDate());
                    dataRow.createCell(3).setCellValue(order.getTotalAmount());
                    dataRow.createCell(4).setCellValue(order.getOrderStatus());
                    dataRow.createCell(5).setCellValue(order.getEmployeeName());
                    dataRow.createCell(6).setCellValue(order.getName());
                    dataRow.createCell(7).setCellValue(order.getOrderQuantity());
                    dataRow.createCell(8).setCellValue(order.getPaymentType());
                    dataRow.createCell(9).setCellValue(order.getPaymentStatus());
                }

                // Adjust column widths
                for (int i = 0; i < 11; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Save the workbook to the chosen file location
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    GetData.showSuccessAlert("Success message", "Selected orders exported successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    workbook.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves data from the database and adds it to an observable list.
     *
     * @return An ObservableList containing Order objects with data from the database.
     */
    private ObservableList<Order> getOrderObservableList() {
        ObservableList<Order> observableList = FXCollections.observableArrayList();
        String sql = "SELECT o.orderId,c.customerId,c.customerName,o.orderDate,o.totalAmount,s.statusName,e.employeeId,e.employeeName,car.phoneId,car.make,car.model, car.price, car.tax, o.quantity,o.statusId, s.statusName ,p.paymentId, p.paymentType,ps.paymentStatusId, ps.paymentStatus " +
                "FROM `order` o " +
                "JOIN customer c ON c.customerId  = o.customerId " +
                "JOIN status s ON s.statusId  = o.statusId " +
                "JOIN employee e ON e.employeeId = o.employeeId " +
                "JOIN car ON car.phoneId = o.phoneId " +
                "JOIN payment p ON o.paymentId = p.paymentId " +
                "JOIN paymentStatus ps ON ps.paymentStatusId  = o.paymentStatusId " +
                "ORDER BY o.orderId";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("orderId");
                int customerId = resultSet.getInt("customerId");
                String customerName = resultSet.getString("customerName");
                String orderDate = resultSet.getString("orderDate");
                double totalAmount = resultSet.getDouble("totalAmount");
                int orderStatusId = resultSet.getInt("statusId");
                String orderStatus = resultSet.getString("statusName");
                int employeeId = resultSet.getInt("employeeId");
                String employeeName = resultSet.getString("employeeName");
                int phoneId = resultSet.getInt("phoneId");
                String phoneName = resultSet.getString("make");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                int paymentId = resultSet.getInt("paymentId");
                String paymentType = resultSet.getString("paymentType");
                int paymentStatusId = resultSet.getInt("paymentStatusId");
                String paymentStatus = resultSet.getString("paymentStatus");

                // add to list
                observableList.add(new Order(orderId, customerId, customerName, orderDate, totalAmount, orderStatusId, orderStatus, employeeId, employeeName, phoneId, phoneName, price, quantity, paymentId, paymentType, paymentStatusId, paymentStatus));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }


    /**
     * Sets up the order history table with data from the database.
     * Configures checkbox column and search functionality.
     */
    private void setupTable() {
        orderObservableList = getOrderObservableList();

        Callback<TableColumn<Order, String>, TableCell<Order, String>> columnTableCellCallback = (param) -> {
            final TableCell<Order, String> checkBoxCell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    if (isEmpty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final CheckBox checkbox = new CheckBox();
                        checkbox.selectedProperty().bindBidirectional(getTableRow().getItem().selectedProperty());
                        setGraphic(checkbox);
                        setText(null);
                    }
                }
            };
            return checkBoxCell;
        };
        checkBoxTableColumn.setCellFactory(columnTableCellCallback);

        orderIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        customerTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        totalAmountTableColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        orderStatusTableColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        sellerTableColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        phoneNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneName"));
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("orderQuantity"));
        paymentTableColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        paymentStatusTableColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        orderNumberTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(orderTableView.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

        totalLabel.setText("Total: " + orderObservableList.size());

        // Create a custom cell factory for the orderDateTableColumn
        orderDateTableColumn.setCellFactory(new Callback<TableColumn<Order, String>, TableCell<Order, String>>() {
            @Override
            public TableCell<Order, String> call(TableColumn<Order, String> column) {
                return new TableCell<Order, String>() {
                    private final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    private final DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            try {
                                Date date = inputFormat.parse(item);
                                setText(outputFormat.format(date));
                            } catch (ParseException e) {
                                setText("");
                            }
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

        // Create filtered list
        FilteredList<Order> filteredList = new FilteredList<>(orderObservableList, b -> true);
        // Listen to the changes in the searchKeyword and search
        searchKeywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterStatusComboBox.setValue(null);
            filteredList.setPredicate(order -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    totalLabel.setText("Total: " + orderObservableList.size());
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                String searchBy = searchComboBox.getValue().toLowerCase();

                if (searchBy.equals("customer") && order.getCustomerName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("make") && order.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("seller") && order.getEmployeeName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("date") && order.getOrderDate().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("status") && order.getOrderStatus().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("amount") && String.valueOf(order.getTotalAmount()).contains(searchKeyword)) {
                    return true;
                } else {
                    totalLabel.setText("Total: " + filteredList.size());
                    return false;
                }
            });
            // Update pagination
            updatePagination(filteredList);
        });

        filterStatusComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            searchKeywordTextField.clear();
            filteredList.setPredicate(order -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    totalLabel.setText("Total: " + orderObservableList.size());
                    return true;
                }

                String filterBy = newValue.toLowerCase();
                if (order.getOrderStatus().toLowerCase().contains(filterBy)) {
                    return true;
                } else {
                    totalLabel.setText("Total: " + filteredList.size());
                    return false;
                }
            });
            updatePagination(filteredList);
        });
        // Update pagination
        updatePagination(filteredList);
    }

    /**
     * Updates the table data based on the current pagination page.
     *
     * @param pageIndex The index of the current pagination page.
     */
    private void updateTableData(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, orderObservableList.size());
        orderTableView.setItems(FXCollections.observableArrayList(orderObservableList.subList(fromIndex, toIndex)));
    }

    /**
     * Sets up the pagination control and links it to the order history table.
     */
    private void setupPagination() {
        int totalPages = (orderObservableList.size() / itemsPerPage) + (orderObservableList.size() % itemsPerPage > 0 ? 1 : 0);
        paginationPagination.setPageCount(totalPages);

        paginationPagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableData(newValue.intValue());
        });
    }

    /**
     * Updates the pagination control based on the filtered order list.
     *
     * @param filteredList The FilteredList containing the filtered orders.
     */
    private void updatePagination(FilteredList<Order> filteredList) {
        int totalItems = filteredList.size();
        int pageCount = (totalItems + itemsPerPage - 1) / itemsPerPage;

        // adjust the pagination's page count and current page if needed
        if (pageCount == 0) {
            pageCount = 1;
        }
        paginationPagination.setPageCount(pageCount);

        if (paginationPagination.getCurrentPageIndex() >= pageCount) {
            paginationPagination.setCurrentPageIndex(pageCount - 1);
        }

        // update the tableView base on the current page
        int fromIndex = paginationPagination.getCurrentPageIndex() * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);

        SortedList<Order> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(orderTableView.comparatorProperty());

        orderTableView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    /**
     * Retrieves status data from the database and puts it into a HashMap (key-value).
     *
     * @return A Map with status IDs as keys and status names as values.
     */
    private Map<Integer, String> getStatusMap() {
        // create an empty Map to store status data
        Map<Integer, String> statusMap = new HashMap<>();

        String sql = "SELECT statusId, statusName FROM status";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                // iterate through the resultSet from db and put into map
                int statusId = resultSet.getInt("statusId");
                String statusName = resultSet.getString("statusName");

                statusMap.put(statusId, statusName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusMap;
    }

    /**
     * Adds status names to the filterStatusComboBox using the statusMap.
     */
    private void addStatusToComboBox() {
        Map<Integer, String> statusMap = getStatusMap();
        filterStatusComboBox.getItems().addAll(statusMap.values());
    }

    /**
     * Resets the order history form, clearing filters and search fields.
     */
    @FXML
    void resetForm() {
        filterStatusComboBox.setValue(null);
        searchKeywordTextField.clear();
        // Clear all the checkboxes in the table
        for (Order order : orderObservableList) {
            order.setSelected(false);
        }
        setupTable();
    }
}
