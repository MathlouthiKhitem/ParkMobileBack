package com.parkmobile.server.web.rest;

import com.parkmobile.server.domain.Parkings;
import com.parkmobile.server.repository.ParkingsRepository;
import com.parkmobile.server.service.ParkingsService;
import com.parkmobile.server.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.parkmobile.server.domain.Parkings}.
 */
@RestController
@RequestMapping("/api")
public class ParkingsResource {

    private final Logger log = LoggerFactory.getLogger(ParkingsResource.class);

    private static final String ENTITY_NAME = "parkings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParkingsService parkingsService;

    private final ParkingsRepository parkingsRepository;

    public ParkingsResource(ParkingsService parkingsService, ParkingsRepository parkingsRepository) {
        this.parkingsService = parkingsService;
        this.parkingsRepository = parkingsRepository;
    }

    /**
     * {@code POST  /parkings} : Create a new parkings.
     *
     * @param parkings the parkings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parkings, or with status {@code 400 (Bad Request)} if the parkings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parkings")
    public ResponseEntity<Parkings> createParkings(@RequestBody Parkings parkings) throws URISyntaxException {
        log.debug("REST request to save Parkings : {}", parkings);
        if (parkings.getId() != null) {
            throw new BadRequestAlertException("A new parkings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parkings result = parkingsService.save(parkings);
        return ResponseEntity
            .created(new URI("/api/parkings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /parkings/:id} : Updates an existing parkings.
     *
     * @param id the id of the parkings to save.
     * @param parkings the parkings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parkings,
     * or with status {@code 400 (Bad Request)} if the parkings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parkings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parkings/{id}")
    public ResponseEntity<Parkings> updateParkings(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Parkings parkings
    ) throws URISyntaxException {
        log.debug("REST request to update Parkings : {}, {}", id, parkings);
        if (parkings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parkings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parkingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Parkings result = parkingsService.update(parkings);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parkings.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /parkings/:id} : Partial updates given fields of an existing parkings, field will ignore if it is null
     *
     * @param id the id of the parkings to save.
     * @param parkings the parkings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parkings,
     * or with status {@code 400 (Bad Request)} if the parkings is not valid,
     * or with status {@code 404 (Not Found)} if the parkings is not found,
     * or with status {@code 500 (Internal Server Error)} if the parkings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parkings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Parkings> partialUpdateParkings(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Parkings parkings
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parkings partially : {}, {}", id, parkings);
        if (parkings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parkings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parkingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Parkings> result = parkingsService.partialUpdate(parkings);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parkings.getId())
        );
    }

    /**
     * {@code GET  /parkings} : get all the parkings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parkings in body.
     */
    @GetMapping("/parkings")
    public List<Parkings> getAllParkings() {
        log.debug("REST request to get all Parkings");
        return parkingsService.findAll();
    }

    /**
     * {@code GET  /parkings/:id} : get the "id" parkings.
     *
     * @param id the id of the parkings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parkings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parkings/{id}")
    public ResponseEntity<Parkings> getParkings(@PathVariable String id) {
        log.debug("REST request to get Parkings : {}", id);
        Optional<Parkings> parkings = parkingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parkings);
    }

    /**
     * {@code DELETE  /parkings/:id} : delete the "id" parkings.
     *
     * @param id the id of the parkings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parkings/{id}")
    public ResponseEntity<Void> deleteParkings(@PathVariable String id) {
        log.debug("REST request to delete Parkings : {}", id);
        parkingsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
