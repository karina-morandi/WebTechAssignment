package com.tus.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.multipart.MultipartFile;

import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.WineValidationException;
import com.tus.jpa.repositories.WineRepository;
import com.tus.jpa.wine_validator.wineValidator;

@ExtendWith(MockitoExtension.class)
public class WineValidatorTest {

	@Mock
    private WineRepository wineRepository;

    @InjectMocks
    private wineValidator wineValidator;

    @BeforeEach
    void setUp() {
        wineRepository = mock(WineRepository.class);
        wineValidator = new wineValidator();
        wineValidator.setWineRepo(wineRepository);
    }

    @Test
    void testValidateWine_WithEmptyName_ShouldThrowException() {
        Wines wine = createWine("", "Country", 2024, "Color", "Grapes", "Winery", "Region", "picture.jpg");
        assertThrows(WineValidationException.class, () -> wineValidator.validateWine(wine));
    }

    @Test
    void testValidateWine_WithEmptyCountry_ShouldThrowException() {
        Wines wine = createWine("Name", "", 2024, "Color", "Grapes", "Winery", "Region", "picture.jpg");
        assertThrows(WineValidationException.class, () -> wineValidator.validateWine(wine));
    }

    @Test
    void testValidateWine_WithEmptyColor_ShouldThrowException() {
        Wines wine = createWine("Name", "Country", 2024, "", "Grapes", "Winery", "Region", "picture.jpg");
        assertThrows(WineValidationException.class, () -> wineValidator.validateWine(wine));
    }

    @Test
    void testValidateWine_WithEmptyGrapes_ShouldThrowException() {
        Wines wine = createWine("Name", "Country", 2024, "Color", "", "Winery", "Region", "picture.jpg");
        assertThrows(WineValidationException.class, () -> wineValidator.validateWine(wine));
    }

    @Test
    void testValidateWine_WithEmptyWinery_ShouldThrowException() {
        Wines wine = createWine("Name", "Country", 2024, "Color", "Grapes", "", "Region", "picture.jpg");
        assertThrows(WineValidationException.class, () -> wineValidator.validateWine(wine));
    }

    @Test
    void testValidateWine_WithEmptyRegion_ShouldThrowException() {
        Wines wine = createWine("Name", "Country", 2024, "Color", "Grapes", "Winery", "", "picture.jpg");
        assertThrows(WineValidationException.class, () -> wineValidator.validateWine(wine));
    }

    @Test
    void testValidateWine_WithEmptyPicture_ShouldThrowException() {
        Wines wine = createWine("Name", "Country", 2024, "Color", "Grapes", "Winery", "Region", "");
        assertThrows(WineValidationException.class, () -> wineValidator.validateWine(wine));
    }
    
    
    @Test
    void checkExistingWineTest() throws WineValidationException {
        Wines wine = createWine("Name", "Country", 2024, "Color", "Grapes", "Winery", "Region", "picture.jpg");
        when(wineRepository.findByName(wine.getName())).thenReturn(List.of(wine));
        assertThrows(WineValidationException.class, () -> wineValidator.checkExistingWine(wine));
    }

    // Helper method to create a wine object with mock MultipartFile
    private Wines createWine(String name, String country, int year, String color, String grapes,
                             String winery, String region, String picture) {
        Wines wine = new Wines();
        wine.setName(name);
        wine.setCountry(country);
        wine.setYear(year);
        wine.setColor(color);
        wine.setGrapes(grapes);
        wine.setWinery(winery);
        wine.setRegion(region);
        wine.setPicture(picture);

        // Mock MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);
        // Set appropriate behavior for the mock if needed
        // For example, when(mockFile.getOriginalFilename()).thenReturn("picture.jpg");

        return wine;
    }
}
