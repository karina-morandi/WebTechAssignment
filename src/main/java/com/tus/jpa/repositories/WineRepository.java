package com.tus.jpa.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tus.jpa.dto.Wines;

@Repository
public interface WineRepository extends JpaRepository<Wines, Long> {
	List<Wines> findByName(String name);
	List<Wines> findByNameContaining(String name);
	Wines findByNameAndYear(String name, int year);
	Optional<Wines> findById(Long id);
	
	@Modifying
    @Transactional
    @Query("UPDATE Wines w SET w.ratings = :rating WHERE w.id = :id")
    void updateRatings(@Param("id") Long id, @Param("rating") Double rating);
}
