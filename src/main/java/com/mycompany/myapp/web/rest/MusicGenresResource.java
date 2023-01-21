package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.MusicGenresService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.MusicGenresDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MusicGenres}.
 */
@RestController
@RequestMapping("/api")
public class MusicGenresResource {

    private final Logger log = LoggerFactory.getLogger(MusicGenresResource.class);

    private static final String ENTITY_NAME = "musicGenres";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MusicGenresService musicGenresService;

    public MusicGenresResource(MusicGenresService musicGenresService) {
        this.musicGenresService = musicGenresService;
    }

    /**
     * {@code POST  /music-genres} : Create a new musicGenres.
     *
     * @param musicGenresDTO the musicGenresDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new musicGenresDTO, or with status {@code 400 (Bad Request)} if the musicGenres has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/music-genres")
    public ResponseEntity<MusicGenresDTO> createMusicGenres(@Valid @RequestBody MusicGenresDTO musicGenresDTO) throws URISyntaxException {
        log.debug("REST request to save MusicGenres : {}", musicGenresDTO);
        if (musicGenresDTO.getId() != null) {
            throw new BadRequestAlertException("A new musicGenres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MusicGenresDTO result = musicGenresService.save(musicGenresDTO);
        return ResponseEntity.created(new URI("/api/music-genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /music-genres} : Updates an existing musicGenres.
     *
     * @param musicGenresDTO the musicGenresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musicGenresDTO,
     * or with status {@code 400 (Bad Request)} if the musicGenresDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the musicGenresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/music-genres")
    public ResponseEntity<MusicGenresDTO> updateMusicGenres(@Valid @RequestBody MusicGenresDTO musicGenresDTO) throws URISyntaxException {
        log.debug("REST request to update MusicGenres : {}", musicGenresDTO);
        if (musicGenresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MusicGenresDTO result = musicGenresService.save(musicGenresDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, musicGenresDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /music-genres} : get all the musicGenres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of musicGenres in body.
     */
    @GetMapping("/music-genres")
    public ResponseEntity<List<MusicGenresDTO>> getAllMusicGenres(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of MusicGenres");
        Page<MusicGenresDTO> page = musicGenresService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /music-genres/:id} : get the "id" musicGenres.
     *
     * @param id the id of the musicGenresDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the musicGenresDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/music-genres/{id}")
    public ResponseEntity<MusicGenresDTO> getMusicGenres(@PathVariable Long id) {
        log.debug("REST request to get MusicGenres : {}", id);
        Optional<MusicGenresDTO> musicGenresDTO = musicGenresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(musicGenresDTO);
    }

    /**
     * {@code DELETE  /music-genres/:id} : delete the "id" musicGenres.
     *
     * @param id the id of the musicGenresDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/music-genres/{id}")
    public ResponseEntity<Void> deleteMusicGenres(@PathVariable Long id) {
        log.debug("REST request to delete MusicGenres : {}", id);
        musicGenresService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
