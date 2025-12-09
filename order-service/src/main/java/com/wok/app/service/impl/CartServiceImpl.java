package com.wok.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wok.app.constant.AppConstant;
import com.wok.app.dto.CartDto;
import com.wok.app.dto.UserDto;
import com.wok.app.exception.wrapper.CartNotFoundException;
import com.wok.app.helper.CartMappingHelper;
import com.wok.app.repository.CartRepository;
import com.wok.app.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	
	private final CartRepository cartRepository;
	private final RestTemplate restTemplate;
	
	@Override
	public List<CartDto> findAll() {
		log.info("*** CartDto List, service; fetch all carts *");
		return this.cartRepository.findAll()
				.stream()
					.map(CartMappingHelper::map)
					.map(c -> {
						c.setUserDto(this.restTemplate.getForObject(AppConstant.DiscoveredDomainsApi
								.USER_SERVICE_API_URL + "/" + c.getUserDto().getUserId(), UserDto.class));
						return c;
					})
					.distinct()
					.collect(Collectors.toUnmodifiableList());
	}
	
	@Override
	public CartDto findById(final Integer cartId) {
		log.info("*** CartDto, service; fetch cart by id *");
		return this.cartRepository.findById(cartId)
				.map(CartMappingHelper::map)
				.map(c -> {
					c.setUserDto(this.restTemplate.getForObject(AppConstant.DiscoveredDomainsApi
							.USER_SERVICE_API_URL + "/" + c.getUserDto().getUserId(), UserDto.class));
					return c;
				})
				.orElseThrow(() -> new CartNotFoundException(String
						.format("Cart with id: %d not found", cartId)));
	}
	
	@Override
	public CartDto save(final CartDto cartDto) {
		log.info("*** CartDto, service; save cart *");
		return CartMappingHelper.map(this.cartRepository
				.save(CartMappingHelper.map(cartDto)));
	}
	
	@Override
	public CartDto update(final CartDto cartDto) {
		log.info("*** CartDto, service; update cart *");
		return CartMappingHelper.map(this.cartRepository
				.save(CartMappingHelper.map(cartDto)));
	}
	
	@Override
	public CartDto update(final Integer cartId, final CartDto cartDto) {
		log.info("*** CartDto, service; update cart with cartId *");
		return CartMappingHelper.map(this.cartRepository
				.save(CartMappingHelper.map(this.findById(cartId))));
	}
	
	@Override
	public void deleteById(final Integer cartId) {
		log.info("*** Void, service; delete cart by id *");
		this.cartRepository.deleteById(cartId);
	}
	
	
	
}










