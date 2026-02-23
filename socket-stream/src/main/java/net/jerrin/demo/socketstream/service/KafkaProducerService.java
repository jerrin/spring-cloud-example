package net.jerrin.demo.socketstream.service;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.model.KafkaRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final Logger logger = Logger.getLogger(KafkaProducerService.class.getName());

    private final KafkaTemplate<String, KafkaRecord> kafkaTemplate;

    public void sendRecord(String topic, KafkaRecord record) {
        logger.info("Sending record to Kafka: " + record);
        kafkaTemplate.send(topic, record);
    }
}
