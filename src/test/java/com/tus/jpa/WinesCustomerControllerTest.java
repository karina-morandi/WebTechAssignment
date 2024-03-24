package com.tus.jpa;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.tus.jpa.controllers.WinesCustomerController;
import com.tus.jpa.dto.Wines;
import com.tus.jpa.repositories.WineRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WinesCustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WineRepository wineRepository;

    @InjectMocks
    private WinesCustomerController controller;

    @Test
    void testGetAllWines() throws Exception {
        // Mock data
    	List<Wines> wines = buildWineList();
        when(wineRepository.findAll()).thenReturn(wines);

        mockMvc.perform(get("/customers/wines"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(wines.size()));
    }

    @Test
    void testGetWineById() throws Exception {
    	MockMultipartFile pictureFile = new MockMultipartFile(
		        "pictureFile", // name of the file parameter in the controller method
		        "test.jpg",    // original filename
		        "image/jpeg",  // content type
		        "Test picture".getBytes() // content as byte array
		    );
	    Wines savedWine = buildWine();
	    savedWine.setId(1L);
        when(wineRepository.findById(1L)).thenReturn(Optional.of(savedWine));

        mockMvc.perform(get("/customers/wines/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/hal+json")))
                .andExpect(jsonPath("$.id").value(savedWine.getId()))
                .andExpect(jsonPath("$.name").value(savedWine.getName()));
    }

    @Test
    void testGetWineById_NotFound() throws Exception {
        when(wineRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/wines/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSubmitRating() throws Exception {
    	MockMultipartFile pictureFile = new MockMultipartFile(
		        "pictureFile", // name of the file parameter in the controller method
		        "test.jpg",    // original filename
		        "image/jpeg",  // content type
		        "Test picture".getBytes() // content as byte array
		    );
	    Wines savedWine = buildWine();
	    savedWine.setId(1L);
        when(wineRepository.findById(1L)).thenReturn(Optional.of(savedWine));

        mockMvc.perform(post("/customers/wines/1/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rating\": 4.5}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rating submitted successfully"));
    }

    @Test
    void testSubmitRating_WineNotFound() throws Exception {
        when(wineRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/customers/wines/1/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rating\": 4.5}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAverageRating() throws Exception {
        // Mock data
    	MockMultipartFile pictureFile = new MockMultipartFile(
		        "pictureFile", // name of the file parameter in the controller method
		        "test.jpg",    // original filename
		        "image/jpeg",  // content type
		        "Test picture".getBytes() // content as byte array
		    );
	    Wines savedWine = buildWine();
	    savedWine.setId(1L);
	    savedWine.setRatings(Arrays.asList(4.0, 5.0, 3.5)); // Ratings: [4.0, 5.0, 3.5]
        when(wineRepository.findById(1L)).thenReturn(Optional.of(savedWine));

        mockMvc.perform(get("/customers/wines/1/averageRating"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("4.166666666666667")); // Average of ratings
    }

    @Test
    void testGetAverageRating_NoRatings() throws Exception {
        // Mock data
    	MockMultipartFile pictureFile = new MockMultipartFile(
		        "pictureFile", // name of the file parameter in the controller method
		        "test.jpg",    // original filename
		        "image/jpeg",  // content type
		        "Test picture".getBytes() // content as byte array
		    );
	    Wines savedWine = buildWine();
	    savedWine.setId(1L);
        when(wineRepository.findById(1L)).thenReturn(Optional.of(savedWine));

        mockMvc.perform(get("/customers/wines/1/averageRating"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("0.0")); // Default value
    }

    @Test
    void testGetAverageRating_WineNotFound() throws Exception {
        when(wineRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/wines/1/averageRating"))
                .andExpect(status().isNotFound());
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
        wineTwo.setName("Wine2");
        List<Wines> wineList = new ArrayList<Wines>();
        wineList.add(wineOne);
        wineList.add(wineTwo);
        return wineList;
	}
    
    @Test
    void mainMethodShouldStartApplicationWithoutError() {
        // Simply call the main method and ensure no exceptions are thrown
        assertDoesNotThrow(() -> AssignmentApplication.main(new String[] {}));
    }
}
