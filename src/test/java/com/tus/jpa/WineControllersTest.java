package com.tus.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.tus.jpa.controllers.WinesController;
import com.tus.jpa.dto.Wines;
import com.tus.jpa.exceptions.WineValidationException;
import com.tus.jpa.repositories.WineRepository;
import com.tus.jpa.wine_validator.ErrorMessage;
import com.tus.jpa.wine_validator.wineValidator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
	public class WineControllersTest {
	
    @Mock
    private WineRepository wineRepo;

    @Mock
    private wineValidator wineValidator;

    @InjectMocks
    private WinesController winesCont;
//	private final WineRepository wineRepo = mock(WineRepository.class);
//	private final wineValidator wineValidator = mock(wineValidator.class);
//	private WinesController winesCont = mock(WinesController.class);;
	private List<Wines> wineList;
	static final String WINE_NAME = "Wine";
	
	@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize annotated mocks

        wineList = buildWineList(); // Initialize wineList with test data
    }

	@Test //Is this test valuable?
    void getAllWines() {
        when(wineRepo.findAll()).thenReturn(wineList);
        Iterable<Wines> returnedWines = winesCont.getAllWines();
        assertEquals(2, ((List<Wines>) returnedWines).size());
        assertEquals(WINE_NAME, ((List<Wines>) returnedWines).get(0).getName());
        assertEquals("Mayo", ((List<Wines>) returnedWines).get(1).getName());
    }
	
	@Test
	void createNewWineOK() throws WineValidationException, IOException {
	    // Prepare test data
	    String name = "Wine";
	    String grapes = "Malbec";
	    String country = "Australia";
	    int year = 2020;
	    String color = "Red";
	    String winery = "New Winery";
	    String region = "Henty";
	    MultipartFile pictureFile = mock(MultipartFile.class); // Mock MultipartFile
	    when(pictureFile.getInputStream()).thenReturn(mock(InputStream.class));
	    doNothing().when(wineValidator).validateWine(any(Wines.class));

	    // Mock the behavior of wineRepo.save
	    Wines savedWine = buildWine();
	    savedWine.setId(1L);
	    when(wineRepo.save(any(Wines.class))).thenReturn(savedWine);

	    // Call the createWine method with the prepared parameters
	    ResponseEntity<?> response = winesCont.createWine(name, grapes, country, year, color, winery, region, pictureFile);

	    // Assert the response
	    assertEquals(HttpStatus.CREATED, response.getStatusCode());
	    assertEquals(savedWine, response.getBody());
	}
	
	@Test
	void createNewWineWithEmptyFieldsThrowsException() throws WineValidationException, IOException {
	    // Prepare test data with empty fields
	    String name = ""; // Empty field
	    String grapes = "Malbec";
	    String country = "Australia";
	    int year = 2020;
	    String color = "Red";
	    String winery = "New Winery";
	    String region = "Henty";
	    MultipartFile pictureFile = mock(MultipartFile.class); // Mock MultipartFile
	    when(pictureFile.getInputStream()).thenReturn(mock(InputStream.class));

	    // Mock WineRepository behavior
	    when(wineRepo.findByName(anyString())).thenReturn(Collections.emptyList());
    
	    
	    doThrow(new WineValidationException("Name field is empty"))
	        .when(wineValidator).validateWine(any());

	    // Call the createWine method with the prepared parameters
	    WineValidationException exception = assertThrows(WineValidationException.class, () -> {
	        winesCont.createWine(name, grapes, country, year, color, winery, region, pictureFile);
	    });

	    // Use assertEquals to verify the message of the thrown exception
	    assertEquals("Name field is empty", exception.getMessage());
	}
	
	@Test
	public void createWineWithExistingName() throws IOException {
	    // Prepare test data
	    String name = "Wine";
	    String grapes = "Malbec";
	    String country = "Australia";
	    int year = 2020;
	    String color = "Red";
	    String winery = "New Winery";
	    String region = "Henty";
	    MultipartFile pictureFile = mock(MultipartFile.class);

	    // Mock WineRepository to return a wine with the same name
	    Wines existingWine = new Wines();
	    existingWine.setName(name);
	    when(wineRepo.findByName(name)).thenReturn(Collections.singletonList(existingWine));

	    // Call the createWine method
	    try {
	        winesCont.createWine(name, grapes, country, year, color, winery, region, pictureFile);
	        // If no exception is thrown, fail the test
	        fail("Expected WineValidationException");
	    } catch (WineValidationException e) {
	        // Verify that WineValidationException is thrown with the expected message
	        assertEquals("Wine with given name already exists", e.getMessage());
	    } catch (IOException e) {
	        // If another IOException occurs, fail the test
	        fail("Unexpected IOException: " + e.getMessage());
	    }
	}
	
	@Test 
    void getWineByIdFound() throws WineValidationException {
		Wines wine = buildWine();
		wine.setId((long) 1);
		Optional<Wines> wineOne = Optional.of(wine);
        when(wineRepo.findById((long) 1)).thenReturn(wineOne);
        ResponseEntity response	=winesCont.getWineById((long)1);
		assertEquals(HttpStatus.OK,response.getStatusCode());
		Wines returnedWine= (Wines) response.getBody();
		assertEquals(1,returnedWine.getId());
		assertEquals(WINE_NAME,returnedWine.getName());
		assertTrue(returnedWine.equals(wine));
       
    }  
	
	@Test 
    void getWineByIdNotFound() {
		when(wineRepo.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity response	=winesCont.getWineById(1L);
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
		assertNull(response.getBody());
    }  
	

//	@Test 
//	void deleteWineOK() throws WineValidationException, IOException {
//	    // Prepare test data for wine creation
//	    String name = "NEW Wine";
//	    String grapes = "Malbec";
//	    String country = "Australia";
//	    int year = 2020;
//	    String color = "Red";
//	    String winery = "New Winery";
//	    String region = "Henty";
//	    MultipartFile pictureFile = mock(MultipartFile.class);
//	    when(pictureFile.getInputStream()).thenReturn(mock(InputStream.class));
//
//	    // Call the createWine method to save the wine
//	    ResponseEntity<?> creationResponse = winesCont.createWine(name, grapes, country, year, color, winery, region, pictureFile);
//
//	    // Verify that wine was created successfully
//	    assertEquals(HttpStatus.CREATED, creationResponse.getStatusCode());
//	    Wines createdWine = (Wines) creationResponse.getBody();
//	    assertNotNull(createdWine);
//
//	    // Now, attempt to delete the wine
//	    ResponseEntity<String> deletionResponse = winesCont.deleteWineByName(name);
//
//	    // Assert the response status code
//	    assertEquals(HttpStatus.NO_CONTENT, deletionResponse.getStatusCode());
//
//	    // Verify that the wine was deleted by checking if it's no longer present in the repository
//	    List<Wines> foundWines = wineRepo.findByName(name);
//	    assertTrue(foundWines.isEmpty(), "The wine should have been deleted from the repository.");
//	}
	
	@Test
	void deleteWineOK() {
	    // Arrange
	    String wineName = "TestWine";
	    Wines wineToDelete = new Wines();
	    wineToDelete.setName(wineName);

	    // Mock the behavior of WineRepository
	    when(wineRepo.findByName(wineName)).thenReturn(Collections.singletonList(wineToDelete));
	    doNothing().when(wineRepo).delete(any(Wines.class));

	    // Act
	    ResponseEntity<String> responseEntity = winesCont.deleteWineByName(wineName);

	    // Assert
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    assertEquals("Wine(s) with name " + wineName + " deleted successfully.", responseEntity.getBody());

	    // Verify that the delete method of WineRepository is called
	    verify(wineRepo, times(1)).delete(wineToDelete);
	}
	
	Wines buildWine() {
		Wines wine = new Wines();
		wine.setName("Wine");
		wine.setColor("Red");
		wine.setCountry("Italy");
		wine.setGrapes("Merlot");
		wine.setPicture("Merlot.png");
		wine.setRegion("Lombardi");
		wine.setWinery("Winery");
		wine.setYear(2024);
		return wine;
	}
	
	List<Wines> buildWineList(){
		Wines wineOne = buildWine();
        wineOne.setId((long) 1);
        Wines wineTwo = buildWine();
        wineTwo.setId((long) 2);
        wineTwo.setName("Mayo");
        List<Wines> wineList = new ArrayList<Wines>();
        wineList.add(wineOne);
        wineList.add(wineTwo);
        return wineList;
	}

}