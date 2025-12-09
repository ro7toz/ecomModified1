package com.wok.app.kafka;

import com.wok.app.event.OrderNotificationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notification Producer Tests")
class NotificationProducerTest {

    @Mock
    private KafkaTemplate<String, OrderNotificationEvent> kafkaTemplate;

    @InjectMocks
    private NotificationProducer notificationProducer;

    private OrderNotificationEvent testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new OrderNotificationEvent(
            "order-123",
            "user-456",
            "user@example.com",
            "CREATED",
            "Your order has been placed successfully",
            LocalDateTime.now(),
            1500.0
        );
    }

    @Test
    @DisplayName("Should send notification to Kafka successfully")
    void testSendOrderNotificationSuccess() {
        notificationProducer.sendOrderNotification(testEvent);

        verify(kafkaTemplate, times(1)).send("notifications", testEvent.getOrderId(), testEvent);
    }

    @Test
    @DisplayName("Should handle exception gracefully when Kafka fails")
    void testSendNotificationWithException() {
        doThrow(new RuntimeException("Kafka broker unavailable"))
            .when(kafkaTemplate).send(anyString(), anyString(), any());

        notificationProducer.sendOrderNotification(testEvent);

        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("Should send multiple notifications without errors")
    void testSendMultipleNotifications() {
        OrderNotificationEvent event1 = new OrderNotificationEvent("order-1", "user-1", "u1@test.com", 
            "CONFIRMED", "Order confirmed", LocalDateTime.now(), 500.0);
        OrderNotificationEvent event2 = new OrderNotificationEvent("order-2", "user-2", "u2@test.com", 
            "SHIPPED", "Order shipped", LocalDateTime.now(), 700.0);

        notificationProducer.sendOrderNotification(event1);
        notificationProducer.sendOrderNotification(event2);

        verify(kafkaTemplate, times(2)).send(anyString(), anyString(), any());
    }
}
