package com.wok.app.helper;

import com.wok.app.domain.Address;
import com.wok.app.domain.User;
import com.wok.app.dto.AddressDto;
import com.wok.app.dto.UserDto;

public interface AddressMappingHelper {
	
	public static AddressDto map(final Address address) {
		return AddressDto.builder()
				.addressId(address.getAddressId())
				.fullAddress(address.getFullAddress())
				.postalCode(address.getPostalCode())
				.city(address.getCity())
				.userDto(
					UserDto.builder()
						.userId(address.getUser().getUserId())
						.firstName(address.getUser().getFirstName())
						.lastName(address.getUser().getLastName())
						.imageUrl(address.getUser().getImageUrl())
						.email(address.getUser().getEmail())
						.phone(address.getUser().getPhone())
						.build())
				.build();
	}
	
	public static Address map(final AddressDto addressDto) {
		return Address.builder()
				.addressId(addressDto.getAddressId())
				.fullAddress(addressDto.getFullAddress())
				.postalCode(addressDto.getPostalCode())
				.city(addressDto.getCity())
				.user(
					User.builder()
						.userId(addressDto.getUserDto().getUserId())
						.firstName(addressDto.getUserDto().getFirstName())
						.lastName(addressDto.getUserDto().getLastName())
						.imageUrl(addressDto.getUserDto().getImageUrl())
						.email(addressDto.getUserDto().getEmail())
						.phone(addressDto.getUserDto().getPhone())
						.build())
				.build();
	}
	
	
	
}










