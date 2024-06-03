package com.calories.front.dto;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
    private Long id;
    private String name;
    private String description;
    private double totalCalories;
    private Long userId;
    private List<IngredientDTO> ingredients;
}