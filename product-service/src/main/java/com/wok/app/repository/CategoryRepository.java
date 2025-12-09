package com.wok.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wok.app.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	
	
}
