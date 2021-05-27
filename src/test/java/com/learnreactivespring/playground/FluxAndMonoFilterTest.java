package com.learnreactivespring.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");

    @Test
    public void filterTest() {
        var flux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("A"))
                .log();

        StepVerifier.create(flux)
                .expectNext("Adam", "Anna")
                .verifyComplete();

    }

    @Test
    public void filterTestLength() {
        var flux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .log();

        StepVerifier.create(flux)
                .expectNext("Jenny")
                .verifyComplete();

    }
}
