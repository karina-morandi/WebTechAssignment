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

import org.springframework.hateoas.CollectionModel; 
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.tus.jpa.dto.Orders;
import com.tus.jpa.dto.Users;
import com.tus.jpa.dto.Wines;
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
	
	@Autowired
	public void setUserRepo(UserRepository userRepo) {
	    this.userRepo = userRepo;
	}
	
	@Autowired
	public void setOrdersRepo(OrdersRepository ordersRepo) {
	    this.ordersRepo = ordersRepo;
	}
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
	    this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	public void setCustomerService(CustomerService customerService) {
	    this.customerService = customerService;
	}
	
	@GetMapping("/customers")
	public List<Users> getAllCustomers() {
		return customerService.getAllCustomers();
	}
	
	@GetMapping("/customers/{id}")
	public EntityModel<Users> getCustomerById(@PathVariable Long id) {
		Users user = customerService.getCustomerById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found!!!"));
		
		return EntityModel.of(user,
			WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomersController.class).getCustomerById(id)).withSelfRel(),
			linkTo(methodOn(CustomersController.class).getAllCustomers()).withRel("customers"));
	}
	
//	@GetMapping("/customers/username/{login}")
//	public Long getUserByName(@PathVariable("login") String login){
//		Users foundUser=userRepo.findByLogin(login);
//		Long id = foundUser.getId();
//		return id;
//	}
	
	
	@GetMapping("/customers/username/{login}")
	public Long getUserByName(@PathVariable("login") String login){
	    Users foundUser = userRepo.findByLogin(login);
	    if (foundUser != null) {
	        return foundUser.getId();
	    } else {
	        // Handle the case where no user is found with the given login
	        // For example, you can throw an exception or return a specific value
	        throw new RuntimeException("User with login " + login + " not found");
	    }
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
	
//	@GetMapping("/{customerId}/orders")
//	public CollectionModel<Orders> getAllOrdersByCustomerId(@PathVariable Long customerId) {
//		List<Orders> orders = customerService.getAllOrdersByCustomerId(customerId);
//		for (Orders order : orders) {
//			Link selfLink = linkTo(methodOn(CustomersController.class).getOrderById(order.getId())).withSelfRel();
//			order.add(selfLink);
//		}
//		Link customerLink = linkTo(methodOn(CustomersController.class).getCustomerById(customerId)).withRel("customer");
//		return CollectionModel.of(orders, customerLink);
//	}
	
	
	@GetMapping("/{customerId}/orders")
	public CollectionModel<Orders> getAllOrdersByCustomerId(@PathVariable Long customerId) {
	    List<Orders> orders = customerService.getAllOrdersByCustomerId(customerId);
	    for (Orders order : orders) {
	        // Link selfLink = linkTo(methodOn(CustomersController.class).getOrderById(order.getId())).withSelfRel();
	        // order.add(selfLink); // Remove these lines
	    }
	    Link selfLink = linkTo(methodOn(CustomersController.class).getAllOrdersByCustomerId(customerId)).withSelfRel();
	    Link customerLink = linkTo(methodOn(CustomersController.class).getCustomerById(customerId)).withRel("customer");
	    return CollectionModel.of(orders, selfLink, customerLink); // Add selfLink here
	}
//	String user=request.getUserPrincipal().getName();

	
	@PostMapping("/{customerId}/newOrder")
	public Orders createOrder(@PathVariable Long customerId, @RequestBody Orders order) {
		Users user = customerService.getCustomerById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found!!!"));
		order.setCustomer(user);
	    Long wineId = order.getWine().getId(); // Extract the wineId from the request body
	    order.setWineId(wineId);
	    order.calculateTotal(); // Calculate the total before saving the order
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
	
	@GetMapping("/{customerId}/orders-link")
	public Link getOrdersLinkForCustomer(@PathVariable Long customerId) {
	Link ordersLink = linkTo(methodOn(CustomersController.class).getAllOrdersByCustomerId(customerId)).withRel("orders");
	return ordersLink;
	}
}
