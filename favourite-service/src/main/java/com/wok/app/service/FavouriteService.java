package com.wok.app.service;

import java.util.List;

import com.wok.app.domain.id.FavouriteId;
import com.wok.app.dto.FavouriteDto;

@Transactional
public interface FavouriteService {
	
	List<FavouriteDto> findAll();
	FavouriteDto findById(final FavouriteId favouriteId);
	FavouriteDto save(final FavouriteDto favouriteDto);
	FavouriteDto update(final FavouriteDto favouriteDto);
	void deleteById(final FavouriteId favouriteId);
	
}
