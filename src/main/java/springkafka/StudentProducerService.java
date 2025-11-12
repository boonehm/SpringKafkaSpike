package springkafka;

import org.example.springkafka.avro.StudentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class StudentProducerService {
    private static final Logger logger = LoggerFactory.getLogger(StudentProducerService.class);
    
    private final KafkaTemplate<String, StudentEvent> kafkaTemplate;
    
    @Value("${app.kafka.topics.student}")
    private String studentTopic;
    
    public StudentProducerService(KafkaTemplate<String, StudentEvent> kafkaTemplate) { 
        this.kafkaTemplate = kafkaTemplate; 
    }

    public void send(StudentEvent event) {
        try {
            if (event == null) {
                throw new IllegalArgumentException("Student event cannot be null");
            }
            
            String key = event.getStudentId().toString();
            logger.info("Sending student event for ID: {}", key);
            
            CompletableFuture<SendResult<String, StudentEvent>> future = 
                kafkaTemplate.send(studentTopic, key, event);
                
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to send student event for ID: {}, error: {}", 
                               key, ex.getMessage(), ex);
                } else {
                    logger.info("Successfully sent student event for ID: {} to partition: {} with offset: {}", 
                               key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                }
            });
            
        } catch (Exception e) {
            logger.error("Error sending student event: {}", e.getMessage(), e);
            throw e;
        }
    }
}