package com.parkmobile.server.repository;

import com.parkmobile.server.domain.Parkings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Parkings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParkingsRepository extends MongoRepository<Parkings, String> {}
