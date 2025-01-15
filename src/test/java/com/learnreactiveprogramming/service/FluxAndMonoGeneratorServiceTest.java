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

    @Test
    void namesMono_flatMapMany(){
        int strLen = 3;

        var value = fluxAndMonoGeneratorService.namesMono_flatMapMany(strLen);

        StepVerifier.create(value)
                .expectNext("A","L","E","X")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        StepVerifier
                .create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_1() {
        int stringLength = 6;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        StepVerifier
                .create(namesFlux)
//                .expectNext("A","L","E","X","C","H","L","O","E")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchIfEmpty() {
        int stringLength = 6;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLength);

        StepVerifier
                .create(namesFlux)
//                .expectNext("A","L","E","X","C","H","L","O","E")
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }


    @Test
    void namesMono_defaultIfEmpty(){

        int stringLength = 4;

        var namesMono = fluxAndMonoGeneratorService.namesMono_defaultIfEmpty(stringLength);

        StepVerifier
                .create(namesMono)
//                .expectNext("ALEX")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesMono_switchIfEmpty(){

        int stringLength = 4;

        var namesMono = fluxAndMonoGeneratorService.namesMono_switchIfEmpty(stringLength);

        StepVerifier
                .create(namesMono)
//                .expectNext("ALEX")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void explore_concatwith() {
        var concatFlux = fluxAndMonoGeneratorService.explore_concatwith();

        StepVerifier
                .create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_concatwith_mono() {
        var concatFlux = fluxAndMonoGeneratorService.explore_concatwith_mono();

        StepVerifier
                .create(concatFlux)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        var value = fluxAndMonoGeneratorService.explore_merge();

        StepVerifier
                .create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith() {
        var value = fluxAndMonoGeneratorService.explore_mergeWith();

        StepVerifier
                .create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith_mono() {
        var value = fluxAndMonoGeneratorService.explore_mergeWith_mono();

        StepVerifier
                .create(value)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        var value = fluxAndMonoGeneratorService.explore_mergeSequential();

        StepVerifier
                .create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        var value = fluxAndMonoGeneratorService.explore_zip();

        StepVerifier
                .create(value)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        var value = fluxAndMonoGeneratorService.explore_zip_1();

        StepVerifier
                .create(value)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }

    @Test
    void explore_zipWith() {
        var value = fluxAndMonoGeneratorService.explore_zipWith();

        StepVerifier
                .create(value)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zipWith_mono() {
        var value = fluxAndMonoGeneratorService.explore_zipWith_mono();

        StepVerifier
                .create(value)
                .expectNext("AB")
                .verifyComplete();
    }
}
