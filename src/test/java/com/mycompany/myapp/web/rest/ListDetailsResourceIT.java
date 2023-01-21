package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.ListDetails;
import com.mycompany.myapp.repository.ListDetailsRepository;
import com.mycompany.myapp.service.ListDetailsService;
import com.mycompany.myapp.service.dto.ListDetailsDTO;
import com.mycompany.myapp.service.mapper.ListDetailsMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ListDetailsResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class ListDetailsResourceIT {

    @Autowired
    private ListDetailsRepository listDetailsRepository;

    @Autowired
    private ListDetailsMapper listDetailsMapper;

    @Autowired
    private ListDetailsService listDetailsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restListDetailsMockMvc;

    private ListDetails listDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ListDetailsResource listDetailsResource = new ListDetailsResource(listDetailsService);
        this.restListDetailsMockMvc = MockMvcBuilders.standaloneSetup(listDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListDetails createEntity(EntityManager em) {
        ListDetails listDetails = new ListDetails();
        return listDetails;
    }

    @BeforeEach
    public void initTest() {
        listDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createListDetails() throws Exception {
        int databaseSizeBeforeCreate = listDetailsRepository.findAll().size();

        // Create the ListDetails
        ListDetailsDTO listDetailsDTO = listDetailsMapper.toDto(listDetails);
        restListDetailsMockMvc.perform(post("/api/list-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the ListDetails in the database
        List<ListDetails> listDetailsList = listDetailsRepository.findAll();
        assertThat(listDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        ListDetails testListDetails = listDetailsList.get(listDetailsList.size() - 1);
    }

    @Test
    @Transactional
    public void createListDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = listDetailsRepository.findAll().size();

        // Create the ListDetails with an existing ID
        listDetails.setId(1L);
        ListDetailsDTO listDetailsDTO = listDetailsMapper.toDto(listDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restListDetailsMockMvc.perform(post("/api/list-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ListDetails in the database
        List<ListDetails> listDetailsList = listDetailsRepository.findAll();
        assertThat(listDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllListDetails() throws Exception {
        // Initialize the database
        listDetailsRepository.saveAndFlush(listDetails);

        // Get all the listDetailsList
        restListDetailsMockMvc.perform(get("/api/list-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listDetails.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getListDetails() throws Exception {
        // Initialize the database
        listDetailsRepository.saveAndFlush(listDetails);

        // Get the listDetails
        restListDetailsMockMvc.perform(get("/api/list-details/{id}", listDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(listDetails.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingListDetails() throws Exception {
        // Get the listDetails
        restListDetailsMockMvc.perform(get("/api/list-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateListDetails() throws Exception {
        // Initialize the database
        listDetailsRepository.saveAndFlush(listDetails);

        int databaseSizeBeforeUpdate = listDetailsRepository.findAll().size();

        // Update the listDetails
        ListDetails updatedListDetails = listDetailsRepository.findById(listDetails.getId()).get();
        // Disconnect from session so that the updates on updatedListDetails are not directly saved in db
        em.detach(updatedListDetails);
        ListDetailsDTO listDetailsDTO = listDetailsMapper.toDto(updatedListDetails);

        restListDetailsMockMvc.perform(put("/api/list-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the ListDetails in the database
        List<ListDetails> listDetailsList = listDetailsRepository.findAll();
        assertThat(listDetailsList).hasSize(databaseSizeBeforeUpdate);
        ListDetails testListDetails = listDetailsList.get(listDetailsList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingListDetails() throws Exception {
        int databaseSizeBeforeUpdate = listDetailsRepository.findAll().size();

        // Create the ListDetails
        ListDetailsDTO listDetailsDTO = listDetailsMapper.toDto(listDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListDetailsMockMvc.perform(put("/api/list-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ListDetails in the database
        List<ListDetails> listDetailsList = listDetailsRepository.findAll();
        assertThat(listDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteListDetails() throws Exception {
        // Initialize the database
        listDetailsRepository.saveAndFlush(listDetails);

        int databaseSizeBeforeDelete = listDetailsRepository.findAll().size();

        // Delete the listDetails
        restListDetailsMockMvc.perform(delete("/api/list-details/{id}", listDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<ListDetails> listDetailsList = listDetailsRepository.findAll();
        assertThat(listDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ListDetails.class);
        ListDetails listDetails1 = new ListDetails();
        listDetails1.setId(1L);
        ListDetails listDetails2 = new ListDetails();
        listDetails2.setId(listDetails1.getId());
        assertThat(listDetails1).isEqualTo(listDetails2);
        listDetails2.setId(2L);
        assertThat(listDetails1).isNotEqualTo(listDetails2);
        listDetails1.setId(null);
        assertThat(listDetails1).isNotEqualTo(listDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ListDetailsDTO.class);
        ListDetailsDTO listDetailsDTO1 = new ListDetailsDTO();
        listDetailsDTO1.setId(1L);
        ListDetailsDTO listDetailsDTO2 = new ListDetailsDTO();
        assertThat(listDetailsDTO1).isNotEqualTo(listDetailsDTO2);
        listDetailsDTO2.setId(listDetailsDTO1.getId());
        assertThat(listDetailsDTO1).isEqualTo(listDetailsDTO2);
        listDetailsDTO2.setId(2L);
        assertThat(listDetailsDTO1).isNotEqualTo(listDetailsDTO2);
        listDetailsDTO1.setId(null);
        assertThat(listDetailsDTO1).isNotEqualTo(listDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(listDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(listDetailsMapper.fromId(null)).isNull();
    }
}
