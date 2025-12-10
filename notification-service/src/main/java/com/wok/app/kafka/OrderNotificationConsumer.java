package com.wok.app.kafka;

import com.wok.app.event.OrderNotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderNotificationConsumer {

    @Autowired(required = false)  // ADD required = false
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
            
            // ADD NULL CHECK
            if (firebaseMessaging != null) {
                // TODO: Implement FCM notification sending
            } else {
                log.debug("Firebase messaging not configured, skipping FCM notification");
            }
        } catch (Exception e) {
            log.error("Failed to process notification for order {}: {}", 
                event.getOrderId(), e.getMessage(), e);
        }
    }
}
