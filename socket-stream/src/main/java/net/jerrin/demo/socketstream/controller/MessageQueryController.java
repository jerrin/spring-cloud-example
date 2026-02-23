package net.jerrin.demo.socketstream.controller;

import net.jerrin.demo.socketstream.model.MessageRecord;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class MessageQueryController {

    List<MessageRecord> messages = List.of(
            new MessageRecord(UUID.randomUUID().toString(), "Hello, World!"),
            new MessageRecord(UUID.randomUUID().toString(), "Welcome to Spring GraphQL!"),
            new MessageRecord(UUID.randomUUID().toString(), "This is a sample message.")
    );

    @QueryMapping
    public MessageRecord message() throws InterruptedException {
        Thread.sleep(2000);
        return new MessageRecord(UUID.randomUUID().toString(), "Hello, World!");
    }

    @QueryMapping
    public List<MessageRecord> messages() {
        return messages;
    }

    @QueryMapping
    public List<MessageRecord> allMessages() {
        return messages;
    }
}
