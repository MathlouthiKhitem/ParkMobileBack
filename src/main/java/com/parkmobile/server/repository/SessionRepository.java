package com.parkmobile.server.repository;

import com.parkmobile.server.domain.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Session entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionRepository extends MongoRepository<Session, String> {



    Optional<Session> findByclients(String clientId);
    List<Session> findByClientsId(String clientId);


}
