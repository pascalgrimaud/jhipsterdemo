package io.github.pascalgrimaud.service;

import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing MicroserviceBankAccount.
 */
public interface MicroserviceBankAccountService {

    /**
     * Save a microserviceBankAccount.
     *
     * @param microserviceBankAccountDTO the entity to save
     * @return the persisted entity
     */
    MicroserviceBankAccountDTO save(MicroserviceBankAccountDTO microserviceBankAccountDTO);

    /**
     * Get all the microserviceBankAccounts.
     *
     * @return the list of entities
     */
    List<MicroserviceBankAccountDTO> findAll();


    /**
     * Get the "id" microserviceBankAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MicroserviceBankAccountDTO> findOne(Long id);

    /**
     * Delete the "id" microserviceBankAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
