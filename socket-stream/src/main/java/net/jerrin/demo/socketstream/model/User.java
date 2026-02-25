package net.jerrin.demo.socketstream.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {
    private int id;
    private String name;
    private String username;
    private String email;
    private List<Post> posts;
}
