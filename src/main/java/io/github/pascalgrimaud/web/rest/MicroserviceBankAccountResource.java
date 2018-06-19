package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.service.MicroserviceBankAccountService;
import io.github.pascalgrimaud.web.rest.errors.BadRequestAlertException;
import io.github.pascalgrimaud.web.rest.util.HeaderUtil;
import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountDTO;
import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountCriteria;
import io.github.pascalgrimaud.service.MicroserviceBankAccountQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MicroserviceBankAccount.
 */
@RestController
@RequestMapping("/api")
public class MicroserviceBankAccountResource {

    private final Logger log = LoggerFactory.getLogger(MicroserviceBankAccountResource.class);

    private static final String ENTITY_NAME = "microserviceBankAccount";

    private final MicroserviceBankAccountService microserviceBankAccountService;

    private final MicroserviceBankAccountQueryService microserviceBankAccountQueryService;

    public MicroserviceBankAccountResource(MicroserviceBankAccountService microserviceBankAccountService, MicroserviceBankAccountQueryService microserviceBankAccountQueryService) {
        this.microserviceBankAccountService = microserviceBankAccountService;
        this.microserviceBankAccountQueryService = microserviceBankAccountQueryService;
    }

    /**
     * POST  /microservice-bank-accounts : Create a new microserviceBankAccount.
     *
     * @param microserviceBankAccountDTO the microserviceBankAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new microserviceBankAccountDTO, or with status 400 (Bad Request) if the microserviceBankAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/microservice-bank-accounts")
    @Timed
    public ResponseEntity<MicroserviceBankAccountDTO> createMicroserviceBankAccount(@Valid @RequestBody MicroserviceBankAccountDTO microserviceBankAccountDTO) throws URISyntaxException {
        log.debug("REST request to save MicroserviceBankAccount : {}", microserviceBankAccountDTO);
        if (microserviceBankAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new microserviceBankAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MicroserviceBankAccountDTO result = microserviceBankAccountService.save(microserviceBankAccountDTO);
        return ResponseEntity.created(new URI("/api/microservice-bank-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /microservice-bank-accounts : Updates an existing microserviceBankAccount.
     *
     * @param microserviceBankAccountDTO the microserviceBankAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated microserviceBankAccountDTO,
     * or with status 400 (Bad Request) if the microserviceBankAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the microserviceBankAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/microservice-bank-accounts")
    @Timed
    public ResponseEntity<MicroserviceBankAccountDTO> updateMicroserviceBankAccount(@Valid @RequestBody MicroserviceBankAccountDTO microserviceBankAccountDTO) throws URISyntaxException {
        log.debug("REST request to update MicroserviceBankAccount : {}", microserviceBankAccountDTO);
        if (microserviceBankAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MicroserviceBankAccountDTO result = microserviceBankAccountService.save(microserviceBankAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, microserviceBankAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /microservice-bank-accounts : get all the microserviceBankAccounts.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of microserviceBankAccounts in body
     */
    @GetMapping("/microservice-bank-accounts")
    @Timed
    public ResponseEntity<List<MicroserviceBankAccountDTO>> getAllMicroserviceBankAccounts(MicroserviceBankAccountCriteria criteria) {
        log.debug("REST request to get MicroserviceBankAccounts by criteria: {}", criteria);
        List<MicroserviceBankAccountDTO> entityList = microserviceBankAccountQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /microservice-bank-accounts/:id : get the "id" microserviceBankAccount.
     *
     * @param id the id of the microserviceBankAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the microserviceBankAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/microservice-bank-accounts/{id}")
    @Timed
    public ResponseEntity<MicroserviceBankAccountDTO> getMicroserviceBankAccount(@PathVariable Long id) {
        log.debug("REST request to get MicroserviceBankAccount : {}", id);
        Optional<MicroserviceBankAccountDTO> microserviceBankAccountDTO = microserviceBankAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(microserviceBankAccountDTO);
    }

    /**
     * DELETE  /microservice-bank-accounts/:id : delete the "id" microserviceBankAccount.
     *
     * @param id the id of the microserviceBankAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/microservice-bank-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteMicroserviceBankAccount(@PathVariable Long id) {
        log.debug("REST request to delete MicroserviceBankAccount : {}", id);
        microserviceBankAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
