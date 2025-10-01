package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
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

    public Flux<String> exception_flux() {
        return Flux.just("A","B","C").concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWith(Flux.just("D"))
                .log();
    }

    public Flux<String> explore_OnErrorReturn() {
        return Flux.just("A","B","C").concatWith(Flux.error(new IllegalStateException("Exception occured")))
                .onErrorReturn("D")
                .log();
    }

    public Flux<String> explore_OnErrorResume(Exception e) {

        var recoveryFlux = Flux.just("D", "E", "F");

        return Flux.just("A", "B", "C").concatWith(Flux.error(e))
                .onErrorResume(ex -> {
                    log.error("Exception is ", ex);
                    if(ex instanceof IllegalStateException) {
                        return recoveryFlux;

                    } else {
                        return Flux.error(ex);
                    }
                })
                .log();
    }

    public Flux<String> explore_OnErrorContinue() {

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if (name.equals("B")){
                        throw new IllegalStateException("Exception Occurred");
                    }
                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorContinue((ex, name) -> {
                    log.error("Exception is ", ex);
                    log.info("name is " + name);
                })
                .log();
    }

    public Flux<String> explore_OnErrorMap() {

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if (name.equals("B")){
                        throw new IllegalStateException("Exception Occurred");
                    }
                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorMap((ex) -> {
                    log.error("Exception is ", ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }

    public Flux<String> explore_doOnError() {
        return Flux.just("A","B","C").concatWith(Flux.error(new IllegalStateException("Exception occured")))
                .doOnError(ex ->{
                    log.error("Exception is ", ex);
                })
                .log();
    }

    public Mono<Object> explore_Mono_onErrorReturn() {
        return Mono.just("A")
                .map(value -> {
                    throw new RuntimeException("Exception occured");
                })
                .onErrorReturn("abc")
                .log();
    }

    public Flux<Integer> explore_generate() {
        return Flux.generate(()->1,(state,sink)->{
            sink.next(state*2);
            if (state==10) {
                sink.complete();
            }
            return state+1;
        });
    }

    public Mono<Object> exception_mono_onErrorMap(Exception e) {
        return Mono.just("B").map(value -> {
            throw new RuntimeException("Exception Occured");
        }).onErrorMap(ex-> {
            log.error("Exception Occured");
            return new ReactorException(ex, ex.getMessage());
        });
    }

    public Mono<String> exception_Mono_onErrorContinue(String input) {
        return Mono.just(input).map(value -> {
            if (value.equals("abc")) {
                throw new RuntimeException("Exception Occured");
            } return value;
        }).onErrorContinue((ex, value) -> {
            log.info("Exception is "+ ex);
            log.info("Value is", value);
        }).log();
    }


    public static List<String> names() {
        delay(1000);
        return List.of("alex","ben","chloe");
    }

    public Flux<String> explore_create() {
        return Flux.create(sink -> {
//            names().forEach(sink::next);

            CompletableFuture
                    .supplyAsync(()->names())
                            .thenAccept(names -> {
                                names().forEach((name) -> {
                                    sink.next(name);
                                    sink.next(name);
                                });
                            })
                    .thenRun(()->sendEvents(sink));
        });
    }

    public void sendEvents(FluxSink<String> sink) {
            CompletableFuture
                    .supplyAsync(()->names())
                    .thenAccept(names -> {
                        names().forEach(sink::next);
                    })
                    .thenRun(sink::complete);
    }

    public Mono<String> explore_create_mono() {
        return Mono.create(sink->{
            sink.success("alex");
        });
    }

    public Flux<String> explore_handle() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .handle((name, sink)->{
                    if(name.length()>3){
                        sink.next(name.toUpperCase());
                    }
                });
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
