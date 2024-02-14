package com.tus.jpa.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.ResourceNotFoundException;
import com.tus.jpa.repositories.WineRepository;

@RestController
public class AssignmentController {

	@Autowired
	private WineRepository wineRepository;
	
//	@GetMapping("/")
//    public String home() {
//        return "index"; // Return the name of the HTML file for the home page
//    }
	
	@GetMapping("/wines")
	public Iterable<Wines> getAllWines(){
		return wineRepository.findAll();
	}
	
	@GetMapping("/wines/{id}")
	public Optional<Wines> getWineById(@PathVariable(value = "id") Long wineId)throws ResourceNotFoundException{
		Optional<Wines> wine = wineRepository.findById(wineId);
		if(wine.isPresent())
			return wine;
		else
			throw new ResourceNotFoundException("No wine with id: "+wineId );
			
	}
	
	@PostMapping("/wines")
	public ResponseEntity createWine(@Valid @RequestBody Wines wine) {
		Wines savedWine=wineRepository.save(wine);
		return ResponseEntity.status(HttpStatus.OK).body(savedWine);
	}
	
	@PutMapping("wines/{id}")
	public ResponseEntity updateWine(@PathVariable("id") Long id, @RequestBody Wines wine) throws ResourceNotFoundException {
		Optional <Wines> savedWine = wineRepository.findById(id);
		if (savedWine.isPresent()) {
			wineRepository.save(wine);
			//just return 200 ok
			return ResponseEntity.status(HttpStatus.OK).body(wine);
		} else {
			throw new ResourceNotFoundException("No wine with id: " + id);
		}
	}
	
	@RequestMapping("/wines/name/{name}")
	public ResponseEntity<List<Wines>> getBookByName(@PathVariable("name") String name){
		List<Wines> winesByName = new ArrayList<>();
		//winesByName=wineRepo.findByName(name); this is exact match
		winesByName=wineRepository.findByNameContaining(name);
		if (winesByName.size()>0) {
			return new ResponseEntity(winesByName, HttpStatus.OK);
		}else {
			return new ResponseEntity(winesByName, HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping("/wines/{id}")
	public void deleteWineById(@PathVariable Long id) throws ResourceNotFoundException {
		Optional<Wines> wine = wineRepository.findById(id);
		if (wine.isPresent()) {
			Wines existingWine=wine.get();
			wineRepository.delete(existingWine);
		}else {
			throw new ResourceNotFoundException("No wine with id: " +id);
		}
	}
}
