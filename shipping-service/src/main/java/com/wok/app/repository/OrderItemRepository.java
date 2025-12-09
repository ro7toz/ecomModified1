package com.wok.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wok.app.domain.OrderItem;
import com.wok.app.domain.id.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
	
	
	
}
