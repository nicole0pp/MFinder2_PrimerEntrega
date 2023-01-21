package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.SongsService;
import com.mycompany.myapp.domain.Songs;
import com.mycompany.myapp.repository.SongsRepository;
import com.mycompany.myapp.service.dto.SongsDTO;
import com.mycompany.myapp.service.mapper.SongsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Songs}.
 */
@Service
@Transactional
public class SongsServiceImpl implements SongsService {

    private final Logger log = LoggerFactory.getLogger(SongsServiceImpl.class);

    private final SongsRepository songsRepository;

    private final SongsMapper songsMapper;

    public SongsServiceImpl(SongsRepository songsRepository, SongsMapper songsMapper) {
        this.songsRepository = songsRepository;
        this.songsMapper = songsMapper;
    }

    /**
     * Save a songs.
     *
     * @param songsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SongsDTO save(SongsDTO songsDTO) {
        log.debug("Request to save Songs : {}", songsDTO);
        Songs songs = songsMapper.toEntity(songsDTO);
        songs = songsRepository.save(songs);
        return songsMapper.toDto(songs);
    }

    /**
     * Get all the songs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SongsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Songs");
        return songsRepository.findAll(pageable)
            .map(songsMapper::toDto);
    }


    /**
     * Get one songs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SongsDTO> findOne(Long id) {
        log.debug("Request to get Songs : {}", id);
        return songsRepository.findById(id)
            .map(songsMapper::toDto);
    }

    /**
     * Delete the songs by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Songs : {}", id);
        songsRepository.deleteById(id);
    }
}
