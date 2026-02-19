package net.jerrin.demo.socketstream.controller;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.MessageEvent;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class MessageSubscriptionController {

    private final Logger logger = Logger.getLogger(MessageSubscriptionController.class.getName());

    private final MessagePublisher messagePublisher;
    private final MessagePublisher reactiveMessagePublisher;

    @SubscriptionMapping
    public Flux<MessageEvent> messageStream() {
        return messagePublisher.getMessages()
                .doOnNext(event -> logger.info("Emitting event: " + event))
                .delayElements(Duration.ofSeconds(1));
    }

    @SubscriptionMapping
    public Flux<MessageEvent> messageReactiveStream() {
        // Throttled at receiver level, so we can emit as fast as Kafka produces
        return reactiveMessagePublisher.getMessages()
                .doOnNext(event -> logger.info("Emitting reactive event: " + event));
    }
}
