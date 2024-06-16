package com.calories.front.api;

import com.calories.front.dto.ReportDTO;
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

    public UserDTO createUser(UserDTO userDTO) {
        return webClient.post()
                .uri("/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .toEntity(UserDTO.class)
                .block()
                .getBody();
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

    public ReportDTO generateReport(Long userId) {
        return this.webClient.get()
                .uri("/v1/users/{id}/daily-report", userId)
                .retrieve()
                .bodyToMono(ReportDTO.class)
                .block();
    }
}
