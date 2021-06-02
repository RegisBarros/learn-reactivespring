package com.learnreactivespring.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");

    @Test
    public void transformUsingMap() {
        Flux<String> fluxNames = Flux.fromIterable(names)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(fluxNames)
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length() {
        Flux<Integer> fluxNames = Flux.fromIterable(names)
                .map(s -> s.length())
                .log();

        StepVerifier.create(fluxNames)
                .expectNext(4, 4, 4, 5)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_Repeat() {
        Flux<Integer> fluxNames = Flux.fromIterable(names)
                .map(s -> s.length())
                .repeat(1)
                .log();

        StepVerifier.create(fluxNames)
                .expectNext(4, 4, 4, 5, 4, 4, 4, 5)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Filter() {
        Flux<String> fluxNames = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(fluxNames)
                .expectNext("JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {
        Flux<String> fluxNames = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })
                .log();

        StepVerifier.create(fluxNames)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    @Test
    public void transformUsingFlatMap_usingParallel_maintain_order() {
        Flux<String> fluxNames = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMapSequential((s) ->
                    s.map(this::convertToList).subscribeOn(parallel())
                )
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(fluxNames)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_usingParallel() {
        Flux<String> fluxNames = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMap((s) ->
                        s.map(this::convertToList).subscribeOn(parallel())
                )
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(fluxNames)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformFluxInMono() {
        Mono<Integer> fluxNames = Flux.fromIterable(names)
                .map(s -> s.length())
                .next()
                .log();

        StepVerifier.create(fluxNames)
                .expectNextCount(1)
                .verifyComplete();
    }
}
