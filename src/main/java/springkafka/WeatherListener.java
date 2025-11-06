package springkafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import springkafka.WeatherReading;

@Component
public class WeatherListener {
    @KafkaListener(topics = "weather", groupId = "weather-app")
    public void onMessage(WeatherReading r,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) int p,
                          @Header(KafkaHeaders.OFFSET) long off) {
        System.out.printf("got %s part=%d offset=%d%n", r, p, off);
    }
}