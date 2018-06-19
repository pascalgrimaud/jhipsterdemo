package io.github.pascalgrimaud.service;

import io.github.pascalgrimaud.domain.EntityWithServiceClassAndPagination;
import io.github.pascalgrimaud.repository.EntityWithServiceClassAndPaginationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing EntityWithServiceClassAndPagination.
 */
@Service
@Transactional
public class EntityWithServiceClassAndPaginationService {

    private final Logger log = LoggerFactory.getLogger(EntityWithServiceClassAndPaginationService.class);

    private final EntityWithServiceClassAndPaginationRepository entityWithServiceClassAndPaginationRepository;

    public EntityWithServiceClassAndPaginationService(EntityWithServiceClassAndPaginationRepository entityWithServiceClassAndPaginationRepository) {
        this.entityWithServiceClassAndPaginationRepository = entityWithServiceClassAndPaginationRepository;
    }

    /**
     * Save a entityWithServiceClassAndPagination.
     *
     * @param entityWithServiceClassAndPagination the entity to save
     * @return the persisted entity
     */
    public EntityWithServiceClassAndPagination save(EntityWithServiceClassAndPagination entityWithServiceClassAndPagination) {
        log.debug("Request to save EntityWithServiceClassAndPagination : {}", entityWithServiceClassAndPagination);        return entityWithServiceClassAndPaginationRepository.save(entityWithServiceClassAndPagination);
    }

    /**
     * Get all the entityWithServiceClassAndPaginations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EntityWithServiceClassAndPagination> findAll(Pageable pageable) {
        log.debug("Request to get all EntityWithServiceClassAndPaginations");
        return entityWithServiceClassAndPaginationRepository.findAll(pageable);
    }


    /**
     * Get one entityWithServiceClassAndPagination by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<EntityWithServiceClassAndPagination> findOne(Long id) {
        log.debug("Request to get EntityWithServiceClassAndPagination : {}", id);
        return entityWithServiceClassAndPaginationRepository.findById(id);
    }

    /**
     * Delete the entityWithServiceClassAndPagination by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EntityWithServiceClassAndPagination : {}", id);
        entityWithServiceClassAndPaginationRepository.deleteById(id);
    }
}
