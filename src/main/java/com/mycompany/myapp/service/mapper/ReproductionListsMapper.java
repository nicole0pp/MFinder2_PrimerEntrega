package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ReproductionListsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReproductionLists} and its DTO {@link ReproductionListsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReproductionListsMapper extends EntityMapper<ReproductionListsDTO, ReproductionLists> {



    default ReproductionLists fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReproductionLists reproductionLists = new ReproductionLists();
        reproductionLists.setId(id);
        return reproductionLists;
    }
}
