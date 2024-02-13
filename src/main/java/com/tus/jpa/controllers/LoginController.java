package com.tus.jpa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.UserRepository;

@Controller
public class LoginController {
	
	@Autowired
    private UserRepository userRepository;

	 @GetMapping("/loginpage")
	    public String login(Model model) {
	        return "login"; // Return the name of the HTML template file (without the file extension)
	    }

	    @PostMapping("/loginpage")
	    public String loginSubmit(String username, String password, RedirectAttributes redirectAttributes) {
	        // Retrieve user from the database based on the provided username
	        Users user = userRepository.findByLogin(username);
	        
	        if (user != null && user.getPassword().equals(password)) {
	            // Redirect to admin dashboard after successful login
	            return "redirect:/admin";
	        } else {
	            // Add an error message to be displayed on the login page
	            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
	            // Redirect back to the login page
	            return "redirect:/loginpage";
	        }
	    }
	}