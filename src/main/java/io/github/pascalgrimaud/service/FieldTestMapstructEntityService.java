package io.github.pascalgrimaud.service;

import io.github.pascalgrimaud.domain.FieldTestMapstructEntity;
import io.github.pascalgrimaud.repository.FieldTestMapstructEntityRepository;
import io.github.pascalgrimaud.service.dto.FieldTestMapstructEntityDTO;
import io.github.pascalgrimaud.service.mapper.FieldTestMapstructEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing FieldTestMapstructEntity.
 */
@Service
@Transactional
public class FieldTestMapstructEntityService {

    private final Logger log = LoggerFactory.getLogger(FieldTestMapstructEntityService.class);

    private final FieldTestMapstructEntityRepository fieldTestMapstructEntityRepository;

    private final FieldTestMapstructEntityMapper fieldTestMapstructEntityMapper;

    public FieldTestMapstructEntityService(FieldTestMapstructEntityRepository fieldTestMapstructEntityRepository, FieldTestMapstructEntityMapper fieldTestMapstructEntityMapper) {
        this.fieldTestMapstructEntityRepository = fieldTestMapstructEntityRepository;
        this.fieldTestMapstructEntityMapper = fieldTestMapstructEntityMapper;
    }

    /**
     * Save a fieldTestMapstructEntity.
     *
     * @param fieldTestMapstructEntityDTO the entity to save
     * @return the persisted entity
     */
    public FieldTestMapstructEntityDTO save(FieldTestMapstructEntityDTO fieldTestMapstructEntityDTO) {
        log.debug("Request to save FieldTestMapstructEntity : {}", fieldTestMapstructEntityDTO);
        FieldTestMapstructEntity fieldTestMapstructEntity = fieldTestMapstructEntityMapper.toEntity(fieldTestMapstructEntityDTO);
        fieldTestMapstructEntity = fieldTestMapstructEntityRepository.save(fieldTestMapstructEntity);
        return fieldTestMapstructEntityMapper.toDto(fieldTestMapstructEntity);
    }

    /**
     * Get all the fieldTestMapstructEntities.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FieldTestMapstructEntityDTO> findAll() {
        log.debug("Request to get all FieldTestMapstructEntities");
        return fieldTestMapstructEntityRepository.findAll().stream()
            .map(fieldTestMapstructEntityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one fieldTestMapstructEntity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<FieldTestMapstructEntityDTO> findOne(Long id) {
        log.debug("Request to get FieldTestMapstructEntity : {}", id);
        return fieldTestMapstructEntityRepository.findById(id)
            .map(fieldTestMapstructEntityMapper::toDto);
    }

    /**
     * Delete the fieldTestMapstructEntity by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FieldTestMapstructEntity : {}", id);
        fieldTestMapstructEntityRepository.deleteById(id);
    }
}
