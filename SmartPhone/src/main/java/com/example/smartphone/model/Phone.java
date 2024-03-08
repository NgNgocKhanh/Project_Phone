package com.example.smartphone.model;
import lombok.*;

@Data
@Getter
@Setter
@ToString

public class Phone {
    private int phone_id;
    private String phoneName;
    private String img;
    private double price;
    private double sellingPrice;
    private int quantity;
    private String distributor;
    public Phone(int id, String phoneName, String image, double price, double sellingPrice, String distributorComboboxx) {
    }

    public Phone(int phone_id, String phoneName, String img, double price, double sellingPrice, int quantity, String distributor) {
        this.phone_id = phone_id;
        this.phoneName = phoneName;
        this.img = img;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.distributor = distributor;
    }

    public int getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(int phone_id) {
        this.phone_id = phone_id;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "phone_id=" + phone_id +
                ", phoneName='" + phoneName + '\'' +
                ", img='" + img + '\'' +
                ", price=" + price +
                ", sellingPrice=" + sellingPrice +
                ", quantity=" + quantity +
                ", distributor='" + distributor + '\'' +
                '}';
    }
}
