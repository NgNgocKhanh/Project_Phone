package com.example.smartphone.model;

import lombok.*;

@Data
@ToString
@Getter
@Setter

public class Phone {
    private int id;
    private String phoneName;
    private double price;
    private String distributor;
    private String image;
    private int quantity;
    private double sellingPrice;

    public Phone(int id, String phoneName, double price, String distributor, String image, double sellingPrice) {
        this.id = id;
        this.phoneName = phoneName;
        this.price = price;
        this.distributor = distributor;
        this.image = image;
        this.sellingPrice = sellingPrice;
    }

    public Phone(int id, String phoneName, double price, String distributor, String image, int quantity, double sellingPrice) {
        this.id = id;
        this.phoneName = phoneName;
        this.price = price;
        this.distributor = distributor;
        this.image = image;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setName(String phoneName) {
        this.phoneName = phoneName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}