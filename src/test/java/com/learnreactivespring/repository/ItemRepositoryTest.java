package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    List<Item> itemList = Arrays.asList(
            new Item(null, "Samsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null, "Sony TV", 560.0),
            new Item(null, "Apple Watch", 299.99),
            new Item(null, "Beats Headphones", 149.99),
            new Item("ABC", "Bose Headphones", 149.99)
    );

    @BeforeEach
    public void setUp() {
        itemRepository.deleteAll()
            .thenMany(Flux.fromIterable(itemList))
            .flatMap(itemRepository::save)
            .doOnNext((item -> {
                System.out.println("Inserted Item is: " + item);
            }))
            .blockLast();
    }

    @Test
    public void getAllItems() {

        Flux<Item> items = itemRepository.findAll();

        StepVerifier.create(items)
            .expectSubscription()
            .expectNextCount(6)
            .verifyComplete();
    }

    @Test
    public void getItemById() {

        String itemId = "ABC";

        Mono<Item> item = itemRepository.findById(itemId);

        StepVerifier.create(item)
                .expectSubscription()
                .expectNextMatches((i -> i.getDescription().equals("Bose Headphones")))
                .verifyComplete();
    }

    @Test
    public void findItemByDescription() {

        String description = "Bose Headphones";

        Mono<Item> item = itemRepository.findByDescription(description);

        StepVerifier.create(item)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void save() {

        var item = new Item(null, "Google Home Mini", 30.00);

        Mono<Item> savedItem = itemRepository.save(item);

        StepVerifier.create(savedItem)
                .expectSubscription()
                .expectNextMatches((i -> i.getId() != null && i.getDescription().equals("Google Home Mini")))
                .verifyComplete();
    }

    @Test
    public void updateItem() {

        String description = "LG TV";
        double newPrice = 520.00;

        Mono<Item> updatedItem = itemRepository.findByDescription(description)
            .map(item -> {
                item.setPrice(newPrice);
                return item;
            })
            .flatMap(i -> {
                return itemRepository.save(i);
            });

        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches((i -> i.getPrice().equals(newPrice)))
                .verifyComplete();
    }

    @Test
    public void deleteItem() {

        String itemId = "ABC";

        Mono<Void> deleteItem = itemRepository.findById(itemId)
                .map(Item::getId)
                .flatMap((id) -> {
                    return itemRepository.deleteById(id);
                });

        StepVerifier.create(deleteItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void deleteItemAlternative() {

        String description = "LG TV";

        Mono<Void> deleteItem = itemRepository.findByDescription(description)
                .flatMap((item) -> {
                    return itemRepository.delete(item);
                });

        StepVerifier.create(deleteItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findByDescription(description))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

}
