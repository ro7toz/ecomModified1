package com.wok.app.kafka;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
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

            if (firebaseMessaging == null) {
                log.warn("Firebase not configured. Skipping FCM notification for order: {}", event.getOrderId());
                return;
            }

            Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle("Order Update: " + event.getStatus())
                    .setBody(event.getMessage())
                    .build())
                .putData("orderId", event.getOrderId())
                .putData("status", event.getStatus())
                .putData("timestamp", event.getTimestamp().toString())
                .putData("totalAmount", event.getTotalAmount().toString())
                .setToken(generateUserToken(event.getUserId()))
                .build();

            String response = firebaseMessaging.send(message);
            log.info("Notification sent successfully via FCM. Message ID: {} for order: {}", response, event.getOrderId());

        } catch (Exception e) {
            log.error("Failed to send FCM notification for order {}: {}", 
                event.getOrderId(), e.getMessage(), e);
        }
    }

    private String generateUserToken(String userId) {
        return "device-token-" + userId;
    }
}
