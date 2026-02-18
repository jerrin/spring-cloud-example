package net.jerrin.demo.fluxservice.dao;

import net.jerrin.demo.fluxservice.model.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Repository
public class UserDao {

    public Flux<User> getUsers() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println("Emitting user " + i))
                .map(i -> User.builder()
                             .id(String.valueOf(i))
                             .name("User " + i)
                             .build());
    }
}
