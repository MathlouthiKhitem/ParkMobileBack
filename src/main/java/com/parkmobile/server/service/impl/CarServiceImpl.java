package com.parkmobile.server.service.impl;

import com.parkmobile.server.domain.Car;
import com.parkmobile.server.repository.CarRepository;
import com.parkmobile.server.service.CarService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Car}.
 */
@Service
public class CarServiceImpl implements CarService {

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Car save(Car car) {
        log.debug("Request to save Car : {}", car);
        return carRepository.save(car);
    }

    @Override
    public Car update(Car car) {
        log.debug("Request to update Car : {}", car);
        return carRepository.save(car);
    }

    @Override
    public Optional<Car> partialUpdate(Car car) {
        log.debug("Request to partially update Car : {}", car);

        return carRepository
            .findById(car.getId())
            .map(existingCar -> {
                if (car.getMatricule() != null) {
                    existingCar.setMatricule(car.getMatricule());
                }

                return existingCar;
            })
            .map(carRepository::save);
    }

    @Override
    public List<Car> findAll() {
        log.debug("Request to get all Cars");
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> findOne(String id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Car : {}", id);
        carRepository.deleteById(id);
    }
}
