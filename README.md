# Spring Kafka Weather Service

Enterprise-ready Spring Boot application demonstrating Kafka integration with Avro serialization.

## Features

- ✅ Kafka Producer/Consumer with Avro serialization
- ✅ Error handling with Dead Letter Queue (DLQ)
- ✅ Retry mechanism with exponential backoff
- ✅ Health checks and monitoring (Actuator)
- ✅ Graceful shutdown
- ✅ Environment-specific configurations
- ✅ Integration tests with embedded Kafka
- ✅ Docker support
- ✅ Structured logging

## Quick Start

### Using Docker Compose (Recommended)

```bash
# Start all services (Kafka, Schema Registry, Application)
docker-compose up -d

# Check application health
curl http://localhost:8080/actuator/health

# View logs
docker-compose logs -f weather-app
```

### Local Development

```bash
# Start Kafka and Schema Registry
docker-compose up -d kafka schema-registry

# Run the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Configuration

### Environment Variables (Production)

- `KAFKA_BROKERS`: Kafka bootstrap servers
- `SCHEMA_REGISTRY_URL`: Schema Registry URL
- `KAFKA_SECURITY_PROTOCOL`: Security protocol (SASL_SSL, etc.)
- `KAFKA_SASL_MECHANISM`: SASL mechanism (PLAIN, SCRAM-SHA-256, etc.)
- `KAFKA_SASL_JAAS_CONFIG`: JAAS configuration for authentication

### Profiles

- `dev`: Development with debug logging
- `prod`: Production with security and optimized settings

## Monitoring

### Health Checks
- Application: `http://localhost:8080/actuator/health`
- Kafka connectivity: Included in health check

### Metrics
- Actuator metrics: `http://localhost:8080/actuator/metrics`
- Kafka-specific metrics available

## Testing

```bash
# Run all tests
./mvnw test

# Run integration tests only
./mvnw test -Dtest=*IntegrationTest
```

## Architecture

```
Producer → Kafka Topic → Consumer
    ↓         ↓           ↓
  Logging   DLQ      Error Handling
```

### Error Handling Flow

1. Message processing fails
2. Retry with exponential backoff (configurable attempts)
3. If all retries fail, send to Dead Letter Queue
4. Log error details for investigation

## Production Considerations

### Security
- Enable SASL/SSL for Kafka connections
- Use proper authentication mechanisms
- Secure Schema Registry access

### Monitoring
- Set up alerts on DLQ message count
- Monitor consumer lag
- Track application health checks

### Scaling
- Increase consumer instances for higher throughput
- Partition topics appropriately
- Configure consumer group settings

## Development

### Adding New Message Types

1. Create Avro schema in `src/main/avro/`
2. Run `mvn generate-sources` to generate Java classes
3. Create producer/consumer services
4. Add configuration and tests

### Schema Evolution

- Follow Avro compatibility rules
- Test schema changes thoroughly
- Use Schema Registry compatibility settings