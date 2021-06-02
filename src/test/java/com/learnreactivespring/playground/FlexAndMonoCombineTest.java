package com.learnreactivespring.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FlexAndMonoCombineTest {

    @Test
    public void combineUsingMerge() {

        Flux<String> fluxOne = Flux.just("A", "B", "C");

        Flux<String> fluxTwo = Flux.just("D", "E", "F");

        Flux<String> fluxMerge = Flux.merge(fluxOne, fluxTwo);

        StepVerifier.create(fluxMerge)
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay() {

        Flux<String> fluxOne = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));

        Flux<String> fluxTwo = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> fluxMerge = Flux.merge(fluxOne, fluxTwo);

        StepVerifier.create(fluxMerge.log())
                .expectSubscription()
                .expectNextCount(6)
//                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat() {

        Flux<String> fluxOne = Flux.just("A", "B", "C");

        Flux<String> fluxTwo = Flux.just("D", "E", "F");

        Flux<String> fluxMerge = Flux.concat(fluxOne, fluxTwo);

        StepVerifier.create(fluxMerge)
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_withDelay() {

        VirtualTimeScheduler.getOrSet();

        Flux<String> fluxOne = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));

        Flux<String> fluxTwo = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> fluxMerge = Flux.concat(fluxOne, fluxTwo);

        StepVerifier.withVirtualTime(() -> fluxMerge.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNextCount(6)
                .verifyComplete();

//        StepVerifier.create(fluxMerge.log())
//                .expectSubscription()
//                .expectNext("A", "B", "C", "D", "E", "F")
//                .verifyComplete();
    }

    @Test
    public void combineUsingZip() {

        Flux<String> fluxOne = Flux.just("A", "B", "C");

        Flux<String> fluxTwo = Flux.just("D", "E", "F");

        Flux<String> fluxMerge = Flux.zip(fluxOne, fluxTwo, (t1, t2) -> {
            return t1.concat(t2);
        });

        StepVerifier.create(fluxMerge.log())
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }
}
