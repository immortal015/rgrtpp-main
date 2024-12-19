package com.tpp.tpplab3.service;

import com.tpp.tpplab3.models.Shop;
import com.tpp.tpplab3.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;    
	

    public List<Shop> getAllShops() {
        return shopRepository.findAll(Sort.by(Sort.Order.asc("Id")));
    }

    public Optional<Shop> findShopById(int id) {
        return shopRepository.findById(id);
    }

    public void saveShop(Shop shop) {
        shopRepository.save(shop);
    }
		public class ShopHasProductsException extends RuntimeException {
			public ShopHasProductsException(String message) {
					super(message);
			}
	}

    public void updateShop(Shop updatedShop) {
        Shop existingShop = shopRepository.findById(updatedShop.getId())
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        // Оновлення властивостей магазину
        existingShop.setShopName(updatedShop.getShopName());
        existingShop.setCity(updatedShop.getCity());  // Передача міста, без перевірки на існування
        existingShop.setAddress(updatedShop.getAddress());
        existingShop.setOpeningYear(updatedShop.getOpeningYear());

        shopRepository.save(existingShop);
    }

    public void deleteShop(int id) {
			shopRepository.deleteById(id);
	}

}
