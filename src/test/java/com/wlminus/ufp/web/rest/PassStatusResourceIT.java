package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.AbstractCassandraTest;
import com.wlminus.ufp.RegisterApp;
import com.wlminus.ufp.domain.PassStatus;
import com.wlminus.ufp.repository.PassStatusRepository;
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
 * Integration tests for the {@link PassStatusResource} REST controller.
 */
@SpringBootTest(classes = RegisterApp.class)
public class PassStatusResourceIT extends AbstractCassandraTest {

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_PASS = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_PASS = "BBBBBBBBBB";

    @Autowired
    private PassStatusRepository passStatusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPassStatusMockMvc;

    private PassStatus passStatus;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PassStatus createEntity() {
        PassStatus passStatus = new PassStatus()
            .studentId(DEFAULT_STUDENT_ID)
            .coursePass(DEFAULT_COURSE_PASS);
        return passStatus;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PassStatus createUpdatedEntity() {
        PassStatus passStatus = new PassStatus()
            .studentId(UPDATED_STUDENT_ID)
            .coursePass(UPDATED_COURSE_PASS);
        return passStatus;
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PassStatusResource passStatusResource = new PassStatusResource(passStatusRepository);
        this.restPassStatusMockMvc = MockMvcBuilders.standaloneSetup(passStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @BeforeEach
    public void initTest() {
        passStatusRepository.deleteAll();
        passStatus = createEntity();
    }

    @Test
    public void createPassStatus() throws Exception {
        int databaseSizeBeforeCreate = passStatusRepository.findAll().size();

        // Create the PassStatus
        restPassStatusMockMvc.perform(post("/api/pass-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passStatus)))
            .andExpect(status().isCreated());

        // Validate the PassStatus in the database
        List<PassStatus> passStatusList = passStatusRepository.findAll();
        assertThat(passStatusList).hasSize(databaseSizeBeforeCreate + 1);
        PassStatus testPassStatus = passStatusList.get(passStatusList.size() - 1);
        assertThat(testPassStatus.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testPassStatus.getCoursePass()).isEqualTo(DEFAULT_COURSE_PASS);
    }

    @Test
    public void createPassStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passStatusRepository.findAll().size();

        // Create the PassStatus with an existing ID
        passStatus.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassStatusMockMvc.perform(post("/api/pass-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passStatus)))
            .andExpect(status().isBadRequest());

        // Validate the PassStatus in the database
        List<PassStatus> passStatusList = passStatusRepository.findAll();
        assertThat(passStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllPassStatuses() throws Exception {
        // Initialize the database
        passStatus.setId(UUID.randomUUID());
        passStatusRepository.save(passStatus);

        // Get all the passStatusList
        restPassStatusMockMvc.perform(get("/api/pass-statuses"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passStatus.getId().toString())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].coursePass").value(hasItem(DEFAULT_COURSE_PASS)));
    }

    @Test
    public void getPassStatus() throws Exception {
        // Initialize the database
        passStatus.setId(UUID.randomUUID());
        passStatusRepository.save(passStatus);

        // Get the passStatus
        restPassStatusMockMvc.perform(get("/api/pass-statuses/{id}", passStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(passStatus.getId().toString()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID))
            .andExpect(jsonPath("$.coursePass").value(DEFAULT_COURSE_PASS));
    }

    @Test
    public void getNonExistingPassStatus() throws Exception {
        // Get the passStatus
        restPassStatusMockMvc.perform(get("/api/pass-statuses/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePassStatus() throws Exception {
        // Initialize the database
        passStatus.setId(UUID.randomUUID());
        passStatusRepository.save(passStatus);

        int databaseSizeBeforeUpdate = passStatusRepository.findAll().size();

        // Update the passStatus
        PassStatus updatedPassStatus = passStatusRepository.findById(passStatus.getId()).get();
        updatedPassStatus
            .studentId(UPDATED_STUDENT_ID)
            .coursePass(UPDATED_COURSE_PASS);

        restPassStatusMockMvc.perform(put("/api/pass-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPassStatus)))
            .andExpect(status().isOk());

        // Validate the PassStatus in the database
        List<PassStatus> passStatusList = passStatusRepository.findAll();
        assertThat(passStatusList).hasSize(databaseSizeBeforeUpdate);
        PassStatus testPassStatus = passStatusList.get(passStatusList.size() - 1);
        assertThat(testPassStatus.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testPassStatus.getCoursePass()).isEqualTo(UPDATED_COURSE_PASS);
    }

    @Test
    public void updateNonExistingPassStatus() throws Exception {
        int databaseSizeBeforeUpdate = passStatusRepository.findAll().size();

        // Create the PassStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassStatusMockMvc.perform(put("/api/pass-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passStatus)))
            .andExpect(status().isBadRequest());

        // Validate the PassStatus in the database
        List<PassStatus> passStatusList = passStatusRepository.findAll();
        assertThat(passStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePassStatus() throws Exception {
        // Initialize the database
        passStatus.setId(UUID.randomUUID());
        passStatusRepository.save(passStatus);

        int databaseSizeBeforeDelete = passStatusRepository.findAll().size();

        // Delete the passStatus
        restPassStatusMockMvc.perform(delete("/api/pass-statuses/{id}", passStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PassStatus> passStatusList = passStatusRepository.findAll();
        assertThat(passStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
