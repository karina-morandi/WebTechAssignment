package com.tus.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tus.jpa.controllers.LoginController;
import com.tus.jpa.dto.Admin;
import com.tus.jpa.repositories.AdminRepository;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginController loginController;

    @Test
    void testGetLogin() {
        ResponseEntity<?> responseEntity = loginController.getLogin();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetUser_UserExists() {
        when(adminRepository.findByLogin(anyString())).thenReturn(new Admin());
        boolean userExists = loginController.getUser("username");
        assertTrue(userExists);
    }

    @Test
    void testGetUser_UserNotExists() {
        when(adminRepository.findByLogin(anyString())).thenReturn(null);
        boolean userExists = loginController.getUser("username");
        assertFalse(userExists);
    }

    @Test
    void testAuthenticate_UserExistsAndPasswordCorrect() {
        when(adminRepository.findByLogin(anyString())).thenReturn(new Admin());
        when(passwordEncoder.matches(anyString(), eq(null))).thenReturn(true); // Update stubbing
        ResponseEntity<?> responseEntity = loginController.authenticate("username", "password");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Login successful", responseEntity.getBody());
    }
    
    @Test
    void testAuthenticate_UserDoesNotExist() {
        when(adminRepository.findByLogin(anyString())).thenReturn(null);
        ResponseEntity<?> responseEntity = loginController.authenticate("username", "password");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("This user does not exist!", responseEntity.getBody());
    }
    
    @Test
    void testAuthenticate_UserExistsAndPasswordIncorrect() {
        when(adminRepository.findByLogin(anyString())).thenReturn(new Admin());
        when(passwordEncoder.matches("password", null)).thenReturn(false);
        
        ResponseEntity<?> responseEntity = loginController.authenticate("username", "password");
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid username or password", responseEntity.getBody());
    }

}
