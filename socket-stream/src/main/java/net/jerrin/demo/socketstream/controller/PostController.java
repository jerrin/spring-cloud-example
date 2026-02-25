package net.jerrin.demo.socketstream.controller;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.model.Comment;
import net.jerrin.demo.socketstream.model.Post;
import net.jerrin.demo.socketstream.service.DataService;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private static final Logger logger = Logger.getLogger(PostController.class.getName());

    private final DataService dataService;

    @BatchMapping(typeName = "Post")
    public Mono<Map<Post, List<Comment>>> comments(List<Post> posts) {
        var postIds = posts.stream().map(Post::getId).toList();
        logger.info("Fetching comments for posts: " + postIds);
        //TODO: instead of fetching all comments and filtering in memory, we should have an endpoint that accepts multiple postIds and returns a map of postId to comments
        Mono<List<Comment>> listMono = this.dataService.getComments()
                .filter(comment -> postIds.contains(comment.getPostId())
                ).collectList();

        return listMono
                .map(comments -> {
                    // Group reviews by postId
                    Map<Integer, List<Comment>> map = comments.stream()
                            .collect(Collectors.groupingBy(Comment::getPostId));

                    // Maintain order of requested posts
                    return posts.stream()
                            .collect(Collectors.toMap(post -> post,
                                    post -> map.get(post.getId())));
                });
    }
}
