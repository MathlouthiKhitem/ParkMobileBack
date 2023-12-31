package com.parkmobile.server.web.rest;

import com.parkmobile.server.domain.Map;
import com.parkmobile.server.repository.MapRepository;
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
 * REST controller for managing {@link com.parkmobile.server.domain.Map}.
 */
@RestController
@RequestMapping("/api")
public class MapResource {

    private final Logger log = LoggerFactory.getLogger(MapResource.class);

    private static final String ENTITY_NAME = "map";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MapRepository mapRepository;

    public MapResource(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    /**
     * {@code POST  /maps} : Create a new map.
     *
     * @param map the map to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new map, or with status {@code 400 (Bad Request)} if the map has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maps")
    public ResponseEntity<Map> createMap(@RequestBody Map map) throws URISyntaxException {
        log.debug("REST request to save Map : {}", map);
        if (map.getId() != null) {
            throw new BadRequestAlertException("A new map cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Map result = mapRepository.save(map);
        return ResponseEntity
            .created(new URI("/api/maps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /maps/:id} : Updates an existing map.
     *
     * @param id the id of the map to save.
     * @param map the map to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated map,
     * or with status {@code 400 (Bad Request)} if the map is not valid,
     * or with status {@code 500 (Internal Server Error)} if the map couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/maps/{id}")
    public ResponseEntity<Map> updateMap(@PathVariable(value = "id", required = false) final String id, @RequestBody Map map)
        throws URISyntaxException {
        log.debug("REST request to update Map : {}, {}", id, map);
        if (map.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, map.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Map result = mapRepository.save(map);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, map.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /maps/:id} : Partial updates given fields of an existing map, field will ignore if it is null
     *
     * @param id the id of the map to save.
     * @param map the map to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated map,
     * or with status {@code 400 (Bad Request)} if the map is not valid,
     * or with status {@code 404 (Not Found)} if the map is not found,
     * or with status {@code 500 (Internal Server Error)} if the map couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/maps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Map> partialUpdateMap(@PathVariable(value = "id", required = false) final String id, @RequestBody Map map)
        throws URISyntaxException {
        log.debug("REST request to partial update Map partially : {}, {}", id, map);
        if (map.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, map.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Map> result = mapRepository
            .findById(map.getId())
            .map(existingMap -> {
                if (map.getLocation() != null) {
                    existingMap.setLocation(map.getLocation());
                }
                if (map.getZoneMap() != null) {
                    existingMap.setZoneMap(map.getZoneMap());
                }

                return existingMap;
            })
            .map(mapRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, map.getId()));
    }

    /**
     * {@code GET  /maps} : get all the maps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maps in body.
     */
    @GetMapping("/maps")
    public List<Map> getAllMaps() {
        log.debug("REST request to get all Maps");
        return mapRepository.findAll();
    }

    /**
     * {@code GET  /maps/:id} : get the "id" map.
     *
     * @param id the id of the map to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the map, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maps/{id}")
    public ResponseEntity<Map> getMap(@PathVariable String id) {
        log.debug("REST request to get Map : {}", id);
        Optional<Map> map = mapRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(map);
    }

    /**
     * {@code DELETE  /maps/:id} : delete the "id" map.
     *
     * @param id the id of the map to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maps/{id}")
    public ResponseEntity<Void> deleteMap(@PathVariable String id) {
        log.debug("REST request to delete Map : {}", id);
        mapRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
