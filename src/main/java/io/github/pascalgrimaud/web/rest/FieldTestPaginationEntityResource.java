package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.domain.FieldTestPaginationEntity;
import io.github.pascalgrimaud.repository.FieldTestPaginationEntityRepository;
import io.github.pascalgrimaud.web.rest.errors.BadRequestAlertException;
import io.github.pascalgrimaud.web.rest.util.HeaderUtil;
import io.github.pascalgrimaud.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FieldTestPaginationEntity.
 */
@RestController
@RequestMapping("/api")
public class FieldTestPaginationEntityResource {

    private final Logger log = LoggerFactory.getLogger(FieldTestPaginationEntityResource.class);

    private static final String ENTITY_NAME = "fieldTestPaginationEntity";

    private final FieldTestPaginationEntityRepository fieldTestPaginationEntityRepository;

    public FieldTestPaginationEntityResource(FieldTestPaginationEntityRepository fieldTestPaginationEntityRepository) {
        this.fieldTestPaginationEntityRepository = fieldTestPaginationEntityRepository;
    }

    /**
     * POST  /field-test-pagination-entities : Create a new fieldTestPaginationEntity.
     *
     * @param fieldTestPaginationEntity the fieldTestPaginationEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fieldTestPaginationEntity, or with status 400 (Bad Request) if the fieldTestPaginationEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/field-test-pagination-entities")
    @Timed
    public ResponseEntity<FieldTestPaginationEntity> createFieldTestPaginationEntity(@Valid @RequestBody FieldTestPaginationEntity fieldTestPaginationEntity) throws URISyntaxException {
        log.debug("REST request to save FieldTestPaginationEntity : {}", fieldTestPaginationEntity);
        if (fieldTestPaginationEntity.getId() != null) {
            throw new BadRequestAlertException("A new fieldTestPaginationEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FieldTestPaginationEntity result = fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity);
        return ResponseEntity.created(new URI("/api/field-test-pagination-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /field-test-pagination-entities : Updates an existing fieldTestPaginationEntity.
     *
     * @param fieldTestPaginationEntity the fieldTestPaginationEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fieldTestPaginationEntity,
     * or with status 400 (Bad Request) if the fieldTestPaginationEntity is not valid,
     * or with status 500 (Internal Server Error) if the fieldTestPaginationEntity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/field-test-pagination-entities")
    @Timed
    public ResponseEntity<FieldTestPaginationEntity> updateFieldTestPaginationEntity(@Valid @RequestBody FieldTestPaginationEntity fieldTestPaginationEntity) throws URISyntaxException {
        log.debug("REST request to update FieldTestPaginationEntity : {}", fieldTestPaginationEntity);
        if (fieldTestPaginationEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FieldTestPaginationEntity result = fieldTestPaginationEntityRepository.save(fieldTestPaginationEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fieldTestPaginationEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /field-test-pagination-entities : get all the fieldTestPaginationEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fieldTestPaginationEntities in body
     */
    @GetMapping("/field-test-pagination-entities")
    @Timed
    public ResponseEntity<List<FieldTestPaginationEntity>> getAllFieldTestPaginationEntities(Pageable pageable) {
        log.debug("REST request to get a page of FieldTestPaginationEntities");
        Page<FieldTestPaginationEntity> page = fieldTestPaginationEntityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/field-test-pagination-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /field-test-pagination-entities/:id : get the "id" fieldTestPaginationEntity.
     *
     * @param id the id of the fieldTestPaginationEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fieldTestPaginationEntity, or with status 404 (Not Found)
     */
    @GetMapping("/field-test-pagination-entities/{id}")
    @Timed
    public ResponseEntity<FieldTestPaginationEntity> getFieldTestPaginationEntity(@PathVariable Long id) {
        log.debug("REST request to get FieldTestPaginationEntity : {}", id);
        Optional<FieldTestPaginationEntity> fieldTestPaginationEntity = fieldTestPaginationEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fieldTestPaginationEntity);
    }

    /**
     * DELETE  /field-test-pagination-entities/:id : delete the "id" fieldTestPaginationEntity.
     *
     * @param id the id of the fieldTestPaginationEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/field-test-pagination-entities/{id}")
    @Timed
    public ResponseEntity<Void> deleteFieldTestPaginationEntity(@PathVariable Long id) {
        log.debug("REST request to delete FieldTestPaginationEntity : {}", id);

        fieldTestPaginationEntityRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
