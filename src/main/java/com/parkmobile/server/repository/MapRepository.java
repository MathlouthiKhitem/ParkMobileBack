package com.parkmobile.server.repository;

import com.parkmobile.server.domain.Map;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Map entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MapRepository extends MongoRepository<Map, String> {}
