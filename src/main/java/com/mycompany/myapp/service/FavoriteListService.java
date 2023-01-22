package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.FavoriteListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.FavoriteList}.
 */
public interface FavoriteListService {

    /**
     * Save a FavoriteList.
     *
     * @param FavoriteListDTO the entity to save.
     * @return the persisted entity.
     */
    FavoriteListDTO save(FavoriteListDTO FavoriteListDTO);

    /**
     * Get all the FavoriteList.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FavoriteListDTO> findAll(Pageable pageable);


    /**
     * Get the "id" FavoriteList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FavoriteListDTO> findOne(Long id);

    /**
     * Delete the "id" FavoriteList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
