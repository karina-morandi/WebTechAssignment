package com.tus.jpa.controllers;

import org.springframework.http.ResponseEntity;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.Admin;
import com.tus.jpa.repositories.AdminRepository;

@RestController
public class LoginController {

    AdminRepository adminRepo;
    PasswordEncoder passwordEncoder;
    
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
    public ResponseEntity authenticate(@RequestParam String login, @RequestParam String password) {   
        Admin user = adminRepo.findByLogin(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user does not exist!");
        } else {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.ok().body("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        }   
    }
    
//    @PostMapping("/login")  
//    public ResponseEntity authenticate(@Valid @RequestParam String login, @RequestParam String password) {   
//    	
//    	if (adminRepo.findByLogin(login) == null) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user does not exit!!");
//	    } else {
//	        // User doesn't exist, proceed with registration
//	    	if (passwordEncoder.matches(password, password)) {
//	            // Return the authenticated user details
//	            return ResponseEntity.ok().body("Login successful");
//	        } else {
//	            // Return 401 Unauthorized status if authentication fails
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid username or password\"}");
//	        }
//	    }	
//	}
}    	


