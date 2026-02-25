package net.jerrin.demo.socketstream.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Post {
    private int id;
    private int userId;
    private String title;
    private String body;
    List<Comment> comments;
}
