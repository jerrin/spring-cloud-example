package net.jerrin.demo.socketstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SocketStreamApplication {

	void main() {
		SpringApplication.run(net.jerrin.demo.socketstream.SocketStreamApplication.class);
	}

}
