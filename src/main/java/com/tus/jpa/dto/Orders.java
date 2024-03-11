package com.tus.jpa.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
	
@Entity
@Table(name="Orders")
public class Orders {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", unique = true)
    private Long id;
	
	@Column(nullable = false, length = 45)
	private String product;
	
	@Column(nullable = false, length = 45)
	private int quantity;

	@Column(nullable = false, length = 75)
	private String customerEmail;
	
	@Column(nullable = false, length = 75)
	private int total;

	public Orders() {
		
	}

	public Orders(String product, int quantity, String customerEmail, int total) {
		this.product = product;
		this.quantity = quantity;
		this.customerEmail = customerEmail;
		this.total = total;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Orders [id=" + id + ", product=" + product + ", quantity=" + quantity + ", customerEmail="
				+ customerEmail + ", total=" + total + "]";
	}
}
