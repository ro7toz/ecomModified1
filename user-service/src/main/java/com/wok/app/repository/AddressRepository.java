package com.wok.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wok.app.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
	
	
	
}
