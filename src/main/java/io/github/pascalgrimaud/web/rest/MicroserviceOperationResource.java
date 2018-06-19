package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.domain.MicroserviceOperation;
import io.github.pascalgrimaud.repository.MicroserviceOperationRepository;
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
 * REST controller for managing MicroserviceOperation.
 */
@RestController
@RequestMapping("/api")
public class MicroserviceOperationResource {

    private final Logger log = LoggerFactory.getLogger(MicroserviceOperationResource.class);

    private static final String ENTITY_NAME = "microserviceOperation";

    private final MicroserviceOperationRepository microserviceOperationRepository;

    public MicroserviceOperationResource(MicroserviceOperationRepository microserviceOperationRepository) {
        this.microserviceOperationRepository = microserviceOperationRepository;
    }

    /**
     * POST  /microservice-operations : Create a new microserviceOperation.
     *
     * @param microserviceOperation the microserviceOperation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new microserviceOperation, or with status 400 (Bad Request) if the microserviceOperation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/microservice-operations")
    @Timed
    public ResponseEntity<MicroserviceOperation> createMicroserviceOperation(@Valid @RequestBody MicroserviceOperation microserviceOperation) throws URISyntaxException {
        log.debug("REST request to save MicroserviceOperation : {}", microserviceOperation);
        if (microserviceOperation.getId() != null) {
            throw new BadRequestAlertException("A new microserviceOperation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MicroserviceOperation result = microserviceOperationRepository.save(microserviceOperation);
        return ResponseEntity.created(new URI("/api/microservice-operations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /microservice-operations : Updates an existing microserviceOperation.
     *
     * @param microserviceOperation the microserviceOperation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated microserviceOperation,
     * or with status 400 (Bad Request) if the microserviceOperation is not valid,
     * or with status 500 (Internal Server Error) if the microserviceOperation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/microservice-operations")
    @Timed
    public ResponseEntity<MicroserviceOperation> updateMicroserviceOperation(@Valid @RequestBody MicroserviceOperation microserviceOperation) throws URISyntaxException {
        log.debug("REST request to update MicroserviceOperation : {}", microserviceOperation);
        if (microserviceOperation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MicroserviceOperation result = microserviceOperationRepository.save(microserviceOperation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, microserviceOperation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /microservice-operations : get all the microserviceOperations.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of microserviceOperations in body
     */
    @GetMapping("/microservice-operations")
    @Timed
    public ResponseEntity<List<MicroserviceOperation>> getAllMicroserviceOperations(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of MicroserviceOperations");
        Page<MicroserviceOperation> page;
        if (eagerload) {
            page = microserviceOperationRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = microserviceOperationRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/microservice-operations?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /microservice-operations/:id : get the "id" microserviceOperation.
     *
     * @param id the id of the microserviceOperation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the microserviceOperation, or with status 404 (Not Found)
     */
    @GetMapping("/microservice-operations/{id}")
    @Timed
    public ResponseEntity<MicroserviceOperation> getMicroserviceOperation(@PathVariable Long id) {
        log.debug("REST request to get MicroserviceOperation : {}", id);
        Optional<MicroserviceOperation> microserviceOperation = microserviceOperationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(microserviceOperation);
    }

    /**
     * DELETE  /microservice-operations/:id : delete the "id" microserviceOperation.
     *
     * @param id the id of the microserviceOperation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/microservice-operations/{id}")
    @Timed
    public ResponseEntity<Void> deleteMicroserviceOperation(@PathVariable Long id) {
        log.debug("REST request to delete MicroserviceOperation : {}", id);

        microserviceOperationRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
