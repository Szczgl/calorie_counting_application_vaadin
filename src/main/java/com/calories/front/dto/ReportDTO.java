package com.calories.front.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String username;
    private LocalDate reportDate;
    private double dailyCalorieIntake;
    private double dailyCalorieConsumption;
    private double calorieBalance;
    private List<String> recipesName;
}
