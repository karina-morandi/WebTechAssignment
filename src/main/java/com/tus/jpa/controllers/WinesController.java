package com.tus.jpa.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.exceptions.WineValidationException;
import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.ResourceNotFoundException;
import com.tus.jpa.exceptions.WineException;
import com.tus.jpa.repositories.WineRepository;
import com.tus.jpa.wine_validator.ErrorMessage;
import com.tus.jpa.wine_validator.ErrorMessages;
import com.tus.jpa.wine_validator.wineValidator;

@RestController
@PreAuthorize("hasRole('ADMIN')")
public class WinesController {

	@Autowired
	WineRepository wineRepository;
	
	@Autowired
	wineValidator wineValidator;
	
	@Autowired
    private ServletContext servletContext;
	
    private static final String UPLOAD_DIR = "static/images/";
	
    @GetMapping("/wines")
    public ResponseEntity<List<Wines>> getAllWines() {
        List<Wines> wines = wineRepository.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(wines, headers, HttpStatus.OK);
    }
    
//	@GetMapping("/wines")
//	public Iterable<Wines> getAllWines(){
//		return wineRepository.findAll();
//	}

	@PostMapping("/wines/createNewWine")
	public ResponseEntity<?> createWine(@Valid @RequestParam("name") String name, @RequestParam("grapes") String grapes,@RequestParam("country") String country, @RequestParam("year") int year, @RequestParam("color") String color, @RequestParam("winery") String winery, @RequestParam("region") String region, @RequestPart("pictureFile") MultipartFile pictureFile) throws WineValidationException, IOException {
	    try {	
	        List<Wines> existingWines = wineRepository.findByName(name);
	        if (!existingWines.isEmpty()) {
	            throw new WineValidationException(ErrorMessages.ALREADY_EXISTS.getMsg());
	        }
	        Wines wine = new Wines();
	        wine.setName(name);
	        wine.setColor(color);
	        wine.setCountry(country);
	        wine.setGrapes(grapes);
	        wine.setRegion(region);
	        wine.setWinery(winery);
	        wine.setYear(year);

	        String rootDirectory = servletContext.getRealPath(UPLOAD_DIR);
	        String picturePath = savePicture(pictureFile);
	        wine.setPicture(picturePath);

	        wineValidator.validateWine(wine);
	        Wines savedWine = wineRepository.save(wine);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedWine);
	    } catch(WineValidationException  e) {
	        throw e;
	    }
	}
	
	@PostMapping("/uploadImage")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
	    try {
	        String picturePath = savePicture(file);
	        return ResponseEntity.ok(picturePath);
	    } catch (WineValidationException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
	    }
	}
	
//	private String savePicture(MultipartFile pictureFile) throws WineValidationException {
//	    // Get the filename
//	    String fileName = StringUtils.cleanPath(pictureFile.getOriginalFilename());
//
//	    // Create the directory if it doesn't exist
//	    String rootDirectory = servletContext.getRealPath(UPLOAD_DIR);
//	    File directory = new File(rootDirectory);
//	    if (!directory.exists()) {
//	        directory.mkdirs();
//	    }
//
//	    // Create the path where the file will be saved
//	    Path uploadPath = Paths.get(rootDirectory, fileName);
//
//	    // Copy the file to the upload path
//	    try {
//	        Files.copy(pictureFile.getInputStream(), uploadPath);
//	    } catch (IOException e) {
//	        // Handle the case where the image cannot be saved
//	        throw new WineValidationException("Failed to save picture: " + e.getMessage());
//	    }
//
//	    // Return the path where the file was saved
//	    return fileName;
//	}

	private String savePicture(MultipartFile pictureFile) throws WineValidationException {
	    try {
	        String fileName = StringUtils.cleanPath(pictureFile.getOriginalFilename());
	        Path uploadPath = Paths.get("src", "main", "resources", "static", "images", fileName);
	        Files.copy(pictureFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
	        return fileName;
	    } catch (IOException e) {
	        throw new WineValidationException("Failed to save picture: " + e.getMessage());
	    }
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
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting wine(s) with name " + name + ".");
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
	public ResponseEntity<?> updateWine(@PathVariable Long id, @Valid @RequestBody Wines updatedWine) {
	    try {
	        // Find the existing wine by ID
	        Optional<Wines> existingWineOptional = wineRepository.findById(id);
	        
	        if (!existingWineOptional.isPresent()) {
	            return ResponseEntity.notFound().build();
	        }
	        
	        // Update the wine details
	        Wines existingWine = existingWineOptional.get();
	        existingWine.setName(updatedWine.getName());
	        existingWine.setGrapes(updatedWine.getGrapes());
	        existingWine.setCountry(updatedWine.getCountry());
	        existingWine.setYear(updatedWine.getYear());
	        existingWine.setColor(updatedWine.getColor());
	        existingWine.setWinery(updatedWine.getWinery());
	        existingWine.setRegion(updatedWine.getRegion());
	        
	        // Save the updated wine
	        Wines savedWine = wineRepository.save(existingWine);
	        
	        return ResponseEntity.ok(savedWine);
	    } catch (Exception e) {
	        // Handle any exceptions and return an error response
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating wine: " + e.getMessage());
	    }
	}

	
//	@PutMapping("/wines/{id}")
//	public ResponseEntity<?> updateWine(Long id, @Valid @RequestBody Wines wine) throws WineValidationException, ResourceNotFoundException {
//	    // Validate the wine object
//	    wineValidator.validateWine(wine); // This may throw WineValidationException if validation fails
//	    
//	    // Update the wine in the repository
//	    Optional<Wines> existingWine = wineRepository.findById(id);
//	    if (!existingWine.isPresent()) {
//	        throw new ResourceNotFoundException("Wine not found with id: " + id);
//	    }
//
//	    Wines updatedWine = existingWine.get();
//	    updatedWine.setName(wine.getName());
//	    updatedWine.setGrapes(wine.getGrapes());
//	    updatedWine.setCountry(wine.getCountry());
//	    updatedWine.setYear(wine.getYear());
//	    updatedWine.setColor(wine.getColor());
//	    updatedWine.setWinery(wine.getWinery());
//	    updatedWine.setRegion(wine.getRegion());
//
//	    wineRepository.save(updatedWine);
//
//	    // Return response entity
//	    return ResponseEntity.ok(updatedWine);
//	}
	
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