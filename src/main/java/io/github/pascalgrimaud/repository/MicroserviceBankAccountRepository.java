package io.github.pascalgrimaud.repository;

import io.github.pascalgrimaud.domain.MicroserviceBankAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MicroserviceBankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MicroserviceBankAccountRepository extends JpaRepository<MicroserviceBankAccount, Long>, JpaSpecificationExecutor<MicroserviceBankAccount> {

}
