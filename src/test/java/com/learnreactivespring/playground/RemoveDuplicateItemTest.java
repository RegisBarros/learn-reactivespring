package com.learnreactivespring.playground;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

public class RemoveDuplicateItemTest {

    @Test
    public void removeDuplicateItem() {
        String duplicateId = UUID.randomUUID().toString();
        var item1 = new Item(UUID.randomUUID().toString(), "Nike");
        var item2 = new Item(duplicateId, "Adidas");
        var item3 = new Item(UUID.randomUUID().toString(), "Puma");
        var item4 = new Item(UUID.randomUUID().toString(), "Kappa");

        var item21 = new Item(duplicateId, "Adidas II");

        Flux<Item> elements = Flux.just(item1, item2, item3, item4, item21);

//        var flux = elements.groupBy(Item::getId)
//                .flatMap(groupedItem -> groupedItem.reduce((a,b) -> b))
//                .subscribe(item -> {
//                    System.out.println(item.name);
//                });

//        var flux = elements
//                .distinct(Item::getId)
//                .subscribe(item -> {
//                    System.out.println(item.name);
//                });
//
        var flux = elements
                .distinct(Item::getId);

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(item1, item2, item3, item4)
                .verifyComplete();
    }

    public class Item {
        private String id;
        private String name;

        public Item(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}



