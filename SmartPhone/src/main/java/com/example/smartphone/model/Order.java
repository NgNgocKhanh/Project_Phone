package com.example.smartphone.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Order {
    private int orderId;
    private int customerId;
    private String customerName;
    private String orderDate;
    private double totalAmount;
    private int statusId;
    private String orderStatus;
    private int employeeId;
    private String employeeName;
    private int phoneId;
    private double phonePrice;
    private int orderQuantity;
    private int paymentId;
    private String paymentType;
    private int paymentStatusId;
    private String paymentStatus;
    private CheckBox selectCheckBox;


    public Order(int orderId, String orderDate, double totalAmount, String orderStatus) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
    }

    public Order(int orderId, int customerId, String customerName, String orderDate, double totalAmount, int statusId, String orderStatus, int employeeId, String employeeName, double price, int orderQuantity, int paymentId, String paymentType, int paymentStatusId, String paymentStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.statusId = statusId;
        this.orderStatus = orderStatus;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.phoneId = phoneId;
        this.phonePrice = phonePrice;
        this.orderQuantity = orderQuantity;
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.paymentStatusId = paymentStatusId;
        this.paymentStatus = paymentStatus;
    }

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
