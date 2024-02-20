package com.example.smartphone.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Order {
    private IntegerProperty order_id;
    private IntegerProperty user_id;
    private IntegerProperty status_id;
    private ObjectProperty<LocalDateTime> date_order;
    private StringProperty productName;
    private DoubleProperty price;

    public Order(int i, int i1, LocalDateTime now, String s, int i2, double v) {
        this.order_id = new SimpleIntegerProperty();
        this.user_id = new SimpleIntegerProperty();
        this.status_id = new SimpleIntegerProperty();
        this.date_order = new SimpleObjectProperty<>();
        this.productName = new SimpleStringProperty();
        this.price = new SimpleDoubleProperty();
    }

    public IntegerProperty order_idProperty() {
        return order_id;
    }

    public int getOrder_id() {
        return order_id.get();
    }

    public void setOrder_id(int order_id) {
        this.order_id.set(order_id);
    }

    public int getUser_id() {
        return user_id.get();
    }

    public IntegerProperty user_idProperty() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id.set(user_id);
    }

    public int getStatus_id() {
        return status_id.get();
    }

    public IntegerProperty status_idProperty() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id.set(status_id);
    }

    public LocalDateTime getDate_order() {
        return date_order.get();
    }

    public ObjectProperty<LocalDateTime> date_orderProperty() {
        return date_order;
    }

    public void setDate_order(LocalDateTime date_order) {
        this.date_order.set(date_order);
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }
    // Các phương thức getter và setter khác

    public Order(IntegerProperty order_id, IntegerProperty user_id, IntegerProperty status_id, ObjectProperty<LocalDateTime> date_order, StringProperty productName, DoubleProperty price) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.status_id = status_id;
        this.date_order = date_order;
        this.productName = productName;
        this.price = price;
    }
}
