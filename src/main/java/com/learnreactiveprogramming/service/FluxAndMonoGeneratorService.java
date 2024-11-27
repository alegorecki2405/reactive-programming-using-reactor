package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Mono<String> namesMono() {
        return Mono.just("alex").log();
    }

    public Mono<String> namesMono_map_filter(int stringLength) {
        return Mono.just("alex")
                .filter(s->s.length()>stringLength)
                .map(String::toUpperCase)
                .log();
    }


    public Flux<String> namesFlux_map(int stringLength) {
        //filter the string whose length is greater than var
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s->s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s)
                .log();
    }

    public Flux<String> namesFlux_imumtability() {
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));

        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Flux<String> namesFlux_flatmap(int stringLength) {
        //filter the string whose length is greater than var
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s->s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                .flatMap(s->splitString(s))
                .log();
    }
    public Flux<String> namesFlux_flatmap_async(int stringLength) {
        //filter the string whose length is greater than var
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s->s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                .flatMap(s->splitString_withDelay(s))
                .log();
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitString_withDelay(String name) {
        var charArray = name.split("");
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux().subscribe(name -> {
            System.out.println("name is :" + name);
        });

        fluxAndMonoGeneratorService.namesMono().subscribe(name -> {
            System.out.println("mono name is :" + name);
        });
    }
}
