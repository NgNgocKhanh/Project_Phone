package com.example.smartphone.model;
import lombok.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;

import java.time.LocalDateTime;

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

    public Order(int orderId, int customerId, String customerName, String orderDate, double totalAmount, int statusId, String orderStatus, int employeeId, String employeeName, int phoneId,  double phonePrice, int orderQuantity, int paymentId, String paymentType, int paymentStatusId, String paymentStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.statusId = statusId;
        this.orderStatus = orderStatus;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.phonePrice = phonePrice;
        this.orderQuantity = orderQuantity;
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.paymentStatusId = paymentStatusId;
        this.paymentStatus = paymentStatus;
    }

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public Order(int orderId, LocalDateTime dateOrder, String productName, int statusId, Double valueOf) {

    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public double getPhonePrice() {
        return phonePrice;
    }

    public void setPhonePrice(double phonePrice) {
        this.phonePrice = phonePrice;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(int paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public CheckBox getSelectCheckBox() {
        return selectCheckBox;
    }

    public void setSelectCheckBox(CheckBox selectCheckBox) {
        this.selectCheckBox = selectCheckBox;
    }
}

