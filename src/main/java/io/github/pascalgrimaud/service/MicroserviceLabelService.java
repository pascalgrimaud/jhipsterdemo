package io.github.pascalgrimaud.service;

import io.github.pascalgrimaud.domain.MicroserviceLabel;
import io.github.pascalgrimaud.repository.MicroserviceLabelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing MicroserviceLabel.
 */
@Service
@Transactional
public class MicroserviceLabelService {

    private final Logger log = LoggerFactory.getLogger(MicroserviceLabelService.class);

    private final MicroserviceLabelRepository microserviceLabelRepository;

    public MicroserviceLabelService(MicroserviceLabelRepository microserviceLabelRepository) {
        this.microserviceLabelRepository = microserviceLabelRepository;
    }

    /**
     * Save a microserviceLabel.
     *
     * @param microserviceLabel the entity to save
     * @return the persisted entity
     */
    public MicroserviceLabel save(MicroserviceLabel microserviceLabel) {
        log.debug("Request to save MicroserviceLabel : {}", microserviceLabel);        return microserviceLabelRepository.save(microserviceLabel);
    }

    /**
     * Get all the microserviceLabels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MicroserviceLabel> findAll(Pageable pageable) {
        log.debug("Request to get all MicroserviceLabels");
        return microserviceLabelRepository.findAll(pageable);
    }


    /**
     * Get one microserviceLabel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MicroserviceLabel> findOne(Long id) {
        log.debug("Request to get MicroserviceLabel : {}", id);
        return microserviceLabelRepository.findById(id);
    }

    /**
     * Delete the microserviceLabel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MicroserviceLabel : {}", id);
        microserviceLabelRepository.deleteById(id);
    }
}
