package com.tus.jpa.controllers;

import javax.validation.Valid;

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
	public ResponseEntity<Boolean> getUser(@PathVariable("login") String login){
		Admin foundUser = adminRepo.findByLogin(login);
	    return ResponseEntity.ok(foundUser != null);
	}
    
	@PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody Admin admin) {
        // Retrieve user from the database based on the provided login
		Admin storedAdmin = adminRepo.findByLogin(admin.getLogin());
        
        if (storedAdmin != null && passwordEncoder.matches(admin.getPassword(), storedAdmin.getPassword())) {
            // Return the authenticated user details
            return ResponseEntity.ok().build(); // Return success status
        } else {
            // Return 401 Unauthorized status if authentication fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}