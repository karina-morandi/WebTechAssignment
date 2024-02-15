package com.tus.jpa.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.tus.jpa.dto.Admin;
import com.tus.jpa.repositories.AdminRepository;

@Component
public class AssignmentRunner implements ApplicationRunner {

//	private static final String PWORD = "$2a$12$t0Urtv6ZcmtmQEAYKzm82.wgq2aZEGiUU6BtgzH266qQRQBfhWGR.";
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	    // Check if admin user already exists
	    Admin existingAdmin = adminRepo.findByLogin("admin");
	    if (existingAdmin == null) {
	        // Create and save admin user
	        Admin admin = new Admin("admin", "admin@gmail.com", "admin", "admin");
	        adminRepo.save(admin);
	    }
    }

}
