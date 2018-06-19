package io.github.pascalgrimaud.repository;

import io.github.pascalgrimaud.domain.MicroserviceOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the MicroserviceOperation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MicroserviceOperationRepository extends JpaRepository<MicroserviceOperation, Long> {

    @Query(value = "select distinct microservice_operation from MicroserviceOperation microservice_operation left join fetch microservice_operation.labels",
        countQuery = "select count(distinct microservice_operation) from MicroserviceOperation microservice_operation")
    Page<MicroserviceOperation> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct microservice_operation from MicroserviceOperation microservice_operation left join fetch microservice_operation.labels")
    List<MicroserviceOperation> findAllWithEagerRelationships();

    @Query("select microservice_operation from MicroserviceOperation microservice_operation left join fetch microservice_operation.labels where microservice_operation.id =:id")
    Optional<MicroserviceOperation> findOneWithEagerRelationships(@Param("id") Long id);

}
