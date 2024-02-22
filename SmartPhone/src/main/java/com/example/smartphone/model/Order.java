package com.example.smartphone.model;

import java.time.LocalDateTime;

public class Order {
    private int order_id;
    private LocalDateTime date_order;
    private String productName;
    private int status_id;
    private  Double price;

    public Order() {
    }

    public Order(int order_id, LocalDateTime date_order, String productName, int status_id, Double price) {
        this.order_id = order_id;
        this.date_order = date_order;
        this.productName = productName;
        this.status_id = status_id;
        this.price = price;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public LocalDateTime getDate_order() {
        return date_order;
    }

    public void setDate_order(LocalDateTime date_order) {
        this.date_order = date_order;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

