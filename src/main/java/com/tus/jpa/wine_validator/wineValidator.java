package com.tus.jpa.wine_validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.WineValidationException;
import com.tus.jpa.repositories.WineRepository;


@Component
public class wineValidator {
	Wines wine;
	
	@Autowired
	WineRepository wineRepo;
	
	private static final String UPLOAD_DIR = "src/main/resources/static/images/";

	
	public void validateWine(Wines wine) throws WineValidationException {
		this.wine = wine;
		checkEmptyFields(wine);
		checkForVintage(wine);
	}
	

	private void checkEmptyFields(Wines wine) throws WineValidationException {
		if ((wine.getName().length() == 0) || (wine.getCountry().length() == 0) || (wine.getYear() == 0)|| (wine.getColor().length() == 0)
				|| (wine.getGrapes().length() == 0)|| (wine.getWinery().length() == 0) || (wine.getRegion().length() == 0) || (wine.getPicture().isEmpty())) {
			throw new WineValidationException(ErrorMessages.EMPTY_FIELDS.getMsg());
		}
	}

	 //if wine with name and year already exists
	private void checkForVintage(Wines wine) throws WineValidationException {
		this.wine = wine;
		Optional<Wines> savedWine = wineRepo.findByNameAndYear(wine.getName(), wine.getYear());
		if (savedWine.isPresent()){
			throw new WineValidationException(ErrorMessages.ALREADY_EXISTS.getMsg());
		}
	}
	
	private String savePicture(MultipartFile pictureFile) throws WineValidationException {
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
	        // Handle the case where the image cannot be saved
	        throw new WineValidationException("Failed to save picture: " + e.getMessage());
	    }

	    // Return the path where the file was saved
	    return fileName;
	}

}

