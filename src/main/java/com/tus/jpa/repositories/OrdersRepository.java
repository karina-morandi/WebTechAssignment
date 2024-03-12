package com.tus.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.jpa.dto.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long>{
	
//	List<Orders> findByCustomerEmail(String customerEmail);
	Optional<Orders> findById(Long id);
}
