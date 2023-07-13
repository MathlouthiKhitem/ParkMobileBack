package com.parkmobile.server.web.rest;

import com.parkmobile.server.domain.Client;
import com.parkmobile.server.domain.Session;
import com.parkmobile.server.domain.Users;
import com.parkmobile.server.repository.ClientRepository;
import com.parkmobile.server.repository.SessionRepository;
import com.parkmobile.server.repository.UsersRepository;
import com.parkmobile.server.service.SessionService;
import com.parkmobile.server.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.parkmobile.server.domain.Session}.
 */
@RestController
@RequestMapping("/api")
public class SessionResource {

    private final Logger log = LoggerFactory.getLogger(SessionResource.class);

    private static final String ENTITY_NAME = "session";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionService sessionService;

    private final SessionRepository sessionRepository;
    private final UsersRepository UsersRepository;

    private final ClientRepository clientUserRepository;

    public SessionResource(SessionService sessionService, SessionRepository sessionRepository, UsersRepository userRepository, com.parkmobile.server.repository.UsersRepository usersRepository, ClientRepository clientUserRepository) {
        this.sessionService = sessionService;
        this.sessionRepository = sessionRepository;
        UsersRepository = usersRepository;

        this.clientUserRepository = clientUserRepository;
    }

    /**
     * {@code POST  /sessions} : Create a new session.
     *
     * @param session the session to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new session, or with status {@code 400 (Bad Request)} if the session has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sessions")
    public ResponseEntity<Session> createSession(@RequestBody Session session) throws URISyntaxException {
        log.debug("REST request to save Session : {}", session);
        if (session.getId() != null) {
            throw new BadRequestAlertException("A new session cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Session result = sessionService.save(session);
        return ResponseEntity
            .created(new URI("/api/sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /sessions/:id} : Updates an existing session.
     *
     * @param id      the id of the session to save.
     * @param session the session to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated session,
     * or with status {@code 400 (Bad Request)} if the session is not valid,
     * or with status {@code 500 (Internal Server Error)} if the session couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sessions/{id}")
    public ResponseEntity<Session> updateSession(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Session session
    ) throws URISyntaxException {
        log.debug("REST request to update Session : {}, {}", id, session);
        if (session.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, session.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Session result = sessionService.update(session);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, session.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /sessions/:id} : Partial updates given fields of an existing session, field will ignore if it is null
     *
     * @param id      the id of the session to save.
     * @param session the session to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated session,
     * or with status {@code 400 (Bad Request)} if the session is not valid,
     * or with status {@code 404 (Not Found)} if the session is not found,
     * or with status {@code 500 (Internal Server Error)} if the session couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sessions/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<Session> partialUpdateSession(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Session session
    ) throws URISyntaxException {
        log.debug("REST request to partial update Session partially : {}, {}", id, session);
        if (session.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, session.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Session> result = sessionService.partialUpdate(session);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, session.getId())
        );
    }

    /**
     * {@code GET  /sessions} : get all the sessions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessions in body.
     */
    @GetMapping("/sessions")
    public List<Session> getAllSessions() {
        log.debug("REST request to get all Sessions");
        return sessionService.findAll();
    }

    /**
     * {@code GET  /sessions/:id} : get the "id" session.
     *
     * @param id the id of the session to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the session, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sessions/{id}")
    public ResponseEntity<Session> getSession(@PathVariable String id) {
        log.debug("REST request to get Session : {}", id);
        Optional<Session> session = sessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(session);
    }

    /**
     * {@code DELETE  /sessions/:id} : delete the "id" session.
     *
     * @param id the id of the session to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id) {
        log.debug("REST request to delete Session : {}", id);
        sessionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    public List<Session> findSessionsByMatricule(String matricule) {
        Optional<Users> userOptional = UsersRepository.findByMatricule(matricule);

        if (userOptional.isEmpty()) {
            return Collections.emptyList();
        }

        Users user = userOptional.get();

        Optional<Client> clientUserOptional = Optional.ofNullable(clientUserRepository.findByUsersId(user.getId()));

        if (clientUserOptional.isEmpty()) {
            return Collections.emptyList();
        }

        Client clientUser = clientUserOptional.get();

        List<Session> sessions = sessionRepository.findByClientsId(clientUser.getId());

        List<Session> sessionDtos = new ArrayList<>();
        for (Session session : sessions) {
            Session sessionDto = new Session();
            sessionDto.setId(session.getId());
            sessionDto.setStatus(session.getStatus());
            // Add other fields as needed

            sessionDtos.add(sessionDto);
        }

        return sessionDtos;
    }


}