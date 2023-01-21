package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AlbumsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Albums}.
 */
public interface AlbumsService {

    /**
     * Save a albums.
     *
     * @param albumsDTO the entity to save.
     * @return the persisted entity.
     */
    AlbumsDTO save(AlbumsDTO albumsDTO);

    /**
     * Get all the albums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlbumsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" albums.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlbumsDTO> findOne(Long id);

    /**
     * Delete the "id" albums.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
