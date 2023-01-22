package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.AlbumService;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.repository.AlbumRepository;
import com.mycompany.myapp.service.dto.AlbumDTO;
import com.mycompany.myapp.service.mapper.AlbumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Album}.
 */
@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {

    private final Logger log = LoggerFactory.getLogger(AlbumServiceImpl.class);

    private final AlbumRepository AlbumRepository;

    private final AlbumMapper AlbumMapper;

    public AlbumServiceImpl(AlbumRepository AlbumRepository, AlbumMapper AlbumMapper) {
        this.AlbumRepository = AlbumRepository;
        this.AlbumMapper = AlbumMapper;
    }

    /**
     * Save a Album.
     *
     * @param AlbumDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AlbumDTO save(AlbumDTO AlbumDTO) {
        log.debug("Request to save Album : {}", AlbumDTO);
        Album Album = AlbumMapper.toEntity(AlbumDTO);
        Album = AlbumRepository.save(Album);
        return AlbumMapper.toDto(Album);
    }

    /**
     * Get all the Album.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AlbumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Album");
        return AlbumRepository.findAll(pageable)
            .map(AlbumMapper::toDto);
    }


    /**
     * Get one Album by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AlbumDTO> findOne(Long id) {
        log.debug("Request to get Album : {}", id);
        return AlbumRepository.findById(id)
            .map(AlbumMapper::toDto);
    }

    /**
     * Delete the Album by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Album : {}", id);
        AlbumRepository.deleteById(id);
    }
}
