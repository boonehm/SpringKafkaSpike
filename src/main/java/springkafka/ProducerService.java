package springkafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private final KafkaTemplate<String, WeatherReading> kafka;

    public ProducerService(KafkaTemplate<String, WeatherReading> kafka) {
        this.kafka = kafka;
    }
    public void send(WeatherReading r) {
        kafka.send("weather", r.city(), r);
    }
}