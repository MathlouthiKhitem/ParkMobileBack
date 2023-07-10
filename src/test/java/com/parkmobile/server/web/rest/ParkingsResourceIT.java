package com.parkmobile.server.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.parkmobile.server.IntegrationTest;
import com.parkmobile.server.domain.Parkings;
import com.parkmobile.server.repository.ParkingsRepository;
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
 * Integration tests for the {@link ParkingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParkingsResourceIT {

    private static final String DEFAULT_ZONE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ZONE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_PARKING = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PARKING = "BBBBBBBBBB";

    private static final String DEFAULT_DUREE = "AAAAAAAAAA";
    private static final String UPDATED_DUREE = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_START_DATE = "AAAAAAAAAA";
    private static final String UPDATED_START_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_END_DATE = "AAAAAAAAAA";
    private static final String UPDATED_END_DATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parkings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ParkingsRepository parkingsRepository;

    @Autowired
    private MockMvc restParkingsMockMvc;

    private Parkings parkings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parkings createEntity() {
        Parkings parkings = new Parkings()
            .zoneTitle(DEFAULT_ZONE_TITLE)
            .numeroParking(DEFAULT_NUMERO_PARKING)
            .duree(DEFAULT_DUREE)
            .price(DEFAULT_PRICE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return parkings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parkings createUpdatedEntity() {
        Parkings parkings = new Parkings()
            .zoneTitle(UPDATED_ZONE_TITLE)
            .numeroParking(UPDATED_NUMERO_PARKING)
            .duree(UPDATED_DUREE)
            .price(UPDATED_PRICE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return parkings;
    }

    @BeforeEach
    public void initTest() {
        parkingsRepository.deleteAll();
        parkings = createEntity();
    }

    @Test
    void createParkings() throws Exception {
        int databaseSizeBeforeCreate = parkingsRepository.findAll().size();
        // Create the Parkings
        restParkingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkings)))
            .andExpect(status().isCreated());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeCreate + 1);
        Parkings testParkings = parkingsList.get(parkingsList.size() - 1);
        assertThat(testParkings.getZoneTitle()).isEqualTo(DEFAULT_ZONE_TITLE);
        assertThat(testParkings.getNumeroParking()).isEqualTo(DEFAULT_NUMERO_PARKING);
        assertThat(testParkings.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testParkings.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testParkings.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testParkings.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    void createParkingsWithExistingId() throws Exception {
        // Create the Parkings with an existing ID
        parkings.setId("existing_id");

        int databaseSizeBeforeCreate = parkingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkings)))
            .andExpect(status().isBadRequest());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllParkings() throws Exception {
        // Initialize the database
        parkingsRepository.save(parkings);

        // Get all the parkingsList
        restParkingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parkings.getId())))
            .andExpect(jsonPath("$.[*].zoneTitle").value(hasItem(DEFAULT_ZONE_TITLE)))
            .andExpect(jsonPath("$.[*].numeroParking").value(hasItem(DEFAULT_NUMERO_PARKING)))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE)));
    }

    @Test
    void getParkings() throws Exception {
        // Initialize the database
        parkingsRepository.save(parkings);

        // Get the parkings
        restParkingsMockMvc
            .perform(get(ENTITY_API_URL_ID, parkings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parkings.getId()))
            .andExpect(jsonPath("$.zoneTitle").value(DEFAULT_ZONE_TITLE))
            .andExpect(jsonPath("$.numeroParking").value(DEFAULT_NUMERO_PARKING))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE));
    }

    @Test
    void getNonExistingParkings() throws Exception {
        // Get the parkings
        restParkingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingParkings() throws Exception {
        // Initialize the database
        parkingsRepository.save(parkings);

        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();

        // Update the parkings
        Parkings updatedParkings = parkingsRepository.findById(parkings.getId()).get();
        updatedParkings
            .zoneTitle(UPDATED_ZONE_TITLE)
            .numeroParking(UPDATED_NUMERO_PARKING)
            .duree(UPDATED_DUREE)
            .price(UPDATED_PRICE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restParkingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParkings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParkings))
            )
            .andExpect(status().isOk());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
        Parkings testParkings = parkingsList.get(parkingsList.size() - 1);
        assertThat(testParkings.getZoneTitle()).isEqualTo(UPDATED_ZONE_TITLE);
        assertThat(testParkings.getNumeroParking()).isEqualTo(UPDATED_NUMERO_PARKING);
        assertThat(testParkings.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testParkings.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testParkings.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testParkings.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    void putNonExistingParkings() throws Exception {
        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();
        parkings.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parkings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchParkings() throws Exception {
        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();
        parkings.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamParkings() throws Exception {
        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();
        parkings.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateParkingsWithPatch() throws Exception {
        // Initialize the database
        parkingsRepository.save(parkings);

        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();

        // Update the parkings using partial update
        Parkings partialUpdatedParkings = new Parkings();
        partialUpdatedParkings.setId(parkings.getId());

        partialUpdatedParkings.zoneTitle(UPDATED_ZONE_TITLE).startDate(UPDATED_START_DATE);

        restParkingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParkings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParkings))
            )
            .andExpect(status().isOk());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
        Parkings testParkings = parkingsList.get(parkingsList.size() - 1);
        assertThat(testParkings.getZoneTitle()).isEqualTo(UPDATED_ZONE_TITLE);
        assertThat(testParkings.getNumeroParking()).isEqualTo(DEFAULT_NUMERO_PARKING);
        assertThat(testParkings.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testParkings.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testParkings.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testParkings.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    void fullUpdateParkingsWithPatch() throws Exception {
        // Initialize the database
        parkingsRepository.save(parkings);

        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();

        // Update the parkings using partial update
        Parkings partialUpdatedParkings = new Parkings();
        partialUpdatedParkings.setId(parkings.getId());

        partialUpdatedParkings
            .zoneTitle(UPDATED_ZONE_TITLE)
            .numeroParking(UPDATED_NUMERO_PARKING)
            .duree(UPDATED_DUREE)
            .price(UPDATED_PRICE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restParkingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParkings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParkings))
            )
            .andExpect(status().isOk());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
        Parkings testParkings = parkingsList.get(parkingsList.size() - 1);
        assertThat(testParkings.getZoneTitle()).isEqualTo(UPDATED_ZONE_TITLE);
        assertThat(testParkings.getNumeroParking()).isEqualTo(UPDATED_NUMERO_PARKING);
        assertThat(testParkings.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testParkings.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testParkings.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testParkings.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    void patchNonExistingParkings() throws Exception {
        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();
        parkings.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parkings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchParkings() throws Exception {
        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();
        parkings.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamParkings() throws Exception {
        int databaseSizeBeforeUpdate = parkingsRepository.findAll().size();
        parkings.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parkings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parkings in the database
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteParkings() throws Exception {
        // Initialize the database
        parkingsRepository.save(parkings);

        int databaseSizeBeforeDelete = parkingsRepository.findAll().size();

        // Delete the parkings
        restParkingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, parkings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parkings> parkingsList = parkingsRepository.findAll();
        assertThat(parkingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
