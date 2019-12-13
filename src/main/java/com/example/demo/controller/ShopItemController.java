package com.example.demo.controller;

import com.example.demo.model.ShopItem;
import com.example.demo.model.ShopItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShopItemController {
	private ShopItemRepository shopItemRepository;
	
	public ShopItemController(ShopItemRepository shopItemRepository) {
		this.shopItemRepository = shopItemRepository;
	}
	
	@GetMapping("/shopItems")
	public List<ShopItem> getAll() {
		return shopItemRepository.findAll();
	}
}
