package io.github.pascalgrimaud.web.rest;

import io.github.pascalgrimaud.JhipsterdemoApp;

import io.github.pascalgrimaud.domain.MicroserviceOperation;
import io.github.pascalgrimaud.repository.MicroserviceOperationRepository;
import io.github.pascalgrimaud.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


import static io.github.pascalgrimaud.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MicroserviceOperationResource REST controller.
 *
 * @see MicroserviceOperationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterdemoApp.class)
public class MicroserviceOperationResourceIntTest {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Autowired
    private MicroserviceOperationRepository microserviceOperationRepository;
    @Mock
    private MicroserviceOperationRepository microserviceOperationRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMicroserviceOperationMockMvc;

    private MicroserviceOperation microserviceOperation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MicroserviceOperationResource microserviceOperationResource = new MicroserviceOperationResource(microserviceOperationRepository);
        this.restMicroserviceOperationMockMvc = MockMvcBuilders.standaloneSetup(microserviceOperationResource)
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
    public static MicroserviceOperation createEntity(EntityManager em) {
        MicroserviceOperation microserviceOperation = new MicroserviceOperation()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT);
        return microserviceOperation;
    }

    @Before
    public void initTest() {
        microserviceOperation = createEntity(em);
    }

    @Test
    @Transactional
    public void createMicroserviceOperation() throws Exception {
        int databaseSizeBeforeCreate = microserviceOperationRepository.findAll().size();

        // Create the MicroserviceOperation
        restMicroserviceOperationMockMvc.perform(post("/api/microservice-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceOperation)))
            .andExpect(status().isCreated());

        // Validate the MicroserviceOperation in the database
        List<MicroserviceOperation> microserviceOperationList = microserviceOperationRepository.findAll();
        assertThat(microserviceOperationList).hasSize(databaseSizeBeforeCreate + 1);
        MicroserviceOperation testMicroserviceOperation = microserviceOperationList.get(microserviceOperationList.size() - 1);
        assertThat(testMicroserviceOperation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMicroserviceOperation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMicroserviceOperation.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createMicroserviceOperationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = microserviceOperationRepository.findAll().size();

        // Create the MicroserviceOperation with an existing ID
        microserviceOperation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMicroserviceOperationMockMvc.perform(post("/api/microservice-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceOperation)))
            .andExpect(status().isBadRequest());

        // Validate the MicroserviceOperation in the database
        List<MicroserviceOperation> microserviceOperationList = microserviceOperationRepository.findAll();
        assertThat(microserviceOperationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceOperationRepository.findAll().size();
        // set the field null
        microserviceOperation.setDate(null);

        // Create the MicroserviceOperation, which fails.

        restMicroserviceOperationMockMvc.perform(post("/api/microservice-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceOperation)))
            .andExpect(status().isBadRequest());

        List<MicroserviceOperation> microserviceOperationList = microserviceOperationRepository.findAll();
        assertThat(microserviceOperationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceOperationRepository.findAll().size();
        // set the field null
        microserviceOperation.setAmount(null);

        // Create the MicroserviceOperation, which fails.

        restMicroserviceOperationMockMvc.perform(post("/api/microservice-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceOperation)))
            .andExpect(status().isBadRequest());

        List<MicroserviceOperation> microserviceOperationList = microserviceOperationRepository.findAll();
        assertThat(microserviceOperationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMicroserviceOperations() throws Exception {
        // Initialize the database
        microserviceOperationRepository.saveAndFlush(microserviceOperation);

        // Get all the microserviceOperationList
        restMicroserviceOperationMockMvc.perform(get("/api/microservice-operations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microserviceOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }
    
    public void getAllMicroserviceOperationsWithEagerRelationshipsIsEnabled() throws Exception {
        MicroserviceOperationResource microserviceOperationResource = new MicroserviceOperationResource(microserviceOperationRepositoryMock);
        when(microserviceOperationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMicroserviceOperationMockMvc = MockMvcBuilders.standaloneSetup(microserviceOperationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMicroserviceOperationMockMvc.perform(get("/api/microservice-operations?eagerload=true"))
        .andExpect(status().isOk());

        verify(microserviceOperationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllMicroserviceOperationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        MicroserviceOperationResource microserviceOperationResource = new MicroserviceOperationResource(microserviceOperationRepositoryMock);
            when(microserviceOperationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMicroserviceOperationMockMvc = MockMvcBuilders.standaloneSetup(microserviceOperationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMicroserviceOperationMockMvc.perform(get("/api/microservice-operations?eagerload=true"))
        .andExpect(status().isOk());

            verify(microserviceOperationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMicroserviceOperation() throws Exception {
        // Initialize the database
        microserviceOperationRepository.saveAndFlush(microserviceOperation);

        // Get the microserviceOperation
        restMicroserviceOperationMockMvc.perform(get("/api/microservice-operations/{id}", microserviceOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(microserviceOperation.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingMicroserviceOperation() throws Exception {
        // Get the microserviceOperation
        restMicroserviceOperationMockMvc.perform(get("/api/microservice-operations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMicroserviceOperation() throws Exception {
        // Initialize the database
        microserviceOperationRepository.saveAndFlush(microserviceOperation);

        int databaseSizeBeforeUpdate = microserviceOperationRepository.findAll().size();

        // Update the microserviceOperation
        MicroserviceOperation updatedMicroserviceOperation = microserviceOperationRepository.findById(microserviceOperation.getId()).get();
        // Disconnect from session so that the updates on updatedMicroserviceOperation are not directly saved in db
        em.detach(updatedMicroserviceOperation);
        updatedMicroserviceOperation
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT);

        restMicroserviceOperationMockMvc.perform(put("/api/microservice-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMicroserviceOperation)))
            .andExpect(status().isOk());

        // Validate the MicroserviceOperation in the database
        List<MicroserviceOperation> microserviceOperationList = microserviceOperationRepository.findAll();
        assertThat(microserviceOperationList).hasSize(databaseSizeBeforeUpdate);
        MicroserviceOperation testMicroserviceOperation = microserviceOperationList.get(microserviceOperationList.size() - 1);
        assertThat(testMicroserviceOperation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMicroserviceOperation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMicroserviceOperation.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingMicroserviceOperation() throws Exception {
        int databaseSizeBeforeUpdate = microserviceOperationRepository.findAll().size();

        // Create the MicroserviceOperation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMicroserviceOperationMockMvc.perform(put("/api/microservice-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceOperation)))
            .andExpect(status().isBadRequest());

        // Validate the MicroserviceOperation in the database
        List<MicroserviceOperation> microserviceOperationList = microserviceOperationRepository.findAll();
        assertThat(microserviceOperationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMicroserviceOperation() throws Exception {
        // Initialize the database
        microserviceOperationRepository.saveAndFlush(microserviceOperation);

        int databaseSizeBeforeDelete = microserviceOperationRepository.findAll().size();

        // Get the microserviceOperation
        restMicroserviceOperationMockMvc.perform(delete("/api/microservice-operations/{id}", microserviceOperation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MicroserviceOperation> microserviceOperationList = microserviceOperationRepository.findAll();
        assertThat(microserviceOperationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MicroserviceOperation.class);
        MicroserviceOperation microserviceOperation1 = new MicroserviceOperation();
        microserviceOperation1.setId(1L);
        MicroserviceOperation microserviceOperation2 = new MicroserviceOperation();
        microserviceOperation2.setId(microserviceOperation1.getId());
        assertThat(microserviceOperation1).isEqualTo(microserviceOperation2);
        microserviceOperation2.setId(2L);
        assertThat(microserviceOperation1).isNotEqualTo(microserviceOperation2);
        microserviceOperation1.setId(null);
        assertThat(microserviceOperation1).isNotEqualTo(microserviceOperation2);
    }
}
