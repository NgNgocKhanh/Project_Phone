package com.example.smartphone.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MyPageManager {

    private final int itemsPerPage;
    private ObservableList<String> items; // Danh sách các mục để hiển thị
    private int totalItems;

    public MyPageManager(int itemsPerPage, ObservableList<String> items) {
        this.itemsPerPage = itemsPerPage;
        this.items = items;
        this.totalItems = items.size();
    }

    public VBox createPage(int pageIndex) {
        VBox pageBox = new VBox(10);

        // Tính toán phạm vi của dữ liệu cho trang hiện tại
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        // Kiểm tra nếu đang ở một trong 4 trang đầu tiên
        if (pageIndex < 4) {
            // Hiển thị dữ liệu cho trang hiện tại
            for (int i = startIndex; i < endIndex; i++) {
                Label label = new Label(items.get(i));
                pageBox.getChildren().add(label);
            }
        }

        return pageBox;
    }

    public int getTotalPages() {
        // Tính toán số trang cần hiển thị
        return Math.min((totalItems + itemsPerPage - 1) / itemsPerPage, 4);
    }
}
