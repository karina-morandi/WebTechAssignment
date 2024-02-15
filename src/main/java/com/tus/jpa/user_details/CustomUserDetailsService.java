package com.tus.jpa.user_details;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tus.jpa.dto.Admin;
import com.tus.jpa.repositories.AdminRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepo;
    
    public CustomUserDetailsService(AdminRepository adminRepo){
    	this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepo.findByLogin(username);
        if(admin ==null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return new CustomUserDetails(admin);
    }
}
