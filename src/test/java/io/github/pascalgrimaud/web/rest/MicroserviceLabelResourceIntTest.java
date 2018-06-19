package io.github.pascalgrimaud.web.rest;

import io.github.pascalgrimaud.JhipsterdemoApp;

import io.github.pascalgrimaud.domain.MicroserviceLabel;
import io.github.pascalgrimaud.domain.MicroserviceOperation;
import io.github.pascalgrimaud.repository.MicroserviceLabelRepository;
import io.github.pascalgrimaud.service.MicroserviceLabelService;
import io.github.pascalgrimaud.web.rest.errors.ExceptionTranslator;
import io.github.pascalgrimaud.service.dto.MicroserviceLabelCriteria;
import io.github.pascalgrimaud.service.MicroserviceLabelQueryService;

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

import javax.persistence.EntityManager;
import java.util.List;


import static io.github.pascalgrimaud.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MicroserviceLabelResource REST controller.
 *
 * @see MicroserviceLabelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterdemoApp.class)
public class MicroserviceLabelResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private MicroserviceLabelRepository microserviceLabelRepository;

    

    @Autowired
    private MicroserviceLabelService microserviceLabelService;

    @Autowired
    private MicroserviceLabelQueryService microserviceLabelQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMicroserviceLabelMockMvc;

    private MicroserviceLabel microserviceLabel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MicroserviceLabelResource microserviceLabelResource = new MicroserviceLabelResource(microserviceLabelService, microserviceLabelQueryService);
        this.restMicroserviceLabelMockMvc = MockMvcBuilders.standaloneSetup(microserviceLabelResource)
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
    public static MicroserviceLabel createEntity(EntityManager em) {
        MicroserviceLabel microserviceLabel = new MicroserviceLabel()
            .label(DEFAULT_LABEL);
        return microserviceLabel;
    }

    @Before
    public void initTest() {
        microserviceLabel = createEntity(em);
    }

    @Test
    @Transactional
    public void createMicroserviceLabel() throws Exception {
        int databaseSizeBeforeCreate = microserviceLabelRepository.findAll().size();

        // Create the MicroserviceLabel
        restMicroserviceLabelMockMvc.perform(post("/api/microservice-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceLabel)))
            .andExpect(status().isCreated());

        // Validate the MicroserviceLabel in the database
        List<MicroserviceLabel> microserviceLabelList = microserviceLabelRepository.findAll();
        assertThat(microserviceLabelList).hasSize(databaseSizeBeforeCreate + 1);
        MicroserviceLabel testMicroserviceLabel = microserviceLabelList.get(microserviceLabelList.size() - 1);
        assertThat(testMicroserviceLabel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createMicroserviceLabelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = microserviceLabelRepository.findAll().size();

        // Create the MicroserviceLabel with an existing ID
        microserviceLabel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMicroserviceLabelMockMvc.perform(post("/api/microservice-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceLabel)))
            .andExpect(status().isBadRequest());

        // Validate the MicroserviceLabel in the database
        List<MicroserviceLabel> microserviceLabelList = microserviceLabelRepository.findAll();
        assertThat(microserviceLabelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceLabelRepository.findAll().size();
        // set the field null
        microserviceLabel.setLabel(null);

        // Create the MicroserviceLabel, which fails.

        restMicroserviceLabelMockMvc.perform(post("/api/microservice-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceLabel)))
            .andExpect(status().isBadRequest());

        List<MicroserviceLabel> microserviceLabelList = microserviceLabelRepository.findAll();
        assertThat(microserviceLabelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMicroserviceLabels() throws Exception {
        // Initialize the database
        microserviceLabelRepository.saveAndFlush(microserviceLabel);

        // Get all the microserviceLabelList
        restMicroserviceLabelMockMvc.perform(get("/api/microservice-labels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microserviceLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }
    

    @Test
    @Transactional
    public void getMicroserviceLabel() throws Exception {
        // Initialize the database
        microserviceLabelRepository.saveAndFlush(microserviceLabel);

        // Get the microserviceLabel
        restMicroserviceLabelMockMvc.perform(get("/api/microservice-labels/{id}", microserviceLabel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(microserviceLabel.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
    }

    @Test
    @Transactional
    public void getAllMicroserviceLabelsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        microserviceLabelRepository.saveAndFlush(microserviceLabel);

        // Get all the microserviceLabelList where label equals to DEFAULT_LABEL
        defaultMicroserviceLabelShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the microserviceLabelList where label equals to UPDATED_LABEL
        defaultMicroserviceLabelShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllMicroserviceLabelsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        microserviceLabelRepository.saveAndFlush(microserviceLabel);

        // Get all the microserviceLabelList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultMicroserviceLabelShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the microserviceLabelList where label equals to UPDATED_LABEL
        defaultMicroserviceLabelShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllMicroserviceLabelsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        microserviceLabelRepository.saveAndFlush(microserviceLabel);

        // Get all the microserviceLabelList where label is not null
        defaultMicroserviceLabelShouldBeFound("label.specified=true");

        // Get all the microserviceLabelList where label is null
        defaultMicroserviceLabelShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    public void getAllMicroserviceLabelsByOperationIsEqualToSomething() throws Exception {
        // Initialize the database
        MicroserviceOperation operation = MicroserviceOperationResourceIntTest.createEntity(em);
        em.persist(operation);
        em.flush();
        microserviceLabel.addOperation(operation);
        microserviceLabelRepository.saveAndFlush(microserviceLabel);
        Long operationId = operation.getId();

        // Get all the microserviceLabelList where operation equals to operationId
        defaultMicroserviceLabelShouldBeFound("operationId.equals=" + operationId);

        // Get all the microserviceLabelList where operation equals to operationId + 1
        defaultMicroserviceLabelShouldNotBeFound("operationId.equals=" + (operationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMicroserviceLabelShouldBeFound(String filter) throws Exception {
        restMicroserviceLabelMockMvc.perform(get("/api/microservice-labels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microserviceLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMicroserviceLabelShouldNotBeFound(String filter) throws Exception {
        restMicroserviceLabelMockMvc.perform(get("/api/microservice-labels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingMicroserviceLabel() throws Exception {
        // Get the microserviceLabel
        restMicroserviceLabelMockMvc.perform(get("/api/microservice-labels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMicroserviceLabel() throws Exception {
        // Initialize the database
        microserviceLabelService.save(microserviceLabel);

        int databaseSizeBeforeUpdate = microserviceLabelRepository.findAll().size();

        // Update the microserviceLabel
        MicroserviceLabel updatedMicroserviceLabel = microserviceLabelRepository.findById(microserviceLabel.getId()).get();
        // Disconnect from session so that the updates on updatedMicroserviceLabel are not directly saved in db
        em.detach(updatedMicroserviceLabel);
        updatedMicroserviceLabel
            .label(UPDATED_LABEL);

        restMicroserviceLabelMockMvc.perform(put("/api/microservice-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMicroserviceLabel)))
            .andExpect(status().isOk());

        // Validate the MicroserviceLabel in the database
        List<MicroserviceLabel> microserviceLabelList = microserviceLabelRepository.findAll();
        assertThat(microserviceLabelList).hasSize(databaseSizeBeforeUpdate);
        MicroserviceLabel testMicroserviceLabel = microserviceLabelList.get(microserviceLabelList.size() - 1);
        assertThat(testMicroserviceLabel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingMicroserviceLabel() throws Exception {
        int databaseSizeBeforeUpdate = microserviceLabelRepository.findAll().size();

        // Create the MicroserviceLabel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMicroserviceLabelMockMvc.perform(put("/api/microservice-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(microserviceLabel)))
            .andExpect(status().isBadRequest());

        // Validate the MicroserviceLabel in the database
        List<MicroserviceLabel> microserviceLabelList = microserviceLabelRepository.findAll();
        assertThat(microserviceLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMicroserviceLabel() throws Exception {
        // Initialize the database
        microserviceLabelService.save(microserviceLabel);

        int databaseSizeBeforeDelete = microserviceLabelRepository.findAll().size();

        // Get the microserviceLabel
        restMicroserviceLabelMockMvc.perform(delete("/api/microservice-labels/{id}", microserviceLabel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MicroserviceLabel> microserviceLabelList = microserviceLabelRepository.findAll();
        assertThat(microserviceLabelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MicroserviceLabel.class);
        MicroserviceLabel microserviceLabel1 = new MicroserviceLabel();
        microserviceLabel1.setId(1L);
        MicroserviceLabel microserviceLabel2 = new MicroserviceLabel();
        microserviceLabel2.setId(microserviceLabel1.getId());
        assertThat(microserviceLabel1).isEqualTo(microserviceLabel2);
        microserviceLabel2.setId(2L);
        assertThat(microserviceLabel1).isNotEqualTo(microserviceLabel2);
        microserviceLabel1.setId(null);
        assertThat(microserviceLabel1).isNotEqualTo(microserviceLabel2);
    }
}
