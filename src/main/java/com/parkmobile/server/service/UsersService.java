package com.parkmobile.server.service;

import com.parkmobile.server.domain.Users;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Users}.
 */
public interface UsersService {
    /**
     * Save a users.
     *
     * @param users the entity to save.
     * @return the persisted entity.
     */
    Users save(Users users);

    /**
     * Updates a users.
     *
     * @param users the entity to update.
     * @return the persisted entity.
     */
    Users update(Users users);

    /**
     * Partially updates a users.
     *
     * @param users the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Users> partialUpdate(Users users);

    /**
     * Get all the users.
     *
     * @return the list of entities.
     */
    List<Users> findAll();

    /**
     * Get the "id" users.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Users> findOne(String id);

    /**
     * Delete the "id" users.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
