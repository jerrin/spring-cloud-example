package net.jerrin.demo.socketstream.controller;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.jerrin.demo.socketstream.model.StockPrice;
import net.jerrin.demo.socketstream.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StockPriceMutationController {

    private final Logger logger = Logger.getLogger(StockPriceMutationController.class.getName());

    public static final String TICKER_AAPL = "AAPL";

    @Value("${kafka.topic.stock-price.name}")
    private String messageTopic;

    private final KafkaProducerService producerService;
    private List<StockPrice> stockPrices;

    @PostConstruct
    public void loadStockPrices() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("data/stock-prices.json");

        ObjectMapper objectMapper = new ObjectMapper();

        var stockPriceList = objectMapper.readValue(
                inputStream,
                new TypeReference<List<StockPrice>>() {}
        );

        this.stockPrices = stockPriceList.stream().map(stockPrice -> {
            stockPrice.setTicker(TICKER_AAPL);
            return stockPrice;
        }).collect(Collectors.toList());

        this.stockPrices.sort((a, b) -> {
            var first = Date.from(Instant.parse(a.getDate()));
            var second = Date.from(Instant.parse(b.getDate()));

            return first.compareTo(second);
        });

        logger.info("Loaded stock prices: " + this.stockPrices);
        logger.info("Loaded stock prices: complete");
    }

    @MutationMapping
    public boolean publishPrices() {
        Flux.fromIterable(this.stockPrices)
                .delayElements(java.time.Duration.ofMillis(500))
                .doOnNext(record -> producerService.sendRecord(messageTopic, record))
                .subscribe();
        return true;
    }
}
