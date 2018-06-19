package io.github.pascalgrimaud.service.impl;

import io.github.pascalgrimaud.service.FieldTestServiceImplEntityService;
import io.github.pascalgrimaud.domain.FieldTestServiceImplEntity;
import io.github.pascalgrimaud.repository.FieldTestServiceImplEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing FieldTestServiceImplEntity.
 */
@Service
@Transactional
public class FieldTestServiceImplEntityServiceImpl implements FieldTestServiceImplEntityService {

    private final Logger log = LoggerFactory.getLogger(FieldTestServiceImplEntityServiceImpl.class);

    private final FieldTestServiceImplEntityRepository fieldTestServiceImplEntityRepository;

    public FieldTestServiceImplEntityServiceImpl(FieldTestServiceImplEntityRepository fieldTestServiceImplEntityRepository) {
        this.fieldTestServiceImplEntityRepository = fieldTestServiceImplEntityRepository;
    }

    /**
     * Save a fieldTestServiceImplEntity.
     *
     * @param fieldTestServiceImplEntity the entity to save
     * @return the persisted entity
     */
    @Override
    public FieldTestServiceImplEntity save(FieldTestServiceImplEntity fieldTestServiceImplEntity) {
        log.debug("Request to save FieldTestServiceImplEntity : {}", fieldTestServiceImplEntity);        return fieldTestServiceImplEntityRepository.save(fieldTestServiceImplEntity);
    }

    /**
     * Get all the fieldTestServiceImplEntities.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FieldTestServiceImplEntity> findAll() {
        log.debug("Request to get all FieldTestServiceImplEntities");
        return fieldTestServiceImplEntityRepository.findAll();
    }


    /**
     * Get one fieldTestServiceImplEntity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FieldTestServiceImplEntity> findOne(Long id) {
        log.debug("Request to get FieldTestServiceImplEntity : {}", id);
        return fieldTestServiceImplEntityRepository.findById(id);
    }

    /**
     * Delete the fieldTestServiceImplEntity by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FieldTestServiceImplEntity : {}", id);
        fieldTestServiceImplEntityRepository.deleteById(id);
    }
}
