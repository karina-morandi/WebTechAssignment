package com.tus.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tus.jpa.controllers.CustomersController;
import com.tus.jpa.repositories.UserRepository;
import com.tus.jpa.service.CustomerService;
import com.tus.jpa.dto.Users;

class RegistrationControllerTest {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
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
    


    
    
    
    
    
    
    
    
    
    
    
}
