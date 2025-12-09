package com.wok.app.helper;

import com.wok.app.domain.Category;
import com.wok.app.domain.Product;
import com.wok.app.dto.CategoryDto;
import com.wok.app.dto.ProductDto;

public interface ProductMappingHelper {
	
	public static ProductDto map(final Product product) {
		return ProductDto.builder()
				.productId(product.getProductId())
				.productTitle(product.getProductTitle())
				.imageUrl(product.getImageUrl())
				.sku(product.getSku())
				.priceUnit(product.getPriceUnit())
				.quantity(product.getQuantity())
				.categoryDto(
						CategoryDto.builder()
							.categoryId(product.getCategory().getCategoryId())
							.categoryTitle(product.getCategory().getCategoryTitle())
							.imageUrl(product.getCategory().getImageUrl())
							.build())
				.build();
	}
	
	public static Product map(final ProductDto productDto) {
		return Product.builder()
				.productId(productDto.getProductId())
				.productTitle(productDto.getProductTitle())
				.imageUrl(productDto.getImageUrl())
				.sku(productDto.getSku())
				.priceUnit(productDto.getPriceUnit())
				.quantity(productDto.getQuantity())
				.category(
						Category.builder()
							.categoryId(productDto.getCategoryDto().getCategoryId())
							.categoryTitle(productDto.getCategoryDto().getCategoryTitle())
							.imageUrl(productDto.getCategoryDto().getImageUrl())
							.build())
				.build();
	}
	
	
	
}










