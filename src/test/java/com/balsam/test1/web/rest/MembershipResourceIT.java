package com.balsam.test1.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.balsam.test1.IntegrationTest;
import com.balsam.test1.domain.Corporate;
import com.balsam.test1.domain.Membership;
import com.balsam.test1.domain.ServicePackage;
import com.balsam.test1.domain.User;
import com.balsam.test1.domain.enumeration.MemberType;
import com.balsam.test1.repository.MembershipRepository;
import com.balsam.test1.service.criteria.MembershipCriteria;
import com.balsam.test1.service.dto.MembershipDTO;
import com.balsam.test1.service.mapper.MembershipMapper;
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
 * Integration tests for the {@link MembershipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MembershipResourceIT {

    private static final String DEFAULT_MEMBERSHIP_ID = "AAAAAAAAAA";
    private static final String UPDATED_MEMBERSHIP_ID = "BBBBBBBBBB";

    private static final MemberType DEFAULT_MEMBER_TYPE = MemberType.SPOUSE;
    private static final MemberType UPDATED_MEMBER_TYPE = MemberType.CHILD;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_HAS_PHYSICAL_VERSION = false;
    private static final Boolean UPDATED_HAS_PHYSICAL_VERSION = true;

    private static final String DEFAULT_MEMBER_SHARE = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_SHARE = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_SHARE = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_SHARE = "BBBBBBBBBB";

    private static final Instant DEFAULT_PRINTING_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRINTING_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/memberships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipMockMvc;

    private Membership membership;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Membership createEntity(EntityManager em) {
        Membership membership = new Membership()
            .membershipId(DEFAULT_MEMBERSHIP_ID)
            .memberType(DEFAULT_MEMBER_TYPE)
            .active(DEFAULT_ACTIVE)
            .hasPhysicalVersion(DEFAULT_HAS_PHYSICAL_VERSION)
            .memberShare(DEFAULT_MEMBER_SHARE)
            .corporateShare(DEFAULT_CORPORATE_SHARE)
            .printingDateTime(DEFAULT_PRINTING_DATE_TIME);
        return membership;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Membership createUpdatedEntity(EntityManager em) {
        Membership membership = new Membership()
            .membershipId(UPDATED_MEMBERSHIP_ID)
            .memberType(UPDATED_MEMBER_TYPE)
            .active(UPDATED_ACTIVE)
            .hasPhysicalVersion(UPDATED_HAS_PHYSICAL_VERSION)
            .memberShare(UPDATED_MEMBER_SHARE)
            .corporateShare(UPDATED_CORPORATE_SHARE)
            .printingDateTime(UPDATED_PRINTING_DATE_TIME);
        return membership;
    }

    @BeforeEach
    public void initTest() {
        membership = createEntity(em);
    }

    @Test
    @Transactional
    void createMembership() throws Exception {
        int databaseSizeBeforeCreate = membershipRepository.findAll().size();
        // Create the Membership
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);
        restMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipDTO)))
            .andExpect(status().isCreated());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeCreate + 1);
        Membership testMembership = membershipList.get(membershipList.size() - 1);
        assertThat(testMembership.getMembershipId()).isEqualTo(DEFAULT_MEMBERSHIP_ID);
        assertThat(testMembership.getMemberType()).isEqualTo(DEFAULT_MEMBER_TYPE);
        assertThat(testMembership.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testMembership.getHasPhysicalVersion()).isEqualTo(DEFAULT_HAS_PHYSICAL_VERSION);
        assertThat(testMembership.getMemberShare()).isEqualTo(DEFAULT_MEMBER_SHARE);
        assertThat(testMembership.getCorporateShare()).isEqualTo(DEFAULT_CORPORATE_SHARE);
        assertThat(testMembership.getPrintingDateTime()).isEqualTo(DEFAULT_PRINTING_DATE_TIME);
    }

    @Test
    @Transactional
    void createMembershipWithExistingId() throws Exception {
        // Create the Membership with an existing ID
        membership.setId(1L);
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);

        int databaseSizeBeforeCreate = membershipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMembershipIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipRepository.findAll().size();
        // set the field null
        membership.setMembershipId(null);

        // Create the Membership, which fails.
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);

        restMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipDTO)))
            .andExpect(status().isBadRequest());

        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemberships() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList
        restMembershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membership.getId().intValue())))
            .andExpect(jsonPath("$.[*].membershipId").value(hasItem(DEFAULT_MEMBERSHIP_ID)))
            .andExpect(jsonPath("$.[*].memberType").value(hasItem(DEFAULT_MEMBER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].hasPhysicalVersion").value(hasItem(DEFAULT_HAS_PHYSICAL_VERSION.booleanValue())))
            .andExpect(jsonPath("$.[*].memberShare").value(hasItem(DEFAULT_MEMBER_SHARE)))
            .andExpect(jsonPath("$.[*].corporateShare").value(hasItem(DEFAULT_CORPORATE_SHARE)))
            .andExpect(jsonPath("$.[*].printingDateTime").value(hasItem(DEFAULT_PRINTING_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getMembership() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get the membership
        restMembershipMockMvc
            .perform(get(ENTITY_API_URL_ID, membership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membership.getId().intValue()))
            .andExpect(jsonPath("$.membershipId").value(DEFAULT_MEMBERSHIP_ID))
            .andExpect(jsonPath("$.memberType").value(DEFAULT_MEMBER_TYPE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.hasPhysicalVersion").value(DEFAULT_HAS_PHYSICAL_VERSION.booleanValue()))
            .andExpect(jsonPath("$.memberShare").value(DEFAULT_MEMBER_SHARE))
            .andExpect(jsonPath("$.corporateShare").value(DEFAULT_CORPORATE_SHARE))
            .andExpect(jsonPath("$.printingDateTime").value(DEFAULT_PRINTING_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getMembershipsByIdFiltering() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        Long id = membership.getId();

        defaultMembershipShouldBeFound("id.equals=" + id);
        defaultMembershipShouldNotBeFound("id.notEquals=" + id);

        defaultMembershipShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMembershipShouldNotBeFound("id.greaterThan=" + id);

        defaultMembershipShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMembershipShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMembershipsByMembershipIdIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where membershipId equals to DEFAULT_MEMBERSHIP_ID
        defaultMembershipShouldBeFound("membershipId.equals=" + DEFAULT_MEMBERSHIP_ID);

        // Get all the membershipList where membershipId equals to UPDATED_MEMBERSHIP_ID
        defaultMembershipShouldNotBeFound("membershipId.equals=" + UPDATED_MEMBERSHIP_ID);
    }

    @Test
    @Transactional
    void getAllMembershipsByMembershipIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where membershipId not equals to DEFAULT_MEMBERSHIP_ID
        defaultMembershipShouldNotBeFound("membershipId.notEquals=" + DEFAULT_MEMBERSHIP_ID);

        // Get all the membershipList where membershipId not equals to UPDATED_MEMBERSHIP_ID
        defaultMembershipShouldBeFound("membershipId.notEquals=" + UPDATED_MEMBERSHIP_ID);
    }

    @Test
    @Transactional
    void getAllMembershipsByMembershipIdIsInShouldWork() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where membershipId in DEFAULT_MEMBERSHIP_ID or UPDATED_MEMBERSHIP_ID
        defaultMembershipShouldBeFound("membershipId.in=" + DEFAULT_MEMBERSHIP_ID + "," + UPDATED_MEMBERSHIP_ID);

        // Get all the membershipList where membershipId equals to UPDATED_MEMBERSHIP_ID
        defaultMembershipShouldNotBeFound("membershipId.in=" + UPDATED_MEMBERSHIP_ID);
    }

    @Test
    @Transactional
    void getAllMembershipsByMembershipIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where membershipId is not null
        defaultMembershipShouldBeFound("membershipId.specified=true");

        // Get all the membershipList where membershipId is null
        defaultMembershipShouldNotBeFound("membershipId.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipsByMembershipIdContainsSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where membershipId contains DEFAULT_MEMBERSHIP_ID
        defaultMembershipShouldBeFound("membershipId.contains=" + DEFAULT_MEMBERSHIP_ID);

        // Get all the membershipList where membershipId contains UPDATED_MEMBERSHIP_ID
        defaultMembershipShouldNotBeFound("membershipId.contains=" + UPDATED_MEMBERSHIP_ID);
    }

    @Test
    @Transactional
    void getAllMembershipsByMembershipIdNotContainsSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where membershipId does not contain DEFAULT_MEMBERSHIP_ID
        defaultMembershipShouldNotBeFound("membershipId.doesNotContain=" + DEFAULT_MEMBERSHIP_ID);

        // Get all the membershipList where membershipId does not contain UPDATED_MEMBERSHIP_ID
        defaultMembershipShouldBeFound("membershipId.doesNotContain=" + UPDATED_MEMBERSHIP_ID);
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberType equals to DEFAULT_MEMBER_TYPE
        defaultMembershipShouldBeFound("memberType.equals=" + DEFAULT_MEMBER_TYPE);

        // Get all the membershipList where memberType equals to UPDATED_MEMBER_TYPE
        defaultMembershipShouldNotBeFound("memberType.equals=" + UPDATED_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberType not equals to DEFAULT_MEMBER_TYPE
        defaultMembershipShouldNotBeFound("memberType.notEquals=" + DEFAULT_MEMBER_TYPE);

        // Get all the membershipList where memberType not equals to UPDATED_MEMBER_TYPE
        defaultMembershipShouldBeFound("memberType.notEquals=" + UPDATED_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberTypeIsInShouldWork() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberType in DEFAULT_MEMBER_TYPE or UPDATED_MEMBER_TYPE
        defaultMembershipShouldBeFound("memberType.in=" + DEFAULT_MEMBER_TYPE + "," + UPDATED_MEMBER_TYPE);

        // Get all the membershipList where memberType equals to UPDATED_MEMBER_TYPE
        defaultMembershipShouldNotBeFound("memberType.in=" + UPDATED_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberType is not null
        defaultMembershipShouldBeFound("memberType.specified=true");

        // Get all the membershipList where memberType is null
        defaultMembershipShouldNotBeFound("memberType.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where active equals to DEFAULT_ACTIVE
        defaultMembershipShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the membershipList where active equals to UPDATED_ACTIVE
        defaultMembershipShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMembershipsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where active not equals to DEFAULT_ACTIVE
        defaultMembershipShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the membershipList where active not equals to UPDATED_ACTIVE
        defaultMembershipShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMembershipsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultMembershipShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the membershipList where active equals to UPDATED_ACTIVE
        defaultMembershipShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMembershipsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where active is not null
        defaultMembershipShouldBeFound("active.specified=true");

        // Get all the membershipList where active is null
        defaultMembershipShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipsByHasPhysicalVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where hasPhysicalVersion equals to DEFAULT_HAS_PHYSICAL_VERSION
        defaultMembershipShouldBeFound("hasPhysicalVersion.equals=" + DEFAULT_HAS_PHYSICAL_VERSION);

        // Get all the membershipList where hasPhysicalVersion equals to UPDATED_HAS_PHYSICAL_VERSION
        defaultMembershipShouldNotBeFound("hasPhysicalVersion.equals=" + UPDATED_HAS_PHYSICAL_VERSION);
    }

    @Test
    @Transactional
    void getAllMembershipsByHasPhysicalVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where hasPhysicalVersion not equals to DEFAULT_HAS_PHYSICAL_VERSION
        defaultMembershipShouldNotBeFound("hasPhysicalVersion.notEquals=" + DEFAULT_HAS_PHYSICAL_VERSION);

        // Get all the membershipList where hasPhysicalVersion not equals to UPDATED_HAS_PHYSICAL_VERSION
        defaultMembershipShouldBeFound("hasPhysicalVersion.notEquals=" + UPDATED_HAS_PHYSICAL_VERSION);
    }

    @Test
    @Transactional
    void getAllMembershipsByHasPhysicalVersionIsInShouldWork() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where hasPhysicalVersion in DEFAULT_HAS_PHYSICAL_VERSION or UPDATED_HAS_PHYSICAL_VERSION
        defaultMembershipShouldBeFound("hasPhysicalVersion.in=" + DEFAULT_HAS_PHYSICAL_VERSION + "," + UPDATED_HAS_PHYSICAL_VERSION);

        // Get all the membershipList where hasPhysicalVersion equals to UPDATED_HAS_PHYSICAL_VERSION
        defaultMembershipShouldNotBeFound("hasPhysicalVersion.in=" + UPDATED_HAS_PHYSICAL_VERSION);
    }

    @Test
    @Transactional
    void getAllMembershipsByHasPhysicalVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where hasPhysicalVersion is not null
        defaultMembershipShouldBeFound("hasPhysicalVersion.specified=true");

        // Get all the membershipList where hasPhysicalVersion is null
        defaultMembershipShouldNotBeFound("hasPhysicalVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberShareIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberShare equals to DEFAULT_MEMBER_SHARE
        defaultMembershipShouldBeFound("memberShare.equals=" + DEFAULT_MEMBER_SHARE);

        // Get all the membershipList where memberShare equals to UPDATED_MEMBER_SHARE
        defaultMembershipShouldNotBeFound("memberShare.equals=" + UPDATED_MEMBER_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberShareIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberShare not equals to DEFAULT_MEMBER_SHARE
        defaultMembershipShouldNotBeFound("memberShare.notEquals=" + DEFAULT_MEMBER_SHARE);

        // Get all the membershipList where memberShare not equals to UPDATED_MEMBER_SHARE
        defaultMembershipShouldBeFound("memberShare.notEquals=" + UPDATED_MEMBER_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberShareIsInShouldWork() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberShare in DEFAULT_MEMBER_SHARE or UPDATED_MEMBER_SHARE
        defaultMembershipShouldBeFound("memberShare.in=" + DEFAULT_MEMBER_SHARE + "," + UPDATED_MEMBER_SHARE);

        // Get all the membershipList where memberShare equals to UPDATED_MEMBER_SHARE
        defaultMembershipShouldNotBeFound("memberShare.in=" + UPDATED_MEMBER_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberShareIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberShare is not null
        defaultMembershipShouldBeFound("memberShare.specified=true");

        // Get all the membershipList where memberShare is null
        defaultMembershipShouldNotBeFound("memberShare.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberShareContainsSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberShare contains DEFAULT_MEMBER_SHARE
        defaultMembershipShouldBeFound("memberShare.contains=" + DEFAULT_MEMBER_SHARE);

        // Get all the membershipList where memberShare contains UPDATED_MEMBER_SHARE
        defaultMembershipShouldNotBeFound("memberShare.contains=" + UPDATED_MEMBER_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByMemberShareNotContainsSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where memberShare does not contain DEFAULT_MEMBER_SHARE
        defaultMembershipShouldNotBeFound("memberShare.doesNotContain=" + DEFAULT_MEMBER_SHARE);

        // Get all the membershipList where memberShare does not contain UPDATED_MEMBER_SHARE
        defaultMembershipShouldBeFound("memberShare.doesNotContain=" + UPDATED_MEMBER_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByCorporateShareIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where corporateShare equals to DEFAULT_CORPORATE_SHARE
        defaultMembershipShouldBeFound("corporateShare.equals=" + DEFAULT_CORPORATE_SHARE);

        // Get all the membershipList where corporateShare equals to UPDATED_CORPORATE_SHARE
        defaultMembershipShouldNotBeFound("corporateShare.equals=" + UPDATED_CORPORATE_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByCorporateShareIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where corporateShare not equals to DEFAULT_CORPORATE_SHARE
        defaultMembershipShouldNotBeFound("corporateShare.notEquals=" + DEFAULT_CORPORATE_SHARE);

        // Get all the membershipList where corporateShare not equals to UPDATED_CORPORATE_SHARE
        defaultMembershipShouldBeFound("corporateShare.notEquals=" + UPDATED_CORPORATE_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByCorporateShareIsInShouldWork() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where corporateShare in DEFAULT_CORPORATE_SHARE or UPDATED_CORPORATE_SHARE
        defaultMembershipShouldBeFound("corporateShare.in=" + DEFAULT_CORPORATE_SHARE + "," + UPDATED_CORPORATE_SHARE);

        // Get all the membershipList where corporateShare equals to UPDATED_CORPORATE_SHARE
        defaultMembershipShouldNotBeFound("corporateShare.in=" + UPDATED_CORPORATE_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByCorporateShareIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where corporateShare is not null
        defaultMembershipShouldBeFound("corporateShare.specified=true");

        // Get all the membershipList where corporateShare is null
        defaultMembershipShouldNotBeFound("corporateShare.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipsByCorporateShareContainsSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where corporateShare contains DEFAULT_CORPORATE_SHARE
        defaultMembershipShouldBeFound("corporateShare.contains=" + DEFAULT_CORPORATE_SHARE);

        // Get all the membershipList where corporateShare contains UPDATED_CORPORATE_SHARE
        defaultMembershipShouldNotBeFound("corporateShare.contains=" + UPDATED_CORPORATE_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByCorporateShareNotContainsSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where corporateShare does not contain DEFAULT_CORPORATE_SHARE
        defaultMembershipShouldNotBeFound("corporateShare.doesNotContain=" + DEFAULT_CORPORATE_SHARE);

        // Get all the membershipList where corporateShare does not contain UPDATED_CORPORATE_SHARE
        defaultMembershipShouldBeFound("corporateShare.doesNotContain=" + UPDATED_CORPORATE_SHARE);
    }

    @Test
    @Transactional
    void getAllMembershipsByPrintingDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where printingDateTime equals to DEFAULT_PRINTING_DATE_TIME
        defaultMembershipShouldBeFound("printingDateTime.equals=" + DEFAULT_PRINTING_DATE_TIME);

        // Get all the membershipList where printingDateTime equals to UPDATED_PRINTING_DATE_TIME
        defaultMembershipShouldNotBeFound("printingDateTime.equals=" + UPDATED_PRINTING_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllMembershipsByPrintingDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where printingDateTime not equals to DEFAULT_PRINTING_DATE_TIME
        defaultMembershipShouldNotBeFound("printingDateTime.notEquals=" + DEFAULT_PRINTING_DATE_TIME);

        // Get all the membershipList where printingDateTime not equals to UPDATED_PRINTING_DATE_TIME
        defaultMembershipShouldBeFound("printingDateTime.notEquals=" + UPDATED_PRINTING_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllMembershipsByPrintingDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where printingDateTime in DEFAULT_PRINTING_DATE_TIME or UPDATED_PRINTING_DATE_TIME
        defaultMembershipShouldBeFound("printingDateTime.in=" + DEFAULT_PRINTING_DATE_TIME + "," + UPDATED_PRINTING_DATE_TIME);

        // Get all the membershipList where printingDateTime equals to UPDATED_PRINTING_DATE_TIME
        defaultMembershipShouldNotBeFound("printingDateTime.in=" + UPDATED_PRINTING_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllMembershipsByPrintingDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the membershipList where printingDateTime is not null
        defaultMembershipShouldBeFound("printingDateTime.specified=true");

        // Get all the membershipList where printingDateTime is null
        defaultMembershipShouldNotBeFound("printingDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipsByServicePackageIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);
        ServicePackage servicePackage = ServicePackageResourceIT.createEntity(em);
        em.persist(servicePackage);
        em.flush();
        membership.setServicePackage(servicePackage);
        membershipRepository.saveAndFlush(membership);
        Long servicePackageId = servicePackage.getId();

        // Get all the membershipList where servicePackage equals to servicePackageId
        defaultMembershipShouldBeFound("servicePackageId.equals=" + servicePackageId);

        // Get all the membershipList where servicePackage equals to (servicePackageId + 1)
        defaultMembershipShouldNotBeFound("servicePackageId.equals=" + (servicePackageId + 1));
    }

    @Test
    @Transactional
    void getAllMembershipsByCorporateIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);
        Corporate corporate = CorporateResourceIT.createEntity(em);
        em.persist(corporate);
        em.flush();
        membership.setCorporate(corporate);
        membershipRepository.saveAndFlush(membership);
        Long corporateId = corporate.getId();

        // Get all the membershipList where corporate equals to corporateId
        defaultMembershipShouldBeFound("corporateId.equals=" + corporateId);

        // Get all the membershipList where corporate equals to (corporateId + 1)
        defaultMembershipShouldNotBeFound("corporateId.equals=" + (corporateId + 1));
    }

    @Test
    @Transactional
    void getAllMembershipsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        membership.setUser(user);
        membershipRepository.saveAndFlush(membership);
        Long userId = user.getId();

        // Get all the membershipList where user equals to userId
        defaultMembershipShouldBeFound("userId.equals=" + userId);

        // Get all the membershipList where user equals to (userId + 1)
        defaultMembershipShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMembershipShouldBeFound(String filter) throws Exception {
        restMembershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membership.getId().intValue())))
            .andExpect(jsonPath("$.[*].membershipId").value(hasItem(DEFAULT_MEMBERSHIP_ID)))
            .andExpect(jsonPath("$.[*].memberType").value(hasItem(DEFAULT_MEMBER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].hasPhysicalVersion").value(hasItem(DEFAULT_HAS_PHYSICAL_VERSION.booleanValue())))
            .andExpect(jsonPath("$.[*].memberShare").value(hasItem(DEFAULT_MEMBER_SHARE)))
            .andExpect(jsonPath("$.[*].corporateShare").value(hasItem(DEFAULT_CORPORATE_SHARE)))
            .andExpect(jsonPath("$.[*].printingDateTime").value(hasItem(DEFAULT_PRINTING_DATE_TIME.toString())));

        // Check, that the count call also returns 1
        restMembershipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMembershipShouldNotBeFound(String filter) throws Exception {
        restMembershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMembershipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMembership() throws Exception {
        // Get the membership
        restMembershipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMembership() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();

        // Update the membership
        Membership updatedMembership = membershipRepository.findById(membership.getId()).get();
        // Disconnect from session so that the updates on updatedMembership are not directly saved in db
        em.detach(updatedMembership);
        updatedMembership
            .membershipId(UPDATED_MEMBERSHIP_ID)
            .memberType(UPDATED_MEMBER_TYPE)
            .active(UPDATED_ACTIVE)
            .hasPhysicalVersion(UPDATED_HAS_PHYSICAL_VERSION)
            .memberShare(UPDATED_MEMBER_SHARE)
            .corporateShare(UPDATED_CORPORATE_SHARE)
            .printingDateTime(UPDATED_PRINTING_DATE_TIME);
        MembershipDTO membershipDTO = membershipMapper.toDto(updatedMembership);

        restMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membershipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipDTO))
            )
            .andExpect(status().isOk());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
        Membership testMembership = membershipList.get(membershipList.size() - 1);
        assertThat(testMembership.getMembershipId()).isEqualTo(UPDATED_MEMBERSHIP_ID);
        assertThat(testMembership.getMemberType()).isEqualTo(UPDATED_MEMBER_TYPE);
        assertThat(testMembership.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testMembership.getHasPhysicalVersion()).isEqualTo(UPDATED_HAS_PHYSICAL_VERSION);
        assertThat(testMembership.getMemberShare()).isEqualTo(UPDATED_MEMBER_SHARE);
        assertThat(testMembership.getCorporateShare()).isEqualTo(UPDATED_CORPORATE_SHARE);
        assertThat(testMembership.getPrintingDateTime()).isEqualTo(UPDATED_PRINTING_DATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingMembership() throws Exception {
        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();
        membership.setId(count.incrementAndGet());

        // Create the Membership
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membershipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMembership() throws Exception {
        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();
        membership.setId(count.incrementAndGet());

        // Create the Membership
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMembership() throws Exception {
        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();
        membership.setId(count.incrementAndGet());

        // Create the Membership
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMembershipWithPatch() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();

        // Update the membership using partial update
        Membership partialUpdatedMembership = new Membership();
        partialUpdatedMembership.setId(membership.getId());

        partialUpdatedMembership.hasPhysicalVersion(UPDATED_HAS_PHYSICAL_VERSION).memberShare(UPDATED_MEMBER_SHARE);

        restMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembership))
            )
            .andExpect(status().isOk());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
        Membership testMembership = membershipList.get(membershipList.size() - 1);
        assertThat(testMembership.getMembershipId()).isEqualTo(DEFAULT_MEMBERSHIP_ID);
        assertThat(testMembership.getMemberType()).isEqualTo(DEFAULT_MEMBER_TYPE);
        assertThat(testMembership.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testMembership.getHasPhysicalVersion()).isEqualTo(UPDATED_HAS_PHYSICAL_VERSION);
        assertThat(testMembership.getMemberShare()).isEqualTo(UPDATED_MEMBER_SHARE);
        assertThat(testMembership.getCorporateShare()).isEqualTo(DEFAULT_CORPORATE_SHARE);
        assertThat(testMembership.getPrintingDateTime()).isEqualTo(DEFAULT_PRINTING_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateMembershipWithPatch() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();

        // Update the membership using partial update
        Membership partialUpdatedMembership = new Membership();
        partialUpdatedMembership.setId(membership.getId());

        partialUpdatedMembership
            .membershipId(UPDATED_MEMBERSHIP_ID)
            .memberType(UPDATED_MEMBER_TYPE)
            .active(UPDATED_ACTIVE)
            .hasPhysicalVersion(UPDATED_HAS_PHYSICAL_VERSION)
            .memberShare(UPDATED_MEMBER_SHARE)
            .corporateShare(UPDATED_CORPORATE_SHARE)
            .printingDateTime(UPDATED_PRINTING_DATE_TIME);

        restMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembership))
            )
            .andExpect(status().isOk());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
        Membership testMembership = membershipList.get(membershipList.size() - 1);
        assertThat(testMembership.getMembershipId()).isEqualTo(UPDATED_MEMBERSHIP_ID);
        assertThat(testMembership.getMemberType()).isEqualTo(UPDATED_MEMBER_TYPE);
        assertThat(testMembership.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testMembership.getHasPhysicalVersion()).isEqualTo(UPDATED_HAS_PHYSICAL_VERSION);
        assertThat(testMembership.getMemberShare()).isEqualTo(UPDATED_MEMBER_SHARE);
        assertThat(testMembership.getCorporateShare()).isEqualTo(UPDATED_CORPORATE_SHARE);
        assertThat(testMembership.getPrintingDateTime()).isEqualTo(UPDATED_PRINTING_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingMembership() throws Exception {
        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();
        membership.setId(count.incrementAndGet());

        // Create the Membership
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, membershipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMembership() throws Exception {
        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();
        membership.setId(count.incrementAndGet());

        // Create the Membership
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMembership() throws Exception {
        int databaseSizeBeforeUpdate = membershipRepository.findAll().size();
        membership.setId(count.incrementAndGet());

        // Create the Membership
        MembershipDTO membershipDTO = membershipMapper.toDto(membership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(membershipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Membership in the database
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMembership() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        int databaseSizeBeforeDelete = membershipRepository.findAll().size();

        // Delete the membership
        restMembershipMockMvc
            .perform(delete(ENTITY_API_URL_ID, membership.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
