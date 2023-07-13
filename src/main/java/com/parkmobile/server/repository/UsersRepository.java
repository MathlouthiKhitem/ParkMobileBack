package com.parkmobile.server.repository;

import com.parkmobile.server.domain.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Users entity.
 */
@SuppressWarnings("unused")
@Repository

public interface UsersRepository extends MongoRepository<Users, String> {
    List<Users> findByEmail(String email);


    Optional<Users> findByMatricule(String matricule);
}
