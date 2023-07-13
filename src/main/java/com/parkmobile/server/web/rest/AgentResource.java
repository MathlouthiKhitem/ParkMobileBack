package com.parkmobile.server.web.rest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.parkmobile.server.domain.*;
import com.parkmobile.server.repository.*;
import com.parkmobile.server.service.AgentService;
import com.parkmobile.server.service.UsersService;
import com.parkmobile.server.web.rest.errors.BadRequestAlertException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;



/**
 * REST controller for managing {@link com.parkmobile.server.domain.Agent}.
 */
@RestController
@RequestMapping("/api")
public class AgentResource {

    private final Logger log = LoggerFactory.getLogger(AgentResource.class);

    private static final String ENTITY_NAME = "agent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final MongoTemplate mongoTemplate;

    private final AgentService agentService;
   @Autowired
private  final UsersService usersService;
    @Autowired
    private UsersRepository usersRepository;
    private final AgentRepository agentRepository;
    private  final ParkingsRepository parkingRepository;
private  final ClientRepository clientRepository;
private  final  SessionRepository sessionRepository;
    public AgentResource(MongoTemplate mongoTemplate, AgentService agentService, UsersService usersService, UsersRepository usersRepository, AgentRepository agentRepository, ClientRepository clientRepository, SessionRepository sessionRepository, ParkingsRepository parkingRepository, ClientRepository clientRepository1, SessionRepository sessionRepository1) {
        this.mongoTemplate = mongoTemplate;
        this.agentService = agentService;
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.agentRepository = agentRepository;

        this.parkingRepository = parkingRepository;
        this.clientRepository = clientRepository1;
        this.sessionRepository = sessionRepository1;
    }

    /**
     * {@code POST  /agents} : Create a new agent.
     *

     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agent, or with status {@code 400 (Bad Request)} if the agent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agents")
    public ResponseEntity<Agent> createAgent(@RequestBody Map<String, String> request) throws URISyntaxException {
        Users users = new Users();
        Agent agent = new Agent();

        // Set the email and password from the request
        String email = request.get("email");
        String password = request.get("password");

        // Set the user's email and password
        users.setEmail(email);
        users.setPassword(password);
        users.setType(1);

        ObjectId id = new ObjectId();
        users.setId(id.toString());
        agent.setId(id.toString());

        // Associate the objects
        agent.setUser(users);
        users.setAgent(agent);

        // Save the objects
        Users savedUsers = usersRepository.save(users);
        Agent savedAgent = agentRepository.save(agent);

        return ResponseEntity.ok(savedAgent);
    }




    /**
     * {@code PUT  /agents/:id} : Updates an existing agent.
     *
     * @param id the id of the agent to save.
     * @param agent the agent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agent,
     * or with status {@code 400 (Bad Request)} if the agent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agents/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable(value = "id", required = false) final String id, @RequestBody Agent agent)
        throws URISyntaxException {
        log.debug("REST request to update Agent : {}, {}", id, agent);
        if (agent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Agent result = agentService.update(agent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agent.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /agents/:id} : Partial updates given fields of an existing agent, field will ignore if it is null
     *
     * @param id the id of the agent to save.
     * @param agent the agent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agent,
     * or with status {@code 400 (Bad Request)} if the agent is not valid,
     * or with status {@code 404 (Not Found)} if the agent is not found,
     * or with status {@code 500 (Internal Server Error)} if the agent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Agent> partialUpdateAgent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Agent agent
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agent partially : {}, {}", id, agent);
        if (agent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Agent> result = agentService.partialUpdate(agent);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agent.getId()));
    }

    /**
     * {@code GET  /agents} : get all the agents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agents in body.
     */
    @GetMapping("/agents")
    public List<Agent> getAllAgents() {
        log.debug("REST request to get all Agents");
        return agentService.findAll();
    }

    /**
     * {@code GET  /agents/:id} : get the "id" agent.
     *
     * @param id the id of the agent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agents/{id}")
    public ResponseEntity<Agent> getAgent(@PathVariable String id) {
        log.debug("REST request to get Agent : {}", id);
        Optional<Agent> agent = agentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agent);
    }

    /**
     * {@code DELETE  /agents/:id} : delete the "id" agent.
     *
     * @param id the id of the agent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agents/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable String id) {
        log.debug("REST request to delete Agent : {}", id);
        agentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
    public List<Parkings> getParkingsByNumber(String number) {
        try {
            Criteria criteria = Criteria.where("numero_parking").is(number);
            Query query = new Query(criteria);
            return mongoTemplate.find(query, Parkings.class);
        } catch (IncorrectResultSizeDataAccessException ex) {
            // Handle the non-unique result here
            throw new IllegalStateException("Non-unique result for parking number: " + number);
        }
    }



//    public ResponseEntity<String> checkSessionStatus( String matricule) {
//        Users userExample = new Users();
//        userExample.setMatricule(matricule);
//        ExampleMatcher matcher = ExampleMatcher.matching()
//            .withIgnoreCase()
//            .withMatcher("matricule", ExampleMatcher.GenericPropertyMatchers.exact());
//        Example<Users> example = Example.of(userExample, matcher);
//        Optional<Users> users = usersRepository.findOne(example);
//        log.info("=================: {}", users);
//        if (users.isPresent()) {
//            Optional<Client> client = clientRepository.findByUsersId(users.get().getId());
//
//            if (client.isPresent()) {
//                String idClient = client.get().getId();
//                Optional<Session> sessions = sessionRepository.findByclients(idClient);
//
//                if (sessions.isPresent()) {
//                    boolean status = sessions.get().getStatus();
//
//                    if (status) {
//                        return ResponseEntity.ok("Session is active.");
//                    } else {
//                        return ResponseEntity.ok("Session is inactive.");
//                    }
//                } else {
//                    return ResponseEntity.ok("Session not found for the given client.");
//                }
//            } else {
//                return ResponseEntity.ok("Client not found for the given user.");
//            }
//        } else {
//            return ResponseEntity.ok("User not found.");
//        }
//    }



}
