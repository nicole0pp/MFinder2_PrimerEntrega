package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MusicGenresDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MusicGenres} and its DTO {@link MusicGenresDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MusicGenresMapper extends EntityMapper<MusicGenresDTO, MusicGenres> {



    default MusicGenres fromId(Long id) {
        if (id == null) {
            return null;
        }
        MusicGenres musicGenres = new MusicGenres();
        musicGenres.setId(id);
        return musicGenres;
    }
}
