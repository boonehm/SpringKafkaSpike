package springkafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Configuration
public class GracefulShutdownConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(GracefulShutdownConfig.class);
    
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    
    @EventListener
    public void handleContextClosed(ContextClosedEvent event) {
        logger.info("Starting graceful shutdown of Kafka listeners...");
        
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(container -> {
            try {
                container.stop();
                logger.info("Stopped Kafka listener container: {}", container.getGroupId());
            } catch (Exception e) {
                logger.error("Error stopping Kafka listener container: {}", e.getMessage(), e);
            }
        });
        
        // Wait a bit for in-flight messages to complete
        try {
            TimeUnit.SECONDS.sleep(5);
            logger.info("Graceful shutdown completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Graceful shutdown interrupted");
        }
    }
}