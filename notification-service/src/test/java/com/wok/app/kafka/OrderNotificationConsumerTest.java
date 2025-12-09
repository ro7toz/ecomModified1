package com.wok.app.kafka;

import com.wok.app.event.OrderNotificationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Notification Consumer Tests")
class OrderNotificationConsumerTest {

    @InjectMocks
    private OrderNotificationConsumer consumer;

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
    @DisplayName("Should consume order notification successfully")
    void testConsumeOrderNotificationSuccess() {
        assertDoesNotThrow(() -> consumer.consumeOrderNotification(testEvent));
    }

    @Test
    @DisplayName("Should handle null event gracefully")
    void testConsumeNullEvent() {
        assertDoesNotThrow(() -> {
            OrderNotificationEvent nullEvent = new OrderNotificationEvent();
            consumer.consumeOrderNotification(nullEvent);
        });
    }
}
