package com.tus.jpa;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tus.jpa.controllers.CustomersController;
import com.tus.jpa.repositories.UserRepository;
import com.tus.jpa.service.CustomerService;
import com.tus.jpa.dto.Users;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepo;

    private CustomersController registrationController;
    private CustomerService customerService;
    private final String LOGIN = "admin";
    private final String EMAIL = "admin@root.ie";
    private final String PASSWORD = "admin";
    private final String ROLE = "ADMIN";
    
    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        customerService = mock(CustomerService.class); // Mock CustomerService
        registrationController = new CustomersController();
        registrationController.setUserRepo(userRepo);
        registrationController.setPasswordEncoder(passwordEncoder);
        registrationController.setCustomerService(customerService); // Inject the mocked CustomerService
    }

    @Test
    void createUser_UserAlreadyExists_ReturnsBadRequest() {
        when(userRepo.findByLogin(anyString())).thenReturn(new Users(LOGIN, EMAIL, PASSWORD, ROLE));
        Users admin = new Users("admin", "admin@root.ie", "admin", "ADMIN");
        ResponseEntity<?> response = registrationController.createUser(admin);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("This user already exists!!", response.getBody());
    }
    
    @Test
    void createUser_NewUser_ReturnsOk() {
        when(userRepo.findByLogin(anyString())).thenReturn(null);
        Users admin = new Users("username", "email@example.com", "password", "ADMIN");
        when(customerService.saveUser(any(Users.class))).thenReturn(admin); // Mocking the behavior of saveUser
        ResponseEntity<?> response = registrationController.createUser(admin);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    


    
    
    
    
    
    
    
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserRole_Admin() throws Exception {
        mockMvc.perform(get("/user/role"))
               .andExpect(status().isOk())
               .andExpect(content().string("[ROLE_ADMIN]"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void testGetUserRole_Customer() throws Exception {
        mockMvc.perform(get("/user/role"))
               .andExpect(status().isOk())
               .andExpect(content().string("[ROLE_CUSTOMER]"));
    }    
}
