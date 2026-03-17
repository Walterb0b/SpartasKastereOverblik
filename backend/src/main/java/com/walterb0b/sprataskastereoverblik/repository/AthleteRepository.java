package com.walterb0b.sprataskastereoverblik.repository;

import com.walterb0b.sprataskastereoverblik.model.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    Optional<Athlete> findByStatletikId(Integer statletikId);
}
