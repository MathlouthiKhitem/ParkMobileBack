package com.parkmobile.server.web.rest;

import com.parkmobile.server.domain.*;
import com.parkmobile.server.repository.ClientRepository;
import com.parkmobile.server.repository.ParkingsRepository;
import com.parkmobile.server.repository.UsersRepository;
import com.parkmobile.server.service.UsersService;
import com.parkmobile.server.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.*;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.parkmobile.server.domain.Users}.
 */
@RestController
@RequestMapping("/api/users")

public class UsersResource {

    private final Logger log = LoggerFactory.getLogger(UsersResource.class);

    private static final String ENTITY_NAME = "users";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsersService usersService;

    private final UsersRepository usersRepository;
    private  final ClientRepository clientRepository;
   private  final SessionResource  sessionRepository;
   private  final  ParkingsResource parkingsRepository;
    private  final  ParkingsRepository parkings;
    public UsersResource(UsersService usersService, UsersRepository usersRepository, ClientRepository clientRepository, SessionResource sessionRepository, ParkingsResource parkingsRepository, ParkingsRepository parkings) {
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
        this.parkingsRepository = parkingsRepository;
        this.parkings = parkings;
    }

    /**
     * {@code POST  /users} : Create a new users.
     *
     * @param users the users to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new users, or with status {@code 400 (Bad Request)} if the users has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/users")
    public ResponseEntity<Users> createUsers(@RequestBody Users users) throws URISyntaxException {
        log.debug("REST request to save Users: {}", users);
        if (users.getId() != null) {
            throw new BadRequestAlertException("A new users cannot already have an ID", ENTITY_NAME, "idexists");
        }
        users.setType(2);

        // Generate ObjectId values for ID fields
        ObjectId id = new ObjectId();

        // Set the ID for both the Users and Client objects
        users.setId(id.toString());
        Client client = new Client();
        client.setId(id.toString());

        // Associate the objects
        users.setClient(client);
        client.setUser(users);

        // Create an empty list of sessions for the client
        HashSet<Session> sessions = new HashSet<>();
        client.setSessions((Set<Session>) sessions);

        Users savedUsers = usersService.save(users);
        Client savedClient = clientRepository.save(client);

        savedUsers.getClient().setSessions(savedClient.getSessions());

        return ResponseEntity
            .created(new URI("/api/users/" + savedUsers.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, savedUsers.getId()))
            .body(savedUsers);
    }









    /**
     * {@code PUT  /users/:id} : Updates an existing users.
     *
     * @param id the id of the users to save.
     * @param users the users to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated users,
     * or with status {@code 400 (Bad Request)} if the users is not valid,
     * or with status {@code 500 (Internal Server Error)} if the users couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<Users> updateUsers(@PathVariable(value = "id", required = false) final String id, @RequestBody Users users)
        throws URISyntaxException {
        log.debug("REST request to update Users : {}, {}", id, users);
        if (users.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, users.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Users result = usersService.update(users);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, users.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /users/:id} : Partial updates given fields of an existing users, field will ignore if it is null
     *
     * @param id the id of the users to save.
     * @param users the users to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated users,
     * or with status {@code 400 (Bad Request)} if the users is not valid,
     * or with status {@code 404 (Not Found)} if the users is not found,
     * or with status {@code 500 (Internal Server Error)} if the users couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Users> partialUpdateUsers(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Users users
    ) throws URISyntaxException {
        log.debug("REST request to partial update Users partially : {}, {}", id, users);
        if (users.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, users.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Users> result = usersService.partialUpdate(users);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, users.getId()));
    }

    /**
     * {@code GET  /users} : get all the users.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @GetMapping("/users")
    public List<Users> getAllUsers() {
        log.debug("REST request to get all Users");
        return usersService.findAll();
    }

    /**
     * {@code GET  /users/:id} : get the "id" users.
     *
     * @param id the id of the users to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the users, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUsers(@PathVariable String id) {
        log.debug("REST request to get Users : {}", id);
        Optional<Users> users = usersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(users);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" users.
     *
     * @param id the id of the users to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable String id) {
        log.debug("REST request to delete Users : {}", id);
        usersService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    public ResponseEntity<Parkings> addParkingWithStartedSession(@RequestBody Map<String, String> request) throws URISyntaxException {
        String clientId = request.get("clientId");
        Optional<Client> clientOptional = clientRepository.findById(clientId);

        if (clientOptional.isPresent()) {
            Parkings parking = new Parkings();
            parking.setZoneTitle(request.get("zoneTitle"));
            parking.setNumeroParking(request.get("numeroParking"));
            parking.setDuree(Integer.toString(Integer.parseInt(request.get("duree")) - 9));
            Session session = new Session();
            session.setStartTime(Instant.now());

            Client client = clientOptional.get();
            Users user = client.getUser();
            String matricule = request.get("matricule");
            user.setMatricule(matricule);
            client.addSession(session);
            parking.setSession(session);
            sessionRepository.createSession(session);
            parkingsRepository.createParkings(parking);
            usersRepository.save(user);
            clientRepository.save(client);

            return ResponseEntity.ok(parking);
        } else {

            return ResponseEntity.notFound().build();
        }
    }
    public List<Parkings> findParkingsForClient(String clientId) {
        // Récupérer le client correspondant à l'ID
        Optional<Client> clientOptional = clientRepository.findById(clientId);

        // Vérifier si le client existe
        if (!clientOptional.isPresent()) {
            System.out.println("Client not found");
            return null;
        }

        Client client = clientOptional.get();
        List<Parkings> parkingsList = new ArrayList<>();

        // Parcourir les sessions du client et chercher si un parking correspondant existe
        for (Session session : client.getSessions()) {
            Optional<Parkings> parkingOptional = parkings.findBySessionId(session.getId());
            if (parkingOptional.isPresent()) {
                // Un parking a été trouvé pour cette session
                System.out.println("Parking found: " + parkingOptional.get().getNumeroParking());
                parkingsList.add(parkingOptional.get());
            } else {
                // Aucun parking n'a été trouvé pour cette session
                System.out.println("No parking found for session " + session.getId());
            }
        }
        return parkingsList;
    }

}
