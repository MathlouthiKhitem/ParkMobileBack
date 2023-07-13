package com.parkmobile.server.service;

import com.parkmobile.server.domain.Admin;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Admin}.
 */
public interface AdminService {
    /**
     * Save a admin.
     *
     * @param admin the entity to save.
     * @return the persisted entity.
     */
    Admin save(Admin admin);

    /**
     * Updates a admin.
     *
     * @param admin the entity to update.
     * @return the persisted entity.
     */
    Admin update(Admin admin);

    /**
     * Partially updates a admin.
     *
     * @param admin the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Admin> partialUpdate(Admin admin);

    /**
     * Get all the admins.
     *
     * @return the list of entities.
     */
    List<Admin> findAll();

    /**
     * Get the "id" admin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Admin> findOne(String id);

    /**
     * Delete the "id" admin.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
