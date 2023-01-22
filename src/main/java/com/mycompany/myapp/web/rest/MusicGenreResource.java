package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.MusicGenreService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.MusicGenreDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.MusicGenre}.
 */
@RestController
@RequestMapping("/api")
public class MusicGenreResource {

    private final Logger log = LoggerFactory.getLogger(MusicGenreResource.class);

    private static final String ENTITY_NAME = "MusicGenre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MusicGenreService MusicGenreService;

    public MusicGenreResource(MusicGenreService MusicGenreService) {
        this.MusicGenreService = MusicGenreService;
    }

    /**
     * {@code POST  /music-genres} : Create a new MusicGenre.
     *
     * @param MusicGenreDTO the MusicGenreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new MusicGenreDTO, or with status {@code 400 (Bad Request)} if the MusicGenre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/music-genres")
    public ResponseEntity<MusicGenreDTO> createMusicGenre(@Valid @RequestBody MusicGenreDTO MusicGenreDTO) throws URISyntaxException {
        log.debug("REST request to save MusicGenre : {}", MusicGenreDTO);
        if (MusicGenreDTO.getId() != null) {
            throw new BadRequestAlertException("A new MusicGenre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MusicGenreDTO result = MusicGenreService.save(MusicGenreDTO);
        return ResponseEntity.created(new URI("/api/music-genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /music-genres} : Updates an existing MusicGenre.
     *
     * @param MusicGenreDTO the MusicGenreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated MusicGenreDTO,
     * or with status {@code 400 (Bad Request)} if the MusicGenreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the MusicGenreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/music-genres")
    public ResponseEntity<MusicGenreDTO> updateMusicGenre(@Valid @RequestBody MusicGenreDTO MusicGenreDTO) throws URISyntaxException {
        log.debug("REST request to update MusicGenre : {}", MusicGenreDTO);
        if (MusicGenreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MusicGenreDTO result = MusicGenreService.save(MusicGenreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, MusicGenreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /music-genres} : get all the MusicGenre.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of MusicGenre in body.
     */
    @GetMapping("/music-genres")
    public ResponseEntity<List<MusicGenreDTO>> getAllMusicGenre(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of MusicGenre");
        Page<MusicGenreDTO> page = MusicGenreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /music-genres/:id} : get the "id" MusicGenre.
     *
     * @param id the id of the MusicGenreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the MusicGenreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/music-genres/{id}")
    public ResponseEntity<MusicGenreDTO> getMusicGenre(@PathVariable Long id) {
        log.debug("REST request to get MusicGenre : {}", id);
        Optional<MusicGenreDTO> MusicGenreDTO = MusicGenreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(MusicGenreDTO);
    }

    /**
     * {@code DELETE  /music-genres/:id} : delete the "id" MusicGenre.
     *
     * @param id the id of the MusicGenreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/music-genres/{id}")
    public ResponseEntity<Void> deleteMusicGenre(@PathVariable Long id) {
        log.debug("REST request to delete MusicGenre : {}", id);
        MusicGenreService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
