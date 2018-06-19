package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.service.EntityWithServiceClassAndDTOService;
import io.github.pascalgrimaud.web.rest.errors.BadRequestAlertException;
import io.github.pascalgrimaud.web.rest.util.HeaderUtil;
import io.github.pascalgrimaud.service.dto.EntityWithServiceClassAndDTODTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EntityWithServiceClassAndDTO.
 */
@RestController
@RequestMapping("/api")
public class EntityWithServiceClassAndDTOResource {

    private final Logger log = LoggerFactory.getLogger(EntityWithServiceClassAndDTOResource.class);

    private static final String ENTITY_NAME = "entityWithServiceClassAndDTO";

    private final EntityWithServiceClassAndDTOService entityWithServiceClassAndDTOService;

    public EntityWithServiceClassAndDTOResource(EntityWithServiceClassAndDTOService entityWithServiceClassAndDTOService) {
        this.entityWithServiceClassAndDTOService = entityWithServiceClassAndDTOService;
    }

    /**
     * POST  /entity-with-service-class-and-dtos : Create a new entityWithServiceClassAndDTO.
     *
     * @param entityWithServiceClassAndDTODTO the entityWithServiceClassAndDTODTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityWithServiceClassAndDTODTO, or with status 400 (Bad Request) if the entityWithServiceClassAndDTO has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-with-service-class-and-dtos")
    @Timed
    public ResponseEntity<EntityWithServiceClassAndDTODTO> createEntityWithServiceClassAndDTO(@RequestBody EntityWithServiceClassAndDTODTO entityWithServiceClassAndDTODTO) throws URISyntaxException {
        log.debug("REST request to save EntityWithServiceClassAndDTO : {}", entityWithServiceClassAndDTODTO);
        if (entityWithServiceClassAndDTODTO.getId() != null) {
            throw new BadRequestAlertException("A new entityWithServiceClassAndDTO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntityWithServiceClassAndDTODTO result = entityWithServiceClassAndDTOService.save(entityWithServiceClassAndDTODTO);
        return ResponseEntity.created(new URI("/api/entity-with-service-class-and-dtos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-with-service-class-and-dtos : Updates an existing entityWithServiceClassAndDTO.
     *
     * @param entityWithServiceClassAndDTODTO the entityWithServiceClassAndDTODTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityWithServiceClassAndDTODTO,
     * or with status 400 (Bad Request) if the entityWithServiceClassAndDTODTO is not valid,
     * or with status 500 (Internal Server Error) if the entityWithServiceClassAndDTODTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-with-service-class-and-dtos")
    @Timed
    public ResponseEntity<EntityWithServiceClassAndDTODTO> updateEntityWithServiceClassAndDTO(@RequestBody EntityWithServiceClassAndDTODTO entityWithServiceClassAndDTODTO) throws URISyntaxException {
        log.debug("REST request to update EntityWithServiceClassAndDTO : {}", entityWithServiceClassAndDTODTO);
        if (entityWithServiceClassAndDTODTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EntityWithServiceClassAndDTODTO result = entityWithServiceClassAndDTOService.save(entityWithServiceClassAndDTODTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityWithServiceClassAndDTODTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-with-service-class-and-dtos : get all the entityWithServiceClassAndDTOS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entityWithServiceClassAndDTOS in body
     */
    @GetMapping("/entity-with-service-class-and-dtos")
    @Timed
    public List<EntityWithServiceClassAndDTODTO> getAllEntityWithServiceClassAndDTOS() {
        log.debug("REST request to get all EntityWithServiceClassAndDTOS");
        return entityWithServiceClassAndDTOService.findAll();
    }

    /**
     * GET  /entity-with-service-class-and-dtos/:id : get the "id" entityWithServiceClassAndDTO.
     *
     * @param id the id of the entityWithServiceClassAndDTODTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityWithServiceClassAndDTODTO, or with status 404 (Not Found)
     */
    @GetMapping("/entity-with-service-class-and-dtos/{id}")
    @Timed
    public ResponseEntity<EntityWithServiceClassAndDTODTO> getEntityWithServiceClassAndDTO(@PathVariable Long id) {
        log.debug("REST request to get EntityWithServiceClassAndDTO : {}", id);
        Optional<EntityWithServiceClassAndDTODTO> entityWithServiceClassAndDTODTO = entityWithServiceClassAndDTOService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entityWithServiceClassAndDTODTO);
    }

    /**
     * DELETE  /entity-with-service-class-and-dtos/:id : delete the "id" entityWithServiceClassAndDTO.
     *
     * @param id the id of the entityWithServiceClassAndDTODTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-with-service-class-and-dtos/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityWithServiceClassAndDTO(@PathVariable Long id) {
        log.debug("REST request to delete EntityWithServiceClassAndDTO : {}", id);
        entityWithServiceClassAndDTOService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
