package net.jerrin.demo.socketstream.config;


import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.KafkaRecord;
import net.jerrin.demo.socketstream.model.MessageRecord;
import net.jerrin.demo.socketstream.model.StockPrice;
import net.jerrin.demo.socketstream.service.ReactiveKafkaConsumer;
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

    @Value("${kafka.topic.message.name}")
    private String messageTopic;

    @Value("${kafka.consumer.message.reactive-group-id}")
    private String reactiveGroupId;

    @Value("${kafka.topic.stock-price.name}")
    private String stockPriceTopic;

    @Value("${kafka.consumer.stock-price.reactive-group-id}")
    private String stockPriceReactiveGroupId;

    @Bean
    public MessagePublisher<MessageRecord> messagePublisher() {
        return new MessagePublisher<>();
    }

    @Bean
    public MessagePublisher<MessageRecord> reactiveMessagePublisher() {
        return new MessagePublisher<>();
    }

    @Bean
    public ReactiveKafkaConsumer<MessageRecord> reactiveKafkaConsumer(
            MessagePublisher<MessageRecord> reactiveMessagePublisher) {
        KafkaReceiver<String, MessageRecord> reactiveKafkaReceiver = setupReceiver(messageTopic, reactiveGroupId);
        return new ReactiveKafkaConsumer<>(reactiveKafkaReceiver, reactiveMessagePublisher, 1000);
    }

    @Bean
    public MessagePublisher<StockPrice> stockPriceMessagePublisher() {
        return new MessagePublisher<>();
    }

    @Bean
    public ReactiveKafkaConsumer<StockPrice> stockPriceKafkaConsumer(
            MessagePublisher<StockPrice> stockPriceMessagePublisher) {
        KafkaReceiver<String, StockPrice> stockPriceKafkaReceiver = setupReceiver(stockPriceTopic, stockPriceReactiveGroupId);
        return new ReactiveKafkaConsumer<>(stockPriceKafkaReceiver, stockPriceMessagePublisher, 100);
    }

    private <T extends KafkaRecord> KafkaReceiver<String, T> setupReceiver(String topic, String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonDeserializer.class);
        props.put("spring.json.trusted.packages", "*");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);

        ReceiverOptions<String, T> receiverOptions =
                ReceiverOptions.<String, T>create(props)
                        .subscription(Collections.singleton(topic));

        return KafkaReceiver.create(receiverOptions);
    }

}
