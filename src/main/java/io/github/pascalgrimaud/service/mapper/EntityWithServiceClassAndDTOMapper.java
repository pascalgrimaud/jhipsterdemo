package io.github.pascalgrimaud.service.mapper;

import io.github.pascalgrimaud.domain.*;
import io.github.pascalgrimaud.service.dto.EntityWithServiceClassAndDTODTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EntityWithServiceClassAndDTO and its DTO EntityWithServiceClassAndDTODTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EntityWithServiceClassAndDTOMapper extends EntityMapper<EntityWithServiceClassAndDTODTO, EntityWithServiceClassAndDTO> {



    default EntityWithServiceClassAndDTO fromId(Long id) {
        if (id == null) {
            return null;
        }
        EntityWithServiceClassAndDTO entityWithServiceClassAndDTO = new EntityWithServiceClassAndDTO();
        entityWithServiceClassAndDTO.setId(id);
        return entityWithServiceClassAndDTO;
    }
}
