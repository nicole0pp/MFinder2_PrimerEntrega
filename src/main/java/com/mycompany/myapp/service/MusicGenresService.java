package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MusicGenresDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MusicGenres}.
 */
public interface MusicGenresService {

    /**
     * Save a musicGenres.
     *
     * @param musicGenresDTO the entity to save.
     * @return the persisted entity.
     */
    MusicGenresDTO save(MusicGenresDTO musicGenresDTO);

    /**
     * Get all the musicGenres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MusicGenresDTO> findAll(Pageable pageable);


    /**
     * Get the "id" musicGenres.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MusicGenresDTO> findOne(Long id);

    /**
     * Delete the "id" musicGenres.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
