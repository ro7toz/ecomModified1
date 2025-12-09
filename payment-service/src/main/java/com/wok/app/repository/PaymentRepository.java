package com.wok.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wok.app.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	
	
	
}
