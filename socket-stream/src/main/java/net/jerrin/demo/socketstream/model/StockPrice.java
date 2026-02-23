package net.jerrin.demo.socketstream.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class StockPrice implements KafkaRecord{
    private String ticker;
    private String date;

    @JsonAlias("close")
    private BigDecimal price;
}
