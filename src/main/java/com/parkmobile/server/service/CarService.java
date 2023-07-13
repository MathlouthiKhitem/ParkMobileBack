package com.parkmobile.server.service;

import com.parkmobile.server.domain.Car;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Car}.
 */
public interface CarService {
    /**
     * Save a car.
     *
     * @param car the entity to save.
     * @return the persisted entity.
     */
    Car save(Car car);

    /**
     * Updates a car.
     *
     * @param car the entity to update.
     * @return the persisted entity.
     */
    Car update(Car car);

    /**
     * Partially updates a car.
     *
     * @param car the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Car> partialUpdate(Car car);

    /**
     * Get all the cars.
     *
     * @return the list of entities.
     */
    List<Car> findAll();

    /**
     * Get the "id" car.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Car> findOne(String id);

    /**
     * Delete the "id" car.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
