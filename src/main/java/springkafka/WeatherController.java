package springkafka;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final ProducerService producer;
    public WeatherController(ProducerService producer) { this.producer = producer; }

    @GetMapping("/{city}/{temp}")
    public String send(@PathVariable String city, @PathVariable double temp) {
        producer.send(new WeatherReading(city, temp, java.time.Instant.now()));
        return "ok";
    }
}