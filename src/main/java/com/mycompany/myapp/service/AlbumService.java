package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AlbumDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Album}.
 */
public interface AlbumService {

    /**
     * Save a Album.
     *
     * @param AlbumDTO the entity to save.
     * @return the persisted entity.
     */
    AlbumDTO save(AlbumDTO AlbumDTO);

    /**
     * Get all the Album.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlbumDTO> findAll(Pageable pageable);


    /**
     * Get the "id" Album.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlbumDTO> findOne(Long id);

    /**
     * Delete the "id" Album.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
