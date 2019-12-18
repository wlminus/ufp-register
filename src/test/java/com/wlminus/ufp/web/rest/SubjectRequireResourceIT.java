package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.AbstractCassandraTest;
import com.wlminus.ufp.RegisterApp;
import com.wlminus.ufp.domain.SubjectRequire;
import com.wlminus.ufp.repository.SubjectRequireRepository;
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
 * Integration tests for the {@link SubjectRequireResource} REST controller.
 */
@SpringBootTest(classes = RegisterApp.class)
public class SubjectRequireResourceIT extends AbstractCassandraTest {

    private static final String DEFAULT_SUBJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIRE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRE_CODE = "BBBBBBBBBB";

    @Autowired
    private SubjectRequireRepository subjectRequireRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSubjectRequireMockMvc;

    private SubjectRequire subjectRequire;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectRequire createEntity() {
        SubjectRequire subjectRequire = new SubjectRequire()
            .subjectCode(DEFAULT_SUBJECT_CODE)
            .requireCode(DEFAULT_REQUIRE_CODE);
        return subjectRequire;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectRequire createUpdatedEntity() {
        SubjectRequire subjectRequire = new SubjectRequire()
            .subjectCode(UPDATED_SUBJECT_CODE)
            .requireCode(UPDATED_REQUIRE_CODE);
        return subjectRequire;
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubjectRequireResource subjectRequireResource = new SubjectRequireResource(subjectRequireRepository);
        this.restSubjectRequireMockMvc = MockMvcBuilders.standaloneSetup(subjectRequireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @BeforeEach
    public void initTest() {
        subjectRequireRepository.deleteAll();
        subjectRequire = createEntity();
    }

    @Test
    public void createSubjectRequire() throws Exception {
        int databaseSizeBeforeCreate = subjectRequireRepository.findAll().size();

        // Create the SubjectRequire
        restSubjectRequireMockMvc.perform(post("/api/subject-requires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subjectRequire)))
            .andExpect(status().isCreated());

        // Validate the SubjectRequire in the database
        List<SubjectRequire> subjectRequireList = subjectRequireRepository.findAll();
        assertThat(subjectRequireList).hasSize(databaseSizeBeforeCreate + 1);
        SubjectRequire testSubjectRequire = subjectRequireList.get(subjectRequireList.size() - 1);
        assertThat(testSubjectRequire.getSubjectCode()).isEqualTo(DEFAULT_SUBJECT_CODE);
        assertThat(testSubjectRequire.getRequireCode()).isEqualTo(DEFAULT_REQUIRE_CODE);
    }

    @Test
    public void createSubjectRequireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subjectRequireRepository.findAll().size();

        // Create the SubjectRequire with an existing ID
        subjectRequire.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubjectRequireMockMvc.perform(post("/api/subject-requires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subjectRequire)))
            .andExpect(status().isBadRequest());

        // Validate the SubjectRequire in the database
        List<SubjectRequire> subjectRequireList = subjectRequireRepository.findAll();
        assertThat(subjectRequireList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllSubjectRequires() throws Exception {
        // Initialize the database
        subjectRequire.setId(UUID.randomUUID());
        subjectRequireRepository.save(subjectRequire);

        // Get all the subjectRequireList
        restSubjectRequireMockMvc.perform(get("/api/subject-requires"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subjectRequire.getId().toString())))
            .andExpect(jsonPath("$.[*].subjectCode").value(hasItem(DEFAULT_SUBJECT_CODE)))
            .andExpect(jsonPath("$.[*].requireCode").value(hasItem(DEFAULT_REQUIRE_CODE)));
    }

    @Test
    public void getSubjectRequire() throws Exception {
        // Initialize the database
        subjectRequire.setId(UUID.randomUUID());
        subjectRequireRepository.save(subjectRequire);

        // Get the subjectRequire
        restSubjectRequireMockMvc.perform(get("/api/subject-requires/{id}", subjectRequire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subjectRequire.getId().toString()))
            .andExpect(jsonPath("$.subjectCode").value(DEFAULT_SUBJECT_CODE))
            .andExpect(jsonPath("$.requireCode").value(DEFAULT_REQUIRE_CODE));
    }

    @Test
    public void getNonExistingSubjectRequire() throws Exception {
        // Get the subjectRequire
        restSubjectRequireMockMvc.perform(get("/api/subject-requires/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSubjectRequire() throws Exception {
        // Initialize the database
        subjectRequire.setId(UUID.randomUUID());
        subjectRequireRepository.save(subjectRequire);

        int databaseSizeBeforeUpdate = subjectRequireRepository.findAll().size();

        // Update the subjectRequire
        SubjectRequire updatedSubjectRequire = subjectRequireRepository.findById(subjectRequire.getId()).get();
        updatedSubjectRequire
            .subjectCode(UPDATED_SUBJECT_CODE)
            .requireCode(UPDATED_REQUIRE_CODE);

        restSubjectRequireMockMvc.perform(put("/api/subject-requires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubjectRequire)))
            .andExpect(status().isOk());

        // Validate the SubjectRequire in the database
        List<SubjectRequire> subjectRequireList = subjectRequireRepository.findAll();
        assertThat(subjectRequireList).hasSize(databaseSizeBeforeUpdate);
        SubjectRequire testSubjectRequire = subjectRequireList.get(subjectRequireList.size() - 1);
        assertThat(testSubjectRequire.getSubjectCode()).isEqualTo(UPDATED_SUBJECT_CODE);
        assertThat(testSubjectRequire.getRequireCode()).isEqualTo(UPDATED_REQUIRE_CODE);
    }

    @Test
    public void updateNonExistingSubjectRequire() throws Exception {
        int databaseSizeBeforeUpdate = subjectRequireRepository.findAll().size();

        // Create the SubjectRequire

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectRequireMockMvc.perform(put("/api/subject-requires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subjectRequire)))
            .andExpect(status().isBadRequest());

        // Validate the SubjectRequire in the database
        List<SubjectRequire> subjectRequireList = subjectRequireRepository.findAll();
        assertThat(subjectRequireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSubjectRequire() throws Exception {
        // Initialize the database
        subjectRequire.setId(UUID.randomUUID());
        subjectRequireRepository.save(subjectRequire);

        int databaseSizeBeforeDelete = subjectRequireRepository.findAll().size();

        // Delete the subjectRequire
        restSubjectRequireMockMvc.perform(delete("/api/subject-requires/{id}", subjectRequire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubjectRequire> subjectRequireList = subjectRequireRepository.findAll();
        assertThat(subjectRequireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
