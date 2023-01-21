package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ListDetailsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ListDetails}.
 */
public interface ListDetailsService {

    /**
     * Save a listDetails.
     *
     * @param listDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    ListDetailsDTO save(ListDetailsDTO listDetailsDTO);

    /**
     * Get all the listDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ListDetailsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" listDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ListDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" listDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
