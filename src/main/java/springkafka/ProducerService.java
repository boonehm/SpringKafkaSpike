package springkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.example.springkafka.avro.WeatherReading;
import java.util.concurrent.CompletableFuture;

@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);
    
    private final KafkaTemplate<String, WeatherReading> kafkaTemplate;
    
    @Value("${app.kafka.topics.weather}")
    private String weatherTopic;
    
    public ProducerService(KafkaTemplate<String, WeatherReading> kafkaTemplate) { 
        this.kafkaTemplate = kafkaTemplate; 
    }

    public void send(WeatherReading reading) {
        try {
            // Validate input
            if (reading == null) {
                throw new IllegalArgumentException("Weather reading cannot be null");
            }
            
            String key = reading.getCity().toString();
            logger.info("Sending weather reading for city: {}", key);
            
            // Send message asynchronously with callback
            CompletableFuture<SendResult<String, WeatherReading>> future = 
                kafkaTemplate.send(weatherTopic, key, reading);
                
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to send weather reading for city: {}, error: {}", 
                               key, ex.getMessage(), ex);
                } else {
                    logger.info("Successfully sent weather reading for city: {} to partition: {} with offset: {}", 
                               key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                }
            });
            
        } catch (Exception e) {
            logger.error("Error sending weather reading: {}", e.getMessage(), e);
            throw e;
        }
    }
}
