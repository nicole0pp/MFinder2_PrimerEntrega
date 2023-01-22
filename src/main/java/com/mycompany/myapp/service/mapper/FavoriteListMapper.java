package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.FavoriteListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FavoriteList} and its DTO {@link FavoriteListDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FavoriteListMapper extends EntityMapper<FavoriteListDTO, FavoriteList> {



    default FavoriteList fromId(Long id) {
        if (id == null) {
            return null;
        }
        FavoriteList FavoriteList = new FavoriteList();
        FavoriteList.setId(id);
        return FavoriteList;
    }
}
