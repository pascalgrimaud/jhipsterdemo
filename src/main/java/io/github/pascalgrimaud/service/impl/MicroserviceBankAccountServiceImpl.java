package io.github.pascalgrimaud.service.impl;

import io.github.pascalgrimaud.service.MicroserviceBankAccountService;
import io.github.pascalgrimaud.domain.MicroserviceBankAccount;
import io.github.pascalgrimaud.repository.MicroserviceBankAccountRepository;
import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountDTO;
import io.github.pascalgrimaud.service.mapper.MicroserviceBankAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing MicroserviceBankAccount.
 */
@Service
@Transactional
public class MicroserviceBankAccountServiceImpl implements MicroserviceBankAccountService {

    private final Logger log = LoggerFactory.getLogger(MicroserviceBankAccountServiceImpl.class);

    private final MicroserviceBankAccountRepository microserviceBankAccountRepository;

    private final MicroserviceBankAccountMapper microserviceBankAccountMapper;

    public MicroserviceBankAccountServiceImpl(MicroserviceBankAccountRepository microserviceBankAccountRepository, MicroserviceBankAccountMapper microserviceBankAccountMapper) {
        this.microserviceBankAccountRepository = microserviceBankAccountRepository;
        this.microserviceBankAccountMapper = microserviceBankAccountMapper;
    }

    /**
     * Save a microserviceBankAccount.
     *
     * @param microserviceBankAccountDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MicroserviceBankAccountDTO save(MicroserviceBankAccountDTO microserviceBankAccountDTO) {
        log.debug("Request to save MicroserviceBankAccount : {}", microserviceBankAccountDTO);
        MicroserviceBankAccount microserviceBankAccount = microserviceBankAccountMapper.toEntity(microserviceBankAccountDTO);
        microserviceBankAccount = microserviceBankAccountRepository.save(microserviceBankAccount);
        return microserviceBankAccountMapper.toDto(microserviceBankAccount);
    }

    /**
     * Get all the microserviceBankAccounts.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MicroserviceBankAccountDTO> findAll() {
        log.debug("Request to get all MicroserviceBankAccounts");
        return microserviceBankAccountRepository.findAll().stream()
            .map(microserviceBankAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one microserviceBankAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MicroserviceBankAccountDTO> findOne(Long id) {
        log.debug("Request to get MicroserviceBankAccount : {}", id);
        return microserviceBankAccountRepository.findById(id)
            .map(microserviceBankAccountMapper::toDto);
    }

    /**
     * Delete the microserviceBankAccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MicroserviceBankAccount : {}", id);
        microserviceBankAccountRepository.deleteById(id);
    }
}
