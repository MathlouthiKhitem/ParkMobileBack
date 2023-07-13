package com.parkmobile.server.controller;

import com.parkmobile.server.domain.Parkings;
import com.parkmobile.server.domain.Users;
import com.parkmobile.server.repository.UsersRepository;
import com.parkmobile.server.service.impl.EmailVerificationService;
import com.parkmobile.server.service.impl.ParkingsServiceImpl;
import com.parkmobile.server.service.impl.SmsVerificationService;
import com.parkmobile.server.service.impl.UsersServiceImpl;
import com.parkmobile.server.web.rest.UsersResource;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Backend/users")
@Component
@AllArgsConstructor
public class User {
    private final Logger log = LoggerFactory.getLogger(UsersResource.class);

    @Autowired
    UsersResource iServContrat;
    @Autowired
    UsersServiceImpl isServUsers;
    @Autowired
    SmsVerificationService isServSmsVerificationService;
    @Autowired
    EmailVerificationService ISsendEmailVerificationService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
     private UsersRepository UsersRepositoryis;
    @Autowired
    ParkingsServiceImpl  IservParkingsService;
    @GetMapping("/show")
    public List<Users> show() {
        return iServContrat.getAllUsers();
    }

    @PostMapping("/adduser")
    public Users signup(@RequestBody Users es) throws URISyntaxException {
        log.debug("REST request to save Users: {}", es);

        ResponseEntity<Users> response = iServContrat.createUsers(es);
        Users savedUsers = response.getBody();

        return savedUsers;
    }

    @PutMapping("/update/{id}")
    public Users retrieveUsers(@PathVariable("id") String id, @RequestBody Users es) throws URISyntaxException {
        return iServContrat.updateUsers(id, es).getBody();
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody Users signInRequest) {
        boolean isValidCredentials = isServUsers.verifyCredentials(signInRequest.getEmail(), signInRequest.getPassword());
        if (isValidCredentials) {
            // User is authenticated, generate and return a token

            Map<String, String> response = new HashMap<>();
            response.put("email", signInRequest.getEmail());

            return ResponseEntity.ok(response);
        } else {
            // Invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody String phoneNumber) {
        isServSmsVerificationService.sendVerificationCode(phoneNumber);
        String verificationCode =  isServSmsVerificationService.sendVerificationCode(phoneNumber);
        return ResponseEntity.ok( verificationCode);
    }

    @PutMapping("/{id}/password")
    public void changePassword(@PathVariable("id") String userId, @RequestBody Users password) {
        isServUsers.changePassword(userId, password);
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email != null && !email.isEmpty()) {
            String verificationCode = ISsendEmailVerificationService.sendVerificationCode(email);
            return ResponseEntity.ok( verificationCode);
        } else {
            return ResponseEntity.badRequest().body("Invalid email address");
        }
    }
    @PutMapping("/{id}/profile")
    public void editUserProfile(@PathVariable("id") String userId, @RequestBody Users updatedUser){
        isServUsers.editUserProfile(userId,updatedUser);
    }
//    @PostMapping("/send-verification-email")
//    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        if (email != null && !email.isEmpty()) {
//            String verificationCode = ISsendEmailVerificationService.sendVerificationCode(email);
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Verification code sent to " + email);
//            response.put("code", verificationCode);
//            return ResponseEntity.ok(response);
//        } else {
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "Invalid email address");
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
    @GetMapping("/{email}")
    public List<String> getUserIdsByEmail(@PathVariable String email) {
        return isServUsers.getUserIdsByEmail(email);
    }
    @GetMapping("/{id}/profileusers")
    public ResponseEntity<String> getUserProfile(@PathVariable("id") String userId){
        return  isServUsers.getUserProfile(userId);
    }
    @PostMapping("/add-parking-session")
    public ResponseEntity<Parkings> addParkingWithStartedSession(@RequestBody Map<String, String> request) throws URISyntaxException {
        return iServContrat.addParkingWithStartedSession(request);
    }

    @GetMapping("/{clientId}/parkings")
    public List<Parkings> findParkingsForClient(@PathVariable String clientId) {
        return iServContrat.findParkingsForClient(clientId);
    }
    }



