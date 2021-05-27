package com.learnreactivespring.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");

    @Test
    public void fluxUsingIterable() {
        Flux<String> fluxNames = Flux.fromIterable(names)
                .log();

        StepVerifier.create(fluxNames)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] names = new String[]{"Adam", "Anna", "Jack", "Jenny"};

        Flux<String> fluxNames = Flux.fromArray(names);

        StepVerifier.create(fluxNames)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> fluxNames = Flux.fromStream(names.stream());

        StepVerifier.create(fluxNames)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty() {

        var mono = Mono.justOrEmpty(null);

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {

        Supplier<String> stringSupplier = () -> "Adam";

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(stringMono)
                .expectNext("Adam")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {

        var integerFlux = Flux.range(1, 5);

        StepVerifier.create(integerFlux)
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }
}
