package net.jerrin.demo.socketstream.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.MessageEvent;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ReactiveKafkaConsumer {

    private final KafkaReceiver<String, MessageEvent> kafkaReceiver;
    private final MessagePublisher reactiveMessagePublisher;

    @PostConstruct
    public void setupKafkaFlux() {
        this.kafkaReceiver.receive()
                .map(ReceiverRecord::value)
                // Throttle consumption: delay each element 100ms
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(reactiveMessagePublisher::publish)
                .publish()
                .connect();
    }

}
