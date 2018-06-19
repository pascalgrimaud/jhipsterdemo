package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.domain.FieldTestPagerEntity;
import io.github.pascalgrimaud.repository.FieldTestPagerEntityRepository;
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
 * REST controller for managing FieldTestPagerEntity.
 */
@RestController
@RequestMapping("/api")
public class FieldTestPagerEntityResource {

    private final Logger log = LoggerFactory.getLogger(FieldTestPagerEntityResource.class);

    private static final String ENTITY_NAME = "fieldTestPagerEntity";

    private final FieldTestPagerEntityRepository fieldTestPagerEntityRepository;

    public FieldTestPagerEntityResource(FieldTestPagerEntityRepository fieldTestPagerEntityRepository) {
        this.fieldTestPagerEntityRepository = fieldTestPagerEntityRepository;
    }

    /**
     * POST  /field-test-pager-entities : Create a new fieldTestPagerEntity.
     *
     * @param fieldTestPagerEntity the fieldTestPagerEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fieldTestPagerEntity, or with status 400 (Bad Request) if the fieldTestPagerEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/field-test-pager-entities")
    @Timed
    public ResponseEntity<FieldTestPagerEntity> createFieldTestPagerEntity(@Valid @RequestBody FieldTestPagerEntity fieldTestPagerEntity) throws URISyntaxException {
        log.debug("REST request to save FieldTestPagerEntity : {}", fieldTestPagerEntity);
        if (fieldTestPagerEntity.getId() != null) {
            throw new BadRequestAlertException("A new fieldTestPagerEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FieldTestPagerEntity result = fieldTestPagerEntityRepository.save(fieldTestPagerEntity);
        return ResponseEntity.created(new URI("/api/field-test-pager-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /field-test-pager-entities : Updates an existing fieldTestPagerEntity.
     *
     * @param fieldTestPagerEntity the fieldTestPagerEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fieldTestPagerEntity,
     * or with status 400 (Bad Request) if the fieldTestPagerEntity is not valid,
     * or with status 500 (Internal Server Error) if the fieldTestPagerEntity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/field-test-pager-entities")
    @Timed
    public ResponseEntity<FieldTestPagerEntity> updateFieldTestPagerEntity(@Valid @RequestBody FieldTestPagerEntity fieldTestPagerEntity) throws URISyntaxException {
        log.debug("REST request to update FieldTestPagerEntity : {}", fieldTestPagerEntity);
        if (fieldTestPagerEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FieldTestPagerEntity result = fieldTestPagerEntityRepository.save(fieldTestPagerEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fieldTestPagerEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /field-test-pager-entities : get all the fieldTestPagerEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fieldTestPagerEntities in body
     */
    @GetMapping("/field-test-pager-entities")
    @Timed
    public ResponseEntity<List<FieldTestPagerEntity>> getAllFieldTestPagerEntities(Pageable pageable) {
        log.debug("REST request to get a page of FieldTestPagerEntities");
        Page<FieldTestPagerEntity> page = fieldTestPagerEntityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/field-test-pager-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /field-test-pager-entities/:id : get the "id" fieldTestPagerEntity.
     *
     * @param id the id of the fieldTestPagerEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fieldTestPagerEntity, or with status 404 (Not Found)
     */
    @GetMapping("/field-test-pager-entities/{id}")
    @Timed
    public ResponseEntity<FieldTestPagerEntity> getFieldTestPagerEntity(@PathVariable Long id) {
        log.debug("REST request to get FieldTestPagerEntity : {}", id);
        Optional<FieldTestPagerEntity> fieldTestPagerEntity = fieldTestPagerEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fieldTestPagerEntity);
    }

    /**
     * DELETE  /field-test-pager-entities/:id : delete the "id" fieldTestPagerEntity.
     *
     * @param id the id of the fieldTestPagerEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/field-test-pager-entities/{id}")
    @Timed
    public ResponseEntity<Void> deleteFieldTestPagerEntity(@PathVariable Long id) {
        log.debug("REST request to delete FieldTestPagerEntity : {}", id);

        fieldTestPagerEntityRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
