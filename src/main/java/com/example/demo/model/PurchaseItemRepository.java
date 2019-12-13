package com.example.demo.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
	List<PurchaseItem> findByDateAfter(Date date);
	
	@Query("SELECT COUNT(id) AS orderCount, firstName AS firstName, lastName AS lastName FROM PurchaseItem WHERE date >= :date GROUP BY firstName, lastName ORDER BY orderCount DESC")
	List<TopBuyerInfo> findTopBuyers(@Param("date") Date date, Pageable pageable);
	
	@Query("SELECT COUNT(p.id) AS orderCount, s.name AS name FROM PurchaseItem p INNER JOIN p.shopItem s WHERE p.date >= :date GROUP BY s.id ORDER BY orderCount DESC")
	List<TopProductInfo> findTopProduct(@Param("date") Date date, Pageable pageable);
	
	@Query("SELECT COUNT(p.id) AS orderCount, s.name AS name FROM PurchaseItem p INNER JOIN p.shopItem s WHERE p.age = :age GROUP BY s.id ORDER BY orderCount DESC")
	List<TopProductInfo> findTopProductByAge(@Param("age") int age, Pageable pageable);
	
	interface TopBuyerInfo {
		int getOrderCount();
		
		String getFirstName();
		
		String getLastName();
	}
	
	interface TopProductInfo {
		int getOrderCount();
		
		String getName();
	}
}
