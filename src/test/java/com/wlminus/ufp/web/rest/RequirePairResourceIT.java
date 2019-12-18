package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.AbstractCassandraTest;
import com.wlminus.ufp.RegisterApp;
import com.wlminus.ufp.domain.RequirePair;
import com.wlminus.ufp.repository.RequirePairRepository;
import com.wlminus.ufp.web.rest.errors.ExceptionTranslator;
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
import org.springframework.validation.Validator;

import java.util.List;
import java.util.UUID;

import static com.wlminus.ufp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RequirePairResource} REST controller.
 */
@SpringBootTest(classes = RegisterApp.class)
public class RequirePairResourceIT extends AbstractCassandraTest {

    private static final String DEFAULT_SUBJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIRE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRE_CODE = "BBBBBBBBBB";

    @Autowired
    private RequirePairRepository requirePairRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restRequirePairMockMvc;

    private RequirePair requirePair;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequirePair createEntity() {
        RequirePair requirePair = new RequirePair()
            .subjectCode(DEFAULT_SUBJECT_CODE)
            .requireCode(DEFAULT_REQUIRE_CODE);
        return requirePair;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequirePair createUpdatedEntity() {
        RequirePair requirePair = new RequirePair()
            .subjectCode(UPDATED_SUBJECT_CODE)
            .requireCode(UPDATED_REQUIRE_CODE);
        return requirePair;
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequirePairResource requirePairResource = new RequirePairResource(requirePairRepository);
        this.restRequirePairMockMvc = MockMvcBuilders.standaloneSetup(requirePairResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @BeforeEach
    public void initTest() {
        requirePairRepository.deleteAll();
        requirePair = createEntity();
    }

    @Test
    public void createRequirePair() throws Exception {
        int databaseSizeBeforeCreate = requirePairRepository.findAll().size();

        // Create the RequirePair
        restRequirePairMockMvc.perform(post("/api/require-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirePair)))
            .andExpect(status().isCreated());

        // Validate the RequirePair in the database
        List<RequirePair> requirePairList = requirePairRepository.findAll();
        assertThat(requirePairList).hasSize(databaseSizeBeforeCreate + 1);
        RequirePair testRequirePair = requirePairList.get(requirePairList.size() - 1);
        assertThat(testRequirePair.getSubjectCode()).isEqualTo(DEFAULT_SUBJECT_CODE);
        assertThat(testRequirePair.getRequireCode()).isEqualTo(DEFAULT_REQUIRE_CODE);
    }

    @Test
    public void createRequirePairWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requirePairRepository.findAll().size();

        // Create the RequirePair with an existing ID
        requirePair.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequirePairMockMvc.perform(post("/api/require-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirePair)))
            .andExpect(status().isBadRequest());

        // Validate the RequirePair in the database
        List<RequirePair> requirePairList = requirePairRepository.findAll();
        assertThat(requirePairList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllRequirePairs() throws Exception {
        // Initialize the database
        requirePair.setId(UUID.randomUUID());
        requirePairRepository.save(requirePair);

        // Get all the requirePairList
        restRequirePairMockMvc.perform(get("/api/require-pairs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requirePair.getId().toString())))
            .andExpect(jsonPath("$.[*].subjectCode").value(hasItem(DEFAULT_SUBJECT_CODE)))
            .andExpect(jsonPath("$.[*].requireCode").value(hasItem(DEFAULT_REQUIRE_CODE)));
    }

    @Test
    public void getRequirePair() throws Exception {
        // Initialize the database
        requirePair.setId(UUID.randomUUID());
        requirePairRepository.save(requirePair);

        // Get the requirePair
        restRequirePairMockMvc.perform(get("/api/require-pairs/{id}", requirePair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requirePair.getId().toString()))
            .andExpect(jsonPath("$.subjectCode").value(DEFAULT_SUBJECT_CODE))
            .andExpect(jsonPath("$.requireCode").value(DEFAULT_REQUIRE_CODE));
    }

    @Test
    public void getNonExistingRequirePair() throws Exception {
        // Get the requirePair
        restRequirePairMockMvc.perform(get("/api/require-pairs/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRequirePair() throws Exception {
        // Initialize the database
        requirePair.setId(UUID.randomUUID());
        requirePairRepository.save(requirePair);

        int databaseSizeBeforeUpdate = requirePairRepository.findAll().size();

        // Update the requirePair
        RequirePair updatedRequirePair = requirePairRepository.findById(requirePair.getId()).get();
        updatedRequirePair
            .subjectCode(UPDATED_SUBJECT_CODE)
            .requireCode(UPDATED_REQUIRE_CODE);

        restRequirePairMockMvc.perform(put("/api/require-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequirePair)))
            .andExpect(status().isOk());

        // Validate the RequirePair in the database
        List<RequirePair> requirePairList = requirePairRepository.findAll();
        assertThat(requirePairList).hasSize(databaseSizeBeforeUpdate);
        RequirePair testRequirePair = requirePairList.get(requirePairList.size() - 1);
        assertThat(testRequirePair.getSubjectCode()).isEqualTo(UPDATED_SUBJECT_CODE);
        assertThat(testRequirePair.getRequireCode()).isEqualTo(UPDATED_REQUIRE_CODE);
    }

    @Test
    public void updateNonExistingRequirePair() throws Exception {
        int databaseSizeBeforeUpdate = requirePairRepository.findAll().size();

        // Create the RequirePair

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequirePairMockMvc.perform(put("/api/require-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirePair)))
            .andExpect(status().isBadRequest());

        // Validate the RequirePair in the database
        List<RequirePair> requirePairList = requirePairRepository.findAll();
        assertThat(requirePairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteRequirePair() throws Exception {
        // Initialize the database
        requirePair.setId(UUID.randomUUID());
        requirePairRepository.save(requirePair);

        int databaseSizeBeforeDelete = requirePairRepository.findAll().size();

        // Delete the requirePair
        restRequirePairMockMvc.perform(delete("/api/require-pairs/{id}", requirePair.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequirePair> requirePairList = requirePairRepository.findAll();
        assertThat(requirePairList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
