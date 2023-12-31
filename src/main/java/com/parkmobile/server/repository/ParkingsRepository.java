package com.parkmobile.server.repository;

import com.parkmobile.server.domain.Parkings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Parkings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParkingsRepository extends MongoRepository<Parkings, String> {
    Optional<Parkings> findBySessionId(String id);
    Optional<Parkings> findByNumeroParking(String numeroParking);



}
