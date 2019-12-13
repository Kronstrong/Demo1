package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class PurchaseItem implements Serializable {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "name")
	private String firstName;
	
	@Column(name = "lastname")
	private String lastName;
	
	private int age;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "purchase_item")
	private ShopItem shopItem;
	
	private int count;
	
	private double amount;
	
	@Column(name = "purchase_date")
	@Temporal(TemporalType.DATE)
	private Date date;
	
	public long getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public ShopItem getShopItem() {
		return shopItem;
	}
	
	public void setShopItem(ShopItem shopItem) {
		this.shopItem = shopItem;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
}
