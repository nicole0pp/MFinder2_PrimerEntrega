package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MFinder2App;
import com.mycompany.myapp.domain.FavoriteList;
import com.mycompany.myapp.repository.FavoriteListRepository;
import com.mycompany.myapp.service.FavoriteListService;
import com.mycompany.myapp.service.dto.FavoriteListDTO;
import com.mycompany.myapp.service.mapper.FavoriteListMapper;
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
 * Integration tests for the {@Link FavoriteListResource} REST controller.
 */
@SpringBootTest(classes = MFinder2App.class)
public class FavoriteListResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    @Autowired
    private FavoriteListRepository FavoriteListRepository;

    @Autowired
    private FavoriteListMapper FavoriteListMapper;

    @Autowired
    private FavoriteListService FavoriteListService;

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

    private MockMvc restFavoriteListMockMvc;

    private FavoriteList FavoriteList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FavoriteListResource FavoriteListResource = new FavoriteListResource(FavoriteListService);
        this.restFavoriteListMockMvc = MockMvcBuilders.standaloneSetup(FavoriteListResource)
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
    public static FavoriteList createEntity(EntityManager em) {
        FavoriteList FavoriteList = new FavoriteList()
            .name(DEFAULT_NAME)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
        return FavoriteList;
    }

    @BeforeEach
    public void initTest() {
        FavoriteList = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavoriteList() throws Exception {
        int databaseSizeBeforeCreate = FavoriteListRepository.findAll().size();

        // Create the FavoriteList
        FavoriteListDTO FavoriteListDTO = FavoriteListMapper.toDto(FavoriteList);
        restFavoriteListMockMvc.perform(post("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(FavoriteListDTO)))
            .andExpect(status().isCreated());

        // Validate the FavoriteList in the database
        List<FavoriteList> FavoriteListList = FavoriteListRepository.findAll();
        assertThat(FavoriteListList).hasSize(databaseSizeBeforeCreate + 1);
        FavoriteList testFavoriteList = FavoriteListList.get(FavoriteListList.size() - 1);
        assertThat(testFavoriteList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFavoriteList.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testFavoriteList.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createFavoriteListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = FavoriteListRepository.findAll().size();

        // Create the FavoriteList with an existing ID
        FavoriteList.setId(1L);
        FavoriteListDTO FavoriteListDTO = FavoriteListMapper.toDto(FavoriteList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoriteListMockMvc.perform(post("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(FavoriteListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavoriteList in the database
        List<FavoriteList> FavoriteListList = FavoriteListRepository.findAll();
        assertThat(FavoriteListList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = FavoriteListRepository.findAll().size();
        // set the field null
        FavoriteList.setName(null);

        // Create the FavoriteList, which fails.
        FavoriteListDTO FavoriteListDTO = FavoriteListMapper.toDto(FavoriteList);

        restFavoriteListMockMvc.perform(post("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(FavoriteListDTO)))
            .andExpect(status().isBadRequest());

        List<FavoriteList> FavoriteListList = FavoriteListRepository.findAll();
        assertThat(FavoriteListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFavoriteList() throws Exception {
        // Initialize the database
        FavoriteListRepository.saveAndFlush(FavoriteList);

        // Get all the FavoriteListList
        restFavoriteListMockMvc.perform(get("/api/reproduction-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(FavoriteList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }
    
    @Test
    @Transactional
    public void getFavoriteList() throws Exception {
        // Initialize the database
        FavoriteListRepository.saveAndFlush(FavoriteList);

        // Get the FavoriteList
        restFavoriteListMockMvc.perform(get("/api/reproduction-lists/{id}", FavoriteList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(FavoriteList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    public void getNonExistingFavoriteList() throws Exception {
        // Get the FavoriteList
        restFavoriteListMockMvc.perform(get("/api/reproduction-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavoriteList() throws Exception {
        // Initialize the database
        FavoriteListRepository.saveAndFlush(FavoriteList);

        int databaseSizeBeforeUpdate = FavoriteListRepository.findAll().size();

        // Update the FavoriteList
        FavoriteList updatedFavoriteList = FavoriteListRepository.findById(FavoriteList.getId()).get();
        // Disconnect from session so that the updates on updatedFavoriteList are not directly saved in db
        em.detach(updatedFavoriteList);
        updatedFavoriteList
            .name(UPDATED_NAME)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);
        FavoriteListDTO FavoriteListDTO = FavoriteListMapper.toDto(updatedFavoriteList);

        restFavoriteListMockMvc.perform(put("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(FavoriteListDTO)))
            .andExpect(status().isOk());

        // Validate the FavoriteList in the database
        List<FavoriteList> FavoriteListList = FavoriteListRepository.findAll();
        assertThat(FavoriteListList).hasSize(databaseSizeBeforeUpdate);
        FavoriteList testFavoriteList = FavoriteListList.get(FavoriteListList.size() - 1);
        assertThat(testFavoriteList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFavoriteList.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFavoriteList.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFavoriteList() throws Exception {
        int databaseSizeBeforeUpdate = FavoriteListRepository.findAll().size();

        // Create the FavoriteList
        FavoriteListDTO FavoriteListDTO = FavoriteListMapper.toDto(FavoriteList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriteListMockMvc.perform(put("/api/reproduction-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(FavoriteListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavoriteList in the database
        List<FavoriteList> FavoriteListList = FavoriteListRepository.findAll();
        assertThat(FavoriteListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFavoriteList() throws Exception {
        // Initialize the database
        FavoriteListRepository.saveAndFlush(FavoriteList);

        int databaseSizeBeforeDelete = FavoriteListRepository.findAll().size();

        // Delete the FavoriteList
        restFavoriteListMockMvc.perform(delete("/api/reproduction-lists/{id}", FavoriteList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<FavoriteList> FavoriteListList = FavoriteListRepository.findAll();
        assertThat(FavoriteListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteList.class);
        FavoriteList FavoriteList1 = new FavoriteList();
        FavoriteList1.setId(1L);
        FavoriteList FavoriteList2 = new FavoriteList();
        FavoriteList2.setId(FavoriteList1.getId());
        assertThat(FavoriteList1).isEqualTo(FavoriteList2);
        FavoriteList2.setId(2L);
        assertThat(FavoriteList1).isNotEqualTo(FavoriteList2);
        FavoriteList1.setId(null);
        assertThat(FavoriteList1).isNotEqualTo(FavoriteList2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteListDTO.class);
        FavoriteListDTO FavoriteListDTO1 = new FavoriteListDTO();
        FavoriteListDTO1.setId(1L);
        FavoriteListDTO FavoriteListDTO2 = new FavoriteListDTO();
        assertThat(FavoriteListDTO1).isNotEqualTo(FavoriteListDTO2);
        FavoriteListDTO2.setId(FavoriteListDTO1.getId());
        assertThat(FavoriteListDTO1).isEqualTo(FavoriteListDTO2);
        FavoriteListDTO2.setId(2L);
        assertThat(FavoriteListDTO1).isNotEqualTo(FavoriteListDTO2);
        FavoriteListDTO1.setId(null);
        assertThat(FavoriteListDTO1).isNotEqualTo(FavoriteListDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(FavoriteListMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(FavoriteListMapper.fromId(null)).isNull();
    }
}
