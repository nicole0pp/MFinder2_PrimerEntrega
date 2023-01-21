package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.Songs;
import com.mycompany.myapp.repository.SongsRepository;
import com.mycompany.myapp.service.SongsService;
import com.mycompany.myapp.service.dto.SongsDTO;
import com.mycompany.myapp.service.mapper.SongsMapper;
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
 * Integration tests for the {@Link SongsResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class SongsResourceIT {

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
    private SongsRepository songsRepository;

    @Autowired
    private SongsMapper songsMapper;

    @Autowired
    private SongsService songsService;

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

    private MockMvc restSongsMockMvc;

    private Songs songs;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SongsResource songsResource = new SongsResource(songsService);
        this.restSongsMockMvc = MockMvcBuilders.standaloneSetup(songsResource)
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
    public static Songs createEntity(EntityManager em) {
        Songs songs = new Songs()
            .name(DEFAULT_NAME)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .duration(DEFAULT_DURATION)
            .audio(DEFAULT_AUDIO)
            .audioContentType(DEFAULT_AUDIO_CONTENT_TYPE)
            .artists(DEFAULT_ARTISTS);
        return songs;
    }

    @BeforeEach
    public void initTest() {
        songs = createEntity(em);
    }

    @Test
    @Transactional
    public void createSongs() throws Exception {
        int databaseSizeBeforeCreate = songsRepository.findAll().size();

        // Create the Songs
        SongsDTO songsDTO = songsMapper.toDto(songs);
        restSongsMockMvc.perform(post("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isCreated());

        // Validate the Songs in the database
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeCreate + 1);
        Songs testSongs = songsList.get(songsList.size() - 1);
        assertThat(testSongs.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSongs.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testSongs.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testSongs.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSongs.getAudio()).isEqualTo(DEFAULT_AUDIO);
        assertThat(testSongs.getAudioContentType()).isEqualTo(DEFAULT_AUDIO_CONTENT_TYPE);
        assertThat(testSongs.getArtists()).isEqualTo(DEFAULT_ARTISTS);
    }

    @Test
    @Transactional
    public void createSongsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = songsRepository.findAll().size();

        // Create the Songs with an existing ID
        songs.setId(1L);
        SongsDTO songsDTO = songsMapper.toDto(songs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSongsMockMvc.perform(post("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Songs in the database
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = songsRepository.findAll().size();
        // set the field null
        songs.setName(null);

        // Create the Songs, which fails.
        SongsDTO songsDTO = songsMapper.toDto(songs);

        restSongsMockMvc.perform(post("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isBadRequest());

        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList
        restSongsMockMvc.perform(get("/api/songs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(songs.getId().intValue())))
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
    public void getSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get the songs
        restSongsMockMvc.perform(get("/api/songs/{id}", songs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(songs.getId().intValue()))
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
    public void getNonExistingSongs() throws Exception {
        // Get the songs
        restSongsMockMvc.perform(get("/api/songs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        int databaseSizeBeforeUpdate = songsRepository.findAll().size();

        // Update the songs
        Songs updatedSongs = songsRepository.findById(songs.getId()).get();
        // Disconnect from session so that the updates on updatedSongs are not directly saved in db
        em.detach(updatedSongs);
        updatedSongs
            .name(UPDATED_NAME)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .duration(UPDATED_DURATION)
            .audio(UPDATED_AUDIO)
            .audioContentType(UPDATED_AUDIO_CONTENT_TYPE)
            .artists(UPDATED_ARTISTS);
        SongsDTO songsDTO = songsMapper.toDto(updatedSongs);

        restSongsMockMvc.perform(put("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isOk());

        // Validate the Songs in the database
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeUpdate);
        Songs testSongs = songsList.get(songsList.size() - 1);
        assertThat(testSongs.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSongs.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testSongs.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testSongs.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSongs.getAudio()).isEqualTo(UPDATED_AUDIO);
        assertThat(testSongs.getAudioContentType()).isEqualTo(UPDATED_AUDIO_CONTENT_TYPE);
        assertThat(testSongs.getArtists()).isEqualTo(UPDATED_ARTISTS);
    }

    @Test
    @Transactional
    public void updateNonExistingSongs() throws Exception {
        int databaseSizeBeforeUpdate = songsRepository.findAll().size();

        // Create the Songs
        SongsDTO songsDTO = songsMapper.toDto(songs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongsMockMvc.perform(put("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Songs in the database
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        int databaseSizeBeforeDelete = songsRepository.findAll().size();

        // Delete the songs
        restSongsMockMvc.perform(delete("/api/songs/{id}", songs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Songs.class);
        Songs songs1 = new Songs();
        songs1.setId(1L);
        Songs songs2 = new Songs();
        songs2.setId(songs1.getId());
        assertThat(songs1).isEqualTo(songs2);
        songs2.setId(2L);
        assertThat(songs1).isNotEqualTo(songs2);
        songs1.setId(null);
        assertThat(songs1).isNotEqualTo(songs2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SongsDTO.class);
        SongsDTO songsDTO1 = new SongsDTO();
        songsDTO1.setId(1L);
        SongsDTO songsDTO2 = new SongsDTO();
        assertThat(songsDTO1).isNotEqualTo(songsDTO2);
        songsDTO2.setId(songsDTO1.getId());
        assertThat(songsDTO1).isEqualTo(songsDTO2);
        songsDTO2.setId(2L);
        assertThat(songsDTO1).isNotEqualTo(songsDTO2);
        songsDTO1.setId(null);
        assertThat(songsDTO1).isNotEqualTo(songsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(songsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(songsMapper.fromId(null)).isNull();
    }
}
