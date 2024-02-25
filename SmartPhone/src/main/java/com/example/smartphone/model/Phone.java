package com.example.smartphone.model;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Phone {
    private int phone_id;
    private String phoneName;
    private String img;
    private String price;
    private double sellingPrice;


}
