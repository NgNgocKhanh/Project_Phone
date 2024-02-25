package com.example.smartphone.model;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Employee {
    private int id;
    private String name;
    private String username;
    private String role;
    private String phone;
    private String email;
    private double salary;
    private String joinDate;
    private String address;
    private String birthday;
}
