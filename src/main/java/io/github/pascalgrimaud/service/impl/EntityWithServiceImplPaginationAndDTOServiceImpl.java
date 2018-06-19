package io.github.pascalgrimaud.service.impl;

import io.github.pascalgrimaud.service.EntityWithServiceImplPaginationAndDTOService;
import io.github.pascalgrimaud.domain.EntityWithServiceImplPaginationAndDTO;
import io.github.pascalgrimaud.repository.EntityWithServiceImplPaginationAndDTORepository;
import io.github.pascalgrimaud.service.dto.EntityWithServiceImplPaginationAndDTODTO;
import io.github.pascalgrimaud.service.mapper.EntityWithServiceImplPaginationAndDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing EntityWithServiceImplPaginationAndDTO.
 */
@Service
@Transactional
public class EntityWithServiceImplPaginationAndDTOServiceImpl implements EntityWithServiceImplPaginationAndDTOService {

    private final Logger log = LoggerFactory.getLogger(EntityWithServiceImplPaginationAndDTOServiceImpl.class);

    private final EntityWithServiceImplPaginationAndDTORepository entityWithServiceImplPaginationAndDTORepository;

    private final EntityWithServiceImplPaginationAndDTOMapper entityWithServiceImplPaginationAndDTOMapper;

    public EntityWithServiceImplPaginationAndDTOServiceImpl(EntityWithServiceImplPaginationAndDTORepository entityWithServiceImplPaginationAndDTORepository, EntityWithServiceImplPaginationAndDTOMapper entityWithServiceImplPaginationAndDTOMapper) {
        this.entityWithServiceImplPaginationAndDTORepository = entityWithServiceImplPaginationAndDTORepository;
        this.entityWithServiceImplPaginationAndDTOMapper = entityWithServiceImplPaginationAndDTOMapper;
    }

    /**
     * Save a entityWithServiceImplPaginationAndDTO.
     *
     * @param entityWithServiceImplPaginationAndDTODTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EntityWithServiceImplPaginationAndDTODTO save(EntityWithServiceImplPaginationAndDTODTO entityWithServiceImplPaginationAndDTODTO) {
        log.debug("Request to save EntityWithServiceImplPaginationAndDTO : {}", entityWithServiceImplPaginationAndDTODTO);
        EntityWithServiceImplPaginationAndDTO entityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTOMapper.toEntity(entityWithServiceImplPaginationAndDTODTO);
        entityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTORepository.save(entityWithServiceImplPaginationAndDTO);
        return entityWithServiceImplPaginationAndDTOMapper.toDto(entityWithServiceImplPaginationAndDTO);
    }

    /**
     * Get all the entityWithServiceImplPaginationAndDTOS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EntityWithServiceImplPaginationAndDTODTO> findAll(Pageable pageable) {
        log.debug("Request to get all EntityWithServiceImplPaginationAndDTOS");
        return entityWithServiceImplPaginationAndDTORepository.findAll(pageable)
            .map(entityWithServiceImplPaginationAndDTOMapper::toDto);
    }


    /**
     * Get one entityWithServiceImplPaginationAndDTO by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EntityWithServiceImplPaginationAndDTODTO> findOne(Long id) {
        log.debug("Request to get EntityWithServiceImplPaginationAndDTO : {}", id);
        return entityWithServiceImplPaginationAndDTORepository.findById(id)
            .map(entityWithServiceImplPaginationAndDTOMapper::toDto);
    }

    /**
     * Delete the entityWithServiceImplPaginationAndDTO by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EntityWithServiceImplPaginationAndDTO : {}", id);
        entityWithServiceImplPaginationAndDTORepository.deleteById(id);
    }
}
