package com.wok.app.kafka;

import com.google.firebase.messaging.FirebaseMessaging;
import com.wok.app.event.OrderNotificationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Notification Consumer Tests")
class OrderNotificationConsumerTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

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
    @DisplayName("Should consume and send FCM notification successfully")
    void testConsumeOrderNotificationSuccess() throws Exception {
        ReflectionTestUtils.setField(consumer, "firebaseMessaging", firebaseMessaging);
        when(firebaseMessaging.send(any())).thenReturn("message-id-123");

        consumer.consumeOrderNotification(testEvent);

        verify(firebaseMessaging, times(1)).send(any());
    }

    @Test
    @DisplayName("Should handle FCM send failure gracefully")
    void testConsumeNotificationWithFCMFailure() throws Exception {
        ReflectionTestUtils.setField(consumer, "firebaseMessaging", firebaseMessaging);
        when(firebaseMessaging.send(any())).thenThrow(new RuntimeException("FCM error"));

        consumer.consumeOrderNotification(testEvent);

        verify(firebaseMessaging, times(1)).send(any());
    }

    @Test
    @DisplayName("Should skip notification when Firebase is not configured")
    void testConsumeNotificationWithoutFirebase() {
        ReflectionTestUtils.setField(consumer, "firebaseMessaging", null);

        consumer.consumeOrderNotification(testEvent);
    }
}
