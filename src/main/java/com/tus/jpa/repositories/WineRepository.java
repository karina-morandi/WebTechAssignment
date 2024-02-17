package com.tus.jpa.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tus.jpa.dto.Wines;

@Repository
public interface WineRepository extends JpaRepository<Wines, Long> {
	List<Wines> findByName(String name);
	List<Wines> findByNameContaining(String name);
	Optional<Wines> findByNameAndYear(String name, int year);
}
