package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ListDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ListDetails} and its DTO {@link ListDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {FavoriteListMapper.class})
public interface ListDetailsMapper extends EntityMapper<ListDetailsDTO, ListDetails> {

    @Mapping(source = "list.id", target = "listId")
    ListDetailsDTO toDto(ListDetails listDetails);

    @Mapping(source = "listId", target = "list")
    @Mapping(target = "Song", ignore = true)
    ListDetails toEntity(ListDetailsDTO listDetailsDTO);

    default ListDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        ListDetails listDetails = new ListDetails();
        listDetails.setId(id);
        return listDetails;
    }
}
