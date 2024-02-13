package com.tus.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.jpa.dto.Wines;

@Repository
public interface WineRepository extends JpaRepository<Wines, Long> {
	List<Wines> findByName(String name);
	List<Wines> findByNameContaining(String name);
}
