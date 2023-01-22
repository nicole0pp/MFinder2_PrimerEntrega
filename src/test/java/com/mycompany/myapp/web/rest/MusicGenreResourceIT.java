package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.MusicGenre;
import com.mycompany.myapp.repository.MusicGenreRepository;
import com.mycompany.myapp.service.MusicGenreService;
import com.mycompany.myapp.service.dto.MusicGenreDTO;
import com.mycompany.myapp.service.mapper.MusicGenreMapper;
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
 * Integration tests for the {@Link MusicGenreResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class MusicGenreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private MusicGenreRepository MusicGenreRepository;

    @Autowired
    private MusicGenreMapper MusicGenreMapper;

    @Autowired
    private MusicGenreService MusicGenreService;

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

    private MockMvc restMusicGenreMockMvc;

    private MusicGenre MusicGenre;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MusicGenreResource MusicGenreResource = new MusicGenreResource(MusicGenreService);
        this.restMusicGenreMockMvc = MockMvcBuilders.standaloneSetup(MusicGenreResource)
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
    public static MusicGenre createEntity(EntityManager em) {
        MusicGenre MusicGenre = new MusicGenre()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE);
        return MusicGenre;
    }

    @BeforeEach
    public void initTest() {
        MusicGenre = createEntity(em);
    }

    @Test
    @Transactional
    public void createMusicGenre() throws Exception {
        int databaseSizeBeforeCreate = MusicGenreRepository.findAll().size();

        // Create the MusicGenre
        MusicGenreDTO MusicGenreDTO = MusicGenreMapper.toDto(MusicGenre);
        restMusicGenreMockMvc.perform(post("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(MusicGenreDTO)))
            .andExpect(status().isCreated());

        // Validate the MusicGenre in the database
        List<MusicGenre> MusicGenreList = MusicGenreRepository.findAll();
        assertThat(MusicGenreList).hasSize(databaseSizeBeforeCreate + 1);
        MusicGenre testMusicGenre = MusicGenreList.get(MusicGenreList.size() - 1);
        assertThat(testMusicGenre.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMusicGenre.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createMusicGenreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = MusicGenreRepository.findAll().size();

        // Create the MusicGenre with an existing ID
        MusicGenre.setId(1L);
        MusicGenreDTO MusicGenreDTO = MusicGenreMapper.toDto(MusicGenre);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusicGenreMockMvc.perform(post("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(MusicGenreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MusicGenre in the database
        List<MusicGenre> MusicGenreList = MusicGenreRepository.findAll();
        assertThat(MusicGenreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = MusicGenreRepository.findAll().size();
        // set the field null
        MusicGenre.setName(null);

        // Create the MusicGenre, which fails.
        MusicGenreDTO MusicGenreDTO = MusicGenreMapper.toDto(MusicGenre);

        restMusicGenreMockMvc.perform(post("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(MusicGenreDTO)))
            .andExpect(status().isBadRequest());

        List<MusicGenre> MusicGenreList = MusicGenreRepository.findAll();
        assertThat(MusicGenreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMusicGenre() throws Exception {
        // Initialize the database
        MusicGenreRepository.saveAndFlush(MusicGenre);

        // Get all the MusicGenreList
        restMusicGenreMockMvc.perform(get("/api/music-genres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(MusicGenre.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getMusicGenre() throws Exception {
        // Initialize the database
        MusicGenreRepository.saveAndFlush(MusicGenre);

        // Get the MusicGenre
        restMusicGenreMockMvc.perform(get("/api/music-genres/{id}", MusicGenre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(MusicGenre.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMusicGenre() throws Exception {
        // Get the MusicGenre
        restMusicGenreMockMvc.perform(get("/api/music-genres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMusicGenre() throws Exception {
        // Initialize the database
        MusicGenreRepository.saveAndFlush(MusicGenre);

        int databaseSizeBeforeUpdate = MusicGenreRepository.findAll().size();

        // Update the MusicGenre
        MusicGenre updatedMusicGenre = MusicGenreRepository.findById(MusicGenre.getId()).get();
        // Disconnect from session so that the updates on updatedMusicGenre are not directly saved in db
        em.detach(updatedMusicGenre);
        updatedMusicGenre
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);
        MusicGenreDTO MusicGenreDTO = MusicGenreMapper.toDto(updatedMusicGenre);

        restMusicGenreMockMvc.perform(put("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(MusicGenreDTO)))
            .andExpect(status().isOk());

        // Validate the MusicGenre in the database
        List<MusicGenre> MusicGenreList = MusicGenreRepository.findAll();
        assertThat(MusicGenreList).hasSize(databaseSizeBeforeUpdate);
        MusicGenre testMusicGenre = MusicGenreList.get(MusicGenreList.size() - 1);
        assertThat(testMusicGenre.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMusicGenre.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingMusicGenre() throws Exception {
        int databaseSizeBeforeUpdate = MusicGenreRepository.findAll().size();

        // Create the MusicGenre
        MusicGenreDTO MusicGenreDTO = MusicGenreMapper.toDto(MusicGenre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicGenreMockMvc.perform(put("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(MusicGenreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MusicGenre in the database
        List<MusicGenre> MusicGenreList = MusicGenreRepository.findAll();
        assertThat(MusicGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMusicGenre() throws Exception {
        // Initialize the database
        MusicGenreRepository.saveAndFlush(MusicGenre);

        int databaseSizeBeforeDelete = MusicGenreRepository.findAll().size();

        // Delete the MusicGenre
        restMusicGenreMockMvc.perform(delete("/api/music-genres/{id}", MusicGenre.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<MusicGenre> MusicGenreList = MusicGenreRepository.findAll();
        assertThat(MusicGenreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MusicGenre.class);
        MusicGenre MusicGenre1 = new MusicGenre();
        MusicGenre1.setId(1L);
        MusicGenre MusicGenre2 = new MusicGenre();
        MusicGenre2.setId(MusicGenre1.getId());
        assertThat(MusicGenre1).isEqualTo(MusicGenre2);
        MusicGenre2.setId(2L);
        assertThat(MusicGenre1).isNotEqualTo(MusicGenre2);
        MusicGenre1.setId(null);
        assertThat(MusicGenre1).isNotEqualTo(MusicGenre2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MusicGenreDTO.class);
        MusicGenreDTO MusicGenreDTO1 = new MusicGenreDTO();
        MusicGenreDTO1.setId(1L);
        MusicGenreDTO MusicGenreDTO2 = new MusicGenreDTO();
        assertThat(MusicGenreDTO1).isNotEqualTo(MusicGenreDTO2);
        MusicGenreDTO2.setId(MusicGenreDTO1.getId());
        assertThat(MusicGenreDTO1).isEqualTo(MusicGenreDTO2);
        MusicGenreDTO2.setId(2L);
        assertThat(MusicGenreDTO1).isNotEqualTo(MusicGenreDTO2);
        MusicGenreDTO1.setId(null);
        assertThat(MusicGenreDTO1).isNotEqualTo(MusicGenreDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(MusicGenreMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(MusicGenreMapper.fromId(null)).isNull();
    }
}
