package com.balsam.test1.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.balsam.test1.IntegrationTest;
import com.balsam.test1.domain.Location;
import com.balsam.test1.repository.LocationRepository;
import com.balsam.test1.service.criteria.LocationCriteria;
import com.balsam.test1.service.dto.LocationDTO;
import com.balsam.test1.service.mapper.LocationMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationResourceIT {

    private static final String DEFAULT_STREET_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationMockMvc;

    private Location location;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .streetAddressLine1(DEFAULT_STREET_ADDRESS_LINE_1)
            .streetAddressLine2(DEFAULT_STREET_ADDRESS_LINE_2)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
        return location;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .streetAddressLine1(UPDATED_STREET_ADDRESS_LINE_1)
            .streetAddressLine2(UPDATED_STREET_ADDRESS_LINE_2)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();
        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getStreetAddressLine1()).isEqualTo(DEFAULT_STREET_ADDRESS_LINE_1);
        assertThat(testLocation.getStreetAddressLine2()).isEqualTo(DEFAULT_STREET_ADDRESS_LINE_2);
        assertThat(testLocation.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testLocation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createLocationWithExistingId() throws Exception {
        // Create the Location with an existing ID
        location.setId(1L);
        LocationDTO locationDTO = locationMapper.toDto(location);

        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddressLine1").value(hasItem(DEFAULT_STREET_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].streetAddressLine2").value(hasItem(DEFAULT_STREET_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)));
    }

    @Test
    @Transactional
    void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.streetAddressLine1").value(DEFAULT_STREET_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.streetAddressLine2").value(DEFAULT_STREET_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE));
    }

    @Test
    @Transactional
    void getLocationsByIdFiltering() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        Long id = location.getId();

        defaultLocationShouldBeFound("id.equals=" + id);
        defaultLocationShouldNotBeFound("id.notEquals=" + id);

        defaultLocationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.greaterThan=" + id);

        defaultLocationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine1 equals to DEFAULT_STREET_ADDRESS_LINE_1
        defaultLocationShouldBeFound("streetAddressLine1.equals=" + DEFAULT_STREET_ADDRESS_LINE_1);

        // Get all the locationList where streetAddressLine1 equals to UPDATED_STREET_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("streetAddressLine1.equals=" + UPDATED_STREET_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine1 not equals to DEFAULT_STREET_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("streetAddressLine1.notEquals=" + DEFAULT_STREET_ADDRESS_LINE_1);

        // Get all the locationList where streetAddressLine1 not equals to UPDATED_STREET_ADDRESS_LINE_1
        defaultLocationShouldBeFound("streetAddressLine1.notEquals=" + UPDATED_STREET_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine1 in DEFAULT_STREET_ADDRESS_LINE_1 or UPDATED_STREET_ADDRESS_LINE_1
        defaultLocationShouldBeFound("streetAddressLine1.in=" + DEFAULT_STREET_ADDRESS_LINE_1 + "," + UPDATED_STREET_ADDRESS_LINE_1);

        // Get all the locationList where streetAddressLine1 equals to UPDATED_STREET_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("streetAddressLine1.in=" + UPDATED_STREET_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine1 is not null
        defaultLocationShouldBeFound("streetAddressLine1.specified=true");

        // Get all the locationList where streetAddressLine1 is null
        defaultLocationShouldNotBeFound("streetAddressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine1 contains DEFAULT_STREET_ADDRESS_LINE_1
        defaultLocationShouldBeFound("streetAddressLine1.contains=" + DEFAULT_STREET_ADDRESS_LINE_1);

        // Get all the locationList where streetAddressLine1 contains UPDATED_STREET_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("streetAddressLine1.contains=" + UPDATED_STREET_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine1 does not contain DEFAULT_STREET_ADDRESS_LINE_1
        defaultLocationShouldNotBeFound("streetAddressLine1.doesNotContain=" + DEFAULT_STREET_ADDRESS_LINE_1);

        // Get all the locationList where streetAddressLine1 does not contain UPDATED_STREET_ADDRESS_LINE_1
        defaultLocationShouldBeFound("streetAddressLine1.doesNotContain=" + UPDATED_STREET_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine2 equals to DEFAULT_STREET_ADDRESS_LINE_2
        defaultLocationShouldBeFound("streetAddressLine2.equals=" + DEFAULT_STREET_ADDRESS_LINE_2);

        // Get all the locationList where streetAddressLine2 equals to UPDATED_STREET_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("streetAddressLine2.equals=" + UPDATED_STREET_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine2 not equals to DEFAULT_STREET_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("streetAddressLine2.notEquals=" + DEFAULT_STREET_ADDRESS_LINE_2);

        // Get all the locationList where streetAddressLine2 not equals to UPDATED_STREET_ADDRESS_LINE_2
        defaultLocationShouldBeFound("streetAddressLine2.notEquals=" + UPDATED_STREET_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine2 in DEFAULT_STREET_ADDRESS_LINE_2 or UPDATED_STREET_ADDRESS_LINE_2
        defaultLocationShouldBeFound("streetAddressLine2.in=" + DEFAULT_STREET_ADDRESS_LINE_2 + "," + UPDATED_STREET_ADDRESS_LINE_2);

        // Get all the locationList where streetAddressLine2 equals to UPDATED_STREET_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("streetAddressLine2.in=" + UPDATED_STREET_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine2 is not null
        defaultLocationShouldBeFound("streetAddressLine2.specified=true");

        // Get all the locationList where streetAddressLine2 is null
        defaultLocationShouldNotBeFound("streetAddressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine2 contains DEFAULT_STREET_ADDRESS_LINE_2
        defaultLocationShouldBeFound("streetAddressLine2.contains=" + DEFAULT_STREET_ADDRESS_LINE_2);

        // Get all the locationList where streetAddressLine2 contains UPDATED_STREET_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("streetAddressLine2.contains=" + UPDATED_STREET_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddressLine2 does not contain DEFAULT_STREET_ADDRESS_LINE_2
        defaultLocationShouldNotBeFound("streetAddressLine2.doesNotContain=" + DEFAULT_STREET_ADDRESS_LINE_2);

        // Get all the locationList where streetAddressLine2 does not contain UPDATED_STREET_ADDRESS_LINE_2
        defaultLocationShouldBeFound("streetAddressLine2.doesNotContain=" + UPDATED_STREET_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude equals to DEFAULT_LATITUDE
        defaultLocationShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the locationList where latitude equals to UPDATED_LATITUDE
        defaultLocationShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude not equals to DEFAULT_LATITUDE
        defaultLocationShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the locationList where latitude not equals to UPDATED_LATITUDE
        defaultLocationShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultLocationShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the locationList where latitude equals to UPDATED_LATITUDE
        defaultLocationShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude is not null
        defaultLocationShouldBeFound("latitude.specified=true");

        // Get all the locationList where latitude is null
        defaultLocationShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude contains DEFAULT_LATITUDE
        defaultLocationShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the locationList where latitude contains UPDATED_LATITUDE
        defaultLocationShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude does not contain DEFAULT_LATITUDE
        defaultLocationShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the locationList where latitude does not contain UPDATED_LATITUDE
        defaultLocationShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude equals to DEFAULT_LONGITUDE
        defaultLocationShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the locationList where longitude equals to UPDATED_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude not equals to DEFAULT_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the locationList where longitude not equals to UPDATED_LONGITUDE
        defaultLocationShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultLocationShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the locationList where longitude equals to UPDATED_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude is not null
        defaultLocationShouldBeFound("longitude.specified=true");

        // Get all the locationList where longitude is null
        defaultLocationShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude contains DEFAULT_LONGITUDE
        defaultLocationShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the locationList where longitude contains UPDATED_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude does not contain DEFAULT_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the locationList where longitude does not contain UPDATED_LONGITUDE
        defaultLocationShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddressLine1").value(hasItem(DEFAULT_STREET_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].streetAddressLine2").value(hasItem(DEFAULT_STREET_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)));

        // Check, that the count call also returns 1
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).get();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .streetAddressLine1(UPDATED_STREET_ADDRESS_LINE_1)
            .streetAddressLine2(UPDATED_STREET_ADDRESS_LINE_2)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        LocationDTO locationDTO = locationMapper.toDto(updatedLocation);

        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getStreetAddressLine1()).isEqualTo(UPDATED_STREET_ADDRESS_LINE_1);
        assertThat(testLocation.getStreetAddressLine2()).isEqualTo(UPDATED_STREET_ADDRESS_LINE_2);
        assertThat(testLocation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation.streetAddressLine1(UPDATED_STREET_ADDRESS_LINE_1).latitude(UPDATED_LATITUDE);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getStreetAddressLine1()).isEqualTo(UPDATED_STREET_ADDRESS_LINE_1);
        assertThat(testLocation.getStreetAddressLine2()).isEqualTo(DEFAULT_STREET_ADDRESS_LINE_2);
        assertThat(testLocation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .streetAddressLine1(UPDATED_STREET_ADDRESS_LINE_1)
            .streetAddressLine2(UPDATED_STREET_ADDRESS_LINE_2)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getStreetAddressLine1()).isEqualTo(UPDATED_STREET_ADDRESS_LINE_1);
        assertThat(testLocation.getStreetAddressLine2()).isEqualTo(UPDATED_STREET_ADDRESS_LINE_2);
        assertThat(testLocation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        restLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, location.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
