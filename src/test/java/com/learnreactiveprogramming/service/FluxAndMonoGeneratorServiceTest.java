package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;
import reactor.tools.agent.ReactorDebugAgent;

import java.time.Duration;
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
    void namesFlux_concatMap_virtualTimer() {
        VirtualTimeScheduler.getOrSet();
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(stringLength);

        StepVerifier
                .withVirtualTime(()->namesFlux)
                .thenAwait(Duration.ofSeconds(10))
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

    @Test
    void exception_flux() {
        var value = fluxAndMonoGeneratorService.exception_flux();

        StepVerifier.create(value).expectNext("A","B","C")
                .expectError(RuntimeException.class).verify();
    }

    @Test
    void exception_flux1() {
        var value = fluxAndMonoGeneratorService.exception_flux();

        StepVerifier.create(value).expectNext("A","B","C")
                .expectError().verify();
    }

    @Test
    void exception_flux2() {
        var value = fluxAndMonoGeneratorService.exception_flux();

        StepVerifier.create(value).expectNext("A","B","C")
                .expectErrorMessage("Exception occured").verify();
    }

    @Test
    void explore_OnErrorReturn() {

        var value = fluxAndMonoGeneratorService.explore_OnErrorReturn();

        StepVerifier.create(value).expectNext("A","B","C","D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume() {
        var e = new IllegalStateException("Not a valid State");
        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume_1() {
        var e = new RuntimeException("Not a valid State");
        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void explore_OnErrorContinue() {
        var value = fluxAndMonoGeneratorService.explore_OnErrorContinue();

        StepVerifier.create(value).expectNext("A","C","D").verifyComplete();
    }

    @Test
    void explore_OnErrorMap() {
        var e = new RuntimeException("Not a valid State");

        var value = fluxAndMonoGeneratorService.explore_OnErrorMap(e);

        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_OnErrorMap_onOperatorDebug() {

//        Hooks.onOperatorDebug();
        var e = new RuntimeException("Not a valid State");

        var value = fluxAndMonoGeneratorService.explore_OnErrorMap(e);

        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_OnErrorMap_reactorDebugAgent() {

        ReactorDebugAgent.init();
        ReactorDebugAgent.processExistingClasses();
        var e = new RuntimeException("Not a valid State");

        var value = fluxAndMonoGeneratorService.explore_OnErrorMap(e);

        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_doOnError() {
        var value = fluxAndMonoGeneratorService.explore_doOnError();

        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void explore_Mono_onErrorReturn() {

        var value = fluxAndMonoGeneratorService.explore_Mono_onErrorReturn();

        StepVerifier.create(value).expectNext("abc").verifyComplete();
    }

    @Test
    void exception_Mono_onErrorContinue() {
        var value = fluxAndMonoGeneratorService.exception_Mono_onErrorContinue("abc");

        StepVerifier.create(value).verifyComplete();
    }

    @Test
    void exception_Mono_onErrorContinue_correct() {
        var value = fluxAndMonoGeneratorService.exception_Mono_onErrorContinue("reactor");

        StepVerifier.create(value).expectNext("reactor").verifyComplete();
    }

    @Test
    void explore_generate() {
        var flux = fluxAndMonoGeneratorService.explore_generate().log();

        StepVerifier.create(flux).expectNextCount(10).verifyComplete();
    }

    @Test
    void explore_create() {
        var flux = fluxAndMonoGeneratorService.explore_create().log();

        StepVerifier.create(flux).expectNextCount(9).verifyComplete();
    }

    @Test
    void explore_create_mono() {
        var mono = fluxAndMonoGeneratorService.explore_create_mono();

        StepVerifier.create(mono).expectNext("alex").verifyComplete();
    }

    @Test
    void explore_handle() {
        var flux = fluxAndMonoGeneratorService.explore_handle().log();

        StepVerifier.create(flux).expectNextCount(2).verifyComplete();
    }
}
