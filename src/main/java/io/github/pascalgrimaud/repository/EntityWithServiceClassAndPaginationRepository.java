package io.github.pascalgrimaud.repository;

import io.github.pascalgrimaud.domain.EntityWithServiceClassAndPagination;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EntityWithServiceClassAndPagination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithServiceClassAndPaginationRepository extends JpaRepository<EntityWithServiceClassAndPagination, Long> {

}
