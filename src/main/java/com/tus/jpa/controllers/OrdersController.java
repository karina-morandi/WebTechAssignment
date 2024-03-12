package com.tus.jpa.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Orders;
import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.ResourceNotFoundException;
import com.tus.jpa.repositories.OrdersRepository;
import com.tus.jpa.repositories.WineRepository;

@RestController
@RequestMapping("/cart")
public class OrdersController {
	
	private final OrdersRepository ordersRepo;
	private final WineRepository wineRepository;
	
	public OrdersController (OrdersRepository ordersRepo, WineRepository wineRepository) {
		this.ordersRepo = ordersRepo;
		this.wineRepository = wineRepository;
	}
	
	@GetMapping("/orders")
	public Iterable<Orders> getAllWines(){
		return ordersRepo.findAll();
	}
	
		
	@PostMapping("/newOrder/{wineId}")
	public ResponseEntity<?> newOrder(@PathVariable("wineId") Long wineId, @Valid @RequestBody Orders order) throws ResourceNotFoundException {
	    // Fetch wine details based on wineId
	    Wines wine = wineRepository.findById(wineId)
	            .orElseThrow(() -> new ResourceNotFoundException("Wine not found with id: " + wineId));

	    // Create a new order
	    Orders newOrder = new Orders();
	    newOrder.setWine(wine); // Set wine name as product
	    newOrder.setQuantity(order.getQuantity());
	    newOrder.setCustomer(order.getCustomer());
	    newOrder.setTotal(wine.getPrice() * order.getQuantity());

	    // Save the order
	    Orders savedOrder = ordersRepo.save(newOrder);
	    return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
	}
}