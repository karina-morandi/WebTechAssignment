package com.tus.jpa.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.ResourceNotFoundException;
import com.tus.jpa.exceptions.WineException;
import com.tus.jpa.repositories.WineRepository;
import com.tus.jpa.wine_validator.ErrorMessage;
import com.tus.jpa.wine_validator.wineValidator;


@RestController
public class AssignmentController {

	@Autowired
	WineRepository wineRepository;
	
	@Autowired
	wineValidator wineValidator;
	
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
	
	@PostMapping("/wines/createNewWine")
	public ResponseEntity createWine(@Valid @RequestBody Wines wine) {
		try {	
			wineValidator.validateWine(wine);
			Wines savedWine=wineRepository.save(wine);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedWine);
			} catch(WineException e) {
				ErrorMessage errorMessage=new ErrorMessage(e.getMessage());
				return ResponseEntity.badRequest().body(errorMessage);
			}
		}
	
	@PutMapping("/wines/{id}")
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
	
//	@RequestMapping("/wines/name/{name}")
//	public ResponseEntity<List<Wines>> getBookByName(@PathVariable("name") String name){
//		List<Wines> winesByName = new ArrayList<>();
//		//winesByName=wineRepo.findByName(name); this is exact match
//		winesByName=wineRepository.findByNameContaining(name);
//		if (winesByName.size()>0) {
//			return new ResponseEntity(winesByName, HttpStatus.OK);
//		}else {
//			return new ResponseEntity(winesByName, HttpStatus.NO_CONTENT);
//		}
//	}
	
	@DeleteMapping("/wines/{name}")
	public ResponseEntity<String> deleteWineByName(@PathVariable("name") String name) {
		try {
	        List<Wines> wines = wineRepository.findByName(name);
	        if (wines.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No wine with name: " + name);
	        } else {
	            for (Wines wine : wines) {
	                wineRepository.delete(wine);
	            }
	            return ResponseEntity.ok().body("Wine(s) with name " + name + " deleted successfully.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting wine(s) with name " + name + ".");
	    }
	}
}