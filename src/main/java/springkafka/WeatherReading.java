package springkafka;
import java.time.Instant;

public record WeatherReading(String city, double tempF, Instant at) {}