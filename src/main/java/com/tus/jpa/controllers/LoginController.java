//package com.tus.jpa.controllers;
//
//import org.springframework.http.ResponseEntity;
//
//import java.util.Collections;
//
//import javax.validation.Valid;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.tus.jpa.dto.Users;
//import com.tus.jpa.repositories.UserRepository;
//
//@CrossOrigin(origins = "*", allowedHeaders = "*") // Enable CORS for all origins and headers
//@RestController
//public class LoginController {
//
//	private final UserRepository userRepo;
//	private final PasswordEncoder passwordEncoder;
//    
//    public LoginController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
//		this.userRepo = userRepo;
//		this.passwordEncoder = passwordEncoder;
//	}
//    
//    @GetMapping("/login")
//    public ResponseEntity<?> getLogin() {
//        // Return whatever data is appropriate for a GET request to /login
//        return ResponseEntity.ok().build();
//    }
//
////    @GetMapping("/login/{login}")
////    public boolean getUser(@PathVariable("login") String login){
////		Users foundUser=userRepo.findByLogin(login);
////		return foundUser!=null;
//////	public boolean getUser(@PathVariable("login") String login){
//////		Users foundUser = userRepo.findByLogin(login);
//////		if(foundUser != null) {
//////			return true;
//////		}else {
//////			return false;
//////		}
////	}
//    
//    @PostMapping("/login")  
//    public ResponseEntity<?> authenticate(@RequestParam String login, @RequestParam String password) {   
//        Users user = userRepo.findByLogin(login);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "This user does not exist!"));
//        } else {
//        	if (passwordEncoder.matches(password, user.getPassword())) {
//        		String role = user.getRole(); // Get the user's role
//                return ResponseEntity.ok().body(Collections.singletonMap("role", role)); // Send the role back in the response
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid username or password"));
//            }
//        } 
//    }
//	
//	@GetMapping("/role")
//	public String getUserRole(){
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		return auth.getAuthorities().toString();
//	}	
//	
////    @PostMapping("/login")  
////    public ResponseEntity authenticate(@Valid @RequestParam String login, @RequestParam String password) {   
////    	
////    	if (adminRepo.findByLogin(login) == null) {
////	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user does not exit!!");
////	    } else {
////	        // User doesn't exist, proceed with registration
////	    	if (passwordEncoder.matches(password, password)) {
////	            // Return the authenticated user details
////	            return ResponseEntity.ok().body("Login successful");
////	        } else {
////	            // Return 401 Unauthorized status if authentication fails
////	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid username or password\"}");
////	        }
////	    }	
////	}
//}    	
//
//
