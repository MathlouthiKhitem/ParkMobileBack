package com.parkmobile.server.service;

import com.parkmobile.server.domain.Agent;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Agent}.
 */
public interface AgentService {
    /**
     * Save a agent.
     *
     * @param agent the entity to save.
     * @return the persisted entity.
     */
    Agent save(Agent agent);

    /**
     * Updates a agent.
     *
     * @param agent the entity to update.
     * @return the persisted entity.
     */
    Agent update(Agent agent);

    /**
     * Partially updates a agent.
     *
     * @param agent the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Agent> partialUpdate(Agent agent);

    /**
     * Get all the agents.
     *
     * @return the list of entities.
     */
    List<Agent> findAll();

    /**
     * Get the "id" agent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Agent> findOne(String id);

    /**
     * Delete the "id" agent.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
