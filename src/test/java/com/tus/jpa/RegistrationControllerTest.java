//package com.tus.jpa;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.tus.jpa.controllers.RegistrationController;
//import com.tus.jpa.repositories.AdminRepository;
//import com.tus.jpa.dto.Admin;
//
//class RegistrationControllerTest {
//
//    private AdminRepository adminRepository;
//    private PasswordEncoder passwordEncoder;
//    private RegistrationController registrationController;
//
//    @BeforeEach
//    void setUp() {
//        adminRepository = mock(AdminRepository.class);
//        passwordEncoder = mock(PasswordEncoder.class);
//        registrationController = new RegistrationController(adminRepository, passwordEncoder);
//    }
//
//    @Test
//    void createUser_UserAlreadyExists_ReturnsBadRequest() {
//        when(adminRepository.findByLogin(anyString())).thenReturn(new Admin());
//        Admin admin = new Admin("username", "email@example.com", "password", "admin");
//        ResponseEntity<?> response = registrationController.createUser(admin);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("This user already exists!!", response.getBody());
//    }
//
//    @Test
//    void createUser_NewUser_ReturnsOk() {
//        when(adminRepository.findByLogin(anyString())).thenReturn(null);
//        when(adminRepository.save(any())).thenReturn(new Admin());
//        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
//        Admin admin = new Admin("username", "email@example.com", "password", "admin");
//        ResponseEntity<?> response = registrationController.createUser(admin);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }
//}
