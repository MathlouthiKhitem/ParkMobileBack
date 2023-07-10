package com.parkmobile.server.service.impl;

import com.parkmobile.server.domain.Parkings;
import com.parkmobile.server.domain.Users;
import com.parkmobile.server.repository.ParkingsRepository;
import com.parkmobile.server.repository.UsersRepository;
import com.parkmobile.server.service.ParkingsService;
import java.util.List;
import java.util.Optional;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Parkings}.
 */
@Service
public class ParkingsServiceImpl implements ParkingsService {

    private final Logger log = LoggerFactory.getLogger(ParkingsServiceImpl.class);

    private final ParkingsRepository parkingsRepository;
    @Autowired
    UsersRepository usersRepository;

    public ParkingsServiceImpl(ParkingsRepository parkingsRepository) {
        this.parkingsRepository = parkingsRepository;
    }

    @Override
    public Parkings save(Parkings parkings) {
        log.debug("Request to save Parkings : {}", parkings);
        return parkingsRepository.save(parkings);
    }

    @Override
    public Parkings update(Parkings parkings) {
        log.debug("Request to update Parkings : {}", parkings);
        return parkingsRepository.save(parkings);
    }

    @Override
    public Optional<Parkings> partialUpdate(Parkings parkings) {
        log.debug("Request to partially update Parkings : {}", parkings);

        return parkingsRepository
            .findById(parkings.getId())
            .map(existingParkings -> {
                if (parkings.getZoneTitle() != null) {
                    existingParkings.setZoneTitle(parkings.getZoneTitle());
                }
                if (parkings.getNumeroParking() != null) {
                    existingParkings.setNumeroParking(parkings.getNumeroParking());
                }
                if (parkings.getDuree() != null) {
                    existingParkings.setDuree(parkings.getDuree());
                }
                if (parkings.getPrice() != null) {
                    existingParkings.setPrice(parkings.getPrice());
                }
                if (parkings.getStartDate() != null) {
                    existingParkings.setStartDate(parkings.getStartDate());
                }
                if (parkings.getEndDate() != null) {
                    existingParkings.setEndDate(parkings.getEndDate());
                }

                return existingParkings;
            })
            .map(parkingsRepository::save);
    }

    @Override
    public List<Parkings> findAll() {
        log.debug("Request to get all Parkings");
        return parkingsRepository.findAll();
    }

    @Override
    public Optional<Parkings> findOne(String id) {
        log.debug("Request to get Parkings : {}", id);
        return parkingsRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Parkings : {}", id);
        parkingsRepository.deleteById(id);
    }


//    public ResponseEntity<String> addParkingToUser( String userId,Parkings parking) {
//        Optional<Users> optionalUser = usersRepository.findById(userId);
//        if (optionalUser.isPresent()) {
//            Users user = optionalUser.get();
//            parking.setUsers(user); // Associate the user with the parking
//            parkingsRepository.save(parking);
//            return ResponseEntity.ok("Parking created successfully");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    public ResponseEntity<String> addParkingToUser(String userId, Parkings parking) {
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            parking.setUsers(user); // Associate the user with the parking
            // Set the matricule from the user
            parkingsRepository.save(parking);
            return ResponseEntity.ok("Parking created successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
