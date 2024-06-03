package com.calories.front.api;

import com.calories.front.dto.IngredientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class IngredientApiClient {

    @Autowired
    private WebClient webClient;

    public List<IngredientDTO> getAllIngredients() {
        return webClient.get()
                .uri("/v1/ingredients")
                .retrieve()
                .toEntityList(IngredientDTO.class)
                .block()
                .getBody();
    }

    public IngredientDTO getIngredientById(Long id) {
        return webClient.get()
                .uri("/v1/ingredients/{id}", id)
                .retrieve()
                .toEntity(IngredientDTO.class)
                .block()
                .getBody();
    }

    public IngredientDTO createIngredient(IngredientDTO ingredientDTO) {
        return webClient.post()
                .uri("/v1/ingredients")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ingredientDTO)
                .retrieve()
                .toEntity(IngredientDTO.class)
                .block()
                .getBody();
    }

    public IngredientDTO updateIngredient(IngredientDTO ingredientDTO, Long id) {
        return webClient.put()
                .uri("/v1/ingredients/{id}", id)
                .bodyValue(ingredientDTO)
                .retrieve()
                .toEntity(IngredientDTO.class)
                .block()
                .getBody();
    }

    public void deleteIngredient(Long id) {
        webClient.delete()
                .uri("/v1/ingredients/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}