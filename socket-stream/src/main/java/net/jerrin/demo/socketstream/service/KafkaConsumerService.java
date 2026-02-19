package net.jerrin.demo.socketstream.service;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.MessageEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MessagePublisher messagePublisher;

    @KafkaListener(topics = "${kafka.stream.topic.name}", groupId = "${kafka.stream.consumer.group-id}")
    public void consume(MessageEvent event) {
        messagePublisher.publish(event);
    }
}
