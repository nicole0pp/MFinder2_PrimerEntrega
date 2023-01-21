package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.AlbumsService;
import com.mycompany.myapp.domain.Albums;
import com.mycompany.myapp.repository.AlbumsRepository;
import com.mycompany.myapp.service.dto.AlbumsDTO;
import com.mycompany.myapp.service.mapper.AlbumsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Albums}.
 */
@Service
@Transactional
public class AlbumsServiceImpl implements AlbumsService {

    private final Logger log = LoggerFactory.getLogger(AlbumsServiceImpl.class);

    private final AlbumsRepository albumsRepository;

    private final AlbumsMapper albumsMapper;

    public AlbumsServiceImpl(AlbumsRepository albumsRepository, AlbumsMapper albumsMapper) {
        this.albumsRepository = albumsRepository;
        this.albumsMapper = albumsMapper;
    }

    /**
     * Save a albums.
     *
     * @param albumsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AlbumsDTO save(AlbumsDTO albumsDTO) {
        log.debug("Request to save Albums : {}", albumsDTO);
        Albums albums = albumsMapper.toEntity(albumsDTO);
        albums = albumsRepository.save(albums);
        return albumsMapper.toDto(albums);
    }

    /**
     * Get all the albums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AlbumsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Albums");
        return albumsRepository.findAll(pageable)
            .map(albumsMapper::toDto);
    }


    /**
     * Get one albums by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AlbumsDTO> findOne(Long id) {
        log.debug("Request to get Albums : {}", id);
        return albumsRepository.findById(id)
            .map(albumsMapper::toDto);
    }

    /**
     * Delete the albums by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Albums : {}", id);
        albumsRepository.deleteById(id);
    }
}
