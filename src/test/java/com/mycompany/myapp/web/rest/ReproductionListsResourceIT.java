package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.ReproductionLists;
import com.mycompany.myapp.repository.ReproductionListsRepository;
import com.mycompany.myapp.service.ReproductionListsService;
import com.mycompany.myapp.service.dto.ReproductionListsDTO;
import com.mycompany.myapp.service.mapper.ReproductionListsMapper;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ReproductionListsResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class ReproductionListsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    @Autowired
    private ReproductionListsRepository reproductionListsRepository;

    @Autowired
    private ReproductionListsMapper reproductionListsMapper;

    @Autowired
    private ReproductionListsService reproductionListsService;

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

    private MockMvc restReproductionListsMockMvc;

    private ReproductionLists reproductionLists;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReproductionListsResource reproductionListsResource = new ReproductionListsResource(reproductionListsService);
        this.restReproductionListsMockMvc = MockMvcBuilders.standaloneSetup(reproductionListsResource)
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
    public static ReproductionLists createEntity(EntityManager em) {
        ReproductionLists reproductionLists = new ReproductionLists()
            .name(DEFAULT_NAME)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
        return reproductionLists;
    }

    @BeforeEach
    public void initTest() {
        reproductionLists = createEntity(em);
    }

    @Test
    @Transactional
    public void createReproductionLists() throws Exception {
        int databaseSizeBeforeCreate = reproductionListsRepository.findAll().size();

        // Create the ReproductionLists
        ReproductionListsDTO reproductionListsDTO = reproductionListsMapper.toDto(reproductionLists);
        restReproductionListsMockMvc.perform(post("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reproductionListsDTO)))
            .andExpect(status().isCreated());

        // Validate the ReproductionLists in the database
        List<ReproductionLists> reproductionListsList = reproductionListsRepository.findAll();
        assertThat(reproductionListsList).hasSize(databaseSizeBeforeCreate + 1);
        ReproductionLists testReproductionLists = reproductionListsList.get(reproductionListsList.size() - 1);
        assertThat(testReproductionLists.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReproductionLists.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testReproductionLists.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createReproductionListsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reproductionListsRepository.findAll().size();

        // Create the ReproductionLists with an existing ID
        reproductionLists.setId(1L);
        ReproductionListsDTO reproductionListsDTO = reproductionListsMapper.toDto(reproductionLists);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReproductionListsMockMvc.perform(post("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reproductionListsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReproductionLists in the database
        List<ReproductionLists> reproductionListsList = reproductionListsRepository.findAll();
        assertThat(reproductionListsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reproductionListsRepository.findAll().size();
        // set the field null
        reproductionLists.setName(null);

        // Create the ReproductionLists, which fails.
        ReproductionListsDTO reproductionListsDTO = reproductionListsMapper.toDto(reproductionLists);

        restReproductionListsMockMvc.perform(post("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reproductionListsDTO)))
            .andExpect(status().isBadRequest());

        List<ReproductionLists> reproductionListsList = reproductionListsRepository.findAll();
        assertThat(reproductionListsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReproductionLists() throws Exception {
        // Initialize the database
        reproductionListsRepository.saveAndFlush(reproductionLists);

        // Get all the reproductionListsList
        restReproductionListsMockMvc.perform(get("/api/reproduction-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reproductionLists.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }
    
    @Test
    @Transactional
    public void getReproductionLists() throws Exception {
        // Initialize the database
        reproductionListsRepository.saveAndFlush(reproductionLists);

        // Get the reproductionLists
        restReproductionListsMockMvc.perform(get("/api/reproduction-lists/{id}", reproductionLists.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reproductionLists.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    public void getNonExistingReproductionLists() throws Exception {
        // Get the reproductionLists
        restReproductionListsMockMvc.perform(get("/api/reproduction-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReproductionLists() throws Exception {
        // Initialize the database
        reproductionListsRepository.saveAndFlush(reproductionLists);

        int databaseSizeBeforeUpdate = reproductionListsRepository.findAll().size();

        // Update the reproductionLists
        ReproductionLists updatedReproductionLists = reproductionListsRepository.findById(reproductionLists.getId()).get();
        // Disconnect from session so that the updates on updatedReproductionLists are not directly saved in db
        em.detach(updatedReproductionLists);
        updatedReproductionLists
            .name(UPDATED_NAME)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);
        ReproductionListsDTO reproductionListsDTO = reproductionListsMapper.toDto(updatedReproductionLists);

        restReproductionListsMockMvc.perform(put("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reproductionListsDTO)))
            .andExpect(status().isOk());

        // Validate the ReproductionLists in the database
        List<ReproductionLists> reproductionListsList = reproductionListsRepository.findAll();
        assertThat(reproductionListsList).hasSize(databaseSizeBeforeUpdate);
        ReproductionLists testReproductionLists = reproductionListsList.get(reproductionListsList.size() - 1);
        assertThat(testReproductionLists.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReproductionLists.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testReproductionLists.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingReproductionLists() throws Exception {
        int databaseSizeBeforeUpdate = reproductionListsRepository.findAll().size();

        // Create the ReproductionLists
        ReproductionListsDTO reproductionListsDTO = reproductionListsMapper.toDto(reproductionLists);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReproductionListsMockMvc.perform(put("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reproductionListsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReproductionLists in the database
        List<ReproductionLists> reproductionListsList = reproductionListsRepository.findAll();
        assertThat(reproductionListsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReproductionLists() throws Exception {
        // Initialize the database
        reproductionListsRepository.saveAndFlush(reproductionLists);

        int databaseSizeBeforeDelete = reproductionListsRepository.findAll().size();

        // Delete the reproductionLists
        restReproductionListsMockMvc.perform(delete("/api/reproduction-lists/{id}", reproductionLists.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<ReproductionLists> reproductionListsList = reproductionListsRepository.findAll();
        assertThat(reproductionListsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReproductionLists.class);
        ReproductionLists reproductionLists1 = new ReproductionLists();
        reproductionLists1.setId(1L);
        ReproductionLists reproductionLists2 = new ReproductionLists();
        reproductionLists2.setId(reproductionLists1.getId());
        assertThat(reproductionLists1).isEqualTo(reproductionLists2);
        reproductionLists2.setId(2L);
        assertThat(reproductionLists1).isNotEqualTo(reproductionLists2);
        reproductionLists1.setId(null);
        assertThat(reproductionLists1).isNotEqualTo(reproductionLists2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReproductionListsDTO.class);
        ReproductionListsDTO reproductionListsDTO1 = new ReproductionListsDTO();
        reproductionListsDTO1.setId(1L);
        ReproductionListsDTO reproductionListsDTO2 = new ReproductionListsDTO();
        assertThat(reproductionListsDTO1).isNotEqualTo(reproductionListsDTO2);
        reproductionListsDTO2.setId(reproductionListsDTO1.getId());
        assertThat(reproductionListsDTO1).isEqualTo(reproductionListsDTO2);
        reproductionListsDTO2.setId(2L);
        assertThat(reproductionListsDTO1).isNotEqualTo(reproductionListsDTO2);
        reproductionListsDTO1.setId(null);
        assertThat(reproductionListsDTO1).isNotEqualTo(reproductionListsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reproductionListsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reproductionListsMapper.fromId(null)).isNull();
    }
}
