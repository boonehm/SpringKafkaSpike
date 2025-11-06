package springkafka;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class DemoRunner {
    @Bean
    CommandLineRunner demo(ProducerService producer) {
        return args -> {
            for (var city : List.of("Denver","Lehi","SLC")) {
                producer.send(new WeatherReading(city, 72.5, Instant.now()));
            }
        };
    }
}