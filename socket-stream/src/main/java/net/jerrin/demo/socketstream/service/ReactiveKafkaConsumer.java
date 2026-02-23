package net.jerrin.demo.socketstream.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.KafkaRecord;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;

@RequiredArgsConstructor
public class ReactiveKafkaConsumer<T extends KafkaRecord> {

    private final KafkaReceiver<String, T> kafkaReceiver;
    private final MessagePublisher<T> reactiveMessagePublisher;
    private final int delayMillis;

    @PostConstruct
    public void setupKafkaFlux() {
        this.kafkaReceiver
                .receive()
                .map(ReceiverRecord::value)
                // Throttle consumption: delay each element 100ms
                .delayElements(Duration.ofMillis(this.delayMillis))
                .doOnNext(reactiveMessagePublisher::publish)
                .publish()
                .connect();
    }

}
