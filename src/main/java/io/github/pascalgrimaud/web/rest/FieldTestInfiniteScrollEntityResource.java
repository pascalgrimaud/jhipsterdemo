package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.domain.FieldTestInfiniteScrollEntity;
import io.github.pascalgrimaud.repository.FieldTestInfiniteScrollEntityRepository;
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
 * REST controller for managing FieldTestInfiniteScrollEntity.
 */
@RestController
@RequestMapping("/api")
public class FieldTestInfiniteScrollEntityResource {

    private final Logger log = LoggerFactory.getLogger(FieldTestInfiniteScrollEntityResource.class);

    private static final String ENTITY_NAME = "fieldTestInfiniteScrollEntity";

    private final FieldTestInfiniteScrollEntityRepository fieldTestInfiniteScrollEntityRepository;

    public FieldTestInfiniteScrollEntityResource(FieldTestInfiniteScrollEntityRepository fieldTestInfiniteScrollEntityRepository) {
        this.fieldTestInfiniteScrollEntityRepository = fieldTestInfiniteScrollEntityRepository;
    }

    /**
     * POST  /field-test-infinite-scroll-entities : Create a new fieldTestInfiniteScrollEntity.
     *
     * @param fieldTestInfiniteScrollEntity the fieldTestInfiniteScrollEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fieldTestInfiniteScrollEntity, or with status 400 (Bad Request) if the fieldTestInfiniteScrollEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/field-test-infinite-scroll-entities")
    @Timed
    public ResponseEntity<FieldTestInfiniteScrollEntity> createFieldTestInfiniteScrollEntity(@Valid @RequestBody FieldTestInfiniteScrollEntity fieldTestInfiniteScrollEntity) throws URISyntaxException {
        log.debug("REST request to save FieldTestInfiniteScrollEntity : {}", fieldTestInfiniteScrollEntity);
        if (fieldTestInfiniteScrollEntity.getId() != null) {
            throw new BadRequestAlertException("A new fieldTestInfiniteScrollEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FieldTestInfiniteScrollEntity result = fieldTestInfiniteScrollEntityRepository.save(fieldTestInfiniteScrollEntity);
        return ResponseEntity.created(new URI("/api/field-test-infinite-scroll-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /field-test-infinite-scroll-entities : Updates an existing fieldTestInfiniteScrollEntity.
     *
     * @param fieldTestInfiniteScrollEntity the fieldTestInfiniteScrollEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fieldTestInfiniteScrollEntity,
     * or with status 400 (Bad Request) if the fieldTestInfiniteScrollEntity is not valid,
     * or with status 500 (Internal Server Error) if the fieldTestInfiniteScrollEntity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/field-test-infinite-scroll-entities")
    @Timed
    public ResponseEntity<FieldTestInfiniteScrollEntity> updateFieldTestInfiniteScrollEntity(@Valid @RequestBody FieldTestInfiniteScrollEntity fieldTestInfiniteScrollEntity) throws URISyntaxException {
        log.debug("REST request to update FieldTestInfiniteScrollEntity : {}", fieldTestInfiniteScrollEntity);
        if (fieldTestInfiniteScrollEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FieldTestInfiniteScrollEntity result = fieldTestInfiniteScrollEntityRepository.save(fieldTestInfiniteScrollEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fieldTestInfiniteScrollEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /field-test-infinite-scroll-entities : get all the fieldTestInfiniteScrollEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fieldTestInfiniteScrollEntities in body
     */
    @GetMapping("/field-test-infinite-scroll-entities")
    @Timed
    public ResponseEntity<List<FieldTestInfiniteScrollEntity>> getAllFieldTestInfiniteScrollEntities(Pageable pageable) {
        log.debug("REST request to get a page of FieldTestInfiniteScrollEntities");
        Page<FieldTestInfiniteScrollEntity> page = fieldTestInfiniteScrollEntityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/field-test-infinite-scroll-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /field-test-infinite-scroll-entities/:id : get the "id" fieldTestInfiniteScrollEntity.
     *
     * @param id the id of the fieldTestInfiniteScrollEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fieldTestInfiniteScrollEntity, or with status 404 (Not Found)
     */
    @GetMapping("/field-test-infinite-scroll-entities/{id}")
    @Timed
    public ResponseEntity<FieldTestInfiniteScrollEntity> getFieldTestInfiniteScrollEntity(@PathVariable Long id) {
        log.debug("REST request to get FieldTestInfiniteScrollEntity : {}", id);
        Optional<FieldTestInfiniteScrollEntity> fieldTestInfiniteScrollEntity = fieldTestInfiniteScrollEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fieldTestInfiniteScrollEntity);
    }

    /**
     * DELETE  /field-test-infinite-scroll-entities/:id : delete the "id" fieldTestInfiniteScrollEntity.
     *
     * @param id the id of the fieldTestInfiniteScrollEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/field-test-infinite-scroll-entities/{id}")
    @Timed
    public ResponseEntity<Void> deleteFieldTestInfiniteScrollEntity(@PathVariable Long id) {
        log.debug("REST request to delete FieldTestInfiniteScrollEntity : {}", id);

        fieldTestInfiniteScrollEntityRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
