package net.jerrin.demo.socketstream.service;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.MessageRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MessagePublisher<MessageRecord> messagePublisher;

    @KafkaListener(topics = "${kafka.topic.message.name}", groupId = "${kafka.consumer.message.group-id}")
    public void consume(MessageRecord event) {
        messagePublisher.publish(event);
    }
}
