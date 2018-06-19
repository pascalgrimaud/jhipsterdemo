package io.github.pascalgrimaud.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.pascalgrimaud.domain.MicroserviceBankAccount;
import io.github.pascalgrimaud.domain.*; // for static metamodels
import io.github.pascalgrimaud.repository.MicroserviceBankAccountRepository;
import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountCriteria;

import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountDTO;
import io.github.pascalgrimaud.service.mapper.MicroserviceBankAccountMapper;

/**
 * Service for executing complex queries for MicroserviceBankAccount entities in the database.
 * The main input is a {@link MicroserviceBankAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MicroserviceBankAccountDTO} or a {@link Page} of {@link MicroserviceBankAccountDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MicroserviceBankAccountQueryService extends QueryService<MicroserviceBankAccount> {

    private final Logger log = LoggerFactory.getLogger(MicroserviceBankAccountQueryService.class);

    private final MicroserviceBankAccountRepository microserviceBankAccountRepository;

    private final MicroserviceBankAccountMapper microserviceBankAccountMapper;

    public MicroserviceBankAccountQueryService(MicroserviceBankAccountRepository microserviceBankAccountRepository, MicroserviceBankAccountMapper microserviceBankAccountMapper) {
        this.microserviceBankAccountRepository = microserviceBankAccountRepository;
        this.microserviceBankAccountMapper = microserviceBankAccountMapper;
    }

    /**
     * Return a {@link List} of {@link MicroserviceBankAccountDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MicroserviceBankAccountDTO> findByCriteria(MicroserviceBankAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MicroserviceBankAccount> specification = createSpecification(criteria);
        return microserviceBankAccountMapper.toDto(microserviceBankAccountRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MicroserviceBankAccountDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MicroserviceBankAccountDTO> findByCriteria(MicroserviceBankAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MicroserviceBankAccount> specification = createSpecification(criteria);
        return microserviceBankAccountRepository.findAll(specification, page)
            .map(microserviceBankAccountMapper::toDto);
    }

    /**
     * Function to convert MicroserviceBankAccountCriteria to a {@link Specification}
     */
    private Specification<MicroserviceBankAccount> createSpecification(MicroserviceBankAccountCriteria criteria) {
        Specification<MicroserviceBankAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MicroserviceBankAccount_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MicroserviceBankAccount_.name));
            }
            if (criteria.getBankNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBankNumber(), MicroserviceBankAccount_.bankNumber));
            }
            if (criteria.getAgencyNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAgencyNumber(), MicroserviceBankAccount_.agencyNumber));
            }
            if (criteria.getLastOperationDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastOperationDuration(), MicroserviceBankAccount_.lastOperationDuration));
            }
            if (criteria.getMeanOperationDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMeanOperationDuration(), MicroserviceBankAccount_.meanOperationDuration));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), MicroserviceBankAccount_.balance));
            }
            if (criteria.getOpeningDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpeningDay(), MicroserviceBankAccount_.openingDay));
            }
            if (criteria.getLastOperationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastOperationDate(), MicroserviceBankAccount_.lastOperationDate));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), MicroserviceBankAccount_.active));
            }
            if (criteria.getAccountType() != null) {
                specification = specification.and(buildSpecification(criteria.getAccountType(), MicroserviceBankAccount_.accountType));
            }
            if (criteria.getOperationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOperationId(), MicroserviceBankAccount_.operations, MicroserviceOperation_.id));
            }
        }
        return specification;
    }

}
