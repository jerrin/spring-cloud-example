package net.jerrin.demo.socketstream.controller;

import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.bridge.MessagePublisher;
import net.jerrin.demo.socketstream.model.StockPrice;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class StockPriceSubscriptionController {

    private final Logger logger = Logger.getLogger(StockPriceSubscriptionController.class.getName());

    private final MessagePublisher<StockPrice> stockPriceMessagePublisher;

    @SubscriptionMapping
    public Flux<StockPrice> stockPriceReactiveStream() {
        return stockPriceMessagePublisher.getRecords()
                .doOnNext(event -> logger.info("Emitting stock price: " + event));
    }
}
