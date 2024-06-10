package com.calories.front.api;

import com.calories.front.dto.RecipeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class RecipeApiClient {

    @Autowired
    private WebClient webClient;

    public List<RecipeDTO> getAllRecipes() {
        return webClient.get()
                .uri("/v1/recipes")
                .retrieve()
                .toEntityList(RecipeDTO.class)
                .block()
                .getBody();
    }

    public RecipeDTO getRecipeById(Long id) {
        return webClient.get()
                .uri("/v1/recipes/{id}", id)
                .retrieve()
                .toEntity(RecipeDTO.class)
                .block()
                .getBody();
    }

    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        return webClient.post()
                .uri("/v1/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(recipeDTO)
                .retrieve()
                .toEntity(RecipeDTO.class)
                .block()
                .getBody();
    }

    public RecipeDTO updateRecipe(RecipeDTO recipeDTO, Long id) {
        return webClient.put()
                .uri("/v1/recipes/{id}", id)
                .bodyValue(recipeDTO)
                .retrieve()
                .toEntity(RecipeDTO.class)
                .block()
                .getBody();
    }

    public void deleteRecipe(Long id) {
        webClient.delete()
                .uri("/v1/recipes/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public boolean existsByIngredients_Id(Long ingredientId) {
        return webClient.get()
                .uri("/v1/recipes/existsByIngredients_Id/{ingredientId}", ingredientId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}