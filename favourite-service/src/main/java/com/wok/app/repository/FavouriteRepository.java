package com.wok.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wok.app.domain.Favourite;
import com.wok.app.domain.id.FavouriteId;

public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {
	
	
	
}
