package com.example.smartphone.model;

import javafx.scene.layout.VBox;

public class MyPageManager {

    private final int itemsPerPage;

    public MyPageManager(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public VBox createPage(int pageIndex) {
        VBox pageBox = new VBox(10);
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, getTotalItems()); // getTotalItems() là phương thức ở lớp con

        for (int i = startIndex; i < endIndex; i++) {
            // Tạo nội dung cho từng trang
            // Ví dụ: Label label = new Label("Item " + (i + 1));
            // pageBox.getChildren().add(label);
        }

        return pageBox;
    }

    // Phương thức này cần được triển khai trong lớp con để cung cấp số lượng tổng cộng các mục
    protected int getTotalItems() {
        // Logic để tính toán tổng số mục
        return 100;
    }
}
