package io.github.pascalgrimaud.service.mapper;

import io.github.pascalgrimaud.domain.*;
import io.github.pascalgrimaud.service.dto.MicroserviceBankAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MicroserviceBankAccount and its DTO MicroserviceBankAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MicroserviceBankAccountMapper extends EntityMapper<MicroserviceBankAccountDTO, MicroserviceBankAccount> {


    @Mapping(target = "operations", ignore = true)
    MicroserviceBankAccount toEntity(MicroserviceBankAccountDTO microserviceBankAccountDTO);

    default MicroserviceBankAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        MicroserviceBankAccount microserviceBankAccount = new MicroserviceBankAccount();
        microserviceBankAccount.setId(id);
        return microserviceBankAccount;
    }
}
