package com.tus.jpa.wine_validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.WineValidationException;
import com.tus.jpa.repositories.WineRepository;


@Component
public class wineValidator {
	Wines wine;
	
	@Autowired
	WineRepository wineRepo;
	
	public void validateWine(Wines wine) throws WineValidationException {
		this.wine = wine;
		checkEmptyFields(wine);
		checkForVintage(wine);
	}
	

	private void checkEmptyFields(Wines wine) throws WineValidationException {
		if ((wine.getName().length() == 0) || (wine.getCountry().length() == 0) || (wine.getYear() == 0)
				|| (wine.getGrapes().length() == 0)) {
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
}