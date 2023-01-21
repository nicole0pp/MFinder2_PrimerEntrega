package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.MusicGenres;
import com.mycompany.myapp.repository.MusicGenresRepository;
import com.mycompany.myapp.service.MusicGenresService;
import com.mycompany.myapp.service.dto.MusicGenresDTO;
import com.mycompany.myapp.service.mapper.MusicGenresMapper;
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
 * Integration tests for the {@Link MusicGenresResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class MusicGenresResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private MusicGenresRepository musicGenresRepository;

    @Autowired
    private MusicGenresMapper musicGenresMapper;

    @Autowired
    private MusicGenresService musicGenresService;

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

    private MockMvc restMusicGenresMockMvc;

    private MusicGenres musicGenres;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MusicGenresResource musicGenresResource = new MusicGenresResource(musicGenresService);
        this.restMusicGenresMockMvc = MockMvcBuilders.standaloneSetup(musicGenresResource)
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
    public static MusicGenres createEntity(EntityManager em) {
        MusicGenres musicGenres = new MusicGenres()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE);
        return musicGenres;
    }

    @BeforeEach
    public void initTest() {
        musicGenres = createEntity(em);
    }

    @Test
    @Transactional
    public void createMusicGenres() throws Exception {
        int databaseSizeBeforeCreate = musicGenresRepository.findAll().size();

        // Create the MusicGenres
        MusicGenresDTO musicGenresDTO = musicGenresMapper.toDto(musicGenres);
        restMusicGenresMockMvc.perform(post("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(musicGenresDTO)))
            .andExpect(status().isCreated());

        // Validate the MusicGenres in the database
        List<MusicGenres> musicGenresList = musicGenresRepository.findAll();
        assertThat(musicGenresList).hasSize(databaseSizeBeforeCreate + 1);
        MusicGenres testMusicGenres = musicGenresList.get(musicGenresList.size() - 1);
        assertThat(testMusicGenres.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMusicGenres.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createMusicGenresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = musicGenresRepository.findAll().size();

        // Create the MusicGenres with an existing ID
        musicGenres.setId(1L);
        MusicGenresDTO musicGenresDTO = musicGenresMapper.toDto(musicGenres);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusicGenresMockMvc.perform(post("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(musicGenresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MusicGenres in the database
        List<MusicGenres> musicGenresList = musicGenresRepository.findAll();
        assertThat(musicGenresList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicGenresRepository.findAll().size();
        // set the field null
        musicGenres.setName(null);

        // Create the MusicGenres, which fails.
        MusicGenresDTO musicGenresDTO = musicGenresMapper.toDto(musicGenres);

        restMusicGenresMockMvc.perform(post("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(musicGenresDTO)))
            .andExpect(status().isBadRequest());

        List<MusicGenres> musicGenresList = musicGenresRepository.findAll();
        assertThat(musicGenresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMusicGenres() throws Exception {
        // Initialize the database
        musicGenresRepository.saveAndFlush(musicGenres);

        // Get all the musicGenresList
        restMusicGenresMockMvc.perform(get("/api/music-genres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musicGenres.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getMusicGenres() throws Exception {
        // Initialize the database
        musicGenresRepository.saveAndFlush(musicGenres);

        // Get the musicGenres
        restMusicGenresMockMvc.perform(get("/api/music-genres/{id}", musicGenres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(musicGenres.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMusicGenres() throws Exception {
        // Get the musicGenres
        restMusicGenresMockMvc.perform(get("/api/music-genres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMusicGenres() throws Exception {
        // Initialize the database
        musicGenresRepository.saveAndFlush(musicGenres);

        int databaseSizeBeforeUpdate = musicGenresRepository.findAll().size();

        // Update the musicGenres
        MusicGenres updatedMusicGenres = musicGenresRepository.findById(musicGenres.getId()).get();
        // Disconnect from session so that the updates on updatedMusicGenres are not directly saved in db
        em.detach(updatedMusicGenres);
        updatedMusicGenres
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);
        MusicGenresDTO musicGenresDTO = musicGenresMapper.toDto(updatedMusicGenres);

        restMusicGenresMockMvc.perform(put("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(musicGenresDTO)))
            .andExpect(status().isOk());

        // Validate the MusicGenres in the database
        List<MusicGenres> musicGenresList = musicGenresRepository.findAll();
        assertThat(musicGenresList).hasSize(databaseSizeBeforeUpdate);
        MusicGenres testMusicGenres = musicGenresList.get(musicGenresList.size() - 1);
        assertThat(testMusicGenres.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMusicGenres.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingMusicGenres() throws Exception {
        int databaseSizeBeforeUpdate = musicGenresRepository.findAll().size();

        // Create the MusicGenres
        MusicGenresDTO musicGenresDTO = musicGenresMapper.toDto(musicGenres);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicGenresMockMvc.perform(put("/api/music-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(musicGenresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MusicGenres in the database
        List<MusicGenres> musicGenresList = musicGenresRepository.findAll();
        assertThat(musicGenresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMusicGenres() throws Exception {
        // Initialize the database
        musicGenresRepository.saveAndFlush(musicGenres);

        int databaseSizeBeforeDelete = musicGenresRepository.findAll().size();

        // Delete the musicGenres
        restMusicGenresMockMvc.perform(delete("/api/music-genres/{id}", musicGenres.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<MusicGenres> musicGenresList = musicGenresRepository.findAll();
        assertThat(musicGenresList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MusicGenres.class);
        MusicGenres musicGenres1 = new MusicGenres();
        musicGenres1.setId(1L);
        MusicGenres musicGenres2 = new MusicGenres();
        musicGenres2.setId(musicGenres1.getId());
        assertThat(musicGenres1).isEqualTo(musicGenres2);
        musicGenres2.setId(2L);
        assertThat(musicGenres1).isNotEqualTo(musicGenres2);
        musicGenres1.setId(null);
        assertThat(musicGenres1).isNotEqualTo(musicGenres2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MusicGenresDTO.class);
        MusicGenresDTO musicGenresDTO1 = new MusicGenresDTO();
        musicGenresDTO1.setId(1L);
        MusicGenresDTO musicGenresDTO2 = new MusicGenresDTO();
        assertThat(musicGenresDTO1).isNotEqualTo(musicGenresDTO2);
        musicGenresDTO2.setId(musicGenresDTO1.getId());
        assertThat(musicGenresDTO1).isEqualTo(musicGenresDTO2);
        musicGenresDTO2.setId(2L);
        assertThat(musicGenresDTO1).isNotEqualTo(musicGenresDTO2);
        musicGenresDTO1.setId(null);
        assertThat(musicGenresDTO1).isNotEqualTo(musicGenresDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(musicGenresMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(musicGenresMapper.fromId(null)).isNull();
    }
}
