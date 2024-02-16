package com.tus.jpa.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Admin;
import com.tus.jpa.repositories.AdminRepository;

@RestController
public class LoginController {

    private AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    
    public LoginController(AdminRepository adminRepo, PasswordEncoder passwordEncoder) {
		this.adminRepo = adminRepo;
		this.passwordEncoder = passwordEncoder;
	}
    
    @GetMapping("/login")
    public ResponseEntity<?> getLogin() {
        // Return whatever data is appropriate for a GET request to /login
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/{login}")
	public boolean getUser(@PathVariable("login") String login){
		Admin foundUser = adminRepo.findByLogin(login);
		if(foundUser != null) {
			return true;
		}else {
			return false;
		}
	}
    
    @PostMapping("/login")  
    public ResponseEntity<?> authenticate(@Valid @RequestParam String login, @RequestParam String password) {
       
	    if (adminRepo.findByLogin(login) == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user does not exist!!");
	    } else {
	        // User exists, proceed with authentication
	        if (passwordEncoder.matches(password, adminRepo.findByLogin(login).getPassword())) {
	            // Return the authenticated user details
	            return ResponseEntity.ok().body("{\"message\": \"Login successful\"}");
	        } else {
	            // Return 401 Unauthorized status if authentication fails
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid username or password\"}");
	        }
	    }
	}        
}     