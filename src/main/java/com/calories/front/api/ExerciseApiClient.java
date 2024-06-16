package com.calories.front.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExerciseApiClient {

    @Autowired
    private WebClient webClient;

    public String getExerciseByName(String name) {
        return webClient.get()
                .uri("/api/exercises/name/" + name)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getDescriptionByName(String name) {
        String exerciseData = getExerciseByName(name);
        StringBuilder description = new StringBuilder();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(exerciseData);
            if (root.isArray() && root.size() > 0) {
                JsonNode exercise = root.get(0);
                exercise.path("instructions").forEach(instruction -> description.append(instruction.asText()).append(" "));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return description.toString().trim();
    }
}