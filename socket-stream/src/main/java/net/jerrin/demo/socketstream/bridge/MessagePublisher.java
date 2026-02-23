package net.jerrin.demo.socketstream.bridge;

import net.jerrin.demo.socketstream.model.KafkaRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.logging.Logger;

public class MessagePublisher<T extends KafkaRecord> {

    private final Logger logger = Logger.getLogger(MessagePublisher.class.getName());

    private final Sinks.Many<T> sink =
            Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    public void publish(T kafkaRecord) {
        sink.tryEmitNext(kafkaRecord);
    }

    public Flux<T> getRecords() {
        return sink.asFlux()
                .onBackpressureBuffer(500, dropped ->
                        logger.info("Dropped record: " + dropped))
                .limitRate(5);
    }
}
