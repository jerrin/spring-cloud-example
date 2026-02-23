package net.jerrin.demo.socketstream.controller;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.MessageRecord;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class MessageSubscriptionController {

    private final Logger logger = Logger.getLogger(MessageSubscriptionController.class.getName());

    private final MessagePublisher<MessageRecord> messagePublisher;
    private final MessagePublisher<MessageRecord> reactiveMessagePublisher;

    @SubscriptionMapping
    public Flux<MessageRecord> messageStream() {
        return messagePublisher.getRecords()
                .doOnNext(event -> logger.info("Emitting event: " + event));
    }

    @SubscriptionMapping
    public Flux<MessageRecord> messageReactiveStream() {
        // Throttled at receiver level, so we can emit as fast as Kafka produces
        return reactiveMessagePublisher.getRecords()
                .doOnNext(event -> logger.info("Emitting reactive event: " + event));
    }
}
