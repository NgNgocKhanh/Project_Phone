package com.example.smartphone.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Distributor {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String birthday;

}
