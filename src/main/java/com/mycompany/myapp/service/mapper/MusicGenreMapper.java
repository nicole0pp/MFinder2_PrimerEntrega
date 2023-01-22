package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MusicGenreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MusicGenre} and its DTO {@link MusicGenreDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MusicGenreMapper extends EntityMapper<MusicGenreDTO, MusicGenre> {



    default MusicGenre fromId(Long id) {
        if (id == null) {
            return null;
        }
        MusicGenre MusicGenre = new MusicGenre();
        MusicGenre.setId(id);
        return MusicGenre;
    }
}
