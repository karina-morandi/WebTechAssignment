package com.tus.jpa.controllers;

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
	
	@GetMapping("/wines/{id}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable(value = "id") Long wineId) {
        Optional<Wines> wine = wineRepository.findById(wineId);
        if (wine.isPresent()) {
            Double averageRating = wine.get().getAverageRating();
            if (averageRating != null) {
                return ResponseEntity.ok(averageRating);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/wines/{id}/rating")
    public ResponseEntity<String> submitRating(@PathVariable(value = "id") Long wineId, @RequestBody Map<String, Integer> ratingMap) {
        // Extract rating from request body
    	Double rating = (double)ratingMap.get("rating");

        // Update the wine with the new rating
        wineRepository.updateRating(wineId, rating);

        // Calculate the new average rating
        Double newAverageRating = wineRepository.calculateAverageRating(wineId);
        newAverageRating = (newAverageRating == null) ? (double) rating : (newAverageRating * (wineRepository.count() - 1) + rating.doubleValue()) / wineRepository.count();

        // Update the wine with the new average rating
        wineRepository.updateAverageRating(wineId, newAverageRating);

        // Return success response
        return ResponseEntity.ok("Rating submitted successfully");
    }
}


