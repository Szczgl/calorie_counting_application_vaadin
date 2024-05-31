package com.calories.front.api;

import com.calories.front.dto.ActivityDTO;
import com.calories.front.dto.RecipeDTO;
import com.calories.front.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UserApiClient {

    @Autowired
    private WebClient webClient;

    public List<UserDTO> getAllUsers() {
        return webClient.get()
                .uri("/v1/users")
                .retrieve()
                .toEntityList(UserDTO.class)
                .block()
                .getBody();
    }

    public UserDTO getUserById(Long id) {
        return webClient.get()
                .uri("/v1/users/{id}", id)
                .retrieve()
                .toEntity(UserDTO.class)
                .block()
                .getBody();
    }

    public void createUser(UserDTO userDTO) {
        webClient.post()
                .uri("/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public UserDTO updateUser(UserDTO userDTO, Long id) {
        return webClient.put()
                .uri("/v1/users/{id}", id)
                .bodyValue(userDTO)
                .retrieve()
                .toEntity(UserDTO.class)
                .block()
                .getBody();
    }

    public void deleteUser(Long id) {
        webClient.delete()
                .uri("/v1/users/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void addRecipeToUser(Long userId, RecipeDTO recipeDTO) {
        webClient.post()
                .uri("/v1/users/{userId}/recipes", userId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(recipeDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void addActivityToUser(Long userId, ActivityDTO activityDTO) {
        webClient.post()
                .uri("/v1/users/{userId}/activities", userId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(activityDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}