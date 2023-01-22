package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SongService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SongDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Song}.
 */
@RestController
@RequestMapping("/api")
public class SongResource {

    private final Logger log = LoggerFactory.getLogger(SongResource.class);

    private static final String ENTITY_NAME = "Song";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SongService SongService;

    public SongResource(SongService SongService) {
        this.SongService = SongService;
    }

    /**
     * {@code POST  /Song} : Create a new Song.
     *
     * @param SongDTO the SongDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new SongDTO, or with status {@code 400 (Bad Request)} if the Song has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/Song")
    public ResponseEntity<SongDTO> createSong(@Valid @RequestBody SongDTO SongDTO) throws URISyntaxException {
        log.debug("REST request to save Song : {}", SongDTO);
        if (SongDTO.getId() != null) {
            throw new BadRequestAlertException("A new Song cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SongDTO result = SongService.save(SongDTO);
        return ResponseEntity.created(new URI("/api/Song/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /Song} : Updates an existing Song.
     *
     * @param SongDTO the SongDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated SongDTO,
     * or with status {@code 400 (Bad Request)} if the SongDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the SongDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/Song")
    public ResponseEntity<SongDTO> updateSong(@Valid @RequestBody SongDTO SongDTO) throws URISyntaxException {
        log.debug("REST request to update Song : {}", SongDTO);
        if (SongDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SongDTO result = SongService.save(SongDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, SongDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /Song} : get all the Song.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Song in body.
     */
    @GetMapping("/Song")
    public ResponseEntity<List<SongDTO>> getAllSong(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Song");
        Page<SongDTO> page = SongService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /Song/:id} : get the "id" Song.
     *
     * @param id the id of the SongDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the SongDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/Song/{id}")
    public ResponseEntity<SongDTO> getSong(@PathVariable Long id) {
        log.debug("REST request to get Song : {}", id);
        Optional<SongDTO> SongDTO = SongService.findOne(id);
        return ResponseUtil.wrapOrNotFound(SongDTO);
    }

    /**
     * {@code DELETE  /Song/:id} : delete the "id" Song.
     *
     * @param id the id of the SongDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/Song/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        log.debug("REST request to delete Song : {}", id);
        SongService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
