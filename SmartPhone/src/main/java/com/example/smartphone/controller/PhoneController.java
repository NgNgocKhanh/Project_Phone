    package com.example.smartphone.controller;

    import com.example.smartphone.model.Phone;
    import dao.JDBCConnect;
    import javafx.beans.property.ReadOnlyObjectWrapper;
    import javafx.beans.value.ChangeListener;
    import javafx.beans.value.ObservableValue;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.collections.transformation.FilteredList;
    import javafx.collections.transformation.SortedList;
    import javafx.event.ActionEvent;
    import javafx.event.Event;
    import javafx.fxml.FXML;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyEvent;
    import javafx.scene.layout.StackPane;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;
    import javafx.util.Callback;

    import java.io.File;
    import java.io.IOException;
    import java.net.URL;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.Statement;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.regex.Pattern;

    public class PhoneController {
        @FXML
        private TextField quantityTextField;

        @FXML
        private Label actionStatusLabel;

        @FXML
        private ComboBox<String> searchComboBox;

        @FXML
        private Label totalLabel;

        @FXML
        private Button addButton;

        @FXML
        private ImageView phoneImageView;

        @FXML
        private TableView<Phone> phoneTableView;

        @FXML
        private Button chooseImageButton;

        @FXML
        private TableColumn<Phone, String> deleteTableColumn;

        @FXML
        private ComboBox<String> distributorComboBox;

        @FXML
        private TableColumn<Phone, String> distributorTableColumn;

        @FXML
        private TableColumn<Event, Integer> orderNumberTableColumn;

        @FXML
        private TableColumn<Phone, Integer> idTableColumn;

        @FXML
        private TextField idTextField;


        @FXML
        private Pagination paginationPagination;

        @FXML
        private TableColumn<Phone, Double> priceTableColumn;
        @FXML
        private TableColumn<Phone, String> phoneTableColumn;

        @FXML
        private TextField priceTextField;


        @FXML
        private TextField searchKeywordTextField;

        @FXML
        private Button updateButton;
        @FXML
        private TextField phoneTextField;


        @FXML
        private TextField sellingPriceTextField;

        @FXML
        private TableColumn<Phone, Double> sellingPriceTableColumn;

        private final int currentPage = 1;
        Connection connection = JDBCConnect.getJDBCConnection();


        ObservableList<Phone> phoneObservableList = FXCollections.observableArrayList();

        private static final int itemsPerPage = 12; // final variable to specify number of items per page

        public void initialize() {
            setIdAdd();
            addDistributorNameToComboBox();
            selectedRecord();
            setupTable();
            setupPagination();
            addSearchByToComboBox();

            // Set the custom row factory
            phoneTableView.setRowFactory(tv -> {
                TableRow<Phone> row = new TableRow<>();
                row.itemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && newValue.getQuantity() == 0) {
                        row.getStyleClass().add("white-row");
                    } else {
                        row.getStyleClass().remove("white-row");
                    }
                });
                return row;
            });


            quantityTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                if (!isNumeric(event.getCharacter())) {
                    event.consume();
                }
            });

            priceTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                if (!isNumericSalary(event.getCharacter())) {
                    event.consume();
                }
            });

            sellingPriceTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                if (!isNumericSalary(event.getCharacter())) {
                    event.consume();
                }
            });

        }

        private boolean isNumericSalary(String str){
            return str.matches("\\d*(\\.\\d*)?");
        }

        private boolean isNumeric(String str) {
            return str.matches("\\d*"); // Check if the given string contains only digits
        }

        private ObservableList<Phone> getListPhone() {
            ObservableList<Phone> observableList = FXCollections.observableArrayList();
            String sql = "SELECT p.phoneId, p.name, p.image, p.price, p.sellingPrice, d.distributorName, i.quantityInStock\n" +
                    "FROM phone AS p\n" +
                    "JOIN distributor AS d ON p.distributorId = d.distributorId\n" +
                    "JOIN phone_inventory AS i ON p.phoneId = i.phoneId\n" +
                    "ORDER BY p.sellingPrice\n" +
                    "LIMIT 1000;\n";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    // iterate through the resultSet from db and add to list
                    int id = resultSet.getInt("phoneId");
                    double price = resultSet.getDouble("price");
                    String Name = resultSet.getString("phoneName");
                    String image = resultSet.getString("image");
                    String distributorComboboxx = resultSet.getString("distributorName");
                    int quantity = resultSet.getInt("quantityInStock");
                    double sellingPrice = resultSet.getDouble("sellingPrice");

                    // add to list
                    observableList.add(new Phone(id, Name, price, distributorComboboxx, image,sellingPrice));}
            } catch (Exception e) {
                e.printStackTrace();
            }
            return observableList;
        }

        private void setupTable() {
            phoneObservableList = getListPhone();
            phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneName"));
            priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            distributorTableColumn.setCellValueFactory(new PropertyValueFactory<>("distributor"));
            sellingPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
            orderNumberTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(phoneTableView.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

    //        imageTableColumn.se   tCellValueFactory(new ("image"));
            totalLabel.setText("Total: " + phoneObservableList.size());

            phoneTableView.setRowFactory(tableView -> {
                TableRow<Phone> row = new TableRow<>();
                StackPane stackPane = new StackPane();
                double imageWidth = 250; // Default image width
                double imageHeight = 150; // Default image height

                row.setOnMouseEntered(event -> {
                    Phone phone = row.getItem();
                    if (phone != null && phone.getImage() != null) {
                        Tooltip tooltip = new Tooltip();
                        ImageView imageView = new ImageView(new Image(new File(phone.getImage()).toURI().toString()));

                        // Set resizing properties
                        imageView.setPreserveRatio(true);
                        imageView.setSmooth(true); // Apply smoother image resizing

                        // Calculate dimensions to fit within the specified width and height
                        double fitWidth = imageWidth;
                        double fitHeight = imageHeight;
                        if (imageView.getImage().getWidth() > imageView.getImage().getHeight()) {
                            fitHeight = fitWidth / imageView.getImage().getWidth() * imageView.getImage().getHeight();
                        } else {
                            fitWidth = fitHeight / imageView.getImage().getHeight() * imageView.getImage().getWidth();
                        }

                        imageView.setFitWidth(fitWidth); // Set the image width
                        imageView.setFitHeight(fitHeight); // Set the image height

                        stackPane.getChildren().setAll(imageView);
                        stackPane.setPrefWidth(imageWidth); // Set the StackPane width
                        stackPane.setPrefHeight(imageHeight); // Set the StackPane height
                        tooltip.setGraphic(stackPane);
                        Tooltip.install(row, tooltip);
                    }
                });

                row.setOnMouseExited(event -> Tooltip.uninstall(row, row.getTooltip()));
                return row;
            });
            // create a TableCell to contain delete button
            // this Callback function is used to create a TableCell for each row
            Callback<TableColumn<Phone, String>, TableCell<Phone, String>> columnTableCellCallback = (param) -> {
                final TableCell<Phone, String> deleteCell = new TableCell<>() { // create a new TableCell to display delete button
                    // method is called whenever the table needs to update the display of a cell
                    @Override
                    public void updateItem(String item, boolean isEmpty) {
                        super.updateItem(item, isEmpty);
                        // if the cell is empty, the delete button will not be displayed
                        if (isEmpty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            final Button deleteButton = new Button("Delete"); // create new button
                            deleteButton.getStyleClass().add("delete-button");
                            deleteButton.setOnAction(event -> {
                                Phone phone = getTableView().getItems().get(getIndex());
                                ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to delete?");
                                // if user confirm delete then delete
                                if (resultConfirm.equals(ButtonType.OK)) {
                                    deletePhoneFromDatbase(phone.getId());
                                }
                            });
                            setGraphic(deleteButton);
                            setText(null);
                        }
                    }
                };
                return deleteCell;
            };
            deleteTableColumn.setCellFactory(columnTableCellCallback);
            FilteredList<Phone> filteredList = new FilteredList<>(phoneObservableList, b -> true); // b->true : means all elements in the list will be included in the filteredList

            // listen to changes in the searchKeyword to update the tableView
            searchKeywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(phone -> {
                    if (newValue == null || newValue.trim().isEmpty()) {
                        totalLabel.setText("Total: " + phoneObservableList.size());
                        return true;
                    }


                    String searchKeyword = newValue.toLowerCase();
                    String searchBy = searchComboBox.getValue().toLowerCase();

                    if (searchBy.equals("name") && phone.getPhoneName().toLowerCase().contains(searchKeyword)) {
                        return true;
                    } else {
                        totalLabel.setText("Total: " + filteredList.size());
                        return false;
                    }
                });
                // update pagination
                updatePagination(filteredList);

            });
            // update pagination
            updatePagination(filteredList);
            updateButton.setDisable(true);
        }

        private void updateTableData(int pageIndex) {
            int fromIndex = pageIndex * itemsPerPage;
            int toIndex = Math.min(fromIndex + itemsPerPage, phoneObservableList.size());

            // Clear the table and re-add the events for the current page
            phoneTableView.getItems().clear();
            phoneTableView.getItems().addAll(phoneObservableList.subList(fromIndex, toIndex));
        }
        private void setupPagination() {
            int totalPages = (phoneObservableList.size() / itemsPerPage) + (phoneObservableList.size() % itemsPerPage > 0 ? 1 : 0);
            paginationPagination.setPageCount(totalPages);

            paginationPagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
                updateTableData(newValue.intValue());
            });
        }

        private void updatePagination(FilteredList<Phone> filteredList) {
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

            SortedList<Phone> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(phoneTableView.comparatorProperty());

            phoneTableView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
        }




        private int setIdAdd() {
            String sql = "SELECT phoneId FROM phone ORDER BY phoneId DESC LIMIT 1";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    int idIncrease = resultSet.getInt("id") + 1;
                    idTextField.setText(String.valueOf(idIncrease));
                    return idIncrease;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }

        /**
         * Retrieves the distributor data from the database and stores it in a HashMap with distributorId as the key.
         * It queries the database to fetch the distributorId and distributorName and puts them in the HashMap.
         *
         * @return The HashMap containing distributorId as the key and distributorName as the value.
         */
        private Map<Integer, String> getDistributorMap() {
            // create an empty Map to store distributor data
            Map<Integer, String> distributorMap = new HashMap<>();

            String sql = "SELECT distributorId, distributorName FROM distributor";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    // iterate through the resultSet from db and put into map
                    int distributorId = resultSet.getInt("distributorId");
                    String distributorName = resultSet.getString("distributorName");

                    distributorMap.put(distributorId, distributorName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return distributorMap;
        }


        private void addDistributorNameToComboBox() {
            Map<Integer, String> distributorMap = getDistributorMap();
            distributorComboBox.getItems().addAll(distributorMap.values());
        }

        private void addSearchByToComboBox() {
            searchComboBox.getItems().addAll("name");
            searchComboBox.setValue("name");
        }

        @FXML
        void chooseFileImage() {
            FileChooser fileChooser = new FileChooser();
            // show a file open dialog box to user select image when click on chooseImageButton
            File file = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());

            // if file is not null then get path of this file
            if (file != null) {
                String destinationFilePath = "src/main/resources/com/example/smartphone/image_phone/" + file.getName(); // Replace this with the actual destination folder path

                GetData.path = destinationFilePath;

                // Copy the selected image file to the new folder
                try {
                    Files.copy(file.toPath(), Paths.get(destinationFilePath), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Image image = new Image("file:" + destinationFilePath);
                phoneImageView.setImage(image);
            }
        }

        /**
         * Retrieves the distributorId from the distributorMap based on the selected distributor name.
         * It searches the HashMap to find the key (distributorId) corresponding to the selected distributor name.
         *
         * @param map   The HashMap containing distributorId as the key and distributorName as the value.
         * @param value The selected distributor name from the ComboBox.
         * @return The distributorId corresponding to the selected distributor name, or -1 if not found.
         */
        private int getKeyFromValue(Map<Integer, String> map, String value) {
            // iterate over the entries of the map object
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                // check if entry of the map equal with the value param of ComboBox then return the key
                if (entry.getValue().equals(value)) {
                    return entry.getKey();
                }
            }
            return -1;
        }

        /**
         * Resets the form by clearing the input fields and resetting the action status.
         * It sets the form to add mode and enables the add button while disabling the update button.
         */
        public void resetForm() {
            searchKeywordTextField.clear();
            setIdAdd(); // set new value for id field
            priceTextField.clear();
            phoneTextField.clear();
            sellingPriceTextField.clear();
            quantityTextField.clear();
            distributorComboBox.setValue(null);
            phoneImageView.setImage(null);
            GetData.path = "";

            actionStatusLabel.setText("Adding New Phone");
            addButton.setDisable(false);
            updateButton.setDisable(true);
        }

        private void selectedRecord() {
            phoneTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Phone>() {
                @Override
                public void changed(ObservableValue<? extends Phone> observableValue, Phone oldValue, Phone newValue) {
                    if (newValue != null) {
                        idTextField.setText(String.valueOf(newValue.getId()));
                        priceTextField.setText(String.valueOf(newValue.getPrice()));
                        distributorComboBox.setValue(String.valueOf(newValue.getDistributor()));
                        quantityTextField.setText(String.valueOf(newValue.getQuantity()));
                        sellingPriceTextField.setText(String.valueOf(newValue.getSellingPrice()));
                        phoneTextField.setText(String.valueOf(newValue.getPhoneName()));
                        File imageFile = new File(newValue.getImage());
                        Image image = null;
                        try {
                            image = new Image(imageFile.getAbsolutePath());
                            phoneImageView.setImage(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                            image = null;
                            phoneImageView.setImage(null);
                            GetData.showWarningAlert("\"Warning message", "Image not found. \\nname sure that image file exists and try again!");

                        }

                        addButton.setDisable(true);
                        updateButton.setDisable(false);
                        actionStatusLabel.setText("Updating Phone");
                    } else {
                        resetForm();
                    }
                }
            });
        }

        private boolean isFilledFields() {
            if (
                    priceTextField.getText().isEmpty()
                            || quantityTextField.getText().isEmpty()
    //                || GetData.path == null
    //                || GetData.path.equals("")
                            || distributorComboBox.getItems().isEmpty() ||sellingPriceTextField.getText().isEmpty()) {
                // alert error if required fields no filled
                GetData.showWarningAlert("Warning message", "Please fill all required fields!");
                return false;
            } else {
                return true;
            }
        }

        private boolean addQuantityToDatabase() {
            String sql = "INSERT INTO phone_inventory(id,quantityInStock) VALUES (?,?);";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, idTextField.getText());
                preparedStatement.setString(2, quantityTextField.getText());

                preparedStatement.executeUpdate();
                return true;


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }
        public void addPhoneToDatabase() {
            String sql = "INSERT INTO phone(name, price, distributorId, image, id) VALUES (?,?,?,?,?);";

            try {
                // check required fields filled or not
                if (isFilledFields() && validateFields()) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, phoneTextField.getText());
                    preparedStatement.setString(2, priceTextField.getText());

                    Map<Integer, String> distributorMap = getDistributorMap();
                    String selectedDistributorValue = distributorComboBox.getValue(); // get the selected distributorName in ComboBox
                    int selectedDistributorKey = getKeyFromValue(distributorMap, selectedDistributorValue); // get selected distributorId

                    preparedStatement.setString(3, String.valueOf(selectedDistributorKey));

                    preparedStatement.setString(4, GetData.path);
                    preparedStatement.setString(5, idTextField.getText());

                    // execute
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0 && addQuantityToDatabase()) {
                        // alert success if add data successfully
                        GetData.showSuccessAlert("Success message", "Added successfully!");

                        // update the table and reset the form
                        setupTable();
                        resetForm();
                    } else {
                        // alert error if adding data failed
                        GetData.showErrorAlert("Error message", "Failed to add phone record.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // alert error if an exception occurs
                GetData.showErrorAlert("Error message", "An error occurred while adding phone record.");
            }
        }


        public void updatePhoneToDatabase() {
            Map<Integer, String> distributorMap = getDistributorMap();
            String selectedDistributorValue = distributorComboBox.getValue();
            int selectedDistributorKey = getKeyFromValue(distributorMap, selectedDistributorValue);

            String sql = "UPDATE phone " +
                    "SET phoneName = ?, price = ?, distributorId = ?, image = ?,sellingPrice = ? " +
                    "WHERE id = ?";

            try {
                if (isFilledFields() && validateFields()) {
                    ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to update?");
                    // if user confirm delete then delete
                    if (resultConfirm.equals(ButtonType.OK)) {
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, phoneTextField.getText());
                        preparedStatement.setString(2, priceTextField.getText());
                        preparedStatement.setString(3, String.valueOf(selectedDistributorKey));
                        preparedStatement.setString(4, GetData.path);
                        preparedStatement.setString(5, sellingPriceTextField.getText());
                        preparedStatement.setString(6, idTextField.getText());

                        int rowAffected = preparedStatement.executeUpdate();
                        // if updated success then alert
                        if (rowAffected > 0) {
                            GetData.showSuccessAlert("Success message", "Updated successfully!");

                            setupTable();
                            resetForm();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private boolean validateFields() {
            if (!validateSellingPrice() || !validateQuantity() || !validateOriginalPrice()) {
                return false;
            }
            return true;
        }
        private boolean validateQuantity() {
            String quantity = quantityTextField.getText().trim();
            if (!Pattern.compile("\\d*").matcher(quantity).matches()) {
                GetData.showWarningAlert("Validation Error", "Quantity must be a number.");
                return false;
            }
            return true;
        }


        private boolean validateOriginalPrice() {
            double price = Double.parseDouble(priceTextField.getText());
            if (price < 0) {
                GetData.showWarningAlert("Validation Error", "Original price must be a number and greater than 0.");
                return false;
            } else {
                return true;
            }
        }

        private boolean validateSellingPrice() {
            double price = Double.parseDouble(sellingPriceTextField.getText());
            if (price < 0) {
                GetData.showWarningAlert("Validation Error", "Selling price must be a number and greater than 0.");
                return false;
            } else {
                return true;
            }
        }



        private void deletePhoneFromDatbase(int id) {
                String updateInventoryQuery = "UPDATE phone_inventory SET inventoryId = NULL WHERE inventoryId =" + id;
            String updateOrderQuery = "UPDATE `order` SET orderId = NULL WHERE orderId = "+ id;

            try {
                Statement updateInventoryStatement = connection.createStatement();
                int rowUpdateInventoryAffected = updateInventoryStatement.executeUpdate(updateInventoryQuery);

                Statement updateOrderStatement = connection.createStatement();
                int rowUpdateOrderAffected = updateOrderStatement.executeUpdate(updateOrderQuery);

                if(rowUpdateInventoryAffected > 0 && rowUpdateOrderAffected>0){
                    String sql = "DELETE FROM phone WHERE phoneId = " + id;

                    Statement statement = connection.createStatement();
                    int rowAffected = statement.executeUpdate(sql);
                    if (rowAffected > 0) {
                        GetData.showSuccessAlert("Success message", "Deleted successfully!");

                        setupTable();
                        resetForm();
                    }

                }
            }catch (Exception e){
                GetData.showErrorAlert("Error message", "Cannot delete!");
                e.printStackTrace();
            }
        }
    }