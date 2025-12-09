package com.wok.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wok.app.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	
	
}
