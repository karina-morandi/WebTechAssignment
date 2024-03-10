package com.tus.jpa.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Wines;
import com.tus.jpa.repositories.WineRepository;
import com.tus.jpa.wine_validator.wineValidator;

@RestController
@RequestMapping("/customers")
public class WinesCustomerController {

	
	@Autowired
	WineRepository wineRepository;
	
	@Autowired
	wineValidator wineValidator;
	
	@GetMapping("/wines")
	public Iterable<Wines> getAllWines(){
		return wineRepository.findAll();
	}
	
	@GetMapping("/wines/{id}")
	public ResponseEntity<?> getWineById(@PathVariable(value = "id") Long wineId) {
	    Optional<Wines> wine = wineRepository.findById(wineId);
	    if (wine.isPresent())
	        return ResponseEntity.ok(wine.get());
	    else
	        return ResponseEntity.notFound().build();
	}
}

