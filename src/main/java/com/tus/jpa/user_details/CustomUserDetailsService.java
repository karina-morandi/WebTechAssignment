package com.tus.jpa.user_details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepo;
    
//    public CustomUserDetailsService(UserRepository userRepo){
//    	this.userRepo = userRepo;
//    }

	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Users user = userRepo.findByLogin(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

	        return new CustomUserDetails(user);
//        Users user = userRepo.findByLogin(username);
//        if(user ==null) {
//            throw new UsernameNotFoundException("User Not Found");
//        }
//        return new CustomUserDetails(user);
    }
}