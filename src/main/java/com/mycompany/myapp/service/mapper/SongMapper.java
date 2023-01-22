package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SongDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Song} and its DTO {@link SongDTO}.
 */
@Mapper(componentModel = "spring", uses = {MusicGenreMapper.class, AlbumMapper.class, ListDetailsMapper.class})
public interface SongMapper extends EntityMapper<SongDTO, Song> {

    @Mapping(source = "musicGenre.id", target = "musicGenreId")
    @Mapping(source = "album.id", target = "albumId")
    @Mapping(source = "listDetails.id", target = "listDetailsId")
    SongDTO toDto(Song Song);

    @Mapping(source = "musicGenreId", target = "musicGenre")
    @Mapping(source = "albumId", target = "album")
    @Mapping(source = "listDetailsId", target = "listDetails")
    Song toEntity(SongDTO SongDTO);

    default Song fromId(Long id) {
        if (id == null) {
            return null;
        }
        Song Song = new Song();
        Song.setId(id);
        return Song;
    }
}
