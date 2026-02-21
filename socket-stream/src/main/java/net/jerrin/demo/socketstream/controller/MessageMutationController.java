package net.jerrin.demo.socketstream.controller;
import net.jerrin.demo.socketstream.model.MessageEvent;
import net.jerrin.demo.socketstream.model.SendMessageInput;
import net.jerrin.demo.socketstream.service.KafkaProducerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Controller
public class MessageMutationController {

    private final KafkaProducerService producerService;

    public MessageMutationController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @MutationMapping
    public MessageEvent sendMessage(@Argument SendMessageInput input) {
        var message = input.content();

        Flux.range(1, 15)
                .delayElements(java.time.Duration.ofMillis(500))
                .map(i -> createMessageEvent(i, message))
                .doOnNext(producerService::sendMessage)
                .subscribe();

        return new MessageEvent(
                UUID.randomUUID().toString(),
                message
        );
    }

    private MessageEvent createMessageEvent(int index, String message) {
        String id = UUID.randomUUID().toString();
        return new MessageEvent(
                id,
                String.format("%s - %s", index, message)
        );    }
}
