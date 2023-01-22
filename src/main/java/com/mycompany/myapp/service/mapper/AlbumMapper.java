package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.AlbumDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Album} and its DTO {@link AlbumDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlbumMapper extends EntityMapper<AlbumDTO, Album> {



    default Album fromId(Long id) {
        if (id == null) {
            return null;
        }
        Album Album = new Album();
        Album.setId(id);
        return Album;
    }
}
