package io.github.pascalgrimaud.repository;

import io.github.pascalgrimaud.domain.EntityWithDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EntityWithDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityWithDTORepository extends JpaRepository<EntityWithDTO, Long> {

}
