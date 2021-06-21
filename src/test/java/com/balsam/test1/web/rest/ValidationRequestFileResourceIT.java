package com.balsam.test1.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.balsam.test1.IntegrationTest;
import com.balsam.test1.domain.ValidationRequest;
import com.balsam.test1.domain.ValidationRequestFile;
import com.balsam.test1.domain.enumeration.ValidationRequestFileType;
import com.balsam.test1.repository.ValidationRequestFileRepository;
import com.balsam.test1.service.criteria.ValidationRequestFileCriteria;
import com.balsam.test1.service.dto.ValidationRequestFileDTO;
import com.balsam.test1.service.mapper.ValidationRequestFileMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ValidationRequestFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValidationRequestFileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final ValidationRequestFileType DEFAULT_TYPE = ValidationRequestFileType.PASSPORT;
    private static final ValidationRequestFileType UPDATED_TYPE = ValidationRequestFileType.ID;

    private static final String ENTITY_API_URL = "/api/validation-request-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ValidationRequestFileRepository validationRequestFileRepository;

    @Autowired
    private ValidationRequestFileMapper validationRequestFileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValidationRequestFileMockMvc;

    private ValidationRequestFile validationRequestFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidationRequestFile createEntity(EntityManager em) {
        ValidationRequestFile validationRequestFile = new ValidationRequestFile()
            .name(DEFAULT_NAME)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .type(DEFAULT_TYPE);
        return validationRequestFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidationRequestFile createUpdatedEntity(EntityManager em) {
        ValidationRequestFile validationRequestFile = new ValidationRequestFile()
            .name(UPDATED_NAME)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .type(UPDATED_TYPE);
        return validationRequestFile;
    }

    @BeforeEach
    public void initTest() {
        validationRequestFile = createEntity(em);
    }

    @Test
    @Transactional
    void createValidationRequestFile() throws Exception {
        int databaseSizeBeforeCreate = validationRequestFileRepository.findAll().size();
        // Create the ValidationRequestFile
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);
        restValidationRequestFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeCreate + 1);
        ValidationRequestFile testValidationRequestFile = validationRequestFileList.get(validationRequestFileList.size() - 1);
        assertThat(testValidationRequestFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testValidationRequestFile.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testValidationRequestFile.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testValidationRequestFile.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createValidationRequestFileWithExistingId() throws Exception {
        // Create the ValidationRequestFile with an existing ID
        validationRequestFile.setId(1L);
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);

        int databaseSizeBeforeCreate = validationRequestFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restValidationRequestFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = validationRequestFileRepository.findAll().size();
        // set the field null
        validationRequestFile.setName(null);

        // Create the ValidationRequestFile, which fails.
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);

        restValidationRequestFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isBadRequest());

        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllValidationRequestFiles() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList
        restValidationRequestFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationRequestFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getValidationRequestFile() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get the validationRequestFile
        restValidationRequestFileMockMvc
            .perform(get(ENTITY_API_URL_ID, validationRequestFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(validationRequestFile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getValidationRequestFilesByIdFiltering() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        Long id = validationRequestFile.getId();

        defaultValidationRequestFileShouldBeFound("id.equals=" + id);
        defaultValidationRequestFileShouldNotBeFound("id.notEquals=" + id);

        defaultValidationRequestFileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultValidationRequestFileShouldNotBeFound("id.greaterThan=" + id);

        defaultValidationRequestFileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultValidationRequestFileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where name equals to DEFAULT_NAME
        defaultValidationRequestFileShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the validationRequestFileList where name equals to UPDATED_NAME
        defaultValidationRequestFileShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where name not equals to DEFAULT_NAME
        defaultValidationRequestFileShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the validationRequestFileList where name not equals to UPDATED_NAME
        defaultValidationRequestFileShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where name in DEFAULT_NAME or UPDATED_NAME
        defaultValidationRequestFileShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the validationRequestFileList where name equals to UPDATED_NAME
        defaultValidationRequestFileShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where name is not null
        defaultValidationRequestFileShouldBeFound("name.specified=true");

        // Get all the validationRequestFileList where name is null
        defaultValidationRequestFileShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByNameContainsSomething() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where name contains DEFAULT_NAME
        defaultValidationRequestFileShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the validationRequestFileList where name contains UPDATED_NAME
        defaultValidationRequestFileShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where name does not contain DEFAULT_NAME
        defaultValidationRequestFileShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the validationRequestFileList where name does not contain UPDATED_NAME
        defaultValidationRequestFileShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where type equals to DEFAULT_TYPE
        defaultValidationRequestFileShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the validationRequestFileList where type equals to UPDATED_TYPE
        defaultValidationRequestFileShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where type not equals to DEFAULT_TYPE
        defaultValidationRequestFileShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the validationRequestFileList where type not equals to UPDATED_TYPE
        defaultValidationRequestFileShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultValidationRequestFileShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the validationRequestFileList where type equals to UPDATED_TYPE
        defaultValidationRequestFileShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        // Get all the validationRequestFileList where type is not null
        defaultValidationRequestFileShouldBeFound("type.specified=true");

        // Get all the validationRequestFileList where type is null
        defaultValidationRequestFileShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllValidationRequestFilesByValidationRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);
        ValidationRequest validationRequest = ValidationRequestResourceIT.createEntity(em);
        em.persist(validationRequest);
        em.flush();
        validationRequestFile.setValidationRequest(validationRequest);
        validationRequestFileRepository.saveAndFlush(validationRequestFile);
        Long validationRequestId = validationRequest.getId();

        // Get all the validationRequestFileList where validationRequest equals to validationRequestId
        defaultValidationRequestFileShouldBeFound("validationRequestId.equals=" + validationRequestId);

        // Get all the validationRequestFileList where validationRequest equals to (validationRequestId + 1)
        defaultValidationRequestFileShouldNotBeFound("validationRequestId.equals=" + (validationRequestId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultValidationRequestFileShouldBeFound(String filter) throws Exception {
        restValidationRequestFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationRequestFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restValidationRequestFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultValidationRequestFileShouldNotBeFound(String filter) throws Exception {
        restValidationRequestFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restValidationRequestFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingValidationRequestFile() throws Exception {
        // Get the validationRequestFile
        restValidationRequestFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewValidationRequestFile() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();

        // Update the validationRequestFile
        ValidationRequestFile updatedValidationRequestFile = validationRequestFileRepository.findById(validationRequestFile.getId()).get();
        // Disconnect from session so that the updates on updatedValidationRequestFile are not directly saved in db
        em.detach(updatedValidationRequestFile);
        updatedValidationRequestFile.name(UPDATED_NAME).file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE).type(UPDATED_TYPE);
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(updatedValidationRequestFile);

        restValidationRequestFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, validationRequestFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
        ValidationRequestFile testValidationRequestFile = validationRequestFileList.get(validationRequestFileList.size() - 1);
        assertThat(testValidationRequestFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValidationRequestFile.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testValidationRequestFile.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testValidationRequestFile.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingValidationRequestFile() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();
        validationRequestFile.setId(count.incrementAndGet());

        // Create the ValidationRequestFile
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationRequestFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, validationRequestFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchValidationRequestFile() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();
        validationRequestFile.setId(count.incrementAndGet());

        // Create the ValidationRequestFile
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationRequestFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamValidationRequestFile() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();
        validationRequestFile.setId(count.incrementAndGet());

        // Create the ValidationRequestFile
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationRequestFileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateValidationRequestFileWithPatch() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();

        // Update the validationRequestFile using partial update
        ValidationRequestFile partialUpdatedValidationRequestFile = new ValidationRequestFile();
        partialUpdatedValidationRequestFile.setId(validationRequestFile.getId());

        partialUpdatedValidationRequestFile.file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restValidationRequestFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidationRequestFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValidationRequestFile))
            )
            .andExpect(status().isOk());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
        ValidationRequestFile testValidationRequestFile = validationRequestFileList.get(validationRequestFileList.size() - 1);
        assertThat(testValidationRequestFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testValidationRequestFile.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testValidationRequestFile.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testValidationRequestFile.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateValidationRequestFileWithPatch() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();

        // Update the validationRequestFile using partial update
        ValidationRequestFile partialUpdatedValidationRequestFile = new ValidationRequestFile();
        partialUpdatedValidationRequestFile.setId(validationRequestFile.getId());

        partialUpdatedValidationRequestFile
            .name(UPDATED_NAME)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .type(UPDATED_TYPE);

        restValidationRequestFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidationRequestFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValidationRequestFile))
            )
            .andExpect(status().isOk());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
        ValidationRequestFile testValidationRequestFile = validationRequestFileList.get(validationRequestFileList.size() - 1);
        assertThat(testValidationRequestFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValidationRequestFile.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testValidationRequestFile.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testValidationRequestFile.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingValidationRequestFile() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();
        validationRequestFile.setId(count.incrementAndGet());

        // Create the ValidationRequestFile
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationRequestFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, validationRequestFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchValidationRequestFile() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();
        validationRequestFile.setId(count.incrementAndGet());

        // Create the ValidationRequestFile
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationRequestFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamValidationRequestFile() throws Exception {
        int databaseSizeBeforeUpdate = validationRequestFileRepository.findAll().size();
        validationRequestFile.setId(count.incrementAndGet());

        // Create the ValidationRequestFile
        ValidationRequestFileDTO validationRequestFileDTO = validationRequestFileMapper.toDto(validationRequestFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationRequestFileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validationRequestFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValidationRequestFile in the database
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteValidationRequestFile() throws Exception {
        // Initialize the database
        validationRequestFileRepository.saveAndFlush(validationRequestFile);

        int databaseSizeBeforeDelete = validationRequestFileRepository.findAll().size();

        // Delete the validationRequestFile
        restValidationRequestFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, validationRequestFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValidationRequestFile> validationRequestFileList = validationRequestFileRepository.findAll();
        assertThat(validationRequestFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
