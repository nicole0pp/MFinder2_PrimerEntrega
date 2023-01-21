package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.ReproductionListsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.ReproductionListsDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReproductionLists}.
 */
@RestController
@RequestMapping("/api")
public class ReproductionListsResource {

    private final Logger log = LoggerFactory.getLogger(ReproductionListsResource.class);

    private static final String ENTITY_NAME = "reproductionLists";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReproductionListsService reproductionListsService;

    public ReproductionListsResource(ReproductionListsService reproductionListsService) {
        this.reproductionListsService = reproductionListsService;
    }

    /**
     * {@code POST  /reproduction-lists} : Create a new reproductionLists.
     *
     * @param reproductionListsDTO the reproductionListsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reproductionListsDTO, or with status {@code 400 (Bad Request)} if the reproductionLists has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reproduction-lists")
    public ResponseEntity<ReproductionListsDTO> createReproductionLists(@Valid @RequestBody ReproductionListsDTO reproductionListsDTO) throws URISyntaxException {
        log.debug("REST request to save ReproductionLists : {}", reproductionListsDTO);
        if (reproductionListsDTO.getId() != null) {
            throw new BadRequestAlertException("A new reproductionLists cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReproductionListsDTO result = reproductionListsService.save(reproductionListsDTO);
        return ResponseEntity.created(new URI("/api/reproduction-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reproduction-lists} : Updates an existing reproductionLists.
     *
     * @param reproductionListsDTO the reproductionListsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reproductionListsDTO,
     * or with status {@code 400 (Bad Request)} if the reproductionListsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reproductionListsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reproduction-lists")
    public ResponseEntity<ReproductionListsDTO> updateReproductionLists(@Valid @RequestBody ReproductionListsDTO reproductionListsDTO) throws URISyntaxException {
        log.debug("REST request to update ReproductionLists : {}", reproductionListsDTO);
        if (reproductionListsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReproductionListsDTO result = reproductionListsService.save(reproductionListsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reproductionListsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reproduction-lists} : get all the reproductionLists.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reproductionLists in body.
     */
    @GetMapping("/reproduction-lists")
    public ResponseEntity<List<ReproductionListsDTO>> getAllReproductionLists(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ReproductionLists");
        Page<ReproductionListsDTO> page = reproductionListsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reproduction-lists/:id} : get the "id" reproductionLists.
     *
     * @param id the id of the reproductionListsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reproductionListsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reproduction-lists/{id}")
    public ResponseEntity<ReproductionListsDTO> getReproductionLists(@PathVariable Long id) {
        log.debug("REST request to get ReproductionLists : {}", id);
        Optional<ReproductionListsDTO> reproductionListsDTO = reproductionListsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reproductionListsDTO);
    }

    /**
     * {@code DELETE  /reproduction-lists/:id} : delete the "id" reproductionLists.
     *
     * @param id the id of the reproductionListsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reproduction-lists/{id}")
    public ResponseEntity<Void> deleteReproductionLists(@PathVariable Long id) {
        log.debug("REST request to delete ReproductionLists : {}", id);
        reproductionListsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
