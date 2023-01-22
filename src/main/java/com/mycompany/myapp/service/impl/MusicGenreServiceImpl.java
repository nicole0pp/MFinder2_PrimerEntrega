package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.MusicGenreService;
import com.mycompany.myapp.domain.MusicGenre;
import com.mycompany.myapp.repository.MusicGenreRepository;
import com.mycompany.myapp.service.dto.MusicGenreDTO;
import com.mycompany.myapp.service.mapper.MusicGenreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MusicGenre}.
 */
@Service
@Transactional
public class MusicGenreServiceImpl implements MusicGenreService {

    private final Logger log = LoggerFactory.getLogger(MusicGenreServiceImpl.class);

    private final MusicGenreRepository MusicGenreRepository;

    private final MusicGenreMapper MusicGenreMapper;

    public MusicGenreServiceImpl(MusicGenreRepository MusicGenreRepository, MusicGenreMapper MusicGenreMapper) {
        this.MusicGenreRepository = MusicGenreRepository;
        this.MusicGenreMapper = MusicGenreMapper;
    }

    /**
     * Save a MusicGenre.
     *
     * @param MusicGenreDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MusicGenreDTO save(MusicGenreDTO MusicGenreDTO) {
        log.debug("Request to save MusicGenre : {}", MusicGenreDTO);
        MusicGenre MusicGenre = MusicGenreMapper.toEntity(MusicGenreDTO);
        MusicGenre = MusicGenreRepository.save(MusicGenre);
        return MusicGenreMapper.toDto(MusicGenre);
    }

    /**
     * Get all the MusicGenre.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MusicGenreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MusicGenre");
        return MusicGenreRepository.findAll(pageable)
            .map(MusicGenreMapper::toDto);
    }


    /**
     * Get one MusicGenre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MusicGenreDTO> findOne(Long id) {
        log.debug("Request to get MusicGenre : {}", id);
        return MusicGenreRepository.findById(id)
            .map(MusicGenreMapper::toDto);
    }

    /**
     * Delete the MusicGenre by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MusicGenre : {}", id);
        MusicGenreRepository.deleteById(id);
    }
}
