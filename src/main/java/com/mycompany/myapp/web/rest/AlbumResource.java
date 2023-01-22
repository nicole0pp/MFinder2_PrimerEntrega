package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.AlbumService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.AlbumDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Album}.
 */
@RestController
@RequestMapping("/api")
public class AlbumResource {

    private final Logger log = LoggerFactory.getLogger(AlbumResource.class);

    private static final String ENTITY_NAME = "Album";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlbumService AlbumService;

    public AlbumResource(AlbumService AlbumService) {
        this.AlbumService = AlbumService;
    }

    /**
     * {@code POST  /Album} : Create a new Album.
     *
     * @param AlbumDTO the AlbumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new AlbumDTO, or with status {@code 400 (Bad Request)} if the Album has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/Album")
    public ResponseEntity<AlbumDTO> createAlbum(@Valid @RequestBody AlbumDTO AlbumDTO) throws URISyntaxException {
        log.debug("REST request to save Album : {}", AlbumDTO);
        if (AlbumDTO.getId() != null) {
            throw new BadRequestAlertException("A new Album cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlbumDTO result = AlbumService.save(AlbumDTO);
        return ResponseEntity.created(new URI("/api/Album/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /Album} : Updates an existing Album.
     *
     * @param AlbumDTO the AlbumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated AlbumDTO,
     * or with status {@code 400 (Bad Request)} if the AlbumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the AlbumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/Album")
    public ResponseEntity<AlbumDTO> updateAlbum(@Valid @RequestBody AlbumDTO AlbumDTO) throws URISyntaxException {
        log.debug("REST request to update Album : {}", AlbumDTO);
        if (AlbumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AlbumDTO result = AlbumService.save(AlbumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, AlbumDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /Album} : get all the Album.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Album in body.
     */
    @GetMapping("/Album")
    public ResponseEntity<List<AlbumDTO>> getAllAlbum(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Album");
        Page<AlbumDTO> page = AlbumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /Album/:id} : get the "id" Album.
     *
     * @param id the id of the AlbumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the AlbumDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/Album/{id}")
    public ResponseEntity<AlbumDTO> getAlbum(@PathVariable Long id) {
        log.debug("REST request to get Album : {}", id);
        Optional<AlbumDTO> AlbumDTO = AlbumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(AlbumDTO);
    }

    /**
     * {@code DELETE  /Album/:id} : delete the "id" Album.
     *
     * @param id the id of the AlbumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/Album/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        log.debug("REST request to delete Album : {}", id);
        AlbumService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
