package com.tus.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.jpa.dto.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,Long>{
	
	Users findByLogin(String login);
}
