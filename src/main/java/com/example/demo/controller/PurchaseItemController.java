package com.example.demo.controller;

import com.example.demo.model.PurchaseItem;
import com.example.demo.model.PurchaseItemRepository;
import com.example.demo.model.ShopItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@RestController
public class PurchaseItemController {
	private PurchaseItemRepository purchaseItemRepository;
	
	public PurchaseItemController(PurchaseItemRepository purchaseItemRepository) {
		this.purchaseItemRepository = purchaseItemRepository;
	}
	
	@PostMapping("/purchaseItems")
	@Transactional
	public String createOrUpdate(@RequestBody PurchaseItem item) {
		purchaseItemRepository.save(item);
		return "OK";
	}
	
	@DeleteMapping("/purchaseItems/{id}")
	public String delete(@PathVariable long id) {
		purchaseItemRepository.deleteById(id);
		return "OK";
	}
	
	@GetMapping("/purchaseItems")
	public PurchaseItems getAll() {
		return new PurchaseItems(purchaseItemRepository.findAll());
	}
	
	@GetMapping("/purchaseItems/lastWeek")
	public PurchaseItems getLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		return new PurchaseItems(purchaseItemRepository.findByDateAfter(calendar.getTime()));
	}
	
	@GetMapping("/purchaseItems/topBuyer")
	public PurchaseItemRepository.TopBuyerInfo getTopBuyer() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -180);
		return purchaseItemRepository.findTopBuyers(calendar.getTime(), PageRequest.of(0, 1)).get(0);
	}
	
	@GetMapping("/purchaseItems/topProduct")
	public PurchaseItemRepository.TopProductInfo getTopProduct() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return purchaseItemRepository.findTopProduct(calendar.getTime(), PageRequest.of(0, 1)).get(0);
	}
	
	@GetMapping("/purchaseItems/topProductByAge")
	public PurchaseItemRepository.TopProductInfo getTopProductByAge(@RequestParam int age) {
		return purchaseItemRepository.findTopProductByAge(age, PageRequest.of(0, 1)).get(0);
	}
	
	public static class PurchaseItems {
		private List<PurchaseItem> data;
		
		public PurchaseItems(List<PurchaseItem> data) {
			this.data = data;
		}
		
		public List<PurchaseItem> getData() {
			return data;
		}
	}
}
