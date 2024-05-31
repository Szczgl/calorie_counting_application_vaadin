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

    public void createRecipe(RecipeDTO recipeDTO) {
        webClient.post()
                .uri("/v1/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(recipeDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
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
}