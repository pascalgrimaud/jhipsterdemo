package io.github.pascalgrimaud.service;

import io.github.pascalgrimaud.domain.EntityWithServiceClass;
import io.github.pascalgrimaud.repository.EntityWithServiceClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing EntityWithServiceClass.
 */
@Service
@Transactional
public class EntityWithServiceClassService {

    private final Logger log = LoggerFactory.getLogger(EntityWithServiceClassService.class);

    private final EntityWithServiceClassRepository entityWithServiceClassRepository;

    public EntityWithServiceClassService(EntityWithServiceClassRepository entityWithServiceClassRepository) {
        this.entityWithServiceClassRepository = entityWithServiceClassRepository;
    }

    /**
     * Save a entityWithServiceClass.
     *
     * @param entityWithServiceClass the entity to save
     * @return the persisted entity
     */
    public EntityWithServiceClass save(EntityWithServiceClass entityWithServiceClass) {
        log.debug("Request to save EntityWithServiceClass : {}", entityWithServiceClass);        return entityWithServiceClassRepository.save(entityWithServiceClass);
    }

    /**
     * Get all the entityWithServiceClasses.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EntityWithServiceClass> findAll() {
        log.debug("Request to get all EntityWithServiceClasses");
        return entityWithServiceClassRepository.findAll();
    }


    /**
     * Get one entityWithServiceClass by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<EntityWithServiceClass> findOne(Long id) {
        log.debug("Request to get EntityWithServiceClass : {}", id);
        return entityWithServiceClassRepository.findById(id);
    }

    /**
     * Delete the entityWithServiceClass by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EntityWithServiceClass : {}", id);
        entityWithServiceClassRepository.deleteById(id);
    }
}
