package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SongDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Song}.
 */
public interface SongService {

    /**
     * Save a Song.
     *
     * @param SongDTO the entity to save.
     * @return the persisted entity.
     */
    SongDTO save(SongDTO SongDTO);

    /**
     * Get all the Song.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SongDTO> findAll(Pageable pageable);


    /**
     * Get the "id" Song.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SongDTO> findOne(Long id);

    /**
     * Delete the "id" Song.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
