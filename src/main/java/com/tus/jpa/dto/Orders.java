package com.tus.jpa.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;
	
@Entity
@Table(name="Orders")
public class Orders extends RepresentationModel<Orders>{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", unique = true)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "wine_id")
	private Wines wine;
	
	@Column(nullable = false, length = 45)
	private int quantity;
 
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Users customer;
	
	@Column(nullable = false)
	private double total;

	public Orders() {
		
	}

	public Orders(Long id, Wines wine, int quantity) {
		this.id = id;
		this.wine = wine;
		this.quantity = quantity;
	}
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Wines getWine() {
		return wine;
	}

	public void setWine(Wines wine) {
		this.wine = wine;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Users getCustomer() {
		return customer;
	}

	public void setCustomer(Users customer) {
		this.customer = customer;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public void calculateTotal() {
	    if (wine != null && getQuantity() > 0) {
	        double winePrice = wine.getPrice();
	        this.total = winePrice * getQuantity();
	    } else {
	        this.total = 0;
	    }
	}
	
	public void setWineId(Long id) {
		if (wine == null) {
	        wine = new Wines(); // Create a new Wines object if it's null
	    }
	    wine.setId(id); // Set the id of the Wines object
	}
	
	@Override
	public String toString() {
		return "Orders [id=" + id + ", wine=" + wine + ", quantity=" + quantity + ", customer="
				+ customer + ", total=" + total + "]";
	}
}
