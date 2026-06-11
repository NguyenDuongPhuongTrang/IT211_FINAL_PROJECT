package com.example.it211project.repository;

import com.example.it211project.model.entity.CourtImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtImageRepository extends JpaRepository<CourtImage, Long> {
    List<CourtImage> findByCourtId(Long courtId);
}
