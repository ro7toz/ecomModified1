package com.wok.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.wok.app.dto.OrderDto;
import com.wok.app.event.OrderNotificationEvent;
import com.wok.app.exception.wrapper.OrderNotFoundException;
import com.wok.app.helper.OrderMappingHelper;
import com.wok.app.kafka.NotificationProducer;
import com.wok.app.repository.OrderRepository;
import com.wok.app.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	private final OrderRepository orderRepository;
	private final NotificationProducer notificationProducer;
	private final RestTemplate restTemplate;
	
	@Override
	public List<OrderDto> findAll() {
		log.info("*** OrderDto List, service; fetch all orders *");
		return this.orderRepository.findAll()
				.stream()
					.map(OrderMappingHelper::map)
					.distinct()
					.collect(Collectors.toUnmodifiableList());
	}
	
	@Override
	public OrderDto findById(final Integer orderId) {
		log.info("*** OrderDto, service; fetch order by id *");
		return this.orderRepository.findById(orderId)
				.map(OrderMappingHelper::map)
				.orElseThrow(() -> new OrderNotFoundException(String
						.format("Order with id: %d not found", orderId)));
	}
	
	@Override
	public OrderDto save(final OrderDto orderDto) {
		log.info("*** OrderDto, service; save order *");
		OrderDto savedOrder = OrderMappingHelper.map(this.orderRepository
                                                 .save(OrderMappingHelper.map(orderDto)));
		if (orderDto.getCartDto() != null && orderDto.getCartDto().getUserId() != null) {
			try {
            // FETCH USER EMAIL
				String userEmail = "user@example.com"; // Default fallback
				try {
                // Inject RestTemplate if not present
					UserDto user = restTemplate.getForObject(
						AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_URL + "/" + 
						orderDto.getCartDto().getUserId(), 
						UserDto.class
                );
                if (user != null && user.getEmail() != null) {
                    userEmail = user.getEmail();
            }
            } catch (Exception e) {
                log.warn("Could not fetch user email, using default: {}", e.getMessage());
            }
            
            OrderNotificationEvent event = new OrderNotificationEvent(
                savedOrder.getOrderId().toString(),
                orderDto.getCartDto().getUserId().toString(),
                userEmail,  // Use fetched email
                "CREATED",
                "Your order has been placed successfully",
                LocalDateTime.now(),
                savedOrder.getOrderFee()
            );
            notificationProducer.sendOrderNotification(event);
        } catch (Exception e) {
            log.error("Failed to send notification for order {}: {}", 
                      savedOrder.getOrderId(), e.getMessage());
        }
    }
    return savedOrder;
}
	
	@Override
	public OrderDto update(final OrderDto orderDto) {
		log.info("*** OrderDto, service; update order *");
		return OrderMappingHelper.map(this.orderRepository
				.save(OrderMappingHelper.map(orderDto)));
	}
	
	@Override
	public OrderDto update(final Integer orderId, final OrderDto orderDto) {
		log.info("*** OrderDto, service; update order with orderId *");
		return OrderMappingHelper.map(this.orderRepository
				.save(OrderMappingHelper.map(this.findById(orderId))));
	}
	
	@Override
	public void deleteById(final Integer orderId) {
		log.info("*** Void, service; delete order by id *");
		this.orderRepository.delete(OrderMappingHelper.map(this.findById(orderId)));
	}
	
}
