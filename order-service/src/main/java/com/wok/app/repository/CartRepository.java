package com.wok.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wok.app.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	
	
	
}
