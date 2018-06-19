package io.github.pascalgrimaud.service.mapper;

import io.github.pascalgrimaud.domain.*;
import io.github.pascalgrimaud.service.dto.EntityWithServiceImplPaginationAndDTODTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EntityWithServiceImplPaginationAndDTO and its DTO EntityWithServiceImplPaginationAndDTODTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EntityWithServiceImplPaginationAndDTOMapper extends EntityMapper<EntityWithServiceImplPaginationAndDTODTO, EntityWithServiceImplPaginationAndDTO> {



    default EntityWithServiceImplPaginationAndDTO fromId(Long id) {
        if (id == null) {
            return null;
        }
        EntityWithServiceImplPaginationAndDTO entityWithServiceImplPaginationAndDTO = new EntityWithServiceImplPaginationAndDTO();
        entityWithServiceImplPaginationAndDTO.setId(id);
        return entityWithServiceImplPaginationAndDTO;
    }
}
