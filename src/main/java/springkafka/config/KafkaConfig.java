package springkafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.BackOff;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topics.weather-dlq}")
    private String deadLetterTopic;

    @Value("${app.kafka.retry.attempts}")
    private int retryAttempts;

    @Value("${app.kafka.retry.backoff}")
    private long backoffInterval;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate) {
        
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler(kafkaTemplate));
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        // Create dead letter publisher
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
            (record, ex) -> {
                // Route failed messages to DLQ
                return new org.apache.kafka.common.TopicPartition(deadLetterTopic, record.partition());
            });

        // Configure exponential backoff
        BackOff backOff = new ExponentialBackOffWithMaxRetries(retryAttempts);
        ((ExponentialBackOffWithMaxRetries) backOff).setInitialInterval(backoffInterval);
        ((ExponentialBackOffWithMaxRetries) backOff).setMultiplier(2.0);

        return new DefaultErrorHandler(recoverer, backOff);
    }
}