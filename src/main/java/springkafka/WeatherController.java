package springkafka;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;
import org.example.springkafka.avro.WeatherReading;
import java.time.Instant;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final ProducerService producer;
    public WeatherController(ProducerService producer) { this.producer = producer; }

    @GetMapping("/{city}/{temp}")
    public String send(@PathVariable String city, @PathVariable double temp) {
        var r = WeatherReading.newBuilder()
                .setCity(city)
                .setTempF(temp)
                .setAt(Instant.now().toString())
                .build();
        producer.send(r);
        return "ok";
    }
}