package com.parkmobile.server.repository;

import com.parkmobile.server.domain.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Car entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarRepository extends MongoRepository<Car, String> {}
