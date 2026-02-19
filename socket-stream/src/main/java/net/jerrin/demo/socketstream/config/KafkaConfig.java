package net.jerrin.demo.socketstream.config;


import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.MessageEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.stream.consumer.reactive-group-id}")
    private String groupId;

    @Value("${kafka.stream.topic.name}")
    private String topicName;

    @Bean
    public NewTopic streamTopic() {
        return new NewTopic(topicName, 1, (short) 1);
    }

    @Bean
    public KafkaReceiver<String, MessageEvent> kafkaReceiver() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonDeserializer.class);
        props.put("spring.json.trusted.packages", "*");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);

        ReceiverOptions<String, MessageEvent> receiverOptions =
                ReceiverOptions.<String, MessageEvent>create(props)
                        .subscription(Collections.singleton(topicName));

        return KafkaReceiver.create(receiverOptions);
    }

    @Bean
    public MessagePublisher reactiveMessagePublisher() {
        return new MessagePublisher();
    }


}
