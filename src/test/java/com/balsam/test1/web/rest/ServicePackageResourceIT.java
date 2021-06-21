package com.balsam.test1.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.balsam.test1.IntegrationTest;
import com.balsam.test1.domain.Country;
import com.balsam.test1.domain.ServicePackage;
import com.balsam.test1.domain.ServicePackageType;
import com.balsam.test1.repository.ServicePackageRepository;
import com.balsam.test1.service.criteria.ServicePackageCriteria;
import com.balsam.test1.service.dto.ServicePackageDTO;
import com.balsam.test1.service.mapper.ServicePackageMapper;
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
 * Integration tests for the {@link ServicePackageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServicePackageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RECOMMENDED = false;
    private static final Boolean UPDATED_RECOMMENDED = true;

    private static final String DEFAULT_TAG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAG_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-packages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Autowired
    private ServicePackageMapper servicePackageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicePackageMockMvc;

    private ServicePackage servicePackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicePackage createEntity(EntityManager em) {
        ServicePackage servicePackage = new ServicePackage().name(DEFAULT_NAME).recommended(DEFAULT_RECOMMENDED).tagName(DEFAULT_TAG_NAME);
        return servicePackage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicePackage createUpdatedEntity(EntityManager em) {
        ServicePackage servicePackage = new ServicePackage().name(UPDATED_NAME).recommended(UPDATED_RECOMMENDED).tagName(UPDATED_TAG_NAME);
        return servicePackage;
    }

    @BeforeEach
    public void initTest() {
        servicePackage = createEntity(em);
    }

    @Test
    @Transactional
    void createServicePackage() throws Exception {
        int databaseSizeBeforeCreate = servicePackageRepository.findAll().size();
        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);
        restServicePackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeCreate + 1);
        ServicePackage testServicePackage = servicePackageList.get(servicePackageList.size() - 1);
        assertThat(testServicePackage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServicePackage.getRecommended()).isEqualTo(DEFAULT_RECOMMENDED);
        assertThat(testServicePackage.getTagName()).isEqualTo(DEFAULT_TAG_NAME);
    }

    @Test
    @Transactional
    void createServicePackageWithExistingId() throws Exception {
        // Create the ServicePackage with an existing ID
        servicePackage.setId(1L);
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        int databaseSizeBeforeCreate = servicePackageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicePackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicePackageRepository.findAll().size();
        // set the field null
        servicePackage.setName(null);

        // Create the ServicePackage, which fails.
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        restServicePackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicePackages() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList
        restServicePackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicePackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].recommended").value(hasItem(DEFAULT_RECOMMENDED.booleanValue())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)));
    }

    @Test
    @Transactional
    void getServicePackage() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get the servicePackage
        restServicePackageMockMvc
            .perform(get(ENTITY_API_URL_ID, servicePackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicePackage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.recommended").value(DEFAULT_RECOMMENDED.booleanValue()))
            .andExpect(jsonPath("$.tagName").value(DEFAULT_TAG_NAME));
    }

    @Test
    @Transactional
    void getServicePackagesByIdFiltering() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        Long id = servicePackage.getId();

        defaultServicePackageShouldBeFound("id.equals=" + id);
        defaultServicePackageShouldNotBeFound("id.notEquals=" + id);

        defaultServicePackageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultServicePackageShouldNotBeFound("id.greaterThan=" + id);

        defaultServicePackageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultServicePackageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServicePackagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where name equals to DEFAULT_NAME
        defaultServicePackageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the servicePackageList where name equals to UPDATED_NAME
        defaultServicePackageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where name not equals to DEFAULT_NAME
        defaultServicePackageShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the servicePackageList where name not equals to UPDATED_NAME
        defaultServicePackageShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultServicePackageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the servicePackageList where name equals to UPDATED_NAME
        defaultServicePackageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where name is not null
        defaultServicePackageShouldBeFound("name.specified=true");

        // Get all the servicePackageList where name is null
        defaultServicePackageShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllServicePackagesByNameContainsSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where name contains DEFAULT_NAME
        defaultServicePackageShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the servicePackageList where name contains UPDATED_NAME
        defaultServicePackageShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where name does not contain DEFAULT_NAME
        defaultServicePackageShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the servicePackageList where name does not contain UPDATED_NAME
        defaultServicePackageShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByRecommendedIsEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where recommended equals to DEFAULT_RECOMMENDED
        defaultServicePackageShouldBeFound("recommended.equals=" + DEFAULT_RECOMMENDED);

        // Get all the servicePackageList where recommended equals to UPDATED_RECOMMENDED
        defaultServicePackageShouldNotBeFound("recommended.equals=" + UPDATED_RECOMMENDED);
    }

    @Test
    @Transactional
    void getAllServicePackagesByRecommendedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where recommended not equals to DEFAULT_RECOMMENDED
        defaultServicePackageShouldNotBeFound("recommended.notEquals=" + DEFAULT_RECOMMENDED);

        // Get all the servicePackageList where recommended not equals to UPDATED_RECOMMENDED
        defaultServicePackageShouldBeFound("recommended.notEquals=" + UPDATED_RECOMMENDED);
    }

    @Test
    @Transactional
    void getAllServicePackagesByRecommendedIsInShouldWork() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where recommended in DEFAULT_RECOMMENDED or UPDATED_RECOMMENDED
        defaultServicePackageShouldBeFound("recommended.in=" + DEFAULT_RECOMMENDED + "," + UPDATED_RECOMMENDED);

        // Get all the servicePackageList where recommended equals to UPDATED_RECOMMENDED
        defaultServicePackageShouldNotBeFound("recommended.in=" + UPDATED_RECOMMENDED);
    }

    @Test
    @Transactional
    void getAllServicePackagesByRecommendedIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where recommended is not null
        defaultServicePackageShouldBeFound("recommended.specified=true");

        // Get all the servicePackageList where recommended is null
        defaultServicePackageShouldNotBeFound("recommended.specified=false");
    }

    @Test
    @Transactional
    void getAllServicePackagesByTagNameIsEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where tagName equals to DEFAULT_TAG_NAME
        defaultServicePackageShouldBeFound("tagName.equals=" + DEFAULT_TAG_NAME);

        // Get all the servicePackageList where tagName equals to UPDATED_TAG_NAME
        defaultServicePackageShouldNotBeFound("tagName.equals=" + UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByTagNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where tagName not equals to DEFAULT_TAG_NAME
        defaultServicePackageShouldNotBeFound("tagName.notEquals=" + DEFAULT_TAG_NAME);

        // Get all the servicePackageList where tagName not equals to UPDATED_TAG_NAME
        defaultServicePackageShouldBeFound("tagName.notEquals=" + UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByTagNameIsInShouldWork() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where tagName in DEFAULT_TAG_NAME or UPDATED_TAG_NAME
        defaultServicePackageShouldBeFound("tagName.in=" + DEFAULT_TAG_NAME + "," + UPDATED_TAG_NAME);

        // Get all the servicePackageList where tagName equals to UPDATED_TAG_NAME
        defaultServicePackageShouldNotBeFound("tagName.in=" + UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByTagNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where tagName is not null
        defaultServicePackageShouldBeFound("tagName.specified=true");

        // Get all the servicePackageList where tagName is null
        defaultServicePackageShouldNotBeFound("tagName.specified=false");
    }

    @Test
    @Transactional
    void getAllServicePackagesByTagNameContainsSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where tagName contains DEFAULT_TAG_NAME
        defaultServicePackageShouldBeFound("tagName.contains=" + DEFAULT_TAG_NAME);

        // Get all the servicePackageList where tagName contains UPDATED_TAG_NAME
        defaultServicePackageShouldNotBeFound("tagName.contains=" + UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByTagNameNotContainsSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList where tagName does not contain DEFAULT_TAG_NAME
        defaultServicePackageShouldNotBeFound("tagName.doesNotContain=" + DEFAULT_TAG_NAME);

        // Get all the servicePackageList where tagName does not contain UPDATED_TAG_NAME
        defaultServicePackageShouldBeFound("tagName.doesNotContain=" + UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackagesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);
        Country country = CountryResourceIT.createEntity(em);
        em.persist(country);
        em.flush();
        servicePackage.setCountry(country);
        servicePackageRepository.saveAndFlush(servicePackage);
        Long countryId = country.getId();

        // Get all the servicePackageList where country equals to countryId
        defaultServicePackageShouldBeFound("countryId.equals=" + countryId);

        // Get all the servicePackageList where country equals to (countryId + 1)
        defaultServicePackageShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    @Test
    @Transactional
    void getAllServicePackagesByPackageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);
        ServicePackageType packageType = ServicePackageTypeResourceIT.createEntity(em);
        em.persist(packageType);
        em.flush();
        servicePackage.setPackageType(packageType);
        servicePackageRepository.saveAndFlush(servicePackage);
        Long packageTypeId = packageType.getId();

        // Get all the servicePackageList where packageType equals to packageTypeId
        defaultServicePackageShouldBeFound("packageTypeId.equals=" + packageTypeId);

        // Get all the servicePackageList where packageType equals to (packageTypeId + 1)
        defaultServicePackageShouldNotBeFound("packageTypeId.equals=" + (packageTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServicePackageShouldBeFound(String filter) throws Exception {
        restServicePackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicePackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].recommended").value(hasItem(DEFAULT_RECOMMENDED.booleanValue())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)));

        // Check, that the count call also returns 1
        restServicePackageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServicePackageShouldNotBeFound(String filter) throws Exception {
        restServicePackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServicePackageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServicePackage() throws Exception {
        // Get the servicePackage
        restServicePackageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServicePackage() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();

        // Update the servicePackage
        ServicePackage updatedServicePackage = servicePackageRepository.findById(servicePackage.getId()).get();
        // Disconnect from session so that the updates on updatedServicePackage are not directly saved in db
        em.detach(updatedServicePackage);
        updatedServicePackage.name(UPDATED_NAME).recommended(UPDATED_RECOMMENDED).tagName(UPDATED_TAG_NAME);
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(updatedServicePackage);

        restServicePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicePackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
        ServicePackage testServicePackage = servicePackageList.get(servicePackageList.size() - 1);
        assertThat(testServicePackage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServicePackage.getRecommended()).isEqualTo(UPDATED_RECOMMENDED);
        assertThat(testServicePackage.getTagName()).isEqualTo(UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void putNonExistingServicePackage() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();
        servicePackage.setId(count.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicePackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicePackage() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();
        servicePackage.setId(count.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicePackage() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();
        servicePackage.setId(count.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicePackageWithPatch() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();

        // Update the servicePackage using partial update
        ServicePackage partialUpdatedServicePackage = new ServicePackage();
        partialUpdatedServicePackage.setId(servicePackage.getId());

        partialUpdatedServicePackage.name(UPDATED_NAME).recommended(UPDATED_RECOMMENDED).tagName(UPDATED_TAG_NAME);

        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicePackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServicePackage))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
        ServicePackage testServicePackage = servicePackageList.get(servicePackageList.size() - 1);
        assertThat(testServicePackage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServicePackage.getRecommended()).isEqualTo(UPDATED_RECOMMENDED);
        assertThat(testServicePackage.getTagName()).isEqualTo(UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void fullUpdateServicePackageWithPatch() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();

        // Update the servicePackage using partial update
        ServicePackage partialUpdatedServicePackage = new ServicePackage();
        partialUpdatedServicePackage.setId(servicePackage.getId());

        partialUpdatedServicePackage.name(UPDATED_NAME).recommended(UPDATED_RECOMMENDED).tagName(UPDATED_TAG_NAME);

        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicePackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServicePackage))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
        ServicePackage testServicePackage = servicePackageList.get(servicePackageList.size() - 1);
        assertThat(testServicePackage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServicePackage.getRecommended()).isEqualTo(UPDATED_RECOMMENDED);
        assertThat(testServicePackage.getTagName()).isEqualTo(UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingServicePackage() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();
        servicePackage.setId(count.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicePackageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicePackage() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();
        servicePackage.setId(count.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicePackage() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageRepository.findAll().size();
        servicePackage.setId(count.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicePackage in the database
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicePackage() throws Exception {
        // Initialize the database
        servicePackageRepository.saveAndFlush(servicePackage);

        int databaseSizeBeforeDelete = servicePackageRepository.findAll().size();

        // Delete the servicePackage
        restServicePackageMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicePackage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServicePackage> servicePackageList = servicePackageRepository.findAll();
        assertThat(servicePackageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
