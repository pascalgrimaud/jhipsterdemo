package io.github.pascalgrimaud.web.rest;

import io.github.pascalgrimaud.JhipsterdemoApp;

import io.github.pascalgrimaud.domain.MicroserviceBankAccount;
import io.github.pascalgrimaud.domain.MicroserviceOperation;
import io.github.pascalgrimaud.repository.MicroserviceBankAccountRepository;
import io.github.pascalgrimaud.service.MicroserviceBankAccountService;
import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountDTO;
import io.github.pascalgrimaud.service.mapper.MicroserviceBankAccountMapper;
import io.github.pascalgrimaud.web.rest.errors.ExceptionTranslator;
import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountCriteria;
import io.github.pascalgrimaud.service.MicroserviceBankAccountQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static io.github.pascalgrimaud.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.pascalgrimaud.domain.enumeration.BankAccountType;
/**
 * Test class for the MicroserviceBankAccountResource REST controller.
 *
 * @see MicroserviceBankAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterdemoApp.class)
public class MicroserviceBankAccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_BANK_NUMBER = 1;
    private static final Integer UPDATED_BANK_NUMBER = 2;

    private static final Long DEFAULT_AGENCY_NUMBER = 1L;
    private static final Long UPDATED_AGENCY_NUMBER = 2L;

    private static final Float DEFAULT_LAST_OPERATION_DURATION = 1F;
    private static final Float UPDATED_LAST_OPERATION_DURATION = 2F;

    private static final Double DEFAULT_MEAN_OPERATION_DURATION = 1D;
    private static final Double UPDATED_MEAN_OPERATION_DURATION = 2D;

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final LocalDate DEFAULT_OPENING_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OPENING_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_LAST_OPERATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_OPERATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final BankAccountType DEFAULT_ACCOUNT_TYPE = BankAccountType.CHECKING;
    private static final BankAccountType UPDATED_ACCOUNT_TYPE = BankAccountType.SAVINGS;

    private static final byte[] DEFAULT_ATTACHMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ATTACHMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MicroserviceBankAccountRepository microserviceBankAccountRepository;


    @Autowired
    private MicroserviceBankAccountMapper microserviceBankAccountMapper;
    

    @Autowired
    private MicroserviceBankAccountService microserviceBankAccountService;

    @Autowired
    private MicroserviceBankAccountQueryService microserviceBankAccountQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMicroserviceBankAccountMockMvc;

    private MicroserviceBankAccount microserviceBankAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MicroserviceBankAccountResource microserviceBankAccountResource = new MicroserviceBankAccountResource(microserviceBankAccountService, microserviceBankAccountQueryService);
        this.restMicroserviceBankAccountMockMvc = MockMvcBuilders.standaloneSetup(microserviceBankAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MicroserviceBankAccount createEntity(EntityManager em) {
        MicroserviceBankAccount microserviceBankAccount = new MicroserviceBankAccount()
            .name(DEFAULT_NAME)
            .bankNumber(DEFAULT_BANK_NUMBER)
            .agencyNumber(DEFAULT_AGENCY_NUMBER)
            .lastOperationDuration(DEFAULT_LAST_OPERATION_DURATION)
            .meanOperationDuration(DEFAULT_MEAN_OPERATION_DURATION)
            .balance(DEFAULT_BALANCE)
            .openingDay(DEFAULT_OPENING_DAY)
            .lastOperationDate(DEFAULT_LAST_OPERATION_DATE)
            .active(DEFAULT_ACTIVE)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .attachment(DEFAULT_ATTACHMENT)
            .attachmentContentType(DEFAULT_ATTACHMENT_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return microserviceBankAccount;
    }

    @Before
    public void initTest() {
        microserviceBankAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createMicroserviceBankAccount() throws Exception {
        int databaseSizeBeforeCreate = microserviceBankAccountRepository.findAll().size();

        // Create the MicroserviceBankAccount
        MicroserviceBankAccountDTO microserviceBankAccountDTO = microserviceBankAccountMapper.toDto(microserviceBankAccount);
        restMicroserviceBankAccountMockMvc.perform(post("/api/microservice-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceBankAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the MicroserviceBankAccount in the database
        List<MicroserviceBankAccount> microserviceBankAccountList = microserviceBankAccountRepository.findAll();
        assertThat(microserviceBankAccountList).hasSize(databaseSizeBeforeCreate + 1);
        MicroserviceBankAccount testMicroserviceBankAccount = microserviceBankAccountList.get(microserviceBankAccountList.size() - 1);
        assertThat(testMicroserviceBankAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMicroserviceBankAccount.getBankNumber()).isEqualTo(DEFAULT_BANK_NUMBER);
        assertThat(testMicroserviceBankAccount.getAgencyNumber()).isEqualTo(DEFAULT_AGENCY_NUMBER);
        assertThat(testMicroserviceBankAccount.getLastOperationDuration()).isEqualTo(DEFAULT_LAST_OPERATION_DURATION);
        assertThat(testMicroserviceBankAccount.getMeanOperationDuration()).isEqualTo(DEFAULT_MEAN_OPERATION_DURATION);
        assertThat(testMicroserviceBankAccount.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testMicroserviceBankAccount.getOpeningDay()).isEqualTo(DEFAULT_OPENING_DAY);
        assertThat(testMicroserviceBankAccount.getLastOperationDate()).isEqualTo(DEFAULT_LAST_OPERATION_DATE);
        assertThat(testMicroserviceBankAccount.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testMicroserviceBankAccount.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testMicroserviceBankAccount.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
        assertThat(testMicroserviceBankAccount.getAttachmentContentType()).isEqualTo(DEFAULT_ATTACHMENT_CONTENT_TYPE);
        assertThat(testMicroserviceBankAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMicroserviceBankAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = microserviceBankAccountRepository.findAll().size();

        // Create the MicroserviceBankAccount with an existing ID
        microserviceBankAccount.setId(1L);
        MicroserviceBankAccountDTO microserviceBankAccountDTO = microserviceBankAccountMapper.toDto(microserviceBankAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMicroserviceBankAccountMockMvc.perform(post("/api/microservice-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceBankAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MicroserviceBankAccount in the database
        List<MicroserviceBankAccount> microserviceBankAccountList = microserviceBankAccountRepository.findAll();
        assertThat(microserviceBankAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceBankAccountRepository.findAll().size();
        // set the field null
        microserviceBankAccount.setName(null);

        // Create the MicroserviceBankAccount, which fails.
        MicroserviceBankAccountDTO microserviceBankAccountDTO = microserviceBankAccountMapper.toDto(microserviceBankAccount);

        restMicroserviceBankAccountMockMvc.perform(post("/api/microservice-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceBankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MicroserviceBankAccount> microserviceBankAccountList = microserviceBankAccountRepository.findAll();
        assertThat(microserviceBankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceBankAccountRepository.findAll().size();
        // set the field null
        microserviceBankAccount.setBalance(null);

        // Create the MicroserviceBankAccount, which fails.
        MicroserviceBankAccountDTO microserviceBankAccountDTO = microserviceBankAccountMapper.toDto(microserviceBankAccount);

        restMicroserviceBankAccountMockMvc.perform(post("/api/microservice-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceBankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MicroserviceBankAccount> microserviceBankAccountList = microserviceBankAccountRepository.findAll();
        assertThat(microserviceBankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccounts() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList
        restMicroserviceBankAccountMockMvc.perform(get("/api/microservice-bank-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microserviceBankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].bankNumber").value(hasItem(DEFAULT_BANK_NUMBER)))
            .andExpect(jsonPath("$.[*].agencyNumber").value(hasItem(DEFAULT_AGENCY_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].lastOperationDuration").value(hasItem(DEFAULT_LAST_OPERATION_DURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].meanOperationDuration").value(hasItem(DEFAULT_MEAN_OPERATION_DURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].openingDay").value(hasItem(DEFAULT_OPENING_DAY.toString())))
            .andExpect(jsonPath("$.[*].lastOperationDate").value(hasItem(DEFAULT_LAST_OPERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].attachmentContentType").value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getMicroserviceBankAccount() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get the microserviceBankAccount
        restMicroserviceBankAccountMockMvc.perform(get("/api/microservice-bank-accounts/{id}", microserviceBankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(microserviceBankAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.bankNumber").value(DEFAULT_BANK_NUMBER))
            .andExpect(jsonPath("$.agencyNumber").value(DEFAULT_AGENCY_NUMBER.intValue()))
            .andExpect(jsonPath("$.lastOperationDuration").value(DEFAULT_LAST_OPERATION_DURATION.doubleValue()))
            .andExpect(jsonPath("$.meanOperationDuration").value(DEFAULT_MEAN_OPERATION_DURATION.doubleValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.openingDay").value(DEFAULT_OPENING_DAY.toString()))
            .andExpect(jsonPath("$.lastOperationDate").value(DEFAULT_LAST_OPERATION_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.attachmentContentType").value(DEFAULT_ATTACHMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachment").value(Base64Utils.encodeToString(DEFAULT_ATTACHMENT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where name equals to DEFAULT_NAME
        defaultMicroserviceBankAccountShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the microserviceBankAccountList where name equals to UPDATED_NAME
        defaultMicroserviceBankAccountShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMicroserviceBankAccountShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the microserviceBankAccountList where name equals to UPDATED_NAME
        defaultMicroserviceBankAccountShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where name is not null
        defaultMicroserviceBankAccountShouldBeFound("name.specified=true");

        // Get all the microserviceBankAccountList where name is null
        defaultMicroserviceBankAccountShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByBankNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where bankNumber equals to DEFAULT_BANK_NUMBER
        defaultMicroserviceBankAccountShouldBeFound("bankNumber.equals=" + DEFAULT_BANK_NUMBER);

        // Get all the microserviceBankAccountList where bankNumber equals to UPDATED_BANK_NUMBER
        defaultMicroserviceBankAccountShouldNotBeFound("bankNumber.equals=" + UPDATED_BANK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByBankNumberIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where bankNumber in DEFAULT_BANK_NUMBER or UPDATED_BANK_NUMBER
        defaultMicroserviceBankAccountShouldBeFound("bankNumber.in=" + DEFAULT_BANK_NUMBER + "," + UPDATED_BANK_NUMBER);

        // Get all the microserviceBankAccountList where bankNumber equals to UPDATED_BANK_NUMBER
        defaultMicroserviceBankAccountShouldNotBeFound("bankNumber.in=" + UPDATED_BANK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByBankNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where bankNumber is not null
        defaultMicroserviceBankAccountShouldBeFound("bankNumber.specified=true");

        // Get all the microserviceBankAccountList where bankNumber is null
        defaultMicroserviceBankAccountShouldNotBeFound("bankNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByBankNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where bankNumber greater than or equals to DEFAULT_BANK_NUMBER
        defaultMicroserviceBankAccountShouldBeFound("bankNumber.greaterOrEqualThan=" + DEFAULT_BANK_NUMBER);

        // Get all the microserviceBankAccountList where bankNumber greater than or equals to UPDATED_BANK_NUMBER
        defaultMicroserviceBankAccountShouldNotBeFound("bankNumber.greaterOrEqualThan=" + UPDATED_BANK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByBankNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where bankNumber less than or equals to DEFAULT_BANK_NUMBER
        defaultMicroserviceBankAccountShouldNotBeFound("bankNumber.lessThan=" + DEFAULT_BANK_NUMBER);

        // Get all the microserviceBankAccountList where bankNumber less than or equals to UPDATED_BANK_NUMBER
        defaultMicroserviceBankAccountShouldBeFound("bankNumber.lessThan=" + UPDATED_BANK_NUMBER);
    }


    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByAgencyNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where agencyNumber equals to DEFAULT_AGENCY_NUMBER
        defaultMicroserviceBankAccountShouldBeFound("agencyNumber.equals=" + DEFAULT_AGENCY_NUMBER);

        // Get all the microserviceBankAccountList where agencyNumber equals to UPDATED_AGENCY_NUMBER
        defaultMicroserviceBankAccountShouldNotBeFound("agencyNumber.equals=" + UPDATED_AGENCY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByAgencyNumberIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where agencyNumber in DEFAULT_AGENCY_NUMBER or UPDATED_AGENCY_NUMBER
        defaultMicroserviceBankAccountShouldBeFound("agencyNumber.in=" + DEFAULT_AGENCY_NUMBER + "," + UPDATED_AGENCY_NUMBER);

        // Get all the microserviceBankAccountList where agencyNumber equals to UPDATED_AGENCY_NUMBER
        defaultMicroserviceBankAccountShouldNotBeFound("agencyNumber.in=" + UPDATED_AGENCY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByAgencyNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where agencyNumber is not null
        defaultMicroserviceBankAccountShouldBeFound("agencyNumber.specified=true");

        // Get all the microserviceBankAccountList where agencyNumber is null
        defaultMicroserviceBankAccountShouldNotBeFound("agencyNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByAgencyNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where agencyNumber greater than or equals to DEFAULT_AGENCY_NUMBER
        defaultMicroserviceBankAccountShouldBeFound("agencyNumber.greaterOrEqualThan=" + DEFAULT_AGENCY_NUMBER);

        // Get all the microserviceBankAccountList where agencyNumber greater than or equals to UPDATED_AGENCY_NUMBER
        defaultMicroserviceBankAccountShouldNotBeFound("agencyNumber.greaterOrEqualThan=" + UPDATED_AGENCY_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByAgencyNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where agencyNumber less than or equals to DEFAULT_AGENCY_NUMBER
        defaultMicroserviceBankAccountShouldNotBeFound("agencyNumber.lessThan=" + DEFAULT_AGENCY_NUMBER);

        // Get all the microserviceBankAccountList where agencyNumber less than or equals to UPDATED_AGENCY_NUMBER
        defaultMicroserviceBankAccountShouldBeFound("agencyNumber.lessThan=" + UPDATED_AGENCY_NUMBER);
    }


    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByLastOperationDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where lastOperationDuration equals to DEFAULT_LAST_OPERATION_DURATION
        defaultMicroserviceBankAccountShouldBeFound("lastOperationDuration.equals=" + DEFAULT_LAST_OPERATION_DURATION);

        // Get all the microserviceBankAccountList where lastOperationDuration equals to UPDATED_LAST_OPERATION_DURATION
        defaultMicroserviceBankAccountShouldNotBeFound("lastOperationDuration.equals=" + UPDATED_LAST_OPERATION_DURATION);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByLastOperationDurationIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where lastOperationDuration in DEFAULT_LAST_OPERATION_DURATION or UPDATED_LAST_OPERATION_DURATION
        defaultMicroserviceBankAccountShouldBeFound("lastOperationDuration.in=" + DEFAULT_LAST_OPERATION_DURATION + "," + UPDATED_LAST_OPERATION_DURATION);

        // Get all the microserviceBankAccountList where lastOperationDuration equals to UPDATED_LAST_OPERATION_DURATION
        defaultMicroserviceBankAccountShouldNotBeFound("lastOperationDuration.in=" + UPDATED_LAST_OPERATION_DURATION);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByLastOperationDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where lastOperationDuration is not null
        defaultMicroserviceBankAccountShouldBeFound("lastOperationDuration.specified=true");

        // Get all the microserviceBankAccountList where lastOperationDuration is null
        defaultMicroserviceBankAccountShouldNotBeFound("lastOperationDuration.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByMeanOperationDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where meanOperationDuration equals to DEFAULT_MEAN_OPERATION_DURATION
        defaultMicroserviceBankAccountShouldBeFound("meanOperationDuration.equals=" + DEFAULT_MEAN_OPERATION_DURATION);

        // Get all the microserviceBankAccountList where meanOperationDuration equals to UPDATED_MEAN_OPERATION_DURATION
        defaultMicroserviceBankAccountShouldNotBeFound("meanOperationDuration.equals=" + UPDATED_MEAN_OPERATION_DURATION);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByMeanOperationDurationIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where meanOperationDuration in DEFAULT_MEAN_OPERATION_DURATION or UPDATED_MEAN_OPERATION_DURATION
        defaultMicroserviceBankAccountShouldBeFound("meanOperationDuration.in=" + DEFAULT_MEAN_OPERATION_DURATION + "," + UPDATED_MEAN_OPERATION_DURATION);

        // Get all the microserviceBankAccountList where meanOperationDuration equals to UPDATED_MEAN_OPERATION_DURATION
        defaultMicroserviceBankAccountShouldNotBeFound("meanOperationDuration.in=" + UPDATED_MEAN_OPERATION_DURATION);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByMeanOperationDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where meanOperationDuration is not null
        defaultMicroserviceBankAccountShouldBeFound("meanOperationDuration.specified=true");

        // Get all the microserviceBankAccountList where meanOperationDuration is null
        defaultMicroserviceBankAccountShouldNotBeFound("meanOperationDuration.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where balance equals to DEFAULT_BALANCE
        defaultMicroserviceBankAccountShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the microserviceBankAccountList where balance equals to UPDATED_BALANCE
        defaultMicroserviceBankAccountShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultMicroserviceBankAccountShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the microserviceBankAccountList where balance equals to UPDATED_BALANCE
        defaultMicroserviceBankAccountShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where balance is not null
        defaultMicroserviceBankAccountShouldBeFound("balance.specified=true");

        // Get all the microserviceBankAccountList where balance is null
        defaultMicroserviceBankAccountShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByOpeningDayIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where openingDay equals to DEFAULT_OPENING_DAY
        defaultMicroserviceBankAccountShouldBeFound("openingDay.equals=" + DEFAULT_OPENING_DAY);

        // Get all the microserviceBankAccountList where openingDay equals to UPDATED_OPENING_DAY
        defaultMicroserviceBankAccountShouldNotBeFound("openingDay.equals=" + UPDATED_OPENING_DAY);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByOpeningDayIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where openingDay in DEFAULT_OPENING_DAY or UPDATED_OPENING_DAY
        defaultMicroserviceBankAccountShouldBeFound("openingDay.in=" + DEFAULT_OPENING_DAY + "," + UPDATED_OPENING_DAY);

        // Get all the microserviceBankAccountList where openingDay equals to UPDATED_OPENING_DAY
        defaultMicroserviceBankAccountShouldNotBeFound("openingDay.in=" + UPDATED_OPENING_DAY);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByOpeningDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where openingDay is not null
        defaultMicroserviceBankAccountShouldBeFound("openingDay.specified=true");

        // Get all the microserviceBankAccountList where openingDay is null
        defaultMicroserviceBankAccountShouldNotBeFound("openingDay.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByOpeningDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where openingDay greater than or equals to DEFAULT_OPENING_DAY
        defaultMicroserviceBankAccountShouldBeFound("openingDay.greaterOrEqualThan=" + DEFAULT_OPENING_DAY);

        // Get all the microserviceBankAccountList where openingDay greater than or equals to UPDATED_OPENING_DAY
        defaultMicroserviceBankAccountShouldNotBeFound("openingDay.greaterOrEqualThan=" + UPDATED_OPENING_DAY);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByOpeningDayIsLessThanSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where openingDay less than or equals to DEFAULT_OPENING_DAY
        defaultMicroserviceBankAccountShouldNotBeFound("openingDay.lessThan=" + DEFAULT_OPENING_DAY);

        // Get all the microserviceBankAccountList where openingDay less than or equals to UPDATED_OPENING_DAY
        defaultMicroserviceBankAccountShouldBeFound("openingDay.lessThan=" + UPDATED_OPENING_DAY);
    }


    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByLastOperationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where lastOperationDate equals to DEFAULT_LAST_OPERATION_DATE
        defaultMicroserviceBankAccountShouldBeFound("lastOperationDate.equals=" + DEFAULT_LAST_OPERATION_DATE);

        // Get all the microserviceBankAccountList where lastOperationDate equals to UPDATED_LAST_OPERATION_DATE
        defaultMicroserviceBankAccountShouldNotBeFound("lastOperationDate.equals=" + UPDATED_LAST_OPERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByLastOperationDateIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where lastOperationDate in DEFAULT_LAST_OPERATION_DATE or UPDATED_LAST_OPERATION_DATE
        defaultMicroserviceBankAccountShouldBeFound("lastOperationDate.in=" + DEFAULT_LAST_OPERATION_DATE + "," + UPDATED_LAST_OPERATION_DATE);

        // Get all the microserviceBankAccountList where lastOperationDate equals to UPDATED_LAST_OPERATION_DATE
        defaultMicroserviceBankAccountShouldNotBeFound("lastOperationDate.in=" + UPDATED_LAST_OPERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByLastOperationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where lastOperationDate is not null
        defaultMicroserviceBankAccountShouldBeFound("lastOperationDate.specified=true");

        // Get all the microserviceBankAccountList where lastOperationDate is null
        defaultMicroserviceBankAccountShouldNotBeFound("lastOperationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where active equals to DEFAULT_ACTIVE
        defaultMicroserviceBankAccountShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the microserviceBankAccountList where active equals to UPDATED_ACTIVE
        defaultMicroserviceBankAccountShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultMicroserviceBankAccountShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the microserviceBankAccountList where active equals to UPDATED_ACTIVE
        defaultMicroserviceBankAccountShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where active is not null
        defaultMicroserviceBankAccountShouldBeFound("active.specified=true");

        // Get all the microserviceBankAccountList where active is null
        defaultMicroserviceBankAccountShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where accountType equals to DEFAULT_ACCOUNT_TYPE
        defaultMicroserviceBankAccountShouldBeFound("accountType.equals=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the microserviceBankAccountList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultMicroserviceBankAccountShouldNotBeFound("accountType.equals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByAccountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where accountType in DEFAULT_ACCOUNT_TYPE or UPDATED_ACCOUNT_TYPE
        defaultMicroserviceBankAccountShouldBeFound("accountType.in=" + DEFAULT_ACCOUNT_TYPE + "," + UPDATED_ACCOUNT_TYPE);

        // Get all the microserviceBankAccountList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultMicroserviceBankAccountShouldNotBeFound("accountType.in=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByAccountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        // Get all the microserviceBankAccountList where accountType is not null
        defaultMicroserviceBankAccountShouldBeFound("accountType.specified=true");

        // Get all the microserviceBankAccountList where accountType is null
        defaultMicroserviceBankAccountShouldNotBeFound("accountType.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceBankAccountsByOperationIsEqualToSomething() throws Exception {
        // Initialize the database
        MicroserviceOperation operation = MicroserviceOperationResourceIntTest.createEntity(em);
        em.persist(operation);
        em.flush();
        microserviceBankAccount.addOperation(operation);
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);
        Long operationId = operation.getId();

        // Get all the microserviceBankAccountList where operation equals to operationId
        defaultMicroserviceBankAccountShouldBeFound("operationId.equals=" + operationId);

        // Get all the microserviceBankAccountList where operation equals to operationId + 1
        defaultMicroserviceBankAccountShouldNotBeFound("operationId.equals=" + (operationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMicroserviceBankAccountShouldBeFound(String filter) throws Exception {
        restMicroserviceBankAccountMockMvc.perform(get("/api/microservice-bank-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microserviceBankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].bankNumber").value(hasItem(DEFAULT_BANK_NUMBER)))
            .andExpect(jsonPath("$.[*].agencyNumber").value(hasItem(DEFAULT_AGENCY_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].lastOperationDuration").value(hasItem(DEFAULT_LAST_OPERATION_DURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].meanOperationDuration").value(hasItem(DEFAULT_MEAN_OPERATION_DURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].openingDay").value(hasItem(DEFAULT_OPENING_DAY.toString())))
            .andExpect(jsonPath("$.[*].lastOperationDate").value(hasItem(DEFAULT_LAST_OPERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].attachmentContentType").value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMicroserviceBankAccountShouldNotBeFound(String filter) throws Exception {
        restMicroserviceBankAccountMockMvc.perform(get("/api/microservice-bank-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingMicroserviceBankAccount() throws Exception {
        // Get the microserviceBankAccount
        restMicroserviceBankAccountMockMvc.perform(get("/api/microservice-bank-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMicroserviceBankAccount() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        int databaseSizeBeforeUpdate = microserviceBankAccountRepository.findAll().size();

        // Update the microserviceBankAccount
        MicroserviceBankAccount updatedMicroserviceBankAccount = microserviceBankAccountRepository.findById(microserviceBankAccount.getId()).get();
        // Disconnect from session so that the updates on updatedMicroserviceBankAccount are not directly saved in db
        em.detach(updatedMicroserviceBankAccount);
        updatedMicroserviceBankAccount
            .name(UPDATED_NAME)
            .bankNumber(UPDATED_BANK_NUMBER)
            .agencyNumber(UPDATED_AGENCY_NUMBER)
            .lastOperationDuration(UPDATED_LAST_OPERATION_DURATION)
            .meanOperationDuration(UPDATED_MEAN_OPERATION_DURATION)
            .balance(UPDATED_BALANCE)
            .openingDay(UPDATED_OPENING_DAY)
            .lastOperationDate(UPDATED_LAST_OPERATION_DATE)
            .active(UPDATED_ACTIVE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        MicroserviceBankAccountDTO microserviceBankAccountDTO = microserviceBankAccountMapper.toDto(updatedMicroserviceBankAccount);

        restMicroserviceBankAccountMockMvc.perform(put("/api/microservice-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceBankAccountDTO)))
            .andExpect(status().isOk());

        // Validate the MicroserviceBankAccount in the database
        List<MicroserviceBankAccount> microserviceBankAccountList = microserviceBankAccountRepository.findAll();
        assertThat(microserviceBankAccountList).hasSize(databaseSizeBeforeUpdate);
        MicroserviceBankAccount testMicroserviceBankAccount = microserviceBankAccountList.get(microserviceBankAccountList.size() - 1);
        assertThat(testMicroserviceBankAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMicroserviceBankAccount.getBankNumber()).isEqualTo(UPDATED_BANK_NUMBER);
        assertThat(testMicroserviceBankAccount.getAgencyNumber()).isEqualTo(UPDATED_AGENCY_NUMBER);
        assertThat(testMicroserviceBankAccount.getLastOperationDuration()).isEqualTo(UPDATED_LAST_OPERATION_DURATION);
        assertThat(testMicroserviceBankAccount.getMeanOperationDuration()).isEqualTo(UPDATED_MEAN_OPERATION_DURATION);
        assertThat(testMicroserviceBankAccount.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testMicroserviceBankAccount.getOpeningDay()).isEqualTo(UPDATED_OPENING_DAY);
        assertThat(testMicroserviceBankAccount.getLastOperationDate()).isEqualTo(UPDATED_LAST_OPERATION_DATE);
        assertThat(testMicroserviceBankAccount.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testMicroserviceBankAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testMicroserviceBankAccount.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
        assertThat(testMicroserviceBankAccount.getAttachmentContentType()).isEqualTo(UPDATED_ATTACHMENT_CONTENT_TYPE);
        assertThat(testMicroserviceBankAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingMicroserviceBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = microserviceBankAccountRepository.findAll().size();

        // Create the MicroserviceBankAccount
        MicroserviceBankAccountDTO microserviceBankAccountDTO = microserviceBankAccountMapper.toDto(microserviceBankAccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMicroserviceBankAccountMockMvc.perform(put("/api/microservice-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceBankAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MicroserviceBankAccount in the database
        List<MicroserviceBankAccount> microserviceBankAccountList = microserviceBankAccountRepository.findAll();
        assertThat(microserviceBankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMicroserviceBankAccount() throws Exception {
        // Initialize the database
        microserviceBankAccountRepository.saveAndFlush(microserviceBankAccount);

        int databaseSizeBeforeDelete = microserviceBankAccountRepository.findAll().size();

        // Get the microserviceBankAccount
        restMicroserviceBankAccountMockMvc.perform(delete("/api/microservice-bank-accounts/{id}", microserviceBankAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MicroserviceBankAccount> microserviceBankAccountList = microserviceBankAccountRepository.findAll();
        assertThat(microserviceBankAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MicroserviceBankAccount.class);
        MicroserviceBankAccount microserviceBankAccount1 = new MicroserviceBankAccount();
        microserviceBankAccount1.setId(1L);
        MicroserviceBankAccount microserviceBankAccount2 = new MicroserviceBankAccount();
        microserviceBankAccount2.setId(microserviceBankAccount1.getId());
        assertThat(microserviceBankAccount1).isEqualTo(microserviceBankAccount2);
        microserviceBankAccount2.setId(2L);
        assertThat(microserviceBankAccount1).isNotEqualTo(microserviceBankAccount2);
        microserviceBankAccount1.setId(null);
        assertThat(microserviceBankAccount1).isNotEqualTo(microserviceBankAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MicroserviceBankAccountDTO.class);
        MicroserviceBankAccountDTO microserviceBankAccountDTO1 = new MicroserviceBankAccountDTO();
        microserviceBankAccountDTO1.setId(1L);
        MicroserviceBankAccountDTO microserviceBankAccountDTO2 = new MicroserviceBankAccountDTO();
        assertThat(microserviceBankAccountDTO1).isNotEqualTo(microserviceBankAccountDTO2);
        microserviceBankAccountDTO2.setId(microserviceBankAccountDTO1.getId());
        assertThat(microserviceBankAccountDTO1).isEqualTo(microserviceBankAccountDTO2);
        microserviceBankAccountDTO2.setId(2L);
        assertThat(microserviceBankAccountDTO1).isNotEqualTo(microserviceBankAccountDTO2);
        microserviceBankAccountDTO1.setId(null);
        assertThat(microserviceBankAccountDTO1).isNotEqualTo(microserviceBankAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(microserviceBankAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(microserviceBankAccountMapper.fromId(null)).isNull();
    }
}
