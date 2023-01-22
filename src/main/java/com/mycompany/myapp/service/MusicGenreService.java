package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MusicGenreDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MusicGenre}.
 */
public interface MusicGenreService {

    /**
     * Save a MusicGenre.
     *
     * @param MusicGenreDTO the entity to save.
     * @return the persisted entity.
     */
    MusicGenreDTO save(MusicGenreDTO MusicGenreDTO);

    /**
     * Get all the MusicGenre.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MusicGenreDTO> findAll(Pageable pageable);


    /**
     * Get the "id" MusicGenre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MusicGenreDTO> findOne(Long id);

    /**
     * Delete the "id" MusicGenre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
