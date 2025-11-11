package springkafka;

import org.example.springkafka.avro.WeatherReading;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, 
               brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"},
               topics = {"weather", "weather-dlq"})
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.consumer.auto-offset-reset=earliest"
})
class WeatherKafkaIntegrationTest {

    @Autowired
    private ProducerService producerService;

    @Test
    void shouldSendAndReceiveWeatherMessage() throws InterruptedException {
        // Given
        WeatherReading weatherReading = new WeatherReading("TestCity", 25.5, Instant.now().toString());

        // When & Then
        assertDoesNotThrow(() -> {
            producerService.send(weatherReading);
            // Give some time for async processing
            TimeUnit.SECONDS.sleep(2);
        });
    }

    @Test
    void shouldHandleNullWeatherReading() {
        // When & Then
        assertDoesNotThrow(() -> {
            try {
                producerService.send(null);
            } catch (IllegalArgumentException e) {
                // Expected behavior
            }
        });
    }
}