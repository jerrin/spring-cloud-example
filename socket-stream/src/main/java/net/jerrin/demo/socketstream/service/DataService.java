package net.jerrin.demo.socketstream.service;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.model.Comment;
import net.jerrin.demo.socketstream.model.Post;
import net.jerrin.demo.socketstream.model.User;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class DataService {

    private static final Logger logger = Logger.getLogger(DataService.class.getName());

    private final WebClient webClient;

    public Mono<User> getUser(int userId) {
        logger.info("Fetching user with id: " + userId);
        return webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(User.class);
    }

    public Flux<Post> getPostsByUserId(int userId) {
        logger.info("Fetching posts by user id: " + userId);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/posts")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .bodyToFlux(Post.class);
    }

    // TODO: replace this with endpoint that accepts multiple postIds and returns a map of postId to comments
    public Flux<Comment> getComments() {
        logger.info("Get all post comments" );
        return webClient.get()
                .uri("/comments")
                .retrieve()
                .bodyToFlux(Comment.class);
    }
}
