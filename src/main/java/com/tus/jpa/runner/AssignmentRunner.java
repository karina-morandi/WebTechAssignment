package com.tus.jpa.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.UserRepository;


@Component
public class AssignmentRunner implements ApplicationRunner {

//	private static final String PWORD = "$2a$12$t0Urtv6ZcmtmQEAYKzm82.wgq2aZEGiUU6BtgzH266qQRQBfhWGR.";
	
	@Autowired
	private UserRepository userRepo;
	
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Users admin = new Users("admin", "admin");
        userRepo.save(admin);
    }

}
