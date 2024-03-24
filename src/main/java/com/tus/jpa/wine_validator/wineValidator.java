package com.tus.jpa.wine_validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
	
	public void setWineRepo(WineRepository wineRepo) {
        this.wineRepo = wineRepo;
    }
	
	private static final String UPLOAD_DIR = "src/main/resources/static/images/";

	
	public void validateWine(Wines wine) throws WineValidationException {
		this.wine = wine;
		checkEmptyFields(wine);
		checkExistingWine(wine);
	}
	

	private void checkEmptyFields(Wines wine) throws WineValidationException {
		if (wine.getName().isEmpty()) {
	        throw new WineValidationException("Name field is empty");
	    }
	    if (wine.getCountry().isEmpty()) {
	        throw new WineValidationException("Country field is empty");
	    }
	    
	    if (wine.getYear() == 0) {
	        throw new WineValidationException("Year field is empty");
	    }
	    if (wine.getColor().isEmpty()) {
	        throw new WineValidationException("Color field is empty");
	    }
	    if (wine.getGrapes().isEmpty()) {
	        throw new WineValidationException("Grapes field is empty");
	    }
	    if (wine.getWinery().isEmpty()) {
	        throw new WineValidationException("Winery field is empty");
	    }
	    if (wine.getRegion().isEmpty()) {
	        throw new WineValidationException("Region field is empty");
	    }
	    if (wine.getPicture().isEmpty()) {
	        throw new WineValidationException("Picture field is empty");
	    }
	    
//		if ((wine.getName().length() == 0) || (wine.getCountry().length() == 0) || (wine.getYear() == 0)|| (wine.getColor().length() == 0)
//				|| (wine.getGrapes().length() == 0)|| (wine.getWinery().length() == 0) || (wine.getRegion().length() == 0) || (wine.getPicture().isEmpty())) {
//	        throw new WineValidationException("One or more empty fields!");
//		}
	}
	
	 //if wine with name and year already exists
	public void checkExistingWine(Wines wine) throws WineValidationException {
		this.wine = wine;
		List<Wines> savedWine = wineRepo.findByName(wine.getName());
		if (savedWine.size() > 0){
			throw new WineValidationException(ErrorMessages.ALREADY_EXISTS.getMsg());
		}
	}
	
//	private String savePicture(MultipartFile pictureFile) throws WineValidationException {
//	    // Get the filename
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
//	        Files.copy(pictureFile.getInputStream(), uploadPath);
//	    } catch (IOException e) {
//	        // Handle the case where the image cannot be saved
//	        throw new WineValidationException("Failed to save picture: " + e.getMessage());
//	    }
//
//	    // Return the path where the file was saved
//	    return fileName;
//	}

}

