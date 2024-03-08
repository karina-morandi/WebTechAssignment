package com.tus.jpa.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tus.jpa.dto.Users;
import com.tus.jpa.repositories.UserRepository;

@Component
public class AssignmentRunner implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(AssignmentRunner.class);

	private static final String PWORD = "$2a$12$t0Urtv6ZcmtmQEAYKzm82.wgq2aZEGiUU6BtgzH266qQRQBfhWGR.";
	
	public static final String GREEN = "\u001B[32m";
	public static final String RED = "\u001B[31m";
	public static final String RESET = "\u001B[0m"; 	
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Users admin = new Users("admin", "admin@gmail.com", PWORD, "ADMIN");
        userRepo.save(admin);
        Users customer = new Users("custoner", "customer@gmail.com", PWORD, "CUSTOMER");
        userRepo.save(customer);
        logger.info("\u001B[32m Users Created \u001B[0m");
    }

}