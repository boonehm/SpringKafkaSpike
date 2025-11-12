package springkafka;

import org.example.springkafka.avro.StudentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class StudentController {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    
    @Autowired
    private StudentProducerService studentProducerService;
    
    @PostMapping("/students")
    public ResponseEntity<String> submitStudentForm(@RequestBody StudentRequest request) {
        logger.info("Received student form submission for student ID: {}", request.getStudentId());
        
        try {
            // Create event from form data
            StudentEvent event = new StudentEvent(
                request.getStudentId(),
                request.getName(),
                request.getEmail(),
                request.getMajor(),
                Instant.now().toString()
            );
            
            // Publish to Kafka
            studentProducerService.send(event);
            
            logger.info("Successfully published student event for ID: {}", request.getStudentId());
            return ResponseEntity.ok("Student form submitted successfully");
            
        } catch (Exception e) {
            logger.error("Error processing student form for ID: {}", request.getStudentId(), e);
            return ResponseEntity.internalServerError().body("Error processing student form");
        }
    }
}