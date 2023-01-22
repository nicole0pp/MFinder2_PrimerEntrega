package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.FavoriteListService;
import com.mycompany.myapp.domain.FavoriteList;
import com.mycompany.myapp.repository.FavoriteListRepository;
import com.mycompany.myapp.service.dto.FavoriteListDTO;
import com.mycompany.myapp.service.mapper.FavoriteListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FavoriteList}.
 */
@Service
@Transactional
public class FavoriteListServiceImpl implements FavoriteListService {

    private final Logger log = LoggerFactory.getLogger(FavoriteListServiceImpl.class);

    private final FavoriteListRepository FavoriteListRepository;

    private final FavoriteListMapper FavoriteListMapper;

    public FavoriteListServiceImpl(FavoriteListRepository FavoriteListRepository, FavoriteListMapper FavoriteListMapper) {
        this.FavoriteListRepository = FavoriteListRepository;
        this.FavoriteListMapper = FavoriteListMapper;
    }

    /**
     * Save a FavoriteList.
     *
     * @param FavoriteListDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FavoriteListDTO save(FavoriteListDTO FavoriteListDTO) {
        log.debug("Request to save FavoriteList : {}", FavoriteListDTO);
        FavoriteList FavoriteList = FavoriteListMapper.toEntity(FavoriteListDTO);
        FavoriteList = FavoriteListRepository.save(FavoriteList);
        return FavoriteListMapper.toDto(FavoriteList);
    }

    /**
     * Get all the FavoriteList.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavoriteListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FavoriteList");
        return FavoriteListRepository.findAll(pageable)
            .map(FavoriteListMapper::toDto);
    }


    /**
     * Get one FavoriteList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriteListDTO> findOne(Long id) {
        log.debug("Request to get FavoriteList : {}", id);
        return FavoriteListRepository.findById(id)
            .map(FavoriteListMapper::toDto);
    }

    /**
     * Delete the FavoriteList by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FavoriteList : {}", id);
        FavoriteListRepository.deleteById(id);
    }
}
