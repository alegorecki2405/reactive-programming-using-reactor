package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

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

    public Mono<String> namesMono_defaultIfEmpty(int stringLength) {
        return Mono.just("alex")
                .filter(s->s.length()>stringLength)
                .map(String::toUpperCase)
                .defaultIfEmpty("default")
                .log();
    }

    public Mono<String> namesMono_switchIfEmpty(int stringLength) {

        var defaultMono = Mono.just("default");

        return Mono.just("alex")
                .filter(s->s.length()>stringLength)
                .map(String::toUpperCase)
                .switchIfEmpty(defaultMono)
                .log();
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length()>stringLength)
                .flatMap(this::splitStringMono);
    }

    public Flux<String> namesMono_flatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length()>stringLength)
                .flatMapMany(this::splitString)
                .log();
    }


    public Flux<String> namesFlux_map(int stringLength) {
        //filter the string whose length is greater than var
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s->s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s)
                .doOnNext(name -> {
                    System.out.println("Name is : " + name);
                    name.toLowerCase();
                })
                .doOnSubscribe(s -> {
                    System.out.println("Subscription is : " + s);
                })
                .doOnComplete(()->{
                    System.out.println("inside the complete callback");
                })
                .doFinally(signalType -> {
                    System.out.println("inside do finally : " + signalType);
                })
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

    public Flux<String> namesFlux_concatMap(int stringLength) {
        //filter the string whose length is greater than var
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s->s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                .concatMap(s->splitString_withDelay(s))
                .log();
    }

    public Flux<String> namesFlux_transform(int stringLength) {

        Function<Flux<String>,Flux<String>> filtermap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

//        Flux.empty()
        //filter the string whose length is greater than var
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filtermap)
                .flatMap(s -> splitString(s))
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {

        Function<Flux<String>,Flux<String>> filtermap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("default").transform(filtermap);

//        Flux.empty()
        //filter the string whose length is greater than var
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filtermap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concatwith() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        return Flux.concat(abcFlux,defFlux).log();
    }

    public Flux<String> explore_concatwith_mono() {
        var aMono = Mono.just("A");
        var bMono = Flux.just("B");

        return aMono.concatWith(bMono).log();
    }

    public Flux<String> explore_merge() {
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux,defFlux).log();
    }

    public Flux<String> explore_mergeWith() {
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> explore_mergeWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.mergeWith(bMono).log();
    }

    public Flux<String> explore_mergeSequential() {
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux, defFlux).log();
//        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> explore_zip() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        return Flux.zip(abcFlux,defFlux,(first, second) -> first+second).log();
    }

    public Flux<String> explore_zip_1() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        var _123Flux = Flux.just("1","2","3");
        var _456Flux = Flux.just("4","5","6");

        return Flux.zip(abcFlux,defFlux,_123Flux,_456Flux)
                .map(t4 -> t4.getT1()+t4.getT2()+t4.getT3()+t4.getT4())
                .log();
    }

    public Flux<String> explore_zipWith() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        return abcFlux.zipWith(defFlux, (first, second) -> first+second).log();
    }

    public Mono<String> explore_zipWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2->t2.getT1()+t2.getT2())
                .log();
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Mono<List<String>> splitStringMono(String name) {
        var charArray = name.split("");
        var charList = List.of(charArray); //ALEX -> A, L, E, X
        return Mono.just(charList);
    }

    public Flux<String> splitString_withDelay(String name) {
        var charArray = name.split("");
//        var delay = new Random().nextInt(1000);
        var delay = 1000;
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
