package com.wok.app.helper;

import com.wok.app.domain.Cart;
import com.wok.app.dto.CartDto;
import com.wok.app.dto.UserDto;

public interface CartMappingHelper {
	
	public static CartDto map(final Cart cart) {
		return CartDto.builder()
				.cartId(cart.getCartId())
				.userId(cart.getUserId())
				.userDto(
						UserDto.builder()
							.userId(cart.getUserId())
							.build())
				.build();
	}
	
	public static Cart map(final CartDto cartDto) {
		return Cart.builder()
				.cartId(cartDto.getCartId())
				.userId(cartDto.getUserId())
				.build();
	}
	
	
	
}










