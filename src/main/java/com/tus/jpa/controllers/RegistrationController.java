package com.tus.jpa.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tus.jpa.dto.LoginRequest;
import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.UserRepository;
import com.tus.jpa.user_details.CustomUserDetails;
import com.tus.jpa.user_details.CustomUserDetailsService;

import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/user")
public class RegistrationController {
	
	@Autowired
	private final UserRepository userRepo;
	@Autowired
	private AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	
	public RegistrationController(UserRepository userRepo, CustomUserDetailsService userService, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	
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
		Optional<Users> foundUser=userRepo.findByLogin(login);
		return foundUser!=null;
//		if(foundUser != null) {
//			return true;
//		}else {
//			return false;
//		}
	}	
//	
//	@GetMapping("/login")
//    public ResponseEntity getLogin(@RequestParam String login, @RequestParam String password) {
//		Users user = userRepo.findByLogin(login);
//	    if (user == null) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "This user does not exist!"));
//	    } else {
//	        if (passwordEncoder.matches(password, user.getPassword())) {
//	            String role = user.getRole(); // Get the user's role
//	            System.out.println("USER SAVED: " + user);
//	            if(role.equals("ADMIN")) {
//	                return ResponseEntity.status(HttpStatus.OK).body("Admin"); // Return "Admin" as data
//	            } else if (role.equals("CUSTOMER")) {
//	                return ResponseEntity.status(HttpStatus.OK).body("Customer"); // Return "Customer" as data
//	            } else {
//	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid role for user");
//	            }
//	        } else {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//	        }
//	    }
//    }
	
//	@PostMapping("/login")  
//	public ResponseEntity<?> authenticate(@RequestBody Map<String, String> loginInfo) {   
//	    String login = loginInfo.get("login");
//	    String password = loginInfo.get("password");
//
//	    Users user = userRepo.findByLogin(login);
//	    if (user == null) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "This user does not exist!"));
//	    } else {
//	        if (passwordEncoder.matches(password, user.getPassword())) {
//	            String role = user.getRole(); // Get the user's role
//	            System.out.println("USER SAVED: " + user);
//	            if(role.equals("ADMIN")) {
//	                return ResponseEntity.ok().body(Collections.singletonMap("role", "Admin"));
//	            } else if (role.equals("CUSTOMER")) {
//	                return ResponseEntity.ok().body(Collections.singletonMap("role", "Customer"));
//	            } else {
//	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Invalid role for user"));
//	            }
//	        } else {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid username or password"));
//	        }
//	    } 
//	}
	
//	@PostMapping("/login")
//	public ResponseEntity authenticate(@RequestParam String login, @RequestParam String password) {
//	    Users user = userRepo.findByLogin(login);
//	    if (user == null) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "This user does not exist!"));
//	    } else {
//	        if (passwordEncoder.matches(password, user.getPassword())) {
//	            String role = user.getRole(); // Get the user's role
//	            System.out.println("USER SAVED: " + user);
//	            if(role.equals("ADMIN")) {
//	                return ResponseEntity.status(HttpStatus.OK).body("Admin"); // Return "Admin" as data
//	            } else if (role.equals("CUSTOMER")) {
//	                return ResponseEntity.status(HttpStatus.OK).body("Customer"); // Return "Customer" as data
//	            } else {
//	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid role for user");
//	            }
//	        } else {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//	        }
//	    }
//	}

//	@PostMapping("/login")
//    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
//        // Validate login credentials and authenticate user
//        // userService.login(loginRequest.getUsername(), loginRequest.getPassword());
//        
//        // Assuming userService.login() returns "Admin" or "Customer" based on successful login
//	    UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
//
//	    if (userDetails != null) {
//	        // Extract user type from UserDetails (you need to implement this logic)
//	        String userType = extractUserType(userDetails);
//
//	        if (userType != null) {
//	            return ResponseEntity.ok(userType);
//	        }
//	    }
//
//	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
	    try {
	        // Authenticate the user
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        loginRequest.getUsername(),
	                        loginRequest.getPassword(),
	                        Collections.emptyList()
	                )
	        );

	        // Set the authentication token in the response
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        // Extract user type from CustomUserDetails
	        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
	        String userType = customUserDetails.getRole(); // Change this line

	        if (userType != null) {
	            return ResponseEntity.ok(userType);
	        }
	    } catch (AuthenticationException e) {
	        // Handle authentication exceptions
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
	    }

	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
	}

	private CustomUserDetails extractUserType(CustomUserDetails userDetails) {
	    // Logic to extract user type from CustomUserDetails
	    String userType = userDetails.getRole(); // Change this line
	    return userType != null ? userDetails : null;
	}


	@GetMapping("/role")
	public String getUserRole(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities().toString();
	}	
}
