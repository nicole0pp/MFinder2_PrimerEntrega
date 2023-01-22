package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.repository.AlbumRepository;
import com.mycompany.myapp.service.AlbumService;
import com.mycompany.myapp.service.dto.AlbumDTO;
import com.mycompany.myapp.service.mapper.AlbumMapper;
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
 * Integration tests for the {@Link AlbumResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class AlbumResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private AlbumRepository AlbumRepository;

    @Autowired
    private AlbumMapper AlbumMapper;

    @Autowired
    private AlbumService AlbumService;

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

    private MockMvc restAlbumMockMvc;

    private Album Album;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlbumResource AlbumResource = new AlbumResource(AlbumService);
        this.restAlbumMockMvc = MockMvcBuilders.standaloneSetup(AlbumResource)
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
    public static Album createEntity(EntityManager em) {
        Album Album = new Album()
            .name(DEFAULT_NAME)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .publicationDate(DEFAULT_PUBLICATION_DATE);
        return Album;
    }

    @BeforeEach
    public void initTest() {
        Album = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlbum() throws Exception {
        int databaseSizeBeforeCreate = AlbumRepository.findAll().size();

        // Create the Album
        AlbumDTO AlbumDTO = AlbumMapper.toDto(Album);
        restAlbumMockMvc.perform(post("/api/Album")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(AlbumDTO)))
            .andExpect(status().isCreated());

        // Validate the Album in the database
        List<Album> AlbumList = AlbumRepository.findAll();
        assertThat(AlbumList).hasSize(databaseSizeBeforeCreate + 1);
        Album testAlbum = AlbumList.get(AlbumList.size() - 1);
        assertThat(testAlbum.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlbum.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testAlbum.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testAlbum.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    public void createAlbumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = AlbumRepository.findAll().size();

        // Create the Album with an existing ID
        Album.setId(1L);
        AlbumDTO AlbumDTO = AlbumMapper.toDto(Album);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumMockMvc.perform(post("/api/Album")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(AlbumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> AlbumList = AlbumRepository.findAll();
        assertThat(AlbumList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = AlbumRepository.findAll().size();
        // set the field null
        Album.setName(null);

        // Create the Album, which fails.
        AlbumDTO AlbumDTO = AlbumMapper.toDto(Album);

        restAlbumMockMvc.perform(post("/api/Album")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(AlbumDTO)))
            .andExpect(status().isBadRequest());

        List<Album> AlbumList = AlbumRepository.findAll();
        assertThat(AlbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlbum() throws Exception {
        // Initialize the database
        AlbumRepository.saveAndFlush(Album);

        // Get all the AlbumList
        restAlbumMockMvc.perform(get("/api/Album?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(Album.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getAlbum() throws Exception {
        // Initialize the database
        AlbumRepository.saveAndFlush(Album);

        // Get the Album
        restAlbumMockMvc.perform(get("/api/Album/{id}", Album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(Album.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAlbum() throws Exception {
        // Get the Album
        restAlbumMockMvc.perform(get("/api/Album/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlbum() throws Exception {
        // Initialize the database
        AlbumRepository.saveAndFlush(Album);

        int databaseSizeBeforeUpdate = AlbumRepository.findAll().size();

        // Update the Album
        Album updatedAlbum = AlbumRepository.findById(Album.getId()).get();
        // Disconnect from session so that the updates on updatedAlbum are not directly saved in db
        em.detach(updatedAlbum);
        updatedAlbum
            .name(UPDATED_NAME)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .publicationDate(UPDATED_PUBLICATION_DATE);
        AlbumDTO AlbumDTO = AlbumMapper.toDto(updatedAlbum);

        restAlbumMockMvc.perform(put("/api/Album")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(AlbumDTO)))
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> AlbumList = AlbumRepository.findAll();
        assertThat(AlbumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = AlbumList.get(AlbumList.size() - 1);
        assertThat(testAlbum.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlbum.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testAlbum.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testAlbum.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAlbum() throws Exception {
        int databaseSizeBeforeUpdate = AlbumRepository.findAll().size();

        // Create the Album
        AlbumDTO AlbumDTO = AlbumMapper.toDto(Album);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc.perform(put("/api/Album")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(AlbumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> AlbumList = AlbumRepository.findAll();
        assertThat(AlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlbum() throws Exception {
        // Initialize the database
        AlbumRepository.saveAndFlush(Album);

        int databaseSizeBeforeDelete = AlbumRepository.findAll().size();

        // Delete the Album
        restAlbumMockMvc.perform(delete("/api/Album/{id}", Album.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Album> AlbumList = AlbumRepository.findAll();
        assertThat(AlbumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Album.class);
        Album Album1 = new Album();
        Album1.setId(1L);
        Album Album2 = new Album();
        Album2.setId(Album1.getId());
        assertThat(Album1).isEqualTo(Album2);
        Album2.setId(2L);
        assertThat(Album1).isNotEqualTo(Album2);
        Album1.setId(null);
        assertThat(Album1).isNotEqualTo(Album2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlbumDTO.class);
        AlbumDTO AlbumDTO1 = new AlbumDTO();
        AlbumDTO1.setId(1L);
        AlbumDTO AlbumDTO2 = new AlbumDTO();
        assertThat(AlbumDTO1).isNotEqualTo(AlbumDTO2);
        AlbumDTO2.setId(AlbumDTO1.getId());
        assertThat(AlbumDTO1).isEqualTo(AlbumDTO2);
        AlbumDTO2.setId(2L);
        assertThat(AlbumDTO1).isNotEqualTo(AlbumDTO2);
        AlbumDTO1.setId(null);
        assertThat(AlbumDTO1).isNotEqualTo(AlbumDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(AlbumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(AlbumMapper.fromId(null)).isNull();
    }
}
