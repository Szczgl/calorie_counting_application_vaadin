package com.calories.front.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Long id;
    private String name;
    private String description;
    private double consumedCalories;
    private Long userId;
    private String source;
}
