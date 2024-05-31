package com.calories.front.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
    private Long id;
    private String name;
    private String description;
    private double totalCalories;
    private Long userId;
    private Set<Long> ingredientIds;

    @JsonIgnore
    public String getIngredientsName() {
        return ingredientIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

    }
}