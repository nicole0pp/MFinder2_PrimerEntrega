package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SongsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Songs} and its DTO {@link SongsDTO}.
 */
@Mapper(componentModel = "spring", uses = {MusicGenreMapper.class, AlbumMapper.class, ListDetailsMapper.class})
public interface SongsMapper extends EntityMapper<SongsDTO, Songs> {

    @Mapping(source = "musicGenre.id", target = "musicGenreId")
    @Mapping(source = "album.id", target = "albumId")
    @Mapping(source = "listDetails.id", target = "listDetailsId")
    SongsDTO toDto(Songs songs);

    @Mapping(source = "musicGenreId", target = "musicGenre")
    @Mapping(source = "albumId", target = "album")
    @Mapping(source = "listDetailsId", target = "listDetails")
    Songs toEntity(SongsDTO songsDTO);

    default Songs fromId(Long id) {
        if (id == null) {
            return null;
        }
        Songs songs = new Songs();
        songs.setId(id);
        return songs;
    }
}
