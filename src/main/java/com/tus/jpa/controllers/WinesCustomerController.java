package com.tus.jpa.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Wines;
import com.tus.jpa.repositories.WineRepository;

@RestController
@RequestMapping("/customers")
public class WinesCustomerController {

	
	@Autowired
	WineRepository wineRepository;
	
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
	
	@PostMapping("/wines/{id}/rating")
	public ResponseEntity<String> submitRating(@PathVariable(value = "id") Long wineId, @RequestBody Map<String, Double> ratingMap) {
	    // Extract rating from request body
	    Double ratingValue = ratingMap.get("rating");

	    // Get the wine
	    Optional<Wines> optionalWine = wineRepository.findById(wineId);
	    if (optionalWine.isPresent()) {
	        Wines wine = optionalWine.get();
	        
	        // Append the new rating to the list of ratings
	        if (wine.getRatings() == null) {
	            wine.setRatings(new ArrayList<>());
	        }
	        wine.getRatings().add(ratingValue);
	        
	        // Update the wine
	        wineRepository.save(wine);
	        
	        return ResponseEntity.ok("Rating submitted successfully");
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	@GetMapping("/wines/{id}/averageRating")
	public ResponseEntity<Double> getAverageRating(@PathVariable(value = "id") Long wineId) {
	    Optional<Wines> optionalWine = wineRepository.findById(wineId);
	    if (optionalWine.isPresent()) {
	        Wines wine = optionalWine.get();
	        
	        // Calculate average rating
	        List<Double> ratings = wine.getRatings();
	        if (ratings != null && !ratings.isEmpty()) {
	            double sum = ratings.stream().mapToDouble(Double::doubleValue).sum();
	            double averageRating = sum / ratings.size();
	            return ResponseEntity.ok(averageRating);
	        } else {
	            return ResponseEntity.ok(0.0); // Or return some default value
	        }
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@GetMapping("/averageRatings")
	public ResponseEntity<Map<Long, Double>> getAverageRatingsForAllWines() {
	    List<Wines> wines = wineRepository.findAll();
	    Map<Long, Double> averageRatingsMap = new HashMap<>();

	    // Calculate average rating for each wine
	    for (Wines wine : wines) {
	        List<Double> ratings = wine.getRatings();
	        if (ratings != null && !ratings.isEmpty()) {
	            double sum = ratings.stream().mapToDouble(Double::doubleValue).sum();
	            double averageRating = sum / ratings.size();
	            averageRatingsMap.put(wine.getId(), averageRating);
	        } else {
	            // If no ratings are available, set the average rating to 0.0
	            averageRatingsMap.put(wine.getId(), 0.0);
	        }
	    }

	    return ResponseEntity.ok(averageRatingsMap);
	}
}
