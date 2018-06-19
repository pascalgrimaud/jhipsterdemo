package io.github.pascalgrimaud.web.rest;

import io.github.pascalgrimaud.JhipsterdemoApp;

import io.github.pascalgrimaud.domain.EntityWithPagination;
import io.github.pascalgrimaud.repository.EntityWithPaginationRepository;
import io.github.pascalgrimaud.web.rest.errors.ExceptionTranslator;

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
 * Test class for the EntityWithPaginationResource REST controller.
 *
 * @see EntityWithPaginationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterdemoApp.class)
public class EntityWithPaginationResourceIntTest {

    private static final String DEFAULT_NATHAN = "AAAAAAAAAA";
    private static final String UPDATED_NATHAN = "BBBBBBBBBB";

    @Autowired
    private EntityWithPaginationRepository entityWithPaginationRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntityWithPaginationMockMvc;

    private EntityWithPagination entityWithPagination;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntityWithPaginationResource entityWithPaginationResource = new EntityWithPaginationResource(entityWithPaginationRepository);
        this.restEntityWithPaginationMockMvc = MockMvcBuilders.standaloneSetup(entityWithPaginationResource)
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
    public static EntityWithPagination createEntity(EntityManager em) {
        EntityWithPagination entityWithPagination = new EntityWithPagination()
            .nathan(DEFAULT_NATHAN);
        return entityWithPagination;
    }

    @Before
    public void initTest() {
        entityWithPagination = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityWithPagination() throws Exception {
        int databaseSizeBeforeCreate = entityWithPaginationRepository.findAll().size();

        // Create the EntityWithPagination
        restEntityWithPaginationMockMvc.perform(post("/api/entity-with-paginations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityWithPagination)))
            .andExpect(status().isCreated());

        // Validate the EntityWithPagination in the database
        List<EntityWithPagination> entityWithPaginationList = entityWithPaginationRepository.findAll();
        assertThat(entityWithPaginationList).hasSize(databaseSizeBeforeCreate + 1);
        EntityWithPagination testEntityWithPagination = entityWithPaginationList.get(entityWithPaginationList.size() - 1);
        assertThat(testEntityWithPagination.getNathan()).isEqualTo(DEFAULT_NATHAN);
    }

    @Test
    @Transactional
    public void createEntityWithPaginationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityWithPaginationRepository.findAll().size();

        // Create the EntityWithPagination with an existing ID
        entityWithPagination.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityWithPaginationMockMvc.perform(post("/api/entity-with-paginations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityWithPagination)))
            .andExpect(status().isBadRequest());

        // Validate the EntityWithPagination in the database
        List<EntityWithPagination> entityWithPaginationList = entityWithPaginationRepository.findAll();
        assertThat(entityWithPaginationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntityWithPaginations() throws Exception {
        // Initialize the database
        entityWithPaginationRepository.saveAndFlush(entityWithPagination);

        // Get all the entityWithPaginationList
        restEntityWithPaginationMockMvc.perform(get("/api/entity-with-paginations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityWithPagination.getId().intValue())))
            .andExpect(jsonPath("$.[*].nathan").value(hasItem(DEFAULT_NATHAN.toString())));
    }
    

    @Test
    @Transactional
    public void getEntityWithPagination() throws Exception {
        // Initialize the database
        entityWithPaginationRepository.saveAndFlush(entityWithPagination);

        // Get the entityWithPagination
        restEntityWithPaginationMockMvc.perform(get("/api/entity-with-paginations/{id}", entityWithPagination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityWithPagination.getId().intValue()))
            .andExpect(jsonPath("$.nathan").value(DEFAULT_NATHAN.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingEntityWithPagination() throws Exception {
        // Get the entityWithPagination
        restEntityWithPaginationMockMvc.perform(get("/api/entity-with-paginations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityWithPagination() throws Exception {
        // Initialize the database
        entityWithPaginationRepository.saveAndFlush(entityWithPagination);

        int databaseSizeBeforeUpdate = entityWithPaginationRepository.findAll().size();

        // Update the entityWithPagination
        EntityWithPagination updatedEntityWithPagination = entityWithPaginationRepository.findById(entityWithPagination.getId()).get();
        // Disconnect from session so that the updates on updatedEntityWithPagination are not directly saved in db
        em.detach(updatedEntityWithPagination);
        updatedEntityWithPagination
            .nathan(UPDATED_NATHAN);

        restEntityWithPaginationMockMvc.perform(put("/api/entity-with-paginations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityWithPagination)))
            .andExpect(status().isOk());

        // Validate the EntityWithPagination in the database
        List<EntityWithPagination> entityWithPaginationList = entityWithPaginationRepository.findAll();
        assertThat(entityWithPaginationList).hasSize(databaseSizeBeforeUpdate);
        EntityWithPagination testEntityWithPagination = entityWithPaginationList.get(entityWithPaginationList.size() - 1);
        assertThat(testEntityWithPagination.getNathan()).isEqualTo(UPDATED_NATHAN);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityWithPagination() throws Exception {
        int databaseSizeBeforeUpdate = entityWithPaginationRepository.findAll().size();

        // Create the EntityWithPagination

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntityWithPaginationMockMvc.perform(put("/api/entity-with-paginations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityWithPagination)))
            .andExpect(status().isBadRequest());

        // Validate the EntityWithPagination in the database
        List<EntityWithPagination> entityWithPaginationList = entityWithPaginationRepository.findAll();
        assertThat(entityWithPaginationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEntityWithPagination() throws Exception {
        // Initialize the database
        entityWithPaginationRepository.saveAndFlush(entityWithPagination);

        int databaseSizeBeforeDelete = entityWithPaginationRepository.findAll().size();

        // Get the entityWithPagination
        restEntityWithPaginationMockMvc.perform(delete("/api/entity-with-paginations/{id}", entityWithPagination.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityWithPagination> entityWithPaginationList = entityWithPaginationRepository.findAll();
        assertThat(entityWithPaginationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityWithPagination.class);
        EntityWithPagination entityWithPagination1 = new EntityWithPagination();
        entityWithPagination1.setId(1L);
        EntityWithPagination entityWithPagination2 = new EntityWithPagination();
        entityWithPagination2.setId(entityWithPagination1.getId());
        assertThat(entityWithPagination1).isEqualTo(entityWithPagination2);
        entityWithPagination2.setId(2L);
        assertThat(entityWithPagination1).isNotEqualTo(entityWithPagination2);
        entityWithPagination1.setId(null);
        assertThat(entityWithPagination1).isNotEqualTo(entityWithPagination2);
    }
}
