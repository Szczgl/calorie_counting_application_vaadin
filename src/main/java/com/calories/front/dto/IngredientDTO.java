package com.calories.front.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private Long id;
    private String name;
    private double quantity;
    private double calories;
}
