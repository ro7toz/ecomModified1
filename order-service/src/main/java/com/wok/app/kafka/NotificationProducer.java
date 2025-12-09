package com.wok.app.kafka;

import com.wok.app.event.OrderNotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationProducer {

    @Autowired
    private KafkaTemplate<String, OrderNotificationEvent> kafkaTemplate;

    private static final String TOPIC = "notifications";

    public void sendOrderNotification(OrderNotificationEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.getOrderId(), event);
            log.info("Order notification sent to Kafka topic '{}' for order: {}", TOPIC, event.getOrderId());
        } catch (Exception e) {
            log.error("Failed to send notification for order {}: {}", event.getOrderId(), e.getMessage(), e);
        }
    }
}
