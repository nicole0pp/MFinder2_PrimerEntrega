package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.ListDetailsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.ListDetailsDTO;

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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ListDetails}.
 */
@RestController
@RequestMapping("/api")
public class ListDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ListDetailsResource.class);

    private static final String ENTITY_NAME = "listDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ListDetailsService listDetailsService;

    public ListDetailsResource(ListDetailsService listDetailsService) {
        this.listDetailsService = listDetailsService;
    }

    /**
     * {@code POST  /list-details} : Create a new listDetails.
     *
     * @param listDetailsDTO the listDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new listDetailsDTO, or with status {@code 400 (Bad Request)} if the listDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/list-details")
    public ResponseEntity<ListDetailsDTO> createListDetails(@RequestBody ListDetailsDTO listDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save ListDetails : {}", listDetailsDTO);
        if (listDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new listDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ListDetailsDTO result = listDetailsService.save(listDetailsDTO);
        return ResponseEntity.created(new URI("/api/list-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /list-details} : Updates an existing listDetails.
     *
     * @param listDetailsDTO the listDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated listDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the listDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the listDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/list-details")
    public ResponseEntity<ListDetailsDTO> updateListDetails(@RequestBody ListDetailsDTO listDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update ListDetails : {}", listDetailsDTO);
        if (listDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ListDetailsDTO result = listDetailsService.save(listDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, listDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /list-details} : get all the listDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of listDetails in body.
     */
    @GetMapping("/list-details")
    public ResponseEntity<List<ListDetailsDTO>> getAllListDetails(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ListDetails");
        Page<ListDetailsDTO> page = listDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /list-details/:id} : get the "id" listDetails.
     *
     * @param id the id of the listDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the listDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/list-details/{id}")
    public ResponseEntity<ListDetailsDTO> getListDetails(@PathVariable Long id) {
        log.debug("REST request to get ListDetails : {}", id);
        Optional<ListDetailsDTO> listDetailsDTO = listDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(listDetailsDTO);
    }

    /**
     * {@code DELETE  /list-details/:id} : delete the "id" listDetails.
     *
     * @param id the id of the listDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/list-details/{id}")
    public ResponseEntity<Void> deleteListDetails(@PathVariable Long id) {
        log.debug("REST request to delete ListDetails : {}", id);
        listDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
