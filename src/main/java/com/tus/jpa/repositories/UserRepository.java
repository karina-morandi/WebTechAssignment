package com.tus.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.jpa.dto.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,Long>{
	Optional<Users>findByLogin(String login);
//	Users findByLogin(String login);
}
