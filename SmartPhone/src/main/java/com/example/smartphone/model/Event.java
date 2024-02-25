package com.example.smartphone.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Event {
    private int id;
    private String name;
    private double discount;
    private String start_date;
    private String start_time;

    private String end_date;
    private String end_time;
    private String address;


}
