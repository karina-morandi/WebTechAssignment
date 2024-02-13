package com.tus.jpa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.UserRepository;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        // Optionally, implement logic for validating user input, checking for existing usernames, etc.
        // For simplicity, I'm skipping validation and assuming unique usernames.
        
        // Check if the username already exists in the database
        if (userRepository.findByLogin(username) != null) {
            model.addAttribute("error", "Username already exists");
            return "redirect:/loginpage"; // Return to the registration page with error message
        }
        
        // Create a new user object with the provided username and password
        Users newUser = new Users(username, password);

        // Save the new user to the database
        userRepository.save(newUser);

        // Redirect to login page or any other page after successful registration
        return "redirect:/loginpage";
    }
}
