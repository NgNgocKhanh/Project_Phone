package com.example.smartphone.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Order {
    private final IntegerProperty order_id;
    private final ObjectProperty<LocalDateTime> date_order;
    private final StringProperty productName;
    private final IntegerProperty status_id;
    private final DoubleProperty price;

    public Order(int order_id, LocalDateTime date_order, String productName, double price, int status_id) {
        this.order_id = new SimpleIntegerProperty(order_id);
        this.date_order = new SimpleObjectProperty<>(date_order);
        this.productName = new SimpleStringProperty(productName);
        this.status_id = new SimpleIntegerProperty(status_id);
        this.price = new SimpleDoubleProperty(price);
    }

    public int getOrder_id() {
        return order_id.get();
    }

    public IntegerProperty order_idProperty() {
        return order_id;
    }

    public LocalDateTime getDate_order() {
        return date_order.get();
    }

    public ObjectProperty<LocalDateTime> date_orderProperty() {
        return date_order;
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public int getStatus_id() {
        return status_id.get();
    }

    public IntegerProperty status_idProperty() {
        return status_id;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setOrder_id(int order_id) {
        this.order_id.set(order_id);
    }

    public void setDate_order(LocalDateTime date_order) {
        this.date_order.set(date_order);
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public void setStatus_id(int status_id) {
        this.status_id.set(status_id);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }
}
