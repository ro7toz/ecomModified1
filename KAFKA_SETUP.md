# Kafka Configuration Guide

## 📦 Topic Creation

```bash
#!/bin/bash

echo "Creating Kafka topics..."
echo "Waiting for Kafka to be ready..."
sleep 10

# Create notifications topic (remove -it flag for non-interactive)
docker exec kafka kafka-topics --create \
  --topic notifications \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 1 \
  --if-not-exists

echo "Verifying topic creation..."
docker exec kafka kafka-topics --list \
  --bootstrap-server localhost:9092

echo "Topic created successfully!"


# Optional: Test producer
echo "Testing Kafka producer (optional)..."
echo "Run the following command to test:"
echo "docker exec -it kafka kafka-console-producer --broker-list localhost:9092 --topic notifications"
```

## 🔍 Monitoring

```bash
# Consumer group status
kafka-consumer-groups --bootstrap-server localhost:9092 --list

# Check lag
kafka-consumer-groups --bootstrap-server localhost:9092 \
  --group notification-service-group \
  --describe

# View messages
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic notifications \
  --from-beginning
```

## ⚙️ Configuration

### order-service (Producer)
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
```

### notification-service (Consumer)
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=notification-service-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.consumer.auto-offset-reset=earliest
```

## 🧪 Testing

### Send Test Message
```bash
# Terminal 1: Start consumer
kafka-console-consumer --bootstrap-server localhost:9092 --topic notifications --from-beginning

# Terminal 2: Send message
kafka-console-producer --broker-list localhost:9092 --topic notifications \
  --property "parse.key=true" --property "key.separator=:"

# Then type:
orderId-123:{ "orderId": "123", "status": "CREATED", "message": "Order placed" }
```

## 🐛 Troubleshooting

### Broker not available
```bash
# Check if Kafka is running
docker ps | grep kafka

# Check logs
docker logs kafka

# Restart
docker-compose restart kafka
```

### Consumer lag
```bash
# Check consumer group status
kafka-consumer-groups --bootstrap-server localhost:9092 \
  --group notification-service-group --describe

# Reset offset if needed
kafka-consumer-groups --bootstrap-server localhost:9092 \
  --group notification-service-group \
  --reset-offsets --to-earliest --execute --topic notifications
```
