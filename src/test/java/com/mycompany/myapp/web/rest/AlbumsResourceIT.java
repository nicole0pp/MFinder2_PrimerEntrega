package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.Albums;
import com.mycompany.myapp.repository.AlbumsRepository;
import com.mycompany.myapp.service.AlbumsService;
import com.mycompany.myapp.service.dto.AlbumsDTO;
import com.mycompany.myapp.service.mapper.AlbumsMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link AlbumsResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class AlbumsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private AlbumsRepository albumsRepository;

    @Autowired
    private AlbumsMapper albumsMapper;

    @Autowired
    private AlbumsService albumsService;

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

    private MockMvc restAlbumsMockMvc;

    private Albums albums;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlbumsResource albumsResource = new AlbumsResource(albumsService);
        this.restAlbumsMockMvc = MockMvcBuilders.standaloneSetup(albumsResource)
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
    public static Albums createEntity(EntityManager em) {
        Albums albums = new Albums()
            .name(DEFAULT_NAME)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .publicationDate(DEFAULT_PUBLICATION_DATE);
        return albums;
    }

    @BeforeEach
    public void initTest() {
        albums = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlbums() throws Exception {
        int databaseSizeBeforeCreate = albumsRepository.findAll().size();

        // Create the Albums
        AlbumsDTO albumsDTO = albumsMapper.toDto(albums);
        restAlbumsMockMvc.perform(post("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumsDTO)))
            .andExpect(status().isCreated());

        // Validate the Albums in the database
        List<Albums> albumsList = albumsRepository.findAll();
        assertThat(albumsList).hasSize(databaseSizeBeforeCreate + 1);
        Albums testAlbums = albumsList.get(albumsList.size() - 1);
        assertThat(testAlbums.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlbums.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testAlbums.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testAlbums.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    public void createAlbumsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = albumsRepository.findAll().size();

        // Create the Albums with an existing ID
        albums.setId(1L);
        AlbumsDTO albumsDTO = albumsMapper.toDto(albums);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumsMockMvc.perform(post("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Albums in the database
        List<Albums> albumsList = albumsRepository.findAll();
        assertThat(albumsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumsRepository.findAll().size();
        // set the field null
        albums.setName(null);

        // Create the Albums, which fails.
        AlbumsDTO albumsDTO = albumsMapper.toDto(albums);

        restAlbumsMockMvc.perform(post("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumsDTO)))
            .andExpect(status().isBadRequest());

        List<Albums> albumsList = albumsRepository.findAll();
        assertThat(albumsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlbums() throws Exception {
        // Initialize the database
        albumsRepository.saveAndFlush(albums);

        // Get all the albumsList
        restAlbumsMockMvc.perform(get("/api/albums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(albums.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getAlbums() throws Exception {
        // Initialize the database
        albumsRepository.saveAndFlush(albums);

        // Get the albums
        restAlbumsMockMvc.perform(get("/api/albums/{id}", albums.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(albums.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAlbums() throws Exception {
        // Get the albums
        restAlbumsMockMvc.perform(get("/api/albums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlbums() throws Exception {
        // Initialize the database
        albumsRepository.saveAndFlush(albums);

        int databaseSizeBeforeUpdate = albumsRepository.findAll().size();

        // Update the albums
        Albums updatedAlbums = albumsRepository.findById(albums.getId()).get();
        // Disconnect from session so that the updates on updatedAlbums are not directly saved in db
        em.detach(updatedAlbums);
        updatedAlbums
            .name(UPDATED_NAME)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .publicationDate(UPDATED_PUBLICATION_DATE);
        AlbumsDTO albumsDTO = albumsMapper.toDto(updatedAlbums);

        restAlbumsMockMvc.perform(put("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumsDTO)))
            .andExpect(status().isOk());

        // Validate the Albums in the database
        List<Albums> albumsList = albumsRepository.findAll();
        assertThat(albumsList).hasSize(databaseSizeBeforeUpdate);
        Albums testAlbums = albumsList.get(albumsList.size() - 1);
        assertThat(testAlbums.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlbums.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testAlbums.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testAlbums.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAlbums() throws Exception {
        int databaseSizeBeforeUpdate = albumsRepository.findAll().size();

        // Create the Albums
        AlbumsDTO albumsDTO = albumsMapper.toDto(albums);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumsMockMvc.perform(put("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Albums in the database
        List<Albums> albumsList = albumsRepository.findAll();
        assertThat(albumsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlbums() throws Exception {
        // Initialize the database
        albumsRepository.saveAndFlush(albums);

        int databaseSizeBeforeDelete = albumsRepository.findAll().size();

        // Delete the albums
        restAlbumsMockMvc.perform(delete("/api/albums/{id}", albums.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Albums> albumsList = albumsRepository.findAll();
        assertThat(albumsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Albums.class);
        Albums albums1 = new Albums();
        albums1.setId(1L);
        Albums albums2 = new Albums();
        albums2.setId(albums1.getId());
        assertThat(albums1).isEqualTo(albums2);
        albums2.setId(2L);
        assertThat(albums1).isNotEqualTo(albums2);
        albums1.setId(null);
        assertThat(albums1).isNotEqualTo(albums2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlbumsDTO.class);
        AlbumsDTO albumsDTO1 = new AlbumsDTO();
        albumsDTO1.setId(1L);
        AlbumsDTO albumsDTO2 = new AlbumsDTO();
        assertThat(albumsDTO1).isNotEqualTo(albumsDTO2);
        albumsDTO2.setId(albumsDTO1.getId());
        assertThat(albumsDTO1).isEqualTo(albumsDTO2);
        albumsDTO2.setId(2L);
        assertThat(albumsDTO1).isNotEqualTo(albumsDTO2);
        albumsDTO1.setId(null);
        assertThat(albumsDTO1).isNotEqualTo(albumsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(albumsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(albumsMapper.fromId(null)).isNull();
    }
}
