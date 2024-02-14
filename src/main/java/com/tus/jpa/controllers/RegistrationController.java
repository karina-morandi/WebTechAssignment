package com.tus.jpa.controllers;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Admin;
import com.tus.jpa.repositories.AdminRepository;

@RestController
@RequestMapping("/user")
public class RegistrationController {
	
	private final AdminRepository adminRepo;
	private final PasswordEncoder passwordEncoder;
	
	public RegistrationController(AdminRepository adminRepo, PasswordEncoder passwordEncoder) {
		this.adminRepo = adminRepo;
		this.passwordEncoder = passwordEncoder;
	}

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody Admin admin) {
    	try {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setEmail(admin.getEmail());
            admin.setLogin(admin.getLogin());
            Admin savedUser = adminRepo.save(admin);
            return ResponseEntity.status(HttpStatus.OK).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating user: " + e.getMessage());
        }
	}
	
	@GetMapping("/name/{login}")
	public ResponseEntity<Boolean> getUserByName(@PathVariable("login") String login){
		Admin foundUser=adminRepo.findByLogin(login);
        return ResponseEntity.ok(foundUser != null);
	}	
}
