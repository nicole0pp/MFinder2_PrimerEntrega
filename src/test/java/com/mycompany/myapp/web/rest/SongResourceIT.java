package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.Song;
import com.mycompany.myapp.repository.SongRepository;
import com.mycompany.myapp.service.SongService;
import com.mycompany.myapp.service.dto.SongDTO;
import com.mycompany.myapp.service.mapper.SongMapper;
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
import java.time.Duration;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link SongResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class SongResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final byte[] DEFAULT_AUDIO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AUDIO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AUDIO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AUDIO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ARTISTS = "AAAAAAAAAA";
    private static final String UPDATED_ARTISTS = "BBBBBBBBBB";

    @Autowired
    private SongRepository SongRepository;

    @Autowired
    private SongMapper SongMapper;

    @Autowired
    private SongService SongService;

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

    private MockMvc restSongMockMvc;

    private Song Song;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SongResource SongResource = new SongResource(SongService);
        this.restSongMockMvc = MockMvcBuilders.standaloneSetup(SongResource)
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
    public static Song createEntity(EntityManager em) {
        Song Song = new Song()
            .name(DEFAULT_NAME)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .duration(DEFAULT_DURATION)
            .audio(DEFAULT_AUDIO)
            .audioContentType(DEFAULT_AUDIO_CONTENT_TYPE)
            .artists(DEFAULT_ARTISTS);
        return Song;
    }

    @BeforeEach
    public void initTest() {
        Song = createEntity(em);
    }

    @Test
    @Transactional
    public void createSong() throws Exception {
        int databaseSizeBeforeCreate = SongRepository.findAll().size();

        // Create the Song
        SongDTO SongDTO = SongMapper.toDto(Song);
        restSongMockMvc.perform(post("/api/Song")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(SongDTO)))
            .andExpect(status().isCreated());

        // Validate the Song in the database
        List<Song> SongList = SongRepository.findAll();
        assertThat(SongList).hasSize(databaseSizeBeforeCreate + 1);
        Song testSong = SongList.get(SongList.size() - 1);
        assertThat(testSong.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSong.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testSong.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testSong.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSong.getAudio()).isEqualTo(DEFAULT_AUDIO);
        assertThat(testSong.getAudioContentType()).isEqualTo(DEFAULT_AUDIO_CONTENT_TYPE);
        assertThat(testSong.getArtists()).isEqualTo(DEFAULT_ARTISTS);
    }

    @Test
    @Transactional
    public void createSongWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = SongRepository.findAll().size();

        // Create the Song with an existing ID
        Song.setId(1L);
        SongDTO SongDTO = SongMapper.toDto(Song);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSongMockMvc.perform(post("/api/Song")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(SongDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> SongList = SongRepository.findAll();
        assertThat(SongList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = SongRepository.findAll().size();
        // set the field null
        Song.setName(null);

        // Create the Song, which fails.
        SongDTO SongDTO = SongMapper.toDto(Song);

        restSongMockMvc.perform(post("/api/Song")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(SongDTO)))
            .andExpect(status().isBadRequest());

        List<Song> SongList = SongRepository.findAll();
        assertThat(SongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSong() throws Exception {
        // Initialize the database
        SongRepository.saveAndFlush(Song);

        // Get all the SongList
        restSongMockMvc.perform(get("/api/Song?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(Song.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].audioContentType").value(hasItem(DEFAULT_AUDIO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].audio").value(hasItem(Base64Utils.encodeToString(DEFAULT_AUDIO))))
            .andExpect(jsonPath("$.[*].artists").value(hasItem(DEFAULT_ARTISTS.toString())));
    }
    
    @Test
    @Transactional
    public void getSong() throws Exception {
        // Initialize the database
        SongRepository.saveAndFlush(Song);

        // Get the Song
        restSongMockMvc.perform(get("/api/Song/{id}", Song.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(Song.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.audioContentType").value(DEFAULT_AUDIO_CONTENT_TYPE))
            .andExpect(jsonPath("$.audio").value(Base64Utils.encodeToString(DEFAULT_AUDIO)))
            .andExpect(jsonPath("$.artists").value(DEFAULT_ARTISTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSong() throws Exception {
        // Get the Song
        restSongMockMvc.perform(get("/api/Song/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSong() throws Exception {
        // Initialize the database
        SongRepository.saveAndFlush(Song);

        int databaseSizeBeforeUpdate = SongRepository.findAll().size();

        // Update the Song
        Song updatedSong = SongRepository.findById(Song.getId()).get();
        // Disconnect from session so that the updates on updatedSong are not directly saved in db
        em.detach(updatedSong);
        updatedSong
            .name(UPDATED_NAME)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .duration(UPDATED_DURATION)
            .audio(UPDATED_AUDIO)
            .audioContentType(UPDATED_AUDIO_CONTENT_TYPE)
            .artists(UPDATED_ARTISTS);
        SongDTO SongDTO = SongMapper.toDto(updatedSong);

        restSongMockMvc.perform(put("/api/Song")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(SongDTO)))
            .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> SongList = SongRepository.findAll();
        assertThat(SongList).hasSize(databaseSizeBeforeUpdate);
        Song testSong = SongList.get(SongList.size() - 1);
        assertThat(testSong.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSong.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testSong.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testSong.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSong.getAudio()).isEqualTo(UPDATED_AUDIO);
        assertThat(testSong.getAudioContentType()).isEqualTo(UPDATED_AUDIO_CONTENT_TYPE);
        assertThat(testSong.getArtists()).isEqualTo(UPDATED_ARTISTS);
    }

    @Test
    @Transactional
    public void updateNonExistingSong() throws Exception {
        int databaseSizeBeforeUpdate = SongRepository.findAll().size();

        // Create the Song
        SongDTO SongDTO = SongMapper.toDto(Song);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongMockMvc.perform(put("/api/Song")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(SongDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> SongList = SongRepository.findAll();
        assertThat(SongList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSong() throws Exception {
        // Initialize the database
        SongRepository.saveAndFlush(Song);

        int databaseSizeBeforeDelete = SongRepository.findAll().size();

        // Delete the Song
        restSongMockMvc.perform(delete("/api/Song/{id}", Song.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Song> SongList = SongRepository.findAll();
        assertThat(SongList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Song.class);
        Song Song1 = new Song();
        Song1.setId(1L);
        Song Song2 = new Song();
        Song2.setId(Song1.getId());
        assertThat(Song1).isEqualTo(Song2);
        Song2.setId(2L);
        assertThat(Song1).isNotEqualTo(Song2);
        Song1.setId(null);
        assertThat(Song1).isNotEqualTo(Song2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SongDTO.class);
        SongDTO SongDTO1 = new SongDTO();
        SongDTO1.setId(1L);
        SongDTO SongDTO2 = new SongDTO();
        assertThat(SongDTO1).isNotEqualTo(SongDTO2);
        SongDTO2.setId(SongDTO1.getId());
        assertThat(SongDTO1).isEqualTo(SongDTO2);
        SongDTO2.setId(2L);
        assertThat(SongDTO1).isNotEqualTo(SongDTO2);
        SongDTO1.setId(null);
        assertThat(SongDTO1).isNotEqualTo(SongDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(SongMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(SongMapper.fromId(null)).isNull();
    }
}
