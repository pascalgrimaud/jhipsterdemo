package io.github.pascalgrimaud.service.impl;

import io.github.pascalgrimaud.service.EntityWithServiceImplAndPaginationService;
import io.github.pascalgrimaud.domain.EntityWithServiceImplAndPagination;
import io.github.pascalgrimaud.repository.EntityWithServiceImplAndPaginationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing EntityWithServiceImplAndPagination.
 */
@Service
@Transactional
public class EntityWithServiceImplAndPaginationServiceImpl implements EntityWithServiceImplAndPaginationService {

    private final Logger log = LoggerFactory.getLogger(EntityWithServiceImplAndPaginationServiceImpl.class);

    private final EntityWithServiceImplAndPaginationRepository entityWithServiceImplAndPaginationRepository;

    public EntityWithServiceImplAndPaginationServiceImpl(EntityWithServiceImplAndPaginationRepository entityWithServiceImplAndPaginationRepository) {
        this.entityWithServiceImplAndPaginationRepository = entityWithServiceImplAndPaginationRepository;
    }

    /**
     * Save a entityWithServiceImplAndPagination.
     *
     * @param entityWithServiceImplAndPagination the entity to save
     * @return the persisted entity
     */
    @Override
    public EntityWithServiceImplAndPagination save(EntityWithServiceImplAndPagination entityWithServiceImplAndPagination) {
        log.debug("Request to save EntityWithServiceImplAndPagination : {}", entityWithServiceImplAndPagination);        return entityWithServiceImplAndPaginationRepository.save(entityWithServiceImplAndPagination);
    }

    /**
     * Get all the entityWithServiceImplAndPaginations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EntityWithServiceImplAndPagination> findAll(Pageable pageable) {
        log.debug("Request to get all EntityWithServiceImplAndPaginations");
        return entityWithServiceImplAndPaginationRepository.findAll(pageable);
    }


    /**
     * Get one entityWithServiceImplAndPagination by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EntityWithServiceImplAndPagination> findOne(Long id) {
        log.debug("Request to get EntityWithServiceImplAndPagination : {}", id);
        return entityWithServiceImplAndPaginationRepository.findById(id);
    }

    /**
     * Delete the entityWithServiceImplAndPagination by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EntityWithServiceImplAndPagination : {}", id);
        entityWithServiceImplAndPaginationRepository.deleteById(id);
    }
}
