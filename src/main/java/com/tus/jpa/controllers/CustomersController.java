package com.tus.jpa.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Orders;
import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.OrdersRepository;
import com.tus.jpa.repositories.UserRepository;
import com.tus.jpa.service.CustomerService;

@RestController
@RequestMapping("/serviceLayer")
public class CustomersController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private OrdersRepository ordersRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/customers")
	public List<Users> getAllCustomers() {
		return customerService.getAllCustomers();
	}
	
	@GetMapping("/customers/{id}")
	public Users getCustomerById(@PathVariable Long id) {
		return customerService.getCustomerById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found!!!"));
	}
	@PostMapping("/register")
	public ResponseEntity createUser(@Valid @RequestBody Users user) {
	    if (userRepo.findByLogin(user.getLogin()) != null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user already exists!!");
	    } else {
	    	user.setPassword(passwordEncoder.encode(user.getPassword())); // Set password before encoding
	    	user.setEmail(user.getEmail());
	    	user.setRole(user.getRole());
	        Users savedUser = customerService.saveUser(user);
	        if (savedUser.getRole().equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.OK).body("Admin registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Customer registered successfully");
            }
	    }	
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		customerService.deleteUser(id);
	}
	
	@GetMapping("/orders/{orderId}")
	public Orders getOrderById(@PathVariable Long orderId) {
		return ordersRepo.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found!!!"));
	}
	
	@GetMapping("/{customerId}/orders")
	public List<Orders> getAllOrdersByCustomerId(@PathVariable Long customerId) {
		return customerService.getAllOrdersByCustomerId(customerId);
	}
	
	@PostMapping("/{customerId}/newOrder")
	public Orders createOrder(@PathVariable Long customerId, @RequestBody Orders order) {
		Users user = customerService.getCustomerById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found!!!"));
		order.setCustomer(user);
		return customerService.saveOrder(order);
	}
	
	@PutMapping("/orders/{orderId}")
	public Orders updateOrder(@PathVariable Long orderId, @RequestBody Orders updatedOrder) {
		Orders existingOrder = ordersRepo.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found!!!"));
		existingOrder.setWine(updatedOrder.getWine());
		existingOrder.setQuantity(updatedOrder.getQuantity());
		return customerService.saveOrder(existingOrder);
	}
	
	@DeleteMapping("/orders/{orderId}")
	public void deleteOrder(@PathVariable Long orderId) {
		customerService.deleteOrder(orderId);
	}
}
