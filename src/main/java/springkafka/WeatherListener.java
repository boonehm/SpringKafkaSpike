package springkafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import org.example.springkafka.avro.WeatherReading;

@Component
public class WeatherListener {
    @KafkaListener(topics = "weather", groupId = "weather-app-avro")
    public void onMessage(WeatherReading r,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) int p,
                          @Header(KafkaHeaders.OFFSET) long off) {
        System.out.printf("got (Avro) %s part=%d offset=%d%n", r, p, off);
    }
}