package com.balsam.test1.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.balsam.test1.IntegrationTest;
import com.balsam.test1.domain.Corporate;
import com.balsam.test1.domain.Country;
import com.balsam.test1.domain.enumeration.ClientSize;
import com.balsam.test1.domain.enumeration.EmployeeSize;
import com.balsam.test1.repository.CorporateRepository;
import com.balsam.test1.service.criteria.CorporateCriteria;
import com.balsam.test1.service.dto.CorporateDTO;
import com.balsam.test1.service.mapper.CorporateMapper;
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
 * Integration tests for the {@link CorporateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorporateResourceIT {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final EmployeeSize DEFAULT_EMPLOYEE_SIZE = EmployeeSize.LESSTHAN10;
    private static final EmployeeSize UPDATED_EMPLOYEE_SIZE = EmployeeSize.BETWEEN10AND50;

    private static final ClientSize DEFAULT_CLIENT_SIZE = ClientSize.LESSTHAN1000;
    private static final ClientSize UPDATED_CLIENT_SIZE = ClientSize.BETWEEN1000AND5000;

    private static final String DEFAULT_EMAIL = "@253.4.225.253";
    private static final String UPDATED_EMAIL = "9@oYBjzjS-5AgCoxM7LolhIfehK8v7U3VBu.tgviu.gr";

    private static final String DEFAULT_WEBSITE = "file:\\/\\/?U";
    private static final String UPDATED_WEBSITE = "ftp:\\/\\/e5";

    private static final String ENTITY_API_URL = "/api/corporates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CorporateRepository corporateRepository;

    @Autowired
    private CorporateMapper corporateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorporateMockMvc;

    private Corporate corporate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corporate createEntity(EntityManager em) {
        Corporate corporate = new Corporate()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .contactPerson(DEFAULT_CONTACT_PERSON)
            .employeeSize(DEFAULT_EMPLOYEE_SIZE)
            .clientSize(DEFAULT_CLIENT_SIZE)
            .email(DEFAULT_EMAIL)
            .website(DEFAULT_WEBSITE);
        return corporate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corporate createUpdatedEntity(EntityManager em) {
        Corporate corporate = new Corporate()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .employeeSize(UPDATED_EMPLOYEE_SIZE)
            .clientSize(UPDATED_CLIENT_SIZE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);
        return corporate;
    }

    @BeforeEach
    public void initTest() {
        corporate = createEntity(em);
    }

    @Test
    @Transactional
    void createCorporate() throws Exception {
        int databaseSizeBeforeCreate = corporateRepository.findAll().size();
        // Create the Corporate
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);
        restCorporateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporateDTO)))
            .andExpect(status().isCreated());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeCreate + 1);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCorporate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCorporate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCorporate.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCorporate.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testCorporate.getContactPerson()).isEqualTo(DEFAULT_CONTACT_PERSON);
        assertThat(testCorporate.getEmployeeSize()).isEqualTo(DEFAULT_EMPLOYEE_SIZE);
        assertThat(testCorporate.getClientSize()).isEqualTo(DEFAULT_CLIENT_SIZE);
        assertThat(testCorporate.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCorporate.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
    }

    @Test
    @Transactional
    void createCorporateWithExistingId() throws Exception {
        // Create the Corporate with an existing ID
        corporate.setId(1L);
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        int databaseSizeBeforeCreate = corporateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorporateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = corporateRepository.findAll().size();
        // set the field null
        corporate.setCode(null);

        // Create the Corporate, which fails.
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        restCorporateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporateDTO)))
            .andExpect(status().isBadRequest());

        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = corporateRepository.findAll().size();
        // set the field null
        corporate.setName(null);

        // Create the Corporate, which fails.
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        restCorporateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporateDTO)))
            .andExpect(status().isBadRequest());

        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmployeeSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = corporateRepository.findAll().size();
        // set the field null
        corporate.setEmployeeSize(null);

        // Create the Corporate, which fails.
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        restCorporateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporateDTO)))
            .andExpect(status().isBadRequest());

        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClientSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = corporateRepository.findAll().size();
        // set the field null
        corporate.setClientSize(null);

        // Create the Corporate, which fails.
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        restCorporateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporateDTO)))
            .andExpect(status().isBadRequest());

        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCorporates() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList
        restCorporateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporate.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].employeeSize").value(hasItem(DEFAULT_EMPLOYEE_SIZE.toString())))
            .andExpect(jsonPath("$.[*].clientSize").value(hasItem(DEFAULT_CLIENT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));
    }

    @Test
    @Transactional
    void getCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get the corporate
        restCorporateMockMvc
            .perform(get(ENTITY_API_URL_ID, corporate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(corporate.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.employeeSize").value(DEFAULT_EMPLOYEE_SIZE.toString()))
            .andExpect(jsonPath("$.clientSize").value(DEFAULT_CLIENT_SIZE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE));
    }

    @Test
    @Transactional
    void getCorporatesByIdFiltering() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        Long id = corporate.getId();

        defaultCorporateShouldBeFound("id.equals=" + id);
        defaultCorporateShouldNotBeFound("id.notEquals=" + id);

        defaultCorporateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCorporateShouldNotBeFound("id.greaterThan=" + id);

        defaultCorporateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCorporateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCorporatesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where code equals to DEFAULT_CODE
        defaultCorporateShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the corporateList where code equals to UPDATED_CODE
        defaultCorporateShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCorporatesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where code not equals to DEFAULT_CODE
        defaultCorporateShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the corporateList where code not equals to UPDATED_CODE
        defaultCorporateShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCorporatesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCorporateShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the corporateList where code equals to UPDATED_CODE
        defaultCorporateShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCorporatesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where code is not null
        defaultCorporateShouldBeFound("code.specified=true");

        // Get all the corporateList where code is null
        defaultCorporateShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporatesByCodeContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where code contains DEFAULT_CODE
        defaultCorporateShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the corporateList where code contains UPDATED_CODE
        defaultCorporateShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCorporatesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where code does not contain DEFAULT_CODE
        defaultCorporateShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the corporateList where code does not contain UPDATED_CODE
        defaultCorporateShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCorporatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where name equals to DEFAULT_NAME
        defaultCorporateShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the corporateList where name equals to UPDATED_NAME
        defaultCorporateShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporatesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where name not equals to DEFAULT_NAME
        defaultCorporateShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the corporateList where name not equals to UPDATED_NAME
        defaultCorporateShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCorporateShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the corporateList where name equals to UPDATED_NAME
        defaultCorporateShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where name is not null
        defaultCorporateShouldBeFound("name.specified=true");

        // Get all the corporateList where name is null
        defaultCorporateShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporatesByNameContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where name contains DEFAULT_NAME
        defaultCorporateShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the corporateList where name contains UPDATED_NAME
        defaultCorporateShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where name does not contain DEFAULT_NAME
        defaultCorporateShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the corporateList where name does not contain UPDATED_NAME
        defaultCorporateShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporatesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where description equals to DEFAULT_DESCRIPTION
        defaultCorporateShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the corporateList where description equals to UPDATED_DESCRIPTION
        defaultCorporateShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCorporatesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where description not equals to DEFAULT_DESCRIPTION
        defaultCorporateShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the corporateList where description not equals to UPDATED_DESCRIPTION
        defaultCorporateShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCorporatesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCorporateShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the corporateList where description equals to UPDATED_DESCRIPTION
        defaultCorporateShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCorporatesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where description is not null
        defaultCorporateShouldBeFound("description.specified=true");

        // Get all the corporateList where description is null
        defaultCorporateShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporatesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where description contains DEFAULT_DESCRIPTION
        defaultCorporateShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the corporateList where description contains UPDATED_DESCRIPTION
        defaultCorporateShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCorporatesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where description does not contain DEFAULT_DESCRIPTION
        defaultCorporateShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the corporateList where description does not contain UPDATED_DESCRIPTION
        defaultCorporateShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCorporatesByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where contactPerson equals to DEFAULT_CONTACT_PERSON
        defaultCorporateShouldBeFound("contactPerson.equals=" + DEFAULT_CONTACT_PERSON);

        // Get all the corporateList where contactPerson equals to UPDATED_CONTACT_PERSON
        defaultCorporateShouldNotBeFound("contactPerson.equals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllCorporatesByContactPersonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where contactPerson not equals to DEFAULT_CONTACT_PERSON
        defaultCorporateShouldNotBeFound("contactPerson.notEquals=" + DEFAULT_CONTACT_PERSON);

        // Get all the corporateList where contactPerson not equals to UPDATED_CONTACT_PERSON
        defaultCorporateShouldBeFound("contactPerson.notEquals=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllCorporatesByContactPersonIsInShouldWork() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where contactPerson in DEFAULT_CONTACT_PERSON or UPDATED_CONTACT_PERSON
        defaultCorporateShouldBeFound("contactPerson.in=" + DEFAULT_CONTACT_PERSON + "," + UPDATED_CONTACT_PERSON);

        // Get all the corporateList where contactPerson equals to UPDATED_CONTACT_PERSON
        defaultCorporateShouldNotBeFound("contactPerson.in=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllCorporatesByContactPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where contactPerson is not null
        defaultCorporateShouldBeFound("contactPerson.specified=true");

        // Get all the corporateList where contactPerson is null
        defaultCorporateShouldNotBeFound("contactPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporatesByContactPersonContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where contactPerson contains DEFAULT_CONTACT_PERSON
        defaultCorporateShouldBeFound("contactPerson.contains=" + DEFAULT_CONTACT_PERSON);

        // Get all the corporateList where contactPerson contains UPDATED_CONTACT_PERSON
        defaultCorporateShouldNotBeFound("contactPerson.contains=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllCorporatesByContactPersonNotContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where contactPerson does not contain DEFAULT_CONTACT_PERSON
        defaultCorporateShouldNotBeFound("contactPerson.doesNotContain=" + DEFAULT_CONTACT_PERSON);

        // Get all the corporateList where contactPerson does not contain UPDATED_CONTACT_PERSON
        defaultCorporateShouldBeFound("contactPerson.doesNotContain=" + UPDATED_CONTACT_PERSON);
    }

    @Test
    @Transactional
    void getAllCorporatesByEmployeeSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where employeeSize equals to DEFAULT_EMPLOYEE_SIZE
        defaultCorporateShouldBeFound("employeeSize.equals=" + DEFAULT_EMPLOYEE_SIZE);

        // Get all the corporateList where employeeSize equals to UPDATED_EMPLOYEE_SIZE
        defaultCorporateShouldNotBeFound("employeeSize.equals=" + UPDATED_EMPLOYEE_SIZE);
    }

    @Test
    @Transactional
    void getAllCorporatesByEmployeeSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where employeeSize not equals to DEFAULT_EMPLOYEE_SIZE
        defaultCorporateShouldNotBeFound("employeeSize.notEquals=" + DEFAULT_EMPLOYEE_SIZE);

        // Get all the corporateList where employeeSize not equals to UPDATED_EMPLOYEE_SIZE
        defaultCorporateShouldBeFound("employeeSize.notEquals=" + UPDATED_EMPLOYEE_SIZE);
    }

    @Test
    @Transactional
    void getAllCorporatesByEmployeeSizeIsInShouldWork() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where employeeSize in DEFAULT_EMPLOYEE_SIZE or UPDATED_EMPLOYEE_SIZE
        defaultCorporateShouldBeFound("employeeSize.in=" + DEFAULT_EMPLOYEE_SIZE + "," + UPDATED_EMPLOYEE_SIZE);

        // Get all the corporateList where employeeSize equals to UPDATED_EMPLOYEE_SIZE
        defaultCorporateShouldNotBeFound("employeeSize.in=" + UPDATED_EMPLOYEE_SIZE);
    }

    @Test
    @Transactional
    void getAllCorporatesByEmployeeSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where employeeSize is not null
        defaultCorporateShouldBeFound("employeeSize.specified=true");

        // Get all the corporateList where employeeSize is null
        defaultCorporateShouldNotBeFound("employeeSize.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporatesByClientSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where clientSize equals to DEFAULT_CLIENT_SIZE
        defaultCorporateShouldBeFound("clientSize.equals=" + DEFAULT_CLIENT_SIZE);

        // Get all the corporateList where clientSize equals to UPDATED_CLIENT_SIZE
        defaultCorporateShouldNotBeFound("clientSize.equals=" + UPDATED_CLIENT_SIZE);
    }

    @Test
    @Transactional
    void getAllCorporatesByClientSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where clientSize not equals to DEFAULT_CLIENT_SIZE
        defaultCorporateShouldNotBeFound("clientSize.notEquals=" + DEFAULT_CLIENT_SIZE);

        // Get all the corporateList where clientSize not equals to UPDATED_CLIENT_SIZE
        defaultCorporateShouldBeFound("clientSize.notEquals=" + UPDATED_CLIENT_SIZE);
    }

    @Test
    @Transactional
    void getAllCorporatesByClientSizeIsInShouldWork() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where clientSize in DEFAULT_CLIENT_SIZE or UPDATED_CLIENT_SIZE
        defaultCorporateShouldBeFound("clientSize.in=" + DEFAULT_CLIENT_SIZE + "," + UPDATED_CLIENT_SIZE);

        // Get all the corporateList where clientSize equals to UPDATED_CLIENT_SIZE
        defaultCorporateShouldNotBeFound("clientSize.in=" + UPDATED_CLIENT_SIZE);
    }

    @Test
    @Transactional
    void getAllCorporatesByClientSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where clientSize is not null
        defaultCorporateShouldBeFound("clientSize.specified=true");

        // Get all the corporateList where clientSize is null
        defaultCorporateShouldNotBeFound("clientSize.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporatesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where email equals to DEFAULT_EMAIL
        defaultCorporateShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the corporateList where email equals to UPDATED_EMAIL
        defaultCorporateShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorporatesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where email not equals to DEFAULT_EMAIL
        defaultCorporateShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the corporateList where email not equals to UPDATED_EMAIL
        defaultCorporateShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorporatesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCorporateShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the corporateList where email equals to UPDATED_EMAIL
        defaultCorporateShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorporatesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where email is not null
        defaultCorporateShouldBeFound("email.specified=true");

        // Get all the corporateList where email is null
        defaultCorporateShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporatesByEmailContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where email contains DEFAULT_EMAIL
        defaultCorporateShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the corporateList where email contains UPDATED_EMAIL
        defaultCorporateShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorporatesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where email does not contain DEFAULT_EMAIL
        defaultCorporateShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the corporateList where email does not contain UPDATED_EMAIL
        defaultCorporateShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorporatesByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where website equals to DEFAULT_WEBSITE
        defaultCorporateShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the corporateList where website equals to UPDATED_WEBSITE
        defaultCorporateShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCorporatesByWebsiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where website not equals to DEFAULT_WEBSITE
        defaultCorporateShouldNotBeFound("website.notEquals=" + DEFAULT_WEBSITE);

        // Get all the corporateList where website not equals to UPDATED_WEBSITE
        defaultCorporateShouldBeFound("website.notEquals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCorporatesByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultCorporateShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the corporateList where website equals to UPDATED_WEBSITE
        defaultCorporateShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCorporatesByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where website is not null
        defaultCorporateShouldBeFound("website.specified=true");

        // Get all the corporateList where website is null
        defaultCorporateShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporatesByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where website contains DEFAULT_WEBSITE
        defaultCorporateShouldBeFound("website.contains=" + DEFAULT_WEBSITE);

        // Get all the corporateList where website contains UPDATED_WEBSITE
        defaultCorporateShouldNotBeFound("website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCorporatesByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList where website does not contain DEFAULT_WEBSITE
        defaultCorporateShouldNotBeFound("website.doesNotContain=" + DEFAULT_WEBSITE);

        // Get all the corporateList where website does not contain UPDATED_WEBSITE
        defaultCorporateShouldBeFound("website.doesNotContain=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCorporatesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);
        Country country = CountryResourceIT.createEntity(em);
        em.persist(country);
        em.flush();
        corporate.setCountry(country);
        corporateRepository.saveAndFlush(corporate);
        Long countryId = country.getId();

        // Get all the corporateList where country equals to countryId
        defaultCorporateShouldBeFound("countryId.equals=" + countryId);

        // Get all the corporateList where country equals to (countryId + 1)
        defaultCorporateShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorporateShouldBeFound(String filter) throws Exception {
        restCorporateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporate.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].employeeSize").value(hasItem(DEFAULT_EMPLOYEE_SIZE.toString())))
            .andExpect(jsonPath("$.[*].clientSize").value(hasItem(DEFAULT_CLIENT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));

        // Check, that the count call also returns 1
        restCorporateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorporateShouldNotBeFound(String filter) throws Exception {
        restCorporateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorporateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCorporate() throws Exception {
        // Get the corporate
        restCorporateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Update the corporate
        Corporate updatedCorporate = corporateRepository.findById(corporate.getId()).get();
        // Disconnect from session so that the updates on updatedCorporate are not directly saved in db
        em.detach(updatedCorporate);
        updatedCorporate
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .employeeSize(UPDATED_EMPLOYEE_SIZE)
            .clientSize(UPDATED_CLIENT_SIZE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);
        CorporateDTO corporateDTO = corporateMapper.toDto(updatedCorporate);

        restCorporateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corporateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCorporate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCorporate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCorporate.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCorporate.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testCorporate.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testCorporate.getEmployeeSize()).isEqualTo(UPDATED_EMPLOYEE_SIZE);
        assertThat(testCorporate.getClientSize()).isEqualTo(UPDATED_CLIENT_SIZE);
        assertThat(testCorporate.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCorporate.getWebsite()).isEqualTo(UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void putNonExistingCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(count.incrementAndGet());

        // Create the Corporate
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corporateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(count.incrementAndGet());

        // Create the Corporate
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(count.incrementAndGet());

        // Create the Corporate
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(corporateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorporateWithPatch() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Update the corporate using partial update
        Corporate partialUpdatedCorporate = new Corporate();
        partialUpdatedCorporate.setId(corporate.getId());

        partialUpdatedCorporate
            .name(UPDATED_NAME)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .employeeSize(UPDATED_EMPLOYEE_SIZE)
            .clientSize(UPDATED_CLIENT_SIZE)
            .email(UPDATED_EMAIL);

        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorporate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorporate))
            )
            .andExpect(status().isOk());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCorporate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCorporate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCorporate.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCorporate.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testCorporate.getContactPerson()).isEqualTo(DEFAULT_CONTACT_PERSON);
        assertThat(testCorporate.getEmployeeSize()).isEqualTo(UPDATED_EMPLOYEE_SIZE);
        assertThat(testCorporate.getClientSize()).isEqualTo(UPDATED_CLIENT_SIZE);
        assertThat(testCorporate.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCorporate.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
    }

    @Test
    @Transactional
    void fullUpdateCorporateWithPatch() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Update the corporate using partial update
        Corporate partialUpdatedCorporate = new Corporate();
        partialUpdatedCorporate.setId(corporate.getId());

        partialUpdatedCorporate
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .employeeSize(UPDATED_EMPLOYEE_SIZE)
            .clientSize(UPDATED_CLIENT_SIZE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);

        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorporate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorporate))
            )
            .andExpect(status().isOk());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCorporate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCorporate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCorporate.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCorporate.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testCorporate.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testCorporate.getEmployeeSize()).isEqualTo(UPDATED_EMPLOYEE_SIZE);
        assertThat(testCorporate.getClientSize()).isEqualTo(UPDATED_CLIENT_SIZE);
        assertThat(testCorporate.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCorporate.getWebsite()).isEqualTo(UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void patchNonExistingCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(count.incrementAndGet());

        // Create the Corporate
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, corporateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corporateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(count.incrementAndGet());

        // Create the Corporate
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corporateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();
        corporate.setId(count.incrementAndGet());

        // Create the Corporate
        CorporateDTO corporateDTO = corporateMapper.toDto(corporate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(corporateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeDelete = corporateRepository.findAll().size();

        // Delete the corporate
        restCorporateMockMvc
            .perform(delete(ENTITY_API_URL_ID, corporate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
