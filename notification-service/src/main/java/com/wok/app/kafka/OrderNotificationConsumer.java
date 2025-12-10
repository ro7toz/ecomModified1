package com.wok.app.kafka;

import com.wok.app.event.OrderNotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderNotificationConsumer {

    @Autowired(required = false)
    private FirebaseMessaging firebaseMessaging;

    @KafkaListener(
        topics = "notifications",
        groupId = "notification-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderNotification(OrderNotificationEvent event) {
        try {
            log.info("Processing notification for order: {}", event.getOrderId());
            log.info("Order Status: {}, Message: {}", event.getStatus(), event.getMessage());
            log.info("User Email: {}, Total Amount: {}", event.getUserEmail(), event.getTotalAmount());
            
            if (firebaseMessaging != null) {
                // TODO: Implement FCM notification sending when Firebase config is available
            }
        } catch (Exception e) {
            log.error("Failed to process notification for order {}: {}", 
                event.getOrderId(), e.getMessage(), e);
        }
    }
}
