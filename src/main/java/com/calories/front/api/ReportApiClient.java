package com.calories.front.api;

import com.calories.front.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReportApiClient {

    @Autowired
    private WebClient webClient;

    public ReportDTO getDailyReport(Long userId) {
        return webClient.get()
                .uri("/v1/users/{id}/daily-report", userId)
                .retrieve()
                .toEntity(ReportDTO.class)
                .block()
                .getBody();
    }
}
