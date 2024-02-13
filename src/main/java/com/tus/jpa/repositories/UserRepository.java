package com.tus.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tus.jpa.dto.Users;

public interface UserRepository extends JpaRepository<Users,Long>{
	Users findByLogin(String login);
}
