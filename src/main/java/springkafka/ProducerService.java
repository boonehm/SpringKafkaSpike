package springkafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.example.springkafka.avro.WeatherReading;

@Service
public class ProducerService {
    private final KafkaTemplate<String, WeatherReading> kafka;
    public ProducerService(KafkaTemplate<String, WeatherReading> kafka) { this.kafka = kafka; }

    public void send(WeatherReading r) {
        kafka.send("weather", r.getCity().toString(), r);
    }
}