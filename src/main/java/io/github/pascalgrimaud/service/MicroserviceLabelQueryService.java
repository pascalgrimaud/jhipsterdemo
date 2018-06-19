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

import io.github.pascalgrimaud.domain.MicroserviceLabel;
import io.github.pascalgrimaud.domain.*; // for static metamodels
import io.github.pascalgrimaud.repository.MicroserviceLabelRepository;
import io.github.pascalgrimaud.service.dto.MicroserviceLabelCriteria;


/**
 * Service for executing complex queries for MicroserviceLabel entities in the database.
 * The main input is a {@link MicroserviceLabelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MicroserviceLabel} or a {@link Page} of {@link MicroserviceLabel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MicroserviceLabelQueryService extends QueryService<MicroserviceLabel> {

    private final Logger log = LoggerFactory.getLogger(MicroserviceLabelQueryService.class);

    private final MicroserviceLabelRepository microserviceLabelRepository;

    public MicroserviceLabelQueryService(MicroserviceLabelRepository microserviceLabelRepository) {
        this.microserviceLabelRepository = microserviceLabelRepository;
    }

    /**
     * Return a {@link List} of {@link MicroserviceLabel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MicroserviceLabel> findByCriteria(MicroserviceLabelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MicroserviceLabel> specification = createSpecification(criteria);
        return microserviceLabelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MicroserviceLabel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MicroserviceLabel> findByCriteria(MicroserviceLabelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MicroserviceLabel> specification = createSpecification(criteria);
        return microserviceLabelRepository.findAll(specification, page);
    }

    /**
     * Function to convert MicroserviceLabelCriteria to a {@link Specification}
     */
    private Specification<MicroserviceLabel> createSpecification(MicroserviceLabelCriteria criteria) {
        Specification<MicroserviceLabel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MicroserviceLabel_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), MicroserviceLabel_.label));
            }
            if (criteria.getOperationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOperationId(), MicroserviceLabel_.operations, MicroserviceOperation_.id));
            }
        }
        return specification;
    }

}
