package com.tus.jpa.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Admin;
import com.tus.jpa.repositories.AdminRepository;

@RestController
@RequestMapping("/user")
public class RegistrationController {

	private AdminRepository adminRepo;
	private PasswordEncoder passwordEncoder;
	
	public RegistrationController(AdminRepository adminRepo, PasswordEncoder passwordEncoder) {
		this.adminRepo = adminRepo;
		this.passwordEncoder = passwordEncoder;
	}

	
	@PostMapping("/register")
	public ResponseEntity createUser(@Valid @RequestBody Admin admin) {
	    // Check if the user already exists
	    boolean response = getUserByName(admin.getLogin());
	    if (adminRepo.findByLogin(admin.getLogin()) != null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user already exists!!");
	    } else {
	        // User doesn't exist, proceed with registration
	        admin.setPassword(passwordEncoder.encode(admin.getPassword())); // Set password before encoding
	        admin.setEmail(admin.getEmail());
	        admin.setRole("admin");
	        Admin savedAdmin = adminRepo.save(admin);
	        System.out.println(savedAdmin);
	        System.out.println("Received registration data: " + savedAdmin.toString());
	        return ResponseEntity.status(HttpStatus.OK).body(savedAdmin);
	    }	
	}
		
	@GetMapping("/name/{login}")
	public boolean getUserByName(@PathVariable("login") String login){
		Admin foundUser=adminRepo.findByLogin(login);
		if(foundUser != null) {
			return true;
		}else {
			return false;
		}
	}	
	
	@GetMapping("/role")
	public String getUserRole(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities().toString();
	}	
}
