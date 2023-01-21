package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ReproductionListsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ReproductionLists}.
 */
public interface ReproductionListsService {

    /**
     * Save a reproductionLists.
     *
     * @param reproductionListsDTO the entity to save.
     * @return the persisted entity.
     */
    ReproductionListsDTO save(ReproductionListsDTO reproductionListsDTO);

    /**
     * Get all the reproductionLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReproductionListsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" reproductionLists.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReproductionListsDTO> findOne(Long id);

    /**
     * Delete the "id" reproductionLists.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
