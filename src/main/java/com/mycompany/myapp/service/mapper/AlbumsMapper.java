package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.AlbumsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Albums} and its DTO {@link AlbumsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlbumsMapper extends EntityMapper<AlbumsDTO, Albums> {



    default Albums fromId(Long id) {
        if (id == null) {
            return null;
        }
        Albums albums = new Albums();
        albums.setId(id);
        return albums;
    }
}
