package com.parkmobile.server.service;

import com.parkmobile.server.domain.Session;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Session}.
 */
public interface SessionService {
    /**
     * Save a session.
     *
     * @param session the entity to save.
     * @return the persisted entity.
     */
    Session save(Session session);

    /**
     * Updates a session.
     *
     * @param session the entity to update.
     * @return the persisted entity.
     */
    Session update(Session session);

    /**
     * Partially updates a session.
     *
     * @param session the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Session> partialUpdate(Session session);

    /**
     * Get all the sessions.
     *
     * @return the list of entities.
     */
    List<Session> findAll();

    /**
     * Get the "id" session.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Session> findOne(String id);

    /**
     * Delete the "id" session.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
