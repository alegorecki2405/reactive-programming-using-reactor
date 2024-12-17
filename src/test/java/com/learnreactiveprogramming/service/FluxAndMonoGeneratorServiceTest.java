package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
//                .expectNextCount(3)
//                .expectNext("alex", "ben", "chloe")]
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesMono() {
        var namesMono = fluxAndMonoGeneratorService.namesMono();

        StepVerifier.create(namesMono)
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {

        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        StepVerifier
                .create(namesFlux)
                .expectNext("4-ALEX","5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_immutability() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_imumtability();

        StepVerifier
                .create(namesFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesMono_map_filter(){

        var namesMono = fluxAndMonoGeneratorService.namesMono_map_filter(3);

        StepVerifier
                .create(namesMono)
                .expectNext("ALEX")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap() {

        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);

        StepVerifier
                .create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap_async() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength);

        StepVerifier
                .create(namesFlux)
//                .expectNext("A","L","E","X","C","H","L","O","E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(stringLength);

        StepVerifier
                .create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap(){
        int strLen = 3;

        var value = fluxAndMonoGeneratorService.namesMono_flatMap(strLen);

        StepVerifier.create(value)
                .expectNext(List.of("A","L","E","X"))
                .verifyComplete();
    }
}
