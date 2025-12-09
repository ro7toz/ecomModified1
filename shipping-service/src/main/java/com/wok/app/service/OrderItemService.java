package com.wok.app.service;

import java.util.List;

import com.wok.app.domain.id.OrderItemId;
import com.wok.app.dto.OrderItemDto;

@Transactional
public interface OrderItemService {
	
	List<OrderItemDto> findAll();
	OrderItemDto findById(final OrderItemId orderItemId);
	OrderItemDto save(final OrderItemDto orderItemDto);
	OrderItemDto update(final OrderItemDto orderItemDto);
	void deleteById(final OrderItemId orderItemId);
	
}
