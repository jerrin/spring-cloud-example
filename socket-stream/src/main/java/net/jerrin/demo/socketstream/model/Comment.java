package net.jerrin.demo.socketstream.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Comment {
    private int id;
    private int postId;
    private String name;
    private String email;
    private String body;
}
