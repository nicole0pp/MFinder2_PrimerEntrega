package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.MusicGenresService;
import com.mycompany.myapp.domain.MusicGenres;
import com.mycompany.myapp.repository.MusicGenresRepository;
import com.mycompany.myapp.service.dto.MusicGenresDTO;
import com.mycompany.myapp.service.mapper.MusicGenresMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MusicGenres}.
 */
@Service
@Transactional
public class MusicGenresServiceImpl implements MusicGenresService {

    private final Logger log = LoggerFactory.getLogger(MusicGenresServiceImpl.class);

    private final MusicGenresRepository musicGenresRepository;

    private final MusicGenresMapper musicGenresMapper;

    public MusicGenresServiceImpl(MusicGenresRepository musicGenresRepository, MusicGenresMapper musicGenresMapper) {
        this.musicGenresRepository = musicGenresRepository;
        this.musicGenresMapper = musicGenresMapper;
    }

    /**
     * Save a musicGenres.
     *
     * @param musicGenresDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MusicGenresDTO save(MusicGenresDTO musicGenresDTO) {
        log.debug("Request to save MusicGenres : {}", musicGenresDTO);
        MusicGenres musicGenres = musicGenresMapper.toEntity(musicGenresDTO);
        musicGenres = musicGenresRepository.save(musicGenres);
        return musicGenresMapper.toDto(musicGenres);
    }

    /**
     * Get all the musicGenres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MusicGenresDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MusicGenres");
        return musicGenresRepository.findAll(pageable)
            .map(musicGenresMapper::toDto);
    }


    /**
     * Get one musicGenres by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MusicGenresDTO> findOne(Long id) {
        log.debug("Request to get MusicGenres : {}", id);
        return musicGenresRepository.findById(id)
            .map(musicGenresMapper::toDto);
    }

    /**
     * Delete the musicGenres by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MusicGenres : {}", id);
        musicGenresRepository.deleteById(id);
    }
}
