package com.wok.app.service;

import java.util.List;

import com.wok.app.dto.CategoryDto;

public interface CategoryService {
	
	List<CategoryDto> findAll();
	CategoryDto findById(final Integer categoryId);
	CategoryDto save(final CategoryDto categoryDto);
	CategoryDto update(final CategoryDto categoryDto);
	CategoryDto update(final Integer categoryId, final CategoryDto categoryDto);
	void deleteById(final Integer categoryId);
	
}
