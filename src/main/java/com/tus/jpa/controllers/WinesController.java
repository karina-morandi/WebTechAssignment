package com.tus.jpa.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.exceptions.WineValidationException;
import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.WineException;
import com.tus.jpa.repositories.WineRepository;
import com.tus.jpa.wine_validator.ErrorMessage;
import com.tus.jpa.wine_validator.ErrorMessages;
import com.tus.jpa.wine_validator.wineValidator;

@RestController
public class WinesController {

	@Autowired
	WineRepository wineRepository;
	
	@Autowired
	wineValidator wineValidator;
	
	private static final String UPLOAD_DIR = "src/main/resources/static/images/";
	
	@GetMapping("/wines")
	public Iterable<Wines> getAllWines(){
		return wineRepository.findAll();
	}

	@PostMapping("/wines/createNewWine")
	public ResponseEntity<?> createWine(@Valid @RequestParam("name") String name, @RequestParam("grapes") String grapes,@RequestParam("country") String country, @RequestParam("year") int year, @RequestParam("color") String color, @RequestParam("winery") String winery, @RequestParam("region") String region, @RequestPart("pictureFile") MultipartFile pictureFile) throws WineValidationException, IOException {
//	public ResponseEntity createWine(@Valid @RequestBody Wines wine, @RequestPart("pictureFile") MultipartFile pictureFile) {
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
			
			String picturePath = savePicture(pictureFile);
	        wine.setPicture(picturePath); // Save the picture and set the path
			wineValidator.validateWine(wine);
			Wines savedWine=wineRepository.save(wine);
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
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }

    private String savePicture(MultipartFile pictureFile) throws IOException {
        // Define the directory where you want to save the uploaded files
        String uploadDir = "src/main/resources/static/images/";

        // Create a unique file name to prevent overwriting existing files
        String fileName = UUID.randomUUID().toString() + "_" + pictureFile.getOriginalFilename();

        // Save the file to the upload directory
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(pictureFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the file path or URL where the file is stored
        return fileName;
    }	
	
//	private String savePicture(MultipartFile pictureFile) {
//		// Get the filename
//	    String fileName = StringUtils.cleanPath(pictureFile.getOriginalFilename());
//	    
//	    // Create the directory if it doesn't exist
//	    File directory = new File(UPLOAD_DIR);
//	    if (!directory.exists()) {
//	        directory.mkdirs();
//	    }
//
//	    // Create the path where the file will be saved
//	    Path uploadPath = Paths.get(UPLOAD_DIR + fileName);
//
//	    // Copy the file to the upload path
//	    try {
//			Files.copy(pictureFile.getInputStream(), uploadPath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	    // Return the path where the file was saved
//	    return fileName;
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
	public ResponseEntity<?> updateWine(@PathVariable("id") Long id, @RequestBody Wines updatedWine) {
	    try {
	        Wines existingWine = wineRepository.findById(id).orElseThrow(() -> new RuntimeException("Wine not found!"));
	        existingWine.setName(updatedWine.getName());
	        existingWine.setGrapes(updatedWine.getGrapes());
	        existingWine.setCountry(updatedWine.getCountry());
	        existingWine.setYear(updatedWine.getYear());
	        existingWine.setColor(updatedWine.getColor());
	        existingWine.setWinery(updatedWine.getWinery());
	        existingWine.setRegion(updatedWine.getRegion());
	     
	        Wines savedWine = wineRepository.save(existingWine);
	        return ResponseEntity.ok(savedWine);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating wine: " + e.getMessage());
	    }
	}
    
	  
//	        if (pictureFile != null && !pictureFile.isEmpty()) {
//	            existingWine.setPicture(savePicture(pictureFile));}
//	        Wines updatedWine = wineRepository.save(existingWine);
//	        System.out.print("Java OK: " + updatedWine);
//	        return ResponseEntity.ok(updatedWine);
//	    } else {
//	        throw new ResourceNotFoundException("No wine with id: " + id);
//	    }
	
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