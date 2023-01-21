package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ReproductionListsService;
import com.mycompany.myapp.domain.ReproductionLists;
import com.mycompany.myapp.repository.ReproductionListsRepository;
import com.mycompany.myapp.service.dto.ReproductionListsDTO;
import com.mycompany.myapp.service.mapper.ReproductionListsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ReproductionLists}.
 */
@Service
@Transactional
public class ReproductionListsServiceImpl implements ReproductionListsService {

    private final Logger log = LoggerFactory.getLogger(ReproductionListsServiceImpl.class);

    private final ReproductionListsRepository reproductionListsRepository;

    private final ReproductionListsMapper reproductionListsMapper;

    public ReproductionListsServiceImpl(ReproductionListsRepository reproductionListsRepository, ReproductionListsMapper reproductionListsMapper) {
        this.reproductionListsRepository = reproductionListsRepository;
        this.reproductionListsMapper = reproductionListsMapper;
    }

    /**
     * Save a reproductionLists.
     *
     * @param reproductionListsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ReproductionListsDTO save(ReproductionListsDTO reproductionListsDTO) {
        log.debug("Request to save ReproductionLists : {}", reproductionListsDTO);
        ReproductionLists reproductionLists = reproductionListsMapper.toEntity(reproductionListsDTO);
        reproductionLists = reproductionListsRepository.save(reproductionLists);
        return reproductionListsMapper.toDto(reproductionLists);
    }

    /**
     * Get all the reproductionLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReproductionListsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReproductionLists");
        return reproductionListsRepository.findAll(pageable)
            .map(reproductionListsMapper::toDto);
    }


    /**
     * Get one reproductionLists by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReproductionListsDTO> findOne(Long id) {
        log.debug("Request to get ReproductionLists : {}", id);
        return reproductionListsRepository.findById(id)
            .map(reproductionListsMapper::toDto);
    }

    /**
     * Delete the reproductionLists by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReproductionLists : {}", id);
        reproductionListsRepository.deleteById(id);
    }
}
