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
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		Admin savedUser = adminRepo.save(admin);
		return ResponseEntity.status(HttpStatus.OK).body(savedUser);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Boolean> getUserByName(@PathVariable("name") String name){
		Admin foundUser=adminRepo.findByLogin(name);
        return ResponseEntity.ok(foundUser != null);
	}	
}
