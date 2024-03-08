package com.tus.jpa.controllers;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.UserInfo;
import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/user")
public class RegistrationController {
	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
//	@RequestMapping("/")
//	public String index() {
//		return "index.html";
//	}

	
	@PostMapping("/register")
	public ResponseEntity createUser(@Valid @RequestBody Users user) {
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		Users savedUser = userRepo.save(user);
//		return ResponseEntity.status(HttpStatus.OK).body(savedUser);
	    // Check if the user already exists
//	    boolean response = getUserByName(user.getLogin());
	    if (userRepo.findByLogin(user.getLogin()) != null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user already exists!!");
	    } else {
	        // User doesn't exist, proceed with registration
	    	user.setPassword(passwordEncoder.encode(user.getPassword())); // Set password before encoding
	    	user.setEmail(user.getEmail());
	    	user.setRole(user.getRole());
	        Users savedUser = userRepo.save(user);
//	        System.out.println(savedUser);
	        if (savedUser.getRole().equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.OK).body("Admin registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Customer registered successfully");
            }
	    }	
	}
		
	@GetMapping("/name/{login}")
	public boolean getUserByName(@PathVariable("login") String login){
		Users foundUser=userRepo.findByLogin(login);
		return foundUser!=null;
//		if(foundUser != null) {
//			return true;
//		}else {
//			return false;
//		}
	}	
	
  @GetMapping("/login")
  public ResponseEntity<?> getLoginPage() {
      // Return whatever data is appropriate for a GET request to /login
      return ResponseEntity.ok().build();
  }
	
//	@GetMapping("/login")
//	public ResponseEntity<?> getLoginPage(@ModelAttribute UserInfo userInfo) {
//		String username = userInfo.getUsername();
//		String password = userInfo.getPassword();
//		Users user = userRepo.findByLogin(username);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "This user does not exist!"));
//        } else {
//        	if (passwordEncoder.matches(password, user.getPassword())) {
////    	        System.out.println(savedUser);
//    	        if (user.getRole().equals("ADMIN")) {
//                    return ResponseEntity.status(HttpStatus.OK).body("Admin");
//                } else {
//                    return ResponseEntity.status(HttpStatus.OK).body("Customer");
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid username or password"));
//            }
//        } 
//	}
	
	@PostMapping("/login")  
    public ResponseEntity<?> authenticate(@ModelAttribute UserInfo userInfo) {   
		String username = userInfo.getUsername();
		String password = userInfo.getPassword();
		
        Users user = userRepo.findByLogin(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "This user does not exist!"));
        } else {
        	if (passwordEncoder.matches(password, user.getPassword())) {
//    	        System.out.println(savedUser);
    	        if (user.getRole().equals("ADMIN")) {
                    return ResponseEntity.status(HttpStatus.OK).body("Admin");
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body("Customer");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid username or password"));
            }
        } 
    }
	
	@GetMapping("/role")
	public String getUserRole(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities().toString();
	}	
}
