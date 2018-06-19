package io.github.pascalgrimaud.repository;

import io.github.pascalgrimaud.domain.MicroserviceLabel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MicroserviceLabel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MicroserviceLabelRepository extends JpaRepository<MicroserviceLabel, Long>, JpaSpecificationExecutor<MicroserviceLabel> {

}
