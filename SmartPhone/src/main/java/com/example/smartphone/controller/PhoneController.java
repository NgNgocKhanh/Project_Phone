package com.example.smartphone.controller;

import com.example.smartphone.model.MyPageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class PhoneController {
    @FXML
    private Pagination pagination;
    private MyPageManager pageManager;

    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    @FXML
    private Pane currentPane;

    @FXML
    private Pane newPane;

    public void initialize() {
        ObservableList<String> items = getData(); // Lấy dữ liệu từ nguồn dữ liệu của bạn
        pageManager = new MyPageManager(8, items);
        pagination.setPageFactory(pageManager::createPage);

        // In trang đầu tiên
        pagination.setCurrentPageIndex(0);
    }

    private ObservableList<String> getData() {
        // Logic để lấy danh sách dữ liệu từ nguồn dữ liệu của bạn
        // Ví dụ:
        return FXCollections.observableArrayList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10", "Item 11", "Item 12", "Item 13", "Item 14", "Item 15");
    }

    // Xử lý sự kiện khi người dùng nhấp vào nút Next
    @FXML
    private void handleNextButton() {
        int currentPageIndex = pagination.getCurrentPageIndex();
        int totalPages = pageManager.getTotalPages();
        if (currentPageIndex < totalPages - 1) {
            pagination.setCurrentPageIndex(currentPageIndex + 1);
        }

        // Thay đổi pane hiện tại sang newPane
        currentPane.getChildren().clear();
        currentPane.getChildren().add(newPane);
    }

    // Xử lý sự kiện khi người dùng nhấp vào nút Back
    @FXML
    private void handleBackButton() {
        int currentPageIndex = pagination.getCurrentPageIndex();
        if (currentPageIndex > 0) {
            pagination.setCurrentPageIndex(currentPageIndex - 1);
        }

        // Thay đổi pane hiện tại sang newPane
        currentPane.getChildren().clear();
        currentPane.getChildren().add(newPane);
    }
}
