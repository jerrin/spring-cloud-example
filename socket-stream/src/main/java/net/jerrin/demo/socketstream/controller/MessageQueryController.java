package net.jerrin.demo.socketstream.controller;

import net.jerrin.demo.socketstream.model.MessageEvent;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class MessageQueryController {

    List<MessageEvent> messages = List.of(
            new MessageEvent(UUID.randomUUID().toString(), "Hello, World!"),
            new MessageEvent(UUID.randomUUID().toString(), "Welcome to Spring GraphQL!"),
            new MessageEvent(UUID.randomUUID().toString(), "This is a sample message.")
    );

    @QueryMapping
    public MessageEvent message() throws InterruptedException {
        Thread.sleep(2000);
        return new MessageEvent(UUID.randomUUID().toString(), "Hello, World!");
    }

    @QueryMapping
    public List<MessageEvent> messages() {
        return messages;
    }

    @QueryMapping
    public List<MessageEvent> allMessages() {
        return messages;
    }
}
