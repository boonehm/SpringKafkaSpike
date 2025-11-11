package springkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import org.example.springkafka.avro.WeatherReading;

@Component
public class WeatherListener {
    private static final Logger logger = LoggerFactory.getLogger(WeatherListener.class);

    @KafkaListener(topics = "${app.kafka.topics.weather}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(WeatherReading reading,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                          @Header(KafkaHeaders.OFFSET) long offset,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        
        logger.info("Processing weather reading from topic={}, partition={}, offset={}", 
                   topic, partition, offset);
        
        try {
            // Validate the message
            if (reading == null) {
                throw new IllegalArgumentException("Weather reading cannot be null");
            }
            
            if (reading.getCity() == null || reading.getCity().toString().trim().isEmpty()) {
                throw new IllegalArgumentException("City cannot be null or empty");
            }
            
            // Process the weather reading
            processWeatherReading(reading);
            
            logger.info("Successfully processed weather reading for city: {}, temperature: {}", 
                       reading.getCity(), reading.getTemperature());
                       
        } catch (Exception e) {
            logger.error("Error processing weather reading from partition={}, offset={}: {}", 
                        partition, offset, e.getMessage(), e);
            // Re-throw to trigger retry/DLQ mechanism
            throw e;
        }
    }
    
    private void processWeatherReading(WeatherReading reading) {
        // Simulate business logic processing
        logger.debug("Processing weather data: City={}, Temperature={}, Timestamp={}", 
                    reading.getCity(), reading.getTemperature(), reading.getTimestamp());
        
        // Add your business logic here
        // For example: save to database, send alerts, etc.
    }
}