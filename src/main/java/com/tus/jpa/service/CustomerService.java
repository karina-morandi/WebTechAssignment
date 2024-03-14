package com.tus.jpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tus.jpa.dto.Orders;
import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.OrdersRepository;
import com.tus.jpa.repositories.UserRepository;

@Service
public class CustomerService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private OrdersRepository ordersRepo;
	
	public List<Users> getAllCustomers() {
		return userRepo.findAll();
	}
	
	public Optional<Users> getCustomerById(Long id) {
		return userRepo.findById(id);
	}

	public Users saveUser(Users user) {
		return userRepo.save(user);
	}
	
	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}
	
	public List<Orders> getAllOrdersByCustomerId(Long customerId) {
		Optional<Users> optionalUser = userRepo.findById(customerId);
		if(optionalUser.isPresent()) {
			return optionalUser.get().getOrders();
		} else {
			throw new RuntimeException("Customer not found!!");
		}
	}
	
	public Orders saveOrder(Orders order) {
		return ordersRepo.save(order);
	}
	
	public void deleteOrder(Long orderId) {
		ordersRepo.deleteById(orderId);
	}
}