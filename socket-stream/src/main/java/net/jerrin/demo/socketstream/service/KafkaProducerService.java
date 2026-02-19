package net.jerrin.demo.socketstream.service;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.model.MessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final Logger logger = Logger.getLogger(KafkaProducerService.class.getName());

    @Value("${kafka.stream.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, MessageEvent> kafkaTemplate;

    public void sendMessage(MessageEvent event) {
        logger.info("Sending message to Kafka: " + event.content());
        kafkaTemplate.send(topicName, event.id(), event);
    }
}
