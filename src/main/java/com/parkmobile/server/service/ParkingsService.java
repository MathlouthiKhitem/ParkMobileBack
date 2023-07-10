package com.parkmobile.server.service;

import com.parkmobile.server.domain.Parkings;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Parkings}.
 */
public interface ParkingsService {
    /**
     * Save a parkings.
     *
     * @param parkings the entity to save.
     * @return the persisted entity.
     */
    Parkings save(Parkings parkings);

    /**
     * Updates a parkings.
     *
     * @param parkings the entity to update.
     * @return the persisted entity.
     */
    Parkings update(Parkings parkings);

    /**
     * Partially updates a parkings.
     *
     * @param parkings the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Parkings> partialUpdate(Parkings parkings);

    /**
     * Get all the parkings.
     *
     * @return the list of entities.
     */
    List<Parkings> findAll();

    /**
     * Get the "id" parkings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Parkings> findOne(String id);

    /**
     * Delete the "id" parkings.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
