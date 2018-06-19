package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.domain.MicroserviceLabel;
import io.github.pascalgrimaud.service.MicroserviceLabelService;
import io.github.pascalgrimaud.web.rest.errors.BadRequestAlertException;
import io.github.pascalgrimaud.web.rest.util.HeaderUtil;
import io.github.pascalgrimaud.web.rest.util.PaginationUtil;
import io.github.pascalgrimaud.service.dto.MicroserviceLabelCriteria;
import io.github.pascalgrimaud.service.MicroserviceLabelQueryService;
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
 * REST controller for managing MicroserviceLabel.
 */
@RestController
@RequestMapping("/api")
public class MicroserviceLabelResource {

    private final Logger log = LoggerFactory.getLogger(MicroserviceLabelResource.class);

    private static final String ENTITY_NAME = "microserviceLabel";

    private final MicroserviceLabelService microserviceLabelService;

    private final MicroserviceLabelQueryService microserviceLabelQueryService;

    public MicroserviceLabelResource(MicroserviceLabelService microserviceLabelService, MicroserviceLabelQueryService microserviceLabelQueryService) {
        this.microserviceLabelService = microserviceLabelService;
        this.microserviceLabelQueryService = microserviceLabelQueryService;
    }

    /**
     * POST  /microservice-labels : Create a new microserviceLabel.
     *
     * @param microserviceLabel the microserviceLabel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new microserviceLabel, or with status 400 (Bad Request) if the microserviceLabel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/microservice-labels")
    @Timed
    public ResponseEntity<MicroserviceLabel> createMicroserviceLabel(@Valid @RequestBody MicroserviceLabel microserviceLabel) throws URISyntaxException {
        log.debug("REST request to save MicroserviceLabel : {}", microserviceLabel);
        if (microserviceLabel.getId() != null) {
            throw new BadRequestAlertException("A new microserviceLabel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MicroserviceLabel result = microserviceLabelService.save(microserviceLabel);
        return ResponseEntity.created(new URI("/api/microservice-labels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /microservice-labels : Updates an existing microserviceLabel.
     *
     * @param microserviceLabel the microserviceLabel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated microserviceLabel,
     * or with status 400 (Bad Request) if the microserviceLabel is not valid,
     * or with status 500 (Internal Server Error) if the microserviceLabel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/microservice-labels")
    @Timed
    public ResponseEntity<MicroserviceLabel> updateMicroserviceLabel(@Valid @RequestBody MicroserviceLabel microserviceLabel) throws URISyntaxException {
        log.debug("REST request to update MicroserviceLabel : {}", microserviceLabel);
        if (microserviceLabel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MicroserviceLabel result = microserviceLabelService.save(microserviceLabel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, microserviceLabel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /microservice-labels : get all the microserviceLabels.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of microserviceLabels in body
     */
    @GetMapping("/microservice-labels")
    @Timed
    public ResponseEntity<List<MicroserviceLabel>> getAllMicroserviceLabels(MicroserviceLabelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MicroserviceLabels by criteria: {}", criteria);
        Page<MicroserviceLabel> page = microserviceLabelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/microservice-labels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /microservice-labels/:id : get the "id" microserviceLabel.
     *
     * @param id the id of the microserviceLabel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the microserviceLabel, or with status 404 (Not Found)
     */
    @GetMapping("/microservice-labels/{id}")
    @Timed
    public ResponseEntity<MicroserviceLabel> getMicroserviceLabel(@PathVariable Long id) {
        log.debug("REST request to get MicroserviceLabel : {}", id);
        Optional<MicroserviceLabel> microserviceLabel = microserviceLabelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(microserviceLabel);
    }

    /**
     * DELETE  /microservice-labels/:id : delete the "id" microserviceLabel.
     *
     * @param id the id of the microserviceLabel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/microservice-labels/{id}")
    @Timed
    public ResponseEntity<Void> deleteMicroserviceLabel(@PathVariable Long id) {
        log.debug("REST request to delete MicroserviceLabel : {}", id);
        microserviceLabelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
