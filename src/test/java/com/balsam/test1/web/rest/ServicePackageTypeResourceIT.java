package com.balsam.test1.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.balsam.test1.IntegrationTest;
import com.balsam.test1.domain.ServicePackage;
import com.balsam.test1.domain.ServicePackageType;
import com.balsam.test1.repository.ServicePackageTypeRepository;
import com.balsam.test1.service.criteria.ServicePackageTypeCriteria;
import com.balsam.test1.service.dto.ServicePackageTypeDTO;
import com.balsam.test1.service.mapper.ServicePackageTypeMapper;
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
 * Integration tests for the {@link ServicePackageTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServicePackageTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-package-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServicePackageTypeRepository servicePackageTypeRepository;

    @Autowired
    private ServicePackageTypeMapper servicePackageTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicePackageTypeMockMvc;

    private ServicePackageType servicePackageType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicePackageType createEntity(EntityManager em) {
        ServicePackageType servicePackageType = new ServicePackageType().name(DEFAULT_NAME);
        return servicePackageType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicePackageType createUpdatedEntity(EntityManager em) {
        ServicePackageType servicePackageType = new ServicePackageType().name(UPDATED_NAME);
        return servicePackageType;
    }

    @BeforeEach
    public void initTest() {
        servicePackageType = createEntity(em);
    }

    @Test
    @Transactional
    void createServicePackageType() throws Exception {
        int databaseSizeBeforeCreate = servicePackageTypeRepository.findAll().size();
        // Create the ServicePackageType
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);
        restServicePackageTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ServicePackageType testServicePackageType = servicePackageTypeList.get(servicePackageTypeList.size() - 1);
        assertThat(testServicePackageType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createServicePackageTypeWithExistingId() throws Exception {
        // Create the ServicePackageType with an existing ID
        servicePackageType.setId(1L);
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);

        int databaseSizeBeforeCreate = servicePackageTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicePackageTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicePackageTypeRepository.findAll().size();
        // set the field null
        servicePackageType.setName(null);

        // Create the ServicePackageType, which fails.
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);

        restServicePackageTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicePackageTypes() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        // Get all the servicePackageTypeList
        restServicePackageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicePackageType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getServicePackageType() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        // Get the servicePackageType
        restServicePackageTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, servicePackageType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicePackageType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getServicePackageTypesByIdFiltering() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        Long id = servicePackageType.getId();

        defaultServicePackageTypeShouldBeFound("id.equals=" + id);
        defaultServicePackageTypeShouldNotBeFound("id.notEquals=" + id);

        defaultServicePackageTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultServicePackageTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultServicePackageTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultServicePackageTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServicePackageTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        // Get all the servicePackageTypeList where name equals to DEFAULT_NAME
        defaultServicePackageTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the servicePackageTypeList where name equals to UPDATED_NAME
        defaultServicePackageTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackageTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        // Get all the servicePackageTypeList where name not equals to DEFAULT_NAME
        defaultServicePackageTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the servicePackageTypeList where name not equals to UPDATED_NAME
        defaultServicePackageTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackageTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        // Get all the servicePackageTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultServicePackageTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the servicePackageTypeList where name equals to UPDATED_NAME
        defaultServicePackageTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackageTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        // Get all the servicePackageTypeList where name is not null
        defaultServicePackageTypeShouldBeFound("name.specified=true");

        // Get all the servicePackageTypeList where name is null
        defaultServicePackageTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllServicePackageTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        // Get all the servicePackageTypeList where name contains DEFAULT_NAME
        defaultServicePackageTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the servicePackageTypeList where name contains UPDATED_NAME
        defaultServicePackageTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackageTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        // Get all the servicePackageTypeList where name does not contain DEFAULT_NAME
        defaultServicePackageTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the servicePackageTypeList where name does not contain UPDATED_NAME
        defaultServicePackageTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllServicePackageTypesByPackagesIsEqualToSomething() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);
        ServicePackage packages = ServicePackageResourceIT.createEntity(em);
        em.persist(packages);
        em.flush();
        servicePackageType.addPackages(packages);
        servicePackageTypeRepository.saveAndFlush(servicePackageType);
        Long packagesId = packages.getId();

        // Get all the servicePackageTypeList where packages equals to packagesId
        defaultServicePackageTypeShouldBeFound("packagesId.equals=" + packagesId);

        // Get all the servicePackageTypeList where packages equals to (packagesId + 1)
        defaultServicePackageTypeShouldNotBeFound("packagesId.equals=" + (packagesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServicePackageTypeShouldBeFound(String filter) throws Exception {
        restServicePackageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicePackageType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restServicePackageTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServicePackageTypeShouldNotBeFound(String filter) throws Exception {
        restServicePackageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServicePackageTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServicePackageType() throws Exception {
        // Get the servicePackageType
        restServicePackageTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServicePackageType() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();

        // Update the servicePackageType
        ServicePackageType updatedServicePackageType = servicePackageTypeRepository.findById(servicePackageType.getId()).get();
        // Disconnect from session so that the updates on updatedServicePackageType are not directly saved in db
        em.detach(updatedServicePackageType);
        updatedServicePackageType.name(UPDATED_NAME);
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(updatedServicePackageType);

        restServicePackageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicePackageTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
        ServicePackageType testServicePackageType = servicePackageTypeList.get(servicePackageTypeList.size() - 1);
        assertThat(testServicePackageType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingServicePackageType() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();
        servicePackageType.setId(count.incrementAndGet());

        // Create the ServicePackageType
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicePackageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicePackageTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicePackageType() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();
        servicePackageType.setId(count.incrementAndGet());

        // Create the ServicePackageType
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicePackageType() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();
        servicePackageType.setId(count.incrementAndGet());

        // Create the ServicePackageType
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicePackageTypeWithPatch() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();

        // Update the servicePackageType using partial update
        ServicePackageType partialUpdatedServicePackageType = new ServicePackageType();
        partialUpdatedServicePackageType.setId(servicePackageType.getId());

        restServicePackageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicePackageType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServicePackageType))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
        ServicePackageType testServicePackageType = servicePackageTypeList.get(servicePackageTypeList.size() - 1);
        assertThat(testServicePackageType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateServicePackageTypeWithPatch() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();

        // Update the servicePackageType using partial update
        ServicePackageType partialUpdatedServicePackageType = new ServicePackageType();
        partialUpdatedServicePackageType.setId(servicePackageType.getId());

        partialUpdatedServicePackageType.name(UPDATED_NAME);

        restServicePackageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicePackageType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServicePackageType))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
        ServicePackageType testServicePackageType = servicePackageTypeList.get(servicePackageTypeList.size() - 1);
        assertThat(testServicePackageType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingServicePackageType() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();
        servicePackageType.setId(count.incrementAndGet());

        // Create the ServicePackageType
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicePackageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicePackageTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicePackageType() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();
        servicePackageType.setId(count.incrementAndGet());

        // Create the ServicePackageType
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicePackageType() throws Exception {
        int databaseSizeBeforeUpdate = servicePackageTypeRepository.findAll().size();
        servicePackageType.setId(count.incrementAndGet());

        // Create the ServicePackageType
        ServicePackageTypeDTO servicePackageTypeDTO = servicePackageTypeMapper.toDto(servicePackageType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicePackageTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicePackageType in the database
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicePackageType() throws Exception {
        // Initialize the database
        servicePackageTypeRepository.saveAndFlush(servicePackageType);

        int databaseSizeBeforeDelete = servicePackageTypeRepository.findAll().size();

        // Delete the servicePackageType
        restServicePackageTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicePackageType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServicePackageType> servicePackageTypeList = servicePackageTypeRepository.findAll();
        assertThat(servicePackageTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
