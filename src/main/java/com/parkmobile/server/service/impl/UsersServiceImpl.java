package com.parkmobile.server.service.impl;

import com.parkmobile.server.domain.User;
import com.parkmobile.server.domain.Users;
import com.parkmobile.server.repository.UsersRepository;
import com.parkmobile.server.service.InvalidPasswordException;
import com.parkmobile.server.service.UsersService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Service Implementation for managing {@link Users}.
 */
@Service
public class UsersServiceImpl implements UsersService {

    private final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public Users save(Users users) {
        log.debug("Request to save Users : {}", users);
        return usersRepository.save(users);
    }

    @Override
    public Users update(Users users) {
        log.debug("Request to update Users : {}", users);
        return usersRepository.save(users);
    }

    @Override
    public Optional<Users> partialUpdate(Users users) {
        log.debug("Request to partially update Users : {}", users);

        return usersRepository
            .findById(users.getId())
            .map(existingUsers -> {
                if (users.getFirstName() != null) {
                    existingUsers.setFirstName(users.getFirstName());
                }
                if (users.getLastName() != null) {
                    existingUsers.setLastName(users.getLastName());
                }
                if (users.getEmail() != null) {
                    existingUsers.setEmail(users.getEmail());
                }
                if (users.getPhoneNumber() != null) {
                    existingUsers.setPhoneNumber(users.getPhoneNumber());
                }
                if (users.getGender() != null) {
                    existingUsers.setGender(users.getGender());
                }
                if (users.getDateOfBirth() != null) {
                    existingUsers.setDateOfBirth(users.getDateOfBirth());
                }
                if (users.getMatricule() != null) {
                    existingUsers.setMatricule(users.getMatricule());
                }
                if (users.getPassword() != null) {
                    existingUsers.setPassword(users.getPassword());
                }

                return existingUsers;
            })
            .map(usersRepository::save);
    }

    @Override
    public List<Users> findAll() {
        log.debug("Request to get all Users");
        return usersRepository.findAll();
    }

    @Override
    public Optional<Users> findOne(String id) {
        log.debug("Request to get Users : {}", id);
        return usersRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Users : {}", id);
        usersRepository.deleteById(id);
    }
    @Override
    public boolean verifyCredentials(String email, String password) {
        List<User> users = usersRepository.findByEmail(email);
        for (User user : users) {
            if (user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void changePassword(String id ,Users ChangePasswordRequest ) {
        Users user = usersRepository.findById(id).get();

        if (!ChangePasswordRequest.getPassword().equals(user.getPassword())) {
        }

        user.setPassword(ChangePasswordRequest.getPassword());
        usersRepository.save(user);
    }

    @ResponseBody
    public List<String> getUserIdsByEmail(String email) {
        List<User> users = usersRepository.findByEmail(email);
        List<String> userIds = new ArrayList<>();

        for (User user : users) {
            userIds.add(user.getId());
        }

        return userIds;
    }

    public void editUserProfile(String userId, Users updatedUser) {
        // Retrieve the existing user from the database based on the provided ID
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();

            // Update the relevant fields with the new values
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setEmail(updatedUser.getEmail());

            // Save the updated user
            usersRepository.save(existingUser);
        } else {
            // Handle the case where the user with the given ID is not found
            // You can throw an exception or return an appropriate response
        }
    }
    public ResponseEntity<String> getUserProfile(String userId) {
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            String firstName = user.getFirstName();
            return ResponseEntity.ok(firstName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

//    public void changePassword(String id, Users changePasswordRequest) {
//        Optional<Users> optionalUser = usersRepository.findById(id);
//        if (optionalUser.isPresent()) {
//            Users user = optionalUser.get();
//            if (!changePasswordRequest.getPassword().equals(user.getPassword())) {
//                user.setPassword(changePasswordRequest.getPassword());
//                usersRepository.save(user);
//            }
//        } else {
//
//        }
//    }

