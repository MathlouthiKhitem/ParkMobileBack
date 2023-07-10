package com.parkmobile.server.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.parkmobile.server.IntegrationTest;
import com.parkmobile.server.domain.Map;
import com.parkmobile.server.domain.enumeration.ZoneMap;
import com.parkmobile.server.repository.MapRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link MapResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MapResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final ZoneMap DEFAULT_ZONE_MAP = ZoneMap.Latitude;
    private static final ZoneMap UPDATED_ZONE_MAP = ZoneMap.Longitude;

    private static final String ENTITY_API_URL = "/api/maps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private MockMvc restMapMockMvc;

    private Map map;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Map createEntity() {
        Map map = new Map().location(DEFAULT_LOCATION).zoneMap(DEFAULT_ZONE_MAP);
        return map;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Map createUpdatedEntity() {
        Map map = new Map().location(UPDATED_LOCATION).zoneMap(UPDATED_ZONE_MAP);
        return map;
    }

    @BeforeEach
    public void initTest() {
        mapRepository.deleteAll();
        map = createEntity();
    }

    @Test
    void createMap() throws Exception {
        int databaseSizeBeforeCreate = mapRepository.findAll().size();
        // Create the Map
        restMapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isCreated());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeCreate + 1);
        Map testMap = mapList.get(mapList.size() - 1);
        assertThat(testMap.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testMap.getZoneMap()).isEqualTo(DEFAULT_ZONE_MAP);
    }

    @Test
    void createMapWithExistingId() throws Exception {
        // Create the Map with an existing ID
        map.setId("existing_id");

        int databaseSizeBeforeCreate = mapRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMaps() throws Exception {
        // Initialize the database
        mapRepository.save(map);

        // Get all the mapList
        restMapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(map.getId())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].zoneMap").value(hasItem(DEFAULT_ZONE_MAP.toString())));
    }

    @Test
    void getMap() throws Exception {
        // Initialize the database
        mapRepository.save(map);

        // Get the map
        restMapMockMvc
            .perform(get(ENTITY_API_URL_ID, map.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(map.getId()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.zoneMap").value(DEFAULT_ZONE_MAP.toString()));
    }

    @Test
    void getNonExistingMap() throws Exception {
        // Get the map
        restMapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingMap() throws Exception {
        // Initialize the database
        mapRepository.save(map);

        int databaseSizeBeforeUpdate = mapRepository.findAll().size();

        // Update the map
        Map updatedMap = mapRepository.findById(map.getId()).get();
        updatedMap.location(UPDATED_LOCATION).zoneMap(UPDATED_ZONE_MAP);

        restMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMap.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMap))
            )
            .andExpect(status().isOk());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
        Map testMap = mapList.get(mapList.size() - 1);
        assertThat(testMap.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testMap.getZoneMap()).isEqualTo(UPDATED_ZONE_MAP);
    }

    @Test
    void putNonExistingMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, map.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(map))
            )
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(map))
            )
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMapWithPatch() throws Exception {
        // Initialize the database
        mapRepository.save(map);

        int databaseSizeBeforeUpdate = mapRepository.findAll().size();

        // Update the map using partial update
        Map partialUpdatedMap = new Map();
        partialUpdatedMap.setId(map.getId());

        partialUpdatedMap.location(UPDATED_LOCATION);

        restMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMap))
            )
            .andExpect(status().isOk());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
        Map testMap = mapList.get(mapList.size() - 1);
        assertThat(testMap.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testMap.getZoneMap()).isEqualTo(DEFAULT_ZONE_MAP);
    }

    @Test
    void fullUpdateMapWithPatch() throws Exception {
        // Initialize the database
        mapRepository.save(map);

        int databaseSizeBeforeUpdate = mapRepository.findAll().size();

        // Update the map using partial update
        Map partialUpdatedMap = new Map();
        partialUpdatedMap.setId(map.getId());

        partialUpdatedMap.location(UPDATED_LOCATION).zoneMap(UPDATED_ZONE_MAP);

        restMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMap))
            )
            .andExpect(status().isOk());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
        Map testMap = mapList.get(mapList.size() - 1);
        assertThat(testMap.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testMap.getZoneMap()).isEqualTo(UPDATED_ZONE_MAP);
    }

    @Test
    void patchNonExistingMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, map.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(map))
            )
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(map))
            )
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMap() throws Exception {
        // Initialize the database
        mapRepository.save(map);

        int databaseSizeBeforeDelete = mapRepository.findAll().size();

        // Delete the map
        restMapMockMvc.perform(delete(ENTITY_API_URL_ID, map.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
