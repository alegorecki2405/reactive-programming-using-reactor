package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/reviews")
            .build();

    ReviewService reviewService = new ReviewService(webClient);

    @Test
    void retrieveReviewsFlux_RestClient() {

    }
}
