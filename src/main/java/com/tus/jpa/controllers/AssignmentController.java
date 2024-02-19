package com.tus.jpa.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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
	
	private static final String UPLOAD_DIR = "src/main/resources/static/images/";
	
//	@GetMapping("/")
//    public String home() {
//        return "index"; // Return the name of the HTML file for the home page
//    }
	
	@GetMapping("/wines")
	public Iterable<Wines> getAllWines(){
		return wineRepository.findAll();
	}
	
//	@GetMapping("/wines/{id}")
//	public Optional<Wines> getWineById(@PathVariable(value = "id") Long wineId)throws ResourceNotFoundException{
//		Optional<Wines> wine = wineRepository.findById(wineId);
//		if(wine.isPresent())
//			return wine;
//		else
//			throw new ResourceNotFoundException("No wine with id: "+wineId );
//			
//	}
	
	@PostMapping("/wines/createNewWine")
	public ResponseEntity<?> createWine(@Valid @RequestParam("name") String name, @RequestParam("grapes") String grapes,@RequestParam("country") String country, @RequestParam("year") int year, @RequestParam("color") String color, @RequestParam("winery") String winery, @RequestParam("region") String region, @RequestPart("pictureFile") MultipartFile pictureFile) {
//	public ResponseEntity createWine(@Valid @RequestBody Wines wine, @RequestPart("pictureFile") MultipartFile pictureFile) {
		try {	
			Wines wine = new Wines();
			wine.setName(name);
			wine.setColor(color);
			wine.setCountry(country);
			wine.setGrapes(grapes);
			wine.setRegion(region);
			wine.setWinery(winery);
			wine.setYear(year);
			
			String picturePath = savePicture(pictureFile);
	        wine.setPicture(picturePath); // Save the picture and set the path
			wineValidator.validateWine(wine);
			Wines savedWine=wineRepository.save(wine);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedWine);
		} catch(WineException e) {
			ErrorMessage errorMessage=new ErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	private String savePicture(MultipartFile pictureFile) {
		// Get the filename
	    String fileName = StringUtils.cleanPath(pictureFile.getOriginalFilename());
	    
	    // Create the directory if it doesn't exist
	    File directory = new File(UPLOAD_DIR);
	    if (!directory.exists()) {
	        directory.mkdirs();
	    }

	    // Create the path where the file will be saved
	    Path uploadPath = Paths.get(UPLOAD_DIR + fileName);

	    // Copy the file to the upload path
	    try {
			Files.copy(pictureFile.getInputStream(), uploadPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // Return the path where the file was saved
	    return fileName;
	}
	
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
	
	@GetMapping("/wines/{id}")
	public ResponseEntity<?> getWineById(@PathVariable(value = "id") Long wineId) {
	    Optional<Wines> wine = wineRepository.findById(wineId);
	    if (wine.isPresent())
	        return ResponseEntity.ok(wine.get());
	    else
	        return ResponseEntity.notFound().build();
	}

	@PutMapping("/wines/{id}")
	public ResponseEntity<?> updateWine(
	    @PathVariable("id") Long id,
	    @RequestParam(value = "name", required = false) String name,
	    @RequestParam(value = "grapes", required = false) String grapes,
	    @RequestParam(value = "country", required = false) String country,
	    @RequestParam(value = "year", required = false) Integer year,
	    @RequestParam(value = "color", required = false) String color,
	    @RequestParam(value = "winery", required = false) String winery,
	    @RequestParam(value = "region", required = false) String region,
	    @RequestParam(value = "pictureFile", required = false) MultipartFile pictureFile
	) throws ResourceNotFoundException {
	    Optional<Wines> savedWine = wineRepository.findById(id);
	    if (savedWine.isPresent()) {
	        Wines existingWine = savedWine.get();
	        if (name != null) existingWine.setName(name);
	        if (grapes != null) existingWine.setGrapes(grapes);
	        if (country != null) existingWine.setCountry(country);
	        if (year != null) existingWine.setYear(year);
	        if (color != null) existingWine.setColor(color);
	        if (winery != null) existingWine.setWinery(winery);
	        if (region != null) existingWine.setRegion(region);
	        if (pictureFile != null) existingWine.setPicture(savePicture(pictureFile));
	        wineRepository.save(existingWine);
	        return ResponseEntity.ok(existingWine);
	    } else {
	        throw new ResourceNotFoundException("No wine with id: " + id);
	    }
	}


	@RequestMapping("/wines/name/{name}")
	public ResponseEntity<List<Wines>> getWineByName(@PathVariable("name") String name){
		List<Wines> winesByName = new ArrayList<>();
		//winesByName=wineRepo.findByName(name); this is exact match
		winesByName=wineRepository.findByNameContaining(name);
		if (winesByName.size()>0) {
			return new ResponseEntity(winesByName, HttpStatus.OK);
		}else {
			return new ResponseEntity(winesByName, HttpStatus.NO_CONTENT);
		}
	}
}