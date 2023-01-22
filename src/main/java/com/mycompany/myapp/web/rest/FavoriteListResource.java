package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.FavoriteListService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.FavoriteListDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.FavoriteList}.
 */
@RestController
@RequestMapping("/api")
public class FavoriteListResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteListResource.class);

    private static final String ENTITY_NAME = "FavoriteList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavoriteListService FavoriteListService;

    public FavoriteListResource(FavoriteListService FavoriteListService) {
        this.FavoriteListService = FavoriteListService;
    }

    /**
     * {@code POST  /reproduction-lists} : Create a new FavoriteList.
     *
     * @param FavoriteListDTO the FavoriteListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new FavoriteListDTO, or with status {@code 400 (Bad Request)} if the FavoriteList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reproduction-lists")
    public ResponseEntity<FavoriteListDTO> createFavoriteList(@Valid @RequestBody FavoriteListDTO FavoriteListDTO) throws URISyntaxException {
        log.debug("REST request to save FavoriteList : {}", FavoriteListDTO);
        if (FavoriteListDTO.getId() != null) {
            throw new BadRequestAlertException("A new FavoriteList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavoriteListDTO result = FavoriteListService.save(FavoriteListDTO);
        return ResponseEntity.created(new URI("/api/reproduction-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reproduction-lists} : Updates an existing FavoriteList.
     *
     * @param FavoriteListDTO the FavoriteListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated FavoriteListDTO,
     * or with status {@code 400 (Bad Request)} if the FavoriteListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the FavoriteListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reproduction-lists")
    public ResponseEntity<FavoriteListDTO> updateFavoriteList(@Valid @RequestBody FavoriteListDTO FavoriteListDTO) throws URISyntaxException {
        log.debug("REST request to update FavoriteList : {}", FavoriteListDTO);
        if (FavoriteListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FavoriteListDTO result = FavoriteListService.save(FavoriteListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, FavoriteListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reproduction-lists} : get all the FavoriteList.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of FavoriteList in body.
     */
    @GetMapping("/reproduction-lists")
    public ResponseEntity<List<FavoriteListDTO>> getAllFavoriteList(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of FavoriteList");
        Page<FavoriteListDTO> page = FavoriteListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reproduction-lists/:id} : get the "id" FavoriteList.
     *
     * @param id the id of the FavoriteListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the FavoriteListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reproduction-lists/{id}")
    public ResponseEntity<FavoriteListDTO> getFavoriteList(@PathVariable Long id) {
        log.debug("REST request to get FavoriteList : {}", id);
        Optional<FavoriteListDTO> FavoriteListDTO = FavoriteListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(FavoriteListDTO);
    }

    /**
     * {@code DELETE  /reproduction-lists/:id} : delete the "id" FavoriteList.
     *
     * @param id the id of the FavoriteListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reproduction-lists/{id}")
    public ResponseEntity<Void> deleteFavoriteList(@PathVariable Long id) {
        log.debug("REST request to delete FavoriteList : {}", id);
        FavoriteListService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
