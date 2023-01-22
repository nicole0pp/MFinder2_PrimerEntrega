package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.SongService;
import com.mycompany.myapp.domain.Song;
import com.mycompany.myapp.repository.SongRepository;
import com.mycompany.myapp.service.dto.SongDTO;
import com.mycompany.myapp.service.mapper.SongMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Song}.
 */
@Service
@Transactional
public class SongServiceImpl implements SongService {

    private final Logger log = LoggerFactory.getLogger(SongServiceImpl.class);

    private final SongRepository SongRepository;

    private final SongMapper SongMapper;

    public SongServiceImpl(SongRepository SongRepository, SongMapper SongMapper) {
        this.SongRepository = SongRepository;
        this.SongMapper = SongMapper;
    }

    /**
     * Save a Song.
     *
     * @param SongDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SongDTO save(SongDTO SongDTO) {
        log.debug("Request to save Song : {}", SongDTO);
        Song Song = SongMapper.toEntity(SongDTO);
        Song = SongRepository.save(Song);
        return SongMapper.toDto(Song);
    }

    /**
     * Get all the Song.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SongDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Song");
        return SongRepository.findAll(pageable)
            .map(SongMapper::toDto);
    }


    /**
     * Get one Song by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SongDTO> findOne(Long id) {
        log.debug("Request to get Song : {}", id);
        return SongRepository.findById(id)
            .map(SongMapper::toDto);
    }

    /**
     * Delete the Song by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Song : {}", id);
        SongRepository.deleteById(id);
    }
}
