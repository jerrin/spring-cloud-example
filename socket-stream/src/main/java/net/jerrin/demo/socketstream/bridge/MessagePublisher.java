package net.jerrin.demo.socketstream.bridge;

import net.jerrin.demo.socketstream.model.MessageEvent;
import net.jerrin.demo.socketstream.service.KafkaProducerService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.logging.Logger;

@Component
public class MessagePublisher {

    private final Logger logger = Logger.getLogger(MessagePublisher.class.getName());

    private final Sinks.Many<MessageEvent> sink =
            Sinks.many().multicast().onBackpressureBuffer(1000);

    public void publish(MessageEvent event) {
        sink.tryEmitNext(event);
    }

    public Flux<MessageEvent> getMessages() {
        return sink.asFlux()
                .onBackpressureBuffer(500, dropped ->
                        logger.info("Dropped message: " + dropped))
                .limitRate(5);
    }
}
