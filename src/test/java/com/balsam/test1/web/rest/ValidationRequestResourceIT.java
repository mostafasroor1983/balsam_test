package com.balsam.test1.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.balsam.test1.IntegrationTest;
import com.balsam.test1.domain.User;
import com.balsam.test1.domain.ValidationRequest;
import com.balsam.test1.domain.ValidationRequestFile;
import com.balsam.test1.domain.enumeration.ValidationRequestStatus;
import com.balsam.test1.repository.ValidationRequestRepository;
import com.balsam.test1.service.criteria.ValidationRequestCriteria;
import com.balsam.test1.service.dto.ValidationRequestDTO;
import com.balsam.test1.service.mapper.ValidationRequestMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ValidationRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValidationRequestResourceIT {

    private static final ValidationRequestStatus DEFAULT_STATUS = ValidationRequestStatus.APPROVED;
    private static final ValidationRequestStatus UPDATED_STATUS = ValidationRequestStatus.UNDER_REVIEW;

    private static final Instant DEFAULT_ACTION_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTION_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/validation-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ValidationRequestRepository validationRequestRepository;

    @Autowired
    private ValidationRequestMapper validationRequestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValidationRequestMockMvc;

    private ValidationRequest validationRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidationRequest createEntity(EntityManager em) {
        ValidationRequest validationRequest = new ValidationRequest()
            .status(DEFAULT_STATUS)
            .actionDateTime(DEFAULT_ACTION_DATE_TIME)
            .reason(DEFAULT_REASON)
            .notes(DEFAULT_NOTES);
        return validationRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidationRequest createUpdatedEntity(EntityManager em) {
        ValidationRequest validationRequest = new ValidationRequest()
            .status(UPDATED_STATUS)
            .actionDateTime(UPDATED_ACTION_DATE_TIME)
            .reason(UPDATED_REASON)
            .notes(UPDATED_NOTES);
        return validationRequest;
    }

    @BeforeEach
    public void initTest() {
        validationRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createValidationRequest() throws Exception {
        int databaseSizeBeforeCreate = validationRequestRepository.findAll().size();
        // Create the ValidationRequest
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(validationRequest);
        restValidationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeCreate + 1);
        ValidationRequest testValidationRequest = validationRequestList.get(validationRequestList.size() - 1);
        assertThat(testValidationRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testValidationRequest.getActionDateTime()).isEqualTo(DEFAULT_ACTION_DATE_TIME);
        assertThat(testValidationRequest.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testValidationRequest.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createValidationRequestWithExistingId() throws Exception {
        // Create the ValidationRequest with an existing ID
        validationRequest.setId(1L);
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(validationRequest);

        int databaseSizeBeforeCreate = validationRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restValidationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllValidationRequests() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList
        restValidationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].actionDateTime").value(hasItem(DEFAULT_ACTION_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getValidationRequest() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get the validationRequest
        restValidationRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, validationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(validationRequest.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.actionDateTime").value(DEFAULT_ACTION_DATE_TIME.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getValidationRequestsByIdFiltering() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        Long id = validationRequest.getId();

        defaultValidationRequestShouldBeFound("id.equals=" + id);
        defaultValidationRequestShouldNotBeFound("id.notEquals=" + id);

        defaultValidationRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultValidationRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultValidationRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultValidationRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where status equals to DEFAULT_STATUS
        defaultValidationRequestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the validationRequestList where status equals to UPDATED_STATUS
        defaultValidationRequestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where status not equals to DEFAULT_STATUS
        defaultValidationRequestShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the validationRequestList where status not equals to UPDATED_STATUS
        defaultValidationRequestShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultValidationRequestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the validationRequestList where status equals to UPDATED_STATUS
        defaultValidationRequestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where status is not null
        defaultValidationRequestShouldBeFound("status.specified=true");

        // Get all the validationRequestList where status is null
        defaultValidationRequestShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllValidationRequestsByActionDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where actionDateTime equals to DEFAULT_ACTION_DATE_TIME
        defaultValidationRequestShouldBeFound("actionDateTime.equals=" + DEFAULT_ACTION_DATE_TIME);

        // Get all the validationRequestList where actionDateTime equals to UPDATED_ACTION_DATE_TIME
        defaultValidationRequestShouldNotBeFound("actionDateTime.equals=" + UPDATED_ACTION_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByActionDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where actionDateTime not equals to DEFAULT_ACTION_DATE_TIME
        defaultValidationRequestShouldNotBeFound("actionDateTime.notEquals=" + DEFAULT_ACTION_DATE_TIME);

        // Get all the validationRequestList where actionDateTime not equals to UPDATED_ACTION_DATE_TIME
        defaultValidationRequestShouldBeFound("actionDateTime.notEquals=" + UPDATED_ACTION_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByActionDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where actionDateTime in DEFAULT_ACTION_DATE_TIME or UPDATED_ACTION_DATE_TIME
        defaultValidationRequestShouldBeFound("actionDateTime.in=" + DEFAULT_ACTION_DATE_TIME + "," + UPDATED_ACTION_DATE_TIME);

        // Get all the validationRequestList where actionDateTime equals to UPDATED_ACTION_DATE_TIME
        defaultValidationRequestShouldNotBeFound("actionDateTime.in=" + UPDATED_ACTION_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByActionDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where actionDateTime is not null
        defaultValidationRequestShouldBeFound("actionDateTime.specified=true");

        // Get all the validationRequestList where actionDateTime is null
        defaultValidationRequestShouldNotBeFound("actionDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllValidationRequestsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where reason equals to DEFAULT_REASON
        defaultValidationRequestShouldBeFound("reason.equals=" + DEFAULT_REASON);

        // Get all the validationRequestList where reason equals to UPDATED_REASON
        defaultValidationRequestShouldNotBeFound("reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByReasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where reason not equals to DEFAULT_REASON
        defaultValidationRequestShouldNotBeFound("reason.notEquals=" + DEFAULT_REASON);

        // Get all the validationRequestList where reason not equals to UPDATED_REASON
        defaultValidationRequestShouldBeFound("reason.notEquals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where reason in DEFAULT_REASON or UPDATED_REASON
        defaultValidationRequestShouldBeFound("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON);

        // Get all the validationRequestList where reason equals to UPDATED_REASON
        defaultValidationRequestShouldNotBeFound("reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where reason is not null
        defaultValidationRequestShouldBeFound("reason.specified=true");

        // Get all the validationRequestList where reason is null
        defaultValidationRequestShouldNotBeFound("reason.specified=false");
    }

    @Test
    @Transactional
    void getAllValidationRequestsByReasonContainsSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where reason contains DEFAULT_REASON
        defaultValidationRequestShouldBeFound("reason.contains=" + DEFAULT_REASON);

        // Get all the validationRequestList where reason contains UPDATED_REASON
        defaultValidationRequestShouldNotBeFound("reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where reason does not contain DEFAULT_REASON
        defaultValidationRequestShouldNotBeFound("reason.doesNotContain=" + DEFAULT_REASON);

        // Get all the validationRequestList where reason does not contain UPDATED_REASON
        defaultValidationRequestShouldBeFound("reason.doesNotContain=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where notes equals to DEFAULT_NOTES
        defaultValidationRequestShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the validationRequestList where notes equals to UPDATED_NOTES
        defaultValidationRequestShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where notes not equals to DEFAULT_NOTES
        defaultValidationRequestShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the validationRequestList where notes not equals to UPDATED_NOTES
        defaultValidationRequestShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultValidationRequestShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the validationRequestList where notes equals to UPDATED_NOTES
        defaultValidationRequestShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where notes is not null
        defaultValidationRequestShouldBeFound("notes.specified=true");

        // Get all the validationRequestList where notes is null
        defaultValidationRequestShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllValidationRequestsByNotesContainsSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where notes contains DEFAULT_NOTES
        defaultValidationRequestShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the validationRequestList where notes contains UPDATED_NOTES
        defaultValidationRequestShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        // Get all the validationRequestList where notes does not contain DEFAULT_NOTES
        defaultValidationRequestShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the validationRequestList where notes does not contain UPDATED_NOTES
        defaultValidationRequestShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllValidationRequestsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        validationRequest.setUser(user);
        validationRequestRepository.saveAndFlush(validationRequest);
        Long userId = user.getId();

        // Get all the validationRequestList where user equals to userId
        defaultValidationRequestShouldBeFound("userId.equals=" + userId);

        // Get all the validationRequestList where user equals to (userId + 1)
        defaultValidationRequestShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllValidationRequestsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);
        User createdBy = UserResourceIT.createEntity(em);
        em.persist(createdBy);
        em.flush();
        validationRequest.setCreatedBy(createdBy);
        validationRequestRepository.saveAndFlush(validationRequest);
        Long createdById = createdBy.getId();

        // Get all the validationRequestList where createdBy equals to createdById
        defaultValidationRequestShouldBeFound("createdById.equals=" + createdById);

        // Get all the validationRequestList where createdBy equals to (createdById + 1)
        defaultValidationRequestShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllValidationRequestsByAcceptedByIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);
        User acceptedBy = UserResourceIT.createEntity(em);
        em.persist(acceptedBy);
        em.flush();
        validationRequest.setAcceptedBy(acceptedBy);
        validationRequestRepository.saveAndFlush(validationRequest);
        Long acceptedById = acceptedBy.getId();

        // Get all the validationRequestList where acceptedBy equals to acceptedById
        defaultValidationRequestShouldBeFound("acceptedById.equals=" + acceptedById);

        // Get all the validationRequestList where acceptedBy equals to (acceptedById + 1)
        defaultValidationRequestShouldNotBeFound("acceptedById.equals=" + (acceptedById + 1));
    }

    @Test
    @Transactional
    void getAllValidationRequestsByFilesIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);
        ValidationRequestFile files = ValidationRequestFileResourceIT.createEntity(em);
        em.persist(files);
        em.flush();
        validationRequest.addFiles(files);
        validationRequestRepository.saveAndFlush(validationRequest);
        Long filesId = files.getId();

        // Get all the validationRequestList where files equals to filesId
        defaultValidationRequestShouldBeFound("filesId.equals=" + filesId);

        // Get all the validationRequestList where files equals to (filesId + 1)
        defaultValidationRequestShouldNotBeFound("filesId.equals=" + (filesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultValidationRequestShouldBeFound(String filter) throws Exception {
        restValidationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].actionDateTime").value(hasItem(DEFAULT_ACTION_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restValidationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultValidationRequestShouldNotBeFound(String filter) throws Exception {
        restValidationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restValidationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingValidationRequest() throws Exception {
        // Get the validationRequest
        restValidationRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewValidationRequest() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();

        // Update the validationRequest
        ValidationRequest updatedValidationRequest = validationRequestRepository.findById(validationRequest.getId()).get();
        // Disconnect from session so that the updates on updatedValidationRequest are not directly saved in db
        em.detach(updatedValidationRequest);
        updatedValidationRequest
            .status(UPDATED_STATUS)
            .actionDateTime(UPDATED_ACTION_DATE_TIME)
            .reason(UPDATED_REASON)
            .notes(UPDATED_NOTES);
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(updatedValidationRequest);

        restValidationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, validationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
        ValidationRequest testValidationRequest = validationRequestList.get(validationRequestList.size() - 1);
        assertThat(testValidationRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testValidationRequest.getActionDateTime()).isEqualTo(UPDATED_ACTION_DATE_TIME);
        assertThat(testValidationRequest.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testValidationRequest.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingValidationRequest() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();
        validationRequest.setId(count.incrementAndGet());

        // Create the ValidationRequest
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(validationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, validationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchValidationRequest() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();
        validationRequest.setId(count.incrementAndGet());

        // Create the ValidationRequest
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(validationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamValidationRequest() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();
        validationRequest.setId(count.incrementAndGet());

        // Create the ValidationRequest
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(validationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateValidationRequestWithPatch() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();

        // Update the validationRequest using partial update
        ValidationRequest partialUpdatedValidationRequest = new ValidationRequest();
        partialUpdatedValidationRequest.setId(validationRequest.getId());

        partialUpdatedValidationRequest.status(UPDATED_STATUS).reason(UPDATED_REASON);

        restValidationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValidationRequest))
            )
            .andExpect(status().isOk());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
        ValidationRequest testValidationRequest = validationRequestList.get(validationRequestList.size() - 1);
        assertThat(testValidationRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testValidationRequest.getActionDateTime()).isEqualTo(DEFAULT_ACTION_DATE_TIME);
        assertThat(testValidationRequest.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testValidationRequest.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateValidationRequestWithPatch() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();

        // Update the validationRequest using partial update
        ValidationRequest partialUpdatedValidationRequest = new ValidationRequest();
        partialUpdatedValidationRequest.setId(validationRequest.getId());

        partialUpdatedValidationRequest
            .status(UPDATED_STATUS)
            .actionDateTime(UPDATED_ACTION_DATE_TIME)
            .reason(UPDATED_REASON)
            .notes(UPDATED_NOTES);

        restValidationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValidationRequest))
            )
            .andExpect(status().isOk());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
        ValidationRequest testValidationRequest = validationRequestList.get(validationRequestList.size() - 1);
        assertThat(testValidationRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testValidationRequest.getActionDateTime()).isEqualTo(UPDATED_ACTION_DATE_TIME);
        assertThat(testValidationRequest.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testValidationRequest.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingValidationRequest() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();
        validationRequest.setId(count.incrementAndGet());

        // Create the ValidationRequest
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(validationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, validationRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchValidationRequest() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();
        validationRequest.setId(count.incrementAndGet());

        // Create the ValidationRequest
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(validationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamValidationRequest() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestRepository.findAll().size();
        validationRequest.setId(count.incrementAndGet());

        // Create the ValidationRequest
        ValidationRequestDTO validationRequestDTO = validationRequestMapper.toDto(validationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValidationRequest in the database
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteValidationRequest() throws Exception {
        // Initialize the database
        validationRequestRepository.saveAndFlush(validationRequest);

        int databaseSizeBeforeDelete = validationRequestRepository.findAll().size();

        // Delete the validationRequest
        restValidationRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, validationRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValidationRequest> validationRequestList = validationRequestRepository.findAll();
        assertThat(validationRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
