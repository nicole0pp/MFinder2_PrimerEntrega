package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ListDetailsService;
import com.mycompany.myapp.domain.ListDetails;
import com.mycompany.myapp.repository.ListDetailsRepository;
import com.mycompany.myapp.service.dto.ListDetailsDTO;
import com.mycompany.myapp.service.mapper.ListDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ListDetails}.
 */
@Service
@Transactional
public class ListDetailsServiceImpl implements ListDetailsService {

    private final Logger log = LoggerFactory.getLogger(ListDetailsServiceImpl.class);

    private final ListDetailsRepository listDetailsRepository;

    private final ListDetailsMapper listDetailsMapper;

    public ListDetailsServiceImpl(ListDetailsRepository listDetailsRepository, ListDetailsMapper listDetailsMapper) {
        this.listDetailsRepository = listDetailsRepository;
        this.listDetailsMapper = listDetailsMapper;
    }

    /**
     * Save a listDetails.
     *
     * @param listDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ListDetailsDTO save(ListDetailsDTO listDetailsDTO) {
        log.debug("Request to save ListDetails : {}", listDetailsDTO);
        ListDetails listDetails = listDetailsMapper.toEntity(listDetailsDTO);
        listDetails = listDetailsRepository.save(listDetails);
        return listDetailsMapper.toDto(listDetails);
    }

    /**
     * Get all the listDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ListDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ListDetails");
        return listDetailsRepository.findAll(pageable)
            .map(listDetailsMapper::toDto);
    }


    /**
     * Get one listDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ListDetailsDTO> findOne(Long id) {
        log.debug("Request to get ListDetails : {}", id);
        return listDetailsRepository.findById(id)
            .map(listDetailsMapper::toDto);
    }

    /**
     * Delete the listDetails by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ListDetails : {}", id);
        listDetailsRepository.deleteById(id);
    }
}
