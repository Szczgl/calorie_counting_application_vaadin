package com.calories.front.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private double dailyCalorieIntake;
    private double dailyCalorieConsumption;
}