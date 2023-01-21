package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SongsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Songs}.
 */
public interface SongsService {

    /**
     * Save a songs.
     *
     * @param songsDTO the entity to save.
     * @return the persisted entity.
     */
    SongsDTO save(SongsDTO songsDTO);

    /**
     * Get all the songs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SongsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" songs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SongsDTO> findOne(Long id);

    /**
     * Delete the "id" songs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
