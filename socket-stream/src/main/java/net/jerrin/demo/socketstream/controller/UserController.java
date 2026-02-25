package net.jerrin.demo.socketstream.controller;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.model.Post;
import net.jerrin.demo.socketstream.model.User;
import net.jerrin.demo.socketstream.service.DataService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final DataService dataService;

    @QueryMapping
    public Mono<User> user(@Argument Integer id) {
        return this.dataService.getUser(id);
    }

    @SchemaMapping(typeName = "User", field = "posts")
    public Flux<Post> post(User user){
        return dataService.getPostsByUserId(user.getId());
    }
}
