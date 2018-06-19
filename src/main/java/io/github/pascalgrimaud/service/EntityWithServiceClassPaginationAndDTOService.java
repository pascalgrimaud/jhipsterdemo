package io.github.pascalgrimaud.service;

import io.github.pascalgrimaud.domain.EntityWithServiceClassPaginationAndDTO;
import io.github.pascalgrimaud.repository.EntityWithServiceClassPaginationAndDTORepository;
import io.github.pascalgrimaud.service.dto.EntityWithServiceClassPaginationAndDTODTO;
import io.github.pascalgrimaud.service.mapper.EntityWithServiceClassPaginationAndDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing EntityWithServiceClassPaginationAndDTO.
 */
@Service
@Transactional
public class EntityWithServiceClassPaginationAndDTOService {

    private final Logger log = LoggerFactory.getLogger(EntityWithServiceClassPaginationAndDTOService.class);

    private final EntityWithServiceClassPaginationAndDTORepository entityWithServiceClassPaginationAndDTORepository;

    private final EntityWithServiceClassPaginationAndDTOMapper entityWithServiceClassPaginationAndDTOMapper;

    public EntityWithServiceClassPaginationAndDTOService(EntityWithServiceClassPaginationAndDTORepository entityWithServiceClassPaginationAndDTORepository, EntityWithServiceClassPaginationAndDTOMapper entityWithServiceClassPaginationAndDTOMapper) {
        this.entityWithServiceClassPaginationAndDTORepository = entityWithServiceClassPaginationAndDTORepository;
        this.entityWithServiceClassPaginationAndDTOMapper = entityWithServiceClassPaginationAndDTOMapper;
    }

    /**
     * Save a entityWithServiceClassPaginationAndDTO.
     *
     * @param entityWithServiceClassPaginationAndDTODTO the entity to save
     * @return the persisted entity
     */
    public EntityWithServiceClassPaginationAndDTODTO save(EntityWithServiceClassPaginationAndDTODTO entityWithServiceClassPaginationAndDTODTO) {
        log.debug("Request to save EntityWithServiceClassPaginationAndDTO : {}", entityWithServiceClassPaginationAndDTODTO);
        EntityWithServiceClassPaginationAndDTO entityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTOMapper.toEntity(entityWithServiceClassPaginationAndDTODTO);
        entityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTORepository.save(entityWithServiceClassPaginationAndDTO);
        return entityWithServiceClassPaginationAndDTOMapper.toDto(entityWithServiceClassPaginationAndDTO);
    }

    /**
     * Get all the entityWithServiceClassPaginationAndDTOS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EntityWithServiceClassPaginationAndDTODTO> findAll(Pageable pageable) {
        log.debug("Request to get all EntityWithServiceClassPaginationAndDTOS");
        return entityWithServiceClassPaginationAndDTORepository.findAll(pageable)
            .map(entityWithServiceClassPaginationAndDTOMapper::toDto);
    }


    /**
     * Get one entityWithServiceClassPaginationAndDTO by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<EntityWithServiceClassPaginationAndDTODTO> findOne(Long id) {
        log.debug("Request to get EntityWithServiceClassPaginationAndDTO : {}", id);
        return entityWithServiceClassPaginationAndDTORepository.findById(id)
            .map(entityWithServiceClassPaginationAndDTOMapper::toDto);
    }

    /**
     * Delete the entityWithServiceClassPaginationAndDTO by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EntityWithServiceClassPaginationAndDTO : {}", id);
        entityWithServiceClassPaginationAndDTORepository.deleteById(id);
    }
}
