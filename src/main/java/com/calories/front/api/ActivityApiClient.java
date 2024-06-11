package com.calories.front.api;

import com.calories.front.dto.ActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
public class ActivityApiClient {

    @Autowired
    private WebClient webClient;

    public List<ActivityDTO> getAllActivities() {
        return webClient.get()
                .uri("/v1/activities")
                .retrieve()
                .toEntityList(ActivityDTO.class)
                .block()
                .getBody();
    }

    public ActivityDTO getActivityById(Long id) {
        return webClient.get()
                .uri("/v1/activities/{id}", id)
                .retrieve()
                .toEntity(ActivityDTO.class)
                .block()
                .getBody();
    }

    public ActivityDTO createActivity(ActivityDTO activityDTO) {
        return webClient.post()
                .uri("/v1/activities")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(activityDTO)
                .retrieve()
                .toEntity(ActivityDTO.class)
                .block()
                .getBody();
    }

    public ActivityDTO updateActivity(ActivityDTO activityDTO, Long id) {
        return webClient.put()
                .uri("/v1/activities/{id}", id)
                .bodyValue(activityDTO)
                .retrieve()
                .toEntity(ActivityDTO.class)
                .block()
                .getBody();
    }

    public void deleteActivity(Long id) {
        webClient.delete()
                .uri("/v1/activities/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}