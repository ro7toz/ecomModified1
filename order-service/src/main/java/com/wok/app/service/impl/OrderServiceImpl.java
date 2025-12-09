package com.wok.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.wok.app.dto.OrderDto;
import com.wok.app.exception.wrapper.OrderNotFoundException;
import com.wok.app.helper.OrderMappingHelper;
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
		
		// Send notification event
		OrderNotificationEvent event = new OrderNotificationEvent(
			savedOrder.getOrderId().toString(),
			orderDto.getCartDto() != null ? orderDto.getCartDto().getUserId().toString() : "unknown",
			"user@example.com", // Fetch from user service
			"CREATED",
			"Your order has been placed successfully",
			LocalDateTime.now(),
			savedOrder.getOrderFee()
		);
		notificationProducer.sendOrderNotification(event);
		
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










