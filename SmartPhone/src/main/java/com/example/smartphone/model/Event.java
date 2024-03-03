package com.example.smartphone.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@ToString
public class Event {
    private int id;
    private String name;
    private double discount;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String address;
}
